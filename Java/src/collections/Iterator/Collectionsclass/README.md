# Java Collections Class - Complete Guide & Cheatsheet

## 📋 Overview
The `java.util.Collections` class provides a comprehensive set of **static utility methods** for operating on or returning collections. These methods offer powerful functionality for common collection operations, making your code more efficient and readable.

## ✨ Key Features
- **All methods are static** - called directly on the `Collections` class
- **Works with various collection types** - primarily Lists and Sets
- **Provides algorithms** for sorting, searching, shuffling, and more
- **Includes factory methods** for creating specialized collections
- **Thread-safe wrappers** for synchronization

---

## 📝 Method Reference & Examples

### 1. **`addAll()`** - Add Multiple Elements
Adds variable number of elements to a collection.

```java
List<String> list = new ArrayList<>();
Collections.addAll(list, "element 1", "element 2", "element 3");
// List now contains: ["element 1", "element 2", "element 3"]
```

### 2. **`binarySearch()`** - Efficient Searching
Searches a sorted list using binary search algorithm (O(log n)).

```java
List<String> list = new ArrayList<>();
list.add("one");
list.add("two");
list.add("three");
list.add("four");
list.add("five");

// List MUST be sorted first!
Collections.sort(list);  // ["five", "four", "one", "three", "two"]

int index = Collections.binarySearch(list, "four");
// Returns: 1 (zero-based index)
// Returns negative value if element not found
```

### 3. **`copy()`** - Copy List Contents
Copies all elements from source list to destination list.

```java
List<String> source = new ArrayList<>();
Collections.addAll(source, "e1", "e2", "e3");

List<String> destination = new ArrayList<>();
// Destination list must have at least same size as source
Collections.addAll(destination, "", "", "");

Collections.copy(destination, source);
// destination now contains: ["e1", "e2", "e3"]
```

### 4. **`reverse()`** - Reverse Order
Reverses the order of elements in a list.

```java
List<String> list = new ArrayList<>();
Collections.addAll(list, "one", "two", "three");

Collections.reverse(list);
// List now: ["three", "two", "one"]
```

### 5. **`shuffle()`** - Randomize Order
Randomly shuffles elements in a list.

```java
List<String> list = new ArrayList<>();
Collections.addAll(list, "A", "B", "C", "D", "E");

Collections.shuffle(list);
// Random order each time, e.g.: ["C", "A", "E", "B", "D"]

// Optional: Provide Random instance for seeded shuffling
Collections.shuffle(list, new Random(42));
```

### 6. **`sort()`** - Sort Elements
Sorts elements in natural order or using a comparator.

```java
// Natural ordering
List<String> list = new ArrayList<>();
Collections.addAll(list, "zebra", "apple", "mango");

Collections.sort(list);
// Result: ["apple", "mango", "zebra"]

// With Comparator
Collections.sort(list, Comparator.reverseOrder());
// Result: ["zebra", "mango", "apple"]

// Sort objects by specific field
Collections.sort(people, Comparator.comparing(Person::getAge));
```

### 7. **`min()` / `max()`** - Find Extremes
Finds minimum or maximum element based on natural ordering.

```java
List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9);

Integer min = Collections.min(numbers);  // Returns: 1
Integer max = Collections.max(numbers);  // Returns: 9

// With Comparator
List<String> words = Arrays.asList("apple", "zoo", "banana");
String longest = Collections.max(words, 
    Comparator.comparing(String::length));  // Returns: "banana"
```

### 8. **`replaceAll()`** - Replace Elements
Replaces all occurrences of one element with another.

```java
List<String> list = new ArrayList<>();
Collections.addAll(list, "A", "B", "A", "C", "A");

boolean changed = Collections.replaceAll(list, "A", "X");
// List becomes: ["X", "B", "X", "C", "X"]
// Returns: true (at least one replacement made)
```

### 9. **`frequency()`** - Count Occurrences
Counts how many times an element appears in a collection.

```java
List<String> list = Arrays.asList("A", "B", "A", "C", "A", "B");

int count = Collections.frequency(list, "A");  // Returns: 3
```

