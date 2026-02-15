# 📚 MySQL Other Handy Functions - Complete Guide

## 🎯 Overview
These versatile functions handle type conversion, NULL management, uniqueness, system information, and conditional logic. They're the "Swiss Army knife" of MySQL functions.

## 📚 Function Reference

### 1. **CAST() / CONVERT()**
**Definition**: Converts a value from one data type to another.

**Syntax**:
```sql
CAST(expression AS type)
CONVERT(expression, type)  -- Alternative syntax
CONVERT(expression USING charset)  -- Character set conversion
```

**Supported Type Conversions**:
| Target Type | Description | Example |
|------------|-------------|---------|
| `CHAR` | Character string | `CAST(123 AS CHAR)` → '123' |
| `SIGNED` | Signed integer | `CAST('123' AS SIGNED)` → 123 |
| `UNSIGNED` | Unsigned integer | `CAST('123' AS UNSIGNED)` → 123 |
| `DECIMAL(M,D)` | Decimal number | `CAST('123.45' AS DECIMAL(5,2))` → 123.45 |
| `DATE` | Date value | `CAST('2024-03-15' AS DATE)` → 2024-03-15 |
| `TIME` | Time value | `CAST('14:30:00' AS TIME)` → 14:30:00 |
| `DATETIME` | Date and time | `CAST('2024-03-15 14:30:00' AS DATETIME)` |
| `BINARY` | Binary string | `CAST('text' AS BINARY)` |
| `JSON` | JSON document | `CAST('{"key":"value"}' AS JSON)` |

**Examples**:
```sql
-- String to number
SELECT CAST('123' AS SIGNED) AS number;  -- Returns: 123
SELECT CONVERT('456.78', DECIMAL(10,2)) AS decimal_num;  -- Returns: 456.78

-- Number to string
SELECT CAST(123 AS CHAR) AS string_num;  -- Returns: '123'
SELECT CAST(price AS CHAR) FROM products;

-- Date/time conversions
SELECT CAST('2024-03-15' AS DATE) AS date_only;
SELECT CAST(NOW() AS DATE) AS today;  -- Removes time portion

-- JSON handling (MySQL 5.7+)
SELECT CAST('{"name":"John","age":30}' AS JSON) AS json_data;

-- Character set conversion
SELECT CONVERT('café' USING latin1) AS latin_text;

-- Implicit vs explicit conversion
SELECT '100' + '200';  -- Implicit: Returns 300 (numbers)
SELECT CONCAT('100', '200');  -- Returns '100200' (strings)

-- Handle conversion errors
SELECT CAST('abc' AS SIGNED);  -- Returns: 0 (with warning)
SELECT CAST('9999999999' AS SIGNED);  -- Returns: 2147483647 (max int)
```

**Real-World Use Cases**:
- Data type normalization in ETL processes
- Preparing data for APIs or external systems
- Dynamic query building
- Cross-database compatibility
- JSON data manipulation

---

### 2. **COALESCE()**
**Definition**: Returns the first non-NULL value from a list of arguments.

**Syntax**:
```sql
COALESCE(value1, value2, ..., valueN)
```

**Parameters**: Two or more expressions of any type

**Important**: Returns NULL if all arguments are NULL.

**Examples**:
```sql
-- Basic NULL handling
SELECT COALESCE(NULL, 'default') AS result;  -- Returns: 'default'
SELECT COALESCE(NULL, NULL, 'third', 'fourth') AS result;  -- Returns: 'third'

-- With table data
SELECT 
    username,
    COALESCE(display_name, username) AS display_name  -- Fallback to username
FROM users;

-- Multiple fallbacks
SELECT 
    product_name,
    COALESCE(
        current_price,
        previous_price,
        suggested_price,
        0  -- Ultimate fallback
    ) AS price_to_use
FROM products;

-- In calculations (avoid NULL results)
SELECT 
    quantity,
    unit_price,
    COALESCE(quantity, 0) * COALESCE(unit_price, 0) AS total_value
FROM order_items;

-- With dates
SELECT 
    order_date,
    COALESCE(shipped_date, estimated_ship_date, order_date) AS expected_date
FROM orders;

-- Nested COALESCE (equivalent to multiple arguments)
SELECT COALESCE(field1, COALESCE(field2, COALESCE(field3, 'default')));
```

