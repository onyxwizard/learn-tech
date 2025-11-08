package phase1.basics.operators.models;

public class Dog extends Animal implements Flyable {
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }

    @Override
    public void fly() {
        System.out.println("Dog flying with jetpack!");
    }
}