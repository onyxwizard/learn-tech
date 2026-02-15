# 📅 MySQL Date and Time Functions - Complete Guide

## 🎯 Overview
Date and time functions allow you to manipulate, format, and calculate with temporal data. Essential for reports, analytics, scheduling, and any time-sensitive operations.

## 📚 Function Reference

### 1. **CURDATE() / CURRENT_DATE()**
**Definition**: Returns the current date (without time).

**Syntax**:
```sql
CURDATE()
CURRENT_DATE()  -- Synonym
CURRENT_DATE    -- Synonym (without parentheses)
```

**Parameters**: None

**Return Type**: `DATE`

**Examples**:
```sql
-- Get today's date
SELECT CURDATE();  -- Returns: 2024-03-15 (example)

-- Use in WHERE clause for today's records
SELECT * FROM orders WHERE order_date = CURDATE();

-- Calculate age based on birth date
SELECT name, CURDATE() AS today, birth_date,
       YEAR(CURDATE()) - YEAR(birth_date) - 
       (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d')) AS age
FROM users;

-- Set default value in INSERT
INSERT INTO logs (log_date, message) 
VALUES (CURDATE(), 'System started');

-- Compare dates
SELECT * FROM events 
WHERE event_date >= CURDATE() 
ORDER BY event_date;
```

**Real-World Use Cases**:
- Daily reports
- Age calculations
- Expiry date checks
- Today's activity tracking

---

### 2. **CURTIME() / CURRENT_TIME()**
**Definition**: Returns the current time (without date).

**Syntax**:
```sql
CURTIME()
CURRENT_TIME()  -- Synonym
CURRENT_TIME    -- Synonym (without parentheses)
CURTIME(fsp)    -- With fractional seconds precision
```

**Parameters**:
- `fsp`: Optional fractional seconds precision (0-6)

**Return Type**: `TIME`

**Examples**:
```sql
-- Get current time
SELECT CURTIME();  -- Returns: 14:30:45 (example)

-- With fractional seconds
SELECT CURTIME(3);  -- Returns: 14:30:45.123

-- Calculate time differences
SELECT start_time, CURTIME() AS current_time,
       TIMEDIFF(CURTIME(), start_time) AS duration
FROM sessions 
WHERE session_active = 1;

-- Log current time
INSERT INTO access_logs (access_time, user_id) 
VALUES (CURTIME(), 123);

-- Check if within business hours
SELECT * FROM support_tickets 
WHERE CURTIME() BETWEEN '09:00:00' AND '17:00:00';

-- Format as 12-hour clock
SELECT TIME_FORMAT(CURTIME(), '%h:%i %p') AS current_time_12hr;
```

**Real-World Use Cases**:
- Timing operations
- Shift management
- Business hour checks
- Session duration calculations

---

### 3. **NOW() / CURRENT_TIMESTAMP()**
**Definition**: Returns the current date and time.

**Syntax**:
```sql
NOW()
CURRENT_TIMESTAMP()  -- Synonym
CURRENT_TIMESTAMP    -- Synonym
NOW(fsp)             -- With fractional seconds precision
LOCALTIME()          -- Synonym
LOCALTIMESTAMP()     -- Synonym
```

**Parameters**:
- `fsp`: Optional fractional seconds precision (0-6)

**Return Type**: `DATETIME` or `TIMESTAMP`

