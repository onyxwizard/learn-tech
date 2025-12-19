# Chapter 6: 🤝 **Generic Methods + Bounded Types**

## Combining Power: Flexible Algorithms with Type Safety

When generic methods meet bounded type parameters, you unlock the ability to write highly reusable algorithms that maintain type safety across complex type hierarchies. This combination is the foundation of many utility classes in the Java Collections Framework and modern libraries.

## 🧩 **The Power Combination**

### Basic Pattern: Generic Methods with Bounds

```java
public class AlgorithmUtils {
    
    // Generic method with single bound
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
    
    // Generic method with multiple bounds
    public static <T extends Number & Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }
    
    // Generic method with wildcard in bounds (more flexible)
    public static <T extends Comparable<? super T>> T findMax(List<? extends T> list) {
        if (list.isEmpty()) return null;
        
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
}
```

## 📊 **Real-World Examples**

### 1. **Sorting Algorithms with Bounds**

```java
public class SortingAlgorithms {
    
    // Generic bubble sort with Comparable bound
    public static <T extends Comparable<T>> void bubbleSort(T[] array) {
        if (array == null || array.length <= 1) return;
        
        boolean swapped;
        for (int i = 0; i < array.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j].compareTo(array[j + 1]) > 0) {
                    // Swap elements
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }
    
    // Generic quicksort with Comparator (more flexible)
    public static <T> void quickSort(T[] array, Comparator<? super T> comparator) {
        quickSort(array, 0, array.length - 1, comparator);
    }
    
    private static <T> void quickSort(T[] array, int low, int high, 
                                     Comparator<? super T> comparator) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, comparator);
            quickSort(array, low, pivotIndex - 1, comparator);
            quickSort(array, pivotIndex + 1, high, comparator);
        }
    }
    
    private static <T> int partition(T[] array, int low, int high, 
                                    Comparator<? super T> comparator) {
        T pivot = array[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }
    
    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    // Generic binary search with bounds
    public static <T extends Comparable<? super T>> int binarySearch(T[] array, T key) {
        return binarySearch(array, key, 0, array.length - 1);
    }
    
    private static <T extends Comparable<? super T>> int binarySearch(
            T[] array, T key, int low, int high) {
        if (low > high) return -1;
        
        int mid = low + (high - low) / 2;
        int cmp = key.compareTo(array[mid]);
        
        if (cmp == 0) return mid;
        if (cmp < 0) return binarySearch(array, key, low, mid - 1);
        return binarySearch(array, key, mid + 1, high);
    }
}
```

### 2. **Mathematical Operations Library**

```java
public class MathUtils {
    
    // Generic sum that works with any Number type
    public static <T extends Number> double sum(Collection<T> numbers) {
        double total = 0.0;
        for (T number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }
    
    // Generic average with bounds
    public static <T extends Number> double average(Collection<T> numbers) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("Collection cannot be empty");
        }
        return sum(numbers) / numbers.size();
    }
    
    // Generic method that returns the same type (tricky!)
    public static <T extends Number> T add(T a, T b) {
        // We need to handle each Number type individually
        if (a instanceof Integer) {
            @SuppressWarnings("unchecked")
            T result = (T) Integer.valueOf(a.intValue() + b.intValue());
            return result;
        } else if (a instanceof Double) {
            @SuppressWarnings("unchecked")
            T result = (T) Double.valueOf(a.doubleValue() + b.doubleValue());
            return result;
        } else if (a instanceof Float) {
            @SuppressWarnings("unchecked")
            T result = (T) Float.valueOf(a.floatValue() + b.floatValue());
            return result;
        } else if (a instanceof Long) {
            @SuppressWarnings("unchecked")
            T result = (T) Long.valueOf(a.longValue() + b.longValue());
            return result;
        } else {
            throw new IllegalArgumentException("Unsupported number type: " + a.getClass());
        }
    }
    
    // Generic variance calculation
    public static <T extends Number> double variance(List<T> numbers) {
        if (numbers.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 numbers");
        }
        
        double mean = average(numbers);
        double sumSquaredDifferences = 0.0;
        
        for (T number : numbers) {
            double diff = number.doubleValue() - mean;
            sumSquaredDifferences += diff * diff;
        }
        
        return sumSquaredDifferences / (numbers.size() - 1);
    }
}
```

