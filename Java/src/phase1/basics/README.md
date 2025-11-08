# Chapter 1: 🧱 Creating Variables and Naming Them

In Java, **variables** are the fundamental building blocks that store data values used throughout your program. Whether you're tracking the speed of a bicycle, counting loop iterations, or passing data between methods, understanding how to properly **create** and **name** variables is essential.


## 🔹 What Is a Variable?

In object-oriented programming, an **object** stores its state in **fields**—which are a type of variable. For example:

```java
int cadence = 0;
int speed = 0;
int gear = 1;
```

These lines declare and initialize **fields** that hold the state of a `Bicycle` object. But Java uses the broader term **"variable"** to describe several kinds of data containers, not just fields.



## 🔸 Types of Variables in Java

Java defines **four** distinct kinds of variables, each with its own scope, lifetime, and purpose:

### 1. **Instance Variables (Non-Static Fields)**
- Belong to an **instance** of a class (i.e., an object).
- Declared **without** the `static` keyword.
- Each object has its **own copy** of these variables.
- Example:
  ```java
  public class Bicycle {
      int cadence;  // instance variable
      int speed;
  }
  ```
  → Two `Bicycle` objects can have different `cadence` values.

### 2. **Class Variables (Static Fields)**
- Shared across **all instances** of a class.
- Declared with the `static` keyword.
- Only **one copy** exists in memory, regardless of how many objects are created.
- Often used for constants or global state.
- Example:
  ```java
  static int numGears = 6;
  static final double PI = 3.14159;  // common for constants
  ```

### 3. **Local Variables**
- Declared **inside a method**, constructor, or block.
- Exist only **during the execution** of that method/block.
- **Not accessible** outside their declaring scope.
- Must be **initialized before use** (no default values).
- Example:
  ```java
  public void calculateSpeed() {
      int tempSpeed = 0;  // local variable
      // usable only inside this method
  }
  ```

### 4. **Parameters**
- Variables in a **method or constructor signature**.
- Act as **local variables** initialized with values passed during the call.
- Example:
  ```java
  public static void main(String[] args) {
      // 'args' is a parameter (and thus a variable)
  }

  public void setSpeed(int speed) {
      // 'speed' is a parameter
      this.speed = speed;
  }
  ```

> 💡 **Terminology Note**:
> - The term **"field"** usually refers to **instance or class variables** (i.e., variables declared at the class level).
> - The term **"variable"** is the **umbrella term** covering all four types.
> - **"Member"** refers to fields, methods, and nested types of a class.



## 🔹 Rules and Conventions for Naming Variables

Java enforces **syntactic rules** for identifiers (variable names), and the community follows strong **naming conventions** for readability and maintainability.

### ✅ **Legal Identifier Rules**
A variable name must:
- Be **case-sensitive** (`speed` ≠ `Speed`).
- Start with:
    - A **letter** (`a–z`, `A–Z`), **or**
    - `$` (dollar sign), **or**
    - `_` (underscore).
- Be followed by any combination of:
    - Letters, digits (`0–9`), `$`, or `_`.
- **Not** be a Java **keyword** or **reserved word** (e.g., `int`, `class`, `public`).
- **Not** contain spaces or special characters like `@`, `#`, `!`, etc.

> ⚠️ **Important**: While `$` and `_` are *allowed* at the start, **they are discouraged** in regular code.
> - `$` is typically used in **auto-generated code** (e.g., by frameworks or compilers).
> - `_` at the start is considered poor style in modern Java.

### 📏 **Naming Conventions (Best Practices)**

| Type of Variable       | Convention                                  | Example              |
|------------------------|---------------------------------------------|----------------------|
| **Single-word name**   | All lowercase                               | `speed`, `gear`      |
| **Multi-word name**    | **camelCase**: first word lowercase, subsequent words capitalized | `currentSpeed`, `gearRatio` |
| **Constants** (`static final`) | **UPPER_SNAKE_CASE**: all caps, words separated by underscores | `MAX_SPEED`, `NUM_GEARS` |

### 🚫 What to Avoid
- **Cryptic abbreviations**: `int s;` → unclear. Prefer `int speed;`.
- **Using `$` or leading `_`**: `int $temp;` or `_count` → non-idiomatic.
- **Keywords**: `int class;` → **compilation error**.
- **Single-letter names** (except in trivial loops like `for (int i = 0; ...)`).

### ✅ Good Examples
```java
int cadence;
double gearRatio;
boolean isMoving;
static final int MAX_GEARS = 10;
String userName;
```

### ❌ Bad Examples
```java
int C;               // unclear, uppercase
int _speed;          // leading underscore discouraged
int $value;          // dollar sign discouraged
int 2fast;           // starts with digit → illegal
int class;           // keyword → illegal
```



## 🔍 Key Takeaways

- Java has **four variable types**: instance, static, local, and parameters—each with different scope and lifetime.
- **Instance variables** define object state; **static variables** define class-wide state.
- **Local variables** and **parameters** are temporary and method-scoped.
- Variable names must follow Java’s **identifier rules** and **naming conventions**.
- Use **descriptive, full-word names** in **camelCase** (or **UPPER_SNAKE_CASE** for constants).
- Avoid `$`, `_`, abbreviations, and keywords to write clean, professional Java code.

---
# Chapter 2: 🔢 Creating Primitive Type Variables in Your Programs

Java is a **statically-typed language**, meaning every variable must be **declared with a specific type** before it can be used. This ensures type safety, performance, and clarity. At the heart of Java’s type system are the **eight primitive data types**—fundamental building blocks that represent simple values like numbers, characters, and boolean flags.

In this section, we’ll explore:
- The **eight primitive types** and their characteristics,
- How to **initialize variables** (including default values),
- The role of **literals** in assigning fixed values,
- Special syntax for **numeric bases**, **scientific notation**, and **readability enhancements** like underscores.



## 🔹 The Eight Primitive Data Types

Primitive types are **not objects**—they are built directly into the language and stored **by value**, not by reference. Each has a fixed size and range.

| Type      | Size (bits) | Range / Description                                                                 | Use Case |
|-----------|-------------|--------------------------------------------------------------------------------------|----------|
| `byte`    | 8           | -128 to 127                                                                          | Memory-efficient storage in large arrays |
| `short`   | 16          | -32,768 to 32,767                                                                    | Rare; similar to `byte` but larger |
| `int`     | 32          | -2³¹ to 2³¹ – 1 (≈ ±2 billion)                                                       | **Default** for integer values |
| `long`    | 64          | -2⁶³ to 2⁶³ – 1                                                                      | When `int` isn’t large enough |
| `float`   | 32          | ~6–7 decimal digits of precision (IEEE 754)                                           | Memory-saving floating-point (avoid for precision) |
| `double`  | 64          | ~15–16 decimal digits of precision (IEEE 754)                                         | **Default** for decimal numbers |
| `boolean` | —           | `true` or `false`                                                                    | Logical flags and conditions |
| `char`    | 16          | `\u0000` (0) to `\uffff` (65,535) — full Unicode (UTF-16)                             | Single characters |

> 💡 **Note on Unsigned Integers (Java 8+)**  
> While Java doesn’t have unsigned types as keywords, you can **treat `int` and `long` as unsigned** using utility methods in `Integer` and `Long`:
> ```java
> int unsignedValue = Integer.parseUnsignedInt("4294967295"); // max unsigned int
> long result = Integer.compareUnsigned(a, b);
> ```



## 🔸 The Special Case: `String`

Although **not a primitive**, `String` enjoys **first-class support** in Java:

```java
String s = "this is a string";
```

- Created automatically using **double quotes**.
- **Immutable**: once created, its value cannot change.
- Internally backed by a `char[]`, but abstracted for ease of use.
- Belongs to the `java.lang.String` class (automatically imported).

> 📌 Remember: `String` is a **reference type**, so its default value is `null` (not an empty string).



## 🔹 Default Values for Fields

