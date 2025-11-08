# 🧩 **Nested Classes in Java — The Complete Guide**

A clear, practical, and structured explanation of **nested classes** in Java — including **inner classes**, **static nested classes**, **local classes**, and **anonymous classes** — with real-world use cases and best practices.



## 🏗️ **What Are Nested Classes?**

Java allows you to define a **class inside another class**. This is called a **nested class**.

```java
class OuterClass {
    // Nested classes go here
    class InnerClass { }           // Non-static → "inner class"
    static class StaticNested { }  // Static → "static nested class"
}
```

### 🔑 **Two Main Types**
| Type | Declaration | Access to Outer Members | Instantiation |
|------|------------|------------------------|--------------|
| **Inner Class** | `class Inner { }` | ✅ Full access (even `private`) | Requires outer instance |
| **Static Nested Class** | `static class Nested { }` | ❌ Only static members directly | Like top-level class |

> 💡 **Key Insight**:
> - **Inner classes** = tied to an **instance** of the outer class
> - **Static nested classes** = tied to the **class itself** (like static methods)



## 🤔 **Why Use Nested Classes?**

### ✅ 1. **Logical Grouping**
If a class is only useful to **one other class**, nest it!

```java
public class LinkedList {
    // Only LinkedList needs Node
    private static class Node {
        Object data;
        Node next;
    }
}
```

### ✅ 2. **Increased Encapsulation**
Hide helper classes and access `private` outer members:

```java
public class BankAccount {
    private double balance;

    // Hidden from outside world
    private class TransactionValidator {
        boolean isValid(double amount) {
            return amount <= balance; // Can access private 'balance'!
        }
    }
}
```

### ✅ 3. **Readable & Maintainable Code**
Keep related code together — no need to jump between files.



## 🔸 **1. Inner Classes (Non-Static Nested)**

### 📌 Characteristics
- Associated with an **instance** of the outer class
- Can access **all members** of the outer class (even `private`)
- **Cannot have static members** (except constants)
- Must be instantiated **through an outer instance**

### 💡 Example: Event Listeners (Classic Use Case)
```java
public class Button {
    private String label;

    // Inner class for click handling
    public class ClickListener {
        public void onClick() {
            System.out.println("Button '" + label + "' clicked!");
        }
    }

    public ClickListener createListener() {
        return new ClickListener(); // Uses current 'label'
    }
}

// Usage
Button btn = new Button();
btn.label = "Submit";
Button.ClickListener listener = btn.new ClickListener(); // Note: 'btn.new'
listener.onClick(); // Output: Button 'Submit' clicked!
```

> ⚠️ **Syntax Quirk**:  
> To instantiate: `outerInstance.new InnerClass()`



## 🔸 **2. Static Nested Classes**

### 📌 Characteristics
- **Not tied** to an outer instance
- Can only access **static members** of outer class directly
- Instantiated like a **top-level class**
- Often used for **helper/utility classes**

### 💡 Example: Builder Pattern
```java
public class Pizza {
    private final String size;
    private final boolean cheese;

    // Static nested builder
    public static class Builder {
        private String size = "medium";
        private boolean cheese = false;

        public Builder setSize(String size) {
            this.size = size;
            return this;
        }

        public Builder addCheese() {
            this.cheese = true;
            return this;
        }

        public Pizza build() {
            return new Pizza(this); // Private constructor
        }
    }

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.cheese = builder.cheese;
    }
}

// Usage
Pizza pizza = new Pizza.Builder()
    .setSize("large")
    .addCheese()
    .build();
```

> ✅ **Advantage**: Clean, fluent API without polluting the main class.



## 🔸 **3. Local Classes**

### 📌 Characteristics
- Defined **inside a method or block**
- Can access **outer class members** + **final/effectively final** local variables
- Cannot have **static members** (except constants)
- Rarely used — mostly for legacy code

### 💡 Example: Phone Number Validator
```java
public class PhoneNumberValidator {
    static String regex = "[^0-9]";

    public void validate(String number) {
        final int VALID_LENGTH = 10; // Effectively final

        // Local class
        class PhoneNumber {
            String cleanNumber;

            PhoneNumber(String num) {
                String digits = num.replaceAll(regex, "");
                cleanNumber = (digits.length() == VALID_LENGTH) ? digits : null;
            }

            String getNumber() { return cleanNumber; }
        }

        PhoneNumber pn = new PhoneNumber(number);
        System.out.println(pn.getNumber());
    }
}
```

