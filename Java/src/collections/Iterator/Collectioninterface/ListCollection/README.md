# Java List Interface - Complete Guide & Cheatsheet

## 📋 Overview
The **Java List interface** (`java.util.List`) represents an **ordered, indexed collection** of elements. Unlike Sets, Lists allow duplicate elements and maintain insertion order, making them ideal for sequences where position matters.

## 🔑 Core Characteristics

### ✅ **Ordered Collection**
- Elements maintain insertion sequence
- Positional access via integer index (0-based)
- Predictable iteration order

### ✅ **Allows Duplicates**
- Same element can appear multiple times
- Each occurrence has its own index

### ✅ **Index-Based Operations**
- Access: `get(index)`
- Insert: `add(index, element)`
- Remove: `remove(index)`
- Search: `indexOf(element)`

### ✅ **Null-Friendly**
- Most implementations allow null elements
- `ArrayList` and `LinkedList` support nulls


## ⚖️ List vs Set Comparison

| Feature | List | Set |
|---------|------|-----|
| **Order** | Maintains insertion order | No guaranteed order (except `LinkedHashSet`, `TreeSet`) |
| **Duplicates** | ✅ Allowed | ❌ Not allowed |
| **Null Elements** | ✅ Allowed (usually) | ⚠️ Depends on implementation |
| **Primary Use** | Ordered sequences, positional access | Unique collections, membership testing |
| **Performance** | Fast index access (`ArrayList`) | Fast lookup (`HashSet` O(1)) |
| **Implementations** | `ArrayList`, `LinkedList`, `Vector`, `Stack` | `HashSet`, `TreeSet`, `LinkedHashSet` |

---

## 🏗️ List Implementations

### 1. **`ArrayList`** (Most Common)
```java
List<String> arrayList = new ArrayList<>();
```
- **Backed by**: Resizable array
- **Best for**: Random access, iteration
- **Worst for**: Frequent insertions/removals in middle
- **Performance**: O(1) for `get()`, `set()`; O(n) for add/remove in middle
- **Thread-safe**: ❌ No (use `Collections.synchronizedList()` or `CopyOnWriteArrayList`)

### 2. **`LinkedList`**
```java
List<String> linkedList = new LinkedList<>();
```
- **Backed by**: Doubly-linked list
- **Best for**: Frequent insertions/removals
- **Worst for**: Random access
- **Performance**: O(1) for add/remove at ends; O(n) for `get()`, `set()`
- **Also implements**: `Queue`, `Deque` interfaces

### 3. **`Vector`** (Legacy)
```java
List<String> vector = new Vector<>();
```
- **Backed by**: Resizable array (like ArrayList)
- **Thread-safe**: ✅ Yes (synchronized methods)
- **Legacy**: Use `ArrayList` with explicit synchronization if needed

### 4. **`Stack`** (Legacy)
```java
List<String> stack = new Stack<>();
```
- **Extends**: `Vector`
- **LIFO structure**: Push/pop operations
- **Modern alternative**: `ArrayDeque`

### 5. **`CopyOnWriteArrayList`** (Thread-safe)
```java
List<String> threadSafeList = new CopyOnWriteArrayList<>();
```
- **Thread-safe**: ✅ Yes
- **Ideal for**: Read-heavy, infrequent write scenarios
- **Performance**: Creates copy on modification

---

## 📝 Creating Lists

### Basic Creation
```java
// Generic lists (Java 5+)
List<String> list1 = new ArrayList<>();
List<Integer> list2 = new LinkedList<>();

// Pre-Java 7 (explicit type on both sides)
List<String> oldStyle = new ArrayList<String>();

// Java 7+ (diamond operator)
List<String> newStyle = new ArrayList<>();

// Java 9+ (immutable lists)
List<String> immutable = List.of("A", "B", "C");
List<String> emptyImmutable = List.of();
```

### Factory Methods (Java 9+)
```java
// Immutable lists (fixed-size, cannot add/remove)
List<String> fixed = List.of("A", "B", "C");
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Empty immutable list
List<String> empty = List.of();
List<String> alsoEmpty = Collections.emptyList();

// Single-element immutable list
List<String> single = List.of("OnlyOne");
List<String> singleton = Collections.singletonList("OnlyOne");
```

