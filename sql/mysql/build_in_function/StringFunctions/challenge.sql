/**
 * @author onyxwizard
 * @date 06-02-2026
 */

-- ✅ Exercise 1: Format phone number as (XXX) XXX-XXXX
-- Why SUBSTRING over LEFT/RIGHT? 
-- Phone numbers often have inconsistent formatting (e.g., "123-456-7890" or "+11234567890").
-- First, we normalize by removing non-digits, THEN format consistently.
SELECT 
  CONCAT(
    '(',
    SUBSTRING(REPLACE(REPLACE(phone_raw, '-', ''), ' ', ''), 1, 3),  -- Extract area code after cleaning
    ') ',
    SUBSTRING(REPLACE(REPLACE(phone_raw, '-', ''), ' ', ''), 4, 3),  -- Middle segment
    '-',
    SUBSTRING(REPLACE(REPLACE(phone_raw, '-', ''), ' ', ''), 7, 4)   -- Last 4 digits
  ) AS formatted_phone
FROM (
  SELECT '123-456-7890' AS phone_raw UNION ALL
  SELECT '  9876543210  ' UNION ALL
  SELECT '(555)1234567'
) AS sample_data;

/*
💡 Key insight: Never assume input format. 
Always cleanse first (remove spaces/dashes) before extracting segments.
This prevents brittle logic that breaks on real-world messy data.
*/

-- ✅ Exercise 2: Extract domain from email address
-- Why SUBSTRING_INDEX instead of POSITION + SUBSTRING?
-- SUBSTRING_INDEX handles edge cases elegantly:
--   • No '@' → returns entire string (safer than POSITION which returns 0)
--   • Multiple '@' → still works correctly (e.g., "user@mail@server.com")
SELECT 
  LOWER(
    TRIM(
      SUBSTRING_INDEX(email, '@', -1)  -- -1 gets everything AFTER last '@'
    )
  ) AS domain
FROM (
  SELECT 'User@Gmail.COM' AS email UNION ALL
  SELECT 'support@sub.domain.co.uk' UNION ALL
  SELECT 'invalid-email'  -- Edge case test
) AS sample_data;

/*
💡 Critical nuance: 
Domains are case-insensitive per RFC standards, so LOWER() is mandatory.
TRIM() handles accidental whitespace (common in form submissions).
*/

-- ✅ Exercise 3: Generate username from email (part before @)
-- Why not use LEFT(email, POSITION('@'...)-1)? 
-- Because POSITION returns 0 if '@' missing → LEFT(email, -1) causes errors.
-- SUBSTRING_INDEX safely returns entire string if delimiter absent.
SELECT 
  LOWER(
    TRIM(
      SUBSTRING_INDEX(email, '@', 1)  -- 1 gets everything BEFORE first '@'
    )
  ) AS username
FROM (
  SELECT '  John.Doe@Example.COM  ' AS email UNION ALL
  SELECT 'admin'  -- Edge case: no '@' → returns 'admin' (graceful degradation)
) AS sample_data;

/*
💡 Production tip: 
Always validate emails BEFORE extraction in application layer.
This SQL handles degradation but shouldn't replace proper validation.
*/

-- ✅ Exercise 4: Format product code: first 3 letters of category + 6-digit ID
-- Why LPAD instead of CONCAT with zeros?
-- LPAD guarantees exact width (critical for SKUs). 
-- CONCAT('000', id) fails if id > 999 → produces 7+ digits.
SELECT 
  CONCAT(
    UPPER(LEFT(TRIM(category), 3)),  -- Standardize case + handle whitespace
    '-',
    LPAD(id, 6, '0'),                -- Always 6 digits (e.g., 42 → '000042')
    '-',
    UPPER(RIGHT(YEAR(created_at), 2)) -- Last 2 digits of year
  ) AS product_code
FROM (
  SELECT 'electronics' AS category, 42 AS id, '2026-02-07' AS created_at UNION ALL
  SELECT '  books  ', 12345, '2025-12-31'
) AS products;

/*
💡 Why TRIM before LEFT? 
User-entered categories often have leading/trailing spaces ("  books  ").
Without TRIM, LEFT would capture spaces → "  b" instead of "boo".
*/

-- ✅ Exercise 5: Clean and standardize address data
-- Why chain multiple functions? Real addresses have layered messiness:
--   • Mixed case ("123 Main St")
--   • Extra spaces ("123  Main   St")
--   • Abbreviations ("St." vs "Street")
SELECT 
  CONCAT(
    UPPER(TRIM(street_line1)),  -- Normalize case + remove padding
    IF(TRIM(street_line2) != '', CONCAT(', ', UPPER(TRIM(street_line2))), ''),
    ', ',
    UPPER(TRIM(city)),
    ', ',
    UPPER(LEFT(TRIM(state), 2)),  -- Standardize to 2-letter codes
    ' ',
    -- Handle ZIP+4 format: "12345-6789" → keep as-is; "123456789" → format
    CASE 
      WHEN LENGTH(REPLACE(zip, '-', '')) = 9 
        THEN CONCAT(LEFT(REPLACE(zip, '-', ''), 5), '-', RIGHT(REPLACE(zip, '-', ''), 4))
      ELSE LEFT(REPLACE(zip, '-', ''), 5)  -- Truncate to 5 digits if invalid
    END
  ) AS standardized_address
