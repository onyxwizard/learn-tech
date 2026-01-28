# **📊 MYSQL TABLE COMPREHENSIVE GUIDE**
## **============================================================================**
## **This script demonstrates complete table lifecycle management in MySQL**
## **including creation, structure modification, constraints, and maintenance**
## **============================================================================**
-- ============================================================================
-- 🏗️ SECTION 1: TABLE CREATION FUNDAMENTALS
-- ============================================================================
-- 📋 Check current database context
--    Always know which database you're working in
SELECT
    DATABASE();

-- 🔍 List all tables in current database
--    Shows all tables you have access to
SHOW TABLES;

-- 🎯 Create a basic table with minimal structure
--    Syntax: CREATE TABLE table_name (column1 datatype, column2 datatype, ...);
--    Minimal example with three columns
CREATE TABLE
    minimal_table (id INT, NAME VARCHAR(50), created DATE);

-- 🔍 Verify table creation
--    Shows the new table in the list
SHOW TABLES;

-- 🗑️ Clean up - remove the minimal table
--    Always clean up temporary/test tables
DROP TABLE IF EXISTS minimal_table;

-- ============================================================================
-- 📐 SECTION 2: COLUMNS & DATA TYPES DEEP DIVE
-- ============================================================================
-- 🎪 Create comprehensive table with various data types
--    Demonstrates: Numeric, String, Date/Time, Boolean, Special types
CREATE TABLE
    comprehensive_data_types (
        -- 🔢 NUMERIC TYPES
        tiny_int_col TINYINT, -- -128 to 127 (1 byte)
        small_int_col SMALLINT, -- -32,768 to 32,767 (2 bytes)
        medium_int_col MEDIUMINT, -- -8,388,608 to 8,388,607 (3 bytes)
        int_col INT, -- -2,147,483,648 to 2,147,483,647 (4 bytes) ← Most Common
        big_int_col BIGINT, -- ±9.22×10¹⁸ (8 bytes)
        unsigned_int INT UNSIGNED, -- 0 to 4,294,967,295 (no negatives)
        auto_inc_id INT AUTO_INCREMENT, -- Auto-generates sequential numbers
        -- 💰 DECIMAL for exact precision (MONEY, PRICES)
        price DECIMAL(10, 2), -- 10 total digits, 2 decimal places
        weight DECIMAL(8, 4), -- 8 total digits, 4 decimal places
        -- 🔢 FLOATING POINT for scientific/approximate values
        float_col FLOAT, -- Single precision (4 bytes)
        double_col DOUBLE, -- Double precision (8 bytes)
        -- 🔤 STRING TYPES
        fixed_char CHAR(10), -- Fixed length, pads with spaces
        var_char VARCHAR(255), -- Variable length, efficient storage ← Most Common
        long_text TEXT, -- Up to 65,535 characters
        medium_text MEDIUMTEXT, -- Up to 16,777,215 characters
        long_text_col LONGTEXT, -- Up to 4,294,967,295 characters
        -- 🎭 ENUM & SET (limited value sets)
        gender ENUM('Male', 'Female', 'Other'), -- Must be one of these values
        colors SET('Red', 'Green', 'Blue'), -- Can be multiple values (comma-separated)
        -- 📅 DATE & TIME TYPES
        birth_date DATE, -- YYYY-MM-DD
        meeting_time TIME, -- HH:MM:SS
        full_datetime DATETIME, -- YYYY-MM-DD HH:MM:SS ← Most Common
        timestamp_col TIMESTAMP, -- UTC timestamp, auto-updates
        year_col YEAR, -- 4-digit year
        -- ✅ BOOLEAN (actually TINYINT(1))
        is_active BOOLEAN, -- TRUE=1, FALSE=0
        is_verified BOOL DEFAULT FALSE, -- Alias for BOOLEAN
        -- 🎨 SPECIAL TYPES (MySQL 5.7+)
        json_data JSON, -- Store JSON documents
        -- geometry_data GEOMETRY,                -- Spatial data (commented for simplicity)
        -- ⏰ AUTO-GENERATED TIMESTAMPS
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        -- 🔑 PRIMARY KEY (for demonstration, will be added in constraints section)
        PRIMARY KEY (auto_inc_id)
    );

