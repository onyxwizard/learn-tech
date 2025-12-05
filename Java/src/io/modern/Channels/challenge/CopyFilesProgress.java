package Channels.challenge;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class CopyFilesProgress {

    private static final int BUFFER_SIZE = 8192;
    private static final long PROGRESS_INTERVAL_BYTES = 1024 * 1024; // 1MB

    public static void copyWithProgress(Path srcPath, Path dstPath) throws IOException {
        try (FileChannel src = FileChannel.open(srcPath, StandardOpenOption.READ);
                FileChannel dst = FileChannel.open(dstPath,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {

            long totalSize = src.size();
            long copied = 0;
            long nextReport = PROGRESS_INTERVAL_BYTES;

            ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

            System.out.printf("📁 Copying %d bytes...\n", totalSize);

            int n;
            while ((n = src.read(buffer)) != -1 && n > 0) {
                buffer.flip();
                copied += n;

                // ✅ Report progress (1MB intervals)
                if (copied >= nextReport || copied == totalSize) {
                    double percent = 100.0 * copied / totalSize;
                    System.out.printf("✅ %.1f%% (%d / %d bytes)\n", percent, copied, totalSize);
                    nextReport += PROGRESS_INTERVAL_BYTES;
                }

                // ✅ Handle partial writes
                while (buffer.hasRemaining()) {
                    dst.write(buffer);
                }

                buffer.clear(); // ready for next read
            }

            System.out.println("🎉 Copy complete.");
        }
    }

    public static void main(String[] args) {
        Path src = Paths.get("Channels/challenge/source.txt");
        Path dst = Paths.get("Channels/challenge/destination.txt");

        try {
            copyWithProgress(src, dst);
        } catch (Exception e) {
            System.err.println("❌ Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}