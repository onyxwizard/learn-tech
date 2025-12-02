package inputstream.pushbackinputstream.lab7;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class Lab7 {
      public static void main(String[] args) throws IOException {
        System.out.println("=== Lab 7: PushbackInputStream — Lookahead for Parsing ===\n");

        String input = "123abc456def";
        System.out.println("Input: \"" + input + "\"\nTokens:");

        try (PushbackInputStream pbis = new PushbackInputStream(
                new ByteArrayInputStream(input.getBytes()))) {

            int tokenCount = 1;
            int b;

            while ((b = pbis.read()) != -1) {
                if (Character.isDigit(b)) {
                    // Start of number — consume all digits
                    StringBuilder num = new StringBuilder();
                    num.append((char) b);
                    while ((b = pbis.read()) != -1 && Character.isDigit(b)) {
                        num.append((char) b);
                    }
                    // b is now non-digit → push it back!
                    if (b != -1) pbis.unread(b);
                    System.out.printf("  %d. NUMBER: %s%n", tokenCount++, num);
                } else {
                    // Start of word — consume all letters
                    StringBuilder word = new StringBuilder();
                    word.append((char) b);
                    while ((b = pbis.read()) != -1 && Character.isLetter(b)) {
                        word.append((char) b);
                    }
                    if (b != -1) pbis.unread(b);
                    System.out.printf("  %d. WORD: %s%n", tokenCount++, word);
                }
            }
        }
    }
}
