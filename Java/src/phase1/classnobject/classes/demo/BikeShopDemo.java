package phase1.classnobject.classes.demo;

import phase1.classnobject.classes.bikes.*;

public class BikeShopDemo {
    public static void main(String[] args) {
        System.out.println("🚲 WELCOME TO BIKE SHOP SIMULATOR\n");

        // Create different bikes
        var mountain = new MountainBike(80, 60, 10, 3);
        var road = new RoadBike(100, 90, 15, 5);

        // Test base behavior (inherited)
        System.out.println("Before riding:");
        System.out.println(mountain);
        System.out.println(road);

        // Ride the mountain bike
        mountain.speedUp(5);
        mountain.setSeatHeight(85);
        mountain.applyBrake(2);

        // Ride the road bike
        road.speedUp(10);
        road.inflateTires(5);
        road.setGear(7);

        System.out.println("\nAfter riding:");
        System.out.println(mountain);
        System.out.println(road);

        // Demonstrate encapsulation: can't access fields directly!
        // mountain.speed = 100; // ❌ COMPILE ERROR! (if fields were private)
        // Instead, we use methods:
        System.out.println("\n✅ Encapsulation works: speed controlled via methods!");
    }
}