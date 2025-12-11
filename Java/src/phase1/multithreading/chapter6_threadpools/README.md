# 🏊‍♂️ **Thread Pools: Reuse, Don’t Recreate**

## 🧩 Core Idea: Separation of Concerns
| Role | Responsibility |
|------|----------------|
| **Task (`Runnable`/`Callable`)** | *What* to do (pure logic) |
| **Executor (`ExecutorService`)** | *How* to run it (thread management, queuing, lifecycle) |

✅ **Benefits**:
- ⚡ **Lower latency**: No thread creation per task  
- 📉 **Resource control**: Cap threads (e.g., 10, not 10,000)  
- 🧹 **Automatic cleanup**: Graceful shutdown, rejected task handling  
- 📈 **Throughput tuning**: Match pool size to workload (CPU vs. I/O bound)

---

## 🛠️ Java’s `ExecutorService` Hierarchy

```java
Executor
 └── ExecutorService
      ├── AbstractExecutorService
      │    ├── ThreadPoolExecutor        ← The powerhouse (customizable)
      │    └── ScheduledThreadPoolExecutor
      └── ForkJoinPool                   ← For recursive parallelism
```

Let’s explore the **4 built-in factory methods** — and when to use each.

---

## ✅ 1. `newFixedThreadPool(n)` — Steady Workload

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
```

- ✅ **Fixed size**: Exactly `n` threads  
- 🔄 **Reuse**: Idle threads pick up new tasks  
- 📥 **Unbounded queue**: `LinkedBlockingQueue` — tasks wait if all busy  
- 🎯 **Best for**: CPU-bound or mixed workloads with predictable load

### Example: Image processing (4-core machine)
```java
ExecutorService pool = Executors.newFixedThreadPool(
    Runtime.getRuntime().availableProcessors() // ← 4 on quad-core
);

List<Future<String>> futures = new ArrayList<>();
for (String img : imagePaths) {
    futures.add(pool.submit(() -> processImage(img)));
}

// Wait for all
for (Future<String> f : futures) {
    System.out.println(f.get()); // Blocks until done
}
pool.shutdown();
```

⚠️ **Risk**: If tasks are I/O-bound, threads spend time *waiting* — underutilized cores.

---

## ✅ 2. `newCachedThreadPool()` — Bursty Workload

```java
ExecutorService pool = Executors.newCachedThreadPool();
```

- 🌊 **Dynamic size**: 0 → unbounded (scales up/down)  
- ⏳ **Idle timeout**: Threads die after 60s idle  
- 📥 **Synchronous queue**: No queue — new task → new thread (if none idle)  
- 🎯 **Best for**: Short-lived, I/O-heavy tasks (e.g., HTTP handlers)

### Example: Web server request handling
```java
ExecutorService serverPool = Executors.newCachedThreadPool();

// Simulate 100 incoming requests
for (int i = 0; i < 100; i++) {
    int reqId = i;
    serverPool.submit(() -> {
        // Simulate I/O: DB call, API fetch
        try { Thread.sleep(10 + (int)(Math.random() * 20)); } 
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("Served request " + reqId + " on " 
            + Thread.currentThread().getName());
    });
}
serverPool.shutdown();
```

⚠️ **Risk**: Unbounded growth → OOM if load > capacity.

> ✅ **Fix in Java 19+**: Use `Executors.newThreadPerTaskExecutor()` for true unbounded (but still managed).

---

## ✅ 3. `newSingleThreadExecutor()` — Sequential Execution

```java
ExecutorService single = Executors.newSingleThreadExecutor();
```

- 🔒 **Exactly 1 thread**  
- 📥 **Unbounded queue**: Tasks run *in order*  
- 🎯 **Best for**:  
  - Event logging  
  - Stateful tasks (no concurrency needed)  
  - Replacing `synchronized` blocks with serial execution

### Example: Safe logging without locks
```java
ExecutorService logger = Executors.newSingleThreadExecutor();

