# Chapter 5: 🔗 **Bounded Type Parameters**

## Constraining Generics: Adding Bounds to Type Parameters

Bounded type parameters allow you to restrict the types that can be used as type arguments, enabling you to safely call methods defined in the bound. This is where generics become truly powerful, moving beyond simple containers to type-safe algorithms.

## 🔒 **Basic Bounded Type Parameters**

### The `extends` Keyword

```java
// T must be a subclass of Number (or Number itself)
public class NumericBox<T extends Number> {
    private T number;
    
    public NumericBox(T number) {
        this.number = number;
    }
    
    // Can safely call Number methods on T
    public double doubleValue() {
        return number.doubleValue();
    }
    
    public int intValue() {
        return number.intValue();
    }
    
    // Type-safe arithmetic operations
    public <U extends Number> double sum(U other) {
        return this.number.doubleValue() + other.doubleValue();
    }
}

// Usage
NumericBox<Integer> intBox = new NumericBox<>(42);
NumericBox<Double> doubleBox = new NumericBox<>(3.14);
// NumericBox<String> stringBox = new NumericBox<>("test"); // Compile error!
```

### Multiple Bounds with `&`

```java
// T must implement both Comparable AND Serializable
public class SerializableComparable<T extends Comparable<T> & Serializable> {
    private T value;
    
    public SerializableComparable(T value) {
        this.value = value;
    }
    
    public int compareTo(SerializableComparable<T> other) {
        return this.value.compareTo(other.value);
    }
    
    // Can serialize because T implements Serializable
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(value);
        }
    }
}

// Valid: String implements Comparable<String> and Serializable
SerializableComparable<String> stringComp = 
    new SerializableComparable<>("Hello");

// Valid: Integer implements Comparable<Integer> and Serializable
SerializableComparable<Integer> intComp = 
    new SerializableComparable<>(123);

// Invalid: SomeClass doesn't implement Serializable
class SomeClass implements Comparable<SomeClass> {
    @Override public int compareTo(SomeClass o) { return 0; }
}
// SerializableComparable<SomeClass> invalid = ... // Compile error!
```

## 🎭 **Class vs Interface Bounds**

### Mixing Class and Interface Bounds

```java
public class ComplexBound<T extends Number & Comparable<T> & Serializable> {
    private T value;
    
    public ComplexBound(T value) {
        this.value = value;
    }
    
    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
    
    public T add(T other) {
        // Note: Number doesn't have add(), so we handle types individually
        if (value instanceof Integer && other instanceof Integer) {
            @SuppressWarnings("unchecked")
            T result = (T) Integer.valueOf(
                value.intValue() + other.intValue());
            return result;
        }
        // Similar for other Number types
        return null;
    }
}

// Works because Integer extends Number AND implements Comparable<Integer> AND Serializable
ComplexBound<Integer> boundInt = new ComplexBound<>(10);

// Works for Double, Float, Long, etc.
ComplexBound<Double> boundDouble = new ComplexBound<>(3.14);
```

### Important Rules for Multiple Bounds

```java
public class BoundsRules {
    // Rule 1: Class must come first
    // ❌ Wrong: <T extends Serializable & Number> 
    // ✅ Correct: <T extends Number & Serializable>
    
    // Rule 2: Only one class allowed
    // ❌ Wrong: <T extends Number & String>
    // ✅ Correct: <T extends Number & Comparable<T>>
    
    // Rule 3: Interfaces can be multiple
    // ✅ Correct: <T extends Number & Comparable<T> & Serializable & Cloneable>
    
    // Valid example following all rules
    public static class ValidBounds<T extends Number & Comparable<T> & Serializable> {
        // T is a Number subclass that's comparable and serializable
    }
}
```

## 🛠️ **Practical Applications**

### 1. **Type-Safe Collections with Bounds**

```java
public class Statistics<T extends Number> {
    private List<T> numbers;
    
    public Statistics(List<T> numbers) {
        this.numbers = numbers;
    }
    
    public double average() {
        double sum = 0.0;
        for (T num : numbers) {
            sum += num.doubleValue();  // Safe because T extends Number
        }
        return numbers.isEmpty() ? 0 : sum / numbers.size();
    }
    
    public T max() {
        if (numbers.isEmpty()) {
            return null;
        }
        
        T max = numbers.get(0);
        for (T num : numbers) {
            // Still need Comparable for comparison
            // This shows limitation of simple Number bound
        }
        return max;
    }
    
    // Better: Use Comparable bound
    public static <T extends Number & Comparable<T>> T findMax(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        
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

### 2. **Generic Repository Pattern**

```java
public interface Entity {
    Long getId();
}

