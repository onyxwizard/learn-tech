# 💱 Currency Exchange Calculator 🌍

Welcome to the **Currency Exchange Calculator**, an Exercism challenge designed to help you practice Python programming while solving real-world problems like currency conversion! 💸 This exercise introduces fundamental Python concepts such as arithmetic operations, type conversions (`int` and `float`), functions, and docstrings. By completing this challenge, you'll gain hands-on experience with Python's versatility and precision in handling numeric calculations.


## 📋 Overview

This project focuses on building a tool to assist your friend Chandler (or any traveler) in calculating currency exchanges during their exotic trips around the world. ✈️ The calculator handles various aspects of currency exchange, including fees, denominations, and leftover amounts. It includes six essential functions:

1. **Estimate value after exchange** 🧾  
   Calculate how much foreign currency you'll receive based on your budget and the exchange rate.

2. **Calculate leftover money after an exchange** 💵  
   Determine how much money remains in your wallet after exchanging a portion of your budget.

3. **Calculate the total value of bills** 💳  
   Find out how much cash you'll receive in whole bills, excluding fractional amounts.

4. **Calculate the number of bills** 📊  
   Determine how many whole bills fit into a given amount of money.

5. **Calculate leftover money after receiving bills** 🪙  
   Compute the remainder that cannot be returned in whole bills.

6. **Calculate maximum exchangeable value with fees** 💳💸  
   Factor in exchange fees and denominations to determine the maximum usable currency you can get.



## 🚀 How to Use

### Prerequisites
- Python 3.x installed on your system.

### Steps to Run the Code
1. Clone or download the project files.
2. Open the Python file containing the currency exchange functions.
3. Run the script in your terminal or IDE.
4. Use the provided functions to perform currency calculations as per Chandler's specifications.



## 📚 Example Usage

Here’s how you can use the completed functions in your Python environment:

```python
# Estimate value after exchange
>>> exchange_money(127.5, 1.2)
106.25

# Calculate leftover money after an exchange
>>> get_change(127.5, 120)
7.5

# Calculate the total value of bills
>>> get_value_of_bills(5, 128)
640

# Calculate the number of bills
>>> get_number_of_bills(127.5, 5)
25

# Calculate leftover money after receiving bills
>>> get_leftover_of_bills(127.5, 20)
7.5

# Calculate maximum exchangeable value with fees
>>> exchangeable_value(127.25, 1.20, 10, 20)
80
>>> exchangeable_value(127.25, 1.20, 10, 5)
95
```



## 🔧 Functions Documentation

Each function comes with detailed docstrings to explain its purpose, parameters, and return values:

### `exchange_money(budget, exchange_rate)`
```python
"""Estimate value after exchange.
:param budget: float - amount of money to be exchanged.
:param exchange_rate: float - the unit value of foreign currency.
:return: float - exchanged value of the foreign currency you can receive.
"""
```

### `get_change(budget, exchanging_value)`
```python
"""Calculate currency left after an exchange.
:param budget: float - amount of money before exchange.
:param exchanging_value: float - amount of money taken from the budget.
:return: float - remaining money after the exchange.
"""
```

### `get_value_of_bills(denomination, number_of_bills)`
```python
"""Calculate the total value of bills.
:param denomination: int - the value of a single bill.
:param number_of_bills: int - total number of bills.
:return: int - total value of bills received.
"""
```

### `get_number_of_bills(amount, denomination)`
```python
"""Calculate the number of bills.
:param amount: float - total starting value.
:param denomination: int - the value of a single bill.
:return: int - number of bills that fit within the amount.
"""
```

### `get_leftover_of_bills(amount, denomination)`
```python
"""Calculate leftover after exchanging into bills.
:param amount: float - total starting value.
:param denomination: int - the value of a single bill.
:return: float - leftover amount after exchanging into bills.
"""
```

### `exchangeable_value(budget, exchange_rate, spread, denomination)`
```python
"""Calculate value after accounting for exchange rate, spread, and denomination.
:param budget: float - the amount of money to be exchanged.
:param exchange_rate: float - the unit value of foreign currency.
:param spread: int - percentage fee added to the exchange.
:param denomination: int - the value of a single bill.
:return: int - maximum value of the new currency after fees and denomination constraints.
"""
```

## 🎯 Learning Objectives

By completing this project, you will:

- Understand **basic Python arithmetic operations** with `int` and `float`.
- Learn how to **convert between types** using `int()` and `float()`.
- Practice writing **functions** with parameters and return values.
- Gain experience with **docstrings** for documentation.
- Explore real-world applications of programming like **currency conversion**.


## 💡 Why Use This Tool?

- **Avoid Scams**: Ensure you're not being overcharged during currency exchanges. 🛡️  
- **Plan Your Budget**: Know exactly how much money you'll have after exchanging currencies. 📊  
- **Learn Python**: Combine practical problem-solving with learning a new programming language. 🌟  



## 🤝 Contributions

Feel free to contribute to this project by:
- Adding more features (e.g., support for multiple currencies).
- Improving the documentation.
- Reporting bugs or suggesting enhancements.



Happy coding—and safe travels! ✈️🌍✨