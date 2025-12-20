package basic;

import java.io.File;
import java.io.FileOutputStream;

import java.io.ObjectOutputStream;

public class SerializeDemo {
  public static void main(String[] args) {

    // Step 1: Create an object to serialize
    Person person = new Person("John Doe", 30, "john@example.com");
    System.out.println("Original Person: " + person);

    // Step 2: Serialize the object to a file
    String filename = "basic/person.ser"; // .ser is conventional for serialized files

    try {
      // Step 3: Create a FileOutputStream (connects to file)
      FileOutputStream fileOut = new FileOutputStream(filename);
      
      // Step 4: Create ObjectOutputStream (does the serialization)
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      
      // Step 5: Write object to stream
      out.writeObject(person);

      // Step 6: Close streams
      out.close();
      fileOut.close();

      System.out.println("✓ Object serialized to " + filename);
      System.out.println("File size: " + new File(filename).length() + " bytes");

    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getLocalizedMessage());
    }
  }
}