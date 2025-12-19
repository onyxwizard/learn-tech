# Chapter 9: ❓ **Wildcards**

## The Unknown Type: Embracing Flexibility with Type Safety

Wildcards (`?`) represent unknown types in Java generics. They provide the flexibility to write methods that can operate on collections of different types while maintaining type safety. Wildcards are the key to understanding and implementing the PECS principle (Producer Extends, Consumer Super).

## ❔ **The Unknown Type: `List<?>`**

### Basic Wildcard Usage

```java
import java.util.*;

public class BasicWildcards {
    
    // Method that accepts any List, regardless of element type
    public static void printList(List<?> list) {
        for (Object obj : list) {
            System.out.print(obj + " ");
        }
        System.out.println();
    }
    
    // Method that returns a List of unknown type
    public static List<?> getList() {
        return Arrays.asList("A", "B", "C");
    }
    
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("Hello", "World");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        
        // All of these work with the wildcard method
        printList(strings);   // Prints: Hello World 
        printList(integers);  // Prints: 1 2 3 
        printList(doubles);   // Prints: 1.1 2.2 3.3 
        
        // Getting a List<?> - can only read as Object
        List<?> unknownList = getList();
        Object first = unknownList.get(0);  // OK
        // String firstStr = unknownList.get(0);  // ❌ Compile error
    }
}
```

### What You CAN and CANNOT Do with Unbounded Wildcards

```java
public class WildcardCapabilities {
    
    public static void demonstrate() {
        List<String> strings = new ArrayList<>();
        strings.add("Hello");
        strings.add("World");
        
        // Assign List<String> to List<?>
        List<?> wildcardList = strings;
        
        // ✅ CAN DO:
        // 1. Read elements as Object
        Object obj = wildcardList.get(0);
        
        // 2. Check size, emptiness
        int size = wildcardList.size();
        boolean empty = wildcardList.isEmpty();
        
        // 3. Remove elements
        wildcardList.remove(0);
        
        // 4. Clear the list
        wildcardList.clear();
        
        // 5. Iterate (as Object)
        for (Object item : wildcardList) {
            System.out.println(item);
        }
        
        // ❌ CANNOT DO:
        // 1. Add elements (except null)
        // wildcardList.add("New");      // ❌ Compile error
        // wildcardList.add(new Object());// ❌ Compile error
        wildcardList.add(null);          // ✅ Only null is allowed
        
        // 2. Call methods that take generic parameters
        // wildcardList.contains("Hello"); // ✅ This actually works!
        // Contains works because it takes Object, not E
        
        // 3. Create array of wildcard type
        // List<?>[] array = new List<?>[10]; // ⚠️ Possible but tricky
    }
    
    // Common misconception: wildcard vs raw type
    public static void wildcardVsRaw() {
        List<String> strings = Arrays.asList("A", "B", "C");
        
        // Raw type - unsafe
        List raw = strings;
        raw.add(123);  // ❌ Corrupts the list!
        
        // Wildcard - safe
        List<?> wild = strings;
        // wild.add("D");  // ❌ Compile error - prevents corruption
    }
}
```

## 🔄 **Flexibility in Method Signatures**

### Why Use Wildcards Instead of Type Parameters?

```java
public class WildcardVsTypeParam {
    
    // Approach 1: Using type parameter
    public static <T> void printWithTypeParam(List<T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }
    
    // Approach 2: Using wildcard
    public static void printWithWildcard(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }
    
    // When to use which?
    public static void comparison() {
        List<String> strings = Arrays.asList("A", "B");
        List<Integer> integers = Arrays.asList(1, 2);
        
        // Both approaches work
        printWithTypeParam(strings);
        printWithWildcard(strings);
        
        // Key differences:
        // 1. Wildcard is simpler when you don't need the type parameter
        // 2. Type parameter is needed when you:
        //    - Need to refer to the type multiple times
        //    - Need to return values of that type
        //    - Need to use the type in multiple parameters
        
        // Example: Type parameter needed
        List<String> result1 = reverseWithTypeParam(strings);
        // List<String> result2 = reverseWithWildcard(strings); // ❌ Can't do this
    }
    
    // Need type parameter when returning the same type
    public static <T> List<T> reverseWithTypeParam(List<T> list) {
        List<T> result = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }
        return result;
    }
    
    // Can't do this with wildcard alone
    // public static List<?> reverseWithWildcard(List<?> list) {
    //     List<?> result = new ArrayList<>(); // What type?
    //     // Can't add to List<?>
    //     return result;
    // }
}
```

## 📖 **Reading vs ✍️ Writing with Wildcards**

### The Fundamental Rule

