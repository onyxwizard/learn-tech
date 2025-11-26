package phase1.lambdaexpression.lambdainterface;

import java.util.function.*;
// ─────────────────────────────────────────────────────
// Functional Interfaces (Single Abstract Method - SAM)
// ─────────────────────────────────────────────────────

@FunctionalInterface
interface Printer {
    void print();
}

@FunctionalInterface
interface NameConsumer {
    void accept(String name);
}

@FunctionalInterface
interface MathOperation {
    void execute(int a, int b);
}

// ─────────────────────────────────────────────────────
// Main Class: Lambda Expression Demonstrations
// ─────────────────────────────────────────────────────
public class LambdaInterfaceDemo {

    public static void main(String[] args) {

        // ─────────────────────────────────────────────────
        // 1. No Parameters → Single Statement
        // ─────────────────────────────────────────────────
        Printer printer = () -> System.out.println("✅ Hello from Lambda!");
        printer.print();

        // ─────────────────────────────────────────────────
        // 2. One Parameter → Type Inferred, Expression Body
        // ─────────────────────────────────────────────────
        NameConsumer greeter = name -> System.out.println("👋 Hello, " + name + "!");
        greeter.accept("AK");

        // ─────────────────────────────────────────────────
        // 3. Two Parameters → Inferred Types, Expression Body
        // ─────────────────────────────────────────────────
        MathOperation adder = (x, y) -> System.out.println("🔢 " + x + " + " + y + " = " + (x + y));
        adder.execute(10, 5);

        // ─────────────────────────────────────────────────
        // 4. Bonus: Block Body with Logic
        // ─────────────────────────────────────────────────
        MathOperation multiplier = (a, b) -> {
            int result = a * b;
            System.out.println("✖️  " + a + " × " + b + " = " + result);
        };
        multiplier.execute(4, 7);

        // ─────────────────────────────────────────────────
        // 5. Real-World Equivalent: Standard Functional Interfaces
        // ─────────────────────────────────────────────────
        // Instead of custom interfaces, prefer JDK built-ins:
        //   Runnable        → () -> { ... }
        //   Consumer<T>     → (T t) -> { ... }
        //   BiConsumer<T,U> → (T t, U u) -> { ... }

        Runnable task = () -> System.out.println("⚙️  Using Runnable");
        Consumer<String> logger = msg -> System.out.println("📝 " + msg);
        BiConsumer<Integer, Integer> summer = (x, y) -> 
            System.out.println("➕ " + x + " + " + y + " = " + (x + y));

        task.run();
        logger.accept("Standard interfaces are preferred!");
        summer.accept(3, 9);
    }
}