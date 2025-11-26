package phase1.lambdaexpression.functionalinterface;


import java.util.function.*;
// ─────────────────────────────────────────────────────
// ✅ VALID Functional Interface (1 abstract method + extras)
// ─────────────────────────────────────────────────────
@FunctionalInterface  // 🔒 Compiler-enforced safety
interface UserProfilePrinter {
    // 🧩 1. Abstract method (SAM — required for lambdas)
    void print();

    // 🛠️ 2. Default method (non-abstract — allowed ✅)
    default void showDetails(String name, int age) {
        System.out.println("🧑 User: " + name + " | 🎂 Age: " + age);
    }

    // 📦 3. Static utility method (allowed ✅)
    static void printBinaryInverse(int bit) {
        int inverted = (bit == 0 || bit == 1) ? 1 - bit : 0;
        System.out.println("🔄 Binary inverse of " + bit + " = " + inverted);
    }
}

// ─────────────────────────────────────────────────────
// ❌ INVALID: Not a functional interface (3 abstract methods)
// ─────────────────────────────────────────────────────
// interface Test {
//     void out(String s);
//     void app(String s, String x);
//     void pp(int s);
//     // ❌ Compile error if used with lambda!
// }

// ─────────────────────────────────────────────────────
// 🚀 Main Demo Class
// ─────────────────────────────────────────────────────
public class FunctionalLambdaInterface {
    public static void main(String[] args) {

        // ─── 1. Lambda for abstract method ────────────────
        UserProfilePrinter printer = () -> System.out.println("🖨️  Lambda executed!");
        printer.print();

        // ─── 2. Call default method ───────────────────────
        printer.showDetails("AK", 28);  // 🧑 User: Alex | 🎂 Age: 28

        // ─── 3. Call static method ────────────────────────
        UserProfilePrinter.printBinaryInverse(1);  // 🔄 Binary inverse of 1 = 0

        // ─── 4. Bonus: Real-world equivalent (JDK style) ──
        Runnable task = () -> System.out.println("✅ Using Runnable");
        task.run();
    }
}