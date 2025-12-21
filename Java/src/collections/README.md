# Java Sorting Collections - Complete Guide & Cheatsheet
## 📋 Overview
**Sorting** is a fundamental operation in Java Collections that arranges elements in a specific order. Java provides multiple ways to sort collections, from simple natural ordering to complex custom sorting using Comparators. This guide covers all aspects of sorting Java Collections.

---

## 🔑 Core Concepts

### ✅ **Sorting Mechanisms**
1. **Natural Ordering**: Objects implement `Comparable<T>` interface
2. **Custom Ordering**: Using `Comparator<T>` interface
3. **Multiple Criteria**: Chaining comparators for complex sorting

### ✅ **Sortable Collections**
| Collection | Sortable? | Method | Notes |
|------------|-----------|--------|-------|
| `List` | ✅ Yes | `Collections.sort()` or `list.sort()` | Most commonly sorted |
| `ArrayList` | ✅ Yes | `Collections.sort()` or `list.sort()` | Random access, efficient |
| `LinkedList` | ✅ Yes | `Collections.sort()` or `list.sort()` | Less efficient for sorting |
| `Set` | ❌ No | Convert to `List` first | `TreeSet` maintains order automatically |
| `Map` | ❌ No | Sort keys/values/entries | Convert to `List<Map.Entry>` |
| `Array` | ✅ Yes | `Arrays.sort()` | Primitive and object arrays |

### ✅ **Sorting Algorithms**
- **Timsort**: Hybrid (merge + insertion) - used by Java for objects
- **Dual-Pivot Quicksort**: Used for primitive arrays
- **Stable Sort**: Equal elements maintain relative order
- **Time Complexity**: O(n log n) average and worst case

---

## 📊 Sorting Lists

### **Basic List Sorting**
```java
import java.util.*;

// Natural ordering (requires Comparable)
List<String> names = new ArrayList<>();
names.add("Charlie");
names.add("Alice");
names.add("Bob");

Collections.sort(names);  // Sorts in place
// Result: ["Alice", "Bob", "Charlie"]

// Java 8+ List.sort() method
names.sort(Comparator.naturalOrder());

// Reverse natural order
Collections.sort(names, Comparator.reverseOrder());
// Or: names.sort(Comparator.reverseOrder());
```

### **Sorting with Comparable**
```java
// Class implementing Comparable
public class Product implements Comparable<Product> {
    private String name;
    private double price;
    
    @Override
    public int compareTo(Product other) {
        // Compare by name (natural order)
        return this.name.compareTo(other.name);
        
        // Alternative: Compare by price
        // return Double.compare(this.price, other.price);
    }
}

// Usage
List<Product> products = new ArrayList<>();
products.add(new Product("Laptop", 999.99));
products.add(new Product("Mouse", 29.99));
products.add(new Product("Keyboard", 79.99));

Collections.sort(products);  // Uses Product.compareTo()

// With Java 8+
products.sort(Comparator.naturalOrder());
```

### **Sorting with Comparator**
```java
// 1. Traditional Comparator class
public class PriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p1.getPrice(), p2.getPrice());
    }
}

// Usage
Collections.sort(products, new PriceComparator());

// 2. Anonymous inner class (Java 7 and earlier)
Collections.sort(products, new Comparator<Product>() {
    @Override
    public int compare(Product p1, Product p2) {
        return p1.getName().compareTo(p2.getName());
    }
});

// 3. Lambda expression (Java 8+)
Collections.sort(products, (p1, p2) -> 
    p1.getName().compareTo(p2.getName()));

// 4. Method reference (Java 8+)
Collections.sort(products, Comparator.comparing(Product::getName));

// 5. Using List.sort() with Comparator
products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
products.sort(Comparator.comparing(Product::getName));
```

---

## 🛠️ Comparator Factory Methods (Java 8+)

