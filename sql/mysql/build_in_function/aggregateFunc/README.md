# MySQL Aggregate Functions - Guide & Examples

## 📋 Overview
This guide covers MySQL's built-in aggregate functions with practical examples using a sample sales database.

## 🗄️ Database Schema Setup

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS sales_demo;
USE sales_demo;

-- Create sales table
CREATE TABLE IF NOT EXISTS sales (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    sale_amount DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    sale_date DATE NOT NULL,
    region VARCHAR(50)
);

-- Insert sample data
INSERT INTO sales (product_name, category, sale_amount, quantity, sale_date, region) VALUES
('Laptop Pro', 'Electronics', 1200.00, 1, '2024-01-15', 'North'),
('Wireless Mouse', 'Electronics', 35.50, 3, '2024-01-15', 'North'),
('Office Chair', 'Furniture', 299.99, 2, '2024-01-16', 'South'),
('Laptop Pro', 'Electronics', 1200.00, 1, '2024-01-17', 'East'),
('Desk Lamp', 'Furniture', 45.75, 5, '2024-01-18', 'West'),
('Mechanical Keyboard', 'Electronics', 89.99, 4, '2024-01-18', 'North'),
('Office Chair', 'Furniture', 299.99, 1, '2024-01-19', 'South'),
('Monitor 24"', 'Electronics', 350.00, 2, '2024-01-20', 'East');
```

## 📊 Aggregate Functions Explained

### 1. **COUNT()**
**Definition**: Counts the number of rows or non-NULL values in a result set.

**Use Case**: Counting records, checking data completeness, generating reports.

**Syntax**:
```sql
SELECT COUNT(column_name) FROM table_name;
SELECT COUNT(*) FROM table_name;
```

**Examples**:
```sql
-- Count all rows
SELECT COUNT(*) AS total_sales FROM sales;

-- Count non-NULL values in a specific column
SELECT COUNT(region) AS region_count FROM sales;

-- Count distinct values
SELECT COUNT(DISTINCT category) AS unique_categories FROM sales;
```

**Real-World Application**: 
- E-commerce: Count total orders per day
- Analytics: Count active users per month
- Inventory: Count products in each category

### 2. **MAX()**
**Definition**: Returns the maximum value in a set of values.

**Use Case**: Finding highest values, performance analysis, price analysis.

**Syntax**:
```sql
SELECT MAX(column_name) FROM table_name;
```

**Examples**:
```sql
-- Find highest sale amount
SELECT MAX(sale_amount) AS highest_sale FROM sales;

-- Find most recent sale date
SELECT MAX(sale_date) AS latest_sale FROM sales;

-- Find maximum quantity sold for electronics
SELECT MAX(quantity) AS max_qty_electronics 
FROM sales 
WHERE category = 'Electronics';
```

**Real-World Application**:
- Sales: Identify top-performing products
- Finance: Find highest transaction values
- HR: Determine maximum salaries by department

### 3. **MIN()**
**Definition**: Returns the minimum value in a set of values.

**Use Case**: Finding lowest values, identifying outliers, range analysis.

**Syntax**:
```sql
SELECT MIN(column_name) FROM table_name;
```

**Examples**:
```sql
-- Find smallest sale amount
SELECT MIN(sale_amount) AS lowest_sale FROM sales;

-- Find earliest sale date
SELECT MIN(sale_date) AS first_sale FROM sales;

-- Find minimum quantity sold in furniture category
SELECT MIN(quantity) AS min_qty_furniture 
FROM sales 
WHERE category = 'Furniture';
```

**Real-World Application**:
- Retail: Identify lowest-selling products
- Logistics: Find minimum delivery times
- Quality Control: Identify minimum test scores

### 4. **SUM()**
**Definition**: Returns the sum of values in a numeric column.

**Use Case**: Calculating totals, aggregating financial data, inventory sums.

**Syntax**:
```sql
SELECT SUM(column_name) FROM table_name;
```

**Examples**:
```sql
-- Total revenue
SELECT SUM(sale_amount) AS total_revenue FROM sales;

-- Total quantity sold
SELECT SUM(quantity) AS total_units_sold FROM sales;

-- Total revenue by category
SELECT category, SUM(sale_amount) AS category_revenue 
FROM sales 
GROUP BY category;
```

**Real-World Application**:
- Finance: Calculate total monthly revenue
- Inventory: Sum total stock value
- Project Management: Total hours worked

### 5. **AVG()**
**Definition**: Returns the average value of a numeric column.

**Use Case**: Calculating averages, performance metrics, trend analysis.

**Syntax**:
```sql
SELECT AVG(column_name) FROM table_name;
```

**Examples**:
```sql
-- Average sale amount
SELECT AVG(sale_amount) AS average_sale FROM sales;

-- Average quantity per sale
SELECT AVG(quantity) AS avg_quantity FROM sales;

-- Average sale amount by region
SELECT region, AVG(sale_amount) AS avg_sale_per_region 
FROM sales 
GROUP BY region;
```

**Real-World Application**:
- Sales: Calculate average order value
- Education: Compute average test scores
- Healthcare: Average patient wait times

## 🎯 GROUP BY Clause

**Definition**: Groups rows that have the same values in specified columns.

**Use Case**: Creating summary reports, category analysis, segmentation.

**Syntax**:
```sql
SELECT column1, AGGREGATE_FUNCTION(column2)
FROM table_name
GROUP BY column1;
```

**Examples**:
```sql
-- Total sales by category
SELECT category, 
       COUNT(*) AS transaction_count,
       SUM(sale_amount) AS total_sales,
       AVG(sale_amount) AS avg_sale
FROM sales
GROUP BY category;