### 3. **Data Structure Utilities**

```java
public class CollectionUtils {
    
    // Merge two sorted collections (generic method with bounds)
    public static <T extends Comparable<? super T>> List<T> mergeSorted(
            Collection<? extends T> first, 
            Collection<? extends T> second) {
        
        List<T> result = new ArrayList<>();
        Iterator<? extends T> firstIter = first.iterator();
        Iterator<? extends T> secondIter = second.iterator();
        
        T firstItem = firstIter.hasNext() ? firstIter.next() : null;
        T secondItem = secondIter.hasNext() ? secondIter.next() : null;
        
        while (firstItem != null || secondItem != null) {
            if (firstItem == null) {
                result.add(secondItem);
                secondItem = secondIter.hasNext() ? secondIter.next() : null;
            } else if (secondItem == null) {
                result.add(firstItem);
                firstItem = firstIter.hasNext() ? firstIter.next() : null;
            } else if (firstItem.compareTo(secondItem) <= 0) {
                result.add(firstItem);
                firstItem = firstIter.hasNext() ? firstIter.next() : null;
            } else {
                result.add(secondItem);
                secondItem = secondIter.hasNext() ? secondIter.next() : null;
            }
        }
        
        return result;
    }
    
    // Find common elements in two collections
    public static <T> List<T> intersection(Collection<? extends T> first, 
                                          Collection<? extends T> second) {
        List<T> result = new ArrayList<>();
        Set<T> secondSet = new HashSet<>((Collection<T>) second);
        
        for (T item : first) {
            if (secondSet.contains(item)) {
                result.add(item);
            }
        }
        
        return result;
    }
    
    // Generic method to partition collection based on predicate
    public static <T> Map<Boolean, List<T>> partition(
            Collection<T> collection, 
            Predicate<? super T> predicate) {
        
        Map<Boolean, List<T>> result = new HashMap<>();
        result.put(true, new ArrayList<>());
        result.put(false, new ArrayList<>());
        
        for (T item : collection) {
            result.get(predicate.test(item)).add(item);
        }
        
        return result;
    }
    
    // Generic method to group by classifier
    public static <T, K> Map<K, List<T>> groupBy(
            Collection<T> collection, 
            Function<? super T, ? extends K> classifier) {
        
        Map<K, List<T>> result = new HashMap<>();
        for (T item : collection) {
            K key = classifier.apply(item);
            result.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        
        return result;
    }
}
```

## ⚠️ **Restrictions and Solutions**

### 1. **Cannot Instantiate Type Parameters**

```java
public class FactoryUtils {
    
    // ❌ Can't do this
    // public static <T> T createInstance() {
    //     return new T();  // Compile error
    // }
    
    // ✅ Solution 1: Pass Class<T> as parameter
    public static <T> T createInstance(Class<T> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }
    
    // ✅ Solution 2: Use Supplier<T>
    public static <T> T createInstance(Supplier<T> supplier) {
        return supplier.get();
    }
    
    // ✅ Solution 3: Factory interface
    public interface Factory<T> {
        T create();
    }
    
    public static <T> T createInstance(Factory<T> factory) {
        return factory.create();
    }
}
```

### 2. **Array Creation Limitations**

```java
public class ArrayUtils {
    
    // ❌ Can't create generic array
    // public static <T> T[] createArray(int size) {
    //     return new T[size];  // Compile error
    // }
    
    // ✅ Solution: Use Array.newInstance with Class<T>
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> clazz, int size) {
        return (T[]) Array.newInstance(clazz, size);
    }
    
    // Generic method to convert collection to array
    public static <T> T[] toArray(Collection<T> collection, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, collection.size());
        return collection.toArray(array);
    }
    
    // More complex: Create array of generic type with bounds
    public static <T extends Number> Number[] createNumberArray(Class<T> clazz, int size) {
        return (Number[]) Array.newInstance(clazz, size);
    }
}
```

### 3. **Static Context Limitations**

