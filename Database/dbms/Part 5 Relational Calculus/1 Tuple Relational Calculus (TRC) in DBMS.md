# 📝 **DBMS – Tuple Relational Calculus (TRC)**  
### *What to Get — Not How to Get It*

> 🧠 *Relational Algebra says*:  
> ✅ *“Join these tables, filter rows, project columns.”*  
>  
> *Tuple Relational Calculus says*:  
> 🔥 *“Give me all tuples where this condition is true.”*  
>  
> It’s **declarative**, **logic-based**, and the foundation of **SQL’s `WHERE` clause**.

Let’s master it — using the **COMPANY database** and **Obsidian-friendly LaTeX**.

---

## 🔍 What is Tuple Relational Calculus?

> ✅ **Definition**: A **non-procedural**, **declarative** query language based on **predicate logic**.  
> - You specify **what** you want — not **how** to compute it.  
> - The DBMS figures out the execution plan.

### 🔤 General Form:
$$
\{ \, t \mid \text{COND}(t) \, \}
$$
- `t` = tuple variable (represents a row)  
- `COND(t)` = logical condition (must evaluate to `TRUE`/`FALSE`)  
- Result = **all tuples `t`** for which `COND(t)` is `TRUE`

> ⚠️ *Unlike RA, TRC doesn’t require step-by-step operations — just logic.*

---

## 🧱 Core Components

| Component | Description | Example |
|----------|-------------|---------|
| **Tuple Variable** | Range over a relation (table) | `EMPLOYEE(t)` → `t` is a row in `EMPLOYEE` |
| **Attribute Reference** | `t.Attribute` | `t.Salary`, `t.Dno` |
| **Logical Operators** | `AND`, `OR`, `NOT` | `t.Salary > 30000 AND t.Dno = 5` |
| **Quantifiers** | `∃` (exists), `∀` (for all) | `∃d (DEPARTMENT(d) AND d.Dnumber = t.Dno)` |
| **Result Attributes** | List before `\|` | `{ t.Fname, t.Lname \| ... }` |

---

## 🧪 TRC Examples (COMPANY DB)

We’ll use:
- `EMPLOYEE(Fname, Lname, Ssn, Dno, Salary, ...)`
- `DEPARTMENT(Dname, Dnumber, Mgr_ssn, ...)`
- `PROJECT(Pname, Pnumber, Dnum, Plocation)`
- `WORKS_ON(Essn, Pno, Hours)`

---

### 1️⃣ Employees in *Research* Dept (with Addresses)

> 🎯 *“Give me names & addresses of employees in Research.”*

#### 🔤 TRC Query:
$$
\{ \, t.\text{Fname},\; t.\text{Lname},\; t.\text{Address} \;\mid\; \text{EMPLOYEE}(t) \;\land\; (\exists d)\,(\text{DEPARTMENT}(d) \;\land\; d.\text{Dname} = \text{'Research'} \;\land\; d.\text{Dnumber} = t.\text{Dno}) \, \}
$$

#### 🧠 Breakdown:
- `EMPLOYEE(t)` → `t` is an employee  
- `∃d(...)` → there exists a department `d` such that:  
  - `d.Dname = 'Research'`  
  - `d.Dnumber = t.Dno` (join condition)  

#### ✅ Result: Ali, Franklin, Ramesh, Joyce  
*(Same as RA example!)*

> 💡 *This is how SQL’s `WHERE EXISTS (...)` works.*

---

### 2️⃣ Projects in *Stafford* + Manager Info

> 🎯 *“List projects in Stafford: `Pnumber`, `Dnum`, manager’s `Lname`, `Bdate`, `Address`.”*

#### 🔤 TRC Query:
$$
\begin{align*}
\{ \, &p.\text{Pnumber},\; p.\text{Dnum},\; m.\text{Lname},\; m.\text{Bdate},\; m.\text{Address} \;\mid \\
&\text{PROJECT}(p) \;\land\; \text{EMPLOYEE}(m) \;\land\; p.\text{Plocation} = \text{'Stafford'} \;\land \\
&(\exists d)\,(\text{DEPARTMENT}(d) \;\land\; p.\text{Dnum} = d.\text{Dnumber} \;\land\; d.\text{Mgr\_ssn} = m.\text{Ssn}) \, \}
\end{align*}
$$

#### 🧠 Why 3 variables?  
- `p` = project  
- `m` = manager (employee)  
- `d` = department (joins `p` and `m`)  

#### ✅ Result:  
| Pnumber | Dnum | Lname   | Bdate       | Address                      |
|---------|------|---------|-------------|------------------------------|
| 10      | 4    | Wallace | 1941-06-20  | 291 Berry, Bellaire, TX      |
| 30      | 4    | Wallace | 1941-06-20  | 291 Berry, Bellaire, TX      |

---

### 3️⃣ Employees Working on **All** Dept 5 Projects  
> 🔥 *The universal quantifier (`∀`) shines here.*

