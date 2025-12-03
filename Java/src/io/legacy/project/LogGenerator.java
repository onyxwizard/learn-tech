
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class LogGenerator {
    private static final String[] LEVELS = {"INFO", "WARN", "ERROR", "DEBUG"};
    private static final String[] ACTIONS = {
        "User %s logged in.",
        "Failed to send email to %s",
        "Password reset requested for %s",
        "API call from %s",
        "Backup completed for %s",
        "Suspicious login from IP, associated with %s"
    };
    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "hotmail.com", "myapp.org", "company.co.uk", "test.net"};

    private static final Random RAND = new Random();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Generate a plausible email
    private static String randomEmail() {
        String name = "user" + RAND.nextInt(10000);
        String domain = DOMAINS[RAND.nextInt(DOMAINS.length)];
        return name + "@" + domain;
    }

    public static void main(String[] args) throws IOException {
        // 🔧 CONFIG: adjust size
        long targetBytes = 100_000_000; // ~100 MB (start small! 1GB = 1_000_000_000)
        String filename = "project/app.log";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            long written = 0;
            int lineNum = 0;

            while (written < targetBytes) {
                LocalDateTime now = LocalDateTime.now().minusSeconds(RAND.nextInt(86400)); // past 24h
                String level = LEVELS[RAND.nextInt(LEVELS.length)];
                String actionTemplate = ACTIONS[RAND.nextInt(ACTIONS.length)];
                String email = randomEmail();
                String line = String.format(
                    "%s %s %s%n", 
                    now.format(FMT), 
                    level, 
                    String.format(actionTemplate, email)
                );

                writer.write(line);
                written += line.getBytes().length;
                lineNum++;

                // Progress every ~10 MB
                if (lineNum % 200_000 == 0) {
                    System.out.printf("Generated %d lines, ~%.1f MB%n", 
                        lineNum, written / 1_000_000.0);
                }
            }
            System.out.printf("✅ Done! %d lines, %.1f MB written to '%s'%n", 
                lineNum, written / 1_000_000.0, filename);
        }
    }
}