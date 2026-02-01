# 🛡️ MySQL Quest: The Constraint Guardians

## 🏰 **Welcome to the Hall of Data Integrity!**

Greetings, brave SQL Knight! The Kingdom of Data is under threat from chaos and inconsistency. As a newly appointed **Constraint Guardian**, you must master the 9 sacred constraints to restore order and protect the realm from data corruption!

**Your Mission:** Master each constraint to build an unbreakable fortress of data integrity!

---

## 🎯 **Tutorial: The Nine Sacred Constraints**

### 🛡️ **Constraint 1: NOT NULL - The Gatekeeper**
**Power:** Prevents empty values from entering the kingdom

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE citizens (
    citizen_id INT NOT NULL,      -- Gatekeeper guards this gate
    citizen_name VARCHAR(50) NOT NULL, -- No nameless visitors!
    age INT                      -- This gate allows empty values
);

-- ✅ SUCCESS: Proper entry
INSERT INTO citizens VALUES (1, 'Sir Arthur', 35);

-- ❌ FAILURE: Trying to sneak in without a name
INSERT INTO citizens VALUES (2, NULL, 25); -- ERROR! "Cannot be null"

-- ❌ FAILURE: Trying to enter without ID
INSERT INTO citizens VALUES (NULL, 'Mage Lyn', 28); -- ERROR!
```

**🎯 Challenge 1: Fortify the Gates**
```
Create a table 'paladins' with:
- paladin_id (INT, NOT NULL)
- paladin_name (VARCHAR, NOT NULL)  
- order_rank (VARCHAR)
- years_service (INT, NOT NULL)
```

<details>
<summary>🎯 Solution</summary>

```sql
CREATE TABLE paladins (
    paladin_id INT NOT NULL,
    paladin_name VARCHAR(50) NOT NULL,
    order_rank VARCHAR(20),
    years_service INT NOT NULL
);
```
✅ **Reward**: +15 Data Integrity Points
</details>

---

### 🔑 **Constraint 2: UNIQUE - The Identity Enforcer**
**Power:** Ensures no two records have identical values in protected columns

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE magical_artifacts (
    artifact_id INT NOT NULL,
    artifact_name VARCHAR(50) UNIQUE,  -- No duplicate artifact names!
    power_level INT
);

-- ✅ SUCCESS: First artifact
INSERT INTO magical_artifacts VALUES (1, 'Sword of Truth', 100);

-- ✅ SUCCESS: Different name
INSERT INTO magical_artifacts VALUES (2, 'Shield of Valor', 85);

-- ❌ FAILURE: Trying to create duplicate
INSERT INTO magical_artifacts VALUES (3, 'Sword of Truth', 75); 
-- ERROR: Duplicate entry for key 'artifact_name'
```

**🎯 Challenge 2: Enforce Uniqueness**
```
Add UNIQUE constraint to:
1. 'spell_name' column in 'spells' table
2. Combination of 'mage_id' and 'spell_id' in 'mage_spells' table
```

<details>
<summary>🎯 Solution</summary>

```sql
-- Single column unique
CREATE TABLE spells (
    spell_id INT NOT NULL,
    spell_name VARCHAR(50) UNIQUE,
    element VARCHAR(20),
    mana_cost INT
);

-- Multiple column unique (composite)
CREATE TABLE mage_spells (
    mage_id INT NOT NULL,
    spell_id INT NOT NULL,
    mastery_level INT,
    UNIQUE (mage_id, spell_id)  -- A mage can't have same spell twice
);
```
✅ **Reward**: +20 Uniqueness Mastery
</details>

---

### 👑 **Constraint 3: PRIMARY KEY - The Royal Seal**
**Power:** The ultimate identifier - UNIQUE + NOT NULL + Only one per table!

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE kingdoms (
    kingdom_id INT PRIMARY KEY,  -- The Royal Seal!
    kingdom_name VARCHAR(50) NOT NULL,
    ruler_name VARCHAR(50),
    population INT
);

-- ✅ SUCCESS: Creating a kingdom
INSERT INTO kingdoms VALUES (1, 'Camelot', 'King Arthur', 5000);

-- ❌ FAILURE: Duplicate kingdom_id
INSERT INTO kingdoms VALUES (1, 'Avalon', 'Lady of Lake', 3000);
-- ERROR: Duplicate entry '1' for key 'PRIMARY'