**Examples**:
```sql
-- Get current datetime
SELECT NOW();  -- Returns: 2024-03-15 14:30:45

-- With microseconds
SELECT NOW(6);  -- Returns: 2024-03-15 14:30:45.123456

-- Set creation timestamp
INSERT INTO orders (order_id, customer_id, created_at, total_amount)
VALUES (1001, 456, NOW(), 199.99);

-- Compare with timezone awareness
SELECT created_at, NOW() AS current_time,
       TIMESTAMPDIFF(MINUTE, created_at, NOW()) AS minutes_ago
FROM orders 
WHERE TIMESTAMPDIFF(HOUR, created_at, NOW()) < 24;

-- Use as default value in table definition
CREATE TABLE logs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Important Notes**:
- `NOW()` returns the time at which the statement began execution
- `SYSDATE()` returns the time at which it executes (different in stored routines)
- For high precision timing, use `NOW(6)` for microseconds

**Real-World Use Cases**:
- Audit trails
- Timestamping transactions
- Calculating durations
- Scheduling

---

### 4. **DATE_ADD()**
**Definition**: Adds a specified time interval to a date.

**Syntax**:
```sql
DATE_ADD(date, INTERVAL expr unit)
date + INTERVAL expr unit  -- Alternative syntax
```

**Parameters**:
- `date`: Starting date/datetime
- `expr`: Number to add (can be negative)
- `unit`: Time unit (see table below)

**Supported Interval Units**:
| Unit | Format | Example |
|------|--------|---------|
| MICROSECOND | `MICROSECOND` | `INTERVAL 100 MICROSECOND` |
| SECOND | `SECOND` | `INTERVAL 30 SECOND` |
| MINUTE | `MINUTE` | `INTERVAL 15 MINUTE` |
| HOUR | `HOUR` | `INTERVAL 2 HOUR` |
| DAY | `DAY` | `INTERVAL 7 DAY` |
| WEEK | `WEEK` | `INTERVAL 2 WEEK` |
| MONTH | `MONTH` | `INTERVAL 3 MONTH` |
| QUARTER | `QUARTER` | `INTERVAL 1 QUARTER` |
| YEAR | `YEAR` | `INTERVAL 5 YEAR` |
| SECOND_MICROSECOND | `SECOND_MICROSECOND` | `INTERVAL '30.500000' SECOND_MICROSECOND` |
| MINUTE_MICROSECOND | `MINUTE_MICROSECOND` | `INTERVAL '1:30.500000' MINUTE_MICROSECOND` |
| MINUTE_SECOND | `MINUTE_SECOND` | `INTERVAL '1:30' MINUTE_SECOND` |
| HOUR_MICROSECOND | `HOUR_MICROSECOND` | `INTERVAL '1:1:30.500000' HOUR_MICROSECOND` |
| HOUR_SECOND | `HOUR_SECOND` | `INTERVAL '1:1:30' HOUR_SECOND` |
| HOUR_MINUTE | `HOUR_MINUTE` | `INTERVAL '1:30' HOUR_MINUTE` |
| DAY_MICROSECOND | `DAY_MICROSECOND` | `INTERVAL '1 1:1:30.500000' DAY_MICROSECOND` |
| DAY_SECOND | `DAY_SECOND` | `INTERVAL '1 1:1:30' DAY_SECOND` |
| DAY_MINUTE | `DAY_MINUTE` | `INTERVAL '1 1:30' DAY_MINUTE` |
| DAY_HOUR | `DAY_HOUR` | `INTERVAL '1 1' DAY_HOUR` |
| YEAR_MONTH | `YEAR_MONTH` | `INTERVAL '1-6' YEAR_MONTH` |

**Examples**:
```sql
-- Add 7 days to a date
SELECT DATE_ADD('2024-03-15', INTERVAL 7 DAY);  -- Returns: 2024-03-22

-- Add 3 months to current date
SELECT DATE_ADD(CURDATE(), INTERVAL 3 MONTH) AS three_months_later;

-- Calculate due dates (30 days from order)
SELECT order_id, order_date,
       DATE_ADD(order_date, INTERVAL 30 DAY) AS due_date
FROM orders;

-- Add complex interval
SELECT NOW() AS current_time,
       DATE_ADD(NOW(), INTERVAL '1:30' HOUR_MINUTE) AS in_90_minutes;

-- Alternative syntax
SELECT '2024-03-15' + INTERVAL 1 WEEK;  -- Returns: 2024-03-22

-- Calculate subscription end date
SELECT user_id, subscription_start,
       DATE_ADD(subscription_start, INTERVAL 1 YEAR) AS subscription_end
