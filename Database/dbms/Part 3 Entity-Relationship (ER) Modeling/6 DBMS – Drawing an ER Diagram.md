# 🎨 **DBMS – Drawing an ER Diagram**  
### *From Business Rules to Visual Blueprint*

> 🗺️ *An ER diagram is the **visual contract** between stakeholders and developers — translating business rules into database structure.*

Let’s walk through designing a full **COMPANY database ERD** — from entities to relationships, constraints to notation — so you can draw any ERD confidently.

## 🧱 Step 1: Identify Core Entities

> ✅ **Entities = Things that matter** — nouns in your business domain.

In the **COMPANY database**, we have:

| Entity | Description | Key Attributes |
|--------|-------------|----------------|
| `EMPLOYEE` | Staff members | `Ssn`, `Name`, `Salary`, `Bdate`, `Address`, `Sex` |
| `DEPARTMENT` | Organizational units | `Dnumber`, `Dname`, `Mgr_ssn`, `Mgr_start_date` |
| `PROJECT` | Tasks managed by departments | `Pnumber`, `Pname`, `Plocation`, `Dnum` |
| `DEPENDENT` | Family members of employees | `Essn`, `Dependent_name`, `Sex`, `Bdate`, `Relationship` |

> 📌 *Tip*: If it has attributes and participates in relationships → it’s an entity.

---

## 🏷️ Step 2: Define Attributes & Their Types

Each entity has **attributes** — properties that describe it.

```plaintext
          ┌─────────────┐
          │  EMPLOYEE   │
          │             │
    ┌─────┴─────┐       │
    │           │       │
┌─────────┐ ┌─────────┐ │
│ Fname   │ │ Minit   │ │
└─────────┘ └─────────┘ │
     ▲           ▲      │
     └───┬───────┘      │
         │              │
     ┌─────────┐        │
     │ Lname   │        │
     └─────────┘        │
          ▲             │
          └─────────────┘
```

> 🔍 **Attribute Types**:
- **Simple**: `Salary`, `Sex` → single value  
- **Composite**: `Name = {Fname, Minit, Lname}` → nested ovals  
- **Multi-valued**: `Locations` for `DEPARTMENT` → double oval  
- **Derived**: `Age = CURRENT_YEAR - Bdate` → dashed oval  
- **Weak Entity**: `DEPENDENT` → double rectangle

---

## ↔️ Step 3: Map Relationships & Cardinalities

> ✅ **Relationships = Verbs** — how entities interact.

Here are the key relationships in COMPANY:

| Relationship | Entities Involved | Cardinality | Participation | Notes |
|--------------|-------------------|-------------|---------------|-------|
| `WORKS_FOR` | `EMPLOYEE` — `DEPARTMENT` | N:1 | Total (Emp) / Partial (Dept) | Every employee must belong to a dept |
| `MANAGES` | `EMPLOYEE` — `DEPARTMENT` | 1:1 | Partial (Emp) / Total (Dept) | Dept must have manager; not all employees manage |
| `CONTROLS` | `DEPARTMENT` — `PROJECT` | 1:N | Total (Proj) / Partial (Dept) | Project must be controlled by one dept |
| `SUPERVISION` | `EMPLOYEE` — `EMPLOYEE` | 1:N | Partial (both) | Recursive relationship — supervisor/supervisee |
| `WORKS_ON` | `EMPLOYEE` — `PROJECT` | M:N | Partial (both) | With attribute `Hours` |
| `DEPENDENTS_OF` | `EMPLOYEE` — `DEPENDENT` | 1:N | Partial (Emp) / Total (Dep) | Dependent cannot exist without employee |

---

## 🖼️ Step 4: Draw the Full ER Diagram (Text-Based)

```plaintext
                            ┌─────────────┐
                            │  DEPARTMENT │
                            │ Dnumber     │
                            │ Dname       │
                            │ Locations   │ ← double oval (multi-valued)
                            └──────┬──────┘
                                   │
                  ┌────────────────▼────────────────┐
                  │            CONTROLS             │ ← diamond
                  └────────────────┬────────────────┘
                                   │
                            ┌─────────────┐
                            │   PROJECT   │
                            │ Pnumber     │
                            │ Pname       │
                            │ Plocation   │
                            └──────┬──────┘
                                   │
                  ┌────────────────▼────────────────┐
                  │            WORKS_ON             │ ← diamond + Hours
                  └────────────────┬────────────────┘
                                   │
                            ┌─────────────┐
                            │  EMPLOYEE   │
                            │ Ssn         │
                            │ Name        │ ← composite (Fname, Minit, Lname)
                            │ Salary      │
                            │ Address     │
                            │ Sex         │
                            └──────┬──────┘
                                   │
                  ┌────────────────▼────────────────┐
                  │           WORKS_FOR             │ ← diamond
                  └────────────────┬────────────────┘
                                   │
                            ┌─────────────┐
                            │  DEPARTMENT │
                            └─────────────┘
                                   │
                  ┌────────────────▼────────────────┐
                  │            MANAGES              │ ← diamond + StartDate
                  └────────────────┬────────────────┘
                                   │
                            ┌─────────────┐
                            │  EMPLOYEE   │
                            └──────┬──────┘
                                   │
                  ┌────────────────▼────────────────┐
                  │          SUPERVISION            │ ← recursive (Supervisor/Superisee)
                  └────────────────┬────────────────┘
                                   │
                            ┌─────────────┐
                            │  EMPLOYEE   │
                            └──────┬──────┘
                                   │
                  ┌────────────────▼────────────────┐
                  │         DEPENDENTS_OF           │ ← double diamond (identifying)
                  └────────────────┬────────────────┘
                                   │
                            ┌─────────────┐
                            │  DEPENDENT  │ ← double rectangle (weak)
                            │ Name        │
                            │ Sex         │
                            │ Bdate       │
                            │ Relationship│
                            └─────────────┘
```