When you declare a **field** (instance or static variable) without initializing it, Java assigns a **default value**:

| Data Type        | Default Value |
|------------------|---------------|
| `byte`, `short`, `int`, `long` | `0` or `0L` |
| `float`          | `0.0f`        |
| `double`         | `0.0d`        |
| `char`           | `\u0000` (null character) |
| `boolean`        | `false`       |
| Any object (e.g., `String`) | `null` |

### ⚠️ Important Exception: **Local Variables**
- **No default values** are assigned.
- Must be **explicitly initialized** before use.
- Attempting to use an uninitialized local variable causes a **compile-time error**:

```java
public void example() {
    int x;
    System.out.println(x); // ❌ Compilation error: variable x might not have been initialized
}
```

> 🛑 **Best Practice**: Always initialize variables at declaration. Relying on defaults makes code less readable and more error-prone.



## 🔸 Creating Values with Literals

A **literal** is a fixed value written directly in your code. No `new` keyword is needed for primitives—just assign the literal:

```java
boolean result = true;
char capitalC = 'C';
byte b = 100;
short s = 10000;
int i = 100000;
long l = 100000L;      // note the 'L'
float f = 123.45f;     // note the 'f'
double d = 123.45;     // 'd' is optional
String name = "Java";  // string literal
```



## 🔹 Integer Literals: Bases and Suffixes

### 🔢 Number Systems
You can write integer literals in multiple bases:

| Base       | Prefix | Example (value = 26)     |
|------------|--------|--------------------------|
| Decimal    | none   | `26`                     |
| Hexadecimal| `0x`   | `0x1a` or `0X1A`         |
| Binary     | `0b`   | `0b11010` (Java 7+)      |

### 🏷️ Type Suffixes
- **`int`**: default for integer literals.
- **`long`**: append `L` or `l` (prefer **`L`**—`l` looks like `1`):

```java
long bigNumber = 1234567890123L; // required if value > int max
```



## 🔸 Floating-Point Literals

- **`double`**: default for decimals.
- **`float`**: append `F` or `f`.
- Use **scientific notation** with `e` or `E`:

```java
double d1 = 123.4;
double d2 = 1.234e2;    // same as 123.4
float f1  = 123.4f;
double d3 = 1.234E2;    // uppercase E also valid
```

> ⚠️ **Never use `float`/`double` for currency!**  
> Use `java.math.BigDecimal` for precise decimal arithmetic (e.g., financial calculations).



## 🔹 Character and String Literals

### ✅ Syntax Rules
- `char`: **single quotes** → `'A'`, `'\n'`, `'\u0108'`
- `String`: **double quotes** → `"Hello"`, `"S\u00ED Se\u00F1or"`

### 🔤 Escape Sequences
Special characters are represented with backslashes:

| Escape | Meaning         |
|--------|-----------------|
| `\n`   | Line feed       |
| `\t`   | Tab             |
| `\"`   | Double quote    |
| `\'`   | Single quote    |
| `\\`   | Backslash       |
| `\uXXXX` | Unicode character (e.g., `\u0041` = `'A'`) |

### 🚫 The `null` Literal
- Can be assigned to **any reference type** (e.g., `String`, `Object`).
- **Cannot** be assigned to primitives.
- Used to indicate "no object":

```java
String s = null; // valid
int x = null;    // ❌ compilation error
```

### 🧩 Class Literals
Special syntax to get the `Class` object of a type:

```java
Class<String> stringClass = String.class;
```

Useful in reflection, generics, and logging.



## 🔸 Numeric Literals with Underscores (Java 7+)

To improve readability of large numbers, insert `_` **between digits**:

```java
long creditCard = 1234_5678_9012_3456L;
float pi = 3.14_15F;
int million = 1_000_000;
long bytes = 0b1101_0010_0110_1001;
```

### ✅ Valid Placement
- Only **between digits**.
- Works in **decimal, hex, binary, and floating-point** literals.

### ❌ Invalid Placement
```java
int x = _52;        // ❌ at start
int y = 52_;        // ❌ at end
float z = 3_.14f;   // ❌ adjacent to decimal point
long n = 999_L;     // ❌ before suffix
int h = 0x_52;      // ❌ after 0x prefix
```

> 💡 The compiler **ignores underscores**—they exist only for human readability.



## 🔍 Key Takeaways

- Java has **8 primitive types**: 4 integer (`byte`, `short`, `int`, `long`), 2 floating-point (`float`, `double`), 1 character (`char`), and 1 boolean (`boolean`).
- **`String`** is not primitive but is treated specially with literal syntax.
- **Fields** get default values; **local variables do not**—must be initialized before use.
- Use **literals** to assign fixed values directly in code.
- Leverage **binary/hex literals**, **scientific notation**, and **underscores** for clarity.
- **Avoid `float`/`double` for precise values**—use `BigDecimal` instead.
- Follow **naming and style conventions** to write clean, maintainable code.

---
# Chapter 3: 📦 Creating Arrays in Your Programs

An **array** is a fundamental data structure in Java that stores a **fixed number of elements** of the **same type** in a contiguous block of memory. Once created, an array’s **length cannot change**, making it ideal for scenarios where the number of items is known in advance.

Arrays are **zero-indexed**, meaning the first element is at index `0`, the second at index `1`, and so on. This section covers everything you need to know about declaring, initializing, accessing, copying, and manipulating arrays—including **multidimensional (jagged) arrays** and utility methods from the `Arrays` class.



## 🔹 Declaring an Array Variable

To declare an array, specify the **element type** followed by **square brackets `[]`**, then the variable name:

```java
int[] numbers;          // preferred style
String[] names;
double[] prices;
```

> ✅ **Best Practice**: Place the brackets **after the type**, not the variable name.  
> ❌ Discouraged: `int numbers[];` (C-style syntax—legal but non-idiomatic in Java).

You can declare arrays of **any type**, including primitives and objects:

```java
byte[] bytes;
boolean[] flags;
char[] letters;
String[] words;  // array of String objects
```

> ⚠️ **Note**: Declaration **does not create** the array—it only tells the compiler that this variable will hold an array reference.



## 🔸 Creating and Initializing Arrays

### 1. **Using `new`**
Allocate memory for a fixed number of elements:

```java
int[] numbers = new int[5];  // creates array of 5 ints (all default to 0)
```

Then assign values by index:

```java
numbers[0] = 10;
numbers[1] = 20;
// ... up to numbers[4]
```

### 2. **Array Initializer (Shorthand)**
Initialize at declaration with a **comma-separated list** inside braces:

```java
int[] numbers = {10, 20, 30, 40, 50};
String[] fruits = {"apple", "banana", "cherry"};
```

> 🔍 The compiler **infers the length** from the number of elements.



## 🔹 Accessing Array Elements

Use **zero-based indexing** to read or modify elements:

```java
int first = numbers[0];   // reads first element
numbers[2] = 99;          // updates third element
```

> ⚠️ **Bounds Check**: Accessing an index outside `[0, length - 1]` throws `ArrayIndexOutOfBoundsException`.

### 🔁 Iterating Over Arrays

#### Standard `for` loop:
```java
for (int i = 0; i < numbers.length; i++) {
        IO.println("Index " + i + ": " + numbers[i]);
}
```

#### Enhanced `for` loop (for-each):
```java
for (int value : numbers) {
        IO.println("Value: " + value);
}
```

> ✅ Use **enhanced for** when you don’t need the index.



## 🔸 Multidimensional Arrays (Arrays of Arrays)

Java supports **multidimensional arrays** by creating arrays whose elements are **other arrays**.

### Declaration:
```java
int[][] matrix;        // 2D array of ints
String[][] names;      // 2D array of Strings
```

### Initialization:
```java
int[][] matrix = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
};
```

### Jagged (Irregular) Arrays
Rows can have **different lengths**:

```java
String[][] schedule = {
        {"Math", "Science"},
        {"History", "Art", "PE", "Music"},
        {"Study Hall"}
};
```

