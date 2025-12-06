# 🧱 **Phase 1: `java.nio` — Buffers (The Data Container)**

### 🔍 **What Problem Does It Solve?**
`java.io` streams read/write *one byte at a time* → too slow for bulk I/O.  
→ NIO introduces **batch processing**: read 8KB at once, process in memory, then write.

### 🧩 **Core Idea: Buffer = Fixed-size container with *state***
A buffer isn’t just an array — it has:
- `capacity`: max size (fixed)
- `position`: where to read/write next
- `limit`: max position for current operation
- `mark`: saved position (for `reset()`)

> 🎯 **Think of it like a delivery truck**:
> - `capacity` = truck size (e.g., 8000 kg)  
> - `position` = how much is loaded so far  
> - `limit` = max allowed for this trip  
> - `flip()` = switch from *loading* mode to *unloading* mode  

### 📚 Key Buffer Types
| Class | Holds | Use Case |
|-------|-------|----------|
| `ByteBuffer` | `byte` | **Most common** — files, network, binary data |
| `CharBuffer` | `char` | Text processing (UTF-16) |
| `IntBuffer`, `LongBuffer`, etc. | primitives | Binary protocols, memory-mapped files |

### 🔁 The Buffer State Machine (Critical!)

| Operation | `position` | `limit` | When to Use |
|-----------|------------|---------|-------------|
| `allocate(1024)` | `0` | `1024` | Create empty buffer |
| `.put(data)` | advances | unchanged | Fill with data |
| `.flip()` | `0` | old `position` | ✅ **After filling, before reading** |
| `.get(destination)` | advances | unchanged | Drain data |
| `.clear()` | `0` | `capacity` | ✅ **After reading, before refilling** |
| `.compact()` | `remaining()` | `capacity` | Keep unread data (streaming) |

#### ✅ Minimal Example: Fill → Flip → Drain
```java
ByteBuffer buf = ByteBuffer.allocate(8);
buf.put("Hi".getBytes());   // position=2, limit=8
buf.flip();                 // position=0, limit=2 ← ready to read
byte[] out = new byte[2];
buf.get(out);               // position=2, limit=2
System.out.println(new String(out)); // "Hi"
buf.clear();                // position=0, limit=8 ← ready to reuse
```

> 💡 **Golden Rule**: **`flip()` after write, `clear()` after read.**  
> Forget this → silent bugs.


# 🎯 **Goal**:  
> *Understand `Buffer` so well that you can predict its state after any sequence of operations — and never misuse `flip()`/`clear()`/`compact()` again.*

## 🧱 **1. What Is a Buffer? (The Mental Model)**

### ❌ **Misconception**:  
> _“A buffer is just a wrapper around a byte array.”_

### ✅ **Reality**:  
> **A `Buffer` is a *stateful cursor* over a fixed block of memory.**  
> It’s not about *storage* — it’s about *control*.

Think of it as a **film reel editor’s workstation**:

| Component | Film Analogy | Buffer Field |
|----------|--------------|--------------|
| Reel of film | Raw data (bytes/chars) | Backing array (`byte[]`, `char[]`, etc.) |
| Current frame marker | Where editing starts | `position` |
| End of selected segment | Where editing stops | `limit` |
| Total reel length | Max capacity | `capacity` |
| Bookmark clip | Saved position | `mark` |

You don’t just “read the reel” — you **mark**, **cut**, **rewind**, **advance**.

---

## 📐 **2. The 4 State Variables (The Heart of Buffer)**

Every `Buffer` subclass (`ByteBuffer`, `CharBuffer`, etc.) has these **4 mutable fields**:

| Field | Meaning | Immutable? | Initial Value (after `allocate(n)`) |
|-------|---------|------------|-------------------------------------|
| `capacity` | Max number of elements it can hold | ✅ Yes | `n` |
| `position` | Index of *next* element to read/write | ❌ No | `0` |
| `limit` | First index *not* to read/write | ❌ No | `capacity` |
| `mark` | Saved `position` (for `reset()`) | ❌ No | *undefined* |

> 📌 **Invariant**: `0 ≤ mark ≤ position ≤ limit ≤ capacity`

Violate this → exceptions (`BufferUnderflow`, `InvalidMark`, etc.)

---

## 🔄 **3. The 6 Core Operations (State Transitions)**

Let’s map each method to *what it changes* — using a table and diagram.

### 📊 State Transition Table

| Method | `position` | `limit` | `mark` | When to Use |
|--------|------------|---------|--------|-------------|
| `clear()` | `0` | `capacity` | undefined | **After reading** — ready to `put()` again |
| `flip()` | `0` | old `position` | undefined | **After writing, before reading** |
| `rewind()` | `0` | unchanged | undefined | **Re-read from start** (e.g., retry send) |
| `compact()` | `remaining()` | `capacity` | undefined | **Streaming: keep unread data** |
| `mark()` | unchanged | unchanged | = `position` | Save current spot |
| `reset()` | = `mark` | unchanged | unchanged | Return to marked spot |

> ⚠️ `reset()` fails if `mark` is undefined → `InvalidMarkException`.

---

