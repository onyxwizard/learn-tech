/**
 * @author onyxwizard
 * @date 08-02-2026
 */

-- 📋 COMPLETE SOLUTIONS

-- ============================================
-- PART 1: FUNCTIONS (Your code is correct, keeping as-is)
-- ============================================

-- Question 1: Calculate Discount Function (Correct)
DELIMITER $$
CREATE FUNCTION calculate_discount(
    original_price DECIMAL(10,2),
    discount_percent INT
)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE final_discount_percent INT DEFAULT discount_percent;
    DECLARE discounted_price DECIMAL(10,2);
    
    IF original_price > 20 THEN
        SET final_discount_percent = discount_percent + 5;
    END IF;
    
    SET discounted_price = original_price * (1 - final_discount_percent/100);
    RETURN ROUND(discounted_price, 2);
END $$
DELIMITER ;

-- Question 2: Loyalty Tier Function (Correct)
DELIMITER $$
CREATE FUNCTION get_loyalty_tier(loyalty_points INT)
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    RETURN CASE
        WHEN loyalty_points >= 200 THEN 'Gold'
        WHEN loyalty_points >= 100 THEN 'Silver'
        WHEN loyalty_points >= 1 THEN 'Bronze'
        ELSE 'New'
    END;
END $$
DELIMITER ;

-- Question 3: Stock Status Function (Correct)
DELIMITER $$
CREATE FUNCTION get_stock_status(
    current_stock INT,
    reorder_level INT
)
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE response VARCHAR(20);
    
    IF current_stock > reorder_level THEN
        SET response = 'In Stock';
    ELSEIF current_stock > 0 AND current_stock <= reorder_level THEN
        SET response = 'Low Stock';
    ELSEIF current_stock = 0 THEN
        SET response = 'Out of Stock';
    ELSE
        SET response = 'Negative Stock';
    END IF;
    
    RETURN response;
END $$
DELIMITER ;

-- Question 4: Tax Calculation Function (Correct)
DELIMITER $$
CREATE FUNCTION calculate_tax(
    subtotal DECIMAL(7,2),
    state_code CHAR(2)
)
RETURNS DECIMAL(7,2)
DETERMINISTIC
BEGIN
    DECLARE result DECIMAL(7,2) DEFAULT 0.00;
    DECLARE tax DECIMAL(7,2) DEFAULT 0.00;
    CASE
        WHEN state_code = 'CA' THEN
            SET tax = 7.25;
        WHEN state_code = 'NY' THEN
            SET tax = 8.875;
        WHEN state_code = 'TX' THEN
            SET tax = 6.25;
        ELSE
            SET tax = 5;
    END CASE;
    SET result = subtotal * (tax/100);
    return (result);
END $$
DELIMITER ;

-- ============================================
-- PART 2: PROCEDURES (Fixed and Complete)
-- ============================================

-- Question 5: Place Order Procedure (FIXED)
DELIMITER $$
CREATE PROCEDURE place_order(
    IN p_customer_id INT,
    IN p_product_id INT,
    IN p_quantity INT,
    OUT p_message VARCHAR(100),
    OUT p_order_id INT
)
BEGIN
    DECLARE v_current_stock INT;
    DECLARE v_unit_price DECIMAL(10,2);
    DECLARE v_total_amount DECIMAL(10,2);
    
    -- Get product info
    SELECT stock_quantity, unit_price 
    INTO v_current_stock, v_unit_price
    FROM products 
    WHERE product_id = p_product_id;
    
    -- Check if product exists
    IF v_current_stock IS NULL THEN
        SET p_message = 'ERROR: Product not found';
        SET p_order_id = NULL;
    -- Check stock
    ELSEIF v_current_stock < p_quantity THEN
        SET p_message = 'ERROR: Insufficient stock';
        SET p_order_id = NULL;
    ELSE
        -- Start transaction
        START TRANSACTION;
        
        -- Deduct stock
        UPDATE products 
        SET stock_quantity = stock_quantity - p_quantity
        WHERE product_id = p_product_id;
        
        -- Calculate total
        SET v_total_amount = v_unit_price * p_quantity;
        
        -- Create order
        INSERT INTO orders (customer_id, order_date, total_amount, status)
        VALUES (p_customer_id, NOW(), v_total_amount, 'completed');
        
        SET p_order_id = LAST_INSERT_ID();
        
        -- Create order details
        INSERT INTO order_details (order_id, product_id, quantity, unit_price, line_total)
        VALUES (p_order_id, p_product_id, p_quantity, v_unit_price, v_total_amount);
        
        -- Update loyalty points (call to procedure in Q6)
        CALL update_loyalty_points(p_customer_id, v_total_amount, @old_pts, @new_pts);
        
        COMMIT;
        SET p_message = CONCAT('SUCCESS: Order #', p_order_id, ' placed');
    END IF;
