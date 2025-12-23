# **Dynamic Arrays (ArrayList) in Java - Cheat Sheet**

## **ArrayList Fundamentals**
```java
import java.util.ArrayList;
import java.util.List;

// Declaration & Initialization
List<Integer> list1 = new ArrayList<>();      // Preferred (interface type)
ArrayList<String> list2 = new ArrayList<>();  // Concrete type
ArrayList<Integer> list3 = new ArrayList<>(20); // Initial capacity

// Initialization with values (Java 9+)
List<Integer> list4 = List.of(1, 2, 3);       // Immutable
List<Integer> list5 = new ArrayList<>(Arrays.asList(1, 2, 3)); // Mutable

// Copy constructor
ArrayList<Integer> copy = new ArrayList<>(list1);
```

## **Key Properties**
- **Dynamic resizing**: Grows automatically (typically 1.5x or 2x)
- **Random access**: O(1) access by index
- **Amortized O(1)** for add operations
- **Not synchronized** (use `Collections.synchronizedList()` for thread safety)
- Implements `List`, `Collection`, `Iterable` interfaces
- Maintains insertion order
- Allows `null` values and duplicates

## **Capacity vs Size**
```java
ArrayList<Integer> list = new ArrayList<>(10); // Capacity = 10, Size = 0
list.add(1);                                   // Size = 1
list.ensureCapacity(100);                      // Increase capacity
list.trimToSize();                             // Trim to current size
```

## **Core Operations**

### **Adding Elements**
```java
ArrayList<String> list = new ArrayList<>();

// Add to end (amortized O(1))
list.add("A");
list.add("B");
list.add("C");

// Add at index (O(n) - shifts elements)
list.add(1, "X");                   // [A, X, B, C]

// Add all from collection
List<String> other = Arrays.asList("D", "E");
list.addAll(other);                 // [A, X, B, C, D, E]
list.addAll(2, other);              // [A, X, D, E, B, C, D, E]
```

### **Accessing Elements**
```java
// Get by index (O(1))
String elem = list.get(0);          // "A"

// Set/replace (O(1))
list.set(1, "Y");                   // [A, Y, D, E, B, C, D, E]

// Check existence
boolean hasA = list.contains("A");  // true (O(n) search)
int index = list.indexOf("D");      // 2 (first occurrence, O(n))
int lastIndex = list.lastIndexOf("D"); // 6 (last occurrence, O(n))
```

### **Removing Elements**
```java
// Remove by index (O(n) - shifts)
String removed = list.remove(0);    // Returns "A", list = [Y, D, E, B, C, D, E]

// Remove by object (O(n) - search + shift)
boolean removed = list.remove("D"); // true, removes first "D", list = [Y, E, B, C, D, E]

// Remove all matching
list.removeIf(s -> s.startsWith("E")); // Remove all starting with "E"

// Remove all from collection
list.removeAll(Arrays.asList("B", "C")); // [Y, D, E]

// Clear all
list.clear();                       // []
```

### **Bulk Operations**
```java
ArrayList<Integer> nums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// Sublist (view, not copy)
List<Integer> sub = nums.subList(1, 4); // [2, 3, 4] (backed by original)

// Retain only specified elements
nums.retainAll(Arrays.asList(2, 3, 6)); // [2, 3] (6 not in original)

// Check if contains all
boolean hasAll = nums.containsAll(Arrays.asList(2, 3)); // true
```

## **Iteration Methods**
```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// 1. For loop (index-based)
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}

// 2. Enhanced for-loop
for (String s : list) {
    System.out.println(s);
}

// 3. Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("B")) {
        it.remove(); // Safe removal during iteration
    }
}

// 4. ListIterator (bidirectional)
ListIterator<String> lit = list.listIterator(list.size());
while (lit.hasPrevious()) {
    System.out.println(lit.previous());
}

// 5. forEach (Java 8+)
list.forEach(System.out::println);
list.forEach(s -> System.out.println(s));
```

## **Conversion Methods**
```java
ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

// To array
String[] array1 = list.toArray(new String[0]);      // Preferred
String[] array2 = list.toArray(new String[list.size()]);

// To other collections
Set<String> set = new HashSet<>(list);
LinkedList<String> linked = new LinkedList<>(list);

// From array
String[] arr = {"X", "Y", "Z"};
ArrayList<String> fromArray = new ArrayList<>(Arrays.asList(arr));
```

