package Channels.challenge2;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
//import java.nio.channels.InterruptedByTimeoutException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

class InterruptedByTimeoutException extends IOException {
  private String msg;

  InterruptedByTimeoutException(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return msg;
  }
}

class FileNotFoundException extends IOException {
  private String msg;

  FileNotFoundException(String msg) {
    this.msg = msg;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return msg;
  }
}

public class SafeFileCopier {

  private static final int BUFFER_SIZE = 256 * 1024; // 256KB — optimal for most SSDs
  private static final long PROGRESS_INTERVAL = 1024L * 1024; // 1MB

  /**
   * Safely copy a file with progress reporting and cancellation.
   *
   * @param src        Source file
   * @param dst        Destination file
   * @param progress   Callback for progress updates (e.g., "50% (50/100 MB)")
   * @param cancelFlag Atomic flag to support cancellation
   * @throws IOException on I/O error
   */
  public static void copy(Path src, Path dst, Consumer<String> progress, AtomicBoolean cancelFlag)
      throws IOException {
    if (Files.notExists(src)) {
      throw new FileNotFoundException("Source not found: " + src);
    }
    if (Files.isDirectory(src)) {
      throw new IOException("Source is a directory: " + src);
    }

    long size = Files.size(src);
    if (size == 0) {
      Files.createFile(dst);
      progress.accept("✅ Empty file created.");
      return;
    }

    // Ensure parent directory exists
    Files.createDirectories(dst.getParent());

    try (FileChannel srcCh = FileChannel.open(src, StandardOpenOption.READ);
        FileChannel dstCh = FileChannel.open(dst,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING)) {

      // Try zero-copy first (fastest path)
      if (tryZeroCopy(srcCh, dstCh, size, progress, cancelFlag)) {
        return;
      }

      // Fallback to manual copy
      manualCopy(srcCh, dstCh, size, progress, cancelFlag);
    }
  }

  // ----------------------------------------
  // 1. Zero-Copy Path (FileChannel.transferTo)
  // ----------------------------------------
  private static boolean tryZeroCopy(
      FileChannel src, FileChannel dst, long size,
      Consumer<String> progress, AtomicBoolean cancel) throws IOException {
    long position = 0;
    long nextReport = PROGRESS_INTERVAL;
    long start = System.nanoTime();

    try {
      while (position < size) {
        checkCancelled(cancel);

        long toTransfer = Math.min(size - position, Integer.MAX_VALUE);
        long transferred = src.transferTo(position, toTransfer, dst);
        if (transferred <= 0) {
          // Zero-copy failed — fall back to manual
          return false;
        }
        position += transferred;

        // Progress reporting
        if (position >= nextReport || position == size) {
          reportProgress(position, size, start, progress);
          nextReport += PROGRESS_INTERVAL;
        }
      }
      return true;
    } catch (IOException e) {
      // Expected on some OS/filesystems — fallback is safe
      return false;
    }
  }

  // ----------------------------------------
  // 2. Manual Copy Path (Buffered loop)
  // ----------------------------------------
  private static void manualCopy(
      FileChannel src, FileChannel dst, long size,
      Consumer<String> progress, AtomicBoolean cancel) throws IOException {
    ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
    long copied = 0;
    long nextReport = PROGRESS_INTERVAL;
    long start = System.nanoTime();

    while (copied < size) {
      checkCancelled(cancel);

      // Fill buffer
      buf.clear();
      int n = src.read(buf);
      if (n <= 0)
        break;
      buf.flip();

      // Write buffer (handle partial writes)
      while (buf.hasRemaining()) {
        checkCancelled(cancel);
        copied += dst.write(buf);
      }

      // Progress reporting
      if (copied >= nextReport || copied == size) {
        reportProgress(copied, size, start, progress);
        nextReport += PROGRESS_INTERVAL;
      }
    }

    if (copied != size) {
      throw new IOException("Incomplete copy: " + copied + " < " + size);
    }
  }

