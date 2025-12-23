# **Vector in Java - Cheat Sheet**

## **Vector Fundamentals**
```java
import java.util.Vector;

// Declaration & Initialization
Vector<Integer> vec1 = new Vector<>();          // Default capacity 10
Vector<String> vec2 = new Vector<>(20);         // Initial capacity 20
Vector<Double> vec3 = new Vector<>(15, 5);      // Capacity 15, increment 5
Vector<Integer> vec4 = new Vector<>(list);      // From Collection

// Legacy style (pre-Java 1.2, still works)
Vector vec5 = new Vector();                     // Raw type (avoid!)
```

## **Key Properties**
- **Synchronized**: Thread-safe (all methods synchronized)
- **Dynamic array**: Similar to ArrayList but synchronized
- **Capacity increment**: Can specify growth amount
- **Legacy class**: From Java 1.0, largely replaced by ArrayList
- **Enumeration**: Legacy iteration interface
- **Implements List, RandomAccess**

## **Capacity Management**
```java
Vector<String> vec = new Vector<>(5, 3); // Capacity 5, increment 3

// Capacity methods
int cap = vec.capacity();               // Current capacity (5)
vec.ensureCapacity(20);                 // Ensure min capacity
vec.trimToSize();                       // Trim to current size
vec.setSize(10);                        // Force size (nulls added)
```

## **Core Operations (Synchronized)**

### **Adding Elements**
```java
Vector<Integer> vec = new Vector<>();

// Add to end
vec.add(10);                            // Synchronized
vec.addElement(20);                     // Legacy method (same as add)
vec.add(1, 15);                         // Insert at index

// Add all
vec.addAll(Arrays.asList(30, 40, 50));
vec.insertElementAt(25, 2);             // Legacy insert

// Append if capacity permits
vec.ensureCapacity(20);
```

### **Accessing Elements**
```java
// Get elements
int first = vec.firstElement();         // Get first (NoSuchElementException if empty)
int last = vec.lastElement();           // Get last
int elem = vec.get(2);                  // By index
int elem2 = vec.elementAt(2);           // Legacy method

// Check existence
boolean contains = vec.contains(25);    // true
int index = vec.indexOf(25);            // 2
int lastIndex = vec.lastIndexOf(25);    // 2

// Direct array access (rarely used)
Object[] array = vec.toArray();
```

### **Removing Elements**
```java
// Remove by index
int removed = vec.remove(0);            // Returns removed element
vec.removeElementAt(0);                 // Legacy (void return)

// Remove by object
boolean removed = vec.remove((Integer)25); // true
vec.removeElement(25);                  // Legacy (boolean return)

// Remove all
vec.removeAllElements();                // Legacy clear()
vec.clear();                            // Modern

// Remove range
vec.removeRange(1, 3);                  // Protected method (subclass access)
```

## **Iteration Methods**
```java
Vector<String> vec = new Vector<>(Arrays.asList("A", "B", "C"));

// 1. Enumeration (legacy)
Enumeration<String> en = vec.elements();
while (en.hasMoreElements()) {
    String s = en.nextElement();
    System.out.println(s);
}

// 2. Iterator (modern)
Iterator<String> it = vec.iterator();
while (it.hasNext()) {
    String s = it.next();
    it.remove(); // Synchronized removal
}

// 3. ListIterator (bidirectional)
ListIterator<String> lit = vec.listIterator();
while (lit.hasNext()) {
    lit.next();
    if (lit.hasPrevious()) {
        lit.previous();
    }
}

// 4. For-loop (synchronized access)
for (int i = 0; i < vec.size(); i++) {
    String s = vec.get(i); // Each get() synchronized
}

// 5. Enhanced for-loop
for (String s : vec) {
    System.out.println(s);
}

// 6. forEach (Java 8+)
vec.forEach(System.out::println);
vec.forEach(s -> System.out.println(s));
```

## **Thread Safety & Performance**
```java
// Vector methods are synchronized
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}

// Performance comparison with ArrayList
// Single-threaded: ArrayList faster (no synchronization overhead)
// Multi-threaded: Vector safer but slower, CopyOnWriteArrayList better for read-heavy
```

