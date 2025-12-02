package inputstream.fileinputstream.lab1;

import java.io.*;

public class Lab1 {
    public static void main(String[] args) {
        System.out.println("=== Lab 1: What FileInputStream *Actually* Sees ===\n");

        try (FileInputStream fis = new FileInputStream("inputstream/fileinputstream/lab1/sample.txt")) {
            int byteValue;
            int index = 0;

            System.out.println("Character | Byte (dec) | Byte (hex)");
            System.out.println("----------|------------|------------");

            while ((byteValue = fis.read()) != -1) {
                // Show: position, decimal byte, hex byte, and what it *would* be as char
                char fakeChar = (byteValue >= 32 && byteValue < 127) 
                              ? (char) byteValue 
                              : '?'; // unprintable → ?
                
                System.out.printf("%9d | %10d | 0x%02X   → '%c'%n", 
                    index, byteValue, byteValue, fakeChar);
                index++;
            }

        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}