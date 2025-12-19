# Chapter 11: 🔄 **Unbounded Wildcards**

## The Ultimate Flexibility: When You Don't Care About the Type

Unbounded wildcards (`?`) represent the most flexible, but also the most restrictive, form of wildcards. They're used when you need to work with generic types but don't need to know anything about the actual type parameter. They're perfect for operations that work on any type or when you only use methods from the `Object` class.

## 🌐 **What Are Unbounded Wildcards?**

### Basic Concept and Usage

```java
import java.util.*;

public class UnboundedWildcardBasics {
    
    // Method that accepts any List, regardless of element type
    public static void printList(List<?> list) {
        for (Object obj : list) {
            System.out.print(obj + " ");
        }
        System.out.println();
    }
    
    // Method that returns a List of unknown type
    public static List<?> getList() {
        return Arrays.asList(1, 2, 3);  // Returns List<Integer>, but as List<?>
    }
    
    // Method that works with any type of collection
    public static int countElements(Collection<?> collection) {
        return collection.size();
    }
    
    // Method that checks if a collection contains a specific element
    public static boolean containsElement(Collection<?> collection, Object element) {
        return collection.contains(element);  // contains() takes Object, so this works
    }
    
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("Hello", "World");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        List<Object> objects = Arrays.asList("A", 1, 3.14, true);
        
        // All of these work with unbounded wildcards
        printList(strings);   // Hello World
        printList(integers);  // 1 2 3
        printList(doubles);   // 1.1 2.2 3.3
        printList(objects);   // A 1 3.14 true
        
        // Counting elements
        System.out.println("String count: " + countElements(strings));    // 2
        System.out.println("Integer count: " + countElements(integers));  // 3
        
        // Checking containment
        System.out.println("Contains 'Hello': " + containsElement(strings, "Hello"));  // true
        System.out.println("Contains 2: " + containsElement(integers, 2));             // true
    }
}
```

## 🧹 **When You Only Need Object-Level Operations**

### Operations That Work with Any Type

```java
import java.util.*;

public class ObjectLevelOperations {
    
    // 1. Size and emptiness checks
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    
    // 2. Clearing collections
    public static void clearAll(Collection<?>... collections) {
        for (Collection<?> collection : collections) {
            collection.clear();
        }
    }
    
    // 3. Creating defensive copies
    public static <T> List<T> defensiveCopy(List<? extends T> source) {
        return new ArrayList<>(source);
    }
    
    // 4. Converting to array
    public static Object[] toArray(Collection<?> collection) {
        return collection.toArray();
    }
    
    // 5. Checking equality of two collections (by content)
    public static boolean haveSameSize(Collection<?> c1, Collection<?> c2) {
        return c1.size() == c2.size();
    }
    
    // 6. Finding common operations
    public static void performCommonOperations(List<?> list) {
        // All these methods work because they don't depend on the element type
        System.out.println("Size: " + list.size());
        System.out.println("Empty: " + list.isEmpty());
        System.out.println("First element: " + list.get(0));  // Returns Object
        System.out.println("Last index of first element: " + list.lastIndexOf(list.get(0)));
        
        // Can also use iterator
        Iterator<?> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            System.out.println("Element: " + element);
        }
    }
    
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("A", "B", "C");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        
        System.out.println("Is strings empty? " + isEmpty(strings));  // false
        System.out.println("Same size? " + haveSameSize(strings, integers));  // true
        
        performCommonOperations(strings);
        performCommonOperations(integers);
        
        // Defensive copy
        List<String> copy = defensiveCopy(strings);
        System.out.println("Copy: " + copy);
        
        // Clear (commented to avoid actual clearing in example)
        // clearAll(strings, integers);
    }
}
```

## 📦 **Useful in Generic APIs Where Type Doesn't Matter**

### Real-World API Examples

