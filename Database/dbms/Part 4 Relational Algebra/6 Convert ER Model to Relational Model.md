# 🔄 **DBMS – ER to Relational Model**  
### *From Sketch to Schema: Turning Diagrams into Tables*

> 🎯 *ER diagrams are the blueprint. The relational model is the construction plan.*  
> We convert entities → tables, relationships → foreign keys, and constraints → primary/foreign keys — as faithfully as possible.

Let’s map **4 key ER concepts** to relational schemas — using simple, memorable examples.



## 🧭 Why Convert? The Big Picture

| ER Concept | Relational Equivalent |
|------------|------------------------|
| Entity     | Table                  |
| Attribute  | Column                 |
| Relationship | Foreign Key + Junction Table |
| Weak Entity | Composite PK + FK      |
| IS-A (Specialization) | Inheritance via FKs |

> ⚠️ *Not all ER constraints map perfectly* — e.g., “total participation” becomes `NOT NULL` or application logic.

---

## 📐 Step 1: Map Entities → Tables

> ✅ **Rule**:  
> - One entity = one table  
> - Attributes = columns  
> - Primary key = `PRIMARY KEY`

### 🧪 Example: `Student` Entity
```plaintext
          ┌─────────────┐
          │   Student   │
          │ Roll_No.    │ ← PK
          │ Name        │
          │ Class       │
          │ Subject     │
          └─────────────┘
```

➡️ **Relational Schema**:
```sql
CREATE TABLE Student (
    Roll_No INT PRIMARY KEY,
    Name VARCHAR(50),
    Class VARCHAR(10),
    Subject VARCHAR(30)
);
```

> ✅ Simple & direct!

---

## 🔗 Step 2: Map Relationships → Tables (or FKs)

> ✅ **Rule**:  
> - Binary relationship → Add **FKs** OR create a **junction table** (if M:N or has attributes)

### 🧪 Example: `Enrolled` Relationship (M:N)
```plaintext
          ┌─────────────┐       ┌─────────────┐
          │   Student   │───────│   Course    │
          └──────┬──────┘       └──────┬──────┘
                 │                      │
          ┌──────▼──────┐
          │  Enrolled   │ ← Relationship with attributes!
          │ Marks       │
          │ JoiningDate │
          └─────────────┘
```

➡️ **Relational Schema**:
```sql
-- Student & Course (already mapped as tables)

CREATE TABLE Enrolled (
    Roll_No INT,
    CID INT,
    Marks DECIMAL(3,1),
    JoiningDate DATE,
    PRIMARY KEY (Roll_No, CID),  -- Composite PK
    FOREIGN KEY (Roll_No) REFERENCES Student(Roll_No),
    FOREIGN KEY (CID) REFERENCES Course(CID)
);
```

> ✅ **Why junction table?**  
> - M:N relationship → needs composite key  
> - Has attributes (`Marks`, `JoiningDate`) → can’t be stored in either table

---

## 🧩 Step 3: Map Weak Entities → Tables with Composite Keys

> ✅ **Rule**:  
> - Create table for weak entity  
> - Include **partial key** + **owner’s PK** → form **composite PK**  
> - Declare FK to owner

### 🧪 Example: `Dependent` (Weak Entity)
```plaintext
          ┌─────────────┐       ┌─────────────┐
          │   Student   │───────│  Dependent  │
          └──────┬──────┘       └──────┬──────┘
                 │                      │
          ┌──────▼──────┐
          │  Depends    │ ← Identifying Relationship
          └─────────────┘
```

➡️ **Relational Schema**:
```sql
CREATE TABLE Dependent (
    Roll_No INT,           -- FK to Student
    Name VARCHAR(50),      -- Partial Key
    Sex CHAR(1),
    Bdate DATE,
    PRIMARY KEY (Roll_No, Name),  -- Composite PK
    FOREIGN KEY (Roll_No) REFERENCES Student(Roll_No)
);
```

> ✅ `Name` alone isn’t unique → combined with `Roll_No` → unique per student

---

## 🧬 Step 4: Map Specialization / Generalization → Inheritance via FKs

