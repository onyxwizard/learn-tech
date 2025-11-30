package phase1.optional;

import java.util.Optional;

// 🍎 Immutable domain object — no setters, no nulls
class Apple {
  private final boolean hasWorm;

  Apple(boolean hasWorm) {
    this.hasWorm = hasWorm;
  }

  // Business method: worm-free = safe to eat
  boolean isWormFree() {
    return !hasWorm;
  }

  // For debugging
  @Override
  public String toString() {
    return hasWorm ? "🍎🐛" : "🍎✓";
  }
}

// 🧺 Encapsulated basket — absence is explicit
class Basket {
  private final Optional<Apple> apple;

  // Private constructor — enforce safety
  private Basket(Optional<Apple> apple) {
    this.apple = apple;
  }

  // Factory: create basket with an apple
  public static Basket with(Apple apple) {
    return new Basket(Optional.of(apple)); // guaranteed non-null apple
  }

  // Factory: create empty basket
  public static Basket empty() {
    return new Basket(Optional.empty());
  }

  // ✅ OOP: basket answers about itself
  public boolean hasWormFreeApple() {
    return apple
        .map(Apple::isWormFree) // Apple → Boolean (true if worm-free)
        .orElse(false); // empty basket → false
    // 🔑 One line. No ifs. No nulls. No exceptions.
  }

  // Safe accessor — never exposes raw Optional internally
  public Optional<Apple> getApple() {
    return apple;
  }

  @Override
  public String toString() {
    return apple.map(a -> "Basket[" + a + "]")
        .orElse("Basket[🧺 empty]");
  }
}

public class AppleBasket {
  public static void main(String[] args) {
    // Create baskets
    Basket basket1 = Basket.with(new Apple(false)); // worm-free
    Basket basket2 = Basket.with(new Apple(true)); // wormy
    Basket basket3 = Basket.empty(); // no apple

    // Query behavior — safe and expressive
    System.out.println(basket1 + " → worm-free? " + basket1.hasWormFreeApple()); // true
    System.out.println(basket2 + " → worm-free? " + basket2.hasWormFreeApple()); // false
    System.out.println(basket3 + " → worm-free? " + basket3.hasWormFreeApple()); // false

    // Bonus: do something only if worm-free apple exists
    basket1.getApple()
        .filter(Apple::isWormFree)
        .ifPresent(apple -> System.out.println("🍏 Enjoy your safe apple!"));
  }
}
