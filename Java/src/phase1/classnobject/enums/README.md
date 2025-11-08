# 🎯 **Enums in Java — The Complete Guide**

A clear, practical, and modern explanation of **enumerated types (enums)** in Java — from basics to advanced patterns, with real-world examples and best practices.


## 🔤 **What Are Enums?**

> **Enums** (short for *enumerated types*) are **special classes** that represent a **fixed set of constants**.

They’re perfect when you need a type that can only have a **small, known set of values**:
- Days of the week
- Card suits
- HTTP status codes
- Game states
- User roles

### ✅ Basic Syntax
```java
public enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

> 🔑 **Key Facts**:
> - All enums **implicitly extend** `java.lang.Enum`
> - **No subclasses** allowed
> - **Only the listed constants** can exist — no `new DayOfWeek()`!



## 🧪 **Using Enums: Access, Compare, Switch**

### 🔹 **As Constants**
```java
DayOfWeek today = DayOfWeek.FRIDAY;
```

### 🔹 **Equality Check**
Use `==` (safe and efficient — enums are singletons!):
```java
if (today == DayOfWeek.FRIDAY) {
    System.out.println("Weekend is near!");
}
```

> ✅ **Why `==` works**: Each enum constant is a **single instance** in memory.

### 🔹 **Switch Statements (Java 14+ Switch Expressions)**
```java
String message = switch (today) {
    case MONDAY -> "Week just started.";
    case TUESDAY, WEDNESDAY, THURSDAY -> "Midweek grind.";
    case FRIDAY -> "TGIF!";
    case SATURDAY, SUNDAY -> "Weekend vibes!";
};
```

> ✅ **Exhaustiveness Check**:  
> The compiler **ensures all cases are covered** — no missing `default` needed!



## 🧱 **Adding Behavior: Fields, Constructors & Methods**

Enums are **full-featured classes** — you can add:
- Fields
- Constructors
- Methods
- Even abstract methods!

### 💡 Example: Day with Abbreviation
```java
public enum DayOfWeek {
    MONDAY("MON"),
    TUESDAY("TUE"),
    WEDNESDAY("WED"),
    THURSDAY("THU"),
    FRIDAY("FRI"),
    SATURDAY("SAT"),
    SUNDAY("SUN");

    private final String abbreviation;

    // Private constructor (only called internally)
    DayOfWeek(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public boolean isWeekend() {
        return this == SATURDAY || this == SUNDAY;
    }
}
```

> ⚠️ **Syntax Note**:  
> Add a **semicolon (`;`)** after the constants list before adding members.

### ▶️ Usage
```java
System.out.println(DayOfWeek.FRIDAY.getAbbreviation()); // "FRI"
System.out.println(DayOfWeek.SUNDAY.isWeekend());      // true
```



## 🛠️ **Built-In Enum Methods**

All enums inherit powerful utility methods:

| Method | Purpose | Example |
|-------|--------|--------|
| `name()` | Returns constant name | `FRIDAY.name()` → `"FRIDAY"` |
| `ordinal()` | Returns position (0-based) | `MONDAY.ordinal()` → `0` |
| `values()` | Returns all constants | `DayOfWeek.values()` → array |
| `valueOf(String)` | Get constant by name | `DayOfWeek.valueOf("FRIDAY")` |
| `compareTo()` | Compare by ordinal | `MONDAY.compareTo(FRIDAY)` → `-4` |

### 💡 Practical Use
```java
// Loop through all days
for (DayOfWeek day : DayOfWeek.values()) {
    System.out.println(day + " = " + day.getAbbreviation());
}

// Parse from string (case-sensitive!)
DayOfWeek parsed = DayOfWeek.valueOf("MONDAY");

// Sort days
List<DayOfWeek> days = Arrays.asList(FRIDAY, MONDAY, SUNDAY);
Collections.sort(days); // Sorted by ordinal: MONDAY, FRIDAY, SUNDAY
```



## 🧩 **Advanced Patterns**

### 🔸 **1. Enums as Singletons**
The **best way** to implement a singleton in Java:
```java
public enum DatabaseConnection {
    INSTANCE;

    private final String url = "jdbc:...";