> 📌 **Key Notations**:
> - 🟨 **Single Rectangle** = Strong Entity  
> - 🟨🟨 **Double Rectangle** = Weak Entity (`DEPENDENT`)  
> - ⬦ **Single Diamond** = Regular Relationship  
> - ⬦⬦ **Double Diamond** = Identifying Relationship (`DEPENDENTS_OF`)  
> - 🟠 **Single Oval** = Simple Attribute  
> - 🟠🟠 **Double Oval** = Multi-valued Attribute (`Locations`)  
> - 🟠 (dashed) = Derived Attribute (`Age`)  
> - **N:1, 1:N, M:N** = Cardinality labels  
> - **Single line** = Partial participation  
> - **Double line** = Total participation  

---

## 🧩 Step 5: Handle Special Cases

### 1️⃣ **Weak Entity: DEPENDENT**
- ❌ No standalone PK → depends on `EMPLOYEE`
- ✅ Composite PK = `(Essn, Dependent_name)`
- ✅ Shown as **double rectangle**
- ✅ Relationship `DEPENDENTS_OF` = **double diamond** (identifying)

> 💡 *Why?* A dependent only makes sense *in the context of an employee*.

---

### 2️⃣ **Recursive Relationship: SUPERVISION**
- One `EMPLOYEE` supervises another `EMPLOYEE`
- Add **role names**: `Supervisor` and `Supervisee`
- Cardinality: 1:N (one supervisor, many supervisees)

```plaintext
          ┌─────────────┐
          │  EMPLOYEE   │
          └──────┬──────┘
                 │
          ┌──────▼──────┐
          │ SUPERVISION │
          └──────┬──────┘
                 │
          ┌──────▼──────┐
          │  EMPLOYEE   │
          └─────────────┘
           ↑            ↑
        Supervisor   Supervisee
```

---

### 3️⃣ **Redundancy Reduction**
> ❗ Don’t store `Mgr_ssn` and `Mgr_start_date` in `DEPARTMENT` — move them to `MANAGES` relationship!

✅ Why?
- Avoid update anomalies (if manager changes, update once in relationship, not in two places)
- Follows normalization principles

```plaintext
          ┌─────────────┐
          │  EMPLOYEE   │
          └──────┬──────┘
                 │
          ┌──────▼──────┐
          │   MANAGES   │ ← add attributes: Mgr_start_date
          └──────┬──────┘
                 │
          ┌─────────────┐
          │  DEPARTMENT │
          └─────────────┘
```

---

### 4️⃣ **Composite & Multi-valued Attributes**

#### ➤ Composite: `Name` in `EMPLOYEE`
```plaintext
          ┌─────────────┐
          │  EMPLOYEE   │
          └──────┬──────┘
                 │
          ┌──────▼──────┐
          │    Name     │ ← composite
          └──────┬──────┘
                 │
          ┌──────▼──────┐
          │ Fname       │
          └─────────────┘
                 │
          ┌──────▼──────┐
          │ Minit       │
          └─────────────┘
                 │
          ┌──────▼──────┐
          │ Lname       │
          └─────────────┘
```

#### ➤ Multi-valued: `Locations` in `DEPARTMENT`
```plaintext
          ┌─────────────┐
          │  DEPARTMENT │
          └──────┬──────┘
                 │
          ┌──────▼──────┐
          │ Locations   │ ← double oval
          └─────────────┘
```

> 🛠️ *Implementation Tip*: In relational model → create separate table (e.g., `Dept_Locations(Dnumber, Location)`).

---

## 🧭 Best Practices for Drawing ER Diagrams

| Rule | Why |
|------|-----|
| ✅ Use consistent naming | `Employee` ≠ `Employees` — pick one style |
| ✅ Label cardinalities clearly | Use `1`, `N`, `M` or Crow’s Foot symbols |
| ✅ Show participation constraints | Especially for business rules (e.g., “Every order must have a customer”) |
| ✅ Avoid crossing lines | Use layout tools (draw.io, Lucidchart) |
| ✅ Keep it simple | Focus on core entities/relationships first |

---

## 🧪 Quick Self-Check

| Question | Answer |
|---------|--------|
| Is `DEPENDENT` a weak entity? | ✅ Yes — double rectangle + identifying relationship |
| What’s the cardinality of `WORKS_ON`? | ✅ M:N (many employees, many projects) |
| Where should `Mgr_start_date` go? | ✅ In `MANAGES` relationship — reduces redundancy |
| Can an employee be their own supervisor? | ❌ No — unless explicitly allowed (rare) |
| Is `Locations` multi-valued? | ✅ Yes — double oval |

📌 **Quick Memory Hook:**

> 🟨 **Entity** = Box  
> ⬦ **Relationship** = Diamond  
> 🟠 **Attribute** = Oval  
> 🟨🟨 **Weak Entity** = Double Box  
> ⬦⬦ **Identifying Rel** = Double Diamond  
> 🟠🟠 **Multi-valued** = Double Oval  
> 🟠 (dashed) = Derived  
> **N:1, 1:N, M:N** = Cardinality  
> **Single/Double Line** = Partial/Total Participation

> *“A great ER diagram doesn’t just show data — it tells the story of your business.”* 🎯✨