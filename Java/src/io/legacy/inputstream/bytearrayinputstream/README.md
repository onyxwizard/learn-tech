## 🔹 Lab 4: `ByteArrayInputStream` — Streams in Memory

### 🎯 Goal:  
Understand that an `InputStream` is **not about files** — it’s about *delivering bytes in order*, wherever they come from.

### 💡 Core Idea:  
> `ByteArrayInputStream` = a `byte[]` pretending to be a file.

No disk. No OS calls. No resources to close.  
Same methods: `read()`, `skip()`, `available()` — same behavior.

### 🛠️ Step 1: Create `Lab4.java`

```java
import java.io.*;

public class Lab4 {
    public static void main(String[] args) {
        System.out.println("=== Lab 4: ByteArrayInputStream — Streams in Memory ===\n");

        // Create a byte array: "Hi!" in ASCII + emoji in UTF-8
        // H=72, i=105, !=33, space=32, 🌍= [0xF0, 0x9F, 0x8C, 0x8D]
        byte[] data = {72, 105, 33, 32, (byte)0xF0, (byte)0x9F, (byte)0x8C, (byte)0x8D};

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            System.out.println("✅ Opened in-memory stream. Length: " + data.length + " bytes");
            System.out.println("Initial available(): " + bais.available());

            // Read one byte at a time (just like Lab 1)
            System.out.println("\n[1] Reading byte-by-byte:");
            int b;
            int pos = 0;
            while ((b = bais.read()) != -1) {
                char c = (b >= 32 && b < 127) ? (char) b : '?';
                System.out.printf("  pos %d → byte %3d (0x%02X) → '%c'%n", pos, b, b, c);
                pos++;
            }

            // Reset and read as UTF-8 text (just like Lab 2)
            bais.reset(); // ← key! ByteArrayInputStream supports mark/reset!
            InputStreamReader isr = new InputStreamReader(bais, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("\n[2] Reading as UTF-8 text:");
            int c;
            while ((c = isr.read()) != -1) {
                System.out.print((char) c);
            }
            System.out.println();

            // Bonus: Try mark/reset
            bais.reset();
            bais.mark(10); // mark current position
            bais.read();   // read 'H'
            bais.read();   // read 'i'
            bais.reset();  // go back!
            System.out.println("\n[3] After mark + 2 reads + reset → next char: '" + (char)bais.read() + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### ▶️ Expected Output:
```
=== Lab 4: ByteArrayInputStream — Streams in Memory ===

✅ Opened in-memory stream. Length: 8 bytes
Initial available(): 8

[1] Reading byte-by-byte:
  pos 0 → byte  72 (0x48) → 'H'
  pos 1 → byte 105 (0x69) → 'i'
  pos 2 → byte  33 (0x21) → '!'
  pos 3 → byte  32 (0x20) → ' '
  pos 4 → byte 240 (0xF0) → '?'
  pos 5 → byte 159 (0x9F) → '?'
  pos 6 → byte 140 (0x8C) → '?'
  pos 7 → byte 141 (0x8D) → '?'

[2] Reading as UTF-8 text:
Hi! 🌍

[3] After mark + 2 reads + reset → next char: 'H'
```

## 🔍 Key Observations (Compare to `FileInputStream`)

| Feature | `FileInputStream` | `ByteArrayInputStream` | Why It Matters |
|--------|-------------------|------------------------|----------------|
| **Source** | Disk file | `byte[]` in RAM | Test data, network buffers, parsed chunks |
| **`mark()`/`reset()`** | ❌ Not supported | ✅ Supported | Essential for parsers (lookahead) |
| **`close()`** | Required (releases fd) | Optional (no-op) | Safe to omit, but good habit |
| **`available()`** | Unreliable hint | ✅ Exact remaining bytes | You can trust it here! |
| **Performance** | Syscall per read (slow unbuffered) | Pure Java (fast) | Great for unit tests |

> 💡 **Real-world use**:  
> When you download data from the web (`byte[] response = httpClient.get()`), you wrap it in `new ByteArrayInputStream(response)` to use stream APIs without saving to disk.

## 🧪 Your Turn: Experiment

Try these **one at a time** — observe what changes:

1. **Remove `bais.reset()` before `[2]`** → what happens?  
2. **Change `mark(10)` to `mark(1)`** → can you still reset after 2 reads? (Hint: read limit in Javadoc)  
3. **Replace `data` with `new byte[0]`** — what does `read()` return immediately?

## 🧩 The Bigger Picture Now

You now know **two fundamental sources** of streams:
```
          +------------------+       +----------------------+
          |  FileInputStream |       | ByteArrayInputStream |
          +------------------+       +----------------------+
                   ↓                            ↓
          (disk → bytes)              (RAM → bytes)
                   ↓                            ↓
           InputStream interface ←──────────────┘
                   ↓
          InputStreamReader(UTF_8)
                   ↓
            BufferedReader
```

Same pipeline. Different origins.