# Java hashCode() and equals() - Complete Guide & Cheatsheet

## 📋 Overview
The **`hashCode()` and `equals()` methods** are fundamental to Java's object equality system and are crucial for proper functioning with Java Collections. These methods work together to enable efficient object comparison, storage, and retrieval in hash-based collections like `HashMap`, `HashSet`, and `Hashtable`.


## 🔑 Core Principles

### ✅ **Contract Between hashCode() and equals()**
1. **Consistency Rule**: If two objects are equal according to `equals()`, they must have the same `hashCode()`
2. **Non-Reverse Rule**: Equal hash codes do NOT guarantee object equality (hash collisions are allowed)
3. **Multiple Invocation Rule**: `hashCode()` must return the same value for an object unless it's modified
4. **Null Safety**: `equals(null)` must return `false`

### ✅ **Importance in Collections**
| Collection | Uses equals() | Uses hashCode() |
|------------|--------------|-----------------|
| `HashMap` | ✅ Key comparison | ✅ Bucket determination |
| `HashSet` | ✅ Element comparison | ✅ Bucket determination |
| `Hashtable` | ✅ Key comparison | ✅ Bucket determination |
| `ArrayList` | ✅ `contains()`, `remove()` | ❌ |
| `TreeMap` | ✅ Key comparison (via Comparator/Comparable) | ❌ |
| `LinkedList` | ✅ `contains()`, `remove()` | ❌ |

---

## ⚖️ equals() Method Deep Dive

### **Default Object.equals() Behavior**
```java
public class Object {
    public boolean equals(Object obj) {
        return (this == obj);  // Reference equality
    }
}
```

### **When to Override equals()**
✅ **Override when:**
- Logical equality differs from object identity
- Objects with same field values should be considered equal
- Using objects as keys in `HashMap` or elements in `HashSet`
- Need value-based comparison in collections

❌ **Don't override when:**
- Each instance is inherently unique (like `Thread`)
- Superclass already provides appropriate implementation
- Class is private or package-private and you control all usage

### **equals() Method Contract**
```java
// The contract:
// 1. Reflexive: x.equals(x) must return true
// 2. Symmetric: x.equals(y) must return same as y.equals(x)
// 3. Transitive: if x.equals(y) and y.equals(z), then x.equals(z)
// 4. Consistent: multiple calls must return same result unless modified
// 5. Null comparison: x.equals(null) must return false
```

### **Proper equals() Implementation**
```java
import java.util.Objects;

public class Employee {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    
    @Override
    public boolean equals(Object obj) {
        // 1. Check reference equality (fast path)
        if (this == obj) return true;
        
        // 2. Check null and class type
        if (obj == null || getClass() != obj.getClass()) return false;
        
        // 3. Cast to correct type
        Employee other = (Employee) obj;
        
        // 4. Compare fields (primitive fields first for performance)
        return id == other.id &&
               Objects.equals(firstName, other.firstName) &&
               Objects.equals(lastName, other.lastName) &&
               Objects.equals(email, other.email);
    }
}
```

### **Step-by-Step equals() Implementation Guide**
```java
public boolean equals(Object obj) {
    // Step 1: Reference equality check (fast path)
    if (this == obj) return true;
    
    // Step 2: Null check
    if (obj == null) return false;
    
    // Step 3: Class check (choose one approach)
    
    // Approach A: Exact class match (strict)
    if (getClass() != obj.getClass()) return false;
    
    // Approach B: instanceof with final class or careful design
    // if (!(obj instanceof Employee)) return false;
    // Employee other = (Employee) obj;
    
    // Step 4: Cast
    Employee other = (Employee) obj;
    
    // Step 5: Compare fields
    // Primitives first (performance)
    if (id != other.id) return false;
    
    // Objects using Objects.equals() (null-safe)
    if (!Objects.equals(firstName, other.firstName)) return false;
    if (!Objects.equals(lastName, other.lastName)) return false;
    if (!Objects.equals(email, other.email)) return false;
    
    // Step 6: Arrays comparison
    // if (!Arrays.equals(someArray, other.someArray)) return false;
    
    return true;
}
```