## **Sorting & Searching**
```java
ArrayList<Integer> nums = new ArrayList<>(Arrays.asList(5, 2, 8, 1));

// Sort (mutates list)
Collections.sort(nums);                 // [1, 2, 5, 8]
nums.sort(Comparator.naturalOrder());   // Java 8+
nums.sort(Comparator.reverseOrder());   // [8, 5, 2, 1]

// Custom sort
nums.sort((a, b) -> b - a);            // Descending

// Binary search (must be sorted!)
int index = Collections.binarySearch(nums, 5); // 1
```

## **Performance Characteristics**
| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| `add()` (append) | Amortized O(1) | May trigger resize |
| `add(index, elem)` | O(n) | Shifts elements |
| `get(index)` | O(1) | Random access |
| `set(index, elem)` | O(1) | Replace at index |
| `remove(index)` | O(n) | Shifts elements |
| `remove(Object)` | O(n) | Search + shift |
| `contains(Object)` | O(n) | Linear search |
| `indexOf(Object)` | O(n) | Linear search |

## **Common Pitfalls & Best Practices**
```java
// 1. Specify initial capacity if size known
ArrayList<Integer> list = new ArrayList<>(1000); // Avoids resizing

// 2. Use isEmpty() instead of size() == 0
if (!list.isEmpty()) { /* ... */ }

// 3. Beware of ConcurrentModificationException
for (String s : list) {
    if (s.equals("X")) {
        list.remove(s); // THROWS EXCEPTION!
    }
}
// Use iterator.remove() instead

// 4. Prefer interface type for declarations
List<String> list = new ArrayList<>(); // More flexible

// 5. Use defensive copying
public List<String> getData() {
    return new ArrayList<>(internalList); // Prevent modification
}

// 6. Consider LinkedList for frequent add/remove in middle
```

## **Thread-Safe Alternatives**
```java
// 1. Synchronized wrapper
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// 2. CopyOnWriteArrayList (for read-heavy)
import java.util.concurrent.CopyOnWriteArrayList;
List<String> cowList = new CopyOnWriteArrayList<>();

// 3. Vector (legacy, synchronized)
import java.util.Vector;
Vector<String> vector = new Vector<>();
```

---

# **Challenge: Custom Dynamic Array Implementation**

## **Problem: Build Your Own ArrayList**

Implement a custom dynamic array class **without using Java's built-in ArrayList**. You must handle resizing, generics, and all core operations.

```java
public class MyArrayList<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    
    private Object[] elementData;
    private int size;
    
    // Constructors
    public MyArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }
    
    public MyArrayList(int initialCapacity) {
        // TODO: Implement
    }
    
    // 1. Basic operations
    public int size() {
        // TODO: Return current size
        return 0;
    }
    
    public boolean isEmpty() {
        // TODO: Check if empty
        return false;
    }
    
    // 2. Add elements
    public boolean add(E element) {
        // TODO: Add to end, resize if needed
        return false;
    }
    
    public void add(int index, E element) {
        // TODO: Add at specific position with bounds check
    }
    
    // 3. Get and set
    @SuppressWarnings("unchecked")
    public E get(int index) {
        // TODO: Return element at index with bounds check
        return null;
    }
    
    public E set(int index, E element) {
        // TODO: Replace element at index, return old value
        return null;
    }
    
    // 4. Remove elements
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        // TODO: Remove at index, shift elements, return removed
        return null;
    }
    
    public boolean remove(Object obj) {
        // TODO: Remove first occurrence of object
        return false;
    }
    
    // 5. Search operations
    public boolean contains(Object obj) {
        // TODO: Check if element exists
        return false;
    }
    
    public int indexOf(Object obj) {
        // TODO: Return first index of element, -1 if not found
        return -1;
    }
    
    public int lastIndexOf(Object obj) {
        // TODO: Return last index of element
        return -1;
    }
    
    // 6. Bulk operations
    public void clear() {
        // TODO: Remove all elements
    }
    
    public void addAll(MyArrayList<E> other) {
        // TODO: Add all elements from another MyArrayList
    }
    
    // 7. Capacity management
    public void ensureCapacity(int minCapacity) {
        // TODO: Ensure minimum capacity
    }
    
    public void trimToSize() {
        // TODO: Trim capacity to current size
    }
    
    // 8. Convert to array
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        // TODO: Return array of all elements
        return null;
    }
    
    // 9. Iterator support
    public Iterator<E> iterator() {
        // TODO: Return iterator (implement inner Iterator class)
        return null;
    }
    
    // 10. Sublist view (BONUS)
    public MyArrayList<E> subList(int fromIndex, int toIndex) {
        // TODO: Return view of portion of list
        return null;
    }
    
    // Private helper methods
    private void rangeCheck(int index) {
        // TODO: Check if index is valid
    }
    
    private void grow(int minCapacity) {
        // TODO: Grow array (typically 1.5x or 2x current size)
    }
    
    private void fastRemove(int index) {
        // TODO: Remove without bounds check (for internal use)
    }
}
```

