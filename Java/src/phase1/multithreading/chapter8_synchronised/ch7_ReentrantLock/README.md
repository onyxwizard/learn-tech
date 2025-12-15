# 🔑 **ReentrantLock: Precision Control for Critical Sections**

## 🧩 **What “Reentrant” Actually Means**

> 🔍 *If a thread already holds a lock, can it acquire it again?*  
> — Yes. That’s **reentrancy**.

```java
ReentrantLock lock = new ReentrantLock();
lock.lock();      // Acquired (hold count = 1)
lock.lock();      // Re-acquired (hold count = 2) — no deadlock!
lock.unlock();    // (hold count = 1)
lock.unlock();    // (hold count = 0) — fully released
```

✅ **Why it matters**:  
- Allows safe recursive locking (e.g., `A()` calls `B()`, both sync on same lock)  
- Matches Java’s intrinsic locks (`synchronized` is also reentrant)

> ❌ **Misconception**: “Reentrant = fair” — no! Fairness is *separate*.

## ⚠️ **Critical Flaw in Your Example: `tryLock()` + `lock()`**

Your code:
```java
boolean lockAcquired = lockr.tryLock(); 
if (lockAcquired) {
   try {
      lockr.lock();   // ❌ DANGER: Double-lock!
      // ...
   } finally {
      lockr.unlock();
   }
}
```

#### 🔥 What’s wrong?
- `tryLock()` **already acquired the lock**  
- `lock()` tries to acquire it *again* → hold count = 2  
- But only **one** `unlock()` → lock is *not fully released*  
- Next thread blocks forever → **silent deadlock**

✅ **Correct usage patterns**:

| Pattern | Code |
|--------|------|
| **Blocking acquire** (most common) | ```lock.lock(); try { /* work */ } finally { lock.unlock(); }``` |
| **Try-with-timeout** | ```if (lock.tryLock(1, SECONDS)) { try { ... } finally { lock.unlock(); } }``` |
| **Non-blocking try** | ```if (lock.tryLock()) { try { ... } finally { lock.unlock(); } }``` |

> 🔑 **Golden Rule**:  
> **Every `lock()` / `tryLock()` must have exactly *one* matching `unlock()` in a `finally` block.**

---

## 📚 Your Examples — Refactored to Correct, Safe Usage

### 🔴 Without Lock: Interleaving (Correct)

Your first example shows **no shared state** (`data` is local to each call), so no race condition — just scheduling randomness.

✅ Output like:
```
1 2 3 4
5 6 7 8
10 11 12 13
```
→ Perfectly safe — no sync needed.

### 🟢 With `ReentrantLock`: Fixed & Safe

```java
import java.util.concurrent.locks.ReentrantLock;

class Thrd {
    // ✅ Fair lock (optional)
    private static final ReentrantLock LOCK = new ReentrantLock(true); // ← fair = true

    static void operation(int data) {
        // ✅ CORRECT: Blocking acquire (no tryLock + lock)
        LOCK.lock(); // Blocks until acquired
        try {
            for (int i = 1; i <= 4; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + (data + i - 1));
            }
            System.out.println("Hold count: " + LOCK.getHoldCount()); // Always 1 here
        } finally {
            LOCK.unlock(); // ✅ Exactly one unlock
        }
    }
}

// Threads (modern Runnable style)
class Worker implements Runnable {
    private final int base;
    Worker(int base) { this.base = base; }
    public void run() { Thrd.operation(base); }
}

public class TestThread {
    public static void main(String[] args) {
        new Thread(new Worker(1), "T1").start();
        new Thread(new Worker(5), "T2").start();
        new Thread(new Worker(10), "T3").start();
    }
}
```

#### ✅ Output (deterministic order with fair lock):
```
T1: 1
T1: 2
T1: 3
T1: 4
Hold count: 1
T2: 5
T2: 6
T2: 7
T2: 8
Hold count: 1
T3: 10
...
```

#### ⚠️ Without `fair = true` (default unfair):
- Order is **non-deterministic** (but still mutually exclusive)  
- Higher throughput, but possible starvation

---

## 🧭 **When to Use `ReentrantLock` vs. `synchronized`**

| Feature | `synchronized` | `ReentrantLock` |
|--------|----------------|-----------------|
| **Automatic lock release** | ✅ (on exit) | ❌ (must use `finally`) |
| **Fairness option** | ❌ | ✅ (`new ReentrantLock(true)`) |
| **Try-lock with timeout** | ❌ | ✅ (`tryLock(1, SECONDS)`) |
| **Interruptible lock acquisition** | ❌ | ✅ (`lockInterruptibly()`) |
| **Multiple condition variables** | ❌ (one per object) | ✅ (`newCondition()`) |
| **Hold count inspection** | ❌ | ✅ (`getHoldCount()`) |
| **Performance (Java 6+)** | ✅ Optimized (biased locking) | ✅ Comparable (slightly higher overhead) |

✅ **Use `synchronized` when**:
- You need simple mutual exclusion  
- You want automatic lock management  
- You don’t need advanced features

✅ **Use `ReentrantLock` when**:
- You need **fairness** (e.g., UI responsiveness)  
- You need **timeout** (`tryLock(timeout)`)  
- You need **multiple wait sets** (`Condition`)  
- You’re building a **custom synchronizer** (e.g., `Semaphore`, `CountDownLatch`)


## 🔍 **Deep Dive: `Condition` — The Real Power of `ReentrantLock`**

This is where `ReentrantLock` shines over `synchronized`:

```java
ReentrantLock lock = new ReentrantLock();
Condition notEmpty = lock.newCondition();
Condition notFull = lock.newCondition();

// Producer
lock.lock();
try {
    while (queue.isFull()) {
        notFull.await(); // ✅ Like wait(), but on specific condition
    }
    queue.add(item);
    notEmpty.signal();   // ✅ Like notify(), but precise
} finally {
    lock.unlock();
}

// Consumer
lock.lock();
try {
    while (queue.isEmpty()) {
        notEmpty.await();
    }
    Item item = queue.remove();
    notFull.signal();
    return item;
} finally {
    lock.unlock();
}
```

✅ **Why better than `wait()`/`notify()`**:
- Multiple conditions per lock (e.g., “buffer full” vs “buffer empty”)  
- No risk of signaling the wrong waiter  
- `await()` can be interrupted (`awaitInterruptibly()`)


## ✅ **Summary: ReentrantLock — The Right Way**

| Principle | Action |
|---------|--------|
| **Never mix `tryLock()` and `lock()`** | Use one or the other |
| **Always use `finally`** | Prevent leaked locks |
| **Prefer `synchronized` for simple cases** | Less error-prone |
| **Use `ReentrantLock` for advanced control** | Fairness, timeout, conditions |
| **Fair locks = lower throughput** | Only enable when needed |

> 🔑 **Golden Rule**:  
> **`ReentrantLock` gives power — but with great power comes great responsibility.**  
> If you don’t need its advanced features, `synchronized` is safer and cleaner.