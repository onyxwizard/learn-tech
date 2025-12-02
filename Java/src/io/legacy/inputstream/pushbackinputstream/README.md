# 🔹 Lab 7: `PushbackInputStream` — The Parser’s Peek Tool

## 🎯 Goal:  
Understand how compilers, JSON parsers, and network protocols **look at the next byte** to decide what to do — *without advancing the stream*.

## 💡 Core Idea:  
> `PushbackInputStream` lets you **unread** bytes — like a “rewind 1 step” button.  
> You read a byte → realize “oops, this belongs to the next token” → push it back.

This enables **lookahead** (e.g., “is the next char `'{'` or `'['`?” in JSON).

## 🛠️ Step 1: A Mini Tokenizer — Detect Numbers vs Words

Imagine a simple format:  
```
123abc456def
```
→ We want tokens: `123`, `abc`, `456`, `def`

Without lookahead, you’d read `'1'` → assume word → wrong!

**Solution**: Read 1 char → if digit, keep reading digits; else, push it back and read word.

## 🛠️ Step 2: Run This Tokenizer

```java
import java.io.*;

public class Lab7 {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Lab 7: PushbackInputStream — Lookahead for Parsing ===\n");

        String input = "123abc456def";
        System.out.println("Input: \"" + input + "\"\nTokens:");

        try (PushbackInputStream pbis = new PushbackInputStream(
                new ByteArrayInputStream(input.getBytes()))) {

            int tokenCount = 1;
            int b;

            while ((b = pbis.read()) != -1) {
                if (Character.isDigit(b)) {
                    // Start of number — consume all digits
                    StringBuilder num = new StringBuilder();
                    num.append((char) b);
                    while ((b = pbis.read()) != -1 && Character.isDigit(b)) {
                        num.append((char) b);
                    }
                    // b is now non-digit → push it back!
                    if (b != -1) pbis.unread(b);
                    System.out.printf("  %d. NUMBER: %s%n", tokenCount++, num);
                } else {
                    // Start of word — consume all letters
                    StringBuilder word = new StringBuilder();
                    word.append((char) b);
                    while ((b = pbis.read()) != -1 && Character.isLetter(b)) {
                        word.append((char) b);
                    }
                    if (b != -1) pbis.unread(b);
                    System.out.printf("  %d. WORD: %s%n", tokenCount++, word);
                }
            }
        }
    }
}
```

### ▶️ Output:
```
=== Lab 7: PushbackInputStream — Lookahead for Parsing ===

Input: "123abc456def"
Tokens:
  1. NUMBER: 123
  2. WORD: abc
  3. NUMBER: 456
  4. WORD: def
```

✅ **It worked!**  
- After reading `'a'` following `123`, it pushed `'a'` back so the *word* loop could start fresh.

## 🔍 How `unread()` Works

- Internally, `PushbackInputStream` has a **pushback buffer** (default size = 1 byte)
- `unread(int b)` puts a byte *back at the front* of the stream
- Next `read()` returns that byte first

You can even unread multiple bytes:
```java
pbis.unread(new byte[]{ 'a', 'b', 'c' });  // now next reads: 'a', 'b', 'c', ...
```

> ⚠️ **Limitation**: Default buffer = 1 byte. For more, use:  
> `new PushbackInputStream(in, 10)` → 10-byte pushback buffer.


# 🧪 Real-World Analogy: The Compiler

Consider Java code:  
```java
int x = 10;
```

A tokenizer must:
1. Read `'i'` → not enough  
2. Read `'n'`, `'t'` → now “int” → keyword!  
3. Read space → discard  
4. Read `'x'` → identifier  
5. Read `'='` → operator  
6. Read `'1'` → start number… but **what if it’s `10L`?**  
   → Read `'1'`, `'0'`, then see `'L'` → “aha, long literal!” → push `'L'` back? No — consume it.  
   → But if it’s `10+`, after `10`, see `'+'` → push `'+'` back for operator parser.

→ **Lookahead drives syntax decisions.**


## 📦 Where You’ll See This

| Tool/Library | Use of `PushbackInputStream` |
|--------------|------------------------------|
| **Java Compiler (`javac`)** | Tokenizing source code |
| **`StreamTokenizer`** (legacy) | Built on `PushbackInputStream` |
| **Custom protocol parsers** | e.g., detect magic number, then switch mode |
| **BOM detection** | Read first 2–3 bytes → if UTF-8 BOM (`EF BB BF`), skip it |

Example: Skip UTF-8 BOM (Byte Order Mark):
```java
PushbackInputStream pbis = new PushbackInputStream(in, 3);
int b1 = pbis.read(), b2 = pbis.read(), b3 = pbis.read();
if (b1 == 0xEF && b2 == 0xBB && b3 == 0xBF) {
    // BOM found — do nothing (skip)
} else {
    // Not BOM — push all back
    pbis.unread(new byte[]{ (byte)b3, (byte)b2, (byte)b1 });
}
```

## 🧩 Updated Big Picture

```
Disk / Network / RAM
        ↓
    Raw InputStream
        ↓
BufferedInputStream      ← performance
        ↓
PushbackInputStream      ← lookahead for parsing
        ↓
DataInputStream          ← structured binary
        ↓
InputStreamReader(UTF_8) ← text (if needed later)
```

Note: Order matters!  
→ You usually buffer *first*, then pushback *on top*.