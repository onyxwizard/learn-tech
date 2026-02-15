# 📝 MySQL String Functions - Complete Guide

## 🎯 Overview
String functions in MySQL allow you to manipulate and transform text data. They're essential for data cleaning, formatting, and extraction in database operations.

## 📚 Function Reference

### 1. **CONCAT()**
**Definition**: Concatenates (combines) two or more strings into a single string.

**Syntax**:
```sql
CONCAT(string1, string2, ..., stringN)
```

**Parameters**:
- `string1, string2, ...`: Strings or column names to concatenate

**Examples**:
```sql
-- Basic concatenation
SELECT CONCAT('Hello', ' ', 'World');  -- Returns: Hello World

-- With table data (assuming users table)
SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM users;

-- With numbers (auto-converted to strings)
SELECT CONCAT('User ID: ', id, ' - Name: ', name) AS user_info FROM users;

-- With NULL handling (CONCAT returns NULL if any argument is NULL)
SELECT CONCAT('Hello', NULL, 'World');  -- Returns: NULL

-- Solution: Use CONCAT_WS or COALESCE
SELECT CONCAT_WS(' ', 'Hello', NULL, 'World');  -- Returns: Hello World
```

**Real-World Use Cases**:
- Creating full names from first and last names
- Generating email addresses from usernames
- Building dynamic SQL queries
- Creating display strings from multiple data points

---

### 2. **FORMAT()**
**Definition**: Formats a number with commas as thousand separators and rounds to a specified decimal place.

**Syntax**:
```sql
FORMAT(number, decimal_places)
```

**Parameters**:
- `number`: The number to format
- `decimal_places`: Number of decimal places (0+)

**Examples**:
```sql
-- Basic formatting
SELECT FORMAT(1234567.891, 2);  -- Returns: 1,234,567.89

-- With different decimal places
SELECT FORMAT(1234567.891, 0);  -- Returns: 1,234,568
SELECT FORMAT(1234567.891, 4);  -- Returns: 1,234,567.8910

-- Currency formatting example
SELECT CONCAT('$', FORMAT(price, 2)) AS formatted_price FROM products;

-- Percentage formatting
SELECT CONCAT(FORMAT(score * 100, 1), '%') AS percentage FROM exam_results;

-- With large numbers
SELECT FORMAT(1500000000, 0);  -- Returns: 1,500,000,000
```

**Real-World Use Cases**:
- Financial reports and currency display
- Percentage displays
- Large number readability
- Data export formatting

---

### 3. **LEFT()**
**Definition**: Extracts a specified number of characters from the beginning (left side) of a string.

**Syntax**:
```sql
LEFT(string, number_of_chars)
```

**Parameters**:
- `string`: The source string
- `number_of_chars`: Number of characters to extract (positive integer)

**Examples**:
```sql
-- Basic extraction
SELECT LEFT('MySQL Database', 5);  -- Returns: MySQL

-- Extract first 3 characters from column
SELECT LEFT(product_code, 3) AS product_prefix FROM products;

-- Get initials from name
SELECT CONCAT(LEFT(first_name, 1), LEFT(last_name, 1)) AS initials FROM employees;

-- Safe extraction (avoid errors with short strings)
SELECT LEFT(name, 10) FROM users;  -- Returns up to 10 chars

-- Date part extraction (assuming YYYYMMDD format)
SELECT LEFT(order_date, 4) AS order_year FROM orders;  -- Gets YYYY
```

**Real-World Use Cases**:
- Extracting area codes from phone numbers
- Getting initials from names
- Parsing codes with fixed prefixes
- Truncating long text for previews

---

### 4. **RIGHT()**
**Definition**: Extracts a specified number of characters from the end (right side) of a string.

**Syntax**:
```sql
RIGHT(string, number_of_chars)
```

**Parameters**:
- `string`: The source string
- `number_of_chars`: Number of characters to extract (positive integer)

