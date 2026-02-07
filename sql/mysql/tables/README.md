# Complete MySQL Locking Guide with Real Examples

## **PART 1: COMPREHENSIVE TABLE LOCKING EXAMPLES**

### **1.1 Basic Table Locking - READ vs WRITE**

```sql
-- ============= SESSION 1 =============
-- Create test table
CREATE TABLE test_accounts (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    balance DECIMAL(10,2)
) ENGINE=InnoDB;

INSERT INTO test_accounts VALUES 
(1, 'Alice', 1000.00),
(2, 'Bob', 2000.00),
(3, 'Charlie', 3000.00);

-- ============= EXAMPLE 1: READ LOCK =============
-- SESSION 1: Acquire READ lock
LOCK TABLES test_accounts READ;

-- DO: You can read from the table
SELECT * FROM test_accounts WHERE id = 1;  -- ✓ Works

-- DON'T: You CANNOT write to the table
UPDATE test_accounts SET balance = 1500 WHERE id = 1;  -- ✗ ERROR 1099
INSERT INTO test_accounts VALUES (4, 'David', 4000);  -- ✗ ERROR 1099

-- DON'T: You CANNOT access other tables (unless locked)
SELECT * FROM another_table;  -- ✗ ERROR 1100

-- Meanwhile in SESSION 2:
-- ✓ CAN read from the table (SELECT works)
-- ✗ CANNOT write (INSERT/UPDATE/DELETE will WAIT or timeout)

-- Release lock
UNLOCK TABLES;

-- ============= EXAMPLE 2: WRITE LOCK =============
-- SESSION 1: Acquire WRITE lock
LOCK TABLES test_accounts WRITE;

-- ✓ CAN read from the table
SELECT * FROM test_accounts;

-- ✓ CAN write to the table
UPDATE test_accounts SET balance = balance + 100 WHERE id = 1;
DELETE FROM test_accounts WHERE id = 3;

-- Meanwhile in SESSION 2:
-- ✗ CANNOT read (SELECT will WAIT)
-- ✗ CANNOT write (INSERT/UPDATE/DELETE will WAIT)

-- Release lock
UNLOCK TABLES;
```

### **1.2 Locking Multiple Tables**

```sql
-- ============= EXAMPLE 3: MULTIPLE TABLE LOCKS =============
CREATE TABLE transactions (
    id INT PRIMARY KEY,
    account_id INT,
    amount DECIMAL(10,2),
    FOREIGN KEY (account_id) REFERENCES test_accounts(id)
);

-- SESSION 1: Lock multiple tables
LOCK TABLES 
    test_accounts READ,
    transactions WRITE;

-- ✓ Can read from test_accounts
SELECT * FROM test_accounts;

-- ✓ Can write to transactions
INSERT INTO transactions VALUES (1, 1, 100.00);

-- ✗ Cannot write to test_accounts (READ lock)
UPDATE test_accounts SET balance = 2000 WHERE id = 1;  -- ERROR

-- ✗ Cannot access unlocked tables
SELECT * FROM other_table;  -- ERROR

UNLOCK TABLES;
```

## **PART 2: ROW-LEVEL LOCKING (INNODB ONLY)**

### **2.1 FOR UPDATE (Exclusive Row Lock)**

```sql
-- ============= EXAMPLE 4: FOR UPDATE =============
-- SESSION 1: Start transaction and lock specific rows
START TRANSACTION;

-- Lock row with id=1 exclusively
SELECT * FROM test_accounts WHERE id = 1 FOR UPDATE;

-- ✓ Can modify the locked row
UPDATE test_accounts SET balance = balance - 100 WHERE id = 1;

-- Meanwhile in SESSION 2:
START TRANSACTION;
-- ✗ This will WAIT (row is locked by SESSION 1):
SELECT * FROM test_accounts WHERE id = 1 FOR UPDATE;

-- ✓ This works (different row):
SELECT * FROM test_accounts WHERE id = 2 FOR UPDATE;

-- ✓ This also works (regular SELECT without FOR UPDATE):
SELECT * FROM test_accounts WHERE id = 1;

COMMIT;  -- Releases all locks
```

### **2.2 LOCK IN SHARE MODE (Shared Row Lock)**

