package phase1.built_in_functions.wrapper;

import java.util.*;

/**
 * Boolean Wrapper Deep Dive — Beyond True/False
 * 
 * <p>Unlike numeric wrappers, Boolean:
 * <ul>
 *   <li>❌ Does NOT extend {@code Number}</li>
 *   <li>✅ Is immutable, final, and thread-safe</li>
 *   <li>✅ Caches only TWO instances: {@code Boolean.TRUE} and {@code Boolean.FALSE}</li>
 *   <li>⚠️ Introduces {@code null} as a third state — the root of many bugs</li>
 * </ul>
 * 
 * <p><b>Key Insight:</b>
 * "The most dangerous value in Java isn't NaN — it's {@code null} masquerading as {@code Boolean}."
 */
public class BooleanWrapperDeepDive {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("BOOLEANS: Truth, Lies, and the Dangerous Third State (null)");
        System.out.println("=".repeat(70));

        // ───────────────────────────────────────────────────────────────────────────────
        // 1. INSTANTIATION — ONLY TWO LEGITIMATE WAYS
        // ───────────────────────────────────────────────────────────────────────────────
        demoInstantiation();

        // ───────────────────────────────────────────────────────────────────────────────
        // 2. CORE INSTANCE METHODS — The Object Contract
        // ───────────────────────────────────────────────────────────────────────────────
        demoCoreMethods();

        // ───────────────────────────────────────────────────────────────────────────────
        // 3. STATIC UTILITIES — Parsing, Conversion, Logic
        // ───────────────────────────────────────────────────────────────────────────────
        demoStaticUtilities();

        // ───────────────────────────────────────────────────────────────────────────────
        // 4. SYSTEM PROPERTIES — The Silent Feature Flag Trap
        // ───────────────────────────────────────────────────────────────────────────────
        demoSystemProperties();

