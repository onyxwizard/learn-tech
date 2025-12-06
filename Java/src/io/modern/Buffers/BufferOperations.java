package Buffers;

import java.nio.ByteBuffer;

public class BufferOperations {
  public static void main(String[] args) {
    ByteBuffer bufferByte = ByteBuffer.allocate(1024); // alocation
    int size = bufferByte.capacity(); //returns buffer size
    System.out.println("Buffer size : " + size);
    
    bufferByte.put("Hi".getBytes()); // position=2, limit=1024
    System.out.println("Limit : " + bufferByte.limit());
    
    bufferByte.flip(); // position=0, limit=2 ← ready to read
    System.out.println(bufferByte.position());

    byte[] out = new byte[2];
    bufferByte.get(out); // position=2, limit=2
    System.out.println("Limit : "+ bufferByte.limit());
    System.out.println(new String(out)); // "Hi"

    System.out.println(bufferByte.position());
    bufferByte.clear(); // position=0, limit=1024 ← ready to reuse
    
    
    System.out.println(bufferByte.position());
    System.out.println("Limit : "+ bufferByte.limit());
  }
}
