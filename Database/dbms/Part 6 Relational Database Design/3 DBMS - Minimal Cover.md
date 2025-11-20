# 🔍 **DBMS – Minimal Cover (Canonical Cover)**  
### *The Leanest Set of FDs That Says Everything*

> ✅ A **minimal cover** (or *canonical cover*) is the *smallest equivalent set* of functional dependencies (FDs) — no redundancies, no extra attributes.

It’s the **optimized blueprint** for normalization — used to find candidate keys, decompose tables, and eliminate anomalies.

Let’s break it down — with **Obsidian-friendly math** and a full worked example.

## 📜 What is a Minimal Cover?

A set $ F_c $ is a **minimal cover** of $ F $ iff:
1. **Single-Attribute RHS**:  
   Every FD is of the form $ X \rightarrow A $, where $ A $ is a single attribute.  
2. **No Redundant FDs**:  
   No FD in $ F_c $ can be derived from the others.  
3. **Minimal LHS**:  
   For each $ X \rightarrow A $, no proper subset of $ X $ determines $ A $.

> 🎯 Goal: $ F_c \equiv F $ (same closure), but *smaller* and *cleaner*.

---

## 🧩 3-Step Algorithm to Compute Minimal Cover

Given a set $ F $ of FDs:

| Step | Action | Rule Used |
|------|--------|-----------|
| **1️⃣** | **Decompose RHS** → Ensure all FDs have single-attribute RHS | Decomposition Rule |
| **2️⃣** | **Remove redundant FDs** → For each FD, test if it’s implied by others | Attribute Closure |
| **3️⃣** | **Minimize LHS** → For FDs like $ XY \rightarrow A $, test if $ X \rightarrow A $ or $ Y \rightarrow A $ | Closure again |

Let’s apply this to a real example.

---

## 🧪 Example: Compute Minimal Cover

### Given FD Set $ F $:
$$
\begin{align*}
&1.\; A \rightarrow B \\
&2.\; C \rightarrow B \\
&3.\; D \rightarrow A, B, C \\
&4.\; AC \rightarrow D
\end{align*}
$$

---

### ✅ Step 1: Decompose RHS (→ single-attribute)

Use decomposition: $ X \rightarrow YZ \Rightarrow X \rightarrow Y,\; X \rightarrow Z $

So:
- $ D \rightarrow A, B, C $ → $ D \rightarrow A $, $ D \rightarrow B $, $ D \rightarrow C $

**New set $ F_1 $**:
$$
\begin{align*}
&1.\; A \rightarrow B \\
&2.\; C \rightarrow B \\
&3.\; D \rightarrow A \\
&4.\; D \rightarrow B \\
&5.\; D \rightarrow C \\
&6.\; AC \rightarrow D
\end{align*}
$$

> ✅ All RHS are single attributes.

---

### ✅ Step 2: Remove Redundant FDs

For each FD $ f \in F_1 $, check:  
**Is $ F_1 - \{f\} \models f $?**  
→ Compute closure of LHS using *other* FDs. If RHS ∈ closure, $ f $ is redundant.

#### 🔹 Test $ A \rightarrow B $:
- Remove it → $ F' = F_1 - \{A \rightarrow B\} $
- Compute $ A^+ $ under $ F' $:  
  Start: `{A}`  
  → No FD with LHS ⊆ `{A}` (since `A→B` removed)  
  → $ A^+ = \{A\} $  
  ❌ `B` ∉ $ A^+ $ → **not redundant** → keep.

#### 🔹 Test $ C \rightarrow B $:
- $ C^+ = \{C\} $ (no other way to get `B`)  
  ❌ `B` ∉ $ C^+ $ → **keep**.

#### 🔹 Test $ D \rightarrow A $:
- $ D^+ $ without `D→A`:  
  Start: `{D}`  
  → `D→B`, `D→C` → `{D, B, C}`  
  → No `A` (need `D→A` or chain like `D→C→?→A` — but `C→B` only)  
  ❌ `A` ∉ closure → **keep**.

#### 🔹 Test $ D \rightarrow B $:
- Compute $ D^+ $ without `D→B`:  
  Start: `{D}`  
  → `D→A` → `{D, A}`  
  → `A→B` → `{D, A, B}` ✅  
  → `D→C` → `{D, A, B, C}`  
  ✅ `B` ∈ $ D^+ $ → `D→B` is **redundant** → **remove**.

#### 🔹 Test $ D \rightarrow C $:
- $ D^+ $ without `D→C`:  
  `{D} → A → B`, but no path to `C`  
  ❌ `C` ∉ closure → **keep**.

#### 🔹 Test $ AC \rightarrow D $:
- $ (AC)^+ $ without `AC→D`:  
  `{A,C} → A→B, C→B` → `{A, B, C}`  
  ❌ `D` ∉ closure → **keep**.

**After Step 2 → $ F_2 $**:
$$
\begin{align*}
&1.\; A \rightarrow B \\
&2.\; C \rightarrow B \\
&3.\; D \rightarrow A \\
&4.\; D \rightarrow C \\
&5.\; AC \rightarrow D
\end{align*}
$$

---

### ✅ Step 3: Minimize LHS

Only FD with multi-attribute LHS: $ AC \rightarrow D $

Check if we can remove `A` or `C`:

#### 🔸 Can we remove `A`? → Is $ C \rightarrow D $?  
- Compute $ C^+ $: `{C} → B` → `{C, B}`  
  ❌ `D` ∉ $ C^+ $ → **cannot remove A**

#### 🔸 Can we remove `C`? → Is $ A \rightarrow D $?  
- $ A^+ = \{A, B\} $  
  ❌ `D` ∉ $ A^+ $ → **cannot remove C**

✅ So $ AC \rightarrow D $ stays.

All other FDs have singleton LHS → done.

---

## ✅ Final Minimal Cover $ F_c $:
$$
\boxed{
\begin{aligned}
& A \rightarrow B \\
& C \rightarrow B \\
& D \rightarrow A \\
& D \rightarrow C \\
& AC \rightarrow D
\end{aligned}
}
$$

> 🔁 Verify: $ (F_c)^+ = F^+ $ — same expressive power, fewer FDs.

---

## 🧠 Why Minimal Cover Matters

| Benefit | Explanation |
|--------|-------------|
| 🧹 **Eliminates Redundancy** | No extra FDs → smaller schema design |
| 🧩 **Simplifies Normalization** | Clearer dependencies → easier 3NF/BCNF decomposition |
| 🔍 **Reveals True Determinants** | Minimal LHS → exposes *exact* keys |
| ⚙️ **Optimizes Dependency Checking** | Faster closure computation |

> 💡 Used in algorithms like **Synthesis (3NF)** and **BCNF Decomposition**.

---

## 📌 Pro Tips for Obsidian Notes

- Store FD sets as code blocks:
  ```fd
  A → B
  C → B
  D → A, C
  AC → D
  ```
- Use `%%` for closure steps:
  ```
  %% Step: Test D→B
  F' = {A→B, C→B, D→A, D→C, AC→D}
  D⁺ = {D} 
       → D→A ⇒ {D,A} 
       → A→B ⇒ {D,A,B} 
       → D→C ⇒ {D,A,B,C}
  B ∈ D⁺ ⇒ redundant
  %%
  ```
- Link to `[[Armstrong's Axioms]]` and `[[Attribute Closure]]`