## **Legacy Methods (Vector-specific)**
```java
Vector<String> vec = new Vector<>();

// Capacity and size
int cap = vec.capacity();
vec.ensureCapacity(100);
vec.trimToSize();
vec.setSize(10);                      // Force size, adds nulls if growing

// Element methods (legacy names)
vec.addElement("X");                  // Same as add()
vec.insertElementAt("Y", 1);          // Same as add(index, element)
vec.setElementAt("Z", 2);             // Same as set(index, element)
vec.removeElement("X");               // Same as remove(Object)
vec.removeElementAt(0);               // Same as remove(index)
vec.removeAllElements();              // Same as clear()

// Access
String first = vec.firstElement();    // Same as get(0)
String last = vec.lastElement();      // Same as get(size()-1)
Enumeration<String> en = vec.elements(); // Legacy iterator

// Copy into array
String[] arr = new String[vec.size()];
vec.copyInto(arr);                    // Legacy array copy
```

## **Common Patterns & Best Practices**
```java
// 1. Modern alternative to Vector
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// 2. Thread-safe iteration
Vector<String> vec = new Vector<>();
// Still need synchronization for compound operations
synchronized(vec) {
    Iterator<String> it = vec.iterator();
    while (it.hasNext()) {
        // Process
    }
}

// 3. Bulk operations (still need external sync for consistency)
synchronized(vec) {
    if (!vec.isEmpty()) {
        String first = vec.get(0);
        vec.remove(0);
    }
}

// 4. Using Vector in legacy code
@SuppressWarnings({"rawtypes", "unchecked"})
Vector legacyVec = new Vector(); // Avoid if possible

// 5. Vector vs ArrayList decision
if (threadSafetyNeeded) {
    // Consider: synchronizedList, CopyOnWriteArrayList, ConcurrentLinkedQueue
    // Vector only if maintaining legacy code
} else {
    List<String> list = new ArrayList<>(); // Faster
}
```

## **Performance Characteristics**
| Operation | Vector | ArrayList | Notes |
|-----------|--------|-----------|-------|
| `add()` | O(1) amortized | O(1) amortized | Vector has sync overhead |
| `get()` | O(1) | O(1) | Vector has sync overhead |
| `insert()` | O(n) | O(n) | Vector has sync overhead |
| `remove()` | O(n) | O(n) | Vector has sync overhead |
| **Thread-safe** | Yes | No | ArrayList needs external sync |
| **Legacy** | Yes | No | Vector from Java 1.0 |

## **Conversion & Compatibility**
```java
// Vector to other collections
Vector<String> vec = new Vector<>(Arrays.asList("A", "B", "C"));

List<String> list = new ArrayList<>(vec);        // To ArrayList
Set<String> set = new HashSet<>(vec);            // To HashSet
String[] array = vec.toArray(new String[0]);     // To array

// From other collections
List<String> arrayList = new ArrayList<>();
Vector<String> fromList = new Vector<>(arrayList);

// Enumeration to Iterator adapter
Enumeration<String> en = vec.elements();
Iterator<String> it = new Iterator<String>() {
    public boolean hasNext() { return en.hasMoreElements(); }
    public String next() { return en.nextElement(); }
    public void remove() { throw new UnsupportedOperationException(); }
};
```

## **Subclassing Vector**
```java
public class Stack<E> extends Vector<E> {
    // Vector is often subclassed (java.util.Stack extends Vector)
    public E push(E item) {
        addElement(item);
        return item;
    }
    
    public synchronized E pop() {
        E obj = peek();
        removeElementAt(size() - 1);
        return obj;
    }
    
    public synchronized E peek() {
        return elementAt(size() - 1);
    }
}
```

---

# **Challenge: Custom Thread-Safe Vector Implementation**

## **Problem: Build a Better Vector**

Implement a modern, thread-safe dynamic array with these features:
1. **Fine-grained locking** (not entire methods synchronized)
2. **Optimistic concurrency control**
3. **Better performance than Vector** while maintaining thread safety

