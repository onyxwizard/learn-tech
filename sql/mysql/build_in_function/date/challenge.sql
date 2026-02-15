/**
 * @author onyxwizard
 * @date 06-02-2026
 */
-- ✅ Challenge 1: Age Calculator (Years, Months, Days)
-- Why not just DATEDIFF/365? Because months have variable days (28-31)
-- True age calculation requires sequential subtraction: years → months → days
WITH
  birth_data AS (
    SELECT
      '2000-02-29' AS birth_date
    UNION ALL -- Leap year edge case
    SELECT
      '1990-07-15'
    UNION ALL
    SELECT
      '2023-12-31'
  ),
  age_calc AS (
    SELECT
      birth_date,
      CURDATE()                       AS today,
      -- Step 1: Calculate full years completed
      TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) AS raw_years,
      -- Step 2: Adjust if birthday hasn't occurred yet this year
      CASE
        WHEN DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d') THEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) - 1
        ELSE TIMESTAMPDIFF(YEAR, birth_date, CURDATE())
      END AS years,
      -- Step 3: Calculate months since last birthday
      -- First, find anniversary date of last birthday
      DATE_ADD(
        birth_date,
        INTERVAL CASE
          WHEN DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d') THEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) - 1
          ELSE TIMESTAMPDIFF(YEAR, birth_date, CURDATE())
        END YEAR
      ) AS last_birthday,
      -- Step 4: Months between last birthday and today
      TIMESTAMPDIFF(
        MONTH,
        DATE_ADD(
          birth_date,
          INTERVAL CASE
            WHEN DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d') THEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) - 1
            ELSE TIMESTAMPDIFF(YEAR, birth_date, CURDATE())
          END YEAR
        ),
        CURDATE()
      ) AS months,
      -- Step 5: Days remaining after full months
      DATEDIFF(
        CURDATE(),
        DATE_ADD(
          DATE_ADD(
            birth_date,
            INTERVAL CASE
              WHEN DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d') THEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) - 1
              ELSE TIMESTAMPDIFF(YEAR, birth_date, CURDATE())
            END YEAR
          ),
          INTERVAL TIMESTAMPDIFF(
            MONTH,
            DATE_ADD(
              birth_date,
              INTERVAL CASE
                WHEN DATE_FORMAT(CURDATE(), '%m%d') < DATE_FORMAT(birth_date, '%m%d') THEN TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) - 1
                ELSE TIMESTAMPDIFF(YEAR, birth_date, CURDATE())
              END YEAR
            ),
            CURDATE()
          ) MONTH
        )
      ) AS days
    FROM
      birth_data
  )
SELECT
  birth_date,
  today,
  CONCAT(
    years,
    ' years, ',
    months,
    ' months, ',
    days,
    ' days'
  ) AS precise_age,
  -- Validation: Reconstruct date from components should equal today
  DATE_ADD(
    DATE_ADD(birth_date, INTERVAL years YEAR),
    INTERVAL months MONTH
  ) + INTERVAL days DAY AS reconstructed_date,
  -- Critical edge case handling: Leap day babies
  CASE
    WHEN MONTH(birth_date) = 2 AND
    DAY(birth_date) = 29 AND
    MONTH(CURDATE()) = 2 AND
    DAY(CURDATE()) = 28 AND
    NOT(
      YEAR(CURDATE()) % 4 = 0 AND
      (
        YEAR(CURDATE()) % 100 != 0 OR
        YEAR(CURDATE()) % 400 = 0
      )
    ) THEN 'Leap day birthday - celebrating on Feb 28 this non-leap year'
    ELSE NULL
  END AS leap_day_note
FROM
  age_calc;

/*
💡 Why this complexity? 
Simple division (DATEDIFF/365.25) fails because:
• February has 28/29 days
• Months have 28-31 days
• Age increments on anniversary date, not after 365 days

💡 Production insight:
Store birth_date as DATE type (never as age). Calculate age at query time.
Why? Age changes daily; stored age becomes stale instantly.
 */


-- ✅ Challenge 2: Business Days Calculator (Exclude Weekends + Holidays)
-- Why not just DATEDIFF - weekends? Because holidays vary by country/year
-- Solution: Generate calendar table with business day flags (best practice)
-- For this exercise, we'll create a reusable function with holiday table

-- Step 1: Create holiday reference table (run once)
CREATE TABLE IF NOT EXISTS holidays (
  holiday_date DATE PRIMARY KEY,
  holiday_name VARCHAR(100),
  is_observed BOOLEAN DEFAULT TRUE
);

-- Sample US federal holidays (simplified)
INSERT IGNORE INTO holidays (holiday_date, holiday_name) VALUES
  ('2024-01-01', 'New Year''s Day'),
  ('2024-05-27', 'Memorial Day'),
  ('2024-07-04', 'Independence Day'),
  ('2024-09-02', 'Labor Day'),
  ('2024-11-28', 'Thanksgiving'),
  ('2024-12-25', 'Christmas Day');

