package phase1.basics.operators.operations;

public class RelationalAndEqualityLab {

    // Relational operators (return boolean)
    public boolean lessThan(int a, int b) { return a < b; }
    public boolean greaterThan(int a, int b) { return a > b; }
    public boolean lessThanOrEqual(int a, int b) { return a <= b; }
    public boolean greaterThanOrEqual(int a, int b) { return a >= b; }

    // Equality operators for primitives
    public boolean equal(int a, int b) { return a == b; }
    public boolean notEqual(int a, int b) { return a != b; }

    // 🧪 Demonstrate primitive comparison
    public void demonstratePrimitives() {
        System.out.println("🔢 Primitive Comparison (int):");
        int x = 10, y = 20;
        System.out.println("x = " + x + ", y = " + y);
        System.out.println("x < y: " + lessThan(x, y));
        System.out.println("x > y: " + greaterThan(x, y));
        System.out.println("x <= y: " + lessThanOrEqual(x, y));
        System.out.println("x >= y: " + greaterThanOrEqual(x, y));
        System.out.println("x == y: " + equal(x, y));
        System.out.println("x != y: " + notEqual(x, y));
        System.out.println();
    }

    // 🧪 Demonstrate object comparison (String)
    public void demonstrateObjects() {
        System.out.println("🔤 Object Comparison (String):");

        // String literals (from string pool)
        String s1 = "hello";
        String s2 = "hello";
        System.out.println("s1 = \"hello\", s2 = \"hello\"");
        System.out.println("s1 == s2 (reference): " + (s1 == s2)); // true (same pool)
        System.out.println("s1.equals(s2) (value): " + s1.equals(s2)); // true

        // New String objects
        String s3 = new String("hello");
        String s4 = new String("hello");
        System.out.println("\ns3 = new String(\"hello\"), s4 = new String(\"hello\")");
        System.out.println("s3 == s4 (reference): " + (s3 == s4)); // false (different objects)
        System.out.println("s3.equals(s4) (value): " + s3.equals(s4)); // true

        // Integer objects (auto-boxing)
        Integer i1 = 127;
        Integer i2 = 127;
        Integer i3 = 128;
        Integer i4 = 128;
        System.out.println("\nInteger caching (≤127):");
        System.out.println("i1 = 127, i2 = 127 → i1 == i2: " + (i1 == i2)); // true (cached)
        System.out.println("i3 = 128, i4 = 128 → i3 == i4: " + (i3 == i4)); // false (not cached)
        System.out.println("i3.equals(i4): " + i3.equals(i4)); // true
        System.out.println();
    }

    // 🧪 Common pitfalls
    public void demonstratePitfalls() {
        System.out.println("⚠️ Common Pitfalls:");

        // Floating-point comparison
        double d1 = 0.1 + 0.2;
        double d2 = 0.3;
        System.out.println("0.1 + 0.2 == 0.3? → " + (d1 == d2)); // false!
        System.out.println("Use epsilon for float comparison!");
        System.out.println();
    }

}