**Comparison with Related Functions**:
```sql
-- COALESCE vs IFNULL (IFNULL only takes 2 arguments)
SELECT COALESCE(NULL, NULL, 'value');  -- Works
SELECT IFNULL(NULL, 'value');  -- Works
-- SELECT IFNULL(NULL, NULL, 'value');  -- Error

-- COALESCE vs NULLIF (opposite purpose)
SELECT NULLIF('A', 'A');  -- Returns NULL if equal
SELECT COALESCE(NULLIF(column, ''), 'N/A');  -- Common pattern
```

**Real-World Use Cases**:
- Default values for missing data
- Data cleaning and normalization
- Report generation with fallbacks
- Configuration value cascading
- API response formatting

---

### 3. **DISTINCT**
**Definition**: Removes duplicate rows from a result set.

**Syntax**:
```sql
SELECT DISTINCT column1, column2, ... FROM table;
-- or with aggregate functions
SELECT COUNT(DISTINCT column) FROM table;
```

**Important**: 
- Applies to the entire row (all selected columns)
- NULL is considered a distinct value
- Can impact performance on large datasets

**Examples**:
```sql
-- Basic distinct values
SELECT DISTINCT category FROM products;  -- Unique categories

-- Multiple columns
SELECT DISTINCT city, state FROM customers;  -- Unique city/state combos

-- With ORDER BY
SELECT DISTINCT department 
FROM employees 
ORDER BY department;

-- COUNT of distinct values
SELECT COUNT(DISTINCT customer_id) AS unique_customers FROM orders;
SELECT COUNT(DISTINCT product_id, order_id) AS unique_combinations FROM order_items;

-- In subqueries
SELECT * FROM products 
WHERE category IN (SELECT DISTINCT category FROM discontinued_products);

-- With expressions
SELECT DISTINCT YEAR(order_date) AS order_years FROM orders;

-- DISTINCT vs GROUP BY (similar results, different purposes)
SELECT DISTINCT category FROM products;  -- Just unique values
SELECT category FROM products GROUP BY category;  -- Can add aggregates
```

**Performance Considerations**:
```sql
-- Adding indexes can improve DISTINCT performance
CREATE INDEX idx_category ON products(category);

-- For large datasets, consider alternatives
SELECT category FROM products GROUP BY category;  -- Might be faster

-- With LIMIT
SELECT DISTINCT customer_id FROM orders LIMIT 100;  -- First 100 unique
```

**Common Pitfalls**:
```sql
-- DISTINCT applies to ALL selected columns
SELECT DISTINCT first_name, last_name FROM users;  -- Unique combos, not unique first names

-- With aggregate functions
SELECT COUNT(DISTINCT *) FROM table;  -- ERROR: Can't use * with DISTINCT
SELECT COUNT(DISTINCT column) FROM table;  -- Correct

-- NULL handling
SELECT DISTINCT NULL, NULL;  -- Returns one row with (NULL, NULL)
```

**Real-World Use Cases**:
- Dropdown list population
- Data quality checks (finding duplicates)
- Report dimension tables
- Unique visitor counting
- Data deduplication in exports

---

### 4. **DATABASE()**
**Definition**: Returns the name of the current (default) database.

**Syntax**:
```sql
DATABASE()
```

**Parameters**: None

