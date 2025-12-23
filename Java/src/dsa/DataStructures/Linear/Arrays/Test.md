# **Java Arrays Quiz: Static vs Dynamic**

## **Question 1: Basic Array Creation**
What happens when you run this code?
```java
int[] arr = new int[3];
arr[0] = 10;
arr[1] = 20;
arr[2] = 30;
arr[3] = 40;
System.out.println(arr[3]);
```

**A)** Prints 40  
**B)** Compilation error  
**C)** Runtime ArrayIndexOutOfBoundsException  
**D)** Prints 0

---

## **Question 2: Array Size**
After creating this array, can you change its size?
```java
String[] names = new String[5];
```

**A)** Yes, using `names.resize(10)`  
**B)** Yes, using `names.length = 10`  
**C)** No, array size is fixed at creation  
**D)** Only if it's declared as `dynamic`

---

## **Question 3: Array Copy vs Reference**
What does this print?
```java
int[] original = {1, 2, 3};
int[] copy = original;
copy[0] = 100;
System.out.println(original[0]);
```

**A)** 1  
**B)** 100  
**C)** 0  
**D)** NullPointerException

---

## **Question 4: Dynamic Alternative**
Which Java collection provides a dynamic alternative to arrays?
```java
// Instead of: String[] names = new String[10];
// We can use:
```

**A)** `HashMap<String> names = new HashMap<>();`  
**B)** `ArrayList<String> names = new ArrayList<>();`  
**C)** `HashSet<String> names = new HashSet<>();`  
**D)** `LinkedList<String> names = new LinkedList<>();`

---

## **Question 5: Resizing Workaround**
How do you effectively "resize" an array in Java?
```java
int[] small = {1, 2, 3};
// Want to add more elements...
```

**A)** `small.add(4);`  
**B)** `small.length = 5;`  
**C)** Create a new larger array and copy elements  
**D)** Use `Arrays.resize(small, 5);`

---

## **Question 6: Multi-dimensional Array**
True or False: This creates a truly dynamic 2D array?
```java
int[][] matrix = new int[3][];
matrix[0] = new int[2];
matrix[1] = new int[4];
matrix[2] = new int[3];
```

**A)** True - each row can have different lengths  
**B)** False - all rows must be same length  
**C)** True - but only for the first dimension  
**D)** False - 2D arrays are always square

---

## **Question 7: Initialization**
Which is **NOT** a valid array initialization?
```java
A) int[] arr = {1, 2, 3};
B) int[] arr = new int[]{1, 2, 3};
C) int[] arr = new int[3]{1, 2, 3};
D) int[] arr = new int[3];
```

---

## **Question 8: Default Values**
What are the default values in this array?
```java
boolean[] flags = new boolean[3];
System.out.println(flags[0] + " " + flags[1] + " " + flags[2]);
```

**A)** true true true  
**B)** false false false  
**C)** null null null  
**D)** 0 0 0

---

## **Question 9: Array vs ArrayList**
What's the main advantage of arrays over ArrayList?
```java
// Array approach:
int[] numbers = new int[1000000];

// ArrayList approach:
ArrayList<Integer> numbersList = new ArrayList<>();
```

**A)** Arrays can resize dynamically  
**B)** Arrays have better performance for primitive types  
**C)** Arrays are easier to use  
**D)** Arrays can store different data types

---

## **Question 10: Practical Scenario**
You're building a student grade system. You know you'll have exactly 30 students this semester. Which should you use?

**A)** `int[] grades = new int[30];`  
**B)** `ArrayList<Integer> grades = new ArrayList<>();`  
**C)** `HashMap<Integer> grades = new HashMap<>();`  
**D)** `LinkedList<Integer> grades = new LinkedList<>();`

---

## **Answers**
Try to answer all questions first, then check below:

<details>
<summary>Click to see answers</summary>

**Q1:** C) Runtime ArrayIndexOutOfBoundsException  
*Explanation: Arrays have fixed size. Index 3 doesn't exist in a size-3 array (indices 0,1,2).*

**Q2:** C) No, array size is fixed at creation  
*Explanation: Once created with `new int[5]`, it's always 5 elements. No resize method exists.*

**Q3:** B) 100  
*Explanation: `copy = original` copies the reference, not the array. Both point to same array.*

**Q4:** B) `ArrayList<String> names = new ArrayList<>();`  
*Explanation: ArrayList grows/shrinks dynamically, unlike fixed-size arrays.*

**Q5:** C) Create a new larger array and copy elements  
*Explanation: You must manually create new array and use `System.arraycopy()` or loop.*

**Q6:** A) True - each row can have different lengths  
*Explanation: Java supports "jagged arrays" where each row has different length.*

**Q7:** C) `int[] arr = new int[3]{1, 2, 3};`  
*Explanation: Can't specify both size and values in same initialization.*

**Q8:** B) false false false  
*Explanation: boolean arrays default to `false`, int to `0`, object arrays to `null`.*

**Q9:** B) Arrays have better performance for primitive types  
*Explanation: ArrayList stores objects (Integer), causing boxing overhead. Arrays store primitives directly.*

**Q10:** A) `int[] grades = new int[30];`  
*Explanation: When size is known and fixed, arrays are simpler and more efficient.*

</details>

---

## **Score Yourself:**
- **10/10**: Array Master! 🎯
- **7-9/10**: Good understanding! 👍
- **5-6/10**: Needs some review 📚
- **Below 5**: Let's review the basics again 🔄

**How did you do? Share your score and any questions you're unsure about!**