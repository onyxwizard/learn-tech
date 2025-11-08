# 🧪 **6. Working with Real DB Systems**  

## *From zero to production-ready — the pragmatic path*

> 💡 Goal: Be productive in 10 minutes — not just understand concepts.



## 🎯 **6.1 Choosing the Right DB: Practical Guidance**

| Use Case | Recommended DB | Why |
|---------|----------------|-----|
| 🧪 **Learning / Prototyping** | **SQLite** | Zero setup, file-based, works in-memory — perfect for tutorials & tests |
| 🐘 **Production Web App (Startup to Scale-up)** | **PostgreSQL** | Rock-solid, JSONB, full-text search, GIS (PostGIS), logical replication, rock-star community |
| 🐬 **Legacy/LAMP Stack, Simplicity** | **MySQL** | Mature, great tooling, but watch out for subtle gotchas (e.g., default isolation level) |
| ☁️ **Fully Managed, Hands-Off** | **Cloud SQL (GCP), RDS (AWS), Azure SQL** | Auto-backups, patching, scaling — pay for convenience |
| 📱 **Mobile / Embedded / Edge** | **SQLite** (or **Realm**, **Firestore**) | Runs on-device, syncs later |

#### 🔍 PostgreSQL vs. MySQL — Quick Cheat Sheet
| Feature | PostgreSQL | MySQL |
|--------|------------|-------|
| **JSON Support** | `JSONB` (binary, indexable, powerful) ✅ | `JSON` (text-based, slower) ⚠️ |
| **Full-Text Search** | Built-in, customizable ✅ | Built-in, basic ✅ |
| **Geospatial** | PostGIS (industry standard) ✅ | Limited (via plugin) ⚠️ |
| **Replication** | Logical (per-table), streaming ✅ | Row-based, GTID ✅ |
| **Licensing** | MIT (truly open) ✅ | GPL + commercial (Oracle) ⚠️ |
| **Best For** | Complex apps, data integrity, extensibility | High-read, simple schemas, legacy |

✅ **Verdict for 2025**:  
→ **Start with PostgreSQL** unless you have a *strong reason* not to. It’s the "batteries-included" open-source RDBMS.



## 🛠️ **6.2 Installation & Setup (Local + Docker)**

### ✅ Option 1: **Docker (Recommended)** — Clean, reproducible, no mess
```bash
# Start PostgreSQL in seconds
docker run --name pg-dev -e POSTGRES_PASSWORD=secret \
  -p 5432:5432 -d postgres:16

# Connect with psql
docker exec -it pg-dev psql -U postgres
```

```bash
# Start MySQL
docker run --name mysql-dev -e MYSQL_ROOT_PASSWORD=secret \
  -p 3306:3306 -d mysql:8.0
```

💡 **Bonus**: Save as `docker-compose.yml` for one-command startup:
```yaml
version: '3.8'
services:
  db:
    image: postgres:16
    ports: ["5432:5432"]
    environment:
      POSTGRES_PASSWORD: secret
      POSTGRES_DB: app_dev
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```
→ `docker-compose up -d` → done.

### ✅ Option 2: **Native Install**
- **macOS**: `brew install postgresql` → `brew services start postgresql`  
- **Ubuntu**: `sudo apt install postgresql`  
- **Windows**: [PostgreSQL Installer](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)



## 💻 **6.3 Basic Operations: CLI & GUI**

### 🔹 **CLI Tools — Fast & Scriptable**
| Command | PostgreSQL | MySQL | SQLite |
|--------|------------|-------|--------|
| **Connect** | `psql -U postgres -d app_dev` | `mysql -u root -p` | `sqlite3 my.db` |
| **List DBs** | `\l` | `SHOW DATABASES;` | `.databases` |
| **List Tables** | `\dt` | `SHOW TABLES;` | `.tables` |
| **Describe Table** | `\d users` | `DESCRIBE users;` | `.schema users` |
| **Quit** | `\q` | `EXIT;` | `.exit` |

💡 Pro tip: Use `\x` in `psql` for expanded display (great for wide tables):
```sql
app_dev=# \x
Expanded display is on.
app_dev=# SELECT * FROM users WHERE id = 101;
-[ RECORD 1 ]-+---------------------
id           | 101
email        | alex@example.com
created_at   | 2025-11-08 12:34:56
profile      | {"theme": "dark", ...}
```