-- 🔍 View table structure in detail
--    Shows column names, types, nullability, defaults, and extra info
DESCRIBE comprehensive_data_types;

-- Short form: DESC comprehensive_data_types;
-- 📊 Show exact SQL to recreate table
--    Useful for migration or documentation
SHOW CREATE TABLE comprehensive_data_types;

-- ============================================================================
-- 🔐 SECTION 3: CONSTRAINTS - DATA INTEGRITY GUARDIANS
-- ============================================================================
-- 🎯 Create table with comprehensive constraints
--    Constraints ensure data validity and relationships
CREATE TABLE
    employee_constraints (
        -- 🆔 PRIMARY KEY: Uniquely identifies each row, automatically NOT NULL
        employee_id INT PRIMARY KEY AUTO_INCREMENT,
        -- 🔤 NOT NULL: Column must have a value
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        -- ✉️ UNIQUE: No duplicate values allowed (can be NULL)
        email VARCHAR(100) UNIQUE NOT NULL,
        ssn VARCHAR(11) UNIQUE,
        -- 💰 CHECK: Enforce business rules (MySQL 8.0+)
        salary DECIMAL(10, 2) CHECK (salary > 0),
        bonus DECIMAL(10, 2) CHECK (
            bonus >= 0 AND
            bonus <= 10000
        ),
        -- 📅 DEFAULT: Automatic value if not specified
        hire_date DATE DEFAULT(CURRENT_DATE),
        is_active BOOLEAN DEFAULT TRUE,
        -- 🔢 FOREIGN KEY: Relationship to another table
        department_id INT,
        manager_id INT,
        -- ✅ ENUM: Limited set of allowed values
        employment_type ENUM('Full-time', 'Part-time', 'Contract') DEFAULT 'Full-time',
        -- ⚠️ Condition-based CHECK
        age TINYINT UNSIGNED CHECK (
            age >= 18 AND
            age <= 70
        ),
        -- 🔄 AUTO-UPDATE TIMESTAMP
        last_modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        -- 🎯 COMPOSITE UNIQUE: Multiple columns together must be unique
        UNIQUE KEY unique_employee_name (first_name, last_name, department_id),
        -- 🔗 FOREIGN KEY CONSTRAINTS with referential actions
        -- FOREIGN KEY (department_id) REFERENCES departments(dept_id)
        --     ON DELETE RESTRICT      -- Prevent delete if employees exist in department
        --     ON UPDATE CASCADE,      -- Update employee if dept_id changes
        -- FOREIGN KEY (manager_id) REFERENCES employee_constraints(employee_id)
        --     ON DELETE SET NULL      -- Set to NULL if manager is deleted
        --     ON UPDATE CASCADE
    );

-- 🔍 Verify constraint implementation
DESC employee_constraints;

-- 📋 Insert test data to validate constraints
INSERT INTO
    employee_constraints (
        first_name,
        last_name,
        email,
        salary,
        department_id,
        age
    )
VALUES
    (
        'John',
        'Doe',
        'john.doe@company.com',
        75000.00,
        1,
        30
    ),
    (
        'Jane',
        'Smith',
        'jane.smith@company.com',
        85000.00,
        2,
        28
    );

