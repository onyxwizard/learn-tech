package inputstream.sequenceinputstream.lab8;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Vector;

public class Lab8 {
  public static void main(String[] args) throws IOException {
    System.out.println("=== Lab 8: SequenceInputStream — Stream Concatenation ===\n");

    // 🔹 Method 1: Two streams
    InputStream seq1 = new SequenceInputStream(
        new FileInputStream("inputstream/sequenceinputstream/lab8/part1.txt"),
        new FileInputStream("inputstream/sequenceinputstream/lab8/part2.txt")
    );

    seq1.close();
    
    
    // 🔹 Method 2: Enumeration of many streams
    Vector<InputStream> streams = new Vector<>();
    streams.add(new FileInputStream("inputstream/sequenceinputstream/lab8/part1.txt"));
    streams.add(new FileInputStream("inputstream/sequenceinputstream/lab8/part2.txt"));
    streams.add(new FileInputStream("inputstream/sequenceinputstream/lab8/part3.txt"));
    InputStream seq2 = new SequenceInputStream(streams.elements());

    // Read and print
    System.out.println("[1] First two parts (via 2-arg constructor):");
    readAndPrint(seq1);

    System.out.println("\n[2] All three parts (via Enumeration):");
    readAndPrint(seq2);
    
    seq2.close();
  }

  static void readAndPrint(InputStream in) throws IOException {
    try (in;
        InputStreamReader isr = new InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr)) {

      String line;
      while ((line = br.readLine()) != null) {
        System.out.println("  → " + line);
      }
    }
  }
}