### Accessing Elements:
```java
IO.println(matrix[0][2]);      // 3
IO.println(schedule[1][3]);    // "Music"
```

### Iterating Over 2D Arrays:
```java
for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
        IO.print(matrix[i][j] + " ");
    }
            IO.println();
}
```

> 💡 Always use `.length` on **each sub-array** (`matrix[i].length`)—rows may vary in size!



## 🔹 The `length` Property

Every array has a **public final field** called `length`:

```java
int size = numbers.length;  // returns 5
```

Use it to safely iterate, especially with jagged arrays:

```java
for (int i = 0; i < strings.length; i++) {
        for (int j = 0; j < strings[i].length; j++) {
        IO.print(strings[i][j] + " ");
    }
            IO.println();
}
```



## 🔸 Copying Arrays

### 1. **Manual Copy (Loop)**
```java
int[] original = {1, 2, 3, 4, 5};
int[] copy = new int[original.length];
for (int i = 0; i < original.length; i++) {
copy[i] = original[i];
        }
```

### 2. **`System.arraycopy()`** (Efficient, native)
```java
System.arraycopy(src, srcPos, dest, destPos, length);
```

Example:
```java
String[] coffees = {"Espresso", "Latte", "Cappuccino", "Mocha"};
String[] favorites = new String[2];
System.arraycopy(coffees, 1, favorites, 0, 2);  // copies "Latte", "Cappuccino"
```

### 3. **`Arrays.copyOf()` and `Arrays.copyOfRange()`** (Convenient)
```java
int[] full = {10, 20, 30, 40, 50};
int[] firstThree = Arrays.copyOf(full, 3);               // [10, 20, 30]
int[] middle = Arrays.copyOfRange(full, 1, 4);           // [20, 30, 40] (end index exclusive)
```

> ✅ These methods **return a new array**—no need to pre-allocate.



## 🔹 Powerful Array Utilities: `java.util.Arrays`

The `Arrays` class provides static methods for common operations:

| Method | Purpose |
|-------|--------|
| `Arrays.toString(arr)` | Converts array to readable string: `[1, 2, 3]` |
| `Arrays.equals(arr1, arr2)` | Compares two arrays for equality |
| `Arrays.sort(arr)` | Sorts array in **ascending order** |
| `Arrays.parallelSort(arr)` | Parallel sort (faster for large arrays on multi-core systems) |
| `Arrays.fill(arr, value)` | Sets **all elements** to the same value |
| `Arrays.binarySearch(arr, key)` | Searches **sorted** array for a value (returns index or negative insertion point) |
| `Arrays.stream(arr)` | Creates a **Stream** for functional-style operations |

### Examples:
```java
int[] nums = {5, 2, 8, 1};
Arrays.sort(nums);  // [1, 2, 5, 8]
IO.println(Arrays.toString(nums));  // prints: [1, 2, 5, 8]

        Arrays.fill(nums, 0);  // [0, 0, 0, 0]

int index = Arrays.binarySearch(nums, 2);  // only works if sorted!
```

### Using Streams:
```java
String[] words = {"Java", "Python", "C++"};
Arrays.stream(words)
      .map(w -> w.toUpperCase())
        .forEach(IO::println);
```



## 🔸 Key Characteristics of Java Arrays

- **Fixed size**: Length is set at creation and cannot change.
- **Homogeneous**: All elements must be of the **same type**.
- **Zero-indexed**: First element at index `0`.
- **Reference type**: Arrays are **objects**, so:
    - Default value for array fields is `null`.
    - Stored on the **heap**.
    - Can be passed to methods (by reference).
- **Jagged support**: Multidimensional arrays can have rows of different lengths.



## 🔍 Key Takeaways

- Declare arrays with `Type[] name;` (preferred style).
- Initialize using `new Type[size]` or `{val1, val2, ...}`.
- Access elements via **zero-based indices**.
- Use `.length` to get the number of elements.
- Multidimensional arrays are **arrays of arrays**—they can be **jagged**.
- Copy arrays using `System.arraycopy()`, `Arrays.copyOf()`, or manual loops.
- Leverage `java.util.Arrays` for sorting, searching, filling, and string conversion.
- Enhanced `for` loops simplify iteration when indices aren’t needed.

> 📌 **Remember**: Arrays are **not dynamic**. For resizable collections, consider `ArrayList` (covered in Collections).

---
# Chapter 4: 🪄 Using the var Type Identifier

Introduced in **Java SE 10**, the `var` keyword enables **local variable type inference**—a feature that allows the Java compiler to **automatically determine the type** of a local variable based on its initializer. This reduces boilerplate code while maintaining **strong static typing**.

> 💡 **Important**: `var` is **not** a keyword in the traditional sense (like `int` or `class`). It’s a **reserved type identifier** that can only be used in specific contexts.



## 🔹 Why Use `var`?

Consider this traditional declaration:

```java
String message = "Hello world!";
Path path = Path.of("debug.log");
InputStream stream = Files.newInputStream(path);
```

The types (`String`, `Path`, `InputStream`) are **redundant** because they’re already evident from the right-hand side of the assignment. With `var`, you can simplify:

```java
var message = "Hello world!";           // inferred as String
var path = Path.of("debug.log");        // inferred as Path
var stream = Files.newInputStream(path); // inferred as InputStream
```

✅ **Benefits**:
- **Less verbose** code.
- **Easier refactoring** (e.g., changing a method return type doesn’t require updating variable declarations).
- **Improved readability**—especially with complex generic types.



## 🔸 Where Can You Use `var`?

`var` is **only allowed** for **local variables** in the following contexts:
- Inside **methods**
- Inside **constructors**
- Inside **initializer blocks** (static or instance)

### ✅ Valid Examples

#### 1. **Basic Local Variables**
```java
var count = 10;                    // int
var name = "Java";                 // String
var pi = 3.14;                     // double
var isActive = true;               // boolean
```

#### 2. **Enhanced `for` Loops**
```java
var fruits = List.of("apple", "banana", "cherry");
for (var fruit : fruits) {         // fruit inferred as String
        IO.println(fruit);
}
```

#### 3. **Try-with-Resources**
```java
var path = Path.of("data.txt");
try (var reader = Files.newBufferedReader(path)) { // reader inferred as BufferedReader
String line = reader.readLine();
}
```

#### 4. **Complex Generic Types**
```java
// Without var:
Map<String, List<Integer>> map = new HashMap<>();

// With var:
var map = new HashMap<String, List<Integer>>();
```

> 🌟 This is where `var` shines—avoiding repetitive, hard-to-read declarations.



## 🔹 Restrictions on `var`

While powerful, `var` comes with **strict rules** to preserve type safety and clarity.

### ❌ 1. **Not Allowed for Fields**
```java
public class User {
    private var name = "Sue";  // ❌ COMPILATION ERROR
}
```
> **Reason**: Field types must be explicit in class structure—`var` is only for local scope.

### ❌ 2. **Not Allowed for Method/Constructor Parameters**
```java
public void setName(var name) {  // ❌ COMPILATION ERROR
    this.name = name;
}
```
> **Reason**: Method signatures must be unambiguous in APIs and bytecode.

### ❌ 3. **Must Be Initialized at Declaration**
```java
public String greet(int time) {
    var greeting;  // ❌ COMPILATION ERROR: missing initializer
    if (time < 12) {
        greeting = "morning";
    } else {
        greeting = "afternoon";
    }
    return "Good " + greeting;
}
```

✅ **Fix**: Initialize at declaration (even with a default):
```java
var greeting = "";  // now inferred as String
if (time < 12) {
greeting = "morning";
        } else {
greeting = "afternoon";
        }
```

### ❌ 4. **Cannot Infer from `null` Alone**
```java
var data = null;  // ❌ COMPILATION ERROR: null has no type
```

✅ **Fix**: Cast or use a typed expression:
```java
var data = (String) null;  // explicitly String
```

