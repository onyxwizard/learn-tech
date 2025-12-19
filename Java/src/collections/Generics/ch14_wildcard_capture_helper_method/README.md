# Chapter 14: 🎣 **Wildcard Capture & Helper Methods**

## Taming the Unknown: Techniques for Working with Wildcards

Wildcard capture is a powerful technique that allows you to "capture" the unknown type of a wildcard in a generic method, enabling operations that would otherwise be impossible due to type safety constraints. This chapter explores how to work around wildcard limitations using helper methods and type capture patterns.

## 🪤 **What is Wildcard Capture?**

### The Problem: Working with Unknown Types

```java
import java.util.*;

public class WildcardCaptureProblem {
    
    // ❌ This doesn't compile - wildcard capture error
    public static void swap(List<?> list, int i, int j) {
        // Compiler error: Cannot resolve symbol '?'
        // ? temp = list.get(i);  
        // list.set(i, list.get(j));
        // list.set(j, temp);
    }
    
    // The issue: The wildcard ? represents an unknown type.
    // We can't declare a variable of type ?.
    // We can't call methods that depend on knowing the exact type.
    
    public static void demonstrateProblem() {
        List<String> strings = Arrays.asList("A", "B", "C");
        
        // We want to swap elements, but can't directly
        // swap(strings, 0, 2);  // Wouldn't compile
        
        // The compiler's perspective:
        // 1. List<?> means "list of some specific but unknown type"
        // 2. We can read from it as Object
        // 3. We can't write to it (except null) because we don't know the type
        // 4. We can't refer to the unknown type in code
    }
}
```

### The Solution: Capturing the Wildcard

```java
public class WildcardCaptureSolution {
    
    // ✅ Solution: Use a helper method with a type parameter
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }
    
    // Helper method captures the wildcard
    private static <T> void swapHelper(List<T> list, int i, int j) {
        // Now T is the captured type!
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // How it works:
    // 1. swap() takes List<?> - unknown type
    // 2. swapHelper() is generic with type parameter T
    // 3. When swapHelper(list, i, j) is called, the compiler INFERS T
    // 4. T becomes the CAPTURED type of the wildcard
    // 5. Now we can use T throughout the method
    
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>(Arrays.asList("A", "B", "C"));
        System.out.println("Before swap: " + strings);  // [A, B, C]
        
        swap(strings, 0, 2);
        System.out.println("After swap: " + strings);   // [C, B, A]
        
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3));
        System.out.println("Before swap: " + numbers);  // [1, 2, 3]
        
        swap(numbers, 0, 2);
        System.out.println("After swap: " + numbers);   // [3, 2, 1]
    }
}
```

## 🛠️ **Workarounds for Wildcard Limitations**

### Pattern 1: The Capture Helper Method

```java
import java.util.*;
import java.util.function.*;

public class CaptureHelperPattern {
    
    // Example 1: Reversing a list with wildcards
    public static void reverse(List<?> list) {
        reverseHelper(list);
    }
    
    private static <T> void reverseHelper(List<T> list) {
        for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
    
    // Example 2: Rotating a list
    public static void rotate(List<?> list, int distance) {
        rotateHelper(list, distance);
    }
    
    private static <T> void rotateHelper(List<T> list, int distance) {
        if (list.isEmpty()) return;
        
        int size = list.size();
        distance = distance % size;
        if (distance < 0) distance += size;
        
        if (distance == 0) return;
        
        // Use three-reversals algorithm
        reverseSublist(list, 0, size - distance - 1);
        reverseSublist(list, size - distance, size - 1);
        reverseSublist(list, 0, size - 1);
    }
    
    private static <T> void reverseSublist(List<T> list, int from, int to) {
        for (int i = from, j = to; i < j; i++, j--) {
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
    
    // Example 3: Applying a function to each element
    public static <T> void applyToList(List<T> list, UnaryOperator<T> operator) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, operator.apply(list.get(i)));
        }
    }
    
    // But what if we have List<?>? Use capture!
    public static void applyToWildcardList(List<?> list, UnaryOperator<Object> operator) {
        applyToWildcardListHelper(list, operator);
    }
    
    private static <T> void applyToWildcardListHelper(List<T> list, UnaryOperator<Object> operator) {
        for (int i = 0; i < list.size(); i++) {
            // Need to cast, but it's safe because operator takes Object
            @SuppressWarnings("unchecked")
            T newValue = (T) operator.apply(list.get(i));
            list.set(i, newValue);
        }
    }
    
    // Better: Use Function<? super T, ? extends T>
    public static <T> void transformList(List<T> list, Function<? super T, ? extends T> transformer) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, transformer.apply(list.get(i)));
        }
    }
    
    // For List<? extends T>, we can't modify, so return new list
    public static <T, R> List<R> mapList(List<? extends T> list, Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : list) {
            result.add(mapper.apply(item));
        }
        return result;
    }
    
    public static void main(String[] args) {
        // Test reverse
        List<String> strings = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        reverse(strings);
        System.out.println("Reversed: " + strings);  // [D, C, B, A]
        
        // Test rotate
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        rotate(numbers, 2);
        System.out.println("Rotated by 2: " + numbers);  // [4, 5, 1, 2, 3]
        
        // Test transform
        List<String> words = new ArrayList<>(Arrays.asList("hello", "world"));
        transformList(words, String::toUpperCase);
        System.out.println("Uppercased: " + words);  // [HELLO, WORLD]
        
        // Test map
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> lengths = mapList(names, String::length);
        System.out.println("Name lengths: " + lengths);  // [5, 3, 7]
    }
}
```

