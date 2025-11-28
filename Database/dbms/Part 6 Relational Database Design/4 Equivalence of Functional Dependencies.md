# ⚖️ **DBMS – Equivalence of Functional Dependencies**  
### *When Two FD Sets Say the Exact Same Thing*

> 🔁 Two sets of functional dependencies $ F $ and $ G $ are **equivalent** iff they *logically imply the same dependencies* — i.e., they have the **same closure**:  
> $$
> F^+ = G^+
> $$

This is critical for:  
✅ Schema refinement  
✅ Normalization validation  
✅ Avoiding redundant constraints  

Let’s master how to **prove equivalence** — using **attribute closures**.

## 📜 Formal Definition

Let $ F $ and $ G $ be sets of FDs over relation schema $ R $.  
Then:
$$
F \equiv G \;\;\text{iff}\;\; F \models G \;\land\; G \models F
$$
where $ F \models G $ means *every FD in $ G $ is implied by $ F $*.

> 🆚 *Subset check*:  
> - $ F \subseteq G $: every FD in $ F $ appears *verbatim* in $ G $  
> - $ F \models G $: every FD in $ G $ is *derivable* from $ F $ (via Armstrong’s axioms)

→ **Equivalence requires $ F \models G $ and $ G \models F $**.

---

## 🔍 How to Check Equivalence: The Closure Method

To verify $ F \equiv G $:

| Step | Action |
|------|--------|
| **1️⃣** | Show $ F \models G $:  
For each $ X \rightarrow Y \in G $, check $ Y \subseteq X^+_F $ |
| **2️⃣** | Show $ G \models F $:  
For each $ X \rightarrow Y \in F $, check $ Y \subseteq X^+_G $ |

> ✅ If both hold → $ F \equiv G $

> 💡 *Pro Tip*: Always minimize $ F $ and $ G $ first (→ minimal cover) to reduce work.

---

## 🧪 Worked Example: Are $ F $ and $ G $ Equivalent?

### Given:
- Schema: $ R(A, C, D, E, H) $
- $ F = \{ A \rightarrow C,\; AC \rightarrow D,\; E \rightarrow AH \} $
- $ G = \{ A \rightarrow CD,\; E \rightarrow AH \} $

---

### ✅ Step 1: Check $ F \models G $

We must verify every FD in $ G $ is implied by $ F $.

#### 🔹 $ A \rightarrow CD \in G $?  
Compute $ A^+_F $:
- Start: $ \{A\} $  
- $ A \rightarrow C $ → $ \{A, C\} $  
- $ AC \rightarrow D $ → $ \{A, C, D\} $  
→ $ A^+_F = \{A, C, D\} $  
✅ $ CD \subseteq A^+_F $ → $ F \models A \rightarrow CD $

#### 🔹 $ E \rightarrow AH \in G $?  
Compute $ E^+_F $:
- Start: $ \{E\} $  
- $ E \rightarrow AH $ → $ \{E, A, H\} $  
- $ A \rightarrow C $ → $ \{E, A, H, C\} $  
- $ AC \rightarrow D $ → $ \{E, A, H, C, D\} $  
→ $ E^+_F = \{A, C, D, E, H\} $  
✅ $ AH \subseteq E^+_F $ → $ F \models E \rightarrow AH $

✅ So $ F \models G $

---

### ✅ Step 2: Check $ G \models F $

Verify every FD in $ F $ is implied by $ G $.

#### 🔹 $ A \rightarrow C \in F $?  
Compute $ A^+_G $:
- Start: $ \{A\} $  
- $ A \rightarrow CD $ → $ \{A, C, D\} $  
→ $ A^+_G = \{A, C, D\} $  
✅ $ C \subseteq A^+_G $ → $ G \models A \rightarrow C $

#### 🔹 $ AC \rightarrow D \in F $?  
Compute $ (AC)^+_G $:
- Start: $ \{A, C\} $  
- $ A \rightarrow CD $ → $ \{A, C, D\} $  
→ $ (AC)^+_G = \{A, C, D\} $  
✅ $ D \subseteq (AC)^+_G $ → $ G \models AC \rightarrow D $

#### 🔹 $ E \rightarrow AH \in F $?  
Compute $ E^+_G $:
- Start: $ \{E\} $  
- $ E \rightarrow AH $ → $ \{E, A, H\} $  
- $ A \rightarrow CD $ → $ \{E, A, H, C, D\} $  
→ $ E^+_G = \{A, C, D, E, H\} $  
✅ $ AH \subseteq E^+_G $ → $ G \models E \rightarrow AH $

✅ So $ G \models F $

---

### ✅ Conclusion:
$$
F \models G \;\land\; G \models F \;\Rightarrow\; F \equiv G
$$

> 🎯 Even though $ F $ has 3 FDs and $ G $ has 2, they are **logically identical** — $ G $ is just a *more compact* representation.

---

## 🧠 Why This Matters in Practice

| Scenario | Use of Equivalence |
|---------|---------------------|
| **Schema Refinement** | Replace complex $ F $ with simpler $ G $ (e.g., before 3NF synthesis) |
| **Constraint Migration** | Ensure new DB enforces same rules as old DB |
| **Tool Verification** | Check if auto-generated FDs match designer’s intent |
| **Normalization Proof** | Show decomposition preserves dependencies |

> ⚠️ *Never assume equivalence by inspection!*  
> Example: $ \{A \rightarrow B,\; B \rightarrow C\} \not\equiv \{A \rightarrow C\} $  
> — the first implies $ A \rightarrow B $, the second does not.

---

## 📌 Pro Tips for Obsidian Notes

- Use `$$ F \equiv G $$` for equivalence  
- Store closure steps in collapsible callouts:
  ````markdown
  > [!note] $ A^+_F $
  > - Start: `{A}`  
  > - `A → C` ⇒ `{A, C}`  
  > - `AC → D` ⇒ `{A, C, D}`  
  > → $ A^+_F = \{A, C, D\} $
  ````
- Link to foundational notes:  
  `[[Armstrong's Axioms]]`, `[[Attribute Closure]]`, `[[Minimal Cover]]`

---