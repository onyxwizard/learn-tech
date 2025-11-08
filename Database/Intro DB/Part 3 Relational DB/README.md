# 🏗️ **3. Relational Databases Deep Dive**  
## *Where structure meets power*

> 💡 **Remember**: Relational ≠ “has relations.”  
> It’s named after *relational algebra* (math by E.F. Codd, 1970) — but for us, it means:  
> ✅ Tables  
> ✅ Rows & Columns  
> ✅ Keys & Relationships  
> ✅ Enforced integrity

Let’s build this step by step.

## 📑 **3.1 Tables, Records (Rows), Fields (Columns)**

Think of a database as a **collection of spreadsheets** — but with rules, speed, and safety.

| Concept | Database Term | Spreadsheet Equivalent | Example |
|--------|----------------|------------------------|---------|
| **Entity** | Table | Worksheet tab | `customers`, `orders` |
| **Instance** | Row (Record) | One full row | `id: 101, name: "Morgan", email: "m@example.com"` |
| **Attribute** | Column (Field) | Column header | `name`, `created_at`, `is_active` |

#### 🧱 Example: `products` Table

| `id` (PK) | `name` | `price` | `category_id` (FK) | `created_at` |
|-----------|--------|---------|---------------------|--------------|
| 1 | Wireless Mouse | 24.99 | 3 | 2025-01-15 08:30:00 |
| 2 | Mechanical Keyboard | 89.99 | 3 | 2025-01-16 10:15:00 |
| 3 | Laptop Stand | 34.50 | 5 | 2025-02-01 14:22:00 |

✅ **Key insight**:  
Every row is a *self-contained fact*.  
Every column has a *data type* (`INT`, `VARCHAR(100)`, `DECIMAL(5,2)`, `TIMESTAMP`), enforced by the DB.

## 📐 **3.2 Schema vs. Schema-less**

| Concept | Relational (Schema-**ful**) | Document (Schema-**less**) |
|--------|------------------------------|----------------------------|
| **Definition** | Structure defined *before* data is inserted | Structure emerges *with* data |
| **Enforcement** | DB rejects invalid data | App must validate (or risk chaos) |
| **Change Cost** | `ALTER TABLE` — can be heavy on large tables | Just add new field in next doc |
| **Safety** | High — guarantees consistency | Low — risk of typos, missing fields |

🔹 **But wait — modern RDBMSs are evolving!**  
- PostgreSQL: `JSONB` column type — store flexible docs *inside* a table  
- MySQL: `JSON` type + functions (`JSON_EXTRACT`, `->>`)  
→ Best of both worlds: **structured core + flexible extensions**.

> 🛠️ Example (PostgreSQL):
> ```sql
> CREATE TABLE users (
>   id SERIAL PRIMARY KEY,
>   email VARCHAR(255) NOT NULL UNIQUE,
>   profile JSONB  -- flexible!
> );
> 
> INSERT INTO users (email, profile)
> VALUES ('sam@example.com', 
>         '{"theme": "dark", "notifications": ["push"], "last_login": "2025-11-08"}');
> ```

✅ **Takeaway**: Schema isn’t “rigid” — it’s *intentional design*. Use flexibility *where needed*, structure *where it matters*.

## 🔑 **3.3 Keys: The Backbone of Relational Integrity**

Keys uniquely identify and connect data.

#### 🥇 **Primary Key (PK)**
- **Purpose**: Uniquely identify *one row* in a table  
- **Rules**:  
  - Must be **unique**  
  - Must **not be NULL**  
  - Ideally **immutable** (never changes)

| Type | Example | Pros | Cons |
|------|---------|------|------|
| **Natural PK** | `email`, `ISBN`, `SSN` | Meaningful, human-readable | Can change (email), privacy risk (SSN), long |
| **Surrogate PK** | Auto-increment `id`, UUID | Simple, stable, fast | No meaning — just an identifier |

✅ **Best practice**: Use `SERIAL` (PostgreSQL) or `AUTO_INCREMENT` (MySQL) **surrogate integer PKs** for most tables — unless you have a *truly stable, unique natural key* (e.g., country codes: `'US'`, `'DE'`).

#### 🔗 **Foreign Key (FK)**
- **Purpose**: Enforce a link between tables  
- **How**: Value in one table *must exist* as a PK in another table

```sql
-- Enforce: every order must belong to a valid user
ALTER TABLE orders 
ADD CONSTRAINT fk_user 
FOREIGN KEY (user_id) REFERENCES users(id);
```

→ Try inserting `user_id = 999` when no user `999` exists? ❌ **Rejected.**

