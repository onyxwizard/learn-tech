# Java Map Interface - Complete Guide & Cheatsheet

## 📋 Overview
The **Java Map interface** (`java.util.Map`) represents a **key-value mapping** where each key maps to exactly one value. Maps are ideal for lookup tables, dictionaries, and associative arrays. Unlike Collections, Map doesn't extend the `Collection` interface but is part of the Java Collections Framework.


## 🔑 Core Characteristics

### ✅ **Key-Value Pairs**
- Each entry consists of a unique key and its associated value
- Keys are unique within a Map (no duplicates)
- Values can be duplicated across different keys

### ✅ **Fast Lookup**
- Direct access to values via keys
- `HashMap`: O(1) average for get/put operations
- `TreeMap`: O(log n) for operations with sorted keys

### ✅ **No Guaranteed Order**
- Most implementations don't guarantee order
- Exceptions: `LinkedHashMap` (insertion/access order), `TreeMap` (sorted order)

### ✅ **Null Handling**
- Most implementations allow null keys and values
- `Hashtable` doesn't allow nulls
- `TreeMap` doesn't allow null keys (unless using special Comparator)

---

## ⚖️ Map vs Collection Comparison

| Feature | Map | List/Set |
|---------|-----|----------|
| **Structure** | Key-Value pairs | Single elements |
| **Duplicates** | Keys: ❌ Not allowed<br>Values: ✅ Allowed | List: ✅ Allowed<br>Set: ❌ Not allowed |
| **Order** | Depends on implementation | List: ✅ Ordered<br>Set: Not guaranteed |
| **Primary Use** | Lookup tables, dictionaries, caches | Sequences, unique collections |
| **Access Method** | By key | By index (List) or object (Set) |
| **Extends Collection** | ❌ No | ✅ Yes |

---

## 🏗️ Map Implementations

### 1. **`HashMap`** (Most Common)
```java
Map<String, Integer> hashMap = new HashMap<>();
```
- **Backed by**: Hash table
- **Order**: No guarantee
- **Performance**: O(1) average for basic operations
- **Null Keys/Values**: ✅ Allowed (one null key, multiple null values)
- **Thread-safe**: ❌ No
- **Best for**: General-purpose, fastest operations

### 2. **`LinkedHashMap`** (Insertion/Access Order)
```java
// Insertion order (default)
Map<String, Integer> linkedMap = new LinkedHashMap<>();

// Access order (LRU cache)
Map<String, Integer> lruCache = new LinkedHashMap<>(16, 0.75f, true);
```
- **Backed by**: Hash table + Linked list
- **Order**: Insertion order or access order
- **Performance**: Slightly slower than HashMap
- **Null Keys/Values**: ✅ Allowed
- **Best for**: Predictable iteration order, LRU caches

### 3. **`TreeMap`** (Sorted Keys)
```java
// Natural ordering
Map<String, Integer> treeMap = new TreeMap<>();

// Custom comparator
Map<String, Integer> reverseTree = new TreeMap<>(Comparator.reverseOrder());
```
- **Backed by**: Red-black tree (balanced BST)
- **Order**: Key natural ordering or custom Comparator
- **Performance**: O(log n) for operations
- **Null Keys**: ❌ Not allowed (except with Comparator)
- **Also implements**: `SortedMap`, `NavigableMap`
- **Best for**: Sorted key collections, range queries

### 4. **`Hashtable`** (Legacy, Thread-Safe)
```java
Map<String, Integer> hashtable = new Hashtable<>();
```
- **Backed by**: Hash table (like HashMap)
- **Thread-safe**: ✅ Yes (synchronized methods)
- **Null Keys/Values**: ❌ Not allowed
- **Legacy**: Use `ConcurrentHashMap` or `Collections.synchronizedMap()` instead

### 5. **`ConcurrentHashMap`** (Modern Thread-Safe)
```java
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();
```
- **Thread-safe**: ✅ Yes (lock striping, better performance than synchronized)
- **Null Keys/Values**: ❌ Not allowed
- **Performance**: Nearly as fast as HashMap for concurrent access
- **Best for**: Thread-safe maps in multi-threaded environments

