package phase1.optional.basic;

import java.util.Optional;

class User {
    private final String email;
    User(String email) { this.email = email; }

    Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}

class EmailService {
    public static void send(String email, String message) {
        System.out.println("📧 Sending to '" + email + "': " + message);
    }
}

public class EmailValidator {
    public static void sendWelcomeEmail(User user) {
        user.getEmail()
            .filter(email -> !email.trim().isEmpty())   // non-empty only
            .ifPresent(email -> EmailService.send(email, "Welcome to our service!"));
    }

    public static void main(String[] args) {
        // Test cases
        sendWelcomeEmail(new User("alice@example.com"));  // ✅ sends
        sendWelcomeEmail(new User(""));                   // ❌ does nothing
        sendWelcomeEmail(new User(null));                 // ❌ does nothing
        sendWelcomeEmail(new User("   "));                // ❌ (whitespace) does nothing
    }
}