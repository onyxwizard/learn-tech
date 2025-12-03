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

# 🧩 GROUP 1: BUFFERED STREAMS — For Performance Optimization

These wrap other streams to **buffer data**, reducing expensive I/O calls.

### ✅ `BufferedInputStream`
- **Extends**: `FilterInputStream` → `InputStream`
- **Purpose**: Buffers bytes read from an underlying `InputStream`.
- **Real-time use**:  
  Reading large files efficiently. Instead of reading 1 byte at a time (slow!), it reads chunks (e.g., 8KB) into memory, then serves from buffer.
  ```java
  FileInputStream fis = new FileInputStream("large.log");
  BufferedInputStream bis = new BufferedInputStream(fis);
  int data;
  while ((data = bis.read()) != -1) {
      // Process byte
  }
  ```

### ✅ `BufferedOutputStream`
- **Extends**: `FilterOutputStream` → `OutputStream`
- **Purpose**: Buffers bytes before writing to underlying stream.
- **Real-time use**: Writing large amounts of data to disk/network. Flushes only when buffer full or `flush()` called.
  ```java
  FileOutputStream fos = new FileOutputStream("output.txt");
  BufferedOutputStream bos = new BufferedOutputStream(fos);
  bos.write("Hello World".getBytes());
  bos.flush(); // Optional: force write now
  ```

### ✅ `BufferedReader`
- **Extends**: `Reader`
- **Purpose**: Buffers characters; adds `readLine()` for text.
- **Real-time use**: Reading text files line-by-line — very common!
  ```java
  FileReader fr = new FileReader("config.properties");
  BufferedReader br = new BufferedReader(fr);
  String line;
  while ((line = br.readLine()) != null) {
      System.out.println(line);
  }
  ```

### ✅ `BufferedWriter`
- **Extends**: `Writer`
- **Purpose**: Buffers characters; efficient for writing text.
- **Real-time use**: Writing logs, config files, CSVs.
  ```java
  FileWriter fw = new FileWriter("log.txt");
  BufferedWriter bw = new BufferedWriter(fw);
  bw.write("User logged in at: " + LocalDateTime.now());
  bw.newLine();
  bw.close();
  ```

> 💡 **Why buffer?** Disk/network I/O is slow. Buffering reduces system calls → faster performance.

---

# 🧩 GROUP 2: MEMORY-BASED STREAMS — For In-Memory Data Handling

These operate on arrays or strings in memory — no file/disk involved.

### ✅ `ByteArrayInputStream`
- **Extends**: `InputStream`
- **Purpose**: Reads bytes from a byte array.
- **Real-time use**: Processing data already in memory (e.g., HTTP response body, serialized object).
  ```java
  byte[] data = {65, 66, 67}; // 'A', 'B', 'C'
  ByteArrayInputStream bais = new ByteArrayInputStream(data);
  int b;
  while ((b = bais.read()) != -1) {
      System.out.print((char)b);
  }
  ```

### ✅ `ByteArrayOutputStream`
- **Extends**: `OutputStream`
- **Purpose**: Writes bytes to a byte array (grows dynamically).
- **Real-time use**: Building binary data in memory (e.g., generating ZIP, image, or protocol buffers).
  ```java
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  baos.write("Hello".getBytes());
  byte[] result = baos.toByteArray(); // Now you have the byte array
  ```

### ✅ `CharArrayReader`
- **Extends**: `Reader`
- **Purpose**: Reads characters from a char array.
- **Real-time use**: Parsing text already loaded into memory (e.g., config string, template).
  ```java
  char[] chars = {'H','e','l','l','o'};
  CharArrayReader car = new CharArrayReader(chars);
  ```

### ✅ `CharArrayWriter`
- **Extends**: `Writer`
- **Purpose**: Writes characters to a char array.
- **Real-time use**: Building strings efficiently without `StringBuilder` (less common now).
  ```java
  CharArrayWriter caw = new CharArrayWriter();
  caw.write("Hello ");
  caw.write("World");
  char[] result = caw.toCharArray();
  ```

