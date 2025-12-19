# Chapter 2: 🧬 **Generic Types**

## Foundations of Generic Programming

Generic types allow you to define classes, interfaces, and methods that operate on types specified by parameters. This is the core concept that enables type-safe, reusable code components.

## 🏗️ **Creating Generic Classes**

### Basic Generic Class Structure

```java
// T is a type parameter (placeholder for actual type)
public class Box<T> {
    private T content;
    
    public void setContent(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
    
    // Generic toString using type parameter
    @Override
    public String toString() {
        return "Box containing: " + content;
    }
}
```

### Multiple Type Parameters

```java
// Two type parameters: K for Key, V for Value
public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
    
    public void setKey(K key) { this.key = key; }
    public void setValue(V value) { this.value = value; }
}
```

## 📛 **Type Parameter Conventions**

Java community follows naming conventions for type parameters:

| Convention | Typical Use | Example |
|------------|-------------|---------|
| **T** | Type (most common) | `Box<T>`, `List<T>` |
| **E** | Element (collections) | `Collection<E>`, `List<E>` |
| **K** | Key (maps) | `Map<K, V>`, `HashMap<K, V>` |
| **V** | Value (maps) | `Map<K, V>`, `Entry<K, V>` |
| **N** | Number | `Calculator<N extends Number>` |
| **R** | Return type | `Function<T, R>`, `Supplier<R>` |
| **U, S** | Additional types | `Pair<T, U>`, `Triplet<T, U, S>` |

### Good Practice Examples

```java
// Clear, conventional naming
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
}

public class TreeNode<T> {
    private T data;
    private List<TreeNode<T>> children;
    
    // Constructor, getters, setters...
}

// Multiple parameters with meaningful names
public class BiFunction<ARG1, ARG2, RESULT> {
    public RESULT apply(ARG1 arg1, ARG2 arg2) {
        // Implementation
    }
}
```

## 📝 **Declaring and Using Parameterized Types**

### Instantiation Examples

```java
// Single type parameter
Box<String> stringBox = new Box<>();
stringBox.setContent("Hello");
String content = stringBox.getContent();  // Type-safe retrieval

// Multiple type parameters
Pair<String, Integer> nameAge = new Pair<>("Alice", 30);
String name = nameAge.getKey();      // Returns String
Integer age = nameAge.getValue();    // Returns Integer

// Nested generics
List<Box<Integer>> numberBoxes = new ArrayList<>();
Box<Integer> intBox = new Box<>();
intBox.setContent(42);
numberBoxes.add(intBox);
```

### Real-World Example: Configuration Holder

```java
public class Configuration<T> {
    private String key;
    private T value;
    private Class<T> type;
    private T defaultValue;
    
    public Configuration(String key, Class<T> type, T defaultValue) {
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }
    
    public void setValue(T value) {
        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Invalid type");
        }
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public Class<T> getType() {
        return type;
    }
}

// Usage
Configuration<Integer> maxConnections = 
    new Configuration<>("db.max.connections", Integer.class, 10);
Configuration<String> dbUrl = 
    new Configuration<>("db.url", String.class, "localhost:5432");
```

## 🏭 **Generic Interfaces**

### Defining Generic Interfaces

```java
// Basic generic interface
public interface Repository<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void delete(T entity);
}

// Interface with multiple type parameters
public interface Converter<S, T> {
    T convert(S source);
    S reverseConvert(T target);
}

// Extending generic interfaces
public interface CrudRepository<T, ID> extends Repository<T> {
    boolean existsById(ID id);
    long count();
}
```

### Implementing Generic Interfaces

```java
// Concrete implementation with specific type
public class UserRepository implements Repository<User> {
    @Override
    public User findById(Long id) {
        // Database lookup
        return new User(id, "John Doe");
    }
    
    @Override
    public List<User> findAll() {
        return List.of(new User(1L, "Alice"), new User(2L, "Bob"));
    }
    
    @Override
    public void save(User entity) {
        // Save to database
    }
    
    @Override
    public void delete(User entity) {
        // Delete from database
    }
}

// Generic implementation
public class InMemoryRepository<T> implements Repository<T> {
    private Map<Long, T> storage = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);
    
    @Override
    public T findById(Long id) {
        return storage.get(id);
    }
    
    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public void save(T entity) {
        Long id = idGenerator.getAndIncrement();
        // In real scenario, entity would have getId()/setId() methods
        storage.put(id, entity);
    }
    
    @Override
    public void delete(T entity) {
        // Implementation
    }
}
```

## 🎯 **Type Parameter Scope**

### Class vs Method Type Parameters

```java
public class Container<T> {  // Class-level type parameter
    private T item;
    
    // Method using class type parameter
    public void setItem(T item) {
        this.item = item;
    }
    
    // Generic method with its own type parameter (different from class T)
    public <U> Container<U> transform(Function<T, U> transformer) {
        Container<U> newContainer = new Container<>();
        newContainer.setItem(transformer.apply(this.item));
        return newContainer;
    }
    
    // Static method cannot use class type parameter
    // This is WRONG: public static T createDefault() { ... }
    
    // Static generic method with its own type parameter
    public static <V> Container<V> createWithDefault(V defaultValue) {
        Container<V> container = new Container<>();
        container.setItem(defaultValue);
        return container;
    }
}
```

## 🔄 **Recursive Type Bounds**

### Self-Referential Types

```java
// Comparable interface uses recursive type bound
public interface Comparable<T> {
    int compareTo(T other);
}

// Implementing Comparable
public class Product implements Comparable<Product> {
    private String name;
    private double price;
    
    @Override
    public int compareTo(Product other) {
        return Double.compare(this.price, other.price);
    }
}

// Recursive generic structure
public class TreeNode<T extends Comparable<T>> {
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;
    
    public void insert(T value) {
        if (data.compareTo(value) > 0) {
            // Insert in left subtree
        } else {
            // Insert in right subtree
        }
    }
}
```

