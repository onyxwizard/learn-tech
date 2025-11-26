// Feature.java
package phase1.classnobject.record.SealedInterfaces;

// Sealed hierarchy: only these feature kinds allowed
sealed interface Feature2 
    permits DarkMode, ThemeEngine, BetaFeature {}

// Each feature has its own shape
record DarkMode(boolean enabled, String theme) implements Feature2 {}
record ThemeEngine(boolean enabled) implements Feature2 {}
record BetaFeature(String variant, int rolloutPercent) implements Feature2 {}

// Service with exhaustive, safe logic
class FeatureFlagService {
    private ThemeEngine themeEngine = new ThemeEngine(false);

    public void setThemeEngine(boolean enabled) {
        this.themeEngine = new ThemeEngine(enabled);
    }

    public boolean isAvailable(Feature2 feature) {
        return switch (feature) {
            case DarkMode dm -> dm.enabled() && themeEngine.enabled();
            case ThemeEngine te -> te.enabled();
            case BetaFeature bf -> themeEngine.enabled() && bf.rolloutPercent() > 50;
        };
    }
}

// Main.java
public class FeatureFlagSystem2 {
    public static void main(String[] args) {
        FeatureFlagService service = new FeatureFlagService();

        Feature2 dark = new DarkMode(true, "midnight");
        Feature2 beta = new BetaFeature("v2", 75);

        service.setThemeEngine(false);
        System.out.println("Dark available (engine off): " + service.isAvailable(dark));  // false
        System.out.println("Beta available (engine off): " + service.isAvailable(beta));   // false

        service.setThemeEngine(true);
        System.out.println("Dark available (engine on): " + service.isAvailable(dark));   // true
        System.out.println("Beta available (engine on): " + service.isAvailable(beta));    // true
    }
}
