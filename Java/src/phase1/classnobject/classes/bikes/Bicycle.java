package phase1.classnobject.classes.bikes;

public class Bicycle {
    // 🔒 Private fields (encapsulation)
    private int cadence;
    private int gear;
    private int speed;

    // Constructor
    public Bicycle(int cadence, int speed, int gear) {
        this.cadence = cadence;
        this.speed = speed;
        this.gear = gear;
    }

    // Getters
    public int getCadence() { return cadence; }
    public int getGear() { return gear; }
    public int getSpeed() { return speed; }

    // Setters (with basic validation)
    public void setCadence(int cadence) { this.cadence = cadence; }
    public void setGear(int gear) { this.gear = gear; }

    // Behavior methods
    public void speedUp(int increment) {
        speed += increment;
    }

    public void applyBrake(int decrement) {
        speed = Math.max(0, speed - decrement); // Can't go below 0!
    }

    // For easy printing
    @Override
    public String toString() {
        return "Bicycle{cadence=" + cadence + ", gear=" + gear + ", speed=" + speed + "}";
    }
}