```java
public class ContextLimitations<T> {  // Class-level type parameter
    
    private T instanceField;
    
    // ❌ Static method can't use class type parameter
    // public static void staticMethod(T param) { }
    
    // ✅ But can have its own type parameter
    public static <U> U staticGenericMethod(U param) {
        return param;
    }
    
    // Instance method using both class and method type parameters
    public <U> Map<T, U> createMap(U value) {
        Map<T, U> map = new HashMap<>();
        map.put(instanceField, value);
        return map;
    }
}
```

## 🏗️ **Advanced Patterns**

### 1. **Generic Builder Pattern with Bounds**

```java
public class QueryBuilder {
    
    public static <T> Select<T> select(Class<T> entityClass) {
        return new Select<>(entityClass);
    }
    
    public static class Select<T> {
        private final Class<T> entityClass;
        private final List<String> fields = new ArrayList<>();
        
        private Select(Class<T> entityClass) {
            this.entityClass = entityClass;
        }
        
        public Select<T> fields(String... fieldNames) {
            this.fields.addAll(Arrays.asList(fieldNames));
            return this;
        }
        
        public <R extends T> Where<R> from(Class<R> specificClass) {
            return new Where<>(specificClass, fields);
        }
    }
    
    public static class Where<T> {
        private final Class<T> entityClass;
        private final List<String> fields;
        private final List<Predicate<T>> predicates = new ArrayList<>();
        
        private Where(Class<T> entityClass, List<String> fields) {
            this.entityClass = entityClass;
            this.fields = fields;
        }
        
        public Where<T> where(Predicate<T> predicate) {
            this.predicates.add(predicate);
            return this;
        }
        
        public <U extends T & Serializable> Where<U> andSerialize(Class<U> serializableClass) {
            // Additional constraints for serializable types
            return new Where<>(serializableClass, fields);
        }
        
        public List<T> execute() {
            // Execute query with type safety
            return new ArrayList<>();
        }
    }
}
```

### 2. **Type-Safe Event System**

```java
public abstract class Event {
    private final long timestamp = System.currentTimeMillis();
    
    public long getTimestamp() {
        return timestamp;
    }
}

public class EventDispatcher {
    private final Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();
    
    // Register handler for specific event type
    public <E extends Event> void registerHandler(
            Class<E> eventType, 
            EventHandler<E> handler) {
        
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(handler);
    }
    
    // Dispatch event with type safety
    @SuppressWarnings("unchecked")
    public <E extends Event> void dispatch(E event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : eventHandlers) {
                ((EventHandler<E>) handler).handle(event);
            }
        }
    }
    
    // Generic method to find handlers of specific type
    public <E extends Event> List<EventHandler<E>> getHandlers(Class<E> eventType) {
        @SuppressWarnings("unchecked")
        List<EventHandler<E>> result = (List<EventHandler<E>>) (List<?>) 
            handlers.getOrDefault(eventType, new ArrayList<>());
        return result;
    }
}

@FunctionalInterface
public interface EventHandler<E extends Event> {
    void handle(E event);
}
```

### 3. **Generic Strategy Pattern**

```java
public interface ValidationStrategy<T> {
    ValidationResult validate(T object);
}

public class ValidationResult {
    private final boolean valid;
    private final String message;
    
    private ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
    
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }
    
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
    
    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
}

public class Validator<T> {
    private final List<ValidationStrategy<? super T>> strategies = new ArrayList<>();
    
    public Validator<T> addStrategy(ValidationStrategy<? super T> strategy) {
        strategies.add(strategy);
        return this;
    }
    
    public List<ValidationResult> validate(T object) {
        return strategies.stream()
            .map(strategy -> strategy.validate(object))
            .collect(Collectors.toList());
    }
    
    public boolean isValid(T object) {
        return validate(object).stream()
            .allMatch(ValidationResult::isValid);
    }
    
    // Generic method to create validator for specific type
    public static <U> Validator<U> forType(Class<U> clazz) {
        return new Validator<>();
    }
}

// Usage with bounds
class UserValidator implements ValidationStrategy<User> {
    @Override
    public ValidationResult validate(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return ValidationResult.failure("Name is required");
        }
        if (user.getAge() < 0 || user.getAge() > 150) {
            return ValidationResult.failure("Age must be between 0 and 150");
        }
        return ValidationResult.success();
    }
}

Validator<User> userValidator = Validator.forType(User.class)
    .addStrategy(new UserValidator());
```