```java
public class ReadWriteRules {
    
    // RULE: You can READ from a structure with an upper bound (extends)
    //       You can WRITE to a structure with a lower bound (super)
    
    public static void demonstrate() {
        // List with upper bound (producer - read mostly)
        List<? extends Number> numbers = Arrays.asList(1, 2, 3.14, 4L);
        
        // ✅ CAN READ as Number
        Number first = numbers.get(0);
        Double sum = 0.0;
        for (Number n : numbers) {  // Iteration works
            sum += n.doubleValue();
        }
        
        // ❌ CANNOT WRITE (except null)
        // numbers.add(5);      // ❌ Compile error
        // numbers.add(3.14);   // ❌ Compile error
        numbers.add(null);     // ✅ Only null allowed
        
        // List with lower bound (consumer - write mostly)
        List<? super Integer> integers = new ArrayList<Number>();
        
        // ✅ CAN WRITE Integers (or subtypes)
        integers.add(1);
        integers.add(2);
        integers.add(null);
        
        // ❌ CANNOT READ as Integer (only as Object)
        Object obj = integers.get(0);
        // Integer i = integers.get(0);  // ❌ Compile error
        
        // Unbounded wildcard (read as Object, write nothing)
        List<?> anything = Arrays.asList("A", 1, 3.14);
        Object item = anything.get(0);  // ✅ Read as Object
        // anything.add("B");           // ❌ Cannot write
    }
    
    // Practical example: Copy method
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        // src is producer (extends) - we read T from it
        // dest is consumer (super) - we write T to it
        for (T item : src) {
            dest.add(item);
        }
    }
    
    public static void testCopy() {
        List<Number> numbers = new ArrayList<>();
        List<Integer> integers = Arrays.asList(1, 2, 3);
        
        copy(numbers, integers);  // Copy Integers to Numbers
        System.out.println(numbers);  // [1, 2, 3]
    }
}
```

## 🏗️ **Wildcard Types in APIs**

### Collections Framework Examples

```java
public class CollectionsWildcards {
    
    // Common wildcard patterns in Collections API
    
    public static void examples() {
        List<String> strings = Arrays.asList("A", "B", "C");
        
        // 1. Collections.copy() - uses both extends and super
        List<Object> dest = new ArrayList<>();
        Collections.copy(dest, strings);  // dest: ? super T, src: ? extends T
        
        // 2. Collections.sort() - uses super
        List<String> mutableStrings = new ArrayList<>(strings);
        Collections.sort(mutableStrings);  // List<T> where T extends Comparable<? super T>
        
        // 3. Collections.binarySearch()
        int index = Collections.binarySearch(strings, "B");
        
        // 4. Collections.disjoint()
        List<Integer> integers = Arrays.asList(1, 2, 3);
        boolean disjoint = Collections.disjoint(strings, integers);
        
        // 5. Collections.frequency()
        int freq = Collections.frequency(strings, "A");
    }
    
    // Understanding Comparator<? super T>
    public static void comparatorExample() {
        List<String> strings = new ArrayList<>();
        strings.add("Zebra");
        strings.add("Apple");
        strings.add("Banana");
        
        // Comparator<? super String> works with String or any supertype
        Comparator<Object> byLength = Comparator.comparing(Object::toString)
                                               .thenComparing(Object::hashCode);
        
        // This works because Comparator<Object> is a Comparator<? super String>
        strings.sort(byLength);
        
        // Why? Because if we can compare Objects, we can certainly compare Strings
        System.out.println(strings);
    }
    
    // Understanding Collection<? extends E>
    public static void collectionExtendsExample() {
        List<Number> numbers = new ArrayList<>();
        
        // Can add all integers (Integer extends Number)
        List<Integer> integers = Arrays.asList(1, 2, 3);
        numbers.addAll(integers);  // Collection<? extends Number>
        
        // Can add all doubles (Double extends Number)
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
        numbers.addAll(doubles);   // Same method
        
        System.out.println(numbers);  // [1, 2, 3, 1.1, 2.2, 3.3]
    }
}
```

## ⚠️ **Common Wildcard Pitfalls**

### 1. **Wildcard Capture Problem**

```java
public class WildcardCapture {
    
    // ❌ This doesn't compile - wildcard capture error
    // public static void swap(List<?> list, int i, int j) {
    //     ? temp = list.get(i);  // What type is ?
    //     list.set(i, list.get(j));
    //     list.set(j, temp);
    // }
    
    // ✅ Solution 1: Use helper method with type parameter
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }
    
    // Helper method captures the wildcard
    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // ✅ Solution 2: Use raw types (not recommended)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void swapRaw(List list, int i, int j) {
        Object temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("A", "B", "C");
        swap(strings, 0, 2);
        System.out.println(strings);  // [C, B, A]
    }
}
```

