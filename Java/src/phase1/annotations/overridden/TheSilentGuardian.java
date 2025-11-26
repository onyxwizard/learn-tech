package phase1.annotations.overridden;

class Automobile {
  public void output() {
    System.out.println("Tag : Automobile");
  }
}

class Car extends Automobile {
  @Override
  public void output() {
    System.out.println("Tag: Car");
  }
}

public class TheSilentGuardian {
  public static void main(String[] args) {
    Automobile object1 = new Automobile();
    Automobile object2 = new Car();

    object1.output();
    object2.output();
    
  }
}
