package phase1.built_in_functions.wrapper;

import java.util.*;

/**
 * Comprehensive Number Wrapper Demonstration
 * 
 * <p>This class illustrates:
 * <ul>
 *   <li>✅ Polymorphic handling via {@code Number} abstraction</li>
 *   <li>✅ Correct implementation of the 6 {@code Number} contract methods</li>
 *   <li>✅ Wrapper-specific static utilities ({@code valueOf}, {@code parseXxx}, {@code min/max})</li>
 *   <li>⚠️ Critical pitfalls: overflow, NaN, caching, signed zero, precision loss</li>
 *   <li>🎯 Guidance: when to use which wrapper in real systems</li>
 * </ul>
 * 
 * <p><b>Design Principle:</b>
 * "Program to the interface (`Number`), but know the implementation (wrapper)!"
 */
public class NumberWrapperComprehensiveDemo {

    public static void main(String[] args) {
        // ───────────────────────────────────────────────────────────────────────────────
        // 1. POLYMORPHIC LIST: Store diverse wrappers as Number
        // ───────────────────────────────────────────────────────────────────────────────
        List<Number> numbers = Arrays.asList(
            Integer.valueOf(100),        // int wrapper
            Long.valueOf(1_000_000_000L), // long wrapper
            Double.valueOf(3.14159),     // double wrapper
            Byte.valueOf((byte) 42),     // byte wrapper
            Float.valueOf(2.5f),         // float wrapper
            Short.valueOf((short) 300)   // short wrapper
        );

        System.out.println("=== Part 1: Polymorphic Number Contract (All Wrappers) ===");
        for (Number n : numbers) {
            demonstrateNumberContract(n);
        }

        // ───────────────────────────────────────────────────────────────────────────────
        // 2. WRAPPER-SPECIFIC UTILITIES & EDGE CASES
        // ───────────────────────────────────────────────────────────────────────────────
        System.out.println("\n=== Part 2: Wrapper-Specific Behavior & Pitfalls ===");

        // 🔹 Integer — caching, overflow, parsing
        demoInteger();

        // 🔹 Byte — full caching, overflow semantics
        demoByte();

        // 🔹 Long — large numbers, truncation to int
        demoLong();

        // 🔹 Double — NaN, infinity, signed zero, precision
        demoDouble();

        // 🔹 Float — precision limits, comparison gotchas
        demoFloat();

        // ───────────────────────────────────────────────────────────────────────────────
        // 3. REAL-WORLD DECISION GUIDE
        // ───────────────────────────────────────────────────────────────────────────────
        printDecisionGuide();
    }

