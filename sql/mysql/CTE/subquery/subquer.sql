/* 🔍 MYSQL SUBQUERY ERROR: "Subquery returns more than 1 row" COMPREHENSIVE GUIDE
============================================================================
This guide explains the subquery error and provides multiple solutions**
============================================================================
 */
-- ============================================================================
-- 🎯 SECTION 1: UNDERSTANDING THE PROBLEM
-- ============================================================================
-- 📊 The Original Failing Query:
--    This query fails with "Subquery returns more than 1 row"
--    Reason: The subquery now returns TWO employee_ids (1 and 3), 
--    but the = operator expects ONLY ONE value
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ============================================================================
-- 📋 SECTION 2: DATABASE SCHEMA & SAMPLE DATA
-- ============================================================================
-- 🏗️ Create the attire database and tables
CREATE DATABASE IF NOT EXISTS attire;

USE attire;

SHOW TABLES;

-- 👔 Create employee table
CREATE TABLE
    employee (
        employee_id INT PRIMARY KEY,
        employee_name VARCHAR(100),
        position_name VARCHAR(100)
    );

-- 🎩 Create wardrobe table
CREATE TABLE
    wardrobe (
        employee_id INT,
        hat_size DECIMAL(4, 2),
        FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
    );

-- 📥 Insert sample data
INSERT INTO
    employee (employee_id, employee_name, position_name)
VALUES
    (1, 'Benedict', 'Pope'),
    (2, 'Garth', 'Singer'),
    (3, 'Francis', 'Pope');

-- ← NEWLY ADDED EMPLOYEE
INSERT INTO
    wardrobe (employee_id, hat_size)
VALUES
    (1, 8.25),
    (2, 7.50),
    (3, 6.75);

-- 🔍 Verify the data
SELECT
    'EMPLOYEE TABLE:' AS TABLE_NAME;

SELECT
    *
FROM
    employee
ORDER BY
    employee_id;

SELECT
    'WARDROBE TABLE:' AS TABLE_NAME;

SELECT
    *
FROM
    wardrobe
ORDER BY
    employee_id;

-- ============================================================================
-- 🔍 SECTION 3: WHY THE ERROR OCCURS
-- ============================================================================
-- ❌ The Problematic Subquery:
--    Returns TWO rows because there are TWO Popes in the database
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ✅ Expected Output:
-- +-------------+
-- | employee_id |
-- +-------------+
-- |           1 |
-- |           3 |
-- +-------------+
-- ❌ The Main Query Fails Because:
--    WHERE employee_id = (1, 3) ← INVALID! Can't compare one value to multiple values
--    The = operator expects a SINGLE value, not a LIST of values
-- ============================================================================
-- 🛠️ SECTION 4: FIX #1 - USE IN OPERATOR (Most Common Fix)
-- ============================================================================
-- ✅ Fix using IN operator (handles multiple values)
--    IN checks if employee_id matches ANY value in the subquery result
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id IN (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ✅ Result: Returns BOTH Popes' hat sizes
-- +-------------+----------+
-- | employee_id | hat_size |
-- +-------------+----------+
-- |           1 |     8.25 |
-- |           3 |     6.75 |
-- +-------------+----------+
-- ============================================================================
-- 🛠️ SECTION 5: FIX #2 - USE JOIN INSTEAD OF SUBQUERY (Better Performance)
-- ============================================================================
-- ✅ Fix using INNER JOIN
--    JOINs are often more efficient than subqueries in MySQL
SELECT
    w.employee_id,
    w.hat_size
FROM
    wardrobe w
    INNER JOIN employee e ON w.employee_id = e.employee_id
WHERE
    e.position_name = 'Pope';

-- ✅ Alternative with explicit JOIN syntax
SELECT
    w.employee_id,
    w.hat_size
FROM
    wardrobe w
    JOIN employee e USING (employee_id)
WHERE
    e.position_name = 'Pope';

-- ============================================================================
-- 🛠️ SECTION 6: FIX #3 - USE EXISTS (Good for Large Datasets)
-- ============================================================================
-- ✅ Fix using EXISTS
--    EXISTS is efficient when you need to check for existence
SELECT
    employee_id,
    hat_size
FROM
    wardrobe w
WHERE
    EXISTS (
        SELECT
            1
        FROM
            employee e
        WHERE
            e.employee_id = w.employee_id AND
            e.position_name = 'Pope'
    );

-- ============================================================================
-- 🛠️ SECTION 7: FIX #4 - USE LIMIT 1 (If Only One Pope Needed)
-- ============================================================================
-- ✅ Fix using LIMIT 1 (if business logic requires only one Pope)
--    This returns ONLY ONE Pope's hat size (the first one found)
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
        LIMIT
            1 -- ← Forces subquery to return only one row
    );

