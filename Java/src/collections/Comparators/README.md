# Java Comparator Interface - Complete Guide & Cheatsheet

## 📋 Overview
The **Java Comparator interface** (`java.util.Comparator`) is a functional interface that defines a **custom ordering** for objects. It provides a way to compare two objects externally, separate from the objects' natural ordering defined by `Comparable`. Comparators are essential for sorting collections with custom logic, multi-field sorting, and defining alternative orderings.

---

## 🔑 Core Concepts

### ✅ **Comparator vs Comparable**
| Feature | Comparator | Comparable |
|---------|------------|------------|
| **Interface** | `java.util.Comparator` | `java.lang.Comparable` |
| **Method** | `compare(T o1, T o2)` | `compareTo(T o)` |
| **Order** | External ordering | Natural (internal) ordering |
| **Implementation** | Separate class | Inside the class |
| **Multiple Orders** | ✅ Multiple comparators per class | ❌ Only one natural order |
| **Modification** | Can compare objects without modifying them | Requires modifying the class |
| **Usage** | `Collections.sort(list, comparator)` | `Collections.sort(list)` |

### ✅ **Comparator Characteristics**
- **Functional Interface**: Single abstract method (`compare()`)
- **Stateless**: Should not depend on mutable state
- **Consistent**: Must follow transitive, symmetric, and reflexive rules
- **Null Handling**: Can define null ordering behavior

### ✅ **Common Use Cases**
- Sorting collections with custom logic
- Defining multiple sorting strategies
- Sorting objects you cannot modify
- Complex multi-field sorting
- Reverse or custom ordering

---

## 🏗️ Comparator Implementation Patterns

### **1. Traditional Class Implementation**
```java
public class AgeComparator implements Comparator<Person> {
    @Override
    public int compare(Person p1, Person p2) {
        return Integer.compare(p1.getAge(), p2.getAge());
    }
}

// Usage
List<Person> people = getPeople();
Collections.sort(people, new AgeComparator());
```

### **2. Anonymous Inner Class**
```java
List<Person> people = getPeople();

Collections.sort(people, new Comparator<Person>() {
    @Override
    public int compare(Person p1, Person p2) {
        return p1.getName().compareTo(p2.getName());
    }
});
```

### **3. Lambda Expression (Java 8+)**
```java
List<Person> people = getPeople();

// Single expression
people.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

// Multi-line lambda
people.sort((p1, p2) -> {
    int nameCompare = p1.getName().compareTo(p2.getName());
    if (nameCompare != 0) return nameCompare;
    return Integer.compare(p1.getAge(), p2.getAge());
});
```

### **4. Method Reference**
```java
List<Person> people = getPeople();

// Using existing methods
people.sort(Comparator.comparing(Person::getName));
people.sort(Comparator.comparing(Person::getAge));
people.sort(Comparator.comparing(Person::getSalary));

// Static method reference
people.sort(Person::compareByAge);  // If Person has static compareByAge method
```

### **5. Comparator.comparing() Factory (Java 8+)**
```java
List<Person> people = getPeople();

// Basic field comparison
Comparator<Person> byName = Comparator.comparing(Person::getName);
Comparator<Person> byAge = Comparator.comparingInt(Person::getAge);
Comparator<Person> bySalary = Comparator.comparingDouble(Person::getSalary);

// With custom key extractor
Comparator<Person> byNameLength = Comparator.comparing(
    p -> p.getName().length()
);

// With custom comparator for extracted key
Comparator<Person> caseInsensitive = Comparator.comparing(
    Person::getName,
    String.CASE_INSENSITIVE_ORDER
);
```

---

## 🛠️ Comparator Factory Methods (Java 8+)

### **Basic Factories**
```java
// Natural ordering
Comparator<String> natural = Comparator.naturalOrder();
Comparator<String> reverseNatural = Comparator.reverseOrder();

// Null handling
Comparator<String> nullsFirst = Comparator.nullsFirst(String::compareTo);
Comparator<String> nullsLast = Comparator.nullsLast(String::compareTo);

// Primitive specializations (avoid boxing)
Comparator<Integer> comparingInt = Comparator.comparingInt(Integer::intValue);
Comparator<Long> comparingLong = Comparator.comparingLong(Long::longValue);
Comparator<Double> comparingDouble = Comparator.comparingDouble(Double::doubleValue);
```

### **Chaining Comparators**
```java
// Multiple field sorting
Comparator<Person> complexComparator = Comparator
    .comparing(Person::getLastName)
    .thenComparing(Person::getFirstName)
    .thenComparingInt(Person::getAge)
    .thenComparing(Person::getSalary, Comparator.reverseOrder());

// Usage
people.sort(complexComparator);

// Mixing different comparison types
Comparator<Employee> empComparator = Comparator
    .comparing(Employee::getDepartment)
    .thenComparing(Employee::getTitle)
    .thenComparingInt(Employee::getYearsOfService)
    .thenComparing(Employee::getLastName, Comparator.reverseOrder())
    .thenComparing(Employee::getFirstName);
```

