package phase1.basics.var;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModernRefactor {
    public static void main(String[] args) {
        // DO NOT change the logic — only refactor type declarations to use `var` where valid and clear

        String appName = "UserProcessor";
        List<String> roles = Arrays.asList("admin", "editor", "viewer");
        Map<String, Object> config = new HashMap<>();
        config.put("timeout", 30);
        config.put("retries", 3);

        System.out.println("App: " + appName);
        System.out.println("Roles: " + roles);
        System.out.println("Config: " + config);

        for (int i = 0; i < roles.size(); i++) {
            String role = roles.get(i);
            System.out.println("Processing role: " + role);
        }

        for (String role : roles) {
            if (role.equals("admin")) {
                System.out.println("→ Special access granted to: " + role);
            }
        }

        Path logPath = Path.of("app.log");
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line = reader.readLine();
            if (line != null) {
                System.out.println("Log preview: " + line);
            }
        } catch (IOException e) {
            System.out.println("Log file not found — using defaults.");
        }
    }
}
