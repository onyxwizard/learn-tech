# 🔹 Lab 8: `SequenceInputStream` — Concatenate Streams Seamlessly

## 🎯 Goal:  
See how Java lets you **chain streams end-to-end**, so a reader sees them as a single continuous stream.

### 💡 Core Idea:  
> `SequenceInputStream` takes an `Enumeration<InputStream>` (or two streams) and serves them **one after another** — like `cat file1 file2 | ...` in Unix.

No copying. No temp files. Just virtual concatenation.

## 🛠️ Step 1: Create Three Small Files

```bash
echo "Part 1: Hello" > part1.txt
echo "Part 2: World" > part2.txt
echo "Part 3: 🌍" > part3.txt
```

Total content:
```
Part 1: Hello
Part 2: World
Part 3: 🌍
```

## 🛠️ Step 2: Read All as One Stream

```java
import java.io.*;
import java.util.*;

public class Lab8 {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Lab 8: SequenceInputStream — Stream Concatenation ===\n");

        // 🔹 Method 1: Two streams
        InputStream seq1 = new SequenceInputStream(
            new FileInputStream("part1.txt"),
            new FileInputStream("part2.txt")
        );

        // 🔹 Method 2: Enumeration of many streams
        Vector<InputStream> streams = new Vector<>();
        streams.add(new FileInputStream("part1.txt"));
        streams.add(new FileInputStream("part2.txt"));
        streams.add(new FileInputStream("part3.txt"));
        InputStream seq2 = new SequenceInputStream(streams.elements());

        // Read and print
        System.out.println("[1] First two parts (via 2-arg constructor):");
        readAndPrint(seq1);

        System.out.println("\n[2] All three parts (via Enumeration):");
        readAndPrint(seq2);
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
```

### ▶️ Output:
```
=== Lab 8: SequenceInputStream — Stream Concatenation ===

[1] First two parts (via 2-arg constructor):
  → Part 1: Hello
  → Part 2: World

[2] All three parts (via Enumeration):
  → Part 1: Hello
  → Part 2: World
  → Part 3: 🌍
```

✅ **No gaps. No extra newlines.** Just seamless join.


# 🔍 Why This Is Powerful — Real Use Cases

| Scenario | How `SequenceInputStream` Helps |
|---------|----------------------------------|
| **Log aggregation** | Merge daily log files (`log.2025-12-01`, `log.2025-12-02`) for analysis |
| **Chunked downloads** | Combine downloaded parts (e.g., torrent chunks) |
| **Template rendering** | Header + body + footer streams → one response |
| **Testing** | Inject test data between real streams |

> 📌 **Key advantage**:  
> You don’t need to copy data into a big `byte[]` or temp file — memory efficient, streaming-friendly.


## 🧪 Your Turn: Investigate

Try these — observe behavior:

1. **Close one inner stream early**:  
   ```java
   FileInputStream f1 = new FileInputStream("part1.txt");
   FileInputStream f2 = new FileInputStream("part2.txt");
   SequenceInputStream seq = new SequenceInputStream(f1, f2);
   f1.close(); // before reading!
   // What happens when you read?
   ```

2. **Mix stream types**:  
   ```java
   new SequenceInputStream(
       new ByteArrayInputStream("In-memory".getBytes()),
       new FileInputStream("part1.txt")
   );
   ```

3. **What if a stream is empty?**  
   Create `empty.txt` (0 bytes) — does it break the sequence?

## ⚠️ Important Notes

- ❗ **Inner streams are closed automatically** when `SequenceInputStream` reaches their end (or when *it* is closed).
- ❗ **Order matters** — streams are read in `Enumeration` order.
- ❗ **Not thread-safe** — don’t share across threads.

> 💡 Pro tip: In modern code, `SequenceInputStream` is rare — often replaced by:
> - `Files.readAllLines()` + `String.join()` (for small text)
> - Reactive streams (Project Reactor, RxJava) for async
> - `InputStream` combinators in libraries (e.g., Okio’s `Buffer.concat()`)

But understanding it reveals how **stream composition** works at the core.

## 🧩 Updated Big Picture

```
Multiple Sources
   ↓       ↓       ↓
FileIS  ByteArrIS  NetIS
   └─── SequenceInputStream ← virtual concatenation
                ↓
        BufferedInputStream
                ↓
        PushbackInputStream
                ↓
        DataInputStream
                ↓
        InputStreamReader(UTF_8)
                ↓
        BufferedReader
```

Each layer adds capability — **without breaking the contract**.