### **Custom Comparator Logic**
```java
// Custom comparison logic
Comparator<Person> custom = (p1, p2) -> {
    // Compare by age group first
    int ageGroup1 = p1.getAge() / 10; // Decade
    int ageGroup2 = p2.getAge() / 10;
    
    if (ageGroup1 != ageGroup2) {
        return Integer.compare(ageGroup1, ageGroup2);
    }
    
    // Then by last name
    int lastNameCompare = p1.getLastName().compareTo(p2.getLastName());
    if (lastNameCompare != 0) return lastNameCompare;
    
    // Then by first name
    return p1.getFirstName().compareTo(p2.getFirstName());
};

// Using Comparator.comparing with custom comparator
Comparator<Person> byCustom = Comparator.comparing(
    Person::getLastName,
    (s1, s2) -> {
        // Custom string comparison, e.g., compare by length then alphabetically
        int lengthCompare = Integer.compare(s1.length(), s2.length());
        return lengthCompare != 0 ? lengthCompare : s1.compareTo(s2);
    }
);
```

---

## 📊 Advanced Comparator Techniques

### **Multi-Criteria Sorting**
```java
public class MultiCriteriaSorter {
    
    public static Comparator<Employee> getComparator(SortField... fields) {
        Comparator<Employee> comparator = null;
        
        for (SortField field : fields) {
            Comparator<Employee> fieldComparator = getFieldComparator(field);
            
            if (comparator == null) {
                comparator = fieldComparator;
            } else {
                comparator = comparator.thenComparing(fieldComparator);
            }
        }
        
        return comparator != null ? comparator : Comparator.naturalOrder();
    }
    
    private static Comparator<Employee> getFieldComparator(SortField field) {
        return switch (field.getField()) {
            case "name" -> Comparator.comparing(Employee::getName);
            case "department" -> Comparator.comparing(Employee::getDepartment);
            case "salary" -> Comparator.comparing(Employee::getSalary);
            case "hireDate" -> Comparator.comparing(Employee::getHireDate);
            default -> throw new IllegalArgumentException("Unknown field: " + field.getField());
        };
    }
    
    public static class SortField {
        private final String field;
        private final boolean ascending;
        
        public SortField(String field, boolean ascending) {
            this.field = field;
            this.ascending = ascending;
        }
        
        public String getField() { return field; }
        public boolean isAscending() { return ascending; }
    }
}

// Usage
List<Employee> employees = getEmployees();
Comparator<Employee> comparator = MultiCriteriaSorter.getComparator(
    new MultiCriteriaSorter.SortField("department", true),
    new MultiCriteriaSorter.SortField("salary", false),
    new MultiCriteriaSorter.SortField("name", true)
);
employees.sort(comparator);
```

### **Dynamic Comparator Creation**
```java
public class DynamicComparator {
    
    public static <T> Comparator<T> create(
            List<Function<T, ?>> keyExtractors,
            List<Boolean> ascendingFlags) {
        
        if (keyExtractors.isEmpty()) {
            return Comparator.nullsLast(Comparator.naturalOrder());
        }
        
        Comparator<T> comparator = createSingleComparator(
            keyExtractors.get(0),
            ascendingFlags.get(0)
        );
        
        for (int i = 1; i < keyExtractors.size(); i++) {
            comparator = comparator.thenComparing(
                createSingleComparator(keyExtractors.get(i), ascendingFlags.get(i))
            );
        }
        
        return comparator;
    }
    
    private static <T, U extends Comparable<? super U>> Comparator<T> createSingleComparator(
            Function<T, U> keyExtractor, boolean ascending) {
        
        Comparator<T> comparator = Comparator.comparing(keyExtractor);
        return ascending ? comparator : comparator.reversed();
    }
}

// Usage
List<Function<Employee, ?>> extractors = Arrays.asList(
    Employee::getDepartment,
    Employee::getSalary,
    Employee::getName
);
List<Boolean> orders = Arrays.asList(true, false, true);

Comparator<Employee> dynamicComparator = DynamicComparator.create(extractors, orders);
employees.sort(dynamicComparator);
```

### **Locale-Aware Sorting**
```java
public class InternationalComparator {
    
    // Case-insensitive comparator
    public static final Comparator<String> CASE_INSENSITIVE = 
        String.CASE_INSENSITIVE_ORDER;
    
    // Locale-specific comparator
    public static Comparator<String> forLocale(Locale locale) {
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(Collator.SECONDARY); // Ignore case but consider accents
        return collator::compare;
    }
    
    // Primary strength (ignore case and accents)
    public static Comparator<String> primaryStrength(Locale locale) {
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(Collator.PRIMARY);
        return collator::compare;
    }
    
    // Swedish locale sorts å, ä, ö correctly
    public static Comparator<String> swedish() {
        return forLocale(new Locale("sv", "SE"));
    }
    
    // French locale
    public static Comparator<String> french() {
        return forLocale(Locale.FRENCH);
    }
}

// Usage
List<String> words = Arrays.asList("café", "cafe", "Český", "château");
words.sort(InternationalComparator.french());
words.sort(InternationalComparator.swedish());
words.sort(InternationalComparator.CASE_INSENSITIVE);
```

