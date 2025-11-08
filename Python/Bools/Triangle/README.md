

# 📐 Triangle Classifier 🌟

Welcome to the **Triangle Classifier** exercise! In this challenge, you'll determine whether a triangle is **equilateral**, **isosceles**, or **scalene** based on the lengths of its sides. You'll also ensure that the given side lengths satisfy the **triangle inequality** rules. This exercise will help you practice conditional logic, mathematical reasoning, and writing clean, reusable functions.



## 📋 Overview

Triangles are classified based on the lengths of their sides:

1. **Equilateral Triangle**: All three sides are of the same length.  
   Example: **`a = b = c`**

2. **Isosceles Triangle**: At least two sides are of the same length.  
   Example: **`a = b ≠ c`** or **`b = c ≠ a`** or **`a = c ≠ b`**

3. **Scalene Triangle**: All three sides are of different lengths.  
   Example: **`a ≠ b ≠ c`**

### Triangle Validity
For a shape to qualify as a triangle:
- All sides must have a length greater than 0.
- The sum of the lengths of any two sides must be greater than or equal to the length of the third side (**Triangle Inequality**):

   ```
   a + b ≥ c,   b + c ≥ a,   a + c ≥ b
   ```

If these conditions are not met, the given side lengths do **not** form a valid triangle.



## 🎯 Instructions

Your task is to implement the following functions:

### 1. **Check if the Triangle is Valid**
Write the `is_valid_triangle(a, b, c)` function that takes three side lengths as input and returns `True` if the sides form a valid triangle, and `False` otherwise.

**Example:**
```python
>>> is_valid_triangle(3, 4, 5)
True
>>> is_valid_triangle(1, 1, 2)
False
```

### 2. **Classify the Triangle**
Write the `classify_triangle(a, b, c)` function that takes three side lengths as input and returns the type of triangle (`"Equilateral"`, `"Isosceles"`, or `"Scalene"`). If the sides do not form a valid triangle, return `"Not a valid triangle"`.

**Example:**
```python
>>> classify_triangle(3, 3, 3)
'Equilateral'
>>> classify_triangle(3, 4, 4)
'Isosceles'
>>> classify_triangle(3, 4, 5)
'Scalene'
>>> classify_triangle(1, 1, 2)
'Not a valid triangle'
```


## 🔧 Function Documentation

Each function comes with detailed docstrings to explain its purpose, parameters, and return values:

### `is_valid_triangle(a, b, c)`
```python
"""Determine if the given sides form a valid triangle.
:param a: float - length of side a.
:param b: float - length of side b.
:param c: float - length of side c.
:return: bool - True if the sides form a valid triangle, False otherwise.

This function checks if the triangle inequality holds for the given sides:
    - a + b ≥ c
    - b + c ≥ a
    - a + c ≥ b
"""
```

### `classify_triangle(a, b, c)`
```python
"""Classify the type of triangle based on its side lengths.
:param a: float - length of side a.
:param b: float - length of side b.
:param c: float - length of side c.
:return: str - type of triangle ("Equilateral", "Isosceles", "Scalene", or "Not a valid triangle").

This function determines the type of triangle based on the side lengths:
    - Equilateral: All three sides are equal.
    - Isosceles: At least two sides are equal.
    - Scalene: All sides are different.
    - Not a valid triangle: If the sides do not satisfy the triangle inequality.
"""
```



## 🚀 How to Get Started

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the `is_valid_triangle` and `classify_triangle` functions.
3. Implement the functions as described in the instructions.
4. Test your code using the provided examples or additional test cases.
5. Refactor and optimize your code for clarity and efficiency.


## 📚 Example Usage

Here’s how you can use the completed functions in your Python environment:

```python
# Check if a triangle is valid
>>> is_valid_triangle(3, 4, 5)
True
>>> is_valid_triangle(1, 1, 2)
False

# Classify a triangle
>>> classify_triangle(3, 3, 3)
'Equilateral'
>>> classify_triangle(3, 4, 4)
'Isosceles'
>>> classify_triangle(3, 4, 5)
'Scalene'
>>> classify_triangle(1, 1, 2)
'Not a valid triangle'
```


## 💡 Why Solve This Problem?

- **Mathematical Insight**: Learn about triangle classification and the triangle inequality theorem. 📐  
- **Programming Practice**: Combine practical problem-solving with learning Python fundamentals. 🌟  
- **Fun Challenge**: Test your skills with a classic coding problem that has real-world relevance. 🎉  


## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more features (e.g., calculating triangle area using Heron's formula).
- Improving the documentation.
- Reporting bugs or suggesting enhancements.



## 🕹️ Interactive Triangle 🌟

To make this project more interactive, we’ve added a simple command-line interface where users can input the side lengths of a triangle and get instant feedback on its validity and type. Here's how it works:

### How to Use the Interactive Mode
1. Run the Python script in your terminal or IDE.
2. Follow the prompts to enter the side lengths of the triangle.
3. The program will output whether the triangle is valid and, if so, classify it as equilateral, isosceles, or scalene.

### Example Interaction
```plaintext
Welcome to the Interactive Triangle Classifier! 📐

Enter the length of side A: 3
Enter the length of side B: 4
Enter the length of side C: 5

The triangle is valid!
Type: Scalene

Thank you for using the Triangle Classifier! 🌟


Happy coding—and may your triangles always be valid! 📐✨