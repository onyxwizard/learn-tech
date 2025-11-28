# 🔑 **DBMS – Attribute Closure & Candidate Keys**  
### *The Algorithmic Heart of Normalization*

> 🔍 **Attribute closure** tells you *what you can determine* from a set of attributes.  
> ✅ **Candidate keys** are the *minimal sets* whose closure = all attributes.

Let’s master how to compute them — using **functional dependencies**, **Armstrong’s axioms**, and smart strategies.
## 📜 What is Attribute Closure?

Given:
- A set of attributes $ X \subseteq R $
- A set of FDs $ F $

The **attribute closure** $ X^+ $ is the set of all attributes *functionally determined* by $ X $ under $ F $.

> 🎯 Formally:  
> $$
> X^+ = \{ A \mid X \rightarrow A \text{ is implied by } F \}
> $$

---

### 🔁 Algorithm to Compute $ X^+ $

```plaintext
Input:  Attribute set X, FD set F
Output: X⁺

1. closure ← X
2. repeat
3.    changed ← false
4.    for each FD Y → Z in F do
5.        if Y ⊆ closure and Z ⊈ closure then
6.            closure ← closure ∪ Z
7.            changed ← true
8.        end if
9.    end for
10. until not changed
11. return closure
```

> 💡 Runs in $ O(|F| \cdot |R|) $ — efficient and exact.

---

## 🧪 Example: Compute Closures

Let $ R(A, B, C, D, E) $,  
$ F = \{ A \rightarrow B,\; B \rightarrow D,\; CD \rightarrow E \} $

### 🔹 Compute $ A^+ $:
| Step | `closure` | Reason |
|------|-----------|--------|
| 0 | `{A}` | start |
| 1 | `{A, B}` | `A → B` |
| 2 | `{A, B, D}` | `B → D` |
| 3 | `{A, B, D}` | no `CD`, so stop |

✅ $ A^+ = \{A, B, D\} $  
❌ Not a superkey (missing `C`, `E`)

---

### 🔹 Compute $ AC^+ $:
| Step | `closure` | Reason |
|------|-----------|--------|
| 0 | `{A, C}` | start |
| 1 | `{A, B, C}` | `A → B` |
| 2 | `{A, B, C, D}` | `B → D` |
| 3 | `{A, B, C, D, E}` | `CD → E` |

✅ $ AC^+ = \{A, B, C, D, E\} = R $  
→ $ AC $ is a **superkey**

Now check minimality…

---

## 🔑 What is a Candidate Key?

A **candidate key** is a **minimal superkey** — i.e., a set $ K \subseteq R $ such that:
1. $ K^+ = R $ (**superkey**)  
2. No proper subset of $ K $ has closure $ R $ (**minimal**)

> 🎯 Primary Key = one chosen candidate key.

---

## 🧩 How to Find *All* Candidate Keys

### ✅ Step-by-Step Strategy

| Step | Action |
|------|--------|
| **1️⃣** | **Identify attributes not on RHS** of *any* FD → must be in *every* key  
| **2️⃣** | **Compute closure of mandatory set** → if = $ R $, it’s the *only* key  
| **3️⃣** | Otherwise, **add attributes** (from RHS-only set) systematically  
| **4️⃣** | For each superkey found, **test minimality** by removing one attribute at a time |

> 🚀 *Optimization*: Only consider combinations involving attributes that appear on LHS.

---

### 🧪 Full Example: Find All Candidate Keys

Let $ R(A, B, C, D) $,  
$ F = \{ A \rightarrow B,\; B \rightarrow C,\; C \rightarrow D \} $

#### 🔹 Step 1: Find *must-have* attributes
- RHS attributes: $ B, C, D $  
- **Only `A` is *not* on RHS** → `A` must be in every key.

#### 🔹 Step 2: Compute $ A^+ $:
- `{A} → B → C → D` → $ A^+ = \{A,B,C,D\} = R $  
✅ `A` is a superkey — and minimal (single attribute)  
→ **Only candidate key: `{A}`**

---

### 🧪 Another Example: Multiple Keys

Let $ R(A, B, C) $,  
$ F = \{ A \rightarrow B,\; B \rightarrow A,\; AB \rightarrow C \} $

#### 🔹 Must-have attributes?  
- RHS: $ A, B, C $ → *no* attribute is RHS-exclusive  
→ Try combinations.

#### 🔹 Compute closures:
| Set | $ X^+ $ | Superkey? | Minimal? |
|-----|---------|-----------|----------|
| `{A}` | `{A, B}` | ❌ | — |
| `{B}` | `{A, B}` | ❌ | — |
| `{C}` | `{C}` | ❌ | — |
| `{A, C}` | `{A, B, C}` | ✅ | ✅ (remove `C` → `{A}⁺ = {A,B}` ≠ R) |
| `{B, C}` | `{A, B, C}` | ✅ | ✅ |
| `{A, B}` | `{A, B, C}` | ✅ | ❌ (both `A,C` and `B,C` are smaller) |

✅ **Candidate keys**: `{A, C}`, `{B, C}`

> 💡 *Note*: `AB → C` is redundant (implied by `A→B`, `B→A`, `A→B→?→C`? — but here we assume it’s given).

---

## 🧠 Pro Tips for Efficient Key Finding

| Tip | Why It Works |
|-----|--------------|
| **🌟 Start with LHS-only attributes** | They *must* be in every key |
| **🔍 Use minimal cover first** | Removes redundant FDs → faster closure |
| **🚫 Skip supersets of known keys** | If `{A}` is a key, `{A,B}` can’t be minimal |
| **🎯 For large schemas**, use heuristic:  
  1. Find `X = {attributes not in RHS}`  
  2. If $ X^+ \ne R $, add one RHS attribute at a time  
  3. Stop when closure = $ R $ |

---

## 📌 Obsidian Note Template

```markdown
## Schema: `R(A,B,C,D,E)`
%% FDs: 
A → B
B → D
CD → E
%%

### Must-Have Attributes
- RHS: B, D, E → **LHS-only: A, C**

### Closures
> [!example] $ A^+ $
- `{A} → B → D`  
- $ A^+ = \{A, B, D\} $ ❌

> [!example] $ AC^+ $
- `{A,C} → B,D → E`  
- $ AC^+ = R $ ✅  
- Test minimality:  
  - $ A^+ = \{A,B,D\} $ ❌  
  - $ C^+ = \{C\} $ ❌  
→ **Candidate Key: `{A, C}`**
```

---