    public void connect() {
        // Thread-safe, serializable, and simple!
    }
}

// Usage
DatabaseConnection.INSTANCE.connect();
```

> ✅ **Advantages**:
> - Automatic serialization safety
> - Thread-safe by design
> - Concise and clear



### 🔸 **2. Abstract Methods in Enums**
Each constant provides its own implementation:

```java
public enum Operation {
    ADD {
        @Override public double apply(double x, double y) { return x + y; }
    },
    SUBTRACT {
        @Override public double apply(double x, double y) { return x - y; }
    },
    MULTIPLY {
        @Override public double apply(double x, double y) { return x * y; }
    };

    public abstract double apply(double x, double y);
}
```

### ▶️ Usage
```java
double result = Operation.ADD.apply(5, 3); // 8.0
```

> 💡 **Alternative**: Use **records** (Java 16+) or **strategy pattern** for complex logic.



### 🔸 **3. Enums with Interfaces**
Enums can implement interfaces for flexibility:

```java
public interface Describable {
    String getDescription();
}

public enum Planet implements Describable {
    MERCURY("Closest to the sun"),
    VENUS("Hottest planet"),
    EARTH("Our home");

    private final String description;

    Planet(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
```



## ⚠️ **Precautions & Best Practices**

### 🚫 **Avoid If:**
- The set of values **changes frequently** (e.g., country list)
- You have **hundreds of constants** (use config files or databases)
- Values come from **external sources** (e.g., API responses)

### ✅ **Do This:**
| Practice | Why |
|--------|-----|
| Use **switch expressions** | Compiler checks exhaustiveness |
| Prefer **meaningful names** | `HTTP_STATUS.OK` vs `CODE_200` |
| Add **behavior, not just data** | Make enums **smart**, not dumb constants |
| Use **`valueOf()` carefully** | Throws `IllegalArgumentException` if name not found |
| **Document changes** | Enums are part of your public API |

### 🔒 **Safe `valueOf()` Wrapper**
```java
public static Optional<DayOfWeek> fromString(String name) {
    try {
        return Optional.of(DayOfWeek.valueOf(name.toUpperCase()));
    } catch (IllegalArgumentException e) {
        return Optional.empty();
    }
}
```



## 🆚 **Enums vs Constants vs Records**

| Feature | `enum` | `static final` | `record` |
|--------|-------|---------------|---------|
| Fixed set of values | ✅ | ❌ | ❌ |
| Built-in methods (`values`, `ordinal`) | ✅ | ❌ | ❌ |
| Switch exhaustiveness | ✅ | ❌ | ❌ |
| Can add behavior | ✅ | Limited | ✅ (but immutable) |
| Serialization-safe | ✅ | ✅ | ✅ |
| Best for | Days, states, types | Math constants | Data carriers |

> 💡 **Rule of Thumb**:  
> Use **enums** when you have a **closed set of named options** with possible behavior.

---

## 🌟 **Real-World Example: HTTP Status Codes**

```java
public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

    public static HttpStatus fromCode(int code) {
        for (HttpStatus status : values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
```

### ▶️ Usage
```java
HttpStatus status = HttpStatus.OK;
System.out.println(status.getCode()); // 200
System.out.println(status.isSuccessful()); // true
```

---

## 📋 **Quick Reference Cheat Sheet**

| Task | Code |
|------|------|
| **Declare enum** | `enum Color { RED, GREEN, BLUE }` |
| **Add fields** | `RED("#FF0000"), ...; private final String hex;` |
| **Get all values** | `Color[] colors = Color.values();` |
| **Parse from string** | `Color c = Color.valueOf("RED");` |
| **Switch with exhaustiveness** | `String s = switch(color) { ... };` |
| **Singleton** | `enum Singleton { INSTANCE; }` |
| **Abstract method** | `abstract void run();` + impl per constant |

---

## 💡 **Why Enums Matter**

Enums are one of Java’s **most underused yet powerful features**:
- **Type-safe**: No invalid values
- **Readable**: Self-documenting code
- **Maintainable**: Compiler catches missing cases
- **Extensible**: Add behavior without breaking clients

> 🔚 **Final Thought**:  
> *"Use enums whenever you find yourself writing a set of related constants — your future self (and teammates) will thank you."* 🚀