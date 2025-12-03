# 🧪 CHALLENGE #1: **The Log Anonymizer**

## 📜 Scenario  
You're building a CLI tool for GDPR compliance.  
It must:
- Read a **large log file** (e.g., `app.log`, could be 10GB+)  
- Replace all email addresses (`\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b`) with `[REDACTED]`  
- Write the result to a new file (`app.anon.log`)  
- **Requirements**:
  - Must be memory-efficient (no loading entire file into RAM)  
  - Must preserve line endings and non-email content exactly  
  - Must handle multi-line logs (though emails won’t span lines)  
  - Must be robust — if it fails, it shouldn’t corrupt output  
  - Bonus: report how many emails were redacted

> 💡 *Why this is hard:*  
> - Regex on streams? Streams read bytes/chars — regex works on strings.  
> - Buffer boundaries: what if an email is split between two buffer reads?  
> - Performance: naive `replace()` on full lines is OK, but what if a line is 1MB long?

## ✅ Your Task (Step-by-Step)

1. **Choose your I/O classes** — which ones will you use? Why?  
   (e.g., `BufferedReader`? `FileInputStream` + manual buffering? `Scanner`?)

2. **Sketch the core algorithm** — how will you avoid missing emails across buffer boundaries?

3. **Write pseudocode or Java code** for the main loop.

4. **Handle errors safely** — what if disk fills mid-write?

5. **Bonus**: Add progress reporting (e.g., “Processed 42 MB…”).