```java
public class ConcurrentDynamicArray<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final float GROWTH_FACTOR = 1.5f;
    
    private volatile Object[] elements;
    private volatile int size;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    
    // Atomic counters for optimistic concurrency
    private final AtomicInteger modCount = new AtomicInteger(0);
    
    // Constructors
    public ConcurrentDynamicArray() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }
    
    public ConcurrentDynamicArray(int initialCapacity) {
        // TODO: Validate and initialize
    }
    
    // 1. Thread-safe add with fine-grained locking
    public boolean add(E element) {
        // TODO: Implement with lock only during resize/copy
        return false;
    }
    
    // 2. Optimistic read (no locking)
    @SuppressWarnings("unchecked")
    public E get(int index) {
        // TODO: Implement with bounds check and version validation
        return null;
    }
    
    // 3. Compare-and-swap update
    public boolean compareAndSet(int index, E expected, E update) {
        // TODO: Implement atomic CAS operation
        return false;
    }
    
    // 4. Batch operations (atomic)
    public void addAll(Collection<? extends E> collection) {
        // TODO: Add all elements atomically
    }
    
    // 5. Snapshot iterator (consistent view)
    public Iterator<E> snapshotIterator() {
        // TODO: Return iterator over snapshot (copy)
        return null;
    }
    
    // 6. Lock-free size operation
    public int size() {
        // TODO: Return current size (memory visibility)
        return 0;
    }
    
    // 7. Blocking operations
    public E blockingTake() throws InterruptedException {
        // TODO: Block until element available
        return null;
    }
    
    public void blockingPut(E element) throws InterruptedException {
        // TODO: Block until space available
    }
    
    // 8. Range operations with lock striping
    public void updateRange(int from, int to, Function<E, E> updater) {
        // TODO: Update range with minimal locking
    }
    
    // 9. Resize strategy with concurrent support
    private void resize(int minCapacity) {
        // TODO: Implement concurrent-safe resize
    }
    
    // 10. Statistics
    public Statistics getStatistics() {
        // TODO: Return contention statistics
        return null;
    }
    
    // Inner class for statistics
    public static class Statistics {
        public final long lockWaitTime;
        public final int resizeCount;
        public final int contentionCount;
        
        public Statistics(long lockWaitTime, int resizeCount, int contentionCount) {
            this.lockWaitTime = lockWaitTime;
            this.resizeCount = resizeCount;
            this.contentionCount = contentionCount;
        }
    }
}
```

## **Advanced Challenge: Vector-Based Data Structures**

### **Problem 1: Thread-Safe Circular Buffer**
Implement a circular buffer using array with Vector-like thread safety but better performance.

```java
public class ConcurrentCircularBuffer<E> {
    private final E[] buffer;
    private int head = 0;
    private int tail = 0;
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock(true); // Fair lock
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    
    @SuppressWarnings("unchecked")
    public ConcurrentCircularBuffer(int capacity) {
        this.buffer = (E[]) new Object[capacity];
    }
    
    public void put(E item) throws InterruptedException {
        // TODO: Implement with blocking when full
    }
    
    public E take() throws InterruptedException {
        // TODO: Implement with blocking when empty
        return null;
    }
    
    public boolean offer(E item, long timeout, TimeUnit unit) {
        // TODO: Implement with timeout
        return false;
    }
    
    public E poll(long timeout, TimeUnit unit) {
        // TODO: Implement with timeout
        return null;
    }
    
    // Atomic snapshot of entire buffer
    public List<E> snapshot() {
        // TODO: Return consistent snapshot
        return new ArrayList<>();
    }
}
```

### **Problem 2: Multi-Version Vector**
Implement a vector that maintains multiple versions for concurrent readers.

```java
public class MultiVersionVector<E> {
    private static class VersionedArray<E> {
        final Object[] elements;
        final int version;
        final int size;
        
        VersionedArray(Object[] elements, int version, int size) {
            this.elements = elements;
            this.version = version;
            this.size = size;
        }
    }
    
    private volatile VersionedArray<E> current;
    private final AtomicInteger versionCounter = new AtomicInteger(0);
    
    public MultiVersionVector() {
        this.current = new VersionedArray<>(new Object[10], 0, 0);
    }
    
    // Readers get snapshot (no lock)
    @SuppressWarnings("unchecked")
    public E get(int index, int version) {
        // TODO: Return element from specific version
        return null;
    }
    
    // Writers create new version
    public void add(E element) {
        // TODO: Create new versioned array
    }
    
    // Get iterator for specific version
    public Iterator<E> versionIterator(int version) {
        // TODO: Return iterator for version
        return null;
    }
    
    // Garbage collect old versions
    public void cleanupVersionsOlderThan(int minVersion) {
        // TODO: Clean up old versions
    }
}
```

