package serialversion;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import serialversion.Person;

public class DeserializeVersion {
  public static void main(String[] args) {
    Person person = null;
    //double serialversion = 123L;
    String file = "serialversion/serializeperson.ser";

    try (
      FileInputStream fl = new FileInputStream(file);
      ObjectInputStream in = new ObjectInputStream(fl);
    ) {
      
      person = (Person) in.readObject();
      
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getLocalizedMessage());
    }

    System.out.println(person);
  }
}