-- ❌ This will FAIL due to duplicate email (UNIQUE constraint)
-- INSERT INTO employee_constraints (first_name, last_name, email, salary, age)
-- VALUES ('Bob', 'Johnson', 'john.doe@company.com', 60000.00, 25);
-- ❌ This will FAIL due to CHECK constraint (salary <= 0)
-- INSERT INTO employee_constraints (first_name, last_name, email, salary, age)
-- VALUES ('Bob', 'Johnson', 'bob@company.com', -5000.00, 25);
-- ============================================================================
-- 🛠️ SECTION 4: TABLE STRUCTURE MANIPULATION
-- ============================================================================
-- 🏢 Create a sample table for manipulation exercises
CREATE TABLE
    company_employees (
        emp_id INT PRIMARY KEY AUTO_INCREMENT,
        emp_name VARCHAR(100),
        emp_department VARCHAR(50),
        emp_salary DECIMAL(10, 2),
        hire_date DATE DEFAULT(CURRENT_DATE),
        STATUS VARCHAR(20) DEFAULT 'Active'
    );

-- 📝 Insert sample data
INSERT INTO
    company_employees (emp_name, emp_department, emp_salary)
VALUES
    ('Alice Johnson', 'Engineering', 85000.00),
    ('Bob Smith', 'Marketing', 72000.00),
    ('Carol Davis', 'Engineering', 92000.00);

-- 🔍 View initial data
SELECT
    *
FROM
    company_employees;

-- ============================================================================
-- 🔄 SECTION 4A: ADDING COLUMNS
-- ============================================================================
-- ➕ Add single column
--    Syntax: ALTER TABLE table_name ADD COLUMN column_name datatype [constraints];
ALTER TABLE company_employees
ADD COLUMN email VARCHAR(100);

-- ➕ Add multiple columns at once
ALTER TABLE company_employees
ADD COLUMN phone VARCHAR(20),
ADD COLUMN address TEXT,
ADD COLUMN date_of_birth DATE;

-- ➕ Add column with constraints and default value
ALTER TABLE company_employees
ADD COLUMN is_manager BOOLEAN DEFAULT FALSE NOT NULL;

-- ➕ Add column at specific position
ALTER TABLE company_employees
ADD COLUMN middle_name VARCHAR(50) AFTER emp_name, -- After specific column
ADD COLUMN employee_code VARCHAR(20) FIRST;

-- As first column
-- 🔍 Verify column addition
DESC company_employees;

-- ============================================================================
-- 🗑️ SECTION 4B: REMOVING COLUMNS
-- ============================================================================
-- ❌ Drop single column
--    Syntax: ALTER TABLE table_name DROP COLUMN column_name;
ALTER TABLE company_employees
DROP COLUMN middle_name;

-- ❌ Drop multiple columns
ALTER TABLE company_employees
DROP COLUMN phone,
DROP COLUMN address;

-- 🛡️ Safe drop (MySQL 8.0.23+)
--    Won't error if column doesn't exist
ALTER TABLE company_employees
DROP COLUMN IF EXISTS nonexistent_column;

-- 🔍 Verify column removal
DESC company_employees;

-- ============================================================================
-- ✏️ SECTION 4C: MODIFYING COLUMNS
-- ============================================================================
-- 🔧 Modify column data type and constraints
--    Syntax: ALTER TABLE table_name MODIFY COLUMN column_name new_datatype [constraints];
ALTER TABLE company_employees
MODIFY COLUMN emp_name VARCHAR(150) NOT NULL;

-- 🔧 Change column name and properties
--    Syntax: ALTER TABLE table_name CHANGE COLUMN old_name new_name new_datatype [constraints];
ALTER TABLE company_employees
CHANGE COLUMN emp_department department VARCHAR(75) DEFAULT 'General';

-- 🔧 Rename column only (MySQL 8.0+)
ALTER TABLE company_employees
RENAME COLUMN emp_salary TO salary;

-- 🔧 Set default value for existing column
ALTER TABLE company_employees
ALTER COLUMN STATUS
SET DEFAULT 'Onboarding';

-- 🔧 Remove default value
ALTER TABLE company_employees
ALTER COLUMN STATUS
DROP DEFAULT;

-- 🔍 Verify modifications
DESC company_employees;

-- ============================================================================
-- 📋 SECTION 4D: TABLE RENAMING
-- ============================================================================
-- 🔄 Rename table using RENAME TABLE
--    Syntax: RENAME TABLE old_name TO new_name;
RENAME TABLE company_employees TO employees;