### Pattern 2: Type-Safe Containers with Capture

```java
import java.util.*;
import java.util.function.Consumer;

public class TypeSafeContainers {
    
    // Problem: We want a container that can hold any type,
    // but still allow type-safe operations when we know the type
    
    static class WildcardContainer {
        private List<?> items = new ArrayList<>();
        
        // Can't do this directly:
        // public void add(? item) { items.add(item); }
        
        // Solution: Use capture in add method
        public <T> void add(T item) {
            // We need to cast, but it's safe because we're adding to List<Object>
            @SuppressWarnings("unchecked")
            List<Object> objectList = (List<Object>) items;
            objectList.add(item);
        }
        
        // For getting items, we need to know the type
        @SuppressWarnings("unchecked")
        public <T> T get(int index, Class<T> type) {
            Object item = ((List<Object>) items).get(index);
            if (type.isInstance(item)) {
                return type.cast(item);
            }
            throw new ClassCastException("Item at index " + index + " is not of type " + type);
        }
        
        // Process all items of a specific type
        public <T> void processAll(Class<T> type, Consumer<T> processor) {
            for (Object item : (List<Object>) items) {
                if (type.isInstance(item)) {
                    processor.accept(type.cast(item));
                }
            }
        }
        
        public int size() {
            return ((List<?>) items).size();
        }
    }
    
    // Better design: Use separate type-safe views
    static class MultiTypeContainer {
        private final List<Object> items = new ArrayList<>();
        
        // Add any item
        public <T> void add(T item) {
            items.add(item);
        }
        
        // Get type-safe view for reading
        public <T> List<T> getView(Class<T> type) {
            return new TypeSafeListView<>(items, type);
        }
        
        // Get type-safe view for writing
        public <T> void addAll(Class<T> type, Collection<T> collection) {
            items.addAll(collection);
        }
        
        // Process all items of a type
        public <T> void forEach(Class<T> type, Consumer<T> action) {
            for (Object item : items) {
                if (type.isInstance(item)) {
                    action.accept(type.cast(item));
                }
            }
        }
        
        // Type-safe list view
        private static class TypeSafeListView<T> extends AbstractList<T> {
            private final List<Object> source;
            private final Class<T> type;
            
            TypeSafeListView(List<Object> source, Class<T> type) {
                this.source = source;
                this.type = type;
            }
            
            @Override
            public T get(int index) {
                // Find the nth item of the right type
                int count = 0;
                for (Object item : source) {
                    if (type.isInstance(item)) {
                        if (count == index) {
                            return type.cast(item);
                        }
                        count++;
                    }
                }
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
            }
            
            @Override
            public int size() {
                int count = 0;
                for (Object item : source) {
                    if (type.isInstance(item)) {
                        count++;
                    }
                }
                return count;
            }
            
            @Override
            public boolean add(T element) {
                return source.add(element);
            }
        }
    }
    
    public static void main(String[] args) {
        // Test WildcardContainer
        WildcardContainer container1 = new WildcardContainer();
        container1.add("Hello");
        container1.add(123);
        container1.add(3.14);
        
        System.out.println("Size: " + container1.size());  // 3
        
        String str = container1.get(0, String.class);
        System.out.println("String item: " + str);  // Hello
        
        container1.processAll(Integer.class, num -> 
            System.out.println("Integer: " + num));  // 123
        
        // Test MultiTypeContainer
        MultiTypeContainer container2 = new MultiTypeContainer();
        container2.add("Apple");
        container2.add(42);
        container2.add("Banana");
        container2.add(3.14);
        
        List<String> strings = container2.getView(String.class);
        System.out.println("Strings: " + strings);  // [Apple, Banana]
        System.out.println("String count: " + strings.size());  // 2
        
        strings.add("Cherry");
        System.out.println("After adding Cherry: " + strings);  // [Apple, Banana, Cherry]
        
        container2.forEach(Integer.class, num -> 
            System.out.println("Found integer: " + num));  // 42
    }
}
```