### **Common equals() Patterns**
```java
// 1. Simple class with primitives
public class Point {
    private final int x, y;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}

// 2. Class with inheritance
public abstract class Shape {
    private final String color;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        Shape shape = (Shape) o;
        return Objects.equals(color, shape.color);
    }
}

public class Circle extends Shape {
    private final double radius;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;
        if (!super.equals(o)) return false;
        Circle circle = (Circle) o;
        return Double.compare(circle.radius, radius) == 0;
    }
}

// 3. Using Lombok @EqualsAndHashCode
@EqualsAndHashCode
public class Employee {
    private long id;
    private String name;
    // Lombok generates equals() and hashCode()
}

// 4. Record classes (Java 14+)
public record Employee(long id, String name, String email) {
    // Auto-generated equals() and hashCode()
}
```

### **equals() with Inheritance Considerations**
```java
// Problem: Symmetry violation with inheritance
public class Point {
    private final int x, y;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }
}

public class ColoredPoint extends Point {
    private final Color color;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColoredPoint)) return false;
        ColoredPoint cp = (ColoredPoint) o;
        return super.equals(o) && Objects.equals(color, cp.color);
    }
}

// Violation: 
// Point p = new Point(1, 2);
// ColoredPoint cp = new ColoredPoint(1, 2, Color.RED);
// p.equals(cp) // true (Point.equals sees it as Point)
// cp.equals(p) // false (ColoredPoint.equals requires ColoredPoint)

// Solution 1: Use getClass() for strict equality
@Override
public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    // ...
}

// Solution 2: Use composition over inheritance
public class ColoredPoint {
    private final Point point;
    private final Color color;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColoredPoint)) return false;
        ColoredPoint cp = (ColoredPoint) o;
        return point.equals(cp.point) && Objects.equals(color, cp.color);
    }
}
```

---

## 🔢 hashCode() Method Deep Dive

### **Default Object.hashCode() Behavior**
```java
public class Object {
    public native int hashCode();  // Typically memory address based
}
```

### **Purpose of hashCode()**
1. **Bucket determination** in hash-based collections
2. **Performance optimization** for lookups (O(1) average case)
3. **Distribution** of objects across hash table

### **hashCode() Contract**
```java
// The contract:
// 1. Consistency: Must return same value for unchanged object
// 2. Equality implication: If a.equals(b) then a.hashCode() == b.hashCode()
// 3. Non-uniqueness: Different objects can have same hashCode (collisions)
```

### **Proper hashCode() Implementation**
```java
import java.util.Objects;

public class Employee {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    
    @Override
    public int hashCode() {
        // Using Objects.hash() - recommended for simplicity
        return Objects.hash(id, firstName, lastName, email);
        
        // Alternative: Manual implementation for performance
        // int result = (int) (id ^ (id >>> 32));
        // result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        // result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        // result = 31 * result + (email != null ? email.hashCode() : 0);
        // return result;
    }
}
```

### **Step-by-Step hashCode() Implementation Guide**
```java
@Override
public int hashCode() {
    // Step 1: Start with a non-zero constant (odd prime)
    int result = 17;  // or any other non-zero odd number
    
    // Step 2: Include each significant field
    // For primitive fields: use Type.hashCode(field) or convert
    result = 31 * result + Long.hashCode(id);
    
    // For object fields: use Objects.hashCode() (handles null)
    result = 31 * result + Objects.hashCode(firstName);
    result = 31 * result + Objects.hashCode(lastName);
    result = 31 * result + Objects.hashCode(email);
    
    // For arrays: use Arrays.hashCode()
    // result = 31 * result + Arrays.hashCode(someArray);
    
    // Step 3: Return result
    return result;
}
```

### **Why 31?**
- **Prime number**: Reduces collisions
- **Odd number**: Better distribution
- **Efficient computation**: `31 * i = (i << 5) - i` (compiler optimization)
- **Good distribution**: Empirical studies show good results

### **Common hashCode() Patterns**
```java
// 1. Simple class
public class Point {
    private final int x, y;
    
    @Override
    public int hashCode() {
        return 31 * (31 + x) + y;
        // Or: return Objects.hash(x, y);
    }
}

// 2. Class with inheritance
public abstract class Shape {
    private final String color;
    
    @Override
    public int hashCode() {
        return Objects.hashCode(color);
    }
}

public class Circle extends Shape {
    private final double radius;
    
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Double.hashCode(radius);
    }
}

// 3. Cached hashCode (immutable objects)
public class Employee {
    private final long id;
    private final String name;
    private int hashCode;  // Default 0
    
    @Override
    public int hashCode() {
        int h = hashCode;
        if (h == 0) {  // Lazy initialization
            h = Objects.hash(id, name);
            hashCode = h;
        }
        return h;
    }
}

// 4. Enum hashCode (inherited from Enum, don't override)
public enum Status {
    ACTIVE, INACTIVE, PENDING;
    // hashCode() is final in Enum
}
```

