# 🎮 MySQL Quest: The Database Adventure Game

## 🏰 Welcome to SQL Kingdom!

Welcome, brave adventurer! You're about to embark on a quest through the magical lands of MySQL. Each level will teach you a powerful SQL spell (query) that you'll need to complete your quest!

## 🎯 **Mission Briefing**
You've been summoned by the **Data Knights Guild** to help restore order to the Kingdom of Information. The ancient data scrolls have been scattered, and only your SQL skills can help recover them!

---

## 📚 **Level 1: CREATE DATABASE - Building Your Castle**

### ✨ **Spell Power**: Creates a new kingdom (database) to store all your treasures (data)

```sql
-- 📖 SPELL SCROLL
CREATE DATABASE kingdom_name;

-- 🎯 PRACTICE CASTING
CREATE DATABASE dragon_quest;
```
✅ **Success!** You've created the Kingdom of Dragon Quest!

### 🧩 **Challenge 1: Foundation of Power**
```
The guild needs you to create a new base of operations called "heroes_guild".
Write the spell to create this database.
```

<details>
<summary>✨ Need a hint?</summary>
Remember the pattern: CREATE DATABASE [name];
</details>

<details>
<summary>🎯 Solution</summary>

```sql
CREATE DATABASE heroes_guild;
```
**Reward**: 🛡️ +10 SQL Experience Points
</details>

---

## 🗺️ **Level 2: USE DATABASE - Choosing Your Battlefield**

### ✨ **Spell Power**: Teleports you to the kingdom where you want to work

```sql
-- 📖 SPELL SCROLL
USE database_name;

-- 🎯 PRACTICE CASTING
USE dragon_quest;
```
✅ **Success!** You're now in the Dragon Quest kingdom!

### 🧩 **Challenge 2: Enter the Arena**
```
Before you can face the data dragons, you need to enter the "heroes_guild" database.
Teleport yourself there!
```

<details>
<summary>✨ Need a hint?</summary>
The USE command takes you to a database, just like entering a room
</details>

<details>
<summary>🎯 Solution</summary>

```sql
USE heroes_guild;
```
**Reward**: 🧭 +10 Navigation Points
</details>

---

## 🏗️ **Level 3: CREATE TABLE - Forging Your Armory**

### ✨ **Spell Power**: Creates a new storage chest (table) for specific items (data)

```sql
-- 📖 SPELL SCROLL
CREATE TABLE table_name (
    column1 datatype,
    column2 datatype,
    ...
);

-- 🎯 PRACTICE CASTING
CREATE TABLE heroes (
    hero_id INT PRIMARY KEY,
    hero_name VARCHAR(50),
    hero_class VARCHAR(30),
    hero_level INT,
    join_date DATE
);
```
✅ **Success!** You've created a heroes registry!

### 🧩 **Challenge 3: Armory Creation**
```
The guild needs an inventory system. Create a table called "weapons" with:
- weapon_id (integer, primary key)
- weapon_name (text, up to 100 characters)
- damage (integer)
- weight (decimal with 5,2 format)
- special_ability (text, up to 200 characters)
```

<details>
<summary>✨ Need a hint?</summary>
INT = whole numbers, VARCHAR = text, DECIMAL = decimal numbers
</details>

<details>
<summary>🎯 Solution</summary>

```sql
CREATE TABLE weapons (
    weapon_id INT PRIMARY KEY,
    weapon_name VARCHAR(100),
    damage INT,
    weight DECIMAL(5,2),
    special_ability VARCHAR(200)
);
```
**Reward**: ⚔️ +20 Crafting Skill
</details>

---

## 📝 **Level 4: INSERT Query - Recruiting Your Army**

### ✨ **Spell Power**: Adds new warriors (records) to your tables

```sql
-- 📖 SPELL SCROLL
INSERT INTO table_name (column1, column2, ...)
VALUES (value1, value2, ...);

-- 🎯 PRACTICE CASTING
INSERT INTO heroes (hero_id, hero_name, hero_class, hero_level, join_date)
VALUES (1, 'Sir Arthur', 'Paladin', 45, '2023-01-15');
```