## **Implementation Requirements**

### **Resizing Strategy**
```java
// Typical growth formula
int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x
// Or: int newCapacity = oldCapacity * 2; // 2x

// Handle overflow
if (newCapacity - minCapacity < 0)
    newCapacity = minCapacity;
if (newCapacity - MAX_ARRAY_SIZE > 0)
    newCapacity = hugeCapacity(minCapacity);
```

### **Iterator Implementation**
```java
private class MyIterator implements Iterator<E> {
    private int cursor = 0;
    private int lastRet = -1;
    
    public boolean hasNext() {
        return cursor != size;
    }
    
    @SuppressWarnings("unchecked")
    public E next() {
        // TODO: Implement with modCount check
        return null;
    }
    
    public void remove() {
        // TODO: Remove last returned element
    }
}
```

## **Challenge Problems**

### **Problem 1: Merge K Sorted Lists**
Given K sorted ArrayLists, merge them into one sorted ArrayList.

```java
public static <T extends Comparable<T>> MyArrayList<T> mergeKSorted(
    MyArrayList<MyArrayList<T>> lists) {
    // TODO: Implement efficient merging
    return new MyArrayList<>();
}
```

### **Problem 2: Sliding Window Maximum**
Given an ArrayList of integers and window size k, find the maximum in each sliding window.

```java
public MyArrayList<Integer> maxSlidingWindow(int k) {
    // TODO: Return list of maximums for each window
    return new MyArrayList<>();
}
```

### **Problem 3: Implement Circular Buffer**
Extend MyArrayList to support circular buffer operations (fixed capacity, overwrite when full).

```java
public class CircularArrayList<E> extends MyArrayList<E> {
    private int head = 0;
    private int tail = 0;
    private boolean full = false;
    
    // Override add to be circular
    @Override
    public boolean add(E element) {
        // TODO: Implement circular behavior
        return super.add(element);
    }
    
    public E poll() {
        // TODO: Remove from head
        return null;
    }
    
    public E peek() {
        // TODO: View head without removing
        return null;
    }
}
```

### **Problem 4: Sparse Vector**
Implement a sparse vector using ArrayList to store only non-zero elements efficiently.

```java
public class SparseVector {
    private MyArrayList<Integer> indices;
    private MyArrayList<Double> values;
    private int dimension;
    
    public double dotProduct(SparseVector other) {
        // TODO: Compute dot product efficiently
        return 0.0;
    }
    
    public SparseVector add(SparseVector other) {
        // TODO: Add two sparse vectors
        return null;
    }
}
```

## **Testing Your Implementation**
```java
public class TestMyArrayList {
    public static void main(String[] args) {
        // Test basic operations
        MyArrayList<Integer> list = new MyArrayList<>();
        
        // Add 1000 elements
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        
        // Test resize
        System.out.println("Size: " + list.size()); // Should be 1000
        
        // Test get/set
        list.set(500, 999);
        System.out.println("Get 500: " + list.get(500)); // Should be 999
        
        // Test remove
        list.remove(0);
        System.out.println("Size after remove: " + list.size()); // 999
        
        // Test iterator
        int sum = 0;
        for (Integer num : list) {
            sum += num;
        }
        System.out.println("Sum: " + sum);
        
        // Test performance
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            list.add(i);
        }
        long end = System.nanoTime();
        System.out.println("Time for 100K adds: " + (end - start) + " ns");
    }
}
```

## **Performance Benchmarks**
Implement and compare:
1. **Add 1M elements** (measure resizing overhead)
2. **Insert at beginning 10K times** (vs LinkedList)
3. **Random access 1M times** (vs LinkedList)
4. **Remove from middle 10K times**

## **Learning Objectives**
- Understand amortized analysis for dynamic arrays
- Learn array resizing strategies
- Implement generics with type safety
- Handle edge cases (null elements, invalid indices)
- Understand iterator pattern and fail-fast behavior
- Compare ArrayList vs LinkedList performance trade-offs
- Learn about modCount for concurrent modification detection

## **Key Insights**
1. **Amortized O(1) add**: Resizing cost distributed over many operations
2. **Space vs Time**: Pre-allocate capacity if size known
3. **Cache locality**: Arrays have better cache performance than linked structures
4. **Trade-offs**: ArrayList excels at random access, LinkedList at frequent insertions/deletions

**Challenge**: Can you make your implementation faster than Java's ArrayList for specific operations? Try optimizing the growth factor or memory allocation strategy!