-- 🔄 Rename table using ALTER TABLE
ALTER TABLE employees
RENAME TO company_staff;

-- 🔄 Rename multiple tables
RENAME TABLE company_staff TO staff,
comprehensive_data_types TO data_types_demo;

-- 🔍 Verify renaming
SHOW TABLES;

-- ============================================================================
-- 🧪 SECTION 4E: TABLE CLONING & COPYING
-- ============================================================================
-- 📋 Clone structure only (no data)
--    Syntax: CREATE TABLE new_table LIKE original_table;
CREATE TABLE
    staff_backup LIKE staff;

-- 🔍 Verify structure clone
DESC staff_backup;

SELECT
    COUNT(*)
FROM
    staff_backup;

-- Should be 0
-- 📋 Clone structure WITH data
--    Syntax: CREATE TABLE new_table SELECT * FROM original_table;
CREATE TABLE
    staff_copy
SELECT
    *
FROM
    staff;

-- 🔍 Verify data clone
SELECT
    *
FROM
    staff_copy;

-- 📋 Clone with partial data
CREATE TABLE
    engineering_staff
SELECT
    *
FROM
    staff
WHERE
    department = 'Engineering';

-- 📋 Clone with modified structure
CREATE TABLE
    staff_summary (
        SELECT
            department,
            COUNT(*)    AS employee_count,
            AVG(salary) AS avg_salary
        FROM
            staff
        GROUP BY
            department
    );

-- 🔍 Verify summary clone
SELECT
    *
FROM
    staff_summary;

-- ============================================================================
-- 🧹 SECTION 4F: TRUNCATE VS DELETE
-- ============================================================================
-- 📊 Check current auto_increment value
SELECT
    AUTO_INCREMENT
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = DATABASE() AND
    TABLE_NAME = 'staff_copy';

-- 🧹 TRUNCATE TABLE - Remove all rows, reset auto_increment
--    Faster, cannot rollback, resets auto_increment
TRUNCATE TABLE staff_copy;

-- 🔍 Verify truncate (should be empty, auto_increment reset to 1)
SELECT
    *
FROM
    staff_copy;

SELECT
    AUTO_INCREMENT
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = DATABASE() AND
    TABLE_NAME = 'staff_copy';

-- 🔄 Repopulate for DELETE demo
INSERT INTO
    staff_copy
SELECT
    *
FROM
    staff
LIMIT
    2;

-- 🗑️ DELETE - Remove rows with conditions
--    Slower, can rollback, doesn't reset auto_increment
DELETE FROM staff_copy
WHERE
    emp_id = 1;

-- 🗑️ DELETE ALL rows (without resetting auto_increment)
DELETE FROM staff_copy;

-- 🔍 Check auto_increment after DELETE (still at last value)
SELECT
    AUTO_INCREMENT
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = DATABASE() AND
    TABLE_NAME = 'staff_copy';

-- ============================================================================
-- ⏱️ SECTION 4G: TEMPORARY TABLES
-- ============================================================================
-- ⏳ Create local temporary table
--    Visible only to current session, auto-dropped on disconnect
CREATE TEMPORARY TABLE
    temp_active_staff (
        SELECT
            *
        FROM
            staff
        WHERE
            STATUS = 'Active'
    );

-- 🔍 Use temporary table
SELECT
    *
FROM
    temp_active_staff;

-- ➕ Add column to temp table
ALTER TABLE temp_active_staff
ADD COLUMN temp_note VARCHAR(100);

-- 🔍 Temp tables shadow permanent tables
CREATE TEMPORARY TABLE
    staff (id INT, NAME VARCHAR(50));

-- Now references temp table, not permanent table
SELECT
    *
FROM
    staff;

-- 🧹 Manually drop temporary table
DROP TEMPORARY TABLE IF EXISTS temp_active_staff;

DROP TEMPORARY TABLE IF EXISTS staff;

