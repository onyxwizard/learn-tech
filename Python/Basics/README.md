# 🍝 Lasagna Cooking Assistant 🍳

Welcome to the **Lasagna Cooking Assistant**, an Exercism challenge designed to help you practice Python programming while cooking up a delicious lasagna! 🧑‍🍳👩‍🍳 This exercise will guide you through writing functions and working with constants, docstrings, and basic Python operations.



## 📋 Exercise Overview

In this Exercism challenge, you'll be implementing a set of functions to assist in preparing your favorite lasagna recipe. By completing these tasks, you'll learn essential Python concepts such as:

- Name assignment (variables & constants)
- Writing and calling functions
- Using docstrings for documentation
- Performing simple calculations

Let's get started! 🚀



## 🔧 Tasks

### 1. Define Expected Bake Time in Minutes as a Constant ⏲️

Define the constant `EXPECTED_BAKE_TIME` which represents how long the lasagna should bake in the oven. According to the recipe, the lasagna should bake for **40 minutes**.

```python
EXPECTED_BAKE_TIME = 40
```

**Example:**
```python
>>> print(EXPECTED_BAKE_TIME)
40
```



### 2. Calculate Remaining Bake Time in Minutes ⏳

Complete the function `bake_time_remaining()` that calculates how many minutes the lasagna still needs to bake based on the actual time it has already been in the oven.

**Function Signature:**
```python
def bake_time_remaining(elapsed_bake_time):
    """Calculate the remaining bake time.
    :param elapsed_bake_time: int - the number of minutes the lasagna has been baking.
    :return: int - remaining bake time in minutes.
    """
```

**Example:**
```python
>>> bake_time_remaining(30)
10
```



### 3. Calculate Preparation Time in Minutes 🕒

Define the function `preparation_time_in_minutes()` that takes the number of layers you want to add to the lasagna and returns how many minutes you would spend preparing them. Assume each layer takes **2 minutes** to prepare.

**Function Signature:**
```python
def preparation_time_in_minutes(number_of_layers):
    """Calculate the preparation time.
    :param number_of_layers: int - the number of layers in the lasagna.
    :return: int - total preparation time in minutes.
    """
```

**Example:**
```python
>>> preparation_time_in_minutes(2)
4
```


### 4. Calculate Total Elapsed Cooking Time (Prep + Bake) in Minutes ⌛

Define the function `elapsed_time_in_minutes()` that calculates the total elapsed cooking time by summing up the preparation time and the time the lasagna has already spent baking.

**Function Signature:**
```python
def elapsed_time_in_minutes(number_of_layers, elapsed_bake_time):
    """Calculate the elapsed cooking time.
    :param number_of_layers: int - the number of layers in the lasagna.
    :param elapsed_bake_time: int - elapsed cooking time.
    :return: int - total time elapsed (in minutes) preparing and cooking.
    """
```

**Example:**
```python
>>> elapsed_time_in_minutes(3, 20)
26
```


### 5. Update the Recipe with Notes 📝

Add detailed "notes" to each function in the form of **docstrings**. These docstrings will explain the purpose of each function, its parameters, and its return value.

**Example Docstring:**
```python
def elapsed_time_in_minutes(number_of_layers, elapsed_bake_time):
    """Calculate the elapsed cooking time.
    :param number_of_layers: int - the number of layers in the lasagna.
    :param elapsed_bake_time: int - elapsed cooking time.
    :return: int - total time elapsed (in minutes) preparing and cooking.
    This function takes two integers representing the number of lasagna layers and the
    time already spent baking and calculates the total elapsed minutes spent cooking the
    lasagna.
    """
```


## 🎯 Learning Objectives

By completing this Exercism challenge, you will:

- Understand how to define and use **constants** and **variables** in Python.
- Learn to write and call **functions** with parameters and return values.
- Practice documenting your code using **docstrings**.
- Perform simple arithmetic operations and manage control flow.



## 📚 Example Usage

Here’s how you can use the completed functions in your Python environment:

```python
# Define constants
EXPECTED_BAKE_TIME = 40  # Total bake time in minutes

# Calculate remaining bake time
remaining_time = bake_time_remaining(30)
print(f"Remaining bake time: {remaining_time} minutes")  # Output: 10

# Calculate preparation time
prep_time = preparation_time_in_minutes(2)
print(f"Preparation time: {prep_time} minutes")  # Output: 4

# Calculate total elapsed cooking time
total_time = elapsed_time_in_minutes(3, 20)
print(f"Total elapsed cooking time: {total_time} minutes")  # Output: 26
```



## 🙌 Why Participate in This Challenge?

- **Learn Python Basics**: Gain hands-on experience with variables, functions, and docstrings.
- **Cooking Fun**: Combine coding with cooking—what could be better? 🍅🧀  
- **Exercism Community**: Get feedback from mentors and improve your skills alongside other learners.


## 🤝 Contributions

Feel free to explore further improvements:
- Add more functionality, such as ingredient calculations.
- Refactor the code for better readability or performance.
- Write additional tests to ensure robustness.

Happy coding—and bon appétit! 🍽️✨