### **Optimizing hashCode() Performance**
```java
public class OptimizedEmployee {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final int hashCode;  // Computed in constructor
    
    public OptimizedEmployee(long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        // Precompute hashCode for immutable objects
        this.hashCode = computeHashCode();
    }
    
    private int computeHashCode() {
        int result = Long.hashCode(id);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }
    
    @Override
    public int hashCode() {
        return hashCode;  // Just return precomputed value
    }
    
    @Override
    public boolean equals(Object o) {
        // Fast path: check cached hashCode first
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptimizedEmployee that = (OptimizedEmployee) o;
        
        // Quick reject: different hashCodes mean different objects
        if (hashCode != that.hashCode) return false;
        
        // Full equality check
        return id == that.id &&
               Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName);
    }
}
```

---

## 🔗 Relationship Between equals() and hashCode()

### **The Critical Contract**
```java
// MUST maintain: a.equals(b) == true → a.hashCode() == b.hashCode()
// Failure causes undefined behavior in hash-based collections

// Example of contract violation
public class BrokenEmployee {
    private long id;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BrokenEmployee)) return false;
        return id == ((BrokenEmployee) o).id;
    }
    
    // Missing hashCode() - uses Object.hashCode()
    // Two equal objects may have different hashCodes!
}

// Problem in HashMap:
BrokenEmployee e1 = new BrokenEmployee(1);
BrokenEmployee e2 = new BrokenEmployee(1);  // e1.equals(e2) == true
map.put(e1, "value");
map.get(e2);  // May return null! (different bucket)
```

### **Visualizing the Contract**
```
┌─────────────────────────────────────────────────────────┐
│                    HashMap Operation                     │
├─────────────────────────────────────────────────────────┤
│ 1. Compute key.hashCode()                               │
│ 2. Find bucket: hashCode % tableSize                    │
│ 3. In bucket, compare keys using equals()               │
│ 4. If equals() returns true, return value               │
└─────────────────────────────────────────────────────────┘

FAILURE SCENARIO if contract broken:
- e1.equals(e2) = true, but e1.hashCode() ≠ e2.hashCode()
- e1 and e2 go to different buckets
- HashMap.get(e2) won't find e1's value
```

### **Testing the Contract**
```java
public class ContractTest {
    public static void testContract(Object a, Object b) {
        boolean equals = a.equals(b);
        int hashA = a.hashCode();
        int hashB = b.hashCode();
        
        System.out.println("a.equals(b): " + equals);
        System.out.println("a.hashCode(): " + hashA);
        System.out.println("b.hashCode(): " + hashB);
        
        if (equals && hashA != hashB) {
            throw new AssertionError("CONTRACT VIOLATION: " +
                "Objects are equal but have different hashCodes!");
        }
        
        if (hashA == hashB && !equals) {
            System.out.println("Hash collision (allowed)");
        }
    }
    
    public static void main(String[] args) {
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(1, "John", "Doe");
        
        testContract(e1, e2);  // Should pass
    }
}
```

---

## 🛠️ Implementation Strategies

### **Using Objects Utility Methods**
```java
import java.util.Objects;

public class ModernEmployee {
    private final long id;
    private final String name;
    private final String department;
    
    @Override
    public boolean equals(Object o) {
        // One-liner with Objects.equals()
        return o instanceof ModernEmployee other &&
               id == other.id &&
               Objects.equals(name, other.name) &&
               Objects.equals(department, other.department);
    }
    
    @Override
    public int hashCode() {
        // One-liner with Objects.hash()
        return Objects.hash(id, name, department);
    }
}
```

### **Manual Implementation (Performance Optimized)**
```java
public class PerformanceEmployee {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        PerformanceEmployee that = (PerformanceEmployee) o;
        
        // Compare most discriminating fields first
        if (id != that.id) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        return email.equals(that.email);
    }
    
    @Override
    public int hashCode() {
        // Manual calculation for better performance
        int result = Long.hashCode(id);
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }
}
```

### **Using Record (Java 14+)**
```java
// Auto-generated equals() and hashCode()
public record EmployeeRecord(long id, String name, String email) {
    // No need to implement equals() and hashCode()
    // They are automatically generated based on all components
}

// Customizing record behavior
public record EmployeeRecord(long id, String name, String email) {
    // Additional validation or methods
    public EmployeeRecord {
        if (id <= 0) throw new IllegalArgumentException("Invalid ID");
    }
    
    // Can still override if needed
    @Override
    public boolean equals(Object o) {
        return o instanceof EmployeeRecord other &&
               id == other.id;  // Custom equality
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(id);  // Must match equals()
    }
}
```