FROM subscriptions;
```

**Real-World Use Cases**:
- Due date calculations
- Subscription end dates
- Project timelines
- Reminder scheduling

---

### 5. **DATE_SUB()**
**Definition**: Subtracts a specified time interval from a date.

**Syntax**:
```sql
DATE_SUB(date, INTERVAL expr unit)
date - INTERVAL expr unit  -- Alternative syntax
```

**Parameters**: Same as DATE_ADD()

**Examples**:
```sql
-- Subtract 7 days from a date
SELECT DATE_SUB('2024-03-15', INTERVAL 7 DAY);  -- Returns: 2024-03-08

-- Get date 30 days ago
SELECT DATE_SUB(CURDATE(), INTERVAL 30 DAY) AS thirty_days_ago;

-- Calculate start date for last month's report
SELECT DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AS last_month_start;

-- Find records from last week
SELECT * FROM sales 
WHERE sale_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
  AND sale_date < CURDATE();

-- Calculate age at specific past date
SELECT name, birth_date,
       DATE_SUB('2000-12-31', INTERVAL 25 YEAR) AS twenty_five_years_ago,
       birth_date <= DATE_SUB('2000-12-31', INTERVAL 25 YEAR) AS was_25_then
FROM people;

-- Alternative syntax
SELECT CURDATE() - INTERVAL 1 DAY AS yesterday;
```

**Pro Tip**: `DATE_SUB(date, INTERVAL X unit)` = `DATE_ADD(date, INTERVAL -X unit)`

**Real-World Use Cases**:
- Rolling time windows
- Historical comparisons
- Age-at-event calculations
- Grace period calculations

---

### 6. **EXTRACT()**
**Definition**: Extracts a specific part from a date/time.

**Syntax**:
```sql
EXTRACT(unit FROM date)
```

**Supported Units**:
| Unit | Returns |
|------|---------|
| `MICROSECOND` | Microseconds (0-999999) |
| `SECOND` | Seconds (0-59) |
| `MINUTE` | Minutes (0-59) |
| `HOUR` | Hours (0-23) |
| `DAY` | Day of month (1-31) |
| `WEEK` | Week number (0-53) |
| `MONTH` | Month (1-12) |
| `QUARTER` | Quarter (1-4) |
| `YEAR` | Year (1000-9999) |
| `SECOND_MICROSECOND` | Seconds.microseconds |
| `MINUTE_MICROSECOND` | Minutes:seconds.microseconds |
| `MINUTE_SECOND` | Minutes:seconds |
| `HOUR_MICROSECOND` | Hours:minutes:seconds.microseconds |
| `HOUR_SECOND` | Hours:minutes:seconds |
| `HOUR_MINUTE` | Hours:minutes |
| `DAY_MICROSECOND` | Days hours:minutes:seconds.microseconds |
| `DAY_SECOND` | Days hours:minutes:seconds |
| `DAY_MINUTE` | Days hours:minutes |
| `DAY_HOUR` | Days hours |
| `YEAR_MONTH` | Year-month |

**Examples**:
```sql
-- Extract year from date
SELECT EXTRACT(YEAR FROM '2024-03-15') AS year;  -- Returns: 2024

-- Extract month and day
SELECT EXTRACT(MONTH FROM order_date) AS month,
       EXTRACT(DAY FROM order_date) AS day
FROM orders;

-- Get hour and minute from timestamp
SELECT created_at,
       EXTRACT(HOUR FROM created_at) AS hour,
       EXTRACT(MINUTE FROM created_at) AS minute
FROM logs;

-- Complex extraction
SELECT EXTRACT(YEAR_MONTH FROM '2024-03-15') AS year_month;  -- Returns: 202403

-- Extract quarter for financial reporting
SELECT EXTRACT(QUARTER FROM sale_date) AS fiscal_quarter,
       SUM(amount) AS total_sales
FROM sales
GROUP BY EXTRACT(QUARTER FROM sale_date);