```java
import java.util.*;
import java.util.function.Predicate;

public class GenericAPIExamples {
    
    // 1. Logging utility - doesn't care about collection type
    public static class CollectionLogger {
        
        public static void logCollection(String name, Collection<?> collection) {
            System.out.println(name + " (size=" + collection.size() + "):");
            int index = 0;
            for (Object item : collection) {
                System.out.println("  [" + index++ + "] " + item + 
                                 " (type: " + item.getClass().getSimpleName() + ")");
            }
        }
        
        public static void logComparison(Collection<?> c1, Collection<?> c2) {
            System.out.println("Collection 1 size: " + c1.size());
            System.out.println("Collection 2 size: " + c2.size());
            System.out.println("Equal sizes: " + (c1.size() == c2.size()));
        }
    }
    
    // 2. Validation utilities
    public static class CollectionValidator {
        
        public static boolean isValid(Collection<?> collection) {
            return collection != null && !collection.isEmpty();
        }
        
        public static void validateNotNull(Collection<?> collection, String name) {
            if (collection == null) {
                throw new IllegalArgumentException(name + " cannot be null");
            }
        }
        
        public static void validateNotEmpty(Collection<?> collection, String name) {
            validateNotNull(collection, name);
            if (collection.isEmpty()) {
                throw new IllegalArgumentException(name + " cannot be empty");
            }
        }
        
        public static <T> boolean containsNull(Collection<T> collection) {
            for (T element : collection) {
                if (element == null) {
                    return true;
                }
            }
            return false;
        }
    }
    
    // 3. Serialization/Deserialization helpers
    public static class SerializationHelper {
        
        // Convert any collection to string representation
        public static String toString(Collection<?> collection) {
            if (collection == null) return "null";
            
            StringBuilder sb = new StringBuilder("[");
            Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
        
        // Parse string back to list of strings (simple example)
        public static List<String> fromString(String str) {
            if (str == null || str.equals("null")) return null;
            
            String content = str.substring(1, str.length() - 1); // Remove brackets
            String[] parts = content.split(", ");
            return Arrays.asList(parts);
        }
    }
    
    // 4. Collection transformation utilities
    public static class TransformationUtils {
        
        // Reversing any list
        public static List<?> reverse(List<?> list) {
            List<Object> result = new ArrayList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                result.add(list.get(i));
            }
            return result;
        }
        
        // Shuffling any list
        public static void shuffle(List<?> list) {
            // Need helper method due to wildcard capture
            shuffleHelper(list);
        }
        
        private static <T> void shuffleHelper(List<T> list) {
            Collections.shuffle(list);
        }
        
        // Creating unmodifiable view
        public static Collection<?> unmodifiableView(Collection<?> collection) {
            return Collections.unmodifiableCollection(collection);
        }
    }
    
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("Apple", "Banana", "Cherry");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        Set<Double> doubles = new HashSet<>(Arrays.asList(1.1, 2.2, 3.3));
        
        // Logging
        CollectionLogger.logCollection("Fruits", strings);
        CollectionLogger.logCollection("Numbers", numbers);
        CollectionLogger.logComparison(strings, numbers);
        
        // Validation
        System.out.println("Is strings valid? " + CollectionValidator.isValid(strings));
        System.out.println("Contains null? " + CollectionValidator.containsNull(strings));
        
        // Serialization
        String serialized = SerializationHelper.toString(strings);
        System.out.println("Serialized: " + serialized);
        
        // Transformation
        List<?> reversed = TransformationUtils.reverse(numbers);
        System.out.println("Reversed: " + reversed);
        
        // Create unmodifiable view
        Collection<?> unmodifiable = TransformationUtils.unmodifiableView(doubles);
        System.out.println("Unmodifiable view: " + unmodifiable);
        
        // Try to modify (will throw exception)
        try {
            ((Collection<Double>) unmodifiable).add(4.4);  // UnsupportedOperationException
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable collection");
        }
    }
}
```

## 🔄 **Unbounded vs Bounded Wildcards**

### Comparison and When to Use Each

