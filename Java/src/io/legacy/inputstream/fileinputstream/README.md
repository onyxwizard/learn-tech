# 📦 What `FileInputStream` *Is*

> **Definition (from Javadoc)**:  
> *“A `FileInputStream` obtains input bytes from a file in a file system. What files are available or may be created depends on the host environment.”*

🔑 **Core Identity**:
- ✅ **Concrete subclass** of `InputStream`
- ✅ Represents a **raw, unbuffered, blocking** byte pipe from **disk → JVM heap**
- ✅ Wraps an OS-level **file descriptor** (e.g., `int fd` on Unix, `HANDLE` on Windows)
- ✅ **Not thread-safe** — sharing across threads without sync = corruption/race
- ✅ Implements `Closeable` → must be closed (prefer `try`-with-resources)

## 🧰 All Public Methods (JDK 21) — Explained by *Purpose*, Not Just Signature

| Method | Signature | What It **Really** Does | When to Use | Gotchas |
|--------|-----------|--------------------------|-------------|---------|
| **Constructor** | `FileInputStream(String name)` | Opens file for **reading only**; throws `FileNotFoundException` if missing/unreadable | Standard case — readable path as string | 🔴 Path is platform-dependent (use `Path` + `Files.newInputStream()` for modern code) |
| | `FileInputStream(File file)` | Same, but with `File` object (deprecated in favor of `Path`) | Legacy code | ⚠️ `File` is immutable but path-string-based |
| | `FileInputStream(Path path, OpenOption... options)` | **JDK 7+** — modern constructor (via `Files.newInputStream`) | ✅ **Preferred today** — supports `StandardOpenOption.READ`, etc. | Not a direct `FileInputStream` ctor — you get it via `Files.newInputStream(path)` |
| `read()` | `int read()` | Reads **one byte** from OS → returns `0..255` or `-1` (EOF) | Rarely used directly (inefficient) | 🔴 1 syscall per call! Avoid in loops. |
| `read(byte[])` | `int read(byte[] b)` | Delegates to `read(b, 0, b.length)` | Basic bulk read | May return < `b.length` (e.g., near EOF) |
| `read(byte[],int,int)` | `int read(byte[] b, int off, int len)` | Reads up to `len` bytes into `b[off..off+len-1]`; returns # read (≥0) or `-1` | ✅ Most common raw read | ✔️ Respect the return value — never assume full buffer filled |
| `skip(long)` | `long skip(long n)` | Advances file position by `n` bytes (via `lseek`/`SetFilePointer`) | Skip headers, known offsets | ⚠️ May skip fewer than `n` (e.g., near EOF); returns actual skipped |
| `available()` | `int available()` | Returns **estimate** of bytes that can be read *without blocking* | ❌ **Unreliable for files** — often returns full remaining size, but spec says *“no guarantee”* | 🔴 Don’t use to size buffers — use fixed buffer (e.g., `new byte[8192]`) |
| `close()` | `void close()` | Releases OS file descriptor; subsequent ops throw `IOException` | ✅ **Always call** — or use try-with-resources | 🔴 Unclosed streams leak **file descriptors** (not just memory!) → `TooManyOpenFilesError` |
| `getFD()` | `final FileDescriptor getFD()` | Returns low-level OS file descriptor (e.g., `int fd`) | JNI/native integration, debugging | ⚠️ Rarely needed in app code |
| `getChannel()` | `FileChannel getChannel()` | Returns NIO `FileChannel` **backed by same fd** | Bridge to NIO (e.g., memory-mapped I/O, async) | ✅ Powerful — `channel.map()`, `channel.position()`, etc. |
| `finalize()` | `protected void finalize()` | Legacy cleanup — calls `close()` if not already closed | ❌ **Avoid** — unpredictable, deprecated in JDK 9+ | 🔴 Never rely on finalization for resource cleanup |

> 📌 **Inherited from `InputStream` (but `FileInputStream` overrides key ones)**:
> - `mark(int)` → throws `IOException("mark/reset not supported")`
> - `reset()` → same
> - `markSupported()` → returns `false`


## 🧪 Real-World Usage Patterns

### ✅ Good: Modern, Safe, Efficient
```java
import java.nio.file.*;

Path path = Path.of("data.bin");
try (InputStream in = Files.newInputStream(path)) { // returns FileInputStream internally
    byte[] buffer = new byte[8192];
    int n;
    while ((n = in.read(buffer)) != -1) {
        process(buffer, 0, n);
    }
}
```

### ❌ Bad: Anti-Patterns
```java
// 1. No try-with-resources → leak!
FileInputStream fis = new FileInputStream("x");
byte[] b = fis.readAllBytes(); // still open after!

// 2. Ignoring read() return value
byte[] b = new byte[100];
fis.read(b); // may read only 10 bytes — rest is garbage!

// 3. Using available() to allocate
byte[] b = new byte[fis.available()]; // may be 0 or huge — unsafe!
fis.read(b);
```

## 🔍 Under the Hood: What Happens on `read()`?

1. JVM calls native method: `Java_java_io_FileInputStream_read0`
2. OS syscall invoked:  
   - Linux: `ssize_t read(int fd, void *buf, size_t count)`  
   - Windows: `ReadFile(HANDLE, ...)`  
3. OS copies bytes from **kernel page cache** → **JVM heap buffer**
4. Returns # bytes read (may be < requested if interrupted or near EOF)

➡️ **No magic. Just syscalls.**


## 🎯 When to Use `FileInputStream` (vs Alternatives)

| Use Case | Prefer `FileInputStream`? | Better Alternative |
|---------|----------------------------|--------------------|
| Reading binary data (images, serialized, custom format) | ✅ Yes | — |
| Reading text | ❌ No | `Files.newBufferedReader(path, UTF_8)` |
| High-throughput (10k+ files) | ⚠️ Only if buffered | `BufferedInputStream(new FileInputStream(...))` |
| Random access (seek + read) | ⚠️ Possible via `skip()` | ✅ `FileChannel` (via `getChannel()`) |
| Streaming huge files (>1GB) | ⚠️ Possible | ✅ `FileChannel.map()` (memory-mapped) |


## 🧭 Socratic Mastery Check

Before moving on, can you explain — *without looking up*:

1. Why does `read()` return `int`, not `byte`?  
2. Why does `markSupported()` return `false`?  
3. If you call `skip(1000)` on a 500-byte file, what does it return?  
4. What resource is leaked if you forget `close()`? (Hint: not memory!)  
5. How is `Files.newInputStream(path)` related to `FileInputStream`?