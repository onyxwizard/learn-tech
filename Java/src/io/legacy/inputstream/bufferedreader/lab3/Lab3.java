package inputstream.bufferedreader.lab3;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Lab3 {
  public static void main(String[] args) {
        System.out.println("=== Lab 3: Reading Like a Human (Lines!) ===\n");

        try (
            FileInputStream fis = new FileInputStream("inputstream/bufferedreader/lab3/sample.txt");
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr)
        ) {
            String line;
            int lineNumber = 1;

            while ((line = br.readLine()) != null) {
                System.out.printf("Line %d: \"%s\"%n", lineNumber, line);
                lineNumber++;
            }

        } catch (IOException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
