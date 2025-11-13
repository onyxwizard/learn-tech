# 🔗 **DBMS – Joins**  
### *Where Tables Hold Hands (and Sometimes Leave Gaps!)*

> 🧩 *Cartesian product? Too messy!*  
> **Joins** are smarter: *“Only pair rows that make sense together.”*  
> Think of it like matching heroes with their powers — only valid combos survive! 🦸‍♂️✨

Let’s explore the 5 key join types — with the **Mystic Rangers** universe 🌌



## 🌌 Meet the Mystic Rangers & Their Gear

### 🦸 **RANGERS** Table
| RangerID | Name      | PowerColor | Team      |
|----------|-----------|------------|-----------|
| R101     | **Blaze** | Red        | Alpha     |
| R102     | **Frost** | Blue       | Alpha     |
| R103     | **Terra** | Green      | Beta      |
| R104     | **Zephyr**| Yellow     | Alpha     |

### ⚔️ **ZORDS** Table *(Giant Battle Mechs)*
| ZordID | ZordName     | PilotColor | Specialty     |
|--------|--------------|------------|---------------|
| Z201   | **Inferno Dragon** | Red        | Fire Blast    |
| Z202   | **Aqua Titan**    | Blue       | Ice Storm     |
| Z203   | **Thunder Wolf**  | Green      | Earthquake    |
| Z204   | **Sky Phoenix**   | Yellow     | Wind Slash    |
| Z205   | **Shadow Panther**| Purple     | Stealth Mode  |

> ✅ Perfect setup for joins: `PowerColor = PilotColor` → who pilots what?

---

## 1️⃣ **Theta (θ) Join — “Match with Custom Rules”**

> ✅ **Definition**: Pair rows where **any condition** is true (>, <, =, ≠, etc.).  
> 🔤 **Notation**: `R ⋈<sub>θ</sub> S`

### 🧪 Example: *“Rangers who can pilot Zords of same or lighter weight class”*  
→ Suppose lighter color = lighter Zord (Red > Blue > Green > Yellow > Purple)

Let’s use: `PowerColor ≥ PilotColor` (alphabetical: Blue > Green > Purple > Red > Yellow ❌ — instead, use ranking)

Better: Assign rank:  
- Red = 5, Blue = 4, Green = 3, Yellow = 2, Purple = 1  
→ Condition: `PowerRank ≥ PilotRank`

But for simplicity, let’s stick to **equality** first → which brings us to…

---

## 2️⃣ **Equijoin — Theta Join with Equality Only**

> ✅ **Definition**: Theta join where **only `=` is used**.  
> 🎯 *Most common join — the workhorse of databases.*

### 🧪 Example: *“Which Ranger pilots which Zord?”*  
→ Match `PowerColor = PilotColor`

```
RANGERS ⋈<sub>Rangers.PowerColor = Zords.PilotColor</sub> ZORDS
```

| RangerID | Name   | PowerColor | ZordID | ZordName         | PilotColor | Specialty  |
|----------|--------|------------|--------|------------------|------------|------------|
| R101     | Blaze  | Red        | Z201   | Inferno Dragon   | Red        | Fire Blast |
| R102     | Frost  | Blue       | Z202   | Aqua Titan       | Blue       | Ice Storm  |
| R103     | Terra  | Green      | Z203   | Thunder Wolf     | Green      | Earthquake |
| R104     | Zephyr | Yellow     | Z204   | Sky Phoenix      | Yellow     | Wind Slash |

✅ Blaze → Inferno Dragon  
✅ Frost → Aqua Titan  
…etc.

> 💡 *This is NOT yet Natural Join — column names differ (`PowerColor` vs `PilotColor`).*

---

## 3️⃣ **Natural Join (⋈) — “Auto-Match on Common Columns”**

> ✅ **Definition**: Join on **all same-named, same-domain attributes** — and **merge duplicates**.  
> 🔤 **Notation**: `R ⋈ S`

### 🔄 Rename `PilotColor` → `PowerColor` in `ZORDS`

| ZordID | ZordName         | **PowerColor** | Specialty  |
|--------|------------------|----------------|------------|
| Z201   | Inferno Dragon   | Red            | Fire Blast |
| Z202   | Aqua Titan       | Blue           | Ice Storm  |
| Z203   | Thunder Wolf     | Green          | Earthquake |
| Z204   | Sky Phoenix      | Yellow         | Wind Slash |
| Z205   | Shadow Panther   | Purple         | Stealth Mode |

Now do:
```
RANGERS ⋈ ZORDS
```

| RangerID | Name   | **PowerColor** | ZordID | ZordName         | Specialty  |
|----------|--------|----------------|--------|------------------|------------|
| R101     | Blaze  | Red            | Z201   | Inferno Dragon   | Fire Blast |
| R102     | Frost  | Blue           | Z202   | Aqua Titan       | Ice Storm  |
| R103     | Terra  | Green          | Z203   | Thunder Wolf     | Earthquake |
| R104     | Zephyr | Yellow         | Z204   | Sky Phoenix      | Wind Slash |

✅ **Only one `PowerColor` column** — merged automatically!  
🚫 **Shadow Panther (Purple)** excluded — no Purple Ranger yet!

> 🎯 *Natural Join = Equijoin + column deduplication.*

---

