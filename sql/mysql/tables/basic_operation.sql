-- ============================================
-- DATABASE & TABLE MANAGEMENT COMPREHENSIVE GUIDE
-- ============================================

-- 🔰 DATABASE SELECTION
USE testdb;
-- Purpose: Sets testdb as the current working database

-- 📋 VIEW EXISTING TABLES
SHOW TABLES;
-- Lists all tables in the current database

-- ============================================
-- 🆕 TABLE CREATION
-- ============================================

-- 🎯 Create a table if it doesn't exist (prevents errors)
CREATE TABLE IF NOT EXISTS student (
    student_id INT AUTO_INCREMENT,              -- Auto-incrementing primary key
    student_name VARCHAR(50),                   -- Variable length string up to 50 chars
    student_age INT NOT NULL,                   -- Integer field that cannot be NULL
    gender VARCHAR(6),                          -- Fixed format gender field
    PRIMARY KEY (student_id)                    -- Defines the primary key constraint
);
-- Note: AUTO_INCREMENT requires the column to be part of a PRIMARY KEY or UNIQUE KEY

-- 📊 VERIFY TABLE CREATION
SHOW TABLES;                                   -- Lists tables including the new one
DESC student;                                  -- Shows table structure (columns, types, constraints)
SHOW FULL TABLES;                              -- Shows table names and their type (BASE TABLE/VIEW)

-- 🔍 ADVANCED TABLE LISTING
SHOW TABLES IN testdb;                         -- Explicitly specify database
SHOW TABLES FROM testdb;                       -- Alternative syntax (same result)

-- 🎭 PATTERN MATCHING FOR TABLES
SHOW TABLES IN testdb LIKE "stu%";             -- Lists tables starting with "stu"
-- % wildcard: matches any sequence of characters
-- _ wildcard: matches exactly one character

-- ============================================
-- 🔧 TABLE STRUCTURE MODIFICATION (ALTER)
-- ============================================
-- ALTER vs UPDATE:
--   - ALTER: Modifies TABLE STRUCTURE (columns, constraints, etc.)
--   - UPDATE: Modifies TABLE DATA (values in rows)

DESC student;                                  -- Check current structure

-- ❌ DROPPING A COLUMN
ALTER TABLE student
DROP gender;                                   -- Removes the 'gender' column permanently
-- Warning: Data in dropped column is irrecoverably lost

DESC student;                                  -- Confirm column removal

-- ➕ ADDING COLUMNS WITH POSITIONING

-- Add column at FIRST position
ALTER TABLE student
ADD gender VARCHAR(7) FIRST;                   -- Adds column as the first column
-- Note: Not all RDBMS support FIRST/LAST positioning

-- Add column at LAST position
ALTER TABLE student
ADD gender VARCHAR(7) LAST;                    -- Adds column at the end
-- MySQL supports FIRST and LAST positioning

DESC student;                                  -- Verify positioning

-- Add column AFTER specific column
ALTER TABLE student
ADD gender VARCHAR(6) AFTER student_name;      -- Adds column after student_name
-- This is the most commonly used positioning syntax

DESC student;                                  -- Verify column position

-- 🔄 MODIFYING COLUMN DATA TYPES
ALTER TABLE student
MODIFY student_age VARCHAR(3);                 -- Changes INT to VARCHAR(3)

ALTER TABLE student
MODIFY student_age INT;                        -- Reverts back to INT
-- Warning: Changing data types may cause data loss or truncation

-- ➕ ADD COLUMN WITH DEFAULT VALUE
ALTER TABLE student
ADD enrolled INT DEFAULT 0 AFTER student_name; -- Adds column with default value 0

DESC student;                                  -- Shows default value

-- ⚙️ MODIFYING DEFAULT VALUES
ALTER TABLE student
ALTER enrolled SET DEFAULT 99;                 -- Changes default to 99

ALTER TABLE student
ALTER enrolled DROP DEFAULT;                   -- Removes default value constraint
-- Note: Existing rows retain their values, only new rows are affected

-- ============================================
-- 🔄 RENAMING TABLES
-- ============================================

-- Single table rename using ALTER
ALTER TABLE student
RENAME stud;                                   -- Renames student to stud

DESC stud;                                     -- Verify rename

-- Single table rename using RENAME TABLE
RENAME TABLE stud TO student;                  -- Alternative syntax for single table
-- Note: Requires ALTER and DROP privileges

DESC student;                                  -- Verify rename back

-- Multiple table rename in one statement
RENAME TABLE 
    student TO stud,                           -- Renames student to stud
    customers TO clients,                      -- Renames customers to clients
    comprehensive_data_types TO datatype;      -- Renames comprehensive_data_types to datatype