### 6. **`EnumMap`** (High-Performance for Enums)
```java
enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }
Map<Day, String> enumMap = new EnumMap<>(Day.class);
```
- **Backed by**: Array indexed by enum ordinal
- **Order**: Natural enum order (declaration order)
- **Performance**: Extremely fast
- **Null Keys**: ❌ Not allowed
- **Best for**: Maps with enum keys

### 7. **`IdentityHashMap`** (Reference Equality)
```java
Map<String, Integer> identityMap = new IdentityHashMap<>();
```
- **Uses**: `==` for key comparison instead of `equals()`
- **Use case**: When object identity matters more than value equality
- **Performance**: Similar to HashMap
- **Best for**: Topology-preserving object graph transformations

### 8. **`WeakHashMap`** (Weakly Referenced Keys)
```java
Map<String, Integer> weakMap = new WeakHashMap<>();
```
- **Keys**: Weakly referenced (can be garbage collected)
- **Use case**: Caches where entries should be removed when keys are no longer referenced
- **Best for**: Memory-sensitive caches

---

## 📝 Creating Maps

### Basic Creation
```java
// Generic maps
Map<String, Integer> map1 = new HashMap<>();
Map<Integer, String> map2 = new TreeMap<>();
Map<String, List<Integer>> map3 = new LinkedHashMap<>();

// With initial capacity (HashMap/LinkedHashMap)
Map<String, Integer> map4 = new HashMap<>(100); // initial capacity
Map<String, Integer> map5 = new HashMap<>(100, 0.75f); // capacity + load factor

// From existing map
Map<String, Integer> source = Map.of("A", 1, "B", 2);
Map<String, Integer> copy = new HashMap<>(source);
```

### Factory Methods (Java 9+)
```java
// Immutable maps (Java 9+)
Map<String, Integer> immutable = Map.of("A", 1, "B", 2, "C", 3);
Map<String, Integer> empty = Map.of();
Map<String, Integer> single = Map.of("Only", 1);

// Up to 10 key-value pairs with Map.of()
// More than 10: use Map.ofEntries()
Map<String, Integer> larger = Map.ofEntries(
    Map.entry("A", 1),
    Map.entry("B", 2),
    Map.entry("C", 3),
    Map.entry("D", 4),
    Map.entry("E", 5)
);

// Java 8 and earlier
Map<String, Integer> immutableOld = Collections.unmodifiableMap(
    new HashMap<>(Map.of("A", 1, "B", 2))
);
```

### Specialized Maps
```java
// EnumMap
enum Priority { LOW, MEDIUM, HIGH }
Map<Priority, String> priorityMap = new EnumMap<>(Priority.class);

// ConcurrentHashMap with estimated size
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>(100);

// TreeMap with custom comparator
Map<String, Integer> reverseMap = new TreeMap<>(Comparator.reverseOrder());

// LinkedHashMap as LRU cache (access-order)
Map<String, Integer> lruCache = new LinkedHashMap<String, Integer>(16, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
        return size() > 100; // Keep only 100 entries
    }
};
```

---

## 🛠️ Core Operations Cheatsheet

### 📥 **Adding/Updating Elements**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `put(K key, V value)` | Add or update key-value pair | `V` (previous value or null) | `map.put("A", 1)` |
| `putAll(Map m)` | Add all entries from another map | `void` | `map.putAll(otherMap)` |
| `putIfAbsent(K key, V value)` | Add only if key not present | `V` (previous value or null) | `map.putIfAbsent("A", 1)` |

```java
Map<String, Integer> map = new HashMap<>();
Integer old1 = map.put("A", 1);    // null (key didn't exist)
Integer old2 = map.put("A", 2);    // 1 (replaced)
Integer old3 = map.putIfAbsent("A", 3); // 2 (not replaced)
Integer old4 = map.putIfAbsent("B", 4); // null, adds B=4
```

### 📤 **Removing Elements**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `remove(Object key)` | Remove by key | `V` (removed value or null) | `map.remove("A")` |
| `remove(Object key, Object value)` | Remove if key maps to value | `boolean` | `map.remove("A", 1)` |
| `clear()` | Remove all entries | `void` | `map.clear()` |

```java
Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2, "C", 3));
Integer removed = map.remove("B");          // 2, removes B=2
boolean removed2 = map.remove("A", 1);      // true, removes A=1
boolean removed3 = map.remove("C", 999);    // false, no removal
map.clear();                                // map is now empty
```

