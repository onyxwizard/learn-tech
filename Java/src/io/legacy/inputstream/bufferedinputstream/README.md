# 🔹 Lab 5: `BufferedInputStream` — The Performance Multiplier

## 🎯 Goal:  
See how **one line of code** makes I/O *10–1000× faster* — and understand *why*.

## 💡 Core Idea:  
> `BufferedInputStream` sits *between* you and a slow stream (like `FileInputStream`).  
> Instead of asking the OS for 1 byte at a time, it asks for **8192 bytes**, caches them, and gives you 1 byte at a time from RAM.

No API change. Massive speedup.

## 🛠️ Step 1: Create a Large Test File (1 MB)

Run this once to generate `large.txt`:
```java
// GenFile.java
import java.io.*;
import java.nio.file.*;

public class GenFile {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("large.txt");
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (int i = 0; i < 100_000; i++) {
                bw.write("Line " + i + ": The quick brown fox jumps over the lazy dog.\n");
            }
        }
        System.out.println("✅ Created large.txt (" + Files.size(path) + " bytes)");
    }
}
```
→ `javac GenFile.java && java GenFile`

## 🛠️ Step 2: Time the Difference — Raw vs Buffered

```java
import java.io.*;

public class Lab5 {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Lab 5: BufferedInputStream — Why Buffering Matters ===\n");

        // 🔹 Test 1: FileInputStream (unbuffered)
        long start = System.nanoTime();
        countBytes(new FileInputStream("large.txt"));
        long rawTime = System.nanoTime() - start;

        // 🔹 Test 2: BufferedInputStream (buffered)
        start = System.nanoTime();
        countBytes(new BufferedInputStream(new FileInputStream("large.txt")));
        long bufferedTime = System.nanoTime() - start;

        // 🔹 Report
        double ratio = (double) rawTime / bufferedTime;
        System.out.printf("""
            Results for ~%.1f MB file:
              Unbuffered (FileInputStream): %,d ms
              Buffered (BufferedInputStream): %,d ms
              Speedup: %.1fx faster!
            """,
            new File("large.txt").length() / 1_000_000.0,
            rawTime / 1_000_000,
            bufferedTime / 1_000_000,
            ratio
        );
    }

    // Counts bytes one-by-one — worst-case for unbuffered I/O
    static void countBytes(InputStream in) throws IOException {
        try (in) {
            int count = 0;
            while (in.read() != -1) count++;
            System.out.println("Total bytes: " + count);
        }
    }
}
```

## ▶️ Expected Output (on a modern laptop):
```
Total bytes: 5300000
Total bytes: 5300000

Results for ~5.3 MB file:
  Unbuffered (FileInputStream): 1,240 ms
  Buffered (BufferedInputStream): 28 ms
  Speedup: 44.3x faster!
```

💡 On slower disks or network files, it can be **1000×**.

# 🔍 Why This Happens — The Syscall Tax

| Action | `FileInputStream.read()` | `BufferedInputStream.read()` |
|--------|---------------------------|-------------------------------|
| **1st `read()`** | OS syscall: `read(fd, &b, 1)` | OS syscall: `read(fd, buffer, 8192)`<br>→ then returns `buffer[0]` |
| **2nd `read()`** | OS syscall: `read(fd, &b, 1)` | Returns `buffer[1]` (**no syscall!**) |
| **... 8192th `read()`** | 8192 syscalls | 1 syscall |
| **8193rd `read()`** | 8193rd syscall | 2nd syscall |

✅ **Each syscall** = kernel switch + context overhead (~1–10 µs)  
✅ **Disk seek** = milliseconds (1,000,000× slower than RAM)

→ Buffering turns **millions of syscalls** into **hundreds**.


## 🧪 Your Turn: Investigate

1. **Change buffer size**:  
   ```java
   new BufferedInputStream(new FileInputStream("large.txt"), 1024)  // 1KB
   new BufferedInputStream(new FileInputStream("large.txt"), 32768) // 32KB
   ```
   → Does bigger = always better? (Try 1 byte — what happens?)

2. **Try bulk `read(byte[])` instead of `read()`**  
   ```java
   byte[] buf = new byte[1];
   while (in.read(buf) != -1) count++;
   ```
   → Is `BufferedInputStream` still helpful? (Yes — but less dramatic)

3. **Wrap `ByteArrayInputStream` in `BufferedInputStream`**  
   → Does it help? (Spoiler: **no** — why?)

## 📦 Where You’ll See This in Real Code

✅ **Everywhere** — it’s the #1 I/O optimization. Examples:
```java
// Reading config
try (var in = new BufferedInputStream(new FileInputStream("app.conf"))) { ... }

// Network stream (SocketInputStream is slow!)
try (var in = new BufferedInputStream(socket.getInputStream())) { ... }

// Even inside JDK:
Files.copy(in, out) // uses buffering internally
```

> 📌 **Rule of thumb**:  
> **Always wrap** `FileInputStream`, `SocketInputStream`, `Process.getInputStream()` in `BufferedInputStream` — unless you’re using `Files.readAllBytes()` or similar high-level method.


## 🧩 Updated Big Picture

```
Disk
  ↓
FileInputStream          ← slow, syscall per read
  ↓
BufferedInputStream      ← fast, syscall per 8KB
  ↓
InputStreamReader(UTF_8) ← bytes → chars
  ↓
BufferedReader           ← chars → lines
```

Same interface. One extra layer. Huge win.