### 2. **Cannot Instantiate Wildcard Types**

```java
public class InstantiationProblems {
    
    // ❌ Cannot create instances of wildcard types
    // List<?> list = new ArrayList<?>();  // Compile error
    
    // ✅ Must specify a type or use diamond
    List<?> list1 = new ArrayList<String>();  // OK
    List<?> list2 = new ArrayList<>();        // Creates ArrayList<Object>
    
    // ❌ Cannot create arrays of wildcard parameterized types
    // List<?>[] array = new List<?>[10];  // Warning: unchecked
    
    // ⚠️ But this compiles with warning
    @SuppressWarnings("unchecked")
    List<?>[] array = new List<?>[10];  // Raw type array
    
    // Better approach: Use list of lists
    List<List<?>> listOfLists = new ArrayList<>();
}

// Generic class with wildcard in field
class Container<T> {
    // ❌ Cannot have fields of wildcard type (except as bounds)
    // List<?> items;  // This is actually allowed!
    
    // But initialization is tricky
    private List<?> items = new ArrayList<String>();  // OK
    
    // ❌ Cannot have method with wildcard return type and implementation
    // public List<?> getItems() {
    //     return new ArrayList<?>();  // Can't instantiate
    // }
    
    // ✅ Return a concrete type
    public List<String> getStrings() {
        return new ArrayList<>();
    }
    
    // ✅ Or use type parameter
    public <U> List<U> getList(Class<U> clazz) {
        return new ArrayList<>();
    }
}
```

### 3. **Overloading with Wildcards**

```java
public class OverloadingWildcards {
    
    // ❌ These conflict after type erasure
    // public void process(List<String> list) {}
    // public void process(List<Integer> list) {}
    
    // ✅ Wildcards can help but still have limitations
    public void processStrings(List<? extends String> list) {
        // Works with List<String> but not List<CharSequence>
    }
    
    public void processIntegers(List<? extends Integer> list) {
        // Works with List<Integer> but not List<Number>
    }
    
    // ⚠️ These still conflict - same erasure
    // public void handle(List<?> list) {}
    // public void handle(List<String> list) {}  // ❌ Compile error
    
    // Solution: Use different method names or type parameters
    public <T> void handleGeneric(List<T> list) {
        // Generic implementation
    }
    
    public void handleWildcard(List<?> list) {
        // Wildcard implementation
    }
}
```

## 🧪 **Practical Wildcard Patterns**

### 1. **The SafeVarargs Pattern**

```java
public class SafeVarargsExample {
    
    // @SafeVarargs indicates the method doesn't perform unsafe operations
    @SafeVarargs
    public static <T> List<T> combine(List<? extends T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    
    // Without @SafeVarargs, compiler warns about possible heap pollution
    public static <T> List<T> unsafeCombine(List<T>... lists) {
        // This is actually safe, but compiler doesn't know
        List<T> result = new ArrayList<>();
        for (List<T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("A", "B");
        List<String> moreStrings = Arrays.asList("C", "D");
        
        List<String> combined = combine(strings, moreStrings);
        System.out.println(combined);  // [A, B, C, D]
        
        // Works with different types through common supertype
        List<Integer> integers = Arrays.asList(1, 2);
        List<Double> doubles = Arrays.asList(3.14, 2.71);
        
        List<Number> numbers = combine(integers, doubles);
        System.out.println(numbers);  // [1, 2, 3.14, 2.71]
    }
}
```

### 2. **Wildcard in Return Types**

```java
public class WildcardReturnTypes {
    
    // ❌ Usually avoid wildcards in return types
    // public static List<?> getList() {
    //     return Arrays.asList(1, 2, 3);  // Returns List<Integer> as List<?>
    // }
    
    // Problem: Caller can't do much with List<?>
    public static void problem() {
        List<?> list = getWildcardList();
        // list.add("Hello");  // ❌ Can't add
        // list.add(123);      // ❌ Can't add
        Object obj = list.get(0);  // Only can get as Object
    }
    
    public static List<?> getWildcardList() {
        return Arrays.asList(1, 2, 3);
    }
    
    // ✅ Better: Use type parameter
    public static <T> List<T> getTypedList(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        // Add elements based on clazz
        return list;
    }
    
    // ✅ Or return a bounded wildcard when appropriate
    public static List<? extends Number> getNumbers() {
        // Can return List<Integer>, List<Double>, etc.
        return Arrays.asList(1, 2, 3);  // List<Integer>
    }
    
    // When wildcard return IS appropriate: Factory methods
    public static List<?> emptyList() {
        return Collections.emptyList();  // Returns immutable empty list
    }
    
    public static List<?> unmodifiableList(List<?> list) {
        return Collections.unmodifiableList(list);
    }
}
```

