# 🔢 Armstrong Number Checker 🧮

Welcome to the **Armstrong Number Checker**, an Exercism challenge designed to help you practice Python programming while exploring the fascinating world of numbers! 💡 This exercise introduces fundamental Python concepts such as loops, arithmetic operations, and functions. By completing this challenge, you'll gain hands-on experience with numeric calculations and writing reusable code.



## 📋 Overview

An **Armstrong number** (also known as a narcissistic number, pluperfect digital invariant, or plus perfect number) is a number that is equal to the sum of its own digits each raised to the power of the number of digits. For example:

- **9** is an Armstrong number because:  
  \( 9 = 9^1 = 9 \)

- **153** is an Armstrong number because:  
  \( 153 = 1^3 + 5^3 + 3^3 = 1 + 125 + 27 = 153 \)

- **154** is **not** an Armstrong number because:  
  \( 154 \neq 1^3 + 5^3 + 4^3 = 1 + 125 + 64 = 190 \)

Your task is to write a Python function that determines whether a given number is an Armstrong number.


## 🚀 How to Use

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the Armstrong number function.
3. Run the script in your terminal or IDE.
4. Use the provided function to check if a number is an Armstrong number.


## 📚 Example Usage

Here’s how you can use the completed function in your Python environment:

```python
# Check if a number is an Armstrong number
>>> is_armstrong_number(9)
True
>>> is_armstrong_number(153)
True
>>> is_armstrong_number(154)
False
>>> is_armstrong_number(9474)
True
```



## 🔧 Function Documentation

The function comes with a detailed docstring to explain its purpose, parameters, and return values:

### `is_armstrong_number(number)`
```python
"""Determine whether a number is an Armstrong number.
:param number: int - the number to check.
:return: bool - True if the number is an Armstrong number, False otherwise.

This function calculates the sum of each digit raised to the power of the number of digits
and checks if it equals the original number.
"""
```



## 🎯 Learning Objectives

By completing this project, you will:

- Understand **loops** and **arithmetic operations** in Python.
- Learn how to manipulate and process individual digits of a number.
- Practice writing **functions** with parameters and return values.
- Gain experience with **docstrings** for documentation.
- Explore mathematical concepts like powers and digit manipulation.



## 💡 Why Solve This Problem?

- **Mathematical Insight**: Deepen your understanding of number theory and properties of numbers. 🧠  
- **Programming Practice**: Combine practical problem-solving with learning Python fundamentals. 🌟  
- **Fun Challenge**: Test your skills with an interesting and classic coding problem. 🎉  



## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more features (e.g., finding all Armstrong numbers within a range).
- Improving the documentation.
- Reporting bugs or suggesting enhancements.



Happy coding—and may your numbers always be Armstrong numbers! 🔢✨