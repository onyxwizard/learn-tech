# 🏆 MySQL Aggregate Functions Challenge

## 📚 Challenge Overview
Test your MySQL skills with these real-world scenarios using aggregate functions. You'll be working with two datasets: the existing sales data and a new employee database.

## 🗄️ Additional Schema Setup

```sql
-- Create employee database for additional challenges
CREATE DATABASE IF NOT EXISTS company_demo;
USE company_demo;

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    budget DECIMAL(12, 2)
);

-- Create employees table
CREATE TABLE IF NOT EXISTS employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    dept_id INT,
    salary DECIMAL(10, 2),
    hire_date DATE,
    commission_pct DECIMAL(5, 2),
    FOREIGN KEY (dept_id) REFERENCES departments(dept_id)
);

-- Create projects table
CREATE TABLE IF NOT EXISTS projects (
    project_id INT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(100) NOT NULL,
    dept_id INT,
    budget DECIMAL(12, 2),
    start_date DATE,
    end_date DATE,
    status ENUM('Planning', 'Active', 'Completed', 'On Hold')
);

-- Insert department data
INSERT INTO departments (dept_name, location, budget) VALUES
('Sales', 'New York', 500000.00),
('Engineering', 'San Francisco', 1200000.00),
('Marketing', 'Chicago', 300000.00),
('HR', 'Boston', 200000.00),
('Finance', 'New York', 450000.00);

-- Insert employee data
INSERT INTO employees (first_name, last_name, email, dept_id, salary, hire_date, commission_pct) VALUES
('John', 'Doe', 'john.doe@email.com', 1, 75000.00, '2020-03-15', 5.00),
('Jane', 'Smith', 'jane.smith@email.com', 1, 82000.00, '2019-07-22', 7.50),
('Bob', 'Johnson', 'bob.johnson@email.com', 2, 95000.00, '2018-05-10', NULL),
('Alice', 'Williams', 'alice.williams@email.com', 2, 110000.00, '2021-01-30', NULL),
('Charlie', 'Brown', 'charlie.brown@email.com', 3, 65000.00, '2022-03-01', NULL),
('Diana', 'Miller', 'diana.miller@email.com', 3, 72000.00, '2020-11-15', 3.00),
('Eve', 'Davis', 'eve.davis@email.com', 4, 58000.00, '2023-02-28', NULL),
('Frank', 'Wilson', 'frank.wilson@email.com', 5, 89000.00, '2017-09-05', NULL),
('Grace', 'Moore', 'grace.moore@email.com', 5, 94000.00, '2019-12-12', NULL),
('Henry', 'Taylor', 'henry.taylor@email.com', 1, 68000.00, '2021-08-19', 4.50);

-- Insert project data
INSERT INTO projects (project_name, dept_id, budget, start_date, end_date, status) VALUES
('Website Redesign', 2, 150000.00, '2024-01-01', '2024-06-30', 'Active'),
('Q4 Marketing Campaign', 3, 75000.00, '2024-03-01', '2024-05-31', 'Active'),
('Sales Training Program', 1, 50000.00, '2024-02-15', '2024-04-15', 'Completed'),
('HR System Upgrade', 4, 120000.00, '2023-11-01', '2024-03-31', 'On Hold'),
('Financial Audit', 5, 85000.00, '2024-03-15', '2024-04-30', 'Active'),
('Mobile App Development', 2, 250000.00, '2023-09-01', '2024-08-31', 'Active');
```

## 🎯 Challenge Questions

### **Sales Database Challenges** (Use `sales_demo` database)

**Beginner Level:**
1. **Counting Challenge**: How many total sales transactions occurred in the 'Electronics' category?
2. **Finding Extremes**: What is the highest and lowest sale amount in the 'Furniture' category?
3. **Simple Aggregation**: What's the total revenue generated from all sales?

**Intermediate Level:**
4. **Category Analysis**: Show total revenue and average sale amount for each product category, sorted by highest revenue first.
5. **Regional Performance**: For each region, calculate:
   - Number of transactions
   - Total quantity sold
   - Average sale amount
   - Only show regions with more than 1 transaction
6. **Date Analysis**: Show daily sales totals, but only for days where total sales exceeded $500.

**Advanced Level:**
7. **Product Ranking**: List all products with:
   - Total revenue generated
   - Number of times sold
   - Average quantity per sale
   - Only include products sold more than once
8. **Percentage Contribution**: For each category, show:
   - Category revenue
   - Percentage of total revenue
   - Difference from category average revenue
9. **Cumulative Analysis**: Create a running total of sales by date (requires window function).

### **Employee Database Challenges** (Use `company_demo` database)

