# 🔹 Lab 6: `DataInputStream` — Bytes → Numbers, Strings, Structure

## 🎯 Goal:  
Go beyond “just bytes” — read **Java’s native binary format** for primitives and strings.

## 💡 Core Idea:  
> `DataInputStream` adds methods like:  
> - `readInt()` → 4 bytes → `int`  
> - `readDouble()` → 8 bytes → `double`  
> - `readUTF()` → length-prefixed UTF-8 string  
>  
> It *requires* the data was written by `DataOutputStream` — same format, same order.

This is how Java serializes simple data (`.class` files, network protocols, game saves).

## 🛠️ Step 1: Write Binary Data (using `DataOutputStream`)

```java
// WriteData.java
import java.io.*;

public class WriteData {
    public static void main(String[] args) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(
                    new FileOutputStream("data.bin")))) {

            // Write structured data
            dos.writeInt(2025);              // 4 bytes
            dos.writeDouble(3.14159);       // 8 bytes
            dos.writeBoolean(true);         // 1 byte
            dos.writeUTF("Hello, 🌍!");    // 2-byte length + UTF-8 bytes

            System.out.println("✅ Wrote data.bin");
        }
    }
}
```
→ `javac WriteData.java && java WriteData`

> 🔍 **File size?**  
> `4 + 8 + 1 + (2 + 12) = 27 bytes`  
> (`"Hello, 🌍!"` = 9 chars → 12 UTF-8 bytes + 2-byte length prefix)

## 🛠️ Step 2: Read It Back — *Exactly the Same Order!*

```java
// Lab6.java
import java.io.*;

public class Lab6 {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Lab 6: DataInputStream — Binary Structure ===\n");

        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                    new FileInputStream("data.bin")))) {

            // MUST read in same order as written!
            int year = dis.readInt();
            double pi = dis.readDouble();
            boolean flag = dis.readBoolean();
            String message = dis.readUTF();

            System.out.println("Read:");
            System.out.println("  year   = " + year);
            System.out.println("  pi     = " + pi);
            System.out.println("  flag   = " + flag);
            System.out.println("  msg    = \"" + message + "\"");
        }
    }
}
```

## ▶️ Output:
```
=== Lab 6: DataInputStream — Binary Structure ===

Read:
  year   = 2025
  pi     = 3.14159
  flag   = true
  msg    = "Hello, 🌍!"
```

✅ **Perfect round-trip** — no parsing, no string splitting.

# 🔍 Why This Matters — Real-World Uses

| Use Case | How It’s Used |
|---------|---------------|
| **Java `.class` files** | Constant pool, method signatures — all `readUnsignedShort()`, `readUTF()` |
| **Network protocols** | Custom binary protocols (e.g., game servers, IoT) |
| **Simple serialization** | When `ObjectOutputStream` is too heavy |
| **File formats** | PNG, ZIP, Java serialization header |

> 📌 **Critical Rule**:  
> **Writer and reader must agree on:**  
> 1. Order of fields  
> 2. Data types (`int` vs `short`)  
> 3. String encoding (`readUTF()` = modified UTF-8, *not* standard UTF-8!)

## ⚠️ Pitfall: `readUTF()` Is *Not* Standard UTF-8

`DataOutputStream.writeUTF()` uses **Modified UTF-8**:
- Null char `\u0000` → encoded as `0xC0 0x80` (not `0x00`)
- Only supports BMP (no full Unicode — but 🌍 is fine)

✅ Safe for most text.  
❌ Not for arbitrary binary data or full Unicode (e.g., emojis with ZWJ).

→ Prefer standard UTF-8 via `InputStreamReader` for pure text.

## 🧪 Your Turn: Break It (Learn by Failure)

Try these — observe the error:

1. **Swap read order**:  
   ```java
   double pi = dis.readDouble(); // try reading double first → corrupt!
   ```
   → `EOFException` or garbage numbers.

2. **Write `short`, read `int`**:  
   ```java
   dos.writeShort(100);
   int x = dis.readInt(); // reads 4 bytes — reads next field too!
   ```

3. **Use `readLine()`** (deprecated!):  
   ```java
   String s = dis.readLine(); // warns, and doesn’t handle \r\n correctly
   ```
## 📦 Where This Fits in the Pipeline

```
Disk
  ↓
FileInputStream
  ↓
BufferedInputStream      ← always add this!
  ↓
DataInputStream          ← now you can read int/double/UTF
```

No `InputStreamReader` here — `DataInputStream` works on *raw bytes*, not chars.