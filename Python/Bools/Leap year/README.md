# 📅 Leap Year Calculator 🌟

Welcome to the **Leap Year Calculator**! This project introduces you to determining whether a given year is a leap year based on the rules of the Gregorian calendar. By completing this exercise, you'll practice conditional logic and gain hands-on experience with Python's `if`, `elif`, and `else` statements.



## 📋 Overview

In the Gregorian calendar, a **leap year** occurs under the following conditions:

1. The year is **evenly divisible by 4**.
2. **Unless** the year is also **evenly divisible by 100**, in which case it's only a leap year if it's **evenly divisible by 400**.

### Examples:
- **1997**: Not a leap year (not divisible by 4). ❌
- **1900**: Not a leap year (divisible by 100 but not by 400). ❌
- **2000**: A leap year (divisible by 4, 100, and 400)! ✅

Your task is to implement a Python function that determines whether a given year qualifies as a leap year.



## 🎯 Instructions

Write a function `is_leap_year(year)` that takes an integer `year` as input and returns `True` if the year is a leap year, and `False` otherwise. Follow the rules outlined above.

### Example Usage:
```python
>>> is_leap_year(1997)
False
>>> is_leap_year(1900)
False
>>> is_leap_year(2000)
True
>>> is_leap_year(2024)
True
```



## 🔧 Function Documentation

The function comes with a detailed docstring to explain its purpose, parameters, and return values:

### `is_leap_year(year)`
```python
"""Determine whether a year is a leap year.
:param year: int - the year to check.
:return: bool - True if the year is a leap year, False otherwise.

This function checks if a year is a leap year based on the Gregorian calendar rules:
    - A year is a leap year if it is divisible by 4.
    - However, if the year is divisible by 100, it is NOT a leap year unless it is also divisible by 400.
"""
```



## 🚀 How to Get Started

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the `is_leap_year` function.
3. Implement the function as described in the instructions.
4. Test your code using the provided examples or additional test cases.
5. Refactor and optimize your code for clarity and efficiency.



## 📚 Example Usage

Here’s how you can use the completed function in your Python environment:

```python
# Check if a year is a leap year
>>> is_leap_year(1997)
False
>>> is_leap_year(1900)
False
>>> is_leap_year(2000)
True
>>> is_leap_year(2024)
True
```



## 🎯 Learning Objectives

By completing this project, you will:

- Understand **conditional logic** using `if`, `elif`, and `else`.
- Practice writing **functions** with parameters and return values.
- Gain experience with **docstrings** for documenting your code.
- Explore real-world applications of programming like calendar calculations.



## 💡 Why Solve This Problem?

- **Calendar Logic**: Learn how leap years work and their significance in the Gregorian calendar. 📅  
- **Programming Practice**: Combine practical problem-solving with learning Python fundamentals. 🌟  
- **Fun Challenge**: Test your skills with a classic coding problem that has real-world relevance. 🎉  



## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more features (e.g., calculating leap years within a range).
- Improving the documentation.
- Reporting bugs or suggesting enhancements.



Happy coding—and may your years always be leap years when you need them! 🌟📅