public interface Repository<T extends Entity> {
    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void delete(T entity);
    
    // Generic method with additional bound
    <U extends T & Serializable> void saveSerializable(U entity);
}

// Concrete implementation
public class User implements Entity, Serializable {
    private Long id;
    private String name;
    
    @Override public Long getId() { return id; }
    // ... other methods
}

public class UserRepository implements Repository<User> {
    @Override
    public User findById(Long id) { /* implementation */ }
    
    @Override
    public List<User> findAll() { /* implementation */ }
    
    @Override
    public void save(User entity) { /* implementation */ }
    
    @Override
    public void delete(User entity) { /* implementation */ }
    
    @Override
    public <U extends User & Serializable> void saveSerializable(U entity) {
        // Can serialize because U implements Serializable
    }
}
```

### 3. **Event System with Bounded Types**

```java
public abstract class Event {
    private final long timestamp;
    
    protected Event() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}

public interface EventHandler<T extends Event> {
    void handle(T event);
}

public class EventDispatcher {
    private final Map<Class<? extends Event>, List<EventHandler<?>>> handlers;
    
    public EventDispatcher() {
        handlers = new HashMap<>();
    }
    
    public <T extends Event> void registerHandler(
            Class<T> eventType, 
            EventHandler<T> handler) {
        
        handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(handler);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Event> void dispatch(T event) {
        List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers != null) {
            for (EventHandler<?> handler : eventHandlers) {
                ((EventHandler<T>) handler).handle(event);
            }
        }
    }
}

// Usage
class UserLoggedInEvent extends Event {
    private final String username;
    
    public UserLoggedInEvent(String username) {
        this.username = username;
    }
    
    public String getUsername() { return username; }
}

class UserLoggedInHandler implements EventHandler<UserLoggedInEvent> {
    @Override
    public void handle(UserLoggedInEvent event) {
        System.out.println("User logged in: " + event.getUsername());
    }
}
```

## 🔄 **Recursive Type Bounds**

### Self-Referential Bounds

```java
// Classic example: Comparable interface
public interface Comparable<T> {
    int compareTo(T other);
}

// Using recursive bounds
public class SortedList<T extends Comparable<T>> {
    private List<T> items = new ArrayList<>();
    
    public void add(T item) {
        int index = 0;
        while (index < items.size() && 
               items.get(index).compareTo(item) < 0) {
            index++;
        }
        items.add(index, item);
    }
    
    public T get(int index) {
        return items.get(index);
    }
    
    // Binary search using recursive bound
    public boolean contains(T target) {
        int left = 0;
        int right = items.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            T midVal = items.get(mid);
            int cmp = midVal.compareTo(target);
            
            if (cmp == 0) return true;
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        
        return false;
    }
}

// Tree node with recursive bound
public class TreeNode<T extends Comparable<T>> {
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;
    
    public TreeNode(T data) {
        this.data = data;
    }
    
    public void insert(T value) {
        int cmp = value.compareTo(data);
        if (cmp < 0) {
            if (left == null) {
                left = new TreeNode<>(value);
            } else {
                left.insert(value);
            }
        } else if (cmp > 0) {
            if (right == null) {
                right = new TreeNode<>(value);
            } else {
                right.insert(value);
            }
        }
    }
}
```

## ⚠️ **Limitations and Workarounds**

### 1. **Cannot Use Primitive Types**

```java
// ❌ This doesn't work with primitives
// Statistics<int> intStats = new Statistics<>(); // Compile error

// ✅ Workaround: Use wrapper types
Statistics<Integer> intStats = new Statistics<>(Arrays.asList(1, 2, 3));

// ✅ Or use specialized implementations
public class IntStatistics {
    private List<Integer> numbers;
    // Specialized implementation for performance
}
```

### 2. **No Arithmetic Operations on T**

```java
public class Calculator<T extends Number> {
    private T value;
    
    // ❌ Cannot do arithmetic directly
    // public T add(T other) {
    //     return this.value + other;  // Compile error
    // }
    
    // ✅ Workaround: Use doubleValue() and create new instance
    public double addAsDouble(T other) {
        return value.doubleValue() + other.doubleValue();
    }
    
    // ✅ Better: Use Number subtypes
    public static <T extends Number> Number add(T a, T b) {
        if (a instanceof Integer && b instanceof Integer) {
            return a.intValue() + b.intValue();
        } else if (a instanceof Double && b instanceof Double) {
            return a.doubleValue() + b.doubleValue();
        }
        // Handle other types...
        return a.doubleValue() + b.doubleValue();
    }
}
```

### 3. **Array Creation Limitations**

```java
public class ArrayUtils<T extends Number> {
    // ❌ Cannot create generic array
    // T[] createArray(int size) {
    //     return new T[size];  // Compile error
    // }
    