**Examples**:
```sql
-- Basic extraction
SELECT RIGHT('MySQL Database', 8);  -- Returns: Database

-- Extract last 4 digits
SELECT RIGHT(phone_number, 4) AS last_four FROM customers;

-- File extension extraction
SELECT RIGHT(filename, 3) AS file_extension FROM documents;

-- Get domain from email
SELECT RIGHT(email, LENGTH(email) - POSITION('@' IN email)) AS domain FROM users;

-- Last name extraction (assuming space-separated)
SELECT RIGHT(full_name, LENGTH(full_name) - POSITION(' ' IN full_name)) AS last_name FROM people;
```

**Real-World Use Cases**:
- Extracting file extensions
- Getting last digits of account numbers
- Domain extraction from emails
- Retrieving suffixes from codes

---

### 5. **LOWER()** / **LCASE()**
**Definition**: Converts all characters in a string to lowercase.

**Syntax**:
```sql
LOWER(string)
-- or
LCASE(string)  -- Alias for LOWER()
```

**Parameters**:
- `string`: The string to convert

**Examples**:
```sql
-- Basic conversion
SELECT LOWER('MySQL DATABASE');  -- Returns: mysql database

-- Case-insensitive search
SELECT * FROM products WHERE LOWER(product_name) = LOWER('iPhone');

-- Normalize email input
UPDATE users SET email = LOWER(email);  -- Standardize all emails to lowercase

-- Display formatting
SELECT LOWER(CONCAT(first_name, ' ', last_name)) AS normalized_name FROM employees;

-- URL slug generation
SELECT LOWER(REPLACE(product_name, ' ', '-')) AS slug FROM products;
```

**Real-World Use Cases**:
- Email normalization
- Case-insensitive searches
- URL slug generation
- Data standardization

---

### 6. **UPPER()** / **UCASE()**
**Definition**: Converts all characters in a string to uppercase.

**Syntax**:
```sql
UPPER(string)
-- or
UCASE(string)  -- Alias for UPPER()
```

**Parameters**:
- `string`: The string to convert

**Examples**:
```sql
-- Basic conversion
SELECT UPPER('mysql database');  -- Returns: MYSQL DATABASE

-- Standardize codes
SELECT UPPER(product_code) AS standardized_code FROM products;

-- Display headers
SELECT UPPER(column_name) AS column_header FROM information_schema.columns;

-- Acronym generation
SELECT UPPER(CONCAT(LEFT(first_name, 1), LEFT(last_name, 1))) AS initials FROM employees;

-- Formatting for reports
SELECT CONCAT(UPPER(status), ' - ', order_id) AS status_display FROM orders;
```

**Real-World Use Cases**:
- Standardizing product/SKU codes
- Generating report headers
- Creating acronyms
- Highlighting important text in displays

---

### 7. **SUBSTRING()** / **SUBSTR()** / **MID()**
**Definition**: Extracts a substring from a string (starting at a position).

**Syntax**:
```sql
SUBSTRING(string, start_position, length)
SUBSTRING(string FROM start_position FOR length)
SUBSTR(string, start_position, length)  -- Alias
MID(string, start_position, length)     -- Alias
```

**Parameters**:
- `string`: Source string
- `start_position`: Starting position (1-indexed)
- `length`: Number of characters to extract (optional)

**Examples**:
```sql
-- Basic extraction
SELECT SUBSTRING('MySQL Database', 7, 8);  -- Returns: Database

-- Without length (extracts to end)
SELECT SUBSTRING('MySQL Database', 7);  -- Returns: Database

-- Using FROM...FOR syntax
SELECT SUBSTRING('MySQL Database' FROM 7 FOR 3);  -- Returns: Dat

-- Extract middle name
SELECT SUBSTRING(full_name, 
                 POSITION(' ' IN full_name) + 1,
                 LENGTH(full_name) - POSITION(' ' IN REVERSE(full_name)) - POSITION(' ' IN full_name)
                ) AS middle_name FROM people;

-- Date parsing (YYYY-MM-DD format)
SELECT SUBSTRING(created_at, 1, 4) AS year,
       SUBSTRING(created_at, 6, 2) AS month,
       SUBSTRING(created_at, 9, 2) AS day
FROM users;
```

