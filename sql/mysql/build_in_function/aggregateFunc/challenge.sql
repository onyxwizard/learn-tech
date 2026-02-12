/**
 * @author onyxwizard
 * @date 06-02-2026
 */
-- 1. Show available databases to verify we're working in the right environment
SHOW DATABASES;

-- Expected: Should see 'sales_demo' and 'company_demo' in the list
-- 2. Switch to the sales_demo database for these challenges
USE sales_demo;

-- Comment: This sets sales_demo as the active database for subsequent queries
-- 3. List all tables in the current database
SHOW TABLES;

-- Expected: Should see 'sales' table
-- 4. Examine the sales table structure to understand available columns
DESC sales;

-- Comment: This shows column names, data types, and constraints
-- Helpful for knowing what fields we can use in our queries
-- Objective: Count total sales transactions in the 'Electronics' category
-- COUNT(*) counts all rows, COUNT(column) counts non-NULL values in that column
-- Use COUNT(*) when you want to count rows regardless of NULL values
SELECT
  COUNT(*) AS electronics_transaction_count -- Counts all rows in the result set
FROM
  sales -- From the sales table
WHERE
  category = 'Electronics';

-- Filter to only include electronics category
-- Note: COUNT(*) is generally preferred for counting rows
-- COUNT(sale_amount) would work too, but COUNT(*) is more efficient here
-- Objective: Find highest and lowest sale amounts in 'Furniture' category
-- Use MIN() for minimum value, MAX() for maximum value
-- Combine both in a single query for efficiency
SELECT
  MIN(sale_amount) AS minimum_furniture_sale, -- Gets smallest sale amount
  MAX(sale_amount) AS maximum_furniture_sale -- Gets largest sale amount
FROM
  sales -- From the sales table
WHERE
  category = 'Furniture';

-- Filter to only furniture category
-- Note: MIN and MAX ignore NULL values, but sale_amount shouldn't have NULLs
-- Objective: Calculate total revenue from ALL sales (all categories)
-- Use SUM() to add up all values in the sale_amount column
-- No WHERE clause needed since we want total for entire table
SELECT
  SUM(sale_amount) AS total_revenue_all_sales -- Sum of all sale amounts
FROM
  sales;

-- From the entire sales table, no filtering needed
-- Note: This is different from filtering by category like in previous queries
-- Objective: Show total revenue and average sale for each category
-- Use GROUP BY to create one row per category
-- Use SUM() for total revenue, AVG() for average sale amount
-- ORDER BY to sort results by highest revenue first (descending order)
SELECT
  category, -- The column we're grouping by
  SUM(sale_amount) AS total_revenue, -- Total sales for each category
  AVG(sale_amount) AS average_sale_amount -- Average sale per transaction
FROM
  sales -- From the sales table
GROUP BY
  category -- Group results by category (one row per category)
ORDER BY
  total_revenue DESC;

-- Sort by total_revenue from highest to lowest
-- Note: ORDER BY references the alias 'total_revenue' without quotes
-- Using quotes would make it a string literal, not a column reference
-- Objective: For each region, calculate various metrics
-- Only show regions with more than 1 transaction
-- Use GROUP BY for aggregation by region
-- Use HAVING to filter groups (not individual rows)
SELECT
  region, -- The column we're grouping by
  COUNT(*) AS transaction_count, -- Count of transactions per region
  SUM(quantity) AS total_quantity_sold, -- Sum of all quantities per region
  AVG(sale_amount) AS average_sale_amount -- Average sale amount per region
FROM
  sales -- From the sales table
GROUP BY
  region -- Group results by region (one row per region)
HAVING
  COUNT(*) > 1;

-- Filter: only keep regions with more than 1 transaction
-- Note: HAVING is used after GROUP BY to filter grouped results
-- WHERE would filter individual rows BEFORE grouping
-- HAVING COUNT(region) > 1 would also work but COUNT(*) is more common
-- Objective: Show daily sales totals for days exceeding $500
-- Group by sale_date to get daily aggregates
-- Use HAVING to filter days by total sales amount
SELECT
  sale_date, -- Group by date to get daily totals
  SUM(sale_amount) AS daily_sales_total -- Total sales for each day
FROM
  sales -- From the sales table
GROUP BY
  sale_date -- Group by date (one row per day)
