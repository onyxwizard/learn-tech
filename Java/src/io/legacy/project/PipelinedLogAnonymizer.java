import java.io.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class PipelinedLogAnonymizer {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");
    
    // Bounded queue → enforces backpressure
    private static final int QUEUE_CAPACITY = 10_000;

    public static void main(String[] args) throws Exception {
        String input = "project/app.log";
        String output = "project/app.anon.log";

        // Stage 1 → Stage 2
        BlockingQueue<String> rawLines = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        // Stage 2 → Stage 3
        BlockingQueue<String> redactedLines = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

        ExecutorService pool = Executors.newFixedThreadPool(3);

        // 📥 STAGE 1: Reader
        Future<Long> reader = pool.submit(() -> {
            long count = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(input))) {
                String line;
                while ((line = br.readLine()) != null) {
                    rawLines.put(line); // blocks if queue full → backpressure!
                    count++;
                }
                rawLines.put(null); // poison pill
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return count;
        });

        // ⚙️ STAGE 2: Processor
        Future<Long> processor = pool.submit(() -> {
            long redactedCount = 0;
            try {
                while (true) {
                    String line = rawLines.take(); // blocks if empty
                    if (line == null) { // poison pill
                        redactedLines.put(null);
                        break;
                    }
                    String redacted = EMAIL_PATTERN.matcher(line).replaceAll("[REDACTED]");
                    redactedLines.put(redacted);
                    redactedCount++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return redactedCount;
        });

        // 📤 STAGE 3: Writer
        Future<Void> writer = pool.submit(() -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(output))) {
                while (true) {
                    String line = redactedLines.take();
                    if (line == null) break; // done
                    bw.write(line);
                    bw.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return null;
        });

        // Wait for all
        long lines = reader.get();
        long redacted = processor.get();
        writer.get();
        pool.shutdown();

        System.out.printf("""
            ✅ Pipeline complete!
            Lines: %,d | Redacted: %,d
            """, lines, redacted);
    }
}