END $$
DELIMITER ;

-- Question 6: Update Loyalty Points Procedure
DELIMITER $$
CREATE PROCEDURE update_loyalty_points(
    IN p_customer_id INT,
    IN p_purchase_amount DECIMAL(10,2),
    OUT p_old_points INT,
    OUT p_new_points INT
)
BEGIN
    DECLARE v_points_earned INT;
    DECLARE v_current_tier VARCHAR(20);
    
    -- Get current points and tier
    SELECT loyalty_points, get_loyalty_tier(loyalty_points)
    INTO p_old_points, v_current_tier
    FROM customers 
    WHERE customer_id = p_customer_id;
    
    -- Calculate base points (1 point per $10)
    SET v_points_earned = FLOOR(p_purchase_amount / 10);
    
    -- Double points for Gold members (Bonus)
    IF v_current_tier = 'Gold' THEN
        SET v_points_earned = v_points_earned * 2;
    END IF;
    
    -- Update customer points
    UPDATE customers 
    SET loyalty_points = loyalty_points + v_points_earned
    WHERE customer_id = p_customer_id;
    
    -- Get new points
    SELECT loyalty_points INTO p_new_points
    FROM customers 
    WHERE customer_id = p_customer_id;
END $$
DELIMITER ;

-- Question 7: Generate Daily Report Procedure
DELIMITER $$
CREATE PROCEDURE generate_daily_report(
    IN p_report_date DATE
)
BEGIN
    -- 1. Total sales for the day
    SELECT 
        DATE(order_date) as sale_date,
        COUNT(*) as total_orders,
        SUM(total_amount) as total_sales,
        AVG(total_amount) as avg_order_value
    FROM orders
    WHERE DATE(order_date) = p_report_date
    AND status = 'completed'
    GROUP BY DATE(order_date);
    
    -- 2. Top 3 selling products
    SELECT 
        p.product_name,
        SUM(od.quantity) as total_quantity,
        SUM(od.line_total) as total_revenue
    FROM order_details od
    JOIN products p ON od.product_id = p.product_id
    JOIN orders o ON od.order_id = o.order_id
    WHERE DATE(o.order_date) = p_report_date
    AND o.status = 'completed'
    GROUP BY p.product_id, p.product_name
    ORDER BY total_quantity DESC
    LIMIT 3;
    
    -- 3. Customers who made purchases
    SELECT DISTINCT
        c.customer_id,
        CONCAT(c.first_name, ' ', c.last_name) as customer_name,
        c.email,
        COUNT(o.order_id) as orders_today,
        SUM(o.total_amount) as total_spent_today
    FROM customers c
    JOIN orders o ON c.customer_id = o.customer_id
    WHERE DATE(o.order_date) = p_report_date
    AND o.status = 'completed'
    GROUP BY c.customer_id, c.first_name, c.last_name, c.email;
END $$
DELIMITER ;