-- Microsecond precision
SELECT EXTRACT(MICROSECOND FROM NOW(6)) AS microseconds;
```

**Comparison with Other Functions**:
```sql
-- EXTRACT vs YEAR/MONTH/DAY functions
SELECT EXTRACT(YEAR FROM date) AS year1,  -- Both return same
       YEAR(date) AS year2               -- YEAR() is simpler
FROM dates;

-- EXTRACT allows complex units
SELECT EXTRACT(HOUR_MINUTE FROM '14:30:45') AS hour_minute;  -- Returns: 1430
```

**Real-World Use Cases**:
- Time-based segmentation
- Financial quarter analysis
- Hourly/daily/monthly reporting
- Event scheduling by time parts

---

### 7. **DATEDIFF()**
**Definition**: Returns the number of days between two dates.

**Syntax**:
```sql
DATEDIFF(date1, date2)
-- Returns: date1 - date2 (in days)
```

**Parameters**:
- `date1`, `date2`: Date or datetime expressions

**Important**: Time portions are ignored, only dates are compared.

**Examples**:
```sql
-- Days between two dates
SELECT DATEDIFF('2024-03-20', '2024-03-15') AS days_diff;  -- Returns: 5

-- Negative result when date1 is earlier
SELECT DATEDIFF('2024-03-10', '2024-03-15') AS days_diff;  -- Returns: -5

-- Calculate age in days
SELECT name, birth_date,
       DATEDIFF(CURDATE(), birth_date) AS age_in_days
FROM users;

-- Days since last order
SELECT customer_id, last_order_date,
       DATEDIFF(CURDATE(), last_order_date) AS days_since_last_order
FROM customers;

-- Filter records from last 30 days
SELECT * FROM events 
WHERE DATEDIFF(CURDATE(), event_date) <= 30;

-- Project duration in days
SELECT project_id, start_date, end_date,
       DATEDIFF(end_date, start_date) AS duration_days
FROM projects;
```

**Time-Aware Alternative**:
```sql
-- For time-aware differences, use TIMESTAMPDIFF
SELECT TIMESTAMPDIFF(HOUR, '2024-03-15 10:00:00', '2024-03-15 14:30:00') AS hours_diff;
```

**Real-World Use Cases**:
- Age calculations
- Service level agreement (SLA) tracking
- Project duration
- Customer retention analysis

---

### 8. **DATE_FORMAT()**
**Definition**: Formats a date according to a format string.

**Syntax**:
```sql
DATE_FORMAT(date, format)
```

**Format Specifiers**:
| Specifier | Description |
|-----------|-------------|
| `%Y` | Year, 4 digits (1970-2155) |
| `%y` | Year, 2 digits (00-99) |
| `%m` | Month, numeric (01-12) |
| `%c` | Month, numeric (1-12) |
| `%M` | Month name (January-December) |
| `%b` | Abbreviated month name (Jan-Dec) |
| `%d` | Day of month, numeric (01-31) |
| `%e` | Day of month, numeric (1-31) |
| `%D` | Day of month with suffix (1st, 2nd, 3rd, ...) |
| `%W` | Weekday name (Sunday-Saturday) |
| `%a` | Abbreviated weekday (Sun-Sat) |
| `%w` | Day of week (0=Sunday, 6=Saturday) |
| `%H` | Hour (00-23) |
| `%h` | Hour (01-12) |
| `%I` | Hour (01-12) |
| `%p` | AM or PM |
| `%i` | Minutes (00-59) |
| `%s` | Seconds (00-59) |
| `%f` | Microseconds (000000-999999) |
| `%j` | Day of year (001-366) |
| `%U` | Week (00-53), Sunday first day |
| `%u` | Week (00-53), Monday first day |
| `%V` | Week (01-53), Sunday first day |
| `%v` | Week (01-53), Monday first day |
| `%%` | Literal % character |

**Examples**:
```sql
-- Basic formatting
SELECT DATE_FORMAT('2024-03-15', '%W, %M %e, %Y');  
-- Returns: Friday, March 15, 2024