### **Basic Comparator Creation**
```java
List<Product> products = getProducts();

// Comparing by single field
products.sort(Comparator.comparing(Product::getName));
products.sort(Comparator.comparing(Product::getPrice));

// Reverse order
products.sort(Comparator.comparing(Product::getName).reversed());
products.sort(Comparator.reverseOrder());  // For Comparable objects

// Natural order
products.sort(Comparator.naturalOrder());

// Null handling
products.sort(Comparator.nullsFirst(Comparator.comparing(Product::getName)));
products.sort(Comparator.nullsLast(Comparator.comparing(Product::getPrice)));

// Case-insensitive string comparison
List<String> names = Arrays.asList("apple", "Orange", "banana", "ORANGE");
names.sort(String.CASE_INSENSITIVE_ORDER);
// Result: ["apple", "banana", "Orange", "ORANGE"]
// Note: Maintains original case, only compares ignoring case
```

### **Chaining Comparators (Multiple Fields)**
```java
// Sort by category, then by price, then by name
products.sort(Comparator
    .comparing(Product::getCategory)
    .thenComparing(Product::getPrice)
    .thenComparing(Product::getName));

// With different sort orders
products.sort(Comparator
    .comparing(Product::getCategory)
    .thenComparing(Comparator.comparing(Product::getPrice).reversed())
    .thenComparing(Product::getName));

// Using explicit comparators
products.sort(Comparator
    .comparing((Product p) -> p.getCategory().toLowerCase())
    .thenComparingInt(Product::getStockQuantity)
    .thenComparing(Product::getName, Comparator.reverseOrder()));
```

### **Specialized Comparators**
```java
// Comparing by int, long, double (avoids boxing)
products.sort(Comparator.comparingInt(Product::getId));
products.sort(Comparator.comparingLong(Product::getTimestamp));
products.sort(Comparator.comparingDouble(Product::getPrice));

// Custom comparison logic
products.sort(Comparator.comparing(
    Product::getName,
    (name1, name2) -> {
        // Custom logic, e.g., compare by length then alphabetically
        int lengthCompare = Integer.compare(name1.length(), name2.length());
        return lengthCompare != 0 ? lengthCompare : name1.compareTo(name2);
    }
));

// Extract and transform before comparing
products.sort(Comparator.comparing(
    p -> p.getName().toLowerCase(),
    Comparator.nullsFirst(String::compareTo)
));
```

---

## 🔄 Advanced Sorting Patterns

### **Sorting Maps**
```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Alice", 95);
scores.put("Bob", 85);
scores.put("Charlie", 90);

// 1. Sort by keys
List<Map.Entry<String, Integer>> entriesByKey = 
    new ArrayList<>(scores.entrySet());
entriesByKey.sort(Map.Entry.comparingByKey());
// Or: entriesByKey.sort(Comparator.comparing(Map.Entry::getKey));

// 2. Sort by values
List<Map.Entry<String, Integer>> entriesByValue = 
    new ArrayList<>(scores.entrySet());
entriesByValue.sort(Map.Entry.comparingByValue());
// Reverse order: Map.Entry.comparingByValue(Comparator.reverseOrder())

// 3. Create new sorted map (LinkedHashMap maintains order)
Map<String, Integer> sortedByKey = new LinkedHashMap<>();
entriesByKey.forEach(entry -> 
    sortedByKey.put(entry.getKey(), entry.getValue()));

Map<String, Integer> sortedByValue = new LinkedHashMap<>();
entriesByValue.forEach(entry -> 
    sortedByValue.put(entry.getKey(), entry.getValue()));

// 4. Using Streams (Java 8+)
Map<String, Integer> sortedMap = scores.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (e1, e2) -> e1,  // merge function
        LinkedHashMap::new  // maintains insertion order
    ));
```

### **Sorting Sets**
```java
// TreeSet automatically maintains sorted order
Set<String> sortedSet = new TreeSet<>();
sortedSet.add("Charlie");
sortedSet.add("Alice");
sortedSet.add("Bob");
// Automatically sorted: ["Alice", "Bob", "Charlie"]

// With custom Comparator
Set<Product> productsByPrice = new TreeSet<>(
    Comparator.comparing(Product::getPrice)
);

// Convert HashSet/LinkedHashSet to sorted List
Set<String> namesSet = new HashSet<>(Arrays.asList("Charlie", "Alice", "Bob"));
List<String> sortedNames = new ArrayList<>(namesSet);
Collections.sort(sortedNames);
```

