package Channels.challenge2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelPosition {
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("Channels/challenge2/FileChannelPositionWriter.txt");
    FileChannel destinationFile = FileChannel.open(path, StandardOpenOption.WRITE,
        StandardOpenOption.TRUNCATE_EXISTING);

    ByteBuffer buffer = ByteBuffer.allocate(100);

    String passage = "Hello";

    buffer.put(passage.getBytes());
    buffer.flip();
    destinationFile.write(buffer, 1);
    buffer.clear();
    buffer.put(passage.getBytes());
    buffer.flip();
    destinationFile.write(buffer, 5);


    buffer.clear();
    buffer.put(passage.getBytes());
    buffer.flip();
    destinationFile.write(buffer, 10);


    buffer.clear();
    buffer.put(passage.getBytes());
    buffer.flip();
    destinationFile.write(buffer, 25);
    
    
    

  }
}