### ❌ 5. **Not Allowed in Lambda Expressions or Array Initializers Alone**
```java
var func = (var x) -> x * 2;      // ❌ var not allowed in lambda parameters
var arr = {1, 2, 3};              // ❌ array initializer requires explicit type
```

✅ **Correct array declaration**:
```java
var arr = new int[]{1, 2, 3};     // ✅ OK
```



## 🔸 Best Practices for Using `var`

### ✅ **Do Use `var` When**:
- The type is **obvious** from the initializer.
- It **reduces clutter** without sacrificing clarity.
- Working with **complex generics** or **builder patterns**.

```java
var users = userRepository.findAll(); // clear if method name is descriptive
var config = new DatabaseConfig.Builder().withHost("localhost").build();
```

### ❌ **Avoid `var` When**:
- The initializer is **not informative**.
- It **reduces readability**.

```java
// ❌ Unclear what type 'result' is
var result = compute();

// ✅ Better: explicit type adds clarity
List<Transaction> result = compute();
```

> 📏 **Rule of Thumb**: If a reader can’t guess the type within 2 seconds, **don’t use `var`**.



## 🔹 How Type Inference Works

The compiler uses the **initializer expression** to determine the type:
- `"text"` → `String`
- `42` → `int`
- `List.of("a", "b")` → `List<String>`
- `new ArrayList<>()` → `ArrayList<E>` (with diamond operator)

> 🔒 Once inferred, the type is **fixed**—you cannot reassign a different type:
```java
var x = 10;   // int
x = "hello";  // ❌ COMPILATION ERROR: incompatible types
```


## 🔍 Key Takeaways

- `var` enables **local variable type inference** (Java 10+).
- It **reduces verbosity** while preserving **static typing**.
- **Only valid for local variables**—not fields, parameters, or return types.
- **Must be initialized** at declaration; cannot infer from `null` alone.
- Use it to **improve readability**, not obscure it.
- Works seamlessly with **enhanced for loops**, **try-with-resources**, and **generics**.

> 🛑 Remember: `var` does **not** make Java dynamically typed. The type is resolved **at compile time** and never changes.

---
# Chapter 5: ⚙️ Using Operators in Your Programs

Operators are **special symbols** in Java that perform operations on **operands** (variables and values) and return a result. They form the backbone of expressions—from simple arithmetic to complex logical decisions.

Java provides a rich set of operators, categorized by function and **precedence** (order of evaluation). Understanding them is essential for writing correct and efficient code.



## 🔹 Operator Precedence Overview

Operators are evaluated based on **precedence**—higher-precedence operators are evaluated first. Here’s the hierarchy (top = highest precedence):

| Category             | Operators |
|----------------------|----------|
| **Postfix**          | `expr++`, `expr--` |
| **Unary**            | `++expr`, `--expr`, `+`, `-`, `~`, `!` |
| **Multiplicative**   | `*`, `/`, `%` |
| **Additive**         | `+`, `-` |
| **Shift**            | `<<`, `>>`, `>>>` |
| **Relational**       | `<`, `>`, `<=`, `>=`, `instanceof` |
| **Equality**         | `==`, `!=` |
| **Bitwise AND**      | `&` |
| **Bitwise XOR**      | `^` |
| **Bitwise OR**       | `\|` |
| **Logical AND**      | `&&` |
| **Logical OR**       | `\|\|` |
| **Ternary**          | `? :` |
| **Assignment**       | `=`, `+=`, `-=`, `*=`, `/=`, etc. |

> 📌 **Associativity**:
> - Most binary operators evaluate **left to right**.
> - Assignment operators evaluate **right to left**.



## 🔸 1. The Simple Assignment Operator (`=`)

The most basic operator—assigns a value to a variable:

```java
int speed = 0;
String name = "Java";
```

- Also used to assign **object references** (not values):
  ```java
  Bicycle bike1 = new Bicycle();
  Bicycle bike2 = bike1; // both refer to the same object
  ```



## 🔸 2. Arithmetic Operators

Perform mathematical operations on numeric types:

| Operator | Name             | Example        | Result |
|---------|------------------|----------------|--------|
| `+`     | Addition         | `5 + 3`        | `8`    |
| `-`     | Subtraction      | `5 - 3`        | `2`    |
| `*`     | Multiplication   | `5 * 3`        | `15`   |
| `/`     | Division         | `5 / 2`        | `2` (integer division) |
| `%`     | Remainder (Modulo)| `5 % 2`       | `1`    |

### 🔁 Compound Assignment
Combine operation + assignment:

```java
x += 5;   // equivalent to x = x + 5;
y *= 2;   // y = y * 2;
```

### ➕ String Concatenation
The `+` operator **concatenates strings**:

```java
String greeting = "Hello" + " " + "World"; // "Hello World"
```

> 💡 If one operand is a `String`, `+` performs concatenation—even with numbers:
> ```java
> String s = "Result: " + 10; // "Result: 10"
> ```



## 🔸 3. Unary Operators

Operate on a **single operand**:

| Operator | Description                     | Example         |
|---------|----------------------------------|-----------------|
| `+`     | Unary plus (rarely used)         | `+5` → `5`      |
| `-`     | Unary minus (negation)           | `-5` → `-5`     |
| `++`    | Increment by 1                   | `x++` or `++x`  |
| `--`    | Decrement by 1                   | `x--` or `--x`  |
| `!`     | Logical NOT (inverts boolean)    | `!true` → `false` |

### 🔁 Prefix vs. Postfix Increment/Decrement

| Form      | Behavior |
|-----------|--------|
| `++x`     | **Prefix**: increment **then** return value |
| `x++`     | **Postfix**: return value **then** increment |

```java
int a = 3;
IO.println(++a); // 4 (a is now 4)
IO.println(a++); // 4 (a is now 5)
IO.println(a);   // 5
```

> ✅ Use postfix/prefix carefully in expressions—order affects results!



## 🔸 4. Equality and Relational Operators

Compare values and return `boolean` results:

| Operator | Meaning           | Example (`a=1, b=2`) |
|---------|--------------------|----------------------|
| `==`    | Equal to           | `a == b` → `false`   |
| `!=`    | Not equal to       | `a != b` → `true`    |
| `>`     | Greater than       | `a > b` → `false`    |
| `<`     | Less than          | `a < b` → `true`     |
| `>=`    | Greater or equal   | `a >= b` → `false`   |
| `<=`    | Less or equal      | `a <= b` → `true`    |

> ⚠️ **Critical**: Use `==` for **primitives**, but **not for objects** (use `.equals()` instead for content comparison).



## 🔸 5. Conditional (Logical) Operators

Work with `boolean` expressions:

| Operator | Name             | Behavior |
|---------|------------------|--------|
| `&&`    | Conditional AND  | **Short-circuits**: if left is `false`, right is **not evaluated** |
| `\|\|`  | Conditional OR   | **Short-circuits**: if left is `true`, right is **not evaluated** |

### Example:
```java
if ((x > 0) && (y < 10)) { ... }
        if ((x == 0) || (name.equals("admin"))) { ... }
```

> ✅ Short-circuiting prevents errors (e.g., `null` checks):
> ```java
> if (str != null && str.length() > 0) { ... }
> ```

### Ternary Operator (`? :`)
Shorthand for `if-else`:

```java
int max = (a > b) ? a : b;
String status = isLoggedIn ? "Active" : "Guest";
```

> ✅ Use only for **simple, side-effect-free** expressions.



## 🔸 6. The `instanceof` Operator

Tests if an object is an **instance of a class, subclass, or interface**:

```java
Object obj = new String("test");
if (obj instanceof String) {
String s = (String) obj; // safe cast
}
```

### Key Points:
- Returns `false` if the object is `null`.
- Works with **inheritance** and **interfaces**.