        // ───────────────────────────────────────────────────────────────────────────────
        // 5. REAL-WORLD PATTERNS & ANTI-PATTERNS
        // ───────────────────────────────────────────────────────────────────────────────
        printBestPractices();
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // 1. INSTANTIATION: How to (and how NOT to) create Boolean objects
    // ───────────────────────────────────────────────────────────────────────────────────
    private static void demoInstantiation() {
        System.out.println("\n🔹 1. Instantiation — The Cache is Tiny (Only 2 Objects!)");

        // ✅ CORRECT: Use valueOf() — always returns cached TRUE/FALSE
        Boolean b1 = Boolean.valueOf(true);
        Boolean b2 = Boolean.valueOf(false);
        Boolean b3 = Boolean.valueOf(true);

        System.out.println("  valueOf(true) ×2 → same ref? " + (b1 == b3)); // true
        System.out.println("  TRUE/FALSE are singletons:");
        System.out.println("    Boolean.TRUE == b1? " + (Boolean.TRUE == b1));   // true
        System.out.println("    Boolean.FALSE == b2? " + (Boolean.FALSE == b2)); // true

        // ❌ DANGEROUS: new Boolean(boolean) — deprecated since Java 9!
        // Creates unnecessary objects (wastes memory, harms GC)
        @SuppressWarnings("deprecation")
        Boolean bad = new Boolean(true); // Avoid! Use valueOf()
        System.out.println("  new Boolean(true) == TRUE? " + (bad == Boolean.TRUE)); // false!

        // ✅ AUTOMATIC: Autoboxing uses valueOf() — safe
        Boolean autoBoxed = true; // compiler → Boolean.valueOf(true)
        System.out.println("  Autoboxing uses valueOf? " + (autoBoxed == Boolean.TRUE)); // true

        // ❗ NULL: The third state — not false!
        Boolean maybe = null;
        System.out.println("  null Boolean: " + maybe); // prints "null"
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // 2. CORE INSTANCE METHODS — Equals, Compare, HashCode
    // ───────────────────────────────────────────────────────────────────────────────────
    private static void demoCoreMethods() {
        System.out.println("\n🔹 2. Core Methods — Beware of null!");

        Boolean t = Boolean.TRUE;
        Boolean f = Boolean.FALSE;
        Boolean n = null;

        /* 
         * 📌 booleanValue() → boolean (unboxing)
         * Throws NullPointerException if this is null.
         */
        System.out.println("  TRUE.booleanValue(): " + t.booleanValue()); // true
        try {
            n.booleanValue(); // 💥 NPE!
        } catch (NullPointerException e) {
            System.out.println("  null.booleanValue() → " + e.getClass().getSimpleName());
        }

        /* 
         * 📌 equals(Object)
         * - Returns true iff obj is Boolean and same value.
         * - Handles null safely (returns false).
         * ✅ Use in collections (Map keys, Set).
         */
        System.out.println("  TRUE.equals(TRUE): " + t.equals(t));     // true
        System.out.println("  TRUE.equals(FALSE): " + t.equals(f));   // false
        System.out.println("  TRUE.equals(null): " + t.equals(n));    // false
        System.out.println("  null.equals(TRUE): " + (n == null ? "N/A (NPE)" : n.equals(t))); // NPE if called!

        /* 
         * 📌 compareTo(Boolean)
         * - false < true → FALSE.compareTo(TRUE) = -1
         * - null throws NullPointerException!
         */
        System.out.println("  FALSE.compareTo(TRUE): " + f.compareTo(t)); // -1
        System.out.println("  TRUE.compareTo(FALSE): " + t.compareTo(f)); // 1
        System.out.println("  TRUE.compareTo(TRUE): " + t.compareTo(t));  // 0
        try {
            t.compareTo(n); // 💥 NPE!
        } catch (NullPointerException e) {
            System.out.println("  TRUE.compareTo(null) → " + e.getClass().getSimpleName());
        }

        /* 
         * 📌 hashCode()
         * - TRUE → 1231, FALSE → 1237 (arbitrary but stable)
         * - Used in HashMap, HashSet.
         */
        System.out.println("  TRUE.hashCode(): " + t.hashCode());   // 1231
        System.out.println("  FALSE.hashCode(): " + f.hashCode()); // 1237
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // 3. STATIC UTILITIES — Parsing, Strings, Logical Ops
    // ───────────────────────────────────────────────────────────────────────────────────
    private static void demoStaticUtilities() {
        System.out.println("\n🔹 3. Static Utilities — Robust, Null-Safe, Functional");

        /* 
         * 📌 parseBoolean(String s)
         * - Returns primitive boolean (no object!).
         * - Returns false for: null, "", "false", "FALSE", etc.
         * - Only "true" (case-insensitive) → true.
         * ✅ Use when you need boolean, not Boolean (e.g., config flags).
         */
        System.out.println("  parseBoolean(\"TRUE\"): " + Boolean.parseBoolean("TRUE"));   // true
        System.out.println("  parseBoolean(\"yes\"): " + Boolean.parseBoolean("yes"));    // false
        System.out.println("  parseBoolean(null): " + Boolean.parseBoolean(null));       // false

        /* 
         * 📌 valueOf(String s)
         * - Returns Boolean object (cached TRUE/FALSE).
         * - Same rules as parseBoolean, but returns object.
         * - null or non-"true" → Boolean.FALSE (never null!).
         * ✅ Safer than new Boolean(s) — no null, no allocation beyond cache.
         */
        System.out.println("  valueOf(\"True\"): " + Boolean.valueOf("True"));   // TRUE
        System.out.println("  valueOf(\"no\"): " + Boolean.valueOf("no"));      // FALSE
        System.out.println("  valueOf(null): " + Boolean.valueOf(null));       // FALSE (not null!)

        /* 
         * 📌 toString(boolean b) / toString()
         * - toString(true) → "true", toString(false) → "false"
         * - Never returns null.
         */
        System.out.println("  toString(true): \"" + Boolean.toString(true) + "\"");
        System.out.println("  TRUE.toString(): \"" + Boolean.TRUE.toString() + "\"");

        /* 
         * 📌 logicalAnd / logicalOr / logicalXor (Java 1.8+)
         * - Static, pure functions — no boxing, no null risk.
         * - Equivalent to &&, ||, ^ but usable in streams/lambdas.
         * ✅ Use in functional pipelines (e.g., reduce, filter).
         */
        boolean a = true, b = false;
        System.out.println("  logicalAnd(true, false): " + Boolean.logicalAnd(a, b)); // false
        System.out.println("  logicalOr(true, false): " + Boolean.logicalOr(a, b));   // true
        System.out.println("  logicalXor(true, false): " + Boolean.logicalXor(a, b)); // true

        // Example: Check if all flags are true
        List<Boolean> flags = Arrays.asList(true, true, false);
        boolean allTrue = flags.stream().reduce(true, Boolean::logicalAnd);
        System.out.println("  All true? " + allTrue); // false
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // 4. SYSTEM PROPERTIES — The Hidden Time Bomb
    // ───────────────────────────────────────────────────────────────────────────────────
    private static void demoSystemProperties() {
        System.out.println("\n🔹 4. System Properties — Feature Flags Done Right (and Wrong)");

        /* 
         * 📌 getBoolean(String name)
         * - Reads system property: System.getProperty(name)
         * - Returns true ONLY if property exists AND equals "true" (case-insensitive).
         * - Returns false for: missing, null, "TRUE", "yes", "1", etc.
         * ❗ Common mistake: assuming "1" → true.
         */
        // Set a property for demo
        System.setProperty("feature.new-ui", "true");
        System.setProperty("feature.dark-mode", "TRUE");  // won't work!
        System.setProperty("feature.logging", "1");       // won't work!

        System.out.println("  getBoolean(\"feature.new-ui\"): " 
            + Boolean.getBoolean("feature.new-ui"));     // true ✅
        System.out.println("  getBoolean(\"feature.dark-mode\"): " 
            + Boolean.getBoolean("feature.dark-mode")); // false ❌
        System.out.println("  getBoolean(\"feature.logging\"): " 
            + Boolean.getBoolean("feature.logging"));   // false ❌
        System.out.println("  getBoolean(\"missing\"): " 
            + Boolean.getBoolean("missing"));           // false

        // ✅ Best practice: Use parseBoolean for flexible configs
        String darkMode = System.getProperty("feature.dark-mode");
        boolean darkModeEnabled = Boolean.parseBoolean(darkMode); // "TRUE" → true!
        System.out.println("  parseBoolean(dark-mode prop): " + darkModeEnabled); // true
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // 5. BEST PRACTICES — What Senior Engineers Know
    // ───────────────────────────────────────────────────────────────────────────────────
    private static void printBestPractices() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🎯 BOOLEAN BEST PRACTICES — Avoiding the Null Trap");
        System.out.println("=".repeat(70));

        System.out.println("✅ DO:");
        System.out.println(" • Use Boolean.valueOf(x) — never 'new Boolean(x)'");
        System.out.println(" • Prefer primitives (boolean) unless null is meaningful");
        System.out.println(" • Use Optional<Boolean> for nullable flags (explicit intent)");
        System.out.println(" • Use parseBoolean() for config strings — more permissive");
        System.out.println(" • Use logicalXxx() in streams — clean, no boxing");

        System.out.println("\n❌ AVOID:");
        System.out.println(" • == on boxed Booleans (TRUE == true → true, but null == false → false!)");
        System.out.println(" • Boolean.getBoolean() for non-standard truthy values");
        System.out.println(" • Storing Boolean in collections when boolean[] suffices");
        System.out.println(" • Assuming 'null Boolean' means 'false' — it means 'unknown'!");

        System.out.println("\n💡 Pro Pattern: Three-Valued Logic");
        System.out.println("   enum TriState { TRUE, FALSE, UNKNOWN }");
        System.out.println("   // Safer than Boolean when null has semantic meaning");
    }
}