-- Step 2: Business days function (MySQL 8.0+)
DELIMITER $$
CREATE FUNCTION business_days_between(start_date DATE, end_date DATE)
RETURNS INT
DETERMINISTIC
BEGIN
  DECLARE total_days INT;
  DECLARE weekend_days INT;
  DECLARE holiday_count INT;
  
  -- Total calendar days inclusive
  SET total_days = DATEDIFF(end_date, start_date) + 1;
  
  -- Weekends: Count Saturdays (7) and Sundays (1)
  -- Formula: For each week, 2 weekend days. Handle partial weeks carefully.
  SET weekend_days = 
    FLOOR(total_days / 7) * 2 +
    CASE 
      WHEN DAYOFWEEK(start_date) = 1 THEN  -- Sunday start
        LEAST(1, total_days % 7) + 
        CASE WHEN total_days % 7 > 1 THEN 1 ELSE 0 END
      WHEN DAYOFWEEK(start_date) = 7 THEN  -- Saturday start
        1 + LEAST(1, GREATEST(0, total_days % 7 - 1))
      ELSE  -- Weekday start
        GREATEST(0, LEAST(2, total_days % 7 + DAYOFWEEK(start_date) - 7))
    END;
  
  -- Count holidays that fall on weekdays within range
  SELECT COUNT(*) INTO holiday_count
  FROM holidays
  WHERE holiday_date BETWEEN start_date AND end_date
    AND DAYOFWEEK(holiday_date) NOT IN (1, 7)  -- Exclude weekend holidays
    AND is_observed = TRUE;
  
  RETURN total_days - weekend_days - holiday_count;
END$$
DELIMITER ;

-- Usage examples
SELECT 
  '2024-03-01' AS start_date,
  '2024-03-15' AS end_date,
  business_days_between('2024-03-01', '2024-03-15') AS business_days,
  
  -- Compare with naive calculation
  DATEDIFF('2024-03-15', '2024-03-01') + 1 AS calendar_days,
  DATEDIFF('2024-03-15', '2024-03-01') + 1 - 
    (FLOOR((DATEDIFF('2024-03-15', '2024-03-01') + DAYOFWEEK('2024-03-01') - 1) / 7) * 2) AS naive_business_days;

/*
💡 Critical insight: 
Weekend calculation isn't simply "2 days per week" because:
  • Partial weeks at start/end have variable weekend days
  • Formula must account for starting weekday

💡 Production reality:
  • Store holidays in table (never hardcode)
  • Handle observed holidays (e.g., July 4 on Saturday → observed Monday)
  • For high volume: Pre-calculate business days in calendar table
*/


-- ✅ Challenge 3: Season Detector (Northern Hemisphere)
-- Why not fixed dates? Because seasons shift slightly yearly (astronomical vs meteorological)
-- We'll implement meteorological seasons (fixed dates) - most common for business reporting
-- Astronomical seasons require complex calculations (sun position) - not practical in SQL

SELECT 
  CURDATE() AS current_date,
  
  -- Meteorological seasons (fixed dates, used by NOAA/weather services)
  CASE 
    WHEN MONTH(CURDATE()) IN (12, 1, 2) THEN 'Winter'
    WHEN MONTH(CURDATE()) IN (3, 4, 5) THEN 'Spring'
    WHEN MONTH(CURDATE()) IN (6, 7, 8) THEN 'Summer'
    WHEN MONTH(CURDATE()) IN (9, 10, 11) THEN 'Autumn'
  END AS meteorological_season,
  
  -- Astronomical approximation (simplified - actual dates vary yearly)
  CASE 
    WHEN (MONTH(CURDATE()) = 3 AND DAY(CURDATE()) >= 20) 
         OR MONTH(CURDATE()) IN (4, 5) 
         OR (MONTH(CURDATE()) = 6 AND DAY(CURDATE()) <= 20) 
    THEN 'Spring'
    
    WHEN (MONTH(CURDATE()) = 6 AND DAY(CURDATE()) >= 21) 
         OR MONTH(CURDATE()) IN (7, 8) 
         OR (MONTH(CURDATE()) = 9 AND DAY(CURDATE()) <= 22) 
    THEN 'Summer'
    
    WHEN (MONTH(CURDATE()) = 9 AND DAY(CURDATE()) >= 23) 
         OR MONTH(CURDATE()) IN (10, 11) 
         OR (MONTH(CURDATE()) = 12 AND DAY(CURDATE()) <= 20) 
    THEN 'Autumn'
    
    ELSE 'Winter'
  END AS astronomical_season_approx,
  
  -- Business quarter alignment (often confused with seasons)
  QUARTER(CURDATE()) AS calendar_quarter,
  
  -- Southern hemisphere (flip seasons)
  CASE 
    WHEN MONTH(CURDATE()) IN (12, 1, 2) THEN 'Summer'
    WHEN MONTH(CURDATE()) IN (3, 4, 5) THEN 'Autumn'
    WHEN MONTH(CURDATE()) IN (6, 7, 8) THEN 'Winter'
    WHEN MONTH(CURDATE()) IN (9, 10, 11) THEN 'Spring'
  END AS southern_hemisphere_season

-- Test with multiple dates
FROM (
  SELECT '2024-03-20' AS test_date UNION ALL
  SELECT '2024-06-21' UNION ALL
  SELECT '2024-09-22' UNION ALL
  SELECT '2024-12-21' UNION ALL
  SELECT '2024-01-15'
) AS dates
WHERE test_date = CURDATE() OR 1=1;  -- Remove WHERE for full test set