### **Sorting Arrays**
```java
// Primitive arrays
int[] numbers = {5, 2, 8, 1, 9};
Arrays.sort(numbers);  // [1, 2, 5, 8, 9]

// Object arrays (requires Comparable or Comparator)
String[] names = {"Charlie", "Alice", "Bob"};
Arrays.sort(names);  // ["Alice", "Bob", "Charlie"]

// With Comparator
Product[] products = getProductArray();
Arrays.sort(products, Comparator.comparing(Product::getPrice));

// Parallel sort (Java 8+) - faster for large arrays
int[] largeArray = new int[1_000_000];
// Fill array...
Arrays.parallelSort(largeArray);
```

### **Sorting with Null Values**
```java
List<String> namesWithNulls = Arrays.asList("Charlie", null, "Alice", null, "Bob");

// Nulls first
namesWithNulls.sort(Comparator.nullsFirst(String::compareTo));
// Result: [null, null, "Alice", "Bob", "Charlie"]

// Nulls last
namesWithNulls.sort(Comparator.nullsLast(String::compareTo));
// Result: ["Alice", "Bob", "Charlie", null, null]

// With custom comparator
namesWithNulls.sort(Comparator.nullsLast(
    Comparator.comparing(String::toLowerCase)
));
```

### **Case-Insensitive and Locale-Specific Sorting**
```java
List<String> names = Arrays.asList("ångström", "Zebra", "apple", "Øresund");

// Default (lexicographic, Unicode order)
Collections.sort(names);
// Result depends on Unicode code points

// Case-insensitive
Collections.sort(names, String.CASE_INSENSITIVE_ORDER);

// Locale-specific sorting
Collections.sort(names, Collator.getInstance(Locale.US));
Collections.sort(names, Collator.getInstance(Locale.FRENCH));

// Swedish locale sorts å, ä, ö correctly
Collections.sort(names, Collator.getInstance(new Locale("sv", "SE")));

// Primary strength (ignore case and accents)
Collator collator = Collator.getInstance(Locale.US);
collator.setStrength(Collator.PRIMARY);
Collections.sort(names, collator);
```

---

## ⚡ Performance Considerations

### **Sorting Algorithm Characteristics**
| Collection/Array Type | Algorithm | Time Complexity | Space Complexity | Stable? |
|-----------------------|-----------|-----------------|------------------|---------|
| `Collections.sort()` | Timsort | O(n log n) | O(n) | ✅ Yes |
| `Arrays.sort()` (objects) | Timsort | O(n log n) | O(n) | ✅ Yes |
| `Arrays.sort()` (primitives) | Dual-Pivot Quicksort | O(n log n) average, O(n²) worst | O(log n) | ❌ No |
| `Arrays.parallelSort()` | Parallel sort-merge | O(n log n) | O(n) | ✅ Yes |
| `TreeSet` (insertion) | Red-Black Tree | O(log n) per insertion | O(n) | N/A |

### **Performance Tips**
```java
// 1. Pre-size ArrayList for better performance
List<Product> largeList = new ArrayList<>(1_000_000);
// Add elements...
Collections.sort(largeList);

// 2. Use Arrays.sort() for primitive arrays (faster than object arrays)
int[] primitiveArray = new int[1_000_000];
Arrays.sort(primitiveArray);  // Faster than Integer[]

// 3. Consider parallel sort for very large arrays
double[] hugeArray = new double[10_000_000];
Arrays.parallelSort(hugeArray);  // Uses ForkJoinPool

// 4. Cache comparators for repeated use
Comparator<Product> priceComparator = Comparator.comparing(Product::getPrice);
// Reuse this comparator instead of creating new ones

// 5. For frequent sorting, consider TreeSet
Set<Product> sortedProducts = new TreeSet<>(Comparator.comparing(Product::getPrice));
// Automatically maintains order during insertion

// 6. Avoid sorting when not needed
if (list.size() > 1) {
    Collections.sort(list);
}

// 7. Use appropriate data structure
// If you need frequently sorted data, consider:
// - TreeMap for sorted key-value pairs
// - TreeSet for sorted unique elements
// - PriorityQueue for sorted access (not iteration)
```

