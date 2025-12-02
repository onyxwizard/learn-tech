package inputstream.datainputstream.lab6;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Lab6 {
  public static void main(String[] args) throws IOException {
        System.out.println("=== Lab 6: DataInputStream — Binary Structure ===\n");

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                    new FileInputStream("inputstream/datainputstream/data.bin")))) {

            // MUST read in same order as written!
            int year = dis.readInt();
            double pi = dis.readDouble();
            boolean flag = dis.readBoolean();
            String message = dis.readUTF();

            System.out.println("Read:");
            System.out.println("  year   = " + year);
            System.out.println("  pi     = " + pi);
            System.out.println("  flag   = " + flag);
            System.out.println("  msg    = \"" + message + "\"");
        }
    }
}
