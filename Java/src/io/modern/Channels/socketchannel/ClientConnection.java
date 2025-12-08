package Channels.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * A robust NIO-based HTTP client demonstrating low-level socket interaction.
 * 
 * 🔍 Key design insights:
 * 
 * 1. TCP is full-duplex & buffered:
 * - write() → sends data to server (via client's *send buffer* in OS kernel).
 * - read() → pulls server's reply from client's *receive buffer* (also in OS
 * kernel).
 * - If read() is never called, responses accumulate in the *client-side kernel
 * buffer*.
 * Once full → TCP flow control stalls the *server's write()*, risking
 * server-side DoS.
 * 
 * 2. This client explicitly:
 * - Sets a socket timeout to avoid indefinite blocking.
 * - Reads in a loop to handle multi-packet responses.
 * - Cleans up resources (socket close) even on error.
 * - Uses StandardCharsets consistently (no platform-default surprises).
 * 
 * 3. Security/Reliability note:
 * - A malicious or broken client that writes but never reads can cause
 * server thread starvation (slow-client attack). Hence, servers MUST
 * enforce write timeouts and use async I/O or backpressure.
 * - This client avoids that risk by always reading — and timing out if needed.
 * 
 * 🧪 To test: Run a local HTTP server first, e.g.:
 * python3 -m http.server 9090
 */
public class ClientConnection {

  public static void main(String[] args) {
    // Use try-with-resources where possible; SocketChannel doesn't implement
    // AutoCloseable,
    // so we manage closure manually in finally block for clarity and control.
    SocketChannel client = null;
    try {
      // Open a non-blocking or blocking SocketChannel (default: blocking)
      client = SocketChannel.open();

      // 🔐 Critical: Set SO_TIMEOUT to avoid indefinite blocking on read().
      // Without this, a stalled server (or network partition) could hang this thread
      // forever.
      // Note: This timeout applies to *blocking* read() calls only.
      client.socket().setSoTimeout(5000); // 5 seconds

      // Connect to localhost:9090 (assumes a server is listening, e.g., `python -m
      // http.server 9090`)
      System.out.println("Connecting to localhost:9090...");
      client.connect(new InetSocketAddress("localhost", 9090));
      System.out.println("✅ Connected.");

      // 📤 Send HTTP/1.1 request
      // - Connection: close → signals server to close after response (simplifies
      // client logic)
      // - Host header required by HTTP/1.1
      String request = "GET / HTTP/1.1\r\n" +
          "Host: localhost\r\n" +
          "Connection: close\r\n" + // 👈 Ensures server closes; avoids keep-alive ambiguity
          "\r\n";

      ByteBuffer requestBuffer = ByteBuffer.wrap(request.getBytes(StandardCharsets.US_ASCII));
      int written = client.write(requestBuffer);
      System.out.println("📤 Sent " + written + " bytes request.");

      // 📥 Read full HTTP response
      // - Server may send response in multiple TCP packets.
      // - Single read() may return partial data → loop until EOF (-1) or timeout.
      ByteBuffer responseBuffer = ByteBuffer.allocate(4096); // Larger buffer for efficiency
      StringBuilder responseBody = new StringBuilder();

      System.out.println("📥 Reading response...");
      while (true) {
        int bytesRead = client.read(responseBuffer);

        if (bytesRead == -1) {
          // EOF: Server closed connection (as requested by "Connection: close")
          break;
        } else if (bytesRead == 0) {
          // Non-blocking mode only: no data available yet.
          // Since we're in blocking mode, this shouldn't occur.
          continue;
        }

        // Flip buffer to read mode: limit = position, position = 0
        responseBuffer.flip();
        // Decode only the newly read portion (0 → limit), not entire array
        responseBody.append(StandardCharsets.UTF_8.decode(responseBuffer));
        // Compact: shift unread data to front, reset position/limit for next read
        responseBuffer.compact();
      }

      System.out.println("✅ Full response received:\n");
      System.out.println(responseBody.toString());

    } catch (IOException e) {
      System.err.println("❌ I/O error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
      e.printStackTrace();
    } finally {
      // 🧹 Always close the channel — even on error or early exit.
      // This releases the file descriptor, sends TCP FIN/RST, and discards
      // any unread data in the *client's kernel receive buffer*.
      if (client != null && client.isOpen()) {
        try {
          client.close();
          System.out.println("🔌 Socket closed.");
        } catch (IOException e) {
          System.err.println("⚠️ Failed to close socket: " + e.getMessage());
        }
      }
    }
  }
}