**Beginner Level:**
10. **Department Stats**: Count the number of employees in each department.
11. **Salary Analysis**: Find the highest, lowest, and average salary across all employees.
12. **Commission Check**: Count how many employees have a commission percentage.

**Intermediate Level:**
13. **Department Budget Analysis**: For each department, calculate:
    - Total salary expenditure
    - Percentage of department budget used for salaries
    - Average employee salary
14. **Tenure Analysis**: Calculate average tenure (in years) of employees in each department.
    *Hint: Use `DATEDIFF()` and `CURDATE()`*
15. **Salary Distribution**: Create salary brackets ($0-50K, $50-75K, $75-100K, $100K+) and count employees in each.

**Advanced Level:**
16. **Department Comparison**: Show for each department:
    - Total number of employees
    - Total salary cost
    - Average salary
    - Difference from company-wide average salary
17. **Project Budget Analysis**: For active projects, calculate:
    - Total project budget by department
    - Average project budget
    - Number of projects
    - Percentage of total department budget allocated to projects
18. **Employee Ranking**: Rank employees within their department by salary (highest to lowest).
19. **Salary Gap Analysis**: Calculate the ratio between the highest and lowest salary in each department.
20. **Hiring Trend**: Show the number of employees hired per year and the cumulative total over time.

### **Cross-Database Challenge** (Use both databases)
21. **Sales Commission Simulation**: Assuming sales employees get commission on sales:
    - Create a query that joins employee data with sales data (hypothetically)
    - Calculate potential commission for each sales employee if they got their commission rate on total sales
    - Show employee name, total sales attributed, commission rate, and potential commission

## 🏅 Challenge Solutions Framework

Here's a template to structure your solutions:

```sql
-- Challenge 1: Counting Challenge
SELECT 
    COUNT(*) AS electronics_transactions
FROM sales_demo.sales
WHERE category = 'Electronics';

-- Challenge 2: Finding Extremes
SELECT 
    MAX(sale_amount) AS highest_furniture_sale,
    MIN(sale_amount) AS lowest_furniture_sale
FROM sales_demo.sales
WHERE category = 'Furniture';

-- Challenge 3: Simple Aggregation
SELECT 
    SUM(sale_amount) AS total_revenue
FROM sales_demo.sales;

-- Continue with other challenges...
```

## 🎖️ Scoring System

| Level | Points per Challenge | Total Possible |
|-------|---------------------|----------------|
| Beginner | 10 points | 120 points |
| Intermediate | 15 points | 90 points |
| Advanced | 25 points | 175 points |
| **Total** | | **385 points** |

**Grading:**
- 300+ points: SQL Aggregate Master 🏆
- 200-299 points: SQL Pro ⭐
- 100-199 points: SQL Intermediate 📈
- Below 100: Keep Practicing! 💪

## 📝 Tips for Success

1. **Read Carefully**: Make sure you understand what each question asks for
2. **Test Incrementally**: Build your queries step by step
3. **Use Aliases**: Make your output readable with clear column names
4. **Check Data Types**: Ensure you're using the right functions for your data
5. **Optimize**: Think about the most efficient way to write each query

## 🔍 Expected Output Examples

For Challenge 4, your output should look something like:
```
+-------------+---------------+----------------+
| category    | total_revenue | avg_sale_amount|
+-------------+---------------+----------------+
| Electronics |     2875.49   |     718.87     |
| Furniture   |      645.73   |     215.24     |
+-------------+---------------+----------------+
```

## 🚀 Bonus Challenges (Extra Credit)

22. **Efficiency Test**: Write the most efficient query to calculate total sales by category and region in a single pass.
23. **Dynamic Analysis**: Create a stored procedure that accepts a date range and returns sales summary for that period.
24. **Data Quality Check**: Identify any data anomalies (like sales with 0 quantity or negative amounts).
25. **Forecasting**: Calculate month-over-month growth percentage for sales.

## 📤 Submission Guidelines

1. Save all your solutions in a single `.sql` file
2. Comment each query with the challenge number
3. Include both the query and a sample of the output
4. Time yourself! Try to complete all challenges in under 90 minutes

## 🛠️ Tools to Use

- **MySQL Workbench** or command line
- **EXPLAIN** to analyze query performance
- **Query Execution Time** tracking
- **Data Sampling**: Use `LIMIT` to test with subset first

## 🎯 Real-World Application Reminder

Each challenge represents a real business scenario:
- Sales reporting
- Employee analytics
- Budget planning
- Performance metrics
- Departmental analysis

---

**Ready to test your skills? Start the timer and begin!** ⏱️

*Remember: The best way to learn is by doing. Don't just copy solutions - understand why each query works!*

Good luck! May your queries be fast and your results accurate! 🍀