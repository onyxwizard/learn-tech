package phase1.oop.nestedclass.anonymousinnerclass;

public class MainAnonymousClass {
  public static void main(String[] args) {
    System.out.println("\n→ 1. Anonymous subclass of abstract class (Car)");
    Car sportsCar = new Car() {
      @Override
      public void engine() {
        System.out.println("V8 roar — engine started! (anonymous Car subclass)");
      }
    };
    sportsCar.engine();

    System.out.println("\n→ 2. Anonymous implementation of interface (EngineStarter)");
    EngineStarter starter = new EngineStarter() {
      @Override
      public void startEngine() {
        System.out.println("Ignition sequence engaged. (anonymous interface impl)");
      }
    };
    starter.startEngine();

    System.out.println("\n→ 3. Anonymous class passed directly as method argument");
    House house = new House();
    house.result(new Room() {
      @Override
      public int count() {
        return 5; // concise; no auto-generated clutter
      }
    });
  }
}