```sql
-- ============= EXAMPLE 5: LOCK IN SHARE MODE =============
-- SESSION 1:
START TRANSACTION;
SELECT * FROM test_accounts WHERE id = 1 LOCK IN SHARE MODE;

-- Meanwhile in SESSION 2:
START TRANSACTION;
-- ✓ This works (shared lock allows other shared locks):
SELECT * FROM test_accounts WHERE id = 1 LOCK IN SHARE MODE;

-- ✗ This will WAIT (cannot get exclusive lock while shared lock exists):
SELECT * FROM test_accounts WHERE id = 1 FOR UPDATE;

-- SESSION 1:
COMMIT;  -- Releases shared lock

-- Now SESSION 2 can get exclusive lock
```

## **PART 3: COMPLETE SYNTAX REFERENCE**

### **3.1 Table Locking Syntax**

```sql
-- Basic Syntax
LOCK TABLES 
    table_name [READ | WRITE] 
    [, table_name2 [READ | WRITE]] 
    ...;

-- Examples
LOCK TABLES employees READ;
LOCK TABLES orders WRITE;
LOCK TABLES customers READ, orders WRITE, products READ;

-- With alias (if you use aliases in queries)
LOCK TABLES employees AS e READ, orders AS o WRITE;
-- Then you must use the alias:
SELECT * FROM e WHERE id = 1;  -- ✓
SELECT * FROM employees WHERE id = 1;  -- ✗ ERROR 1100

-- Unlock syntax
UNLOCK TABLES;  -- Releases ALL table locks in current session
```

### **3.2 Row Locking Syntax**

```sql
-- In transactions only
START TRANSACTION;
-- or
BEGIN;
-- or
SET autocommit = 0;

-- Exclusive lock (for writes)
SELECT ... FROM table_name WHERE ... FOR UPDATE;
-- Examples:
SELECT * FROM accounts WHERE id = 1 FOR UPDATE;
SELECT * FROM orders WHERE status = 'pending' FOR UPDATE;

-- Shared lock (for reads)
SELECT ... FROM table_name WHERE ... LOCK IN SHARE MODE;
-- Example:
SELECT * FROM products WHERE stock > 0 LOCK IN SHARE MODE;

-- Lock with NOWAIT (MySQL 8.0+)
SELECT * FROM table_name WHERE ... FOR UPDATE NOWAIT;
-- Returns error immediately if lock cannot be acquired

-- Lock with SKIP LOCKED (MySQL 8.0+)
SELECT * FROM table_name WHERE ... FOR UPDATE SKIP LOCKED;
-- Skips already locked rows

-- Release locks
COMMIT;      -- Makes changes permanent, releases locks
ROLLBACK;    -- Undoes changes, releases locks
```

## **PART 4: DO's AND DON'Ts WITH EXAMPLES**

### **4.1 DO's**

```sql
-- DO: Keep transactions short
START TRANSACTION;
SELECT * FROM accounts WHERE id = 1 FOR UPDATE;
-- Do minimal work here
UPDATE accounts SET balance = balance - 100 WHERE id = 1;
COMMIT;  -- ✓ Release lock quickly

-- DO: Lock tables in consistent order to prevent deadlocks
-- Session 1: Lock A then B
LOCK TABLES table_a WRITE, table_b WRITE;
-- Session 2: ALSO Lock A then B (not B then A)
LOCK TABLES table_a WRITE, table_b WRITE;

-- DO: Use appropriate indexes for row locks
-- Without index (locks entire table):
SELECT * FROM accounts WHERE name = 'John' FOR UPDATE;  -- ✗ Bad if no index on name
-- With index (locks only specific rows):
SELECT * FROM accounts WHERE id = 1 FOR UPDATE;  -- ✓ Good (id is primary key)

-- DO: Check lock status before long operations
SHOW PROCESSLIST;  -- See if anyone else is locking tables
```

### **4.2 DON'Ts**

```sql
-- DON'T: Hold locks during user input
START TRANSACTION;
SELECT * FROM orders WHERE id = 100 FOR UPDATE;
-- ✗ BAD: Waiting for user input while holding lock
-- User thinks for 30 seconds...
UPDATE orders SET status = 'processed' WHERE id = 100;
COMMIT;

-- DON'T: Mix table locks and transactions
LOCK TABLES accounts WRITE;
START TRANSACTION;  -- ✗ Strange behavior!
-- Use either LOCK TABLES OR transactions, not both

-- DON'T: Forget to unlock or commit
LOCK TABLES accounts WRITE;
-- Do work...
-- ✗ OOPS! Forgot UNLOCK TABLES;
-- Connection stays locked until disconnect!

-- DON'T: Lock more than needed
LOCK TABLES huge_table WRITE;  -- ✗ Locks millions of rows
-- Instead:
START TRANSACTION;
SELECT * FROM huge_table WHERE id = 12345 FOR UPDATE;  -- ✓ Locks only one row
```

