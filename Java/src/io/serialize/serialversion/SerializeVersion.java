package serialversion;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeVersion {
  public static void main(String[] args) {
    Person person = new Person("AK", 26, "akinfosec@mail.com");

    String file = "serialversion/serializeperson.ser";

    try (
      FileOutputStream fl = new FileOutputStream(file);
      ObjectOutputStream out = new ObjectOutputStream(fl);
    ) {
      
      out.writeObject(person);
      
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getLocalizedMessage());
    }

  }
}
