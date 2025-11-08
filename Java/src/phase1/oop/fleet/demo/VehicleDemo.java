// phase1.java.chapter1/fleet/demo/VehicleDemo.java
package phase1.oop.fleet.demo;

import phase1.oop.fleet.model.*;


public class VehicleDemo {
    public static void main(String[] args) {
        Vehicle car1 = new Vehicle("Toyota");
        Vehicle car2 = new Vehicle("Ford");

        car1.start();
        car1.accelerate(10);
        car1.stop();
        car1.start();
        car1.accelerate(10);
        System.out.println(car1.getStatus());

        car2.start();
        car2.accelerate(50);
        System.out.println(car2.getStatus());

        Car c1 = new Car("Lambo");
        c1.setDoor(2);
        System.out.println(c1.getStatus());

        // Optional: Try Bicycle!
        Bicycle bike = new Bicycle("Trek", 21);
        bike.start();
        bike.accelerate(15);
        System.out.println(bike.getStatus());
    }
}