// From any thread:
logger.submit(() -> {
    Files.write(logFile, ("[" + Instant.now() + "] " + msg + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
});
```

✅ Guarantees:  
- No `ConcurrentModificationException`  
- Log entries in submission order  
- No lock contention

---

## ✅ 4. `newScheduledThreadPool(n)` — Delayed/Periodic Tasks

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
```

- 🕒 **Schedule one-time or recurring tasks**  
- 🔁 **Fixed pool size** (unlike `Timer`, which uses 1 thread)  
- 🎯 **Best for**: Polling, cache refresh, heartbeat

### Example: Cache auto-refresh every 5s
```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

// Refresh cache every 5 seconds
scheduler.scheduleAtFixedRate(
    this::refreshCache,   // Runnable
    0,                    // initial delay
    5,                    // period
    TimeUnit.SECONDS
);

// One-time cleanup after 30s
scheduler.schedule(this::cleanup, 30, TimeUnit.SECONDS);
```

⚠️ **Critical**: Tasks must **not throw exceptions** — they kill the scheduler thread!  
✅ **Always wrap**:
```java
scheduler.scheduleAtFixedRate(() -> {
    try {
        refreshCache();
    } catch (Exception e) {
        log.error("Cache refresh failed", e);
        // Don't rethrow!
    }
}, 0, 5, SECONDS);
```

---

## ⚙️ Going Deeper: `ThreadPoolExecutor` — Full Control

The factory methods are convenient — but sometimes you need precision.

```java
new ThreadPoolExecutor(
    int corePoolSize,      // Min threads to keep alive
    int maximumPoolSize,   // Max threads allowed
    long keepAliveTime,    // Idle time before shrinking (above core)
    TimeUnit unit,
    BlockingQueue<Runnable> workQueue,
    ThreadFactory threadFactory,
    RejectedExecutionHandler handler
);
```

### 🔧 Tuning for I/O-bound work (e.g., microservices)
```java
int cores = Runtime.getRuntime().availableProcessors();
int ioThreads = cores * 2; // Heuristic: 2x for I/O wait

ExecutorService pool = new ThreadPoolExecutor(
    cores,          // core: keep at least #cores alive
    ioThreads,      // max: scale up for I/O bursts
    60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(100), // Bounded queue!
    new CustomThreadFactory("api-worker-"),
    new ThreadPoolExecutor.CallerRunsPolicy() // Fallback
);
```

### 📉 Why **bounded queues** matter:
| Queue Type | Risk |
|-----------|------|
| `LinkedBlockingQueue()` (unbounded) | OOM if producers >> consumers |
| `ArrayBlockingQueue(n)` (bounded) | Forces backpressure — callers block or reject |

#### 🛑 Rejection Policies (when queue + max threads full):
| Policy | Behavior |
|--------|----------|
| `AbortPolicy` (default) | Throws `RejectedExecutionException` |
| `CallerRunsPolicy` | **Smart fallback**: Task runs on *submitting thread* (slows producer) |
| `DiscardPolicy` | Silently drop task |
| `DiscardOldestPolicy` | Drop oldest in queue, add new |

✅ **Production tip**: `CallerRunsPolicy` is often safest — it’s *self-throttling*.

---

## 🧪 Real-World Demo: Web Crawler with Backpressure

```java
public class WebCrawler {
    // Bounded pool: 10 threads, 20-queue, caller-runs fallback
    private final ExecutorService pool = new ThreadPoolExecutor(
        5, 10, 60L, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(20),
        r -> {
            Thread t = new Thread(r, "crawler-" + COUNTER.getAndIncrement());
            t.setDaemon(true);
            return t;
        },
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private final Set<String> visited = ConcurrentHashMap.newKeySet();

    public void crawl(String url) {
        if (!visited.add(url)) return; // Already seen

        pool.submit(() -> {
            try {
                String html = fetch(url);
                List<String> links = extractLinks(html);
                links.parallelStream()
                     .filter(this::isRelevant)
                     .forEach(this::crawl); // Recursive — but bounded!
            } catch (Exception e) {
                System.err.println("Failed: " + url);
            }
        });
    }

    // Shutdown gracefully
    public void shutdown() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
        }
    }
}
```

✅ Benefits:  
- No OOM (bounded threads + queue)  
- Self-throttling under load (`CallerRunsPolicy`)  
- Graceful shutdown

---

## 🧭 Thread Pool Best Practices

| Do ✅ | Don’t ❌ |
|------|----------|
| Use `try { ... } finally { pool.shutdown(); }` | Forget to shut down → JVM hangs |
| Prefer bounded queues for production | Use unbounded queues blindly |
| Name threads (`ThreadFactory`) | Leave as "pool-1-thread-1" (debugging hell) |
| Handle exceptions in tasks | Let them bubble (kills thread!) |
| Match pool size to workload: <br> - CPU: `N_cores` <br> - I/O: `N_cores * (1 + wait/compute)` | Use `newCachedThreadPool()` for CPU work |

> 🔢 **Pool size formula (for mixed workloads)**:  
> \[
> \text{threads} = N_{\text{cores}} \times \left(1 + \frac{\text{wait time}}{\text{compute time}}\right)
> \]  
> e.g., DB call (10ms wait) + 2ms compute → ratio = 5 → 4 cores × 6 = **24 threads**

---

## 🚀 The Future: Virtual Threads (Project Loom)

Java 21+ introduces **virtual threads** — lightweight threads managed by JVM:

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 10_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(1000); // Blocks virtual thread, not OS thread!
            System.out.println(i);
            return i;
        });
    });
} // Auto-shutdown
```

✅ **Game-changer**:  
- Millions of concurrent tasks  
- No pool tuning needed  
- Existing `ExecutorService` API — just swap factory

> We can dive deeper into Loom if you’d like — it’s the biggest concurrency shift since Java 5.