### **Custom Null Handling**
```java
public class NullSafeComparator {
    
    // Nulls first with custom comparator
    public static <T> Comparator<T> nullsFirst(Comparator<T> comparator) {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;
            return comparator.compare(a, b);
        };
    }
    
    // Nulls last with custom comparator
    public static <T> Comparator<T> nullsLast(Comparator<T> comparator) {
        return (a, b) -> {
            if (a == null && b == null) return 0;
            if (a == null) return 1;
            if (b == null) return -1;
            return comparator.compare(a, b);
        };
    }
    
    // Nulls as specific value (e.g., treat null as 0 for numbers)
    public static Comparator<Integer> nullsAsZero() {
        return (a, b) -> {
            int valA = a == null ? 0 : a;
            int valB = b == null ? 0 : b;
            return Integer.compare(valA, valB);
        };
    }
    
    // Nulls as empty string for text
    public static Comparator<String> nullsAsEmpty() {
        return (a, b) -> {
            String strA = a == null ? "" : a;
            String strB = b == null ? "" : b;
            return strA.compareTo(strB);
        };
    }
}

// Usage
List<String> namesWithNulls = Arrays.asList("Alice", null, "Bob", null, "Charlie");
namesWithNulls.sort(NullSafeComparator.nullsFirst(String::compareTo));
namesWithNulls.sort(NullSafeComparator.nullsLast(String::compareTo));
namesWithNulls.sort(NullSafeComparator.nullsAsEmpty());
```

---

## 🔄 Comparator Composition Patterns

### **Decorator Pattern for Comparators**
```java
public abstract class ComparatorDecorator<T> implements Comparator<T> {
    protected final Comparator<T> baseComparator;
    
    public ComparatorDecorator(Comparator<T> baseComparator) {
        this.baseComparator = baseComparator;
    }
}

// Logging decorator
public class LoggingComparator<T> extends ComparatorDecorator<T> {
    private final String name;
    
    public LoggingComparator(Comparator<T> baseComparator, String name) {
        super(baseComparator);
        this.name = name;
    }
    
    @Override
    public int compare(T o1, T o2) {
        int result = baseComparator.compare(o1, o2);
        System.out.printf("%s.compare(%s, %s) = %d%n", name, o1, o2, result);
        return result;
    }
}

// Reverse decorator (already exists as Comparator.reversed())
public class ReverseComparator<T> extends ComparatorDecorator<T> {
    public ReverseComparator(Comparator<T> baseComparator) {
        super(baseComparator);
    }
    
    @Override
    public int compare(T o1, T o2) {
        return baseComparator.compare(o2, o1); // Reverse order
    }
}

// Usage
Comparator<Person> base = Comparator.comparing(Person::getName);
Comparator<Person> logged = new LoggingComparator<>(base, "NameComparator");
Comparator<Person> reversed = new ReverseComparator<>(logged);

people.sort(logged);
people.sort(reversed);
```

### **Comparator Builder Pattern**
```java
public class ComparatorBuilder<T> {
    private Comparator<T> comparator = (a, b) -> 0; // Default: all equal
    
    public ComparatorBuilder<T> compareBy(Function<T, Comparable> keyExtractor) {
        comparator = comparator.thenComparing((a, b) -> {
            Comparable keyA = keyExtractor.apply(a);
            Comparable keyB = keyExtractor.apply(b);
            return keyA.compareTo(keyB);
        });
        return this;
    }
    
    public ComparatorBuilder<T> compareBy(
            Function<T, ?> keyExtractor, 
            Comparator<?> keyComparator) {
        
        comparator = comparator.thenComparing((a, b) -> {
            Object keyA = keyExtractor.apply(a);
            Object keyB = keyExtractor.apply(b);
            @SuppressWarnings("unchecked")
            Comparator<Object> comp = (Comparator<Object>) keyComparator;
            return comp.compare(keyA, keyB);
        });
        return this;
    }
    
    public ComparatorBuilder<T> reverse() {
        comparator = comparator.reversed();
        return this;
    }
    
    public ComparatorBuilder<T> nullsFirst() {
        comparator = Comparator.nullsFirst(comparator);
        return this;
    }
    
    public ComparatorBuilder<T> nullsLast() {
        comparator = Comparator.nullsLast(comparator);
        return this;
    }
    
    public Comparator<T> build() {
        return comparator;
    }
}

// Usage
Comparator<Employee> comparator = new ComparatorBuilder<Employee>()
    .compareBy(Employee::getDepartment)
    .compareBy(Employee::getSalary, Comparator.reverseOrder())
    .compareBy(Employee::getName)
    .nullsFirst()
    .build();

employees.sort(comparator);
```

