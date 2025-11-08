// phase1.java.chapter1/fleet/model/Car.java
package phase1.oop.fleet.model;

public class Car extends Vehicle {
    int numDoors; // ✅ Only new field

    public Car(String brand) {
        super(brand); // ✅ Initializes Vehicle.brand
    }

    public void setDoor(int count) {
        this.numDoors = count;
    }

    public String getDoor() {
        return String.format("The Door Count is %d", numDoors);
    }

    @Override
    public String getStatus() {
        if (isRunning) {
            return String.format("%s Car with Door Count is %d, running at %d", brand, numDoors, speed);
        } else {
            return String.format("%s with Door Count is %d, Stopped at %d", brand, numDoors, speed);
        }
    }
}