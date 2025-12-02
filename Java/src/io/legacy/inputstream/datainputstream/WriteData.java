package inputstream.datainputstream;

import java.io.*;

public class WriteData {
    public static void main(String[] args) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream("inputstream/datainputstream/data.bin")))) {

            // Write structured data
            dos.writeInt(2025);              // 4 bytes
            dos.writeDouble(3.14159);       // 8 bytes
            dos.writeBoolean(true);         // 1 byte
            dos.writeUTF("Hello, 🌍!");    // 2-byte length + UTF-8 bytes

            System.out.println("✅ Wrote data.bin");
        }
    }
}
