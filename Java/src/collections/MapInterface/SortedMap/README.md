# 🔍 Layer 1: The Symmetry Principle — Maps as Paired Sets

You wrote:
> *"The Java SortedMap interface [...] is a subtype of the java.util.Map interface, with the addition that the elements [...] are sorted internally."*

But what *exactly* is sorted?

### Prompt:
Consider:
```java
SortedMap<String, Integer> map = new TreeMap<>();
map.put("zebra", 10);
map.put("apple", 5);
```

❓ **What does `map.keySet()` return? `map.values()`? `map.entrySet()`?**  
➡️  
- `keySet()`: `["apple", "zebra"]` — **sorted**  
- `values()`: `[5, 10]` — *not a `SortedSet`*, just a `Collection` in key-order  
- `entrySet()`: `[("apple",5), ("zebra",10)]` — entries ordered by key

> 🧠 Insight:  
> `SortedMap` is **not** a “sorted collection of entries” — it’s a map whose **keys form a `SortedSet`**, and everything else *derives* from that.

This is why:
- `firstKey()` and `lastKey()` exist — but no `firstValue()` (values aren’t sorted!)
- `headMap("c")` means *all entries with key < "c"* — **not** first N entries.

✅ **Golden Rule**:  
> In `SortedMap`, *order is defined on keys only*. Values are passengers.

---

## 🌐 Layer 2: The View Contract — Live, Lazy, and Lethal (Again — but Richer)

Just like `SortedSet`, `SortedMap` offers **subrange views** — but now with *key-based navigation*.

| Method | Returns | Inclusive? | Live? | Backed by? |
|--------|---------|------------|-------|------------|
| `headMap(toKey)` | `SortedMap` | `toKey` excluded | ✅ | Original map |
| `headMap(toKey, inclusive)` | `SortedMap` | configurable | ✅ | Original map |
| `tailMap(fromKey)` | `SortedMap` | `fromKey` included | ✅ | Original |
| `tailMap(fromKey, inclusive)` | `SortedMap` | configurable | ✅ | Original |
| `subMap(from, to)` | `SortedMap` | `[from, to)` | ✅ | Original |
| `subMap(from, fromInc, to, toInc)` | `SortedMap` | configurable | ✅ | Original |
| `descendingMap()` | `NavigableMap` | — | ✅ | Original (reversed) |
| `descendingKeySet()` | `NavigableSet<K>` | — | ✅ | Keys, reversed |

### 🔹 Critical Behavior: Views Are *Bounded Proxies*
```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(10, "ten");
map.put(20, "twenty");
map.put(30, "thirty");

SortedMap<Integer, String> view = map.subMap(15, true, 25, true); // [20 → "twenty"]

view.put(18, "eighteen"); // ✅ OK — 18 ∈ [15,25]
view.put(5, "five");      // ❌ IllegalArgumentException! key=5 < 15
```

➡️ The view *enforces key bounds on every mutation* — not just at creation.

### 🔹 They’re *transitive*:
```java
view.put(20, "new twenty"); // updates original map!
System.out.println(map.get(20)); // "new twenty"
```

### 🔹 And *hierarchical*:
```java
SortedMap<Integer, String> nested = view.subMap(17, true, 22, false);
nested.put(18, "updated"); // reflects in view and original
```

> 💡 Real-world use:  
> - **Time-series sharding**: `subMap(start, end)` for hourly buckets  
> - **Geo-fencing**: `subMap(minLat, maxLat)` for spatial queries  
> - **Multi-level caching**: `headMap(expiryTime)` to evict stale entries

---

## ⚠️ Layer 3: Key Design Tensions — Where Bugs Hide

### Tension 1: `compareTo()` vs `equals()` — Again, But *Worse*
Recall the `SortedSet` rule: `x.compareTo(y) == 0` ⇔ `x.equals(y)`.

In `SortedMap`, it’s **even more critical** — because *keys define identity*.

#### Scenario:
```java
class Key implements Comparable<Key> {
    String label;
    int version;
    
    Key(String label, int version) { this.label = label; this.version = version; }
    
    @Override public int compareTo(Key o) { return label.compareTo(o.label); } // by label only
    @Override public boolean equals(Object o) { 
        return o instanceof Key k && label.equals(k.label) && version == k.version; 
    }
    @Override public int hashCode() { return Objects.hash(label, version); }
}
```

Now:
```java
SortedMap<Key, String> map = new TreeMap<>();
map.put(new Key("A", 1), "v1");
map.put(new Key("A", 2), "v2");

System.out.println(map.size()); // → 1!
System.out.println(map.get(new Key("A", 1))); // → "v2"!
```

➡️ `TreeMap` uses `compareTo()` for *key uniqueness* — so `"A",1` and `"A",2` collide.

✅ **Rule**:  
> For `TreeMap`, your key’s `compareTo()` **must be consistent with `equals()`**, *or* you must accept that `Map` semantics (`.get()`, `.containsKey()`) will behave unexpectedly.

---

### Tension 2: `null` Keys — The Silent Killer

```java
new TreeMap<>().put(null, "value"); // ❌ NullPointerException!
```

Why?  
- Natural ordering (`Comparable`) cannot compare `null`.
- Even `map.containsKey(null)` fails.

✅ Allowed *only* with a `Comparator` that handles `null`:
```java
TreeMap<String, String> map = new TreeMap<>((a, b) -> {
    if (a == null) return b == null ? 0 : -1;
    if (b == null) return 1;
    return a.compareTo(b);
});
map.put(null, "zero"); // ✅
```

But — now ask:  
❓ Does `map.get(null)` return `"zero"`?  
✅ Yes — if your comparator says `null == null`.

