# 🧠 **Part V: When Threads Collide — And How to Prevent It**

## 🔍 Reflection: A Deceptively Simple Counter

```java
class Counter {
    private int count = 0;
    
    public void increment() {
        count = count + 1;  // Looks atomic — but is it?
    }
    
    public int get() { return count; }
}

// Two threads call increment() 1000 times each
// Expected final count: 2000
// Actual result? Try it — you’ll often get < 2000.
```

❓ **Why? Let’s decompose `count = count + 1` at the JVM level:**

1. `read count` → e.g., 42  
2. `add 1` → 43  
3. `write count = 43`

But with two threads, the OS may interleave:

| Thread A | Thread B |
|---------|----------|
| read → 42 |          |
|          | read → 42 |
| add → 43 |          |
| write → 43 |         |
|          | add → 43 |
|          | write → 43 |

➡️ **Lost update!** Both did +1, but count only increased by 1.

This is a **race condition**: outcome depends on *timing*, not logic.

🔍 **Key Insight**:  
> **Atomicity ≠ “one line of Java”**  
> It means: *“This sequence of operations appears to happen instantly — no thread sees a partial state.”*

So — how do we make `increment()` atomic?

---

## 🔒 Strategy 1: `synchronized` — The Intrinsic Lock

Java gives every object a built-in **monitor lock**.  
Use `synchronized` to enforce *mutual exclusion*:

```java
public synchronized void increment() {
    count = count + 1;
}
```

Or, more flexibly (locking on a private object):

```java
private final Object lock = new Object();

public void increment() {
    synchronized (lock) {
        count++;
    }
}
```

✅ **What it guarantees**:
- Only **one thread** can execute *any* `synchronized` block on the *same lock object* at a time.
- **Visibility**: Changes made inside a sync block are *visible* to the next thread entering a sync block on the same lock (prevents stale cached values).

⚠️ **What it does NOT guarantee**:
- Ordering of *which* waiting thread gets the lock next (no fairness).
- Protection if you forget to sync *all* accesses (e.g., `get()` must also be `synchronized`!).

---

## 🗣️ Strategy 2: Inter-Thread Communication — `wait()` / `notify()`

Sometimes, threads don’t just need *exclusion* — they need to **signal**.

Example: **Producer-Consumer**  
- Producer: generates data → puts in buffer  
- Consumer: takes data → processes it  
- Buffer has max size → producer must *wait* if full; consumer must *wait* if empty.

`wait()` / `notify()` let threads *pause* and *resume* based on state:

```java
public class BoundedBuffer {
    private final List<String> buffer = new ArrayList<>();
    private final int MAX = 5;

    public synchronized void put(String item) throws InterruptedException {
        while (buffer.size() == MAX) {
            wait();  // ← Releases lock & waits
        }
        buffer.add(item);
        notifyAll(); // ← Wake up waiting consumers
    }

    public synchronized String take() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();  // ← Releases lock & waits
        }
        String item = buffer.remove(0);
        notifyAll(); // ← Wake up waiting producers
        return item;
    }
}
```

🔑 **Critical Rules**:
1. Always call `wait()`/`notify()` **inside a `synchronized` block** on the *same object*.  
2. Always use `while`, not `if`, to check condition (spurious wakeups!).  
3. Prefer `notifyAll()` over `notify()` unless you’re certain only one thread needs waking.

🧠 **Analogy**:  
`wait()` = “I’m done with the lock; wake me when the condition might be true.”  
`notify()` = “The state changed — someone might now be able to proceed.”

---

## ⚠️ The Big Three Pitfalls — And How to Spot Them

| Pitfall | Symptoms | Prevention |
|--------|----------|------------|
| **1. Race Condition** | Intermittent wrong results, data corruption | Identify shared mutable state → protect with `synchronized`, `volatile`, or atomic classes |
| **2. Deadlock** | App freezes; threads stuck forever | Avoid nested locks; use lock ordering; `tryLock()` with timeout; tools: `jstack` |
| **3. Visibility Problem** | One thread doesn’t see another’s update | Use `synchronized`, `volatile`, or `final` fields; never share mutable state without coordination |

#### 🕳️ Deadlock Example (Classic):
```java
// Thread 1:          // Thread 2:
synchronized (A) {     synchronized (B) {
    synchronized (B) {     synchronized (A) {
        ...                 ...
    }                   }
}
```
Both hold one lock and wait for the other → eternal wait.

🔍 **Deadlock requires 4 conditions** (Coffman conditions):  
1. Mutual exclusion  
2. Hold and wait  
3. No preemption  
4. Circular wait  
→ Break *any one* to prevent deadlock.

---

## 🛡️ Beyond `synchronized`: Safer Alternatives

Raw `Thread` + `synchronized` is low-level. Java offers higher-level tools:

| Tool | Use Case | Why Better |
|------|----------|------------|
| `java.util.concurrent.atomic.*` | Counters, flags | Lock-free, high-performance (`AtomicInteger`, `AtomicBoolean`) |
| `volatile` | Single variable visibility (no atomicity) | Ensures reads/writes go to main memory; prevents reordering |
| `ReentrantLock` | Advanced locking (fairness, tryLock, condition vars) | More control than `synchronized` |
| `ExecutorService` | Managing thread pools | No manual thread creation; structured concurrency |
| `ConcurrentHashMap` | Shared map | Thread-safe without external sync |

✅ Example: Fix the counter *without* `synchronized`:
```java
private final AtomicInteger count = new AtomicInteger();

public void increment() {
    count.incrementAndGet();  // atomic, lock-free, fast
}
```

---

### 🧪 Let’s Debug a Realistic Scenario — Socratically

Imagine this logging system:

```java
class Logger {
    private final List<String> buffer = new ArrayList<>();
    
    public void log(String msg) {
        buffer.add(msg);           // (1)
        if (buffer.size() >= 10) {
            flush();               // (2)
        }
    }
    
    private void flush() {
        // write buffer to disk
        buffer.clear();            // (3)
    }
}
```

Two threads call `log()` concurrently.

❓ **Questions**:
1. Is there a race condition? Where?  
2. Could `flush()` be called twice unintentionally?  
3. Could `buffer.clear()` happen *while* another thread is adding?  
4. How would you fix it — minimally?

*(Pause. Trace interleavings.)*  

✅ **Answer**:  
- (1) and (3) are unsynchronized → `ConcurrentModificationException` or lost logs.  
- Yes, both threads could see `size() == 10` and call `flush()` → double-flush.  
- Fix: `synchronized` the entire `log()` method — or better, use `synchronized (this)` around the critical section.

---

### 🧭 Summary: Concurrency Mindset

| Principle | Action |
|---------|--------|
| 🔍 **Assume everything is shared** | Audit all fields: `final`? Immutable? Thread-confined? |
| 🛑 **No unprotected mutable shared state** | Sync, atomic, or isolate |
| 🔄 **Prefer immutable or thread-local data** | `String`, `LocalDate`, `ThreadLocal<T>` |
| 🧩 **Use high-level concurrency utilities** | `ExecutorService`, `ConcurrentHashMap`, `CompletableFuture` |
| 🛠️ **Test concurrency** | Use stress tests; tools: `jcstress`, `ThreadSanitizer`, `jstack` on hangs |