### **Caching Comparators for Performance**
```java
public class ComparatorCache {
    private static final Map<String, Comparator<?>> cache = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> getComparator(
            Class<T> clazz, 
            String... fields) {
        
        String key = clazz.getName() + ":" + String.join(",", fields);
        
        return (Comparator<T>) cache.computeIfAbsent(key, k -> {
            Comparator<T> comparator = null;
            
            for (String field : fields) {
                Comparator<T> fieldComparator = createFieldComparator(clazz, field);
                
                if (comparator == null) {
                    comparator = fieldComparator;
                } else {
                    comparator = comparator.thenComparing(fieldComparator);
                }
            }
            
            return comparator != null ? comparator : (a, b) -> 0;
        });
    }
    
    private static <T> Comparator<T> createFieldComparator(
            Class<T> clazz, 
            String field) {
        
        try {
            Method getter = clazz.getMethod("get" + 
                field.substring(0, 1).toUpperCase() + field.substring(1));
            
            return (a, b) -> {
                try {
                    Comparable<?> valA = (Comparable<?>) getter.invoke(a);
                    Comparable<?> valB = (Comparable<?>) getter.invoke(b);
                    return ((Comparable<Object>) valA).compareTo(valB);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to compare", e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No getter for field: " + field, e);
        }
    }
}

// Usage
Comparator<Employee> cachedComparator = ComparatorCache.getComparator(
    Employee.class, "department", "salary", "name"
);

// Reuse the same comparator instance (no allocation overhead)
employees.sort(cachedComparator);
```

---

## 📚 Real-World Examples

### **Example 1: E-commerce Product Sorting**
```java
public class ProductSorter {
    
    public enum SortOption {
        NAME_ASC, NAME_DESC, PRICE_ASC, PRICE_DESC, 
        RATING_DESC, POPULARITY_DESC, RELEVANCE
    }
    
    public static Comparator<Product> getComparator(SortOption option) {
        return switch (option) {
            case NAME_ASC -> Comparator.comparing(Product::getName, 
                String.CASE_INSENSITIVE_ORDER);
            case NAME_DESC -> Comparator.comparing(Product::getName, 
                String.CASE_INSENSITIVE_ORDER).reversed();
            case PRICE_ASC -> Comparator.comparing(Product::getPrice);
            case PRICE_DESC -> Comparator.comparing(Product::getPrice).reversed();
            case RATING_DESC -> Comparator.comparing(Product::getAverageRating)
                                         .reversed()
                                         .thenComparing(Product::getName);
            case POPULARITY_DESC -> Comparator.comparing(Product::getSalesCount)
                                              .reversed()
                                              .thenComparing(Product::getName);
            case RELEVANCE -> createRelevanceComparator();
        };
    }
    
    private static Comparator<Product> createRelevanceComparator() {
        return (p1, p2) -> {
            // Complex relevance algorithm
            double score1 = calculateRelevanceScore(p1);
            double score2 = calculateRelevanceScore(p2);
            return Double.compare(score2, score1); // Descending
        };
    }
    
    private static double calculateRelevanceScore(Product product) {
        // Combine multiple factors: rating, sales, recency, etc.
        double ratingWeight = product.getAverageRating() * 0.4;
        double salesWeight = Math.log(product.getSalesCount() + 1) * 0.3;
        double recencyWeight = calculateRecencyScore(product.getLastUpdated()) * 0.3;
        return ratingWeight + salesWeight + recencyWeight;
    }
    
    private static double calculateRecencyScore(LocalDateTime date) {
        // More recent = higher score
        long daysAgo = ChronoUnit.DAYS.between(date, LocalDateTime.now());
        return Math.max(0, 30 - daysAgo) / 30.0;
    }
}

// Usage
List<Product> products = getProducts();
products.sort(ProductSorter.getComparator(ProductSorter.SortOption.PRICE_DESC));
```

### **Example 2: Employee Directory with Multiple Sort Options**
```java
public class EmployeeDirectory {
    private List<Employee> employees = new ArrayList<>();
    
    public void sort(SortConfig... configs) {
        Comparator<Employee> comparator = null;
        
        for (SortConfig config : configs) {
            Comparator<Employee> fieldComparator = createFieldComparator(config);
            
            if (!config.isAscending()) {
                fieldComparator = fieldComparator.reversed();
            }
            
            if (comparator == null) {
                comparator = fieldComparator;
            } else {
                comparator = comparator.thenComparing(fieldComparator);
            }
        }
        
        if (comparator != null) {
            employees.sort(comparator);
        }
    }
    
    private Comparator<Employee> createFieldComparator(SortConfig config) {
        return switch (config.getField()) {
            case "name" -> Comparator.comparing(Employee::getFullName, 
                String.CASE_INSENSITIVE_ORDER);
            case "department" -> Comparator.comparing(Employee::getDepartment);
            case "title" -> Comparator.comparing(Employee::getTitle);
            case "salary" -> Comparator.comparing(Employee::getSalary);
            case "hireDate" -> Comparator.comparing(Employee::getHireDate);
            case "yearsOfService" -> Comparator.comparing(Employee::getYearsOfService);
            default -> throw new IllegalArgumentException("Unknown field: " + config.getField());
        };
    }
    
    public static class SortConfig {
        private final String field;
        private final boolean ascending;
        
        public SortConfig(String field, boolean ascending) {
            this.field = field;
            this.ascending = ascending;
        }
        
        public String getField() { return field; }
        public boolean isAscending() { return ascending; }
    }
}

// Usage
EmployeeDirectory directory = new EmployeeDirectory();
directory.sort(
    new EmployeeDirectory.SortConfig("department", true),
    new EmployeeDirectory.SortConfig("salary", false),
    new EmployeeDirectory.SortConfig("name", true)
);
```

