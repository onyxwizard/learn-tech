# 🔹 Lab 1: `FileInputStream` — The Raw Truth

## 🎯 Goal:  
See what a file *really* looks like to Java — **before** interpretation.

## 🛠️ Step 1: Create a Test File  
Create `sample.txt` with this exact content (use VS Code, Notepad++, etc. — *save as UTF-8*):
```
Hi! 🌍
```

🔍 Fun fact: This is **7 characters**, but **9 bytes** in UTF-8:
- `H` `i` `!` ` ` → 1 byte each (4 bytes)
- `🌍` → 4 bytes: `F0 9F 8C 8D`

### 🛠️ Step 2: Run This Code (Your Updated Version — Now Safe & Clear)

```java
import java.io.*;

public class Lab1 {
    public static void main(String[] args) {
        System.out.println("=== Lab 1: What FileInputStream *Actually* Sees ===\n");

        try (FileInputStream fis = new FileInputStream("sample.txt")) {
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
```

## ▶️ Run It — You’ll See:
```
=== Lab 1: What FileInputStream *Actually* Sees ===

Character | Byte (dec) | Byte (hex)
----------|------------|------------
        0 |         72 | 0x48   → 'H'
        1 |        105 | 0x69   → 'i'
        2 |         33 | 0x21   → '!'
        3 |         32 | 0x20   → ' '
        4 |        240 | 0xF0   → '?'
        5 |        159 | 0x9F   → '?'
        6 |        140 | 0x8C   → '?'
        7 |        141 | 0x8D   → '?'
```

🛑 **Pause here.**  
- Do you see how `🌍` became **4 question marks**?  
- That’s not a bug — that’s `FileInputStream` doing its *one job*: **give you raw bytes**.

> 💡 **Key Insight #1**:  
> Files store **bytes**.  
> Humans read **characters**.  
> Java needs a *translator* to go from one to the other.