## **PART 5: PRACTICAL BANKING SYSTEM EXAMPLE**

```sql
-- ============= COMPLETE BANKING TRANSACTION =============
-- Session 1: Transfer $100 from account 1 to account 2

START TRANSACTION;

-- Step 1: Lock both accounts (prevent concurrent modifications)
SELECT * FROM test_accounts WHERE id IN (1, 2) FOR UPDATE;

-- Step 2: Verify balances
SELECT id, balance FROM test_accounts WHERE id = 1;
-- Assume balance = 1000

-- Step 3: Check if sufficient funds
SELECT balance >= 100 FROM test_accounts WHERE id = 1;
-- Returns 1 (true) or 0 (false)

-- Step 4: Perform transfer
UPDATE test_accounts SET balance = balance - 100 WHERE id = 1;
UPDATE test_accounts SET balance = balance + 100 WHERE id = 2;

-- Step 5: Record transaction
INSERT INTO transactions (account_id, amount, type) 
VALUES (1, -100, 'transfer'), (2, 100, 'transfer');

-- Step 6: Verify (can read without locking)
SELECT SUM(balance) FROM test_accounts;  -- Should be same as before

-- Step 7: Release all locks
COMMIT;

-- ============= WHAT HAPPENS IN OTHER SESSIONS? =============
-- Session 2 tries to access same accounts:

-- This will WAIT until Session 1 commits:
START TRANSACTION;
SELECT * FROM test_accounts WHERE id = 1 FOR UPDATE;  -- ✗ Waits

-- This works immediately (different account):
SELECT * FROM test_accounts WHERE id = 3 FOR UPDATE;  -- ✓ Works

-- This works immediately (no lock needed):
SELECT * FROM test_accounts WHERE id = 1;  -- ✓ Works (read uncommitted in READ COMMITTED isolation)
```

## **PART 6: PROS AND CONS COMPARISON**

### **Table Locks vs Row Locks**

| Aspect | Table Locks | Row Locks |
|--------|-------------|-----------|
| **Granularity** | Entire table | Individual rows |
| **Concurrency** | Low (blocks others) | High (allows parallel access) |
| **Overhead** | Low | Higher (manages multiple locks) |
| **Deadlocks** | Rare | More common |
| **Syntax** | Simple | More complex (needs transactions) |
| **Storage Engine** | All engines | InnoDB only |

### **When to Use Which?**

```sql
-- USE TABLE LOCKS WHEN:
-- 1. Doing bulk operations on entire table
LOCK TABLES big_table WRITE;  -- ✓ Good for: DELETE FROM big_table WHERE date < '2020-01-01';
UNLOCK TABLES;

-- 2. MyISAM tables (no row locking)
LOCK TABLES myisam_table WRITE;  -- MyISAM only supports table locks

-- 3. Simple, quick operations
LOCK TABLES config READ;  -- ✓ Read config values
SELECT * FROM config;
UNLOCK TABLES;

-- USE ROW LOCKS WHEN:
-- 1. Working with specific rows
START TRANSACTION;
SELECT * FROM users WHERE id = 123 FOR UPDATE;  -- ✓ Only locks one user
UPDATE users SET last_login = NOW() WHERE id = 123;
COMMIT;

-- 2. High concurrency needed
-- Multiple users can edit different orders simultaneously

-- 3. Complex transactions involving multiple tables
```

## **PART 7: TROUBLESHOOTING LOCKS**

### **7.1 Checking Current Locks**

```sql
-- See all table locks
SHOW OPEN TABLES WHERE In_use > 0;

-- See current processes (look for 'Locked' state)
SHOW PROCESSLIST;

-- For InnoDB locks (MySQL 5.7+)
SELECT * FROM information_schema.INNODB_LOCKS;
SELECT * FROM information_schema.INNODB_LOCK_WAITS;

-- See which transactions are waiting
SELECT 
    r.trx_id waiting_trx_id,
    r.trx_mysql_thread_id waiting_thread,
    TIMESTAMPDIFF(SECOND, r.trx_wait_started, NOW()) wait_time,
    b.trx_id blocking_trx_id,
    b.trx_mysql_thread_id blocking_thread
FROM information_schema.INNODB_LOCK_WAITS w
INNER JOIN information_schema.INNODB_TRX b ON b.trx_id = w.blocking_trx_id
INNER JOIN information_schema.INNODB_TRX r ON r.trx_id = w.requesting_trx_id;
```

