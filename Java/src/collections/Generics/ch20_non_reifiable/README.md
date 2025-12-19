# 👻 **20. Non-Reifiable Types**

> 🕵️ *Understanding types that lose information at runtime and their implications*

In Java's type system, **reifiable types** are those whose type information is fully available at runtime, while **non-reifiable types** lose some or all of their type information due to type erasure. This distinction has significant consequences for what you can and cannot do with generics.

## 🎯 **What Does "Reifiable" Mean?**

A type is **reifiable** if it satisfies one of these conditions:
- It's a **non-generic** class or interface (e.g., `String`, `Number`)
- It's a **raw type** (e.g., `List`, `Map`)
- It's an **unbounded wildcard** (e.g., `List<?>`, `Map<?, ?>`)
- It's an **array** of a reifiable type (e.g., `String[]`, `List<?>[]`)
- It's a **primitive** type (e.g., `int`, `double`)
- It's the **null type**

A type is **non-reifiable** if it's a **parameterized type** (e.g., `List<String>`, `Map<Integer, String>`) or a **bounded wildcard** (e.g., `List<? extends Number>`).

## 🔍 **The Runtime Reality Check**

```java
import java.util.ArrayList;
import java.util.List;

public class ReifiabilityDemo {
    public static void main(String[] args) {
        // Reifiable types
        Class<?> stringClass = String.class;
        Class<?> listRawClass = List.class;
        Class<?> wildcardListClass = List<?>.class;
        
        // Non-reifiable types - COMPILE-TIME ERRORS!
        // Class<?> stringListClass = List<String>.class;      // ❌ Error
        // Class<?> numberListClass = List<? extends Number>.class; // ❌ Error
        
        // But you can get the raw type class
        List<String> stringList = new ArrayList<>();
        Class<?> rawClass = stringList.getClass();  // Returns ArrayList, not ArrayList<String>
        System.out.println(rawClass);  // class java.util.ArrayList
    }
}
```

## 🚫 **Restrictions on Non-Reifiable Types**

### 1. ❌ **No `instanceof` with Parameterized Types**

```java
public class InstanceofRestriction {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        
        // These are ILLEGAL at compile time:
        // if (list instanceof List<String>) {}          // ❌ Compile error
        // if (list instanceof ArrayList<String>) {}     // ❌ Compile error
        
        // But these are LEGAL:
        if (list instanceof List) {                    // ✅ Raw type
            System.out.println("It's a List");
        }
        
        if (list instanceof List<?>) {                 // ✅ Unbounded wildcard (reifiable)
            System.out.println("It's a List of something");
        }
        
        // Workaround: Use raw type or unbounded wildcard
        if (list instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<String> stringList = (ArrayList<String>) list;
            // Proceed with caution!
        }
    }
}
```

### 2. ❌ **No Arrays of Parameterized Types**

```java
public class ArrayRestriction {
    public static void main(String[] args) {
        // These are ILLEGAL:
        // List<String>[] arrayOfLists = new List<String>[10];  // ❌ Compile error
        // ArrayList<String>[] stringArrays = new ArrayList<>[5]; // ❌ Compile error
        
        // But you CAN create arrays of raw types or unbounded wildcards:
        List[] rawArray = new List[10];                 // ✅ Raw type array
        List<?>[] wildcardArray = new List<?>[10];      // ✅ Unbounded wildcard array
        
        // Or use collections instead:
        List<List<String>> listOfLists = new ArrayList<>();  // ✅ Better alternative
    }
}
```

**Why this restriction?** Consider this dangerous example that would break type safety:

```java
// Hypothetical (and illegal) code:
List<String>[] stringLists = new List<String>[1];  // Illegal, but imagine if it were allowed
List<Integer> intList = List.of(42);
Object[] objects = stringLists;  // Arrays are covariant: String[] is an Object[]
objects[0] = intList;            // This would corrupt the array!
String s = stringLists[0].get(0); // ClassCastException at runtime!
```

### 3. ❌ **No `new T()` or `new T[]`**

```java
class Creator<T> {
    // These are ILLEGAL:
    // T instance = new T();               // ❌ Cannot instantiate type parameter
    // T[] array = new T[10];             // ❌ Cannot create array of type parameter
    
    // Workaround 1: Pass Class<T> and use reflection (with runtime check)
    T createInstance(Class<T> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }
    
    // Workaround 2: Use Supplier
    T createWithSupplier(Supplier<T> supplier) {
        return supplier.get();
    }
    
    // Workaround 3: For arrays, use Array.newInstance() with Class<?>
    @SuppressWarnings("unchecked")
    T[] createArray(Class<T> clazz, int size) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, size);
    }
}
```

### 4. ❌ **Limitations with Varargs and Generics**

```java
public class VarargsWarning {
    // This generates a warning: "Possible heap pollution"
    @SafeVarargs  // Add this annotation if you're sure it's safe
    public static <T> List<T> flatten(List<? extends T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<String> strings = List.of("a", "b");
        List<Integer> ints = List.of(1, 2);
        
        // Warning at call site
        List<Object> flattened = flatten(strings, ints);
    }
}
```

## 🛠️ **Practical Workarounds**

### 1. **Type Tokens for Runtime Type Information**

```java
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// Classic Type Token pattern
abstract class TypeReference<T> {
    private final Type type;
    
    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }
    
    public Type getType() {
        return type;
    }
}

// Usage
public class TypeTokenDemo {
    public static void main(String[] args) {
        TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
        System.out.println(typeRef.getType());  // java.util.List<java.lang.String>
    }
}
```

### 2. **The Class.cast() Method**