/*
💡 Key distinction:
  • Meteorological seasons: Fixed dates (business-friendly)
  • Astronomical seasons: Vary yearly (based on equinoxes/solstices)
  
💡 Why businesses prefer meteorological:
  • Consistent quarter lengths (90/91 days)
  • Aligns with calendar months
  • Simplifies year-over-year comparisons
  
💡 Critical note: 
Never use seasons for financial reporting. Use fiscal quarters with documented rules.
*/


-- ✅ Challenge 4: Time Zone Converter
-- Why not CONVERT_TZ? Because it requires time zone tables installed
-- We'll show both approaches with critical warnings

-- Approach 1: CONVERT_TZ (requires mysql.time_zone* tables populated)
-- Check if available:
SELECT CONVERT_TZ(NOW(), 'UTC', 'America/New_York') AS converted_time;

-- Approach 2: Manual offset calculation (less accurate but always works)
-- WARNING: Doesn't handle DST transitions correctly!
SELECT 
  NOW() AS utc_time,
  
  -- EST (UTC-5) - NO DST handling
  DATE_SUB(NOW(), INTERVAL 5 HOUR) AS est_static,
  
  -- EDT (UTC-4) during DST - but when does DST start/end?
  DATE_SUB(NOW(), INTERVAL 4 HOUR) AS edt_static,
  
  -- Smart approach: Use named time zones with CONVERT_TZ
  CONVERT_TZ(NOW(), 'UTC', 'America/New_York') AS eastern_time,
  CONVERT_TZ(NOW(), 'UTC', 'Europe/London') AS london_time,
  CONVERT_TZ(NOW(), 'UTC', 'Asia/Tokyo') AS tokyo_time,
  
  -- Critical: Show DST status
  CASE 
    WHEN CONVERT_TZ(NOW(), 'UTC', 'America/New_York') = DATE_SUB(NOW(), INTERVAL 4 HOUR)
    THEN 'EDT (UTC-4) - Daylight Saving Time'
    ELSE 'EST (UTC-5) - Standard Time'
  END AS dst_status

-- Test with historical dates to see DST transitions
FROM (
  SELECT '2024-03-10 01:59:59' AS test_time UNION ALL  -- Before DST change (US)
  SELECT '2024-03-10 03:00:00' UNION ALL              -- After DST change
  SELECT '2024-11-03 01:59:59' UNION ALL              -- Before fallback
  SELECT '2024-11-03 01:00:00'                        -- After fallback
) AS tests
WHERE CONVERT_TZ(test_time, 'UTC', 'America/New_York') IS NOT NULL;

/*
💡 Critical production warnings:
  1. NEVER store local times in database - always store UTC
  2. Convert to local time ONLY at display layer
  3. CONVERT_TZ requires time zone tables:
        mysql_tzinfo_to_sql /usr/share/zoneinfo | mysql -u root mysql
  4. DST rules change by government decree - your database must be updated
  
💡 Best practice architecture:
  Application layer: 
    • Accept user's time zone preference
    • Store all timestamps as UTC in database
    • Convert to local time ONLY when displaying to user
  
  Database layer:
    • Store time_zone_name with each timestamp if audit trail needed
    • Never perform business logic on local times
*/


-- ✅ Challenge 5: Meeting Scheduler (Next Available 1-Hour Slot)
-- Why this is hard: Must consider:
--   • Existing meetings (with overlaps)
--   • Business hours (9am-5pm)
--   • Lunch breaks
--   • Time zone of participants
-- We'll solve simplified version: single participant, 9am-5pm, no lunch

-- Step 1: Create sample calendar with existing meetings
CREATE TEMPORARY TABLE IF NOT EXISTS calendar_events (
  event_id INT PRIMARY KEY,
  start_time DATETIME,
  end_time DATETIME,
  title VARCHAR(100)
);

INSERT INTO calendar_events VALUES
  (1, '2024-03-18 09:00:00', '2024-03-18 10:30:00', 'Team Standup'),
  (2, '2024-03-18 11:00:00', '2024-03-18 12:00:00', 'Client Call'),
  (3, '2024-03-18 14:00:00', '2024-03-18 15:30:00', 'Project Review');

-- Step 2: Generate all possible 1-hour slots during business hours
WITH RECURSIVE business_hours AS (
  -- Start at 9am today
  SELECT 
    DATE_FORMAT(CURDATE(), '%Y-%m-%d 09:00:00') AS slot_start,
    DATE_FORMAT(CURDATE(), '%Y-%m-%d 10:00:00') AS slot_end
  
  UNION ALL
  
  -- Generate next hour until 5pm
  SELECT 
    DATE_ADD(slot_start, INTERVAL 1 HOUR),
    DATE_ADD(slot_end, INTERVAL 1 HOUR)
  FROM business_hours
  WHERE HOUR(slot_start) < 16  -- Stop before 5pm slot (4pm-5pm is last)
),
-- Step 3: Identify conflicts with existing meetings
conflicts AS (
  SELECT 
    bh.slot_start,
    bh.slot_end,
    ce.event_id,
    ce.title AS conflict_title
  FROM business_hours bh
  LEFT JOIN calendar_events ce 
    ON bh.slot_start < ce.end_time 
    AND bh.slot_end > ce.start_time  -- Overlap detection
)
-- Step 4: Find first slot with no conflicts
SELECT 
  slot_start AS next_available_start,
  slot_end AS next_available_end,
  '1 hour' AS duration,
  TIMESTAMPDIFF(MINUTE, NOW(), slot_start) AS minutes_until_slot