-- Daily sales summary
SELECT sale_date,
       COUNT(*) AS daily_transactions,
       SUM(sale_amount) AS daily_revenue
FROM sales
GROUP BY sale_date
ORDER BY sale_date;

-- Regional performance
SELECT region,
       category,
       COUNT(*) AS count,
       SUM(quantity) AS total_quantity
FROM sales
GROUP BY region, category
ORDER BY region, category;
```

**Real-World Framework**:
```sql
-- Framework for business intelligence queries
SELECT 
    -- Grouping dimensions
    time_period,
    category,
    region,
    
    -- Key metrics
    COUNT(*) AS transaction_count,
    SUM(sales_amount) AS total_revenue,
    AVG(sales_amount) AS avg_order_value,
    MAX(sales_amount) AS max_sale,
    MIN(sales_amount) AS min_sale
    
FROM sales_data
WHERE date BETWEEN 'start_date' AND 'end_date'
GROUP BY time_period, category, region
HAVING total_revenue > threshold_value  -- Optional filtering
ORDER BY total_revenue DESC;
```

## 🔄 Optional Parameters & Advanced Usage

### **DISTINCT with Aggregate Functions**
```sql
SELECT COUNT(DISTINCT region) AS unique_regions FROM sales;
SELECT AVG(DISTINCT sale_amount) FROM sales;  -- Rare but possible
```

### **HAVING Clause for Filtering Groups**
```sql
-- Categories with total sales > 500
SELECT category, SUM(sale_amount) AS total
FROM sales
GROUP BY category
HAVING total > 500;
```

### **Combining Multiple Functions**
```sql
SELECT 
    category,
    COUNT(*) AS transactions,
    SUM(sale_amount) AS total_sales,
    AVG(sale_amount) AS avg_sale,
    MAX(sale_amount) AS highest_sale,
    MIN(sale_amount) AS lowest_sale
FROM sales
GROUP BY category;
```

## 🌍 Real-World Scenarios

### **E-Commerce Analytics Framework**
```sql
-- Monthly sales report
SELECT 
    DATE_FORMAT(sale_date, '%Y-%m') AS month,
    COUNT(*) AS orders,
    SUM(sale_amount) AS revenue,
    AVG(sale_amount) AS avg_order_value,
    COUNT(DISTINCT product_name) AS unique_products
FROM sales
GROUP BY DATE_FORMAT(sale_date, '%Y-%m')
ORDER BY month DESC;
```

### **Inventory Management**
```sql
-- Category performance analysis
SELECT 
    category,
    SUM(quantity) AS total_units_sold,
    SUM(sale_amount) AS category_revenue,
    SUM(sale_amount) / SUM(quantity) AS avg_price_per_unit
FROM sales
GROUP BY category
ORDER BY category_revenue DESC;
```

### **Regional Performance Dashboard**
```sql
SELECT 
    region,
    COUNT(*) AS transaction_count,
    SUM(sale_amount) AS total_revenue,
    AVG(sale_amount) AS avg_sale_size,
    MAX(sale_amount) AS largest_sale,
    100 * SUM(sale_amount) / (SELECT SUM(sale_amount) FROM sales) AS percent_of_total
FROM sales
GROUP BY region
HAVING total_revenue > 100
ORDER BY total_revenue DESC;
```

## 💡 Best Practices

1. **Use Aliases**: Always use meaningful aliases for readability
2. **Filter First**: Use WHERE before GROUP BY to reduce data volume
3. **Index Grouping Columns**: Index columns used in GROUP BY for performance
4. **Consider NULLs**: Aggregate functions ignore NULL values (except COUNT(*))
5. **Test with EXPLAIN**: Use EXPLAIN to optimize query performance

## 🚀 Quick Reference

| Function | Purpose | Ignores NULL? |
|----------|---------|---------------|
| COUNT()  | Count rows/values | Yes (except COUNT(*)) |
| MAX()    | Find maximum value | Yes |
| MIN()    | Find minimum value | Yes |
| SUM()    | Calculate total | Yes |
| AVG()    | Calculate average | Yes |

## 📁 Exporting Results

```sql
-- Save aggregated results to a new table
CREATE TABLE sales_summary AS
SELECT category, COUNT(*) as count, SUM(sale_amount) as total
FROM sales
GROUP BY category;

-- Export to CSV (MySQL 8.0+)
SELECT category, COUNT(*), SUM(sale_amount)
FROM sales
GROUP BY category
INTO OUTFILE '/tmp/sales_summary.csv'
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n';
```
## Understanding SQL execution order:
```sql
-- Understanding SQL execution order helps write better queries:
-- 1. FROM (and JOINs) - Get data from tables
-- 2. WHERE - Filter individual rows
-- 3. GROUP BY - Group rows into aggregates
-- 4. HAVING - Filter grouped results
-- 5. SELECT - Choose columns to display
-- 6. ORDER BY - Sort final results
-- 7. LIMIT - Limit number of rows returned

-- Example showing all clauses:
SELECT 
    category,                           -- Step 5: Select columns
    COUNT(*) AS count,                  -- Step 5: Calculate aggregates
    SUM(sale_amount) AS total           -- Step 5: Calculate aggregates
FROM 
    sales                               -- Step 1: Get data from table
WHERE 
    sale_date >= '2024-01-01'           -- Step 2: Filter rows
GROUP BY 
    category                            -- Step 3: Group by category
HAVING 
    SUM(sale_amount) > 1000             -- Step 4: Filter groups
ORDER BY 
    total DESC                          -- Step 6: Sort results
LIMIT 10;                               -- Step 7: Limit output
```

This guide provides a practical foundation for using MySQL aggregate functions in real-world scenarios. Experiment with the provided examples and modify them to fit your specific use cases!