-- ⚠️ Warning: LIMIT without ORDER BY is non-deterministic!
--    It might return different results on different executions
-- ✅ Better: Use ORDER BY for deterministic result
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
        ORDER BY
            employee_id -- Most recent? Oldest? Depends on business logic
        LIMIT
            1
    );

-- ============================================================================
-- 🛠️ SECTION 8: FIX #5 - USE AGGREGATE FUNCTIONS (Single Value Return)
-- ============================================================================
-- ✅ Fix using MAX() - returns the highest employee_id
--    Useful if you want the "most recent" or "highest" Pope
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            MAX(employee_id)
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ✅ Fix using MIN() - returns the lowest employee_id
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            MIN(employee_id)
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ✅ Fix using GROUP_CONCAT() - returns comma-separated list
--    (Then you'd need to handle this differently in main query)
SELECT
    GROUP_CONCAT(employee_id) AS pope_ids
FROM
    employee
WHERE
    position_name = 'Pope';

-- ============================================================================
-- 🛠️ SECTION 9: FIX #6 - USE ANY/SOME OPERATORS
-- ============================================================================
-- ✅ Fix using = ANY (synonym for IN)
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = ANY (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ✅ Fix using = SOME (same as = ANY)
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = SOME (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ============================================================================
-- 📊 SECTION 10: COMPARING ALL SOLUTIONS
-- ============================================================================
-- 🔍 Performance Comparison Setup
-- Create indexes for fair comparison
CREATE INDEX idx_position ON employee (position_name);

CREATE INDEX idx_emp_id ON wardrobe (employee_id);

-- 📈 Method 1: IN Operator (Subquery)
EXPLAIN
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id IN (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- 📈 Method 2: JOIN
EXPLAIN
SELECT
    w.employee_id,
    w.hat_size
FROM
    wardrobe w
    INNER JOIN employee e ON w.employee_id = e.employee_id
WHERE
    e.position_name = 'Pope';

-- 📈 Method 3: EXISTS
EXPLAIN
SELECT
    employee_id,
    hat_size
FROM
    wardrobe w
WHERE
    EXISTS (
        SELECT
            1
        FROM
            employee e
        WHERE
            e.employee_id = w.employee_id AND
            e.position_name = 'Pope'
    );

-- ============================================================================
-- 🎯 SECTION 11: REAL-WORLD SCENARIOS
-- ============================================================================
-- 🎭 SCENARIO 1: Department Heads
--    Multiple employees can be department heads
--    Original query would fail if multiple employees have same position
CREATE TABLE
    department_heads (
        dept_id INT,
        employee_id INT,
        PRIMARY KEY (dept_id, employee_id)
    );

INSERT INTO
    department_heads
VALUES
    (1, 101),
    (1, 102),
    (2, 103);

-- ❌ Would fail if multiple heads in same department
SELECT
    *
FROM
    employees
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            department_heads
        WHERE
            dept_id = 1
    );

-- ✅ Fixed with IN
SELECT
    *
FROM
    employees
WHERE
    employee_id IN (
        SELECT
            employee_id
        FROM
            department_heads
        WHERE
            dept_id = 1
    );

-- 🎭 SCENARIO 2: Product Categories
--    Multiple products in same category
CREATE TABLE
    products (product_id INT PRIMARY KEY, category_id INT);

-- ❌ Would fail if category has multiple products
SELECT
    *
FROM
    inventory
WHERE
    product_id = (
        SELECT
            product_id
        FROM
            products
        WHERE
            category_id = 5
    );

-- ✅ Fixed with JOIN
SELECT
    i.*
FROM
    inventory i
    JOIN products p ON i.product_id = p.product_id
WHERE
    p.category_id = 5;

-- ============================================================================
-- ⚠️ SECTION 12: WHY DID IT USED TO WORK?
-- ============================================================================
-- 🔍 Historical Context:
--    The query worked when there was ONLY ONE Pope in the database
--    After adding employee 3 (Francis Pope), the subquery returns 2 rows
-- 📅 Timeline:
-- 1. INITIAL STATE: Only Benedict (employee_id 1) was Pope
--    Subquery returned: [1] ✓
--    WHERE employee_id = 1 ✓ (works fine)
-- 2. CURRENT STATE: Added Francis (employee_id 3) as Pope
--    Subquery returns: [1, 3] ✗
--    WHERE employee_id = (1, 3) ✗ (ERROR!)
-- 🔍 Demonstration of the old state:
-- Remove Francis temporarily to show original working state
DELETE FROM employee
WHERE
    employee_id = 3;

-- ✅ Query works when only one Pope exists
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- 🔄 Restore Francis
INSERT INTO
    employee (employee_id, employee_name, position_name)
VALUES
    (3, 'Francis', 'Pope');

-- ❌ Now fails with multiple Popes
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id = (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- ============================================================================
-- 🛡️ SECTION 13: PREVENTIVE MEASURES
-- ============================================================================
-- 🔒 Method 1: Add UNIQUE constraint if business rule allows only one Pope
--    This prevents the error at database level
ALTER TABLE employee
ADD CONSTRAINT unique_pope UNIQUE (position_name)
WHERE
    position_name = 'Pope';

-- MySQL doesn't support filtered UNIQUE constraints directly
-- 🔒 Method 2: Use triggers to enforce single Pope
DELIMITER / /
CREATE TRIGGER enforce_single_pope BEFORE
INSERT
    ON employee FOR EACH ROW BEGIN IF NEW.position_name = 'Pope' THEN IF EXISTS (
        SELECT
            1
        FROM
            employee
        WHERE
            position_name = 'Pope'
    ) THEN SIGNAL SQLSTATE '45000'
SET
    MESSAGE_TEXT = 'Only one Pope allowed in the database';

END IF;

END IF;

END / / DELIMITER;

-- 🔒 Method 3: Use application logic to check before insert
--    Check count before adding new Pope
SELECT
    COUNT(*)
FROM
    employee
WHERE
    position_name = 'Pope';

-- 🔒 Method 4: Use views for safe queries
CREATE VIEW
    pope_wardrobe AS
SELECT
    w.employee_id,
    w.hat_size
FROM
    wardrobe w
WHERE
    w.employee_id IN (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- Now users query the view instead
SELECT
    *
FROM
    pope_wardrobe;

-- ============================================================================
-- 📚 SECTION 14: COMPREHENSIVE FIX RECOMMENDATION
-- ============================================================================
/*
🎯 RECOMMENDED FIX: Use IN operator or JOIN

WHY?
1. ✅ Handles multiple rows in subquery
2. ✅ Clear intent - easy to understand
3. ✅ Good performance with proper indexes
4. ✅ Standard SQL - works across databases
5. ✅ Returns ALL matching rows (likely what business wants)

📝 IMPLEMENTATION:
 */
-- Option A: IN operator (Most intuitive fix)
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id IN (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

-- Option B: JOIN (Better for complex queries)
SELECT
    w.employee_id,
    w.hat_size
FROM
    wardrobe w
    JOIN employee e ON w.employee_id = e.employee_id
WHERE
    e.position_name = 'Pope';

/*
⚡ PERFORMANCE NOTES:
- For small datasets: Both are fine
- For large datasets: JOIN is often faster
- Always test with EXPLAIN on your data
- Add indexes on join/where columns
 */
-- ============================================================================
-- 🧪 SECTION 15: TESTING ALL SOLUTIONS
-- ============================================================================
-- 🧹 Clean test environment
DROP TABLE IF EXISTS test_results;

CREATE TABLE
    test_results (
        solution_name VARCHAR(50),
        query_sql TEXT,
        result_count INT,
        execution_time_ms DECIMAL(10, 2),
        notes VARCHAR(200)
    );

-- 🕒 Test Function
DELIMITER / /
CREATE PROCEDURE test_solution (
    IN solution_name VARCHAR(50),
    IN query_sql TEXT,
    IN notes VARCHAR(200)
) BEGIN DECLARE start_time BIGINT;

DECLARE end_time BIGINT;

DECLARE result_cnt INT;

SET
    start_time = UNIX_TIMESTAMP(UTC_TIMESTAMP(6)) * 1000000 + MICROSECOND(UTC_TIMESTAMP(6));

-- Create temporary table to capture results
CREATE TEMPORARY TABLE
    temp_result AS query_sql;

SET
    result_cnt = (
        SELECT
            COUNT(*)
        FROM
            temp_result
    );

SET
    end_time = UNIX_TIMESTAMP(UTC_TIMESTAMP(6)) * 1000000 + MICROSECOND(UTC_TIMESTAMP(6));

INSERT INTO
    test_results (
        solution_name,
        query_sql,
        result_count,
        execution_time_ms,
        notes
    )
VALUES
    (
        solution_name,
        query_sql,
        result_cnt,
        (end_time - start_time) / 1000,
        notes
    );

DROP TEMPORARY TABLE temp_result;

END / / DELIMITER;

-- 🧪 Run tests (Note: In practice, you'd use a proper benchmarking tool)
/*
-- Test each solution
CALL test_solution('IN Operator', 
'SELECT employee_id, hat_size FROM wardrobe WHERE employee_id IN (SELECT employee_id FROM employee WHERE position_name = ''Pope'')',
'Standard fix, handles multiple rows');

CALL test_solution('JOIN',
'SELECT w.employee_id, w.hat_size FROM wardrobe w JOIN employee e ON w.employee_id = e.employee_id WHERE e.position_name = ''Pope''',
'Better for performance, more explicit');

-- View test results
SELECT * FROM test_results ORDER BY execution_time_ms;
 */
-- ============================================================================
-- 📋 SECTION 16: QUICK REFERENCE GUIDE
-- ============================================================================
/*
❌ PROBLEM: "Subquery returns more than 1 row"
- Occurs when subquery returns multiple rows but main query expects single value
- Common with = operator in WHERE clause

✅ SOLUTIONS (in order of preference):

1. IN Operator - Use when you want ALL matching rows
WHERE employee_id IN (subquery)

2. JOIN - Use for better performance and readability
FROM table1 JOIN table2 ON condition

3. EXISTS - Use when checking for existence
WHERE EXISTS (subquery)

4. LIMIT 1 - Use when you need ONLY ONE row
WHERE employee_id = (SELECT ... LIMIT 1)

5. Aggregate Function - Use when you need specific single value
WHERE employee_id = (SELECT MAX(employee_id) FROM ...)

6. ANY/SOME - Alternative to IN
WHERE employee_id = ANY (subquery)

🎯 WHEN TO USE WHICH:
- Use IN/ANY for "is in this list" scenarios
- Use JOIN for complex queries with multiple conditions
- Use EXISTS for "if exists" checks
- Use LIMIT 1 when business requires single result
- Use aggregates when you need min/max/avg

⚡ PERFORMANCE TIPS:
- Add indexes on columns used in WHERE and JOIN
- Use EXPLAIN to analyze query plans
- Test with production-like data volumes
- Consider materialized views for complex subqueries
 */
-- ============================================================================
-- 🚨 SECTION 17: COMMON PITFALLS TO AVOID
-- ============================================================================
/*
⚠️ PITFALL 1: Assuming single row when multiple possible
- Always consider: "What if multiple rows match?"
- Use IN instead of = for potentially multiple values

⚠️ PITFALL 2: Non-deterministic LIMIT
- LIMIT without ORDER BY can return different rows
- Always specify ORDER BY with LIMIT

⚠️ PITFALL 3: Ignoring NULL values
- IN (NULL, 1, 2) won't match NULL values
- Use COALESCE or handle NULLs separately

⚠️ PITFALL 4: Performance with large datasets
- Correlated subqueries can be slow
- Consider rewriting as JOINs

⚠️ PITFALL 5: Not testing edge cases
- Test with 0, 1, and multiple rows
- Test with NULL values
- Test with duplicate values
 */
-- ============================================================================
-- 🔄 SECTION 18: MIGRATION SCRIPT FOR EXISTING CODE
-- ============================================================================
-- 📝 Script to find and fix similar issues in your database
/*
-- Find all potentially problematic = comparisons with subqueries
SELECT 
ROUTINE_SCHEMA,
ROUTINE_NAME,
ROUTINE_TYPE
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_DEFINITION LIKE '%= (SELECT%'
OR ROUTINE_DEFINITION LIKE '%= (select%';

-- Find in stored procedures
SELECT 
SPECIFIC_NAME,
ROUTINE_DEFINITION
FROM INFORMATION_SCHEMA.ROUTINES
WHERE ROUTINE_TYPE = 'PROCEDURE'
AND (ROUTINE_DEFINITION LIKE '%= (SELECT%'
OR ROUTINE_DEFINITION LIKE '%= (select%');
 */
-- ============================================================================
-- 🧹 SECTION 19: CLEANUP
-- ============================================================================
-- 🗑️ Clean up test database
DROP DATABASE IF EXISTS attire;

-- ============================================================================
-- 🎉 SECTION 20: FINAL ANSWER TO THE ORIGINAL QUESTION
-- ============================================================================
/*
❓ QUESTION: "How would you fix the query? Why do you think the query used to work?"

🎯 ANSWER:

HOW TO FIX:
Use the IN operator instead of =:
 */
SELECT
    employee_id,
    hat_size
FROM
    wardrobe
WHERE
    employee_id IN (
        SELECT
            employee_id
        FROM
            employee
        WHERE
            position_name = 'Pope'
    );

/*
Alternatively, use a JOIN:
 */
SELECT
    w.employee_id,
    w.hat_size
FROM
    wardrobe w
    JOIN employee e ON w.employee_id = e.employee_id
WHERE
    e.position_name = 'Pope';

/*
WHY IT USED TO WORK:
1. ✅ Originally, only ONE employee (Benedict, employee_id 1) had position_name = 'Pope'
2. ✅ The subquery returned a SINGLE value: [1]
3. ✅ The = operator compared: WHERE employee_id = 1 ✓ (valid comparison)

WHY IT NOW FAILS:
1. ❌ After adding Francis (employee_id 3) as Pope, there are TWO Popes
2. ❌ The subquery now returns TWO values: [1, 3]
3. ❌ The = operator tries: WHERE employee_id = (1, 3) ✗ (INVALID - can't compare to list)

🎯 ROOT CAUSE:
The query was designed with the assumption that only one Pope could exist.
When business rules changed (adding a second Pope), the query logic broke.

🛡️ PREVENTIVE MEASURE:
1. Use IN instead of = for subqueries that might return multiple rows
2. Add database constraints if business rules limit cardinality
3. Write defensive queries that handle multiple results
4. Document assumptions about data cardinality
 */
-- ============================================================================
-- 🏁 END OF SUBQUERY ERROR FIXING GUIDE
-- ============================================================================