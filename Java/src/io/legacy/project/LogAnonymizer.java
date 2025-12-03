import java.io.*;
import java.util.regex.Pattern;

public class LogAnonymizer {

    // Compile regex once — faster & reusable
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b");

    public static void main(String[] args) {
        String inputPath = "project/app.log";
        String outputPath = "project/app.anon.log";
        long startTime = System.nanoTime();
        long totalRedacted = 0;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))
        ) {
            String line;
            int lineNum = 0;

            while ((line = reader.readLine()) != null) { // ✅ null-check for EOF
                lineNum++;

                // Count matches before replacing (optional)
                java.util.regex.Matcher matcher = EMAIL_PATTERN.matcher(line);
                int countInLine = 0;
                while (matcher.find()) countInLine++;

                // Replace ALL emails in line
                String redactedLine = EMAIL_PATTERN.matcher(line).replaceAll("[REDACTED]");
                writer.write(redactedLine);
                writer.newLine(); // preserves line breaks

                totalRedacted += countInLine;

                // Progress every 100k lines
                if (lineNum % 100_000 == 0) {
                    System.out.printf("Processed %d lines, %d emails redacted%n", lineNum, totalRedacted);
                }
            }

            System.out.printf("""
                ✅ Anonymization complete!
                - Lines processed: %,d
                - Emails redacted: %,d
                - Output: %s
                """, lineNum, totalRedacted, outputPath);

        } catch (IOException e) {
            System.err.println("❌ I/O Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
          } finally {
          long endTime = System.nanoTime();
          int sum = (int)(endTime - startTime)/1_000_000;
          System.out.println(sum+"ms");
        }
    }
}