### 🔍 **Querying Operations**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `get(Object key)` | Get value by key | `V` (value or null) | `map.get("A")` |
| `getOrDefault(Object key, V defaultValue)` | Get with default | `V` | `map.getOrDefault("X", 0)` |
| `containsKey(Object key)` | Check if key exists | `boolean` | `map.containsKey("A")` |
| `containsValue(Object value)` | Check if value exists | `boolean` | `map.containsValue(1)` |
| `isEmpty()` | Check if empty | `boolean` | `map.isEmpty()` |
| `size()` | Get number of entries | `int` | `map.size()` |

```java
Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);
Integer value = map.get("A");                     // 1
Integer defaultVal = map.getOrDefault("X", 0);    // 0
boolean hasKey = map.containsKey("B");            // true
boolean hasValue = map.containsValue(4);          // false
boolean empty = map.isEmpty();                    // false
int size = map.size();                            // 3
```

### ✏️ **Updating Elements**
| Method | Description | Returns | Example |
|--------|-------------|---------|---------|
| `replace(K key, V value)` | Replace if key exists | `V` (previous value or null) | `map.replace("A", 5)` |
| `replace(K key, V oldValue, V newValue)` | Replace if key maps to oldValue | `boolean` | `map.replace("A", 1, 5)` |
| `replaceAll(BiFunction)` | Replace all values | `void` | `map.replaceAll((k,v)->v*2)` |

```java
Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2));
Integer old = map.replace("A", 10);          // 1, now A=10
boolean replaced = map.replace("B", 2, 20);  // true, now B=20
map.replaceAll((key, value) -> value * 10);  // A=100, B=200
```

---

## 🔄 Iterating Over Maps

### 1. **EntrySet Iteration (Recommended)**
```java
Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);

// Using entrySet() with enhanced for loop
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    String key = entry.getKey();
    Integer value = entry.getValue();
    System.out.println(key + " = " + value);
}

// Using iterator
Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
while (iterator.hasNext()) {
    Map.Entry<String, Integer> entry = iterator.next();
    // Can safely remove: iterator.remove()
}
```

### 2. **KeySet Iteration**
```java
// Iterate keys, then get values
for (String key : map.keySet()) {
    Integer value = map.get(key);
    System.out.println(key + " = " + value);
}

// Just keys
map.keySet().forEach(System.out::println);
```

### 3. **Values Iteration**
```java
// Just values
for (Integer value : map.values()) {
    System.out.println(value);
}

// With stream
map.values().stream()
    .filter(v -> v > 1)
    .forEach(System.out::println);
```

### 4. **Java 8+ forEach()**
```java
// Using lambda
map.forEach((key, value) -> 
    System.out.println(key + " = " + value));

// With method reference for values
map.forEach((key, value) -> processEntry(key, value));
```

### 5. **Stream API**
```java
// Filter entries
map.entrySet().stream()
    .filter(entry -> entry.getValue() > 1)
    .forEach(entry -> System.out.println(entry.getKey()));

// Transform map
Map<String, String> upperCaseKeys = map.entrySet().stream()
    .collect(Collectors.toMap(
        entry -> entry.getKey().toUpperCase(),
        Map.Entry::getValue
    ));

// Parallel processing for large maps
map.entrySet().parallelStream()
    .forEach(entry -> processHeavy(entry));
```

### 6. **Sorted Map Iteration (TreeMap)**
```java
TreeMap<String, Integer> treeMap = new TreeMap<>(map);

// Natural order
for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
    // A, B, C (alphabetical)
}

// Reverse order
for (Map.Entry<String, Integer> entry : treeMap.descendingMap().entrySet()) {
    // C, B, A
}

// Range iteration
SortedMap<String, Integer> subMap = treeMap.subMap("B", "D");
for (Map.Entry<String, Integer> entry : subMap.entrySet()) {
    // Only B, C
}
```

---

## 📊 Performance Comparison