    /**
     * Demonstrates the 6 core methods defined/required by {@code java.lang.Number}.
     * Shows how each wrapper implements them faithfully — with type-specific behavior.
     */
    private static void demonstrateNumberContract(Number n) {
        String type = n.getClass().getSimpleName();
        System.out.printf("\n[%s] Original: %s%n", type, n);

        // 1. byteValue() — narrowing (may wrap)
        byte byt = n.byteValue();
        System.out.printf("  byteValue(): %d (0x%02X)%n", byt, byt & 0xFF);

        // 2. shortValue() — narrowing
        short s = n.shortValue();
        System.out.printf("  shortValue(): %d%n", s);

        // 3. intValue() — truncating for floating-point
        int i = n.intValue();
        System.out.printf("  intValue(): %d%n", i);

        // 4. longValue() — widening (exact for integral types)
        long l = n.longValue();
        System.out.printf("  longValue(): %d%n", l);

        // 5. floatValue() — may lose precision
        float f = n.floatValue();
        System.out.printf("  floatValue(): %.6f%n", f);

        // 6. doubleValue() — most precise conversion
        double d = n.doubleValue();
        System.out.printf("  doubleValue(): %.10f%n", d);
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // INDIVIDUAL WRAPPER DEMOS — Showing unique behavior & gotchas
    // ───────────────────────────────────────────────────────────────────────────────────

    private static void demoInteger() {
        System.out.println("\n🔹 Integer — The Workhorse");

        // ✅ Caching in [-128, 127]
        Integer a = 100, b = 100, c = 200, d = 200;
        System.out.println("  Caching: 100 → same ref? " + (a == b)); // true
        System.out.println("  Caching: 200 → same ref? " + (c == d)); // false (JVM-dependent)

        // ❗ Overflow (silent!)
        int max = Integer.MAX_VALUE;
        System.out.println("  MAX_VALUE + 1 = " + (max + 1)); // → -2147483648

        // ✅ Safe parsing
        try {
            int parsed = Integer.parseInt("12345");
            System.out.println("  parseInt(\"12345\") = " + parsed);
        } catch (NumberFormatException e) {
            System.out.println("  Parsing failed: " + e.getMessage());
        }

        // ✅ min/max for clamping
        int clamped = Integer.max(0, Integer.min(100, 150)); // → 100
        System.out.println("  Clamp 150 to [0,100] → " + clamped);
    }

    private static void demoByte() {
        System.out.println("\n🔹 Byte — Full Caching & Wrap-Around");

        // ✅ ALL byte values are cached (range: -128 to 127)
        Byte x = 100, y = 100;
        System.out.println("  All Byte values cached? " + (x == y)); // true

        // ❗ Overflow wraps modulo 256
        byte b = 127;
        b++; // → -128
        System.out.println("  byte 127 + 1 = " + b);

        // ✅ Parsing with radix
        try{
            byte  hexx = Byte.parseByte("FF", 16); // 255 → but 255 > 127!
        } catch (NumberFormatException e) {
            // ⚠️ Throws NumberFormatException (255 not representable as byte)
            System.out.println("  parseByte(\"FF\",16) → " + e.getMessage());
        }
        try {
            Byte.parseByte("80", 16); // 128 → too big
        } catch (NumberFormatException e) {
            System.out.println("  parseByte(\"80\",16) → " + e.getMessage());
        }
    }

    private static void demoLong() {
        System.out.println("\n🔹 Long — Big Integers, Truncation Risks");

        // ✅ Exact for large integers
        long big = 9_223_372_036_854_775_807L;
        System.out.println("  Long.MAX_VALUE: " + big);

        // ❗ Truncation when converting to int
        long large = 3_000_000_000L;
        int truncated = (int) large; // or large.intValue()
        System.out.println("  3e9L → int: " + truncated); // → -1294967296 (silent!)

        // ✅ Safe parsing for timestamps
        long timestamp = Long.parseLong("1700000000000");
        System.out.println("  Parsed timestamp: " + timestamp);
    }

    private static void demoDouble() {
        System.out.println("\n🔹 Double — Precision, NaN, and Infinity");

        // ✅ High precision for integers up to 2⁵³
        double exact = Math.pow(2, 53); // 9007199254740992.0
        System.out.println("  2⁵³ is exact? " + (exact == exact + 1)); // false

        double inexact = Math.pow(2, 53) + 1;
        System.out.println("  2⁵³+1 == 2⁵³? " + (inexact == exact)); // true → precision lost!

        // ❗ NaN ≠ NaN (even to itself!)
        double nan = Double.NaN;
        System.out.println("  NaN == NaN? " + (nan == nan)); // false
        System.out.println("  Double.isNaN(NaN)? " + Double.isNaN(nan)); // true

        // ❗ Signed zero matters in some domains
        double posZero = 0.0;
        double negZero = -0.0;
        System.out.println("  +0.0 == -0.0? " + (posZero == negZero)); // true
        System.out.println("  1/+0.0 = " + (1.0 / posZero)); // Infinity
        System.out.println("  1/-0.0 = " + (1.0 / negZero)); // -Infinity

        // ✅ String conversion (avoids exponential notation for small numbers)
        System.out.println("  toString(1.0): \"" + Double.toString(1.0) + "\"");
        System.out.println("  toString(0.0001): \"" + Double.toString(0.0001) + "\""); // "1.0E-4"
    }

    private static void demoFloat() {
        System.out.println("\n🔹 Float — 32-Bit Tradeoffs");

        // ❗ Precision loss early
        float f = 16_777_217f; // 2²⁴ + 1
        System.out.println("  float 16777217 == 16777216? " + (f == 16_777_216f)); // true

        // ✅ Use for memory-constrained systems (e.g., OpenGL, ML model weights)
        float[] weights = new float[1_000_000]; // 4 MB vs 8 MB for double

        // ❗ Avoid == for equality testing — use epsilon
        float a = 0.1f + 0.2f;
        float b = 0.3f;
        System.out.println("  0.1f + 0.2f == 0.3f? " + (a == b)); // false!
        boolean close = Math.abs(a - b) < 1e-6f;
        System.out.println("  ...but close? " + close); // true
    }

    // ───────────────────────────────────────────────────────────────────────────────────
    // REAL-WORLD DECISION GUIDE — When to Use Which Wrapper
    // ───────────────────────────────────────────────────────────────────────────────────

    private static void printDecisionGuide() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🎯 REAL-WORLD WRAPPER SELECTION GUIDE");
        System.out.println("=".repeat(80));
        System.out.printf("%-10s | %-25s | %-35s%n", "Wrapper", "Use When...", "Avoid When...");
        System.out.println("-".repeat(80));

        System.out.printf("%-10s | %-25s | %-35s%n", 
            "Integer", 
            "Loop counters, array indices, IDs, config values", 
            "Numbers > 2B or need unsigned");
        System.out.printf("%-10s | %-25s | %-35s%n", 
            "Long", 
            "Timestamps, file sizes, counts > 2B", 
            "Memory-critical (2× int size)");
        System.out.printf("%-10s | %-25s | %-35s%n", 
            "Double", 
            "Scientific data, JSON numbers, ML", 
            "Financial calculations (use BigDecimal!)");
        System.out.printf("%-10s | %-25s | %-35s%n", 
            "Float", 
            "Graphics, audio, ML weights (GPU)", 
            "Precision-critical arithmetic");
        System.out.printf("%-10s | %-25s | %-35s%n", 
            "Byte", 
            "Network I/O, binary protocols, pixels", 
            "Arithmetic (promotes to int anyway)");
        System.out.printf("%-10s | %-25s | %-35s%n", 
            "Short", 
            "Memory-constrained arrays (rare)", 
            "Most cases — prefer int");

        System.out.println("\n💡 Pro Tips:");
        System.out.println(" • Always use valueOf() — never 'new Integer()'");
        System.out.println(" • For parsing, prefer parseInt/parseLong over valueOf when you need primitives");
        System.out.println(" • Use Math.min/Math.max for doubles/floats; wrapper.min/max for ints/longs");
        System.out.println(" • Never use == on boxed numbers — use equals() or compare primitives");
    }
}