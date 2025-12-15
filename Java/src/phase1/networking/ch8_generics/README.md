# 🔤 Java Generics Guide

## 🎯 Introduction to Generics

**Java Generics** enable you to write a single sort method that can sort elements in an `Integer` array, a `String` array, or an array of **any type** that supports ordering! This powerful feature allows for **type-safe** code reuse without sacrificing type checking at compile time.

### 🤔 Why Use Generics?

Generics allow you to create classes, interfaces, and methods with parameters that operate on different data types. Introduced in Java 5, they provide:

- **🚫 No Type-Safety Sacrifice** - Maintain strong type checking
- **🔤 No Type-Casting Required** - Cleaner, more readable code
- **✅ Compile-Time Checking** - Catch errors early
- **♻️ Code Reusability** - Write once, use with multiple types
- **⚡ Improved Performance** - Eliminates runtime type checks

## 📋 Types of Java Generics

### 1. 📝 **Generic Methods**
### 2. 🏗️ **Generic Classes**

## 📝 Generic Methods

Generic methods allow you to write a **single method declaration** that can be called with arguments of different types. The compiler handles each method call appropriately based on the argument types.

### 📜 Rules for Defining Generic Methods

1. **⟨⟩ Angle Brackets**  
   All generic method declarations have a type parameter section delimited by angle brackets (`<` and `>`) that precedes the method's return type.

2. **🔤 Type Parameters**  
   Each type parameter section contains one or more type parameters separated by commas (e.g., `<E>`, `<T, U>`).

3. **🎭 Placeholder Usage**  
   Type parameters can declare the return type and act as placeholders for actual type arguments.

4. **⚠️ Reference Types Only**  
   Type parameters can represent only reference types, not primitive types (like `int`, `double`, `char`).

### 💡 Example: Generic Method

```java
public class GenericMethodTest {
   
   // ✨ Generic method to print any type of array
   public static <E> void printArray(E[] inputArray) {
      // Display array elements
      for (E element : inputArray) {
         System.out.printf("%s ", element);
      }
      System.out.println();
   }

   public static void main(String args[]) {
      // Create arrays of different types
      Integer[] intArray = {1, 2, 3, 4, 5};
      Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
      Character[] charArray = {'H', 'E', 'L', 'L', 'O'};

      System.out.println("📊 Array integerArray contains:");
      printArray(intArray);   // ✅ Pass an Integer array

      System.out.println("\n📊 Array doubleArray contains:");
      printArray(doubleArray);   // ✅ Pass a Double array

      System.out.println("\n📊 Array characterArray contains:");
      printArray(charArray);   // ✅ Pass a Character array
   }
}
```

### 📤 Output:
```
📊 Array integerArray contains:
1 2 3 4 5 

📊 Array doubleArray contains:
1.1 2.2 3.3 4.4 

📊 Array characterArray contains:
H E L L O
```

## ⛓️ Bounded Type Parameters

Sometimes you need to restrict the types that can be passed to a type parameter. For example, a method operating on numbers might only accept instances of `Number` or its subclasses.

### 📏 Syntax
```java
<T extends UpperBoundType>
```
The `extends` keyword here means either "extends" (for classes) or "implements" (for interfaces).

### 💡 Example: Bounded Type Parameters

```java
public class MaximumTest {
   
   // 🔼 Finds the largest of three Comparable objects
   public static <T extends Comparable<T>> T maximum(T x, T y, T z) {
      T max = x;   // Assume x is initially the largest
      
      if (y.compareTo(max) > 0) {
         max = y;   // y is the largest so far
      }
      
      if (z.compareTo(max) > 0) {
         max = z;   // z is the largest now
      }
      return max;   // Returns the largest object
   }
   
   public static void main(String args[]) {
      // Compare integers
      System.out.printf("🔢 Max of %d, %d and %d is %d\n\n", 
         3, 4, 5, maximum(3, 4, 5));

      // Compare doubles
      System.out.printf("🔢 Max of %.1f, %.1f and %.1f is %.1f\n\n",
         6.6, 8.8, 7.7, maximum(6.6, 8.8, 7.7));

      // Compare strings
      System.out.printf("🔤 Max of %s, %s and %s is %s\n", "pear",
         "apple", "orange", maximum("pear", "apple", "orange"));
   }
}
```

### 📤 Output:
```
🔢 Max of 3, 4 and 5 is 5

🔢 Max of 6.6, 8.8 and 7.7 is 8.8

🔤 Max of pear, apple and orange is pear
```

## 🏗️ Generic Classes

A **generic class** declaration looks like a non-generic class, except the class name is followed by a type parameter section. These are also called **parameterized classes** or **parameterized types**.

### 🏭 Example: Generic Class

