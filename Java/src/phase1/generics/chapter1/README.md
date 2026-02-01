# 📦 Chapter 1: Introducing Generics in Java

## 📚 Table of Contents
1. [🎯 Why Use Generics?](#why-use-generics)
2. [📦 Generic Types](#generic-types)
   - [📝 A Simple Box Class](#a-simple-box-class)
   - [✨ A Generic Version of the Box Class](#a-generic-version-of-the-box-class)
3. [🏷️ Type Parameter Naming Conventions](#type-parameter-naming-conventions)
4. [🚀 Invoking and Instantiating a Generic Type](#invoking-and-instantiating-a-generic-type)
   - [💎 The Diamond Operator](#the-diamond-operator)
5. [🎭 Multiple Type Parameters](#multiple-type-parameters)
6. [🔗 Parameterized Types](#parameterized-types)
7. [⚠️ Raw Types](#raw-types)
8. [🚨 Unchecked Error Messages](#unchecked-error-messages)
9. [🛠️ Generic Methods](#generic-methods)
10. [🔒 Bounded Type Parameters](#bounded-type-parameters)
    - [🔗 Multiple Bounds](#multiple-bounds)
11. [⚖️ Generic Methods and Bounded Type Parameters](#generic-methods-and-bounded-type-parameters)
12. [🧬 Generics, Inheritance, and Subtypes](#generics-inheritance-and-subtypes)
13. [🌳 Generic Classes and Subtyping](#generic-classes-and-subtyping)

---

## 🎯 Why Use Generics?

Generics allow you to use **types as parameters** when defining classes, interfaces, and methods. Think of them as templates where you can plug in different types, just like you plug in different values into method parameters! 🎨

### Key Benefits:
1. **🔍 Stronger Type Checks at Compile Time** - Catch errors early!
2. **🗑️ Elimination of Casts** - Cleaner code without messy casting
3. **🔄 Reusable Generic Algorithms** - Write once, use with multiple types

### Example: Before and After Generics

**❌ Without Generics (Messy Casting):**
```java
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);  // Cast required!
```

**✅ With Generics (Clean and Safe):**
```java
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);  // No cast needed! ✨
```

---

## 📦 Generic Types

### 📝 A Simple Box Class

Imagine a Box that can hold anything:

```java
public class Box {
    private Object object;
    
    public void set(Object object) { this.object = object; }
    public Object get() { return object; }
}
```

**Problem:** 🤔 This Box is too flexible! It can hold ANY Object, leading to potential runtime errors:

```java
Box box = new Box();
box.set("Hello");  // String is fine
Integer number = (Integer) box.get();  // Runtime error! 💥
```

### ✨ A Generic Version of the Box Class

Let's make it type-safe with generics:

```java
public class Box<T> {
    private T t;
    
    public void set(T t) { this.t = t; }
    public T get() { return t; }
}
```

**🎉 Now it's type-safe!**
- `T` is a **type parameter** (stands for "Type")
- Replace `Object` with `T` throughout the class
- Can be used with any non-primitive type

```java
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String value = stringBox.get();  // Safe! ✅

// Compile-time error if you try:
// stringBox.set(123);  // ❌ Won't compile!
```

---

## 🏷️ Type Parameter Naming Conventions

Java developers use **single uppercase letters** for type parameters. This makes them easy to spot! 👀

| Convention | Meaning | Typical Use |
|------------|---------|-------------|
| **E** | Element | Used in collections (List<E>, Set<E>) |
| **K** | Key | Used in maps (Map<K,V>) |
| **N** | Number | For numeric types |
| **T** | Type | General purpose |
| **V** | Value | Used in maps (Map<K,V>) |
| **S, U, V** | 2nd, 3rd, 4th types | When you need multiple |

**Example:** `Box<T>` uses `T` for Type. Simple and clear! ✅

---

## 🚀 Invoking and Instantiating a Generic Type

### Creating Generic Objects:

```java
// Declaration
Box<Integer> integerBox;  // "Box of Integer"

// Instantiation (before Java 7)
Box<Integer> integerBox = new Box<Integer>();

// Type Argument vs Type Parameter:
// - T in Box<T> is a TYPE PARAMETER
// - Integer in Box<Integer> is a TYPE ARGUMENT
```

### 💎 The Diamond Operator (Java 7+)

Java 7 introduced the **diamond operator** `<>` for type inference:

```java
// Old way (verbose)
Box<Integer> integerBox = new Box<Integer>();

// New way with diamond (cleaner!)
Box<Integer> integerBox = new Box<>();
```

The compiler infers the type from the left side! 🧠

---

## 🎭 Multiple Type Parameters

You can have multiple type parameters! Here's a `Pair` example:

```java
// Interface with two type parameters
public interface Pair<K, V> {
    public K getKey();
    public V getValue();
}

// Implementing class
public class OrderedPair<K, V> implements Pair<K, V> {
    private K key;
    private V value;
    
    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}
```

**Usage:**
```java
// Different type combinations!
Pair<String, Integer> p1 = new OrderedPair<>("Age", 25);
Pair<String, String> p2 = new OrderedPair<>("Hello", "World");
Pair<String, Box<Integer>> p3 = new OrderedPair<>("Box", new Box<>());
```

---

## 🔗 Parameterized Types

You can use parameterized types as type arguments! 🤯

```java
// A Box containing a List of Strings
Box<List<String>> boxOfLists = new Box<>();

// A Pair with a String key and a Box of Integer value
OrderedPair<String, Box<Integer>> pair = 
    new OrderedPair<>("primes", new Box<>());
```

This is like **Russian nesting dolls** of types! 🪆

---

## ⚠️ Raw Types

**Raw types** are generic types used WITHOUT type arguments. They exist for backward compatibility but are **dangerous**! ⚠️

```java
// Parameterized type (Safe)
Box<String> stringBox = new Box<>();

// Raw type (Dangerous! Legacy code)
Box rawBox = new Box();
```

### Why Avoid Raw Types?
1. **Lose type safety** - Back to pre-generics behavior
2. **Compiler warnings** - Get "unchecked" warnings
3. **Runtime errors** - Type mismatches caught late

```java
Box<String> stringBox = new Box<>();
Box rawBox = stringBox;        // OK (backward compatibility)
rawBox.set(123);               // Warning! But compiles
String s = stringBox.get();    // Runtime ClassCastException! 💥
```

---

## 🚨 Unchecked Error Messages

When mixing legacy (raw type) and generic code:

```java
public class WarningDemo {
    public static void main(String[] args) {
        Box<Integer> bi = createBox();  // Warning! ⚠️
    }
    
    static Box createBox() {  // Raw type return
        return new Box();
    }
}
```

**Compile with:** `javac -Xlint:unchecked WarningDemo.java`

**Output:**
```
WarningDemo.java:4: warning: [unchecked] unchecked conversion
found   : Box
required: Box<java.lang.Integer>
```

**Fix it with generics!** ✅

---

## 🛠️ Generic Methods

Methods can have their own type parameters too! These are **generic methods**:

```java
public class Util {
    // Generic method comparing two Pairs
    public static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) &&
               p1.getValue().equals(p2.getValue());
    }
}
```

**Usage with explicit types:**
```java
boolean same = Util.<Integer, String>compare(p1, p2);
```

**Usage with type inference (usually better):**
```java
boolean same = Util.compare(p1, p2);  // Compiler infers types! 🎯
```

---

## 🔒 Bounded Type Parameters

Sometimes you want to **restrict** what types can be used. Use `extends` keyword! 🔐

```java
// T must be Number or its subclass
public class NumberBox<T extends Number> {
    private T number;
    
    public double getDoubleValue() {
        return number.doubleValue();  // Safe! Number has doubleValue()
    }
}
```

**Valid uses:**
```java
NumberBox<Integer> intBox = new NumberBox<>();    // ✅
NumberBox<Double> doubleBox = new NumberBox<>();  // ✅
// NumberBox<String> stringBox = new NumberBox<>();  // ❌ Compile error!
```

### 🔗 Multiple Bounds

A type parameter can have multiple bounds:

```java
// T must extend ClassA AND implement InterfaceB and InterfaceC
class MultiBound<T extends ClassA & InterfaceB & InterfaceC> {
    // ...
}
```

**Important:** If there's a class, it must come first!
```java
class D<T extends ClassA & InterfaceB>  // ✅
class D<T extends InterfaceB & ClassA>  // ❌ Compile error!
```

---

## ⚖️ Generic Methods and Bounded Type Parameters

Great for algorithms! Here's counting elements greater than a value:

```java
// First attempt (won't compile - can't compare objects with >)
public static <T> int countGreaterThan(T[] array, T elem) {
    int count = 0;
    for (T item : array)
        if (item > elem)  // ❌ Error! > not for objects
            count++;
    return count;
}

// Fixed with bounded type parameter
public static <T extends Comparable<T>> int countGreaterThan(T[] array, T elem) {
    int count = 0;
    for (T item : array)
        if (item.compareTo(elem) > 0)  // ✅ Now works!
            count++;
    return count;
}
```

**Usage:**
```java
Integer[] numbers = {1, 5, 3, 8, 2};
int count = countGreaterThan(numbers, 3);  // Returns 2 (5 and 8) 🎉
```

---

## 🧬 Generics, Inheritance, and Subtypes

### Important Concept: ⚠️
**`Box<Integer>` is NOT a subtype of `Box<Number>` even though `Integer` is a subtype of `Number`!**

```java
// This works (regular inheritance)
Number number = new Integer(10);  // ✅

// This DOESN'T work (generics)
Box<Number> numberBox = new Box<Integer>();  // ❌ Compile error!
```

**Why?** For type safety! If it were allowed:

```java
// If this were allowed (it's not):
Box<Number> box = new Box<Integer>();  // Pretend it works
box.set(new Double(10.5));  // Would corrupt Integer box! 💥
```

### Visualizing the Relationship:
```
Number (parent)     vs     Box<Number> (parent)
   ↑                           ↑
Integer (child)     vs     Box<Integer> (NOT a child!)
```

---

## 🌳 Generic Classes and Subtyping

You CAN create subtype relationships with consistent type parameters:

```java
// Standard collection hierarchy
Collection<String>  ←  List<String>  ←  ArrayList<String>
        ↑                    ↑                  ↑
   (supertype)        (subtype)          (subtype)
```

### Custom Example: PayloadList

```java
// Extends List<E> with an additional payload type P
interface PayloadList<E, P> extends List<E> {
    void setPayload(int index, P payload);
    P getPayload(int index);
}
```

**Subtype Relationships:**
- `PayloadList<String, String>` is a subtype of `List<String>`
- `PayloadList<String, Integer>` is a subtype of `List<String>`
- `PayloadList<String, Exception>` is a subtype of `List<String>`

```java
// All these are valid List<String> instances:
List<String> list1 = new PayloadList<String, String>();
List<String> list2 = new PayloadList<String, Integer>();
List<String> list3 = new PayloadList<String, Exception>();
```

**Key Insight:** The **first type argument** (`String`) must match for subtype relationships! The second can vary. 🎯

---

## 📝 Summary

Generics bring **type safety**, **code reuse**, and **cleaner code** to Java! Remember:

1. ✅ Use generics for compile-time type checking
2. ✅ Follow naming conventions (T, E, K, V)
3. ✅ Use diamond operator `<>` for cleaner code
4. ❌ Avoid raw types (they're dangerous!)
5. 🔒 Use bounded type parameters when you need restrictions
6. ⚠️ Remember: `Box<Integer>` ≠ `Box<Number>`
7. 🌳 Subtyping works when type arguments match

Generics might seem tricky at first, but they're your best friend for writing robust, reusable Java code! 🚀💪