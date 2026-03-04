# **Standard Stored Procedure Template**

```sql
-- =============================================
-- Procedure: [procedure_name]
-- Description: [Brief description]
-- Created By: [Your Name]
-- Created Date: [Date]
-- =============================================

DELIMITER $$

DROP PROCEDURE IF EXISTS `schema_name`.`procedure_name`$$

CREATE PROCEDURE `schema_name`.`procedure_name`(
    -- Input Parameters
    IN param1 DATATYPE,
    IN param2 DATATYPE,
    
    -- Output Parameters
    OUT out_param1 DATATYPE,
    OUT out_param2 DATATYPE,
    
    -- Input/Output Parameters
    INOUT inout_param DATATYPE
)
BEGIN
    /*
    Purpose: [Detailed description]
    
    Parameters:
        param1: [Description]
        param2: [Description]
        out_param1: [Description]
        inout_param: [Description]
    
    Returns/Outputs:
        out_param1: [What it returns]
        inout_param: [What it returns]
        
    Error Codes:
        0 - Success
        1 - [Error description]
        2 - [Error description]
    
    Example Call:
        CALL procedure_name(value1, value2, @out1, @out2, @inout);
        SELECT @out1, @out2, @inout;
    
    Revision History:
        [Date] - [Your Name] - [Change description]
    */
    
    -- =========================================
    -- DECLARATIONS
    -- =========================================
    DECLARE v_local_variable1 DATATYPE DEFAULT default_value;
    DECLARE v_local_variable2 DATATYPE;
    DECLARE v_exit_handler BOOLEAN DEFAULT FALSE;
    
    -- Error handling variables
    DECLARE v_error_code INT DEFAULT 0;
    DECLARE v_error_message VARCHAR(1000);
    
    -- Cursor declarations (if needed)
    DECLARE v_done INT DEFAULT FALSE;
    DECLARE cursor_name CURSOR FOR SELECT ...;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = TRUE;
    
    -- Exception/Error handlers
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1
            v_error_code = MYSQL_ERRNO,
            v_error_message = MESSAGE_TEXT;
        
        -- Rollback transaction if active
        IF @transaction_active = 1 THEN
            ROLLBACK;
            SET @transaction_active = 0;
        END IF;
        
        -- Log error (if you have error log table)
        -- INSERT INTO error_log (error_code, error_message, procedure_name, created_at)
        -- VALUES (v_error_code, v_error_message, 'procedure_name', NOW());
        
        -- Set output parameters
        SET out_param1 = CONCAT('Error: ', v_error_code);
        SET out_param2 = v_error_message;
        
        -- Optional: Signal error to application
        -- SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_error_message;
    END;
    
    -- =========================================
    -- VALIDATION
    -- =========================================
    -- Check required parameters
    IF param1 IS NULL OR param1 = '' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'param1 is required';
    END IF;
    
    -- Validate business rules
    IF param2 < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'param2 cannot be negative';
    END IF;
    
    -- =========================================
    -- INITIALIZATION
    -- =========================================
    SET @transaction_active = 0;
    SET v_local_variable1 = COALESCE(param1, default_value);
    
    -- =========================================
    -- MAIN LOGIC
    -- =========================================
    START TRANSACTION;
    SET @transaction_active = 1;
    
    BEGIN
        -- Example: Data retrieval
        SELECT column1, column2 INTO v_local_variable1, v_local_variable2
        FROM table_name
        WHERE conditions
        LIMIT 1;
        
        -- Example: Data modification
        UPDATE table_name
        SET column1 = value1,
            column2 = value2,
            modified_at = NOW(),
            modified_by = USER()
        WHERE conditions;
        
        -- Example: Insert with error checking
        INSERT INTO table_name (col1, col2, created_at)
        VALUES (v_local_variable1, param2, NOW());
        
        -- Example: Using cursor (rarely needed, usually avoid)
        OPEN cursor_name;
        read_loop: LOOP
            FETCH cursor_name INTO v_local_variable1, v_local_variable2;
            IF v_done THEN
                LEAVE read_loop;
            END IF;
            
            -- Process each row
        END LOOP;
        CLOSE cursor_name;
        
        -- Set output parameters
        SET out_param1 = v_local_variable1;
        SET out_param2 = (SELECT COUNT(*) FROM table_name);
        SET inout_param = inout_param * 2;
        
        COMMIT;
        SET @transaction_active = 0;
        
        -- Set success message/flag
        SET out_param1 = 'Success';
        
    END;
    
    -- =========================================
    -- CLEANUP (if needed)
    -- =========================================
    -- Reset session variables
    SET @transaction_active = NULL;
    
END$$

DELIMITER ;
```

