package phase1.oop.nestedclass.innermethodclass;

class OuterClass {
  void output() {
    class InnerClass {
      void out() {
        System.out.println("Inner Class - Method Local");
      }
    }

    // Method Local - invocation
    InnerClass inner = new InnerClass();
    inner.out();
  }
}

public class MainClassNested {
  public static void main(String[] args) {
    OuterClass outerObj = new OuterClass();
    outerObj.output();
  }
}
