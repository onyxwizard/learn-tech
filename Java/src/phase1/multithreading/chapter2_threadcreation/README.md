# ⚙️ **Part III: Creating & Launching Threads**

# Step 0: The Fundamental Question  
Every thread needs **code to run**. In Java, that code lives in a method with this signature:

```java
public void run()
```

But — where does this method live?

Java offers two answers:

| Strategy | Where `run()` lives | Relationship to `Thread` |
|----------|---------------------|---------------------------|
| ✅ **`implements Runnable`** | In *your* task class | Your class *has-a* `Thread` (composition) |
| ⚠️ **`extends Thread`** | In *your subclass* of `Thread` | Your class *is-a* `Thread` (inheritance) |

Let’s explore both — but start with the *design thinking* behind them.

---

## ✅ Approach 1: `implements Runnable` *(The Preferred Way)*

### 🔧 Step-by-step — reconstructed from first principles:

1. **Define the *task***  
   → What work should be done concurrently?  
   → Encapsulate it in a class that *only* knows *what* to do — not *how* it’s scheduled.

   ```java
   class FileDownloader implements Runnable {
       private String url;
       public FileDownloader(String url) { this.url = url; }
       
       @Override
       public void run() {        // ← The task’s entry point
           System.out.println("Downloading: " + url);
           // ... actual download logic
       }
   }
   ```

2. **Create the *executor***  
   → Now, *assign* this task to a real thread:

   ```java
   Runnable task = new FileDownloader("https://example.com/data.zip");
   Thread worker = new Thread(task, "Downloader-Thread");  // ← Composition!
   ```

3. **Launch**  
   ```java
   worker.start();  // ← JVM + OS spin up native thread → calls task.run()
   ```

🔍 **Why is this better?**  
- ✅ Your `FileDownloader` *is not* a thread — it’s a *task*. Clean separation of concerns.  
- ✅ You preserve single inheritance (Java doesn’t allow `extends Thread` *and* `extends JFrame`, for example).  
- ✅ Easier to test: `new FileDownloader(...).run()` can be called *synchronously* in unit tests.  
- ✅ Scales to executors: `ExecutorService.submit(task)` expects `Runnable`/`Callable`.

> 🧠 *Socratic insight:*  
> In OOP, **composition > inheritance** when modeling “has-a” relationships.  
> A downloader *has a* thread (to run on), but it *is not* a thread.

---

## ⚠️ Approach 2: `extends Thread`

Now, the alternative — and *why* it’s discouraged:

```java
class FileDownloaderThread extends Thread {  // ← Inheritance!
    private String url;
    public FileDownloaderThread(String url) { this.url = url; }
    
    @Override
    public void run() {
        System.out.println("Downloading: " + url);
        // ... logic
    }
}

// Usage:
Thread t = new FileDownloaderThread("...");
t.start();
```

Looks simpler — but hides design debt.

🔍 **Reflection**:  
What happens if you later want your downloader to *also* be a Swing component (e.g., `JPanel`)?

➡️ You *can’t* — because Java doesn’t support multiple inheritance.  
`class FileDownloader extends Thread, JPanel` → **compile error**.

Also:  
- You inherit *all* of `Thread`’s 50+ methods — most irrelevant to downloading.  
- Harder to reuse the *task* in thread pools or async frameworks.

💡 Exception: Tiny demos or overriding *thread behavior itself* (e.g., custom `start()` logic — rare!).

---

## 🧪 Side-by-Side Comparison: Your Examples Revisited

Let’s refactor your original `RunnableDemo` to highlight the pattern:

### ✅ Clean `Runnable` (task-only)
```java
// Task: Count down and print
class CountdownTask implements Runnable {
    private final String name;
    
    CountdownTask(String name) { this.name = name; }
    
    @Override
    public void run() {
        System.out.println("Running " + name);
        try {
            for (int i = 4; i > 0; i--) {
                System.out.println("Thread: " + name + ", " + i);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status!
            System.out.println(name + " interrupted.");
        }
        System.out.println(name + " exiting.");
    }
}

// Launcher:
public class Launcher {
    public static void main(String[] args) {
        Thread t1 = new Thread(new CountdownTask("Thread-1"), "Thread-1");
        Thread t2 = new Thread(new CountdownTask("Thread-2"), "Thread-2");
        t1.start();
        t2.start();
    }
}
```

✅ Notice:
- `CountdownTask` has *zero* thread-related fields (`private Thread t` is gone!).  
- No custom `start()` method — we use `Thread.start()` directly.  
- Cleaner, more testable, more reusable.

---

## 🎯 Critical Pitfall: `start()` vs `run()` — The #1 Beginner Mistake

```java
Thread t = new Thread(task);
t.run();   // ❌ NEVER DO THIS!
```

❓ **What happens?**  
→ `run()` executes *in the current thread* — **no concurrency!**  
→ It’s just a regular method call. The OS never gets involved.

✅ Only `t.start()`:
- Requests a new call stack  
- Registers with OS scheduler  
- Eventually → JVM invokes `run()` *on the new thread*

🧠 **Mnemonic**:  
> **`start()` → *Starts a new thread* that calls `run()`**  
> **`run()` → *Runs on the current thread* — like any method**