### Initializing with Elements
```java
// Method 1: Add individually
List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("C");

// Method 2: Collections.addAll()
Collections.addAll(list, "D", "E", "F");

// Method 3: Arrays.asList() (fixed-size!)
List<String> fixedList = Arrays.asList("A", "B", "C");
// fixedList.add("D"); // Throws UnsupportedOperationException

// Method 4: Constructor with collection
List<String> fromAnother = new ArrayList<>(fixedList);

// Method 5: Stream API (Java 8+)
List<String> fromStream = Stream.of("A", "B", "C")
                                 .collect(Collectors.toList());
```

---

## 🛠️ Core Operations Cheatsheet

### 📥 **Adding Elements**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `add(E e)` | Append to end | `boolean` | `list.add("New")` |
| `add(int index, E e)` | Insert at position | `void` | `list.add(0, "First")` |
| `addAll(Collection c)` | Add all from collection | `boolean` | `list.addAll(otherList)` |
| `addAll(int index, Collection c)` | Insert all at position | `boolean` | `list.addAll(0, otherList)` |

```java
List<String> list = new ArrayList<>();
list.add("Apple");                    // ["Apple"]
list.add(0, "Banana");                // ["Banana", "Apple"]
list.addAll(List.of("Cherry", "Date")); // ["Banana", "Apple", "Cherry", "Date"]
```

### 📤 **Removing Elements**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `remove(int index)` | Remove by position | `E` (removed element) | `list.remove(0)` |
| `remove(Object o)` | Remove first occurrence | `boolean` | `list.remove("Apple")` |
| `removeAll(Collection c)` | Remove all matching | `boolean` | `list.removeAll(toRemove)` |
| `retainAll(Collection c)` | Keep only matching | `boolean` | `list.retainAll(toKeep)` |
| `clear()` | Remove all elements | `void` | `list.clear()` |

```java
List<String> list = new ArrayList<>(List.of("A", "B", "C", "A"));
list.remove(0);                     // Removes "A" -> ["B", "C", "A"]
list.remove("A");                   // Removes first "A" -> ["B", "C"]
list.removeAll(List.of("B", "C"));  // Removes all -> []
```

### 🔍 **Accessing & Searching**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `get(int index)` | Get element at index | `E` | `list.get(0)` |
| `indexOf(Object o)` | Find first occurrence | `int` (index or -1) | `list.indexOf("A")` |
| `lastIndexOf(Object o)` | Find last occurrence | `int` (index or -1) | `list.lastIndexOf("A")` |
| `contains(Object o)` | Check if element exists | `boolean` | `list.contains("A")` |
| `containsAll(Collection c)` | Check if all exist | `boolean` | `list.containsAll(other)` |
| `subList(int from, int to)` | Get view of sublist | `List<E>` | `list.subList(1, 3)` |

```java
List<String> list = List.of("A", "B", "C", "A", "B");
String first = list.get(0);              // "A"
int firstB = list.indexOf("B");          // 1
int lastB = list.lastIndexOf("B");       // 4
boolean hasC = list.contains("C");       // true
List<String> sub = list.subList(1, 4);   // ["B", "C", "A"]
```

### 📏 **Size & Capacity**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `size()` | Number of elements | `int` | `list.size()` |
| `isEmpty()` | Check if empty | `boolean` | `list.isEmpty()` |

```java
List<String> list = new ArrayList<>();
list.isEmpty();      // true
list.add("A");
list.size();         // 1
list.isEmpty();      // false
```

### ✏️ **Modifying Elements**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `set(int index, E e)` | Replace at index | `E` (previous element) | `list.set(0, "New")` |
| `replaceAll(UnaryOperator op)` | Transform all elements | `void` | `list.replaceAll(String::toUpperCase)` |

```java
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
String old = list.set(1, "B");          // old = "b", list = ["a", "B", "c"]
list.replaceAll(String::toUpperCase);   // list = ["A", "B", "C"]
```

---

## 🔄 Iterating Over Lists

### 1. **For Loop (Index-based)**
```java
List<String> list = List.of("A", "B", "C");

for (int i = 0; i < list.size(); i++) {
    String element = list.get(i);
    System.out.println(i + ": " + element);
}
// Best for: Random access, when index is needed
```

### 2. **Enhanced For Loop (For-each)**
```java
for (String element : list) {
    System.out.println(element);
}
// Best for: Simple iteration, no modification needed
```