-- ============================================================================
-- 🩺 SECTION 4H: TABLE REPAIR & MAINTENANCE
-- ============================================================================
-- 🏗️ Create a MyISAM table for repair demonstration
--    Note: REPAIR TABLE mainly for MyISAM, InnoDB has auto-recovery
CREATE TABLE
    myisam_demo (id INT PRIMARY KEY, DATA VARCHAR(100)) ENGINE = MyISAM;

-- 🛠️ REPAIR TABLE (MyISAM only)
--    Syntax: REPAIR TABLE table_name [options];
REPAIR TABLE myisam_demo;

-- Standard repair
REPAIR TABLE myisam_demo QUICK;

-- Quick repair (index only)
REPAIR TABLE myisam_demo EXTENDED;

-- Extended repair (slow but thorough)
-- 📊 CHECK TABLE (Both MyISAM and InnoDB)
--    Checks table for errors
CHECK TABLE myisam_demo;

CHECK TABLE myisam_demo QUICK;

CHECK TABLE myisam_demo FAST;

CHECK TABLE myisam_demo CHANGED;

-- 🔄 ANALYZE TABLE - Update index statistics
--    Helps optimizer choose better query plans
ANALYZE TABLE staff;

-- 🔧 OPTIMIZE TABLE - Defragment and reclaim space
--    Useful after大量 DELETE operations
OPTIMIZE TABLE staff;

-- 🔍 Table status information
SHOW TABLE STATUS LIKE 'staff';

-- 🗑️ Clean up MyISAM demo
DROP TABLE myisam_demo;

-- ============================================================================
-- 🔒 SECTION 4I: TABLE LOCKING
-- ============================================================================
-- 🔐 READ LOCK - Allow concurrent reads, block writes
--    Other sessions can SELECT, cannot INSERT/UPDATE/DELETE
LOCK TABLES staff READ;

-- 🔍 Test read operations (works in same session)
SELECT
    *
FROM
    staff
LIMIT
    1;

-- ❌ Test write operations in same session (works with lock)
-- INSERT INTO staff (emp_name, department) VALUES ('Test', 'HR');
-- 🔓 Unlock tables
UNLOCK TABLES;

-- 🔐 WRITE LOCK - Exclusive access
--    Other sessions cannot read or write
LOCK TABLES staff WRITE;

-- ✅ Current session can read/write
SELECT
    COUNT(*)
FROM
    staff;

-- INSERT INTO staff (emp_name, department) VALUES ('Lock Test', 'IT');
-- 🔓 Unlock
UNLOCK TABLES;

-- 🔄 Concurrent locking example
LOCK TABLES staff READ,
staff_summary WRITE;

-- ... perform operations ...
UNLOCK TABLES;

-- ============================================================================
-- 🧩 SECTION 4J: DERIVED TABLES (SUBQUERIES IN FROM)
-- ============================================================================
-- 🎯 Basic derived table
--    A subquery in FROM clause that acts like a temporary table
SELECT
    dept_summary.*
FROM
    (
        SELECT
            department,
            COUNT(*)    AS emp_count,
            AVG(salary) AS avg_salary
        FROM
            staff
        GROUP BY
            department
    ) AS dept_summary
WHERE
    avg_salary > 80000;

-- 🔗 Derived table with JOIN
SELECT
    s.emp_name,
    s.salary,
    dept_avg.avg_dept_salary,
    s.salary - dept_avg.avg_dept_salary AS diff_from_avg
FROM
    staff s
    JOIN (
        SELECT
            department,
            AVG(salary) AS avg_dept_salary
        FROM
            staff
        GROUP BY
            department
    ) AS dept_avg ON s.department = dept_avg.department;

-- 📊 Multiple derived tables
SELECT
    high_earners.emp_name,
    high_earners.salary,
    dept_info.department