### 10. **`disjoint()`** - Check for Common Elements
Checks if two collections have no elements in common.

```java
List<String> list1 = Arrays.asList("A", "B", "C");
List<String> list2 = Arrays.asList("X", "Y", "Z");

boolean noCommon = Collections.disjoint(list1, list2);  // Returns: true
```

### 11. **`fill()`** - Replace All Elements
Replaces all elements of a list with the specified element.

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));

Collections.fill(list, "X");
// List becomes: ["X", "X", "X"]
```

### 12. **`swap()`** - Swap Elements
Swaps elements at specified positions.

```java
List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));

Collections.swap(list, 1, 3);
// List becomes: ["A", "D", "C", "B"]
```

### 13. **`rotate()`** - Rotate Elements
Rotates elements by specified distance.

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

Collections.rotate(list, 2);
// List becomes: [4, 5, 1, 2, 3] (rotated right by 2)

Collections.rotate(list, -1);
// List becomes: [5, 1, 2, 3, 4] (rotated left by 1)
```

### 14. **Immutable Collections** - Create Unmodifiable Views
Create read-only views of collections.

```java
// Unmodifiable Set
Set<String> normalSet = new HashSet<>(Arrays.asList("A", "B", "C"));
Set<String> immutableSet = Collections.unmodifiableSet(normalSet);
// immutableSet.add("D"); // Throws UnsupportedOperationException

// Other unmodifiable collections:
List<String> unmodifiableList = Collections.unmodifiableList(list);
Map<K, V> unmodifiableMap = Collections.unmodifiableMap(map);
Collection<String> unmodifiableCollection = Collections.unmodifiableCollection(collection);
```

### 15. **Synchronized Collections** - Thread-safe Wrappers
Create thread-safe versions of collections.

```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
Map<String, String> syncMap = Collections.synchronizedMap(new HashMap<>());
```

### 16. **`singleton()` / `emptyX()`** - Special Collections
Create special purpose collections.

```java
// Singleton collections (immutable, single-element)
Set<String> singleSet = Collections.singleton("Hello");
List<String> singleList = Collections.singletonList("Hello");

// Empty collections (immutable, zero elements)
List<String> emptyList = Collections.emptyList();
Set<String> emptySet = Collections.emptySet();
Map<String, String> emptyMap = Collections.emptyMap();
```

### 17. **`checkedCollection()`** - Type-safe Wrappers
Create dynamically type-checked collections.

```java
List rawList = new ArrayList();
List<String> safeList = Collections.checkedList(rawList, String.class);
// safeList.add(123); // Throws ClassCastException immediately
```

---

## 📊 Quick Reference Cheatsheet

### 📋 Basic Operations
| Method | Purpose | Returns |
|--------|---------|---------|
| `addAll(Collection, T...)` | Add multiple elements | `boolean` |
| `binarySearch(List, key)` | Search sorted list | `int` (index) |
| `copy(dest, src)` | Copy list contents | `void` |
| `reverse(List)` | Reverse element order | `void` |
| `shuffle(List)` | Randomly shuffle | `void` |
| `sort(List)` | Sort (natural order) | `void` |
| `sort(List, Comparator)` | Sort with comparator | `void` |

### 🔍 Finding Elements
| Method | Purpose | Returns |
|--------|---------|---------|
| `min(Collection)` | Find minimum element | `T` |
| `max(Collection)` | Find maximum element | `T` |
| `frequency(Collection, obj)` | Count occurrences | `int` |
| `disjoint(Collection, Collection)` | Check for no common elements | `boolean` |

### ✏️ Modifying Collections
| Method | Purpose | Returns |
|--------|---------|---------|
| `replaceAll(List, old, new)` | Replace all occurrences | `boolean` |
| `fill(List, value)` | Replace all elements | `void` |
| `swap(List, i, j)` | Swap elements at indices | `void` |
| `rotate(List, distance)` | Rotate elements | `void` |