### 🧩 **Challenge 4: Recruit Three Heroes**
```
Add these heroes to your heroes table:
1. ID: 2, Name: 'Mage Lyn', Class: 'Wizard', Level: 38, Joined: '2023-02-20'
2. ID: 3, Name: 'Ranger Finn', Class: 'Archer', Level: 42, Joined: '2023-03-10'
3. ID: 4, Name: 'Berserker Thrain', Class: 'Warrior', Level: 50, Joined: '2023-01-05'
```

<details>
<summary>🎯 Solution</summary>

```sql
-- Hero 1
INSERT INTO heroes (hero_id, hero_name, hero_class, hero_level, join_date)
VALUES (2, 'Mage Lyn', 'Wizard', 38, '2023-02-20');

-- Hero 2
INSERT INTO heroes (hero_id, hero_name, hero_class, hero_level, join_date)
VALUES (3, 'Ranger Finn', 'Archer', 42, '2023-03-10');

-- Hero 3
INSERT INTO heroes (hero_id, hero_name, hero_class, hero_level, join_date)
VALUES (4, 'Berserker Thrain', 'Warrior', 50, '2023-01-05');
```
**Reward**: 👥 +30 Leadership Points
</details>

---

## 🔄 **Level 5: UPDATE Query - Training Your Troops**

### ✨ **Spell Power**: Improves your existing warriors (modifies records)

```sql
-- 📖 SPELL SCROLL
UPDATE table_name
SET column1 = value1, column2 = value2, ...
WHERE condition;

-- 🎯 PRACTICE CASTING
UPDATE heroes
SET hero_level = 46
WHERE hero_name = 'Sir Arthur';
```

### 🧩 **Challenge 5: Promote Your Heroes**
```
1. Sir Arthur has trained hard! Increase his level to 55
2. Mage Lyn has specialized in fire magic. Change her class to 'Fire Mage'
3. All heroes who joined before February 2023 get +5 levels
```

<details>
<summary>🎯 Solution</summary>

```sql
-- 1. Sir Arthur levels up
UPDATE heroes
SET hero_level = 55
WHERE hero_name = 'Sir Arthur';

-- 2. Mage Lyn specializes
UPDATE heroes
SET hero_class = 'Fire Mage'
WHERE hero_name = 'Mage Lyn';

-- 3. Early joiners bonus
UPDATE heroes
SET hero_level = hero_level + 5
WHERE join_date < '2023-02-01';
```
**Reward**: 📈 +25 Training Points
</details>

---

## 🔧 **Level 6: ALTER Query - Enhancing Your Armory**

### ✨ **Spell Power**: Modifies the structure of your storage chests (tables)

```sql
-- 📖 SPELL SCROLL
ALTER TABLE table_name
ADD column_name datatype;

-- 🎯 PRACTICE CASTING
ALTER TABLE heroes
ADD COLUMN gold_coins INT DEFAULT 100;
```

### 🧩 **Challenge 6: Upgrade Your Systems**
```
1. Add a 'health_points' column (integer) to the heroes table
2. Add a 'magic_type' column (text, max 50 chars) to the weapons table
3. Add a default value of 0 for the new health_points column
```

<details>
<summary>🎯 Solution</summary>

```sql
-- 1. Add health points to heroes
ALTER TABLE heroes
ADD COLUMN health_points INT;

-- 2. Add magic type to weapons
ALTER TABLE weapons
ADD COLUMN magic_type VARCHAR(50);

-- 3. Set default health (Note: MySQL handles default differently)
ALTER TABLE heroes
ALTER COLUMN health_points SET DEFAULT 0;
-- Or for adding with default:
ALTER TABLE heroes
ADD COLUMN health_points INT DEFAULT 0;
```
**Reward**: 🛠️ +15 Engineering Skill
</details>

---

## 🗑️ **Level 7: DELETE Query - Expelling Traitors**

