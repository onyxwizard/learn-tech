# 🎓 Student Grade Analyzer — Java Language Basics Capstone

A console application that demonstrates **all core Java language fundamentals** in one cohesive project.

## 🧠 Concepts Demonstrated

- ✅ **Variables & Naming** (`camelCase`, meaningful names)
- ✅ **Primitive Types** (`int`, `double`, `boolean`)
- ✅ **Arrays** (jagged, fixed-length)
- ✅ **`var`** (local variable type inference)
- ✅ **Operators** (arithmetic, relational, logical, ternary)
- ✅ **Expressions, Statements, Blocks**
- ✅ **Control Flow** (`if`/`else`, `for`, `while`)
- ✅ **Branching** (`break`, `continue`, `return`)
- ✅ **Switch Statements & Expressions** (with `yield`)

## 📁 Project Structure

```
src/
└── basics/
    ├── model/
    │   └── Student.java          // Immutable data holder
    ├── lab/
    │   └── GradeAnalyzer.java    // Core logic — pure Java basics
    └── demo/
        └── GradeReportDemo.java  // Main runner
```

## ▶️ How to Run

```bash
# Compile
javac src/basics/demo/GradeReportDemo.java

# Run
java basics.demo.GradeReportDemo
```

## 🖨️ Sample Output

```
📊 STUDENT GRADE REPORT
========================================
Processing 3 students...

✅ Alice (Grades: [88, 92, 79])
   → Avg: 86.3 | Letter: B
   → Status: Passing

✅ Bob (Grades: [95, 87])
   → Avg: 91.0 | Letter: A
   → Status: Passing

✅ Charlie (Grades: [76, 82, 90, 88])
   → Avg: 84.0 | Letter: B
   → Status: Passing

📈 CLASS SUMMARY
Total Students: 3
Passing Rate: 100.0%
Highest Avg: 91.0 (Bob)
Lowest Avg: 84.0 (Charlie)
```



## 🛠️ **Step-by-Step Implementation Guide**

Follow this order to build like an SDE:



### 🔹 **Step 1: `Student.java` (Model Layer)**

**Purpose**: Hold immutable student data.  
**Rules**:
- No methods — just `public final` fields
- Constructor initializes fields
- Package: `basics.model`

```java
package basics.model;

public class Student {
    public final String name;
    public final int[] grades;

    public Student(String name, int[] grades) {
        this.name = name;
        // Defensive copy (optional but good practice)
        this.grades = grades.clone();
    }
}
```

> 💡 **Why `final`?** Ensures immutability — a core Java best practice.



### 🔹 **Step 2: `GradeAnalyzer.java` (Business Logic)**

**Purpose**: Process student data using **all Java basics**.  
**Key Methods to Implement**:

| Method | Purpose | Concepts Used |
|-------|--------|--------------|
| `analyzeStudent(Student s)` | Compute stats for one student | `var`, arrays, loops, operators, ternary |
| `getLetterGrade(double avg)` | Convert avg → A/B/C | `switch` expression + `yield` |
| `isPassing(double avg)` | Check if avg ≥ 60 | Relational operators, `boolean` |
| `generateReport(Student[] students)` | Full report generation | Control flow, blocks, statements |
| `calculateClassSummary(Student[] students)` | Class-wide stats | Arrays, `break`/`continue`, expressions |

#### 📌 **Critical Requirements for Each Method**

##### 1. `analyzeStudent(Student s)`
- Use `var` for all local variables
- Calculate **sum** with a `for` loop (not streams!)
- Compute **average** as `double`
- Use **ternary operator** for simple decisions
- Print student line with **expressions** like:  
  `"→ Avg: " + String.format("%.1f", avg)`

##### 2. `getLetterGrade(double avg)`
- **Must use `switch` expression with `yield`** (Java 14+)
- Handle ranges:
    - A: ≥ 90
    - B: ≥ 80
    - C: ≥ 70
    - D: ≥ 60
    - F: < 60

```java
return switch ((int) avg / 10) {
    case 10, 9 -> "A";
    case 8 -> "B";
    case 7 -> "C";
    case 6 -> "D";
    default -> {
        // Complex logic block
        if (avg < 0) yield "Invalid";
        yield "F";
    }
};
```

##### 3. `isPassing(double avg)`
- Simple method: `return avg >= 60.0;`
- Demonstrates **relational operator** and **boolean expression**

##### 4. `generateReport(Student[] students)`
- Use **blocks** `{}` to group related statements
- Use **labeled `continue`** to skip invalid students (if any)
- Print separator lines with **expressions**: `"=".repeat(40)`

##### 5. `calculateClassSummary(...)`
- Find **highest/lowest avg** with `break`/`continue` in loops
- Calculate **passing rate** with **logical operators**
- Use **compound assignment** (`+=`) for totals



### 🔹 **Step 3: `GradeReportDemo.java` (Entry Point)**

**Purpose**: Orchestrate the report generation.  
**Rules**:
- Create **jagged array** of `Student` objects
- Use `var` for all local variables
- Call `GradeAnalyzer.generateReport()`
- Handle edge cases (empty array)

```java
package basics.demo;

import basics.lab.GradeAnalyzer;
import basics.model.Student;

public class GradeReportDemo {
    public static void main(String[] args) {
        // 🧪 Test data: jagged array of students
        var students = new Student[] {
            new Student("Alice", new int[]{88, 92, 79}),
            new Student("Bob", new int[]{95, 87}),
            new Student("Charlie", new int[]{76, 82, 90, 88})
        };

        // 🚀 Generate report
        var analyzer = new GradeAnalyzer();
        analyzer.generateReport(students);
    }
}
```

> 💡 **Why jagged array?** Demonstrates **real-world irregular data** (students have different # of grades).



## 🧪 **Key Implementation Tips (SDE Mindset)**

1. **No Streams or Collections**  
   → This project is about **core language basics**, not APIs.

2. **Use `var` Everywhere (Local Only)**  
   → `var students = ...`, `var avg = ...`, etc.

3. **Prefer Switch Expressions Over Statements**  
   → Modern Java style (with `yield` for complex cases).

4. **Handle Edge Cases**  
   → Empty grades array, negative grades, etc.

5. **Format Output Cleanly**  
   → Use `String.format("%.1f", avg)` for decimals.

6. **Comment Key Concepts**  
   → Add comments like `// TERNARY OPERATOR` or `// LABELED CONTINUE`.



## 🚀 **Final Checklist Before Running**

- [✅] `Student` class has `final` fields
- [✅] `GradeAnalyzer` uses **no streams**
- [✅] `getLetterGrade()` uses **`switch` expression + `yield`**
- [✅] All local variables use **`var`**
- [✅] Report shows **avg, letter grade, passing status**
- [✅] Class summary includes **passing rate, highest/lowest**
- [✅] Code compiles on **Java 14+** (for `yield`)



## 💡 **Why This Project Matters**

This isn’t just another demo — it’s a **microcosm of real Java code**:
- You’re using **language primitives** (not frameworks)
- You’re making **explicit control flow decisions**
- You’re handling **real data irregularities** (jagged arrays)
- You’re writing **readable, modern Java** (`var`, switch expressions)

When you finish this, you’ll have **mastered Java fundamentals** — and be ready for **OOP, Collections, and beyond**.