### **Memory Efficiency**
```java
// Sorting in-place vs. creating new collections
List<String> names = getNames();

// In-place sorting (memory efficient)
Collections.sort(names);  // Modifies original list

// Creating new sorted list (preserves original)
List<String> sortedNames = new ArrayList<>(names);
Collections.sort(sortedNames);

// Using streams (creates new collection)
List<String> streamSorted = names.stream()
    .sorted()
    .collect(Collectors.toList());

// For very large datasets, consider external sorting
// or database sorting instead of in-memory
```

---

## 🎯 Real-World Examples

### **Example 1: Employee Sorting System**
```java
public class EmployeeSorter {
    
    public static List<Employee> sortEmployees(List<Employee> employees, 
                                               SortCriteria criteria) {
        Comparator<Employee> comparator = switch (criteria) {
            case BY_NAME -> Comparator.comparing(Employee::getLastName)
                                     .thenComparing(Employee::getFirstName);
            case BY_DEPARTMENT -> Comparator.comparing(Employee::getDepartment)
                                           .thenComparing(Employee::getLastName);
            case BY_SALARY -> Comparator.comparing(Employee::getSalary).reversed()
                                       .thenComparing(Employee::getLastName);
            case BY_HIRE_DATE -> Comparator.comparing(Employee::getHireDate)
                                          .thenComparing(Employee::getLastName);
            case BY_MULTIPLE -> Comparator.comparing(Employee::getDepartment)
                                         .thenComparing(Employee::getTitle)
                                         .thenComparing(Employee::getSalary).reversed()
                                         .thenComparing(Employee::getLastName);
        };
        
        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(comparator);
        return sorted;
    }
    
    public enum SortCriteria {
        BY_NAME, BY_DEPARTMENT, BY_SALARY, BY_HIRE_DATE, BY_MULTIPLE
    }
}

// Usage
List<Employee> employees = getEmployees();
List<Employee> sortedBySalary = EmployeeSorter.sortEmployees(
    employees, EmployeeSorter.SortCriteria.BY_SALARY
);
```

### **Example 2: Product Catalog with Multiple Sort Options**
```java
public class ProductCatalog {
    private List<Product> products;
    
    public void sort(SortOption option, boolean ascending) {
        Comparator<Product> comparator = createComparator(option);
        
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        products.sort(comparator);
    }
    
    private Comparator<Product> createComparator(SortOption option) {
        return switch (option) {
            case NAME -> Comparator.comparing(Product::getName, 
                String.CASE_INSENSITIVE_ORDER);
            case PRICE -> Comparator.comparing(Product::getPrice);
            case RATING -> Comparator.comparing(Product::getAverageRating).reversed();
            case POPULARITY -> Comparator.comparing(Product::getSalesCount).reversed();
            case DATE_ADDED -> Comparator.comparing(Product::getDateAdded).reversed();
            case RELEVANCE -> {
                // Complex relevance score calculation
                yield Comparator.comparing((Product p) -> 
                    calculateRelevanceScore(p)).reversed();
            }
        };
    }
    
    public enum SortOption {
        NAME, PRICE, RATING, POPULARITY, DATE_ADDED, RELEVANCE
    }
    
    private double calculateRelevanceScore(Product product) {
        // Implementation of relevance algorithm
        return 0.0; // Simplified
    }
}
```

### **Example 3: Leaderboard System**
```java
public class Leaderboard {
    private Map<String, PlayerScore> scores = new HashMap<>();
    
    public List<PlayerScore> getTopPlayers(int limit) {
        return scores.values().stream()
            .sorted(Comparator
                .comparing(PlayerScore::getScore).reversed()
                .thenComparing(PlayerScore::getLastUpdated)
                .thenComparing(PlayerScore::getPlayerName))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    public List<PlayerScore> getPlayersByLevel(int level) {
        return scores.values().stream()
            .filter(score -> score.getLevel() == level)
            .sorted(Comparator
                .comparing(PlayerScore::getScore).reversed()
                .thenComparing(PlayerScore::getTimePlayed))
            .collect(Collectors.toList());
    }
    
    public void updateScore(String playerId, int points) {
        PlayerScore score = scores.getOrDefault(playerId, 
            new PlayerScore(playerId));
        score.addPoints(points);
        scores.put(playerId, score);
    }
    
    public static class PlayerScore implements Comparable<PlayerScore> {
        private final String playerName;
        private int score;
        private int level;
        private long lastUpdated;
        private long timePlayed;
        
        @Override
        public int compareTo(PlayerScore other) {
            return Integer.compare(this.score, other.score);
        }
        
        // Getters, setters, constructor
    }
}
```

