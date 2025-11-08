# 🎨 **5. Database Design Fundamentals**

### Step 1 → Step 2 → Step 3…  
A repeatable workflow from *idea* to *robust schema*.



### 🖼️ **5.1 Entity-Relationship (ER) Modeling**  
*Sketch before you build.*

An **ER diagram** is a blueprint:  
- **Entities** = nouns (things you store): `User`, `Product`, `Order`  
- **Attributes** = properties: `email`, `price`, `status`  
- **Relationships** = verbs (how they connect): *places*, *contains*, *belongs to*

#### 🔑 ER Diagram Symbols (Simplified)
```
[User] ────<places>───┬── [Order]
                       │
                       └──<contains>── [OrderItem] ───<has>── [Product]
```

- **Cardinality** (how many?):  
  - `1` —── `N` → One user → many orders *(1:N)*  
  - `M` —── `N` → Orders ↔ Products via `OrderItem` *(M:N resolved)*

#### 🛠️ Free Tools to Draw ERDs:
| Tool | Why Use It |
|------|------------|
| [**dbdiagram.io**](https://dbdiagram.io) ✅ | Write code-like syntax → auto-generate diagram + SQL |
| [**draw.io**](https://draw.io) (Diagrams.net) | Drag-and-drop, offline, export to PNG/SVG |
| [**Lucidchart**](https://lucidchart.com) | Team collaboration, templates |

🔹 **dbdiagram.io example** (simple & powerful):
```dbml
Table users {
  id int [pk, increment]
  email varchar(255) [unique, not null]
  created_at timestamp [default: `now()`]
}

Table orders {
  id int [pk, increment]
  user_id int [ref: > users.id]
  status varchar(20) [default: 'pending']
  total decimal(10,2)
}

Ref: orders.user_id > users.id
```
→ Instantly generates a clean diagram + PostgreSQL DDL.

✅ **Best practice**:  
**Design in ER first** → review with team → *then* generate SQL.  
Never design directly in `CREATE TABLE` — it’s like coding without planning.



### 📐 **5.2 Normalization: Taming Redundancy**  
*“What’s the least amount of data I need to store — without losing meaning?”*

Normalization = a series of **forms** (1NF → 2NF → 3NF) to eliminate redundancy and update anomalies.

Let’s walk through with a bad example → fix it step by step.

#### 🚫 Anti-Pattern: The “Kitchen Sink” Table
```sql
orders (
  order_id,
  customer_name,
  customer_email,
  customer_address,
  product1_name, product1_price,
  product2_name, product2_price,
  ...
)
```
**Problems**:  
- Redundant: Customer address repeated per order  
- Inflexible: What if order has 50 items?  
- Update anomaly: Change email? Must update *every order*!  
- Insert anomaly: Can’t store a product without an order.



### ✅ **1NF: First Normal Form — Atomic Values**
> **Rule**: Every column contains *one value* — no lists, no repeating groups.

🔧 Fix: Split multi-values into rows.
```sql
-- BEFORE (violates 1NF)
orders (id, products) → products = "Mouse, Keyboard"

-- AFTER (1NF)
orders (id)  
order_items (order_id, product_id, quantity)
```

✅ **Test for 1NF**: Can you access a single value without parsing (e.g., `SPLIT()`)? If yes → 1NF.



### ✅ **2NF: Second Normal Form — No Partial Dependencies**
> **Rule**: In tables with **composite PKs**, every non-key column must depend on the *entire* PK — not just part of it.

🔧 Example violation:
```sql
enrollments (
  student_id,  -- part of PK
  course_id,   -- part of PK
  course_name, -- ❌ depends only on course_id!
  grade
)
```

🔧 Fix: Split into two tables.
```sql
courses (course_id PK, course_name)  
enrollments (student_id, course_id, grade)  
-- FK: (student_id, course_id) → composite PK
```

✅ **Test for 2NF**: If your PK has >1 column, ask: *“Does this column depend on ALL parts of the key?”*



### ✅ **3NF: Third Normal Form — No Transitive Dependencies**
> **Rule**: Non-key columns must depend *only* on the PK — not on *other non-key columns*.

🔧 Example violation:
```sql
users (
  id PK,
  zip_code,
  city,        -- ❌ depends on zip_code, not id!
  state        -- ❌ depends on zip_code!
)
```

🔧 Fix: Extract dependent attributes.
```sql
zip_codes (zip_code PK, city, state)  
users (id PK, name, zip_code FK → zip_codes.zip_code)
```

✅ **Test for 3NF** (the “Telephone Test”):  
*“If I call the customer and they tell me their `id`, can I learn `city` directly — or do I need another piece of info (like `zip_code`) first?”*  
→ If the latter → violation.

### ↔️ **Denormalization: When to Break the Rules**

Normalization is ideal for **transactional systems** (OLTP).  
But for **analytics/reporting** (OLAP), strict 3NF can hurt performance.

🔹 **Common denormalization tactics**:
| Technique | Why | Example |
|---------|-----|---------|
| **Add redundant columns** | Avoid `JOIN`s in reports | `orders.customer_name` (cached from `users`) |
| **Pre-aggregate data** | Speed up dashboards | `daily_sales(date, total_orders, revenue)` |
| **Embed nested data** | Reduce round trips | Store `user.preferences` as JSONB in `users` |

✅ **Golden rule**:  
> **Normalize first. Denormalize *only* when you have proof (metrics!) that it’s needed.**  
> → Profile queries. Measure. Then optimize.

💡 Real-world hybrid:  
- **OLTP DB** (PostgreSQL, normalized)  
- **Data Warehouse** (Snowflake, BigQuery, denormalized star schema)  
→ Sync via ETL/ELT (e.g., Airflow, Fivetran)



## 🛠️ **5.3 Data Modeling Tools & Workflow**

### 🔄 Recommended Design Process:
1. **Gather requirements**  
   → “What questions must the system answer?”  
   → “What actions will users perform?”

2. **Sketch ER diagram** (pen/paper or dbdiagram.io)  
   → Identify entities, relationships, cardinality

3. **Normalize to 3NF**  
   → Eliminate redundancy, ensure integrity

4. **Review & refine**  
   → “What queries will be frequent? Any bottlenecks?”  
   → Consider *strategic denormalization*

5. **Generate DDL**  
   → Use tool (dbdiagram → SQL) or write manually  
   → Add indexes, constraints, comments

6. **Evolve with migrations**  
   → Never `ALTER TABLE` in production manually  
   → Use migration tools:  
     - **Liquibase** / **Flyway** (Java)  
     - **Alembic** (Python)  
     - **EF Core Migrations** (.NET)  
     - **Rails Migrations** (Ruby)

#### 📝 Pro Tips:
- **Name consistently**:  
  `snake_case` for tables/columns (`user_id`, `created_at`)  
  `plural` for tables (`users`, `orders`)  
- **Use comments**:  
  ```sql
  COMMENT ON COLUMN orders.status IS 'One of: pending, shipped, delivered, cancelled';
  ```
- **Version your schema**: Treat it like code — in Git.



### ✅ **Summary: Part 5 in 60 Seconds**

| Concept | Key Takeaway |
|--------|--------------|
| 🖼️ **ER Modeling** | Visual blueprint → prevents costly mistakes early |
| 📐 **1NF** | Atomic values — no lists or repeating groups |
| 📐 **2NF** | In composite PK tables, depend on *all* PK parts |
| 📐 **3NF** | Depend *only* on PK — not on other non-key columns |
| ↔️ **Denormalization** | OK for analytics — but measure first! |
| 🛠️ **Tools & Workflow** | Sketch → Normalize → Review → Migrate → Evolve |

> 🎯 **You now have a repeatable process to design databases that scale, stay correct, and adapt over time.**

