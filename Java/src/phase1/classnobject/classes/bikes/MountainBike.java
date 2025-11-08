package phase1.classnobject.classes.bikes;

public class MountainBike extends Bicycle {
    private int seatHeight; // extra field

    public MountainBike(int seatHeight, int cadence, int speed, int gear) {
        super(cadence, speed, gear); // call parent constructor
        this.seatHeight = seatHeight;
    }

    public void setSeatHeight(int height) {
        this.seatHeight = height;
    }

    public int getSeatHeight() {
        return seatHeight;
    }

    @Override
    public String toString() {
        return "MountainBike{" +
                "seatHeight=" + seatHeight + ", " +
                super.toString().replace("Bicycle", "") +
                "}";
    }
}