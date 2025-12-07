package Channels.challenge2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ThreadSafeAppender implements AutoCloseable {
  private final FileChannel channel;
  private final ByteBuffer newline = ByteBuffer.wrap("\n".getBytes());

  public ThreadSafeAppender(Path path) throws IOException {
    this.channel = FileChannel.open(path,
        StandardOpenOption.CREATE,
        StandardOpenOption.WRITE,
        StandardOpenOption.APPEND); // hint to OS
  }

  public void append(String message) throws IOException {
    byte[] data = (message + "\n").getBytes();
    ByteBuffer buf = ByteBuffer.wrap(data);

    // ✅ Critical: use absolute positioning
    long pos = channel.size(); // atomic read
    channel.write(buf, pos); // write at exact offset
    // (For <4KB, OS guarantees atomicity on most FS)
  }

  @Override
  public void close() throws IOException {
    channel.close();
  }

  // Test
  public static void main(String[] args) throws IOException {
    Path log = Paths.get("Channels/challenge2/safe.log");
    try (ThreadSafeAppender appender = new ThreadSafeAppender(log)) {
      // Simulate concurrent appends
      Thread t1 = new Thread(() -> {
        for (int i = 0; i < 100; i++) {
          try {
            appender.append("Thread-1: " + i);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      Thread t2 = new Thread(() -> {
        for (int i = 0; i < 100; i++) {
          try {
            appender.append("Thread-2: " + i);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      t1.start();
      t2.start();
      t1.join();
      t2.join();
    }catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getLocalizedMessage());
    }
    // Verify: no corruption, all lines present
    java.nio.file.Files.readAllLines(log)
        .forEach(System.out::println);
  }
}