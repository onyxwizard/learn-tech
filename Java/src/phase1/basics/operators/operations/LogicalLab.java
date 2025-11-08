package phase1.basics.operators.operations;

public class LogicalLab {
    // Conditional AND (&&) - short-circuits if left is false
    public boolean and(boolean a, boolean b) {
        return a && b;
    }

    // Conditional OR (||) - short-circuits if left is true
    public boolean or(boolean a, boolean b) {
        return a || b;
    }

    // 🧪 Demonstrate short-circuiting with side effects
    public void demonstrateShortCircuit() {
        System.out.println("⚡ Short-Circuit Behavior Demo:");

        // AND: right side NOT evaluated if left is false
        System.out.print("false && (sideEffect()): ");
        boolean result1 = false && sideEffect(); // sideEffect() NOT called
        System.out.println(result1);

        // AND: right side IS evaluated if left is true
        System.out.print("true && (sideEffect()): ");
        boolean result2 = true && sideEffect(); // sideEffect() IS called
        System.out.println(result2);

        System.out.println();

        // OR: right side NOT evaluated if left is true
        System.out.print("true || (sideEffect()): ");
        boolean result3 = true || sideEffect(); // sideEffect() NOT called
        System.out.println(result3);

        // OR: right side IS evaluated if left is false
        System.out.print("false || (sideEffect()): ");
        boolean result4 = false || sideEffect(); // sideEffect() IS called
        System.out.println(result4);
        System.out.println();
    }

    // Helper method to show if it's called
    private boolean sideEffect() {
        System.out.print("[SIDE EFFECT EXECUTED] ");
        return true;
    }

    // 🧪 Show truth tables
    public void demonstrateTruthTables() {
        System.out.println("📋 Truth Tables:");
        boolean[] values = {true, false};

        System.out.println("AND (&&):");
        for (boolean a : values) {
            for (boolean b : values) {
                System.out.printf("  %s && %s = %s%n", a, b, and(a, b));
            }
        }

        System.out.println("OR (||):");
        for (boolean a : values) {
            for (boolean b : values) {
                System.out.printf("  %s || %s = %s%n", a, b, or(a, b));
            }
        }
        System.out.println();
    }
}
