package phase1.files.chapter2;

import java.io.FileInputStream;


public class ByteReader {
  public static void main(String[] args) {
    String fileName = "D:/Learn/github/learn-tech/Java/src/phase1/files/chapter2/notes.txt";
    byte[] size;
    try (FileInputStream f = new FileInputStream(fileName.trim())) {
      size = f.readAllBytes();
      int n = size.length;
      while (n > 0) {
        char x = (char) size[n-1];
        System.out.println(x);
        n--;
      }
      System.out.println("Length of file : " + size.length);
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }
}