### ✨ **Spell Power**: Removes specific warriors (records) from your ranks

```sql
-- 📖 SPELL SCROLL
DELETE FROM table_name WHERE condition;

-- 🎯 PRACTICE CASTING
DELETE FROM heroes WHERE hero_id = 3;
```

### 🧩 **Challenge 7: Clean Up the Ranks**
```
1. Remove any hero with level below 30
2. Remove the hero named 'Berserker Thrain'
3. Remove all heroes who joined after March 2023
```

<details>
<summary>⚠️ Danger Zone</summary>
BE CAREFUL! DELETE removes data permanently! Always test with SELECT first:

```sql
-- First check what you'll delete:
SELECT * FROM heroes WHERE hero_level < 30;

-- If correct, then delete:
DELETE FROM heroes WHERE hero_level < 30;
```
</details>

<details>
<summary>🎯 Solution</summary>

```sql
-- 1. Remove low-level heroes
DELETE FROM heroes WHERE hero_level < 30;

-- 2. Remove specific hero
DELETE FROM heroes WHERE hero_name = 'Berserker Thrain';

-- 3. Remove recent joiners
DELETE FROM heroes WHERE join_date > '2023-03-31';
```
**Reward**: 🧹 +10 Cleanup Efficiency
</details>

---

## 🧹 **Level 8: TRUNCATE TABLE - Clearing the Training Grounds**

### ✨ **Spell Power**: Removes ALL warriors but keeps the training grounds (table structure)

```sql
-- 📖 SPELL SCROLL
TRUNCATE TABLE table_name;

-- 🎯 PRACTICE CASTING
TRUNCATE TABLE temp_recruits;
```

### 🧩 **Challenge 8: Reset Practice Area**
```
You have a practice table called 'training_dummies' filled with test data.
Clear all the dummy data but keep the table structure for future training.
```

<details>
<summary>🎯 Solution</summary>

```sql
TRUNCATE TABLE training_dummies;
```
**Reward**: 🔄 +20 Reset Power
</details>

---

## 💥 **Level 9: DROP Query - Destroying Enemy Forts**

### ✨ **Spell Power**: Completely destroys a storage chest (table) - BE CAREFUL!

```sql
-- 📖 SPELL SCROLL
DROP TABLE table_name;

-- 🎯 PRACTICE CASTING
DROP TABLE old_logs;
```

### 🧩 **Challenge 9: Demolish Abandoned Structures**
```
The enemy has abandoned their 'orc_camp' table. 
Completely destroy it so they can't reuse it.
```

<details>
<summary>⚠️ EXTREME DANGER!</summary>
DROP TABLE is PERMANENT! The table AND all its data are GONE FOREVER!
</details>

<details>
<summary>🎯 Solution</summary>

```sql
DROP TABLE orc_camp;
```
**Reward**: 💥 +25 Demolition Expert
</details>

---

## 🏆 **FINAL BOSS CHALLENGE**

### 🐉 **The Database Dragon's Lair**

```
The final boss has appeared! Complete this multi-step quest to defeat the Database Dragon:

1. Create a database called "final_quest"
2. Enter that database
3. Create a "dragons" table with:
   - dragon_id (INT, primary key)
   - dragon_name (VARCHAR)
   - element_type (VARCHAR)
   - health (INT)
   - treasure_guarded (DECIMAL)
4. Insert 3 dragons:
   - Ember, Fire, 5000, 15000.50
   - Glacier, Ice, 4500, 12000.00
   - Volt, Lightning, 4800, 18000.75
5. Update: Increase all dragon health by 20%
6. Alter: Add a "weakness" column
7. Delete any dragon with health less than 1000
8. (Optional) If you're feeling brave: DROP a test table you create
```

<details>
<summary>🎯 Complete Solution</summary>

