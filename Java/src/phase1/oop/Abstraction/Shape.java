package phase1.oop.Abstraction;


interface Drawable {
    void draw();                         // abstract
    default void drawWithBorder() {      // default method
        System.out.print("[BOX] ");
        draw();
    }
}

interface Resizable {
    void resize(double factor);
    default boolean isTooSmall() {
        return getSize() < 1.0;
    }
    double getSize(); // abstract — used by default method!
}

class Circle implements Drawable, Resizable {
  private double radius;

  public Circle(double radius) {
    this.radius = radius;
  }

  @Override
  public void draw() {
    System.out.println("⚪ (radius=" + radius + ")");
  }

  @Override
  public void resize(double factor) {
    radius *= factor;
  }

  @Override
  public double getSize() {
    return radius;
  }
}


public class Shape {
  public static void main(String[] args) {
    Circle c = new Circle(2.0);
    c.draw();               // ⚪ (radius=2.0)
    c.drawWithBorder();     // [BOX] ⚪ (radius=2.0)
    c.resize(0.4);
    System.out.println("Too small? " + c.isTooSmall()); // true (0.8 < 1.0)
  }
}
