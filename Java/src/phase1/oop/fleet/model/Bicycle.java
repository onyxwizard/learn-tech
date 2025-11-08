// phase1.java.chapter1/fleet/model/Bicycle.java
package phase1.oop.fleet.model;

public class Bicycle extends Vehicle {
    int numGears;

    public Bicycle(String brand, int numGears) {
        super(brand);
        this.numGears = numGears;
    }

    @Override
    public String getStatus() {
        if (isRunning) {
            return String.format("%s Bicycle (%d gears) running at %d", brand, numGears, speed);
        } else {
            return String.format("%s Bicycle (%d gears) Stopped at %d", brand, numGears, speed);
        }
    }
}