### **Example 3: File System Browser Comparator**
```java
public class FileComparator {
    
    public static Comparator<File> byName() {
        return Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER);
    }
    
    public static Comparator<File> bySize() {
        return Comparator.comparing(File::length);
    }
    
    public static Comparator<File> byModifiedDate() {
        return Comparator.comparing(File::lastModified).reversed(); // Newest first
    }
    
    public static Comparator<File> byType() {
        return (f1, f2) -> {
            // Directories first, then by extension
            if (f1.isDirectory() && !f2.isDirectory()) return -1;
            if (!f1.isDirectory() && f2.isDirectory()) return 1;
            
            String ext1 = getExtension(f1);
            String ext2 = getExtension(f2);
            
            if (ext1 == null && ext2 == null) return 0;
            if (ext1 == null) return -1;
            if (ext2 == null) return 1;
            
            return ext1.compareToIgnoreCase(ext2);
        };
    }
    
    public static Comparator<File> multiSort(boolean dirsFirst, String... fields) {
        Comparator<File> comparator = null;
        
        // Directories first if requested
        if (dirsFirst) {
            comparator = (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) return -1;
                if (!f1.isDirectory() && f2.isDirectory()) return 1;
                return 0;
            };
        }
        
        // Add requested fields
        for (String field : fields) {
            Comparator<File> fieldComparator = switch (field.toLowerCase()) {
                case "name" -> byName();
                case "size" -> bySize();
                case "date" -> byModifiedDate();
                case "type" -> byType();
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            };
            
            if (comparator == null) {
                comparator = fieldComparator;
            } else {
                comparator = comparator.thenComparing(fieldComparator);
            }
        }
        
        return comparator != null ? comparator : byName();
    }
    
    private static String getExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return dotIndex > 0 ? name.substring(dotIndex + 1).toLowerCase() : null;
    }
}

// Usage
File[] files = directory.listFiles();
Arrays.sort(files, FileComparator.multiSort(true, "type", "name"));
Arrays.sort(files, FileComparator.byModifiedDate());
Arrays.sort(files, FileComparator.bySize().reversed());
```

### **Example 4: Game Leaderboard Comparator**
```java
public class LeaderboardComparator {
    
    public static Comparator<PlayerScore> byScore() {
        return Comparator.comparing(PlayerScore::getScore).reversed();
    }
    
    public static Comparator<PlayerScore> byScoreThenTime() {
        return Comparator
            .comparing(PlayerScore::getScore).reversed()
            .thenComparing(PlayerScore::getCompletionTime);
    }
    
    public static Comparator<PlayerScore> byLevelThenScore() {
        return Comparator
            .comparing(PlayerScore::getLevel).reversed()
            .thenComparing(PlayerScore::getScore).reversed();
    }
    
    public static Comparator<PlayerScore> byCustomWeighting() {
        return (p1, p2) -> {
            // Custom weighting: 70% score, 20% accuracy, 10% time
            double weight1 = p1.getScore() * 0.7 + 
                           p1.getAccuracy() * 20 + 
                           (1.0 / (p1.getCompletionTime() + 1)) * 10;
            double weight2 = p2.getScore() * 0.7 + 
                           p2.getAccuracy() * 20 + 
                           (1.0 / (p2.getCompletionTime() + 1)) * 10;
            return Double.compare(weight2, weight1); // Descending
        };
    }
    
    public static Comparator<PlayerScore> withTieBreaker(
            Comparator<PlayerScore> primary,
            Comparator<PlayerScore>... tieBreakers) {
        
        Comparator<PlayerScore> comparator = primary;
        
        for (Comparator<PlayerScore> tieBreaker : tieBreakers) {
            comparator = comparator.thenComparing(tieBreaker);
        }
        
        return comparator;
    }
    
    public static class PlayerScore {
        private final String playerName;
        private final int score;
        private final long completionTime; // milliseconds
        private final double accuracy; // percentage
        private final int level;
        
        // Constructor, getters
        
        public static Comparator<PlayerScore> defaultComparator() {
            return withTieBreaker(
                byScore(),
                Comparator.comparing(PlayerScore::getCompletionTime),
                Comparator.comparing(PlayerScore::getPlayerName)
            );
        }
    }
}

// Usage
List<LeaderboardComparator.PlayerScore> leaderboard = getScores();
leaderboard.sort(LeaderboardComparator.byScoreThenTime());
leaderboard.sort(LeaderboardComparator.byCustomWeighting());
leaderboard.sort(LeaderboardComparator.PlayerScore.defaultComparator());
```