-- Common formats
SELECT DATE_FORMAT(NOW(), '%Y-%m-%d') AS iso_date,          -- 2024-03-15
       DATE_FORMAT(NOW(), '%m/%d/%Y') AS us_date,           -- 03/15/2024
       DATE_FORMAT(NOW(), '%d/%m/%Y') AS eu_date,           -- 15/03/2024
       DATE_FORMAT(NOW(), '%Y%m%d') AS compact_date;        -- 20240315

-- Time formatting
SELECT DATE_FORMAT(NOW(), '%H:%i:%s') AS time_24hr,         -- 14:30:45
       DATE_FORMAT(NOW(), '%h:%i:%s %p') AS time_12hr;      -- 02:30:45 PM

-- Report formatting
SELECT order_id,
       DATE_FORMAT(order_date, '%b %e, %Y') AS formatted_date,
       DATE_FORMAT(order_date, '%l:%i %p') AS formatted_time
FROM orders;

-- Localized month names (depends on lc_time_names)
SET lc_time_names = 'es_ES';
SELECT DATE_FORMAT('2024-03-15', '%M') AS spanish_month;  -- Returns: marzo

-- Combine with other functions
SELECT CONCAT('Order placed on ', 
       DATE_FORMAT(created_at, '%W, %M %e at %l:%i %p')) AS order_message
FROM orders;
```

**Real-World Use Cases**:
- Report generation
- Display formatting
- Localization
- Log file naming
- Email content formatting

---

### 9. **STR_TO_DATE()**
**Definition**: Converts a string to a date using a specified format.

**Syntax**:
```sql
STR_TO_DATE(string, format)
```

**Important**: The inverse of DATE_FORMAT()

**Examples**:
```sql
-- Convert string to date
SELECT STR_TO_DATE('15,03,2024', '%d,%m,%Y');  -- Returns: 2024-03-15

-- Parse US date format
SELECT STR_TO_DATE('03/15/2024', '%m/%d/%Y');  -- Returns: 2024-03-15

-- Parse with time
SELECT STR_TO_DATE('2024-03-15 14:30:45', '%Y-%m-%d %H:%i:%s');

-- Handle different formats
SELECT STR_TO_DATE('March 15, 2024', '%M %d, %Y');

-- Import data from CSV/text
INSERT INTO events (event_date, event_name)
SELECT STR_TO_DATE(date_string, '%Y-%m-%d'), event_name
FROM imported_data;

-- Validate and convert
SELECT 
    input_date_string,
    CASE 
        WHEN STR_TO_DATE(input_date_string, '%Y-%m-%d') IS NOT NULL 
        THEN STR_TO_DATE(input_date_string, '%Y-%m-%d')
        ELSE NULL 
    END AS validated_date
FROM raw_data;
```

**Common Issues & Solutions**:
```sql
-- Handle NULL for invalid dates
SELECT STR_TO_DATE('invalid', '%Y-%m-%d');  -- Returns: NULL

-- Strict mode may cause errors
SET sql_mode = '';
SELECT STR_TO_DATE('2024-02-30', '%Y-%m-%d');  -- Returns: NULL (not error)

-- Two-digit year handling (70-99 = 1970-1999, 00-69 = 2000-2069)
SELECT STR_TO_DATE('02/15/70', '%m/%d/%y');  -- Returns: 1970-02-15
SELECT STR_TO_DATE('02/15/20', '%m/%d/%y');  -- Returns: 2020-02-15
```

**Real-World Use Cases**:
- Data import/ETL
- User input parsing
- Legacy system integration
- Data validation

---

### 10. **TIME_FORMAT()**
**Definition**: Formats a time value according to a format string (similar to DATE_FORMAT but for time only).

**Syntax**:
```sql
TIME_FORMAT(time, format)
```

**Note**: Uses same format specifiers as DATE_FORMAT for time-related parts.

**Examples**:
```sql
-- Basic time formatting
SELECT TIME_FORMAT('14:30:45', '%H:%i:%s');  -- Returns: 14:30:45

-- 12-hour format
SELECT TIME_FORMAT('14:30:45', '%h:%i:%s %p');  -- Returns: 02:30:45 PM