-- ❌ FAILURE: NULL kingdom_id
INSERT INTO kingdoms VALUES (NULL, 'El Dorado', 'Unknown', 2000);
-- ERROR: Column 'kingdom_id' cannot be null
```

**🎯 Challenge 3: Crown the Tables**
```
Add PRIMARY KEY to:
1. 'dragon_id' in 'dragons' table
2. Composite primary key on (quest_id, hero_id) in 'quest_participants' table
```

<details>
<summary>🎯 Solution</summary>

```sql
-- Single primary key
CREATE TABLE dragons (
    dragon_id INT PRIMARY KEY,
    dragon_name VARCHAR(50) NOT NULL,
    element_type VARCHAR(20),
    treasure_guarded DECIMAL(10,2)
);

-- Composite primary key (multiple columns)
CREATE TABLE quest_participants (
    quest_id INT NOT NULL,
    hero_id INT NOT NULL,
    role VARCHAR(30),
    reward_share DECIMAL(5,2),
    PRIMARY KEY (quest_id, hero_id)  -- Together they form unique identifier
);
```
✅ **Reward**: +25 Royal Authority
</details>

---

### 🔗 **Constraint 4: FOREIGN KEY - The Family Bond**
**Power:** Creates relationships between tables, ensuring data consistency

```sql
-- 📜 SCROLL OF KNOWLEDGE
-- Parent Table (Kingdom)
CREATE TABLE kingdoms_fk (
    kingdom_id INT PRIMARY KEY,
    kingdom_name VARCHAR(50) NOT NULL
);

-- Child Table (Castle) - References Kingdom
CREATE TABLE castles (
    castle_id INT PRIMARY KEY,
    castle_name VARCHAR(50) NOT NULL,
    kingdom_id INT,
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms_fk(kingdom_id)
    -- Castle must belong to an existing kingdom!
);

-- First create the kingdom
INSERT INTO kingdoms_fk VALUES (1, 'Camelot');

-- ✅ SUCCESS: Castle in existing kingdom
INSERT INTO castles VALUES (101, 'Round Table Castle', 1);

-- ❌ FAILURE: Castle in non-existent kingdom
INSERT INTO castles VALUES (102, 'Mystery Castle', 999);
-- ERROR: Cannot add or update a child row: a foreign key constraint fails
```

**🎯 Challenge 4: Forge the Bonds**
```
Create a relationship where:
1. 'knights' table has foreign key to 'kingdoms' table
2. 'quest_rewards' table has foreign keys to both 'quests' and 'items' tables
```

<details>
<summary>🎯 Solution</summary>

```sql
-- Create parent tables first
CREATE TABLE kingdoms_ref (
    kingdom_id INT PRIMARY KEY,
    kingdom_name VARCHAR(50)
);

CREATE TABLE quests (
    quest_id INT PRIMARY KEY,
    quest_name VARCHAR(100)
);

CREATE TABLE items (
    item_id INT PRIMARY KEY,
    item_name VARCHAR(50)
);

-- Knights reference kingdoms
CREATE TABLE knights (
    knight_id INT PRIMARY KEY,
    knight_name VARCHAR(50),
    kingdom_id INT,
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms_ref(kingdom_id)
);

-- Quest rewards reference both quests and items
CREATE TABLE quest_rewards (
    quest_id INT,
    item_id INT,
    quantity INT,
    PRIMARY KEY (quest_id, item_id),
    FOREIGN KEY (quest_id) REFERENCES quests(quest_id),
    FOREIGN KEY (item_id) REFERENCES items(item_id)
);
```
✅ **Reward**: +30 Relationship Mastery
</details>

---

### ✅ **Constraint 5: CHECK - The Rule Setter**
**Power:** Enforces specific conditions on data values

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE adventurers (
    adventurer_id INT PRIMARY KEY,
    adventurer_name VARCHAR(50) NOT NULL,
    age INT CHECK (age >= 18),  -- Must be adult to adventure!
    class VARCHAR(30) CHECK (class IN ('Warrior', 'Mage', 'Rogue', 'Cleric')),
    gold INT CHECK (gold >= 0)  -- Can't have negative gold!
);

-- ✅ SUCCESS: Valid adventurer
INSERT INTO adventurers VALUES (1, 'Sir Brave', 25, 'Warrior', 100);

-- ❌ FAILURE: Too young
INSERT INTO adventurers VALUES (2, 'Youngling', 16, 'Mage', 50);
-- ERROR: Check constraint 'adventurers_chk_1' is violated

-- ❌ FAILURE: Invalid class
INSERT INTO adventurers VALUES (3, 'Weirdo', 30, 'Pirate', 200);
-- ERROR: Check constraint 'adventurers_chk_2' is violated

-- ❌ FAILURE: Negative gold
INSERT INTO adventurers VALUES (4, 'Bankrupt', 28, 'Rogue', -50);
-- ERROR: Check constraint 'adventurers_chk_3' is violated
```

