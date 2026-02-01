/**
 * @author onyxwizard
 * @date 31-01-2026
 */

-- Create the kingdom
CREATE DATABASE rpg_kingdom;
USE rpg_kingdom;

SHOW tables;
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
    min_level INT DEFAULT 1,
    max_level INT,
    reward_gold INT DEFAULT 0,
    reward_exp INT DEFAULT 0,
    is_repeatable BOOLEAN DEFAULT FALSE,
    time_limit INT,
    CONSTRAINT chk_min_level CHECK (min_level >= 1),
    CONSTRAINT chk_max_level CHECK (max_level >= min_level OR max_level IS NULL),
    CONSTRAINT chk_reward_gold CHECK (reward_gold >= 0),
    CONSTRAINT chk_reward_exp CHECK (reward_exp >= 0),
    CONSTRAINT chk_time_limit CHECK (time_limit > 0 OR time_limit IS NULL)
);

-- Character quests junction table
CREATE TABLE character_quests (
    char_id INT NOT NULL,
    quest_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'Not Started' CHECK (status IN ('Not Started' , 'In Progress', 'Completed', 'Failed')),
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_time TIMESTAMP NULL,
    PRIMARY KEY (char_id , quest_id),
    FOREIGN KEY (char_id)
        REFERENCES characters (char_id)
        ON DELETE CASCADE,
    FOREIGN KEY (quest_id)
        REFERENCES quests (quest_id)
        ON DELETE CASCADE,
    CHECK (completion_time IS NULL
        OR completion_time >= start_time)
);

show tables;