-- Extract just hours and minutes
SELECT TIME_FORMAT(NOW(), '%H:%i') AS current_hour_minute;

-- Format duration (time difference)
SELECT start_time, end_time,
       TIME_FORMAT(TIMEDIFF(end_time, start_time), '%H hours, %i minutes') AS duration
FROM shifts;

-- User-friendly display
SELECT appointment_time,
       TIME_FORMAT(appointment_time, '%l:%i %p') AS friendly_time
FROM appointments;

-- With microseconds
SELECT TIME_FORMAT('14:30:45.123456', '%H:%i:%s.%f');  -- Returns: 14:30:45.123456
```

**Real-World Use Cases**:
- Display formatting for users
- Time-only reports
- Duration formatting
- Scheduling interfaces

---

## 🎯 **Common Date/Time Patterns**

### 1. **Date Ranges and Windows**
```sql
-- Today
SELECT * FROM sales WHERE DATE(sale_date) = CURDATE();

-- Yesterday
SELECT * FROM sales WHERE DATE(sale_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY);

-- This week (Monday to Sunday)
SELECT * FROM sales 
WHERE YEARWEEK(sale_date) = YEARWEEK(CURDATE());

-- Last 7 days
SELECT * FROM sales 
WHERE sale_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY);

-- This month
SELECT * FROM sales 
WHERE MONTH(sale_date) = MONTH(CURDATE()) 
  AND YEAR(sale_date) = YEAR(CURDATE());

-- Last 30 days
SELECT * FROM sales 
WHERE sale_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY);

-- Specific month/year
SELECT * FROM sales 
WHERE YEAR(sale_date) = 2024 
  AND MONTH(sale_date) = 3;