### 🛡️ Thread Safety & Immutability
| Method | Purpose |
|--------|---------|
| `synchronizedList(List)` | Thread-safe list wrapper |
| `synchronizedSet(Set)` | Thread-safe set wrapper |
| `synchronizedMap(Map)` | Thread-safe map wrapper |
| `unmodifiableList(List)` | Immutable list view |
| `unmodifiableSet(Set)` | Immutable set view |
| `unmodifiableMap(Map)` | Immutable map view |

### 🎯 Special Collections
| Method | Purpose |
|--------|---------|
| `singleton(T)` | Single-element immutable set |
| `singletonList(T)` | Single-element immutable list |
| `emptyList()` | Empty immutable list |
| `emptySet()` | Empty immutable set |
| `emptyMap()` | Empty immutable map |
| `checkedList(List, Class)` | Type-safe list wrapper |

---

## ⚠️ Important Notes & Best Practices

### 1. **Preconditions Matter**
```java
// binarySearch() requires sorted list
Collections.sort(list);  // MUST sort first!
int index = Collections.binarySearch(list, key);

// copy() requires destination with sufficient capacity
List<String> dest = new ArrayList<>(Collections.nCopies(src.size(), null));
Collections.copy(dest, src);
```

### 2. **Performance Considerations**
- `binarySearch()`: O(log n) but requires sorted list
- `sort()`: Typically O(n log n)
- `frequency()`: O(n) - scans entire collection
- `disjoint()`: O(min(n, m)) - stops at first common element

### 3. **Null Handling**
- Most methods throw `NullPointerException` if collection is null
- `min()`, `max()` throw NPE if collection contains null (natural ordering)
- Use comparators that handle nulls if needed

### 4. **Immutable vs Unmodifiable**
- **Unmodifiable wrappers**: View of existing collection (changes in original are visible)
- **Java 9+**: Use `List.of()`, `Set.of()`, `Map.of()` for true immutable collections

### 5. **Type Safety**
```java
// Raw type (unsafe)
List rawList = new ArrayList();

// Type-safe wrapper (fails fast)
List<String> safeList = Collections.checkedList(rawList, String.class);
safeList.add(123);  // Immediate ClassCastException
```

---

## 🔄 Common Patterns & Examples

### Sorting with Custom Comparator
```java
List<Person> people = getPeople();
Collections.sort(people, 
    Comparator.comparing(Person::getLastName)
              .thenComparing(Person::getFirstName));
```

### Finding Top N Elements
```java
List<Integer> numbers = getNumbers();
int max = Collections.max(numbers);
int min = Collections.min(numbers);
```

### Creating Thread-safe Collections
```java
// Decorate at creation time
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
Map<String, Integer> syncMap = Collections.synchronizedMap(new HashMap<>());

// Iteration requires manual synchronization
synchronized(syncList) {
    for (String item : syncList) {
        // Process item
    }
}
```

### Efficient List Initialization
```java
// One-liner initialization
List<String> list = new ArrayList<>();
Collections.addAll(list, "A", "B", "C", "D", "E");

// Using Arrays.asList (fixed-size)
List<String> fixedList = Arrays.asList("A", "B", "C");
```


## 🎯 Pro Tips

1. **Use `Collections.emptyList()`** instead of `new ArrayList<>()` for empty returns
2. **Prefer `Collections.singletonList()`** for single-element returns
3. **Remember that `binarySearch()` returns**: 
   - Index if found
   - `(-(insertion point) - 1)` if not found
4. **`unmodifiableXXX()` creates a view** - original collection can still be modified
5. **For concurrent access**: Consider `java.util.concurrent` collections instead of synchronized wrappers


## 📚 Related Classes

- **`Arrays`**: Similar utility methods for arrays
- **`Objects`**: Utility methods for object operations
- **`Comparator`**: Used with `Collections.sort()` for custom ordering
- **`java.util.concurrent`**: Advanced concurrent collections

This comprehensive guide covers the essential methods of the `Collections` class. Bookmark this cheatsheet for quick reference during your Java development work!