package Buffers;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Challenge1 {
  public static void main(String[] args) {
    ByteBuffer b = ByteBuffer.allocate(4);
    b.put(new byte[] { 1, 2, 3, 4 });
    b.flip(); // [1,2,3,4], pos=0, lim=4
    b.get(); // read 1
    b.get(); // read 2 → pos=2
    b.compact(); // ?
    System.out.println(b.position()); // ?
    System.out.println(b.remaining()); // ?
  }
}
