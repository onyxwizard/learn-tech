package basic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeserializeDemo {
  
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    Person person;
    String filename = "basic/person.ser";
    
    FileInputStream file = new FileInputStream(filename);
    ObjectInputStream out = new ObjectInputStream(file);
    
    //Cast to store
    person = (Person) out.readObject();


    out.close();
    file.close();

    System.out.println(person);
    System.out.println(person.getAge());
    System.out.println(person.getName());

  }
}