### **Time Complexity**
| Operation | HashMap | LinkedHashMap | TreeMap | ConcurrentHashMap |
|-----------|---------|---------------|---------|-------------------|
| `get()` | O(1) avg | O(1) avg | O(log n) | O(1) avg |
| `put()` | O(1) avg | O(1) avg | O(log n) | O(1) avg |
| `remove()` | O(1) avg | O(1) avg | O(log n) | O(1) avg |
| `containsKey()` | O(1) avg | O(1) avg | O(log n) | O(1) avg |
| `iteration` | O(n) | O(n) | O(n) | O(n) |
| `firstKey()/lastKey()` | N/A | N/A | O(log n) | N/A |

### **Memory Usage**
- **HashMap**: Moderate (array + linked lists/red-black trees for collisions)
- **LinkedHashMap**: Higher (additional linked list pointers)
- **TreeMap**: Higher (tree node objects)
- **EnumMap**: Minimal (array indexed by enum ordinal)

### **When to Use Which?**
- **Use HashMap** when:
  - Need fastest operations
  - Don't care about order
  - General-purpose mapping

- **Use LinkedHashMap** when:
  - Need predictable iteration order
  - Implementing LRU cache
  - Can afford slight performance hit

- **Use TreeMap** when:
  - Need keys sorted
  - Need range operations (subMap, headMap, tailMap)
  - Keys are Comparable or have Comparator

- **Use ConcurrentHashMap** when:
  - Need thread-safe map
  - High concurrent read/write performance needed
  - Don't need null keys/values

- **Use EnumMap** when:
  - Keys are enum constants
  - Need maximum performance

- **Use IdentityHashMap** when:
  - Object identity matters (== instead of equals())
  - Implementing object graph algorithms

- **Use WeakHashMap** when:
  - Building memory-sensitive caches
  - Want entries automatically removed when keys become unreachable

---

## 🔧 Advanced Operations

### **Functional Operations (Java 8+)**
```java
Map<String, Integer> map = new HashMap<>();

// compute() - compute new value for key
map.compute("A", (key, oldValue) -> 
    oldValue == null ? 1 : oldValue + 1);

// computeIfAbsent() - compute only if key absent
map.computeIfAbsent("B", key -> 10);  // Adds B=10 if B not present

// computeIfPresent() - compute only if key present
map.computeIfPresent("A", (key, oldValue) -> oldValue * 2);

// merge() - merge old and new value
map.merge("A", 5, (oldValue, newValue) -> oldValue + newValue);

// getOrDefault() - safe get with default
int value = map.getOrDefault("C", 0);  // Returns 0 if C not present

// putIfAbsent() - atomic put if absent
map.putIfAbsent("D", 20);  // Only adds if D not present

// replace() with old value check
boolean replaced = map.replace("A", 10, 20);  // Replaces A=10 with A=20
```

### **SortedMap Operations (TreeMap)**
```java
TreeMap<String, Integer> treeMap = new TreeMap<>(Map.of(
    "A", 1, "B", 2, "C", 3, "D", 4, "E", 5
));

// First and last entries
Map.Entry<String, Integer> first = treeMap.firstEntry();  // A=1
Map.Entry<String, Integer> last = treeMap.lastEntry();    // E=5

// Head, tail, and sub maps
SortedMap<String, Integer> head = treeMap.headMap("C");        // {A=1, B=2}
SortedMap<String, Integer> tail = treeMap.tailMap("C");        // {C=3, D=4, E=5}
SortedMap<String, Integer> sub = treeMap.subMap("B", "D");     // {B=2, C=3}

// Navigable operations
String ceilingKey = treeMap.ceilingKey("C");      // "C" (≥ C)
String floorKey = treeMap.floorKey("C");          // "C" (≤ C)
String higherKey = treeMap.higherKey("C");        // "D" (> C)
String lowerKey = treeMap.lowerKey("C");          // "B" (< C)

// Poll first/last (retrieve and remove)
Map.Entry<String, Integer> polled = treeMap.pollFirstEntry();  // removes A=1
```