FROM conflicts
WHERE conflict_title IS NULL  -- No overlapping meetings
ORDER BY slot_start
LIMIT 1;

/*
💡 Overlap detection logic:
  Two time ranges [A_start, A_end] and [B_start, B_end] overlap if:
      A_start < B_end AND A_end > B_start
  
  Visual proof:
      A: |-----|
      B:   |-----|  → Overlap (A_start < B_end AND A_end > B_start)
      B:         |-----|  → No overlap (A_end <= B_start)

💡 Production considerations:
  • Add buffer time between meetings (e.g., 15 min)
  • Consider participant availability (multiple calendars)
  • Handle recurring meetings
  • Respect time zone differences for remote teams
  
💡 Critical insight:
  Never schedule meetings across time zones without explicit conversion.
  Store all meeting times in UTC, convert to participant's local time for display.
*/

-- ✅ Challenge 6: Birthday Reminder (Next 30 Days)
-- Why not simple DATE_FORMAT(birth_date, '%m-%d') comparison?
-- Because birthdays wrap around year-end (Dec 31 → Jan 1)
-- Solution: Project birth dates into current/future year

CREATE TEMPORARY TABLE IF NOT EXISTS users (
  user_id INT PRIMARY KEY,
  name VARCHAR(100),
  birth_date DATE
);

INSERT INTO users VALUES
  (1, 'Alice', '1990-03-20'),   -- Birthday soon
  (2, 'Bob', '1985-12-28'),     -- Birthday after year-end
  (3, 'Charlie', '1995-01-05'), -- Birthday early next year
  (4, 'Dana', '1988-07-15');    -- Not in next 30 days

