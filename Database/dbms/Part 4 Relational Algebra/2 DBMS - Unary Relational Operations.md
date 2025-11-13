
# 🔍 **DBMS – Unary Relational Operations**  
### *Filter Rows & Columns — the Building Blocks of Queries*

> 🧮 *Unary operations work on **one relation** (table) at a time.*  
> Only **two core ops**:  
> - **SELECT (σ)** → *Filter rows*  
> - **PROJECT (∏)** → *Filter columns*

Let’s master them — using a simple **`EMPLOYEE`** table.


## 📋 Sample Data: `EMPLOYEE`

| Fname    | Lname   | Dno | Salary |
|----------|---------|-----|--------|
| Ali      | Khan    | 4   | 25000  |
| Sara     | Lee     | 5   | 40000  |
| Ben      | Smith   | 4   | 30000  |
| Maya     | Patel   | 5   | 45000  |
| Tom      | Garcia  | 4   | 25000  |

> ✅ We’ll use just 4 columns for simplicity: `Fname`, `Lname`, `Dno`, `Salary`.

---

## 1️⃣ **SELECT (σ) — “Show me rows that match…”**

> ✅ **Purpose**: Filter **rows** based on a condition.  
> 🎯 *Like `WHERE` in SQL.*

### 🔤 Syntax:
```
σ<sub>condition</sub>(Relation)
```

### 🧪 Simple Examples:

| Operation | Meaning | Result Rows |
|----------|---------|-------------|
| `σ<sub>Dno = 4</sub>(EMPLOYEE)` | All employees in Dept 4 | Ali, Ben, Tom |
| `σ<sub>Salary > 35000</sub>(EMPLOYEE)` | High earners ( >$35K) | Sara, Maya |
| `σ<sub>Dno = 5 ∧ Salary > 40000</sub>(EMPLOYEE)` | Dept 5 + >$40K | Maya |
| `σ<sub>Dno = 4 ∨ Salary = 25000</sub>(EMPLOYEE)` | Either Dept 4 OR $25K | Ali, Ben, Tom, Sara |

> 💡 `∧` = AND, `∨` = OR  
> ✅ **Commutative**: `σ<sub>A</sub>(σ<sub>B</sub>(R)) = σ<sub>B</sub>(σ<sub>A</sub>(R))`

---

### ↔️ SQL Equivalent:
```sql
SELECT * 
FROM EMPLOYEE 
WHERE Dno = 4;
```

---

## 2️⃣ **PROJECT (∏) — “Show me only these columns…”**

> ✅ **Purpose**: Filter **columns** — keep only what you need.  
> 🎯 *Like `SELECT col1, col2` in SQL — with automatic `DISTINCT`!*

### 🔤 Syntax:
```
∏<sub>col1, col2, ...</sub>(Relation)
```

### 🧪 Simple Examples:

| Operation | Meaning | Output |
|----------|---------|--------|
| `∏<sub>Fname, Lname</sub>(EMPLOYEE)` | Just names | Ali Khan, Sara Lee, Ben Smith, Maya Patel, Tom Garcia |
| `∏<sub>Dno</sub>(EMPLOYEE)` | All departments (unique!) | **4, 5** *(duplicates removed!)* |
| `∏<sub>Salary</sub>(EMPLOYEE)` | Unique salaries | **25000, 30000, 40000, 45000** |

> ⚠️ **Duplicates are automatically removed!**  
> → `∏<sub>Dno</sub>` returns 2 rows — not 5.

---

### ↔️ SQL Equivalent:
```sql
SELECT DISTINCT Dno 
FROM EMPLOYEE;
```

---

## 🧩 Combine SELECT + PROJECT — Real Queries!

> ✅ Chain operations: First **filter rows**, then **filter columns**.

### 📌 Example: *“Names & salaries of employees in Dept 5”*

#### Step-by-Step:
1. **SELECT** Dept 5:  
   `TEMP ← σ<sub>Dno = 5</sub>(EMPLOYEE)`  
   → Sara, Maya

2. **PROJECT** name & salary:  
   `RESULT ← ∏<sub>Fname, Lname, Salary</sub>(TEMP)`

| Fname | Lname | Salary |
|-------|-------|--------|
| Sara  | Lee   | 40000  |
| Maya  | Patel | 45000  |

#### ✅ Inline (compact):
```
∏<sub>Fname, Lname, Salary</sub>(σ<sub>Dno = 5</sub>(EMPLOYEE))
```

### ↔️ SQL:
```sql
SELECT Fname, Lname, Salary
FROM EMPLOYEE
WHERE Dno = 5;
```

---

## 🏷️ Bonus: RENAME (ρ) — Give It a Friendly Name

> ✅ Use `ρ` (rho) to rename attributes or relations.

#### Example: *“Call ‘Fname’ → ‘First_Name’ in the output”*
```
ρ<sub>(First_Name, Last_Name)</sub>(∏<sub>Fname, Lname</sub>(EMPLOYEE))
```

| First_Name | Last_Name |
|------------|-----------|
| Ali        | Khan      |
| Sara       | Lee       |
| ...        | ...       |

### ↔️ SQL:
```sql
SELECT Fname AS First_Name, Lname AS Last_Name
FROM EMPLOYEE;
```

---

## 🧠 Quick Mental Model

| Operation | Real-World Analogy |
|----------|--------------------|
| **SELECT (σ)** | 🧾 *Filtering receipts*: “Show me all receipts > $50” |
| **PROJECT (∏)** | 📋 *Copying columns*: “Copy only Date and Amount from receipts” |
| **Together** | 📊 *Creating a summary report*: “List names & salaries of managers” |

---

## 🆚 Summary Table

| Operation | Symbol | Input | Output | SQL Equivalent |
|----------|--------|-------|--------|----------------|
| **Select** | σ | Relation | Subset of rows | `WHERE` |
| **Project** | ∏ | Relation | Subset of columns **(deduped!)** | `SELECT cols` + `DISTINCT` |
| **Rename** | ρ | Relation/Attrs | Renamed relation | `AS` |

---

## ✅ Key Takeaways

- 🔍 **SELECT = Filter Rows**  
- 📤 **PROJECT = Filter Columns + Remove Duplicates**  
- ➕ Combine them: `∏(...)(σ(...)(R))`  
- 🏷️ Use `ρ` to rename for clarity  
- 💡 **All relational queries start here** — even complex joins build on these!



📌 **Memory Hook**:

> **σ** = **S**elect **S**ome **R**ows  
> **∏** = **P**roject **P**articular **C**olumns  
> *(And remember: **P**roject removes **D**uplicates!)*

> *“Master σ and ∏ — and you’ve mastered 80% of querying.”* ✅✨
