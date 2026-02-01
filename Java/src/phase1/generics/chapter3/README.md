# 🌟 Chapter 3: Wildcards in Java Generics

## 📚 Table of Contents
1. [🎯 Introduction to Wildcards](#introduction-to-wildcards)
2. [📈 Upper Bounded Wildcards](#upper-bounded-wildcards)
   - [🎮 Practical Example: sumOfList](#practical-example-sumoflist)
3. [🌀 Unbounded Wildcards](#unbounded-wildcards)
   - [🔄 List<Object> vs List<?>](#listobject-vs-list)
4. [📉 Lower Bounded Wildcards](#lower-bounded-wildcards)
   - [📥 "In" Variables and "Out" Variables](#in-variables-and-out-variables)
5. [🧬 Wildcards and Subtyping](#wildcards-and-subtyping)
   - [🔄 Wildcard Hierarchy](#wildcard-hierarchy)
6. [🎯 Wildcard Capture and Helper Methods](#wildcard-capture-and-helper-methods)
   - [🔧 Fixing Capture Errors](#fixing-capture-errors)
7. [📝 Guidelines for Wildcard Use](#guidelines-for-wildcard-use)
   - [🎯 PECS Principle (Producer Extends, Consumer Super)](#pecs-principle)
8. [⚡ Advanced Patterns and Best Practices](#advanced-patterns-and-best-practices)

---

## 🎯 Introduction to Wildcards

Wildcards (`?`) are the **magic wand** ✨ of Java generics that add flexibility when you don't know or don't care about the exact type! They're like saying "I'll accept anything that fits this pattern" instead of "I need exactly this type"! 🎭

### The Three Types of Wildcards:
1. **Upper Bounded:** `? extends Type` - Accepts `Type` or its **subtypes** 📈
2. **Unbounded:** `?` - Accepts **any type** 🌍
3. **Lower Bounded:** `? super Type` - Accepts `Type` or its **supertypes** 📉

---

## 📈 Upper Bounded Wildcards

Upper bounded wildcards let you write methods that work on a type **and all its subtypes**! Think of it as being **inclusive** - "I accept this family of types!" 👨‍👩‍👧‍👦

### Syntax: `? extends Type`

```java
// Works only with List<Number> - too restrictive! 😒
public static void processNumbers(List<Number> numbers) {
    for (Number n : numbers) {
        System.out.println(n.doubleValue());
    }
}

// Works with List<Number>, List<Integer>, List<Double> - flexible! 🎉
public static void processNumbersFlexible(List<? extends Number> numbers) {
    for (Number n : numbers) {  // Can read as Number!
        System.out.println(n.doubleValue());
    }
}
```

### 🚫 Important Restriction: Read-Only (Mostly)
```java
public static void addToList(List<? extends Number> list) {
    // list.add(10);          // ❌ Compile error!
    // list.add(3.14);        // ❌ Compile error!
    // list.add(new Integer(5)); // ❌ Compile error!
    list.add(null);           // ✅ Only null is allowed!
    
    // But you CAN read from it!
    Number n = list.get(0);   // ✅ Always safe
}
```

**Why can't you add?** Because the compiler doesn't know the exact type! Is it `List<Integer>`, `List<Double>`, or `List<Number>`? To be safe, it prevents all additions (except `null`).

### 🎮 Practical Example: sumOfList

```java
// Works with ANY List of Number or its subclasses! 🎯
public static double sumOfList(List<? extends Number> list) {
    double sum = 0.0;
    for (Number n : list) {
        sum += n.doubleValue();  // All Numbers have doubleValue()
    }
    return sum;
}

// Usage with different types:
List<Integer> integers = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
List<Float> floats = Arrays.asList(1.1f, 2.2f, 3.3f);

System.out.println(sumOfList(integers));  // ✅ 6.0
System.out.println(sumOfList(doubles));   // ✅ 7.5  
System.out.println(sumOfList(floats));    // ✅ 6.6
```

---

## 🌀 Unbounded Wildcards

Unbounded wildcards (`?`) are the **ultimate flexible** wildcard! They say "I don't care about the type at all!" 🤷‍♂️

### When to Use Unbounded Wildcards:
1. **When using Object class methods** is enough
2. **When the method doesn't depend on the type parameter**
3. **When you want maximum flexibility**

### Example: Generic Printer
```java
// ❌ Too restrictive - only List<Object>
public static void printList(List<Object> list) {
    for (Object elem : list) {
        System.out.print(elem + " ");
    }
    System.out.println();
}

// ✅ Flexible - accepts List<String>, List<Integer>, anything!
public static void printListUniversal(List<?> list) {
    for (Object elem : list) {  // Everything extends Object!
        System.out.print(elem + " ");
    }
    System.out.println();
}

// Usage:
List<Integer> ints = Arrays.asList(1, 2, 3);
List<String> strings = Arrays.asList("A", "B", "C");

printListUniversal(ints);     // ✅ Prints: 1 2 3
printListUniversal(strings);  // ✅ Prints: A B C
// printList(ints);           // ❌ Wouldn't compile!
```

### 🔄 List<Object> vs List<?>

| Aspect | `List<Object>` | `List<?>` |
|--------|----------------|-----------|
| **Can add** | ✅ Any Object | ❌ Only `null` |
| **Can read** | ✅ As Object | ✅ As Object |
| **Flexibility** | Only `List<Object>` | Any `List<T>` |
| **Purpose** | Know type is Object | Don't know/care about type |

```java
List<Object> objectList = new ArrayList<>();
List<?> wildcardList = new ArrayList<String>();

objectList.add("Hello");      // ✅
objectList.add(123);          // ✅
// wildcardList.add("Hello"); // ❌ Compile error!
// wildcardList.add(123);     // ❌ Compile error!
wildcardList.add(null);       // ✅ Only null allowed!

Object obj1 = objectList.get(0);    // ✅
Object obj2 = wildcardList.get(0);  // ✅
```

---

## 📉 Lower Bounded Wildcards

Lower bounded wildcards are the **opposite** of upper bounded! They accept a type **and all its supertypes** - like saying "I need at least this type or more general" 📤

### Syntax: `? super Type`

```java
// Only accepts List<Integer> - too specific! 😒
public static void addToIntegerList(List<Integer> list) {
    list.add(42);
}

// Accepts List<Integer>, List<Number>, List<Object> - flexible! 🎉
public static void addToNumberList(List<? super Integer> list) {
    list.add(42);      // ✅ Can add Integer
    list.add(100);     // ✅ Can add Integer
    // list.add(3.14); // ❌ Can't add Double (not Integer)
}
```

### 📥 "In" Variables and "Out" Variables

This is the **KEY CONCEPT** to understand wildcards! 🗝️

```java
// COPY METHOD PATTERN:
public static <T> void copy(List<? extends T> source,  // IN - produces T
                            List<? super T> destination) { // OUT - consumes T
    for (T item : source) {
        destination.add(item);  // ✅ Safe! Takes from source, adds to dest
    }
}

// Usage:
List<Integer> source = Arrays.asList(1, 2, 3);
List<Number> destination = new ArrayList<>();
copy(source, destination);  // ✅ Works perfectly!
```

**Visual Guide:**
```
SOURCE (Producer) → 📤 → T → 📥 → DESTINATION (Consumer)
   ? extends T          |          ? super T
   (Gives out T)        |        (Takes in T)
```

---

## 🧬 Wildcards and Subtyping

Wildcards create **flexible relationships** between generic types! Let's explore the hierarchy: 🏗️

### The Subtyping Problem Without Wildcards:
```java
Integer integer = 10;
Number number = integer;  // ✅ Integer is a subtype of Number

List<Integer> intList = new ArrayList<>();
// List<Number> numList = intList;  // ❌ Compile error!
// Generic types are INVARIANT!
```

### Solution: Wildcards Create Relationships!
```java
// With wildcards, we CAN create relationships:
List<? extends Integer> intList = new ArrayList<Integer>();
List<? extends Number> numList = intList;  // ✅ Works!

// Visualizing the hierarchy:
//                List<?>
//                   ↑
//         List<? extends Number>
//           ↗               ↖
// List<? extends Integer>  List<Number>
//           ↑
//     List<Integer>
```

### 🔄 Wildcard Hierarchy

```
                    List<?> (Unbounded)
                         ↑
           List<? extends Number> (Upper bounded)
          ↗                          ↖
List<? extends Integer>         List<? extends Double>
         ↑                               ↑
   List<Integer>                    List<Double>


                    List<? super Integer> (Lower bounded)
          ↗                          ↖
List<Integer>                    List<? super Number>
                                     ↑
                               List<? super Object>
                                     ↑
                                List<Object>
```

### Practical Subtyping Examples:
```java
// Upper bounded wildcards are COVARIANT
List<Integer> intList = Arrays.asList(1, 2, 3);
List<? extends Integer> intWild = intList;       // ✅
List<? extends Number> numWild = intList;        // ✅
// List<Number> numList = intList;               // ❌

// Lower bounded wildcards are CONTRAVARIANT  
List<Number> numList = new ArrayList<>();
List<? super Number> numSuper = numList;         // ✅
List<? super Integer> intSuper = numList;        // ✅
List<Object> objList = new ArrayList<>();
List<? super Integer> objSuper = objList;        // ✅
```

---

## 🎯 Wildcard Capture and Helper Methods

Sometimes wildcards are **too flexible** and the compiler gets confused about types! This is called **wildcard capture** - when the compiler needs to "capture" the unknown type. 🎭

### The Wildcard Capture Problem:
```java
public class WildcardError {
    void swapFirst(List<?> list) {
        // Trying to swap first two elements
        Object temp = list.get(0);
        // list.set(0, list.get(1));  // ❌ Compile error!
        // list.set(1, temp);         // ❌ Compile error!
    }
}
```

**Error Message:** `CAP#1` appears! This is the compiler's internal name for the captured type.

```
error: method set in interface List<E> cannot be applied to given types
  required: int, CAP#1
  found: int, Object
  where CAP#1 is a fresh type-variable:
    CAP#1 extends Object from capture of ?
```

### 🔧 Fixing Capture Errors with Helper Methods

The solution: Create a **helper method with a type parameter**! 🛠️

```java
public class WildcardFixed {
    
    // Public method with wildcard
    public void swapFirst(List<?> list) {
        swapFirstHelper(list);  // Delegate to helper
    }
    
    // Private helper with type parameter
    private <T> void swapFirstHelper(List<T> list) {
        if (list.size() >= 2) {
            T temp = list.get(0);
            list.set(0, list.get(1));  // ✅ Now works!
            list.set(1, temp);         // ✅ Now works!
        }
    }
}
```

### Why This Works:
1. **Public method** accepts `List<?>` (flexible for callers)
2. **Helper method** captures the wildcard as `T`
3. **Compiler now knows** `T` is consistent throughout

### Another Example: Safe vs Unsafe Operations
```java
public class WildcardExamples {
    
    // ❌ UNSAFE - mixing different lists
    void unsafeSwap(List<? extends Number> list1, 
                    List<? extends Number> list2) {
        Number temp = list1.get(0);
        // list1.set(0, list2.get(0));  // ❌ Compile error - smart!
        // list2.set(0, temp);          // ❌ Compile error - smart!
    }
    
    // ✅ SAFE - same list operations
    <T> void safeSwap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
```

**The compiler prevents** putting an `Integer` from one list into a `Double` list, even though both are `List<? extends Number>`! 🧠

---

## 📝 Guidelines for Wildcard Use

### 🎯 PECS Principle (Producer Extends, Consumer Super)

This is the **GOLDEN RULE** for wildcards! Remember: **PECS = Producer Extends, Consumer Super** 🏆

| Scenario | Wildcard | Mnemonic |
|----------|----------|----------|
| **You GET from it** (Producer) | `? extends T` | **P**roducer **E**xtends |
| **You PUT into it** (Consumer) | `? super T` | **C**onsumer **S**uper |
| **You do both** | No wildcard | Use type parameter `<T>` |

### Practical PECS Examples:

#### Example 1: Copy Method (Classic PECS)
```java
// RIGHT: Producer extends, Consumer super ✅
public static <T> void copy(List<? extends T> src,  // PRODUCER
                            List<? super T> dest) { // CONSUMER
    for (T item : src) {
        dest.add(item);
    }
}

// WRONG: Wouldn't work with all types ❌
public static <T> void badCopy(List<T> src, List<T> dest) {
    // Can't copy List<Integer> to List<Number>
}
```

#### Example 2: Max Function
```java
// Producer of Comparable objects
public static <T extends Comparable<? super T>> T max(Collection<? extends T> coll) {
    T max = null;
    for (T item : coll) {  // PRODUCER: extends T
        if (max == null || item.compareTo(max) > 0) {
            max = item;
        }
    }
    return max;
}
```

#### Example 3: Add All Implementation
```java
// Consumer for adding elements
public static <T> void addAll(List<? super T> list,  // CONSUMER: super T
                              T... elements) {
    for (T element : elements) {
        list.add(element);
    }
}

// Usage:
List<Number> numbers = new ArrayList<>();
addAll(numbers, 1, 2, 3.14, 4L);  // ✅ All are Number or subclass
```

### Decision Flowchart for Wildcards: 🗺️

```
          Start: Need a parameterized type
                    |
                    ↓
         Do you need to both READ and WRITE?
            /                  \
           /                    \
          YES                    NO
          |                      |
          ↓                      ↓
    Use type parameter       Need to READ from it?
    <T> List<T>             /                \
                          YES                NO
                          /                    \
                         ↓                      ↓
                    Producer              Need to WRITE to it?
                    ? extends T           /                \
                                         YES                NO
                                         /                    \
                                        ↓                      ↓
                                   Consumer               Use unbounded
                                   ? super T                  List<?>
```

### 🚫 When NOT to Use Wildcards:

1. **Return types** - Force callers to deal with wildcards
   ```java
   // ❌ Bad: Caller gets wildcard
   public List<?> getList() { ... }
   
   // ✅ Good: Caller gets concrete type
   public <T> List<T> getList(Class<T> type) { ... }
   ```

2. **Both reading and writing** - Use type parameters instead
   ```java
   // ❌ Confusing with wildcards
   public void process(List<?> list) {
       // Can't add, can't use type-specific methods
   }
   
   // ✅ Clear with type parameter
   public <T> void process(List<T> list) {
       list.add(list.get(0));  // Works!
   }
   ```

---

## ⚡ Advanced Patterns and Best Practices

### Pattern 1: The "Flexible Parameter" Pattern
```java
// Accepts any collection of any type that extends Number
public void processNumbers(Collection<? extends Number> numbers) {
    double sum = 0;
    for (Number n : numbers) {
        sum += n.doubleValue();
    }
    // Can't modify 'numbers' but that's often OK!
}

// Usage with ANY Number collection:
processNumbers(new ArrayList<Integer>());
processNumbers(new HashSet<Double>());
processNumbers(new LinkedList<BigDecimal>());
```

### Pattern 2: The "Builder/Accumulator" Pattern
```java
// Lower bounded for building/accumulating
public static void addIntegers(List<? super Integer> list) {
    for (int i = 1; i <= 10; i++) {
        list.add(i);  // ✅ Can always add Integer
    }
}

// Usage with different containers:
List<Integer> ints = new ArrayList<>();
List<Number> nums = new ArrayList<>();
List<Object> objs = new ArrayList<>();

addIntegers(ints);  // ✅
addIntegers(nums);  // ✅  
addIntegers(objs);  // ✅
```

### Pattern 3: The "Type-Safe Heterogeneous Container"
```java
// Advanced pattern using wildcards for type safety
public class Favorites {
    private Map<Class<?>, Object> favorites = new HashMap<>();
    
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(type, instance);
    }
    
    public <T> T getFavorite(Class<T> type) {
        // Cast is safe because of putFavorite's type guarantee
        return type.cast(favorites.get(type));
    }
}

// Usage:
Favorites f = new Favorites();
f.putFavorite(String.class, "Java");
f.putFavorite(Integer.class, 42);

String s = f.getFavorite(String.class);  // ✅ Type safe!
Integer i = f.getFavorite(Integer.class); // ✅ Type safe!
```

### Best Practices Summary: 📋

1. **✅ Use PECS** - Remember "Producer Extends, Consumer Super"
2. **✅ Prefer type parameters** when you need both read and write
3. **✅ Use unbounded wildcards** (`?`) for maximum flexibility when only using Object methods
4. **✅ Use helper methods** to fix wildcard capture errors
5. **❌ Avoid wildcards in return types**
6. **❌ Don't use raw types** - wildcards are better
7. **✅ Document wildcard intent** with comments
8. **✅ Test with different type arguments** to ensure flexibility

---

## 🎓 Summary

Wildcards make Java generics **powerful and flexible**! They're the bridge between strict type safety and practical flexibility. 🌉

### Key Takeaways:
- **📈 `? extends T`** - For **reading/producing** (`List<? extends Number>`)
- **📉 `? super T`** - For **writing/consuming** (`List<? super Integer>`)
- **🌀 `?`** - When you **don't care** about the type
- **🎯 PECS** - The golden rule: **Producer Extends, Consumer Super**
- **🔧 Helper methods** - Fix wildcard capture errors
- **🧬 Wildcards enable subtyping** between generic types

### Remember This Mnemonic: 🧠
```
GET from Producer → EXTENDS (G → E)
PUT into Consumer → SUPER (P → S)
Don't care at all → ? (Wildcard)
Both GET and PUT → Type parameter <T>
```

Wildcards turn generic code from "rigid but safe" to "flexible AND safe"! They're what make Java collections so powerful and reusable. 🚀✨

**Pro tip:** When in doubt, ask: "Am I getting from this (Producer) or putting into this (Consumer)?" That answer tells you which wildcard to use! 🎯