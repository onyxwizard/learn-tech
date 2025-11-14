# 🔤 **DBMS – Domain Relational Calculus (DRC)**  
### *Querying by Columns, Not Rows*

> 🔍 *Tuple Relational Calculus (TRC)* says:  
> ✅ *“Give me rows where this condition holds.”*  
>  
> *Domain Relational Calculus (DRC)* says:  
> 🔥 *“Give me values of **these attributes** for which this condition holds.”*  
>  
> Instead of whole rows (`t`), DRC uses **domain variables** — one per attribute/column.

Let’s master it — with **Obsidian-friendly LaTeX**, real examples, and clear comparisons to TRC.

---

## 🔍 What is Domain Relational Calculus?

> ✅ **Definition**: A **non-procedural**, **declarative** query language where **variables range over attribute domains** (e.g., `Ssn`, `Name`, `Salary`), not whole tuples.

### 🔤 General Form:
$$
\{ \, x_1, x_2, \dots, x_n \mid \text{COND}(x_1, x_2, \dots, x_n) \, \}
$$
- `x₁, ..., xₙ` = domain variables (values of attributes)  
- `COND(...)` = logical condition using those variables  
- Result = **all combinations** of values satisfying the condition

> 🆚 *TRC*: `{ t \| COND(t) }` → *t is a row*  
> 🆚 *DRC*: `{ a, b \| COND(a, b) }` → *a, b are column values*

---

## 🧱 Core Concepts

| Concept | Description | Example |
|--------|-------------|---------|
| **Domain Variable** | Represents a value from an attribute’s domain | `fname`, `ssn`, `dname` |
| **Relation Atom** | `R(x₁, x₂, ..., xₖ)` binds variables to a relation’s attributes | `EMPLOYEE(fname, minit, lname, ssn, ..., dno)` |
| **Logical Operators** | `∧`, `∨`, `¬` | `salary > 50000 ∧ dno = 5` |
| **Quantifiers** | `∃x`, `∀x` — bind variables to finite domains | `∃ssn_m (DEPARTMENT(..., ssn_m) ∧ ssn_m = ssn)` |
| **Result List** | Variables before `\|` define output columns | `{ fname, lname \| ... }` |

> ⚠️ **Order matters** in relation atoms:  
> `EMPLOYEE(Fname, Minit, Lname, Ssn, Bdate, Address, Sex, Salary, Super_ssn, Dno)`  
> → `EMPLOYEE(f, m, l, s, b, a, x, sal, sup, d)`

---

## 🧪 DRC Examples (COMPANY DB)

We’ll use abbreviated schemas for readability:
- `EMPLOYEE(f, l, ssn, dno, sal)`  
  (`f=Fname`, `l=Lname`, `ssn=Ssn`, `dno=Dno`, `sal=Salary`)
- `DEPARTMENT(dname, dnum, mgr_ssn)`  
- `PROJECT(pname, pnum, dnum, ploc)`  
- `DEPENDENT(essn, dname_dep)`

---

### 1️⃣ Employees in *Research* Dept (Names & Addresses)

> 🎯 *“First name, last name, and address of employees in Research.”*

#### 🔤 DRC Query:
$$
\{ \, f, l, addr \mid (\exists dno)\,(\exists mgr)\,(\text{EMPLOYEE}(f, m, l, ssn, b, addr, x, sal, sup, dno) \;\land\; \text{DEPARTMENT}(\text{'Research'}, dno, mgr)) \, \}
$$

#### 🧠 Simpler (using known positions):  
Assume `EMPLOYEE = (f, m, l, ssn, b, addr, x, sal, sup, dno)`, `DEPARTMENT = (dname, dnum, mgr_ssn)`:
$$
\{ \, f, l, addr \mid (\exists dno)\,(\text{EMPLOYEE}(f, \_, l, \_, \_, addr, \_, \_, \_, dno) \;\land\; \text{DEPARTMENT}(\text{'Research'}, dno, \_)) \, \}
$$

> 💡 `_` means “any value” — we ignore unneeded attributes.

#### ✅ Result: Ali, Franklin, Ramesh, Joyce + addresses  
→ Same as TRC/RA.

---

### 2️⃣ Projects in *Stafford* + Manager Info

> 🎯 *“Pnumber, Dnum, manager’s Lname, Address.”*

#### 🔤 DRC Query:
$$
\begin{align*}
\{ \, &pnum, dnum, mlname, maddr \mid \\
&(\exists pname)\,(\exists ploc)\,(\exists dname)\,(\exists mgr\_ssn)\,(\exists mf)\,(\exists mm)\,(\exists mb)\,(\exists mx)\,(\exists msal)\,(\exists msup) \\
&\quad \text{PROJECT}(pname, pnum, ploc, dnum) \;\land\; ploc = \text{'Stafford'} \;\land \\
&\quad \text{DEPARTMENT}(dname, dnum, mgr\_ssn) \;\land \\
&\quad \text{EMPLOYEE}(mf, mm, mlname, mgr\_ssn, mb, maddr, mx, msal, msup, \_) \, \}
\end{align*}
$$