### **Using Lombok**
```java
import lombok.EqualsAndHashCode;

// All fields included
@EqualsAndHashCode
public class EmployeeLombok {
    private long id;
    private String name;
    private String email;
}

// Customizing field inclusion
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeLombokCustom {
    @EqualsAndHashCode.Include
    private long id;
    
    private String name;  // Not included in equals/hashCode
    
    @EqualsAndHashCode.Include
    private String email;
}

// With inheritance
@EqualsAndHashCode(callSuper = true)
public class Manager extends EmployeeLombok {
    private List<Employee> team;
}
```

### **Using Apache Commons Lang**
```java
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class EmployeeCommons {
    private long id;
    private String name;
    private String email;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        EmployeeCommons that = (EmployeeCommons) o;
        
        return new EqualsBuilder()
                .append(id, that.id)
                .append(name, that.name)
                .append(email, that.email)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(email)
                .toHashCode();
    }
}
```

---

## 💡 Best Practices

### ✅ **Do:**
```java
// 1. Override both equals() and hashCode() together
public class CorrectClass {
    @Override public boolean equals(Object o) { /* ... */ }
    @Override public int hashCode() { /* ... */ }
}

// 2. Use Objects.equals() and Objects.hash() for null safety
@Override
public boolean equals(Object o) {
    return o instanceof MyClass other &&
           Objects.equals(field1, other.field1) &&
           Objects.equals(field2, other.field2);
}

@Override
public int hashCode() {
    return Objects.hash(field1, field2);
}

// 3. Include all significant fields in hashCode()
// Fields used in equals() MUST be in hashCode()

// 4. Make fields final for immutable objects with cached hashCode
public final class ImmutableClass {
    private final String data;
    private final int hashCode;
    
    public ImmutableClass(String data) {
        this.data = data;
        this.hashCode = Objects.hashCode(data);
    }
    
    @Override public int hashCode() { return hashCode; }
}

// 5. Test the contract
assert a.equals(b) ? a.hashCode() == b.hashCode() : true;

// 6. Consider performance: compare cheap fields first
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MyClass)) return false;
    MyClass other = (MyClass) o;
    
    // Compare ints before strings
    if (id != other.id) return false;
    if (version != other.version) return false;
    return expensiveField.equals(other.expensiveField);
}
```

### ❌ **Don't:**
```java
// 1. Don't override only one method
public class BrokenClass {
    @Override
    public boolean equals(Object o) { /* ... */ }
    // Missing hashCode()!
}

// 2. Don't use mutable fields in hashCode()
public class MutableKey {
    private int value;
    
    @Override
    public int hashCode() {
        return value;  // Changes if value changes!
    }
    
    // If used as HashMap key and value changes,
    // the key becomes "lost" in wrong bucket
}

// 3. Don't ignore inheritance issues
public class Base {
    private int baseField;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Base)) return false;
        return baseField == ((Base) o).baseField;
    }
}

public class Derived extends Base {
    private int derivedField;
    
    // Problem: symmetry broken with Base
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Derived)) return false;
        return super.equals(o) && derivedField == ((Derived) o).derivedField;
    }
}

// 4. Don't use == for object comparison (except primitives/enums)
@Override
public boolean equals(Object o) {
    if (!(o instanceof MyClass)) return false;
    MyClass other = (MyClass) o;
    
    // WRONG: return field == other.field;  // Compares references!
    // CORRECT: return Objects.equals(field, other.field);
}

// 5. Don't forget the null check
@Override
public boolean equals(Object o) {
    // Missing: if (o == null) return false;
    if (!(o instanceof MyClass)) return false;
    // ...
}
```

