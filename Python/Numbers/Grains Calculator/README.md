# 🌾 Chessboard Grains Calculator 🎯

Welcome to the **Chessboard Grains Calculator**, an Exercism challenge designed to help you practice Python programming while solving a classic mathematical problem! This exercise introduces fundamental Python concepts such as arithmetic operations, exception handling (`raise` and `ValueError`), and writing reusable functions. By completing this challenge, you'll gain hands-on experience with exponential calculations and error management.



## 📋 Overview

This project is inspired by the story of a wise servant who asked for grains of wheat on a chessboard:  
- **Square 1** has **1 grain** of wheat.
- **Square 2** has **2 grains** (double the previous square).
- **Square 3** has **4 grains** (double again), and so on, doubling each time up to **64 squares**.

Your task is to write code that calculates:
1. The number of grains on a specific square of the chessboard.
2. The total number of grains across all squares of the chessboard.

Additionally, you'll need to handle invalid inputs by raising a `ValueError` with a meaningful error message.



## 🚀 How to Use

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the chessboard grains functions.
3. Run the script in your terminal or IDE.
4. Use the provided functions to calculate grains on a specific square or the total grains on the chessboard.



## 📚 Example Usage

Here’s how you can use the completed functions in your Python environment:

```python
# Calculate grains on a specific square
>>> grains_on_square(1)
1
>>> grains_on_square(5)
16

# Calculate total grains on the chessboard
>>> total_grains()
18446744073709551615

# Handle invalid input
>>> grains_on_square(0)
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
ValueError: square must be between 1 and 64
```



## 🔧 Functions Documentation

Each function comes with detailed docstrings to explain its purpose, parameters, and return values:

### `grains_on_square(square)`
```python
"""Calculate the number of grains on a given square of the chessboard.
:param square: int - the square number (1 to 64).
:return: int - the number of grains on the specified square.
:raises ValueError: if the square is not between 1 and 64.
"""
```

### `total_grains()`
```python
"""Calculate the total number of grains on the chessboard.
:return: int - the total number of grains across all 64 squares.
"""
```



## 🎯 Learning Objectives

By completing this project, you will:

- Understand **exponential growth** through doubling calculations.
- Learn how to use **exception handling** to manage invalid inputs.
- Practice writing **functions** with parameters, return values, and error messages.
- Gain experience with **docstrings** for documentation.
- Explore real-world applications of programming like **mathematical modeling**.

## 💡 Why Solve This Problem?

- **Mathematical Insight**: Visualize exponential growth in a tangible way. 📈  
- **Error Handling**: Learn to gracefully handle invalid inputs using exceptions. ⚠️  
- **Programming Practice**: Combine practical problem-solving with learning Python fundamentals. 🌟  



## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more features (e.g., calculating grains for custom-sized boards).
- Improving the documentation.
- Reporting bugs or suggesting enhancements.



Happy coding—and may your chessboard always be full of grains! 🌾✨