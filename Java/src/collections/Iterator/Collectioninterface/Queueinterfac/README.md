# Java Queue Interface - Complete Guide & Cheatsheet

## 📋 Overview
The **Java Queue interface** (`java.util.Queue`) represents a **FIFO (First-In-First-Out)** data structure where elements are inserted at the **tail** and removed from the **head**. It's ideal for scenarios requiring ordered processing, task scheduling, and breadth-first traversal. The Queue interface extends `Collection` and provides specialized methods for queue operations.



## 🔑 Core Characteristics

### ✅ **FIFO Principle**
- First element inserted is the first removed
- Elements processed in insertion order
- Like a real-world queue (line)

### ✅ **Two Sets of Methods**
- **Throw-exception methods**: `add()`, `remove()`, `element()`
- **Return-null/boolean methods**: `offer()`, `poll()`, `peek()`

### ✅ **Bounded vs Unbounded**
- Some implementations have capacity limits
- `offer()` returns false when queue is full (bounded)
- `add()` throws exception when queue is full

### ✅ **Priority Variations**
- `PriorityQueue`: Elements ordered by priority
- `LinkedList`: Maintains insertion order
- `ArrayDeque`: Double-ended queue (Deque)

---

## ⚖️ Queue vs Stack vs List

| Feature | Queue | Stack | List |
|---------|-------|-------|------|
| **Order Principle** | FIFO (First-In-First-Out) | LIFO (Last-In-First-Out) | Index-based |
| **Primary Operations** | enqueue (add), dequeue (remove) | push (add), pop (remove) | add, get, set, remove |
| **Use Cases** | Task scheduling, BFS, messaging | Undo/redo, recursion, expression evaluation | General-purpose collections |
| **Interface** | `Queue` | `Deque` (as stack) | `List` |
| **Java Implementation** | `LinkedList`, `PriorityQueue`, `ArrayDeque` | `ArrayDeque`, `Stack` (legacy) | `ArrayList`, `LinkedList` |

---

## 🏗️ Queue Implementations

### 1. **`LinkedList`** (Most Common General Queue)
```java
Queue<String> queue = new LinkedList<>();
```
- **Backed by**: Doubly-linked list
- **Order**: Insertion order (FIFO)
- **Bounded**: ❌ No (unbounded)
- **Null Elements**: ✅ Allowed
- **Performance**: O(1) for enqueue/dequeue
- **Best for**: General-purpose FIFO queues

### 2. **`PriorityQueue`** (Priority-Based)
```java
Queue<Integer> pq = new PriorityQueue<>();
Queue<String> reversePq = new PriorityQueue<>(Comparator.reverseOrder());
```
- **Backed by**: Priority heap (balanced binary heap)
- **Order**: Natural ordering or Comparator
- **Bounded**: ❌ No (unbounded)
- **Null Elements**: ❌ Not allowed
- **Performance**: O(log n) for enqueue/dequeue
- **Best for**: Task scheduling, Dijkstra's algorithm

### 3. **`ArrayDeque`** (High-Performance Double-ended Queue)
```java
Queue<String> dequeAsQueue = new ArrayDeque<>();
Deque<String> deque = new ArrayDeque<>(); // Can be used as queue or stack
```
- **Backed by**: Resizable circular array
- **Order**: Insertion order
- **Bounded**: ❌ No (resizable)
- **Null Elements**: ❌ Not allowed
- **Performance**: O(1) amortized for all operations
- **Best for**: High-performance queues, stacks, deques

### 4. **`ConcurrentLinkedQueue`** (Thread-Safe)
```java
Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
```
- **Backed by**: Linked nodes
- **Thread-safe**: ✅ Yes (lock-free, CAS operations)
- **Bounded**: ❌ No
- **Null Elements**: ❌ Not allowed
- **Best for**: Multi-threaded producer-consumer scenarios

### 5. **`ArrayBlockingQueue`** (Bounded, Blocking)
```java
Queue<String> blockingQueue = new ArrayBlockingQueue<>(100);
```
- **Backed by**: Array
- **Bounded**: ✅ Yes (fixed capacity)
- **Thread-safe**: ✅ Yes (blocking operations)
- **Best for**: Thread pools, producer-consumer with capacity limits

