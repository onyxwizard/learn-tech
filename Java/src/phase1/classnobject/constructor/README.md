# 📞 **Calling Methods and Constructors — The Full Guide**

A deep, clear, and practical explanation of **how to pass data to methods and constructors in Java**, including **parameter types**, **varargs**, **naming**, and the **critical difference between passing primitives vs. objects**.



## 🎯 **Core Idea: Parameters vs. Arguments**

> 🔑 **Parameters** = Variables in the **method/constructor declaration**  
> **Arguments** = Actual **values passed in** when you **call** the method

```java
// Declaration: 'loanAmt', 'rate' are PARAMETERS
public double computePayment(double loanAmt, double rate, int periods) { ... }

// Call: 250000.0, 4.5, 360 are ARGUMENTS
double payment = computePayment(250000.0, 4.5, 360);
```

✅ **Rule**: Arguments must **match** parameters in **type, order, and count**.



## 🔢 **1. Parameter Types — Any Type Allowed!**

You can use **any data type** as a parameter:

### ✅ Primitive Types
```java
public void setSpeed(int speed) { ... }
public double calculateArea(double radius) { ... }
public boolean isValid(char input) { ... }
```

### ✅ Reference Types (Objects, Arrays)
```java
// Object parameter
public void moveCircle(Circle c, int dx, int dy) { ... }

// Array parameter
public Polygon polygonFrom(Point[] corners) { ... }
```

> 💡 **Key Insight**: Whether it’s `int`, `String`, `MyClass`, or `int[]` — Java handles it the same way at the call site.



## 🌟 **2. Varargs — Pass Any Number of Arguments**

When you don’t know **how many arguments** will be passed, use **varargs** (`...`).

### 🔧 Syntax
```java
public ReturnType methodName(Type... paramName) { ... }
```

### ✅ Example: Flexible Drawing Method
```java
public void drawShapes(Shape... shapes) {
    System.out.println("Drawing " + shapes.length + " shapes:");
    for (Shape s : shapes) {
        s.render();
    }
}
```

### ▶️ How to Call It
```java
// Pass individual objects
drawShapes(circle, square, triangle);

// Pass an array
Shape[] myShapes = {circle, hexagon};
drawShapes(myShapes);

// Pass zero arguments
drawShapes(); // shapes.length == 0
```

> 🔸 Inside the method, `shapes` behaves like a **regular array** (`Shape[]`).

### 📝 Real-World Use: `printf()`
```java
System.out.printf("Name: %s, Age: %d", name, age);        // 2 args
System.out.printf("Coords: %d, %d, %d", x, y, z);         // 3 args
```

> ⚠️ **Rule**: Varargs **must be the last parameter**:
> ```java
> public void log(String level, Object... messages) { ... } // ✅ OK
> public void badExample(Object... items, String tag) { ... } // ❌ Compile error!
> ```



## 🏷️ **3. Parameter Naming — Clarity & Shadowing**

### ✅ Good Practice: Descriptive Names
```java
public void setSpeed(int kilometersPerHour) { ... }  // clear
public void setSpeed(int x) { ... }                  // unclear ❌
```

### ⚠️ **Shadowing**: When Parameters Hide Fields

```java
public class Circle {
    private int x, y; // fields

    // Parameters 'x' and 'y' SHADOW the fields
    public void setOrigin(int x, int y) {
        // ❌ This assigns parameter to itself (useless!)
        x = x;
        y = y;

        // ✅ Use 'this' to access fields
        this.x = x;
        this.y = y;
    }
}
```

> 🔑 **`this.x`** = the **field**  
> **`x`** = the **parameter**

> 💡 **Convention**: It’s **common and acceptable** to shadow fields in **constructors and setters** — just remember to use `this`.



## 🔁 **4. Passing Data: The Critical Difference**

Java **always passes by value** — but what that **means** depends on the type.

---