```java
public class SafeCasting {
    public static <T> List<T> castList(Class<T> clazz, List<?> rawList) {
        List<T> result = new ArrayList<>();
        for (Object item : rawList) {
            result.add(clazz.cast(item));  // Runtime type check
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<Object> mixed = List.of("hello", "world", 42);
        
        try {
            List<String> strings = castList(String.class, mixed);  // Throws ClassCastException
        } catch (ClassCastException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
```

### 3. **Using Collections Instead of Arrays**

```java
public class CollectionsOverArrays {
    // Instead of: T[][] (nested arrays)
    // Use: List<List<T>> (nested lists)
    
    public static <T> List<List<T>> createMatrix(int rows, int cols) {
        List<List<T>> matrix = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            matrix.add(new ArrayList<>(cols));
        }
        return matrix;
    }
    
    // Instead of: T[] (array of unknown type)
    // Use: List<T> (list of known type)
    
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
                   .filter(predicate)
                   .collect(Collectors.toList());
    }
}
```

## 🔍 **Type Erasure in Detail**

Let's see exactly what information is lost:

```java
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ErasureInspection {
    static class StringList extends ArrayList<String> {
        // This field retains SOME generic information
        private List<Integer> numbers;
    }
    
    public static void main(String[] args) throws Exception {
        // Get generic superclass information
        Type genericSuperclass = StringList.class.getGenericSuperclass();
        System.out.println("Generic superclass: " + genericSuperclass);
        // Output: java.util.ArrayList<java.lang.String>
        
        // Get raw superclass
        Class<?> rawSuperclass = StringList.class.getSuperclass();
        System.out.println("Raw superclass: " + rawSuperclass);
        // Output: class java.util.ArrayList
        
        // Inspect field type
        Field numbersField = StringList.class.getDeclaredField("numbers");
        Type fieldType = numbersField.getGenericType();
        System.out.println("Field generic type: " + fieldType);
        // Output: java.util.List<java.lang.Integer>
        
        // But at runtime, the actual object doesn't know its type arguments
        StringList list = new StringList();
        list.add("test");
        
        // This cast works because of type erasure
        ArrayList raw = list;
        raw.add(123);  // Compiles with warning, fails at runtime when accessed as String
    }
}
```

## 🎭 **Generic Arrays: The Special Case**

While you can't create arrays of parameterized types directly, there's a special case with wildcards:

```java
public class WildcardArrays {
    public static void main(String[] args) {
        // Array of unbounded wildcard - LEGAL but tricky
        List<?>[] wildcardArray = new List<?>[10];
        wildcardArray[0] = new ArrayList<String>();
        wildcardArray[1] = new ArrayList<Integer>();
        
        // Reading from it is safe
        List<?> list = wildcardArray[0];
        Object item = list.get(0);  // Always returns Object
        
        // Writing to it requires exact type match
        // wildcardArray[0].add("test");  // ❌ Cannot add to List<?>
        
        // Array of raw types - LEGAL but dangerous
        List[] rawArray = new List[10];
        rawArray[0] = new ArrayList<String>();
        rawArray[1] = new ArrayList<Integer>();
        
        // This compiles but may cause runtime errors
        rawArray[0].add("test");  // ✅
        // rawArray[0].add(123);   // Also compiles! Runtime type inconsistency
    }
}
```

## 🧪 **Testing Non-Reifiable Types**

```java
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class NonReifiableTest {
    
    @Test
    void testInstanceofWithRawType() {
        List<String> list = new ArrayList<>();
        
        // These should work
        assertTrue(list instanceof List);
        assertTrue(list instanceof ArrayList);
        assertTrue(list instanceof List<?>);
    }
    
    @Test
    void testArrayCreationAlternatives() {
        // Test that collection-based alternatives work
        List<List<String>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>());
        matrix.get(0).add("test");
        
        assertEquals("test", matrix.get(0).get(0));
    }
    
    @Test
    void testTypeTokenPattern() {
        TypeReference<List<String>> token = new TypeReference<List<String>>() {};
        String typeName = token.getType().getTypeName();
        
        assertTrue(typeName.contains("List"));
        assertTrue(typeName.contains("String"));
    }
}
```

## 📊 **Reifiability Decision Tree**

```
Is the type generic?
├── No → ✅ REIFIABLE (e.g., String, Integer[])
├── Yes → Is it a raw type?
│   ├── Yes → ✅ REIFIABLE (e.g., List, Map)
│   └── No → Is it an unbounded wildcard?
│       ├── Yes → ✅ REIFIABLE (e.g., List<?>, Map<?, ?>)
│       └── No → ❌ NON-REIFIABLE (e.g., List<String>, Map<? extends Number, ?>)
```

## 🎯 **Key Takeaways**

1. **Non-reifiable types lose type arguments at runtime** due to erasure
2. **You cannot use `instanceof` with parameterized types**—use raw types or unbounded wildcards instead
3. **Arrays of parameterized types are prohibited** to maintain type safety
4. **Type tokens and `Class` objects** can preserve some type information
5. **Collections are often better alternatives** to arrays for generic data structures
6. **Varargs with generics generate warnings**—use `@SafeVarargs` when appropriate
7. **Reflection can sometimes access generic signatures** through `getGenericXxx()` methods

## 🛡️ **Best Practices**

1. **Prefer collections over arrays** when working with generics
2. **Use type tokens** when you need runtime type information
3. **Avoid raw types** except in `instanceof` checks and legacy code
4. **Be explicit with unchecked casts** and add `@SuppressWarnings` with comments
5. **Consider design alternatives** that don't rely on runtime type information
6. **Test generic code thoroughly** to catch type safety issues

Understanding non-reifiable types is crucial for writing correct generic code and avoiding the pitfalls of Java's type erasure model. While these restrictions might seem limiting, they're essential for maintaining type safety and backward compatibility.