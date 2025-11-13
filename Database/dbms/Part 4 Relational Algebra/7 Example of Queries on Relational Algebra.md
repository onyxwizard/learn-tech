
# 🧮 **Examples of Queries in Relational Algebra**

> ✅ *Relational Algebra turns English questions into precise, executable logic.*  
> Let’s solve real-world queries — using only `σ` (select), `∏` (project), `⋈` (join), `∪`, `−`, `÷`, and renames.

We’ll use the **COMPANY database**:

- **`EMPLOYEE(Fname, Minit, Lname, Ssn, Bdate, Address, Sex, Salary, Super_ssn, Dno)`**
- **`DEPARTMENT(Dname, Dnumber, Mgr_ssn, Mgr_start_date)`**
- **`PROJECT(Pname, Pnumber, Plocation, Dnum)`**
- **`WORKS_ON(Essn, Pno, Hours)`**
- **`DEPENDENT(Essn, Dependent_name, Sex, Bdate, Relationship)`**

---

## 1️⃣ Retrieve Name & Address of Employees in *Research* Dept

> 🎯 *“Who works in Research? Show me their names & addresses.”*

### 🔤 Relational Algebra:
$$
\pi_{\text{Fname, Lname, Address}}\left(
  \sigma_{\text{Dname} = \text{'Research'}}(\text{DEPARTMENT}) \;\bowtie_{\text{Dnumber} = \text{Dno}}\; \text{EMPLOYEE}
\right)
$$

### 🧾 Steps:
1. `σ_{Dname='Research'}(DEPARTMENT)` → gets `Dnumber=5`
2. Join with `EMPLOYEE` on `Dno = Dnumber`
3. Project only `Fname`, `Lname`, `Address`

### ✅ Result:
| Fname     | Lname   | Address                          |
|-----------|---------|----------------------------------|
| John      | Smith   | 731 Fondren, Houston, TX         |
| Franklin  | Wong    | 638 Voss, Houston, TX            |
| Ramesh    | Narayan | 975 Fire Oak, Humble, TX         |
| Joyce     | English | 5631 Rice, Houston, TX           |

---

## 2️⃣ Project Details for *Stafford* — with Manager Info

> 🎯 *“List all projects in Stafford. For each, show: `Pnumber`, `Dnum`, manager’s `Lname`, `Address`, `Bdate`.”*

### 🔤 Relational Algebra:
$$
\begin{align*}
&\text{STAFFORD\_PROJS} \leftarrow \sigma_{\text{Plocation} = \text{'Stafford'}}(\text{PROJECT}) \\
&\text{PROJ\_DEPT} \leftarrow \text{STAFFORD\_PROJS} \;\bowtie_{\text{Dnum} = \text{Dnumber}}\; \text{DEPARTMENT} \\
&\text{PROJ\_MGR} \leftarrow \text{PROJ\_DEPT} \;\bowtie_{\text{Mgr\_ssn} = \text{Ssn}}\; \text{EMPLOYEE} \\
&\text{RESULT} \leftarrow \pi_{\text{Pnumber, Dnum, Lname, Address, Bdate}}(\text{PROJ\_MGR})
\end{align*}
$$

> 💡 *We rename intermediate results for clarity (use `ρ` if needed).*

### ✅ Result:
| Pnumber | Dnum | Lname   | Address                      | Bdate       |
|---------|------|---------|------------------------------|-------------|
| 10      | 4    | Wallace | 291 Berry, Bellaire, TX      | 1941-06-20  |
| 30      | 4    | Wallace | 291 Berry, Bellaire, TX      | 1941-06-20  |

---

## 3️⃣ Employees Working on **All** Projects Controlled by Dept 5

> 🔥 *The classic “for all” query — requires **division**.*

### 🔤 Relational Algebra:
$$
\begin{align*}
&\text{DEPT5\_PROJS} \leftarrow \pi_{\text{Pnumber}}(\sigma_{\text{Dnum}=5}(\text{PROJECT})) \\
&\text{EMP\_PROJ} \leftarrow \pi_{\text{Essn, Pno}}(\text{WORKS\_ON}) \\
&\text{SSNs} \leftarrow \text{EMP\_PROJ} \;\div\; \text{DEPT5\_PROJS} \\
&\text{RESULT} \leftarrow \pi_{\text{Fname, Lname}}(\text{SSNs} \;\bowtie_{\text{Ssn} = \text{Essn}}\; \text{EMPLOYEE})
\end{align*}
$$

### 🧠 Why it works:
- `DEPT5_PROJS = {1, 2, 3}`
- `EMP_PROJ` has pairs like `(123..., 1)`, `(123..., 2)` → but **only John (123...) and Joyce (453...) have all 3**
- Division returns `{123456789, 453453453}`
- Join → names: **John Smith**, **Joyce English**

> ✅ *Matches our earlier division example!*

---

## 4️⃣ Projects Involving **Smith** (as Worker **or** Manager)

> 🎯 *Union of two paths.*

