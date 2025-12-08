package Channels.serversocketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A defensively coded non-blocking HTTP server using Java NIO.
 * 
 * 🔐 Security & Reliability Design Principles:
 * 
 * 1. 🛑 Slow-Client Attack Mitigation:
 * - Clients that send a request but never READ the response cause server-side
 * write stalls (due to full client kernel receive buffers).
 * - This server tracks last I/O activity per connection and closes idle/stalled
 * connections via scheduled timeout checks.
 * 
 * 2. ⏱️ Dual Timeouts:
 * - READ_TIMEOUT: If client doesn’t send full request in time → drop.
 * - WRITE_TIMEOUT: If client doesn’t consume response (i.e., stalls on read) →
 * drop.
 * → This defends against the "write-block" DoS we discussed.
 * 
 * 3. 🔄 Backpressure-Aware:
 * - Only generates response *after* request is fully read.
 * - Only writes next chunk if previous write completed (OP_WRITE readiness).
 * 
 * 4. 🧹 Resource Safety:
 * - All channels closed explicitly (even on error).
 * - Selector + ScheduledExecutorService shut down cleanly.
 * 
 * 🧪 Test with:
 * - Normal client: java ClientConnection
 * - Slow client: comment out read() → server auto-closes after 3s.
 */
public class RobustNIOServer {

    private static final int PORT = 9090;
    private static final int READ_TIMEOUT_MS = 3_000; // Client must send request within 3s
    private static final int WRITE_TIMEOUT_MS = 3_000; // Client must consume response within 3s