FROM (
  SELECT 
    '  123 main st  ' AS street_line1,
    '  Apt 4B ' AS street_line2,
    'new york' AS city,
    'ny' AS state,
    '10001' AS zip
) AS raw_address;

/*
💡 Philosophy: 
Standardization isn't about perfection—it's about consistency.
We make deliberate tradeoffs (e.g., truncating invalid ZIPs) to ensure downstream systems receive predictable formats.
*/

-- ✅ Exercise 6: Create case-insensitive search function
-- Why NOT use LOWER(column) in WHERE clause?
-- It prevents index usage → full table scan on large tables.
-- Better approaches (in order of preference):
--   1. Use case-insensitive collation (best performance)
--   2. Store normalized copy in separate column
--   3. Only use LOWER() when unavoidable

-- Approach 1: Collation-based (recommended for MySQL 8.0+)
SELECT product_name 
FROM products 
WHERE product_name COLLATE utf8mb4_general_ci = 'iphone 15';

-- Approach 2: Normalized column (for older MySQL)
-- ALTER TABLE products ADD COLUMN search_name VARCHAR(255) 
--   AS (LOWER(TRIM(product_name))) STORED;
-- CREATE INDEX idx_search_name ON products(search_name);
SELECT product_name 
FROM products 
WHERE search_name = LOWER(TRIM('iPhone 15'));

-- Approach 3: Fallback for ad-hoc queries (avoid in production)
SELECT product_name 
FROM products 
WHERE LOWER(product_name) LIKE LOWER('%iphone%');

/*
💡 Socratic question: 
Why does Approach 1 outperform Approach 3?
→ Because collation-aware comparisons use indexes; function-wrapped columns cannot.
This reveals a deeper principle: *data modeling decisions impact query performance more than clever SQL*.
*/

-- ✅ Exercise 7: Generate report-friendly names (First Last → LAST, First)
-- Why not assume exactly one space between names?
-- Real data has: "Mary Jane Smith" (multiple spaces), "Cher" (no space), "de la Cruz" (prefixes)
SELECT 
  CONCAT(
    UPPER(
      SUBSTRING_INDEX(TRIM(full_name), ' ', -1)  -- Last token = last name
    ),
    ', ',
    -- First name = everything before last space
    SUBSTRING(
      TRIM(full_name), 
      1, 
      LENGTH(TRIM(full_name)) - LENGTH(SUBSTRING_INDEX(TRIM(full_name), ' ', -1)) - 1
    )
  ) AS report_name
FROM (
  SELECT 'John Doe' AS full_name UNION ALL
  SELECT '  Mary Jane   Smith  ' UNION ALL
  SELECT 'Cher'  -- Edge case: single name
) AS people;

/*
💡 Handling edge cases:
- For "Cher": SUBSTRING_INDEX(..., ' ', -1) = 'Cher', then SUBSTRING calculates negative length → returns empty string
- Result: "CHER, " → acceptable degradation (better than crashing)
- Production systems would add validation: WHERE LENGTH(TRIM(full_name)) - LENGTH(REPLACE(full_name, ' ', '')) >= 1
*/

-- ✅ Exercise 8: Parse full name into first, middle, last components
-- Why this approach? Names have 3 patterns:
--   Pattern A: "First Last"          → 2 tokens
--   Pattern B: "First Middle Last"   → 3+ tokens (middle may be compound)
--   Pattern C: "First M Last"        → 3 tokens with initial
WITH name_tokens AS (
  SELECT 
    full_name,
    TRIM(full_name) AS clean_name,
    LENGTH(TRIM(full_name)) - LENGTH(REPLACE(TRIM(full_name), ' ', '')) + 1 AS token_count
  FROM (
    SELECT 'John Michael Doe' AS full_name UNION ALL
    SELECT 'Sarah  Connor' UNION ALL
    SELECT 'Robert James Lee Smith'
  ) AS raw_names
)
SELECT
  full_name,
  
  -- First name = first token
  SUBSTRING_INDEX(clean_name, ' ', 1) AS first_name,
  
  -- Middle name = everything between first and last token (if exists)
  CASE 
    WHEN token_count > 2 
    THEN TRIM(
      SUBSTRING(
        clean_name,
        LENGTH(SUBSTRING_INDEX(clean_name, ' ', 1)) + 2,  -- After first name + space
        LENGTH(clean_name) - 
        LENGTH(SUBSTRING_INDEX(clean_name, ' ', 1)) - 
        LENGTH(SUBSTRING_INDEX(clean_name, ' ', -1)) - 2  -- Before last name
      )
    )
    ELSE NULL 
  END AS middle_name,
  
  -- Last name = last token
  SUBSTRING_INDEX(clean_name, ' ', -1) AS last_name
  
FROM name_tokens;

/*
💡 Critical limitation: 
This heuristic fails for:
  • Compound last names ("de la Hoya")
  • Prefixes ("Dr. John Smith")
  • Non-Western name orders ("Kim Young Sam" → Korean order: family name first)

💡 Deeper lesson: 
No string function can perfectly parse names. 
Best practice: Collect first/middle/last as separate fields during data entry.
SQL parsing should only be used for legacy data migration—not as a primary strategy.
*/

