# 🚫 Chapter:21. Restrictions on Generics

> 📋 *The complete list of what you can't do with Java generics—and why*

Java generics come with several intentional restrictions to maintain type safety, ensure backward compatibility, and work within the constraints of type erasure. Understanding these limitations helps you write better code and avoid common pitfalls.

## 📋 **The Complete "Can't Do" List**

### 1. ❌ **No Primitive Type Parameters**

```java
// These are ALL ILLEGAL:
// List<int> primitiveList;                    // ❌ Compile error
// Map<double, String> primitiveKeyMap;       // ❌ Compile error
// Optional<boolean> primitiveOptional;       // ❌ Compile error

// Instead, use wrapper classes:
List<Integer> integerList;                    // ✅
Map<Double, String> doubleKeyMap;            // ✅
Optional<Boolean> booleanOptional;           // ✅

// Auto-boxing makes this manageable:
List<Integer> numbers = new ArrayList<>();
numbers.add(42);                             // Auto-boxing: int → Integer
int value = numbers.get(0);                  // Auto-unboxing: Integer → int
```

**Why?** Generics are implemented using type erasure, which replaces type parameters with `Object`. Since primitives don't inherit from `Object`, they can't be used. This also ensures backward compatibility with pre-generics code.

**Workaround:** Use wrapper classes and rely on auto-boxing/unboxing.

### 2. ❌ **No Instantiation of Type Parameters**

```java
class Container<T> {
    // These are ILLEGAL:
    // T instance = new T();                     // ❌ Cannot instantiate T
    // T[] array = new T[10];                   // ❌ Cannot create array of T
    
    // Common workarounds:
    
    // 1. Pass a Class<T> and use reflection
    private Class<T> clazz;
    
    public Container(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    public T createInstance() throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }
    
    // 2. Use a Supplier<T>
    private Supplier<T> supplier;
    
    public Container(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T createWithSupplier() {
        return supplier.get();
    }
    
    // 3. For arrays, use Array.newInstance()
    @SuppressWarnings("unchecked")
    public T[] createArray(int size) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, size);
    }
}

// Usage examples:
Container<String> stringContainer = new Container<>(String.class);
Container<String> stringSupplierContainer = new Container<>(() -> "default");
```

**Why?** At runtime, `T` is erased to `Object`, so `new Object()` wouldn't be what you want. Also, `T` might not have a no-arg constructor.

### 3. ❌ **No Static Fields of Type `T`**

```java
class Shared<T> {
    // These are ILLEGAL:
    // private static T sharedInstance;          // ❌ Cannot have static field of type T
    // private static List<T> sharedList;        // ❌ Same restriction
    
    // These are OK:
    private static int count = 0;               // ✅ Non-generic static field
    private static final Object lock = new Object(); // ✅ Non-generic
    
    // This is also OK but probably not what you want:
    private static List<?> sharedWildcardList;  // ✅ But loses type safety
}

// Why this makes sense:
Shared<String> stringShared = new Shared<>();
Shared<Integer> integerShared = new Shared<>();
// If static T field were allowed, what type would it be? String or Integer?
```

**Why?** Static fields are shared across all instances of the class. If `Shared<String>` and `Shared<Integer>` shared the same static field of type `T`, it would create type conflicts.

### 4. ❌ **No `instanceof` with Parameterized Types**

```java
public void process(Object obj) {
    // These are ILLEGAL:
    // if (obj instanceof List<String>) { }          // ❌ Compile error
    // if (obj instanceof Map<Integer, String>) { }  // ❌ Compile error
    
    // Legal alternatives:
    if (obj instanceof List) {                      // ✅ Raw type
        // Need to cast and handle with care
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) obj;
    }
    
    if (obj instanceof List<?>) {                   // ✅ Unbounded wildcard
        // Can only read as Object
        List<?> list = (List<?>) obj;
        Object first = list.get(0);
    }
}

// Type pattern matching (Java 16+)
public void processPattern(Object obj) {
    if (obj instanceof List<?> list) {              // ✅ Pattern matching
        // list is of type List<?>
    }
}
```

**Why?** Due to type erasure, `List<String>` and `List<Integer>` have the same runtime type (`List`). The JVM can't distinguish them.

### 5. ❌ **No Arrays of Parameterized Types**

```java
// These are ILLEGAL:
// List<String>[] stringLists = new List<String>[10];  // ❌ Compile error
// Map<Integer, String>[] mapArray = new Map<>[5];     // ❌ Compile error

// Legal alternatives:
List<?>[] wildcardLists = new List<?>[10];           // ✅ Array of unbounded wildcard
List[] rawLists = new List[10];                      // ✅ Array of raw types (unsafe)

// Better: Use collections instead
List<List<String>> listOfLists = new ArrayList<>();  // ✅ Much safer

// Workaround with @SuppressWarnings
@SuppressWarnings("unchecked")
List<String>[] createStringListArray(int size) {
    return (List<String>[]) new List[size];          // ✅ But generates warning
}
```