#### 🔀 **Composite Key**
- A PK made of **multiple columns**  
- Used when *no single column* is unique — but the *combination* is.

```sql
-- A student can enroll in a course only once per semester
CREATE TABLE enrollments (
  student_id INT,
  course_id INT,
  semester VARCHAR(10),
  PRIMARY KEY (student_id, course_id, semester)  -- composite PK
);
```

✅ **When to use**: Junction tables (see 3.4), time-bounded facts, multi-tenant IDs.


## 🔄 **3.4 Relationships: How Tables Talk**

Relational databases shine because they model real-world connections.

#### 1️⃣ **One-to-One (1:1)**  
- One row in Table A ↔ *exactly one* row in Table B  
- Rare — usually indicates a table split for performance/security

✅ **Example**:  
`users` ↔ `user_profiles`  
→ Separate `password_hash` and `ssn` into a restricted table.

```sql
users (id PK, email)  
user_profiles (user_id PK + FK → users.id, bio, ssn_encrypted)
```

> ⚠️ Often merged into one table unless strong reason not to.

#### 2️⃣ **One-to-Many (1:N)**  
✅ **Most common relationship**

✅ **Example**:  
One `author` → many `books`  
One `customer` → many `orders`

```sql
authors (id PK, name)  
books (id PK, title, author_id FK → authors.id)
```

→ Query: *“Get all books by author 5”* → `SELECT * FROM books WHERE author_id = 5;`

#### 3️⃣ **Many-to-Many (M:N)**  
- Neither table can hold the FK directly  
- Requires a **junction table** (aka *bridge*, *link*, or *associative* table)

✅ **Example**:  
`students` ⟷ `courses`  
A student takes many courses. A course has many students.

```sql
students (id PK, name)  
courses (id PK, title)  
enrollments (  -- junction table
  student_id INT REFERENCES students(id),
  course_id  INT REFERENCES courses(id),
  enrolled_at TIMESTAMP,
  PRIMARY KEY (student_id, course_id)  -- composite PK
)
```

→ Query: *“Which courses is student 101 taking?”*  
```sql
SELECT c.title 
FROM courses c
JOIN enrollments e ON c.id = e.course_id
WHERE e.student_id = 101;
```

💡 **Pro tip**: Junction tables can store *metadata about the relationship*:  
→ `enrolled_at`, `grade`, `role` (e.g., “admin” in a team), `status`.

## 🛑 **3.5 Constraints: Your Data’s Safety Net**

Constraints = **rules enforced by the DB** — not just the app.  
They prevent bad data *at the source*.

| Constraint | Purpose | Example |
|-----------|---------|---------|
| `NOT NULL` | Column must have a value | `email VARCHAR(255) NOT NULL` |
| `UNIQUE` | No duplicate values in column(s) | `email VARCHAR(255) UNIQUE` |
| `PRIMARY KEY` | `NOT NULL` + `UNIQUE` (defines row identity) | `id SERIAL PRIMARY KEY` |
| `FOREIGN KEY` | Value must exist in another table | `user_id INT REFERENCES users(id)` |
| `CHECK` | Custom validation logic | `age INT CHECK (age >= 0 AND age <= 150)` |
| `DEFAULT` | Auto-fill if no value provided | `created_at TIMESTAMP DEFAULT NOW()` |

#### 🔍 Real-world `CHECK` examples:
```sql
-- Prevent negative prices
price DECIMAL(10,2) CHECK (price > 0)

-- Ensure email looks like an email (basic)
email VARCHAR(255) CHECK (email ~* '^.+@.+\..+$')

-- Status must be one of these values
status VARCHAR(20) CHECK (status IN ('pending', 'shipped', 'delivered'))
```

✅ **Why enforce at DB level?**  
- Apps change (new frontend, mobile, API) — DB is the *source of truth*  
- Prevents bugs, manual SQL mistakes, and malicious inputs  
- Self-documenting: schema tells you the rules


### ✅ **Summary: Part 3 in 60 Seconds**

| Concept | Key Takeaway |
|--------|--------------|
| 📑 **Tables & Rows** | Data = structured facts in rows; columns = typed attributes |
| 📐 **Schema** | Not rigidity — *intentional design*; modern RDBMSs support flexibility (JSONB) |
| 🔑 **Keys** | PK = identity, FK = link, Composite = multi-column uniqueness |
| 🔄 **Relationships** | 1:1 (rare), 1:N (common), M:N (needs junction table) |
| 🛑 **Constraints** | `NOT NULL`, `UNIQUE`, `CHECK`, etc. = automatic data quality |

> 🎯 **You now understand how to *model* real-world data reliably** — the essential prep work before writing a single SQL query.