#### 🧠 Why so many `∃`?  
- We need to bind every attribute used — even if we don’t output it.  
- `mgr_ssn` links `DEPARTMENT` → `EMPLOYEE`.

#### ✅ Result:  
| Pnumber | Dnum | Lname   | Address                      |
|---------|------|---------|------------------------------|
| 10      | 4    | Wallace | 291 Berry, Bellaire, TX      |
| 30      | 4    | Wallace | 291 Berry, Bellaire, TX      |

---

### 3️⃣ Employees **Without** Dependents

> ✅ *Negation + existential quantifier.*

#### 🔤 DRC Query:
$$
\{ \, f, l \mid \text{EMPLOYEE}(f, \_, l, ssn, \_, \_, \_, \_, \_, \_) \;\land\; \neg(\exists dname\_dep)\,(\text{DEPENDENT}(ssn, dname\_dep)) \, \}
$$

#### 🧠 Meaning:  
- Employee with `ssn` such that **no** `DEPENDENT(ssn, ...)` exists.

#### ✅ Result: Alicia, Ramesh, Joyce, Ahmad, James

> 🔄 *Equivalent to TRC: `{ e.Fname, e.Lname \| EMPLOYEE(e) ∧ ¬∃d (DEPENDENT(d) ∧ e.Ssn = d.Essn) }`*

---

### 4️⃣ **Managers with Dependents**

> ✅ *Two existential conditions: manager + dependent.*

#### 🔤 DRC Query:
$$
\{ \, f, l \mid 
\text{EMPLOYEE}(f, \_, l, ssn, \_, \_, \_, \_, \_, \_) \;\land\;
(\exists dnum)\,(\text{DEPARTMENT}(\_, dnum, ssn)) \;\land\;
(\exists dname\_dep)\,(\text{DEPENDENT}(ssn, dname\_dep))
\, \}
$$

#### ✅ Result: **Franklin Wong**, **Jennifer Wallace**

---

## ⚠️ Safety in DRC

A DRC query is **safe** iff it returns a **finite** result.

### ✅ Safe Query:
$$
\{ \, f, l \mid \text{EMPLOYEE}(f, \_, l, ssn, \_, \_, \_, \_, \_, \_) \;\land\; sal > 50000 \, \}
$$

### ❌ Unsafe Query:
$$
\{ \, x, y \mid \neg\text{EMPLOYEE}(x, y, \_, \_, \_, \_, \_, \_, \_, \_) \, \}
$$
→ Infinite: all `(x, y)` pairs **not** in `EMPLOYEE`.

> 🛡️ **Rule**: All variables must be **bound** to relations via `R(...)` — never free in negation.

---

## 🆚 DRC vs. TRC vs. Relational Algebra

| Feature | DRC | TRC | Relational Algebra |
|--------|-----|-----|---------------------|
| **Variables** | Attribute values (`f`, `l`, `ssn`) | Whole tuples (`t`, `e`, `d`) | Relations (tables) |
| **Granularity** | Column-level | Row-level | Set-level |
| **Readability** | High for attribute-focused logic | High for entity-focused logic | High for engineers |
| **Expressiveness** | ≡ TRC ≡ RA (Codd’s Theorem) | ≡ DRC ≡ RA | ≡ TRC ≡ DRC |
| **SQL Analogy** | Closest to `SELECT a, b WHERE ...` | Closest to `SELECT * WHERE EXISTS (...)` | Closest to query plans |

> 🎯 *All three are equivalent in power* — but DRC matches **attribute-centric thinking** best.

---

## 🧠 Key Takeaways (Obsidian Note-Taking Tips)

- ✅ Use `$ \{ x, y \mid \text{COND}(x, y) \} $` for basic form  
- ✅ `R(a, b, c)` binds variables to relation’s attribute order  
- ✅ Use `_` for ignored attributes: `EMPLOYEE(f, _, l, ssn, ...)`  
- ✅ Quantifiers (`∃x`) bind variables to finite domains  
- ✅ Avoid `¬R(...)` without existential guards

📌 **Obsidian Pro Tips**:
- Define relation schemas at top of note:  
  `%% EMPLOYEE = (Fname, Minit, Lname, Ssn, ..., Dno) %%`  
- Use `\text{}` for strings: `$ \text{'Research'} $`  
- For long queries, break with `\\` inside `$$ ... $$`