package Files.Challenge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

public class LogAnalyzer {

    private static final String ERROR_KEYWORD = "ERROR";
    private static final String LOG_EXTENSION = ".log";

    public static void main(String[] args) {
        // Default directory — change as needed
        Path logDir = Paths.get("Files/Challenge/logs");

        System.out.println("🔍 Scanning for ERRORs in: " + logDir.toAbsolutePath());
        System.out.println("==============================================");

        try {
            analyzeLogs(logDir);
            System.out.println("✅ Done.");
        } catch (IOException e) {
            System.err.println("❌ Scan failed: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Scans directory recursively for .log files and prints ERROR lines.
     *
     * @param dir Root directory to scan
     * @throws IOException if I/O error occurs
     */
    public static void analyzeLogs(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            System.out.println("⚠️  Directory not found: " + dir);
            return;
        }

        try (Stream<Path> paths = Files.walk(dir)) {
            paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(LOG_EXTENSION))
                .sorted() // predictable order
                .forEach(LogAnalyzer::processLogFile);
        }
    }

    /**
     * Processes a single log file: streams lines, finds ERRORs, prints matches.
     */
    private static void processLogFile(Path file) {
        try {
            // Stream lines lazily — no full-file load
            try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
                lines
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .filter(line -> line.contains(ERROR_KEYWORD))
                    .forEach(line -> {
                        // To get line numbers, we'd need indexed stream — let's upgrade
                    });
            }
        } catch (IOException e) {
            System.err.println("⚠️ Skipping unreadable file: " + file.getFileName() + " (" + e.getMessage() + ")");
        }
    }
}



/**
 * package nio2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Stream;

public class LogAnalyzer {

    private static final String ERROR_KEYWORD = "ERROR";
    private static final String LOG_EXTENSION = ".log";

    public static void main(String[] args) {
        Path logDir = Paths.get("logs");
        System.out.println("🔍 Scanning for ERRORs in: " + logDir.toAbsolutePath());
        System.out.println("==============================================");

        try {
            analyzeLogs(logDir);
            System.out.println("✅ Done.");
        } catch (IOException e) {
            System.err.println("❌ Scan failed: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void analyzeLogs(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            System.out.println("⚠️  Directory not found: " + dir);
            return;
        }

        try (Stream<Path> paths = Files.walk(dir)) {
            paths
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(LOG_EXTENSION))
                .sorted()
                .forEach(LogAnalyzer::processLogFile);
        }
    }

    private static void processLogFile(Path file) {
        try (LineNumberReader reader = new LineNumberReader(
                Files.newBufferedReader(file, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(ERROR_KEYWORD)) {
                    // Format: filename:lineNumber: line
                    System.out.printf("%s:%d: %s%n",
                            file.getFileName(),
                            reader.getLineNumber(),
                            line.trim());
                }
            }

        } catch (IOException e) {
            System.err.println("⚠️ Skipping unreadable file: " + file.getFileName() + " (" + e.getMessage() + ")");
        }
    }
}
 */