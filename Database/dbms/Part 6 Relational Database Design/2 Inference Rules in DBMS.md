# 🧮 **DBMS – Inference Rules for Functional Dependencies**  
### *Armstrong’s Axioms: The Logic Engine of Database Design*

> 🔑 *Functional Dependencies (FDs) describe data rules. **Armstrong’s Axioms** let us derive new rules from old ones — like a proof system for databases.*

Let’s master the **3 core axioms** + **5 derived rules** — with **Obsidian-friendly math**, real examples, and closure-based reasoning.
## 📜 What Are Armstrong’s Axioms?

> ✅ A sound & complete set of inference rules for FDs.  
> - **Sound**: Only valid FDs are derived.  
> - **Complete**: All valid FDs can be derived from given ones.

Let $ R $ be a relation schema, and $ X, Y, Z \subseteq R $.  
We write $ F \models X \rightarrow Y $ if $ X \rightarrow Y $ *logically follows* from FD set $ F $.

---

## 🔷 The 3 Core Axioms

### 1️⃣ **Reflexivity**  
> If $ Y \subseteq X $, then $ X \rightarrow Y $.

#### 🔤 Formal:
$$
Y \subseteq X \;\Rightarrow\; X \rightarrow Y
$$

#### 🧪 Example:  
`Student(RollNo, Name, Dept)`  
→ $ \{ \text{RollNo, Name} \} \rightarrow \text{Name} $  
✅ Trivial FD — always true.

> 💡 Used to justify projections and decompositions.

---

### 2️⃣ **Augmentation**  
> If $ X \rightarrow Y $, then $ XZ \rightarrow YZ $.

#### 🔤 Formal:
$$
X \rightarrow Y \;\Rightarrow\; XZ \rightarrow YZ
$$

#### 🧪 Example:  
Given: $ \text{RollNo} \rightarrow \text{Name} $  
→ Augment with `Dept`:  
$$
\text{RollNo, Dept} \rightarrow \text{Name, Dept}
$$

> 💡 Adds “context” without breaking dependency.

---

### 3️⃣ **Transitivity**  
> If $ X \rightarrow Y $ and $ Y \rightarrow Z $, then $ X \rightarrow Z $.

#### 🔤 Formal:
$$
X \rightarrow Y \;\land\; Y \rightarrow Z \;\Rightarrow\; X \rightarrow Z
$$

#### 🧪 Example:  
Given:  
- $ \text{RollNo} \rightarrow \text{Dept} $  
- $ \text{Dept} \rightarrow \text{Building} $  
→ Then:  
$$
\text{RollNo} \rightarrow \text{Building}
$$

> 🔄 *This is how we chain dependencies — the heart of normalization.*

---

## ➕ 5 Derived Rules (Provable from Axioms)

| Rule | Statement | Derivation Sketch |
|------|-----------|-------------------|
| **Union** | $ X \rightarrow Y \;\land\; X \rightarrow Z \;\Rightarrow\; X \rightarrow YZ $ | Augmentation + Transitivity |
| **Decomposition** | $ X \rightarrow YZ \;\Rightarrow\; X \rightarrow Y \;\land\; X \rightarrow Z $ | Reflexivity + Transitivity |
| **Composition** | $ X \rightarrow Y \;\land\; Z \rightarrow W \;\Rightarrow\; XZ \rightarrow YW $ | Augmentation ×2 + Union |
| **Pseudo-Transitivity** | $ X \rightarrow Y \;\land\; YZ \rightarrow W \;\Rightarrow\; XZ \rightarrow W $ | Augmentation + Transitivity |
| **Self-Determination** | $ X \rightarrow X $ | Reflexivity ($ X \subseteq X $) |

Let’s prove a few and apply them.

---

### 🔹 **Union Rule**  
> If $ X \rightarrow Y $ and $ X \rightarrow Z $, then $ X \rightarrow YZ $.

#### 🔤 Proof:
1. $ X \rightarrow Y $ (given)  
2. $ X \rightarrow Z $ (given)  
3. $ X \rightarrow XZ $ (Augmentation on 2: $ X \rightarrow Z \Rightarrow XX \rightarrow XZ $, i.e., $ X \rightarrow XZ $)  
4. $ XZ \rightarrow YZ $ (Augmentation on 1: $ X \rightarrow Y \Rightarrow XZ \rightarrow YZ $)  
5. $ X \rightarrow YZ $ (Transitivity: 3 + 4)

#### 🧪 Example:  
$ \text{RollNo} \rightarrow \text{Name} $, $ \text{RollNo} \rightarrow \text{Dept} $  
→ $ \text{RollNo} \rightarrow \text{Name, Dept} $

