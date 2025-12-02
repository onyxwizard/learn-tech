# 🧭 Java I/O Cheatsheet (Organized & Conceptual)

> *“Java I/O is built around streams — sequences of data. Everything else is an adapter, decorator, or specialized stream.”*

## 🗃️ 1. Core Abstractions

| Category             | Key Classes/Interfaces                     | Purpose |
|----------------------|--------------------------------------------|---------|
| **Base Streams**     | `InputStream`, `OutputStream`              | Abstract base for byte-based I/O. All concrete streams extend these. |
| **Character Streams**| `Reader`, `Writer`                         | Abstract base for character-based I/O (Unicode). |
| **File Abstraction** | `File`                                     | Represents file/directory path. Not a stream itself — used to create streams. |


## 📁 2. File & Directory Operations

| Class                | Purpose |
|----------------------|---------|
| `File`               | Manipulate file paths, check existence, create/delete files/dirs. |
| `RandomAccessFile`   | Read/write anywhere in a file (seekable). Uses `byte[]` or primitives. |

## ⛓️ 3. Byte Streams (InputStream / OutputStream Hierarchy)

### ➤ Basic Sources/Sinks
- `FileInputStream` / `FileOutputStream` — Read/write files as bytes.
- `ByteArrayInputStream` / `ByteArrayOutputStream` — In-memory byte arrays.
- `PipedInputStream` / `PipedOutputStream` — Thread-to-thread communication.
- `SequenceInputStream` — Concatenate multiple input streams.

### ➤ Filter Streams (Decorators)
> Wrap other streams to add functionality (buffering, filtering, etc.)

- `BufferedInputStream` / `BufferedOutputStream` — Add buffering for performance.
- `FilterInputStream` / `FilterOutputStream` — Base for custom filters.
- `DataInputStream` / `DataOutputStream` — Read/write primitive types (int, double, etc.) + strings.
- `PrintStream` — Formatted output (e.g., `System.out`). Can auto-flush.
- `PushbackInputStream` — Push back bytes for re-reading (useful in parsers).

### ➤ Specialized
- `ObjectInputStream` / `ObjectOutputStream` — Serialize/deserialize objects (requires `Serializable`).
- `ZipInputStream` / `ZipOutputStream` — (Not listed, but commonly used) For compressed archives.

## 🔤 4. Character Streams (Reader / Writer Hierarchy)

### ➤ Basic Sources/Sinks
- `FileReader` / `FileWriter` — Convenience for reading/writing text files (UTF-8 default).
- `CharArrayReader` / `CharArrayWriter` — In-memory char arrays.
- `StringReader` / `StringWriter` — Read/write from/to `String`/`StringBuilder`.

### ➤ Filter Streams (Decorators)
- `BufferedReader` / `BufferedWriter` — Add buffering + `readLine()`.
- `FilterReader` / `FilterWriter` — Base for custom character filters.
- `PushbackReader` — Push back characters.
- `LineNumberReader` — Tracks line numbers while reading.
- `PrintWriter` — Formatted output (like `PrintStream` but for chars). Auto-flush optional.

### ➤ Adapters
- `InputStreamReader` / `OutputStreamWriter` — Bridge between byte streams and character streams. **Crucial for encoding control** (e.g., `new InputStreamReader(inputStream, "UTF-8")`).


## 🔄 5. Stream Conversion & Bridging

| Adapter              | Purpose |
|----------------------|---------|
| `InputStreamReader`  | Converts `InputStream` (bytes) → `Reader` (chars). Specify charset! |
| `OutputStreamWriter` | Converts `Writer` (chars) → `OutputStream` (bytes). Specify charset! |

> 💡 **Rule of Thumb**:  
> - Use **byte streams** for binary data (images, PDFs, serialized objects).  
> - Use **character streams** for text (UTF-8, ASCII, etc.).  
> - Always wrap with `BufferedReader`/`BufferedWriter` for performance on text.

## 🚨 6. Exception Handling & Utilities

| Topic                 | Notes |
|-----------------------|-------|
| **Exception Handling**| Most I/O operations throw `IOException`. Always wrap in try-catch or declare `throws`. Use try-with-resources (Java 7+) for automatic closing. |
| **Input Parsing**     | Use `Scanner`, `BufferedReader.readLine()` + `split()`, or `StreamTokenizer` for parsing structured text. |
| **Serializable**      | Interface marker for objects that can be written/read via `ObjectOutputStream`/`ObjectInputStream`. |


## 🌐 7. Networking (Bonus Context)

> Though not strictly “I/O” in the file sense, network sockets use streams:
- `Socket.getInputStream()` → `InputStream`
- `Socket.getOutputStream()` → `OutputStream`

Use `BufferedReader` + `PrintWriter` for text-based protocols (HTTP, SMTP).

## 🧠 Quick Reference: When to Use What?

| Task                          | Recommended Class(es) |
|-------------------------------|------------------------|
| Read text file line-by-line   | `BufferedReader` + `FileReader` or `InputStreamReader` |
| Write formatted text          | `PrintWriter` or `BufferedWriter` |
| Read/write binary files       | `FileInputStream`/`FileOutputStream` or `RandomAccessFile` |
| Serialize objects             | `ObjectOutputStream`/`ObjectInputStream` |
| Parse CSV/structured text     | `BufferedReader.readLine()` + `split()` or `Scanner` |
| Network text communication    | `BufferedReader` + `PrintWriter` over socket streams |
| Avoid blocking I/O            | Consider `java.nio` (non-blocking) — outside scope here |

## ✅ Pro Tips

1. **Always close streams** → Use `try-with-resources`:
   ```java
   try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
       // ... read
   } // Auto-closed
   ```

2. **Specify charset explicitly** when converting bytes ↔ chars:
   ```java
   Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
   ```

3. **Buffering matters** → Unbuffered streams are slow for small reads/writes.

4. **Don’t mix byte/char streams** without adapters — it causes encoding bugs.

