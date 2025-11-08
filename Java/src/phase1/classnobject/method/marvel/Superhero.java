package phase1.classnobject.method.marvel;


public abstract class Superhero {
    protected String name;
    protected int energy;

    public Superhero(String name, int energy) {
        this.name = name;
        this.energy = energy;
    }

    // Basic method: all heroes can train
    public void train() {
        energy = Math.min(100, energy + 10);
        System.out.println(name + " trained! Energy: " + energy + "%");
    }

    // Abstract method: each hero has a unique power
    public abstract void usePower();

    public String getName() { return name; }
    public int getEnergy() { return energy; }
}