-- ============================================================================
-- 🏗️  DATABASE CREATION & MANAGEMENT SCRIPT
-- ============================================================================
-- 🎯 Section 1: Database Creation & Verification
-- ==============================================
-- 🆕 Create a new database named 'testdb'
--    Note: This will fail if testdb already exists
CREATE DATABASE testdb;

-- 👀 Display all available databases to verify creation
--    Shows all databases on the current MySQL server instance
SHOW DATABASES;

-- ✅ Safe database creation (with existence check)
--    Creates database only if it doesn't already exist
--    Prevents "database already exists" error
CREATE DATABASE IF NOT EXISTS testdb;

-- 🗑️ Delete (drop) the database
--    Warning: This removes all tables and data permanently!
--    Use with extreme caution in production environments
DROP DATABASE testdb;

-- 👀 Verify database has been deleted
--    Confirm testdb is no longer in the database list
SHOW DATABASES;

-- 🛡️ Safe database deletion (with existence check)
--    Only drops the database if it exists
--    Prevents "database doesn't exist" error
DROP DATABASE IF EXISTS testdb;

-- 👀 Final verification of database removal
--    Ensure clean state before proceeding
SHOW DATABASES;

-- ============================================================================
-- 🔧 Section 2: Database Selection & Navigation
-- ============================================================================
-- 🎯 Recreate the database for further operations
CREATE DATABASE IF NOT EXISTS testdb;

-- 📂 Select/Use the database for subsequent operations
--    All following SQL commands will apply to 'testdb'
--    Equivalent to changing working directory in file systems
USE testdb;

-- 📋 Display all databases (global view)
--    Shows databases from entire server, not just current context
SHOW DATABASES;

-- 📊 Alternative command to list databases
--    'SCHEMAS' is synonymous with 'DATABASES' in MySQL
SHOW SCHEMAS;

-- ============================================================================
-- 📑 Section 3: Database Copy Operations
-- ============================================================================
-- 🆕 Create a copy database for backup/testing purposes
--    Naming convention: originaldb_copy is recommended
CREATE DATABASE IF NOT EXISTS testdb_copy;

-- 📂 Switch to the new database context
--    All table operations will now affect testdb_copy
USE testdb_copy;

-- 🔄 Copy table structure from source to target database
--    Syntax: CREATE TABLE target_db.table_name LIKE source_db.table_name;
--    This copies ONLY structure (columns, data types, constraints)
--    DOES NOT copy data or indexes
--    Note: Assumes 'customers' table exists in 'testdb'
CREATE TABLE
     testdb_copy.customers LIKE testdb.customers;

-- 📥 Copy data from source table to target table
--    Syntax: INSERT target_table SELECT * FROM source_table;
--    Copies all rows from source to target
--    Requires matching column structure between tables
INSERT INTO
     testdb_copy.customers
SELECT
     *
FROM
     testdb.customers;

-- 📊 Verify table creation and data transfer
--    Show all tables in current database (testdb_copy)
SHOW TABLES;

-- 👁️ Preview data in the copied table
--    Basic SELECT to verify data integrity
SELECT
     *
FROM
     customers;

-- ============================================================================
-- 🔍 Section 4: Table Structure Analysis
-- ============================================================================
-- 🔎 Examine table structure (schema)
--    Shows: Column names, data types, nullability, keys, defaults
--    Alternative commands: DESC (short for DESCRIBE) or SHOW COLUMNS FROM
DESCRIBE customers;

-- 📝 Alternative methods for structure inspection:
--    Method 1: Full column details
-- SHOW COLUMNS FROM customers;
--    Method 2: Detailed column information
-- SHOW FULL COLUMNS FROM customers;
--    Method 3: Table creation statement (with all details)
-- SHOW CREATE TABLE customers;
-- ============================================================================
-- 💡 BEST PRACTICES & IMPORTANT NOTES
-- ============================================================================
/*
🌟 IMPORTANT CONSIDERATIONS:

1. 🛡️ PERMISSIONS:
- Ensure you have CREATE, DROP, and SELECT privileges
- Use appropriate user roles in production

2. ⚠️ DATA LOSS WARNING:
- DROP DATABASE is irreversible! Always backup first
- Consider using mysqldump for proper backups

3. 📏 NAMING CONVENTIONS:
- Use lowercase with underscores (snake_case)
- Be descriptive but concise
- Avoid reserved keywords

4. 🔄 COPYING ENTIRE DATABASES:
- For full database copy, use: mysqldump testdb > backup.sql
- Then: mysql testdb_copy < backup.sql

5. 🕐 TIMING CONSIDERATIONS:
- Large tables may take time to copy
- Consider indexing after data insertion for speed

6. 💾 STORAGE ENGINE:
- Default is InnoDB (supports transactions, foreign keys)
- MyISAM for read-heavy, no-transaction needs
 */
-- ============================================================================
-- 🚀 EXAMPLE: COMPLETE WORKFLOW
-- ============================================================================
/*
-- Sample workflow for creating and verifying a database:

-- 1️⃣ Create database
CREATE DATABASE IF NOT EXISTS my_app_db;

-- 2️⃣ Select database
USE my_app_db;

-- 3️⃣ Verify selection
SELECT DATABASE(); -- Shows current database

-- 4️⃣ Create a sample table
CREATE TABLE IF NOT EXISTS users (
id INT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) UNIQUE NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5️⃣ Verify table creation
SHOW TABLES;

-- 6️⃣ Check table structure
DESCRIBE users;

-- 7️⃣ Insert test data
INSERT INTO users (username, email) VALUES ('john_doe', 'john@example.com');

-- 8️⃣ Verify data
SELECT * FROM users;
 */
-- 🔄 Switch between databases without USE command
-- SELECT * FROM database_name.table_name;
-- 📊 Check current database
SELECT
     DATABASE();

-- 🕐 Show database creation time and collation
SHOW CREATE DATABASE testdb;

-- 📈 Check database size
SELECT
     table_schema                                           AS 'Database',
     SUM(data_length + index_length) / 1024 / 1024 AS 'Size (MB)'
FROM
     information_schema.tables
WHERE
     table_schema = 'testdb'
GROUP BY
     table_schema;

-- 🗑️ Cleanup all temporary databases
-- DROP DATABASE IF EXISTS testdb;
-- DROP DATABASE IF EXISTS testdb_copy;
-- ============================================================================
-- 🎉 END OF SCRIPT
-- ============================================================================