### 6. **`LinkedBlockingQueue`** (Optionally Bounded, Blocking)
```java
Queue<String> unboundedBlocking = new LinkedBlockingQueue<>();
Queue<String> boundedBlocking = new LinkedBlockingQueue<>(100);
```
- **Backed by**: Linked nodes
- **Bounded**: Optional (constructor parameter)
- **Thread-safe**: ✅ Yes (two locks for put/take)
- **Best for**: Thread pools (Executors default)

### 7. **`PriorityBlockingQueue`** (Priority, Blocking)
```java
Queue<String> priorityBlocking = new PriorityBlockingQueue<>();
```
- **Backed by**: Priority heap
- **Thread-safe**: ✅ Yes
- **Unbounded**: ✅ Yes (automatically grows)
- **Best for**: Priority-based task scheduling in concurrent apps

---

## 📝 Creating Queues

### Basic Creation
```java
// LinkedList as Queue
Queue<String> linkedListQueue = new LinkedList<>();

// PriorityQueue (natural ordering)
Queue<Integer> priorityQueue = new PriorityQueue<>();

// PriorityQueue with custom Comparator
Queue<String> reversePriority = new PriorityQueue<>(Comparator.reverseOrder());
Queue<Person> personQueue = new PriorityQueue<>(Comparator.comparing(Person::getAge));

// ArrayDeque (recommended over LinkedList for single-threaded)
Queue<String> arrayDequeQueue = new ArrayDeque<>();

// With initial capacity
Queue<String> sizedQueue = new ArrayDeque<>(100);
Queue<Integer> sizedPriority = new PriorityQueue<>(100);

// From existing collection
List<String> list = Arrays.asList("A", "B", "C");
Queue<String> fromCollection = new LinkedList<>(list);
```

### Specialized Queues
```java
// Concurrent queues
Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(100);

// Bounded queues
Queue<String> boundedQueue = new ArrayBlockingQueue<>(50);

// DelayQueue (for scheduling)
DelayQueue<Delayed> delayQueue = new DelayQueue<>();

// SynchronousQueue (handoff queue)
BlockingQueue<String> syncQueue = new SynchronousQueue<>();
```

---

## 🛠️ Core Operations Cheatsheet

### **Operation Comparison Table**
| Operation | Throws Exception | Returns Special Value | Use When |
|-----------|------------------|----------------------|----------|
| **Insert** | `add(e)` | `offer(e)` | Prefer `offer()` for bounded queues |
| **Remove** | `remove()` | `poll()` | Prefer `poll()` to avoid exceptions |
| **Examine** | `element()` | `peek()` | Prefer `peek()` to avoid exceptions |

### 📥 **Adding Elements (Enqueue)**
```java
Queue<String> queue = new LinkedList<>();

// add() - throws exception if capacity restricted
boolean added = queue.add("A");  // true, throws IllegalStateException if full

// offer() - returns false if capacity restricted
boolean offered = queue.offer("B");  // true, returns false if full

// addAll() - add multiple elements
queue.addAll(Arrays.asList("C", "D", "E"));
```

### 📤 **Removing Elements (Dequeue)**
```java
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));

// remove() - throws NoSuchElementException if empty
String first = queue.remove();  // "A"

// poll() - returns null if empty
String second = queue.poll();   // "B"
String empty = queue.poll();    // null (if queue empty)

// remove(Object) - remove specific element
queue.add("X");
queue.add("Y");
boolean removed = queue.remove("X");  // true
```

### 👀 **Examining Elements (Peek)**
```java
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));

// element() - throws NoSuchElementException if empty
String head = queue.element();  // "A" (doesn't remove)

// peek() - returns null if empty
String peek = queue.peek();     // "A" (doesn't remove)

queue.clear();
String emptyPeek = queue.peek();    // null
// String emptyElement = queue.element(); // NoSuchElementException
```

### 📊 **Utility Operations**
```java
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));

// Size
int size = queue.size();  // 3

// Check empty
boolean isEmpty = queue.isEmpty();  // false

// Check contains
boolean hasA = queue.contains("A");  // true
boolean hasZ = queue.contains("Z");  // false

// Clear all elements
queue.clear();  // queue is now empty

// Convert to array
Object[] array = queue.toArray();
String[] stringArray = queue.toArray(new String[0]);

// Iterate (doesn't remove elements)
for (String element : queue) {
    System.out.println(element);  // A, B, C
}
```

