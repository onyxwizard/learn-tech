# 🧩 **2. Database Models & Types**  
## *Choosing the Right Foundation for Your Data*

> 💡 **Rule of thumb**:  
> - Need strong consistency, complex queries, and integrity? → **Relational**  
> - Rapid iteration, flexible schema, scale-out? → **NoSQL**  
> - Blazing-fast caching or simple lookups? → **Key-Value**  
> - Relationships are the *core* of your app? → **Graph**


## 📋 **2.1 Relational Databases (RDBMS)**  
✅ *The gold standard for transactional systems*

**Core Idea**: Data lives in **tables** — rows (records) and columns (attributes) — with strict **schema** and defined **relationships**.

#### 🔑 Key Concepts:
| Term | Meaning | Example |
|------|---------|---------|
| **Table** | A collection of related entities | `users`, `orders`, `products` |
| **Row (Record)** | One instance of an entity | `id: 101, name: "Alex", email: "alex@example.com"` |
| **Column (Field)** | A property/attribute | `name`, `created_at`, `price` |
| **Schema** | The blueprint: table structure + data types + constraints | Enforced before any data is inserted |
| **Primary Key (PK)** | Unique identifier for a row | `user_id` |
| **Foreign Key (FK)** | Link to another table’s PK | `orders.user_id` → `users.id` |
| **Relationships** | How tables connect: 1:1, 1:Many, Many:Many | One user → many orders |

#### 🛠️ Popular RDBMSs:
| System | Best For | Notes |
|--------|----------|-------|
| **PostgreSQL** | Complex apps, JSON support, extensibility | Most advanced open-source; "JSONB" lets it handle semi-structured data too 🦄 |
| **MySQL** | Web apps, simplicity, speed | Widely used (WordPress, Shopify backend) |
| **SQLite** | Embedded apps, mobile, dev/testing | Zero setup — file-based, single-user |
| **SQL Server / Oracle** | Enterprise, legacy systems, deep integrations | Commercial, high support cost |

✅ **When to choose RDBMS**:  
✔️ Financial systems (banking, payments)  
✔️ E-commerce (orders, inventory, users)  
✔️ Any app requiring complex queries (`JOIN`s, aggregations, transactions)

---

### 📄 **2.2 Document Databases**  
✅ *Flexibility & developer velocity*

**Core Idea**: Store data as **self-contained documents** (usually JSON/BSON), each with its own structure. No fixed schema. Think “objects in code” → “documents in DB”.

```json
{
  "_id": "u101",
  "name": "Taylor",
  "email": "taylor@example.com",
  "preferences": {
    "theme": "dark",
    "notifications": ["email", "sms"]
  },
  "orders": [
    { "id": "o789", "total": 45.99, "items": [...] }
  ]
}
```

#### 🔑 Strengths:
- 🌱 **Schema-less**: Add new fields on the fly  
- 📦 **Nested data**: No need for `JOIN`s — related data lives together  
- 🚀 **Horizontal scaling**: Distribute data across servers easily  
- 🧩 **Great for**: User profiles, catalogs, content management, IoT device configs

#### 🛠️ Popular Options:
- **MongoDB**: Most popular; rich querying, aggregations, Atlas (cloud)  
- **Firestore** (Google): Real-time sync, mobile/web focus, offline support  
- **Couchbase**: Hybrid (document + key-value), enterprise-grade

⚠️ Trade-offs:  
- Harder to enforce global constraints (e.g., “every user must have an email”)  
- Complex joins require app-level logic or denormalization  
- Less ACID-compliant by default (though modern ones like MongoDB 4.0+ support multi-document ACID)



## 🔑 **2.3 Key-Value Stores**  
✅ *Speed. Simplicity. Scalability.*

**Core Idea**: Ultra-simple `key → value` mapping.  
- **Key**: Unique identifier (e.g., `"session:abc123"`)  
- **Value**: Anything — string, JSON, binary blob

```bash
SET "user:101:name" "Jordan"
GET "user:101:name"  → "Jordan"
```

#### 🔑 Strengths:
- ⚡ **Microsecond reads/writes**  
- 📏 **Massive scale**: Millions of ops/sec  
- 🧱 **Simple to shard/distribute**

#### 🛠️ Use Cases & Examples:
| Use Case | Example Key | Example Value |
|---------|-------------|---------------|
| **Caching** | `cache:product_homepage` | HTML snippet (gzipped) |
| **Sessions** | `sess:xyz789` | `{ user_id: 101, cart: [...] }` |
| **Configuration** | `config:feature_flags` | `{ dark_mode: true, beta: false }` |
| **Real-time counters** | `clicks:ad_205` | `142857` (integer) |

| System | Notes |
|--------|-------|
| **Redis** | In-memory (blazing fast), supports lists, sets, streams, pub/sub |
| **DynamoDB** (AWS) | Managed, auto-scaling, durable (disk-backed), pay-per-request |
| **etcd** | Distributed config store (used by Kubernetes) |