FROM
    (
        SELECT
            emp_id,
            emp_name,
            salary,
            department
        FROM
            staff
        WHERE
            salary > 85000
    ) AS high_earners
    JOIN (
        SELECT DISTINCT
            department
        FROM
            staff
        WHERE
            department LIKE 'E%'
    ) AS dept_info ON high_earners.department = dept_info.department;

-- 🎨 Using WITH clause (Common Table Expressions - CTEs)
--    More readable alternative to derived tables
WITH
    department_stats AS (
        SELECT
            department,
            COUNT(*)    AS emp_count,
            AVG(salary) AS avg_salary
        FROM
            staff
        GROUP BY
            department
    ),
    high_paying_depts AS (
        SELECT
            *
        FROM
            department_stats
        WHERE
            avg_salary > 80000
    )
SELECT
    *
FROM
    high_paying_depts
ORDER BY
    avg_salary DESC;

-- ============================================================================
-- 📚 REAL-WORLD SCENARIOS
-- ============================================================================
-- 🎯 SCENARIO 1: E-commerce Product Table
CREATE TABLE
    products (
        product_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        sku VARCHAR(50) UNIQUE NOT NULL,
        product_name VARCHAR(200) NOT NULL,
        DESCRIPTION TEXT,
        price DECIMAL(10, 2) NOT NULL CHECK (price > 0),
        cost_price DECIMAL(10, 2) NOT NULL CHECK (cost_price > 0),
        stock_quantity INT UNSIGNED DEFAULT 0,
        reorder_level INT UNSIGNED DEFAULT 10,
        category_id INT UNSIGNED,
        supplier_id INT UNSIGNED,
        is_active BOOLEAN DEFAULT TRUE,
        weight_kg DECIMAL(8, 3),
        dimensions VARCHAR(50), -- "10x5x2 cm"
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        -- Indexes for performance
        INDEX idx_category (category_id),
        INDEX idx_supplier (supplier_id),
        INDEX idx_price (price),
        INDEX idx_stock (stock_quantity),
        -- Check: price should be >= cost_price
        CHECK (price >= cost_price),
        -- Check: reorder_level should be <= stock_quantity initially
        CHECK (reorder_level <= 1000)
    ) COMMENT = 'Stores product information for e-commerce';

-- 🎯 SCENARIO 2: User Authentication System
CREATE TABLE
    users (
        user_id BINARY(16) PRIMARY KEY DEFAULT(UUID_TO_BIN(UUID())), -- UUID as binary
        username VARCHAR(50) UNIQUE NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        first_name VARCHAR(50),
        last_name VARCHAR(50),
        date_of_birth DATE,
        phone VARCHAR(20),
        -- Authentication fields
        email_verified BOOLEAN DEFAULT FALSE,
        verification_token VARCHAR(100),
        reset_token VARCHAR(100),
        reset_token_expiry DATETIME,
        -- Security
        FAILED_LOGIN_ATTEMPTS TINYINT UNSIGNED DEFAULT 0,
        account_locked_until DATETIME,
        last_login DATETIME,
        password_changed_at DATETIME,
        -- Preferences
        timezone VARCHAR(50) DEFAULT 'UTC',
        locale VARCHAR(10) DEFAULT 'en_US',
        -- Status
        is_active BOOLEAN DEFAULT TRUE,
        deactivated_at DATETIME,
        -- Timestamps
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        -- Indexes
        INDEX idx_email (email),
        INDEX idx_username (username),
        INDEX idx_created_at (created_at),
        INDEX idx_status (is_active, deactivated_at),
        -- Constraints
        CHECK (CHAR_LENGTH(username) >= 3),
        CHECK (email LIKE '%@%.%')
    ) COMMENT = 'User authentication and profile data';

