package phase1.basics.operators.operations;

public class UnaryLab {
    // Unary plus (rarely used — just returns the value)
    public int unaryPlus(int a) {
        return +a;
    }

    // Unary minus (negation)
    public int unaryMinus(int a) {
        return -a;
    }

    // Logical NOT
    public boolean logicalNot(boolean flag) {
        return !flag;
    }

    // 🧪 Demonstrate prefix vs postfix increment/decrement
    public void demonstrateIncrementDecrement() {
        System.out.println("🔄 Increment/Decrement Behavior:");

        // Postfix: use then increment
        int a = 5;
        int result1 = a++; // result1 = 5, then a = 6
        System.out.println("Postfix: int result = a++; → result = " + result1 + ", a = " + a);

        // Prefix: increment then use
        int b = 5;
        int result2 = ++b; // b = 6, then result2 = 6
        System.out.println("Prefix:  int result = ++b; → result = " + result2 + ", b = " + b);

        // Postfix decrement
        int c = 5;
        int result3 = c--; // result3 = 5, then c = 4
        System.out.println("Postfix: int result = c--; → result = " + result3 + ", c = " + c);

        // Prefix decrement
        int d = 5;
        int result4 = --d; // d = 4, then result4 = 4
        System.out.println("Prefix:  int result = --d; → result = " + result4 + ", d = " + d);
        System.out.println();
    }
}
