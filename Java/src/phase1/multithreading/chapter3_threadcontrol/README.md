
# 🛠️ **Part IV: Thread Control & Coordination**

#### 🔑 Core Question:  
> *If threads run independently, how do we express dependencies like:*  
> - “Wait until this download finishes before unzipping”  
> - “Don’t check for updates more than once per second”  
> - “Stop this background task if the user cancels”?

Java gives us precise, low-level tools — but they must be used *intentionally*.

Let’s categorize them:

| Purpose | Key Methods | Acts On |
|--------|-------------|---------|
| **Pause execution** | `Thread.sleep(millis)` | Current thread |
| **Voluntarily yield CPU** | `Thread.yield()` | Current thread |
| **Wait for another thread** | `thread.join()`, `thread.join(timeout)` | Calling thread |
| **Request cancellation** | `thread.interrupt()` | Target thread |
| **Self-query** | `Thread.currentThread()`, `isAlive()`, `getState()` | Current thread |

Note:  
- **Instance methods** (e.g., `t.join()`) act on *another* thread (`t`).  
- **Static methods** (e.g., `Thread.sleep()`) act on *the current thread*.

Let’s unpack the most important ones — with *intent* and *gotchas*.

---

### 🛑 1. `Thread.sleep(millis)` — “Pause me for a while”

```java
try {
    Thread.sleep(1000);  // Current thread sleeps for ~1s
} catch (InterruptedException e) {
    // Must handle!
}
```

✅ **Purpose**:  
- Simulate work (e.g., polling delay)  
- Rate-limit operations  
- Let other threads run (cooperative scheduling)

⚠️ **Critical Rules**:  
1. **It’s a static method** — `t.sleep()` is misleading! You’re always sleeping *yourself*.  
2. **It can be interrupted** → throws `InterruptedException`  
3. **It does NOT release locks** — if called inside `synchronized`, the lock is *held* while sleeping!

🔍 **Reflection**:  
Why is holding locks during `sleep()` dangerous?  
➡️ Other threads waiting for the same lock get blocked — defeating concurrency.

➡️ **Best practice**: Sleep *outside* synchronized blocks.

---

### 🤝 2. `thread.join()` — “Wait for you to finish”

```java
Thread downloader = new Thread(downloadTask);
downloader.start();

// In main thread:
downloader.join();  // ← Blocks here until downloader finishes
System.out.println("Now safe to process file");
```

✅ **Purpose**:  
- Enforce ordering (Task B after Task A)  
- Graceful shutdown (wait for workers before exiting)

Variants:
- `join()` → wait forever  
- `join(500)` → wait up to 500ms, then resume (even if thread still alive)

🧠 **Analogy**:  
Like `await` in async/await — but blocking, not non-blocking.

⚠️ **Watch out**:  
- Calling `join()` on *yourself* → deadlock (`t.join()` from inside `t`’s `run()`).  
- Don’t join on the UI thread — freezes the interface!

---

### 🚨 3. `thread.interrupt()` — “Please stop when convenient”

This is Java’s *cooperative cancellation* mechanism.

#### How it works:
- Sets the **interrupt status** (a boolean flag) on the target thread.
- If the target is in `WAITING`/`TIMED_WAITING` (e.g., `sleep()`, `wait()`), it *immediately* throws `InterruptedException` and *clears* the flag.

✅ **Correct idiom**:
```java
// In the worker thread:
while (!Thread.currentThread().isInterrupted()) {
    // Do chunk of work
    try {
        Thread.sleep(100);  // Checkpoint: can be interrupted here
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Restore flag!
        return; // or break
    }
}
```

🔑 **Golden Rules**:
1. **Never ignore `InterruptedException`** — it’s a *request to stop*.  
2. **Always restore the interrupt status** (`Thread.currentThread().interrupt()`) if you catch and don’t exit.  
3. Busy loops *must* check `isInterrupted()` — otherwise, no way to cancel!

❌ **Anti-pattern**:
```java
try { Thread.sleep(1000); } 
catch (InterruptedException e) { 
    // empty — thread becomes "undeletable"!
}
```

---

### 👻 4. Daemon Threads — “Background helpers”

```java
Thread logger = new Thread(logTask);
logger.setDaemon(true);  // ← Must be set BEFORE start()
logger.start();
```

✅ **What it means**:  
- Daemon threads are *servants* — they don’t prevent JVM shutdown.  
- When *all non-daemon threads* finish, the JVM exits — **killing all daemon threads immediately**, even mid-`run()`.

💡 **Use cases**:  
- Log flushers  
- Monitoring/heartbeat threads  
- Cache cleanup (best-effort)

⚠️ **Never use for**:  
- Saving user data, network writes, file I/O — could be truncated on exit!

🔍 **Check**: `t.isDaemon()` returns `true` if daemon.

---

### 🧪 Let’s Analyze Your `ThreadClassDemo` — Deeper Dive

Recall:
```java
Runnable hello = new DisplayMessage("Hello");
Thread thread1 = new Thread(hello);
thread1.setDaemon(true);   // ← Critical!
thread1.start();

// ... later
thread3.join();  // ← Main thread waits for thread3
```

❓ **Why is `setDaemon(true)` essential here?**  
→ Because `DisplayMessage.run()` has `while(true)` — infinite loop!  
→ Without daemon, the JVM would *never exit* — even after `main()` ends — because non-daemon threads are still running.

✅ So: daemon + infinite loop = safe background chatter.

Also note:
- `thread3.join()` ensures the program doesn’t end until the *first* `GuessANumber` finishes.  
- `thread4` runs *concurrently* with program exit — but since it’s non-daemon, the JVM waits for it too! (Because `main()` ends *after* starting `thread4`, but `thread4` is still alive.)

Wait — is that true?  
Let’s trace `main()`’s end:

```java
System.out.println("main() is ending..."); 
// ← main() exits here, but:
//   - thread1 & thread2: daemon → killed
//   - thread3: already joined → dead
//   - thread4: non-daemon & still alive → JVM waits!
```

✅ So the program *does* wait for `thread4` — good, since it prints results.

🧠 **Socratic twist**:  
What if `thread4` were also set to `setDaemon(true)`?  
→ The JVM could exit *before* it finds the number — output cut off.

---

### 🧭 Summary: Thread Control Cheat Sheet

| Method | Purpose | Safe to Call From | Key Caution |
|-------|---------|-------------------|-------------|
| `Thread.sleep(m)` | Pause current thread | Any thread | Holds locks; check interruption |
| `thread.join()` | Wait for `thread` | Any thread | Don’t join yourself or UI thread |
| `thread.interrupt()` | Request cancellation | Any thread | Target must *cooperate* |
| `Thread.yield()` | Suggest “let others run” | Current thread | No guarantee; rarely needed |
| `setDaemon(true)` | Background thread | Before `start()` | Never for critical work |