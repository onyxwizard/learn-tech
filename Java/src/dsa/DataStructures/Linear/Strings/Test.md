# **String vs StringBuilder vs StringBuffer: The Ultimate Test!**

## **Question 1: Basic Understanding**
Which class creates IMMUTABLE strings in Java?
```java
A) StringBuilder
B) StringBuffer  
C) String
D) All of them
```

---

## **Question 2: Thread Safety**
You're building a web server that handles multiple requests simultaneously. Which string class should you use for shared log messages?

**A)** `String`  
**B)** `StringBuilder`  
**C)** `StringBuffer`  
**D)** It doesn't matter

---

## **Question 3: Performance in Loops**
What's the output of this code?
```java
String result = "";
for (int i = 0; i < 3; i++) {
    result += i;
}
System.out.println(result);
```

**A)** "012"  
**B)** "3"  
**C)** "111"  
**D)** Compilation error

---

## **Question 4: String Pool Trick**
What does this print?
```java
String s1 = "Java";
String s2 = "Java";
String s3 = new String("Java");
String s4 = s3.intern();

System.out.println(s1 == s2);
System.out.println(s1 == s3);
System.out.println(s1 == s4);
```

**A)** true, false, true  
**B)** true, true, true  
**C)** false, false, true  
**D)** true, false, false

---

## **Question 5: Mutable vs Immutable**
Which operation is NOT allowed?
```java
// For each class, what happens?
String str = "Hello";
StringBuilder sb = new StringBuilder("Hello");
StringBuffer sbf = new StringBuffer("Hello");

A) str = str + " World";
B) sb.append(" World");
C) sbf.append(" World");
D) str.append(" World");  // ← This one?
```

---

## **Question 6: Real-World Scenario**
You're processing a large CSV file (100,000 lines). Which approach is MOST efficient?
```java
// Option 1:
String content = "";
for (String line : lines) {
    content += line + "\n";
}

// Option 2:
StringBuilder content = new StringBuilder();
for (String line : lines) {
    content.append(line).append("\n");
}

// Option 3:
StringBuffer content = new StringBuffer();
for (String line : lines) {
    content.append(line).append("\n");
}
```

**A)** Option 1 - Simplest is best  
**B)** Option 2 - Fastest for single thread  
**C)** Option 3 - Thread-safe is always better  
**D)** All are equally efficient

---

## **Question 7: Method Chaining**
What does this print?
```java
StringBuilder sb = new StringBuilder("Hello");
sb.append(" ").insert(6, "World").reverse();
System.out.println(sb);
```

**A)** "Hello World"  
**B)** "dlroW olleH"  
**C)** "World Hello"  
**D)** " olleHdlroW"

---

## **Question 8: Memory Efficiency**
Why is this code problematic?
```java
String html = "<html>";
for (int i = 0; i < 1000; i++) {
    html += "<div>Item " + i + "</div>";
}
html += "</html>";
```

**A)** Creates too many String objects  
**B)** StringBuilder would be better  
**C)** Should use StringBuffer for thread safety  
**D)** All of the above

---

## **Question 9: Conversion**
How do you convert a StringBuilder to a String?
```java
StringBuilder sb = new StringBuilder("Hello");

A) String str = sb;
B) String str = sb.toString();
C) String str = (String)sb;
D) String str = new String(sb);
```

---

## **Question 10: The Intern Method**
What does `intern()` do?
```java
String s1 = new String("Java");
String s2 = s1.intern();

A) Makes the string mutable
B) Adds string to String Pool if not already there
C) Converts to uppercase
D) Trims whitespace
```

---

## **Question 11: Capacity Management**
What's the default initial capacity of StringBuilder?
```java
StringBuilder sb = new StringBuilder();

A) 10 characters
B) 16 characters  
C) 32 characters
D) It depends on the string length
```

---

## **Question 12: Synchronization**
What's the key difference between StringBuilder and StringBuffer?
```java
// In StringBuilder class:
public StringBuilder append(String str) {
    // No synchronization
}

// In StringBuffer class:
public synchronized StringBuffer append(String str) {
    // Synchronized method
}
```

**A)** StringBuilder is faster but not thread-safe  
**B)** StringBuffer is slower but thread-safe  
**C)** Both are correct  
**D)** Neither is correct

---

## **Question 13: API Similarity**
True or False: StringBuilder and StringBuffer have identical methods (append, insert, delete, reverse, etc.)

**A)** True - Same API, different synchronization  
**B)** False - Completely different methods  
**C)** True, but only for basic operations  
**D)** False - StringBuffer has more methods

---

## **Question 14: Practical Choice**
You're writing a single-threaded command-line tool that builds SQL queries. Which should you use?

**A)** `String` - Simple and readable  
**B)** `StringBuilder` - Efficient for building  
**C)** `StringBuffer` - Future-proof for threading  
**D)** `char[]` - Most efficient

---

## **Question 15: Final Challenge**
What's the output?
```java
String s1 = "Hello";
String s2 = "Hello";
StringBuilder sb1 = new StringBuilder("Hello");
StringBuilder sb2 = new StringBuilder("Hello");

System.out.println(s1 == s2);
System.out.println(s1.equals(s2));
System.out.println(sb1 == sb2);
System.out.println(sb1.equals(sb2));
```

**A)** true, true, false, false  
**B)** true, true, false, true  
**C)** false, true, false, false  
**D)** true, true, true, true

---

## **Answers**
<details>
<summary>Click to see answers</summary>

**Q1:** C) String  
*Only String is immutable. StringBuilder and StringBuffer are mutable.*

**Q2:** C) StringBuffer  
*StringBuffer is synchronized (thread-safe). StringBuilder is not.*

**Q3:** A) "012"  
*Works but inefficient - creates 4 String objects!*

**Q4:** A) true, false, true  
*s1 and s2 share pool, s3 is new object, s4 interns to pool.*

**Q5:** D) `str.append(" World")`  
*String has no append() method - it's immutable!*

**Q6:** B) Option 2  
*StringBuilder is fastest for single-threaded loops.*

**Q7:** B) "dlroW olleH"  
*Trace: "Hello " → "Hello World" → "dlroW olleH"*

**Q8:** A) Creates too many String objects  
*Each += creates new String. Use StringBuilder!*

**Q9:** B) `sb.toString()`  
*toString() converts to immutable String.*

**Q10:** B) Adds string to String Pool if not already there  
*intern() ensures string is in pool, returns reference.*

**Q11:** B) 16 characters  
*Default capacity is 16, grows as needed.*

**Q12:** C) Both are correct  
*StringBuilder: faster, not synchronized. StringBuffer: slower, synchronized.*

**Q13:** A) True - Same API, different synchronization  
*They have identical public methods, just different synchronization.*

**Q14:** B) StringBuilder  
*Single-threaded + building strings = StringBuilder is perfect.*

**Q15:** A) true, true, false, false  
*Strings compare content with equals(), StringBuilder doesn't override equals().*

</details>

---

## **Scoring:**
- **15/15**: String Guru! 🏆
- **12-14**: Excellent! You understand all three classes! 🌟
- **9-11**: Good foundation, needs a bit more practice 👍
- **6-8**: Time to review the differences 📚
- **Below 6**: Let's go through the concepts again 🔄

## **Key Takeaways:**
1. **String**: Immutable, thread-safe, uses String Pool
2. **StringBuilder**: Mutable, NOT thread-safe, fastest for single thread
3. **StringBuffer**: Mutable, thread-safe (synchronized), slightly slower

**Remember:** `String` for storage, `StringBuilder`/`StringBuffer` for construction!

**How did you score? Share your results and any confusing questions!**