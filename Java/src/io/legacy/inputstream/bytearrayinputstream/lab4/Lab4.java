package inputstream.bytearrayinputstream.lab4;

import java.io.*;

public class Lab4 {
  public static void main(String[] args) {
        System.out.println("=== Lab 4: ByteArrayInputStream — Streams in Memory ===\n");

        // Create a byte array: "Hi!" in ASCII + emoji in UTF-8
        // H=72, i=105, !=33, space=32, 🌍= [0xF0, 0x9F, 0x8C, 0x8D]
        byte[] data = {72, 105, 33, 32, (byte)0xF0, (byte)0x9F, (byte)0x8C, (byte)0x8D};

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            System.out.println("✅ Opened in-memory stream. Length: " + data.length + " bytes");
            System.out.println("Initial available(): " + bais.available());

            // Read one byte at a time (just like Lab 1)
            System.out.println("\n[1] Reading byte-by-byte:");
            int b;
            int pos = 0;
            while ((b = bais.read()) != -1) {
                char c = (b >= 32 && b < 127) ? (char) b : '?';
                System.out.printf("  pos %d → byte %3d (0x%02X) → '%c'%n", pos, b, b, c);
                pos++;
            }

            // Reset and read as UTF-8 text (just like Lab 2)
            bais.reset(); // ← key! ByteArrayInputStream supports mark/reset!
            InputStreamReader isr = new InputStreamReader(bais, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("\n[2] Reading as UTF-8 text:");
            int c;
            while ((c = isr.read()) != -1) {
                System.out.print((char) c);
            }
            System.out.println();

            // Bonus: Try mark/reset
            bais.reset();
            bais.mark(1); // mark current position
            bais.read();   // read 'H'
            bais.read();   // read 'i'
            bais.reset();  // go back!
            System.out.println("\n[3] After mark + 2 reads + reset → next char: '" + (char)bais.read() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
