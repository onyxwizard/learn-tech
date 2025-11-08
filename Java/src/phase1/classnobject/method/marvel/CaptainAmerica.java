package phase1.classnobject.method.marvel;


public class CaptainAmerica extends Superhero {

    public CaptainAmerica() {
        super("Captain America", 85);
    }

    // 🛡️ Overloaded: throw shield (default)
    public void throwShield() {
        System.out.println("🛡️ " + name + " throws his shield!");
        energy -= 12;
    }

    // 🛡️ Overloaded: throw shield with bounce count
    public void throwShield(int bounces) {
        System.out.printf("🔄 %s's shield bounces %d times!\n", name, bounces);
        energy -= 15;
    }

    // 🛡️ Overloaded: throw shield at enemy
    public void throwShield(String enemy) {
        System.out.printf("🎯 %s hits %s with his shield!\n", name, enemy);
        energy -= 18;
    }

    @Override
    public void usePower() {
        throwShield();
    }
}