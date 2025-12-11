package chapter5_threadscheduling;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSchedulingDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("⏱️  JAVA THREAD SCHEDULING: The Illusion of Control");
        System.out.println("=".repeat(70));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("JVM: " + System.getProperty("java.version"));
        System.out.println();

        // 1️⃣ Priority myth-buster
        demoPriorityUnreliability();

        // 2️⃣ yield() experiment
        demoYieldEffect();

        // 3️⃣ Starvation vs. fairness
        demoStarvationAndFairLock();

        // 4️⃣ Modern best practice: task scheduling
        demoTaskScheduling();

        System.out.println("=".repeat(70));
        System.out.println("✅ Scheduling truths demonstrated.");
    }

    // ────────────────────────────────────────────────────────────────
    // 1️⃣ Thread Priorities: Hope vs. Reality
    // Run 10x — observe how often HIGH wins
    // ────────────────────────────────────────────────────────────────
    static void demoPriorityUnreliability() throws InterruptedException {
        System.out.println("🔴 DEMO 1: Thread Priorities Are Unreliable");

        final int RUNS = 5;
        int highWins = 0;

        for (int run = 1; run <= RUNS; run++) {
            CountDownLatch latch = new CountDownLatch(2);
            long[] counts = new long[2]; // [low, high]

            Thread low = new Thread(() -> {
                long c = 0;
                while (!Thread.currentThread().isInterrupted() && c < 50_000_000L) c++;
                counts[0] = c;
                latch.countDown();
            }, "Low-Pri");

            Thread high = new Thread(() -> {
                long c = 0;
                while (!Thread.currentThread().isInterrupted() && c < 50_000_000L) c++;
                counts[1] = c;
                latch.countDown();
            }, "High-Pri");

            // Set priorities
            low.setPriority(Thread.MIN_PRIORITY);
            high.setPriority(Thread.MAX_PRIORITY);

            Instant start = Instant.now();
            low.start(); high.start();

            // Let them run ~200ms, then interrupt
            Thread.sleep(200);
            low.interrupt(); high.interrupt();

            latch.await(1, TimeUnit.SECONDS); // Wait for both

            Duration elapsed = Duration.between(start, Instant.now());
            boolean highWon = counts[1] > counts[0];
            if (highWon) highWins++;

            System.out.printf("   Run %d (%4d ms): Low=%8d, High=%8d → %s%n",
                run, elapsed.toMillis(), counts[0], counts[1],
                highWon ? "✅ High won" : "❌ Low won");
        }

        System.out.printf("   ➡️  High-priority won %d/%d runs (%.0f%%)%n",
            highWins, RUNS, (highWins * 100.0 / RUNS));
        System.out.println("   → Not deterministic! OS scheduler dominates.");
    }

    // ────────────────────────────────────────────────────────────────
    // 2️⃣ Thread.yield(): Does it help?
    // Compare spin loops with/without yield
    // ────────────────────────────────────────────────────────────────
    static void demoYieldEffect() throws InterruptedException {
        System.out.println("\n🟡 DEMO 2: Thread.yield() — Polite Suggestion");

        AtomicBoolean flag = new AtomicBoolean(false);

        // Without yield
        long noYieldTime = timeSpinLoop(flag, false);
        flag.set(false);
        Thread.sleep(100);

        // With yield
        long withYieldTime = timeSpinLoop(flag, true);

        System.out.printf("   Without yield: %4d ms%n", noYieldTime);
        System.out.printf("   With yield   : %4d ms%n", withYieldTime);
        System.out.printf("   → yield() %s%n",
            withYieldTime < noYieldTime ? "helped" :
            withYieldTime > noYieldTime ? "hurt" : "no effect");
        System.out.println("   (Highly platform-dependent!)");
    }

    static long timeSpinLoop(AtomicBoolean flag, boolean useYield) throws InterruptedException {
        Thread spinner = new Thread(() -> {
            while (!flag.get()) {
                if (useYield) Thread.yield();
            }
        }, "Spinner");

        Instant start = Instant.now();
        spinner.start();

        // Let spinner run, then set flag after 50ms
        Thread.sleep(50);
        flag.set(true);

        spinner.join();
        return Duration.between(start, Instant.now()).toMillis();
    }

    // ────────────────────────────────────────────────────────────────
    // 3️⃣ Starvation vs. Fair Lock
    // Show how unfair lock can starve, and how fair lock prevents it
    // ────────────────────────────────────────────────────────────────
    static void demoStarvationAndFairLock() throws InterruptedException {
        System.out.println("\n⚖️  DEMO 3: Starvation and Fair Locks");

        // 🔴 Unfair (default) lock: starvation possible
        System.out.println("   🔴 Unfair Lock (default):");
        testLock(new ReentrantLock(false), 1000);

        // 🟢 Fair lock: FIFO, no starvation
        System.out.println("   🟢 Fair Lock (true):");
        testLock(new ReentrantLock(true), 1000);
    }

    static void testLock(ReentrantLock lock, int iterations) throws InterruptedException {
        Thread greedy = new Thread(() -> {
            for (int i = 0; i < iterations; i++) {
                lock.lock();
                try {
                    // Tiny critical section
                } finally {
                    lock.unlock();
                }
            }
        }, "Greedy");

        Thread[] patient = new Thread[5];
        CountDownLatch latch = new CountDownLatch(patient.length);

        for (int i = 0; i < patient.length; i++) {
            final int id = i;
            patient[i] = new Thread(() -> {
                lock.lock();
                try {
                    System.out.printf("      Patient-%d got lock%n", id);
                } finally {
                    lock.unlock();
                }
                latch.countDown();
            }, "Patient-" + id);
        }

        // Start greedy first to "hog" lock
        greedy.start();
        Thread.sleep(10); // Let greedy acquire first

        // Start patients
        for (Thread p : patient) p.start();

        // Wait for patients (max 2s)
        if (!latch.await(2, TimeUnit.SECONDS)) {
            System.out.println("      ❌ Some patients starved!");
        }
        greedy.interrupt();
    }

    // ────────────────────────────────────────────────────────────────
    // 4️⃣ Modern Best Practice: Schedule Tasks, Not Threads
    // Compare manual threads vs. ExecutorService
    // ────────────────────────────────────────────────────────────────
    static void demoTaskScheduling() throws InterruptedException {
        System.out.println("\n✅ DEMO 4: Task Scheduling (ExecutorService)");

        int tasks = 1000;
        int poolSize = Runtime.getRuntime().availableProcessors();

        // ⚠️ Manual threads: unbounded, high overhead
        Instant start = Instant.now();
        Thread[] manual = new Thread[tasks];
        for (int i = 0; i < tasks; i++) {
            final int id = i;
            manual[i] = new Thread(() -> {
                if (id % 200 == 0) System.out.print("."); // Progress
            });
            manual[i].start();
        }
        for (Thread t : manual) t.join();
        long manualTime = Duration.between(start, Instant.now()).toMillis();

        // ✅ ExecutorService: controlled, reusable
        start = Instant.now();
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        CountDownLatch latch = new CountDownLatch(tasks);

        for (int i = 0; i < tasks; i++) {
            pool.submit(() -> {
                if (latch.getCount() % 200 == 0) System.out.print("*");
                latch.countDown();
            });
        }
        latch.await();
        pool.shutdown();
        long poolTime = Duration.between(start, Instant.now()).toMillis();

        System.out.println();
        System.out.printf("   Manual threads (%d): %4d ms%n", tasks, manualTime);
        System.out.printf("   Thread pool     (%d): %4d ms%n", poolSize, poolTime);
        System.out.println("   → Pool reuses threads, avoids creation overhead.");
    }
}