### **Example 4: File System Browser with Sorting**
```java
public class FileSystemBrowser {
    
    public List<FileInfo> listFiles(File directory, SortBy sortBy) {
        File[] files = directory.listFiles();
        if (files == null) return Collections.emptyList();
        
        List<FileInfo> fileInfos = Arrays.stream(files)
            .map(FileInfo::new)
            .collect(Collectors.toList());
        
        fileInfos.sort(getComparator(sortBy));
        return fileInfos;
    }
    
    private Comparator<FileInfo> getComparator(SortBy sortBy) {
        return switch (sortBy) {
            case NAME -> Comparator.comparing(FileInfo::getName, 
                String.CASE_INSENSITIVE_ORDER);
            case SIZE -> Comparator.comparing(FileInfo::getSize);
            case DATE_MODIFIED -> Comparator.comparing(FileInfo::getLastModified).reversed();
            case TYPE -> Comparator.comparing(FileInfo::getType)
                                  .thenComparing(FileInfo::getName);
            case NAME_DESCENDING -> Comparator.comparing(FileInfo::getName).reversed();
        };
    }
    
    public enum SortBy {
        NAME, SIZE, DATE_MODIFIED, TYPE, NAME_DESCENDING
    }
    
    public static class FileInfo {
        private final String name;
        private final long size;
        private final long lastModified;
        private final String type;
        
        public FileInfo(File file) {
            this.name = file.getName();
            this.size = file.length();
            this.lastModified = file.lastModified();
            this.type = getFileType(file);
        }
        
        private String getFileType(File file) {
            if (file.isDirectory()) return "Directory";
            String name = file.getName();
            int dotIndex = name.lastIndexOf('.');
            return dotIndex > 0 ? name.substring(dotIndex + 1).toUpperCase() : "File";
        }
        
        // Getters
    }
}
```

### **Example 5: Multi-language String Sorting**
```java
public class InternationalSorter {
    
    public List<String> sortStrings(List<String> strings, Locale locale) {
        List<String> sorted = new ArrayList<>(strings);
        
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(Collator.SECONDARY);  // Ignore case but consider accents
        
        sorted.sort(collator);
        return sorted;
    }
    
    public Map<String, List<String>> groupAndSortByFirstLetter(
            List<String> strings, Locale locale) {
        
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(Collator.PRIMARY);  // Group by base letter
        
        // Sort the list
        strings.sort(collator);
        
        // Group by first letter
        Map<String, List<String>> grouped = new TreeMap<>();
        
        for (String str : strings) {
            if (str == null || str.isEmpty()) continue;
            
            // Get the first character's collation key
            String firstLetter = collator.getCollationKey(str.substring(0, 1))
                                        .getSourceString();
            
            grouped.computeIfAbsent(firstLetter, k -> new ArrayList<>())
                  .add(str);
        }
        
        // Sort each group
        for (List<String> group : grouped.values()) {
            group.sort(collator);
        }
        
        return grouped;
    }
    
    // Example with different locales
    public void demonstrate() {
        List<String> names = Arrays.asList(
            "café", "cafe", "čaj", "chateau", "école", "Édouard", "zebra"
        );
        
        System.out.println("US English:");
        sortStrings(names, Locale.US).forEach(System.out::println);
        
        System.out.println("\nFrench:");
        sortStrings(names, Locale.FRENCH).forEach(System.out::println);
        
        System.out.println("\nCzech:");
        sortStrings(names, new Locale("cs", "CZ")).forEach(System.out::println);
    }
}
```

---

## 🔍 Debugging & Testing

