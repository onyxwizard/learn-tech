package phase1.basics.operators.operations;

import phase1.basics.operators.models.*;

public class InstanceofLab {

    // Basic instanceof check
    public boolean isAnimal(Object obj) {
        return obj instanceof Animal;
    }

    // Check for specific subclass
    public boolean isDog(Object obj) {
        return obj instanceof Dog;
    }

    // Check for interface
    public boolean isFlyable(Object obj) {
        return obj instanceof Flyable;
    }

    // 🧪 Demonstrate inheritance hierarchy
    public void demonstrateInheritance() {
        System.out.println("🐾 Inheritance Hierarchy Checks:");
        Animal animal = new Animal();
        Dog dog = new Dog();
        Cat cat = new Cat();
        Object obj = new Object();

        System.out.println("new Animal() instanceof Animal: " + (animal instanceof Animal)); // true
        System.out.println("new Dog() instanceof Animal: " + (dog instanceof Animal));       // true (inheritance)
        System.out.println("new Dog() instanceof Dog: " + (dog instanceof Dog));             // true
//uncomment you will see the error
//        System.out.println("new Cat() instanceof Dog: " + (cat instanceof Dog));             // false
        System.out.println("new Object() instanceof Animal: " + (obj instanceof Animal));    // false
        System.out.println();
    }

    // 🧪 Demonstrate interface implementation
    public void demonstrateInterface() {
        System.out.println("✈️ Interface Implementation Checks:");
        Dog dog = new Dog();
        Cat cat = new Cat();

        System.out.println("new Dog() instanceof Flyable: " + (dog instanceof Flyable)); // true
        System.out.println("new Cat() instanceof Flyable: " + (cat instanceof Flyable)); // false
        System.out.println();
    }

    // 🧪 Demonstrate null behavior
    public void demonstrateNull() {
        System.out.println("🚫 Null Behavior:");
        Animal nullAnimal = null;
        System.out.println("null instanceof Animal: " + (nullAnimal instanceof Animal)); // false (never throws!)
        System.out.println();
    }

    // 🧪 Pattern Matching (Java 14+) - Bonus!
    public void demonstratePatternMatching() {
        System.out.println("🆕 Pattern Matching (Java 14+):");
        Animal animal = new Dog();

        // Old way
        if (animal instanceof Dog) {
            Dog d = (Dog) animal;
            d.makeSound();
        }

        // New way (Java 14+)
        if (animal instanceof Dog d) {
            d.makeSound(); // d is in scope and already cast!
        }
        System.out.println();
    }

    // 🧪 Run full demo
    public void runDemo() {
        System.out.println("🔍 INSTANCEOF OPERATOR DEMO");
        System.out.println("=".repeat(45));

        demonstrateInheritance();
        demonstrateInterface();
        demonstrateNull();
        demonstratePatternMatching();

        System.out.println("💡 Key Rules:\n" +
                "   • instanceof returns false for null\n" +
                "   • Works with classes, subclasses, and interfaces\n" +
                "   • Use for safe casting (especially with pattern matching)\n" +
                "   • Always prefer instanceof over getClass() for type checks");
    }
}