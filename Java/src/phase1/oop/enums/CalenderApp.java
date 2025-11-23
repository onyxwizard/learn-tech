package phase1.oop.enums;


enum Day {
  MONDAY(1),
  TUESDAY(2),
  WEDNESDAY(3),
  THURSDAY(4),
  FRIDAY(5),
  SATURDAY(6),
  SUNDAY(7);

  // Field to store the day number
  private final int number;

  // Constructor (package-private — default)
  Day(int number) {
    this.number = number;
  }

  // Getter
  public int getNumber() {
    return number;
  }
}

public class CalenderApp {
  public static void main(String[] args) {
    Day today = Day.FRIDAY;
    System.out.println(today.name());
    System.out.println(today);
    System.out.println(today.hashCode());
    System.out.println(today.ordinal());
    System.out.println(today.describeConstable());
    System.out.println(today.getDeclaringClass());
    System.out.println(today.getNumber()); 
  }
}
