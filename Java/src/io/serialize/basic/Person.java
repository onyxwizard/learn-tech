package basic;
import java.io.Serializable;


class Person implements Serializable {
  private String name;
  private int age;
  private String email;

  Person(String name, int age, String email) {
    this.name = name;
    this.age = age;
    this.email = email;
  }

  // Step 4: Getters and setters
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getEmail() {
        return email;
    }
    
    // Step 5: toString() method for display
    @Override
    public String toString() {
        return "Person[name=" + name + ", age=" + age + ", email=" + email + "]";
    }
}