**Why?** Arrays are covariant and reifiable, while generics are invariant and non-reifiable. Mixing them would break type safety.

### 6. ❌ **No Generic Exception Classes**

```java
// These are ILLEGAL:
// class GenericException<T> extends Exception { }    // ❌ Compile error
// class GenericThrowable<T> extends Throwable { }    // ❌ Compile error

// But you CAN have generic fields in exceptions:
class ContainerException extends Exception {
    private final Object value;                      // ✅ Generic field (as Object)
    
    public <T> ContainerException(T value) {
        this.value = value;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;                            // ⚠️ Unsafe cast
    }
}

// Better: Use a specific type or Class parameter
class TypedException<T> extends Exception {
    private final T value;
    private final Class<T> type;
    
    public TypedException(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }
    
    public T getValue() {
        return value;
    }
}
```

**Why?** The Java exception handling mechanism relies on the runtime type of exceptions. Generic exceptions would be erased, making catch blocks unreliable.

### 7. ❌ **No Overloading with Same Erasure**

```java
class Overloader {
    // These methods CONFLICT - same erasure:
    // public void process(List<String> list) { }    // ❌ Both erase to process(List)
    // public void process(List<Integer> list) { }   // ❌ Compile error
    
    // These are OK - different erasure:
    public void process(List<String> list) { }       // ✅ Erases to process(List)
    public void process(Set<String> set) { }         // ✅ Erases to process(Set)
    
    // This is also OK (not generic):
    public void process(String str) { }              // ✅ Different parameter type
}
```

**Why?** After type erasure, both methods would have the same signature (`process(List)`), making them indistinguishable at runtime.

### 8. ❌ **Cannot Override with Different Type Parameter**

```java
class Base {
    public void process(List<String> list) { }
}

class Derived extends Base {
    // This is NOT overriding - it's overloading:
    // @Override  // This annotation would cause compile error
    public void process(List<Integer> list) { }      // ❌ Different type parameter
    
    // This WOULD override:
    // @Override
    // public void process(List<String> list) { }    // ✅ Same type parameter
}
```

**Why?** `List<String>` and `List<Integer>` are different types, not subtypes of each other.

### 9. ❌ **Cannot Use Generics in Static Context of Generic Class**

```java
class Utility<T> {
    // These are ILLEGAL:
    // public static T getDefault() { return null; }   // ❌ Static method cannot use T
    // public static List<T> createList() { return new ArrayList<>(); } // ❌
    
    // These are OK:
    public static <U> U getDefault(Class<U> clazz) {   // ✅ Different type parameter
        return null;
    }
    
    public static <U> List<U> createList() {           // ✅ Different type parameter
        return new ArrayList<>();
    }
}
```

**Why?** Static members don't have access to instance type parameters because they're shared across all instantiations.

### 10. ❌ **Cannot Create Catch Block for Generic Exception**

```java
class ExceptionHandler {
    public <T extends Exception> void handle() {
        try {
            // Some code that might throw T
        } 
        // These are ILLEGAL:
        // catch (T e) { }                          // ❌ Cannot catch type parameter
        // catch (Exception<T> e) { }               // ❌ Generic exception class not allowed
        
        // But you can do this:
        catch (Exception e) {
            if (e instanceof RuntimeException) {
                // Handle
            }
        }
    }
}
```

### 11. ❌ **Cannot Use `Class` Literals with Parameterized Types**

```java
// These are ILLEGAL:
// Class<List<String>> stringListClass = List<String>.class;  // ❌ Compile error
// Class<Map<Integer, String>> mapClass = Map<Integer, String>.class; // ❌

// Legal alternatives:
Class<List> rawListClass = List.class;                      // ✅ Raw type
Class<?> wildcardListClass = List<?>.class;                 // ✅ Unbounded wildcard

// Type token pattern (as seen earlier)
abstract class TypeToken<T> {
    private final Type type;
    
    protected TypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
    
    public Type getType() { return type; }
}
```

### 12. ❌ **Cannot Use Generics in Enum Declarations**

```java
// This is ILLEGAL:
// enum Color<T> { RED, GREEN, BLUE }              // ❌ Compile error

// But you CAN have generic methods in enums:
enum Color {
    RED, GREEN, BLUE;
    
    public <T> T process(T input) {                // ✅ Generic method
        return input;
    }
}
```

**Why?** Enums are meant to represent a fixed set of values. Generic enums would complicate this model.

## 🛠️ **Workarounds and Patterns**

### Pattern 1: **Factory Method with Class Parameter**

