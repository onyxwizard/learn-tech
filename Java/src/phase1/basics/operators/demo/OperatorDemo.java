package phase1.basics.operators.demo;

import phase1.basics.operators.operations.*;

public class OperatorDemo {
    public static void main(String[] args) {
        int x = 10, y = 3;
        ArithmeticLab arithmetic = new ArithmeticLab();
        System.out.println("🔢 ARITHMETIC OPERATORS DEMO");
        System.out.println("=".repeat(40));
        System.out.println("Operands: x = " + x + ", y = " + y);
        System.out.println();

        System.out.println("Addition (x + y): " + arithmetic.add(x, y));
        System.out.println("Subtraction (x - y): " + arithmetic.subtract(x, y));
        System.out.println("Multiplication (x * y): " + arithmetic.multiply(x, y));
        System.out.println("Division (x / y): " + arithmetic.divide(x, y));
        System.out.println("Remainder (x % y): " + arithmetic.remainder(x, y));
        System.out.println();

        // String concatenation
        String result = arithmetic.concatenate("Hello", " World!");
        System.out.println("String Concatenation: \"" + result + "\"");
        System.out.println();

        System.out.println("🖋️  ASSIGNMENT OPERATORS DEMO");
        System.out.println("=".repeat(40));
        AssignmentLab assignment = new AssignmentLab();
        x = 10;
        System.out.println("Initial value: x = " + x);
        System.out.println();

        // Simple assignment (via method)
        y = assignment.assign(20);
        System.out.println("Simple assignment: y = assign(20) → y = " + y);

        // Compound assignments
        System.out.println("\nCompound assignments (starting with x = 10):");
        System.out.println("x += 5  → " + assignment.addAssign(10, 5));
        System.out.println("x -= 3  → " + assignment.subtractAssign(10, 3));
        System.out.println("x *= 2  → " + assignment.multiplyAssign(10, 2));
        System.out.println("x /= 4  → " + assignment.divideAssign(10, 4));
        System.out.println("x %= 3  → " + assignment.remainderAssign(10, 3));
        System.out.println();

        // Show that compound assignment modifies the variable
        int z = 8;
        System.out.println("Before: z = " + z);
        z += 2; // modifies z directly
        System.out.println("After z += 2: z = " + z);
        System.out.println();

        // ----------------------------------------------------------------------------
        System.out.println("🔁 UNARY OPERATORS DEMO");
        System.out.println("=".repeat(40));
        UnaryLab unary = new UnaryLab();
        x = 10;
        boolean flag = false;

        System.out.println("Original values: x = " + x + ", flag = " + flag);
        System.out.println();

        // Unary plus/minus
        System.out.println("Unary plus (+x): " + unary.unaryPlus(x));   // 10
        System.out.println("Unary minus (-x): " + unary.unaryMinus(x)); // -10

        // Logical NOT
        System.out.println("Logical NOT (!flag): " + unary.logicalNot(flag)); // true
        System.out.println();

        // Increment/decrement demo
        unary.demonstrateIncrementDecrement();


        //-------------------------------------------------------------------------
        System.out.println("🔗 LOGICAL OPERATORS DEMO");
        System.out.println("=".repeat(40));
        LogicalLab logical = new LogicalLab();
        // Basic examples
        boolean sunny = true;
        boolean weekend = false;

        System.out.println("Example: sunny = " + sunny + ", weekend = " + weekend);
        System.out.println("Go to beach? (sunny && weekend): " + logical.and(sunny, weekend));
        System.out.println("Stay home? (!sunny || !weekend): " + logical.or(!sunny, !weekend));
        System.out.println();

        logical.demonstrateTruthTables();
        logical.demonstrateShortCircuit();

        //-------------------------------------------------------------------------
        System.out.println("❓ TERNARY OPERATOR DEMO");
        System.out.println("=".repeat(40));
        TernaryLab ternary = new TernaryLab();
        // Basic examples
        System.out.println("Max of 15 and 25: " + ternary.max(15, 25));
        System.out.println("Status for score 75: " + ternary.getStatus(75));
        System.out.println("Status for score 45: " + ternary.getStatus(45));
        System.out.println("Weather (raining): " + ternary.getWeatherMessage(true));
        System.out.println("Weather (sunny): " + ternary.getWeatherMessage(false));
        System.out.println("Safe length (null): " + ternary.safeStringLength(null));
        System.out.println("Safe length (\"Hello\"): " + ternary.safeStringLength("Hello"));
        System.out.println();

        ternary.demonstrateTernaryVsIfElse();
        ternary.demonstrateNestedTernary();

        System.out.println("💡 Best Practice: Use ternary for SIMPLE assignments.\n" +
                "   Avoid complex/nested logic — prefer if-else for readability!");

        // -------------------------------------------------------------------------
        System.out.println("⚖️  RELATIONAL & EQUALITY OPERATORS DEMO");
        System.out.println("=".repeat(50));
        RelationalAndEqualityLab relational = new RelationalAndEqualityLab();

        relational.demonstratePrimitives();
        relational.demonstrateObjects();
        relational.demonstratePitfalls();

        System.out.println("💡 Key Rules:\n" +
                "   • Use ==/!= for primitives\n" +
                "   • Use .equals() for object VALUE comparison\n" +
                "   • Never use == for String/content comparison\n" +
                "   • Avoid == for floating-point numbers");

    }
}