### 🎞️ **Visual State Walkthrough**  
*Scenario: Read 5 bytes from file, then write them to network*

#### Step 0: Allocate
```java
ByteBuffer buf = ByteBuffer.allocate(8);
```
```
[ . . . . . . . . ]  ← backing array (8 slots)
 ↑
position=0
limit=8
capacity=8
```

#### Step 1: Read from channel (fill)
```java
channel.read(buf); // reads 5 bytes: "Hello"
```
```
[H e l l o . . . ]
           ↑
position=5   ← next write will go here
limit=8
```

#### Step 2: Prepare to read → `flip()`
```java
buf.flip();
```
```
[H e l l o . . . ]
 ↑
position=0   ← next read starts here
limit=5      ← only 5 valid bytes
```

#### Step 3: Read data
```java
byte[] out = new byte[buf.remaining()]; // 5
buf.get(out); // reads 5 bytes
```
```
[H e l l o . . . ]
           ↑
position=5
limit=5
```

#### Step 4: Done reading → `clear()` for reuse
```java
buf.clear();
```
```
[H e l l o . . . ]  ← old data still there (but ignored)
 ↑
position=0
limit=8
```

✅ Ready for next `read()`.

---

## 🧪 **4. `compact()` — The Streaming Secret Weapon**

What if you only read *part* of the buffer?

#### Scenario: Buffer has `[A B C D]`, you read `A B`, want to keep `C D` for next read.

##### ❌ Wrong: `clear()`  
→ Loses `C D`.

##### ✅ Right: `compact()`
```java
buf.position(2); // after reading A B
buf.compact();
```
Before:
```
[A B C D . . . . ]
     ↑
position=2, limit=4
```
After `compact()`:
```
[C D . . . . . . ]
     ↑
position=2 (remaining=2), limit=8
```
→ Now `read()` will append after `D`.

> 💡 **Use `compact()` in streaming loops** (e.g., network proxy, log tailer).

---

## 🧬 **5. Buffer Types & Hierarchy**

All buffers extend `java.nio.Buffer`. Key subclasses:

```
Buffer
├── ByteBuffer      → most important (files, network)
├── CharBuffer
├── ShortBuffer
├── IntBuffer
├── LongBuffer
├── FloatBuffer
└── DoubleBuffer
```

### 🔑 Special: `ByteBuffer` Has Extra Powers

| Method | Purpose |
|--------|---------|
| `asCharBuffer()` | View bytes as chars (with current charset) |
| `asReadOnlyBuffer()` | Immutable view |
| `slice()` | Sub-buffer sharing backing array |
| `duplicate()` | New buffer, same content & array |

#### Example: Safe UTF-8 decode
```java
ByteBuffer buf = ByteBuffer.wrap("Hi".getBytes(UTF_8));
CharBuffer chars = UTF_8.newDecoder().decode(buf); // correct
// vs buggy:
String s = new String(buf.array()); // ignores position/limit!
```

---

## 🛑 **6. Common Pitfalls (And How to Avoid Them)**

| Mistake | Symptom | Fix |
|--------|---------|-----|
| Forget `flip()` after `put()` | `get()` returns 0 or garbage | Always `flip()` before reading what you wrote |
| Use `array()` blindly | Reads entire array, not just valid data | Use `get(byte[])` or check `position`/`limit` |
| Call `reset()` without `mark()` | `InvalidMarkException` | Only `reset()` after `mark()` |
| Share buffer across threads | Silent corruption | Confine to one thread, or synchronize |
| Use `remaining()` after `clear()` | Returns `capacity`, not 0 | Remember: `remaining() = limit - position` |

> 📌 **Golden Debug Tip**:  
> When in doubt, print buffer state:
> ```java
> System.out.printf("pos=%d lim=%d cap=%d%n", buf.position(), buf.limit(), buf.capacity());
> ```

---

## ✅ **7. Your Buffer Mastery Checklist**

Before moving on, can you predict the output?

### 🧩 Challenge 1:
```java
ByteBuffer b = ByteBuffer.allocate(4);
b.put((byte)1);
b.put((byte)2);
System.out.println(b.position()); // ?
b.flip();
System.out.println(b.remaining()); // ?
b.get();
System.out.println(b.position()); // ?
b.clear();
System.out.println(b.limit()); // ?
```

<details>
<summary>💡 Answer (try first!)</summary>

```
2   ← after two puts
2   ← flip sets limit=2, position=0 → remaining=2
1   ← after one get
4   ← clear sets limit=capacity=4
```
</details>


### 🧩 Challenge 2: Streaming Loop (Partial Read)
```java
ByteBuffer b = ByteBuffer.allocate(4);
b.put(new byte[]{1,2,3,4});
b.flip(); // [1,2,3,4], pos=0, lim=4
b.get(); // read 1
b.get(); // read 2 → pos=2
b.compact(); // ?
System.out.println(b.position()); // ?
System.out.println(b.remaining()); // ?
```

<details>
<summary>💡 Answer</summary>

After `compact()`:  
- Unread data (`3,4`) shifted to front  
- `position = 2` (remaining=2), `limit = 4`  
→ Output:  
```
2
2
```
</details>