-- All renames are atomic (all succeed or all fail)

SHOW TABLES;                                   -- Verify all renames

-- Revert using ALTER TABLE
ALTER TABLE stud RENAME TO student;            -- Single table rename back

SHOW TABLES;                                   -- Final table list

-- ============================================
-- 📑 TABLE CLONING TECHNIQUES
-- ============================================

-- 🎯 Create base table for cloning examples
CREATE TABLE CUSTOMERS (
    ID INT AUTO_INCREMENT,                     -- Auto-incrementing primary key
    NAME VARCHAR(20) NOT NULL,                 -- Non-nullable name field
    AGE INT NOT NULL,                          -- Non-nullable age field
    ADDRESS CHAR(25),                          -- Fixed-length address
    SALARY DECIMAL(18, 2),                     -- Decimal with precision (18,2)
    PRIMARY KEY (ID)                           -- Primary key constraint
);

-- 📥 Insert sample data
INSERT INTO CUSTOMERS (ID, NAME, AGE, ADDRESS, SALARY) VALUES
(1, 'Ramesh', 32, 'Ahmedabad', 2000.00),
(2, 'Khilan', 25, 'Delhi', 1500.00),
(3, 'Kaushik', 23, 'Kota', 2000.00),
(4, 'Chaitali', 25, 'Mumbai', 6500.00),
(5, 'Hardik', 27, 'Bhopal', 8500.00),
(6, 'Komal', 22, 'Hyderabad', 4500.00),
(7, 'Muffy', 24, 'Indore', 10000.00);

-- 📝 View table creation statement
SHOW CREATE TABLE CUSTOMERS;                   -- Shows exact CREATE TABLE statement
DESC CUSTOMERS;                                -- Shows table structure

-- ============================================
-- 🧬 CLONING METHODS
-- ============================================