### **Testing Sorting Logic**
```java
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SortingTest {
    
    @Test
    void testNaturalSorting() {
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        List<String> expected = Arrays.asList("Alice", "Bob", "Charlie");
        
        Collections.sort(names);
        assertEquals(expected, names);
    }
    
    @Test
    void testCustomComparator() {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99),
            new Product("Mouse", 29.99),
            new Product("Keyboard", 79.99)
        );
        
        List<Product> expected = Arrays.asList(
            new Product("Mouse", 29.99),
            new Product("Keyboard", 79.99),
            new Product("Laptop", 999.99)
        );
        
        products.sort(Comparator.comparing(Product::getPrice));
        assertEquals(expected, products);
    }
    
    @Test
    void testStableSort() {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "Sales"),
            new Employee("Bob", "Engineering"),
            new Employee("Charlie", "Sales"),
            new Employee("David", "Engineering")
        );
        
        // Sort by department, then by name
        employees.sort(Comparator
            .comparing(Employee::getDepartment)
            .thenComparing(Employee::getName));
        
        // Verify stable sort within same department
        assertEquals("Alice", employees.get(0).getName());  // First in Sales
        assertEquals("Charlie", employees.get(1).getName()); // Second in Sales
        assertEquals("Bob", employees.get(2).getName());    // First in Engineering
        assertEquals("David", employees.get(3).getName());  // Second in Engineering
    }
    
    @Test
    void testReverseSort() {
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9);
        List<Integer> expected = Arrays.asList(9, 8, 5, 2, 1);
        
        numbers.sort(Comparator.reverseOrder());
        assertEquals(expected, numbers);
    }
    
    @Test
    void testNullHandling() {
        List<String> names = Arrays.asList("Charlie", null, "Alice", null, "Bob");
        List<String> expected = Arrays.asList(null, null, "Alice", "Bob", "Charlie");
        
        names.sort(Comparator.nullsFirst(String::compareTo));
        assertEquals(expected, names);
    }
    
    @Test
    void testPerformance() {
        List<Integer> largeList = new ArrayList<>();
        for (int i = 0; i < 1_000_000; i++) {
            largeList.add((int) (Math.random() * 1_000_000));
        }
        
        long start = System.currentTimeMillis();
        Collections.sort(largeList);
        long end = System.currentTimeMillis();
        
        assertTrue(end - start < 1000, "Sorting should complete within 1 second");
        
        // Verify sorted
        for (int i = 1; i < largeList.size(); i++) {
            assertTrue(largeList.get(i - 1) <= largeList.get(i), 
                      "List should be sorted");
        }
    }
}
```

### **Debugging Tips**
```java
// 1. Log sorting process
public class DebuggingComparator<T> implements Comparator<T> {
    private final Comparator<T> delegate;
    private final String name;
    
    public DebuggingComparator(Comparator<T> delegate, String name) {
        this.delegate = delegate;
        this.name = name;
    }
    
    @Override
    public int compare(T o1, T o2) {
        int result = delegate.compare(o1, o2);
        System.out.printf("%s.compare(%s, %s) = %d%n", 
            name, o1, o2, result);
        return result;
    }
}

// Usage
products.sort(new DebuggingComparator<>(
    Comparator.comparing(Product::getPrice),
    "PriceComparator"
));

// 2. Verify sort stability
List<Employee> original = new ArrayList<>(employees);
employees.sort(comparator);

// Check if equal elements maintain relative order
Map<String, List<Employee>> byDept = new HashMap<>();
for (Employee emp : original) {
    byDept.computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>())
          .add(emp);
}

// 3. Test with edge cases
void testSortingEdgeCases() {
    // Empty list
    List<String> empty = new ArrayList<>();
    Collections.sort(empty);
    assertTrue(empty.isEmpty());
    
    // Single element
    List<String> single = Collections.singletonList("Single");
    Collections.sort(single);
    assertEquals(1, single.size());
    
    // Already sorted
    List<Integer> sorted = Arrays.asList(1, 2, 3, 4, 5);
    Collections.sort(sorted);
    assertEquals(Arrays.asList(1, 2, 3, 4, 5), sorted);
    
    // Reverse sorted
    List<Integer> reverseSorted = Arrays.asList(5, 4, 3, 2, 1);
    Collections.sort(reverseSorted);
    assertEquals(Arrays.asList(1, 2, 3, 4, 5), reverseSorted);
    
    // All equal
    List<Integer> allEqual = Arrays.asList(1, 1, 1, 1);
    Collections.sort(allEqual);
    assertEquals(Arrays.asList(1, 1, 1, 1), allEqual);
}
```

