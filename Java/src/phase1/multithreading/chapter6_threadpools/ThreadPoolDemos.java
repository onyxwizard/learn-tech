package chapter6_threadpools;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


public class ThreadPoolDemos {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        
        System.out.println("🏊‍♂️ THREAD POOLS: Reuse, Don’t Recreate");
        System.out.println("=".repeat(70));
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println();

        // 1️⃣ Fixed Thread Pool — CPU-bound work
        demoFixedThreadPool();

        // 2️⃣ Cached Thread Pool — I/O burst
        demoCachedThreadPool();

        // 3️⃣ Single-Thread Executor — Sequential logging
        demoSingleThreadExecutor();

        // 4️⃣ Scheduled Thread Pool — Periodic tasks
        demoScheduledThreadPool();

        // 5️⃣ Custom ThreadPoolExecutor — Bounded, backpressure
        demoCustomThreadPool();

        // 6️⃣ (Java 21+) Virtual Threads — Future of concurrency
        if (Runtime.version().feature() >= 21) {
            demoVirtualThreads();
        } else {
            System.out.println("💡 Virtual Threads require Java 21+ (current: " 
                + Runtime.version().feature() + ")");
        }

        System.out.println("=".repeat(70));
        System.out.println("✅ Thread pool strategies demonstrated.");
    }

    // ────────────────────────────────────────────────────────────────
    // 1️⃣ Fixed Thread Pool: CPU-bound work (e.g., image processing)
    // ✅ Steady throughput, predictable resource use
    // ⚠️ Risk: I/O wait → underutilized threads
    // ────────────────────────────────────────────────────────────────
    static void demoFixedThreadPool() throws InterruptedException, ExecutionException {
        System.out.println("✅ DEMO 1: Fixed Thread Pool (CPU-bound)");
        
        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);

        // Simulate CPU work: compute primes
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 1_000_000; i < 1_000_010; i++) {
            final int num = i;
            futures.add(pool.submit(() -> countPrimesUpTo(num)));
        }

        System.out.print("   Computing primes...");
        long start = System.nanoTime();
        for (Future<Integer> f : futures) {
            System.out.print(" " + f.get()); // Blocks until done
        }
        long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("\n   ✅ Done in " + ms + " ms using " + nThreads + " threads");

        pool.shutdown();
    }

    static int countPrimesUpTo(int n) {
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (isPrime(i)) count++;
        }
        return count;
    }

    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // ────────────────────────────────────────────────────────────────
    // 2️⃣ Cached Thread Pool: Bursty I/O (e.g., HTTP requests)
    // ✅ Scales to demand, idle threads expire
    // ⚠️ Risk: Unbounded → OOM under sustained load
    // ────────────────────────────────────────────────────────────────
    static void demoCachedThreadPool() throws InterruptedException {
        System.out.println("\n✅ DEMO 2: Cached Thread Pool (I/O burst)");

        ExecutorService pool = Executors.newCachedThreadPool();

        // Simulate 50 short I/O tasks
        CountDownLatch latch = new CountDownLatch(50);
        for (int i = 0; i < 50; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    // Simulate I/O: 10-30ms network call
                    Thread.sleep(10 + (int)(Math.random() * 20));
                    System.out.printf("   [%s] Request %2d served%n", 
                        Thread.currentThread().getName(), id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        System.out.println("   ✅ All 50 requests handled");
        pool.shutdown();
    }

    // ────────────────────────────────────────────────────────────────
    // 3️⃣ Single-Thread Executor: Sequential logging
    // ✅ Guaranteed order, no locks, no races
    // ────────────────────────────────────────────────────────────────
    static void demoSingleThreadExecutor() throws IOException, InterruptedException {
        System.out.println("\n✅ DEMO 3: Single-Thread Executor (Safe Logging)");

        Path logFile = Files.createTempFile("demo-log-", ".txt");
        ExecutorService logger = Executors.newSingleThreadExecutor(
            r -> new Thread(r, "Logger-Thread")
        );

        // 10 threads submit log entries concurrently
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            final int id = i;
            new Thread(() -> {
                logger.submit(() -> {
                    try {
                        String entry = String.format("[%s] Log %d%n", 
                            Instant.now(), id);
                        Files.write(logFile, entry.getBytes(), 
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                latch.countDown();
            }).start();
        }

        latch.await();
        logger.shutdown();
        logger.awaitTermination(1, TimeUnit.SECONDS);

        // Verify order & content
        List<String> lines = Files.readAllLines(logFile);
        System.out.println("   ✅ Log file has " + lines.size() + " entries (in submission order):");
        lines.forEach(System.out::print);
        Files.deleteIfExists(logFile);
    }

    // ────────────────────────────────────────────────────────────────
    // 4️⃣ Scheduled Thread Pool: Periodic tasks
    // ✅ Reliable timing, multiple threads
    // ⚠️ Critical: Catch exceptions to avoid silent death!
    // ────────────────────────────────────────────────────────────────
    static void demoScheduledThreadPool() throws InterruptedException {
        System.out.println("\n✅ DEMO 4: Scheduled Thread Pool (Periodic Tasks)");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        AtomicInteger counter = new AtomicInteger();
        
        // Safe periodic task (with exception handling)
        ScheduledFuture<?> refresh = scheduler.scheduleAtFixedRate(() -> {
            try {
                int c = counter.incrementAndGet();
                System.out.println("   🔄 Cache refresh #" + c);
                if (c == 3) throw new RuntimeException("Simulated failure");
            } catch (Exception e) {
                System.err.println("   ❗ Refresh failed: " + e.getMessage());
                // Do NOT rethrow — prevents scheduler thread death
            }
        }, 0, 1, TimeUnit.SECONDS);

        // One-time cleanup
        scheduler.schedule(() -> {
            System.out.println("   🧹 Cleanup task");
            refresh.cancel(false);
            scheduler.shutdown();
        }, 4, TimeUnit.SECONDS);

        scheduler.awaitTermination(5, TimeUnit.SECONDS);
    }

    // ────────────────────────────────────────────────────────────────
    // 5️⃣ Custom ThreadPoolExecutor: Bounded, backpressure
    // ✅ Production-grade: bounded, named, self-throttling
    // ────────────────────────────────────────────────────────────────
    static void demoCustomThreadPool() throws InterruptedException {
        System.out.println("\n✅ DEMO 5: Custom ThreadPoolExecutor (Backpressure)");

        // Bounded pool: 2 core, 4 max, 20-queue, caller-runs fallback
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20),
            r -> new Thread(r, "api-worker-" + COUNTER.incrementAndGet()),
            new ThreadPoolExecutor.CallerRunsPolicy() // Self-throttling
        );

        // Simulate burst: 30 tasks (20 in queue + 4 threads + 6 caller-runs)
        AtomicInteger handled = new AtomicInteger();
        for (int i = 0; i < 30; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    Thread.sleep(50); // Simulate work
                    handled.incrementAndGet();
                    System.out.printf("   [%s] Task %2d done%n", 
                        Thread.currentThread().getName(), id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("   ✅ " + handled.get() + "/30 tasks completed");
        System.out.println("   → CallerRunsPolicy prevented rejection!");
    }

    // ────────────────────────────────────────────────────────────────
    // 6️⃣ Virtual Threads (Java 21+): Millions of tasks, no tuning
    // ✅ Lightweight, no pool config, blocks cheaply
    // ────────────────────────────────────────────────────────────────
    static void demoVirtualThreads() throws InterruptedException {
        System.out.println("\n🚀 DEMO 6: Virtual Threads (Java 21+)");

        // Traditional platform threads: OOM at ~2000 threads
        int platformThreads = (int) Math.min(2000, 500 + Runtime.getRuntime().maxMemory() / 1_000_000);
        System.out.println("   Platform threads: max ~" + platformThreads + " before OOM");

        // Virtual threads: 10,000 easily
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CountDownLatch latch = new CountDownLatch(10_000);
            
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(10); // Blocks virtual thread (not OS thread!)
                        if (i % 2000 == 0) System.out.print(".");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            });

            latch.await(3, TimeUnit.SECONDS);
            System.out.println("\n   ✅ 10,000 virtual threads handled!");
            System.out.println("   → No tuning, no OOM, same ExecutorService API");
        }
    }
}