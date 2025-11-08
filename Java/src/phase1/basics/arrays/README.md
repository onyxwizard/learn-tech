## 🎯 Project: **"Student Grade Manager"** 📊
**A Console Application to Manage & Analyze Student Grades Using Arrays**

> ✅ **Goal**: Apply **all core array concepts** from the dev.java guide in one practical, runnable program.



### 📁 File to Create
```
StudentGradeManager.java
```

> 💡 Run with: `java StudentGradeManager.java` (JDK 11+)



### 🧱 Project Requirements

#### 1. **Use a Jagged (Irregular) 2D Array**
- Store grades for **4 students**, where each student has a **different number of subjects**.
- Use **`double[][]`** to hold decimal grades.
- Example structure:
  ```java
  double[][] grades = {
      {88.5, 92.0, 79.5},        // Student 0
      {95.0, 87.5},              // Student 1
      {76.0, 82.5, 90.0, 88.0},  // Student 2
      {100.0}                    // Student 3
  };
  ```
> ✅ Demonstrates: **multidimensional arrays**, **jagged arrays**, **array literals**



#### 2. **Print All Grades Using Traditional `for` Loops**
- Loop through each student using `grades.length`
- For each student, loop through their grades using `grades[i].length`
- Output format:
  ```
  Student 0: 88.5 92.0 79.5
  Student 1: 95.0 87.5
  ...
  ```
> ✅ Teaches: **`.length` property**, **safe indexing**, **nested loops**


#### 3. **Calculate & Display Stats per Student**
For **each student**, compute:
- **Sum** of grades
- **Average** (`sum / count`)
- **Minimum** and **Maximum** grade

Use the **enhanced `for` loop** (`for (double grade : grades[i])`) for calculations.

Output example:
```
Student 0 → Avg: 86.7, Min: 79.5, Max: 92.0
Student 1 → Avg: 91.2, Min: 87.5, Max: 95.0
...
```
> ✅ Covers: **enhanced for loop**, **array traversal**, **numeric operations**



#### 4. **Copy One Student’s Grades Using `System.arraycopy()`**
- Create a new array to hold a copy of **Student 0’s grades**
- Use `System.arraycopy()` to perform the copy
- **Modify the first element** of the copy (e.g., set to `0.0`)
- Print **both original and copy** to prove they are **independent**

> ✅ Demonstrates: **`System.arraycopy()`**, **array independence**, **manual copying**


#### 5. **Use the `Arrays` Utility Class**
- Print the **entire 2D array** using `Arrays.deepToString()`
- **Sort** one student’s grades (e.g., Student 2) using `Arrays.sort()`
- Print the sorted result using `Arrays.toString()`

Example:
```java
System.out.println("Full grade book: " + Arrays.deepToString(grades));
Arrays.sort(grades[2]);
System.out.println("Sorted Student 2: " + Arrays.toString(grades[2]));
```
> ✅ Shows: **`Arrays.toString()`**, **`deepToString()`**, **`sort()`**, **utility methods**



### 📏 Concepts You’ll Practice (Mapped to dev.java)

| dev.java Topic | How It Appears in Your Code |
|----------------|------------------------------|
| **Array Declaration** | `double[][] grades = { ... };` |
| **Array Literals** | Shorthand initialization with `{}` |
| **Indexing (0-based)** | `grades[0][1]` |
| **`.length`** | Used in loops for safe traversal |
| **Multidimensional Arrays** | `double[][]` |
| **Jagged Arrays** | Rows of different lengths |
| **Traditional `for` Loop** | Index-based iteration |
| **Enhanced `for` Loop** | Clean value-based iteration |
| **`System.arraycopy()`** | Low-level copying |
| **`Arrays` Class** | `sort()`, `toString()`, `deepToString()` |



### 🖨️ Expected Final Output (Example)
```
Student 0: 88.5 92.0 79.5
Student 1: 95.0 87.5
Student 2: 76.0 82.5 90.0 88.0
Student 3: 100.0

Student 0 → Avg: 86.7, Min: 79.5, Max: 92.0
Student 1 → Avg: 91.2, Min: 87.5, Max: 95.0
Student 2 → Avg: 84.1, Min: 76.0, Max: 90.0
Student 3 → Avg: 100.0, Min: 100.0, Max: 100.0

Original Student 0: [88.5, 92.0, 79.5]
Modified Copy:      [0.0, 92.0, 79.5]

📊 Full grade book:
[[88.5, 92.0, 79.5], [95.0, 87.5], [76.0, 82.5, 90.0, 88.0], [100.0]]
Sorted Student 2: [76.0, 82.5, 88.0, 90.0]
```



### ✅ Your Task
1. Create `StudentGradeManager.java`
2. Implement all 5 requirements above
3. Run it with:
   ```bash
   java StudentGradeManager.java
   ```
4. **Submit your code for review**

Once submitted, I’ll:
- ✅ Confirm correct array usage
- 🔍 Check loop logic and `.length` safety
- 💡 Suggest improvements (if any)

This project will **solidify your array skills** before you move to **operators**, **control flow**, or **Collections Framework**.

Ready to build it? 😊