```

### 2. **Age and Duration Calculations**
```sql
-- Age in years (accurate)
SELECT name, birth_date,
       YEAR(CURDATE()) - YEAR(birth_date) - 
       (DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d')) AS age
FROM people;

-- Tenure in days/weeks/months
SELECT employee_id, hire_date,
       DATEDIFF(CURDATE(), hire_date) AS tenure_days,
       TIMESTAMPDIFF(MONTH, hire_date, CURDATE()) AS tenure_months,
       TIMESTAMPDIFF(YEAR, hire_date, CURDATE()) AS tenure_years
FROM employees;

-- Project duration with working days (excluding weekends)
SELECT project_id, start_date, end_date,
       (DATEDIFF(end_date, start_date) + 1) - 
       ((WEEK(end_date) - WEEK(start_date)) * 2) -
       (CASE WHEN WEEKDAY(start_date) = 6 THEN 1 ELSE 0 END) -
       (CASE WHEN WEEKDAY(end_date) = 5 THEN 1 ELSE 0 END) AS working_days
FROM projects;
```

### 3. **Business Date Calculations**
```sql
-- Next business day (skip weekends)
SELECT 
    order_date,
    CASE 
        WHEN DAYOFWEEK(order_date) = 6 THEN DATE_ADD(order_date, INTERVAL 3 DAY)  -- Friday → Monday
        WHEN DAYOFWEEK(order_date) = 7 THEN DATE_ADD(order_date, INTERVAL 2 DAY)  -- Saturday → Monday
        ELSE DATE_ADD(order_date, INTERVAL 1 DAY)  -- Normal day
    END AS next_business_day
FROM orders;

-- Add business days
CREATE FUNCTION add_business_days(start_date DATE, days_to_add INT)
RETURNS DATE
DETERMINISTIC
BEGIN
    DECLARE result_date DATE;
    DECLARE days_added INT DEFAULT 0;
    
    SET result_date = start_date;
    
    WHILE days_added < days_to_add DO
        SET result_date = DATE_ADD(result_date, INTERVAL 1 DAY);
        IF DAYOFWEEK(result_date) NOT IN (1, 7) THEN  -- Not Sunday (1) or Saturday (7)
            SET days_added = days_added + 1;
        END IF;
    END WHILE;
    
    RETURN result_date;
END;
```

### 4. **Financial Periods**
```sql
-- Fiscal quarters (assuming fiscal year starts April 1)
SELECT 
    sale_date,
    CASE 
        WHEN MONTH(sale_date) BETWEEN 4 AND 6 THEN 'Q1'
        WHEN MONTH(sale_date) BETWEEN 7 AND 9 THEN 'Q2'
        WHEN MONTH(sale_date) BETWEEN 10 AND 12 THEN 'Q3'
        ELSE 'Q4'
    END AS fiscal_quarter,
    CASE 
        WHEN MONTH(sale_date) >= 4 THEN YEAR(sale_date)
        ELSE YEAR(sale_date) - 1
    END AS fiscal_year
FROM sales;

-- Get first/last day of month
SELECT 
    DATE_FORMAT(sale_date, '%Y-%m-01') AS first_of_month,
    LAST_DAY(sale_date) AS last_of_month
FROM sales;
```

---

## 🚀 **Real-World Applications Framework**

### **E-commerce Order Management**
```sql
-- Order lifecycle tracking
SELECT 
    order_id,
    order_date,
    -- Estimated delivery (3-5 business days)
    CASE 
        WHEN DAYOFWEEK(order_date) = 5 THEN DATE_ADD(order_date, INTERVAL 5 DAY)  -- Friday
        WHEN DAYOFWEEK(order_date) = 6 THEN DATE_ADD(order_date, INTERVAL 4 DAY)  -- Saturday
        WHEN DAYOFWEEK(order_date) = 7 THEN DATE_ADD(order_date, INTERVAL 4 DAY)  -- Sunday
        ELSE DATE_ADD(order_date, INTERVAL 3 DAY)
    END AS estimated_delivery,
    
    -- Actual delivery (if shipped)
    shipment_date,
    DATEDIFF(delivery_date, shipment_date) AS shipping_days,
    
    -- SLA compliance
    CASE 
        WHEN DATEDIFF(delivery_date, order_date) <= 7 THEN 'On Time'
        ELSE 'Delayed'
    END AS sla_status,
    
    -- Age of pending orders
    CASE 
        WHEN status = 'pending' THEN DATEDIFF(CURDATE(), order_date)
        ELSE NULL
    END AS days_pending
FROM orders;
```

### **Employee Attendance & Payroll**
```sql
-- Timesheet calculation
SELECT 
    employee_id,
    DATE(clock_in) AS work_date,
    TIME_FORMAT(clock_in, '%H:%i') AS time_in,
    TIME_FORMAT(clock_out, '%H:%i') AS time_out,
    
    -- Calculate hours worked (decimal)
    ROUND(TIMESTAMPDIFF(MINUTE, clock_in, clock_out) / 60.0, 2) AS hours_worked,
    
    -- Overtime calculation (>8 hours)
    GREATEST(0, ROUND(TIMESTAMPDIFF(MINUTE, clock_in, clock_out) / 60.0, 2) - 8) AS overtime_hours,
    
    -- Late arrival (>9:00 AM)
    CASE 
        WHEN TIME(clock_in) > '09:00:00' THEN 'Late'
        ELSE 'On Time'
    END AS arrival_status,
    
    -- Weekend bonus flag
    CASE 
        WHEN DAYOFWEEK(clock_in) IN (1, 7) THEN 'Weekend'
        ELSE 'Weekday'
    END AS day_type
FROM time_records
WHERE clock_out IS NOT NULL;
```

### **Subscription & Billing System**
```sql
-- Subscription analytics
SELECT 
    user_id,
    subscription_start,
    subscription_end,
    
    -- Current status
    CASE 
        WHEN subscription_end >= CURDATE() THEN 'Active'
        ELSE 'Expired'
    END AS status,
    
    -- Days until expiry
    DATEDIFF(subscription_end, CURDATE()) AS days_remaining,
    
    -- Renewal window (30 days before expiry)
    CASE 
        WHEN DATEDIFF(subscription_end, CURDATE()) BETWEEN 0 AND 30 THEN 'Renewal Window'
        WHEN subscription_end < CURDATE() THEN 'Expired'
        ELSE 'Active - Not in window'
    END AS renewal_status,
    
    -- Calculate next billing date (monthly subscriptions)
    DATE_ADD(subscription_start, 
             INTERVAL TIMESTAMPDIFF(MONTH, subscription_start, CURDATE()) + 1 MONTH
            ) AS next_billing_date,
    
    -- Grace period (7 days after expiry)
    DATE_ADD(subscription_end, INTERVAL 7 DAY) AS grace_period_end
FROM subscriptions;
```

---

## 🔧 **Performance Optimization**

### 1. **Indexing Date Columns**
```sql
-- Add indexes for date-based queries
CREATE INDEX idx_order_date ON orders(order_date);
CREATE INDEX idx_created_at ON logs(created_at);

-- Composite index for common patterns
CREATE INDEX idx_year_month ON sales(YEAR(sale_date), MONTH(sale_date));
```

### 2. **Avoid Functions on Indexed Columns**
```sql
-- Bad: Function on indexed column prevents index usage
SELECT * FROM orders WHERE DATE_FORMAT(order_date, '%Y-%m') = '2024-03';

-- Good: Use range query
SELECT * FROM orders 
WHERE order_date >= '2024-03-01' 
  AND order_date < '2024-04-01';

-- Bad: Extract function on indexed column
SELECT * FROM logs WHERE EXTRACT(YEAR FROM created_at) = 2024;

-- Good: Range query
SELECT * FROM logs 
WHERE created_at >= '2024-01-01' 
  AND created_at < '2025-01-01';
```

### 3. **Partitioning by Date**
```sql
-- Partition large tables by date range
CREATE TABLE logs (
    id INT NOT NULL,
    log_date DATE NOT NULL,
    message TEXT,
    PRIMARY KEY (id, log_date)
)
PARTITION BY RANGE (YEAR(log_date)) (
    PARTITION p2022 VALUES LESS THAN (2023),
    PARTITION p2023 VALUES LESS THAN (2024),
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION pfuture VALUES LESS THAN MAXVALUE
);
```

---

## 🎯 **Quick Reference Cheat Sheet**

| Task | Function | Example |
|------|----------|---------|
| Current date | `CURDATE()` | `SELECT CURDATE()` → 2024-03-15 |
| Current time | `CURTIME()` | `SELECT CURTIME()` → 14:30:45 |
| Current datetime | `NOW()` | `SELECT NOW()` → 2024-03-15 14:30:45 |
| Add time | `DATE_ADD()` | `DATE_ADD(date, INTERVAL 7 DAY)` |
| Subtract time | `DATE_SUB()` | `DATE_SUB(date, INTERVAL 1 MONTH)` |
| Extract part | `EXTRACT()` | `EXTRACT(YEAR FROM date)` |
| Days between | `DATEDIFF()` | `DATEDIFF(date1, date2)` |
| Format date | `DATE_FORMAT()` | `DATE_FORMAT(date, '%Y-%m-%d')` |
| String to date | `STR_TO_DATE()` | `STR_TO_DATE(str, '%Y-%m-%d')` |
| Format time | `TIME_FORMAT()` | `TIME_FORMAT(time, '%H:%i')` |

---

## 🧪 **Practice Challenges**

1. **Age Calculator**: Calculate accurate age in years, months, and days
2. **Business Days**: Calculate working days between dates (exclude weekends/holidays)
3. **Season Detector**: Determine season based on date (Northern Hemisphere)
4. **Time Zone Converter**: Convert between time zones
5. **Meeting Scheduler**: Find next available 1-hour slot
6. **Birthday Reminder**: Find users with birthdays in next 30 days
7. **Fiscal Period**: Convert dates to custom fiscal periods
8. **Time Series Gaps**: Identify missing dates in time series data
9. **Shift Roster**: Generate employee shift schedule
10. **Subscription Churn**: Identify users likely to cancel