### **Example 5: Scientific Data Comparator**
```java
public class ScientificDataComparator {
    
    // Compare with tolerance for floating point errors
    public static Comparator<Double> withTolerance(double tolerance) {
        return (d1, d2) -> {
            double diff = d1 - d2;
            if (Math.abs(diff) <= tolerance) {
                return 0; // Consider equal within tolerance
            }
            return Double.compare(d1, d2);
        };
    }
    
    // Comparator for scientific measurements with uncertainty
    public static Comparator<Measurement> byValueWithUncertainty() {
        return (m1, m2) -> {
            // Compare central values, accounting for uncertainty overlap
            double value1 = m1.getValue();
            double value2 = m2.getValue();
            double uncertainty1 = m1.getUncertainty();
            double uncertainty2 = m2.getUncertainty();
            
            // If values overlap within uncertainties, consider equal
            if (Math.abs(value1 - value2) <= (uncertainty1 + uncertainty2)) {
                return 0;
            }
            
            return Double.compare(value1, value2);
        };
    }
    
    // Comparator for dates with different precision
    public static Comparator<ScientificDate> byDatePrecision() {
        return (d1, d2) -> {
            // Compare years first
            int yearCompare = Integer.compare(d1.getYear(), d2.getYear());
            if (yearCompare != 0) return yearCompare;
            
            // If years equal and months available, compare months
            if (d1.hasMonth() && d2.hasMonth()) {
                int monthCompare = Integer.compare(d1.getMonth(), d2.getMonth());
                if (monthCompare != 0) return monthCompare;
                
                // If months equal and days available, compare days
                if (d1.hasDay() && d2.hasDay()) {
                    return Integer.compare(d1.getDay(), d2.getDay());
                }
                
                // One has day, other doesn't - the one with day is more precise
                if (d1.hasDay() && !d2.hasDay()) return -1;
                if (!d1.hasDay() && d2.hasDay()) return 1;
            }
            
            // Both have same precision level
            return 0;
        };
    }
    
    public static class Measurement {
        private final double value;
        private final double uncertainty;
        // Getters
    }
    
    public static class ScientificDate {
        private final int year;
        private final Integer month; // null if not specified
        private final Integer day;   // null if not specified
        // Getters and hasMonth(), hasDay() methods
    }
}

// Usage
List<Double> measurements = getMeasurements();
measurements.sort(ScientificDataComparator.withTolerance(0.001));

List<ScientificDataComparator.Measurement> data = getData();
data.sort(ScientificDataComparator.byValueWithUncertainty());
```

---

## ⚡ Performance Optimization

### **Caching Comparators**
```java
public class ComparatorCache {
    private static final Map<String, Comparator<?>> CACHE = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> getOrCreate(
            String key, 
            Supplier<Comparator<T>> creator) {
        return (Comparator<T>) CACHE.computeIfAbsent(key, k -> creator.get());
    }
    
    // Pre-built common comparators
    public static final Comparator<String> CASE_INSENSITIVE = 
        String.CASE_INSENSITIVE_ORDER;
    
    public static final Comparator<String> NATURAL = 
        Comparator.naturalOrder();
    
    public static final Comparator<String> REVERSE = 
        Comparator.reverseOrder();
}

// Usage - reuse comparator instance
Comparator<Employee> comparator = ComparatorCache.getOrCreate(
    "Employee.byDeptAndSalary",
    () -> Comparator
        .comparing(Employee::getDepartment)
        .thenComparing(Employee::getSalary, Comparator.reverseOrder())
);

// Reuse across multiple sorts
employees1.sort(comparator);
employees2.sort(comparator);
employees3.sort(comparator);
```

### **Primitive Specialization**
```java
// Avoid boxing overhead with primitive comparators
public class PrimitiveComparators {
    
    // For lists of primitive wrappers
    public static Comparator<Integer> comparingInt() {
        return (a, b) -> {
            int x = a != null ? a : 0;
            int y = b != null ? b : 0;
            return Integer.compare(x, y);
        };
    }
    
    public static Comparator<Double> comparingDouble() {
        return (a, b) -> {
            double x = a != null ? a : 0.0;
            double y = b != null ? b : 0.0;
            return Double.compare(x, y);
        };
    }
    
    // Direct array sorting without boxing
    public static void sortIntArray(int[] array) {
        // Arrays.sort uses Dual-Pivot Quicksort for primitives
        Arrays.sort(array);
    }
    
    public static void sortObjectsWithPrimitiveField(List<Person> people) {
        // Use comparingInt to avoid Integer boxing
        people.sort(Comparator.comparingInt(Person::getAge));
    }
}

// Benchmark: comparingInt vs comparing
List<Person> largeList = createLargeList(1_000_000);

// Slower: creates Integer objects
long start1 = System.currentTimeMillis();
largeList.sort(Comparator.comparing(Person::getAge));
long end1 = System.currentTimeMillis();

// Faster: no boxing
long start2 = System.currentTimeMillis();
largeList.sort(Comparator.comparingInt(Person::getAge));
long end2 = System.currentTimeMillis();

System.out.println("Comparing: " + (end1 - start1) + "ms");
System.out.println("ComparingInt: " + (end2 - start2) + "ms");
```