    // Track last I/O time per channel to enforce timeouts
    private final ConcurrentHashMap<SocketChannel, Long> lastActivity = new ConcurrentHashMap<>();
    private final ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) throws Exception {
        new RobustNIOServer().start();
    }

    public void start() throws IOException {
        try (
                Selector selector = Selector.open();
                ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("🚀 Server listening on localhost:" + PORT);
            System.out.println("   - READ_TIMEOUT: " + READ_TIMEOUT_MS + "ms");
            System.out.println("   - WRITE_TIMEOUT: " + WRITE_TIMEOUT_MS + "ms\n");

            // Start background task to check for idle connections
            timeoutChecker.scheduleAtFixedRate(this::checkTimeouts, 1, 1, TimeUnit.SECONDS);

            while (true) {
                int ready = selector.select(500); // Wake up every 500ms for responsiveness
                if (ready == 0)
                    continue;

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove(); // ⚠️ Critical: always remove after processing

                    try {
                        if (!key.isValid())
                            continue;

                        if (key.isAcceptable()) {
                            handleAccept(serverChannel, selector);
                        } else if (key.isReadable()) {
                            handleRead(key);
                        } else if (key.isWritable()) {
                            handleWrite(key);
                        }

                    } catch (IOException e) {
                        System.err.println("❌ I/O error on " + key.channel() + ": " + e.getMessage());
                        safeClose(key.channel());
                    }
                }
            }

        } finally {
            timeoutChecker.shutdownNow();
            System.out.println("🛑 Server stopped.");
        }
    }

    // ———————————————————————————————————————————————————————————————————————
    // ACCEPT: New client connects
    // ———————————————————————————————————————————————————————————————————————
    private void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {
        SocketChannel client = serverChannel.accept();
        if (client == null)
            return;

        client.configureBlocking(false);
        SelectionKey key = client.register(selector, SelectionKey.OP_READ);

        // Attach state: [request buffer, response buffer (null until ready)]
        key.attach(new ConnectionState());
        lastActivity.put(client, System.currentTimeMillis());

        System.out.println("📥 Accepted: " + client.getRemoteAddress());
    }

    // ———————————————————————————————————————————————————————————————————————
    // READ: Client sends request (e.g., HTTP GET)
    // ———————————————————————————————————————————————————————————————————————
    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ConnectionState state = (ConnectionState) key.attachment();

        // Read into request buffer
        int bytesRead = client.read(state.requestBuffer);
        lastActivity.put(client, System.currentTimeMillis());

        if (bytesRead == -1) {
            // Client closed connection
            System.out.println("CloseOperation: " + client.getRemoteAddress() + " (EOF on read)");
            safeClose(client);
            return;
        }

        // Check if we have a complete HTTP request (simplified: look for \r\n\r\n)
        state.requestBuffer.flip();
        if (isRequestComplete(state.requestBuffer)) {
            // 🎯 Request fully received → generate response
            generateResponse(state);
            state.requestBuffer.clear(); // reuse buffer

            // Switch to OP_WRITE to send response
            key.interestOps(SelectionKey.OP_WRITE);
        } else {
            // Need more data → compact and keep reading
            state.requestBuffer.compact();
            // Still interested in OP_READ
        }
    }

    // ———————————————————————————————————————————————————————————————————————
    // WRITE: Send response to client
    // ———————————————————————————————————————————————————————————————————————
    private void handleWrite(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ConnectionState state = (ConnectionState) key.attachment();

        // Write as much as possible
        int bytesWritten = client.write(state.responseBuffer);
        lastActivity.put(client, System.currentTimeMillis());

        if (bytesWritten > 0) {
            System.out.println("📤 Wrote " + bytesWritten + " bytes to " + client.getRemoteAddress());
        }

        // Is response fully sent?
        if (!state.responseBuffer.hasRemaining()) {
            // ✅ Done — close connection (since we use Connection: close)
            System.out.println("CloseOperation: " + client.getRemoteAddress() + " (response sent)");
            safeClose(client);
        } else {
            // Partial write → keep OP_WRITE interest
            // (Selector will wake us when socket buffer has space)
        }
    }

    // ———————————————————————————————————————————————————————————————————————
    // HELPERS
    // ———————————————————————————————————————————————————————————————————————

    private boolean isRequestComplete(ByteBuffer buffer) {
        // Simple HTTP request detection: look for "\r\n\r\n"
        // In production: use proper HTTP parser (e.g., Netty's)
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data); // copies, doesn't advance position
        buffer.rewind(); // reset for later read

        String request = new String(data, StandardCharsets.US_ASCII);
        return request.contains("\r\n\r\n");
    }

    private void generateResponse(ConnectionState state) {
        // 🛡️ Backpressure: only generate response *after* full request is read
        String responseBody = "<h1>Hello from RobustNIOServer!</h1>\n" +
                "<p>Time: " + java.time.Instant.now() + "</p>\n" +
                "<p>This server defends against slow clients.</p>";

        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=utf-8\r\n" +
                "Content-Length: " + responseBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "Connection: close\r\n" + // 👈 Ensures clean close
                "\r\n" +
                responseBody;

        state.responseBuffer = ByteBuffer.wrap(httpResponse.getBytes(StandardCharsets.UTF_8));
    }

    private void checkTimeouts(){
        long now = System.currentTimeMillis();
        lastActivity.entrySet().removeIf(entry -> {
            long last = entry.getValue();
            SocketChannel client = entry.getKey();

            boolean timedOut = (now - last) > WRITE_TIMEOUT_MS;
            try {
                if (timedOut && client.isOpen()) {
                System.out.println("⏳ TIMEOUT: " + client.getRemoteAddress() + " (no I/O for " + (now - last) + "ms) → closing");
                safeClose(client);
            }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println(e.getLocalizedMessage());
            }
            
            return timedOut;
        });
    }

    private void safeClose(Channel channel) {
        try {
            if (channel.isOpen()) {
                channel.close();
                lastActivity.remove(channel);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Error closing " + channel + ": " + e.getMessage());
        }
    }

    // ———————————————————————————————————————————————————————————————————————
    // INNER CLASS: Per-connection state
    // ———————————————————————————————————————————————————————————————————————
    private static class ConnectionState {
        // Buffer for incoming request (reused)
        final ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
        // Buffer for outgoing response (set after request is parsed)
        ByteBuffer responseBuffer = null;
    }
}