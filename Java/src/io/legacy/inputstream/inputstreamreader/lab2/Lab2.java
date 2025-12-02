package inputstream.inputstreamreader.lab2;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Lab2 {
    public static void main(String[] args) {
        System.out.println("=== Lab 2: Adding the Translator (UTF-8) ===\n");

        try (
            FileInputStream fis = new FileInputStream("inputstream/inputstreamreader/lab2/sample.txt");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8)
        ) {
            int charValue;
            int index = 0;

            System.out.println("Position | Code Point | Character");
            System.out.println("---------|------------|----------");

            while ((charValue = isr.read()) != -1) {
                System.out.printf("%8d | %10d | '%c'%n", 
                    index, charValue, (char) charValue);
                index++;
            }

        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
