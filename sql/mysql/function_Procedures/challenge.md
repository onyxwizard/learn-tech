# 🧠 **CHAPTER 11 CHALLENGE: FUNCTIONS & PROCEDURES MASTER TEST**

## 📚 **SETUP: Coffee Shop Database**
First, let's create our test database:

```sql
-- Create database and tables
CREATE DATABASE coffee_shop_test;
USE coffee_shop_test;

-- Products table
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    unit_price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    reorder_level INT DEFAULT 10
);

-- Customers table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    loyalty_points INT DEFAULT 0,
    join_date DATE DEFAULT (CURDATE())
);

-- Orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    order_date DATETIME DEFAULT NOW(),
    total_amount DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'pending',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Order details table
CREATE TABLE order_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2),
    line_total DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- Insert sample data
INSERT INTO products (product_name, category, unit_price, stock_quantity, reorder_level) VALUES
('Espresso', 'Coffee', 2.50, 100, 20),
('Cappuccino', 'Coffee', 3.75, 50, 15),
('Latte', 'Coffee', 4.00, 75, 20),
('Blueberry Muffin', 'Pastry', 2.25, 30, 10),
('Croissant', 'Pastry', 2.75, 25, 8),
('Chocolate Chip Cookie', 'Pastry', 1.50, 40, 15);

INSERT INTO customers (first_name, last_name, email, loyalty_points, join_date) VALUES
('John', 'Smith', 'john@email.com', 150, '2023-01-15'),
('Sarah', 'Johnson', 'sarah@email.com', 75, '2023-03-10'),
('Mike', 'Williams', 'mike@email.com', 0, '2024-02-01'),
('Lisa', 'Brown', 'lisa@email.com', 200, '2022-11-05');

INSERT INTO orders (customer_id, order_date, total_amount, status) VALUES
(1, '2024-01-15 08:30:00', 10.75, 'completed'),
(2, '2024-01-15 09:15:00', 7.50, 'completed'),
(1, '2024-01-16 10:00:00', 15.25, 'completed'),
(3, '2024-01-16 14:30:00', 4.00, 'pending');

INSERT INTO order_details (order_id, product_id, quantity, unit_price, line_total) VALUES
(1, 1, 2, 2.50, 5.00),  -- 2 Espressos
(1, 6, 2, 1.50, 3.00),  -- 2 Cookies
(1, 4, 1, 2.25, 2.25),  -- 1 Muffin
(2, 2, 2, 3.75, 7.50),  -- 2 Cappuccinos
(3, 3, 3, 4.00, 12.00), -- 3 Lattes
(3, 5, 1, 2.75, 2.75),  -- 1 Croissant
(4, 3, 1, 4.00, 4.00);  -- 1 Latte
```

---

## 🎯 **PART 1: FUNCTIONS CHALLENGE**

### **Question 1: Calculate Discount Function**
Create a function called `calculate_discount` that:
- Takes `original_price` (DECIMAL) and `discount_percent` (INT) as parameters
- Returns the discounted price (DECIMAL)
- **Bonus**: Apply a 5% additional discount if the original price is over $20

```sql
-- Write your function here
```

### **Question 2: Loyalty Tier Function**
Create a function called `get_loyalty_tier` that:
- Takes `loyalty_points` (INT) as parameter
- Returns VARCHAR(20) with:
  - "Gold" for 200+ points
  - "Silver" for 100-199 points
  - "Bronze" for 1-99 points
  - "New" for 0 points

```sql
-- Write your function here
```

### **Question 3: Stock Status Function**
Create a function called `get_stock_status` that:
- Takes `current_stock` and `reorder_level` as parameters
- Returns VARCHAR(20):
  - "In Stock" if current_stock > reorder_level
  - "Low Stock" if current_stock > 0 AND current_stock <= reorder_level
  - "Out of Stock" if current_stock = 0
  - "Negative Stock" if current_stock < 0

```sql
-- Write your function here
```

### **Question 4: Tax Calculation Function**
Create a function called `calculate_tax` that:
- Takes `subtotal` (DECIMAL) and `state_code` (CHAR(2)) as parameters
- Returns the tax amount based on:
  - "CA": 7.25%
  - "NY": 8.875%
  - "TX": 6.25%
  - Default: 5%
- Round to 2 decimal places

```sql
-- Write your function here
```

---

## ⚙️ **PART 2: PROCEDURES CHALLENGE**

### **Question 5: Place Order Procedure**
Create a procedure called `place_order` that:
- Takes `customer_id`, `product_id`, and `quantity` as IN parameters
- Checks if product has enough stock
- If yes:
  - Deducts stock
  - Creates order record
  - Returns SUCCESS message and new order_id
- If no:
  - Returns ERROR message

```sql
-- Write your procedure here
```

### **Question 6: Update Loyalty Points Procedure**
Create a procedure called `update_loyalty_points` that:
- Takes `customer_id` and `purchase_amount` as IN parameters
- Calculates points: 1 point per $10 spent
- Updates customer's loyalty_points
- Returns old_points and new_points as OUT parameters
- **Bonus**: Double points for Gold members

