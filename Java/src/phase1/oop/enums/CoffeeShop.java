package phase1.oop.enums;

/** ENUM : Constructor
 * The enum constants (SMALL, MEDIUM, etc.) are part of the enum itself — 
 * so they’re allowed to call the private constructor, just like any class can call its own private methods.
*/

enum CoffeeSize {
  SMALL(250), // 250 ml
  MEDIUM(350), // 350 ml
  LARGE(500); // 500 ml

  private final int volumeInMl;

  // 🔒 Explicit 'private' constructor — makes intent crystal clear
  private CoffeeSize(int volumeInMl) {
    this.volumeInMl = volumeInMl;
  }

  public int getVolume() {
    return volumeInMl;
  }
}

public class CoffeeShop {
  public static void main(String[] args) {
        CoffeeSize order = CoffeeSize.LARGE;

        System.out.println("Size: " + order.name());           // → LARGE
        System.out.println("Display: " + order);               // → LARGE (default toString)
        System.out.println("Volume: " + order.getVolume() + "ml"); // → 500ml
        System.out.println("Ordinal: " + order.ordinal());     // → 2 (0=SMALL, 1=MEDIUM, 2=LARGE)
        System.out.println("Class: " + order.getDeclaringClass().getSimpleName()); // → CoffeeSize
    }
}
