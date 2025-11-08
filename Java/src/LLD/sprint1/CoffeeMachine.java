package LLD.sprint1;

enum CoffeeType {
    ESPRESSO(1, 0, 0),
    LATTE(1, 1, 0),
    CAPPUCCINO(1, 1, 1);

    private final int coffee, milk, foam;
    CoffeeType(int c, int m, int f) { coffee = c; milk = m; foam = f; }
    public int getCoffee() { return coffee; }
    public int getMilk() { return milk; }
    public int getFoam() { return foam; }
}

class CoffeeMachine {
    private int coffee = 5, milk = 3, foam = 2;

    public boolean brew(CoffeeType type) {
        if (coffee < type.getCoffee() ||
                milk < type.getMilk() ||
                foam < type.getFoam()) {
            return false;
        }
        coffee -= type.getCoffee();
        milk -= type.getMilk();
        foam -= type.getFoam();
        return true;
    }
}