> 💡 These are great for **testing**, **serialization**, or **processing in-memory data** without touching disk.

---
# 🧩 GROUP 3: FILE HANDLING — Direct File Access

These directly interact with files on disk.

### ✅ `File`
- **Extends**: `Object`
- **Purpose**: Represents a file/directory path — NOT for reading/writing! Just metadata.
- **Real-time use**: Checking if file exists, creating directories, listing files.
  ```java
  File f = new File("/tmp/data.txt");
  if (f.exists()) {
      System.out.println("File size: " + f.length());
  }
  ```

### ✅ `FileInputStream`
- **Extends**: `InputStream`
- **Purpose**: Reads raw bytes from a file.
- **Real-time use**: Reading binary files (images, PDFs, executables).
  ```java
  FileInputStream fis = new FileInputStream("image.jpg");
  ```

### ✅ `FileOutputStream`
- **Extends**: `OutputStream`
- **Purpose**: Writes raw bytes to a file.
- **Real-time use**: Saving downloaded files, generated reports.
  ```java
  FileOutputStream fos = new FileOutputStream("downloaded.zip");
  ```

### ✅ `FileReader`
- **Extends**: `InputStreamReader` → `Reader`
- **Purpose**: Reads text from a file using default charset.
- **Real-time use**: Simple text file reading (avoid if encoding matters).
  ```java
  FileReader fr = new FileReader("notes.txt");
  ```

### ✅ `FileWriter`
- **Extends**: `OutputStreamWriter` → `Writer`
- **Purpose**: Writes text to a file using default charset.
- **Real-time use**: Writing simple logs or configs.
  ```java
  FileWriter fw = new FileWriter("log.txt", true); // append mode
  fw.write("Error occurred\n");
  ```

> ⚠️ **Note**: `FileReader`/`FileWriter` use **platform default encoding** — dangerous for international apps. Prefer `InputStreamReader`/`OutputStreamWriter` with explicit charset (e.g., UTF-8).

---
# 🧩 GROUP 4: DATA STREAMS — For Primitive Types & Serialization

These handle structured data — not raw bytes/chars.

### ✅ `DataInputStream`
- **Extends**: `FilterInputStream`
- **Purpose**: Reads primitive types (`int`, `double`, `boolean`, etc.) and strings from an `InputStream`.
- **Real-time use**: Reading binary data formats (e.g., game saves, network protocols).
  ```java
  DataInputStream dis = new DataInputStream(new FileInputStream("data.bin"));
  int id = dis.readInt();
  double price = dis.readDouble();
  ```

### ✅ `DataOutputStream`
- **Extends**: `FilterOutputStream`
- **Purpose**: Writes primitive types to an `OutputStream`.
- **Real-time use**: Writing binary data for later reading by `DataInputStream`.
  ```java
  DataOutputStream dos = new DataOutputStream(new FileOutputStream("data.bin"));
  dos.writeInt(100);
  dos.writeDouble(9.99);
  ```

> 💡 These are **not for human-readable data** — they write in binary format. Use `PrintWriter` or JSON/XML for readable output.

---
# 🧩 GROUP 5: PIPES — For Thread Communication

These allow **inter-thread communication** via streams.

### ✅ `PipedInputStream` & `PipedOutputStream`
- **Extends**: `InputStream` / `OutputStream`
- **Purpose**: Connect two threads — one writes to `PipedOutputStream`, another reads from `PipedInputStream`.
- **Real-time use**: Producer-consumer pattern within same JVM.
  ```java
  PipedOutputStream pos = new PipedOutputStream();
  PipedInputStream pis = new PipedInputStream(pos); // connected!

  // Thread 1: writes
  new Thread(() -> {
      try {
          pos.write("Hello from Thread 1".getBytes());
          pos.close();
      } catch (IOException e) {}
  }).start();

  // Thread 2: reads
  new Thread(() -> {
      try {
          byte[] buf = new byte[1024];
          int len = pis.read(buf);
          System.out.println(new String(buf, 0, len));
          pis.close();
      } catch (IOException e) {}
  }).start();
  ```

