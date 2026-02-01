# 🚫 Chapter 5: Restrictions on Java Generics

## 📚 Table of Contents
1. [🎯 Introduction: Why Restrictions Exist](#introduction-why-restrictions-exist)
2. [🔢 Cannot Use Primitive Types](#cannot-use-primitive-types)
   - [🎁 Autoboxing to the Rescue](#autoboxing-to-the-rescue)
3. [🏗️ Cannot Create Instances of Type Parameters](#cannot-create-instances-of-type-parameters)
   - [🔮 Reflection Workaround](#reflection-workaround)
   - [🏭 Factory Pattern Solution](#factory-pattern-solution)
4. [🏛️ Cannot Declare Static Fields of Type Parameters](#cannot-declare-static-fields-of-type-parameters)
   - [⚡ The Static Context Problem](#the-static-context-problem)
5. [🔍 Cannot Use instanceof with Parameterized Types](#cannot-use-instanceof-with-parameterized-types)
   - [🎭 Type Erasure Strikes Again](#type-erasure-strikes-again)
   - [✅ What You CAN Check](#what-you-can-check)
6. [🎭 Cannot Cast Parameterized Types (Mostly)](#cannot-cast-parameterized-types-mostly)
   - [⚠️ Unchecked Cast Warnings](#unchecked-cast-warnings)
7. [📦 Cannot Create Arrays of Parameterized Types](#cannot-create-arrays-of-parameterized-types)
   - [💥 The Array Store Exception Problem](#the-array-store-exception-problem)
   - [🛠️ Workarounds for Generic Arrays](#workarounds-for-generic-arrays)
8. [🚫 Exception-Related Restrictions](#exception-related-restrictions)
   - [🎯 Cannot Extend Throwable](#cannot-extend-throwable)
   - [🎭 Cannot Catch Type Parameters](#cannot-catch-type-parameters)
   - [✅ Can Use in Throws Clause](#can-use-in-throws-clause)
9. [🔄 Cannot Overload with Same Erasure](#cannot-overload-with-same-erasure)
   - [🎭 The Method Signature Conflict](#the-method-signature-confolution)
10. [🔧 Other Minor Restrictions](#other-minor-restrictions)
11. [🎮 Practical Solutions and Patterns](#practical-solutions-and-patterns)

---

## 🎯 Introduction: Why Restrictions Exist

Java generics have **limitations** that might seem annoying at first, but they exist for good reasons! These restrictions mostly come from two things:

1. **Type Erasure** - Type information disappears at runtime 🔄
2. **Backward Compatibility** - Must work with pre-generics Java ⏪

Think of generics as a **compile-time safety net** 🎪 that catches errors early, but doesn't change the fundamental Java runtime!

---

## 🔢 Cannot Use Primitive Types

### The Problem:
You **cannot use primitive types** (`int`, `double`, `char`, etc.) as type arguments! ❌

```java
// COMPILE ERROR! ❌
Pair<int, char> primitivePair = new Pair<>(8, 'a');

// ERROR MESSAGE:
// Type argument cannot be of primitive type
```

### Why?
Generics work with **Objects** only, and primitives are not objects! Java's type system has this fundamental split:
- **Primitives**: `int`, `double`, `char`, `boolean`, etc. - not objects
- **Reference types**: Everything else - are objects

### 🎁 Autoboxing to the Rescue

Java automatically converts between primitives and their wrapper classes! This is called **autoboxing/unboxing**:

```java
// ✅ CORRECT: Use wrapper classes
Pair<Integer, Character> objectPair = new Pair<>(8, 'a');

// What really happens:
Pair<Integer, Character> objectPair = 
    new Pair<>(Integer.valueOf(8), Character.valueOf('a'));
```

### Complete Example:
```java
class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}

// Usage:
Pair<Integer, Character> pair = new Pair<>(42, 'X');
int key = pair.getKey();      // Auto-unboxing: Integer → int
char value = pair.getValue(); // Auto-unboxing: Character → char
```

**Note:** Autoboxing has a **performance cost** ⚡ (creating objects), but for most applications, it's negligible!

---

## 🏗️ Cannot Create Instances of Type Parameters

### The Problem:
You **cannot use `new` with type parameters**! ❌

```java
public class Container<T> {
    private T value;
    
    public Container() {
        // COMPILE ERROR! ❌
        this.value = new T();
        
        // Also errors:
        // T[] array = new T[10];      ❌
        // T.class                     ❌ (partially)
    }
}
```

### Why?
Because of **type erasure**! At runtime, `T` becomes `Object` (or its bound), so `new Object()` wouldn't be what you want!

### 🔮 Reflection Workaround

You **can** create instances using **reflection**, but you need the `Class` object:

```java
public class Container<T> {
    private T value;
    
    // Pass the Class object to know T at runtime
    public Container(Class<T> clazz) throws Exception {
        this.value = clazz.newInstance();  // ✅ Creates instance of T
    }
    
    // Java 8+ version with better exception handling
    public Container(Class<T> clazz) 
            throws NoSuchMethodException, IllegalAccessException, 
                   InvocationTargetException, InstantiationException {
        this.value = clazz.getDeclaredConstructor().newInstance();
    }
}

// Usage:
Container<String> container = new Container<>(String.class);
```

**Warning:** Reflection is **slower** ⏱️ and checked exceptions are annoying!

### 🏭 Factory Pattern Solution

A cleaner solution: Use a **factory interface**:

```java
// Factory interface
interface Factory<T> {
    T create();
}

// Container using factory
class Container<T> {
    private T value;
    
    public Container(Factory<T> factory) {
        this.value = factory.create();  // ✅
    }
}

// Usage with lambda (Java 8+):
Container<String> container = new Container<>(() -> "Default");
Container<ArrayList<String>> listContainer = 
    new Container<>(ArrayList::new);  // Method reference! 🎯
```

### Alternative: Supplier Interface (Java 8+)
```java
import java.util.function.Supplier;

class Container<T> {
    private T value;
    
    public Container(Supplier<T> supplier) {
        this.value = supplier.get();  // ✅ Clean and simple!
    }
}

// Usage:
Container<String> container = new Container<>(() -> "Hello");
Container<HashMap<String, Integer>> mapContainer = 
    new Container<>(HashMap::new);
```

---

## 🏛️ Cannot Declare Static Fields of Type Parameters

### The Problem:
**Static fields cannot have generic type parameters!** ❌

```java
class MobileDevice<T> {
    // COMPILE ERROR! ❌
    private static T operatingSystem;
    
    // Also errors:
    // private static List<T> allDevices; ❌
    // public static T getDefaultOS() { ... } ❌ (if returns T)
}
```

### ⚡ The Static Context Problem

**Why?** Static members belong to the **class**, not instances. But type parameters are per **instance**!

```java
MobileDevice<Smartphone> phone = new MobileDevice<>();
MobileDevice<Tablet> tablet = new MobileDevice<>();

// If static T os; were allowed:
// What type should MobileDevice.os be? 
// Smartphone? Tablet? Both? Impossible! 🤯
```

### What You CAN Do:

#### 1. Use Raw Type or Wildcard:
```java
class MobileDevice<T> {
    // ✅ OK: Raw type
    private static Object sharedData;
    
    // ✅ OK: Wildcard
    private static List<?> allDevices = new ArrayList<>();
    
    // ✅ OK: Non-generic type
    private static int deviceCount = 0;
}
```

#### 2. Generic Static Method (Different Type Parameter):
```java
class MobileDevice<T> {
    // ✅ OK: Static method with its OWN type parameter
    public static <U> U process(U input) {
        return input;
    }
    
    // This <U> is DIFFERENT from the class's <T>!
}
```

#### 3. Separate Generic and Non-Generic Parts:
```java
// Put static members in non-generic parent class
abstract class MobileDeviceBase {
    protected static String defaultOS = "Android";
    protected static int totalDevices = 0;
}

// Generic subclass
class MobileDevice<T> extends MobileDeviceBase {
    private T deviceSpecificData;
    // ...
}
```

---

## 🔍 Cannot Use instanceof with Parameterized Types

### 🎭 Type Erasure Strikes Again

Because of type erasure, you **cannot check** the specific generic type at runtime:

```java
public static <T> void checkList(List<T> list) {
    // COMPILE ERROR! ❌
    if (list instanceof ArrayList<String>) {
        System.out.println("It's an ArrayList of Strings");
    }
    
    // Also error: ❌
    // if (list instanceof List<Integer>) { ... }
}
```

### ✅ What You CAN Check

#### 1. Check Raw Type (without generic info):
```java
public static void checkList(List<?> list) {
    // ✅ OK: Check if it's an ArrayList (any generic type)
    if (list instanceof ArrayList) {
        System.out.println("It's some kind of ArrayList");
    }
    
    // ✅ OK: Check if it's a List (any generic type)
    if (list instanceof List) {
        System.out.println("It's some kind of List");
    }
}
```

#### 2. Check Unbounded Wildcard (Java's preferred way):
```java
public static void checkList(List<?> list) {
    // ✅ OK: Unbounded wildcard is reifiable
    if (list instanceof ArrayList<?>) {
        System.out.println("It's an ArrayList of something");
    }
}
```

#### 3. Workaround: Store Class Object
```java
class TypedList<T> {
    private List<T> list;
    private Class<T> type;
    
    public TypedList(Class<T> type) {
        this.list = new ArrayList<>();
        this.type = type;
    }
    
    public boolean isType(Class<?> otherType) {
        return type.equals(otherType);  // ✅ Runtime type check!
    }
}

// Usage:
TypedList<String> stringList = new TypedList<>(String.class);
if (stringList.isType(String.class)) {
    System.out.println("It's a String list!");  // ✅
}
```

---

## 🎭 Cannot Cast Parameterized Types (Mostly)

### The General Rule: No Casting Between Different Generic Types

```java
List<Integer> integerList = new ArrayList<>();
// COMPILE ERROR! ❌
List<Number> numberList = (List<Number>) integerList;

// ERROR MESSAGE:
// Inconvertible types: cannot cast List<Integer> to List<Number>
```

### Why?
Because it's **unsafe**! If it were allowed:

```java
// Pretend this worked (it doesn't):
List<Number> numbers = (List<Number>) integerList;
numbers.add(3.14);  // Add a Double to what's really a List<Integer>!
Integer i = integerList.get(0);  // 💥 ClassCastException!
```

### ⚠️ Unchecked Cast Warnings

Sometimes you get **unchecked cast warnings** instead of errors:

```java
List rawList = new ArrayList();
rawList.add("Hello");
rawList.add(123);

// ⚠️ Warning: Unchecked cast
List<String> stringList = (List<String>) rawList;
// At runtime: stringList thinks it has only Strings, but has Integer too!
```

### When Casting IS Allowed:

#### 1. Same Raw Type, Compatible Bounds:
```java
List<String> stringList = new ArrayList<>();
// ✅ OK: Cast to same raw type with same/higher bound
ArrayList<String> arrayList = (ArrayList<String>) stringList;
```

#### 2. With Wildcards (Sometimes):
```java
List<? extends Number> numbers = new ArrayList<Integer>();
// ✅ OK: Cast back to specific type (with warning)
List<Integer> integers = (List<Integer>) numbers;
```

#### 3. Using @SuppressWarnings (Use Carefully!):
```java
@SuppressWarnings("unchecked")
public static <T> List<T> castList(Object obj) {
    // You're telling the compiler: "I know what I'm doing!"
    return (List<T>) obj;
}
```

---

## 📦 Cannot Create Arrays of Parameterized Types

### The Big Restriction: No Generic Arrays! ❌

```java
// ALL OF THESE ARE COMPILE ERRORS: ❌
List<String>[] arrayOfLists = new List<String>[10];
Pair<Integer, String>[] pairs = new Pair<Integer, String>[5];
T[] array = new T[10];  // Inside generic class/method
```

### 💥 The Array Store Exception Problem

**Why?** Arrays in Java have **runtime type checking**, but generics don't!

```java
// With regular arrays:
String[] strings = new String[2];
strings[0] = "Hello";
strings[1] = 123;  // 💥 ArrayStoreException at runtime!

// If generic arrays were allowed:
List<String>[] array = new List<String>[2];  // Pretend it works
array[0] = new ArrayList<String>();
array[1] = new ArrayList<Integer>();  // Should fail but won't be caught!
// Because after erasure: both are just ArrayList!
```

### 🛠️ Workarounds for Generic Arrays

#### 1. Use ArrayList Instead (Usually Best):
```java
// ✅ Instead of array, use List of Lists
List<List<String>> listOfLists = new ArrayList<>();

// Add lists as needed
listOfLists.add(new ArrayList<String>());
listOfLists.add(new ArrayList<String>());
```

#### 2. Create Array of Raw Type, Then Cast:
```java
// ⚠️ Warning: Unchecked cast
@SuppressWarnings("unchecked")
List<String>[] createArray(int size) {
    return (List<String>[]) new List[size];
}

// Usage:
List<String>[] array = createArray(10);
array[0] = new ArrayList<String>();
```

#### 3. Use Array.newInstance with Class Object:
```java
import java.lang.reflect.Array;

public static <T> T[] createGenericArray(Class<T> clazz, int size) {
    // ✅ Creates array of the specific type
    @SuppressWarnings("unchecked")
    T[] array = (T[]) Array.newInstance(clazz, size);
    return array;
}

// Usage:
String[] strings = createGenericArray(String.class, 10);
Integer[] integers = createGenericArray(Integer.class, 5);
```

#### 4. For Collections: ToArray Method
```java
List<String> list = Arrays.asList("A", "B", "C");
// ✅ Creates properly typed array
String[] array = list.toArray(new String[0]);
```

---

## 🚫 Exception-Related Restrictions

### 🎯 Cannot Extend Throwable

Generic classes **cannot extend `Throwable`** (directly or indirectly):

```java
// COMPILE ERRORS: ❌
class GenericException<T> extends Exception { }
class MathException<T> extends RuntimeException { }
class CustomError<T> extends Error { }
```

**Why?** The Java catch mechanism needs to know the exact exception type at runtime, but generics are erased!

### What You CAN Do:

#### 1. Use Type Parameter in Fields (Not in Inheritance):
```java
// ✅ OK: Exception with generic data
class DataException extends Exception {
    private Object data;  // Or use wildcards
    
    public DataException(String message, Object data) {
        super(message);
        this.data = data;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;  // Cast with warning
    }
}
```

#### 2. Use Different Exception Classes:
```java
// Instead of generic exception, create specific ones:
class StringProcessingException extends Exception { }
class IntegerProcessingException extends Exception { }
// Or use a common base without generics
```

### 🎭 Cannot Catch Type Parameters

You **cannot catch** a type parameter:

```java
public static <T extends Exception> void riskyMethod() {
    try {
        // Do something risky
    } catch (T e) {  // ❌ COMPILE ERROR!
        // Handle exception
    }
}
```

### ✅ Can Use in Throws Clause

But you **CAN** use type parameters in `throws` clause:

```java
// ✅ OK: Type parameter in throws
interface Parser<T extends Exception> {
    void parse(File file) throws T;
}

// Implementation with specific exception
class XmlParser implements Parser<IOException> {
    @Override
    public void parse(File file) throws IOException {
        // Parse XML, might throw IOException
    }
}

// Implementation with runtime exception  
class JsonParser implements Parser<RuntimeException> {
    @Override
    public void parse(File file) {  // No checked exception
        // Parse JSON
    }
}
```

---

## 🔄 Cannot Overload with Same Erasure

### 🎭 The Method Signature Conflict

You **cannot overload** methods when they'd have the same signature after type erasure:

```java
class Example {
    // ❌ COMPILE ERROR: Both become print(Set) after erasure!
    public void print(Set<String> set) { }
    public void print(Set<Integer> set) { }
    
    // Also error:
    // public void process(List<String> list) { }
    // public void process(List<Number> list) { }
}
```

**Error Message:** `name clash: methodName(Set<X>) and methodName(Set<Y>) have the same erasure`

### Why?
After type erasure, both methods become:
```java
public void print(Set set) { }  // Raw type - same signature!
```

### Solutions:

#### 1. Different Method Names:
```java
class Example {
    // ✅ OK: Different names
    public void printStrings(Set<String> set) { }
    public void printIntegers(Set<Integer> set) { }
}
```

#### 2. Different Parameter Types (Not Just Generic):
```java
class Example {
    // ✅ OK: Different parameter types
    public void print(Set<String> set) { }
    public void print(List<String> list) { }  // Different raw type!
    
    // ✅ OK: Different number of parameters
    public void process(Set<String> set) { }
    public void process(Set<Integer> set, String label) { }
}
```

#### 3. Use Wildcards (Limited):
```java
class Example {
    // ✅ OK: Different wildcard bounds
    public void process(Set<? extends Number> set) { }
    public void process(Set<? super Integer> set) { }
    // But these are probably doing different things anyway!
}
```

---

## 🔧 Other Minor Restrictions

### 1. Cannot Use Primitive Arrays as Type Arguments:
```java
// ❌ Error: int[] is still primitive-based
List<int[]> listOfIntArrays = new ArrayList<>();  // ✅ OK
// But int[] is an object (array), so this works!

// ❌ This is what's not allowed:
// SomeGenericClass<int>  // Can't use primitive
// SomeGenericClass<int[]> // ✅ OK - array is object
```

### 2. Cannot Have Two Type Parameters with Same Erasure:
```java
// ❌ In the same class:
class BadExample<T, U> {
    // Can't have two methods that erase to same signature
    void method(List<T> list) { }
    void method(List<U> list) { }  // ❌ Same erasure: method(List)
}
```

### 3. Enum Restrictions:
```java
// ❌ Cannot create generic enums
// enum Color<T> { RED, GREEN, BLUE }  // Compile error

// ✅ But enum can implement generic interface:
interface ComparableEnum<T> extends Comparable<T> { }

enum Color implements ComparableEnum<Color> {
    RED, GREEN, BLUE;
    
    @Override
    public int compareTo(Color other) {
        return this.ordinal() - other.ordinal();
    }
}
```

---

## 🎮 Practical Solutions and Patterns

### Pattern 1: The Type Token Pattern (Reified Generics)
```java
// Store Class object to "reify" the type
public class TypeSafeMap {
    private Map<Class<?>, Object> map = new HashMap<>();
    
    public <T> void put(Class<T> type, T instance) {
        map.put(type, instance);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        return (T) map.get(type);
    }
}

// Usage:
TypeSafeMap container = new TypeSafeMap();
container.put(String.class, "Hello");
container.put(Integer.class, 42);

String s = container.get(String.class);    // ✅ Type safe
Integer i = container.get(Integer.class);  // ✅ Type safe
```

### Pattern 2: Generic Factory with Supplier
```java
// Clean creation without reflection exceptions
public class ObjectPool<T> {
    private Supplier<T> factory;
    private Queue<T> pool = new LinkedList<>();
    
    public ObjectPool(Supplier<T> factory) {
        this.factory = factory;
    }
    
    public T getObject() {
        if (pool.isEmpty()) {
            return factory.get();
        }
        return pool.poll();
    }
    
    public void returnObject(T obj) {
        pool.offer(obj);
    }
}

// Usage:
ObjectPool<StringBuilder> pool = 
    new ObjectPool<>(StringBuilder::new);
StringBuilder sb = pool.getObject();
```

### Pattern 3: Safe Generic Array Creation
```java
// Use List internally, provide array access
public class SafeGenericArray<E> {
    private List<E> list;
    
    public SafeGenericArray(int initialCapacity) {
        list = new ArrayList<>(initialCapacity);
        // Fill with nulls to simulate array
        for (int i = 0; i < initialCapacity; i++) {
            list.add(null);
        }
    }
    
    public void set(int index, E element) {
        list.set(index, element);
    }
    
    public E get(int index) {
        return list.get(index);
    }
    
    // Convert to array (type-safe)
    public E[] toArray(E[] array) {
        return list.toArray(array);
    }
}
```

### Best Practices Summary: 📋

1. **✅ Use wrapper classes** for primitives (autoboxing helps)
2. **✅ Pass `Class<T>`** when you need to create instances
3. **✅ Use `Supplier<T>`** for factory logic (Java 8+)
4. **✅ Prefer `List<List<T>>`** over `List<T>[]`
5. **✅ Check raw types** with `instanceof`, not parameterized types
6. **✅ Avoid unchecked casts** - use proper typing instead
7. **✅ Use different method names** instead of overloading with same erasure
8. **✅ Document** when you use `@SuppressWarnings`

---

## 🎓 Summary

Java generics have restrictions, but they exist for **good reasons**! Most come from **type erasure** and the need for **backward compatibility**.

### The Restrictions Cheat Sheet: 🗺️

| Restriction | Reason | Workaround |
|-------------|--------|------------|
| **No primitives** | Primitives aren't objects | Use wrapper classes |
| **No `new T()`** | Type erasure | Pass `Class<T>` or `Supplier<T>` |
| **No static generic fields** | Shared across all instances | Use non-generic type or wildcard |
| **No `instanceof List<String>`** | Type erasure | Check raw type or use `List<?>` |
| **No generic arrays** | Array store exception safety | Use `ArrayList` or reflection |
| **No generic exceptions** | Catch mechanism needs exact type | Use type-specific exceptions |
| **No overload with same erasure** | Method signature conflict | Use different method names |

### Remember: 🤔
Java generics are **compile-time magic** 🎩✨. The restrictions keep the runtime simple and compatible. While they can be frustrating, understanding **why** they exist helps you work within them effectively!

**Final Thought:** These restrictions are the price we pay for having **backward-compatible, performant generics** in Java. The workarounds might require a bit more code, but they lead to **type-safe, maintainable systems**! 🏗️✅