**Examples**:
```sql
-- Get current database name
SELECT DATABASE();  -- Returns: 'sales_demo' or current db name

-- Use in queries
SELECT * FROM information_schema.tables 
WHERE table_schema = DATABASE();

-- Dynamic query building
SET @table_name = CONCAT(DATABASE(), '.users');
PREPARE stmt FROM CONCAT('SELECT * FROM ', @table_name);
EXECUTE stmt;

-- In stored procedures
CREATE PROCEDURE get_tables_in_current_db()
BEGIN
    SELECT table_name 
    FROM information_schema.tables 
    WHERE table_schema = DATABASE();
END;

-- Check if database is selected
SELECT 
    CASE 
        WHEN DATABASE() IS NULL THEN 'No database selected'
        ELSE CONCAT('Current database: ', DATABASE())
    END AS status;

-- Logging with database context
INSERT INTO global_log (db_name, message, timestamp)
VALUES (DATABASE(), 'Procedure executed', NOW());
```

**Related Functions**:
```sql
-- Get current user
SELECT USER(), CURRENT_USER();

-- Get connection ID
SELECT CONNECTION_ID();

-- Get version
SELECT VERSION();
```

**Real-World Use Cases**:
- Multi-tenant application routing
- Dynamic schema management
- Audit logging
- Migration scripts
- Database management tools

---

### 5. **IF()**
**Definition**: Returns one value if a condition is TRUE, another value if FALSE.

**Syntax**:
```sql
IF(condition, value_if_true, value_if_false)
```

**Parameters**:
- `condition`: Expression to evaluate
- `value_if_true`: Returned if condition is TRUE (not zero, not NULL)
- `value_if_false`: Returned if condition is FALSE or NULL

**Examples**:
```sql
-- Basic conditional
SELECT IF(1 > 0, 'Yes', 'No');  -- Returns: 'Yes'
SELECT IF(NULL, 'True', 'False');  -- Returns: 'False' (NULL is falsey)

-- With table data
SELECT 
    product_name,
    quantity,
    IF(quantity > 0, 'In Stock', 'Out of Stock') AS stock_status
FROM products;

-- Nested IF statements
SELECT 
    score,
    IF(score >= 90, 'A',
       IF(score >= 80, 'B',
          IF(score >= 70, 'C',
             IF(score >= 60, 'D', 'F')))) AS grade
FROM exam_results;

-- With calculations
SELECT 
    price,
    IF(price > 100, price * 0.9, price) AS discounted_price  -- 10% off if > 100
FROM products;

-- Date comparisons
SELECT 
    order_date,
    IF(order_date >= CURDATE() - INTERVAL 7 DAY, 'Recent', 'Older') AS recency
FROM orders;

-- NULL handling
SELECT 
    middle_name,
    IF(middle_name IS NULL, 'No middle name', middle_name) AS display_name
FROM users;
```

**Comparison with CASE**:
```sql
-- Simple conditions: IF is cleaner
SELECT IF(status = 'A', 'Active', 'Inactive') FROM users;

-- Multiple conditions: CASE is better
SELECT 
    CASE status
        WHEN 'A' THEN 'Active'
        WHEN 'I' THEN 'Inactive'
        WHEN 'S' THEN 'Suspended'
        ELSE 'Unknown'
    END AS status_text
FROM users;

-- Complex logic: CASE is more readable
SELECT 
    CASE 
        WHEN score >= 90 THEN 'A'
        WHEN score >= 80 THEN 'B'
        WHEN score >= 70 THEN 'C'
        WHEN score >= 60 THEN 'D'
        ELSE 'F'
    END AS grade
FROM exams;
```

**Real-World Use Cases**:
- Conditional formatting in reports
- Business rule implementation
- Data classification
- Feature flagging
- Default value assignment

---

### 6. **VERSION()**
**Definition**: Returns the MySQL server version as a string.

**Syntax**:
```sql
VERSION()
```