### **7.2 Killing Locked Sessions**

```sql
-- Find the blocking process
SHOW PROCESSLIST;
-- Note the Id of the blocking process

-- Kill it (as admin)
KILL 12345;  -- Replace with actual process ID
```

### **7.3 Timeout Configuration**

```sql
-- Set lock wait timeout (default 50 seconds)
SET SESSION innodb_lock_wait_timeout = 10;  -- Wait 10 seconds max

-- Set global timeout
SET GLOBAL innodb_lock_wait_timeout = 30;

-- Check current settings
SHOW VARIABLES LIKE 'innodb_lock_wait_timeout';
```

## **PART 8: COMPLETE WORKFLOW EXAMPLE**

```sql
-- ============= SAFE DATA MIGRATION WORKFLOW =============
-- Scenario: Migrate old data to archive table

-- Step 1: Lock tables (minimize disruption)
LOCK TABLES 
    active_orders READ,      -- Allow reads during migration
    archived_orders WRITE;   -- Only we write to archive

-- Step 2: Copy old data (2019 and earlier)
INSERT INTO archived_orders 
SELECT * FROM active_orders WHERE order_date < '2020-01-01';

-- Step 3: Verify copy
SELECT COUNT(*) FROM active_orders WHERE order_date < '2020-01-01';
SELECT COUNT(*) FROM archived_orders WHERE order_date < '2020-01-01';

-- Step 4: Switch to WRITE lock on active_orders
UNLOCK TABLES;
LOCK TABLES active_orders WRITE, archived_orders WRITE;

-- Step 5: Delete migrated data
DELETE FROM active_orders WHERE order_date < '2020-01-01';

-- Step 6: Verify totals
SELECT (SELECT COUNT(*) FROM active_orders) + 
       (SELECT COUNT(*) FROM archived_orders) AS total_orders;

-- Step 7: Release all locks
UNLOCK TABLES;

-- Step 8: Optimize tables if needed
OPTIMIZE TABLE active_orders;
```

## **PART 9: COMMON ERRORS AND SOLUTIONS**

```sql
-- ERROR 1099: Table 'x' was locked with a READ lock and can't be updated
-- Solution: Use WRITE lock or don't update
LOCK TABLES mytable READ;  -- Can't update
-- Change to:
LOCK TABLES mytable WRITE;  -- Can update

-- ERROR 1100: Table 'x' was not locked with LOCK TABLES
-- Solution: Lock all tables you'll access
LOCK TABLES table1 WRITE;
SELECT * FROM table2;  -- ERROR!
-- Fix:
LOCK TABLES table1 WRITE, table2 READ;

-- ERROR 1205: Lock wait timeout exceeded
-- Solution 1: Increase timeout
SET SESSION innodb_lock_wait_timeout = 120;
-- Solution 2: Check for long-running transactions
SHOW PROCESSLIST;
-- Solution 3: Optimize queries to use indexes

-- ERROR 1213: Deadlock found
-- Solution 1: Retry the transaction
START TRANSACTION;
-- Your queries...
-- If deadlock occurs, catch it and retry

-- Solution 2: Access tables in consistent order
-- All sessions should lock tables in same order: A then B then C
```

## **PART 10: BEST PRACTICES SUMMARY**

1. **Use row locks** for high-concurrency applications
2. **Use table locks** for maintenance/backup operations
3. **Always release locks** (UNLOCK TABLES or COMMIT/ROLLBACK)
4. **Keep lock duration short**
5. **Use transactions** for multi-statement operations
6. **Monitor lock waits** regularly
7. **Use appropriate indexes** for row locking
8. **Test locking strategy** in development environment
9. **Consider isolation levels** (READ COMMITTED vs REPEATABLE READ)
10. **Have a timeout strategy** for handling lock waits

```sql
-- Example of good practice
SET SESSION innodb_lock_wait_timeout = 30;
START TRANSACTION;

-- Use index for efficient locking
SELECT * FROM accounts WHERE id = 100 FOR UPDATE;

-- Do work quickly
UPDATE accounts SET balance = balance - 50 WHERE id = 100;

-- Commit quickly
COMMIT;
```

This comprehensive guide covers all major aspects of MySQL locking. Practice these examples in a test environment to understand the behavior fully. Remember that locking behavior can vary based on storage engine, isolation level, and MySQL version.