## **Simplified Template (Most Common Use)**

```sql
DELIMITER $$

DROP PROCEDURE IF EXISTS `your_schema`.`procedure_name`$$

CREATE PROCEDURE `your_schema`.`procedure_name`(
    IN p_input_param INT,
    OUT p_output_message VARCHAR(100)
)
BEGIN
    /*
    Example: Updates user status and returns message
    */
    
    DECLARE v_user_count INT DEFAULT 0;
    DECLARE v_current_status VARCHAR(50);
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_output_message = 'Error occurred';
    END;
    
    -- Validate input
    IF p_input_param IS NULL THEN
        SET p_output_message = 'Input parameter is required';
        RETURN;
    END IF;
    
    START TRANSACTION;
    
    -- Check if record exists
    SELECT COUNT(*) INTO v_user_count
    FROM users
    WHERE user_id = p_input_param;
    
    IF v_user_count = 0 THEN
        ROLLBACK;
        SET p_output_message = 'User not found';
        RETURN;
    END IF;
    
    -- Get current status
    SELECT status INTO v_current_status
    FROM users
    WHERE user_id = p_input_param;
    
    -- Business logic
    IF v_current_status = 'ACTIVE' THEN
        UPDATE users
        SET status = 'INACTIVE',
            updated_at = NOW()
        WHERE user_id = p_input_param;
        
        SET p_output_message = 'User deactivated';
    ELSE
        UPDATE users
        SET status = 'ACTIVE',
            updated_at = NOW()
        WHERE user_id = p_input_param;
        
        SET p_output_message = 'User activated';
    END IF;
    
    -- Optional: Insert audit log
    INSERT INTO audit_log (user_id, action, performed_at)
    VALUES (p_input_param, p_output_message, NOW());
    
    COMMIT;
    
END$$

DELIMITER ;
```

## **CRUD Operation Templates**

### **1. SELECT Procedure (Read)**
```sql
DELIMITER $$

CREATE PROCEDURE `get_records`(
    IN p_id INT,
    IN p_active_only BOOLEAN,
    IN p_limit INT,
    IN p_offset INT
)
BEGIN
    /*
    Gets records with filtering and pagination
    */
    
    SET @sql = '
        SELECT id, name, status, created_at
        FROM records
        WHERE 1=1';
    
    IF p_id IS NOT NULL THEN
        SET @sql = CONCAT(@sql, ' AND id = ', p_id);
    END IF;
    
    IF p_active_only = TRUE THEN
        SET @sql = CONCAT(@sql, ' AND status = "ACTIVE"');
    END IF;
    
    SET @sql = CONCAT(@sql, ' ORDER BY created_at DESC');
    
    IF p_limit IS NOT NULL THEN
        SET @sql = CONCAT(@sql, ' LIMIT ', p_limit);
        
        IF p_offset IS NOT NULL THEN
            SET @sql = CONCAT(@sql, ' OFFSET ', p_offset);
        END IF;
    END IF;
    
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
END$$

DELIMITER ;
```

### **2. INSERT Procedure (Create)**
```sql
DELIMITER $$

CREATE PROCEDURE `create_record`(
    IN p_name VARCHAR(100),
    IN p_description TEXT,
    OUT p_new_id INT,
    OUT p_message VARCHAR(100)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_new_id = 0;
        SET p_message = 'Insert failed';
    END;
    
    -- Check for duplicates
    IF EXISTS (SELECT 1 FROM records WHERE name = p_name) THEN
        SET p_new_id = 0;
        SET p_message = 'Duplicate name';
        RETURN;
    END IF;
    
    INSERT INTO records (name, description, created_at)
    VALUES (p_name, p_description, NOW());
    
    SET p_new_id = LAST_INSERT_ID();
    SET p_message = 'Record created successfully';
    
END$$

DELIMITER ;
```