### **Memoization for Expensive Comparisons**
```java
public class MemoizingComparator<T> implements Comparator<T> {
    private final Comparator<T> delegate;
    private final Map<T, Map<T, Integer>> cache = new HashMap<>();
    
    public MemoizingComparator(Comparator<T> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public int compare(T o1, T o2) {
        // Check cache
        Map<T, Integer> innerCache = cache.get(o1);
        if (innerCache != null) {
            Integer cached = innerCache.get(o2);
            if (cached != null) {
                return cached;
            }
        }
        
        // Compute comparison
        int result = delegate.compare(o1, o2);
        
        // Cache result
        cache.computeIfAbsent(o1, k -> new HashMap<>())
             .put(o2, result);
        
        // Also cache reverse comparison
        cache.computeIfAbsent(o2, k -> new HashMap<>())
             .put(o1, -result);
        
        return result;
    }
    
    public void clearCache() {
        cache.clear();
    }
}

// Usage for expensive comparisons (e.g., string distance)
Comparator<String> expensiveComparator = (s1, s2) -> {
    // Expensive operation like Levenshtein distance
    return calculateStringDistance(s1, s2);
};

Comparator<String> memoized = new MemoizingComparator<>(expensiveComparator);

// First comparison computes and caches
int result1 = memoized.compare("hello", "world");

// Subsequent comparisons use cache
int result2 = memoized.compare("hello", "world"); // From cache
int result3 = memoized.compare("world", "hello"); // From cache (-result1)
```

---

## 🔍 Testing & Debugging

### **Unit Testing Comparators**
```java
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ComparatorTest {
    
    @Test
    void testComparatorContract() {
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        
        // Reflexive
        assertEquals(0, comparator.compare("test", "test"));
        
        // Symmetric
        String a = "Apple";
        String b = "Banana";
        int ab = comparator.compare(a, b);
        int ba = comparator.compare(b, a);
        assertTrue(ab == -ba);
        
        // Transitive
        String c = "Cherry";
        int abResult = comparator.compare(a, b);
        int bcResult = comparator.compare(b, c);
        int acResult = comparator.compare(a, c);
        
        if (abResult < 0 && bcResult < 0) {
            assertTrue(acResult < 0);
        }
    }
    
    @Test
    void testComparatorSorting() {
        List<Person> people = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
        );
        
        List<Person> expectedByAge = Arrays.asList(
            new Person("Bob", 25),
            new Person("Alice", 30),
            new Person("Charlie", 35)
        );
        
        people.sort(Comparator.comparing(Person::getAge));
        assertEquals(expectedByAge, people);
    }
    
    @Test
    void testNullHandling() {
        List<String> list = Arrays.asList("B", null, "A", null, "C");
        List<String> expectedNullsFirst = Arrays.asList(null, null, "A", "B", "C");
        List<String> expectedNullsLast = Arrays.asList("A", "B", "C", null, null);
        
        list.sort(Comparator.nullsFirst(String::compareTo));
        assertEquals(expectedNullsFirst, list);
        
        list.sort(Comparator.nullsLast(String::compareTo));
        assertEquals(expectedNullsLast, list);
    }
    
    @Test
    void testChainedComparator() {
        List<Employee> employees = Arrays.asList(
            new Employee("IT", "Alice", 50000),
            new Employee("HR", "Bob", 60000),
            new Employee("IT", "Charlie", 45000),
            new Employee("HR", "Alice", 55000)
        );
        
        List<Employee> expected = Arrays.asList(
            new Employee("HR", "Alice", 55000),
            new Employee("HR", "Bob", 60000),
            new Employee("IT", "Charlie", 45000),
            new Employee("IT", "Alice", 50000)
        );
        
        Comparator<Employee> comparator = Comparator
            .comparing(Employee::getDepartment)
            .thenComparing(Employee::getName);
        
        employees.sort(comparator);
        assertEquals(expected, employees);
    }
    
    @Test
    void testCustomComparatorLogic() {
        Comparator<String> lengthThenAlpha = (s1, s2) -> {
            int lengthCompare = Integer.compare(s1.length(), s2.length());
            return lengthCompare != 0 ? lengthCompare : s1.compareTo(s2);
        };
        
        List<String> words = Arrays.asList("apple", "kiwi", "banana", "fig");
        List<String> expected = Arrays.asList("fig", "kiwi", "apple", "banana");
        
        words.sort(lengthThenAlpha);
        assertEquals(expected, words);
    }
}
```

### **Debugging Comparator Issues**
```java
public class ComparatorDebugger {
    
    public static <T> Comparator<T> debug(Comparator<T> comparator, String name) {
        return (a, b) -> {
            int result = comparator.compare(a, b);
            System.out.printf("[%s] compare(%s, %s) = %d%n", 
                name, a, b, result);
            return result;
        };
    }
    
    public static <T> void validateComparator(Comparator<T> comparator, List<T> items) {
        System.out.println("Validating comparator...");
        
        // Check reflexive
        for (T item : items) {
            if (comparator.compare(item, item) != 0) {
                System.err.println("FAIL: Not reflexive for " + item);
            }
        }
        
        // Check symmetric
        for (int i = 0; i < items.size(); i++) {
            for (int j = i + 1; j < items.size(); j++) {
                T a = items.get(i);
                T b = items.get(j);
                int ab = comparator.compare(a, b);
                int ba = comparator.compare(b, a);
                
                if (ab != -ba) {
                    System.err.printf("FAIL: Not symmetric for %s and %s (ab=%d, ba=%d)%n", 
                        a, b, ab, ba);
                }
            }
        }
        
        // Check transitive
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                for (int k = 0; k < items.size(); k++) {
                    T a = items.get(i);
                    T b = items.get(j);
                    T c = items.get(k);
                    
                    int ab = comparator.compare(a, b);
                    int bc = comparator.compare(b, c);
                    int ac = comparator.compare(a, c);
                    
                    if (ab < 0 && bc < 0 && ac >= 0) {
                        System.err.printf("FAIL: Not transitive for %s, %s, %s%n", 
                            a, b, c);
                    }
                }
            }
        }
        
        System.out.println("Validation complete.");
    }
    
    public static <T> void traceSort(List<T> list, Comparator<T> comparator) {
        System.out.println("Initial list: " + list);
        
        List<T> copy = new ArrayList<>(list);
        copy.sort(debug(comparator, "Trace"));
        
        System.out.println("Sorted list: " + copy);
    }
}

// Usage
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
Comparator<String> comparator = ComparatorDebugger.debug(
    String::compareTo, "NameComparator"
);
ComparatorDebugger.validateComparator(comparator, names);
ComparatorDebugger.traceSort(names, comparator);
```

