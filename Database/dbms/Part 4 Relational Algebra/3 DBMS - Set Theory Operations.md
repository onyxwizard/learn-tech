# ➕ **DBMS – Set Theory Operations**  
### *Combine, Compare, and Contrast Tables Like a Pro*

> 🎯 Since relations (tables) are **sets of tuples**, we can use **set theory operations** to manipulate them — just like math!

Let’s master the 4 key operations:  
- **UNION (∪)** — *All items from A or B*  
- **INTERSECTION (∩)** — *Only items in both A and B*  
- **MINUS (−)** — *Items in A but NOT in B*  
- **CARTESIAN PRODUCT (×)** — *All possible pairings*

We’ll use two simple tables:


## 📋 Sample Tables

### 🧑 `STUDENTS`
| ID | Name  | Dept |
|----|-------|------|
| 1  | Ali   | CS   |
| 2  | Sara  | EE   |
| 3  | Ben   | CS   |
| 4  | Maya  | ME   |

### 🏆 `AWARD_WINNERS`
| ID | Name  | Prize     |
|----|-------|-----------|
| 2  | Sara  | Gold      |
| 3  | Ben   | Silver    |
| 5  | Tom   | Bronze    |

> ✅ Both tables have `ID` and `Name` → **union-compatible** for set ops.

---

## 1️⃣ **UNION (∪) — “Everyone who’s in A *or* B”**

> ✅ Combines rows from two relations → **removes duplicates**.  
> 🎯 *Like merging two guest lists.*

### 🔤 Syntax:
```
R ∪ S
```

### 🧪 Example: *“All students OR award winners”*
```
STUDENTS ∪ AWARD_WINNERS
```

| ID | Name  | Dept / Prize |
|----|-------|--------------|
| 1  | Ali   | CS           |
| 2  | Sara  | EE / Gold    |
| 3  | Ben   | CS / Silver  |
| 4  | Maya  | ME           |
| 5  | Tom   | Bronze       |

> ⚠️ Note: **Sara & Ben appear only once** — duplicates removed!

### ↔️ SQL:
```sql
SELECT ID, Name FROM STUDENTS
UNION
SELECT ID, Name FROM AWARD_WINNERS;
```

> 🔁 Use `UNION ALL` to keep duplicates.

---

## 2️⃣ **INTERSECTION (∩) — “Only those in *both* A and B”**

> ✅ Returns rows present in **both** relations.  
> 🎯 *Shared members — the overlap.*

### 🔤 Syntax:
```
R ∩ S
```

### 🧪 Example: *“Students who also won awards”*
```
π<sub>ID, Name</sub>(STUDENTS) ∩ π<sub>ID, Name</sub>(AWARD_WINNERS)
```

| ID | Name |
|----|------|
| 2  | Sara |
| 3  | Ben  |

> ✅ Only Sara & Ben are in both tables.

### ↔️ SQL:
```sql
SELECT ID, Name FROM STUDENTS
INTERSECT
SELECT ID, Name FROM AWARD_WINNERS;
```

> ⚠️ Not all DBs support `INTERSECT` (MySQL doesn’t — use `JOIN` or `IN` instead).

---

## 3️⃣ **MINUS (−) — “In A but *not* in B”**

> ✅ Returns rows in first relation **not** in second.  
> 🎯 *The difference — “exclusives”.*

### 🔤 Syntax:
```
R − S
```

### 🧪 Example: *“Students who did *not* win awards”*
```
π<sub>ID, Name</sub>(STUDENTS) − π<sub>ID, Name</sub>(AWARD_WINNERS)
```

| ID | Name |
|----|------|
| 1  | Ali  |
| 4  | Maya |

> ✅ Ali & Maya are students, but not award winners.

### ↔️ SQL:
```sql
SELECT ID, Name FROM STUDENTS
EXCEPT          -- PostgreSQL, SQL Server
-- MINUS        -- Oracle
SELECT ID, Name FROM AWARD_WINNERS;
```

> 🔄 In MySQL: Use `NOT IN` or `LEFT JOIN ... WHERE ... IS NULL`.

---

## 4️⃣ **CARTESIAN PRODUCT (×) — “Every possible pairing”**

> ✅ Combines **every row of A** with **every row of B**.  
> 🎯 *Like a menu: 3 appetizers × 4 mains = 12 combos.*

### 🔤 Syntax:
```
R × S
```

### 🧪 Example: *“Pair every student with every award winner”*
```
STUDENTS × AWARD_WINNERS
```

| S.ID | S.Name | S.Dept | A.ID | A.Name | A.Prize |
|------|--------|--------|------|--------|---------|
| 1    | Ali    | CS     | 2    | Sara   | Gold    |
| 1    | Ali    | CS     | 3    | Ben    | Silver  |
| 1    | Ali    | CS     | 5    | Tom    | Bronze  |
| 2    | Sara   | EE     | 2    | Sara   | Gold    |
| 2    | Sara   | EE     | 3    | Ben    | Silver  |
| ...  | ...    | ...    | ...  | ...    | ...     |
| 4    | Maya   | ME     | 5    | Tom    | Bronze  |

→ **4 students × 3 winners = 12 rows**

> ⚠️ Rarely useful alone — usually filtered with `σ` (e.g., `S.ID = A.ID` → join!).

---

## 🧩 Real Use: **Cartesian + Select = Join!**

Want *actual* student-award pairs (i.e., who won what)?

```
σ<sub>STUDENTS.ID = AWARD_WINNERS.ID</sub>(STUDENTS × AWARD_WINNERS)
```

| ID | Name | Dept | Prize  |
|----|------|------|--------|
| 2  | Sara | EE   | Gold   |
| 3  | Ben  | CS   | Silver |

✅ This is how **theta joins** are built.

### ↔️ SQL:
```sql
SELECT s.ID, s.Name, s.Dept, a.Prize
FROM STUDENTS s
JOIN AWARD_WINNERS a ON s.ID = a.ID;
```

---

## 🧠 Quick Mental Model

| Operation | Real-World Analogy |
|----------|--------------------|
| **UNION (∪)** | 🎒 *Merge backpacks* — take all unique items |
| **INTERSECTION (∩)** | 🤝 *Find mutual friends* |
| **MINUS (−)** | 🚫 *Remove allergens* from food list |
| **CARTESIAN (×)** | 🍽️ *Build a meal combo*: appetizer × main × dessert |

---

## ✅ Key Rules

| Rule | Why It Matters |
|------|----------------|
| ✅ **Union Compatibility** | For ∪, ∩, −: same # of columns, compatible types |
| ✅ **No Duplicates** | Relations are *sets* — duplicates auto-removed |
| ✅ **Order Doesn’t Matter** for ∪, ∩ (commutative), but **does** for − |

---

## 🆚 Summary Table

| Operation | Symbol | Meaning | SQL |
|----------|--------|---------|-----|
| **Union** | `R ∪ S` | A or B | `UNION` |
| **Intersect** | `R ∩ S` | A and B | `INTERSECT` |
| **Minus** | `R − S` | A but not B | `EXCEPT` / `MINUS` |
| **Cartesian** | `R × S` | All pairings | `CROSS JOIN` |

📌 **Memory Hook**:

> **∪** = **U**nion → **U**nited  
> **∩** = **I**ntersect → **I**n both  
> **−** = **M**inus → **M**issing in B  
> **×** = **C**ross → **C**ombinations

> *“Set ops are the LEGO bricks of queries — simple alone, powerful together.”* 🧱✨