```java
Parent p = new Child();
IO.println(p instanceof Parent);     // true
IO.println(p instanceof Child);      // true
IO.println(p instanceof MyInterface); // true (if Child implements it)
```



## 🔸 7. Bitwise and Bit Shift Operators (Less Common)

Used for **low-level programming**, flags, or performance-critical code.

### Bitwise Operators (on integers):
| Operator | Operation        | Example (`5 & 3`) |
|---------|------------------|-------------------|
| `&`     | AND              | `0101 & 0011 = 0001` → `1` |
| `\|`    | OR               | `0101 \| 0011 = 0111` → `7` |
| `^`     | XOR              | `0101 ^ 0011 = 0110` → `6` |
| `~`     | NOT (complement) | `~5` (inverts all bits) |

### Bit Shift Operators:
| Operator | Description                     | Example |
|---------|----------------------------------|--------|
| `<<`    | Left shift (fills with 0)        | `5 << 1` → `10` (`101` → `1010`) |
| `>>`    | Signed right shift (sign-extended)| `-8 >> 1` → `-4` |
| `>>>`   | Unsigned right shift (fills with 0)| `-8 >>> 1` → large positive number |

### Practical Use:
```java
int bitmask = 0x000F; // 0000 0000 0000 1111
int value = 0x2222;   // 0010 0010 0010 0010
IO.println(value & bitmask); // 2 (only last 4 bits)
```

> 🛑 Most application developers **rarely use** these—reserved for systems programming, cryptography, or embedded logic.



## 🔍 Key Takeaways

- **Assignment (`=`)** stores values; **arithmetic (`+ - * / %`)** computes them.
- **Unary operators** (`++`, `--`, `!`) act on single operands—mind prefix vs. postfix!
- **Relational (`<`, `>`, `==`, etc.)** and **logical (`&&`, `||`)** operators return `boolean`.
- **Ternary (`? :`)** is a compact `if-else` for simple assignments.
- **`instanceof`** safely checks object types before casting.
- **Bitwise operators** manipulate individual bits—powerful but niche.
- **Operator precedence** dictates evaluation order—use parentheses `()` to clarify or override.

> 💡 **Pro Tip**: When in doubt about precedence, **use parentheses** to make your intent explicit:
> ```java
> if ((a + b) * c > d) { ... } // clearer than a + b * c > d
> ```

---
# Chapter 6: 📋 Summary of Operators

This section provides a **concise reference** for all Java operators covered so far. Use this as a quick lookup guide to understand what each operator does, its category, and typical use cases.



## 🔹 1. Simple Assignment Operator

| Operator | Description |
|---------|-------------|
| `=`     | Assigns the value on the right to the variable on the left. |

**Example**:
```java
int x = 10;
String name = "Java";
```

> 💡 Also used for **object reference assignment** (not value copying).



## 🔸 2. Arithmetic Operators

Used for **mathematical calculations** on numeric types.

| Operator | Description |
|---------|-------------|
| `+`     | Addition (also **string concatenation**) |
| `-`     | Subtraction |
| `*`     | Multiplication |
| `/`     | Division |
| `%`     | Remainder (modulo) |

**Examples**:
```java
int sum = 5 + 3;        // 8
int remainder = 10 % 3; // 1
String message = "Hello" + " World"; // "Hello World"
```

> ⚠️ Integer division truncates: `7 / 2 = 3` (not `3.5`).



## 🔸 3. Unary Operators

Operate on a **single operand**.

| Operator | Description |
|---------|-------------|
| `+`     | Unary plus (rarely used; indicates positive value) |
| `-`     | Unary minus (negates the value) |
| `++`    | Increment by 1 |
| `--`    | Decrement by 1 |
| `!`     | Logical NOT (inverts a `boolean`) |

**Examples**:
```java
int a = -5;           // unary minus
boolean flag = !true; // false
a++;                  // post-increment
```

> 🔁 **Prefix (`++x`)** vs **Postfix (`x++`)** matters in expressions!



## 🔸 4. Equality and Relational Operators

Compare two values and return a **`boolean`** result.

| Operator | Description |
|---------|-------------|
| `==`    | Equal to |
| `!=`    | Not equal to |
| `>`     | Greater than |
| `>=`    | Greater than or equal to |
| `<`     | Less than |
| `<=`    | Less than or equal to |

**Examples**:
```java
if (age >= 18) { ... }
        if (name != null) { ... }
```

> ⚠️ For **objects**, `==` compares **references**, not content. Use `.equals()` for value comparison.



## 🔸 5. Conditional (Logical) Operators

Combine or invert boolean expressions.

| Operator | Description |
|---------|-------------|
| `&&`    | Conditional-AND (**short-circuits**) |
| `\|\|`  | Conditional-OR (**short-circuits**) |
| `?:`    | Ternary operator (shorthand for `if-else`) |

**Examples**:
```java
if (x > 0 && y < 10) { ... }
String status = isLoggedIn ? "Active" : "Guest";
```

> ✅ Short-circuiting prevents unnecessary evaluations (e.g., avoids `NullPointerException`).



## 🔸 6. Type Comparison Operator

| Operator | Description |
|---------|-------------|
| `instanceof` | Tests if an object is an instance of a class, subclass, or interface. |

**Example**:
```java
if (obj instanceof String) {
String s = (String) obj; // safe cast
}
```

> 📌 Returns `false` if the object is `null`.



## 🔸 7. Bitwise and Bit Shift Operators

Perform operations at the **bit level** (mostly for low-level programming).

### Bitwise Operators:
| Operator | Description |
|---------|-------------|
| `~`     | Unary bitwise complement (inverts all bits) |
| `&`     | Bitwise AND |
| `^`     | Bitwise XOR (exclusive OR) |
| `\|`    | Bitwise OR (inclusive OR) |

### Bit Shift Operators:
| Operator | Description |
|---------|-------------|
| `<<`    | Signed left shift (fills with 0) |
| `>>`    | Signed right shift (sign-extended) |
| `>>>`   | Unsigned right shift (fills with 0) |

**Example**:
```java
int result = 0b1010 & 0b1100; // 0b1000 → 8
int shifted = 4 << 1;         // 8 (4 * 2)
```

> 🛑 Rarely used in general application development—common in embedded systems, cryptography, or performance tuning.



## 🔍 Operator Precedence Recap (High → Low)

1. **Postfix**: `expr++`, `expr--`
2. **Unary**: `++expr`, `--expr`, `+`, `-`, `~`, `!`
3. **Multiplicative**: `*`, `/`, `%`
4. **Additive**: `+`, `-`
5. **Shift**: `<<`, `>>`, `>>>`
6. **Relational**: `<`, `>`, `<=`, `>=`, `instanceof`
7. **Equality**: `==`, `!=`
8. **Bitwise AND**: `&`
9. **Bitwise XOR**: `^`
10. **Bitwise OR**: `\|`
11. **Logical AND**: `&&`
12. **Logical OR**: `\|\|`
13. **Ternary**: `? :`
14. **Assignment**: `=`, `+=`, `-=`, etc.

> ✅ **Best Practice**: Use **parentheses** to clarify intent when precedence is unclear.



## ✅ Final Tips

- Prefer **readability** over cleverness.
- Use `var` (Java 10+) to reduce verbosity **without sacrificing clarity**.
- Always use `.equals()` for **object content comparison**, not `==`.
- Leverage **short-circuiting** (`&&`, `||`) for safe and efficient conditions.
- Avoid bitwise operators unless you **truly need** them.

This summary serves as your **go-to cheat sheet** for Java operators—bookmark it for quick reference!

---
# Chapter 7: 🧩 Expressions, Statements, and Blocks

Understanding how code is structured at the syntactic level is essential for writing correct and readable Java programs. This section explains the three foundational building blocks of Java code:

- **Expressions**: pieces of code that **produce a value**.
- **Statements**: complete units of **execution** (like sentences).
- **Blocks**: groups of statements enclosed in **braces `{}`**, treated as a single unit.



