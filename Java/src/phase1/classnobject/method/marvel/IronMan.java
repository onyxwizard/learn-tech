package phase1.classnobject.method.marvel;


public class IronMan extends Superhero {
    private int arcReactor = 100;

    public IronMan() {
        super("Iron Man", 90);
    }

    // 🔥 Overloaded: fire repulsors (default)
    public void fire() {
        fire(20); // default damage
    }

    // 🔥 Overloaded: fire with custom power
    public void fire(int damage) {
        if (arcReactor >= 10 && energy >= 15) {
            System.out.printf("💥 %s fires repulsors! Damage: %d\n", name, damage);
            energy -= 15;
            arcReactor -= 10;
        } else {
            System.out.println("⚠️ " + name + "'s suit is low on power!");
        }
    }

    // 🛡️ Overloaded: fire at coordinates
    public void fire(int x, int y) {
        System.out.printf("📍 %s targets (%d, %d)!\n", name, x, y);
        fire(25); // stronger shot
    }

    @Override
    public void usePower() {
        fire();
    }
}