### **3. UPDATE Procedure**
```sql
DELIMITER $$

CREATE PROCEDURE `update_record`(
    IN p_id INT,
    IN p_name VARCHAR(100),
    IN p_status VARCHAR(20),
    OUT p_rows_affected INT,
    OUT p_message VARCHAR(100)
)
BEGIN
    DECLARE v_old_name VARCHAR(100);
    
    START TRANSACTION;
    
    -- Get old values for audit
    SELECT name INTO v_old_name
    FROM records
    WHERE id = p_id
    FOR UPDATE;
    
    IF v_old_name IS NULL THEN
        ROLLBACK;
        SET p_rows_affected = 0;
        SET p_message = 'Record not found';
        RETURN;
    END IF;
    
    UPDATE records
    SET name = COALESCE(p_name, name),
        status = COALESCE(p_status, status),
        updated_at = NOW(),
        version = version + 1
    WHERE id = p_id;
    
    -- Audit trail
    INSERT INTO record_history 
        (record_id, changed_column, old_value, new_value, changed_by)
    VALUES 
        (p_id, 'name', v_old_name, p_name, USER());
    
    SET p_rows_affected = ROW_COUNT();
    SET p_message = CONCAT('Updated ', p_rows_affected, ' record(s)');
    
    COMMIT;
    
END$$

DELIMITER ;
```

### **4. DELETE Procedure**
```sql
DELIMITER $$

CREATE PROCEDURE `delete_record`(
    IN p_id INT,
    IN p_hard_delete BOOLEAN DEFAULT FALSE,
    OUT p_message VARCHAR(100)
)
BEGIN
    DECLARE v_reference_count INT;
    
    -- Check for foreign key constraints
    SELECT COUNT(*) INTO v_reference_count
    FROM child_table
    WHERE record_id = p_id;
    
    IF v_reference_count > 0 THEN
        SET p_message = 'Cannot delete - referenced by other records';
        RETURN;
    END IF;
    
    IF p_hard_delete THEN
        -- Permanent delete
        DELETE FROM records WHERE id = p_id;
        SET p_message = 'Record permanently deleted';
    ELSE
        -- Soft delete
        UPDATE records 
        SET status = 'DELETED',
            deleted_at = NOW(),
            deleted_by = USER()
        WHERE id = p_id;
        SET p_message = 'Record soft deleted';
    END IF;
    
END$$

DELIMITER ;
```

## **Quick Reference Template (Most Basic)**
```sql
DELIMITER $$

CREATE PROCEDURE procedure_name(
    IN input_param INT,
    OUT output_param VARCHAR(100)
)
BEGIN
    -- Variable declarations
    DECLARE local_var INT DEFAULT 0;
    
    -- Error handling
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET output_param = 'Error';
    END;
    
    -- Main logic
    SELECT column INTO local_var
    FROM table
    WHERE condition;
    
    IF local_var > 0 THEN
        -- Do something
        SET output_param = 'Success';
    ELSE
        SET output_param = 'Failed';
    END IF;
    
END$$

DELIMITER ;
```

## **Best Practices Checklist**

1. **Naming Conventions**
   - Use meaningful names: `get_user_orders`
   - Prefix parameters: `p_` for input, `v_` for local variables
   - Use snake_case for consistency

2. **Error Handling**
   - Always include error handlers
   - Use transactions for data integrity
   - Provide meaningful error messages

3. **Performance**
   - Avoid cursors when possible
   - Use appropriate indexes
   - Limit result sets with pagination

4. **Security**
   - Validate all inputs
   - Use parameterized queries (avoid dynamic SQL)
   - Implement proper permissions

5. **Maintainability**
   - Add comments and documentation
   - Include revision history
   - Keep procedures focused (single responsibility)

6. **Testing**
   - Test with NULL values
   - Test edge cases
   - Test rollback scenarios

**Example Usage:**
```sql
-- Create
CALL create_record('Test', 'Description', @id, @msg);
SELECT @id, @msg;

-- Read
CALL get_records(NULL, TRUE, 10, 0);

-- Update
CALL update_record(1, 'New Name', 'ACTIVE', @rows, @msg);
SELECT @rows, @msg;

-- Delete
CALL delete_record(1, FALSE, @msg);
SELECT @msg;
```

Choose the template based on your needs - use the simplified one for basic operations and the comprehensive one for complex business logic with auditing and error logging.