### ⚠️ **Common Pitfalls:**
```java
// 1. Forgetting to override hashCode()
Map<Employee, String> map = new HashMap<>();
Employee e1 = new Employee(1, "John");
Employee e2 = new Employee(1, "John");  // equals(e1) == true

map.put(e1, "value");
String result = map.get(e2);  // null! (different hashCode)

// 2. Using mutable fields
public class Account {
    private String accountNumber;
    private double balance;
    
    @Override
    public int hashCode() {
        return accountNumber.hashCode();  // Good
    }
    
    // Problem if balance is in equals()
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account)) return false;
        Account other = (Account) o;
        return accountNumber.equals(other.accountNumber) &&
               balance == other.balance;  // balance can change!
    }
}

// 3. Inconsistent with compareTo() (for Comparable objects)
public class Person implements Comparable<Person> {
    private String name;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) return false;
        return name.equals(((Person) o).name);
    }
    
    @Override
    public int compareTo(Person other) {
        return name.compareTo(other.name);
    }
    
    // Must ensure: a.compareTo(b) == 0 ⇔ a.equals(b)
    // Otherwise TreeSet/TreeMap behavior is undefined
}

// 4. Overloading instead of overriding
public class MyClass {
    // This is overloading, not overriding!
    public boolean equals(MyClass other) {
        return ...;  // Wrong signature!
    }
    
    // Correct:
    @Override
    public boolean equals(Object other) {
        ...
    }
}

// 5. Ignoring arrays in equals()
public class DataHolder {
    private int[] data;
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataHolder)) return false;
        DataHolder other = (DataHolder) o;
        
        // WRONG: return data.equals(other.data);  // Compares references
        // CORRECT: return Arrays.equals(data, other.data);
    }
    
    @Override
    public int hashCode() {
        // WRONG: return data.hashCode();
        // CORRECT: return Arrays.hashCode(data);
    }
}
```

---

## 🔍 Testing equals() and hashCode()

### **JUnit Test Template**
```java
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

public class EmployeeTest {
    
    @Test
    public void testEqualsReflexive() {
        Employee e = new Employee(1, "John", "Doe");
        assertTrue("Object must equal itself", e.equals(e));
    }
    
    @Test
    public void testEqualsSymmetric() {
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(1, "John", "Doe");
        assertTrue("Symmetric property", 
            e1.equals(e2) && e2.equals(e1));
    }
    
    @Test
    public void testEqualsTransitive() {
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(1, "John", "Doe");
        Employee e3 = new Employee(1, "John", "Doe");
        
        assertTrue("Transitive property",
            e1.equals(e2) && e2.equals(e3) && e1.equals(e3));
    }
    
    @Test
    public void testEqualsNull() {
        Employee e = new Employee(1, "John", "Doe");
        assertFalse("Must return false for null", e.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        Employee e = new Employee(1, "John", "Doe");
        assertFalse("Must return false for different class", 
            e.equals("Not an Employee"));
    }
    
    @Test
    public void testHashCodeConsistency() {
        Employee e = new Employee(1, "John", "Doe");
        int hash1 = e.hashCode();
        int hash2 = e.hashCode();
        assertEquals("hashCode must be consistent", hash1, hash2);
    }
    
    @Test
    public void testHashCodeEqualsContract() {
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(1, "John", "Doe");
        
        assertTrue("If equals is true, hashCode must be equal",
            !e1.equals(e2) || e1.hashCode() == e2.hashCode());
    }
    
    @Test
    public void testInHashSet() {
        Set<Employee> set = new HashSet<>();
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(1, "John", "Doe");
        
        set.add(e1);
        assertTrue("Set must contain equal object", set.contains(e2));
        assertEquals("Set size must be 1", 1, set.size());
    }
    
    @Test
    public void testInHashMap() {
        Map<Employee, String> map = new HashMap<>();
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(1, "John", "Doe");
        
        map.put(e1, "value");
        assertEquals("HashMap must retrieve value for equal key",
            "value", map.get(e2));
    }
    
    @Test
    public void testNotEqualObjects() {
        Employee e1 = new Employee(1, "John", "Doe");
        Employee e2 = new Employee(2, "Jane", "Smith");
        assertFalse("Different objects must not be equal", e1.equals(e2));
        // Note: hashCode CAN be equal (collision allowed)
    }
}
```

### **Property-Based Testing**
```java
import net.jqwik.api.*;
import static org.assertj.core.api.Assertions.*;

@Property
void equalsIsReflexive(@ForAll Employee employee) {
    assertThat(employee).isEqualTo(employee);
}

@Property
void equalsIsSymmetric(@ForAll Employee e1, @ForAll Employee e2) {
    assertThat(e1.equals(e2)).isEqualTo(e2.equals(e1));
}

@Property
void hashCodeContract(@ForAll Employee e1, @ForAll Employee e2) {
    if (e1.equals(e2)) {
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
    }
}

@Property
void worksInHashSet(@ForAll Set<Employee> employees) {
    Set<Employee> set = new HashSet<>(employees);
    // All elements should be found
    for (Employee e : employees) {
        assertThat(set).contains(e);
    }
}
```