#### 🔤 TRC Query:
$$
\{ \, e.\text{Fname},\; e.\text{Lname} \;\mid\; \text{EMPLOYEE}(e) \;\land\; (\forall x)\,(\text{PROJECT}(x) \;\land\; x.\text{Dnum} = 5 \;\rightarrow\; (\exists w)\,(\text{WORKS\_ON}(w) \;\land\; w.\text{Essn} = e.\text{Ssn} \;\land\; w.\text{Pno} = x.\text{Pnumber})) \, \}
$$

#### 🧠 Logic:
- For **all** projects `x` in Dept 5 →  
- There **exists** a `WORKS_ON` record `w` where:  
  - `w.Essn = e.Ssn` (employee works on it)  
  - `w.Pno = x.Pnumber` (correct project)  

> ⚠️ Note: `A → B` is equivalent to `¬A ∨ B` — safer than `∀` with `AND`.

#### ✅ Result: **John Smith**, **Joyce English**

> 💡 *This is TRC’s version of the division operator (`÷`) in RA.*

---

### 4️⃣ Employees **Without** Dependents  
> ✅ *Negation + Existential Quantifier.*

#### 🔤 TRC Query:
$$
\{ \, e.\text{Fname},\; e.\text{Lname} \;\mid\; \text{EMPLOYEE}(e) \;\land\; \neg(\exists d)\,(\text{DEPENDENT}(d) \;\land\; e.\text{Ssn} = d.\text{Essn}) \, \}
$$

#### 🧠 Meaning:  
- Employee `e` such that **there does NOT exist** a dependent `d` linked to `e`.

#### ✅ Result: Alicia, Ramesh, Joyce, Ahmad, James

> 🔄 *Equivalent to RA: `π_{Fname,Lname}(EMPLOYEE) − π_{Fname,Lname}(EMPLOYEE ⋈ DEPENDENT)`*

---

### 5️⃣ **Managers with Dependents**  
> ✅ *Two existential quantifiers — one for dept, one for dependent.*

#### 🔤 TRC Query:
$$
\{ \, e.\text{Fname},\; e.\text{Lname} \;\mid\; \text{EMPLOYEE}(e) \;\land\; (\exists d)\,(\text{DEPARTMENT}(d) \;\land\; d.\text{Mgr\_ssn} = e.\text{Ssn}) \;\land\; (\exists dep)\,(\text{DEPENDENT}(dep) \;\land\; dep.\text{Essn} = e.\text{Ssn}) \, \}
$$

#### ✅ Result: **Franklin Wong**, **Jennifer Wallace**

---

## ⚠️ Safety in TRC: Avoiding Infinite Results

A TRC query is **safe** iff it returns a **finite** result — even with negation/quantifiers.

### ✅ Safe Query:
$$
\{ \, t \;\mid\; \text{EMPLOYEE}(t) \;\land\; t.\text{Salary} > 50000 \, \}
$$
→ Only existing employees with high salary.

### ❌ Unsafe Query:
$$
\{ \, t \;\mid\; \neg\text{EMPLOYEE}(t) \, \}
$$
→ All tuples **not** in `EMPLOYEE` → infinite! (e.g., `("X", "Y", ...)` forever)

> 🛡️ **Rule**: Always bind variables to **existing relations** via `R(t)` before using attributes.

## 🆚 TRC vs. Relational Algebra

| Feature | Tuple Relational Calculus | Relational Algebra |
|--------|----------------------------|---------------------|
| **Style** | Declarative (*what*) | Procedural (*how*) |
| **Basis** | Predicate logic | Set theory + algebra |
| **Query Form** | `{ t \| COND(t) }` | `∏(...)(σ(...)(R ⋈ S))` |
| **Readability** | High for logic experts | High for engineers |
| **Expressiveness** | Equivalent (Codd’s Theorem) | Equivalent |
| **Used in** | SQL theory, query optimization | DBMS optimizers, RA-based tools |

> 🎯 *SQL is closer to TRC* — e.g.,  
> ```sql
> SELECT Fname, Lname 
> FROM EMPLOYEE E
> WHERE EXISTS (
>   SELECT 1 FROM DEPARTMENT D 
>   WHERE D.Dname = 'Research' AND D.Dnumber = E.Dno
> );
> ```

## 🧠 Key Takeaways (Obsidian Note-Taking Tips)

- ✅ Use `$ \{ t \mid \text{COND}(t) \} $` for basic form  
- ✅ Quantifiers: `$ \exists $`, `$ \forall $`, `$ \neg $`  
- ✅ Always include `$ R(t) $` to bind tuple variables  
- ✅ `A → B` is safer than `∀x (A ∧ B)` for “for all” queries  
- ✅ TRC ≡ RA in power — but TRC matches human reasoning better

📌 **Obsidian Pro Tips**:
- Define shorthands:  
  `%% Let EMP = EMPLOYEE, DEPT = DEPARTMENT %%`  
- Use `\text{}` for multi-word attributes: `$ \text{Mgr\_ssn} $`  
- For long queries, use `$$ ... $$` + line breaks (`\\`) for readability
