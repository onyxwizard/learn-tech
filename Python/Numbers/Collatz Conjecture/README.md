# 🌟 Collatz Conjecture Solver 🧮

Welcome to the **Collatz Conjecture Solver**, an Exercism challenge designed to help you practice Python programming while exploring one of mathematics' most intriguing unsolved puzzles! 🔍 This exercise introduces fundamental Python concepts such as loops, conditionals, exception handling (`raise` and `ValueError`), and writing reusable functions. By completing this challenge, you'll gain hands-on experience with numeric calculations and error management.


## 📋 Overview

The **Collatz Conjecture** is a fascinating mathematical puzzle that has baffled thinkers for decades. The rules are simple:

1. Start with any positive integer.
2. If the number is **even**, divide it by 2.
3. If the number is **odd**, multiply it by 3 and add 1.
4. Repeat the process with the new result.

No matter the starting number, the conjecture claims that you will always eventually reach **1**. Your task is to write a Python function that calculates how many steps it takes for a given number to reach 1 according to these rules.

For example:
- Starting with **12**:  
  \( 12 \to 6 \to 3 \to 10 \to 5 \to 16 \to 8 \to 4 \to 2 \to 1 \)  
  It takes **9 steps** to reach 1.

However, if the input is zero or a negative integer, your function should raise a `ValueError` with a meaningful error message.



## 🚀 How to Use

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the Collatz Conjecture function.
3. Run the script in your terminal or IDE.
4. Use the provided function to calculate the number of steps required to reach 1 for any positive integer.



## 📚 Example Usage

Here’s how you can use the completed function in your Python environment:

```python
# Calculate the number of steps for a positive integer
>>> collatz_steps(12)
9
>>> collatz_steps(19)
20
>>> collatz_steps(1)
0

# Handle invalid input (zero or negative integer)
>>> collatz_steps(-5)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
ValueError: Only positive integers are allowed
```



## 🔧 Function Documentation

The function comes with a detailed docstring to explain its purpose, parameters, return values, and exceptions:

### `collatz_steps(number)`
```python
"""Calculate the number of steps to reach 1 using the Collatz Conjecture.
:param number: int - the starting positive integer.
:return: int - the number of steps required to reach 1.

This function implements the Collatz Conjecture algorithm:
    - If the number is even, divide it by 2.
    - If the number is odd, multiply it by 3 and add 1.
    - Repeat until the number reaches 1.

:raises ValueError: if the input is zero or a negative integer.
"""
```



## 🎯 Learning Objectives

By completing this project, you will:

- Understand **loops** and **conditionals** in Python.
- Learn how to handle **exceptions** using `raise` and `ValueError`.
- Practice writing **functions** with parameters, return values, and error messages.
- Gain experience with **docstrings** for documentation.
- Explore mathematical concepts like sequences and iterative processes.



## 💡 Why Solve This Problem?

- **Mathematical Insight**: Dive into the mystery of the Collatz Conjecture and explore its unpredictable yet consistent behavior. 🧠  
- **Programming Practice**: Combine practical problem-solving with learning Python fundamentals. 🌟  
- **Fun Challenge**: Test your skills with a classic coding problem that has intrigued mathematicians for decades. 🎉  



## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more features (e.g., visualizing the sequence or finding patterns).
- Improving the documentation.
- Reporting bugs or suggesting enhancements.


Happy coding—and may your numbers always find their way to 1! 🔢✨