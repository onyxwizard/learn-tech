package phase1.methodchain.beginner;

/**
 * 🎯 Build a Person with setName(), setAge(), setCity(). Chain them to create:
 * Alice, 30, Tokyo — then print it in one line.
 */

public class Person {
  private String name;
  private int age;
  private String city;

  Person setCity(String city) {
    this.city = city;
    return this;
  }

  Person setAge(int age) {
    this.age = age;
    return this;
  }

  Person setName(String name) {
    this.name = name;
    return this;
  }

  Person getName() {
    System.out.println("Person Name : " + this.name);
    return this;
  }

  Person getAge() {
    System.out.println("Person Age : " + this.age);
    return this;
  }

  Person getCity() {
    System.out.println("Person City : " + this.city);
    return this;
  }

  // Composite Inspector
  Person getDetails() {
    getName().getAge().getCity();
    return this;
  }


  public static void main(String[] args) {
    Person user = new Person();
    user.setName("ak").setAge(28).setCity("UK").getName();
    user.setName("ak").setAge(28).setCity("UK").getName().getAge();
    user.setName("ak").setAge(28).setCity("UK").getName().getAge().getCity();
    user.setName("ak").setAge(28).setCity("UK").getDetails();
  }
}