## 4️⃣ **Outer Joins — “Don’t Leave Anyone Behind!”**

Inner joins (θ, equi, natural) **drop unmatched rows**.  
But real life is messy — sometimes heroes don’t have Zords (yet!), or Zords wait for pilots.

Enter **Outer Joins** 🌈

---

### 🔴 **Left Outer Join (R ⟕ S)**  
> ✅ **All Rangers** — even if no Zord. Unmatched Zord fields → `NULL`.

```
RANGERS ⟕ ZORDS
```

| RangerID | Name   | PowerColor | ZordID | ZordName         | Specialty    |
|----------|--------|------------|--------|------------------|--------------|
| R101     | Blaze  | Red        | Z201   | Inferno Dragon   | Fire Blast   |
| R102     | Frost  | Blue       | Z202   | Aqua Titan       | Ice Storm    |
| R103     | Terra  | Green      | Z203   | Thunder Wolf     | Earthquake   |
| R104     | Zephyr | Yellow     | Z204   | Sky Phoenix      | Wind Slash   |
| **R105** | **Nova** | **Purple** | **NULL** | **NULL**         | **NULL**     | ← 🆕 New Ranger!

> 💡 *Use when you care about **all left-side entities** (e.g., all customers, even without orders).*

---

### 🔵 **Right Outer Join (R ⟖ S)**  
> ✅ **All Zords** — even if no Ranger. Unmatched Ranger fields → `NULL`.

```
RANGERS ⟖ ZORDS
```

| RangerID | Name   | PowerColor | ZordID | ZordName         | Specialty    |
|----------|--------|------------|--------|------------------|--------------|
| R101     | Blaze  | Red        | Z201   | Inferno Dragon   | Fire Blast   |
| R102     | Frost  | Blue       | Z202   | Aqua Titan       | Ice Storm    |
| R103     | Terra  | Green      | Z203   | Thunder Wolf     | Earthquake   |
| R104     | Zephyr | Yellow     | Z204   | Sky Phoenix      | Wind Slash   |
| **NULL** | **NULL** | **NULL**   | Z205   | Shadow Panther   | Stealth Mode | ← Unassigned Zord!

> 💡 *Use for inventory — all products, even if not sold.*

---

### 🟣 **Full Outer Join (R ⟗ S)**  
> ✅ **Everyone** — Rangers + Zords. Missing parts → `NULL`.

```
RANGERS ⟗ ZORDS
```

| RangerID | Name   | PowerColor | ZordID | ZordName         | Specialty    |
|----------|--------|------------|--------|------------------|--------------|
| R101     | Blaze  | Red        | Z201   | Inferno Dragon   | Fire Blast   |
| R102     | Frost  | Blue       | Z202   | Aqua Titan       | Ice Storm    |
| R103     | Terra  | Green      | Z203   | Thunder Wolf     | Earthquake   |
| R104     | Zephyr | Yellow     | Z204   | Sky Phoenix      | Wind Slash   |
| **R105** | **Nova** | **Purple** | **NULL** | **NULL**         | **NULL**     |
| **NULL** | **NULL** | **NULL**   | Z205   | Shadow Panther   | Stealth Mode |

✅ Perfect for audits: *“Who’s missing a partner?”*

---

## 🧠 Join Cheat Sheet

| Join Type | Symbol | Keeps | Use Case |
|-----------|--------|-------|----------|
| **Theta** | `⋈<sub>θ</sub>` | Matches only | Custom logic (`>`, `<`, `≠`) |
| **Equijoin** | `⋈<sub>=</sub>` | Matches only | Most queries (`A.id = B.id`) |
| **Natural** | `⋈` | Matches only | Tables with same-named keys |
| **Left Outer** | `⟕` | **All Left** + matches | All customers (even without orders) |
| **Right Outer** | `⟖` | **All Right** + matches | All products (even unsold) |
| **Full Outer** | `⟗` | **All** + matches | Audits, sync checks |

> 🎯 In SQL:
```sql
-- Equijoin (most common)
SELECT * FROM Rangers JOIN Zords ON Rangers.PowerColor = Zords.PilotColor;

-- Left Outer
SELECT * FROM Rangers LEFT JOIN Zords ON ...;

-- Full Outer (not in MySQL)
SELECT * FROM Rangers FULL OUTER JOIN Zords ON ...;
```

---

## 🦸 Final Mission: Assemble the Team!

| Ranger | Zord | Status |
|--------|------|--------|
| Blaze  | Inferno Dragon | ✅ Matched |
| Frost  | Aqua Titan | ✅ Matched |
| Terra  | Thunder Wolf | ✅ Matched |
| Zephyr | Sky Phoenix | ✅ Matched |
| Nova   | — | ⚠️ Needs Zord! *(Assign Shadow Panther?)* |
| —      | Shadow Panther | ⚠️ Needs Pilot! |

> 🚀 **Your move, Commander!** Use joins to balance the team. 😎



📌 **Memory Hook**:

> 🔴 **Left** = **L**eft side fully included  
> 🔵 **Right** = **R**ight side fully included  
> 🟣 **Full** = **F**ull coverage  
> ⚖️ **Natural** = **N**ame-matching auto-join  
> 🧩 **Theta** = **T**otal custom logic

> *“Joins don’t just connect tables — they tell stories of relationships.”* 🌐✨
