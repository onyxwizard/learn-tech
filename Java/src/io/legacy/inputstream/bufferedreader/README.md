# 🔹 Lab 3: `BufferedReader` — The Human Reader

## 🎯 Goal: Read *lines* (like a person), not bytes or chars.

## 🛠️ Update `sample.txt` to:
```
Line 1: Hello
Line 2: 🌍 Earth
Line 3: 123
```

## Run This:
```java
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Lab3 {
    public static void main(String[] args) {
        System.out.println("=== Lab 3: Reading Like a Human (Lines!) ===\n");

        try (
            FileInputStream fis = new FileInputStream("sample.txt");
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
```

## ▶️ Output:
```
=== Lab 3: Reading Like a Human (Lines!) ===

Line 1: "Line 1: Hello"
Line 2: "Line 2: 🌍 Earth"
Line 3: "Line 3: 123"
```

✅ Now you’re reading **text the way it’s meant to be read**.

> 💡 **Key Insight #3**:  
> Real-world text I/O =  
> `FileInputStream` → `InputStreamReader(UTF_8)` → `BufferedReader`