> ⚠️ Can cause deadlocks if not managed carefully — usually replaced by `BlockingQueue` or `CompletableFuture` in modern code.

---
# 🧩 GROUP 6: OBJECT STREAMS — For Serialization

These handle **object serialization/deserialization**.

### ✅ `ObjectInputStream`
- **Extends**: `InputStream`
- **Purpose**: Reads objects written by `ObjectOutputStream`.
- **Real-time use**: Loading saved game state, caching objects, RPC.
  ```java
  ObjectInputStream ois = new ObjectInputStream(new FileInputStream("game.save"));
  Player player = (Player) ois.readObject();
  ```

### ✅ `ObjectOutputStream`
- **Extends**: `OutputStream`
- **Purpose**: Writes objects to stream (must implement `Serializable`).
- **Real-time use**: Saving application state.
  ```java
  ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("state.ser"));
  oos.writeObject(player);
  ```

> ⚠️ Security risk: Deserializing untrusted data can execute malicious code. Avoid in public-facing systems.

---
# 🧩 GROUP 7: PRINT STREAMS — For Human-Readable Output

These are designed for **printing formatted output**.

### ✅ `PrintStream`
- **Extends**: `FilterOutputStream`
- **Purpose**: Adds `print()`, `println()`, `printf()` methods. Handles auto-flush.
- **Real-time use**: Logging, console output (`System.out` is a `PrintStream`!).
  ```java
  PrintStream ps = new PrintStream(new FileOutputStream("output.log"));
  ps.println("User ID: " + userId);
  ps.printf("Balance: %.2f%n", balance);
  ```

### ✅ `PrintWriter`
- **Extends**: `Writer`
- **Purpose**: Like `PrintStream` but for characters (text). Also has `print()`, `println()`.
- **Real-time use**: Writing text files with formatting.
  ```java
  PrintWriter pw = new PrintWriter(new FileWriter("report.txt"));
  pw.println("Report Generated:");
  pw.printf("Total: %d items%n", count);
  ```

> 💡 Both are forgiving — they don’t throw checked exceptions (unlike `Writer`/`OutputStream`). Great for quick scripting or logging.

---
# 🧩 GROUP 8: READER/WRITE WRAPPERS — For Enhanced Text Processing

These add features like line numbers, pushback, tokenization.

### ✅ `LineNumberReader`
- **Extends**: `BufferedReader`
- **Purpose**: Keeps track of line numbers while reading.
- **Real-time use**: Parsing source code, config files — useful for error reporting.
  ```java
  LineNumberReader lnr = new LineNumberReader(new FileReader("code.java"));
  String line;
  while ((line = lnr.readLine()) != null) {
      System.out.println(lnr.getLineNumber() + ": " + line);
  }
  ```

### ✅ `PushbackInputStream` & `PushbackReader`
- **Extends**: `FilterInputStream` / `Reader`
- **Purpose**: Lets you “push back” a byte/character — useful for parsers that need to peek ahead.
- **Real-time use**: Implementing custom parsers (e.g., XML, JSON tokenizer).
  ```java
  PushbackReader pr = new PushbackReader(new FileReader("input.txt"), 1);
  int ch = pr.read();
  if (ch == '{') {
      pr.unread(ch); // put it back
      // start parsing object...
  }
  ```

