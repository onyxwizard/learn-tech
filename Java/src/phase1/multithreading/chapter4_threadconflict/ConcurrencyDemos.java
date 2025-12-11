package chapter4_threadconflict;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrencyDemos {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🧠 PART V: When Threads Collide — And How to Prevent It");
        System.out.println("=".repeat(70));

        // 1️⃣ Race Condition Demo: Unsafe Counter
        demoRaceCondition();

        // 2️⃣ Fix #1: synchronized
        demoSynchronizedCounter();

        // 3️⃣ Fix #2: AtomicInteger (lock-free)
        demoAtomicCounter();

        // 4️⃣ Fix #3: ReentrantLock (explicit locking)
        demoReentrantLockCounter();

        // 5️⃣ wait()/notify(): Producer-Consumer
        demoProducerConsumer();

        // 6️⃣ Deadlock Simulation + Avoidance
        demoDeadlock();

        System.out.println("=".repeat(70));
        System.out.println("✅ All concurrency demos completed.");
    }

    // ────────────────────────────────────────────────────────────────
    // 1️⃣ RACE CONDITION: Unprotected shared counter
    // Expected: 2000 | Actual: often 1200–1900 (non-deterministic!)
    // ────────────────────────────────────────────────────────────────
    static void demoRaceCondition() throws InterruptedException {
        System.out.println("\n🔴 DEMO 1: Race Condition (Unsynchronized Counter)");
        CounterUnsafe counter = new CounterUnsafe();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        }, "T1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        }, "T2");

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("   ❌ Final count: " + counter.get() + " (expected 2000)");
        System.out.println("   → Lost updates due to interleaving!");
    }

    static class CounterUnsafe {
        private int count = 0;
        public void increment() { count++; } // NOT atomic!
        public int get() { return count; }
    }

    // ────────────────────────────────────────────────────────────────
    // 2️⃣ FIX 1: synchronized (intrinsic lock)
    // Guarantees atomicity + visibility
    // ────────────────────────────────────────────────────────────────
    static void demoSynchronizedCounter() throws InterruptedException {
        System.out.println("\n🟢 DEMO 2: Fix with synchronized");
        CounterSync counter = new CounterSync();

        Thread t1 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });
        Thread t2 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("   ✅ Final count: " + counter.get() + " (always 2000)");
    }

    static class CounterSync {
        private int count = 0;
        public synchronized void increment() { count++; }
        public synchronized int get() { return count; } // Must sync reads too!
    }

    // ────────────────────────────────────────────────────────────────
    // 3️⃣ FIX 2: AtomicInteger (lock-free, high performance)
    // CAS (Compare-And-Swap) internally
    // ────────────────────────────────────────────────────────────────
    static void demoAtomicCounter() throws InterruptedException {
        System.out.println("\n🟢 DEMO 3: Fix with AtomicInteger");
        CounterAtomic counter = new CounterAtomic();

        Thread t1 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });
        Thread t2 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("   ✅ Final count: " + counter.get() + " (always 2000)");
    }

    static class CounterAtomic {
        private final AtomicInteger count = new AtomicInteger();
        public void increment() { count.incrementAndGet(); }
        public int get() { return count.get(); }
    }

    // ────────────────────────────────────────────────────────────────
    // 4️⃣ FIX 3: ReentrantLock (explicit, flexible locking)
    // Allows tryLock(), fairness, multiple condition variables
    // ────────────────────────────────────────────────────────────────
    static void demoReentrantLockCounter() throws InterruptedException {
        System.out.println("\n🟢 DEMO 4: Fix with ReentrantLock");
        CounterLock counter = new CounterLock();

        Thread t1 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });
        Thread t2 = new Thread(() -> { for (int i = 0; i < 1000; i++) counter.increment(); });

        t1.start(); t2.start();
        t1.join(); t2.join();

        System.out.println("   ✅ Final count: " + counter.get() + " (always 2000)");
    }

    static class CounterLock {
        private int count = 0;
        private final ReentrantLock lock = new ReentrantLock();

        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock(); // ALWAYS in finally!
            }
        }

        public int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    // ────────────────────────────────────────────────────────────────
    // 5️⃣ wait()/notify(): Producer-Consumer with Bounded Buffer
    // Shows *coordination*, not just exclusion
    // ────────────────────────────────────────────────────────────────
    static void demoProducerConsumer() throws InterruptedException {
        System.out.println("\n💬 DEMO 5: wait()/notify() — Producer-Consumer");

        BoundedBuffer buffer = new BoundedBuffer(2); // Small buffer to force waiting

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    buffer.put("Item-" + i);
                    System.out.println("   📥 Produced: Item-" + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    String item = buffer.take();
                    System.out.println("   📤 Consumed: " + item);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }, "Consumer");

        producer.start();
        Thread.sleep(100); // Let producer go first
        consumer.start();

        producer.join(); consumer.join();
        System.out.println("   ✅ All items produced & consumed.");
    }

    static class BoundedBuffer {
        private final List<String> buffer = new ArrayList<>();
        private final int capacity;

        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void put(String item) throws InterruptedException {
            while (buffer.size() == capacity) {
                wait(); // 🔑 Releases lock, waits for notify
            }
            buffer.add(item);
            notifyAll(); // 🔑 Wake up waiting consumers
        }

        public synchronized String take() throws InterruptedException {
            while (buffer.isEmpty()) {
                wait(); // 🔑 Releases lock, waits for notify
            }
            String item = buffer.remove(0);
            notifyAll(); // 🔑 Wake up waiting producers
            return item;
        }
    }

    // ────────────────────────────────────────────────────────────────
    // 6️⃣ DEADLOCK: Classic AB/BA lock ordering
    // Shows how to detect (jstack) and avoid (lock ordering)
    // ────────────────────────────────────────────────────────────────
    static void demoDeadlock() throws InterruptedException {
        System.out.println("\n⚠️  DEMO 6: Deadlock Simulation & Avoidance");

        final Object lockA = new Object();
        final Object lockB = new Object();

        // ❌ Dangerous: circular wait
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("   T1: Got lockA, waiting for lockB...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lockB) { // Will block forever
                    System.out.println("   T1: Got both locks!");
                }
            }
        }, "T1");

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("   T2: Got lockB, waiting for lockA...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                synchronized (lockA) { // Will block forever
                    System.out.println("   T2: Got both locks!");
                }
            }
        }, "T2");

        t1.start(); t2.start();
        Thread.sleep(500); // Let them deadlock

        System.out.println("   ❌ Deadlock detected! (T1 & T2 blocked)");
        System.out.println("      → Run `jstack <pid>` to confirm:");
        System.out.println("         \"Found 1 deadlock.\"");

        // ✅ Safe version: enforce global lock order (A before B)
        System.out.println("\n   ✅ Fix: Always acquire locks in same order (A → B)");
        Thread safeT1 = new Thread(() -> {
            synchronized (lockA) {
                synchronized (lockB) {
                    System.out.println("   SafeT1: Acquired A→B");
                }
            }
        });

        Thread safeT2 = new Thread(() -> {
            synchronized (lockA) { // ← Same order!
                synchronized (lockB) {
                    System.out.println("   SafeT2: Acquired A→B");
                }
            }
        });

        safeT1.start(); safeT2.start();
        safeT1.join(); safeT2.join();
        System.out.println("   ✅ No deadlock — safe ordering works.");
    }
}