### 🔹 **GUI Tools — Visual & Beginner-Friendly**
| Tool | Best For | Notes |
|------|----------|-------|
| **pgAdmin 4** | PostgreSQL | Official, web-based, powerful (runs in Docker too) |
| **DBeaver** | Multi-DB (PG, MySQL, SQLite, etc.) | Free, open-source, great ERD & data export |
| **TablePlus** | macOS/Windows (Modern UI) | Fast, clean, native app (free tier available) |
| **SQLite Browser** | SQLite only | Simple, drag-and-drop, perfect for mobile devs |

🎯 **Recommendation**:  
→ Start with **DBeaver** (free, cross-platform, no setup)  
→ Move to **TablePlus** if you want sleekness & speed.



## 📤📥 **6.4 Import/Export: CSV, JSON, Backups**

### 🔸 **Import CSV → Table**
```sql
-- PostgreSQL
COPY users(email, name, created_at) 
FROM '/data/users.csv' 
DELIMITER ',' CSV HEADER;

-- MySQL
LOAD DATA INFILE '/data/users.csv'
INTO TABLE users
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
```

💡 **Safer alternative** (no file access needed):
```sql
-- Use psql's \copy (client-side)
\copy users FROM 'users.csv' CSV HEADER;
```

#### 🔸 **Import JSON → Table**
```sql
-- PostgreSQL (powerful!)
CREATE TABLE raw_import (data JSONB);
\COPY raw_import FROM 'data.json';

-- Then transform
INSERT INTO users (email, name)
SELECT 
  data->>'email',
  data->>'full_name'
FROM raw_import;
```

#### 🔸 **Export Table → CSV**
```sql
-- PostgreSQL
\COPY (SELECT id, email FROM users) TO 'users_export.csv' CSV HEADER;

-- Or from CLI
psql -U postgres -d app_dev -c "COPY (SELECT * FROM users) TO STDOUT CSV HEADER" > users.csv
```

### 🔸 **Backups & Restore**
| Operation | PostgreSQL | MySQL |
|----------|------------|-------|
| **Logical Backup** (SQL) | `pg_dump app_dev > backup.sql` | `mysqldump -u root -p app_dev > backup.sql` |
| **Restore** | `psql -U postgres -d new_db < backup.sql` | `mysql -u root -p new_db < backup.sql` |
| **Binary Backup** | `pg_basebackup` (WAL included) | `mysqlbackup` (Enterprise) / `Percona XtraBackup` (OSS) |

✅ **Daily habit**:  
```bash
# One-liner backup + gzip
pg_dump app_dev | gzip > app_dev_$(date +%F).sql.gz
```

## 🧰 Bonus: Handy Real-World Tips

### 🐘 **PostgreSQL First-Time Setup (after install)**
```bash
# Create a non-superuser (safer!)
createuser --interactive --pwprompt app_user
createdb -O app_user app_dev

# Then connect
psql -U app_user -d app_dev
```

### 🐬 **MySQL: Fix Common Pain Points**
```sql
-- Enable strict mode (avoid silent data truncation!)
SET GLOBAL sql_mode = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- Use utf8mb4 (full Unicode — emojis included!)
ALTER DATABASE app_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 📦 **Sample Datasets for Practice**
- [Chinook Database](https://github.com/lerocha/chinook-database) — Music store (PG/MySQL/SQLite)  
- [Northwind](https://github.com/pthom/northwind_psql) — Classic sample (PostgreSQL)  
- [Mockaroo](https://mockaroo.com) — Generate realistic CSV/JSON test data



## ✅ **Summary: Part 6 in 60 Seconds**

| Task | Tool/Command | Pro Tip |
|------|--------------|---------|
| 🐳 **Run DB Locally** | `docker run postgres` | Use `docker-compose` for reproducibility |
| 💻 **Connect** | `psql`, `mysql`, DBeaver | `\x` in psql for readability |
| 📥 **Import CSV** | `\copy` (PG), `LOAD DATA` (MySQL) | Prefer `\copy` — no server file access needed |
| 📤 **Export** | `\copy ... TO` | Pipe to `gzip` for compression |
| 🔄 **Backup** | `pg_dump`, `mysqldump` | Schedule daily; test restore quarterly |
| 🛠️ **GUI** | DBeaver (free), TablePlus (polish) | Use GUI for exploration, CLI for scripts |

> 🎯 **You’re now equipped to set up, load, explore, and maintain real databases — today.**
