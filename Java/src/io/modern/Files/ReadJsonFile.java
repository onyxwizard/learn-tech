package Files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class ReadJsonFile {
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("Files/app.json");
    String data = Files.readString(path,StandardCharsets.UTF_8);
    System.out.println(data);
  }
}
