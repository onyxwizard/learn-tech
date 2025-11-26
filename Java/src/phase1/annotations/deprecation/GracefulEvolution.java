package phase1.annotations.deprecation;

class LegacyLogger {
    @Deprecated(since = "2.0", forRemoval = true)
    public static void log(String msg) {
        System.out.println("[LEGACY] " + msg);
    }
}

class NewLogger {
    public static void log(String msg) {
        System.out.println("[NEW] " + msg);
    }
}

public class GracefulEvolution {
  public static void main(String[] args) {
        LegacyLogger.log("Hello"); // ⚠️ Warning: 'log(String)' is deprecated
        NewLogger.log("Hello");    // ✅ Clean
    }
}