```java
// 📦 Generic Box class that can hold any type of object
public class Box<T> {
   private T content;  // Content of the box
   
   // ➕ Method to add content to the box
   public void add(T content) {
      this.content = content;
   }
   
   // 📤 Method to get content from the box
   public T get() {
      return content;
   }
   
   public static void main(String[] args) {
      // Create boxes for different types
      Box<Integer> integerBox = new Box<Integer>();
      Box<String> stringBox = new Box<String>();
      
      // Add content to boxes
      integerBox.add(10);  // Auto-boxing: int to Integer
      stringBox.add("Hello World");
      
      // Retrieve and display content
      System.out.printf("📦 Integer Value: %d\n\n", integerBox.get());
      System.out.printf("📦 String Value: %s\n", stringBox.get());
   }
}
```

### 📤 Output:
```
📦 Integer Value: 10

📦 String Value: Hello World
```

## 🎭 Multiple Type Parameters

Generic classes and methods can have multiple type parameters:

```java
// 🔤 Generic class with two type parameters
public class Pair<K, V> {
   private K key;
   private V value;
   
   public Pair(K key, V value) {
      this.key = key;
      this.value = value;
   }
   
   public K getKey() { return key; }
   public V getValue() { return value; }
   
   public static void main(String[] args) {
      Pair<String, Integer> student = new Pair<>("Alice", 95);
      System.out.println("👩‍🎓 " + student.getKey() + ": " + student.getValue());
   }
}
```

## 🚫 Type Erasure

Java uses **type erasure** to implement generics. This means:
- Type parameters are removed during compilation
- Generic types become raw types
- Type casts are inserted where necessary
- Bridge methods may be generated

```java
// 🔍 Before compilation (in source code)
List<String> list = new ArrayList<>();

// 🔍 After compilation (type erasure)
List list = new ArrayList();  // Raw type
```

## 🎯 Best Practices

### ✅ Do's:
1. **Use Descriptive Type Parameter Names**  
   ```java
   public class Container<T> {}  // ❌ Not descriptive
   public class Container<ELEMENT> {}  // ✅ Better
   ```

2. **Use Bounds When Necessary**  
   ```java
   public static <T extends Comparable<T>> T max(T a, T b) {}
   ```

3. **Use Diamond Operator (Java 7+)**  
   ```java
   List<String> list = new ArrayList<>();  // ✅
   List<String> list = new ArrayList<String>();  // ❌ Verbose
   ```

### ❌ Don'ts:
1. **Don't Use Raw Types (if possible)**  
   ```java
   List list = new ArrayList();  // ❌ Raw type
   List<String> list = new ArrayList<>();  // ✅ Generic
   ```

2. **Don't Ignore Warnings**  
   Unchecked warnings indicate potential type safety issues.

3. **Don't Create Generic Arrays**  
   ```java
   T[] array = new T[10];  // ❌ Compile error
   List<T> list = new ArrayList<>();  // ✅ Use collections
   ```

## 🔄 Real-World Use Cases

### 1. **Collections Framework**
```java
List<String> names = new ArrayList<>();
Map<Integer, String> employees = new HashMap<>();
Set<Double> prices = new HashSet<>();
```

### 2. **Custom Data Structures**
```java
public class TreeNode<T> {
   private T data;
   private TreeNode<T> left, right;
   // ... methods
}
```

### 3. **Utility Methods**
```java
public class ArrayUtils {
   public static <T> void swap(T[] array, int i, int j) {
      T temp = array[i];
      array[i] = array[j];
      array[j] = temp;
   }
}
```

## 🧪 Practice Exercise

Try creating a generic method that reverses an array of any type:

```java
public class ArrayReverser {
   // 🎯 Your task: Implement a generic reverse method
   public static <T> void reverse(T[] array) {
      // Your code here
   }
   
   public static void main(String[] args) {
      Integer[] numbers = {1, 2, 3, 4, 5};
      String[] words = {"apple", "banana", "cherry"};
      
      reverse(numbers);
      reverse(words);
      
      System.out.println(Arrays.toString(numbers));
      System.out.println(Arrays.toString(words));
   }
}
```

## 📚 Summary

| Feature | Description | Example |
|---------|-------------|---------|
| **Generic Methods** | Methods that can work with different types | `<T> void print(T item)` |
| **Generic Classes** | Classes that can be parameterized with types | `class Box<T>` |
| **Bounded Types** | Restrict type parameters with bounds | `<T extends Number>` |
| **Type Erasure** | Compiler removes type information | Runtime sees raw types |
| **Wildcards** | Unknown types (not covered here) | `List<?>` |

## 🚀 Key Takeaways

1. **🛡️ Type Safety** - Catch errors at compile time
2. **♻️ Reusability** - Write once, use with multiple types
3. **📖 Readability** - Clear type intentions in code
4. **⚡ Performance** - No runtime type checks needed
5. **🔧 Flexibility** - Create adaptable, maintainable code

**✨ Happy Generic Programming with Java! ✨**