    // ✅ Workaround: Use Array.newInstance with Class<T>
    @SuppressWarnings("unchecked")
    public T[] createArray(int size, Class<T> clazz) {
        return (T[]) Array.newInstance(clazz, size);
    }
    
    // ✅ Alternative: Accept array from caller
    public void processArray(T[] array) {
        // Process the array
    }
}
```

## 🧪 **Advanced Pattern: Builder with Bounds**

```java
public abstract class Animal {
    private final String name;
    
    protected Animal(String name) {
        this.name = name;
    }
    
    public abstract String makeSound();
    
    public String getName() { return name; }
}

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public String makeSound() {
        return "Woof!";
    }
}

public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }
    
    @Override
    public String makeSound() {
        return "Meow!";
    }
}

// Generic builder with recursive bound
public class AnimalBuilder<T extends AnimalBuilder<T>> {
    protected String name;
    
    @SuppressWarnings("unchecked")
    public T name(String name) {
        this.name = name;
        return (T) this;
    }
    
    public Animal build() {
        return new Animal(name) {
            @Override
            public String makeSound() {
                return "Generic animal sound";
            }
        };
    }
}

// Specialized builders
public class DogBuilder extends AnimalBuilder<DogBuilder> {
    private String breed;
    
    public DogBuilder breed(String breed) {
        this.breed = breed;
        return this;
    }
    
    @Override
    public Dog build() {
        return new Dog(name);
    }
}

public class CatBuilder extends AnimalBuilder<CatBuilder> {
    private boolean hasClaws = true;
    
    public CatBuilder hasClaws(boolean hasClaws) {
        this.hasClaws = hasClaws;
        return this;
    }
    
    @Override
    public Cat build() {
        return new Cat(name);
    }
}

// Usage
Dog dog = new DogBuilder()
    .name("Buddy")
    .breed("Golden Retriever")
    .build();

Cat cat = new CatBuilder()
    .name("Whiskers")
    .hasClaws(false)
    .build();
```

## 📊 **Performance Considerations**

### Boxing vs Specialization

```java
public class PerformanceDemo {
    // Generic approach (boxing overhead)
    public static <T extends Number> double sumGeneric(List<T> numbers) {
        double total = 0.0;
        for (T num : numbers) {
            total += num.doubleValue();  // Boxing overhead for primitives
        }
        return total;
    }
    
    // Specialized approach (better performance)
    public static double sumInts(int[] numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;  // No boxing
        }
        return total;
    }
    
    // Modern Java: Use specialized streams
    public static double sumModern(List<Integer> numbers) {
        return numbers.stream()
            .mapToInt(Integer::intValue)  // Converts to IntStream
            .sum();
    }
}
```

## 🎓 **Best Practices**

1. **Use the most general bound possible** to maximize reusability
2. **Consider using wildcards** (`? extends T`) instead of bounds when you only need read access
3. **Document the requirements** of bounded type parameters in Javadoc
4. **Test with edge cases** - what happens with `null`, empty collections, boundary values?
5. **Consider performance** - bounds don't affect runtime, but boxing might
6. **Use `@SuppressWarnings` judiciously** when you know casts are safe

## 🔧 **Debugging Tips**

```java
public class BoundsDebugging {
    // Common error: Forgetting that T extends Object by default
    public static <T> void process(T item) {
        // T is actually T extends Object here
    }
    
    // Use Class<T> for runtime type information
    public static <T extends Number> void debugType(T item, Class<T> clazz) {
        System.out.println("Type: " + clazz.getName());
        System.out.println("Value: " + item);
        System.out.println("Double value: " + item.doubleValue());
    }
    
    // Check bounds at runtime (when needed)
    public static <T> void safeProcess(T item) {
        if (item instanceof Number) {
            Number num = (Number) item;
            System.out.println("Number value: " + num.doubleValue());
        } else {
            System.out.println("Not a number: " + item);
        }
    }
}
```

## 🚀 **Next Steps**

Now that you understand how to constrain type parameters, let's combine this knowledge with generic methods. In [Chapter 6](#6-generic-methods--bounded-types), we'll explore **Generic Methods + Bounded Types** - creating powerful, flexible algorithms that work within type hierarchies while maintaining compile-time safety.

> 💡 **Practice Exercise**: Create a `Range<T extends Comparable<T>>` class that represents a range between two values. Add methods to check if a value is within the range, to check if two ranges overlap, and to merge overlapping ranges. Then create a `Statistics<T extends Number & Comparable<T>>` class that can find min, max, median, and quartiles.