> ✅ **Rule**:  
> - Superclass → Table with common attributes  
> - Subclasses → Tables with unique attributes + FK to superclass  
> - Optional: Use `IS_A` flag column for disjoint/overlapping

### 🧪 Example: `Person → Student / Teacher`
```plaintext
          ┌─────────────┐
          │   Person    │
          │ Name        │
          │ Age         │
          │ Gender      │
          └──────┬──────┘
                 │ IS-A
     ┌───────────┴───────────┐
     │                       │
┌─────────────┐        ┌─────────────┐
│ Student     │        │ Teacher     │
│ Roll_No     │        │ EmpID       │
└─────────────┘        └─────────────┘
```

➡️ **Relational Schema**:

#### Option A: **Table per Subclass** (Most Common)
```sql
CREATE TABLE Person (
    SSN VARCHAR(11) PRIMARY KEY,
    Name VARCHAR(50),
    Age INT,
    Gender CHAR(1)
);

CREATE TABLE Student (
    SSN VARCHAR(11) PRIMARY KEY,
    Roll_No INT,
    Major VARCHAR(30),
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);

CREATE TABLE Teacher (
    SSN VARCHAR(11) PRIMARY KEY,
    EmpID VARCHAR(10),
    Dept VARCHAR(20),
    FOREIGN KEY (SSN) REFERENCES Person(SSN)
);
```

> ✅ **Advantages**: No nulls, clean separation  
> ❌ **Disadvantage**: Querying “all people” requires `UNION`

#### Option B: **Single Table with Type Flag**
```sql
CREATE TABLE Person (
    SSN VARCHAR(11) PRIMARY KEY,
    Name VARCHAR(50),
    Age INT,
    Gender CHAR(1),
    Role ENUM('Student', 'Teacher'),
    Roll_No INT NULL,
    EmpID VARCHAR(10) NULL,
    Major VARCHAR(30) NULL,
    Dept VARCHAR(20) NULL,
    CHECK ((Role = 'Student' AND Roll_No IS NOT NULL) OR
           (Role = 'Teacher' AND EmpID IS NOT NULL))
);
```

> ✅ Simpler queries  
> ❌ Wasted space, complex constraints

---

## 🖼️ Visual Summary: ER → Relational Mapping

```plaintext
          ER Element              →      Relational Equivalent
          ┌─────────────┐                ┌─────────────┐
          │   Entity    │                │   Table     │
          │ Attributes  │                │ Columns     │
          └──────┬──────┘                └──────┬──────┘
                 │                              │
          ┌──────▼──────┐                ┌──────▼──────┐
          │ Relationship│                │ Foreign Key │
          │ (Binary)    │                │ or Junction │
          └──────┬──────┘                └──────┬──────┘
                 │                              │
          ┌──────▼──────┐                ┌──────▼──────┐
          │ Weak Entity │                │ Composite PK│
          │ + Owner FK  │                │ + FK        │
          └──────┬──────┘                └──────┬──────┘
                 │                              │
          ┌──────▼──────┐                ┌──────▼──────┐
          │ IS-A        │                │ Inheritance │
          │ Hierarchy   │                │ via FKs     │
          └─────────────┘                └─────────────┘
```

---

## 🧪 Quick Self-Check

| ER Element | Relational Mapping |
|------------|---------------------|
| `Student` (Entity) | `Student` table with `Roll_No` as PK |
| `Enrolled` (M:N relationship with attributes) | Junction table `Enrolled` with composite PK `(Roll_No, CID)` |
| `Dependent` (Weak Entity) | Table with composite PK `(Roll_No, Name)` + FK to `Student` |
| `Person → Student/Teacher` | Either separate tables with FKs, or single table with type flag |


📌 **Quick Memory Hook**:

> 🧱 **Entity** → Table  
> 🔗 **Relationship** → FK or Junction Table  
> 🧩 **Weak Entity** → Composite PK + FK  
> 🧬 **IS-A** → Inheritance via FKs

> *“ER diagrams are the story. Relational schema is the script.”* 🎬✨