**🎯 Challenge 5: Set the Rules**
```
Create constraints that:
1. Ensure 'mana' is between 0 and 1000
2. Ensure 'spell_level' is between 1 and 10  
3. Ensure 'alignment' is either 'Good', 'Neutral', or 'Evil'
```

<details>
<summary>🎯 Solution</summary>

```sql
CREATE TABLE wizards (
    wizard_id INT PRIMARY KEY,
    wizard_name VARCHAR(50),
    mana INT CHECK (mana BETWEEN 0 AND 1000),
    spell_level INT CHECK (spell_level >= 1 AND spell_level <= 10),
    alignment VARCHAR(20) CHECK (alignment IN ('Good', 'Neutral', 'Evil'))
);
```
✅ **Reward**: +25 Rule Enforcement
</details>

---

### 🏠 **Constraint 6: DEFAULT - The Fallback Provider**
**Power:** Provides a default value when none is specified

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE guild_members (
    member_id INT PRIMARY KEY,
    member_name VARCHAR(50) NOT NULL,
    join_date DATE DEFAULT (CURRENT_DATE()),  -- Auto today's date!
    status VARCHAR(20) DEFAULT 'Active',      -- Default status
    rank VARCHAR(20) DEFAULT 'Initiate',      -- Starting rank
    gold_contributed INT DEFAULT 0            -- Start with zero
);

-- ✅ Insert with defaults
INSERT INTO guild_members (member_id, member_name) 
VALUES (1, 'Arthur');

-- ✅ Insert overriding defaults
INSERT INTO guild_members 
VALUES (2, 'Merlin', '2023-01-15', 'Elder', 'Archmage', 5000);

-- Check the results
SELECT * FROM guild_members;
/*
member_id | member_name | join_date   | status | rank     | gold_contributed
----------|-------------|-------------|--------|----------|-----------------
1         | Arthur      | 2023-11-05  | Active | Initiate | 0
2         | Merlin      | 2023-01-15  | Elder  | Archmage | 5000
*/
```

**🎯 Challenge 6: Provide Fallbacks**
```
Set defaults for:
1. 'created_at' timestamp to current time
2. 'updated_at' timestamp to current time
3. 'is_active' to TRUE
4. 'attempts_left' to 3
```

<details>
<summary>🎯 Solution</summary>

```sql
CREATE TABLE user_accounts (
    user_id INT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    attempts_left INT DEFAULT 3,
    last_login DATE DEFAULT NULL
);
```
✅ **Reward**: +20 Default Wisdom
</details>

---

### ⚡ **Constraint 7: CREATE INDEX - The Speed Wizard**
**Power:** Creates invisible indexes to speed up searches (not technically a constraint, but enforces faster data retrieval)

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE massive_library (
    book_id INT PRIMARY KEY,
    title VARCHAR(200),
    author VARCHAR(100),
    genre VARCHAR(50),
    year_published INT,
    pages INT,
    isbn VARCHAR(13) UNIQUE
);

-- Without index (slow search)
SELECT * FROM massive_library WHERE author = 'Tolkien';

-- ⚡ Create index for faster searches
CREATE INDEX idx_author ON massive_library(author);
CREATE INDEX idx_genre_year ON massive_library(genre, year_published);

-- Now these searches are MUCH faster!
SELECT * FROM massive_library WHERE author = 'Tolkien';
SELECT * FROM massive_library WHERE genre = 'Fantasy' AND year_published > 2000;

-- ❌ Don't over-index! Too many indexes slow down INSERT/UPDATE
```

**🎯 Challenge 7: Speed Up the Kingdom**
```
Create indexes to optimize:
1. Searching knights by kingdom_id
2. Searching spells by element and mana_cost  
3. Searching quests by difficulty and reward
```

<details>
<summary>🎯 Solution</summary>