### 3. **Iterator**
```java
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String element = iterator.next();
    System.out.println(element);
    // Can remove current element safely:
    // iterator.remove();
}
// Best for: Safe removal during iteration
```

### 4. **ListIterator (Bidirectional)**
```java
ListIterator<String> listIterator = list.listIterator();

// Forward iteration
while (listIterator.hasNext()) {
    String element = listIterator.next();
    System.out.println(element);
}

// Backward iteration
while (listIterator.hasPrevious()) {
    String element = listIterator.previous();
    System.out.println(element);
}
// Best for: Bidirectional traversal, add/set during iteration
```

### 5. **Java 8+ Streams**
```java
list.stream()
    .filter(s -> s.startsWith("A"))
    .map(String::toUpperCase)
    .forEach(System.out::println);
// Best for: Functional operations, chaining transformations
```

### 6. **Java 8+ forEach()**
```java
list.forEach(System.out::println);
// or with method reference
list.forEach(System.out::println);
// Best for: Concise iteration
```

---

## 📊 Performance Comparison

### **Time Complexity**
| Operation | ArrayList | LinkedList | Notes |
|-----------|-----------|------------|-------|
| `get(index)` | O(1) | O(n) | LinkedList must traverse nodes |
| `add(element)` | O(1) amortized | O(1) | Both efficient at end |
| `add(index, element)` | O(n) | O(1) if near start/end, O(n) if middle | ArrayList shifts elements |
| `remove(index)` | O(n) | O(1) if near start/end, O(n) if middle | ArrayList shifts elements |
| `remove(element)` | O(n) | O(n) | Both must search |
| `contains(element)` | O(n) | O(n) | Linear search |
| `set(index, element)` | O(1) | O(n) | LinkedList must traverse |

### **Memory Usage**
- **ArrayList**: Less overhead per element (just array storage)
- **LinkedList**: More overhead (node objects with prev/next pointers)

### **When to Use Which?**
- **Use ArrayList** when:
  - More reads than writes
  - Random access needed
  - Memory efficiency important
  - Most operations at end of list

- **Use LinkedList** when:
  - Frequent insertions/deletions in middle
  - Implementing stacks/queues
  - Memory not a constraint
  - Sequential access is primary pattern

---

## 🔧 Advanced Operations

### **Sorting Lists**
```java
// Natural order (requires Comparable)
List<String> list = new ArrayList<>(List.of("Z", "A", "M"));
Collections.sort(list);  // ["A", "M", "Z"]

// With Comparator
Collections.sort(list, Comparator.reverseOrder());  // ["Z", "M", "A"]

// Using List.sort() (Java 8+)
list.sort(Comparator.naturalOrder());
list.sort((a, b) -> a.compareTo(b));

// Complex sorting
list.sort(Comparator.comparing(String::length)
                   .thenComparing(String::compareTo));
```

### **Converting Between Collections**
```java
// List to Array
List<String> list = List.of("A", "B", "C");
String[] array1 = list.toArray(new String[0]);        // Best practice
String[] array2 = list.toArray(new String[list.size()]);

// Array to List (Java 8+)
String[] array = {"A", "B", "C"};
List<String> list1 = Arrays.asList(array);           // Fixed-size view
List<String> list2 = new ArrayList<>(Arrays.asList(array)); // Mutable copy
List<String> list3 = List.of(array);                 // Immutable (Java 9+)

// List to Set (removes duplicates)
Set<String> set = new HashSet<>(list);

// Set to List
List<String> fromSet = new ArrayList<>(set);
```

### **Immutable Lists**
```java
// Java 9+ (recommended)
List<String> immutable1 = List.of("A", "B", "C");

// Java 8 and earlier
List<String> immutable2 = Collections.unmodifiableList(
    new ArrayList<>(List.of("A", "B", "C"))
);

// Empty immutable lists
List<String> empty1 = List.of();
List<String> empty2 = Collections.emptyList();

// Single-element immutable list
List<String> single1 = List.of("Only");
List<String> single2 = Collections.singletonList("Only");
```

### **Thread-Safe Lists**
```java
// Synchronized wrapper (legacy approach)
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// CopyOnWriteArrayList (modern, read-heavy)
List<String> copyOnWrite = new CopyOnWriteArrayList<>();

// Using concurrent collections
ConcurrentLinkedDeque<String> concurrentDeque = new ConcurrentLinkedDeque<>();
```