### **Bulk Operations**
```java
Map<String, Integer> map1 = new HashMap<>(Map.of("A", 1, "B", 2));
Map<String, Integer> map2 = new HashMap<>(Map.of("B", 3, "C", 4));

// Merge two maps
Map<String, Integer> merged = new HashMap<>(map1);
map2.forEach((key, value) -> 
    merged.merge(key, value, Integer::sum));  // B=5 (2+3)

// Find common keys
Set<String> commonKeys = new HashSet<>(map1.keySet());
commonKeys.retainAll(map2.keySet());  // ["B"]

// Find entries only in map1
Map<String, Integer> onlyInMap1 = new HashMap<>(map1);
onlyInMap1.keySet().removeAll(map2.keySet());  // ["A"]

// Invert map (swap keys and values)
Map<Integer, String> inverted = map.entrySet().stream()
    .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
// Warning: values must be unique!
```

### **Immutable Maps**
```java
// Java 9+ (recommended)
Map<String, Integer> immutable1 = Map.of("A", 1, "B", 2, "C", 3);
// immutable1.put("D", 4); // UnsupportedOperationException

// Java 8 and earlier
Map<String, Integer> immutable2 = Collections.unmodifiableMap(
    new HashMap<>(Map.of("A", 1, "B", 2))
);

// Builder pattern for more than 10 entries (Java 9+)
Map<String, Integer> largerImmutable = Map.ofEntries(
    Map.entry("A", 1),
    Map.entry("B", 2),
    // ... up to any number
    Map.entry("Z", 26)
);

// Empty immutable map
Map<String, Integer> empty1 = Map.of();
Map<String, Integer> empty2 = Collections.emptyMap();

// Single-entry immutable map
Map<String, Integer> single1 = Map.of("Only", 1);
Map<String, Integer> single2 = Collections.singletonMap("Only", 1);
```

### **Thread-Safe Maps**
```java
// Synchronized wrapper (legacy)
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

// ConcurrentHashMap (modern)
Map<String, Integer> concurrentMap = new ConcurrentHashMap<>();

// ConcurrentSkipListMap (sorted, concurrent)
Map<String, Integer> concurrentSorted = new ConcurrentSkipListMap<>();

// Using synchronized blocks
Map<String, Integer> map = new HashMap<>();
synchronized(map) {
    map.put("key", 1);
    // Other operations
}

// Read-write lock for more control
Map<String, Integer> mapWithLock = new HashMap<>();
ReadWriteLock lock = new ReentrantReadWriteLock();

// Write
lock.writeLock().lock();
try {
    mapWithLock.put("key", 1);
} finally {
    lock.writeLock().unlock();
}

// Read
lock.readLock().lock();
try {
    Integer value = mapWithLock.get("key");
} finally {
    lock.readLock().unlock();
}
```

### **Map Utilities**
```java
Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2, "C", 3));

// Unmodifiable view
Map<String, Integer> unmodifiable = Collections.unmodifiableMap(map);

// Checked map (runtime type safety)
Map<String, Integer> checked = Collections.checkedMap(map, String.class, Integer.class);

// Synchronized map
Map<String, Integer> synchronizedMap = Collections.synchronizedMap(map);

// Empty and singleton maps
Map<String, Integer> empty = Collections.emptyMap();
Map<String, Integer> singleton = Collections.singletonMap("Only", 1);

// Frequency count (values)
int frequency = Collections.frequency(map.values(), 1);
```

---

## 💡 Best Practices & Common Pitfalls

### ✅ **Do:**
```java
// Use appropriate implementation
Map<String, Integer> cache = new HashMap<>();  // Fast, no order needed
Map<String, Integer> sorted = new TreeMap<>();  // Need sorting
Map<String, Integer> ordered = new LinkedHashMap<>();  // Need insertion order

// Specify initial capacity for large HashMaps
Map<String, Integer> largeMap = new HashMap<>(10000);

// Use interfaces as types
Map<String, Integer> map = new HashMap<>();  // Good
HashMap<String, Integer> bad = new HashMap<>();  // Avoid

// Check for null returns
Integer value = map.get("key");
if (value != null) {
    // Process value
}

// Use getOrDefault() for safe access
int safeValue = map.getOrDefault("key", 0);

// Use entrySet() for iteration (most efficient)
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    // Process entry
}

// Use computeIfAbsent() for lazy initialization
Map<String, List<String>> multimap = new HashMap<>();
multimap.computeIfAbsent("key", k -> new ArrayList<>())
        .add("value");
```