---

### 🔹 **Decomposition Rule**  
> If $ X \rightarrow YZ $, then $ X \rightarrow Y $ and $ X \rightarrow Z $.

#### 🔤 Proof:
1. $ X \rightarrow YZ $ (given)  
2. $ YZ \rightarrow Y $ (Reflexivity)  
3. $ X \rightarrow Y $ (Transitivity: 1 + 2)  
*(Similarly for $ Z $)*

#### 🧪 Example:  
$ \text{RollNo} \rightarrow \text{Name, Dept} $  
→ $ \text{RollNo} \rightarrow \text{Name} $  
→ $ \text{RollNo} \rightarrow \text{Dept} $

> 💡 Used in **3NF/BCNF** to isolate dependencies.

---

### 🔹 **Pseudo-Transitivity**  
> If $ X \rightarrow Y $ and $ YZ \rightarrow W $, then $ XZ \rightarrow W $.

#### 🔤 Proof:
1. $ X \rightarrow Y $ (given)  
2. $ XZ \rightarrow YZ $ (Augmentation on 1)  
3. $ YZ \rightarrow W $ (given)  
4. $ XZ \rightarrow W $ (Transitivity: 2 + 3)

#### 🧪 Example:  
- $ \text{RollNo} \rightarrow \text{Class} $  
- $ \text{Class, Teacher} \rightarrow \text{Subject} $  
→ $ \text{RollNo, Teacher} \rightarrow \text{Subject} $

> 🎯 Critical for multi-attribute determinants.

---

## 🧠 Why These Rules Matter

| Use Case | Rule Applied |
|---------|--------------|
| **FD Simplification** | Union → merge $ X \rightarrow A $, $ X \rightarrow B $ → $ X \rightarrow AB $ |
| **FD Verification** | Decomposition → check if $ X \rightarrow AB $ implies $ X \rightarrow A $ |
| **Closure Computation** | Transitivity + Augmentation → compute $ X^+ $ |
| **Normalization** | All rules → derive minimal cover, find candidate keys |

---

## 🔍 Attribute Closure — Putting It All Together

The **attribute closure** $ X^+ $ is the set of all attributes functionally determined by $ X $, using Armstrong’s rules.

### 📌 Algorithm to Compute $ X^+ $:
1. Initialize: $ \text{closure} = X $  
2. Repeat until no change:  
   For each FD $ U \rightarrow V $ in $ F $:  
   &nbsp;&nbsp;If $ U \subseteq \text{closure} $, then $ \text{closure} = \text{closure} \cup V $  
3. Return `closure`

#### 🧪 Example:
Let $ F = \{ \text{RollNo} \rightarrow \text{Name},\; \text{Dept} \rightarrow \text{Building},\; \text{RollNo} \rightarrow \text{Dept} \} $  
Find $ \{ \text{RollNo} \}^+ $:

| Step | Closure | Reason |
|------|---------|--------|
| 0 | `{RollNo}` | start |
| 1 | `{RollNo, Name, Dept}` | `RollNo → Name`, `RollNo → Dept` |
| 2 | `{RollNo, Name, Dept, Building}` | `Dept → Building` |

✅ $ \{ \text{RollNo} \}^+ = \text{All Attributes} $ → `RollNo` is a **candidate key**.

---

## 🧩 Closure-Based Inference

> 🔁 To check if $ F \models X \rightarrow Y $:  
> ✅ Compute $ X^+ $ using $ F $  
> ✅ If $ Y \subseteq X^+ $, then $ X \rightarrow Y $ is implied.

#### 📌 Example:  
Does $ F \models \text{RollNo} \rightarrow \text{Building} $?  
→ $ \{ \text{RollNo} \}^+ = \{ \text{RollNo, Name, Dept, Building} \} $  
→ `Building` ∈ closure → ✅ Yes.

---

## 🧠 Key Takeaways (Obsidian Note)

- ✅ **Reflexivity**: $ Y \subseteq X \Rightarrow X \rightarrow Y $ (trivial)  
- ✅ **Augmentation**: Add same attrs to both sides  
- ✅ **Transitivity**: Chain dependencies  
- ✅ **Union/Decomposition**: Merge/split RHS  
- ✅ **Pseudo-Transitivity**: Handle multi-attribute determinants  
- 🔑 **Closure $ X^+ $** = All attrs derivable from $ X $ — use it to:  
  - Verify FDs  
  - Find candidate keys  
  - Compute minimal cover  

> 💬 *“Armstrong’s Axioms don’t just validate FDs — they generate them.”*