**Examples**:
```sql
-- Get server version
SELECT VERSION();  -- Returns: '8.0.33' or similar

-- Version-based feature detection
SELECT 
    CASE 
        WHEN VERSION() LIKE '8.%' THEN 'MySQL 8.x'
        WHEN VERSION() LIKE '5.7%' THEN 'MySQL 5.7'
        WHEN VERSION() LIKE '5.6%' THEN 'MySQL 5.6'
        ELSE 'Older version'
    END AS version_category;

-- Check for specific features
SELECT 
    IF(VERSION() >= '8.0.0', 'Window Functions Available', 'Upgrade Needed') AS feature_status;

-- In conditional logic
CREATE PROCEDURE dynamic_query()
BEGIN
    IF VERSION() >= '8.0.0' THEN
        -- Use window functions
        SELECT *, ROW_NUMBER() OVER (ORDER BY id) FROM table;
    ELSE
        -- Use alternative for older versions
        SELECT * FROM table;
    END IF;
END;

-- Log version for debugging
INSERT INTO system_log (component, message, metadata)
VALUES ('Database', 'Connection established', 
        CONCAT('MySQL Version: ', VERSION()));

-- Compare with @@version system variable
SELECT VERSION(), @@version;  -- Usually the same
```

**Version Components**:
```sql
-- Parse version components
SELECT 
    VERSION() AS full_version,
    SUBSTRING_INDEX(VERSION(), '.', 1) AS major_version,
    SUBSTRING_INDEX(SUBSTRING_INDEX(VERSION(), '.', 2), '.', -1) AS minor_version,
    SUBSTRING_INDEX(VERSION(), '-', 1) AS version_number,
    IF(VERSION() LIKE '%MariaDB%', 'MariaDB', 'MySQL') AS database_type;
```

**Real-World Use Cases**:
- Version-specific SQL scripts
- Feature availability checks
- Migration planning
- Compatibility testing
- Support diagnostics

---

## 🎯 **Combined Use Cases Framework**

### **Data Cleaning Pipeline**
```sql
-- Comprehensive data cleaning with multiple functions
CREATE TABLE clean_customers AS
SELECT 
    -- ID field (ensure integer)
    CAST(COALESCE(customer_id, 0) AS UNSIGNED) AS customer_id,
    
    -- Name fields (clean and format)
    COALESCE(
        NULLIF(TRIM(first_name), ''),
        'Unknown'
    ) AS first_name,
    
    COALESCE(
        NULLIF(TRIM(last_name), ''),
        'Unknown'
    ) AS last_name,
    
    -- Email (lowercase, trim, default)
    LOWER(TRIM(COALESCE(email, CONCAT('user_', customer_id, '@example.com')))) AS email,
    
    -- Phone (standardize format)
    IF(
        phone IS NOT NULL AND phone != '',
        CONCAT(
            '(', LEFT(phone, 3), ') ',
            SUBSTRING(phone, 4, 3), '-',
            RIGHT(phone, 4)
        ),
        NULL
    ) AS formatted_phone,
    
    -- Status (with business logic)
    IF(
        COALESCE(last_purchase_date, '1900-01-01') > DATE_SUB(CURDATE(), INTERVAL 6 MONTH),
        'Active',
        'Inactive'
    ) AS customer_status,
    
    -- Metadata
    DATABASE() AS source_database,
    VERSION() AS mysql_version,
    NOW() AS cleaning_timestamp
    
FROM raw_customers
WHERE CAST(customer_id AS UNSIGNED) > 0;  -- Valid IDs only
```

