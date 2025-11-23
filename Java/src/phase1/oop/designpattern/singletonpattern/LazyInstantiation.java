package phase1.oop.designpattern.singletonpattern;

class LazyOfficePrinter {
    // 🖨️ Starts as null — not created yet
    private static LazyOfficePrinter instance;

    private LazyOfficePrinter() {
        System.out.println("🖨️ LazyOfficePrinter initialized (on first use).");
    }

    public static LazyOfficePrinter getInstance() {
        if (instance == null) {
            instance = new LazyOfficePrinter(); // first request → power on!
        }
        return instance;
    }

    public void print(String doc) {
        System.out.println("🖨️ Printing (lazy): \"" + doc + "\"");
    }
}

public class LazyInstantiation {
  public static void main(String[] args) {
        System.out.println("Before first getInstance()...");
        LazyOfficePrinter p1 = LazyOfficePrinter.getInstance(); // ← printer turns on here
        LazyOfficePrinter p2 = LazyOfficePrinter.getInstance();
        LazyOfficePrinter p3 = LazyOfficePrinter.getInstance();
        
        System.out.println("Same printer? " + (p1 == p2)); // true
        System.out.println("Same printer? " + (p2 == p3)); // true
    }
}