## 📐 **Best Practices for Combining Generic Methods and Bounds**

### 1. **Use `? super T` for Maximum Flexibility**

```java
public class FlexibilityExamples {
    
    // Less flexible: only works with exact T type
    public static <T extends Comparable<T>> T maxExact(List<T> list) {
        // Implementation
        return null;
    }
    
    // More flexible: works with T or its supertypes
    public static <T extends Comparable<? super T>> T maxFlexible(List<? extends T> list) {
        // Can handle List<Dog> when Dog extends Animal implements Comparable<Animal>
        return null;
    }
}
```

### 2. **Balance Between Generality and Usability**

```java
public class BalanceExamples {
    
    // Too specific: only works with exact Comparable types
    public static <T extends Comparable<T>> void sortSpecific(T[] array) {
        Arrays.sort(array);
    }
    
    // Good balance: works with Comparable types and their subtypes
    public static <T extends Comparable<? super T>> void sortBalanced(T[] array) {
        Arrays.sort(array);
    }
    
    // Most flexible: accepts Comparator for any type
    public static <T> void sortFlexible(T[] array, Comparator<? super T> comparator) {
        Arrays.sort(array, comparator);
    }
}
```

### 3. **Document Complex Bounds**

```java
/**
 * Finds the maximum element in a collection.
 * 
 * @param <T> the type of elements in the collection, must be comparable
 *           to itself or its supertypes
 * @param collection the collection to search
 * @return the maximum element, or null if collection is empty
 * @throws NullPointerException if collection is null
 */
public static <T extends Comparable<? super T>> T findMax(
        Collection<? extends T> collection) {
    // Implementation
    return null;
}
```

## 🧪 **Testing Generic Methods with Bounds**

```java
public class GenericMethodTest {
    
    @Test
    public void testMaxWithIntegers() {
        Integer result = AlgorithmUtils.max(5, 10);
        assertEquals(10, result.intValue());
    }
    
    @Test
    public void testMaxWithStrings() {
        String result = AlgorithmUtils.max("apple", "banana");
        assertEquals("banana", result);
    }
    
    @Test
    public void testClampWithDoubles() {
        Double result = AlgorithmUtils.clamp(15.5, 10.0, 20.0);
        assertEquals(15.5, result, 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAverageWithEmptyCollection() {
        MathUtils.average(new ArrayList<Integer>());
    }
    
    @Test
    public void testGenericSorting() {
        Integer[] numbers = {5, 2, 8, 1, 9};
        SortingAlgorithms.bubbleSort(numbers);
        assertArrayEquals(new Integer[]{1, 2, 5, 8, 9}, numbers);
    }
}
```

## 🎓 **Key Takeaways**

1. **Generic methods + bounds = powerful, reusable algorithms** that maintain type safety
2. **Use `Comparable<? super T>`** for maximum flexibility when comparing
3. **Pass `Class<T>` or `Supplier<T>`** when you need to create instances
4. **Document complex bounds thoroughly** - they can be hard to understand
5. **Test with various type arguments** to ensure your methods work correctly
6. **Balance flexibility with clarity** - don't overcomplicate bounds unnecessarily

## 🚀 **Next Steps**

Now that you've mastered combining generic methods with bounded types, it's time to understand how generics interact with inheritance. In [Chapter 7](#7-generics-inheritance-and-subtypes), we'll explore **Generics, Inheritance, and Subtypes** - answering critical questions like "Is `List<String>` a subtype of `List<Object>`?" and understanding covariance, contravariance, and invariance in Java generics.

> 💡 **Practice Exercise**: Create a `Graph<T>` class with generic methods for depth-first and breadth-first traversal. Then create a `WeightedGraph<T extends Comparable<T>>` that can find the shortest path using Dijkstra's algorithm. Finally, create a `SocialNetwork<User extends Comparable<User>>` that uses these graph algorithms to find connections.