### **Dynamic Reporting System**
```sql
-- Report with conditional logic and formatting
SELECT 
    -- Dynamic identifiers
    CONCAT('ORD-', LPAD(CAST(order_id AS CHAR), 8, '0')) AS order_number,
    
    -- Customer info with fallbacks
    COALESCE(
        CONCAT(c.first_name, ' ', c.last_name),
        'Guest Customer'
    ) AS customer_name,
    
    -- Status with colors (for UI)
    IF(
        o.status = 'shipped',
        CONCAT('🟢 ', UPPER(o.status)),
        IF(
            o.status = 'processing',
            CONCAT('🟡 ', UPPER(o.status)),
            CONCAT('🔴 ', UPPER(o.status))
        )
    ) AS status_display,
    
    -- Financials with formatting
    CONCAT('$', FORMAT(CAST(o.total_amount AS DECIMAL(10,2)), 2)) AS order_total,
    
    -- Age of order
    CAST(DATEDIFF(CURDATE(), o.order_date) AS CHAR) AS days_old,
    
    -- Priority flag
    IF(
        o.total_amount > 1000 
        OR DATEDIFF(CURDATE(), o.order_date) > 30,
        'HIGH PRIORITY',
        'Normal'
    ) AS priority,
    
    -- Source context
    DATABASE() AS report_database
    
FROM orders o
LEFT JOIN customers c ON o.customer_id = c.customer_id
ORDER BY 
    IF(priority = 'HIGH PRIORITY', 0, 1),  -- High priority first
    o.order_date DESC;
```

### **Multi-Database Management**
```sql
-- Database inventory and management
SELECT 
    -- Database information
    schema_name AS database_name,
    
    -- Size information
    CAST(
        ROUND(SUM(data_length + index_length) / 1024 / 1024, 2)
        AS DECIMAL(10,2)
    ) AS size_mb,
    
    -- Table count
    COUNT(*) AS table_count,
    
    -- Version compatibility
    IF(
        VERSION() LIKE '8.%',
        'Fully Compatible',
        'May Need Migration'
    ) AS compatibility,
    
    -- Default database check
    IF(
        schema_name = DATABASE(),
        '✅ Current',
        '📁 Other'
    ) AS current_status,
    
    -- Last update (estimated)
    COALESCE(
        MAX(update_time),
        MAX(create_time),
        'Unknown'
    ) AS last_modified
    
FROM information_schema.tables 
WHERE table_schema NOT IN ('mysql', 'information_schema', 'performance_schema', 'sys')
GROUP BY schema_name
ORDER BY 
    IF(schema_name = DATABASE(), 0, 1),  -- Current db first
    size_mb DESC;
```

---

## 🔧 **Performance & Best Practices**

### 1. **Type Conversion Optimization**
```sql
-- Implicit conversion (let MySQL decide)
SELECT * FROM products WHERE id = '100';  -- String converted to number

-- Explicit conversion (when needed)
SELECT * FROM products WHERE CAST(id AS CHAR) LIKE '10%';  -- Slower

-- Create computed columns for frequent conversions
ALTER TABLE products ADD COLUMN id_str VARCHAR(20) AS (CAST(id AS CHAR)) STORED;
CREATE INDEX idx_id_str ON products(id_str);
```

### 2. **NULL Handling Strategies**
```sql
-- Index-friendly NULL handling
SELECT * FROM users WHERE COALESCE(name, '') = '';  -- Can't use index on name
SELECT * FROM users WHERE name IS NULL OR name = '';  -- Better for indexing

-- Use DEFAULT values instead of COALESCE in some cases
ALTER TABLE users MODIFY COLUMN status VARCHAR(20) DEFAULT 'active' NOT NULL;
```

### 3. **DISTINCT Alternatives**
```sql
-- For large datasets, consider these alternatives
-- Option 1: EXISTS (often faster than DISTINCT)
SELECT c1 FROM t1 WHERE EXISTS (SELECT 1 FROM t2 WHERE t2.c1 = t1.c1);

-- Option 2: Temporary table with index
CREATE TEMPORARY TABLE temp_unique AS 
SELECT DISTINCT column FROM large_table;
ALTER TABLE temp_unique ADD INDEX (column);

-- Option 3: GROUP BY if you need aggregates anyway
SELECT column, COUNT(*) FROM large_table GROUP BY column;
```

---

## 🎯 **Quick Reference Cheat Sheet**

