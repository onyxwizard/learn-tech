package inputstream.bufferedinputstream.lab5;
import java.io.*;

public class Lab5 {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Lab 5: BufferedInputStream — Why Buffering Matters ===\n");

        // 🔹 Test 1: FileInputStream (unbuffered)
        long start = System.nanoTime();
        countBytes(new FileInputStream("inputstream/bufferedinputstream/large.txt"));
        long rawTime = System.nanoTime() - start;

        // 🔹 Test 2: BufferedInputStream (buffered)
        start = System.nanoTime();
        countBytes(new BufferedInputStream(new FileInputStream("inputstream/bufferedinputstream/large.txt")));
        long bufferedTime = System.nanoTime() - start;

        // 🔹 Report
        double ratio = (double) rawTime / bufferedTime;
        System.out.printf("""
            Results for ~%.1f MB file:
              Unbuffered (FileInputStream): %,d ms
              Buffered (BufferedInputStream): %,d ms
              Speedup: %.1fx faster!
            """,
            new File("large.txt").length() / 1_000_000.0,
            rawTime / 1_000_000,
            bufferedTime / 1_000_000,
            ratio
        );
    }

    // Counts bytes one-by-one — worst-case for unbuffered I/O
    static void countBytes(InputStream in) throws IOException {
        try (in) {
            int count = 0;
            while (in.read() != -1) count++;
            System.out.println("Total bytes: " + count);
        }
    }
}