-- 🎯 SCENARIO 3: Financial Transactions
CREATE TABLE
    financial_transactions (
        transaction_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        transaction_uuid CHAR(36) UNIQUE DEFAULT(UUID()),
        from_account_id INT UNSIGNED NOT NULL,
        to_account_id INT UNSIGNED NOT NULL,
        amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
        currency CHAR(3) DEFAULT 'USD',
        transaction_type ENUM('transfer', 'deposit', 'withdrawal', 'payment') NOT NULL,
        STATUS ENUM('pending', 'completed', 'failed', 'cancelled') DEFAULT 'pending',
        DESCRIPTION VARCHAR(255),
        reference_number VARCHAR(100),
        -- Metadata
        initiated_by INT UNSIGNED,
        reviewed_by INT UNSIGNED,
        reviewed_at DATETIME,
        -- Financial controls
        exchange_rate DECIMAL(12, 6),
        fees DECIMAL(10, 2) DEFAULT 0,
        net_amount DECIMAL(15, 2) GENERATED ALWAYS AS (amount - fees) STORED,
        -- Timestamps
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        processed_at DATETIME,
        completed_at DATETIME,
        -- Audit trail
        ip_address VARCHAR(45),
        user_agent TEXT,
        -- Indexes
        INDEX idx_from_account (from_account_id),
        INDEX idx_to_account (to_account_id),
        INDEX idx_status_created (STATUS, created_at),
        INDEX idx_reference (reference_number),
        INDEX idx_dates (created_at, processed_at, completed_at),
        -- Foreign keys (commented as example)
        -- FOREIGN KEY (from_account_id) REFERENCES accounts(account_id),
        -- FOREIGN KEY (to_account_id) REFERENCES accounts(account_id),
        -- Check: from and to accounts cannot be same for transfers
        CHECK (
            transaction_type != 'transfer' OR
            from_account_id != to_account_id
        )
    ) COMMENT = 'Financial transaction ledger with audit trail';

-- ============================================================================
-- ⚠️ BEST PRACTICES & GUIDELINES
-- ============================================================================
/*
1. 🏷️ NAMING CONVENTIONS
- Use snake_case for tables and columns
- Be descriptive but concise (users, not tbl_usr_data)
- Use singular table names (user, not users)
- Prefix with module for large systems (crm_contacts, inventory_products)

2. 🔑 PRIMARY KEYS
- Every table MUST have a primary key
- Prefer INT/BIGINT AUTO_INCREMENT for simplicity
- Use natural keys only if truly unique and stable
- Consider UUID for distributed systems

3. 📊 DATA TYPE SELECTION
- Use smallest data type that fits your data
- DECIMAL for money, never FLOAT
- VARCHAR over CHAR for variable-length strings
- DATE/DATETIME for dates, never VARCHAR
- Use ENUM for limited, static value sets

4. 🛡️ CONSTRAINTS
- Always use NOT NULL for required fields
- Add CHECK constraints for business rules (MySQL 8.0+)
- Use UNIQUE for columns that shouldn't duplicate
- Implement foreign keys for data integrity

5. 🚀 PERFORMANCE
- Add indexes based on query patterns, not guesses
- Avoid over-indexing (hurts write performance)
- Use composite indexes for multi-column queries
- Consider partitioning for very large tables

6. 📝 DOCUMENTATION
- Use COMMENT on tables and columns
- Maintain data dictionary
- Document constraints and relationships
- Include sample data for complex tables

7. 🔒 SECURITY
- Don't store plain text passwords
- Encrypt sensitive data (SSN, credit cards)
- Use appropriate access controls
- Audit sensitive tables regularly

8. 📈 SCALABILITY
- Plan for growth in BIGINT columns
- Consider sharding strategy for massive tables
- Use generated columns for computed values
- Archive old data to separate tables
 */
