package phase1.classnobject.classes.bikes;


public class RoadBike extends Bicycle {
    private int tirePressure; // PSI

    public RoadBike(int tirePressure, int cadence, int speed, int gear) {
        super(cadence, speed, gear);
        this.tirePressure = tirePressure;
    }

    public void inflateTires(int psi) {
        tirePressure += psi;
    }

    public int getTirePressure() {
        return tirePressure;
    }

    @Override
    public String toString() {
        return "RoadBike{tirePressure=" + tirePressure + " PSI, " +
                super.toString().replace("Bicycle", "") +
                "}";
    }
}