HAVING
  SUM(sale_amount) > 500 -- Filter: only days with total sales > $500
ORDER BY
  sale_date;

-- Optional: sort chronologically for readability
-- Note: HAVING can reference aggregate functions directly
-- Could also use: HAVING daily_sales_total > 500 (using alias)
-- Objective: List products sold more than once with various metrics
-- Group by product_name to analyze each product
-- Use HAVING to filter products sold multiple times
-- Multiple aggregate functions for comprehensive analysis
SELECT
  product_name, -- Group by product name
  SUM(sale_amount) AS total_revenue, -- Total money from this product
  COUNT(*) AS times_sold, -- How many times this product was sold
  AVG(quantity) AS average_quantity_per_sale -- Average quantity per transaction
FROM
  sales -- From the sales table
GROUP BY
  product_name -- One row per product
HAVING
  COUNT(*) > 1 -- Only include products sold more than once
ORDER BY
  total_revenue DESC;

-- Sort by highest revenue first for ranking
-- Note: Could also add ROUND() to format numbers: ROUND(AVG(quantity), 2)
-- Objective: Show each category's revenue, percentage of total, and difference from avg
-- Requires a subquery to get overall total for percentage calculation
-- Three different aggregate calculations in one query
SELECT
  category, -- Group by category
  SUM(sale_amount) AS category_revenue, -- Total revenue per category
  -- Calculate percentage of total revenue
  -- Subquery gets overall total, then we calculate percentage
  ROUND(
    (
      SUM(sale_amount) / (
        SELECT
          SUM(sale_amount)
        FROM
          sales
      )
    ) * 100,
    2
  ) AS percentage_of_total_revenue,
  -- Difference from category average revenue
  -- SUM(sale_amount) gives category total, AVG(sale_amount) gives average per transaction
  -- To get difference from category's own average transaction, we'd need a different approach
  -- This actually shows: category total - average transaction amount in that category
  SUM(sale_amount) - AVG(sale_amount) AS diff_from_category_avg_transaction
  -- Note: If you meant difference from OVERALL average category revenue, you'd need:
  -- SUM(sale_amount) - (SELECT AVG(category_total) FROM (SELECT SUM(sale_amount) AS category_total FROM sales GROUP BY category) AS cat_totals)
  -- But that's more complex
FROM
  sales -- From the sales table
GROUP BY
  category;

-- One row per category
-- Objective: Create a running total of sales by date
-- Window functions allow calculations across rows without collapsing them
-- OVER() clause defines the window/range for the calculation
-- Method 1: Simple window function (if your MySQL version supports it - 8.0+)
SELECT
  sale_date,
  sale_amount,
  -- Calculate running total: sum of all amounts up to and including current row
  SUM(sale_amount) OVER (
    ORDER BY
      sale_date -- Order rows by date
      ROWS BETWEEN UNBOUNDED PRECEDING AND
      CURRENT ROW -- Include all previous rows
  ) AS running_total
FROM
  sales
ORDER BY
  sale_date;

-- Method 2: For grouped daily totals with running total
SELECT
  sale_date,
  daily_total,
  -- Calculate running total of daily totals
  SUM(daily_total) OVER (
    ORDER BY
      sale_date ROWS BETWEEN UNBOUNDED PRECEDING AND
      CURRENT ROW
  ) AS cumulative_total
FROM
  (
    -- First get daily totals
    SELECT
      sale_date,
      SUM(sale_amount) AS daily_total
    FROM
      sales
    GROUP BY
      sale_date
  ) AS daily_sales
ORDER BY
  sale_date;

-- Method 3: For older MySQL versions without window functions (using variables)
SET
  @running_total := 0;

-- Initialize variable
SELECT
  sale_date,
  daily_total,
  (@running_total := @running_total + daily_total) AS cumulative_total
FROM
  (
    SELECT
      sale_date,
      SUM(sale_amount) AS daily_total
    FROM
      sales
    GROUP BY
      sale_date
    ORDER BY
      sale_date
  ) AS daily_sales;

-- Window Function Explanation:
-- OVER(): Defines the window/frame for the calculation
-- ORDER BY sale_date: Determines the order of rows in the window
-- ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW: 
--   Includes all rows from the start up to the current row
-- UNBOUNDED PRECEDING: No lower bound (start of partition)
-- CURRENT ROW: Stop at current row