package Channels.challenge2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.concurrent.CountDownLatch;

public class NIOBenchmark {

    private static final Path SRC_FILE = Paths.get("Channels/challenge2/zerocopysrc.txt");
    private static final Path DST_FILE = Paths.get("Channels/challenge2/zerocopydst.txt");
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
        // Ensure test file exists
        ensureTestFile();

        System.out.println("🚀 NIO Zero-Copy Benchmark");
        System.out.println("File size: " + Files.size(SRC_FILE) / (1024 * 1024) + " MB");
        System.out.println("=".repeat(60));

        // 1. File → File (transferTo)
        benchmark("File → File (transferTo)", NIOBenchmark::fileToFileZeroCopy);

        // 2. File → Socket (transferTo) — requires server/client handshake
        benchmark("File → Socket (transferTo)", NIOBenchmark::fileToSocketZeroCopy);

        // 3. Manual copy (buffered)
        benchmark("Manual Copy (buffered)", NIOBenchmark::manualCopy);

        System.out.println("\n✅ Benchmark complete.");
    }

    // ---------------------------
    // 1. File → File (Zero-Copy)
    // ---------------------------
    private static void fileToFileZeroCopy() throws IOException {
        try (FileChannel src = FileChannel.open(SRC_FILE, StandardOpenOption.READ);
                FileChannel dst = FileChannel.open(DST_FILE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {

            long size = src.size();
            long transferred = src.transferTo(0, size, dst);
            if (transferred != size) {
                throw new IOException("Incomplete transfer: " + transferred + " < " + size);
            }
        }
    }

    // -----------------------------
    // 2. File → Socket (Zero-Copy)
    // -----------------------------
    private static void fileToSocketZeroCopy() throws Exception {
        // Start server in background
        CountDownLatch serverReady = new CountDownLatch(1);
        CountDownLatch clientDone = new CountDownLatch(1);
        Thread serverThread = new Thread(() -> {
            try (ServerSocketChannel server = ServerSocketChannel.open()) {
                server.bind(new InetSocketAddress(SERVER_PORT));
                server.configureBlocking(true);
                serverReady.countDown();

                try (SocketChannel client = server.accept()) {
                    // Read request
                    ByteBuffer req = ByteBuffer.allocate(1024);
                    client.read(req);

                    // Send minimal HTTP header
                    String header = "HTTP/1.1 200 OK\r\nContent-Length: " +
                            Files.size(SRC_FILE) + "\r\nConnection: close\r\n\r\n";
                    client.write(ByteBuffer.wrap(header.getBytes()));

                    // ✅ ZERO-COPY: File → Socket
                    try (FileChannel file = FileChannel.open(SRC_FILE, StandardOpenOption.READ)) {
                        long size = file.size();
                        long transferred = file.transferTo(0, size, client);
                        if (transferred != size) {
                            System.err.println("⚠️ Partial transfer: " + transferred + " < " + size);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clientDone.countDown();
            }
        });
        serverThread.start();
        serverReady.await();

        // Client: fetch and discard
        try (SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", SERVER_PORT))) {
            // Send GET request
            String req = "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n";
            client.write(ByteBuffer.wrap(req.getBytes()));

            // Discard response (we only care about send speed)
            ByteBuffer buf = ByteBuffer.allocate(65536);
            while (client.read(buf) > 0) {
                buf.clear();
            }
        }

        clientDone.await();
        serverThread.join(1000);
    }

    // -----------------------
    // 3. Manual Copy (Buffered)
    // -----------------------
    private static void manualCopy() throws IOException {
        try (FileChannel src = FileChannel.open(SRC_FILE, StandardOpenOption.READ);
                FileChannel dst = FileChannel.open(DST_FILE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {

            ByteBuffer buf = ByteBuffer.allocateDirect(65536);
            long copied = 0;
            long size = src.size();

            while (copied < size) {
                buf.clear();
                int n = src.read(buf);
                if (n <= 0)
                    break;

                buf.flip();
                while (buf.hasRemaining()) {
                    copied += dst.write(buf);
                }
            }

            if (copied != size) {
                throw new IOException("Incomplete copy: " + copied + " < " + size);
            }
        }
    }

    // -----------------
    // Benchmark Harness
    // -----------------
    private static void benchmark(String name, IOOperation op) throws Exception {
        // Warmup
        for (int i = 0; i < 2; i++)
            op.run();

        // Measure
        long start = System.nanoTime();
        op.run();
        long timeNs = System.nanoTime() - start;

        double mbps = (Files.size(SRC_FILE) / (1024.0 * 1024)) / (timeNs / 1_000_000_000.0);
        System.out.printf("%-30s: %6.1f ms | %7.1f MB/s%n",
                name, timeNs / 1_000_000.0, mbps);
    }

    // --------------
    // Helper Methods
    // --------------
    private static void ensureTestFile() throws IOException {
        if (!Files.exists(SRC_FILE)) {
            System.out.println("🔧 Creating 100MB test file (test.dat)...");
            try (FileChannel ch = FileChannel.open(SRC_FILE,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                ByteBuffer buf = ByteBuffer.allocate(1024 * 1024); // 1MB buffer
                for (int i = 0; i < 100; i++) {
                    buf.clear();
                    buf.put(new byte[buf.capacity()]); // zero-filled
                    buf.flip();
                    ch.write(buf);
                }
            }
            System.out.println("✅ test.dat created.");
        }
    }

    @FunctionalInterface
    interface IOOperation {
        void run() throws Exception;
    }
}