### **Problem 3: Persistent Vector (Immutable)**
Implement a persistent vector with structural sharing (like Clojure's PersistentVector).

```java
public class PersistentVector<E> {
    private static final int BRANCHING_FACTOR = 32;
    private static final int BITS_PER_LEVEL = 5; // log2(32)
    
    private final Object root;
    private final int size;
    private final int shift;
    
    private PersistentVector(Object root, int size, int shift) {
        this.root = root;
        this.size = size;
        this.shift = shift;
    }
    
    public PersistentVector() {
        this(new Object[BRANCHING_FACTOR], 0, 0);
    }
    
    @SuppressWarnings("unchecked")
    public E get(int index) {
        // TODO: Navigate tree structure
        return null;
    }
    
    public PersistentVector<E> assoc(int index, E value) {
        // TODO: Return new vector with updated value (structural sharing)
        return this;
    }
    
    public PersistentVector<E> conj(E value) {
        // TODO: Add to end, grow tree if needed
        return this;
    }
    
    public PersistentVector<E> pop() {
        // TODO: Remove last element
        return this;
    }
    
    // Builder for efficient construction
    public static class Builder<E> {
        // TODO: Implement transient builder
    }
}
```

## **Performance Testing Challenge**
Create benchmark comparing:
1. **Java Vector**
2. **Collections.synchronizedList**
3. **CopyOnWriteArrayList**
4. **Your ConcurrentDynamicArray**
5. **Your ConcurrentCircularBuffer**

```java
public class VectorBenchmark {
    public static void main(String[] args) throws InterruptedException {
        int numThreads = 10;
        int operationsPerThread = 100000;
        
        // Test 1: Contended writes
        benchmark("Vector", () -> {
            Vector<Integer> vec = new Vector<>();
            runConcurrentTest(vec, numThreads, operationsPerThread);
        });
        
        // Test 2: Read-heavy workload
        benchmark("CopyOnWriteArrayList", () -> {
            List<Integer> list = new CopyOnWriteArrayList<>();
            // Pre-populate
            for (int i = 0; i < 1000; i++) list.add(i);
            runReadHeavyTest(list, numThreads, operationsPerThread);
        });
        
        // Test 3: Mixed workload
        benchmark("ConcurrentDynamicArray", () -> {
            ConcurrentDynamicArray<Integer> cda = new ConcurrentDynamicArray<>();
            runMixedTest(cda, numThreads, operationsPerThread);
        });
    }
    
    private static void benchmark(String name, Runnable test) {
        long start = System.nanoTime();
        test.run();
        long end = System.nanoTime();
        System.out.printf("%s: %d ms%n", name, (end - start) / 1_000_000);
    }
}
```

## **Real-World Application: Thread Pool Work Queue**
Implement a work queue for thread pool using your concurrent vector.

```java
public class WorkQueue<E> {
    private final ConcurrentDynamicArray<E> tasks;
    private final Semaphore availableTasks;
    private volatile boolean shutdown = false;
    
    public WorkQueue(int initialCapacity) {
        this.tasks = new ConcurrentDynamicArray<>(initialCapacity);
        this.availableTasks = new Semaphore(0);
    }
    
    public void submit(E task) {
        if (shutdown) throw new RejectedExecutionException("Queue shutdown");
        tasks.add(task);
        availableTasks.release();
    }
    
    public E take() throws InterruptedException {
        availableTasks.acquire();
        if (shutdown) return null;
        // TODO: Implement task retrieval
        return null;
    }
    
    public void shutdown() {
        shutdown = true;
        availableTasks.release(tasks.size()); // Wake all waiting threads
    }
    
    public List<E> drain() {
        // TODO: Return all pending tasks
        return new ArrayList<>();
    }
}
```

## **Learning Objectives**
- Understand synchronization costs and alternatives
- Learn lock striping and fine-grained locking
- Implement optimistic concurrency control
- Compare Vector with modern concurrent collections
- Understand memory visibility and volatile variables
- Learn about CAS operations and atomic variables
- Implement persistent data structures

## **Key Insights**
1. **Vector is legacy**: Use concurrent collections from `java.util.concurrent`
2. **Synchronization granularity**: Fine-grained locks can improve performance
3. **Copy-on-write**: Good for read-heavy workloads
4. **Lock-free algorithms**: Can provide better scalability
5. **Memory barriers**: Understand `volatile` and `Atomic` classes

**Final Challenge**: Can you implement a vector that outperforms both Java's Vector and ArrayList in multi-threaded scenarios while maintaining correctness?