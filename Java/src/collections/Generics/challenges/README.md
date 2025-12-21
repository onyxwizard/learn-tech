# 🧪 **Java Generics Practical Knowledge Test**

> **Time:** 60-90 minutes | **Difficulty:** Intermediate to Advanced | **Format:** Code Challenges + Conceptual Questions

## 📋 **Instructions**
1. Write code to solve each problem
2. Explain your reasoning for conceptual questions
3. Test your code with edge cases
4. No looking at previous answers - trust your knowledge!

---

## 🔢 **PART 1: Implementation Challenges** (60 points)

### **Challenge 1: Generic Stack** (10 points)
Implement a type-safe `GenericStack<T>` class with the following requirements:
- Must be fully generic (no raw types)
- Should have: `push(T item)`, `pop()`, `peek()`, `isEmpty()`, `size()` methods
- Internal storage must use an array (not `ArrayList`)
- Handle array resizing when capacity is reached
- No `@SuppressWarnings` allowed except in `pop()` method (explain why)

```java
// Write your implementation here
```

**Follow-up:** Why can't we create `new T[capacity]` in the constructor? What's the workaround and what are the risks?

---

### **Challenge 2: Type-Safe Pair with Constraints** (10 points)
Create a `ConstrainedPair<T, U>` class where:
- `T` must be a `Number` or its subclass
- `U` must implement both `Comparable<U>` and `Serializable`
- Include methods: `swap()` that returns a new `ConstrainedPair<U, T>`
- Implement `equals()` and `hashCode()` that respect type safety

```java
// Write your implementation here
```

**Follow-up:** How would you modify this to make `T` accept any type that has a `doubleValue()` method, even if it doesn't extend `Number`?

---

### **Challenge 3: Generic Algorithm - Merge Sorted Lists** (15 points)
Implement a generic method that merges two sorted lists into one sorted list:
- Method signature: `<T extends Comparable<? super T>> List<T> mergeSortedLists(List<? extends T> list1, List<? extends T> list2)`
- Must be efficient (O(n+m) time complexity)
- Should preserve stability (equal elements maintain original order)
- Must not modify the input lists
- Handle `null` lists gracefully

```java
// Write your implementation here
```

**Follow-up:** Why do we use `Comparable<? super T>` instead of just `Comparable<T>`? Give a concrete example where this matters.

---

### **Challenge 4: Wildcard Processor** (10 points)
Create a utility class `CollectionProcessor` with these methods:

1. **Count elements matching predicate:**
   ```java
   static <T> long countMatches(Collection<? extends T> collection, 
                                 Predicate<? super T> predicate)
   ```

2. **Copy with transformation (PECS applied correctly):**
   ```java
   static <T, R> List<R> transformCopy(List<? extends T> source, 
                                       Function<? super T, ? extends R> transformer)
   ```

3. **Find maximum using custom comparator (handle empty collections):**
   ```java
   static <T> Optional<T> findMax(Collection<? extends T> collection, 
                                  Comparator<? super T> comparator)
   ```

```java
// Write your implementation here
```

**Follow-up:** Explain why each wildcard (`extends`/`super`) is positioned where it is in the method signatures.

---

### **Challenge 5: Bridge Method Analysis** (15 points)
Given this inheritance hierarchy:

```java
interface Processor<T> {
    void process(T item);
    T getResult();
}

class StringProcessor implements Processor<String> {
    private String result;
    
    @Override
    public void process(String item) {
        result = item.toUpperCase();
    }
    
    @Override
    public String getResult() {
        return result;
    }
}
```

1. **Write what the compiler generates after type erasure**
2. **Show all bridge methods explicitly**
3. **Explain what happens when this code runs:**
   ```java
   Processor raw = new StringProcessor();
   raw.process(123);  // Integer instead of String
   ```
4. **Write a test that proves where the ClassCastException occurs**

```java
// Write your analysis and test here
```

---

## 💭 **PART 2: Conceptual Questions** (40 points)

### **Question 1: Type Erasure Mysteries** (10 points)
Given this code:

```java
List<String> strings = new ArrayList<>();
strings.add("Hello");
Class<?> clazz = strings.getClass();
```