```sql
-- Single column index
CREATE INDEX idx_kingdom_id ON knights(kingdom_id);

-- Composite index (multiple columns)
CREATE INDEX idx_element_mana ON spells(element, mana_cost);
CREATE INDEX idx_difficulty_reward ON quests(difficulty_level, min_reward);
```
✅ **Reward**: +35 Speed Enhancement
</details>

---

### 🔄 **Constraint 8: AUTO_INCREMENT - The Auto-Generator**
**Power:** Automatically generates unique numbers for primary keys

```sql
-- 📜 SCROLL OF KNOWLEDGE
CREATE TABLE monster_spawns (
    spawn_id INT PRIMARY KEY AUTO_INCREMENT,  -- Auto-magic numbers!
    monster_name VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    difficulty INT,
    spawn_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert without specifying spawn_id (it auto-generates!)
INSERT INTO monster_spawns (monster_name, location, difficulty) 
VALUES ('Goblin', 'Dark Forest', 2);

INSERT INTO monster_spawns (monster_name, location, difficulty)
VALUES ('Dragon', 'Volcano Peak', 10);

INSERT INTO monster_spawns (monster_name, location, difficulty)
VALUES ('Orc', 'Mountain Pass', 5);

-- Check the auto-generated IDs
SELECT * FROM monster_spawns;
/*
spawn_id | monster_name | location      | difficulty | spawn_time
---------|--------------|---------------|------------|------------
1        | Goblin       | Dark Forest   | 2          | 2023-11-05 10:00:00
2        | Dragon       | Volcano Peak  | 10         | 2023-11-05 10:00:01
3        | Orc          | Mountain Pass | 5          | 2023-11-05 10:00:02
*/

-- Start from custom value
CREATE TABLE legendary_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(50)
) AUTO_INCREMENT = 1000;  -- Start IDs from 1000
```

**🎯 Challenge 8: Automate the Records**
```
Create tables with AUTO_INCREMENT:
1. 'quest_id' starting from 1000
2. 'transaction_id' for banking system
3. 'log_id' for system logs
```

<details>
<summary>🎯 Solution</summary>

```sql
-- Starting from custom value
CREATE TABLE quest_log (
    quest_id INT PRIMARY KEY AUTO_INCREMENT,
    quest_name VARCHAR(100) NOT NULL,
    start_date DATE,
    status VARCHAR(20)
) AUTO_INCREMENT = 1000;

-- Regular auto-increment
CREATE TABLE bank_transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    from_account INT,
    to_account INT,
    amount DECIMAL(10,2),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Multiple auto-increment columns (only one per table allowed!)
-- This would ERROR:
CREATE TABLE bad_design (
    id1 INT AUTO_INCREMENT,
    id2 INT AUTO_INCREMENT,  -- ERROR: Only one auto-increment column allowed
    PRIMARY KEY (id1, id2)
);
```
✅ **Reward**: +30 Automation Mastery
</details>

---

## 🏆 **FINAL BOSS: The Complete Fortress**

### 🐉 **Ultimate Challenge: Build the Kingdom Database**

```
Create a complete RPG database using ALL constraints:

REQUIREMENTS:
1. Database: "rpg_kingdom"
2. Tables with relationships:
   - players (player_id PK AI, username UNIQUE NOT NULL, email UNIQUE, created_at DEFAULT)
   - characters (char_id PK AI, player_id FK, name NOT NULL, class CHECK, level DEFAULT 1)
   - items (item_id PK AI, name UNIQUE NOT NULL, value CHECK > 0, weight CHECK >= 0)
   - inventory (inv_id PK AI, char_id FK, item_id FK, quantity CHECK > 0)
   - quests (quest_id PK AI, name NOT NULL, min_level CHECK >= 1, reward CHECK > 0)
   - character_quests (char_id FK, quest_id FK, status, PRIMARY KEY(char_id, quest_id))

CONSTRAINTS TO USE:
✓ PRIMARY KEY
✓ FOREIGN KEY  
✓ NOT NULL
✓ UNIQUE
✓ CHECK
✓ DEFAULT
✓ AUTO_INCREMENT
✓ CREATE INDEX (for performance)
```

<details>
<summary>🎯 Complete Solution</summary>