**Advanced Examples**:
```sql
-- Extract text between parentheses
SELECT SUBSTRING(description, 
                 POSITION('(' IN description) + 1,
                 POSITION(')' IN description) - POSITION('(' IN description) - 1
                ) AS parenthetical FROM notes;

-- Dynamic extraction based on pattern
SELECT SUBSTRING(email, 1, POSITION('@' IN email) - 1) AS username FROM users;
```

**Real-World Use Cases**:
- Parsing structured text
- Extracting parts of codes/IDs
- Text between delimiters
- Variable-length data extraction

---

### 8. **TRIM()**
**Definition**: Removes leading and trailing spaces (or other specified characters) from a string.

**Syntax**:
```sql
TRIM([{BOTH | LEADING | TRAILING} [removal_char] FROM] string)
TRIM(string)  -- Removes spaces from both sides
```

**Parameters**:
- `BOTH | LEADING | TRAILING`: Which side to trim (default: BOTH)
- `removal_char`: Character to remove (default: space)
- `string`: The string to trim

**Examples**:
```sql
-- Basic space removal
SELECT TRIM('   MySQL   ');  -- Returns: 'MySQL'

-- Remove specific characters
SELECT TRIM(BOTH '-' FROM '---MySQL---');  -- Returns: 'MySQL'
SELECT TRIM(LEADING '0' FROM '000123');    -- Returns: '123'
SELECT TRIM(TRAILING '.' FROM 'filename...');  -- Returns: 'filename'

-- Data cleaning example
UPDATE users SET username = TRIM(username);  -- Clean up user inputs

-- Combined with other functions
SELECT TRIM(LOWER(email)) AS clean_email FROM users;
```

**Real-World Use Cases**:
- Cleaning user input data
- Removing padding characters
- Normalizing codes/identifiers
- Preparing data for comparison

---

### 9. **LTRIM()**
**Definition**: Removes leading spaces (or specified characters) from the beginning of a string.

**Syntax**:
```sql
LTRIM(string)
```

**Parameters**:
- `string`: The string to trim

**Examples**:
```sql
-- Basic left trim
SELECT LTRIM('   MySQL');  -- Returns: 'MySQL'

-- Remove leading zeros
SELECT LTRIM('000123', '0');  -- Some databases support char parameter
-- In MySQL, use TRIM(LEADING '0' FROM '000123') for specific chars

-- Clean user input
UPDATE products SET sku = LTRIM(sku) WHERE sku LIKE ' %';

-- File path cleaning
SELECT LTRIM('/path/to/file', '/');  -- Might need REPLACE instead
```

**Real-World Use Cases**:
- Removing indentation
- Cleaning imported data
- Standardizing codes
- Path normalization

---

### 10. **RTRIM()**
**Definition**: Removes trailing spaces (or specified characters) from the end of a string.

**Syntax**:
```sql
RTRIM(string)
```

**Parameters**:
- `string`: The string to trim

**Examples**:
```sql
-- Basic right trim
SELECT RTRIM('MySQL   ');  -- Returns: 'MySQL'

-- Remove trailing slashes
-- MySQL: Use TRIM(TRAILING '/' FROM '/path/to/file/')
SELECT TRIM(TRAILING '/' FROM '/path/to/file/') AS clean_path;

-- Clean sentence endings
SELECT RTRIM(sentence, '.!?') AS clean_sentence FROM documents;

-- Remove line breaks
UPDATE comments SET content = RTRIM(content, '\r\n');
```

**Real-World Use Cases**:
- Removing punctuation
- Cleaning file paths
- Text normalization
- Removing control characters

---

## 🎯 **Common String Function Combinations**

### 1. **Full Name Formatting**
```sql
SELECT CONCAT(
    UPPER(LEFT(first_name, 1)), LOWER(SUBSTRING(first_name, 2)), 
    ' ', 
    UPPER(LEFT(last_name, 1)), LOWER(SUBSTRING(last_name, 2))
) AS formatted_name FROM employees;
```

### 2. **Email Validation/Cleaning**
```sql
SELECT 
    LOWER(TRIM(email)) AS clean_email,
    SUBSTRING_INDEX(LOWER(TRIM(email)), '@', 1) AS username,
    SUBSTRING_INDEX(LOWER(TRIM(email)), '@', -1) AS domain
FROM users;
```

