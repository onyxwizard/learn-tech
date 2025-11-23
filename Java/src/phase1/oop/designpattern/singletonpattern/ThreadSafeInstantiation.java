package phase1.oop.designpattern.singletonpattern;

class SafeOfficePrinter {
  private static SafeOfficePrinter instance;

  private SafeOfficePrinter() {
    System.out.println("🖨️ SafeOfficePrinter initialized (thread-safe).");
  }

  // 🔐 Synchronize the whole method
  public static synchronized SafeOfficePrinter getInstance() {
    if (instance == null) {
      instance = new SafeOfficePrinter();
    }
    return instance;
  }
  
  public void print(String doc) {
    System.out.println("🔒 Printing (thread-safe): \"" + doc + "\"");
    exit();
  }

  private void exit() {
    System.out.println("Exiting ....");
  }
}

public class ThreadSafeInstantiation {
  public static void main(String[] args) {
        // Simulate two "users" trying to get the printer at the same time
        Thread t1 = new Thread(() -> SafeOfficePrinter.getInstance().print("Doc1"));
        Thread t2 = new Thread(() -> SafeOfficePrinter.getInstance().print("Doc2"));

        t1.start();
        t2.start();
    }
}
