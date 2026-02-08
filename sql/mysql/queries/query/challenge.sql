-- ============================================
-- 🏰 LEVEL 1: CREATING THE DRAGON KINGDOM
-- ============================================

-- 🎮 CREATE the dragon quest database (our game world)
CREATE DATABASE final_quest;
-- ✅ Creates a new database named 'final_quest' where all our dragon data will live

-- 👁️ SHOW all available databases (see what worlds exist)
SHOW DATABASES;
-- ✅ Displays: information_schema, final_quest, mysql, performance_schema, sys

-- 🚀 ENTER the dragon quest world
USE final_quest;
-- ✅ Switches our current session to work in the 'final_quest' database

-- 🧭 VERIFY which database we're currently in
SELECT DATABASE();
-- ✅ Returns: final_quest (confirms we're in the right place)

-- ============================================
-- 🐲 LEVEL 2: FORGING THE DRAGON TABLE
-- ============================================

-- 🛠️ CREATE the dragons table (our main monster storage)
CREATE TABLE dragon(
    dragon_id INT AUTO_INCREMENT,            -- 🆔 Auto-generating ID (1, 2, 3...)
    dragon_name VARCHAR(50),                  -- 📛 Dragon's name (max 50 characters)
    element_type VARCHAR(50),                 -- 🔥 Element type (Fire, Ice, etc.)
    health INT,                               -- ❤️ Health points (whole number)
    treasure_guarded DECIMAL(7,2),           -- 💰 Treasure amount (max 99999.99)
    PRIMARY KEY(dragon_id)                    -- 🔑 Unique identifier for each dragon
);
-- ✅ Table created with 5 columns including a primary key

-- 📋 LIST all tables in our database
SHOW TABLES;
-- ✅ Returns: dragon (our newly created table)

-- 🔍 EXAMINE the dragon table structure
DESC dragon;
-- ✅ Shows: column names, types, constraints (like a blueprint)

-- 🔧 FIX the treasure column precision (already correct, but good practice)
ALTER TABLE dragon MODIFY COLUMN treasure_guarded DECIMAL(7,2);
-- ✅ Ensures treasure column can store up to 99,999.99 gold

-- ============================================
-- 🐉 LEVEL 3: SUMMONING THE DRAGONS
-- ============================================

-- ✨ INSERT three legendary dragons into our table
INSERT INTO dragon (dragon_name, element_type, health, treasure_guarded)
VALUES 
    ('Ember', 'Fire', 5000, 15000.50),       -- 🔥 Fire dragon with 5K health
    ('Glacier', 'Ice', 4500, 12000.00),      -- ❄️ Ice dragon with 4.5K health
    ('Volt', 'Lightning', 4800, 18000.75);   -- ⚡ Lightning dragon with 4.8K health
-- ✅ 3 dragons successfully added to our collection

-- 👀 VIEW all dragons in our database
SELECT * FROM dragon;
-- ✅ Returns:
-- | dragon_id | dragon_name | element_type | health | treasure_guarded |
-- |-----------|-------------|--------------|--------|------------------|
-- | 1         | Ember       | Fire         | 5000   | 15000.50         |
-- | 2         | Glacier     | Ice          | 4500   | 12000.00         |
-- | 3         | Volt        | Lightning    | 4800   | 18000.75         |

-- ============================================
-- ⚔️ LEVEL 4: TRAINING & EVOLVING DRAGONS
-- ============================================

-- 📈 UPGRADE all dragons' health by 20% (they're training!)
UPDATE dragon SET health = health + ((health * 20) / 100);
-- ✅ Increases each dragon's health by 20%
-- 🧮 Math: health = health + (health * 0.20) = health * 1.20

-- 👀 CHECK the upgraded dragon stats
SELECT * FROM dragon;
-- ✅ Returns (health increased):
-- | dragon_id | dragon_name | element_type | health | treasure_guarded |
-- |-----------|-------------|--------------|--------|------------------|
-- | 1         | Ember       | Fire         | 6000   | 15000.50         |  (5000 * 1.2)
-- | 2         | Glacier     | Ice          | 5400   | 12000.00         |  (4500 * 1.2)
-- | 3         | Volt        | Lightning    | 5760   | 18000.75         |  (4800 * 1.2)

-- 🎭 ADD a new column for dragon weaknesses (every hero needs to know!)
ALTER TABLE dragon ADD COLUMN weakness VARCHAR(50);
-- ✅ Adds 'weakness' column to track what each dragon is vulnerable to
-- 📝 New structure: | dragon_id | name | element | health | treasure | weakness |

-- ============================================
-- ⚠️ LEVEL 5: DANGER! DRAGON PURGE
-- ============================================

-- 🔍 FIRST: Always check which dragons will be affected (SAFETY FIRST!)
SELECT * FROM dragon WHERE health < 5500;
-- ✅ Shows which dragons have less than 5500 health
-- Returns: Glacier (5400) and Volt (5760) - WAIT! Volt has 5760 (>5500)

-- ❌ DELETE weak dragons (health < 5500)
DELETE FROM dragon WHERE health < 5500;
-- ✅ Removes: Glacier (5400 health) 
-- ❗ IMPORTANT: ONLY Glacier is deleted, Volt stays (5760 > 5500)

-- 👀 VERIFY remaining dragons after purge
SELECT * FROM dragon;
-- ✅ Returns:
-- | dragon_id | dragon_name | element_type | health | treasure_guarded | weakness |
-- |-----------|-------------|--------------|--------|------------------|----------|
-- | 1         | Ember       | Fire         | 6000   | 15000.50         | NULL     |
-- | 3         | Volt        | Lightning    | 5760   | 18000.75         | NULL     |
-- Note: Weakness column is NULL (not set yet)

-- ============================================
-- 🧪 LEVEL 6: TESTING & BACKUP SYSTEMS
-- ============================================

-- 🧪 CREATE a test table (practice before real operations)
CREATE TABLE dragon_test (
    id INT  -- Simple test table with one column
);
-- ✅ Creates temporary table for testing purposes

-- 📋 CHECK all tables including our test table
SHOW TABLES;
-- ✅ Returns: dragon, dragon_test

-- 💥 DESTROY the test table (cleanup after testing)
DROP TABLE dragon_test;
-- ✅ Permanently removes the test table
-- ⚠️ WARNING: DROP is permanent! All data in dragon_test is gone forever!

-- 📋 VERIFY test table is gone
SHOW TABLES;
-- ✅ Returns: dragon (only our main table remains)

-- ============================================
-- 💾 LEVEL 7: CREATING BACKUPS (SMART PRACTICE!)
-- ============================================

-- 💿 CREATE a backup of our dragon table (DISASTER RECOVERY!)
CREATE TABLE backup_dragon AS
SELECT * FROM dragon;
-- ✅ Creates an exact copy of our dragon table with all data
-- 📊 This is a SNAPSHOT backup - perfect for emergency restores

-- 📋 CONFIRM backup was created
SHOW TABLES;
-- ✅ Returns: dragon, backup_dragon (we now have a backup!)

-- 👀 VIEW the backup data
SELECT * FROM backup_dragon;
-- ✅ Returns exact copy of our dragon table:
-- | dragon_id | dragon_name | element_type | health | treasure_guarded | weakness |
-- |-----------|-------------|--------------|--------|------------------|----------|
-- | 1         | Ember       | Fire         | 6000   | 15000.50         | NULL     |
-- | 3         | Volt        | Lightning    | 5760   | 18000.75         | NULL     |