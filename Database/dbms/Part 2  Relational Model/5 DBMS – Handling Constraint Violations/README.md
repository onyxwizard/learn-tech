# 🛠️ **DBMS – Handling Constraint Violations**  
### *When Data Breaks the Rules — How DBMS Fights Back*

> ⚠️ *Constraints protect truth. But users *will* try to break them.*  
> Your DBMS doesn’t just say *“no”* — it offers **smart strategies** to preserve integrity *without* breaking workflows.

Let’s explore **real violations**, **automatic defenses**, and **transactional safety nets** — using the **COMPANY database** as our battlefield.



## 🔄 The Big Three: Insert, Delete, Update

| Operation | Purpose | Risk Level |
|----------|---------|------------|
| ➕ **INSERT** | Add new records | ⚠️ Medium (domain/PK/FK issues) |
| ➖ **DELETE** | Remove records | ⚠️⚠️ **High** (orphaned FKs!) |
| ✏️ **UPDATE** | Modify records | ⚠️⚠️⚠️ **Highest** (PK/FK changes = identity crisis) |

> 💡 *All operations run inside **transactions** — more on that later.*



## 🚨 Common Constraint Violations & Real Examples

### 1️⃣ **➕ INSERT Violations**

| Violation | Example | DBMS Response |
|----------|---------|---------------|
| **Domain** | `INSERT INTO Employee(Age) VALUES ('abc');` | ❌ Reject: *“Invalid integer”* |
| **PK Duplicate** | `INSERT INTO Employee(Ssn) VALUES ('123-45-6789');` (already exists) | ❌ Reject: *“Duplicate SSN”* |
| **NULL PK** | `INSERT INTO Employee(Ssn) VALUES (NULL);` | ❌ Reject: *“PK cannot be NULL”* |
| **FK Missing** | `INSERT INTO Employee(Dno) VALUES (99);` (no Dept 99) | ❌ Reject: *“Dept 99 not found”* |

> 💬 *User sees*:  
> `ERROR 1452 (23000): Cannot add or update a child row: a foreign key constraint fails...`



### 2️⃣ **➖ DELETE Violations (The Orphan Crisis)**

Deleting a record referenced by others → **referential integrity breach**.

#### 📋 Scenario:
Delete `Employee(Ssn = '333-44-5555')` — but this person is:
- Manager of **Research Dept** (`DEPARTMENT.Mgr_ssn`)
- Working on **3 projects** (`WORKS_ON.Essn`)
- Has **2 dependents** (`DEPENDENT.Essn`)

➡️ **Result**: Orphaned records with dangling references.

| Strategy | Action | When to Use |
|---------|--------|-------------|
| **🚫 RESTRICT** (default) | ❌ Block deletion | *Critical data — e.g., active managers* |
| **🌀 CASCADE** | ✅ Delete all dependent records | *Test cleanup / soft-delete workflows* |
| **🔘 SET NULL** | ✅ Set `Mgr_ssn = NULL`, `Essn = NULL` | *Graceful degradation — “unassign”* |
| **🔘 SET DEFAULT** | ✅ Reassign to default (e.g., `Dno = 1`) | *Reassign to “Unassigned” dept* |

#### 💡 SQL Implementation:
```sql
-- Safe: Prevent deletion if referenced
ALTER TABLE Department
ADD CONSTRAINT fk_mgr 
FOREIGN KEY (Mgr_ssn) REFERENCES Employee(Ssn)
ON DELETE RESTRICT;

-- Flexible: Unassign employees on manager deletion
ALTER TABLE Works_On
ADD CONSTRAINT fk_essn
FOREIGN KEY (Essn) REFERENCES Employee(Ssn)
ON DELETE SET NULL;
```



### 3️⃣ **✏️ UPDATE Violations (The Identity Shift)**

Updating **PKs or FKs** is risky — it’s like changing someone’s SSN or employer mid-contract.

| Violation | Example | Resolution |
|----------|---------|------------|
| **PK → Duplicate** | `UPDATE Employee SET Ssn = '123-45-6789' WHERE Ssn = '999-88-7777';` | ❌ Reject: *“SSN already exists”* |
| **FK → Invalid** | `UPDATE Employee SET Dno = 99 WHERE Ssn = '999-88-7777';` | ❌ Reject: *“Dept 99 missing”* |
| **PK Change → FK Mismatch** | Change `Ssn = '333-44-5555'` → `Ssn = '000-00-0000'`, but `Department.Mgr_ssn` still points to old value | ⚠️ **Cascade UPDATE** needed |

#### 🛠️ **CASCADE UPDATE** (Rare but Powerful)
```sql
ALTER TABLE Department
ADD CONSTRAINT fk_mgr_update
FOREIGN KEY (Mgr_ssn) REFERENCES Employee(Ssn)
ON UPDATE CASCADE;  -- Auto-update Mgr_ssn when Ssn changes!
```

> ⚠️ *Use with extreme caution!* PK changes should be avoided — use surrogate keys instead.



## 🧩 Smart DBMS: Beyond “Reject” — Proactive Handling