### **Using AssertJ for Readable Tests**
```java
import static org.assertj.core.api.Assertions.*;

@Test
public void testEmployee() {
    Employee e1 = new Employee(1, "John", "Doe");
    Employee e2 = new Employee(1, "John", "Doe");
    Employee e3 = new Employee(2, "Jane", "Smith");
    
    assertThat(e1)
        .isEqualTo(e2)
        .isNotEqualTo(e3)
        .isNotEqualTo(null)
        .isNotEqualTo("string")
        .hasSameHashCodeAs(e2);
    
    assertThat(e1.hashCode())
        .isEqualTo(e2.hashCode());
    
    // Test collections
    Set<Employee> set = new HashSet<>();
    set.add(e1);
    assertThat(set)
        .contains(e2)
        .hasSize(1);
}
```

---

## 🎯 Quick Decision Guide

### **When and How to Implement**
```
Class used as HashMap/HashSet key? → MUST override both equals() and hashCode()

Class used in ArrayList.contains()? → SHOULD override equals()

Immutable class? → Consider caching hashCode()

Inheritance hierarchy? → 
  ├── Use getClass() for strict equality
  ├── Or use instanceof with careful design
  └── Consider composition over inheritance

Performance critical? → 
  ├── Manual implementation over Objects.hash()
  ├── Compare cheap fields first in equals()
  └── Cache hashCode for immutable objects

Java 14+? → Consider record classes (auto-generated)

Using Lombok? → @EqualsAndHashCode annotation

Testing needed? → Test reflexive, symmetric, transitive properties and hashCode contract
```

### **Implementation Checklist**
```java
// ✅ equals() checklist:
// 1. @Override annotation
// 2. Parameter type: Object
// 3. Check this == obj
// 4. Check null
// 5. Check class (getClass() or instanceof)
// 6. Cast to correct type
// 7. Compare all significant fields
// 8. Use Objects.equals() for object fields
// 9. Use == for primitives
// 10. Handle arrays with Arrays.equals()

// ✅ hashCode() checklist:
// 1. @Override annotation
// 2. Include all fields used in equals()
// 3. Use prime number (31) as multiplier
// 4. Use Objects.hash() for simplicity
// 5. Or manual calculation for performance
// 6. Consider caching for immutable objects
// 7. Handle arrays with Arrays.hashCode()
```

### **Common Implementation Templates**
```java
// Template 1: Simple class with Objects utility
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MyClass)) return false;
    MyClass other = (MyClass) o;
    return field1 == other.field1 &&
           Objects.equals(field2, other.field2);
}

@Override
public int hashCode() {
    return Objects.hash(field1, field2);
}

// Template 2: Manual implementation (optimized)
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyClass other = (MyClass) o;
    // Compare in order of discrimination power
    if (id != other.id) return false;
    if (!name.equals(other.name)) return false;
    return Objects.equals(description, other.description);
}

@Override
public int hashCode() {
    int result = Long.hashCode(id);
    result = 31 * result + name.hashCode();
    result = 31 * result + (description != null ? description.hashCode() : 0);
    return result;
}

// Template 3: Record class (Java 14+)
public record MyRecord(long id, String name, String description) {
    // Auto-generated equals() and hashCode()
}
```

---

## 📖 Summary

The **`equals()` and `hashCode()` methods** are critical for:
- **Correct behavior** in hash-based collections (`HashMap`, `HashSet`)
- **Object comparison** and equality semantics
- **Performance optimization** in collections

**Key Takeaways:**
1. **Always override both methods together** when overriding either
2. **Maintain the contract**: Equal objects must have equal hash codes
3. **Include all significant fields** used in `equals()` in `hashCode()`
4. **Use `Objects.equals()` and `Objects.hash()`** for null safety and simplicity
5. **Consider `record` classes** (Java 14+) for data carriers with auto-generated methods
6. **Test thoroughly** for reflexive, symmetric, and transitive properties
7. **Avoid mutable fields** in `hashCode()` for objects used as map keys

**Remember**: Proper implementation of `equals()` and `hashCode()` is not just about correctness—it's about enabling efficient and reliable use of Java's powerful collection framework.

This comprehensive guide covers everything from basic principles to advanced patterns. Bookmark this cheatsheet for quick reference during your Java development work!