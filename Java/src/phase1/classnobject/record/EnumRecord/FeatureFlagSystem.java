// Feature.java
package phase1.classnobject.record.EnumRecord;

// Pure identity — no ON/OFF states (those are *values*, not identities)
enum Feature {
    DARK_MODE,
    LIGHT_MODE,
    THEME_ENGINE  // ← just the *feature*, not its state
}

// Pure data — immutable, no static, no behavior
record FeatureFlag(Feature feature, String key, String description) {}

// State lives where it belongs: a service
class FeatureFlagService {
    private Feature themeEngineStatus = Feature.THEME_ENGINE; // enabled by default? adjust as needed
    private final java.util.Set<Feature> enabledFeatures = new java.util.HashSet<>();

    public void enable(Feature f) { enabledFeatures.add(f); }
    public void disable(Feature f) { enabledFeatures.remove(f); }

    public boolean isEnabled(Feature f) {
        return enabledFeatures.contains(f);
    }

    public void setThemeEngineEnabled(boolean enabled) {
        if (enabled) enable(Feature.THEME_ENGINE);
        else disable(Feature.THEME_ENGINE);
    }

    // Rule: DARK_MODE requires THEME_ENGINE
    public boolean isAvailable(Feature f) {
        if (f == Feature.DARK_MODE) {
            return isEnabled(Feature.THEME_ENGINE);
        }
        return isEnabled(f);
    }
}

// Main.java
public class FeatureFlagSystem {
    public static void main(String[] args) {
        FeatureFlagService service = new FeatureFlagService();

        // Define flags (metadata only — state is in service)
        FeatureFlag darkFlag = new FeatureFlag(Feature.DARK_MODE, "dark_mode", "Enable dark theme");

        // Initially: engine OFF → dark mode unavailable
        service.setThemeEngineEnabled(false);
        System.out.println("Theme engine: " + service.isEnabled(Feature.THEME_ENGINE));
        System.out.println("Dark mode available: " + service.isAvailable(Feature.DARK_MODE));

        // Enable engine → dark mode becomes available
        service.setThemeEngineEnabled(true);
        System.out.println("Theme engine: " + service.isEnabled(Feature.THEME_ENGINE));
        System.out.println("Dark mode available: " + service.isAvailable(Feature.DARK_MODE));
    }
}