## 🔹 Expressions

An **expression** is any valid combination of:
- **Variables**
- **Operators**
- **Method calls**
- **Literals**

…that **evaluates to a single value** of a specific type.

### ✅ Examples of Expressions

```java
int cadence = 0;                          // '0' is an expression (literal)
anArray[0] = 100;                         // '100' and 'anArray[0] = 100' are expressions
        IO.println("Value: " + anArray[0]);       // string concatenation is an expression
int result = 1 + 2;                       // '1 + 2' evaluates to 3
if (value1 == value2) { ... }             // 'value1 == value2' evaluates to boolean
```

> 💡 **Key Insight**: Even **assignment** (`x = 5`) is an expression—it returns the assigned value (type matches the left operand).

### 🔁 Compound Expressions

You can nest expressions to build more complex ones:

```java
1 * 2 * 3                // evaluates to 6
        (x + y) / 100            // parentheses enforce order
        Math.sqrt(a * a + b * b) // method call with arithmetic expression
```

### ⚠️ Operator Precedence & Clarity

Without parentheses, Java uses **operator precedence** to determine evaluation order:

```java
x + y / 100   // same as x + (y / 100) because / has higher precedence than +
```

✅ **Best Practice**: **Use parentheses** to make intent clear—even when not strictly necessary:

```java
// Preferred for readability:
double average = (total + bonus) / (count + 1);
```

### 🔍 Floating-Point Arithmetic Warning

Floating-point math can be **surprising** due to binary representation limitations:

```java
double d1 = 0.1 + 0.1 + 0.1 + 0.1 + 0.1 +
        0.1 + 0.1 + 0.1 + 0.1 + 0.1;
IO.println("d1 == 1.0? " + (d1 == 1.0)); // prints false!
```

> 📌 **Why?** `0.1` cannot be represented exactly in binary floating-point.  
> ✅ **Solution**: Use `BigDecimal` for precise decimal arithmetic (e.g., financial calculations).



## 🔸 Statements

A **statement** is a **complete unit of execution**, analogous to a sentence in English. It ends with a **semicolon (`;`)**.

Java has **three kinds** of statements:

### 1. **Expression Statements**
Any expression followed by `;` becomes a statement if it:
- Assigns a value
- Increments/decrements
- Calls a method
- Creates an object

```java
// Assignment
speed = 25;

// Increment
count++;

// Method call
        IO.println("Hello");

// Object creation
Bicycle bike = new Bicycle();
```

> ❗ Not all expressions can be statements!  
> Example: `x + y;` is **invalid**—it computes a value but doesn’t use it.

### 2. **Declaration Statements**
Declare (and optionally initialize) a variable:

```java
int age;              // declaration
double price = 19.99; // declaration + initialization
String name = "Java";
```

> ✅ Every declaration is a statement—but not every statement is a declaration.

### 3. **Control Flow Statements**
Direct the flow of execution (covered in next section):
- `if`, `switch`
- `for`, `while`, `do-while`
- `break`, `continue`, `return`

```java
if (isLoggedIn) {
greetUser();
}
```



## 🔸 Blocks

A **block** is a group of **zero or more statements** enclosed in **curly braces `{}`**. It can be used **anywhere a single statement is allowed**.

### ✅ Syntax:
```java
{
statement1;
statement2;
// ...
}
```

### 🔁 Common Uses of Blocks

#### 1. **Control Flow Bodies**
```java
if (x > 0) {
        IO.println("Positive");
x = x * 2;
        }
```

#### 2. **Method Bodies**
```java
public void calculate() {
    int a = 10;
    int b = 20;
    IO.println(a + b);
}
```

#### 3. **Local Scoping**
```java
{
int temp = 42; // only visible inside this block
    IO.println(temp);
}
// temp is no longer accessible here
```

> 💡 Blocks **limit variable scope**, improving code safety and readability.

### 📌 BlockDemo Example
```java
class BlockDemo {
    public static void main(String[] args) {
        boolean condition = true;
        if (condition) {
            IO.println("Condition is true.");
        } else {
            IO.println("Condition is false.");
        }
    }
}
```

Each branch of the `if-else` uses a **block** to group its statements.



## 🔍 Key Takeaways

| Concept      | Purpose | Ends With | Example |
|-------------|--------|----------|--------|
| **Expression** | Computes a **value** | — | `x + 5`, `list.size()`, `"Hi" + name` |
| **Statement**  | Performs an **action** | `;` | `x = 10;`, `count++;`, `IO.println(...);` |
| **Block**      | Groups **multiple statements** as one unit | `}` (no `;`) | `{ x++; y--; }` |

- **All expressions** that produce useful side effects can become **expression statements**.
- **Declarations** are statements that introduce new variables.
- **Blocks** enable grouping and **local scoping**—essential for control structures.
- **Prefer explicit parentheses** in compound expressions to avoid precedence confusion.
- **Beware of floating-point precision**—don’t use `==` for `double`/`float` equality checks.

> ✅ **Pro Tip**:
> ```java
> // Instead of:
> if (d1 == 1.0) { ... }
> 
> // Use a tolerance for floating-point comparison:
> if (Math.abs(d1 - 1.0) < 1e-10) { ... }
> ```

---
# Chapter 8: 🧭 Control Flow Statements

Control flow statements determine the **order of execution** in your program. They allow your code to:
- Make **decisions** (`if`, `switch`)
- **Repeat** actions (`for`, `while`, `do-while`)
- **Branch** or **exit** early (`break`, `continue`, `return`, `yield`)

This section covers all major control flow constructs in Java—with syntax, examples, and best practices.



## 🔹 1. The `if-then` Statement

Executes a block **only if** a condition is `true`.

### ✅ Syntax:
```java
if (condition) {
        // code to execute if true
        }
```

### 🔁 Example:
```java
void applyBrakes() {
    if (isMoving) {
        currentSpeed--;
    }
}
```

> ⚠️ **Braces are optional for single statements**, but **always recommended**:
> ```java
> if (isMoving) currentSpeed--; // legal but risky
> ```
> Adding a second line later without braces causes bugs the compiler won’t catch.



## 🔸 2. The `if-then-else` Statement

Provides an **alternative path** when the condition is `false`.

### ✅ Syntax:
```java
if (condition) {
        // true branch
        } else {
        // false branch
        }
```

### 🔁 Cascading `else-if`:
```java
if (score >= 90) {
grade = 'A';
        } else if (score >= 80) {
grade = 'B';
        } else if (score >= 70) {
grade = 'C';
        } else {
grade = 'F';
        }
```

> 💡 **Important**: Conditions are evaluated **top to bottom**—only the **first matching** block executes.



## 🔸 3. The `while` Loop

Repeats a block **while** a condition remains `true`.

### ✅ Syntax:
```java
while (condition) {
        // loop body
        }
```

### 🔁 Example:
```java
int count = 1;
while (count <= 10) {
        IO.println("Count: " + count);
count++;
        }
```

> 🔁 **Infinite loop**:
> ```java
> while (true) { ... }
> ```



## 🔸 4. The `do-while` Loop

Like `while`, but **guarantees at least one execution** (condition checked **after** the loop body).

### ✅ Syntax:
```java
do {
        // loop body
        } while (condition);
```

### 🔁 Example:
```java
int count = 1;
do {
        IO.println("Count: " + count);
count++;
        } while (count <= 10);
```

> ✅ Use when you **must run the loop once**, even if the condition is initially `false`.



## 🔸 5. The `for` Loop

A compact way to loop over a known range.

### ✅ General Form:
```java
for (initialization; termination; increment) {
        // loop body
        }
```

### 🔁 Example:
```java
for (int i = 1; i <= 10; i++) {
        IO.println("Count: " + i);
}
```

#### Key Rules:
- `initialization`: runs **once** at start.
- `termination`: checked **before each iteration**.
- `increment`: runs **after each iteration**.
- All three parts are **optional** → `for (;;)` = infinite loop.

