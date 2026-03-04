# 📚 **CHAPTER 11: CREATING FUNCTIONS AND PROCEDURES**

### **1. FUNCTIONS vs. PROCEDURES - The Key Difference**

**Function:**
- **Returns a single value** (like a result)
- Can be used in SQL statements (SELECT, WHERE, etc.)
- Similar to built-in functions like `UPPER()`, `ROUND()`
- **Example**: Calculate total price with tax

**Procedure:**
- **Performs actions** (like a script)
- Can return multiple values or no values
- Called with `CALL` statement
- Can have `IN`, `OUT`, and `INOUT` parameters
- **Example**: Update inventory and log the change

---

## 🧮 **PART 1: CREATING FUNCTIONS**

### **Example 1: Simple Function - Calculate Discount**
```sql
DELIMITER $$

CREATE FUNCTION calculate_discount(original_price DECIMAL(10,2), discount_percent INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE discounted_price DECIMAL(10,2);
    SET discounted_price = original_price * (1 - discount_percent/100);
    RETURN discounted_price;
END$$

DELIMITER ;
```

**Usage:**
```sql
SELECT 
    product_name,
    price,
    calculate_discount(price, 15) AS discounted_price
FROM products;
```

---

### **Example 2: Function with Conditional Logic**
```sql
DELIMITER $$

CREATE FUNCTION customer_status(total_purchases DECIMAL(10,2))
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE status VARCHAR(20);
    
    IF total_purchases > 1000 THEN
        SET status = 'Gold';
    ELSEIF total_purchases > 500 THEN
        SET status = 'Silver';
    ELSE
        SET status = 'Bronze';
    END IF;
    
    RETURN status;
END$$

DELIMITER ;
```

**Usage:**
```sql
SELECT 
    customer_name,
    total_spent,
    customer_status(total_spent) AS status
FROM customers;
```

---

## ⚙️ **PART 2: CREATING PROCEDURES**

### **Example 3: Simple Procedure - Update Inventory**
```sql
DELIMITER $$

CREATE PROCEDURE update_inventory(
    IN product_id INT,
    IN quantity_change INT
)
BEGIN
    UPDATE products 
    SET stock_quantity = stock_quantity + quantity_change
    WHERE id = product_id;
    
    SELECT CONCAT('Inventory updated for product ', product_id) AS message;
END$$

DELIMITER ;
```

**Usage:**
```sql
CALL update_inventory(101, -5);  -- Reduce stock by 5
```

---

### **Example 4: Procedure with OUT Parameter**
```sql
DELIMITER $$

CREATE PROCEDURE get_order_summary(
    IN order_id INT,
    OUT total_items INT,
    OUT total_amount DECIMAL(10,2)
)
BEGIN
    -- Get total items in order
    SELECT COUNT(*), SUM(quantity * price)
    INTO total_items, total_amount
    FROM order_items
    WHERE order_id = order_id;
END$$

DELIMITER ;
```

**Usage:**
```sql
-- Declare variables to hold output
SET @items = 0;
SET @amount = 0.00;

-- Call procedure
CALL get_order_summary(123, @items, @amount);

-- View results
SELECT @items AS total_items, @amount AS total_amount;
```

---

## 🔄 **PART 3: ADVANCED CONCEPTS**

### **Example 5: Function with Loop**
```sql
DELIMITER $$

CREATE FUNCTION factorial(n INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE result INT DEFAULT 1;
    DECLARE counter INT DEFAULT 1;
    
    WHILE counter <= n DO
        SET result = result * counter;
        SET counter = counter + 1;
    END WHILE;
    
    RETURN result;
END$$

DELIMITER ;
```

**Usage:**
```sql
SELECT factorial(5);  -- Returns 120 (5*4*3*2*1)
```

---

### **Example 6: Procedure with Cursor (Looping through rows)**
```sql
DELIMITER $$

CREATE PROCEDURE apply_discount_to_category(
    IN category_name VARCHAR(50),
    IN discount_percent INT
)
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE product_id INT;
    DECLARE current_price DECIMAL(10,2);
    DECLARE cur CURSOR FOR 
        SELECT id, price FROM products WHERE category = category_name;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO product_id, current_price;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- Apply discount
        UPDATE products 
        SET price = current_price * (1 - discount_percent/100)
        WHERE id = product_id;
        
    END LOOP;
    
    CLOSE cur;
    
    SELECT CONCAT('Discount applied to ', category_name, ' category') AS result;
END$$

DELIMITER ;
```

**Usage:**
```sql
CALL apply_discount_to_category('Electronics', 10);
```

---

## 📋 **PART 4: REAL-WORLIBRARY MANAGEMENT EXAMPLES**

Let's create practical examples for our library system:

### **Example 7: Function to Calculate Late Fees**
```sql
DELIMITER $$

CREATE FUNCTION calculate_late_fee(
    borrow_date DATE,
    due_date DATE,
    return_date DATE,
    daily_fee DECIMAL(5,2)
)
RETURNS DECIMAL(5,2)
DETERMINISTIC
BEGIN
    DECLARE days_late INT;
    DECLARE fee DECIMAL(5,2);
    
    -- Calculate days late (only if returned after due date)
    IF return_date IS NULL OR return_date <= due_date THEN
        RETURN 0.00;
    END IF;
    
    SET days_late = DATEDIFF(return_date, due_date);
    SET fee = days_late * daily_fee;
    
    -- Cap fee at $20.00
    IF fee > 20.00 THEN
        SET fee = 20.00;
    END IF;
    
    RETURN fee;
END$$

DELIMITER ;
```

