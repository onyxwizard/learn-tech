// phase1.java.chapter1/fleet/model/Vehicle.java
package phase1.oop.fleet.model;

public class Vehicle implements Drivable {
    String brand;
    int speed = 0;
    boolean isRunning = false;

    public Vehicle() {}

    public Vehicle(String brand) {
        this.brand = brand;
    }

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
        speed = 0;
    }

    public void accelerate(int increment) {
        speed += increment;
    }

    public String getStatus() {
        if (isRunning) {
            return String.format("%s running at %d", brand, speed);
        } else {
            return String.format("%s Stopped at %d", brand, speed);
        }
    }
}