```java
import java.util.*;

public class WildcardComparison {
    
    // Three approaches to the same problem
    
    // 1. Using raw types (DON'T DO THIS)
    public static void printRaw(List list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
        // Can add anything - UNSAFE!
        list.add("anything");
        list.add(123);
    }
    
    // 2. Using unbounded wildcard
    public static void printUnbounded(List<?> list) {
        for (Object obj : list) {
            System.out.println(obj);
        }
        // Cannot add anything (except null) - SAFE!
        // list.add("anything");  // Compile error
        // list.add(123);         // Compile error
        list.add(null);           // Only null allowed
    }
    
    // 3. Using upper bounded wildcard
    public static void printNumbers(List<? extends Number> list) {
        for (Number num : list) {
            System.out.println(num.doubleValue());
        }
        // Cannot add - SAFE!
        // list.add(123);  // Compile error
    }
    
    // 4. Using lower bounded wildcard
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        // Can read only as Object
        Object obj = list.get(0);
    }
    
    // When to use which?
    public static void guidelines() {
        List<String> strings = Arrays.asList("A", "B");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Number> numbers = Arrays.asList(1, 2.5, 3L);
        
        // Use unbounded wildcard when:
        // - You only need Object-level operations
        // - Type doesn't matter at all
        // - You want maximum flexibility
        
        printUnbounded(strings);    // OK
        printUnbounded(integers);   // OK
        printUnbounded(numbers);    // OK
        
        // Use upper bounded wildcard when:
        // - You need to use methods from a specific class/interface
        // - You want to restrict to a type hierarchy
        
        // printNumbers(strings);    // ❌ Compile error - String not a Number
        printNumbers(integers);      // OK - Integer extends Number
        printNumbers(numbers);       // OK - Number extends Number
        
        // Use lower bounded wildcard when:
        // - You need to add elements
        // - You're implementing consumer behavior
        
        List<Object> objects = new ArrayList<>();
        addIntegers(objects);        // OK - Object super Integer
        addIntegers(numbers);        // OK - Number super Integer
        addIntegers(integers);       // OK - Integer super Integer
        // addIntegers(strings);     // ❌ Compile error - String not super Integer
    }
    
    // Real example: Collections.disjoint()
    // Signature: public static boolean disjoint(Collection<?> c1, Collection<?> c2)
    public static void disjointExample() {
        List<String> strings = Arrays.asList("A", "B", "C");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<String> moreStrings = Arrays.asList("C", "D", "E");
        
        System.out.println("disjoint(strings, integers): " + 
                          Collections.disjoint(strings, integers));  // true
        System.out.println("disjoint(strings, moreStrings): " + 
                          Collections.disjoint(strings, moreStrings));  // false
        
        // Works because disjoint only needs to compare objects using equals()
        // which takes Object as parameter
    }
    
    public static void main(String[] args) {
        guidelines();
        disjointExample();
    }
}
```

## 🏗️ **Practical Patterns and Examples**

### 1. **Visitor Pattern with Unbounded Wildcards**

```java
import java.util.*;

public class VisitorPattern {
    
    // Base interface for visitable elements
    interface Visitable {
        void accept(Visitor visitor);
    }
    
    // Visitor interface that can visit any Visitable
    interface Visitor {
        void visit(Visitable visitable);
    }
    
    // Concrete elements
    static class Document implements Visitable {
        private final String content;
        
        public Document(String content) {
            this.content = content;
        }
        
        public String getContent() {
            return content;
        }
        
        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
    
    static class Image implements Visitable {
        private final String filename;
        private final int width;
        private final int height;
        
        public Image(String filename, int width, int height) {
            this.filename = filename;
            this.width = width;
            this.height = height;
        }
        
        public String getFilename() {
            return filename;
        }
        
        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }
    
    // Collection processor using unbounded wildcards
    static class CollectionProcessor implements Visitor {
        
        // Process any collection of visitable items
        public void processCollection(Collection<? extends Visitable> collection) {
            for (Visitable item : collection) {
                item.accept(this);
            }
        }
        
        @Override
        public void visit(Visitable visitable) {
            if (visitable instanceof Document) {
                Document doc = (Document) visitable;
                System.out.println("Processing document: " + doc.getContent());
            } else if (visitable instanceof Image) {
                Image img = (Image) visitable;
                System.out.println("Processing image: " + img.getFilename() + 
                                 " (" + img.width + "x" + img.height + ")");
            } else {
                System.out.println("Processing unknown visitable: " + visitable);
            }
        }
    }
    
    public static void main(String[] args) {
        List<Visitable> items = Arrays.asList(
            new Document("Hello World"),
            new Image("photo.jpg", 800, 600),
            new Document("Another document")
        );
        
        CollectionProcessor processor = new CollectionProcessor();
        processor.processCollection(items);
    }
}
```

### 2. **Event System with Unbounded Wildcards**