### ❌ **Don't:**
```java
// Don't modify map while iterating (except with Iterator.remove())
for (String key : map.keySet()) {
    map.remove(key);  // ConcurrentModificationException!
}

// Don't rely on HashMap order
Map<String, Integer> map = new HashMap<>();
map.put("Z", 1);
map.put("A", 2);
// Iteration order is undefined!

// Don't use raw types
Map rawMap = new HashMap();  // Bad - no generics
Map<String, Integer> typedMap = new HashMap<>();  // Good

// Don't use Hashtable for new code
Map<String, Integer> old = new Hashtable<>();  // Legacy
Map<String, Integer> modern = new HashMap<>();  // Preferred

// Don't use null keys in TreeMap
Map<String, Integer> treeMap = new TreeMap<>();
treeMap.put(null, 1);  // NullPointerException

// Don't ignore ConcurrentModificationException
List<String> keys = new ArrayList<>(map.keySet());
for (String key : keys) {  // Safe iteration over copy
    if (shouldRemove(key)) {
        map.remove(key);
    }
}
```

### ⚠️ **Common Gotchas:**
```java
// HashMap depends on hashCode() and equals() of keys
class BadKey {
    String value;
    // Missing hashCode() and equals() implementations
}

Map<BadKey, Integer> map = new HashMap<>();
map.put(new BadKey("A"), 1);
map.get(new BadKey("A"));  // null - different objects!

// Concurrent modification
Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2, "C", 3));
for (String key : map.keySet()) {
    if (key.equals("B")) {
        map.remove(key);  // ConcurrentModificationException!
    }
}
// Fix: Use iterator.remove() or collect keys to remove first

// Performance with large HashMaps
Map<Integer, String> map = new HashMap<>();
for (int i = 0; i < 1000000; i++) {
    map.put(i, "value");  // Trigger rehashing multiple times
}
// Better: Set initial capacity
Map<Integer, String> better = new HashMap<>(1000000);

// Integer vs int in remove()
Map<String, Integer> map = new HashMap<>(Map.of("A", 1));
map.remove("A");    // Removes entry with key "A"
map.remove("A", 1); // Removes entry with key "A" and value 1

// Default values with primitive types
Map<String, Integer> map = new HashMap<>();
int value = map.get("missing");  // NullPointerException!
// Fix: use getOrDefault() or check for null
int safe = map.getOrDefault("missing", 0);
```

---

## 🚀 Java 8+ Features

### **Stream Operations on Maps**
```java
Map<String, Integer> map = Map.of("apple", 5, "banana", 3, "cherry", 8);

// Filter entries
Map<String, Integer> filtered = map.entrySet().stream()
    .filter(entry -> entry.getValue() > 4)
    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

// Transform values
Map<String, String> transformed = map.entrySet().stream()
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        entry -> entry.getKey() + ":" + entry.getValue()
    ));

// Group by value
Map<Integer, List<String>> groupedByValue = map.entrySet().stream()
    .collect(Collectors.groupingBy(
        Map.Entry::getValue,
        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
    ));

// Sort by value
Map<String, Integer> sortedByValue = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,  // merge function for duplicate keys
        LinkedHashMap::new  // preserve order
    ));

// Find max/min by value
Optional<Map.Entry<String, Integer>> maxEntry = map.entrySet().stream()
    .max(Map.Entry.comparingByValue());

// Reduce operations
int sum = map.values().stream().mapToInt(Integer::intValue).sum();
```

### **New Methods (Java 8+)**
```java
Map<String, Integer> map = new HashMap<>();

// Functional methods
map.compute("key", (k, v) -> v == null ? 1 : v + 1);
map.computeIfAbsent("newKey", k -> 10);
map.computeIfPresent("key", (k, v) -> v * 2);
map.merge("key", 5, (oldVal, newVal) -> oldVal + newVal);

// Convenience methods
map.putIfAbsent("key", 100);
map.getOrDefault("missing", 0);
map.replace("key", 50);
map.replace("key", 50, 100);

// Bulk operations
map.replaceAll((k, v) -> v * 10);
```