1. What does `clazz` represent at runtime? `ArrayList` or `ArrayList<String>`?
2. Can you use reflection to determine that this is a `List<String>` at runtime?
3. If you have a method parameter `List<? extends Number>`, what is its erased type?
4. Why does `List<Integer>` get erased to `List` but `List<? extends Number>` also erases to `List`?

---

### **Question 2: The PECS Principle** (10 points)
Consider this method from `java.util.Collections`:

```java
public static <T> void copy(List<? super T> dest, List<? extends T> src)
```

1. Explain why `dest` uses `? super T` and `src` uses `? extends T`
2. What would break if we swapped them: `copy(List<? extends T> dest, List<? super T> src)`?
3. Write a concrete example showing both correct and incorrect usage
4. How does PECS relate to covariance and contravariance?

---

### **Question 3: Generic Array Creation Problem** (10 points)
Explain ALL the problems with this code and provide the best fix:

```java
public class Stack<T> {
    private T[] elements;
    private int size;
    
    @SuppressWarnings("unchecked")
    public Stack(int capacity) {
        elements = (T[]) new Object[capacity];
    }
    
    public void push(T item) {
        elements[size++] = item;
    }
    
    public T pop() {
        return elements[--size];
    }
    
    public T[] toArray() {
        return elements;  // Problem here!
    }
}
```

**Specific questions:**
1. What warning are we suppressing and why is it unavoidable?
2. What's wrong with the `toArray()` method?
3. How could a client cause a `ClassCastException`?
4. What's the best practice solution for this pattern?

---

### **Question 4: Advanced Wildcard Puzzle** (10 points)
Explain what's happening in this code and predict the output:

```java
List<? super Integer> list1 = new ArrayList<Number>();
List<? extends Number> list2 = new ArrayList<Integer>();

// Which of these compile? Why or why not?
list1.add(42);           // A
// list1.add(3.14);     // B
// Integer i = list1.get(0);  // C
Object o = list1.get(0); // D

// list2.add(42);       // E  
// list2.add(null);     // F
Number n = list2.get(0); // G
// Integer j = list2.get(0); // H

// What about this?
List<?> wildcardList = new ArrayList<String>();
// wildcardList.add("test");  // I
wildcardList.add(null);       // J
Object obj = wildcardList.get(0); // K
```

For each line (A-K), explain:
1. Does it compile?
2. If not, why?
3. If yes, what type is added/retrieved?
4. What are the type safety guarantees?

---

## 🎯 **BONUS: Real-World Scenario** (Extra 20 points)

### **Scenario: Generic Repository Pattern**
Design a generic `Repository<T, ID>` interface for database operations with these requirements:

1. `T` is the entity type, `ID` is the ID type (must be `Comparable` and `Serializable`)
2. Methods should include: `save(T entity)`, `findById(ID id)`, `delete(ID id)`, `findAll()`
3. Add a method `findByCriteria(Predicate<? super T> criteria)` that returns matching entities
4. Add a method `update(ID id, Function<? super T, ? extends T> updater)`
5. Ensure the API is type-safe and follows PECS

Then implement a `InMemoryRepository<T, ID>` that stores entities in a `Map<ID, T>`.

**Challenge:** How would you handle the case where `T` needs to have a method `ID getId()` without requiring `T` to implement a specific interface?

```java
// Design your solution here
```

---

## 📝 **Evaluation Criteria**

| Criteria | Points | What We're Looking For |
|----------|--------|------------------------|
| **Correctness** | 40 | Code compiles, meets requirements |
| **Type Safety** | 20 | No unsafe casts, proper wildcards |
| **PECS Application** | 15 | Correct use of extends/super |
| **Edge Cases** | 10 | Handles nulls, empty collections, etc. |
| **Explanation Quality** | 15 | Clear reasoning, understands "why" |

## 🎯 **Passing Standards**
- **80+ points:** Generics Expert 🏆
- **60-79 points:** Proficient ✅
- **40-59 points:** Needs Review 📚
- **<40 points:** Back to Fundamentals 🔄


## 🧠 **Hints (Only if stuck)**
1. Remember: Producers use `extends`, Consumers use `super`
2. Type erasure means runtime doesn't know generic types
3. Arrays are covariant, generics are invariant
4. You can't instantiate type parameters
5. Bridge methods maintain polymorphism after erasure

**⏰ Time yourself!** When finished, compare your answers with the ideal solutions. Be honest about what you needed to look up!

Ready? Set... Go! 🚀