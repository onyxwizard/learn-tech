package Channels.level1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
public class Hello {
  public static void main(String[] args) throws IOException {
    //Create File for this test
    Path path = Paths.get("Channels/level1/hello.txt");
    //Files.write(path, "Hello World".getBytes());

    try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
      ByteBuffer buffer = ByteBuffer.allocate(1024);

      //Now Read from file using channel and load it to buffer
      int bytesRead = channel.read(buffer);
      System.out.println("Bytes Read : " + bytesRead);
      
      //Store
      buffer.flip();
      byte[] byteArray = new byte[buffer.remaining()];
      buffer.get(byteArray);


      System.out.println("Data : " + Arrays.toString(byteArray));
      System.out.println("Data : " + new String(byteArray));

    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getLocalizedMessage());
    }
    
  }
}