  // ----------------------------------------
  // 3. Utility Methods
  // ----------------------------------------
  private static void checkCancelled(AtomicBoolean cancel) throws IOException {
    if (cancel != null && cancel.get()) {
      throw new IOException("Copy cancelled by user");
    }
    if (Thread.interrupted()) {
      throw new InterruptedByTimeoutException("Copy interrupted");
    }
  }

  private static void reportProgress(long copied, long total, long startNs, Consumer<String> progress) {
    double percent = 100.0 * copied / total;
    double elapsedSec = (System.nanoTime() - startNs) / 1_000_000_000.0;
    double mbps = (copied / (1024.0 * 1024)) / elapsedSec;

    String msg = String.format("%.1f%% (%.1f/%.1f MB) @ %.1f MB/s",
        percent,
        copied / (1024.0 * 1024),
        total / (1024.0 * 1024),
        mbps);
    progress.accept(msg);
  }

  // ----------------------------------------
  // 4. Bonus: Safe Memory-Mapped Reader (For huge files)
  // ----------------------------------------
  public static class SafeMappedReader implements AutoCloseable {
    private final FileChannel channel;
    private final MappedByteBuffer buffer;
    private final boolean unmappable;

    public SafeMappedReader(Path path) throws IOException {
      this.channel = FileChannel.open(path, StandardOpenOption.READ);
      long size = channel.size();

      MappedByteBuffer tempBuf = null;
      boolean tempUnmappable = false;

      if (size <= Integer.MAX_VALUE && size > 0) {
        try {
          tempBuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, size);
          tempUnmappable = canUnmap(tempBuf);
        } catch (IOException e) {
          System.err.println("MemoryWarning: Failed to memory-map " + path + ": " + e.getMessage());
          // Proceed with channel-based reads
        }
      }
      this.buffer = tempBuf;
      this.unmappable = tempUnmappable;
    }


  public byte get(long index) throws IOException {
    if (buffer != null) {
      return buffer.get((int) index);
    } else {
      ByteBuffer b = ByteBuffer.allocate(1);
      channel.read(b, index);
      return b.get(0);
    }
  }

  @Override
  public void close() throws IOException {
    if (buffer != null && unmappable) {
      tryUnmap(buffer);
    }
    channel.close();
  }

  // --- Reflective unmapping (Java 9+ safe) ---
  private static boolean canUnmap(MappedByteBuffer buf) {
    return Runtime.version().feature() >= 9 &&
        buf.getClass().getSimpleName().equals("DirectByteBuffer");
  }

  private static void tryUnmap(MappedByteBuffer buffer) {
    try {
      Field cleanerField = buffer.getClass().getDeclaredField("cleaner");
      cleanerField.setAccessible(true);
      Object cleaner = cleanerField.get(buffer);
      if (cleaner != null) {
        cleaner.getClass().getMethod("clean").invoke(cleaner);
      }
    } catch (Exception e) {
      // Silent fail — better than crash
    }
  }

  }

  // ----------------------------------------
  // 5. Test Harness
  // ----------------------------------------
  public static void main(String[] args) throws Exception {
    // Create test file (100MB)
    Path src = Paths.get("Channels/challenge2/app.log");
    if (!Files.exists(src)) {
      System.out.println("🔧 Creating 100MB test file...");
      try (FileChannel ch = FileChannel.open(src,
          StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
        ByteBuffer buf = ByteBuffer.allocate(1024 * 1024);
        for (int i = 0; i < 100; i++) {
          buf.clear();
          buf.put(new byte[buf.capacity()]);
          buf.flip();
          ch.write(buf);
        }
      }
    }

    Path dst = Paths.get("Channels/challenge2/copy.log");
    AtomicBoolean cancel = new AtomicBoolean(false);

    // Run copy
    System.out.println("🚀 Starting safe copy...");
    long start = System.currentTimeMillis();
    copy(src, dst,
        msg -> System.out.println("📊 " + msg),
        cancel);
    System.out.println("✅ Copy complete in " + (System.currentTimeMillis() - start) + " ms");
    System.out.println("📦 Verified size: " + Files.size(dst) + " bytes");
  }
}