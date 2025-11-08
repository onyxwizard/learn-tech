package phase1.classnobject.method.demo;

import phase1.classnobject.method.marvel.*;

public class AvengersAcademy {
    public static void main(String[] args) {
        System.out.println("🦸‍♂️ WELCOME TO AVENGERS ACADEMY — METHOD TRAINING SIMULATOR\n");

        // Create heroes
        var spiderMan = new SpiderMan();
        var ironMan = new IronMan();
        var cap = new CaptainAmerica();

        Superhero[] heroes = {spiderMan, ironMan, cap};

        // Basic training
        for (var hero : heroes) {
            hero.train();
        }

        System.out.println("\n⚡ POWER DEMONSTRATION:\n");

        // Demonstrate overloaded methods
        spiderMan.shootWeb();              // default
        spiderMan.shootWeb(30);            // custom distance
        spiderMan.shootWeb("Green Goblin"); // targeted

        System.out.println();

        ironMan.fire();                    // default
        ironMan.fire(50);                  // high power
        ironMan.fire(100, 200);            // coordinates

        System.out.println();

        cap.throwShield();                 // default
        cap.throwShield(3);                // bounces
        cap.throwShield("Red Skull");      // enemy

        System.out.println("\n✅ All methods use proper overloading and naming!");
    }
}