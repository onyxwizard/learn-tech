# 🕹️ Pac-Man Rules Implementation 🎮

Welcome to the **Pac-Man Rules Implementation** exercise! In this challenge, you'll use Python's `bool` type and logical operators (`and`, `or`, `not`) to implement game rules from the classic arcade game Pac-Man. This exercise will help you practice Boolean logic and writing clean, reusable functions.


## 📋 Overview

In this exercise, you'll implement four functions that define key game states in Pac-Man:

1. **Can Pac-Man eat a ghost?** 👻  
   Determine if Pac-Man can eat a ghost based on whether he has a power pellet active and is touching a ghost.

2. **Did Pac-Man score?** 🎉  
   Check if Pac-Man scores by touching a power pellet or a dot.

3. **Did Pac-Man lose?** 💀  
   Determine if Pac-Man loses by touching a ghost without an active power pellet.

4. **Did Pac-Man win?** 🏆  
   Check if Pac-Man wins by eating all the dots and avoiding losing conditions.

Each function will return a Boolean value (`True` or `False`) based on the given conditions.


## 🚀 How to Get Started

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the Pac-Man functions.
3. Implement the functions as described in the instructions below.
4. Test your code using the provided examples or additional test cases.
5. Refactor and optimize your code for clarity and efficiency.


## 🎯 Instructions

### 1. Define if Pac-Man Eats a Ghost 👻

Write the `eat_ghost()` function that takes two parameters:
- `power_pellet_active`: A Boolean indicating if Pac-Man has an active power pellet.
- `touching_ghost`: A Boolean indicating if Pac-Man is touching a ghost.

The function should return `True` only if Pac-Man has a power pellet active **and** is touching a ghost.

**Example:**
```python
>>> eat_ghost(False, True)
False
```



### 2. Define if Pac-Man Scores 🎉

Write the `score()` function that takes two parameters:
- `touching_power_pellet`: A Boolean indicating if Pac-Man is touching a power pellet.
- `touching_dot`: A Boolean indicating if Pac-Man is touching a dot.

The function should return `True` if Pac-Man is touching a power pellet **or** a dot.

**Example:**
```python
>>> score(True, True)
True
```


### 3. Define if Pac-Man Loses 💀

Write the `lose()` function that takes two parameters:
- `power_pellet_active`: A Boolean indicating if Pac-Man has an active power pellet.
- `touching_ghost`: A Boolean indicating if Pac-Man is touching a ghost.

The function should return `True` if Pac-Man is touching a ghost **and** does not have a power pellet active.

**Example:**
```python
>>> lose(False, True)
True
```



### 4. Define if Pac-Man Wins 🏆

Write the `win()` function that takes three parameters:
- `has_eaten_all_dots`: A Boolean indicating if Pac-Man has eaten all the dots.
- `power_pellet_active`: A Boolean indicating if Pac-Man has an active power pellet.
- `touching_ghost`: A Boolean indicating if Pac-Man is touching a ghost.

The function should return `True` if Pac-Man has eaten all the dots **and** has not lost (based on the conditions defined in part 3).

**Example:**
```python
>>> win(False, True, False)
False
```



## 🔧 Function Documentation

Each function comes with a detailed docstring to explain its purpose, parameters, and return values:

### `eat_ghost(power_pellet_active, touching_ghost)`
```python
"""Determine if Pac-Man can eat a ghost.
:param power_pellet_active: bool - whether a power pellet is active.
:param touching_ghost: bool - whether Pac-Man is touching a ghost.
:return: bool - True if Pac-Man can eat a ghost, False otherwise.
"""
```

### `score(touching_power_pellet, touching_dot)`
```python
"""Determine if Pac-Man scores.
:param touching_power_pellet: bool - whether Pac-Man is touching a power pellet.
:param touching_dot: bool - whether Pac-Man is touching a dot.
:return: bool - True if Pac-Man scores, False otherwise.
"""
```

### `lose(power_pellet_active, touching_ghost)`
```python
"""Determine if Pac-Man loses.
:param power_pellet_active: bool - whether a power pellet is active.
:param touching_ghost: bool - whether Pac-Man is touching a ghost.
:return: bool - True if Pac-Man loses, False otherwise.
"""
```

### `win(has_eaten_all_dots, power_pellet_active, touching_ghost)`
```python
"""Determine if Pac-Man wins.
:param has_eaten_all_dots: bool - whether Pac-Man has eaten all the dots.
:param power_pellet_active: bool - whether a power pellet is active.
:param touching_ghost: bool - whether Pac-Man is touching a ghost.
:return: bool - True if Pac-Man wins, False otherwise.
"""
```



## 📚 Example Usage

Here’s how you can use the completed functions in your Python environment:

```python
# Can Pac-Man eat a ghost?
>>> eat_ghost(True, True)
True
>>> eat_ghost(False, True)
False

# Did Pac-Man score?
>>> score(True, False)
True
>>> score(False, False)
False

# Did Pac-Man lose?
>>> lose(False, True)
True
>>> lose(True, True)
False

# Did Pac-Man win?
>>> win(True, True, False)
True
>>> win(False, True, False)
False
```



## 🎯 Learning Objectives

By completing this project, you will:

- Understand **Boolean logic** and how to combine conditions using `and`, `or`, and `not`.
- Practice writing **functions** with parameters and return values.
- Gain experience with **docstrings** for documenting your code.
- Explore real-world applications of programming like game logic.



## 💡 Why Solve This Problem?

- **Game Logic**: Learn how Boolean logic powers decision-making in games. 🎮  
- **Programming Practice**: Combine practical problem-solving with learning Python fundamentals. 🌟  
- **Fun Challenge**: Relive the nostalgia of Pac-Man while sharpening your coding skills. 🕹️  



## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more game rules or features.
- Improving the documentation.
- Reporting bugs or suggesting enhancements.



Happy coding—and may Pac-Man always win! 🏆👻🎉