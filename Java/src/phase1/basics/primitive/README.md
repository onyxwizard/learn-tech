
## 🎯 Project: **"Data Type Showcase: Primitive & Literal Lab"** 🔬

A **single-file, self-documenting Java program** that **demonstrates every primitive type**, **literal format**, and **naming convention**—all in one place.

> ✅ Designed so **you learn by seeing and running real examples**  
> ✅ No external dependencies — runs with `java DataTypesLab.java`



## 📁 File to Create
```
DataTypesLab.java
```


## 🧪 What Your Program Must Do

Create a class `DataTypesLab` with a `main` method that:

### 1. **Declares and initializes one variable for each primitive type**
Use **meaningful names** and **correct literals**:
- `byte`: small integer (e.g., `maxVolume`)
- `short`: medium integer (e.g., `year`)
- `int`: general-purpose integer (e.g., `population`)
- `long`: large number (e.g., `galaxyStars`) → use `L` suffix
- `float`: single-precision decimal (e.g., `temperature`) → use `f` suffix
- `double`: double-precision decimal (e.g., `pi`) → default
- `boolean`: flag (e.g., `isJavaFun`)
- `char`: single character (e.g., `grade`) → use `' '`

✅ **Use underscores** in large numbers for readability  
✅ **Show hex, binary, and scientific notation**



### 2. **Demonstrate default values for fields**
- Create a **nested static class** (e.g., `DefaultsDemo`)
- Declare **uninitialized fields** of each type
- Print their values → shows compiler defaults

> 💡 Local variables **cannot** be uninitialized—but **fields can**!


### 3. **Show String as special type**
- Declare a `String` with double quotes
- Show **escape sequences**: `\n`, `\t`, `\"`
- Show **Unicode**: `'Ω'`, `'\u03A9'`



### 4. **Print everything clearly**
Use `System.out.println()` with labels so output is educational.



## 🧱 Structure Guide (What to Code)

### A. Main Class: `DataTypesLab`
- `main()` method
- Local variables for each primitive + `String`
- Print examples of:
    - Decimal, hex (`0xFF`), binary (`0b1010`)
    - Scientific notation (`1.23e4`)
    - Underscores (`1_000_000`)

### B. Helper Class: `DefaultsDemo` (inside `DataTypesLab`)
```java
static class DefaultsDemo {
    byte b;
    short s;
    int i;
    long l;
    float f;
    double d;
    char c;
    boolean bool;
    String str; // reference type → null
}
```
Then in `main()`:
```java
DefaultsDemo demo = new DefaultsDemo();
System.out.println("Default byte: " + demo.b);
// ... etc.
```



## 📏 Naming & Style Rules to Follow

| Rule | Example |
|------|--------|
| **camelCase** for variables | `maxVolume`, `isJavaFun` |
| **No `$` or `_` at start** | ❌ `_temp`, `$val` |
| **Underscores only between digits** | `1_000_000` ✅, `1__000` ✅, `1_` ❌ |
| **Use `L` for long, `f` for float** | `100L`, `3.14f` |
| **Single quotes for `char`** | `'A'` |
| **Double quotes for `String`** | `"Hello"` |



## 🖨️ Expected Output Snippet
```
🔢 INTEGERS:
  byte maxVolume = 100
  short year = 2025
  int population = 8_000_000_000
  long galaxyStars = 100_000_000_000_000L
  Hex: 0xFF = 255
  Binary: 0b1111 = 15

🧮 FLOATING-POINT:
  float temperature = 98.6f
  double pi = 3.14159
  Scientific: 1.23e4 = 12300.0

🔤 CHAR & STRING:
  char grade = 'A'
  String message = "Hello\n\tWorld!"

✅ DEFAULT VALUES (from uninitialized fields):
  byte: 0
  short: 0
  int: 0
  long: 0
  float: 0.0
  double: 0.0
  char: [blank or \u0000]
  boolean: false
  String: null
```



## 🚀 Your Task

1. Create `DataTypesLab.java`
2. Implement all the above **in your own code**
3. Run it with:
   ```bash
   java DataTypesLab.java
   ```
4. **Submit your code here**

I’ll then:
- ✅ Verify all 8 primitives are used correctly
- 🔍 Check literal formats (hex, binary, underscores)
- 💡 Confirm default values are demonstrated
- 🎯 Ensure naming follows Java conventions

This lab will **lock in your understanding** of Java’s type system—before you move to **operators** and **control flow**.

Ready to build it? 😊