### **Collectors for Maps**
```java
List<String> words = Arrays.asList("apple", "banana", "apple", "cherry");

// Frequency map
Map<String, Long> frequency = words.stream()
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ));

// Partitioning
Map<Boolean, List<String>> byLength = words.stream()
    .collect(Collectors.partitioningBy(s -> s.length() > 5));

// To map with duplicate handling
Map<String, Integer> wordLengths = words.stream()
    .collect(Collectors.toMap(
        Function.identity(),  // key mapper
        String::length,       // value mapper
        (v1, v2) -> v1        // merge function for duplicates
    ));

// Grouping with downstream collector
Map<Integer, Set<String>> byLengthSet = words.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.toSet()
    ));
```

---

## 📚 Real-World Examples

### **Example 1: Word Frequency Counter**
```java
public Map<String, Integer> countWords(String text) {
    return Arrays.stream(text.toLowerCase().split("\\W+"))
                 .filter(word -> !word.isEmpty())
                 .collect(Collectors.toMap(
                     word -> word,
                     word -> 1,
                     Integer::sum,
                     HashMap::new
                 ));
}

// With groupingBy (more readable)
public Map<String, Long> countWordsBetter(String text) {
    return Pattern.compile("\\W+")
                  .splitAsStream(text.toLowerCase())
                  .filter(word -> !word.isEmpty())
                  .collect(Collectors.groupingBy(
                      Function.identity(),
                      Collectors.counting()
                  ));
}
```

### **Example 2: Cache with Expiration**
```java
public class Cache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
            return size() > MAX_SIZE || 
                   eldest.getValue().isExpired();
        }
    };
    
    public void put(K key, V value, Duration ttl) {
        cache.put(key, new CacheEntry<>(value, ttl));
    }
    
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }
    
    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;
        
        CacheEntry(V value, Duration ttl) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttl.toMillis();
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
        
        V getValue() { return value; }
    }
}
```

### **Example 3: Configuration Manager**
```java
public class Configuration {
    private final Map<String, String> config = new HashMap<>();
    private final Map<String, List<Consumer<String>>> listeners = new HashMap<>();
    
    public void set(String key, String value) {
        String oldValue = config.put(key, value);
        if (!Objects.equals(oldValue, value)) {
            notifyListeners(key, value);
        }
    }
    
    public String get(String key) {
        return config.get(key);
    }
    
    public String get(String key, String defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }
    
    public void addListener(String key, Consumer<String> listener) {
        listeners.computeIfAbsent(key, k -> new ArrayList<>())
                 .add(listener);
    }
    
    private void notifyListeners(String key, String value) {
        listeners.getOrDefault(key, Collections.emptyList())
                 .forEach(listener -> listener.accept(value));
    }
}
```

### **Example 4: Inverted Index for Search**
```java
public class InvertedIndex {
    private final Map<String, Set<Integer>> index = new HashMap<>();
    
    public void addDocument(int docId, String text) {
        Arrays.stream(text.toLowerCase().split("\\W+"))
              .filter(word -> !word.isEmpty())
              .forEach(word -> 
                  index.computeIfAbsent(word, k -> new HashSet<>())
                       .add(docId)
              );
    }
    
    public Set<Integer> search(String query) {
        return Arrays.stream(query.toLowerCase().split("\\W+"))
                     .filter(word -> !word.isEmpty())
                     .map(index::get)
                     .filter(Objects::nonNull)
                     .flatMap(Set::stream)
                     .collect(Collectors.toSet());
    }
    
    public Set<Integer> searchAnd(String... words) {
        return Arrays.stream(words)
                     .map(index::get)
                     .filter(Objects::nonNull)
                     .reduce((set1, set2) -> {
                         Set<Integer> intersection = new HashSet<>(set1);
                         intersection.retainAll(set2);
                         return intersection;
                     })
                     .orElse(Collections.emptySet());
    }
}
```

### **Example 5: MultiMap Implementation**
```java
public class MultiMap<K, V> {
    private final Map<K, List<V>> map = new HashMap<>();
    
    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>())
           .add(value);
    }
    
    public List<V> get(K key) {
        return map.getOrDefault(key, Collections.emptyList());
    }
    
    public boolean remove(K key, V value) {
        List<V> values = map.get(key);
        if (values != null && values.remove(value)) {
            if (values.isEmpty()) {
                map.remove(key);
            }
            return true;
        }
        return false;
    }
    
    public Set<K> keySet() {
        return map.keySet();
    }
    
    public Collection<List<V>> values() {
        return map.values();
    }
    
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }
}
```

