package inputstream.pipedinputstream.lab9;

import java.io.*;

public class Challenge {
  public static void main(String[] args) throws Exception {
    System.out.println("=== Challenge: Piped Streams — Thread-to-Thread Pipes ===\n");

        // 🔹 Step 1: Create connected pair
    PipedInputStream pis = new PipedInputStream();
    PipedOutputStream pos = new PipedOutputStream(pis);

    // Lets Create Reader
    Thread readerThread = new Thread(() -> {
      try (pis) {
        System.out.println("[Reader] Started. Waiting for data...");
        int b;
        StringBuilder sb = new StringBuilder();
        while ((b = pis.read()) != -1) {
          sb.append((char) b);
        }
        System.out.println("[Reader] Done. Received: \"" + sb + "\"");
      } catch (IOException e) {
        System.out.println(e.getLocalizedMessage());
      }
    }, "reader-Thread");
    readerThread.start();

    //Lets create Write
    Thread writerThread = new Thread(() -> {
      try (pos) {
        System.out.println("[Writer] Started. Sending data...");
        for (int i = 0; i < 10; i++) {
          pos.write(("Message " + i + "\n").getBytes());
          pos.flush();
          Thread.sleep(100);
        }
        pos.close();
        
      } catch (IOException e) {
        System.out.println(e.getLocalizedMessage());
      }catch (InterruptedException e) {
        System.out.println(e.getLocalizedMessage());
      }
    }, "Write-Thread");
    writerThread.start();

    writerThread.join();
    readerThread.join();

  }
}
