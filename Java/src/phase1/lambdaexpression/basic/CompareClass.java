package phase1.lambdaexpression.basic;

import java.util.Comparator;

public class CompareClass {

    public static void main(String[] args) {

        // ─────────────────────────────────────────────────────
        // 1. Pre-Lambda: Anonymous Class (Java 1–7 style)
        // ─────────────────────────────────────────────────────
        Comparator<String> stringCompare = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };

        System.out.println("=== Anonymous Class ===");
        System.out.println("hello vs Hello: " + stringCompare.compare("hello", "Hello")); // 32
        System.out.println("hello vs hello: " + stringCompare.compare("hello", "hello")); // 0
        System.out.println("Hello vs hello: " + stringCompare.compare("Hello", "hello")); // -32

        // ─────────────────────────────────────────────────────
        // 2. Lambda: Full Syntax (explicit types + block)
        // ─────────────────────────────────────────────────────
        Comparator<String> stringLambdaFull = (String o1, String o2) -> {
            return o1.compareTo(o2);
        };

        System.out.println("\n=== Lambda (Full Syntax) ===");
        System.out.println("null vs null: " + stringLambdaFull.compare("null", "null")); // 0

        // ─────────────────────────────────────────────────────
        // 3. Lambda: Simplified Syntax (inferred types + expression)
        // ─────────────────────────────────────────────────────
        Comparator<String> stringLambdaSimple = (o1, o2) -> o1.compareTo(o2);

        System.out.println("\n=== Lambda (Simplified) ===");
        System.out.println("null vs null: " + stringLambdaSimple.compare("null", "null")); // 0

        // ─────────────────────────────────────────────────────
        // 4. Most Idiomatic: Method Reference & Built-ins
        // ─────────────────────────────────────────────────────
        Comparator<String> methodRef = String::compareTo;
        Comparator<String> natural = Comparator.naturalOrder();

        System.out.println("\n=== Modern Java (Recommended) ===");
        System.out.println("Method ref: " + methodRef.compare("A", "B"));      // -1
        System.out.println("Natural order: " + natural.compare("Z", "A"));    // 25
    }
}