```java
import java.util.*;
import java.util.function.Consumer;

public class EventSystem {
    
    // Base event class
    static abstract class Event {
        private final long timestamp;
        
        protected Event() {
            this.timestamp = System.currentTimeMillis();
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
    
    // Concrete events
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
        private final String action;
        
        public SystemEvent(String component, String action) {
            this.component = component;
            this.action = action;
        }
        
        public String getComponent() {
            return component;
        }
        
        public String getAction() {
            return action;
        }
    }
    
    // Event bus that can handle any type of event
    static class EventBus {
        private final Map<Class<?>, List<Consumer<?>>> handlers = new HashMap<>();
        
        // Register handler for any event type
        public <E extends Event> void registerHandler(Class<E> eventType, Consumer<E> handler) {
            handlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        }
        
        // Publish any event
        public void publish(Event event) {
            List<Consumer<?>> eventHandlers = handlers.get(event.getClass());
            if (eventHandlers != null) {
                for (Consumer<?> handler : eventHandlers) {
                    // Need to cast due to type erasure
                    @SuppressWarnings("unchecked")
                    Consumer<Event> typedHandler = (Consumer<Event>) handler;
                    typedHandler.accept(event);
                }
            }
        }
        
        // Get all handlers (for inspection)
        public Collection<Consumer<?>> getAllHandlers() {
            List<Consumer<?>> allHandlers = new ArrayList<>();
            for (List<Consumer<?>> handlerList : handlers.values()) {
                allHandlers.addAll(handlerList);
            }
            return allHandlers;
        }
        
        // Clear all handlers for a specific event type
        public void clearHandlers(Class<?> eventType) {
            handlers.remove(eventType);
        }
    }
    
    public static void main(String[] args) {
        EventBus bus = new EventBus();
        
        // Register handlers
        bus.registerHandler(UserEvent.class, event -> {
            System.out.println("User event: " + event.getUsername());
        });
        
        bus.registerHandler(SystemEvent.class, event -> {
            System.out.println("System event: " + event.getComponent() + " - " + event.getAction());
        });
        
        // Register generic event handler using unbounded wildcard
        bus.registerHandler(Event.class, event -> {
            System.out.println("Generic event: " + event.getClass().getSimpleName() + 
                             " at " + event.getTimestamp());
        });
        
        // Publish events
        bus.publish(new UserEvent("john_doe"));
        bus.publish(new SystemEvent("Database", "Backup"));
        
        // Show all handlers
        System.out.println("Total handlers: " + bus.getAllHandlers().size());
    }
}
```

## ⚠️ **Limitations and Considerations**

### What You Cannot Do with Unbounded Wildcards

```java
import java.util.*;

public class UnboundedLimitations {
    
    public static void demonstrateLimitations() {
        List<String> strings = Arrays.asList("A", "B", "C");
        List<?> wildcardList = strings;
        
        // 1. Cannot add elements (except null)
        // wildcardList.add("D");        // ❌ Compile error
        // wildcardList.add(new Object()); // ❌ Compile error
        wildcardList.add(null);          // ✅ Only null allowed
        
        // 2. Cannot use type-specific methods
        // String first = wildcardList.get(0);  // ❌ Compile error
        Object first = wildcardList.get(0);     // ✅ Must use Object
        
        // 3. Cannot create arrays directly
        // List<?>[] array = new List<?>[10];  // ⚠️ Warning, but allowed
        
        // 4. Cannot use in instanceof with generic type
        // if (wildcardList instanceof List<String>)  // ❌ Compile error
        if (wildcardList instanceof List) {          // ✅ Raw type only
            System.out.println("It's a List");
        }
        
        // 5. Wildcard capture issues
        // swap(wildcardList, 0, 1);  // ❌ Directly won't work
        
        // Solution: Use helper method
        swapHelper(wildcardList, 0, 1);
    }
    
    // Wildcard capture problem example
    public static void swap(List<?> list, int i, int j) {
        // This doesn't compile due to wildcard capture
        // ? temp = list.get(i);
        // list.set(i, list.get(j));
        // list.set(j, temp);
        
        // Need helper method
        swapHelper(list, i, j);
    }
    
    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // 6. Cannot use as return type for some operations
    public static List<?> problematicReturn() {
        List<String> strings = Arrays.asList("A", "B");
        // Returning as List<?> loses type information
        return strings;
    }
    
    public static void testProblematicReturn() {
        List<?> result = problematicReturn();
        // result.add("C");  // ❌ Can't add
        Object item = result.get(0);  // Only Object
    }
    
    // Better approach: Use generic method
    public static <T> List<T> betterReturn(List<T> input) {
        List<T> result = new ArrayList<>(input);
        // Can modify result
        return result;
    }
    
    public static void main(String[] args) {
        demonstrateLimitations();
        
        List<String> strings = Arrays.asList("A", "B", "C");
        List<String> modified = betterReturn(strings);
        modified.add("D");  // ✅ Can add
        System.out.println(modified);
    }
}
```

## 🎓 **Best Practices for Unbounded Wildcards**

### When to Use and When to Avoid