```sql
-- 1. Create the kingdom
CREATE DATABASE final_quest;

-- 2. Enter the kingdom
USE final_quest;

-- 3. Build the dragon lair
CREATE TABLE dragons (
    dragon_id INT PRIMARY KEY,
    dragon_name VARCHAR(50),
    element_type VARCHAR(30),
    health INT,
    treasure_guarded DECIMAL(10,2)
);

-- 4. Summon the dragons
INSERT INTO dragons VALUES 
(1, 'Ember', 'Fire', 5000, 15000.50),
(2, 'Glacier', 'Ice', 4500, 12000.00),
(3, 'Volt', 'Lightning', 4800, 18000.75);

-- 5. Power up the dragons
UPDATE dragons
SET health = health * 1.2;

-- 6. Add dragon weakness
ALTER TABLE dragons
ADD COLUMN weakness VARCHAR(30);

-- 7. Remove weak dragons (none should be removed)
DELETE FROM dragons WHERE health < 1000;

-- 8. Create and destroy a test table (if brave)
CREATE TABLE test_table (id INT);
DROP TABLE test_table;
```

**🎉 CONGRATULATIONS! YOU'VE DEFEATED THE DATABASE DRAGON! 🎉**
**Final Reward**: 🏆 **MySQL Master** title + 💎 **All SQL Powers Unlocked**
</details>

---

## 📊 **Your Quest Log (Progress Tracker)**

| Level | Skill Learned | Points Earned | Status |
|-------|---------------|---------------|--------|
| 1 | CREATE DATABASE | 10 XP | ✅ |
| 2 | USE DATABASE | 10 XP | ✅ |
| 3 | CREATE TABLE | 20 XP | ✅ |
| 4 | INSERT Query | 30 XP | ✅ |
| 5 | UPDATE Query | 25 XP | ✅ |
| 6 | ALTER Query | 15 XP | ✅ |
| 7 | DELETE Query | 10 XP | ✅ |
| 8 | TRUNCATE TABLE | 20 XP | ✅ |
| 9 | DROP Query | 25 XP | ✅ |
| Final | Complete Quest | 🏆 | ✅ |

**Total Points**: 165 XP + 🏆 Master Title

---

## 🎓 **Pro Tips for Your Journey**

### ✅ **DO's**
```sql
-- Always backup before dangerous operations
CREATE TABLE backup_table AS SELECT * FROM original_table;

-- Use transactions for safety
START TRANSACTION;
DELETE FROM table WHERE condition;
-- Check if correct, then:
COMMIT;
-- Or if mistake:
ROLLBACK;

-- Test DELETE with SELECT first
SELECT * FROM table WHERE condition;  -- First
DELETE FROM table WHERE condition;    -- Then
```

### ❌ **DON'Ts**
```sql
-- NEVER run without WHERE (unless you mean to!)
DELETE FROM users;  -- 😱 Deletes ALL users!

-- Don't mix up DELETE, TRUNCATE, DROP
DELETE FROM table;    -- Removes rows, slow, can rollback
TRUNCATE TABLE table; -- Removes all rows, fast, can't rollback
DROP TABLE table;     -- Removes table entirely, gone forever

-- Don't forget to specify columns in INSERT
INSERT INTO table VALUES (all, values, here);  -- ❌ Risky
INSERT INTO table (col1, col2) VALUES (v1, v2);  -- ✅ Safe
```

---

## 🔮 **Next Adventures Await!**

You've mastered the basics! Ready for more advanced quests?

**Coming Soon:**
- 🛡️ **JOIN Quests**: Unite multiple tables in epic battles
- 🎯 **SELECT Queries**: Scry data from the deepest tables
- ⚔️ **WHERE Clauses**: Filter your enemies precisely
- 📈 **Aggregate Functions**: Summon powerful data insights
- 🔐 **Permissions & Security**: Guard your kingdom

---

## 💬 **Need Help on Your Quest?**

Remember:
1. **SQL is case-insensitive** (but use uppercase for commands by convention)
2. **End every command with a semicolon** `;`
3. **Use backticks** `` ` `` for names with spaces or special characters
4. **Comments start with** `--` or `/* */`

May your queries be fast and your databases never corrupt! 🚀

**The Data Knights Guild salutes you!** 🛡️