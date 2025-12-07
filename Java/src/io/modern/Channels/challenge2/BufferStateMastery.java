package Channels.challenge2;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BufferStateMastery {
    public static void main(String[] args) {
        // ✅ 16-byte buffer, filled with 16 bytes
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put("ERROR:DB\nWARN:CP".getBytes()); // 16 bytes
        System.out.println("After fill: pos=" + buffer.position() + ", lim=" + buffer.limit());

        // ✅ Flip to read
        buffer.flip();
        System.out.println("After flip: pos=" + buffer.position() + ", lim=" + buffer.limit());

        // ✅ Consume first 10 bytes
        byte[] first10 = new byte[10];
        buffer.get(first10);
        System.out.println("Consumed: '" + new String(first10) + "'");
        System.out.println("After get: pos=" + buffer.position() + ", rem=" + buffer.remaining());

        // ✅ Compact to keep unread data
        buffer.compact();
        System.out.println("After compact: pos=" + buffer.position() + ", lim=" + buffer.limit());

        // ✅ Now ready for next read — new data will go at position 6
        System.out.println("Buffer content: " + 
            Arrays.toString(Arrays.copyOf(buffer.array(), 16)));
    }
}