| Function | Purpose | Example |
|----------|---------|---------|
| `CAST()` | Type conversion | `CAST('123' AS SIGNED)` → 123 |
| `COALESCE()` | First non-NULL | `COALESCE(NULL, 'default')` → 'default' |
| `DISTINCT` | Remove duplicates | `SELECT DISTINCT city` |
| `DATABASE()` | Current database | `SELECT DATABASE()` → 'mydb' |
| `IF()` | Conditional | `IF(score>50,'Pass','Fail')` |
| `VERSION()` | MySQL version | `SELECT VERSION()` → '8.0.33' |

---

## 🧪 **Practice Challenges**

1. **Type-Safe Calculator**: Create a calculator that handles string inputs safely using CAST
2. **Smart Default System**: Implement a cascading default value system using COALESCE
3. **Duplicate Detective**: Find and classify duplicates in a table using DISTINCT and COUNT
4. **Dynamic Schema Handler**: Write a stored procedure that works across multiple databases
5. **Grading System**: Implement a complex grading system using nested IF/CASE statements
6. **Version-Aware Migrations**: Create SQL scripts that adapt to different MySQL versions
7. **Data Type Validator**: Validate and convert user input to appropriate SQL types
8. **NULL Safety Wrapper**: Create views that guarantee no NULLs in business-facing data
9. **Multi-tenant Query Builder**: Build queries that safely reference current database
10. **Conditional Aggregation**: Create reports with dynamic calculations based on conditions

---

## 📊 **Real-World Project: Customer Data Hub**

```sql
-- End-to-end data processing example
CREATE PROCEDURE process_customer_data()
BEGIN
    DECLARE current_db VARCHAR(64);
    DECLARE mysql_ver VARCHAR(64);
    
    -- Capture context
    SET current_db = DATABASE();
    SET mysql_ver = VERSION();
    
    -- Process and clean data
    INSERT INTO customer_master (
        customer_id,
        full_name,
        email,
        phone,
        status,
        data_source,
        processed_at
    )
    SELECT 
        -- ID management
        CAST(COALESCE(cust_id, 0) AS UNSIGNED) AS customer_id,
        
        -- Name composition with smart defaults
        IF(
            COALESCE(first_name, '') != '' AND COALESCE(last_name, '') != '',
            CONCAT(
                UPPER(LEFT(first_name, 1)), LOWER(SUBSTRING(first_name, 2)),
                ' ',
                UPPER(LEFT(last_name, 1)), LOWER(SUBSTRING(last_name, 2))
            ),
            COALESCE(company_name, 'Unknown Customer')
        ) AS full_name,
        
        -- Email validation and cleaning
        IF(
            email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$',
            LOWER(TRIM(email)),
            CONCAT('invalid_', CAST(cust_id AS CHAR), '@placeholder.com')
        ) AS email,
        
        -- Phone standardization
        IF(
            phone IS NOT NULL AND phone != '',
            CONCAT(
                '+1',  -- Assuming US numbers
                REGEXP_REPLACE(phone, '[^0-9]', '')
            ),
            NULL
        ) AS phone,
        
        -- Business logic for status
        CASE 
            WHEN last_order_date > DATE_SUB(CURDATE(), INTERVAL 30 DAY) THEN 'Active'
            WHEN last_order_date > DATE_SUB(CURDATE(), INTERVAL 90 DAY) THEN 'Recent'
            WHEN COALESCE(total_orders, 0) > 0 THEN 'Past Customer'
            ELSE 'Prospect'
        END AS status,
        
        -- Metadata
        current_db AS data_source,
        mysql_ver AS mysql_version,
        NOW() AS processed_at
        
    FROM raw_customer_import
    WHERE CAST(COALESCE(cust_id, 0) AS UNSIGNED) > 0;
    
    -- Log the operation
    INSERT INTO process_log (
        process_name,
        database_name,
        mysql_version,
        rows_processed,
        executed_at
    )
    VALUES (
        'customer_data_processing',
        current_db,
        mysql_ver,
        ROW_COUNT(),
        NOW()
    );
    
END;
```