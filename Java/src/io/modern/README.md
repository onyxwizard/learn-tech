# 🧭 Java NIO Cheatsheet (Organized & Conceptual)

> *“NIO = Non-blocking I/O. It replaces streams with channels and buffers, adds selectors for multiplexing, and introduces a modern file API.”*

## 🗃️ 1. Core Concepts Overview

| Term             | Purpose |
|------------------|---------|
| **Channel**      | Like a stream, but bidirectional and often non-blocking. Connects to I/O sources (files, sockets). |
| **Buffer**       | A container for data (byte, char, int, etc.) that you read into or write from. Central to all NIO operations. |
| **Selector**     | Multiplexes multiple channels — allows one thread to manage many connections (key for scalable servers). |
| **Path / Files** | Modern, robust file/path API replacing `java.io.File`. More expressive and less error-prone. |

## 🔄 2. Buffer Operations (The Heart of NIO)

Buffers are **not** just arrays — they have state: `position`, `limit`, `capacity`.

### Key Methods:
- `put()` — Write data into buffer.
- `flip()` — Switch from writing to reading mode (sets `limit = position`, `position = 0`).
- `get()` — Read data from buffer.
- `clear()` — Reset buffer for writing again (`position = 0`, `limit = capacity`).
- `compact()` — Compact unread data to beginning (for partial reads).

### Common Buffer Types:
- `ByteBuffer` — Most common; can be direct (off-heap) or heap-based.
- `CharBuffer`, `IntBuffer`, etc. — For specific primitive types.
- `MappedByteBuffer` — Memory-mapped file access (very fast for large files).

> 💡 **Rule**: Always `flip()` after writing, before reading. Always `clear()` or `compact()` after reading.

## ⛓️ 3. Channel Types & Usage

Channels are the conduits for data transfer. They are **non-blocking by default** (can be switched to blocking).

| Channel Type               | Purpose |
|----------------------------|---------|
| `FileChannel`              | For file I/O. Supports memory-mapping, locking, and scatter/gather. |
| `SocketChannel`            | For TCP client-side communication. Can be non-blocking. |
| `ServerSocketChannel`      | For TCP server-side. Accepts incoming connections. |
| `DatagramChannel`          | For UDP communication (connectionless). |
| `Pipe.SourceChannel` / `Pipe.SinkChannel` | For inter-thread communication (like piped streams, but NIO-style). |

### ➤ Scatter/Gather (Advanced)
- `read(ByteBuffer[])` — Scatters data into multiple buffers.
- `write(ByteBuffer[])` — Gathers data from multiple buffers to write.
> Useful for structured data (e.g., header + payload).

## 🎯 4. Selector: The Non-blocking Multiplier

Selectors allow **a single thread to monitor multiple channels** for readiness (read, write, connect, accept).

### How it works:
1. Register channels with selector, specifying interest ops (`OP_READ`, `OP_WRITE`, etc.).
2. Call `selector.select()` — blocks until at least one channel is ready.
3. Get selected keys → process each ready channel.
4. Repeat.

### Key Classes:
- `Selector`
- `SelectionKey` — Represents registration of a channel with a selector. Holds interest ops and ready ops.

> 💡 **Use Case**: Building a scalable chat server or web server handling thousands of concurrent connections with few threads.

## 📁 5. Path & Files API (Modern File Handling)

Replaces `java.io.File` with richer, more reliable abstractions.

| Class/Interface   | Purpose |
|-------------------|---------|
| `Path`            | Immutable representation of a file/directory path. Created via `Paths.get(...)`. |
| `Files`           | Utility class with static methods for common file operations (copy, move, delete, read, write, etc.). |
| `FileSystem`      | Abstracts the underlying filesystem (local, ZIP, custom). |

### Common `Files` Methods:
```java
Files.readAllLines(path);         // Read entire file as List<String>
Files.write(path, lines);         // Write lines to file
Files.copy(source, target);       // Copy file
Files.walk(path).forEach(...);    // Traverse directory tree
Files.createDirectory(path);      // Create dir (with parents if needed)
```

> ✅ **Advantages over `File`**:  
> - Throws `IOException` with detailed messages.  
> - Supports symbolic links, attributes, atomic operations.  
> - Integrates with NIO channels via `Files.newByteChannel(...)`.

## 🆚 6. NIO vs. IO: When to Use Which?

| Feature                  | Legacy IO (`java.io`)                | NIO (`java.nio`)                     |
|--------------------------|--------------------------------------|---------------------------------------|
| **Blocking?**            | Always blocking                      | Can be non-blocking                   |
| **Data Unit**            | Streams (bytes/chars)                | Buffers + Channels                    |
| **Scalability**          | One thread per connection            | One thread handles many connections   |
| **File Access**          | `File`, `FileInputStream`            | `Path`, `Files`, `FileChannel`        |
| **Best For**             | Simple, small-scale apps             | High-performance, scalable servers    |
| **Memory Mapping**       | No                                   | Yes (`FileChannel.map()`)             |
| **Scatter/Gather**       | No                                   | Yes                                   |

> 🧠 **Rule of Thumb**:  
> - Use **IO** for simple file/text processing, small apps.  
> - Use **NIO** for network servers, high-throughput systems, or when you need non-blocking I/O.

## ⚡ 7. Asynchronous I/O (NIO.2 — Java 7+)

> Introduced in Java 7 as part of `java.nio.file` package — truly asynchronous operations.

| Class                          | Purpose |
|--------------------------------|---------|
| `AsynchronousFileChannel`      | Non-blocking file I/O. Uses callbacks or futures. |
| `AsynchronousSocketChannel`    | Non-blocking TCP client. |
| `AsynchronousServerSocketChannel` | Non-blocking TCP server. |

### Example Pattern:
```java
AsynchronousFileChannel channel = AsynchronousFileChannel.open(path);
Future<Integer> future = channel.read(buffer, 0);
// Do other work...
Integer bytesRead = future.get(); // Blocks until done
```

> 💡 **Note**: Asynchronous I/O is “fire-and-forget” — ideal for event-driven architectures.


## 🌐 8. Non-blocking Server Example (Conceptual)

```java
Selector selector = Selector.open();
ServerSocketChannel server = ServerSocketChannel.open();
server.bind(new InetSocketAddress(8080));
server.configureBlocking(false);
server.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select(); // Blocks until ready
    Set<SelectionKey> keys = selector.selectedKeys();
    for (SelectionKey key : keys) {
        if (key.isAcceptable()) {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            // Read from client
        }
    }
    keys.clear();
}
```

## ✅ Pro Tips for NIO

1. **Always configure blocking mode** explicitly — default is blocking for most channels.
2. **Use `try-with-resources`** for channels and selectors.
3. **Buffer size matters** — too small → many syscalls; too large → memory waste.
4. **Direct buffers** (`ByteBuffer.allocateDirect()`) avoid JVM heap copying — good for performance-critical I/O.
5. **Avoid mixing NIO and legacy IO** unless necessary — they have different semantics.

This structure gives you a clear mental model of NIO:
- **Buffers** hold data.
- **Channels** move data.
- **Selectors** manage many channels efficiently.
- **Path/Files** provide modern file operations.
- **Asynchronous** APIs enable true non-blocking behavior.