**Usage:**
```sql
SELECT 
    book_title,
    borrow_date,
    due_date,
    return_date,
    calculate_late_fee(borrow_date, due_date, return_date, 0.50) AS late_fee
FROM borrowings;
```

---

### **Example 8: Procedure to Borrow a Book**
```sql
DELIMITER $$

CREATE PROCEDURE borrow_book(
    IN p_member_id INT,
    IN p_book_id INT,
    IN p_loan_days INT,
    OUT p_borrow_id INT,
    OUT p_message VARCHAR(100)
)
BEGIN
    DECLARE available_copies INT;
    DECLARE member_status VARCHAR(20);
    
    -- Check book availability
    SELECT available_copies INTO available_copies
    FROM books WHERE book_id = p_book_id;
    
    -- Check member status
    SELECT is_active INTO member_status
    FROM members WHERE member_id = p_member_id;
    
    IF available_copies > 0 AND member_status = 1 THEN
        -- Insert borrowing record
        INSERT INTO borrowings (book_id, member_id, borrow_date, due_date)
        VALUES (p_book_id, p_member_id, CURDATE(), DATE_ADD(CURDATE(), INTERVAL p_loan_days DAY));
        
        -- Update available copies
        UPDATE books 
        SET available_copies = available_copies - 1
        WHERE book_id = p_book_id;
        
        -- Set output parameters
        SET p_borrow_id = LAST_INSERT_ID();
        SET p_message = 'Book borrowed successfully';
    ELSEIF available_copies <= 0 THEN
        SET p_borrow_id = NULL;
        SET p_message = 'Book not available';
    ELSE
        SET p_borrow_id = NULL;
        SET p_message = 'Member account is not active';
    END IF;
END$$

DELIMITER ;
```

**Usage:**
```sql
-- Call the procedure
CALL borrow_book(1, 3, 21, @borrow_id, @message);

-- View results
SELECT @borrow_id, @message;
```

---

## 🧪 **PRACTICAL EXERCISES FOR YOU**

### **Exercise 1: Create a Simple Function**
Create a function called `get_book_age` that takes a `published_year` and returns:
- "Classic" if published before 1950
- "Modern" if published between 1950 and 2000
- "Contemporary" if published after 2000

### **Exercise 2: Create a Procedure with Error Handling**
Create a procedure called `return_book` that:
1. Takes `borrow_id` as input
2. Sets the `return_date` to current date
3. Updates the book's `available_copies`
4. Calculates and returns any late fee

### **Exercise 3: Function with Validation**
Create a function called `validate_email` that:
- Returns TRUE if email contains '@' and '.'
- Returns FALSE otherwise
- Handles NULL values gracefully

---

## 🔧 **KEY SYNTAX NOTES**

### **Changing the Delimiter**
```sql
DELIMITER $$  -- Changes delimiter from ; to $$
-- Your function/procedure code here
DELIMITER ;   -- Changes back to ;
```

### **Function Characteristics**
```sql
CREATE FUNCTION function_name(parameters)
RETURNS data_type
[DETERMINISTIC | NOT DETERMINISTIC]
[SQL DATA ACCESS {CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA}]
BEGIN
    -- SQL statements
    RETURN value;
END
```

### **Procedure Parameters**
- `IN` - Input parameter (default)
- `OUT` - Output parameter
- `INOUT` - Both input and output

### **Common Control Structures**
```sql
-- IF statement
IF condition THEN
    -- statements
ELSEIF condition THEN
    -- statements
ELSE
    -- statements
END IF;

-- WHILE loop
WHILE condition DO
    -- statements
END WHILE;

-- CURSOR for row-by-row processing
DECLARE cur CURSOR FOR SELECT ...;
OPEN cur;
FETCH cur INTO variables;
CLOSE cur;
```

---

## 📊 **MANAGING STORED ROUTINES**

### **View All Functions/Procedures**
```sql
-- Show all functions
SHOW FUNCTION STATUS WHERE Db = 'your_database';

-- Show all procedures
SHOW PROCEDURE STATUS WHERE Db = 'your_database';

-- View function/procedure definition
SHOW CREATE FUNCTION function_name;
SHOW CREATE PROCEDURE procedure_name;
```

### **Drop Functions/Procedures**
```sql
DROP FUNCTION IF EXISTS function_name;
DROP PROCEDURE IF EXISTS procedure_name;
```

---

## 🚨 **COMMON PITFALLS & BEST PRACTICES**

1. **Always use `DELIMITER`** when creating functions/procedures
2. **Use meaningful parameter names** (prefix with p_ or in_ for clarity)
3. **Include error handling** with `DECLARE CONTINUE HANDLER`
4. **Set function characteristics** (DETERMINISTIC/NOT DETERMINISTIC)
5. **Test thoroughly** with various inputs
6. **Document your code** with comments

---

## 🎯 **QUICK TEST YOUR UNDERSTANDING**

1. **True or False**: A function can return multiple values.
2. **True or False**: A procedure can be used in a SELECT statement.
3. **What's the difference between `IN` and `OUT` parameters?**
4. **When would you use a cursor in a procedure?**

**Answers:**
1. **False** - Functions return exactly one value
2. **False** - Procedures are called with `CALL`, not used in SELECT
3. `IN` is for input, `OUT` is for output values from the procedure
4. When you need to process rows one by one (rarely needed, usually there's a set-based alternative)

---