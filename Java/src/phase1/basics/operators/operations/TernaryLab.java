package phase1.basics.operators.operations;

public class TernaryLab {
    // Basic ternary: return max of two numbers
    public int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Ternary for string assignment
    public String getStatus(int score) {
        return (score >= 60) ? "Pass" : "Fail";
    }

    // Ternary for boolean logic
    public String getWeatherMessage(boolean isRaining) {
        return isRaining ? "Take an umbrella!" : "Enjoy the sun!";
    }

    // Ternary with method calls (safe due to short-circuiting)
    public String safeStringLength(String str) {
        // Only calls .length() if str is not null
        return (str != null) ? "Length: " + str.length() : "Null string";
    }

    // 🧪 Demonstrate ternary vs if-else
    public void demonstrateTernaryVsIfElse() {
        System.out.println("🔄 Ternary vs If-Else:");

        int x = 10, y = 20;

        // Using ternary
        int maxTernary = (x > y) ? x : y;
        System.out.println("Ternary: max = " + maxTernary);

        // Equivalent if-else
        int maxIfElse;
        if (x > y) {
            maxIfElse = x;
        } else {
            maxIfElse = y;
        }
        System.out.println("If-Else: max = " + maxIfElse);
        System.out.println();
    }

    // 🧪 Show nested ternary (use sparingly!)
    public void demonstrateNestedTernary() {
        System.out.println("⚠️ Nested Ternary (use carefully!):");
        int score = 85;
        // Readable version:
        String grade = (score >= 90) ? "A" :
                (score >= 80) ? "B" :
                        (score >= 70) ? "C" : "F";
        System.out.println("Score " + score + " → Grade: " + grade);
        System.out.println();
    }

}