## 🧩 **Helper Method Pattern**

### Advanced Capture Patterns

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HelperMethodPattern {
    
    // Pattern 1: Capturing for type-safe comparisons
    public static <T> boolean containsDuplicates(List<T> list, Comparator<? super T> comparator) {
        Set<T> seen = new HashSet<>();
        for (T item : list) {
            if (!seen.add(item)) {
                return true;
            }
        }
        return false;
    }
    
    // But what if we have List<? extends T>? Use capture!
    public static boolean containsDuplicatesWildcard(List<?> list, Comparator<Object> comparator) {
        return containsDuplicatesWildcardHelper(list, comparator);
    }
    
    private static <T> boolean containsDuplicatesWildcardHelper(List<T> list, Comparator<Object> comparator) {
        // We need a Comparator<? super T>, but we have Comparator<Object>
        // Since Object is super of everything, we can use it
        @SuppressWarnings("unchecked")
        Comparator<? super T> typedComparator = (Comparator<? super T>) comparator;
        return containsDuplicates(list, typedComparator);
    }
    
    // Pattern 2: Capturing for sorting with wildcards
    public static void sortWildcard(List<?> list, Comparator<Object> comparator) {
        sortWildcardHelper(list, comparator);
    }
    
    private static <T> void sortWildcardHelper(List<T> list, Comparator<Object> comparator) {
        @SuppressWarnings("unchecked")
        Comparator<? super T> typedComparator = (Comparator<? super T>) comparator;
        list.sort(typedComparator);
    }
    
    // Pattern 3: Capturing for filtering
    public static <T> List<T> filterWildcard(List<?> list, Predicate<Object> predicate, Class<T> type) {
        List<T> result = new ArrayList<>();
        for (Object item : list) {
            if (predicate.test(item) && type.isInstance(item)) {
                result.add(type.cast(item));
            }
        }
        return result;
    }
    
    // Pattern 4: Capturing for mapping with type conversion
    public static <R> List<R> mapWildcard(List<?> list, Function<Object, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (Object item : list) {
            result.add(mapper.apply(item));
        }
        return result;
    }
    
    // Pattern 5: Two-level capture (nested wildcards)
    public static void processNestedWildcard(List<List<?>> listOfLists) {
        processNestedWildcardHelper(listOfLists);
    }
    
    private static void processNestedWildcardHelper(List<List<?>> listOfLists) {
        // Each inner list has its own captured type
        for (List<?> innerList : listOfLists) {
            // We can process each inner list using capture
            processInnerList(innerList);
        }
    }
    
    private static <T> void processInnerList(List<T> innerList) {
        System.out.println("Processing list of " + 
            (innerList.isEmpty() ? "unknown type" : innerList.get(0).getClass().getSimpleName()));
        // Can now work with T
    }
    
    // Pattern 6: Capture in streams
    public static <R> List<R> collectWildcard(List<?> list, 
                                             Collector<Object, ?, List<R>> collector) {
        return list.stream().collect(collector);
    }
    
    // Special case: Convert List<?> to List<T> with filtering
    public static <T> List<T> convertWildcardList(List<?> source, Class<T> targetType) {
        return source.stream()
            .filter(targetType::isInstance)
            .map(targetType::cast)
            .collect(Collectors.toList());
    }
    
    // Pattern 7: Type-safe min/max with wildcards
    public static <T> T minWildcard(List<? extends T> list, Comparator<? super T> comparator) {
        if (list.isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        
        Iterator<? extends T> iterator = list.iterator();
        T min = iterator.next();
        
        while (iterator.hasNext()) {
            T current = iterator.next();
            if (comparator.compare(current, min) < 0) {
                min = current;
            }
        }
        
        return min;
    }
    
    // For List<?> we need to capture first
    public static Object minWildcardUnknown(List<?> list, Comparator<Object> comparator) {
        return minWildcardHelper(list, comparator);
    }
    
    private static <T> T minWildcardHelper(List<T> list, Comparator<Object> comparator) {
        @SuppressWarnings("unchecked")
        Comparator<? super T> typedComparator = (Comparator<? super T>) comparator;
        return minWildcard(list, typedComparator);
    }
    
    // Test the patterns
    public static void main(String[] args) {
        // Test 1: Sort with wildcard
        List<String> strings = new ArrayList<>(Arrays.asList("C", "A", "B"));
        sortWildcard(strings, Comparator.naturalOrder());
        System.out.println("Sorted strings: " + strings);  // [A, B, C]
        
        // Test 2: Filter with wildcard
        List<Object> mixed = Arrays.asList("Hello", 123, "World", 456, 3.14);
        List<String> stringsOnly = filterWildcard(mixed, 
            obj -> obj instanceof String, String.class);
        System.out.println("Strings only: " + stringsOnly);  // [Hello, World]
        
        // Test 3: Map with wildcard
        List<Object> objects = Arrays.asList("apple", "banana", "cherry");
        List<Integer> lengths = mapWildcard(objects, obj -> obj.toString().length());
        System.out.println("String lengths: " + lengths);  // [5, 6, 6]
        
        // Test 4: Nested wildcard
        List<List<?>> nested = Arrays.asList(
            Arrays.asList(1, 2, 3),
            Arrays.asList("A", "B", "C"),
            Arrays.asList(1.1, 2.2, 3.3)
        );
        processNestedWildcard(nested);
        
        // Test 5: Convert wildcard list
        List<?> wildcardList = Arrays.asList(1, "two", 3, "four", 5);
        List<Integer> integers = convertWildcardList(wildcardList, Integer.class);
        System.out.println("Integers only: " + integers);  // [1, 3, 5]
        
        // Test 6: Min with wildcard
        List<Integer> numbers = Arrays.asList(5, 2, 8, 1, 9);
        Integer min = minWildcard(numbers, Comparator.naturalOrder());
        System.out.println("Min number: " + min);  // 1
        
        Object minObj = minWildcardUnknown(numbers, Comparator.naturalOrder());
        System.out.println("Min object: " + minObj);  // 1
    }
}
```

## 🔄 **Real-World Examples**

### Example 1: Database Result Set Processing

```java
import java.util.*;
import java.sql.*;
import java.util.function.Function;

public class DatabaseExample {
    
    // Generic method to process database results
    public static <T> List<T> processResultSet(ResultSet rs, 
                                              Function<ResultSet, T> rowMapper) 
                                              throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rowMapper.apply(rs));
        }
        return results;
    }
    
    // But what if we have different mappers for different result sets?
    // Use wildcard capture to handle unknown types
    
    public static List<?> processDynamicResultSet(ResultSet rs, 
                                                 Function<ResultSet, ?> rowMapper) 
                                                 throws SQLException {
        return processDynamicResultSetHelper(rs, rowMapper);
    }
    
    private static <T> List<T> processDynamicResultSetHelper(ResultSet rs, 
                                                           Function<ResultSet, T> rowMapper) 
                                                           throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rowMapper.apply(rs));
        }
        return results;
    }
    
    // Even more flexible: accept any function that can process ResultSet
    public static <R> R processResultSetWithCollector(ResultSet rs,
                                                     Function<ResultSet, ?> rowMapper,
                                                     Collector<Object, ?, R> collector)
                                                     throws SQLException {
        return processResultSetWithCollectorHelper(rs, rowMapper, collector);
    }
    
    private static <T, R> R processResultSetWithCollectorHelper(ResultSet rs,
                                                               Function<ResultSet, T> rowMapper,
                                                               Collector<T, ?, R> collector)
                                                               throws SQLException {
        List<T> results = new ArrayList<>();
        while (rs.next()) {
            results.add(rowMapper.apply(rs));
        }
        return results.stream().collect(collector);
    }
    
    // Example mappers
    static class User {
        final int id;
        final String name;
        
        User(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }
    
    static Function<ResultSet, User> userMapper = rs -> {
        try {
            return new User(rs.getInt("id"), rs.getString("name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };
    
    static Function<ResultSet, String> nameMapper = rs -> {
        try {
            return rs.getString("name");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };
    
    static Function<ResultSet, Integer> idMapper = rs -> {
        try {
            return rs.getInt("id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };
    
    // Simulated ResultSet for testing
    static class MockResultSet {
        private final List<Map<String, Object>> data;
        private int currentRow = -1;
        
        MockResultSet(List<Map<String, Object>> data) {
            this.data = data;
        }
        
        boolean next() {
            currentRow++;
            return currentRow < data.size();
        }
        
        int getInt(String column) {
            return (Integer) data.get(currentRow).get(column);
        }
        
        String getString(String column) {
            return (String) data.get(currentRow).get(column);
        }
    }
    
    public static void main(String[] args) throws SQLException {
        // Create mock data
        List<Map<String, Object>> mockData = new ArrayList<>();
        
        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", 1);
        row1.put("name", "Alice");
        mockData.add(row1);
        
        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", 2);
        row2.put("name", "Bob");
        mockData.add(row2);
        
        MockResultSet mockRs = new MockResultSet(mockData);
        
        // Test 1: Process with specific mapper
        List<User> users = processResultSet(mockRs, userMapper);
        System.out.println("Users: " + users);
        
        // Reset for next test
        mockRs = new MockResultSet(mockData);
        
        // Test 2: Process with dynamic mapper (wildcard)
        List<?> dynamicResults = processDynamicResultSet(mockRs, nameMapper);
        System.out.println("Dynamic results: " + dynamicResults);
        
        // Reset for next test
        mockRs = new MockResultSet(mockData);
        
        // Test 3: Process with collector
        Map<Integer, String> idToName = processResultSetWithCollector(
            mockRs, 
            rs -> new AbstractMap.SimpleEntry<>(
                rs.getInt("id"), 
                rs.getString("name")
            ),
            Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            )
        );
        System.out.println("ID to Name map: " + idToName);
    }
}
```

### Example 2: Event System with Type Capture

```java
import java.util.*;
import java.util.function.Consumer;

public class EventSystemWithCapture {
    
    // Event hierarchy
    static abstract class Event {
        private final long timestamp;
        
        protected Event() {
            this.timestamp = System.currentTimeMillis();
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
    
    static class UserEvent extends Event {
        private final String username;
        
        public UserEvent(String username) {
            this.username = username;
        }
        
        public String getUsername() {
            return username;
        }
    }
    
    static class SystemEvent extends Event {
        private final String component;
        private final String message;
        
        public SystemEvent(String component, String message) {
            this.component = component;
            this.message = message;
        }
        
        public String getComponent() {
            return component;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    // Event bus that can handle any event type
    static class EventBus {
        private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();
        
        // Register handler for specific event type
        public <E extends Event> void registerHandler(Class<E> eventType, Consumer<E> handler) {
            handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        }
        
        // Register handler for any event (wildcard)
        public void registerWildcardHandler(Consumer<? super Event> handler) {
            // We need to capture the wildcard to store it
            registerWildcardHandlerHelper(handler);
        }
        
        private <E extends Event> void registerWildcardHandlerHelper(Consumer<E> handler) {
            // Store as Consumer<?> since we don't know the exact type
            handlers.computeIfAbsent(Event.class, k -> new ArrayList<>()).add(handler);
        }
        
        // Publish event
        public void publish(Event event) {
            // Call handlers for the exact event type
            callHandlers(event.getClass(), event);
            
            // Call wildcard handlers (those registered for Event.class)
            callHandlers(Event.class, event);
            
            // Call handlers for supertypes
            callSupertypeHandlers(event);
        }
        
        @SuppressWarnings("unchecked")
        private void callHandlers(Class<?> eventType, Event event) {
            List<Consumer<?>> eventHandlers = handlers.get(eventType);
            if (eventHandlers != null) {
                for (Consumer<?> handler : eventHandlers) {
                    // Need to cast, but it's safe because we registered with the right type
                    Consumer<Event> typedHandler = (Consumer<Event>) handler;
                    typedHandler.accept(event);
                }
            }
        }
        
        private void callSupertypeHandlers(Event event) {
            Class<?> clazz = event.getClass().getSuperclass();
            while (clazz != null && Event.class.isAssignableFrom(clazz)) {
                callHandlers(clazz, event);
                clazz = clazz.getSuperclass();
            }
        }
        
        // Get all handlers for inspection
        public Map<Class<?>, List<Consumer<?>>> getAllHandlers() {
            return Collections.unmodifiableMap(handlers);
        }
    }
    
    // Utility to create typed event handlers
    public static <E extends Event> Consumer<E> typedHandler(Consumer<E> handler) {
        return handler;
    }
    
    // Wildcard handler that can handle any event
    public static Consumer<? super Event> wildcardHandler(Consumer<Event> handler) {
        return handler;
    }
    
    public static void main(String[] args) {
        EventBus bus = new EventBus();
        
        // Register typed handlers
        bus.registerHandler(UserEvent.class, 
            typedHandler((UserEvent e) -> 
                System.out.println("User event: " + e.getUsername())));
        
        bus.registerHandler(SystemEvent.class,
            typedHandler((SystemEvent e) ->
                System.out.println("System event: " + e.getComponent() + " - " + e.getMessage())));
        
        // Register wildcard handler
        bus.registerWildcardHandler(
            wildcardHandler((Event e) ->
                System.out.println("Generic handler: " + e.getClass().getSimpleName())));
        
        // Publish events
        bus.publish(new UserEvent("alice"));
        bus.publish(new SystemEvent("Database", "Backup completed"));
        
        // Show all handlers
        System.out.println("\nRegistered handlers:");
        bus.getAllHandlers().forEach((type, handlers) -> {
            System.out.println("  " + type.getSimpleName() + ": " + handlers.size() + " handlers");
        });
    }
}
```

## ⚠️ **Common Pitfalls and Solutions**

### Pitfall 1: Overusing Wildcard Capture

```java
import java.util.*;

public class WildcardPitfalls {
    
    // ❌ Overcomplicated - too many wildcards
    public static <T> void overcomplicatedMethod(
        List<? extends List<? extends T>> listOfLists,
        Function<? super T, ? extends T> transformer) {
        // Very hard to understand and use
    }
    
    // ✅ Better: Simplify when possible
    public static <T> void simplifiedMethod(
        List<List<T>> listOfLists,
        Function<T, T> transformer) {
        // Clearer and easier to use
    }
    
    // Pitfall 2: Losing type information
    public static List<?> getList() {
        return Arrays.asList("A", "B", "C");
    }
    
    // Problem: Caller gets List<?> and can't do much with it
    public static void testLosingType() {
        List<?> list = getList();
        // list.add("D");  // ❌ Can't add
        // String s = list.get(0);  // ❌ Can't get as String
        Object o = list.get(0);  // ✅ Only Object
        
        // Solution: Return typed list or use generic method
    }
    
    // Better approach
    public static <T> List<T> getTypedList(Class<T> type) {
        List<T> list = new ArrayList<>();
        // Initialize based on type
        return list;
    }
    
    // Pitfall 3: Unnecessary capture
    public static void unnecessaryCapture(List<?> list) {
        // Don't need capture helper if you're only reading
        for (Object item : list) {
            System.out.println(item);
        }
        
        // Only need capture when you need to write
    }
    
    // Pitfall 4: Capturing the wrong type
    public static void wrongCapture(List<?> list1, List<?> list2) {
        // These might be lists of different types!
        // Can't assume they have the same element type
        
        // ❌ Wrong: Trying to add from list2 to list1
        // for (Object item : list2) {
        //     // list1.add(item);  // Compile error
        // }
        
        // ✅ Correct: Check types or use bounded wildcards
    }
    
    // Correct approach with bounded wildcards
    public static <T> void mergeLists(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }
    
    // Pitfall 5: Forgetting about null
    public static void nullHandling(List<?> list) {
        // Can always add null to any wildcard list
        list.add(null);
        
        // But need to handle null when reading
        for (Object item : list) {
            if (item != null) {
                System.out.println(item.toString());
            }
        }
    }
    
    public static void main(String[] args) {
        // Demonstrate the pitfalls
        List<String> strings = Arrays.asList("A", "B");
        List<Integer> integers = Arrays.asList(1, 2);
        
        // Can't merge different types
        // mergeLists(strings, integers);  // ❌ Compile error
        
        List<Object> objects = new ArrayList<>();
        mergeLists(objects, strings);  // ✅ OK
        mergeLists(objects, integers); // ✅ OK
        
        System.out.println("Merged objects: " + objects);  // [A, B, 1, 2]
    }
}
```

## 📊 **Performance Considerations**

### Capture vs No-Capture Performance

```java
import java.util.*;
import java.util.function.Function;

public class CapturePerformance {
    
    // Method with capture
    public static void processWithCapture(List<?> list, Function<Object, Object> func) {
        processWithCaptureHelper(list, func);
    }
    
    private static <T> void processWithCaptureHelper(List<T> list, Function<Object, Object> func) {
        for (int i = 0; i < list.size(); i++) {
            @SuppressWarnings("unchecked")
            T newValue = (T) func.apply(list.get(i));
            list.set(i, newValue);
        }
    }
    
    // Method without capture (typed)
    public static <T> void processTyped(List<T> list, Function<T, T> func) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, func.apply(list.get(i)));
        }
    }
    
    // Method using streams (modern approach)
    public static <T> List<T> processWithStreams(List<T> list, Function<T, T> func) {
        return list.stream()
            .map(func)
            .toList();
    }
    
    // Benchmark
    public static void benchmark() {
        int size = 1000000;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            numbers.add(i);
        }
        
        Function<Integer, Integer> increment = x -> x + 1;
        Function<Object, Object> incrementObj = x -> (Integer) x + 1;
        
        // Warm up
        for (int i = 0; i < 10; i++) {
            List<Integer> copy1 = new ArrayList<>(numbers);
            processTyped(copy1, increment);
        }
        
        // Test typed
        long start = System.nanoTime();
        List<Integer> copy1 = new ArrayList<>(numbers);
        processTyped(copy1, increment);
        long timeTyped = System.nanoTime() - start;
        
        // Test with capture
        start = System.nanoTime();
        List<Integer> copy2 = new ArrayList<>(numbers);
        processWithCapture(copy2, incrementObj);
        long timeCapture = System.nanoTime() - start;
        
        // Test streams
        start = System.nanoTime();
        List<Integer> copy3 = new ArrayList<>(numbers);
        List<Integer> result3 = processWithStreams(copy3, increment);
        long timeStreams = System.nanoTime() - start;
        
        System.out.println("Typed processing: " + timeTyped + " ns");
        System.out.println("Capture processing: " + timeCapture + " ns");
        System.out.println("Streams processing: " + timeStreams + " ns");
        
        // Note: The capture overhead is minimal - mostly compile-time.
        // The runtime performance difference is usually negligible.
        // Choose based on API clarity and type safety, not performance.
    }
    
    public static void main(String[] args) {
        benchmark();
    }
}
```

## 🎓 **Best Practices for Wildcard Capture**

1. **Use capture only when necessary** - if you're only reading, you might not need it
2. **Keep helper methods private** - they're implementation details
3. **Document the capture behavior** - especially in public APIs
4. **Test with different type arguments** - ensure your capture works correctly
5. **Consider alternative designs** - sometimes type parameters are better than wildcards
6. **Use @SuppressWarnings judiciously** - document why the warning is safe to suppress
7. **Balance flexibility with complexity** - don't over-engineer with wildcards
8. **Follow PECS principle** - helps decide when to use extends vs super

## 🚀 **Next Steps**

Now that you've mastered wildcard capture and helper methods, you're ready for the definitive guide on when to use which wildcard. In [Chapter 15](#15-guidelines-for-wildcard-use), we'll explore **Guidelines for Wildcard Use**, including the famous PECS principle, when to choose wildcards vs type parameters, and best practices for clean, flexible API design.

> 💡 **Practice Exercise**: 
> 1. Implement a generic `swap` method for arrays using wildcard capture
> 2. Create a `WildcardUtils` class with helper methods for common operations
> 3. Design a type-safe heterogeneous container using wildcard capture
> 4. Implement a `zip` method that combines two `List<?>` into a `List<Pair<?, ?>>`
> 5. Benchmark different approaches to wildcard capture vs type parameters