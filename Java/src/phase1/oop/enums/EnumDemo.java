package phase1.oop.enums;


// File: Color.java (or inside same class — see note below)
enum Color {
    RED, GREEN, BLUE
}

public class EnumDemo {
    public static void main(String[] args) {
        Color c1 = Color.RED;
        Color c2 = Color.BLUE;

        // 1. name() → exact declared name (String)
        System.out.println("1. name(): " + c1.name());        // → RED

        // 2. toString() → by default, same as name()
        System.out.println("2. toString(): " + c1);           // → RED

        // 3. ordinal() → position (starts at 0)
        System.out.println("3. ordinal(): " + c1.ordinal());  // → 0

        // 4. compareTo() → compares ordinals
        System.out.println("4. compareTo(): " + c1.compareTo(c2)); // → -2 (0 - 2)

        // 5. equals() → true if same constant
        System.out.println("5. equals(): " + c1.equals(Color.RED)); // → true

        // 6. valueOf() → get enum from String (case-sensitive!)
        Color fromString = Color.valueOf("GREEN");
        System.out.println("6. valueOf(): " + fromString);    // → GREEN

        // 7. getDeclaringClass() → returns Color.class
        System.out.println("7. declaring class: " + c1.getDeclaringClass().getSimpleName()); // → Color

        // 8. hashCode() → consistent for same enum
        System.out.println("8. hashCode(): " + c1.hashCode()); // → some int (same for all RED)
    }
}