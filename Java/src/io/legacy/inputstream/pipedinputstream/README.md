# 🔹 Lab 9 (Revisited): `PipedInputStream` & `PipedOutputStream`  
### 🎯 Goal:  
Stream data **from one thread to another** — without files, sockets, or shared buffers.

### 💡 Core Idea:  
> - `PipedOutputStream` → writes bytes  
> - `PipedInputStream` → reads those same bytes  
> - They are **connected** — like `stdout` → `stdin` in `cmd1 | cmd2`  
> - Backed by a **circular buffer** (default 1024 bytes)  
> - **Blocking**: writer blocks if buffer full; reader blocks if buffer empty

Think: *Producer thread → pipe → Consumer thread*


## ⚠️ Critical Warning First  
> ❗ **Never use the same thread for both ends** — it will **deadlock**.  
> ❗ **Never share pipes across >2 threads** — not thread-safe.  
> ✅ **Always**: One writer thread, one reader thread.

Modern alternative: `java.util.concurrent` (e.g., `BlockingQueue<byte[]>`), but pipes teach *stream semantics* beautifully.


### 🛠️ Step 1: Safe Producer-Consumer Demo

```java
import java.io.*;

public class Lab8_Piped {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Lab 8: Piped Streams — Thread-to-Thread Pipes ===\n");

        // 🔹 Step 1: Create connected pair
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream(pis); // connect!

        // 🔹 Step 2: Start READER thread (consumer)
        Thread readerThread = new Thread(() -> {
            try (pis) { // auto-close when done
                System.out.println("[Reader] Started. Waiting for data...");
                int b;
                StringBuilder sb = new StringBuilder();
                while ((b = pis.read()) != -1) {
                    sb.append((char) b);
                }
                System.out.println("[Reader] Done. Received: \"" + sb + "\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Reader-Thread");
        readerThread.start();

        // 🔹 Step 3: Start WRITER thread (producer)
        Thread writerThread = new Thread(() -> {
            try (pos) {
                System.out.println("[Writer] Started. Sending data...");
                String msg = "Hello from Writer! 🌍";
                pos.write(msg.getBytes());
                pos.flush(); // ensure sent
                System.out.println("[Writer] Sent: " + msg.length() + " bytes");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Writer-Thread");
        writerThread.start();

        // 🔹 Wait for both
        writerThread.join();
        readerThread.join();
        System.out.println("\n✅ Pipe communication completed.");
    }
}
```

### ▶️ Expected Output:
```
=== Lab 8: Piped Streams — Thread-to-Thread Pipes ===

[Reader] Started. Waiting for data...
[Writer] Started. Sending data...
[Writer] Sent: 22 bytes
[Reader] Done. Received: "Hello from Writer! 🌍"

✅ Pipe communication completed.
```

✅ **No files. No sockets. Just in-JVM streaming.**


## 🔍 How the Pipe Works Internally

```
Writer Thread          Pipe Buffer (1024B)          Reader Thread
     ↓                       ↑  ↓                       ↑
pos.write('H')       →   [H, ?, ?, ...]          ←   pis.read() → 'H'
pos.write('i')       →   [H, i, ?, ...]          ←   pis.read() → 'i'
...                  →   [H, i, ..., 🌍]         ←   ...
pos.close()          →   EOF signal              ←   read() → -1
```

- If writer is faster → blocks when buffer full  
- If reader is faster → blocks when buffer empty  
- `close()` on writer side → sends EOF to reader

## 🧪 Common Pitfalls — And How to Avoid Them

| Mistake | Symptom | Fix |
|--------|---------|-----|
| **Same thread for read/write** | Deadlock — thread blocks forever | ✅ Always use **two threads** |
| **Not connecting streams** | `IOException: Pipe not connected` | ✅ Use `new PipedOutputStream(pis)` or `pis.connect(pos)` |
| **Ignoring `flush()`** | Data stuck in writer’s side buffer | ✅ Call `flush()` after writes (or use `BufferedOutputStream`) |
| **Large writes > buffer** | Writer blocks until reader consumes | ✅ Accept blocking (it’s backpressure!) or increase buffer:  
`new PipedInputStream(8192)` |

Try this broken version — see the deadlock:
```java
// ❌ WRONG: Same thread!
PipedInputStream pis = new PipedInputStream();
PipedOutputStream pos = new PipedOutputStream(pis);

pos.write("test".getBytes());  // Blocks! Buffer fills, no reader running
int b = pis.read();            // Never reached!
```


# 🌐 Real-World Uses (Rare but Vital)

| Use Case | Why Pipes Fit |
|---------|---------------|
| **Legacy API bridging** | Convert `OutputStream`-based API to `InputStream` (or vice versa) |
| **Testing stream code** | Inject mock data into a component that reads from `InputStream` |
| **Isolated subprocess simulation** | Simulate `Process.getOutputStream()`/`.getInputStream()` |
| **Audio/Video frame passing** | Between decoder and renderer threads (though `BlockingQueue` preferred today) |

Example: Convert `OutputStream` → `InputStream` for testing:
```java
PipedInputStream pis = new PipedInputStream();
PipedOutputStream pos = new PipedOutputStream(pis);

// Component that writes to OutputStream
logger.setOutput(pos); // e.g., a custom logger

// In test thread: read what it wrote
new Thread(() -> {
    int b;
    while ((b = pis.read()) != -1) {
        // assert bytes...
    }
}).start();

logger.log("Test message"); // writes to pos → appears in pis
```
## 🧩 Updated Big Picture

```
Thread A (Producer)        Thread B (Consumer)
       ↓                          ↑
PipedOutputStream →→→ PipedInputStream
       │                (JVM-internal pipe)
       └── circular buffer (1KB default)
```

It’s the only `InputStream` whose source is **another thread**, not a file, network, or array.

## ✅ Your Turn: Try This Challenge

Modify the demo to:
1. Send **10 messages** with 100ms delay between each  
2. Reader prints each message *as it arrives* (not all at end)  
3. Use `BufferedOutputStream` on the writer side  

💡 Hint:  
```java
for (int i = 0; i < 10; i++) {
    pos.write(("Message " + i + "\n").getBytes());
    pos.flush();
    Thread.sleep(100);
}
pos.close(); // signal EOF
```