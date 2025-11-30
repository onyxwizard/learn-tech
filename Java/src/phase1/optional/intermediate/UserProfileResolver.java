package phase1.optional.intermediate;

import java.util.Optional;

// Immutable domain objects — no setters, no null exposure
class User {
    private final String id;
    private final Optional<Profile> profile;

    public User(String id, Optional<Profile> profile) {
        this.id = id;
        this.profile = profile;
    }

    public String getId() { return id; }
    public Optional<Profile> getProfile() { return profile; }
}

class Profile {
    private final String displayName;
    private final Optional<Settings> settings;

    public Profile(String displayName, Optional<Settings> settings) {
        this.displayName = displayName;
        this.settings = settings;
    }

    public String getDisplayName() { return displayName; }
    public Optional<Settings> getSettings() { return settings; }
}

class Settings {
    private final String theme;
    private final boolean notificationsEnabled;

    public Settings(String theme, boolean notificationsEnabled) {
        this.theme = theme;
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getTheme() { return theme; }
}

// 🚀 Resolver — pure functional, no side effects
public class UserProfileResolver {

    /**
     * Resolves display name with priority:
     * 1. profile.displayName (non-blank)
     * 2. settings.theme (uppercased), if displayName missing
     * 3. "User-" + id, if neither exists
     *
     * Uses only: map, flatMap, filter, orElse, orElseGet
     */
    public static String resolveDisplayName(User user) {
        // ✅ Pipe 1: displayName (non-blank only)
        Optional<String> displayNameOpt = user.getProfile()
            .map(Profile::getDisplayName)                  // Optional<String>
            .filter(name -> name != null && !name.trim().isEmpty()); // exclude null/blank

        // ✅ Pipe 2: theme (uppercased)
        Optional<String> themeOpt = user.getProfile()
            .flatMap(Profile::getSettings)                 // Optional<Settings>
            .map(Settings::getTheme)
            .filter(theme -> !theme.trim().isEmpty() && theme!= null)                     // Optional<String>
            .map(String::toUpperCase);                     // Optional<String>

        // ✅ Chain: displayName → theme → fallback
        return displayNameOpt
            .orElseGet(() -> 
                themeOpt.orElse("User-" + user.getId())
            );
    }

    // 🧪 Test harness
    public static void main(String[] args) {
        // Test Case 1: Has displayName → "Alice"
        User u1 = new User("101", 
            Optional.of(new Profile("Alice", Optional.empty()))
        );

        // Test Case 2: No displayName, but has settings.theme → "DARK"
        User u2 = new User("102", 
            Optional.of(new Profile(null, 
                Optional.of(new Settings("dark", true))
            ))
        );

        // Test Case 3: No profile → "User-103"
        User u3 = new User("103", Optional.empty());
        User u4 = new User("104", Optional.of(new Profile(null, Optional.of(new Settings("ok", false)))));

        // ✅ Run and print
        System.out.println(resolveDisplayName(u1)); // → Alice
        System.out.println(resolveDisplayName(u2)); // → DARK
        System.out.println(resolveDisplayName(u3)); // → User-103
        System.out.println(resolveDisplayName(u4)); // → User-103
    }
}