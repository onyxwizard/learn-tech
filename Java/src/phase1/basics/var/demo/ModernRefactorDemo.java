package phase1.basics.var.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;


public class ModernRefactorDemo {
    public static void main(String[] args) {
        // DO NOT change the logic — only refactor type declarations to use `var` where valid and clear

        var appName = "UserProcessor";
        var roles = Arrays.asList("admin", "editor", "viewer");
        var config = new HashMap<String, Object>();
        config.put("timeout", 30);
        config.put("retries", 3);

        System.out.println("App: " + appName);
        System.out.println("Roles: " + roles);
        System.out.println("Config: " + config);

        for (var i = 0; i < roles.size(); i++) {
            var role = roles.get(i);
            System.out.println("Processing role: " + role);
        }

        for (var role : roles) { // ✅ Now consistent!
            if (role.equals("admin")) {
                System.out.println("→ Special access granted to: " + role);
            }
        }

        var logPath = Path.of("app.log");
        try (var reader = Files.newBufferedReader(logPath)) { // ✅ Also use var here!
            var line = reader.readLine();
            if (line != null) {
                System.out.println("Log preview: " + line);
            }
        } catch (IOException e) {
            System.out.println("Log file not found — using defaults.");
        }

        // ❌ INVALID USES (commented out - would cause compiler errors)
        // private var name = "test";        // ❌ var not allowed for fields
        // public void method(var param) {}  // ❌ var not allowed in parameters
        // var uninitialized;                // ❌ must have initializer
        // var nullVar = null;               // ❌ null has no type
    }
}