---

## 🔍 Debugging & Testing Tips

### **Common Issues & Solutions**
| Issue | Cause | Solution |
|-------|-------|----------|
| `ConcurrentModificationException` | Modifying while iterating | Use `Iterator.remove()` or iterate over copy |
| `NullPointerException` in TreeMap | Adding null key | Avoid nulls or use Comparator that handles nulls |
| HashMap not recognizing keys | Incorrect `hashCode()`/`equals()` | Implement both methods properly |
| Performance issues | Frequent rehashing in HashMap | Set initial capacity and load factor |
| ClassCastException | Wrong type in generic Map | Use `Collections.checkedMap()` for runtime checks |
| Memory leaks with keys | Keys not being garbage collected | Use `WeakHashMap` for cache scenarios |

### **Testing Maps**
```java
// JUnit assertions
Map<String, Integer> map = Map.of("A", 1, "B", 2);
assertEquals(2, map.size());
assertTrue(map.containsKey("A"));
assertTrue(map.containsValue(1));
assertEquals(Integer.valueOf(1), map.get("A"));

// AssertJ (more readable)
assertThat(map)
    .hasSize(2)
    .containsEntry("A", 1)
    .containsKey("B")
    .doesNotContainKey("C")
    .doesNotContainValue(3);

// Testing order
Map<String, Integer> linkedMap = new LinkedHashMap<>();
linkedMap.put("A", 1);
linkedMap.put("B", 2);
linkedMap.put("C", 3);
assertThat(linkedMap).containsExactly(
    entry("A", 1), entry("B", 2), entry("C", 3)
);

// Testing sorted maps
Map<String, Integer> treeMap = new TreeMap<>(Comparator.reverseOrder());
treeMap.put("A", 1);
treeMap.put("B", 2);
treeMap.put("C", 3);
assertThat(treeMap).containsExactly(
    entry("C", 3), entry("B", 2), entry("A", 1)
);
```

---

## 🎯 Quick Decision Guide

### **Which Map Implementation?**
```
Need thread-safe?
  ├── High concurrency, no nulls → ConcurrentHashMap
  ├── Sorted and concurrent → ConcurrentSkipListMap
  └── Legacy synchronization → Collections.synchronizedMap()

Keys are enums? → EnumSet (best performance)

Need sorted keys? → TreeMap

Need predictable iteration order? → LinkedHashMap

Building a cache?
  ├── LRU cache → LinkedHashMap (access-order)
  ├── Weak keys (GC friendly) → WeakHashMap
  └── General purpose → HashMap with eviction policy

Otherwise (most cases): → HashMap (fastest, most flexible)
```

### **Which Iteration Method?**
```
Need both key and value? → entrySet() (most efficient)
Need to modify during iteration? → Iterator.remove()
Just keys? → keySet()
Just values? → values()
Functional style? → forEach() or Stream API
Parallel processing? → parallelStream()
```

### **Null Safety Strategy**
```
HashMap/LinkedHashMap:
  ├── Check with containsKey() before get()
  ├── Use getOrDefault() for safe access
  └── Use computeIfAbsent() for lazy initialization

TreeMap:
  └── Avoid null keys (use special Comparator if needed)

ConcurrentHashMap:
  └── No null keys or values allowed

General:
  └── Consider Optional<V> as value type for explicit null handling
```

---

## 📖 Summary

The **Java Map interface** is essential for:
- **Key-value lookups** and associative arrays
- **Building caches** with various eviction policies
- **Counting frequencies** and building indices
- **Configuration management** and property storage
- **Object relationship mapping**

**Key Takeaways:**
1. **`HashMap`** is the default choice for most scenarios
2. **`TreeMap`** provides sorted keys but has O(log n) operations
3. **`LinkedHashMap`** maintains insertion/access order
4. **`ConcurrentHashMap`** is the modern thread-safe map
5. **`EnumMap`** is extremely efficient for enum keys
6. Always implement **proper `hashCode()` and `equals()`** for custom key objects
7. Use **Java 8+ functional methods** for cleaner code

This comprehensive guide covers everything from basic operations to advanced techniques. Bookmark this cheatsheet for quick reference during your Java development work!