### 3. **Wildcard in Class/Interface Definitions**

```java
// Wildcards in class/interface type parameters have special meaning

// ❌ Can't use wildcard directly in class definition
// class Container<?> {}  // Compile error

// ✅ But can use bounded type parameters
class Container<T extends Number> {
    private T value;
    
    // Can use wildcards in methods
    public boolean isGreaterThan(Container<?> other) {
        // Need to handle unknown type
        return false;
    }
    
    // Better: bounded wildcard
    public boolean isGreaterThanNumber(Container<? extends Number> other) {
        return this.value.doubleValue() > other.value.doubleValue();
    }
}

// Wildcards in interface inheritance
interface Reader<T> {
    T read();
}

// Can implement with specific type
class StringReader implements Reader<String> {
    @Override
    public String read() {
        return "Hello";
    }
}

// Or with wildcard in implementing class (rare)
class WildcardReader implements Reader<?> {
    @Override
    public Object read() {  // Must return Object
        return "Anything";
    }
}
```

## 📊 **Wildcard Type Hierarchy**

```java
public class WildcardHierarchy {
    
    // Understanding the relationships
    
    public static void demonstrate() {
        // Specific types
        List<String> stringList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        
        // Unbounded wildcard accepts any List
        List<?> wildcardList;
        wildcardList = stringList;   // ✅ OK
        wildcardList = integerList;  // ✅ OK
        
        // Upper bounded wildcard
        List<? extends Number> numberList;
        numberList = integerList;            // ✅ OK - Integer extends Number
        // numberList = stringList;          // ❌ String doesn't extend Number
        
        List<Double> doubleList = new ArrayList<>();
        numberList = doubleList;             // ✅ OK - Double extends Number
        
        // Lower bounded wildcard
        List<? super Integer> integerSuperList;
        integerSuperList = integerList;      // ✅ OK - Integer super Integer
        List<Number> numberList2 = new ArrayList<>();
        integerSuperList = numberList2;      // ✅ OK - Number super Integer
        List<Object> objectList = new ArrayList<>();
        integerSuperList = objectList;       // ✅ OK - Object super Integer
        // List<Double> doubleList2 = new ArrayList<>();
        // integerSuperList = doubleList2;   // ❌ Double not super of Integer
    }
    
    // Visualizing the hierarchy
    public static void hierarchy() {
        // For a given type hierarchy:
        // Object
        //   └── Number
        //         ├── Integer
        //         └── Double
        
        // The wildcard type relationships:
        // List<?>           (most general)
        //   ├── List<? super Object>      = List<Object> (concrete)
        //   ├── List<? super Number>      = can be List<Number> or List<Object>
        //   ├── List<? super Integer>     = can be List<Integer>, List<Number>, or List<Object>
        //   ├── List<? extends Object>    = any List
        //   ├── List<? extends Number>    = List<Number>, List<Integer>, List<Double>
        //   └── List<? extends Integer>   = List<Integer>
        
        // Key insight: The "extends" side gets more specific as you go down
        //             The "super" side gets more general as you go down
    }
}
```

## 🎓 **Key Takeaways**

1. **Wildcards (`?`) represent unknown types** and provide flexibility
2. **Three types of wildcards**:
   - `?` (unbounded): Unknown type, read as Object, write nothing
   - `? extends T` (upper bounded): Unknown subtype of T, read as T
   - `? super T` (lower bounded): Unknown supertype of T, write T
3. **PECS Principle** (Producer Extends, Consumer Super) guides usage
4. **Wildcards enable API flexibility** while maintaining type safety
5. **Avoid wildcards in return types** (except for special cases like factories)
6. **Use helper methods** to capture wildcards when needed
7. **Understand the limitations**: can't instantiate, can't use in all contexts

## 🚀 **Next Steps**

Now that you understand basic wildcards, let's dive deeper into the three specific types. In [Chapter 10](#10-upper-bounded-wildcards), we'll explore **Upper Bounded Wildcards** (`? extends T`) - the "producer" wildcards that let you read from generic structures with confidence.

> 💡 **Practice Exercise**: 
> 1. Write a method that takes `List<? extends Number>` and returns the sum as a Double
> 2. Write a method that takes `List<? super Integer>` and adds the numbers 1 through 10 to it
> 3. Create a utility class with methods that demonstrate when to use type parameters vs wildcards
> 4. Fix a wildcard capture error in a swap method using a helper method