### **List Utilities**
```java
List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));

// Reverse
Collections.reverse(list);  // ["D", "C", "B", "A"]

// Shuffle (randomize)
Collections.shuffle(list);  // Random order

// Rotate
Collections.rotate(list, 1);  // ["D", "A", "B", "C"]

// Swap elements
Collections.swap(list, 0, 2);  // Swap positions 0 and 2

// Fill with same value
Collections.fill(list, "X");  // ["X", "X", "X", "X"]

// Frequency count
int freq = Collections.frequency(list, "A");  // Count occurrences

// Disjoint check (no common elements)
boolean disjoint = Collections.disjoint(list1, list2);
```

---

## 💡 Best Practices & Common Pitfalls

### ✅ **Do:**
```java
// Specify capacity for large ArrayLists
List<String> largeList = new ArrayList<>(1000);

// Use interfaces as types
List<String> list = new ArrayList<>();  // Good
ArrayList<String> bad = new ArrayList<>();  // Avoid

// Check bounds before get()
if (index >= 0 && index < list.size()) {
    String element = list.get(index);
}

// Use isEmpty() instead of size() == 0
if (list.isEmpty()) {  // Good
    // handle empty
}

// Prefer enhanced for loops for readability
for (String item : list) {  // Good
    // process
}

// Use Java 8+ streams for complex operations
double average = list.stream()
                     .mapToInt(String::length)
                     .average()
                     .orElse(0.0);
```

### ❌ **Don't:**
```java
// Don't modify list while iterating (except with Iterator.remove())
for (String item : list) {
    list.remove(item);  // Throws ConcurrentModificationException!
}

// Don't use raw types
List rawList = new ArrayList();  // Bad - no generics
List<String> typedList = new ArrayList<>();  // Good

// Don't ignore subList() is a view
List<String> sub = list.subList(1, 3);
sub.clear();  // Also removes from original list!

// Don't use Vector/Stack for new code
Vector<String> old = new Vector<>();  // Legacy
List<String> modern = new ArrayList<>();  // Preferred

// Don't assume List allows null
List<String> list = new ArrayList<>();
list.add(null);  // OK for ArrayList, but not all implementations
```

### ⚠️ **Common Gotchas:**
```java
// Arrays.asList() returns fixed-size list
List<String> fixed = Arrays.asList("A", "B", "C");
// fixed.add("D");  // UnsupportedOperationException!

// subList() is backed by original
List<String> original = new ArrayList<>(List.of("A", "B", "C", "D"));
List<String> sub = original.subList(1, 3);
sub.add("X");  // Also adds to original!
// Now original = ["A", "B", "C", "X", "D"]

// Concurrent modification
List<String> list = new ArrayList<>(List.of("A", "B", "C"));
for (String s : list) {
    if (s.equals("B")) {
        list.remove(s);  // ConcurrentModificationException!
    }
}
// Fix: Use iterator.remove() or collect items to remove first

// Integer vs int in remove()
List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3));
numbers.remove(1);    // Removes element at index 1 (value 2)
numbers.remove(Integer.valueOf(1));  // Removes element with value 1
```

---

## 🚀 Java 8+ Features

### **Stream Operations**
```java
List<String> list = Arrays.asList("apple", "banana", "cherry", "date");

// Filtering
List<String> filtered = list.stream()
    .filter(s -> s.length() > 5)
    .collect(Collectors.toList());

// Mapping
List<String> upper = list.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Sorting
List<String> sorted = list.stream()
    .sorted()
    .collect(Collectors.toList());

// Finding
Optional<String> firstA = list.stream()
    .filter(s -> s.startsWith("a"))
    .findFirst();

// Grouping
Map<Integer, List<String>> byLength = list.stream()
    .collect(Collectors.groupingBy(String::length));

// Joining
String joined = list.stream()
    .collect(Collectors.joining(", "));
```

### **Parallel Streams**
```java
List<String> list = Arrays.asList("a", "b", "c", "d", "e");

// Parallel processing
List<String> result = list.parallelStream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// Use with caution: parallel has overhead
// Only beneficial for large datasets or expensive operations
```