Modern DBMSs don’t just fail — they **guide** users:

| Violation | User-Friendly Recovery |
|----------|------------------------|
| **FK Missing** | ❗ *“Dept 99 not found. Would you like to:* <br> • *Create Dept 99?* <br> • *Choose existing dept?* <br> • *Cancel?”* |
| **NULL PK** | ❗ *“SSN is required. Please enter a valid SSN (e.g., 123-45-6789).”* |
| **Domain Error** | ❗ *“Age must be 18–65. You entered 17.”* → *Auto-suggest: “Did you mean 27?”* |

> 🌐 *Used in admin UIs (e.g., phpMyAdmin, DBeaver), not raw SQL.*



## 🔁 Transactions: The Safety Net

> 🛡️ *A transaction is a **single logical unit of work** — all or nothing.*

### ✅ ACID in Action (Bank Transfer Example)

```sql
BEGIN TRANSACTION;
UPDATE Accounts SET Balance = Balance - 500 WHERE AccID = 'A1';  -- Debit
UPDATE Accounts SET Balance = Balance + 500 WHERE AccID = 'A2';  -- Credit
COMMIT;
```

| Step | What Happens If Constraint Fails? |
|------|-----------------------------------|
| **Before COMMIT** | DBMS checks all constraints |
| **Violation Detected** | ❌ **ROLLBACK** — undo *both* updates |
| **After ROLLBACK** | DB remains in original state — no $500 vanishes! |

> 💡 *Without transactions*: Partial updates → inconsistent state (e.g., money debited but not credited).



## 🧪 Real-World Scenario Walkthrough

### 🎯 Problem: Update Employee’s Dept to Non-Existent Value
```sql
UPDATE EMPLOYEE 
SET Dno = 10 
WHERE Ssn = '999-88-7777';
```

#### 🔍 What the DBMS Does:
1. Checks: Does `Dnumber = 10` exist in `DEPARTMENT`? → **No**  
2. Checks FK constraint on `EMPLOYEE.Dno` → **Violation**  
3. Aborts update → rolls back  
4. Returns error:  
   > `ERROR 1452: Cannot add or update a child row: ... FOREIGN KEY (Dno) REFERENCES Department(Dnumber)`

#### ✅ Fix Options:
- ✅ **Create Dept 10 first**  
- ✅ **Use existing Dept (e.g., Dno = 5)**  
- ✅ **Set Dno = NULL** (if allowed)

## 🎯 Best Practices for Constraint Handling

| Principle | Why | How |
|---------|-----|-----|
| **✅ Prefer RESTRICT over CASCADE** | Prevent accidental data loss | Default for critical tables (e.g., `Customer`, `Account`) |
| **✅ Use SET NULL for optional relationships** | Avoid orphaning | `WORKS_ON.Essn`, `Employee.ManagerID` |
| **✅ Avoid PK updates** | Too risky — breaks references | Use surrogate keys (`EmployeeID INT AUTO_INCREMENT`) |
| **✅ Validate early in apps** | Reduce DB round-trips | Check FK existence before `INSERT` |
| **✅ Log violations for audit** | Track attempted breaches | DB triggers → `ViolationLog` table |



## 🖼️ Visual Summary: Violation Response Flow

```
┌───────────────────────┐
│   User Operation      │
│   (INSERT/UPDATE/DEL) │
└──────────┬────────────┘
           ↓
┌───────────────────────┐
│   DBMS Checks ALL     │
│   Constraints in TX   │
└──────────┬────────────┘
     ✅ Pass?             ❌ Fail?
       ↓                     ↓
┌─────────────┐     ┌───────────────────────┐
│   COMMIT    │     │      ROLLBACK         │
│   (Save)    │     │   (Undo all changes)  │
└─────────────┘     └───────────┬───────────┘
                                ↓
                   ┌──────────────────────────┐
                   │   Return Error Message   │
                   │   + Suggested Fix (UI)   │
                   └──────────────────────────┘
```


## 🧠 Pro Tip: Violation Debugging Checklist

When a constraint fails:
1. 🔍 **Which table/column?** (Check error code & message)  
2. 🔗 **Is it a PK, FK, or CHECK?**  
3. 📋 **What value caused it?** (e.g., `Dno=99`)  
4. 🔎 **Does referenced record exist?** (`SELECT * FROM Department WHERE Dnumber = 99;`)  
5. 🛠️ **Which strategy fits?** (RESTRICT? CASCADE? SET NULL?)

> 🛠️ *Tool Tip*: Use `SHOW ENGINE INNODB STATUS` (MySQL) or `pg_constraint` (PostgreSQL) to diagnose.


📌 **Quick Memory Hook:**

> ➕ **INSERT**: Check domain, PK, FK  
> ➖ **DELETE**: Orphans? → RESTRICT / CASCADE / SET NULL  
> ✏️ **UPDATE**: PK/FK changes = high risk  
> 🔁 **TRANSACTION**: All or nothing — ACID saves the day  

> *“Constraints don’t hinder progress — they ensure every step forward is solid ground.”* 🏗️✨