-- Question 8: Restock Products Procedure with Cursor
DELIMITER $$
CREATE PROCEDURE restock_low_inventory()
BEGIN
    DECLARE v_product_id INT;
    DECLARE v_product_name VARCHAR(100);
    DECLARE v_current_stock INT;
    DECLARE v_reorder_level INT;
    DECLARE v_done BOOLEAN DEFAULT FALSE;
    DECLARE v_restocked_count INT DEFAULT 0;
    
    -- Create temporary table for logging
    CREATE TEMPORARY TABLE IF NOT EXISTS restock_log (
        log_id INT PRIMARY KEY AUTO_INCREMENT,
        product_id INT,
        product_name VARCHAR(100),
        old_stock INT,
        new_stock INT,
        restocked_at DATETIME DEFAULT NOW()
    );
    
    -- Declare cursor
    DECLARE product_cursor CURSOR FOR
        SELECT product_id, product_name, stock_quantity, reorder_level
        FROM products
        WHERE stock_quantity <= reorder_level;
    
    -- Declare handler for cursor
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = TRUE;
    
    OPEN product_cursor;
    
    product_loop: LOOP
        FETCH product_cursor INTO v_product_id, v_product_name, v_current_stock, v_reorder_level;
        
        IF v_done THEN
            LEAVE product_loop;
        END IF;
        
        -- Increase stock by 50 units
        UPDATE products 
        SET stock_quantity = stock_quantity + 50
        WHERE product_id = v_product_id;
        
        -- Log the update
        INSERT INTO restock_log (product_id, product_name, old_stock, new_stock)
        VALUES (v_product_id, v_product_name, v_current_stock, v_current_stock + 50);
        
        SET v_restocked_count = v_restocked_count + 1;
    END LOOP;
    
    CLOSE product_cursor;
    
    -- Return summary
    SELECT 
        v_restocked_count as products_restocked,
        (SELECT GROUP_CONCAT(product_name) FROM restock_log) as restocked_products,
        (SELECT COUNT(*) FROM restock_log) as total_log_entries;
    
    -- Show detailed log
    SELECT * FROM restock_log ORDER BY log_id;
    
    -- Cleanup (optional)
    DROP TEMPORARY TABLE IF EXISTS restock_log;
END $$
DELIMITER ;

-- ============================================
-- PART 3: ADVANCED CHALLENGE
-- ============================================

-- Question 9: Function Calling Another Function
DELIMITER $$
CREATE FUNCTION calculate_total_with_tax(
    subtotal DECIMAL(10,2),
    state_code CHAR(2),
    discount_percent INT
)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE v_discounted_amount DECIMAL(10,2);
    DECLARE v_tax_amount DECIMAL(10,2);
    DECLARE v_final_total DECIMAL(10,2);
    
    -- Call calculate_discount function (from Q1)
    SET v_discounted_amount = calculate_discount(subtotal, discount_percent);
    
    -- Call calculate_tax function (from Q4)
    SET v_tax_amount = calculate_tax(v_discounted_amount, state_code);
    
    -- Calculate final total
    SET v_final_total = v_discounted_amount + v_tax_amount;
    
    RETURN ROUND(v_final_total, 2);
END $$
DELIMITER ;

-- Question 10: Procedure with Transaction & Error Handling
DELIMITER $$
CREATE PROCEDURE process_order_transaction(
    IN p_customer_id INT,
    IN p_product_list JSON, -- Format: [{"product_id": 1, "quantity": 2}, ...]
    OUT p_order_id INT,
    OUT p_message VARCHAR(200)
)
BEGIN
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_product_count INT;
    DECLARE v_product_id INT;
    DECLARE v_quantity INT;
    DECLARE v_unit_price DECIMAL(10,2);
    DECLARE v_stock_quantity INT;
    DECLARE v_line_total DECIMAL(10,2);
    DECLARE v_order_total DECIMAL(10,2) DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_message = CONCAT('ERROR: Transaction failed - ', SQLSTATE, ': ', SQLERRM);
        SET p_order_id = NULL;
    END;
    
    -- Start transaction
    START TRANSACTION;
    
    -- Parse product list and check inventory
    SET v_product_count = JSON_LENGTH(p_product_list);
    
    WHILE v_index < v_product_count DO
        SET v_product_id = JSON_EXTRACT(p_product_list, CONCAT('$[', v_index, '].product_id'));
        SET v_quantity = JSON_EXTRACT(p_product_list, CONCAT('$[', v_index, '].quantity'));
        
        -- Check stock
        SELECT stock_quantity, unit_price 
        INTO v_stock_quantity, v_unit_price
        FROM products 
        WHERE product_id = v_product_id
        FOR UPDATE; -- Lock row for update
        
        IF v_stock_quantity < v_quantity THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = CONCAT('Insufficient stock for product ID ', v_product_id);
        END IF;
        
        SET v_index = v_index + 1;
    END LOOP;
    
    -- Reset index for processing
    SET v_index = 0;
    
    -- Create order
    INSERT INTO orders (customer_id, order_date, total_amount, status)
    VALUES (p_customer_id, NOW(), 0, 'pending');
    
    SET p_order_id = LAST_INSERT_ID();
    
    -- Process each product
    WHILE v_index < v_product_count DO
        SET v_product_id = JSON_EXTRACT(p_product_list, CONCAT('$[', v_index, '].product_id'));
        SET v_quantity = JSON_EXTRACT(p_product_list, CONCAT('$[', v_index, '].quantity'));
        
        -- Get price
        SELECT unit_price INTO v_unit_price
        FROM products 
        WHERE product_id = v_product_id;
        
        SET v_line_total = v_unit_price * v_quantity;
        SET v_order_total = v_order_total + v_line_total;
        
        -- Update inventory
        UPDATE products 
        SET stock_quantity = stock_quantity - v_quantity
        WHERE product_id = v_product_id;
        
        -- Add order detail
        INSERT INTO order_details (order_id, product_id, quantity, unit_price, line_total)
        VALUES (p_order_id, v_product_id, v_quantity, v_unit_price, v_line_total);
        
        SET v_index = v_index + 1;
    END WHILE;
    
    -- Update order total
    UPDATE orders 
    SET total_amount = v_order_total, status = 'completed'
    WHERE order_id = p_order_id;
    
    -- Update loyalty points
    CALL update_loyalty_points(p_customer_id, v_order_total, @old_pts, @new_pts);
    
    -- Commit transaction
    COMMIT;
    
    SET p_message = CONCAT('SUCCESS: Order #', p_order_id, ' processed. Total: $', v_order_total);