### **New Methods (Java 8+)**
```java
List<String> list = new ArrayList<>(List.of("A", "B", "C"));

// removeIf (bulk removal)
list.removeIf(s -> s.equals("B"));  // Removes all "B"

// replaceAll (bulk replacement)
list.replaceAll(String::toLowerCase);  // ["a", "c"]

// sort with Comparator
list.sort(Comparator.reverseOrder());

// forEach
list.forEach(System.out::println);
```

---

## 📚 Real-World Examples

### **Example 1: Pagination**
```java
public List<String> getPage(List<String> allItems, int page, int pageSize) {
    int fromIndex = page * pageSize;
    int toIndex = Math.min(fromIndex + pageSize, allItems.size());
    
    if (fromIndex >= allItems.size()) {
        return Collections.emptyList();
    }
    
    return new ArrayList<>(allItems.subList(fromIndex, toIndex));
}
```

### **Example 2: Removing Duplicates While Preserving Order**
```java
public List<String> removeDuplicates(List<String> list) {
    // Using LinkedHashSet preserves order
    return new ArrayList<>(new LinkedHashSet<>(list));
    
    // Java 8+ alternative
    // return list.stream().distinct().collect(Collectors.toList());
}
```

### **Example 3: Finding Common Elements**
```java
public List<String> findCommonElements(List<String> list1, List<String> list2) {
    List<String> common = new ArrayList<>(list1);
    common.retainAll(list2);
    return common;
}
```

### **Example 4: Batch Processing**
```java
public void processInBatches(List<String> items, int batchSize, Consumer<List<String>> processor) {
    for (int i = 0; i < items.size(); i += batchSize) {
        int end = Math.min(i + batchSize, items.size());
        List<String> batch = items.subList(i, end);
        processor.accept(batch);
    }
}
```

### **Example 5: Flattening Lists**
```java
public List<String> flattenLists(List<List<String>> listOfLists) {
    return listOfLists.stream()
                      .flatMap(List::stream)
                      .collect(Collectors.toList());
}
```

---

## 🔍 Debugging & Testing Tips

### **Common Issues & Solutions**
| Issue | Cause | Solution |
|-------|-------|----------|
| `IndexOutOfBoundsException` | Accessing invalid index | Check `index < list.size()` before `get(index)` |
| `ConcurrentModificationException` | Modifying while iterating | Use `Iterator.remove()` or collect items to remove |
| `UnsupportedOperationException` | Modifying immutable/fixed list | Use mutable copy: `new ArrayList<>(immutableList)` |
| Performance issues with large Lists | Wrong List implementation | Use `ArrayList` for random access, `LinkedList` for frequent modifications |
| Memory issues | Large capacity with few elements | Use `trimToSize()` on ArrayList or set initial capacity |

### **Testing Lists**
```java
// JUnit assertions
assertEquals(3, list.size());
assertTrue(list.contains("A"));
assertFalse(list.isEmpty());
assertEquals("A", list.get(0));

// AssertJ (more readable)
assertThat(list)
    .hasSize(3)
    .contains("A", "B")
    .doesNotContain("Z")
    .startsWith("A")
    .isSorted();
```

---

## 🎯 Quick Decision Guide

### **Which List Implementation?**
```
Need thread-safe? → CopyOnWriteArrayList (read-heavy) or synchronized wrapper
Need LIFO stack? → ArrayDeque (not Stack!)
Legacy code? → Vector/Stack (but consider migrating)
Otherwise: 
  Frequent random access? → ArrayList
  Frequent insertions/removals in middle? → LinkedList
  Unsure? → Start with ArrayList (most common case)
```

### **Which Iteration Method?**
```
Need index? → Standard for loop
Need to remove during iteration? → Iterator
Simple read-only iteration? → Enhanced for loop
Bidirectional traversal? → ListIterator
Functional operations? → Stream API
Most concise? → forEach()
```

---

## 📖 Summary

The **Java List interface** is a fundamental collection type that provides:
- **Ordered, indexed** storage of elements
- **Duplicate elements** allowed
- **Rich API** for manipulation and querying
- **Multiple implementations** for different use cases

**Key Takeaways:**
1. **`ArrayList`** is the go-to for most scenarios
2. Use **generics** for type safety
3. **Iterate carefully** to avoid ConcurrentModificationException
4. **Java 8+** adds powerful functional operations
5. Choose implementation based on **access patterns**

This comprehensive guide covers everything from basic operations to advanced techniques. Bookmark this cheatsheet for quick reference during your Java development work!