> 🧪 Danger: `HashMap` allows `null` keys (one), but `TreeMap` *only* with custom comparator. Mixing them in generic code (`Map<K,V>`) is risky.

---

### Tension 3: `descendingMap()` — Not Just an Iterator

You showed:
```java
Iterator it = map.descendingKeySet().iterator();
```

But consider:
```java
NavigableMap<String, Integer> rev = map.descendingMap();
rev.put("zulu", 26); // adds to *original map*!
System.out.println(map.lastKey()); // "zulu"
```

➡️ `descendingMap()` returns a **full `NavigableMap` view** — not a copy, not just an iterator.

✅ Use it for:
- Bidirectional caches
- “Top N” and “Bottom N” dashboards from the same data
- Algorithms needing both min-heap and max-heap semantics

---

## 🎯 Layer 4: Real-World Patterns — Beyond the Textbook

### Pattern 1: **Range Query with Payload**
You have sensor readings: `TreeMap<Instant, Double> data`.

Find all readings in `[start, end)`:
```java
NavigableMap<Instant, Double> window = data.subMap(start, true, end, false);
double avg = window.values().stream().mapToDouble(v -> v).average().orElse(0);
```

✅ Efficient — no copying, O(log n) to locate bounds.

---

### Pattern 2: **Find Nearest Neighbor (Key + Value)**
```java
NavigableMap<Integer, String> cache = new TreeMap<>();
cache.put(100, "A");
cache.put(200, "B");
cache.put(300, "C");

int target = 180;
Map.Entry<Integer, String> floor = cache.floorEntry(target);   // (100, "A")
Map.Entry<Integer, String> ceil  = cache.ceilingEntry(target); // (200, "B")

// Compare distances:
if (floor != null && ceil != null) {
    int d1 = target - floor.getKey();
    int d2 = ceil.getKey() - target;
    String closest = d1 <= d2 ? floor.getValue() : ceil.getValue();
}
```

➡️ `floorEntry()` and `ceilingEntry()` return *full entries* — no extra `get()` call.

| Method | Returns | Use When |
|--------|---------|----------|
| `floorKey(k)` | `K` | You only need the key |
| `floorEntry(k)` | `Map.Entry<K,V>` | You need key **and** value — avoids 2 lookups |

✅ Always prefer `*Entry()` methods for efficiency.

---

### Pattern 3: **Atomic Polling + Update**
Simulate a priority queue with inspection:
```java
NavigableMap<Long, Task> queue = new TreeMap<>(); // priority = timestamp

// Peek and update
Map.Entry<Long, Task> first = queue.firstEntry();
if (first != null && needsReschedule(first.getValue())) {
    queue.pollFirstEntry(); // removes and returns entry
    queue.put(newTime, updatedTask);
}
```

➡️ `pollFirstEntry()` and `pollLastEntry()` are **atomic** — no race between `firstKey()` and `remove()`.

---

## 📊 Performance & Trade-Offs

| Operation | `TreeMap` | `HashMap` | `LinkedHashMap` |
|----------|-----------|-----------|-----------------|
| `get(k)` | O(log n) | O(1) avg | O(1) avg |
| `put(k,v)` | O(log n) | O(1) avg | O(1) avg |
| `firstKey()` | O(1) | ❌ not supported | O(1) (if access-ordered) |
| `subMap(a,b)` | O(1) (view) | ❌ | ❌ |
| `floorEntry(k)` | O(log n) | ❌ | ❌ |
| Memory overhead | ~40–60 bytes/entry | ~32 bytes/entry | ~40 bytes/entry |

✅ Choose `TreeMap` when:
- You need **ordered iteration**, **range queries**, or **neighbor lookups**
- Keys are comparable and consistent with `equals()`
- Data size is moderate (< 1M entries)

❌ Avoid when:
- You only need `get/put` — `HashMap` is faster
- Keys have poor `hashCode()` — `TreeMap` is more stable
- You need frequent random access by index — consider `ArrayList<Map.Entry>` + sort

---

## 🧪 Socratic Self-Test

1. What does `map.subMap("b", "e").keySet()` return type? Is it a `SortedSet`?  
2. Can you call `map.descendingMap().headMap("m")`? What does it mean?  
3. If `map1` and `map2` are `TreeMap`s with same entries but different comparators, is `map1.equals(map2)` `true`?  
4. Why does `TreeMap` not implement `RandomAccess`?

—

**Answers**:

1. ✅ `SortedSet<K>` — because `SortedMap.keySet()` returns a `SortedSet`, and `subMap()` preserves that.  
2. ✅ Yes — `descendingMap()` returns a `NavigableMap`, so `headMap("m")` gives entries *greater than "m"* in original order (i.e., *smaller* in descending view).  
3. ❌ No — `Map.equals()` requires *same key-value mappings AND same order* (since Java 17, `Map` contract requires order for equality if ordered). Earlier versions: implementation-defined — but `TreeMap.equals()` compares entries *in iteration order*, so different comparators → different order → `false`.  
4. Because `TreeMap` is a tree — no O(1) random access by index. `RandomAccess` is for `List`s like `ArrayList`.

---

## 🌟 Final Insight: SortedMap as a *Temporal* Data Structure

`SortedMap` isn’t just “a map that’s sorted” — it’s a **first-class representation of ordered time or space**.

- Log timestamps → `TreeMap<Instant, LogEntry>`  
- Version history → `TreeMap<Integer, Snapshot>`  
- IP routing → `TreeMap<IPRange, Route>` (with custom comparator)  
- Financial ticks → `TreeMap<LocalDateTime, Price>`

In each case, the *navigation methods* (`floorEntry`, `subMap`, `pollFirstEntry`) are not “convenience” — they’re **domain verbs**.

That’s why `SortedMap` remains irreplaceable — even in the age of streams and reactive programming.

---