```java
class GenericFactory<T> {
    private final Class<T> type;
    private final Supplier<T> supplier;
    
    public GenericFactory(Class<T> type) {
        this.type = type;
        this.supplier = null;
    }
    
    public GenericFactory(Supplier<T> supplier) {
        this.type = null;
        this.supplier = supplier;
    }
    
    public T createInstance() throws Exception {
        if (supplier != null) {
            return supplier.get();
        }
        return type.getDeclaredConstructor().newInstance();
    }
    
    @SuppressWarnings("unchecked")
    public T[] createArray(int size) {
        if (type != null) {
            return (T[]) Array.newInstance(type, size);
        }
        throw new IllegalStateException("No type information available");
    }
}
```

### Pattern 2: **Type-Safe Heterogeneous Container**

```java
class TypeSafeContainer {
    private final Map<Class<?>, Object> container = new HashMap<>();
    
    public <T> void put(Class<T> type, T instance) {
        container.put(type, type.cast(instance));
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return type.cast(container.get(type));
    }
    
    public static void main(String[] args) {
        TypeSafeContainer container = new TypeSafeContainer();
        container.put(String.class, "Hello");
        container.put(Integer.class, 42);
        
        String str = container.get(String.class);  // "Hello"
        Integer num = container.get(Integer.class); // 42
    }
}
```

### Pattern 3: **Super Type Token**

```java
abstract class SuperTypeToken<T> {
    private final Type type;
    
    protected SuperTypeToken() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
    
    public Type getType() { return type; }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof SuperTypeToken && 
               ((SuperTypeToken<?>) o).type.equals(type);
    }
    
    @Override
    public int hashCode() { return type.hashCode(); }
}

// Usage:
SuperTypeToken<List<String>> token = new SuperTypeToken<List<String>>() {};
System.out.println(token.getType());  // java.util.List<java.lang.String>
```

## 📊 **Restrictions Summary Table**

| Restriction | Reason | Workaround |
|------------|--------|------------|
| No primitives | Type erasure uses `Object` | Use wrapper classes |
| No `new T()` | Erasure loses type info | Pass `Class<T>` or `Supplier<T>` |
| No static `T` fields | Shared across all instantiations | Use non-generic static fields |
| No `instanceof` with generics | Runtime type erasure | Use raw types or wildcards |
| No generic arrays | Type safety violation | Use collections or wildcard arrays |
| No generic exceptions | Catch block reliability | Use specific exception types |
| No overloading by erasure | Same method signature | Use different parameter types |
| No `Class<T>` literals | Non-reifiable types | Use type tokens |
| No generic enums | Fixed value set model | Use enum with generic methods |

## 🧪 **Testing Generic Code Within Restrictions**

```java
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GenericRestrictionsTest {
    
    @Test
    void testNoPrimitiveTypeParameters() {
        // This test verifies we use wrapper classes correctly
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        
        assertEquals(2, numbers.size());
        assertEquals(1, numbers.get(0).intValue());  // Auto-unboxing
    }
    
    @Test
    void testFactoryPattern() throws Exception {
        GenericFactory<String> factory = new GenericFactory<>(String.class);
        String instance = factory.createInstance();
        
        assertNotNull(instance);
        assertEquals("", instance);  // Default String constructor
    }
    
    @Test
    void testTypeSafeContainer() {
        TypeSafeContainer container = new TypeSafeContainer();
        container.put(String.class, "test");
        container.put(Integer.class, 123);
        
        assertEquals("test", container.get(String.class));
        assertEquals(123, container.get(Integer.class));
    }
    
    @Test
    void testArrayAlternative() {
        // Instead of T[], use List<T>
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(Arrays.asList("a", "b"));
        matrix.add(Arrays.asList("c", "d"));
        
        assertEquals(2, matrix.size());
        assertEquals("a", matrix.get(0).get(0));
    }
}
```

## 🎯 **Key Takeaways**

1. **Generics have limits by design** to ensure type safety and compatibility
2. **Type erasure is the root cause** of most restrictions
3. **Workarounds exist** for common patterns (factories, containers, tokens)
4. **Collections are often better** than arrays for generic data structures
5. **Runtime type information** can be preserved with careful design
6. **Testing is crucial** for verifying type safety in generic code

## 🚀 **Best Practices Summary**

1. **Accept the limitations**—they're there for good reasons
2. **Use wrapper classes** for primitive types
3. **Pass `Class<T>` or `Supplier<T>`** when you need instantiation
4. **Prefer collections over arrays** for generic structures
5. **Use type tokens** when you need runtime type information
6. **Avoid raw types** except in `instanceof` checks
7. **Add `@SuppressWarnings` judiciously** with explanatory comments
8. **Test thoroughly** to catch type safety issues

Understanding these restrictions is just as important as understanding what you *can* do with generics. By working within these boundaries and using established patterns, you can write robust, type-safe generic code that leverages Java's type system effectively.