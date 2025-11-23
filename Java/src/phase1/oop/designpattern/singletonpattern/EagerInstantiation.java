package phase1.oop.designpattern.singletonpattern;

class OfficePrinter {
  // 🖨️ Instance created at class loading time (eager)
  private static final OfficePrinter INSTANCE = new OfficePrinter();

  // 🔒 Private constructor: no one can make another printer
  private OfficePrinter() {
    System.out.println("🖨️ OfficePrinter is initialized (eagerly).");
  }

  // 📬 Public access point
  public static OfficePrinter getInstance() {
    return INSTANCE;
  }

  // 🖨️ A useful method
  public void print(String document) {
    System.out.println("Printing: \"" + document + "\"");
  }
}


public class EagerInstantiation {
  public static void main(String[] args) {
        OfficePrinter printer1 = OfficePrinter.getInstance();
        OfficePrinter printer2 = OfficePrinter.getInstance();
        
        System.out.println("Same printer? " + (printer1 == printer2)); // true
        printer1.print("Report.pdf");
    }
  
}