```java
import java.util.*;

public class BestPractices {
    
    // ✅ GOOD: Use unbounded wildcards when:
    
    // 1. Implementing utility methods that work on any collection
    public static boolean hasElements(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
    
    // 2. Writing methods that only use Object-class methods
    public static String getClassName(Object obj) {
        return obj.getClass().getName();
    }
    
    // 3. Creating APIs that should be maximally flexible
    public static void logAll(Collection<?>... collections) {
        for (Collection<?> collection : collections) {
            System.out.println("Collection size: " + collection.size());
        }
    }
    
    // 4. Working with legacy code that uses raw types
    public static List<?> safeView(List rawList) {
        // Wrap raw list to prevent unsafe operations
        return Collections.unmodifiableList(rawList);
    }
    
    // ❌ AVOID: Using unbounded wildcards when:
    
    // 1. You need to add elements (use ? super T instead)
    // public static void addElement(List<?> list, Object element) {
    //     list.add(element);  // ❌ Compile error
    // }
    
    // Better: Use lower bounded wildcard
    public static void addElement(List<Object> list, Object element) {
        list.add(element);  // ✅ Works
    }
    
    // Or use generic method
    public static <T> void addElementGeneric(List<T> list, T element) {
        list.add(element);  // ✅ Works
    }
    
    // 2. You need to return a specific type
    // public static List<?> createList() {
    //     return new ArrayList<String>();  // Caller gets List<?>
    // }
    
    // Better: Use generic method
    public static <T> List<T> createList(Class<T> clazz) {
        return new ArrayList<>();
    }
    
    // 3. You need to perform type-specific operations
    // public static void processNumbers(List<?> list) {
    //     for (Object obj : list) {
    //         // Can't call Number methods without casting
    //         if (obj instanceof Number) {
    //             Number num = (Number) obj;
    //             System.out.println(num.doubleValue());
    //         }
    //     }
    // }
    
    // Better: Use upper bounded wildcard
    public static void processNumbers(List<? extends Number> list) {
        for (Number num : list) {
            System.out.println(num.doubleValue());  // ✅ Direct access
        }
    }
    
    // 4. In class fields (usually better to use type parameters)
    // class Container {
    //     private List<?> items;  // Can't add to this
    // }
    
    // Better: Use type parameter
    class Container<T> {
        private List<T> items;  // Can add T to this
        
        public void addItem(T item) {
            items.add(item);
        }
    }
    
    public static void main(String[] args) {
        // Demonstrate good practices
        List<String> strings = Arrays.asList("A", "B");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        
        System.out.println("Strings have elements? " + hasElements(strings));
        System.out.println("Integers have elements? " + hasElements(integers));
        
        logAll(strings, integers);
        
        // Demonstrate alternatives
        List<Object> objectList = new ArrayList<>();
        addElement(objectList, "Test");
        
        List<String> stringList = createList(String.class);
        stringList.add("Hello");
        
        processNumbers(integers);
    }
}
```

## 📊 **Summary Table: Unbounded Wildcard Capabilities**

| Operation | Allowed with `List<?>` | Notes |
|-----------|------------------------|-------|
| **Read** | ✅ Yes, as `Object` | `Object item = list.get(0)` |
| **Iterate** | ✅ Yes, as `Object` | `for (Object obj : list)` |
| **Size/Empty** | ✅ Yes | `list.size()`, `list.isEmpty()` |
| **Clear** | ✅ Yes | `list.clear()` |
| **Remove** | ✅ Yes | `list.remove(0)`, `list.remove(obj)` |
| **Contains** | ✅ Yes | `list.contains(obj)` |
| **Add** | ❌ No (except `null`) | `list.add(null)` only |
| **Set** | ❌ No | Cannot replace elements |
| **Type-specific methods** | ❌ No | Cannot call `String` methods on elements |
| **Instanceof** | ❌ No (with generics) | Only raw type: `instanceof List` |

## 🚀 **Next Steps**

Unbounded wildcards give us maximum flexibility but with significant restrictions. For cases where you need to write to a collection, you need the opposite approach. In [Chapter 12](#12-lower-bounded-wildcards), we'll explore **Lower Bounded Wildcards** (`? super T`) - the "consumer" wildcards that let you add elements to generic structures safely.

> 💡 **Practice Exercise**: 
> 1. Create a `CollectionUtils` class with methods that only use unbounded wildcards
> 2. Implement methods to find the maximum element in any collection (using `Comparable`)
> 3. Write a method that takes two `Collection<?>` and returns true if they have the same elements (order doesn't matter)
> 4. Create a `WildcardCaptureDemo` that shows how to work around wildcard capture limitations
> 5. Benchmark the performance of operations on `Collection<?>` vs typed collection