### 🔹 Enhanced `for` Loop (for-each)

Simplifies iteration over **arrays** and **Collections**.

### ✅ Syntax:
```java
for (Type element : collection) {
        // use element
        }
```

### 🔁 Example:
```java
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
        IO.println("Number: " + num);
}
```

> ✅ **Best Practice**: Prefer enhanced `for` when you **don’t need the index**.



## 🔸 6. The `break` Statement

Exits the **innermost** loop or `switch`.

### ✅ Unlabeled `break`:
```java
for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) {
foundIndex = i;
        break; // exits the for loop
                }
                }
```

### 🔹 Labeled `break`

Exits an **outer** loop marked with a label.

### ✅ Syntax:
```java
labelName:
        for (...) {
        for (...) {
        if (found) break labelName;
    }
            }
```

### 🔁 Example:
```java
search:
        for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] == target) {
        break search; // exits outer loop
        }
                }
                }
```

> 📌 Labels must be **immediately before** the loop.



## 🔸 7. The `continue` Statement

Skips the **rest of the current iteration** and jumps to the next loop cycle.

### ✅ Unlabeled `continue`:
```java
for (char c : text.toCharArray()) {
        if (c != 'p') continue; // skip non-'p' chars
count++;
        }
```

### 🔹 Labeled `continue`

Skips to the next iteration of an **outer** labeled loop.

### 🔁 Example (substring search):
```java
test:
        for (int i = 0; i <= max; i++) {
        int j = i, k = 0;
    while (k < pattern.length()) {
        if (text.charAt(j++) != pattern.charAt(k++)) {
        continue test; // restart outer loop
        }
                }
found = true;
        break test;
}
```



## 🔸 8. The `return` Statement

Exits the **current method** and optionally returns a value.

### ✅ Two Forms:
```java
return;          // for void methods
        return value;    // for non-void methods
```

### 🔁 Examples:
```java
public int add(int a, int b) {
    return a + b; // returns int
}

public void log(String msg) {
    IO.println(msg);
    return; // optional in void methods
}
```

> ⚠️ The return type **must match** the method’s declared type.



## 🔸 9. The `yield` Statement (Java 13+)

Used **only inside `switch` expressions** to return a value from a `switch` block.

### ✅ Syntax:
```java
return switch (day) {
        case SATURDAY, SUNDAY -> 0;
default -> {
int workDays = 5 - day.ordinal();
yield workDays; // returns value from switch
    }
            };
```

> 💡 `yield` is the **block equivalent** of the `->` arrow in switch expressions.
> - Use `->` for **simple expressions**
> - Use `{ yield value; }` for **multi-statement logic**



## 🔍 Control Flow Summary Table

| Statement        | Purpose | Can Be Labeled? |
|------------------|--------|----------------|
| `if` / `else`    | Conditional execution | ❌ |
| `for`            | Count-controlled loop | ✅ |
| `while`          | Condition-controlled loop | ✅ |
| `do-while`       | Post-test loop | ✅ |
| `switch`         | Multi-way branch | ❌ (but used with `yield`) |
| `break`          | Exit loop/switch | ✅ |
| `continue`       | Skip to next iteration | ✅ |
| `return`         | Exit method | ❌ |
| `yield`          | Return value from `switch` expression | ❌ |



## ✅ Best Practices

- **Always use braces** with `if`, `for`, `while`—even for one-liners.
- Prefer **enhanced `for`** over traditional `for` when possible.
- Use **`break`/`continue` with labels** sparingly—they can reduce readability.
- Avoid deep nesting—extract logic into methods instead.
- In `switch` expressions, prefer **arrow syntax (`->`)** for simple cases; use **`yield`** only when you need local variables or multiple statements.

---
# Chapter 9: 🔄 Branching with Switch Statements

The **`switch` statement** is a powerful control flow construct that allows your program to **select one of many execution paths** based on the value of a single expression. It’s especially useful when you have **multiple discrete cases** to handle—like menu options, state machines, or category-based logic.



## 🔹 When to Use `switch`

Use `switch` when:
- You’re comparing a **single variable** against **multiple constant values**.
- The values are of a **supported type** (see below).
- You want **cleaner, more readable code** than long `if-else if` chains.

> ✅ **Not suitable** for:
> - Range checks (`x > 10`)
> - Complex boolean conditions
> - Floating-point or `boolean` types



## 🔸 Supported Selector Types

The expression in a `switch` statement **must be** one of the following:

| Category | Types |
|--------|------|
| **Primitive integers** | `byte`, `short`, `char`, `int` |
| **Wrapper classes** | `Byte`, `Short`, `Character`, `Integer` |
| **Reference types** | `String` (Java 7+), **enum types** |

### ❌ **Unsupported Types**
- `long`, `float`, `double`, `boolean`
- Custom objects (unless wrapped in `String` or `enum`)

> 💡 **Why no `long`?** Historically, `switch` was designed for efficient **jump tables** using 32-bit integers. `long` doesn’t fit this model.



## 🔹 Basic Syntax

```java
switch (selector) {
    case value1:
        // statements
        break;
    case value2:
        // statements
        break;
    // ... more cases
    default:
        // optional fallback
}
```

### 🔑 Key Components:
- **`selector`**: the variable/expression being tested.
- **`case`**: constant value to match.
- **`break`**: exits the `switch` block (prevents **fall-through**).
- **`default`**: optional catch-all for unmatched values.



## 🔸 Fall-Through Behavior

If you **omit `break`**, execution **continues into the next case**—this is called **fall-through**.

### ✅ Intentional Fall-Through Example:
```java
int month = 8;
List<String> futureMonths = new ArrayList<>();

switch (month) {
    case 1:  futureMonths.add("January");
    case 2:  futureMonths.add("February");
    case 3:  futureMonths.add("March");
    // ... continues adding until break
    case 8:  futureMonths.add("August");
    case 9:  futureMonths.add("September");
    case 10: futureMonths.add("October");
    case 11: futureMonths.add("November");
    case 12: futureMonths.add("December");
             break;
    default: break;
}
```
→ For `month = 8`, this adds **August through December**.

> ⚠️ **Danger**: Accidental fall-through is a **common bug**. Always use `break` unless fall-through is intentional (and document it!).



## 🔸 Multiple Case Labels

You can **share logic** across multiple cases:

```java
switch (month) {
    case 1: case 3: case 5:
    case 7: case 8: case 10:
    case 12:
        numDays = 31;
        break;
    case 4: case 6:
    case 9: case 11:
        numDays = 30;
        break;
    case 2:
        numDays = isLeapYear(year) ? 29 : 28;
        break;
    default:
        IO.println("Invalid month");
}
```

> ✅ Clean way to group months with same number of days.



## 🔹 Using `String` in `switch` (Java 7+)

You can use `String` objects as selectors:

```java
String month = "August";
int monthNumber = -1;

switch (month.toLowerCase()) {
    case "january":   monthNumber = 1; break;
    case "february":  monthNumber = 2; break;
    // ...
    case "december":  monthNumber = 12; break;
    default:          monthNumber = 0; break;
}
```

### 🔍 How It Works:
- Uses **`String.equals()`** for comparison (case-sensitive!).
- **Null-safe?** ❌ No—passing `null` throws `NullPointerException`.

> ✅ **Best Practice**: Normalize case (e.g., `.toLowerCase()`) if input may vary.



## 🔸 Handling `null` Selectors

If the selector is `null`, the `switch` throws a **`NullPointerException`**:

```java
String input = null;
switch (input) { // ❌ Throws NullPointerException
    case "test": ...
}
```

### ✅ Safe Approach:
```java
if (input == null) {
    handleNull();
} else {
    switch (input) {
        case "value": ...
    }
}
```

> 🛡️ Always **validate** object selectors for `null` before `switch`.



## 🔹 `switch` vs. `if-else if`

