# **Strings in Java: Static**

## **PART 1: STATIC STRINGS (IMMUTABLE)**

### **What are Static Strings?**
Strings that don't change after creation. Java's `String` class represents static strings.

### **Characteristics:**
- ✅ **Immutable**: Cannot be modified after creation
- ✅ **String Pool**: Memory optimization via shared storage
- ✅ **Thread-safe**: Multiple threads can read safely
- ✅ **Predictable**: Behavior never changes

### **How Java Stores Static Strings: The String Pool**
```java
// String Pool Visualization
String s1 = "Hello";      // Creates in pool
String s2 = "Hello";      // Reuses from pool
String s3 = new String("Hello"); // Creates in heap (bypasses pool)

// Memory layout:
// String Pool: ["Hello"] ← s1, s2 point here
// Heap: [String object "Hello"] ← s3 points here
```

### **When to Use Static Strings:**
1. **Constants and Configuration**
```java
public class Config {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/db";
    public static final String APP_NAME = "MyApp";
    public static final String[] WEEKDAYS = {"Mon", "Tue", "Wed", "Thu", "Fri"};
}
```

2. **Messages and UI Text**
```java
public class Messages {
    public static final String WELCOME = "Welcome to our application!";
    public static final String ERROR_404 = "Page not found";
    public static final String SUCCESS = "Operation completed successfully";
}
```

3. **Keys and Identifiers**
```java
public class API {
    public static final String API_KEY = "sk-123456789";
    public static final String USER_AGENT = "MyApp/1.0";
    public static final String CONTENT_TYPE = "application/json";
}
```

### **Performance Benefits of Static Strings**
```java
// Compile-time concatenation (FAST)
String optimized = "Hello" + " " + "World";  // Becomes "Hello World" at compile time

// String interning (MEMORY EFFICIENT)
String s1 = "Java";
String s2 = "Java";
String s3 = "Ja" + "va";

System.out.println(s1 == s2);  // true (same reference)
System.out.println(s1 == s3);  // true (compiler optimizes)
```

### **Best Practices for Static Strings:**
```java
// 1. Use final for constants
private static final String PREFIX = "usr_";

// 2. Use string literals for comparisons
if ("admin".equals(userRole)) {  // Prevents NPE
    // ...
}

// 3. Pre-compute concatenated strings
// BAD: Computed every time
public String greet(String name) {
    return "Hello " + name + "!";  // New string each call
}

// GOOD: Pre-computed
private static final String GREETING_PREFIX = "Hello ";
private static final String GREETING_SUFFIX = "!";
public String greet(String name) {
    return GREETING_PREFIX + name + GREETING_SUFFIX;
}
```

# **Strings in Java - Cheat Sheet**

## **String Fundamentals**
```java
// Creation
String s1 = "Hello";                    // String literal (in pool)
String s2 = new String("Hello");        // New object
char[] chars = {'H', 'e', 'l', 'l', 'o'};
String s3 = new String(chars);          // From char array

// Immutability
String s = "Hello";
s = s + " World";                       // New object created!
```

## **Key Properties**
- Immutable (cannot be changed after creation)
- Stored in String Pool (for literals)
- Implements `CharSequence` interface
- Zero-based indexing
- `length()` method (NOT a field like arrays!)

## **Important Methods**

### **Basic Operations**
```java
String str = "Hello World";

// Length & Access
int len = str.length();                 // 11
char ch = str.charAt(0);                // 'H'
char[] arr = str.toCharArray();         // Convert to char[]

// Comparison
boolean eq = str.equals("Hello");       // false (case-sensitive)
boolean eqIgnore = str.equalsIgnoreCase("hello world"); // true
int cmp = str.compareTo("Hello");       // Positive (lexicographic)
int cmpIgnore = str.compareToIgnoreCase("hello"); // Positive

boolean starts = str.startsWith("Hel"); // true
boolean ends = str.endsWith("rld");     // true

// Searching
int idx = str.indexOf('o');             // 4 (first occurrence)
int idx2 = str.indexOf('o', 5);         // 7 (from index 5)
int lastIdx = str.lastIndexOf('o');     // 7
int idxStr = str.indexOf("World");      // 6

boolean contains = str.contains("World"); // true

// Case Conversion
String upper = str.toUpperCase();       // "HELLO WORLD"
String lower = str.toLowerCase();       // "hello world"

// Trimming & Whitespace
String trimmed = "  hello  ".trim();    // "hello"
boolean empty = str.isEmpty();          // false
boolean blank = "   ".isBlank();        // true (Java 11+)

// Substrings
String sub1 = str.substring(6);         // "World"
String sub2 = str.substring(0, 5);      // "Hello" (0 to 4)

// Replace
String rep1 = str.replace('l', 'x');    // "Hexxo Worxd"
String rep2 = str.replace("World", "Java"); // "Hello Java"
String repAll = str.replaceAll("l+", "L"); // Regex replace
String repFirst = str.replaceFirst("l", "L"); // "HeLlo World"

// Splitting & Joining
String[] parts = str.split(" ");        // ["Hello", "World"]
String[] parts2 = str.split(" ", 2);    // ["Hello", "World"]
String[] parts3 = "a,b,c".split(",");   // ["a", "b", "c"]

String joined = String.join("-", "a", "b", "c"); // "a-b-c"
String joined2 = String.join(", ", parts); // "Hello, World"

// Formatting
String fmt = String.format("Value: %d, Text: %s", 10, "Hi");
String concat = "Hello".concat(" World"); // "Hello World"
```

### **Character Methods (Character class)**
```java
Character.isLetter('a');      // true
Character.isDigit('5');       // true
Character.isWhitespace(' ');  // true
Character.isUpperCase('A');   // true
Character.isLowerCase('a');   // true
Character.toUpperCase('a');   // 'A'
Character.toLowerCase('A');   // 'a'
```

## **Edge Cases to Test**
- Empty string
- Single character
- All same characters
- Very long strings
- Unicode characters
- Multiple spaces/punctuation
- Nested parentheses
- Strings with numbers
- Case sensitivity issues

## **Performance Considerations**
1. Use `charAt()` instead of `toCharArray()` when possible
2. Pre-compile regex patterns for repeated use
3. Use `StringBuilder` for building strings in loops
4. Consider using character frequency arrays (size 256 or 26)
5. Use two-pointer techniques for in-place modifications

## **Learning Objectives**
- Master string immutability and its implications
- Understand character encoding (ASCII/Unicode)
- Implement common string algorithms
- Handle edge cases and special characters
- Optimize for time and space complexity
- Practice with regex and pattern matching

**Key Insight**: Strings are immutable char arrays with helper methods. Understanding this helps optimize string operations!