```sql
-- Write your procedure here
```

### **Question 7: Generate Daily Report Procedure**
Create a procedure called `generate_daily_report` that:
- Takes `report_date` (DATE) as IN parameter
- Returns multiple result sets for:
  1. Total sales for the day
  2. Top 3 selling products
  3. Customers who made purchases

```sql
-- Write your procedure here
```

### **Question 8: Restock Products Procedure with Cursor**
Create a procedure called `restock_low_inventory` that:
- Uses a CURSOR to loop through products with stock <= reorder_level
- For each product, increases stock by 50 units
- Logs each update in a temporary table
- Returns summary of restocked products

```sql
-- Write your procedure here
```

---

## 🔄 **PART 3: ADVANCED CHALLENGE**

### **Question 9: Function Calling Another Function**
Create a function called `calculate_total_with_tax` that:
- Takes `subtotal`, `state_code`, and `discount_percent` as parameters
- Calls your `calculate_discount` and `calculate_tax` functions
- Returns final total: subtotal - discount + tax

```sql
-- Write your function here
```

### **Question 10: Procedure with Transaction & Error Handling**
Create a procedure called `process_order_transaction` that:
- Uses START TRANSACTION, COMMIT, and ROLLBACK
- Takes multiple product orders (you can design the parameters)
- Updates inventory for each product
- Creates order record
- Updates loyalty points
- Rolls back if any step fails

```sql
-- Write your procedure here
```

---

## 📝 **PART 4: DEBUGGING CHALLENGE**

### **Question 11: Fix the Broken Function**
Here's a broken function. Fix all the errors:

```sql
DELIMITER $$

CREATE FUNCTION calculate_profit(sales_price DECIMAL cost DECIMAL, quantity INT)
RETURNS INT
BEGIN
    DECLARE total_cost = cost * quantity;
    DECLARE total_sales = sales_price * quantity;
    DECLARE profit DECIMAL;
    
    profit = total_sales - total_cost;
    
    IF profit > 1000 THEN
        RETURN 'High Profit'
    ELSE
        RETURN 'Low Profit'
    END
END$$

DELIMITER ;
```

### **Question 12: Fix the Broken Procedure**
Here's a broken procedure. Fix all the errors:

```sql
DELIMITER $$

CREATE PROCEDURE update_product_price(product_id INT, new_price DECIMAL)
BEGIN
    SELECT @old_price := unit_price FROM products WHERE product_id = product_id;
    
    IF new_price < 0 THEN
        PRINT 'Price cannot be negative';
    ELSE
        UPDATE products SET unit_price = new_price WHERE product_id = product_id;
        RETURN CONCAT('Price updated from ', @old_price, ' to ', new_price);
    END
END$$

DELIMITER ;
```

---

## 🧪 **PART 5: TEST YOUR UNDERSTANDING**

### **Multiple Choice Questions:**

1. **What is the main difference between a function and a procedure?**
   A) Functions return values, procedures don't
   B) Procedures return values, functions don't
   C) Functions can be called in SELECT, procedures can't
   D) A and C

2. **Which keyword is used to declare a variable in MySQL?**
   A) VAR
   B) DECLARE
   C) SET
   D) LET

3. **What does DETERMINISTIC mean in function creation?**
   A) Function always returns same result for same input
   B) Function modifies database data
   C) Function is optimized for speed
   D) Function cannot be used in queries

4. **How do you call a procedure with OUT parameters?**
   A) SELECT procedure_name(parameters);
   B) EXEC procedure_name(parameters);
   C) CALL procedure_name(@out_var);
   D) RUN procedure_name(parameters);

5. **What is a cursor used for?**
   A) Storing query results
   B) Looping through result sets row by row
   C) Speeding up queries
   D) Creating temporary tables

---

## 📊 **SCORING SYSTEM**

- **Questions 1-4 (Functions)**: 10 points each (40 total)
- **Questions 5-8 (Procedures)**: 15 points each (60 total)
- **Questions 9-10 (Advanced)**: 20 points each (40 total)
- **Questions 11-12 (Debugging)**: 15 points each (30 total)
- **Multiple Choice**: 5 points each (25 total)
- **Total Possible**: 195 points

**Grading:**
- 175+ = SQL Wizard 🧙‍♂️
- 150-174 = Advanced Developer 🚀
- 120-149 = Intermediate 💪
- 90-119 = Getting There 📚
- <90 = Keep Practicing! 🔄

---

## 🎯 **HOW TO SUBMIT**

Write your solutions in this format:

```sql
-- Question 1 Solution
DELIMITER $$

CREATE FUNCTION calculate_discount(...)
...

-- Question 2 Solution
DELIMITER $$

CREATE FUNCTION get_loyalty_tier(...)
...

-- [Continue for all questions]
```

**Ready when you are! Show me your SQL skills!** 💻