---

## 🎯 Quick Reference Guide

### **Comparator Creation Cheatsheet**
```
// 1. Lambda expressions
Comparator<Person> byAge = (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge());

// 2. Method references
Comparator<Person> byName = Comparator.comparing(Person::getName);

// 3. Factory methods
Comparator<String> natural = Comparator.naturalOrder();
Comparator<String> reverse = Comparator.reverseOrder();
Comparator<String> nullsFirst = Comparator.nullsFirst(String::compareTo);
Comparator<String> nullsLast = Comparator.nullsLast(String::compareTo);

// 4. Primitive specializations
Comparator<Integer> comparingInt = Comparator.comparingInt(Integer::intValue);
Comparator<Long> comparingLong = Comparator.comparingLong(Long::longValue);
Comparator<Double> comparingDouble = Comparator.comparingDouble(Double::doubleValue);

// 5. Chaining
Comparator<Person> complex = Comparator
    .comparing(Person::getLastName)
    .thenComparing(Person::getFirstName)
    .thenComparingInt(Person::getAge);
```

### **Common Comparator Patterns**
```
// Multi-field sorting
comparator = Comparator
    .comparing(Employee::getDepartment)
    .thenComparing(Employee::getSalary, Comparator.reverseOrder())
    .thenComparing(Employee::getName);

// Case-insensitive string comparison
comparator = Comparator.comparing(
    Person::getName, 
    String.CASE_INSENSITIVE_ORDER
);

// Custom comparison logic
comparator = (a, b) -> customComparison(a, b);

// Reverse existing comparator
comparator = existingComparator.reversed();

// Null handling
comparator = Comparator.nullsFirst(existingComparator);
comparator = Comparator.nullsLast(existingComparator);
```

### **Performance Optimization Guide**
```
Small collections (< 1,000 items):
  → Any comparator approach works
  → Focus on readability

Medium collections (1,000 - 100,000 items):
  → Use Comparator.comparing() for clarity
  → Consider caching comparator instances
  → Use primitive specializations (comparingInt, etc.)

Large collections (> 100,000 items):
  → Use primitive specializations to avoid boxing
  → Cache comparator instances
  → Consider memoization for expensive comparisons
  → Profile with actual data

Frequent sorting operations:
  → Reuse comparator instances
  → Consider TreeSet/TreeMap for automatic sorting
  → Cache sorted results when possible
```

### **Best Practices Checklist**
```java
// ✅ DO:
// 1. Use Comparator.comparing() for readability
people.sort(Comparator.comparing(Person::getName));

// 2. Chain comparators for multi-field sorting
employees.sort(Comparator
    .comparing(Employee::getDepartment)
    .thenComparing(Employee::getSalary));

// 3. Handle nulls explicitly
list.sort(Comparator.nullsLast(Comparator.naturalOrder()));

// 4. Use primitive specializations
list.sort(Comparator.comparingInt(Person::getAge));

// 5. Make comparators stateless and thread-safe

// ❌ DON'T:
// 1. Don't modify compared objects during sorting
// 2. Don't create new comparator instances in hot loops
// 3. Don't ignore transitive property
// 4. Don't forget to handle nulls
// 5. Don't use comparators with side effects
```

---

## 📖 Summary

**Java Comparator** is a powerful tool for defining custom ordering:
1. **External ordering** separate from object implementation
2. **Multiple sorting strategies** per class
3. **Flexible composition** through chaining
4. **Modern factory methods** (Java 8+) for concise syntax

**Key Takeaways:**
1. **Prefer `Comparator.comparing()`** over manual implementations for readability
2. **Chain comparators** with `thenComparing()` for multi-field sorting
3. **Handle nulls explicitly** with `nullsFirst()` or `nullsLast()`
4. **Use primitive specializations** (`comparingInt`, etc.) for performance
5. **Make comparators stateless** and thread-safe
6. **Test comparator properties** (reflexive, symmetric, transitive)
7. **Consider caching** comparator instances for repeated use

**Remember**: A well-designed comparator makes your code more readable, maintainable, and performant. Choose the right pattern for your use case, from simple lambda expressions to complex chained comparators with custom logic.

This comprehensive guide covers everything from basic comparator usage to advanced patterns. Bookmark this cheatsheet for quick reference during your Java development work!