---

## 🔄 Iterating Over Queues

### 1. **For-each Loop**
```java
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));

for (String element : queue) {
    System.out.println(element);  // A, B, C
    // Warning: Modifying queue during iteration may cause ConcurrentModificationException
}
```

### 2. **Iterator**
```java
Iterator<String> iterator = queue.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    System.out.println(element);
    // Can safely remove current element
    // iterator.remove();
}
```

### 3. **forEach (Java 8+)**
```java
queue.forEach(System.out::println);
// or
queue.forEach(element -> System.out.println(element));
```

### 4. **Stream API (Java 8+)**
```java
queue.stream()
     .filter(s -> s.startsWith("A"))
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

### 5. **While Loop with Poll (Destructive Iteration)**
```java
// This consumes the queue
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));
while (!queue.isEmpty()) {
    String element = queue.poll();  // Removes element
    System.out.println(element);    // Process element
}
// Queue is now empty
```

---

## 📊 Performance Comparison

### **Time Complexity**
| Operation | LinkedList | ArrayDeque | PriorityQueue | ArrayBlockingQueue |
|-----------|------------|------------|---------------|-------------------|
| `enqueue` (add/offer) | O(1) | O(1) amortized | O(log n) | O(1) |
| `dequeue` (remove/poll) | O(1) | O(1) | O(log n) | O(1) |
| `peek` (element/peek) | O(1) | O(1) | O(1) | O(1) |
| `contains` | O(n) | O(n) | O(n) | O(n) |
| `size` | O(1) | O(1) | O(1) | O(1) |

### **Memory Usage**
- **LinkedList**: Higher (node objects with pointers)
- **ArrayDeque**: Lower (contiguous array, better cache locality)
- **PriorityQueue**: Moderate (array-based heap)
- **ArrayBlockingQueue**: Fixed (pre-allocated array)

### **When to Use Which?**
- **Use LinkedList** when:
  - Need null elements
  - Already using LinkedList as List
  - Simple FIFO requirements

- **Use ArrayDeque** when:
  - High-performance single-threaded queue
  - Also need stack operations (LIFO)
  - Better memory locality needed

- **Use PriorityQueue** when:
  - Need priority-based ordering
  - Implementing Dijkstra, A* algorithms
  - Task scheduling by priority

- **Use ConcurrentLinkedQueue** when:
  - Non-blocking concurrent access
  - High contention scenarios
  - Unbounded concurrent queue

- **Use ArrayBlockingQueue** when:
  - Fixed capacity needed
  - Blocking producer-consumer
  - Thread pool work queues

- **Use LinkedBlockingQueue** when:
  - Executor service work queue
  - Optional bounded capacity
  - Higher throughput than ArrayBlockingQueue

---

## 🔧 Advanced Operations & Patterns

### **PriorityQueue with Custom Objects**
```java
class Task implements Comparable<Task> {
    private String name;
    private int priority;  // 1 = highest priority
    
    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    
    @Override
    public int compareTo(Task other) {
        return Integer.compare(this.priority, other.priority);
    }
    
    // Getters, toString, etc.
}

// Usage
Queue<Task> taskQueue = new PriorityQueue<>();
taskQueue.offer(new Task("Low priority", 3));
taskQueue.offer(new Task("High priority", 1));
taskQueue.offer(new Task("Medium priority", 2));

while (!taskQueue.isEmpty()) {
    Task task = taskQueue.poll();
    System.out.println(task);  // High, Medium, Low
}

// With Comparator
Queue<Task> reversePriority = new PriorityQueue<>(
    Comparator.comparing(Task::getPriority).reversed()
);
```

### **BFS (Breadth-First Search) Example**
```java
public void bfs(Node start) {
    Queue<Node> queue = new LinkedList<>();
    Set<Node> visited = new HashSet<>();
    
    queue.offer(start);
    visited.add(start);
    
    while (!queue.isEmpty()) {
        Node current = queue.poll();
        
        // Process current node
        System.out.println(current);
        
        // Visit neighbors
        for (Node neighbor : current.getNeighbors()) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                queue.offer(neighbor);
            }
        }
    }
}
```

### **Producer-Consumer Pattern**
```java
public class ProducerConsumer {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>(100);
    
