package phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.impl;

//FileLogger.java
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.service.Logger;
import java.util.ArrayList;
import java.util.List;

public class FileLogger implements Logger {
    private final String filePath;
    private final List<String> logs = new ArrayList<>(); // ✅ per-instance, not static

    public FileLogger(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void log(String message) {
        String entry = "[LOG] " + message;
        logs.add(entry);
        System.out.println(entry); // Simulate file write
    }

    // Optional: expose logs for testing
    public List<String> getLogs() { return new ArrayList<>(logs); }
}