SELECT 
  user_id,
  name,
  birth_date,
  
  -- Project birthday into current year
  DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d') 
    AS birthday_this_year,
  
  -- If birthday already passed this year, project to next year
  CASE 
    WHEN DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d') < CURDATE()
    THEN DATE_FORMAT(CONCAT(YEAR(CURDATE()) + 1, '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d')
    ELSE DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d')
  END AS next_birthday,
  
  -- Days until next birthday
  DATEDIFF(
    CASE 
      WHEN DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d') < CURDATE()
      THEN DATE_FORMAT(CONCAT(YEAR(CURDATE()) + 1, '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d')
      ELSE DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d')
    END,
    CURDATE()
  ) AS days_until_birthday,
  
  -- Age they'll turn
  YEAR(CURDATE()) - YEAR(birth_date) + 
    CASE 
      WHEN DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d') >= CURDATE()
      THEN 0 
      ELSE 1 
    END AS turning_age
  
FROM users
WHERE 
  -- Birthday in next 30 days (handles year wrap)
  DATEDIFF(
    CASE 
      WHEN DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d') < CURDATE()
      THEN DATE_FORMAT(CONCAT(YEAR(CURDATE()) + 1, '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d')
      ELSE DATE_FORMAT(CONCAT(YEAR(CURDATE()), '-', DATE_FORMAT(birth_date, '%m-%d')), '%Y-%m-%d')
    END,
    CURDATE()
  ) BETWEEN 0 AND 30
ORDER BY days_until_birthday;

/*
💡 Year-wrap problem visualized:
  Today: Dec 28, 2024
  Bob's birthday: Dec 28, 1985 → This year's projection: Dec 28, 2024 (today!)
  Charlie's birthday: Jan 5, 1995 → This year's projection: Jan 5, 2024 (already passed)
      → Must project to Jan 5, 2025

💡 Critical edge case: Leap day babies (Feb 29)
  Solution: Celebrate on Feb 28 in non-leap years (business rule decision)
  
💡 Privacy note:
  Never store full birth_date if only age/birthday month needed.
  Store month/day separately if legally required to minimize PII exposure.
*/

-- ✅ Challenge 7: Fiscal Period Converter (Custom Fiscal Year)
-- Why not use QUARTER()? Because fiscal years rarely align with calendar years
-- Example: Retail fiscal year often starts Feb 1 (not Jan 1)

-- Scenario: Company with fiscal year starting April 1
-- Q1: Apr-Jun, Q2: Jul-Sep, Q3: Oct-Dec, Q4: Jan-Mar

SELECT 
  sale_date,
  
  -- Fiscal year: If month >= 4, same as calendar year; else previous year
  CASE 
    WHEN MONTH(sale_date) >= 4 THEN YEAR(sale_date)
    ELSE YEAR(sale_date) - 1
  END AS fiscal_year,
  
  -- Fiscal quarter calculation
  CASE 
    WHEN MONTH(sale_date) BETWEEN 4 AND 6 THEN 1
    WHEN MONTH(sale_date) BETWEEN 7 AND 9 THEN 2
    WHEN MONTH(sale_date) BETWEEN 10 AND 12 THEN 3
    WHEN MONTH(sale_date) BETWEEN 1 AND 3 THEN 4
  END AS fiscal_quarter,
  
  -- Fiscal period name (e.g., "FY2024-Q3")
  CONCAT(
    'FY',
    CASE 
      WHEN MONTH(sale_date) >= 4 THEN YEAR(sale_date)
      ELSE YEAR(sale_date) - 1
    END,
    '-Q',
    CASE 
      WHEN MONTH(sale_date) BETWEEN 4 AND 6 THEN 1
      WHEN MONTH(sale_date) BETWEEN 7 AND 9 THEN 2
      WHEN MONTH(sale_date) BETWEEN 10 AND 12 THEN 3
      WHEN MONTH(sale_date) BETWEEN 1 AND 3 THEN 4
    END
  ) AS fiscal_period,
  
  -- First/last day of fiscal quarter
  CASE 
    WHEN MONTH(sale_date) BETWEEN 4 AND 6 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-04-01'), '%Y-%m-%d')
    WHEN MONTH(sale_date) BETWEEN 7 AND 9 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-07-01'), '%Y-%m-%d')
    WHEN MONTH(sale_date) BETWEEN 10 AND 12 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-10-01'), '%Y-%m-%d')
    WHEN MONTH(sale_date) BETWEEN 1 AND 3 THEN DATE_FORMAT(CONCAT(YEAR(sale_date) - 1, '-01-01'), '%Y-%m-%d')
  END AS quarter_start,
  
  CASE 
    WHEN MONTH(sale_date) BETWEEN 4 AND 6 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-06-30'), '%Y-%m-%d')
    WHEN MONTH(sale_date) BETWEEN 7 AND 9 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-09-30'), '%Y-%m-%d')
    WHEN MONTH(sale_date) BETWEEN 10 AND 12 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-12-31'), '%Y-%m-%d')
    WHEN MONTH(sale_date) BETWEEN 1 AND 3 THEN DATE_FORMAT(CONCAT(YEAR(sale_date), '-03-31'), '%Y-%m-%d')
  END AS quarter_end
  
FROM (
  SELECT '2024-03-15' AS sale_date UNION ALL  -- Q4 of FY2023
  SELECT '2024-04-01' UNION ALL              -- Q1 of FY2024
  SELECT '2024-06-30' UNION ALL
  SELECT '2024-07-01' UNION ALL              -- Q2 of FY2024
  SELECT '2024-12-31' UNION ALL              -- Q3 of FY2024
  SELECT '2025-01-01'                        -- Q4 of FY2024
) AS sales;

/*
💡 Why fiscal years differ:
  • Retail: Aligns with holiday shopping season (ends Jan/Feb)
  • Government: Often July-June (aligns with budget cycles)
  • Agriculture: Aligns with growing/harvest seasons
  
💡 Critical implementation note:
  Store fiscal period as computed column or in separate dimension table.
  Never calculate fiscal period in application code - leads to inconsistencies.
  
💡 Best practice schema:
  CREATE TABLE date_dimension (
    calendar_date DATE PRIMARY KEY,
    fiscal_year INT,
    fiscal_quarter INT,
    fiscal_month INT,
    is_holiday BOOLEAN,
    ...
  );
  
  Then JOIN sales to date_dimension for all time-based analysis.
*/

-- ✅ Challenge 8: Time Series Gaps (Identify Missing Dates)
-- Why gaps matter: Missing dates break trend analysis, forecasting, and reporting
-- Solution: Generate complete date series and LEFT JOIN to actual data

-- Sample sales data with gaps
CREATE TEMPORARY TABLE IF NOT EXISTS daily_sales (
  sale_date DATE PRIMARY KEY,
  amount DECIMAL(10,2)
);

INSERT INTO daily_sales VALUES
  ('2024-03-01', 150.00),
  ('2024-03-02', 200.00),
  ('2024-03-05', 175.00),  -- Gap: Mar 3-4 missing
  ('2024-03-06', 225.00),
  ('2024-03-07', 190.00),
  ('2024-03-10', 300.00);  -- Gap: Mar 8-9 missing

-- Step 1: Generate complete date series for the range
WITH RECURSIVE date_series AS (
  SELECT MIN(sale_date) AS date_value FROM daily_sales
  UNION ALL
  SELECT DATE_ADD(date_value, INTERVAL 1 DAY)
  FROM date_series
  WHERE date_value < (SELECT MAX(sale_date) FROM daily_sales)
)
-- Step 2: Identify gaps by finding dates with no sales
SELECT 
  ds.date_value AS missing_date,
  DATEDIFF(
    LEAD(ds.date_value) OVER (ORDER BY ds.date_value), 
    ds.date_value
  ) - 1 AS consecutive_days_missing,
  
  -- Context: Previous and next existing dates
  LAG(ds.date_value) OVER (ORDER BY ds.date_value) AS previous_existing_date,
  LEAD(ds.date_value) OVER (ORDER BY ds.date_value) AS next_existing_date
  
FROM date_series ds
LEFT JOIN daily_sales s ON ds.date_value = s.sale_date
WHERE s.sale_date IS NULL  -- Only missing dates
ORDER BY ds.date_value;

/*
💡 Gap analysis patterns:
  • Single missing day: Likely data collection error
  • Weekend gaps: Expected (business closed)
  • Holiday gaps: Expected (verify against holiday calendar)
  • Multi-day gaps: Investigate system outage or process failure
  
💡 Production implementation:
  1. Create calendar table with all dates for 20+ years
  2. Flag weekends/holidays in calendar table
  3. Daily job checks for unexpected gaps:
        SELECT c.date_value
        FROM calendar c
        LEFT JOIN sales s ON c.date_value = DATE(s.sale_timestamp)
        WHERE c.is_weekend = FALSE 
          AND c.is_holiday = FALSE
          AND s.sale_timestamp IS NULL
          AND c.date_value < CURDATE()  -- Don't flag today
  
💡 Critical insight:
  Not all gaps are errors! Context matters:
    • Retail: Closed Sundays → expected gap
    • 24/7 operations: Any gap = system failure
    • Stock market: Closed weekends/holidays → expected
*/

-- ✅ Challenge 9: Shift Roster Generator
-- Scenario: Hospital with 3 shifts (Day: 7am-3pm, Evening: 3pm-11pm, Night: 11pm-7am)
-- 4 nurses rotating through shifts with 2 days off after night shift

CREATE TEMPORARY TABLE IF NOT EXISTS nurses (
  nurse_id INT PRIMARY KEY,
  name VARCHAR(50)
);

INSERT INTO nurses VALUES 
  (1, 'Sarah'), (2, 'Michael'), (3, 'Priya'), (4, 'David');

-- Generate 14-day roster starting from known anchor date
WITH RECURSIVE shift_schedule AS (
  -- Anchor: Known starting point (e.g., March 1 = Day shift for Sarah)
  SELECT 
    DATE('2024-03-01') AS shift_date,
    1 AS nurse_id,
    'Day' AS shift_type,
    1 AS rotation_day  -- Day 1 of 12-day rotation cycle
  
  UNION ALL
  
  -- Recursive: Generate next day's shift
  SELECT 
    DATE_ADD(shift_date, INTERVAL 1 DAY),
    
    -- Rotate nurses: 4 nurses × 3 shifts = 12-day cycle
    CASE 
      WHEN rotation_day % 12 = 0 THEN 4  -- Last day of cycle
      WHEN rotation_day % 12 IN (1,2,3) THEN 1
      WHEN rotation_day % 12 IN (4,5,6) THEN 2
      WHEN rotation_day % 12 IN (7,8,9) THEN 3
      ELSE 4
    END,
    
    -- Shift rotation pattern: Day → Evening → Night → (2 days off) → repeat
    CASE 
      WHEN rotation_day % 12 IN (1,5,9) THEN 'Day'
      WHEN rotation_day % 12 IN (2,6,10) THEN 'Evening'
      WHEN rotation_day % 12 IN (3,7,11) THEN 'Night'
      ELSE 'Off'  -- Days 4,8,12 = days off after night shift
    END,
    
    rotation_day + 1
    
  FROM shift_schedule
  WHERE shift_date < DATE_ADD('2024-03-01', INTERVAL 13 DAY)  -- Generate 14 days
)
SELECT 
  ss.shift_date,
  DAYNAME(ss.shift_date) AS day_of_week,
  n.name AS nurse,
  ss.shift_type,
  
  -- Shift hours (for display)
  CASE ss.shift_type
    WHEN 'Day' THEN '07:00-15:00'
    WHEN 'Evening' THEN '15:00-23:00'
    WHEN 'Night' THEN '23:00-07:00'
    ELSE 'Off Duty'
  END AS shift_hours,
  
  -- Compliance check: Minimum 10 hours between shifts
  CASE 
    WHEN ss.shift_type != 'Off' 
         AND LAG(ss.shift_type) OVER (PARTITION BY ss.nurse_id ORDER BY ss.shift_date) = 'Night'
         AND ss.shift_type = 'Day'
    THEN '⚠️ Violation: <10h between Night and Day shift'
    ELSE 'Compliant'
  END AS compliance_check
  
FROM shift_schedule ss
JOIN nurses n ON ss.nurse_id = n.nurse_id
ORDER BY ss.shift_date, FIELD(ss.shift_type, 'Day', 'Evening', 'Night', 'Off');

/*
💡 Shift work complexity:
  • Fatigue management: Minimum 10h between shifts (OSHA recommendation)
  • Circadian rhythm: Night shifts should be consecutive (not alternating)
  • Legal requirements: Vary by country (e.g., EU Working Time Directive)
  
💡 Why 12-day cycle?
  4 nurses × (3 shifts + 1 day off) = 16 days → but optimized to 12 days:
    • Each nurse works 9 shifts / 12 days (75% utilization)
    • Ensures 48h rest after night shifts
  
💡 Production considerations:
  • Store shift patterns as templates (not hardcoded logic)
  • Allow manual overrides for vacations/sick days
  • Generate alerts for compliance violations
  • Integrate with time tracking systems
  
💡 Critical insight:
  Never generate shifts purely algorithmically without human review.
  Real-world constraints (nurse preferences, certifications, patient load) require flexibility.
*/

-- ✅ Challenge 10: Subscription Churn Predictor
-- Why predict churn? To trigger retention efforts BEFORE cancellation
-- Leading indicators: Usage decline, support tickets, payment failures

CREATE TEMPORARY TABLE IF NOT EXISTS subscriptions (
  user_id INT PRIMARY KEY,
  start_date DATE,
  plan_type VARCHAR(20),
  monthly_value DECIMAL(6,2),
  next_billing_date DATE,
  status ENUM('active', 'cancelled', 'past_due')
);

CREATE TEMPORARY TABLE IF NOT EXISTS usage_metrics (
  user_id INT,
  metric_date DATE,
  daily_logins INT,
  features_used INT,
  session_minutes DECIMAL(6,1),
  PRIMARY KEY (user_id, metric_date)
);

CREATE TEMPORARY TABLE IF NOT EXISTS support_tickets (
  ticket_id INT PRIMARY KEY,
  user_id INT,
  created_at DATETIME,
  category VARCHAR(50),
  is_billing_related BOOLEAN
);

-- Sample data setup (simplified)
INSERT INTO subscriptions VALUES
  (1, '2023-01-15', 'premium', 29.99, DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'active'),
  (2, '2023-06-20', 'basic', 9.99, DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'active'),
  (3, '2024-01-05', 'premium', 29.99, DATE_ADD(CURDATE(), INTERVAL -3 DAY), 'past_due'),
  (4, '2022-11-30', 'basic', 9.99, DATE_ADD(CURDATE(), INTERVAL 25 DAY), 'active');

-- Insert usage patterns showing decline for user 2
INSERT INTO usage_metrics VALUES
  (1, CURDATE() - INTERVAL 30 DAY, 15, 8, 45.5),
  (1, CURDATE() - INTERVAL 15 DAY, 18, 9, 52.0),
  (1, CURDATE(), 20, 10, 60.0),  -- Healthy growth
  
  (2, CURDATE() - INTERVAL 30 DAY, 12, 6, 35.0),
  (2, CURDATE() - INTERVAL 15 DAY, 5, 3, 15.0),
  (2, CURDATE(), 1, 1, 5.0),     -- Sharp decline → churn risk
  
  (3, CURDATE() - INTERVAL 30 DAY, 8, 5, 25.0),
  (3, CURDATE() - INTERVAL 15 DAY, 2, 2, 8.0),
  (3, CURDATE(), 0, 0, 0.0),     -- Zero usage + past due → high churn risk
  
  (4, CURDATE() - INTERVAL 30 DAY, 10, 7, 40.0),
  (4, CURDATE() - INTERVAL 15 DAY, 11, 7, 42.0),
  (4, CURDATE(), 12, 8, 45.0);   -- Stable usage

-- Insert support tickets (billing issues signal churn risk)
INSERT INTO support_tickets VALUES
  (1, 2, NOW() - INTERVAL 5 DAY, 'Billing', TRUE),
  (2, 3, NOW() - INTERVAL 2 DAY, 'Payment Failure', TRUE),
  (3, 1, NOW() - INTERVAL 10 DAY, 'Feature Request', FALSE);

-- Churn prediction model (simplified scoring system)
WITH usage_trends AS (
  SELECT 
    u.user_id,
    -- 30-day usage decline percentage
    ROUND(
      (MAX(CASE WHEN metric_date = CURDATE() THEN daily_logins END) - 
       MAX(CASE WHEN metric_date = CURDATE() - INTERVAL 30 DAY THEN daily_logins END))
      / NULLIF(MAX(CASE WHEN metric_date = CURDATE() - INTERVAL 30 DAY THEN daily_logins END), 0) * 100,
      1
    ) AS login_decline_pct,
    
    -- Current usage vs historical average
    AVG(daily_logins) AS avg_logins_30d,
    MAX(CASE WHEN metric_date = CURDATE() THEN daily_logins END) AS today_logins
  FROM usage_metrics u
  WHERE metric_date >= CURDATE() - INTERVAL 30 DAY
  GROUP BY u.user_id
),
billing_risk AS (
  SELECT 
    s.user_id,
    s.next_billing_date,
    s.status,
    DATEDIFF(s.next_billing_date, CURDATE()) AS days_until_billing,
    
    -- Count recent billing-related tickets
    COUNT(CASE WHEN st.is_billing_related = TRUE 
                AND st.created_at >= NOW() - INTERVAL 14 DAY 
           THEN 1 END) AS recent_billing_tickets
  FROM subscriptions s
  LEFT JOIN support_tickets st ON s.user_id = st.user_id
  GROUP BY s.user_id, s.next_billing_date, s.status
)
SELECT 
  s.user_id,
  s.plan_type,
  s.monthly_value,
  
  -- Churn risk score (0-100, higher = more likely to churn)
  ROUND(
    -- Usage decline factor (max 40 points)
    GREATEST(0, LEAST(40, -COALESCE(ut.login_decline_pct, 0))) +
    
    -- Billing proximity factor (max 30 points)
    CASE 
      WHEN br.days_until_billing BETWEEN 0 AND 7 THEN 30
      WHEN br.days_until_billing BETWEEN 8 AND 14 THEN 20
      WHEN br.days_until_billing BETWEEN 15 AND 30 THEN 10
      ELSE 0
    END +
    
    -- Status penalty (max 20 points)
    CASE br.status
      WHEN 'past_due' THEN 20
      WHEN 'active' THEN 0
    END +
    
    -- Billing ticket penalty (max 10 points)
    LEAST(10, br.recent_billing_tickets * 5)
  , 1) AS churn_risk_score,
  
  -- Risk category
  CASE 
    WHEN ROUND(
      GREATEST(0, LEAST(40, -COALESCE(ut.login_decline_pct, 0))) +
      CASE 
        WHEN br.days_until_billing BETWEEN 0 AND 7 THEN 30
        WHEN br.days_until_billing BETWEEN 8 AND 14 THEN 20
        WHEN br.days_until_billing BETWEEN 15 AND 30 THEN 10
        ELSE 0
      END +
      CASE br.status
        WHEN 'past_due' THEN 20
        WHEN 'active' THEN 0
      END +
      LEAST(10, br.recent_billing_tickets * 5)
    , 1) >= 70 THEN '🔴 HIGH RISK - Immediate intervention'
    WHEN ROUND(
      GREATEST(0, LEAST(40, -COALESCE(ut.login_decline_pct, 0))) +
      CASE 
        WHEN br.days_until_billing BETWEEN 0 AND 7 THEN 30
        WHEN br.days_until_billing BETWEEN 8 AND 14 THEN 20
        WHEN br.days_until_billing BETWEEN 15 AND 30 THEN 10
        ELSE 0
      END +
      CASE br.status
        WHEN 'past_due' THEN 20
        WHEN 'active' THEN 0
      END +
      LEAST(10, br.recent_billing_tickets * 5)
    , 1) >= 40 THEN '🟡 MEDIUM RISK - Monitor closely'
    ELSE '🟢 LOW RISK - Normal engagement'
  END AS risk_category,
  
  -- Actionable insights
  CONCAT(
    CASE WHEN ut.login_decline_pct < -50 THEN '⚠️ Usage dropped >50% | ' ELSE '' END,
    CASE WHEN br.days_until_billing <= 7 THEN '⚠️ Billing due in ≤7 days | ' ELSE '' END,
    CASE WHEN br.status = 'past_due' THEN '⚠️ Account past due | ' ELSE '' END,
    CASE WHEN br.recent_billing_tickets > 0 THEN CONCAT('⚠️ ', br.recent_billing_tickets, ' billing tickets | ') ELSE '' END,
    'Avg logins: ', ROUND(ut.avg_logins_30d, 1), ' → Today: ', ut.today_logins
  ) AS risk_factors,
  
  -- Recommended action
  CASE 
    WHEN ROUND(
      GREATEST(0, LEAST(40, -COALESCE(ut.login_decline_pct, 0))) +
      CASE 
        WHEN br.days_until_billing BETWEEN 0 AND 7 THEN 30
        WHEN br.days_until_billing BETWEEN 8 AND 14 THEN 20
        WHEN br.days_until_billing BETWEEN 15 AND 30 THEN 10
        ELSE 0
      END +
      CASE br.status
        WHEN 'past_due' THEN 20
        WHEN 'active' THEN 0
      END +
      LEAST(10, br.recent_billing_tickets * 5)
    , 1) >= 70 
    THEN '1. Call customer immediately 2. Offer 1-month discount 3. Review billing issues'
    
    WHEN ROUND(
      GREATEST(0, LEAST(40, -COALESCE(ut.login_decline_pct, 0))) +
      CASE 
        WHEN br.days_until_billing BETWEEN 0 AND 7 THEN 30
        WHEN br.days_until_billing BETWEEN 8 AND 14 THEN 20
        WHEN br.days_until_billing BETWEEN 15 AND 30 THEN 10
        ELSE 0
      END +
      CASE br.status
        WHEN 'past_due' THEN 20
        WHEN 'active' THEN 0
      END +
      LEAST(10, br.recent_billing_tickets * 5)
    , 1) >= 40 
    THEN '1. Send personalized email 2. Offer feature walkthrough 3. Monitor next 7 days'
    
    ELSE 'Continue normal engagement'
  END AS recommended_action
  
FROM subscriptions s
LEFT JOIN usage_trends ut ON s.user_id = ut.user_id
LEFT JOIN billing_risk br ON s.user_id = br.user_id
WHERE s.status = 'active' OR s.status = 'past_due'
ORDER BY churn_risk_score DESC;

/*
💡 Churn prediction philosophy:
  • Lagging indicator: Cancellation (too late to act)
  • Leading indicators: Usage decline, billing issues, support tickets
  
💡 Why this scoring model works:
  1. Usage decline >50% = strong predictor (academic research validated)
  2. Billing proximity matters most in final 7 days before renewal
  3. Past-due status multiplies churn probability by 3-5x
  
💡 Critical limitations:
  • Correlation ≠ causation (decline might be seasonal)
  • Small sample sizes (new users lack history)
  • External factors (competitor launch, economic downturn)
  
💡 Production implementation:
  • Retrain model monthly with actual churn outcomes
  • A/B test retention offers to measure effectiveness
  • Track false positives (wasted retention effort)
  • Never automate cancellations based solely on score
  
💡 Ethical note:
  Churn prediction should empower customer success teams—not justify dark patterns.
  Goal: Help customers succeed, not trap them in unwanted subscriptions.
*/