package inputstream.pipedinputstream.lab9;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Lab9 {
      public static void main(String[] args) throws Exception {
        System.out.println("=== Lab 9: Piped Streams — Thread-to-Thread Pipes ===\n");

        // 🔹 Step 1: Create connected pair
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis); // connect!

        // 🔹 Step 2: Start READER thread (consumer)
        Thread readerThread = new Thread(() -> {
            try (pis) { // auto-close when done
                System.out.println("[Reader] Started. Waiting for data...");
                int b;
                StringBuilder sb = new StringBuilder();
                while ((b = pis.read()) != -1) {
                    sb.append((char) b);
                }
                System.out.println("[Reader] Done. Received: \"" + sb + "\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Reader-Thread");
        readerThread.start();

        // 🔹 Step 3: Start WRITER thread (producer)
        Thread writerThread = new Thread(() -> {
            try (pos) {
                System.out.println("[Writer] Started. Sending data...");
                String msg = "Hello from Writer! 🌍";
                pos.write(msg.getBytes());
                pos.flush(); // ensure sent
                System.out.println("[Writer] Sent: " + msg.length() + " bytes");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Writer-Thread");
        writerThread.start();

        // 🔹 Wait for both
        writerThread.join();
        readerThread.join();
        System.out.println("\n✅ Pipe communication completed.");
    }
}