    public void start() {
        // Producer thread
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    String item = "Item-" + i;
                    queue.put(item);  // Blocks if queue is full
                    System.out.println("Produced: " + item);
                    Thread.sleep(10);
                }
                queue.put("POISON_PILL");  // Signal completion
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Consumer thread
        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String item = queue.take();  // Blocks if queue is empty
                    if ("POISON_PILL".equals(item)) {
                        break;  // Stop consuming
                    }
                    System.out.println("Consumed: " + item);
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        producer.start();
        consumer.start();
    }
}
```

### **Task Scheduler with PriorityQueue**
```java
public class TaskScheduler {
    private final PriorityQueue<ScheduledTask> queue = new PriorityQueue<>();
    
    public void schedule(Runnable task, long delayMs) {
        long executeTime = System.currentTimeMillis() + delayMs;
        queue.offer(new ScheduledTask(task, executeTime));
    }
    
    public void run() {
        while (!queue.isEmpty()) {
            ScheduledTask next = queue.peek();
            if (next.getExecuteTime() <= System.currentTimeMillis()) {
                queue.poll().getTask().run();
            } else {
                try {
                    long sleepTime = next.getExecuteTime() - System.currentTimeMillis();
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    private static class ScheduledTask implements Comparable<ScheduledTask> {
        private final Runnable task;
        private final long executeTime;
        
        public ScheduledTask(Runnable task, long executeTime) {
            this.task = task;
            this.executeTime = executeTime;
        }
        
        @Override
        public int compareTo(ScheduledTask other) {
            return Long.compare(this.executeTime, other.executeTime);
        }
        
        public Runnable getTask() { return task; }
        public long getExecuteTime() { return executeTime; }
    }
}
```

### **Rate Limiter with Queue**
```java
public class RateLimiter {
    private final Queue<Long> requestTimes = new LinkedList<>();
    private final int maxRequests;
    private final long timeWindowMs;
    
    public RateLimiter(int maxRequests, long timeWindowMs) {
        this.maxRequests = maxRequests;
        this.timeWindowMs = timeWindowMs;
    }
    
    public synchronized boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        
        // Remove old requests outside time window
        while (!requestTimes.isEmpty() && 
               currentTime - requestTimes.peek() > timeWindowMs) {
            requestTimes.poll();
        }
        
        // Check if under rate limit
        if (requestTimes.size() < maxRequests) {
            requestTimes.offer(currentTime);
            return true;
        }
        
        return false;
    }
}

// Usage: 100 requests per minute
RateLimiter limiter = new RateLimiter(100, 60_000);
if (limiter.allowRequest()) {
    // Process request
} else {
    // Rate limit exceeded
}
```

---

## ⚡ Concurrent Queue Patterns

### **Multiple Producers, Single Consumer**
```java
public class MultiProducerSingleConsumer {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    
    public void start(int numProducers) {
        // Start producers
        for (int i = 0; i < numProducers; i++) {
            final int producerId = i;
            new Thread(() -> produce(producerId)).start();
        }
        
        // Start consumer
        new Thread(this::consume).start();
    }
    
    private void produce(int producerId) {
        try {
            while (running) {
                String item = "Item-from-" + producerId + "-" + System.currentTimeMillis();
                queue.put(item);
                Thread.sleep(100 + (int)(Math.random() * 100));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void consume() {
        try {
            while (running || !queue.isEmpty()) {
                String item = queue.poll(100, TimeUnit.MILLISECONDS);
                if (item != null) {
                    System.out.println("Consumed: " + item);
                    // Process item
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void stop() {
        running = false;
    }
}
```

### **Work Stealing Pattern**
```java
public class WorkStealingQueue {
    private final Deque<Runnable>[] queues;
    private final int numWorkers;
    
    @SuppressWarnings("unchecked")
    public WorkStealingQueue(int numWorkers) {
        this.numWorkers = numWorkers;
        this.queues = new ArrayDeque[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            queues[i] = new ArrayDeque<>();
        }
    }
    
    public void submit(int workerId, Runnable task) {
        queues[workerId].addLast(task);
    }
    
    public Runnable steal(int thiefId) {
        for (int i = 0; i < numWorkers; i++) {
            if (i != thiefId) {
                Deque<Runnable> queue = queues[i];
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        return queue.removeLast();  // Steal from opposite end
                    }
                }
            }
        }
        return null;
    }
    
    public Runnable poll(int workerId) {
        Deque<Runnable> queue = queues[workerId];
        synchronized (queue) {
            return queue.pollFirst();  // Process from own queue
        }
    }
}
```

---

## 💡 Best Practices & Common Pitfalls

### ✅ **Do:**
```java
// Use appropriate queue implementation
Queue<String> generalQueue = new ArrayDeque<>();  // Single-threaded, high performance
Queue<Task> priorityQueue = new PriorityQueue<>();  // Priority-based
BlockingQueue<String> concurrentQueue = new LinkedBlockingQueue<>();  // Multi-threaded

// Prefer offer/poll/peek over add/remove/element
Queue<String> queue = new ArrayDeque<>();
queue.offer("item");  // Returns false if full, doesn't throw
String item = queue.poll();  // Returns null if empty, doesn't throw
String peek = queue.peek();  // Returns null if empty, doesn't throw

// Check capacity for bounded queues
BlockingQueue<String> bounded = new ArrayBlockingQueue<>(10);
if (bounded.remainingCapacity() > 0) {
    bounded.offer("item");
}

// Use proper synchronization for non-concurrent queues
Queue<String> nonConcurrent = new LinkedList<>();
synchronized(nonConcurrent) {
    nonConcurrent.offer("item");
    String item = nonConcurrent.poll();
}

// Clear queue properly
queue.clear();  // O(n) for LinkedList, O(1) for ArrayDeque (just reset pointers)

// Use iterators for read-only traversal
for (String element : queue) {
    // Read element
}
```

### ❌ **Don't:**
```java
// Don't modify queue during iteration (except with iterator.remove())
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));
for (String s : queue) {
    queue.remove(s);  // ConcurrentModificationException!
}

// Don't assume LinkedList order in PriorityQueue
Queue<String> pq = new PriorityQueue<>(Comparator.reverseOrder());
pq.offer("C");
pq.offer("A");
pq.offer("B");
// Iteration order is not guaranteed!
for (String s : pq) {
    System.out.println(s);  // Might not be C, B, A
}
// Use poll() for correct order
while (!pq.isEmpty()) {
    System.out.println(pq.poll());  // C, B, A (correct)
}

// Don't use null in PriorityQueue or ArrayDeque
Queue<String> ad = new ArrayDeque<>();
ad.offer(null);  // NullPointerException!

Queue<String> pq = new PriorityQueue<>();
pq.offer(null);  // NullPointerException!

// Don't ignore return values
queue.offer("item");  // Check return value for bounded queues
boolean added = queue.offer("item");
if (!added) {
    // Handle queue full
}

// Don't use legacy Vector/Stack for queue operations
Stack<String> stack = new Stack<>();  // Legacy, use ArrayDeque instead
Queue<String> better = new ArrayDeque<>();
```

### ⚠️ **Common Gotchas:**
```java
// 1. PriorityQueue iterator doesn't guarantee order
PriorityQueue<Integer> pq = new PriorityQueue<>();
pq.addAll(Arrays.asList(5, 1, 3, 2, 4));
System.out.println(pq);  // [1, 2, 3, 5, 4] - heap order, not sorted!

// Correct way to get sorted order
List<Integer> sorted = new ArrayList<>();
while (!pq.isEmpty()) {
    sorted.add(pq.poll());  // [1, 2, 3, 4, 5]
}

// 2. LinkedList allows null, ArrayDeque doesn't
Queue<String> linked = new LinkedList<>();
linked.offer(null);  // OK

Queue<String> arrayDeque = new ArrayDeque<>();
arrayDeque.offer(null);  // NullPointerException!

// 3. Concurrent modification during iteration
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));
Iterator<String> it = queue.iterator();
queue.offer("D");  // Structural modification
String next = it.next();  // ConcurrentModificationException!

// 4. BlockingQueue operations can be interrupted
try {
    String item = blockingQueue.take();  // Can throw InterruptedException
} catch (InterruptedException e) {
    // Restore interrupt status
    Thread.currentThread().interrupt();
    // Handle interruption
}

// 5. Memory leak with long-lived queues
Queue<byte[]> memoryHog = new LinkedList<>();
while (true) {
    memoryHog.offer(new byte[1024 * 1024]);  // 1MB chunks
    // Queue grows indefinitely - memory leak!
}
// Solution: Use bounded queue or implement eviction policy
```

---

## 🚀 Java 8+ Features

### **Stream Operations on Queues**
```java
Queue<String> queue = new LinkedList<>(Arrays.asList("apple", "banana", "cherry"));

// Filter and collect
List<String> filtered = queue.stream()
    .filter(s -> s.length() > 5)
    .collect(Collectors.toList());

// Map operations
List<String> upper = queue.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Find operations
Optional<String> firstA = queue.stream()
    .filter(s -> s.startsWith("a"))
    .findFirst();

// Reduce
Optional<String> concatenated = queue.stream()
    .reduce((s1, s2) -> s1 + ", " + s2);

// Parallel processing
List<String> parallelProcessed = queue.parallelStream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### **Default Methods in Queue**
```java
Queue<String> queue = new LinkedList<>();

// removeIf (Java 8+)
queue.removeIf(s -> s.equals("remove-me"));

// spliterator
Spliterator<String> spliterator = queue.spliterator();

// stream() and parallelStream()
Stream<String> stream = queue.stream();
Stream<String> parallelStream = queue.parallelStream();
```

### **Method References with Queues**
```java
Queue<String> queue = new LinkedList<>(Arrays.asList("A", "B", "C"));

// Method reference for printing
queue.forEach(System.out::println);

// Method reference for transformation
List<String> upper = queue.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Constructor reference
Queue<String> copy = queue.stream()
    .collect(Collectors.toCollection(LinkedList::new));
```

---

## 📚 Real-World Examples

### **Example 1: Web Crawler Queue**
```java
public class WebCrawler {
    private final Queue<String> urlQueue = new LinkedList<>();
    private final Set<String> visitedUrls = new HashSet<>();
    private final int maxPages;
    
    public WebCrawler(String startUrl, int maxPages) {
        this.maxPages = maxPages;
        urlQueue.offer(startUrl);
    }
    
    public void crawl() {
        int pagesCrawled = 0;
        
        while (!urlQueue.isEmpty() && pagesCrawled < maxPages) {
            String url = urlQueue.poll();
            
            if (!visitedUrls.contains(url)) {
                visitedUrls.add(url);
                pagesCrawled++;
                
                System.out.println("Crawling: " + url);
                List<String> links = extractLinks(url);
                
                for (String link : links) {
                    if (!visitedUrls.contains(link)) {
                        urlQueue.offer(link);
                    }
                }
            }
        }
    }
    
    private List<String> extractLinks(String url) {
        // Implementation: fetch page, parse HTML, extract links
        return new ArrayList<>(); // Simplified
    }
}
```

### **Example 2: Print Job Queue**
```java
public class PrintServer {
    private final PriorityQueue<PrintJob> printQueue = new PriorityQueue<>();
    
    public void submitPrintJob(PrintJob job) {
        printQueue.offer(job);
        System.out.println("Submitted: " + job);
    }
    
    public void processJobs() {
        while (!printQueue.isEmpty()) {
            PrintJob job = printQueue.poll();
            System.out.println("Printing: " + job);
            // Simulate printing
            try {
                Thread.sleep(job.getPages() * 100L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            System.out.println("Completed: " + job);
        }
    }
    
    public static class PrintJob implements Comparable<PrintJob> {
        private final String document;
        private final int pages;
        private final int priority; // 1 = highest
        
        public PrintJob(String document, int pages, int priority) {
            this.document = document;
            this.pages = pages;
            this.priority = priority;
        }
        
        @Override
        public int compareTo(PrintJob other) {
            return Integer.compare(this.priority, other.priority);
        }
        
        // Getters, toString
    }
}
```

### **Example 3: Recent Items Cache**
```java
public class RecentItemsCache {
    private final Queue<String> recentItems;
    private final int capacity;
    private final Set<String> itemSet; // For fast lookups
    
    public RecentItemsCache(int capacity) {
        this.capacity = capacity;
        this.recentItems = new LinkedList<>();
        this.itemSet = new HashSet<>();
    }
    
    public void addItem(String item) {
        // If item already exists, move it to front
        if (itemSet.contains(item)) {
            recentItems.remove(item);
        }
        
        // Add to front
        recentItems.offer(item);
        itemSet.add(item);
        
        // Remove oldest if over capacity
        if (recentItems.size() > capacity) {
            String removed = recentItems.poll();
            itemSet.remove(removed);
        }
    }
    
    public List<String> getRecentItems() {
        return new ArrayList<>(recentItems);
    }
    
    public boolean contains(String item) {
        return itemSet.contains(item);
    }
    
    public void clear() {
        recentItems.clear();
        itemSet.clear();
    }
}
```

### **Example 4: Event Processing System**
```java
public class EventProcessor {
    private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();
    private final List<Event> processedEvents = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean running = true;
    
    public void start(int numWorkers) {
        // Start worker threads
        for (int i = 0; i < numWorkers; i++) {
            new Thread(this::processEvents).start();
        }
    }
    
    public void submitEvent(Event event) {
        eventQueue.offer(event);
    }
    
    private void processEvents() {
        while (running) {
            try {
                Event event = eventQueue.poll(100, TimeUnit.MILLISECONDS);
                if (event != null) {
                    // Process event
                    System.out.println("Processing: " + event);
                    processedEvents.add(event);
                    
                    // Simulate processing time
                    Thread.sleep(event.getProcessingTime());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public void stop() {
        running = false;
    }
    
    public static class Event {
        private final String id;
        private final String type;
        private final long timestamp;
        private final long processingTime;
        
        // Constructor, getters
    }
}
```

### **Example 5: Undo/Redo System using Deque as Stack**
```java
public class UndoRedoManager {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    
    public void execute(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear redo stack on new command
    }
    
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }
    
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
    
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    public interface Command {
        void execute();
        void undo();
    }
}
```

---

## 🔍 Debugging & Testing

### **Common Issues & Solutions**
| Issue | Cause | Solution |
|-------|-------|----------|
| `ConcurrentModificationException` | Modifying queue during iteration | Use iterator.remove() or collect items to modify |
| `NoSuchElementException` | Calling remove()/element() on empty queue | Use poll()/peek() or check isEmpty() first |
| `NullPointerException` in ArrayDeque/PriorityQueue | Adding null element | Check for null before adding |
| PriorityQueue wrong order | Using iterator instead of poll() | Use poll() to get elements in priority order |
| Memory leak | Unbounded queue grows indefinitely | Use bounded queue or implement eviction |
| Deadlock in BlockingQueue | Thread stuck on put()/take() | Use offer()/poll() with timeout |

### **Testing Queues**
```java
// JUnit tests for queue
@Test
public void testQueueOperations() {
    Queue<String> queue = new LinkedList<>();
    
    // Test offer
    assertTrue(queue.offer("A"));
    assertEquals(1, queue.size());
    
    // Test peek
    assertEquals("A", queue.peek());
    assertEquals(1, queue.size()); // Size unchanged
    
    // Test poll
    assertEquals("A", queue.poll());
    assertTrue(queue.isEmpty());
    
    // Test poll on empty
    assertNull(queue.poll());
}

@Test
public void testPriorityQueueOrder() {
    Queue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
    pq.offer(3);
    pq.offer(1);
    pq.offer(2);
    
    assertEquals(Integer.valueOf(3), pq.poll());
    assertEquals(Integer.valueOf(2), pq.poll());
    assertEquals(Integer.valueOf(1), pq.poll());
}

@Test(expected = NoSuchElementException.class)
public void testRemoveOnEmpty() {
    Queue<String> queue = new LinkedList<>();
    queue.remove(); // Should throw
}

// Test with Mockito
@Test
public void testQueueWithMock() {
    @SuppressWarnings("unchecked")
    Queue<String> mockQueue = mock(Queue.class);
    when(mockQueue.poll()).thenReturn("A", "B", null);
    
    assertEquals("A", mockQueue.poll());
    assertEquals("B", mockQueue.poll());
    assertNull(mockQueue.poll());
    
    verify(mockQueue, times(3)).poll();
}
```

### **Queue Debugging Utilities**
```java
public class QueueDebugger {
    public static <E> void printQueue(Queue<E> queue, String name) {
        System.out.println("=== " + name + " ===");
        System.out.println("Size: " + queue.size());
        System.out.println("Is empty: " + queue.isEmpty());
        
        if (!queue.isEmpty()) {
            System.out.println("Head (peek): " + queue.peek());
        }
        
        // Print all elements (without consuming)
        System.out.print("Elements: ");
        queue.forEach(e -> System.out.print(e + " "));
        System.out.println();
        
        // For PriorityQueue, show order
        if (queue instanceof PriorityQueue) {
            System.out.print("Priority order: ");
            Queue<E> copy = new PriorityQueue<>(queue);
            while (!copy.isEmpty()) {
                System.out.print(copy.poll() + " ");
            }
            System.out.println();
        }
    }
    
    public static <E> void traceOperations(Queue<E> queue) {
        // Create a wrapper that traces all operations
        Queue<E> tracedQueue = new LinkedList<E>() {
            @Override
            public boolean offer(E e) {
                System.out.println("offer(" + e + ")");
                boolean result = super.offer(e);
                System.out.println("  -> " + result + ", size: " + size());
                return result;
            }
            
            @Override
            public E poll() {
                System.out.println("poll()");
                E result = super.poll();
                System.out.println("  -> " + result + ", size: " + size());
                return result;
            }
            
            @Override
            public E peek() {
                System.out.println("peek()");
                E result = super.peek();
                System.out.println("  -> " + result + ", size: " + size());
                return result;
            }
        };
        
        // Copy elements
        tracedQueue.addAll(queue);
        // Use tracedQueue for debugging
    }
}
```

---

## 🎯 Quick Decision Guide

### **Which Queue Implementation?**
```
Need thread-safe?
  ├── Non-blocking, high contention → ConcurrentLinkedQueue
  ├── Bounded, blocking → ArrayBlockingQueue
  ├── Unbounded, blocking → LinkedBlockingQueue
  └── Priority-based, blocking → PriorityBlockingQueue

Need priority ordering? → PriorityQueue

Single-threaded, high performance? → ArrayDeque

Need null elements? → LinkedList

Need stack operations (LIFO) too? → ArrayDeque (as Deque)

Simple FIFO, don't need nulls? → ArrayDeque (better than LinkedList)

Otherwise (most cases): → ArrayDeque for single-threaded, 
                       → LinkedBlockingQueue for multi-threaded
```

### **Which Method to Use?**
```
Adding elements:
  ├── Bounded queue → offer() (check return value)
  └── Unbounded queue → add() or offer()

Removing elements:
  ├── Don't want exception → poll()
  └── Want to detect empty → remove()

Examining head:
  ├── Don't want exception → peek()
  └── Want to detect empty → element()

Multi-threaded:
  ├── Blocking wait → put()/take()
  ├── Timed wait → offer(e, timeout)/poll(timeout)
  └── Non-blocking → offer()/poll()
```

### **Common Patterns Quick Reference**
```
BFS traversal → LinkedList or ArrayDeque
Task scheduler → PriorityQueue
Producer-Consumer → BlockingQueue
Recent items → LinkedList + HashSet
Undo/Redo → ArrayDeque (as Deque/Stack)
Rate limiting → LinkedList with timestamp cleanup
Work stealing → ArrayDeque per worker
```

---

## 📖 Summary

The **Java Queue interface** is essential for:
- **FIFO processing** and task scheduling
- **Breadth-first search** algorithms
- **Producer-consumer patterns** in concurrent programming
- **Priority-based processing** with `PriorityQueue`
- **Building caches** and rate limiters

**Key Takeaways:**
1. **`ArrayDeque`** is generally better than `LinkedList` for single-threaded queues
2. **`PriorityQueue`** provides O(log n) enqueue/dequeue for priority ordering
3. **Blocking queues** (`ArrayBlockingQueue`, `LinkedBlockingQueue`) are essential for concurrent programming
4. **Prefer `offer()`, `poll()`, `peek()`** over `add()`, `remove()`, `element()` to avoid exceptions
5. **Queue iteration** doesn't guarantee order for `PriorityQueue` - use `poll()` for ordered access
6. **Consider bounded queues** to prevent memory issues in producer-consumer scenarios

This comprehensive guide covers everything from basic operations to advanced patterns. Bookmark this cheatsheet for quick reference during your Java development work!