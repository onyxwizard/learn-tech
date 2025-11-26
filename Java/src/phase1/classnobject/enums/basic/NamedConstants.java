package phase1.classnobject.enums.basic;


enum Status {
  PENDING, CONFIRMED, SHIPPED, CANCELLED;
}
public class NamedConstants {
  public static void main(String[] args) {
    System.out.println(Status.PENDING);
  }
}