✅ **Ideal for**:  
✔️ Caching layers  
✔️ Session stores  
✔️ Leaderboards, rate limiting  
✔️ Simple state management


## 📊 **2.4 Column-Family / Wide-Column Stores**  
✅ *Built for analytics, time-series, and massive scale*

**Core Idea**:  
Instead of storing data row-by-row (like RDBMS), store it **column-by-column** — optimized for reading *many rows, few columns*.

Think:  
> 🔍 *“What were the temperatures at sensor S7 every hour last week?”*  
→ You only need the `timestamp` and `temp` columns for thousands of rows.

#### Structure:
| Row Key | Column Family: `stats` | Column Family: `metadata` |
|---------|------------------------|----------------------------|
| `sensor:S7` | `2025-11-01_12:00:temp=22.4`, `2025-11-01_13:00:temp=22.6` | `location=warehouse_A`, `type=indoor` |

#### 🔑 Strengths:
- 📈 **Extreme write throughput** (millions of writes/sec)  
- 📉 **Efficient compression** (similar data in columns compresses well)  
- 🌐 **Distributed by design** — no single point of failure

#### 🛠️ Popular Systems:
- **Apache Cassandra**: Masterless, linear scalability, tunable consistency  
- **Google Bigtable**: Powers Gmail, Analytics — petabyte-scale  
- **ScyllaDB**: Cassandra-compatible, rewritten in C++ for speed

✅ **Use when**:  
✔️ Time-series data (metrics, logs, IoT)  
✔️ Event sourcing  
✔️ Real-time analytics dashboards

## 🌐 **2.5 Graph Databases**  
✅ *When relationships *are* the data*

**Core Idea**:  
Model the world as **nodes** (entities) and **edges** (relationships) — with properties on both.

```
(Alex) —[FRIENDS_WITH {since: 2020}]→ (Sam)  
(Sam) —[WORKS_AT]→ (Company X)  
(Alex) —[LIKES]→ (PostgreSQL)
```

### 🔑 Why it’s powerful:
- 🔍 **Traverse relationships in constant time**  
  → “Find friends of friends who like PostgreSQL” = 2 hops  
  (In SQL? Complex `JOIN`s that slow down exponentially!)

- 🧠 **Intuitive for connected domains**:  
  - Social networks  
  - Fraud detection (find hidden rings)  
  - Recommendation engines (“People like you also bought…”)  
  - Knowledge graphs (Google, Alexa)

#### 🛠️ Popular Systems:
- **Neo4j**: Mature, Cypher query language (`MATCH (u:User)-[:FRIENDS*2]->(f) RETURN f`)  
- **Amazon Neptune**: Managed, supports both property graph & RDF  
- **ArangoDB**: Multi-model (graph + document + key-value)

## ⏱️ **2.6 Time-Series & Specialized Databases**  
✅ *When general-purpose DBs aren’t optimized enough*

| Type | Purpose | Examples | Why Specialized? |
|------|---------|----------|------------------|
| **Time-Series** | Store & analyze time-stamped data (metrics, events) | InfluxDB, TimescaleDB (PostgreSQL extension), Prometheus | Efficient compression, downsampling, window functions |
| **Geospatial** | Location-based queries (`ST_Distance`, `ST_Contains`) | PostGIS (on PostgreSQL), MongoDB GeoJSON | Spatial indexing (R-trees), coordinate systems |
| **Vector** | Similarity search (AI embeddings, recommendations) | Pinecone, Weaviate, pgvector | Approximate Nearest Neighbor (ANN) indexing |
| **Ledger** | Immutable, verifiable history (blockchain-like) | Amazon QLDB | Cryptographic verification, no tampering |

💡 **TimescaleDB tip**: It’s *not* a new DB — it’s PostgreSQL + time-series superpowers. You get SQL + scalability + full ACID.



## 🧭 Quick Decision Guide: Which DB When?

| Need | Best Fit |
|------|----------|
| 💰 Financial app, complex reports | **PostgreSQL** or **MySQL** |
| 📱 Mobile app with offline sync | **Firestore** or **SQLite** |
| 🚀 Real-time leaderboard / caching | **Redis** |
| 📈 IoT sensor data (1M writes/min) | **TimescaleDB** or **Cassandra** |
| 👥 Social network / fraud detection | **Neo4j** |
| 🧠 AI app (semantic search) | **pgvector** (PostgreSQL) or **Pinecone** |



### ✅ **Summary: Part 2 in 60 Seconds**

| Model | Schema | Scale | Consistency | Best For |
|-------|--------|-------|-------------|----------|
| **Relational** | Strict | Vertical | Strong (ACID) | Transactions, integrity |
| **Document** | Flexible | Horizontal | Tunable | Rapid dev, evolving data |
| **Key-Value** | None | Massive | Eventual/Strong | Caching, simple state |
| **Wide-Column** | Semi-flexible | Massive | Tunable | Time-series, analytics |
| **Graph** | Schema-optional | Medium-Large | Strong | Deep relationships |
| **Specialized** | Varies | Varies | Varies | Domain-specific needs |
