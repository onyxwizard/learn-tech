## ✅ Core Concepts: Creating Classes in Java

### 1. **Basic Class Structure**
A class is a blueprint for creating objects. It defines:
- **State** → via **fields** (member variables)
- **Behavior** → via **methods**
- **Initialization** → via **constructors**

```java
public class ClassName {
    // Fields (state)
    private int field1;
    public String field2;

    // Constructor
    public ClassName(int value) {
        this.field1 = value;
    }

    // Methods (behavior)
    public void doSomething() { ... }
}
```

> 🔸 **Naming Convention**:
> - Class names → `PascalCase` (e.g., `MountainBike`)
> - Method/variable names → `camelCase` (e.g., `setCadence`, `seatHeight`)



### 2. **Access Control: `public` vs `private`**

| Modifier | Accessible From |
|--------|------------------|
| `public` | Any class, anywhere |
| `private` | Only within the **same class** |

✅ **Best Practice**:
> **Make fields `private`** → Enforce **encapsulation**  
> Provide `public` **getter/setter methods** to control access

```java
private int speed;

public int getSpeed() { return speed; }
public void setSpeed(int speed) {
    if (speed >= 0) this.speed = speed; // add validation!
}
```

This protects your object’s internal state from invalid changes.



### 3. **Inheritance with `extends`**

Use `extends` to create a **subclass** (child) of a **superclass** (parent):

```java
public class MountainBike extends Bicycle {
    private int seatHeight;

    public MountainBike(int seatHeight, int cadence, int speed, int gear) {
        super(cadence, speed, gear); // call parent constructor
        this.seatHeight = seatHeight;
    }

    public void setSeatHeight(int height) {
        this.seatHeight = height;
    }
}
```

> 🔸 A subclass **inherits** all `public` and `protected` members of its parent.  
> 🔸 Use `super(...)` to invoke the parent constructor.



### 4. **Class Declaration Syntax (Full Form)**

```java
[access-modifier] class ClassName 
    [extends SuperClassName] 
    [implements Interface1, Interface2, ...] {
    
    // fields, constructors, methods
}
```

Examples:
```java
public class Car { ... }
class Engine { ... }               // package-private (default)
public class ElectricCar extends Car implements Rechargeable { ... }
```

> ⚠️ Java allows **only single inheritance** (`extends` one class),  
> but **multiple interface implementation** (`implements` many).



### 5. **Types of Variables in Classes**

| Type | Scope | Example |
|------|------|--------|
| **Field** (instance variable) | Belongs to the object | `private int speed;` |
| **Local variable** | Inside a method/block | `void method() { int temp = 5; }` |
| **Parameter** | In method signature | `void setSpeed(int speed) { ... }` |

> 🔸 Fields live as long as the object exists.  
> 🔸 Local variables are destroyed when the method ends.



### 6. **Why Encapsulation Matters**

By making fields `private` and exposing behavior through methods, you:
- Prevent invalid state (e.g., negative speed)
- Allow future changes without breaking other code
- Make your class **self-managing** and **reliable**

Example:
```java
public void applyBrake(int decrement) {
    speed = Math.max(0, speed - decrement); // never go below 0!
}
```



## 🚀 What’s Next? (Suggested Learning Path)

Now that you understand **how to create and structure classes**, you’re ready for:

### ➡️ **1. Constructors in Depth**
- Default vs. custom constructors
- Constructor chaining (`this(...)`)
- Overloading constructors

### ➡️ **2. The `this` Keyword**
- Refer to current object
- Resolve naming conflicts (`this.speed = speed;`)

### ➡️ **3. Static Members**
- `static` fields & methods (belong to class, not instance)
- When to use them (e.g., `Math.PI`, counters)

### ➡️ **4. Packages & Imports**
- Organize classes into namespaces (`package com.myapp;`)
- Control visibility across packages

### ➡️ **5. Introduction to Interfaces**
- Define contracts (`implements Drivable`)
- Enable polymorphism and flexibility