```sql
-- Create the kingdom
CREATE DATABASE rpg_kingdom;
USE rpg_kingdom;

-- Players table
CREATE TABLE players (
    player_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    CHECK (email LIKE '%@%')  -- Basic email validation
);

-- Characters table
CREATE TABLE characters (
    char_id INT PRIMARY KEY AUTO_INCREMENT,
    player_id INT NOT NULL,
    char_name VARCHAR(50) NOT NULL,
    class VARCHAR(20) CHECK (class IN ('Warrior', 'Mage', 'Rogue', 'Cleric', 'Archer')),
    level INT DEFAULT 1 CHECK (level BETWEEN 1 AND 100),
    health INT DEFAULT 100 CHECK (health >= 0),
    mana INT DEFAULT 50 CHECK (mana >= 0),
    experience INT DEFAULT 0 CHECK (experience >= 0),
    created_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE
);

-- Items table
CREATE TABLE items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) UNIQUE NOT NULL,
    item_type VARCHAR(30) CHECK (item_type IN ('Weapon', 'Armor', 'Potion', 'Scroll', 'Resource')),
    value DECIMAL(10,2) CHECK (value >= 0),
    weight DECIMAL(5,2) CHECK (weight >= 0),
    rarity VARCHAR(20) DEFAULT 'Common' CHECK (rarity IN ('Common', 'Uncommon', 'Rare', 'Epic', 'Legendary')),
    max_stack INT DEFAULT 1 CHECK (max_stack >= 1)
);

-- Inventory table
CREATE TABLE inventory (
    inv_id INT PRIMARY KEY AUTO_INCREMENT,
    char_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT DEFAULT 1 CHECK (quantity > 0),
    UNIQUE (char_id, item_id),  -- Can't have same item in same slot
    FOREIGN KEY (char_id) REFERENCES characters(char_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(item_id) ON DELETE CASCADE
);

-- Quests table
CREATE TABLE quests (
    quest_id INT PRIMARY KEY AUTO_INCREMENT,
    quest_name VARCHAR(100) NOT NULL,
    description TEXT,
    min_level INT DEFAULT 1 CHECK (min_level >= 1),
    max_level INT CHECK (max_level >= min_level OR max_level IS NULL),
    reward_gold INT CHECK (reward_gold >= 0),
    reward_exp INT CHECK (reward_exp >= 0),
    is_repeatable BOOLEAN DEFAULT FALSE,
    time_limit INT CHECK (time_limit > 0 OR time_limit IS NULL)
);

-- Character quests junction table
CREATE TABLE character_quests (
    char_id INT NOT NULL,
    quest_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'Not Started' CHECK (status IN ('Not Started', 'In Progress', 'Completed', 'Failed')),
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_time TIMESTAMP NULL,
    PRIMARY KEY (char_id, quest_id),  -- Composite primary key
    FOREIGN KEY (char_id) REFERENCES characters(char_id) ON DELETE CASCADE,
    FOREIGN KEY (quest_id) REFERENCES quests(quest_id) ON DELETE CASCADE,
    CHECK (completion_time IS NULL OR completion_time >= start_time)
);

-- ⚡ Create indexes for performance
CREATE INDEX idx_char_class ON characters(class);
CREATE INDEX idx_char_level ON characters(level);
CREATE INDEX idx_item_type ON items(item_type);
CREATE INDEX idx_quest_level ON quests(min_level, max_level);
CREATE INDEX idx_inv_char ON inventory(char_id);
CREATE INDEX idx_char_player ON characters(player_id);
CREATE INDEX idx_quest_status ON character_quests(status);

-- Insert sample data
INSERT INTO players (username, email) VALUES
('arthur_king', 'arthur@camelot.com'),
('merlin_mage', 'merlin@avalon.com');

INSERT INTO characters (player_id, char_name, class, level) VALUES
(1, 'Sir Lancelot', 'Warrior', 45),
(1, 'Lady Guinevere', 'Cleric', 38),
(2, 'Merlin', 'Mage', 99);

INSERT INTO items (item_name, item_type, value, weight) VALUES
('Excalibur', 'Weapon', 5000.00, 8.5),
('Holy Grail', 'Resource', 10000.00, 3.0),
('Mana Potion', 'Potion', 50.00, 0.5);

INSERT INTO quests (quest_name, min_level, reward_gold, reward_exp) VALUES
('Slay the Dragon', 30, 1000, 5000),
('Find the Holy Grail', 20, 5000, 10000),
('Rescue the Princess', 10, 500, 1000);

-- Test constraints
-- This will fail due to CHECK constraint:
INSERT INTO characters (player_id, char_name, class, level) 
VALUES (1, 'Test', 'InvalidClass', 1);  -- ERROR: Check constraint

-- This will fail due to FOREIGN KEY constraint:
INSERT INTO inventory (char_id, item_id) 
VALUES (999, 1);  -- ERROR: Foreign key constraint fails

-- This will fail due to UNIQUE constraint:
INSERT INTO players (username, email) 
VALUES ('arthur_king', 'different@email.com');  -- ERROR: Duplicate username
```