| Feature | `switch` | `if-else if` |
|--------|--------|-------------|
| **Type support** | Limited (int, enum, String) | Any boolean expression |
| **Range checks** | ❌ Not possible | ✅ `if (x >= 0 && x < 10)` |
| **Readability** | ✅ Excellent for discrete values | ❌ Verbose for many cases |
| **Performance** | ✅ Optimized (jump table) | Linear evaluation |

### ✅ Use `switch` when:
```java
// Good for switch
int day = 3;
switch (day) {
    case 1: ... 
    case 2: ...
}
```

### ❌ Use `if-else` when:
```java
// Requires if-else (range check)
if (temp < 0) {
    state = "ice";
} else if (temp < 100) {
    state = "liquid";
} else {
    state = "vapor";
}
```



## 🔍 Key Takeaways

- `switch` works with **`int`-compatible types, `String`, and `enum`**.
- **Always use `break`** unless fall-through is intentional.
- **`default`** is optional but recommended for robustness.
- **Multiple `case` labels** can share the same block.
- **`String` switches** use `.equals()`—be mindful of case and `null`.
- **Never pass `null`** to a `switch` without checking first.
- Prefer `switch` over `if-else` for **discrete, constant-based branching**.

> 💡 **Modern Alternative**: Starting in **Java 12**, **`switch` expressions** (with `->` and `yield`) offer even cleaner, safer syntax—covered in the next section!

---
# Chapter 10: 🌈 Branching with Switch Expressions

Introduced as a **preview feature in Java 12** and **standardized in Java 14**, **`switch` expressions** modernize the traditional `switch` statement by making it **expression-oriented**, **safer**, and **more concise**.

Unlike the classic `switch` **statement**, a `switch` **expression** **returns a value**, eliminates accidental fall-through, and enforces **exhaustiveness**—making your code less error-prone and more readable.



## 🔹 Why Switch Expressions?

The traditional `switch` statement has well-known pitfalls:
- **Fall-through** by default → easy to forget `break`.
- **Not an expression** → can’t assign result directly.
- **Variable scoping issues** → all cases share one block.
- **Not exhaustive** → missing cases cause silent bugs.

`switch` expressions solve all these problems.



## 🔸 Basic Syntax: Arrow Labels (`->`)

The new syntax uses **arrow labels** (`case value ->`) instead of colons (`case value:`). Each case **executes only its own code**—**no fall-through**, **no `break` needed**.

### ✅ Traditional `switch` statement:
```java
int len = 0;
switch (day) {
        case MONDAY:
        case FRIDAY:
        case SUNDAY:
len = 6;
        break;
        case TUESDAY:
len = 7;
        break;
        // ... more cases
        }
```

### 🔁 Modern `switch` **expression**:
```java
int len = switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> 6;
    case TUESDAY                -> 7;
    case THURSDAY, SATURDAY     -> 8;
    case WEDNESDAY              -> 9;
};
```

> ✅ **Benefits**:
> - **Multiple constants per case** (comma-separated).
> - **No `break`** required.
> - **Direct assignment**—`switch` **returns a value**.



## 🔹 Producing a Value

A `switch` expression **must produce a value** of a consistent type. There are two ways to do this:

### 1. **Simple Expression (Arrow Syntax)**
```java
String quarterLabel = switch (quarter) {
    case 0 -> "Q1 - Winter";
    case 1 -> "Q2 - Spring";
    case 2 -> "Q3 - Summer";
    case 3 -> "Q4 - Fall";
    default -> "Unknown quarter";
};
```

### 2. **Block with `yield` (for multi-statement logic)**
When you need **local variables**, logging, or complex logic, use a **block** (`{ }`) and **`yield`** to return the value.

```java
String quarterLabel = switch (quarter) {
    case 0 -> {
        IO.println("Processing Q1");
        String label = "Q1 - Winter";
        yield label; // returns value from switch
    }
    default -> "Unknown quarter";
};
```

> ⚠️ **Never use `return` inside a `switch` expression block**—it exits the **method**, not the `switch`!  
> ✅ Always use **`yield`** to return a value from a `switch` expression block.



## 🔸 Exhaustiveness: No Missing Cases

`switch` expressions **must be exhaustive**—they must handle **all possible input values**.

### ✅ For `enum` types:
If you cover **all enum constants**, you **don’t need `default`**:

```java
enum Color { RED, GREEN, BLUE }

String desc = switch (color) {
    case RED   -> "Stop";
    case GREEN -> "Go";
    case BLUE  -> "Caution";
    // No default needed—exhaustive!
};
```

> 🔒 The compiler **verifies exhaustiveness** at compile time.

### ⚠️ What if a new enum constant is added later?
- The compiler **automatically inserts a hidden `default`** that throws `IncompatibleClassChangeError`.
- This **fails fast** instead of silently ignoring the new case.

### ✅ For non-enum types (`int`, `String`, etc.):
You **must include `default`** to be exhaustive:

```java
String season = switch (month) {
    case "Dec", "Jan", "Feb" -> "Winter";
    case "Mar", "Apr", "May" -> "Spring";
    // ...
    default -> "Invalid month"; // required!
};
```

> 🛡️ This prevents silent bugs from unhandled inputs.



## 🔹 Using Colon Labels (`:`) in Switch Expressions

You **can** still use traditional colon syntax (`case X:`) inside a `switch` expression—but **fall-through is allowed**, and you **must use `yield`** to return values.

```java
String quarterLabel = switch (quarter) {
    case 0:  yield "Q1 - Winter";
    case 1:  yield "Q2 - Spring";
    case 2:  yield "Q3 - Summer";
    case 3:  yield "Q4 - Fall";
    default:
        IO.println("Unknown quarter");
        yield "Unknown";
};
```

> ⚠️ **Not recommended**—defeats the safety benefits of arrow syntax.  
> ✅ Prefer **arrow (`->`)** for new code.



## 🔸 Handling `null` Values

### ❌ Traditional behavior (Java ≤ 16):
Passing `null` to any `switch` (statement or expression) throws **`NullPointerException`**.

### ✅ Java 17+ (Preview → Standard in later versions):
Enhanced `switch` expressions **support `null`** via **explicit `case null`**:

```java
// Java 17+ (with preview features enabled)
String result = switch (input) {
            case null -> "No input";
            case "A"  -> "Alpha";
            case "B"  -> "Beta";
            default   -> "Other";
        };
```

> 📌 Until then, **always check for `null` before switching**:
> ```java
> if (input == null) {
>     handleNull();
> } else {
>     String out = switch (input) { ... };
> }
> ```



## 🔍 Key Differences: Statement vs. Expression

| Feature | `switch` Statement | `switch` Expression |
|--------|-------------------|---------------------|
| **Returns value?** | ❌ No | ✅ Yes |
| **Fall-through?** | ✅ Yes (dangerous) | ❌ No (arrow syntax) |
| **`break` needed?** | ✅ Yes | ❌ No |
| **Exhaustive?** | ❌ Optional | ✅ Required |
| **Local variables per case?** | ❌ Shared scope | ✅ Block scope |
| **Syntax** | `case X:` | `case X ->` or `case X: yield ...` |



## ✅ Best Practices

1. **Prefer `switch` expressions** over statements for new code.
2. Use **arrow syntax (`->`)**—avoid colons unless you need fall-through (rare).
3. Use **`yield`**, never `return`, inside switch blocks.
4. **Always be exhaustive**—use `default` or cover all enum constants.
5. **Validate `null`** before switching (unless using Java 17+ with null support).
6. **Group cases** with commas for cleaner code.



## 🔚 Final Example: Complete and Safe

```java
public static String getDayType(Day day) {
    return switch (day) {
        case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> {
            IO.println("Weekday: " + day);
            yield "Workday";
        }
        case SATURDAY, SUNDAY -> "Weekend";
    };
    // Exhaustive for enum → no default needed
}
```

> 💡 This code is **safe**, **readable**, **maintainable**, and **compile-time verified**.