### ✅ `StreamTokenizer`
- **Extends**: `Object`
- **Purpose**: Tokenizes input stream into words, numbers, symbols.
- **Real-time use**: Legacy parser for simple grammars (mostly obsolete — use regex or ANTLR now).
  ```java
  StreamTokenizer st = new StreamTokenizer(new FileReader("script.txt"));
  while (st.nextToken() != StreamTokenizer.TT_EOF) {
      if (st.ttype == StreamTokenizer.TT_WORD) {
          System.out.println("Word: " + st.sval);
      }
  }
  ```

---
# 🧩 GROUP 9: MISCELLANEOUS — Specialized Tools

### ✅ `RandomAccessFile`
- **Extends**: `Object`
- **Purpose**: Read/write anywhere in a file — supports seeking.
- **Real-time use**: Editing large binary files (e.g., databases, media files), log rotation.
  ```java
  RandomAccessFile raf = new RandomAccessFile("data.db", "rw");
  raf.seek(1024); // jump to position
  raf.writeInt(42);
  ```

### ✅ `Console`
- **Extends**: `Object`
- **Purpose**: Accesses the system console for secure password input.
- **Real-time use**: CLI apps requiring passwords (avoids echoing).
  ```java
  Console cons = System.console();
  char[] password = cons.readPassword("Enter password: ");
  ```

> ⚠️ `System.console()` returns `null` if running in IDE or non-interactive shell — use `Scanner` instead.

---
# 🧩 GROUP 10: ABSTRACT BASE CLASSES — The Foundation

You asked earlier about abstract classes — here are the key ones again:

| Class                  | Extends        | Purpose |
|------------------------|----------------|---------|
| `InputStream`          | `Object`       | Base for all byte input |
| `OutputStream`         | `Object`       | Base for all byte output |
| `Reader`               | `Object`       | Base for character input |
| `Writer`               | `Object`       | Base for character output |
| `FilterInputStream`    | `InputStream`  | Wraps another stream to filter bytes |
| `FilterOutputStream`   | `OutputStream` | Wraps another stream to filter bytes |
| `FilterReader`         | `Reader`       | Wraps another reader to filter text |
| `FilterWriter`         | `Writer`       | Wraps another writer to filter text |

> 🎯 These enable **polymorphism** and **composition** — you can plug any stream into a buffered wrapper, decorator, etc.

---
# 🧩 GROUP 11: INTERFACES & EXCEPTIONS — Supporting Elements

### ✅ `Serializable`
- **Interface**: Not in your list, but critical for `ObjectInputStream`/`ObjectOutputStream`.

### ✅ `Closeable`, `AutoCloseable`
- **Interfaces**: All streams implement these — allows `try-with-resources`.

### ✅ Exceptions:
- `IOException` — base for most I/O errors
- `FileNotFoundException` — specific to file access
- `EOFException` — when reading past end of stream

---
# 🔍 FINAL REFLECTION — When Would You Actually Use These?

Here’s a quick decision tree:

> 📌 **Need to read/write a file?**  
→ Use `FileInputStream`/`FileOutputStream` for binary, `BufferedReader`/`BufferedWriter` for text.

> 📌 **Need fast I/O?**  
→ Always wrap with `BufferedXXX` — unless you’re doing low-level control.

> 📌 **Need to serialize objects?**  
→ Use `ObjectInputStream`/`ObjectOutputStream` — but beware security risks.

> 📌 **Need to process text line-by-line?**  
→ `BufferedReader.readLine()`

> 📌 **Need to write logs or user-friendly output?**  
→ `PrintWriter` or `PrintStream`

> 📌 **Need to parse structured binary data?**  
→ `DataInputStream`/`DataOutputStream`

> 📌 **Need to communicate between threads?**  
→ `PipedInputStream`/`PipedOutputStream` — or better, use `BlockingQueue`.

> 📌 **Need to edit a file randomly?**  
→ `RandomAccessFile`

> 📌 **Need to hide password input?**  
→ `Console.readPassword()`

3. **Buffering matters** → Unbuffered streams are slow for small reads/writes.

4. **Don’t mix byte/char streams** without adapters — it causes encoding bugs.