🎉 **CONGRATULATIONS! YOU'VE BUILT AN UNBREAKABLE DATA FORTRESS!** 🎉

**Final Reward**: 🏆 **Master Constraint Guardian** Title + 🛡️ **All Constraint Powers Unlocked**
</details>

---

## 📊 **Constraint Power Level Chart**

| Constraint | Power Level | Best For | Danger Level |
|------------|-------------|----------|--------------|
| PRIMARY KEY | ⭐⭐⭐⭐⭐ | Unique identification | Low |
| FOREIGN KEY | ⭐⭐⭐⭐⭐ | Relationships | Medium |
| NOT NULL | ⭐⭐⭐ | Required fields | Low |
| UNIQUE | ⭐⭐⭐⭐ | Preventing duplicates | Low |
| CHECK | ⭐⭐⭐⭐ | Data validation | Medium |
| DEFAULT | ⭐⭐ | Fallback values | Low |
| AUTO_INCREMENT | ⭐⭐⭐ | Automatic IDs | Low |
| CREATE INDEX | ⭐⭐⭐⭐ | Performance | Medium (if overused) |

---

## 🚨 **Common Pitfalls & Solutions**

### ❌ **Pitfall 1: Circular References**
```sql
-- ❌ DON'T: Circular foreign keys
CREATE TABLE table_a (
    id INT PRIMARY KEY,
    b_id INT,
    FOREIGN KEY (b_id) REFERENCES table_b(id)
);

CREATE TABLE table_b (
    id INT PRIMARY KEY,
    a_id INT,
    FOREIGN KEY (a_id) REFERENCES table_a(id)  -- Circular!
);

-- ✅ DO: Design hierarchical relationships
CREATE TABLE parent (
    id INT PRIMARY KEY
);

CREATE TABLE child (
    id INT PRIMARY KEY,
    parent_id INT,
    FOREIGN KEY (parent_id) REFERENCES parent(id)
);
```

### ❌ **Pitfall 2: Over-constraining**
```sql
-- ❌ DON'T: Too many constraints that conflict
CREATE TABLE overconstrained (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20) CHECK (status IN ('A', 'B', 'C')),
    code VARCHAR(10) DEFAULT 'XYZ',
    -- What if 'XYZ' is not in allowed statuses?
    CHECK (status = code)  -- Conflicting constraints!
);

-- ✅ DO: Ensure constraints are compatible
CREATE TABLE well_constrained (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(20) DEFAULT 'Pending' CHECK (status IN ('Pending', 'Active', 'Completed')),
    -- Constraints work together harmoniously
    completion_date DATE,
    CHECK (status != 'Completed' OR completion_date IS NOT NULL)
);
```

---

## 🎓 **Pro Guardian Tips**

1. **Start Simple**: Add constraints after testing basic functionality
2. **Name Your Constraints**: For easier debugging
```sql
CREATE TABLE example (
    id INT,
    age INT,
    CONSTRAINT chk_age_range CHECK (age BETWEEN 0 AND 150),
    CONSTRAINT uc_unique_id UNIQUE (id)
);
```
3. **Test Constraints Early**: Insert test data to verify constraints work
4. **Use ON DELETE/UPDATE**: Define behavior for foreign keys
```sql
FOREIGN KEY (parent_id) REFERENCES parent(id)
    ON DELETE CASCADE      -- Delete children when parent deleted
    ON UPDATE SET NULL    -- Set to NULL when parent updated
```
5. **Document Constraints**: Comment your constraint logic

---

## 🔮 **Next Adventure Awaits: Advanced Queries!**

You've mastered the constraints, Guardian! Next, you'll learn:
- 🎯 **SELECT with WHERE**: Filtering your data precisely
- 🔗 **JOIN Queries**: Connecting multiple tables in battle
- 📊 **Aggregate Functions**: Summoning powerful data insights
- 🏆 **Subqueries**: The nested mysteries of SQL

---

**The Data Integrity Council salutes your mastery!** 🛡️

*"May your data be ever consistent and your constraints never broken!"*