# 🔹 Lab 10: `DigestInputStream` — Hash While You Stream

## 🎯 Goal:  
Compute a cryptographic hash (e.g., SHA-256) of a file **as you read it** — zero extra memory, one pass.

### 💡 Core Idea:  
> `DigestInputStream` wraps any `InputStream` and silently feeds every byte to a `MessageDigest`.  
> You read normally — and *at any time*, you can ask: “What’s the hash so far?”

This is how tools like `shasum` work on huge files without OOM errors.

## 🛠️ Step 1: Generate a Test File (Reusing `large.txt`)

If you still have `large.txt` from Lab 5 (~5 MB), great!  
If not, run `GenFile.java` again (or create any file, even `sample.txt`).

## 🛠️ Step 2: Compute SHA-256 — Two Ways

### ✅ ethod A: Naive (Load All → Hash) — *Bad for large files*
```java
byte[] all = Files.readAllBytes(Path.of("large.txt"));
byte[] hash = MessageDigest.getInstance("SHA-256").digest(all);
```
→ Works for small files. Fails for 10 GB.

### ✅ Method B: Streaming (With `DigestInputStream`) — *Production-ready*
```java
// Lab10.java
import java.io.*;
import java.security.*;

public class Lab10 {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Lab 10: DigestInputStream — Stream Hashing ===\n");

        File file = new File("large.txt");
        System.out.println("File: " + file.getName() + " (" + file.length() + " bytes)");

        // 🔹 Setup digest
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // 🔹 Wrap FileInputStream in DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(
                new BufferedInputStream(
                    new FileInputStream(file)), md)) {

            // Read all bytes (we don’t care about content — just trigger hashing)
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {
                // No-op — bytes are auto-fed to md
            }

            // 🔹 Get final hash
            byte[] hash = md.digest(); // ← critical: call .digest() AFTER reading!

            // Format as hex
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            
            System.out.println("SHA-256: " + hex);
        }

        // 🔹 Verify with system tool (optional)
        System.out.println("\n✅ Verify with: shasum -a 256 large.txt");
    }
}
```

## ▶️ Output:
```
=== Lab 10: DigestInputStream — Stream Hashing ===

File: large.txt (5300000 bytes)
SHA-256: a1b2c3... (64 hex chars)

✅ Verify with: shasum -a 256 large.txt
```

✅ Matches `shasum -a 256 large.txt` exactly.

# 🔍 How It Works — The Magic

- `DigestInputStream` overrides `read()` methods.
- Every time you call `read()`, it:
  1. Delegates to the wrapped stream
  2. **Before returning**, calls `md.update(byteArray, off, len)`
- So the hash is **always up to date** with what you’ve read.

You can even check *mid-stream*:
```java
dis.read(buffer, 0, 1000);        // read 1KB
byte[] partial = md.digest();      // get hash of first 1KB
md.reset();                        // if you want to restart (rare)
```

> 📌 **Critical note**:  
> Call `md.digest()` **only after** you’re done reading — it finalizes the hash and resets the digest.  
> To check *intermediate* state, use `md.clone()`:
> ```java
> MessageDigest snapshot = (MessageDigest) md.clone();
> byte[] interim = snapshot.digest();
> ```

## 🌐 Real-World Uses

| Use Case | Why `DigestInputStream` Wins |
|---------|------------------------------|
| **Secure file download** | Verify checksum *while* downloading — fail fast on corruption |
| **Docker/OCI layer verification** | Each layer has SHA-256 — validate during pull |
| **Blockchain log integrity** | Hash logs as they’re written to disk |
| **Backup systems** | Store hash alongside backup — verify on restore |

Example: Download + verify in one pass:
```java
try (InputStream net = new URL("https://example.com/file.zip").openStream();
     DigestInputStream dis = new DigestInputStream(net, md);
     FileOutputStream out = new FileOutputStream("file.zip")) {

    dis.transferTo(out); // reads & writes — hash computed automatically!
    
    if (!Arrays.equals(md.digest(), expectedHash)) {
        throw new SecurityException("Hash mismatch!");
    }
}
```

## 🧪 Your Turn: Experiment

1. **Try MD5** (insecure, but fast):  
   ```java
   MessageDigest md = MessageDigest.getInstance("MD5");
   ```

2. **Hash only first 1KB**:  
   ```java
   dis.read(buffer, 0, 1024);
   byte[] partial = ((MessageDigest) md.clone()).digest();
   ```

3. **Break it**: Call `md.digest()` *before* finishing read → what happens?

## 🧩 Where This Fits

```
Network / Disk
      ↓
  FileInputStream
      ↓
BufferedInputStream
      ↓
DigestInputStream   ← hash computed transparently
      ↓
DataInputStream / BufferedReader / etc.
```

It’s a **transparent sidecar** — your code doesn’t change.