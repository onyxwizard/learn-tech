package phase1.files.chapter1;

import java.io.File;

public class FileCreation {

  static String createLogFile(String logDir) {
    File f = new File(logDir);

    System.out.println("Dir Exists : " + f.isDirectory());

    if (f.isDirectory())
      return logDir;
    f.mkdir();
    return logDir;
  }
  
  static void fileStatus(File f) {
    System.out.println("Name : "+ f.getName());
    System.out.println("Absolute Path : "+ f.getAbsolutePath());
    System.out.println("Exist : "+ f.exists());
    System.out.println("Type : "+ f.getParent());
    System.out.println("Size : "+ f.length());
    System.out.println("Readable : "+ f.canRead()+ "|" + f.canWrite());
  }
  
  public static void main(String[] args) {
    String path = "D:/Learn/github/learn-tech/Java/src/phase1/files/chapter1";
    String fileName = "notes.txt";
    File f = new File(createLogFile(path),fileName);
    
    if(f.exists()) {
      System.out.println("Success");
    } else {
      try {
        f.createNewFile();
        System.out.println("File created");
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    
    fileStatus(f);
  }
}