END $$
DELIMITER ;

-- ============================================
-- PART 4: DEBUGGING CHALLENGE
-- ============================================

-- Question 11: Fix the Broken Function
DELIMITER $$
CREATE FUNCTION calculate_profit(
    sales_price DECIMAL(10,2),
    cost DECIMAL(10,2),
    quantity INT
)
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE total_cost DECIMAL(10,2);
    DECLARE total_sales DECIMAL(10,2);
    DECLARE profit DECIMAL(10,2);
    
    SET total_cost = cost * quantity;
    SET total_sales = sales_price * quantity;
    SET profit = total_sales - total_cost;
    
    IF profit > 1000 THEN
        RETURN 'High Profit';
    ELSE
        RETURN 'Low Profit';
    END IF;
END $$
DELIMITER ;

-- Question 12: Fix the Broken Procedure
DELIMITER $$
CREATE PROCEDURE update_product_price(
    IN p_product_id INT,
    IN new_price DECIMAL(10,2),
    OUT result_message VARCHAR(100)
)
BEGIN
    DECLARE old_price DECIMAL(10,2);
    
    -- Get current price
    SELECT unit_price INTO old_price
    FROM products 
    WHERE product_id = p_product_id;
    
    IF old_price IS NULL THEN
        SET result_message = 'Product not found';
    ELSEIF new_price < 0 THEN
        SET result_message = 'Price cannot be negative';
    ELSE
        UPDATE products 
        SET unit_price = new_price 
        WHERE product_id = p_product_id;
        
        SET result_message = CONCAT('Price updated from ', old_price, ' to ', new_price);
    END IF;
END $$
DELIMITER ;

-- ============================================
-- PART 5: TEST YOUR UNDERSTANDING
-- ============================================

/*
Multiple Choice Answers:
1. D) A and C (Functions return values, procedures don't; Functions can be called in SELECT, procedures can't)
2. B) DECLARE
3. A) Function always returns same result for same input
4. C) CALL procedure_name(@out_var);
5. B) Looping through result sets row by row
*/

-- ============================================
-- TESTING EXAMPLES
-- ============================================

-- Test Question 5
CALL place_order(1, 1, 2, @message, @order_id);
SELECT @message, @order_id;

-- Test Question 6
CALL update_loyalty_points(1, 50.00, @old_pts, @new_pts);
SELECT @old_pts, @new_pts;

-- Test Question 7
CALL generate_daily_report('2024-01-15');

-- Test Question 8
CALL restock_low_inventory();

-- Test Question 9
SELECT calculate_total_with_tax(100.00, 'CA', 10);

-- Test Question 10
SET @product_list = '[{"product_id": 1, "quantity": 2}, {"product_id": 4, "quantity": 1}]';
CALL process_order_transaction(1, @product_list, @order_id, @message);
SELECT @order_id, @message;

-- Test Question 11
SELECT calculate_profit(25.00, 10.00, 100);

-- Test Question 12
CALL update_product_price(1, 3.00, @result);
SELECT @result;