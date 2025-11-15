# 🔗 **DBMS – Functional Dependency (FD)**  
### *The Hidden Rules That Govern Your Tables*

> 🧩 *Functional dependency is the “grammar” of your data — it tells you which columns **determine** others.*  
> Break it → you get redundancy, update anomalies, and inconsistent reports.

Let’s demystify it — with simple math, clear examples, and **Obsidian-ready notation**.

## 🔍 What is a Functional Dependency?

> ✅ **Definition**:  
> In a relation $ R $, an attribute set $ X $ **functionally determines** attribute set $ Y $ — written:  
> $$
> X \rightarrow Y
> $$  
> iff: **for any two tuples $ t_1, t_2 \in R $**,  
> if $ t_1[X] = t_2[X] $, then $ t_1[Y] = t_2[Y] $.

> 🗣️ *“Same $ X $ ⇒ always same $ Y $.”*

---

### 🧮 Real-World Analogy: Math Functions  
| Math | Database |
|------|----------|
| $ f(x) = x^2 $ | $ \text{RollNo} \rightarrow \text{Name} $ |
| Input `x=5` ⇒ Output `25` | RollNo=`101` ⇒ Name=`Ali` |
| Same input → same output | Same RollNo → same Name |

> ⚠️ *Unlike math, databases don’t compute $ Y $ — they just **look it up**.*

---

## 🧪 Functional Dependency in Action

### ✅ Valid FD: $ A \rightarrow B $
| A | B |
|---|---|
| 1 | X |
| 2 | Y |
| 3 | Z |

→ $ A = 2 \Rightarrow B = Y $ — *always*.  
✅ **Valid**: No two rows with same `A` have different `B`.

---

### ❌ Invalid FD: $ A \rightarrow B $
| A | B |
|---|---|
| 1 | X |
| 1 | Y | ← Same `A`, different `B`!  
| 2 | Z |

→ $ A = 1 $ gives *both* `X` and `Y` → violates FD.  
❌ **Invalid**.

---

## 🧱 Key Components

| Term | Role | Example |
|------|------|---------|
| **Determinant** | Left side (`X` in $ X \rightarrow Y $) | `RollNo`, `DeptName` |
| **Dependent** | Right side (`Y`) | `Name`, `Building` |
| **Trivial FD** | $ Y \subseteq X $ | $ \{A, B\} \rightarrow A $ |
| **Non-Trivial FD** | $ Y \nsubseteq X $ | $ \text{RollNo} \rightarrow \text{Name} $ |

> 💡 *Only non-trivial FDs matter for normalization.*

---

## 🧪 Real Examples (Student Table)

| RollNo | Name  | DeptName | Building |
|--------|-------|----------|----------|
| 101    | Ali   | CS       | A4       |
| 102    | Sara  | IT       | B2       |
| 103    | Ben   | CS       | A4       |
| 104    | Tom   | ME       | B2       |

### ✅ Valid Functional Dependencies

| FD | Why? |
|----|------|
| $ \text{RollNo} \rightarrow \text{Name, DeptName, Building} $ | RollNo is unique → determines all |
| $ \text{DeptName} \rightarrow \text{Building} $ | `CS → A4`, `IT → B2`, `ME → B2` — same dept ⇒ same building |
| $ \text{RollNo, DeptName} \rightarrow \text{Name} $ | Still valid (superset of determinant) |

> 🔁 *Note*: $ \text{DeptName} \rightarrow \text{Building} $ is valid **even though** `B2` appears twice — FD only requires **consistency**, not uniqueness.

---

### ❌ Invalid Functional Dependencies

| FD | Counterexample |
|----|----------------|
| $ \text{Name} \rightarrow \text{DeptName} $ | If two `Ali`s exist: one in CS, one in IT → same name, different depts |
| $ \text{Building} \rightarrow \text{DeptName} $ | `B2 → IT` and `B2 → ME` → one building, multiple depts |

> 🚫 **Same determinant → different dependents = Invalid FD**

---

## 📊 FD Validity Checklist

| Scenario | Valid? | Example |
|----------|--------|---------|
| 🔑 Unique determinant → anything | ✅ | `RollNo → Name` |
| 🔄 Same determinant → same dependent (redundant) | ✅ | `DeptName → Building` (CS always → A4) |
| ➕ Multiple determinants → same dependent | ✅ | `IT → B2`, `ME → B2` |
| 🚨 Same determinant → different dependents | ❌ | `Name → Dept` (two "Ali"s in different depts) |

---

## ⚙️ Why Functional Dependencies Matter

| Problem | Caused by Ignoring FDs | Solved By |
|---------|------------------------|-----------|
| **Insertion Anomaly** | Can’t add dept until first student | Normalize using FDs |
| **Update Anomaly** | Change `CS → A4` in 100 rows — miss one? | Store `Dept → Building` once |
| **Deletion Anomaly** | Delete last CS student → lose `CS → A4` | Separate tables per FD |
| **Redundancy** | `Building = "A4"` repeated for all CS students | Decompose tables |

> 💡 *FDs are the compass for normalization* — they tell you *how* to split tables.

---

## 🔬 Formal Properties (Armstrong’s Axioms)

FDs follow 3 inference rules — let $ X, Y, Z \subseteq R $:

| Rule | Notation | Meaning |
|------|----------|---------|
| **Reflexivity** | If $ Y \subseteq X $, then $ X \rightarrow Y $ | `RollNo, Name → Name` |
| **Augmentation** | If $ X \rightarrow Y $, then $ XZ \rightarrow YZ $ | `RollNo → Name` ⇒ `RollNo, Dept → Name, Dept` |
| **Transitivity** | If $ X \rightarrow Y $ and $ Y \rightarrow Z $, then $ X \rightarrow Z $ | `RollNo → Dept`, `Dept → Building` ⇒ `RollNo → Building` |

> 🧠 *All other FDs (e.g., union, decomposition) derive from these.*

---

## 🧩 Attribute Closure & Candidate Keys

> 🔑 A **superkey** is any $ X $ such that $ X \rightarrow \text{All Attributes} $.  
> A **candidate key** is a *minimal* superkey.

### 📌 How to find candidate keys:
1. Compute **attribute closure** $ X^+ $: all attributes determined by $ X $
2. If $ X^+ = R $, then $ X $ is a superkey
3. Remove attributes — if still a superkey, not minimal

#### ✅ Example:  
Given FDs:  
- $ \text{RollNo} \rightarrow \text{Name, Dept} $  
- $ \text{Dept} \rightarrow \text{Building} $

Then:  
- $ \{\text{RollNo}\}^+ = \{ \text{RollNo, Name, Dept, Building} \} $ → ✅ candidate key  
- $ \{\text{RollNo, Name}\}^+ = \text{All} $, but not minimal → ❌ superkey only

---

## 🧠 Key Takeaways (Obsidian Note)

- ✅ FD: $ X \rightarrow Y $ means *same $ X $ ⇒ same $ Y $*  
- ✅ Valid even with duplicates — as long as mapping is **consistent**  
- ❌ Invalid if one $ X $ maps to multiple $ Y $  
- 🔑 Candidate keys = minimal $ X $ where $ X^+ = \text{All Attributes} $  
- 🛠️ Use FDs to: eliminate redundancy, prevent anomalies, guide normalization

> 💬 *“Functional dependencies don’t just describe data — they prescribe structure.”*