-- 1. MANUAL CLONING (Structure + Data separately)
CREATE TABLE `copy_customers` (
    `ID` INT NOT NULL AUTO_INCREMENT,
    `NAME` VARCHAR(20) NOT NULL,
    `AGE` INT NOT NULL,
    `ADDRESS` CHAR(25) DEFAULT NULL,
    `SALARY` DECIMAL(18, 2) DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB AUTO_INCREMENT = 8 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;
-- Recreates exact structure including storage engine and charset

INSERT INTO copy_customers (ID, NAME, AGE, ADDRESS, SALARY)
SELECT ID, NAME, AGE, ADDRESS, SALARY
FROM CUSTOMERS;                                -- Copies data from original table

SELECT * FROM copy_customers;                  -- Verify data copy

-- 2. SIMPLE CLONING (Structure + Data in one command)
CREATE TABLE copy_customer
SELECT * FROM CUSTOMERS;                       -- Creates table with structure AND data
-- Note: Does NOT copy constraints (PK, AI, defaults) or indexes

-- 3. SHALLOW CLONING (Structure only)
CREATE TABLE testcustomer LIKE CUSTOMERS;      -- Copies structure including constraints
-- Creates empty table with same schema but NO data

-- 4. DEEP CLONING (Structure + Data in two steps)
CREATE TABLE deep_customer LIKE CUSTOMERS;     -- Step 1: Copy structure
INSERT INTO deep_customer SELECT * FROM customers;  -- Step 2: Copy data
-- Most complete cloning method - preserves everything

-- 🗑️ DATA DELETION METHODS
TRUNCATE TABLE deep_customer;                  -- Removes all rows, resets auto-increment
DELETE FROM deep_customer;                     -- Removes rows but doesn't reset auto-increment
-- TRUNCATE vs DELETE:
--   TRUNCATE: Faster, cannot rollback, resets auto-increment
--   DELETE: Slower, can rollback, doesn't reset auto-increment

SELECT * FROM deep_customer;                   -- Verify table is empty

-- ============================================
-- ⏱️ TEMPORARY TABLES
-- ============================================

-- Create temporary table (session-specific)
CREATE TEMPORARY TABLE CUSTOMERS (
    ID INT NOT NULL,
    NAME VARCHAR(20) NOT NULL,
    AGE INT NOT NULL,
    ADDRESS CHAR(25),
    SALARY DECIMAL(18, 2),
    PRIMARY KEY (ID)
);
-- Temporary tables:
--   - Exist only for current session
--   - Automatically dropped when session ends
--   - Can have same name as permanent tables (shadows them)
--   - Faster than permanent tables for temporary operations

INSERT INTO CUSTOMERS VALUES
(1, 'Ramesh', 32, 'Ahmedabad', 2000.00),
(2, 'Khilan', 25, 'Delhi', 1500.00),
(3, 'kaushik', 23, 'Kota', 2000.00);

SELECT * FROM CUSTOMERS;                       -- Shows temporary table data

DROP TEMPORARY TABLE CUSTOMERS;                -- Explicitly drop temporary table
SHOW TABLES;                                   -- Temporary tables don't appear here

-- ============================================
-- 🔧 TABLE MAINTENANCE (MyISAM only)
-- ============================================
-- Note: REPAIR TABLE only works with MyISAM storage engine

-- Convert to MyISAM for demonstration (not recommended for production)
ALTER TABLE CUSTOMERS ENGINE = MyISAM;         -- Changes storage engine
ALTER TABLE stud ENGINE = MyISAM;              -- Changes another table

-- Repair table (fixes corrupted tables)
REPAIR TABLE CUSTOMERS;                        -- Basic repair
REPAIR TABLE customers, stud;                  -- Repair multiple tables

-- REPAIR OPTIONS:
REPAIR TABLE CUSTOMERS QUICK;                  -- Quick repair (default)
REPAIR TABLE CUSTOMERS EXTENDED;               -- Extended repair (rebuilds indexes)
REPAIR TABLE CUSTOMERS USE_FRM;                -- Use when .MYI index file is missing

-- ============================================
-- 📋 TABLE INSPECTION COMMANDS
-- ============================================

-- View table structure
DESC customers;                                -- Short form
DESCRIBE customers;                            -- Long form
DESCRIBE customers name;                       -- Describe specific column

-- Alternative syntax for column listing
SHOW COLUMNS FROM customers;                   -- Standard syntax
SHOW COLUMNS IN customers;                     -- Alternative syntax
SHOW COLUMNS IN CUSTOMERS FROM testdb;         -- Explicit database specification
SHOW FIELDS IN CUSTOMERS;                      -- Synonym for SHOW COLUMNS

-- Filtered column listing
SHOW COLUMNS FROM CUSTOMERS WHERE Type = 'int';  -- Shows only integer columns

-- Detailed column information
SHOW FULL COLUMNS IN CUSTOMERS;                -- Shows privileges, collation, etc.

-- Table analysis (query optimization)
EXPLAIN customers;                             -- Shows how MySQL would execute queries

-- EXPLAIN FORMATS for query analysis:
EXPLAIN FORMAT = TRADITIONAL SELECT * FROM CUSTOMERS;  -- Traditional tabular format
EXPLAIN FORMAT = JSON SELECT * FROM CUSTOMERS;         -- JSON format for programmatic use
EXPLAIN FORMAT = TREE SELECT * FROM CUSTOMERS;         -- Tree format (MySQL 8.0+)

-- ============================================
-- 🔄 ADVANCED ALTER OPERATIONS
-- ============================================

-- Add single column
ALTER TABLE CUSTOMERS 
ADD COLUMN STATUS_id INT NOT NULL;             -- Adds non-nullable column

-- Add multiple columns
ALTER TABLE CUSTOMERS 
ADD COLUMN Addrs CHAR(25),                     -- Add first column
ADD COLUMN CONTACT INT;                        -- Add second column

-- Drop single column
ALTER TABLE CUSTOMERS
DROP COLUMN Addrs;                             -- Removes the Addrs column

-- Drop multiple columns
ALTER TABLE CUSTOMERS 
DROP COLUMN contact,                           -- Remove contact column
DROP COLUMN name;                              -- Remove name column

-- Rename columns (MySQL 8.0+)
ALTER TABLE CUSTOMERS 
RENAME COLUMN S_NO TO cust_name,               -- Rename S_NO to cust_name
RENAME COLUMN AGE TO cust_age;                 -- Rename AGE to cust_age

-- Change column definition (old syntax)
ALTER TABLE CUSTOMERS 
CHANGE COLUMN ID AGE varchar(10);              -- Changes column name AND data type
-- CHANGE vs MODIFY:
--   CHANGE: Can rename column AND change type
--   MODIFY: Can only change type (keeps same name)
--   RENAME COLUMN: Only renames (doesn't change type)

-- ============================================
-- 📝 BEST PRACTICES SUMMARY
-- ============================================
/*
1. Always use IF NOT EXISTS for table creation to avoid errors
2. Use descriptive, consistent naming conventions
3. Consider using InnoDB for transactional support
4. Always specify PRIMARY KEY for better performance
5. Use appropriate data types (avoid over-sizing)
6. Document your schema changes
7. Test ALTER operations on staging before production
8. Use transactions for multiple related operations
9. Consider using migrations tools for version control
10. Regular maintenance (optimize, analyze) for performance
*/

-- 🎯 Final verification
SHOW TABLES;                                   -- List all final tables
SELECT 'Script execution completed successfully' AS Status;