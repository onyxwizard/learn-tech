package Channels.challenge;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class CopyFiles {
  public static void main(String[] args) throws IOException {
    //Assume we have both files created
    Path sourcePath = Paths.get("Channels/challenge/source.txt");
    Path destinationPath = Paths.get("Channels/challenge/destination.txt");
    
    try (FileChannel sourceChannel = FileChannel.open(sourcePath, StandardOpenOption.READ);
    FileChannel destinationChannel = FileChannel.open(destinationPath, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
    ) {
     // long size = sourceChannel.size();
      // if (size > Integer.MAX_VALUE) {
      //   throw new SizeLimitExceededException("File Size Exceeded");
      // }

      ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
      while (sourceChannel.read(buffer) != -1) {
        buffer.flip(); // prepare for writing
        while (buffer.hasRemaining()) {
          destinationChannel.write(buffer); // may write partially — loop until done
        }
        buffer.clear(); // reset for next read
      }
      System.out.println("Copied Successfully");
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(e.getLocalizedMessage());
    }
  }
}