### 🔤 Relational Algebra:
$$
\begin{align*}
&\text{SMITH\_SSN} \leftarrow \pi_{\text{Ssn}}(\sigma_{\text{Lname} = \text{'Smith'}}(\text{EMPLOYEE})) \\
&\text{SMITH\_WORKER} \leftarrow \pi_{\text{Pno}}(\text{WORKS\_ON} \;\bowtie_{\text{Essn} = \text{Ssn}}\; \text{SMITH\_SSN}) \\
&\text{SMITH\_MGR\_DEPTS} \leftarrow \pi_{\text{Dnumber}}(\sigma_{\text{Lname} = \text{'Smith'}}(\text{EMPLOYEE} \;\bowtie_{\text{Ssn} = \text{Mgr\_ssn}}\; \text{DEPARTMENT})) \\
&\text{SMITH\_MGR\_PROJS} \leftarrow \pi_{\text{Pnumber}}(\text{PROJECT} \;\bowtie_{\text{Dnum} = \text{Dnumber}}\; \text{SMITH\_MGR\_DEPTS}) \\
&\text{RESULT} \leftarrow \text{SMITH\_WORKER} \;\cup\; \text{SMITH\_MGR\_PROJS}
\end{align*}
$$

### 🧾 Breakdown:
- Smith (Ssn=`123...`) works on `Pno = {1, 2}`
- No dept is managed by a Smith (Mgr: Wong, Wallace, Borg) → `SMITH_MGR_PROJS = ∅`
- Final result: **{1, 2}**

> ✅ Projects: **ProductX (1)**, **ProductY (2)**

---

## 5️⃣ Employees with **≥2 Dependents**

> ⚠️ *Basic relational algebra doesn’t support aggregation — but we can simulate count with self-join or use extended operators like `ℑ` (group-by).*

### 🔤 Using Extended RA (`ℑ` = group & aggregate):
$$
\begin{align*}
&\text{T1} \leftarrow \gamma_{\text{Essn} \rightarrow \text{Ssn},\; \text{COUNT}(\text{Dependent\_name}) \rightarrow \text{DepCount}}(\text{DEPENDENT}) \\
&\text{T2} \leftarrow \sigma_{\text{DepCount} \geq 2}(\text{T1}) \\
&\text{RESULT} \leftarrow \pi_{\text{Fname, Lname}}(\text{T2} \;\bowtie\; \text{EMPLOYEE})
\end{align*}
$$

### ✅ Result:
| Fname | Lname   |
|-------|---------|
| John  | Smith   | ← 3 dependents  
| Franklin | Wong | ← 3 dependents  

> 📌 *In pure RA (no `γ`), you’d use a self-join on `DEPENDENT` to find pairs of different dependents per employee.*

---

## 6️⃣ Employees **Without** Dependents

> ✅ *Classic set difference.*

### 🔤 Relational Algebra:
$$
\begin{align*}
&\text{ALL\_EMPS} \leftarrow \pi_{\text{Ssn}}(\text{EMPLOYEE}) \\
&\text{EMPS\_WITH\_DEPS} \leftarrow \pi_{\text{Essn}}(\text{DEPENDENT}) \\
&\text{EMPS\_NO\_DEPS} \leftarrow \text{ALL\_EMPS} - \text{EMPS\_WITH\_DEPS} \\
&\text{RESULT} \leftarrow \pi_{\text{Fname, Lname}}(\text{EMPS\_NO\_DEPS} \;\bowtie\; \text{EMPLOYEE})
\end{align*}
$$

### ✅ Result:
| Fname   | Lname   |
|---------|---------|
| Alicia  | Zelaya  |
| Ramesh  | Narayan |
| Joyce   | English |
| Ahmad   | Jabbar  |
| James   | Borg    |

> ❗ Aliases like `ρ_{Ssn}(EMPS_NO_DEPS)` help if attribute names mismatch after `−`.

---

## 7️⃣ **Managers with Dependents**

> ✅ *Intersection of two sets.*

### 🔤 Relational Algebra:
$$
\begin{align*}
&\text{MANAGERS} \leftarrow \pi_{\text{Mgr\_ssn}}(\text{DEPARTMENT}) \\
&\text{PARENTS} \leftarrow \pi_{\text{Essn}}(\text{DEPENDENT}) \\
&\text{MGR\_PARENTS} \leftarrow \text{MANAGERS} \;\cap\; \text{PARENTS} \\
&\text{RESULT} \leftarrow \pi_{\text{Fname, Lname}}(\text{MGR\_PARENTS} \;\bowtie\; \text{EMPLOYEE})
\end{align*}
$$

### ✅ Result:
| Fname     | Lname   |
|-----------|---------|
| Franklin  | Wong    | ← Mgr of Research + 3 dependents  
| Jennifer  | Wallace | ← Mgr of Admin + 1 dependent  

---

## 🧠 Key Takeaways

| Query Type | Operator(s) Used | Obsidian Tip |
|------------|------------------|--------------|
| Simple filter | `σ`, `∏` | Use `$ \sigma_{...} $` |
| Joins | `⋈` or `× + σ` | `$ R \bowtie_{A=B} S $` |
| “All X” | `÷` | `$ R \div S $` → rare but powerful |
| “Either/Or” | `∪` | `$ A \cup B $` |
| “Not in” | `−` | `$ A - B $` |
| “Both in” | `∩` | `$ A \cap B $` |

> 🔄 *Real queries combine these — like LEGO blocks.*


📌 **Obsidian Pro Tips**:
- Wrap **all math** in `$$...$$` (block) or `$...$` (inline)
- Use `\text{}` for multi-letter identifiers: `$ \pi_{\text{Fname, Lname}} $`
- Define shorthands in notes:  
  ```
  Let $ E = \text{EMPLOYEE} $, $ D = \text{DEPARTMENT} $, etc.
  ```
- Use `%% comments %%` for step explanations

