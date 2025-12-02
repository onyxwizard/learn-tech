package inputstream.bufferedinputstream;

// GenFile.java
import java.io.*;
import java.nio.file.*;

public class GenFile {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("inputstream/bufferedinputstream/large.txt");
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (int i = 0; i < 100_000; i++) {
                bw.write("Line " + i + ": The quick brown fox jumps over the lazy dog.\n");
            }
        }
        System.out.println("✅ Created large.txt (" + Files.size(path) + " bytes)");
    }
}