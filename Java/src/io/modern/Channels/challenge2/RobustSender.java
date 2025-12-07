package Channels.challenge2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;


public class RobustSender {
    private static final long TIMEOUT_NS = 5_000_000_000L; // 5 sec
    private static final long PROGRESS_INTERVAL = 50_000;   // 50KB

    public static void send(SocketChannel ch, byte[] data) throws IOException {
        ByteBuffer buf = ByteBuffer.wrap(data);
        long start = System.nanoTime();
        long total = data.length;
        long lastReport = 0;

        while (buf.hasRemaining()) {
            // Check timeout
            if (System.nanoTime() - start > TIMEOUT_NS) {
                throw new IOException("Send timeout after " + 
                    (total - buf.remaining()) + " of " + total + " bytes");
            }

            int written = ch.write(buf);
            if (written == 0) {
                // Optional: use Selector to wait for writable
                Thread.yield(); 
                continue;
            }

            // Progress reporting
            long sent = total - buf.remaining();
            if (sent - lastReport >= PROGRESS_INTERVAL || sent == total) {
                double pct = 100.0 * sent / total;
                System.out.printf("📤 Sent %,d/%,d bytes (%.1f%%)%n", 
                    sent, total, pct);
                lastReport = sent;
            }
        }
    }

    // Test
    public static void main(String[] args) throws Exception {
        try (ServerSocketChannel server = ServerSocketChannel.open()) {
            server.bind(new InetSocketAddress(8081));
            
            // Start receiver
            new Thread(() -> {
                try (SocketChannel client = server.accept()) {
                    ByteBuffer buf = ByteBuffer.allocate(65536);
                    long total = 0;
                    while (client.read(buf) > 0) {
                        total += buf.position();
                        buf.clear();
                    }
                    System.out.println("✅ Received: " + total + " bytes");
                } catch (IOException e) { e.printStackTrace(); }
            }).start();

            // Sender
            Thread.sleep(100);
            try (SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8081))) {
                byte[] msg = new byte[200_000];
                for (int i = 0; i < msg.length; i++) msg[i] = (byte) ('A' + (i % 26));
                
                send(client, msg);
            }
        }
    }
}