-- ============================================================================
-- 🛠️ TROUBLESHOOTING COMMON ISSUES
-- ============================================================================
/*
ISSUE 1: "Table doesn't exist" error
SOLUTION: Check database context with SELECT DATABASE();
Verify with SHOW TABLES;

ISSUE 2: "Duplicate entry" for primary key
SOLUTION: Check AUTO_INCREMENT value or use INSERT IGNORE/ON DUPLICATE KEY UPDATE

ISSUE 3: "Data too long" for column
SOLUTION: Increase column size or validate input before insert

ISSUE 4: "Cannot add foreign key constraint"
SOLUTION: Ensure referenced table exists and columns have same data type
Both tables must use InnoDB engine

ISSUE 5: "Incorrect integer value" for ENUM/SET
SOLUTION: Always use string values, not numeric indexes

ISSUE 6: "Table is full" (MyISAM)
SOLUTION: Increase data file size or switch to InnoDB

ISSUE 7: "Deadlock found"
SOLUTION: Access tables in consistent order in transactions
Use appropriate isolation levels

ISSUE 8: "Can't create table (errno: 150)"
SOLUTION: Check foreign key constraint names are unique
Verify column types match exactly
 */
-- ============================================================================
-- 📈 ADVANCED: TABLE OPTIMIZATION TECHNIQUES
-- ============================================================================
-- 🎯 Using generated columns
CREATE TABLE
    order_calculations (
        order_id INT PRIMARY KEY,
        quantity INT NOT NULL,
        unit_price DECIMAL(10, 2) NOT NULL,
        discount DECIMAL(5, 2) DEFAULT 0,
        -- Virtual column (calculated on read)
        subtotal DECIMAL(12, 2) AS (quantity * unit_price) VIRTUAL,
        -- Stored column (calculated on write)
        total DECIMAL(12, 2) AS ((quantity * unit_price) * (1 - discount / 100)) STORED,
        INDEX idx_total (total)
    );

-- 🎯 Table compression for InnoDB
CREATE TABLE
    large_archive (
        id BIGINT PRIMARY KEY,
        DATA JSON,
        created_at TIMESTAMP
    ) ENGINE = InnoDB ROW_FORMAT = COMPRESSED KEY_BLOCK_SIZE = 8;

-- 🎯 Partitioning large tables
CREATE TABLE
    sensor_data (
        sensor_id INT,
        reading_value DECIMAL(10, 4),
        reading_time DATETIME NOT NULL,
        location_id INT
    )
PARTITION BY
    RANGE (YEAR(reading_time)) (
        PARTITION p2020
        VALUES
            LESS THAN (2021),
            PARTITION p2021
        VALUES
            LESS THAN (2022),
            PARTITION p2022
        VALUES
            LESS THAN (2023),
            PARTITION p2023
        VALUES
            LESS THAN (2024),
            PARTITION p_future
        VALUES
            LESS THAN MAXVALUE
    );

-- ============================================================================
-- 🧹 FINAL CLEANUP SCRIPT
-- ============================================================================
-- 🗑️ Drop all tables created in this demo (for testing environments only)
/*
DROP TABLE IF EXISTS minimal_table;
DROP TABLE IF EXISTS comprehensive_data_types;
DROP TABLE IF EXISTS data_types_demo;
DROP TABLE IF EXISTS employee_constraints;
DROP TABLE IF EXISTS company_employees;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS company_staff;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS staff_backup;
DROP TABLE IF EXISTS staff_copy;
DROP TABLE IF EXISTS engineering_staff;
DROP TABLE IF EXISTS staff_summary;
DROP TABLE IF EXISTS temp_active_staff;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS financial_transactions;
DROP TABLE IF EXISTS order_calculations;
DROP TABLE IF EXISTS large_archive;
DROP TABLE IF EXISTS sensor_data;
 */
-- 🔍 Final verification - show all remaining tables
SHOW TABLES;

-- 📊 Show table sizes and statistics
SELECT
    TABLE_NAME,
    TABLE_ROWS,
    DATA_LENGTH,
    INDEX_LENGTH,
    CREATE_TIME,
    UPDATE_TIME
FROM
    INFORMATION_SCHEMA.TABLES
WHERE
    TABLE_SCHEMA = DATABASE()
ORDER BY
    TABLE_NAME;

-- ============================================================================
-- 🎉 END OF COMPREHENSIVE TABLE MANAGEMENT GUIDE
-- ============================================================================