---

## 🎯 Quick Reference Guide

### **Sorting Methods Cheatsheet**
```
Collections.sort(list)                     // Natural order, modifies list
Collections.sort(list, comparator)         // Custom order, modifies list
list.sort(comparator)                      // Java 8+, modifies list
list.sort(null)                            // Natural order (Java 8+)

Arrays.sort(array)                         // Natural order, primitive/object arrays
Arrays.sort(array, comparator)             // Custom order for object arrays
Arrays.parallelSort(array)                 // Parallel sort for large arrays

stream.sorted()                            // Natural order stream
stream.sorted(comparator)                  // Custom order stream

new TreeSet<>(comparator)                  // Automatically sorted set
new TreeMap<>(comparator)                  // Automatically sorted map
```

### **Comparator Creation Cheatsheet**
```
Comparator.naturalOrder()                  // Natural ordering
Comparator.reverseOrder()                  // Reverse natural ordering
Comparator.comparing(Function)             // By field extraction
Comparator.comparingInt(ToIntFunction)     // By int field (no boxing)
Comparator.comparingDouble(...)            // By double field
Comparator.comparingLong(...)              // By long field

Comparator.nullsFirst(comparator)          // Nulls come first
Comparator.nullsLast(comparator)           // Nulls come last

String.CASE_INSENSITIVE_ORDER              // Case-insensitive string comparator
Collator.getInstance(locale)               // Locale-specific string comparator

comparator.reversed()                      // Reverse existing comparator
comparator1.thenComparing(comparator2)     // Chain comparators
```

### **Common Sorting Patterns**
```
// Basic patterns
names.sort(String::compareTo);                     // Natural string order
names.sort(Comparator.reverseOrder());             // Reverse natural order
people.sort(Comparator.comparing(Person::getAge)); // By age
products.sort(Comparator.comparing(Product::getPrice).reversed()); // Price descending

// Multi-field sorting
employees.sort(Comparator
    .comparing(Employee::getDepartment)
    .thenComparing(Employee::getLastName)
    .thenComparing(Employee::getFirstName));

// Case-insensitive
names.sort(String.CASE_INSENSITIVE_ORDER);

// Null handling
list.sort(Comparator.nullsLast(Comparator.naturalOrder()));

// Custom logic
items.sort((a, b) -> customComparison(a, b));
```

### **Performance Decision Guide**
```
Small collections (< 1,000 items):
  → Collections.sort() or list.sort()
  → No significant performance differences

Medium collections (1,000 - 100,000 items):
  → Collections.sort() for Lists
  → Arrays.sort() for arrays
  → Consider caching comparators

Large collections (> 100,000 items):
  → Arrays.parallelSort() for primitive arrays
  → Consider external sorting or database
  → Profile and test with actual data

Frequent sorting operations:
  → Use TreeSet or TreeMap for automatic sorting
  → Consider maintaining sorted order during insertion
  → Cache sorted results if possible

Memory-constrained environment:
  → Sort in-place (modifies original)
  → Avoid creating multiple copies
  → Use arrays instead of collections for primitives
```

---

## 📖 Summary

**Java Sorting Collections** provides powerful, flexible ways to order data:
1. **Natural ordering** via `Comparable` interface
2. **Custom ordering** via `Comparator` interface
3. **Multiple sorting strategies** with comparator chaining
4. **Performance-optimized algorithms** (Timsort, Dual-Pivot Quicksort)

**Key Takeaways:**
1. **Use `Collections.sort()` or `list.sort()`** for List sorting
2. **Implement `Comparable`** for natural ordering of your objects
3. **Use `Comparator` factories** (Java 8+) for concise, readable sorting logic
4. **Chain comparators** with `thenComparing()` for multi-field sorting
5. **Handle nulls properly** with `nullsFirst()` or `nullsLast()`
6. **Consider performance** - choose appropriate algorithm for your data size
7. **Test edge cases** - empty lists, null values, already sorted data
8. **Use locale-aware sorting** (`Collator`) for international applications

This comprehensive guide covers everything from basic sorting to advanced patterns. Bookmark this cheatsheet for quick reference during your Java development work!