## 🧩 **Complex Generic Structures**

### Generic Builder Pattern

```java
public class QueryBuilder<T> {
    private Class<T> entityClass;
    private List<String> selectFields = new ArrayList<>();
    private Map<String, Object> whereConditions = new HashMap<>();
    
    public QueryBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public QueryBuilder<T> select(String... fields) {
        selectFields.addAll(Arrays.asList(fields));
        return this;
    }
    
    public QueryBuilder<T> where(String field, Object value) {
        whereConditions.put(field, value);
        return this;
    }
    
    public List<T> execute() {
        // Build and execute SQL query
        // Return typed results
        return new ArrayList<>();
    }
}

// Usage
List<User> users = new QueryBuilder<>(User.class)
    .select("id", "name", "email")
    .where("active", true)
    .where("age", 25)
    .execute();
```

### Generic Event System

```java
// Event interface
public interface Event<T> {
    T getSource();
    long getTimestamp();
}

// Generic event handler
public interface EventHandler<T extends Event<?>> {
    void handle(T event);
    Class<T> getEventType();
}

// Event dispatcher using generics
public class EventDispatcher {
    private Map<Class<?>, List<EventHandler<?>>> handlers = new HashMap<>();
    
    public <T extends Event<?>> void registerHandler(
            Class<T> eventType, 
            EventHandler<T> handler) {
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(handler);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Event<?>> void dispatch(T event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : eventHandlers) {
                ((EventHandler<T>) handler).handle(event);
            }
        }
    }
}
```

## ⚠️ **Common Pitfalls and Best Practices**

### 1. **Don't Use Raw Types**
```java
// ❌ Bad - Raw type
Box rawBox = new Box();
rawBox.setContent("String");  // No type safety
Object content = rawBox.getContent();  // Need to cast

// ✅ Good - Parameterized type
Box<String> typedBox = new Box<>();
typedBox.setContent("String");
String content = typedBox.getContent();  // Type-safe
```

### 2. **Avoid Unnecessary Generics**
```java
// ❌ Over-engineered
public class StringContainer {  // Simple class doesn't need generics
    private String value;
    // ... 
}

// ✅ Appropriate use of generics
public class Container<T> {  // Will be reused for different types
    private T value;
    // ...
}
```

### 3. **Document Generic Constraints**
```java
/**
 * Cache implementation with time-based expiration.
 * 
 * @param <K> The type of keys maintained by this cache
 * @param <V> The type of mapped values
 */
public class ExpiringCache<K, V> {
    private final Map<K, CacheEntry<V>> storage = new HashMap<>();
    
    private static class CacheEntry<V> {
        V value;
        long expiryTime;
        
        CacheEntry(V value, long ttl) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttl;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
    
    public void put(K key, V value, long ttlMillis) {
        storage.put(key, new CacheEntry<>(value, ttlMillis));
    }
    
    public V get(K key) {
        CacheEntry<V> entry = storage.get(key);
        if (entry == null || entry.isExpired()) {
            storage.remove(key);
            return null;
        }
        return entry.value;
    }
}
```

## 🧪 **Practical Exercise: Generic Stack**

```java
public class Stack<T> {
    private List<T> elements = new ArrayList<>();
    private int capacity;
    
    public Stack(int capacity) {
        this.capacity = capacity;
    }
    
    public void push(T element) {
        if (elements.size() >= capacity) {
            throw new IllegalStateException("Stack is full");
        }
        elements.add(element);
    }
    
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elements.remove(elements.size() - 1);
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elements.get(elements.size() - 1);
    }
    
    public boolean isEmpty() {
        return elements.isEmpty();
    }
    
    public int size() {
        return elements.size();
    }
    
    // Generic method within generic class
    public <U> Stack<U> transform(Function<T, U> transformer) {
        Stack<U> newStack = new Stack<>(this.capacity);
        for (T element : this.elements) {
            newStack.push(transformer.apply(element));
        }
        return newStack;
    }
}

// Usage
Stack<Integer> intStack = new Stack<>(10);
intStack.push(1);
intStack.push(2);
intStack.push(3);

Stack<String> stringStack = intStack.transform(Object::toString);
System.out.println(stringStack.pop());  // "3"
```

## 📊 **Type Parameter vs Wildcard**

Understanding when to use type parameters (`<T>`) versus wildcards (`<?>`) is crucial:

```java
// Type parameter - when you need to refer to the type
public class Container<T> {
    private T item;
    public T getItem() { return item; }
}

// Wildcard - when you don't care about the specific type
public void processList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}
```

## 🎓 **Key Takeaways**

1. **Generic classes** use type parameters as placeholders (`Box<T>`)
2. **Follow naming conventions**: `T` for type, `E` for element, `K/V` for key/value
3. **Multiple type parameters** are separated by commas (`Pair<K, V>`)
4. **Generic interfaces** enable reusable contracts (`Repository<T>`)
5. **Type parameter scope**: Class-level vs method-level
6. **Avoid raw types** - they bypass type safety
7. **Document generic classes** thoroughly with Javadoc

## 🚀 **Next Steps**

Now that you understand how to create generic types, you might wonder about compatibility with pre-generics code. In [Chapter 3](#3-raw-types), we'll explore **Raw Types** - what they are, why they exist, and the risks of using them.

> 💡 **Practice Exercise**: Create a generic `LinkedList<T>` class with methods for adding, removing, and finding elements. Then create a `SortedLinkedList<T extends Comparable<T>>` that maintains elements in sorted order.