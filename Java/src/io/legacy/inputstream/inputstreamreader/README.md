# 🔹 Lab 2: `InputStreamReader` — The Translator

## 🎯 Goal: Add the *missing translator* — and see `🌍` appear!

## 🛠️ Run This Code:
```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Lab2 {
    public static void main(String[] args) {
        System.out.println("=== Lab 2: Adding the Translator (UTF-8) ===\n");

        try (
            FileInputStream fis = new FileInputStream("inputstream/fileinputstream/lab2/sample.txt");
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
```

## ▶️ Run It — You’ll See:
```
=== Lab 2: Adding the Translator (UTF-8) ===

Position | Code Point | Character
---------|------------|----------
       0 |         72 | 'H'
       1 |        105 | 'i'
       2 |         33 | '!'
       3 |         32 | ' '
       4 |     127748 | '🌍'
```

✅ **Success!** `InputStreamReader` used **UTF-8 rules** to combine 4 bytes → 1 character.

> 💡 **Key Insight #2**:  
> `InputStreamReader` = `FileInputStream` + **encoding rules**.  
> Always specify `StandardCharsets.UTF_8` — never rely on “default”.