### 3. **Phone Number Formatting**
```sql
SELECT CONCAT(
    '(', LEFT(phone, 3), ') ', 
    SUBSTRING(phone, 4, 3), '-', 
    RIGHT(phone, 4)
) AS formatted_phone FROM customers;
```

### 4. **Address Standardization**
```sql
SELECT 
    UPPER(TRIM(street)) AS street,
    UPPER(TRIM(city)) AS city,
    UPPER(LEFT(state, 2)) AS state_code,
    CONCAT(LEFT(zip, 5), 
           CASE WHEN LENGTH(zip) > 5 THEN CONCAT('-', RIGHT(zip, 4)) ELSE '' END
    ) AS formatted_zip
FROM addresses;
```

### 5. **Product Code Generation**
```sql
SELECT CONCAT(
    UPPER(LEFT(category, 3)),
    '-',
    LPAD(id, 6, '0'),
    '-',
    UPPER(RIGHT(YEAR(created_at), 2))
) AS product_code FROM products;
```

---

## 🔧 **Performance Considerations**

### 1. **Index Usage**
```sql
-- Problem: Functions on indexed columns prevent index usage
SELECT * FROM users WHERE LOWER(username) = 'admin';  -- Won't use index

-- Solution: Store normalized data or use functional indexes (MySQL 8.0+)
ALTER TABLE users ADD INDEX idx_username_lower ((LOWER(username)));
```

### 2. **Character Set and Collation**
```sql
-- Check character set
SHOW VARIABLES LIKE 'character_set%';
SHOW VARIABLES LIKE 'collation%';

-- Case-insensitive comparison alternative
SELECT * FROM products WHERE product_name COLLATE utf8_general_ci = 'iphone';
```

### 3. **NULL Handling Strategies**
```sql
-- CONCAT with NULL returns NULL
SELECT CONCAT('Hello', NULL, 'World');  -- Returns NULL

-- Use CONCAT_WS or COALESCE
SELECT CONCAT_WS(' ', 'Hello', NULL, 'World');  -- Returns 'Hello World'
SELECT CONCAT(COALESCE(first_name, ''), ' ', COALESCE(last_name, '')) FROM users;
```

---

## 🚀 **Practical Framework for Data Cleaning**

```sql
-- Data cleaning template
CREATE TABLE clean_data AS
SELECT 
    -- Text fields
    TRIM(name) AS clean_name,
    LOWER(TRIM(email)) AS clean_email,
    
    -- Phone formatting
    CONCAT(
        '(', LEFT(REPLACE(phone, '-', ''), 3), ') ',
        SUBSTRING(REPLACE(phone, '-', ''), 4, 3), '-',
        RIGHT(REPLACE(phone, '-', ''), 4)
    ) AS formatted_phone,
    
    -- Code standardization
    UPPER(TRIM(product_code)) AS standardized_code,
    
    -- Address cleaning
    UPPER(TRIM(address)) AS clean_address,
    UPPER(TRIM(city)) AS clean_city,
    UPPER(LEFT(state, 2)) AS state_abbr,
    
    -- Text extraction
    SUBSTRING_INDEX(email, '@', 1) AS email_username,
    SUBSTRING_INDEX(email, '@', -1) AS email_domain,
    
    -- Original values (for reference)
    name AS original_name,
    email AS original_email
    
FROM raw_data
WHERE 
    -- Validation
    email LIKE '%@%.%'  -- Basic email format check
    AND LENGTH(TRIM(name)) > 0;
```

---

## 📊 **Real-World Scenarios**

### **Scenario 1: User Registration System**
```sql
-- Clean and validate user input
INSERT INTO users (username, email, display_name)
SELECT 
    LOWER(TRIM(input_username)),
    LOWER(TRIM(input_email)),
    CONCAT(
        UPPER(LEFT(TRIM(input_firstname), 1)),
        LOWER(SUBSTRING(TRIM(input_firstname), 2)),
        ' ',
        UPPER(LEFT(TRIM(input_lastname), 1)),
        LOWER(SUBSTRING(TRIM(input_lastname), 2))
    )
FROM registration_form
WHERE LENGTH(TRIM(input_username)) >= 3
  AND input_email LIKE '%@%.%';
```

