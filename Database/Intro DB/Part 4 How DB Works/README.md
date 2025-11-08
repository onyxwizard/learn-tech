# ⚙️ **4. How Databases Work — Simplified Internals**  
## *From `SELECT *` to data on screen: the magic, revealed*

> 💡 **Goal**: Not to turn you into a DB engineer — but to give you *mental models* that prevent footguns and unlock performance.


### 🖥️ **4.1 Client–Server Architecture**

Most production databases use a **client-server model**:

```
[Your App] → (network) → [DB Server]
   ↑                          ↑
[CLI: psql]             [Storage Engine]
[GUI: pgAdmin]          [Query Processor]
                        [Transaction Log]
```

- ✅ **Client**: Sends queries (e.g., `SELECT name FROM users WHERE id = 101;`)  
- ✅ **Server**: Parses, plans, executes, returns results  
- ✅ **Separation allows**:  
  - Multiple apps → one DB  
  - Centralized security & backups  
  - Scaling (more RAM/CPU on server)

💡 **Exception**: **SQLite** is *embedded* — the DB runs *inside your app process*, reading/writing a local `.db` file. Great for mobile, testing, edge cases — but no concurrency beyond one writer.


## 🧱 **4.2 Storage Engines: Where Data Lives**

A DBMS can support multiple **storage engines** — pluggable modules that handle *how* data is stored and retrieved.

#### 🔍 Example: **MySQL’s Engines**
| Engine | Transactional? | Crash-Safe? | Full-Text Search? | Best For |
|--------|----------------|-------------|-------------------|----------|
| **InnoDB** ✅ (Default) | Yes (ACID) | Yes (WAL) | Yes (v5.6+) | Most apps — production standard |
| **MyISAM** ❌ | No | No (corrupts on crash) | Yes (older) | Read-heavy, legacy, logging |

#### 🔍 PostgreSQL: **Only one engine** (but highly optimized)  
- Everything is ACID, WAL-based, MVCC (see below)  
- Extensions add capabilities (e.g., `pg_partman` for partitioning)

✅ **Key takeaway**:  
If you’re using PostgreSQL or modern MySQL — you’re almost certainly on a **transactional, crash-safe engine**.  
*(Don’t use MyISAM in 2025. Just don’t.)*



## 📚 **4.3 Indexes: Your Query’s Turbocharger**

> 🚨 **Without indexes**, every query scans *every row* — **O(n)** time.  
> With indexes? **O(log n)** or even **O(1)** — *millions of rows in milliseconds*.

#### 🌳 **B-tree (Balanced Tree) — The Default Workhorse**
- Used for `=`, `>`, `<`, `BETWEEN`, `ORDER BY`, `JOIN`  
- Keeps data sorted → fast range scans  
- Self-balancing → performance stays consistent as data grows

```
        [50]
       /    \
   [25]      [75]
   /  \      /  \
[10] [30] [60] [90]  ← leaf nodes point to actual table rows
```

✅ **When to index**:  
- Columns in `WHERE` (`WHERE status = 'active'`)  
- Columns in `JOIN` (`ON orders.user_id = users.id`)  
- Columns in `ORDER BY` (`ORDER BY created_at DESC`)  
- Columns in `GROUP BY`

⚠️ **Trade-offs**:  
- Slower `INSERT`/`UPDATE`/`DELETE` (index must be updated)  
- More disk space  
→ **Index wisely** — not every column!

#### Other Index Types (Know When They Shine):
| Type | Best For | Example Use Case |
|------|----------|------------------|
| **Hash Index** | Exact matches (`=`) only | Session lookup: `WHERE session_id = 'abc123'` |
| **Bitmap Index** | Low-cardinality columns (`status: active/inactive`) | Data warehousing (Oracle, PostgreSQL via extensions) |
| **Full-Text Index** | Text search (`MATCH ... AGAINST`) | Search blog posts by keyword |
| **GiST / SP-GiST** | Geospatial, hierarchical | PostGIS: `ST_Contains(geom, point)` |
| **GIN** | JSON/array/search | PostgreSQL: `WHERE tags ? 'database'` |

💡 **Pro tip**:  
```sql
-- See if your query uses an index!
EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'test@example.com';
```
→ Look for `Index Scan` (good) vs. `Seq Scan` (full table scan — bad for large tables).



## 🔄 **4.4 Query Processing: From Text to Results**

What happens after you hit **Enter** on `SELECT name FROM users WHERE id = 101;`?

#### Step-by-step:
1. **Parsing**  
   → Check syntax: Is this valid SQL?  
   → Build **parse tree**

2. **Rewriting / Semantic Analysis**  
   → Resolve table/column names  
   → Check permissions, types, constraints  
   → Apply rules (e.g., views expand to base tables)

3. **Optimization** 🧠 *(The Brain)*  
   → Generate multiple **execution plans**  
   → Estimate cost (disk I/O, CPU, memory)  
   → Pick the **cheapest plan**  
   → *This is where indexes, stats, and JOIN order matter!*

4. **Execution**  
   → Run the plan: scan tables/indexes, filter, sort, join  
   → Stream results back to client

🔍 **Optimizer relies on statistics**:  
- Row counts  
- Value distributions (`most emails end in .com?`)  
→ Run `ANALYZE` (PostgreSQL) or `ANALYZE TABLE` (MySQL) after big data changes!

---

### 📝 **4.5 Transactions & Logging: ACID in Action**

How does a DB guarantee **Durability** and **Atomicity** even during a crash?

#### 🔁 **Write-Ahead Logging (WAL)** — The Secret Sauce

> ✅ **Rule**: *No data is written to the main table files until the change is safely logged.*

```
1. App: BEGIN; UPDATE accounts SET balance = balance - 100 WHERE id = 1;
2. DB: Write "UPDATE account 1: -100" to **WAL file** (on disk)
3. DB: Apply change to in-memory buffer (fast!)
4. App: COMMIT;
5. DB: Flush WAL to disk → ✅ Transaction durable!
6. Later: Checkpoint → apply buffered changes to main data files
```

Why WAL rocks:
- ⚡ **Fast writes**: Just append to a log (sequential I/O)  
- 🛡️ **Crash recovery**: On restart, replay WAL to restore consistency  
- 🔄 **Replication**: Send WAL to replicas for real-time sync  

#### 🧊 **MVCC (Multi-Version Concurrency Control)**  
How PostgreSQL/Oracle allow readers *and* writers to work **without locking**:

- When you `UPDATE`, the DB doesn’t overwrite the old row.  
- It writes a **new version**, and marks the old one as “valid until timestamp X”.  
- Concurrent `SELECT`s see the version *valid at their transaction start time*.

✅ Result:  
- No “readers block writers” (unlike old MySQL/MyISAM)  
- Consistent snapshots without `LOCK TABLE`



### ✅ **Summary: Part 4 in 60 Seconds**

| Component | What It Does | Why It Matters |
|----------|--------------|----------------|
| 🖥️ **Client-Server** | Separates app logic from data storage | Scalability, security, shared access |
| 🧱 **Storage Engine** | Manages on-disk format & transactions | InnoDB/PostgreSQL = safe & robust |
| 📚 **Indexes (B-tree)** | Skip scanning rows; jump to data | 1000× faster queries — use wisely |
| 🔄 **Query Processing** | Parse → Optimize → Execute | Optimizer needs stats to choose best plan |
| 📝 **WAL + MVCC** | Crash safety + concurrency without locks | ACID compliance in real-world systems |

