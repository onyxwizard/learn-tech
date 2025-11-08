package phase1.basics.operators.operations;

public class ArithmeticLab {

    // ➕ Addition
    public int add(int a, int b) {
        return a + b;
    }

    // ➖ Subtraction
    public int subtract(int a, int b) {
        return a - b;
    }

    // ✖️ Multiplication
    public int multiply(int a, int b) {
        return a * b;
    }

    // ➗ Division (integer)
    public int divide(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Division by zero!");
        return a / b;
    }

    // ➗ Remainder (Modulo)
    public int remainder(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Modulo by zero!");
        return a % b;
    }

    // 🔗 String Concatenation (using +)
    public String concatenate(String s1, String s2) {
        return s1 + s2;
    }

}