### **Scenario 2: Product Catalog Management**
```sql
-- Generate SKU codes
UPDATE products 
SET sku = CONCAT(
    UPPER(LEFT(REPLACE(category, ' ', ''), 4)),
    '-',
    LPAD(id, 6, '0'),
    '-',
    UPPER(SUBSTRING(MD5(product_name), 1, 4))
)
WHERE sku IS NULL;

-- Search with case-insensitive partial match
SELECT * FROM products 
WHERE LOWER(product_name) LIKE LOWER(CONCAT('%', @search_term, '%'))
   OR LOWER(description) LIKE LOWER(CONCAT('%', @search_term, '%'));
```

### **Scenario 3: Report Generation**
```sql
-- Create formatted report data
SELECT 
    CONCAT('INV-', LPAD(id, 8, '0')) AS invoice_number,
    DATE_FORMAT(created_at, '%Y-%m-%d') AS invoice_date,
    CONCAT('$', FORMAT(total_amount, 2)) AS amount,
    CONCAT(
        UPPER(LEFT(status, 1)),
        LOWER(SUBSTRING(status, 2))
    ) AS status_display,
    SUBSTRING_INDEX(email, '@', 1) AS customer_username
FROM invoices
WHERE created_at BETWEEN @start_date AND @end_date
ORDER BY created_at DESC;
```

---

## 🎯 **Quick Reference Cheat Sheet**

| Function | Purpose | Example | Result |
|----------|---------|---------|---------|
| `CONCAT()` | Join strings | `CONCAT('My', 'SQL')` | 'MySQL' |
| `FORMAT()` | Format numbers | `FORMAT(1234.567, 2)` | '1,234.57' |
| `LEFT()` | Left substring | `LEFT('MySQL', 2)` | 'My' |
| `RIGHT()` | Right substring | `RIGHT('MySQL', 3)` | 'SQL' |
| `LOWER()` | To lowercase | `LOWER('MySQL')` | 'mysql' |
| `UPPER()` | To uppercase | `UPPER('mysql')` | 'MYSQL' |
| `SUBSTRING()` | Extract substring | `SUBSTRING('MySQL', 3, 3)` | 'SQL' |
| `TRIM()` | Remove spaces | `TRIM('  text  ')` | 'text' |
| `LTRIM()` | Remove left spaces | `LTRIM('  text')` | 'text' |
| `RTRIM()` | Remove right spaces | `RTRIM('text  ')` | 'text' |

---

## 🔍 **Common Pitfalls & Solutions**

1. **NULL Propagation**
   ```sql
   -- Bad: Returns NULL if any part is NULL
   SELECT CONCAT(first_name, ' ', middle_name, ' ', last_name) FROM people;
   
   -- Good: Handle NULLs
   SELECT CONCAT_WS(' ', first_name, middle_name, last_name) FROM people;
   ```

2. **Character Set Issues**
   ```sql
   -- Specify character set if needed
   SELECT CONCAT(_utf8'Hello', _utf8'World');
   ```

3. **Performance with Large Data**
   ```sql
   -- Avoid functions in WHERE clauses when possible
   -- Instead of:
   SELECT * FROM users WHERE LOWER(username) = 'admin';
   
   -- Consider:
   SELECT * FROM users WHERE username = 'admin' OR username = 'ADMIN';
   -- Or store lowercase version in separate column
   ```

4. **Length Considerations**
   ```sql
   -- SUBSTRING is safe with out-of-bounds positions
   SELECT SUBSTRING('Hello', 10, 5);  -- Returns empty string, not error
   ```

---

## 🧪 **Testing Your Knowledge**
Try these exercises with your database:

1. Create a function that formats a phone number as (XXX) XXX-XXXX
2. Extract the domain from an email address
3. Generate a username from email (part before @)
4. Format a product code: first 3 letters of category + 6-digit ID
5. Clean and standardize address data
6. Create a search function that's case-insensitive
7. Generate report-friendly names (First Last → LAST, First)
8. Parse a full name into first, middle, and last components

---

**Next Up**: Date and Time Functions! 📅

Ready for challenges with string functions? Let me know when you want to practice!