### 🟢 **A. Passing Primitives (int, double, boolean, etc.)**

- A **copy** of the value is passed.
- Changes inside the method **do NOT affect** the original variable.

#### Example:
```java
public class PassPrimitive {
    public static void main(String[] args) {
        int x = 5;
        changeValue(x);
        System.out.println(x); // Output: 5 (unchanged!)
    }

    public static void changeValue(int num) {
        num = 100; // Only changes the COPY
    }
}
```

> ✅ **Safe**: Original data is **never modified** by the method.



### 🔵 **B. Passing Objects (Reference Types)**

- A **copy of the reference** (memory address) is passed.
- Both the original and the parameter **point to the same object**.
- You can **modify the object’s state** (fields).
- But you **cannot reassign** the original reference.

#### Example:
```java
public class PassObject {
    public static void main(String[] args) {
        Circle c = new Circle(10, 20);
        System.out.println("Before: " + c); // (10, 20)

        modifyCircle(c);
        System.out.println("After: " + c);  // (110, 120) → CHANGED!

        reassignCircle(c);
        System.out.println("After reassign: " + c); // Still (110, 120)!
    }

    public static void modifyCircle(Circle circle) {
        // ✅ Modifies the SAME object
        circle.x += 100;
        circle.y += 100;
    }

    public static void reassignCircle(Circle circle) {
        // ❌ Only changes the LOCAL copy of the reference
        circle = new Circle(0, 0); // Original 'c' is unaffected
    }
}
```

> 🔑 **Summary**:
> - ✅ **Object state can be changed** (fields updated)
> - ❌ **Original reference cannot be changed** (reassignment is local)



## 🛠️ **5. Constructors — Special Methods for Initialization**

Constructors **initialize new objects** and follow the same parameter rules.

### ✅ Example with Shadowing & Validation
```java
public class BankAccount {
    private String owner;
    private double balance;

    // Constructor with parameters that shadow fields
    public BankAccount(String owner, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Balance can't be negative");
        }
        this.owner = owner;           // 'this' resolves shadowing
        this.balance = initialBalance;
    }

    // Varargs constructor (e.g., for joint accounts)
    public BankAccount(double initialBalance, String... owners) {
        this.balance = initialBalance;
        this.owner = String.join(" & ", owners);
    }
}
```

### ▶️ Usage
```java
var account1 = new BankAccount("Alice", 1000.0);
var account2 = new BankAccount(500.0, "Bob", "Carol"); // varargs
```



## 📋 **Best Practices Cheat Sheet**

| Scenario | Recommendation |
|--------|----------------|
| **Parameter count** | Keep it under 5–6; use objects if too many |
| **Varargs** | Use only when truly variable (e.g., logging, math functions) |
| **Shadowing** | Acceptable in constructors/setters — **always use `this`** |
| **Primitives** | Safe to pass — original never changes |
| **Objects** | Can mutate state, but not reassign caller’s reference |
| **Null safety** | Check for `null` in object parameters if needed |


## 💡 **Why This Matters in Real Code**

Understanding **how data is passed** prevents **common bugs**:
- Thinking a method changed your `int` variable (it didn’t!)
- Accidentally modifying shared objects
- Confusing why reassigning a parameter didn’t “take”

And **varargs** let you write **flexible, user-friendly APIs** like:
```java
Logger.info("User {} logged in from {}", username, ip);
Logger.error("Failed to process {} items", count, item1, item2, item3);
```



## ✅ **Final Summary**

| Concept | Key Takeaway |
|--------|-------------|
| **Parameters vs Arguments** | Declaration vs. call-time values |
| **Varargs (`...`)** | Flexible argument count; treated as array |
| **Shadowing** | Use `this.field` to access hidden fields |
| **Passing Primitives** | Copy of value → original unchanged |
| **Passing Objects** | Copy of reference → object state can change, reference cannot |
| **Constructors** | Follow same rules; ideal place for shadowing with `this` |