> 🔑 **Java 8+**: Local variables must be **effectively final** (not modified after initialization).



## 🔸 **4. Anonymous Classes**

### 📌 Characteristics
- **No name** — declared and instantiated in one expression
- Extend a class **or** implement an interface
- Common for **event handlers**, **runnables**, **comparators**
- Cannot have **constructors**

### 💡 Example: GUI Event Handling
```java
button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
});
```

### 💡 Example: Custom Comparator
```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.sort(new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return b.length() - a.length(); // Sort by length (desc)
    }
});
```

> ✅ **Modern Alternative**: Use **lambda expressions** (Java 8+) for single-method interfaces:
> ```java
> names.sort((a, b) -> b.length() - a.length());
> ```



## 🌓 **Shadowing in Nested Classes**

When names conflict, use **qualified `this`**:

```java
public class Outer {
    int x = 1;

    class Inner {
        int x = 2;

        void print() {
            System.out.println(x);           // 2 (inner)
            System.out.println(this.x);      // 2 (inner)
            System.out.println(Outer.this.x); // 1 (outer)
        }
    }
}
```

## ⚠️ **Important Limitations & Gotchas**

| Feature | Inner Class | Static Nested | Local | Anonymous |
|--------|------------|--------------|-------|----------|
| Access outer instance members | ✅ | ❌ (needs ref) | ✅ | ✅ |
| Access outer static members | ✅ | ✅ | ✅ | ✅ |
| Static fields/methods | ❌ (except `final`) | ✅ | ❌ (except `final`) | ❌ (except `final`) |
| Implement interfaces | ✅ | ✅ | ✅ | ✅ |
| Extend classes | ✅ | ✅ | ✅ | ✅ |
| Constructors | ✅ | ✅ | ✅ | ❌ |
| Serialization | 🚫 Discouraged | ✅ Safe | 🚫 Discouraged | 🚫 Discouraged |

> 🚫 **Never serialize inner/anonymous classes** — they create **synthetic fields** that break compatibility across JVMs.


## 📋 **When to Use Which?**

| Scenario | Best Choice |
|---------|------------|
| Helper class used only by one class | **Static nested class** |
| Need access to outer instance state | **Inner class** |
| One-time use (e.g., event listener) | **Anonymous class** → or **lambda** |
| Complex logic inside a method | **Local class** (rare) |
| Builder pattern | **Static nested class** |
| Iterator/Comparator implementations | **Static nested** or **anonymous** |



## 🛠️ **Best Practices**

1. **Prefer static nested classes** over inner classes (less memory overhead)
2. **Use anonymous classes sparingly** — prefer lambdas for functional interfaces
3. **Avoid local classes** unless absolutely necessary
4. **Never make inner classes `public`** — breaks encapsulation
5. **For builders/helpers**: Always use `static` nested classes



## 💡 **Real-World Example: Data Structure with Iterator**

```java
public class DataStructure {
    private int[] data = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    // Static nested interface
    public interface Iterator {
        boolean hasNext();
        int next();
    }

    // Inner class (needs access to 'data')
    private class EvenIterator implements Iterator {
        private int index = 0;

        public boolean hasNext() {
            return index < data.length;
        }

        public int next() {
            int value = data[index];
            index += 2;
            return value;
        }
    }

    public void printEven() {
        Iterator it = new EvenIterator(); // Inner class instantiation
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
    }
}
```

Output: `0 2 4 6 8`

> ✅ **Why inner class?** Because `EvenIterator` needs direct access to the `private` `data` array.



## 🎯 **Key Takeaways**

- **Nested classes** = powerful tool for **encapsulation** and **logical grouping**
- **Inner classes** = for when you need **instance-level access**
- **Static nested classes** = preferred for **helpers/builders** (more efficient)
- **Anonymous classes** = legacy way to implement single-use interfaces (use **lambdas** instead when possible)
- **Avoid serialization** of non-static nested classes

Master nested classes, and you’ll write **cleaner, more maintainable, and more secure** Java code! 🚀