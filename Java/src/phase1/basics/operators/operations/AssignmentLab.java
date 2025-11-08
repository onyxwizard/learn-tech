package phase1.basics.operators.operations;

public class AssignmentLab {
    // Simple assignment (demonstrated via method return)
    public int assign(int value) {
        return value; // equivalent to: int x = value;
    }

    // Compound assignments — each returns the result
    public int addAssign(int a, int b) {
        a += b; // same as: a = a + b;
        return a;
    }

    public int subtractAssign(int a, int b) {
        a -= b; // same as: a = a - b;
        return a;
    }

    public int multiplyAssign(int a, int b) {
        a *= b; // same as: a = a * b;
        return a;
    }

    public int divideAssign(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Division by zero!");
        a /= b; // same as: a = a / b;
        return a;
    }

    public int remainderAssign(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Modulo by zero!");
        a %= b; // same as: a = a % b;
        return a;
    }
}
