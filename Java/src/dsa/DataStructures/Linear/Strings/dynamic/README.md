# **Strings in Java: Dynamic**

## **PART 2: DYNAMIC STRINGS (MUTABLE)**

### **What are Dynamic Strings?**
Strings that change during program execution. Use `StringBuilder` or `StringBuffer`.

### **When Dynamic Strings Shine:**
1. **Building Strings in Loops**
```java
// Static approach (INEFFICIENT - creates 1000+ objects!)
String result = "";
for (int i = 0; i < 1000; i++) {
    result += "Item " + i + "\n";  // Creates new String each iteration
}

// Dynamic approach (EFFICIENT - 1 object!)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append("Item ").append(i).append("\n");
}
String result = sb.toString();  // Convert to String when done
```

2. **User Input Processing**
```java
// Dynamic string building from user input
StringBuilder userInput = new StringBuilder();
Scanner scanner = new Scanner(System.in);

System.out.println("Enter lines (type 'END' to finish):");
while (true) {
    String line = scanner.nextLine();
    if ("END".equalsIgnoreCase(line)) break;
    userInput.append(line).append("\n");
}

String finalInput = userInput.toString();
```

3. **Template Processing**
```java
public String generateEmail(String name, String orderId, String status) {
    StringBuilder email = new StringBuilder();
    email.append("Dear ").append(name).append(",\n\n");
    email.append("Your order #").append(orderId);
    email.append(" is now ").append(status).append(".\n\n");
    email.append("Thank you for shopping with us!\n");
    
    return email.toString();  // Convert to immutable String
}
```

### **StringBuilder vs StringBuffer: The Choice**

#### **StringBuilder (Single-threaded)**
```java
// When only ONE thread accesses it
StringBuilder receipt = new StringBuilder();
receipt.append("=== RECEIPT ===\n");
receipt.append("Item 1: $10.99\n");
receipt.append("Item 2: $5.49\n");
receipt.append("Total: $").append(16.48).append("\n");

String finalReceipt = receipt.toString();
```

#### **StringBuffer (Multi-threaded)**
```java
// When MULTIPLE threads access it
public class SharedLogger {
    private StringBuffer log = new StringBuffer();
    
    // Thread-safe append
    public synchronized void logMessage(String message) {
        log.append(new Date()).append(": ").append(message).append("\n");
    }
    
    public String getLog() {
        return log.toString();
    }
}
```

### **Dynamic String Operations**
```java
StringBuilder sb = new StringBuilder("Start");

// Chain operations (fluent API)
sb.append(" middle")
  .insert(6, "some ")
  .replace(11, 17, "text")
  .delete(15, 20)
  .reverse();

System.out.println(sb);  // "txet emos tratS"

// Reset and reuse
sb.setLength(0);  // Clear contents
sb.append("Fresh start");
```

### **Performance Comparison**
```java
public class StringPerformanceTest {
    public static void main(String[] args) {
        int iterations = 100000;
        
        // Test 1: String concatenation (SLOW)
        long start = System.nanoTime();
        String staticResult = "";
        for (int i = 0; i < iterations; i++) {
            staticResult += i;  // Creates new object each time!
        }
        long staticTime = System.nanoTime() - start;
        
        // Test 2: StringBuilder (FAST)
        start = System.nanoTime();
        StringBuilder dynamicBuilder = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            dynamicBuilder.append(i);  // Modifies same object
        }
        String dynamicResult = dynamicBuilder.toString();
        long builderTime = System.nanoTime() - start;
        
        System.out.println("Static (String): " + staticTime / 1_000_000 + "ms");
        System.out.println("Dynamic (StringBuilder): " + builderTime / 1_000_000 + "ms");
        System.out.println("Speedup: " + (staticTime / builderTime) + "x faster!");
    }
}
```

### **Real-World Pattern: Builder Pattern with Strings**
```java
public class SQLQueryBuilder {
    private StringBuilder query = new StringBuilder();
    
    public SQLQueryBuilder select(String... columns) {
        query.append("SELECT ");
        if (columns.length == 0) {
            query.append("*");
        } else {
            for (int i = 0; i < columns.length; i++) {
                if (i > 0) query.append(", ");
                query.append(columns[i]);
            }
        }
        return this;
    }
    
    public SQLQueryBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }
    
    public SQLQueryBuilder where(String condition) {
        query.append(" WHERE ").append(condition);
        return this;
    }
    
    public String build() {
        return query.toString();
    }
}

// Usage:
String query = new SQLQueryBuilder()
    .select("id", "name", "email")
    .from("users")
    .where("active = true")
    .build();
```

---

## **PART 3: WHEN TO USE WHICH?**

### **Decision Table**

| Scenario | Use | Why |
|----------|-----|-----|
| **Constants, Config values** | `String` (static) | Immutable, predictable, memory-efficient |
| **Message formats** | `String` (static) | Read-only, thread-safe |
| **Loop concatenations** | `StringBuilder` (dynamic) | Avoids creating multiple objects |
| **User input building** | `StringBuilder` (dynamic) | Dynamic size, efficient modifications |
| **Multi-threaded logging** | `StringBuffer` (dynamic) | Thread-safe modifications |
| **Template generation** | `StringBuilder` (dynamic) | Efficient piece-by-piece building |
| **Return values from methods** | `String` (static) | Immutable, safe to share |

### **Conversion Between Types**
```java
// Static → Dynamic
String staticStr = "Hello";
StringBuilder dynamicStr = new StringBuilder(staticStr);

// Dynamic → Static
StringBuilder builder = new StringBuilder("Dynamic");
String staticResult = builder.toString();  // Freeze into immutable

// Appending static to dynamic
StringBuilder sb = new StringBuilder();
sb.append("Static text: ").append(staticStr);
```

### **Memory Management Tips**
```java
// 1. Pre-size StringBuilder when possible
StringBuilder sb = new StringBuilder(1000);  // Initial capacity
// Better than default (16) if you know approximate size

// 2. Reuse StringBuilder
StringBuilder reusable = new StringBuilder();
for (int i = 0; i < 10; i++) {
    reusable.setLength(0);  // Clear for reuse
    reusable.append("Iteration ").append(i);
    process(reusable.toString());
}

// 3. Use string literals for common prefixes/suffixes
private static final String HTML_START = "<div class='content'>";
private static final String HTML_END = "</div>";

public String wrapContent(String content) {
    return HTML_START + content + HTML_END;  // Efficient
}
```

## **Summary**

### **Static Strings (`String` class):**
- **Use for**: Constants, messages, keys, return values
- **Strength**: Thread-safe, memory-efficient (pool), predictable
- **Weakness**: Expensive to modify (creates new objects)

### **Dynamic Strings (`StringBuilder`/`StringBuffer`):**
- **Use for**: Loops, user input, templates, building operations
- **Strength**: Efficient modifications, mutable, good performance
- **Weakness**: Not thread-safe (except StringBuffer), more complex

### **Golden Rule:**
> **"Use static strings for storage, dynamic strings for construction."**

Build with `StringBuilder`, store as `String` when complete! 🚀