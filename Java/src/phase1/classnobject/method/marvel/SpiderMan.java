package phase1.classnobject.method.marvel;



public class SpiderMan extends Superhero {
    private int webFluid = 100; // 0–100%

    public SpiderMan() {
        super("Spider-Man", 80);
    }

    // 🕸️ Overloaded: shoot web (default)
    public void shootWeb() {
        shootWeb(15); // default distance: 15 meters
    }

    // 🕸️ Overloaded: shoot web with distance
    public void shootWeb(int meters) {
        if (energy >= 10 && webFluid >= 5) {
            System.out.printf("🕷️ %s shot a web %d meters!\n", name, meters);
            energy -= 10;
            webFluid -= 5;
        } else {
            System.out.println("⚠️ " + name + " is too tired or out of web fluid!");
        }
    }

    // 🕸️ Overloaded: shoot web at a target
    public void shootWeb(String target) {
        System.out.printf("🎯 %s webs %s!\n", name, target);
        energy -= 8;
    }

    @Override
    public void usePower() {
        shootWeb(); // uses default
    }
}