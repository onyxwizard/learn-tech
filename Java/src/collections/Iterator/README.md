# 🧭 Java `Iterator` & `Iterable` Cheatsheet  
*(For reasoning, debugging, and design — not just syntax)*


## 🔁 Core Interfaces: Roles & Relationships

| Concept       | Responsibility | Key Question to Ask |
|---------------|----------------|---------------------|
| `Iterable<T>` | *“I can be iterated”* | → Can this object be used in a `for-each` loop? Does it expose `iterator()`? |
| `Iterator<T>` | *“I walk through one collection, once”* | → Am I holding state (position)? Do I detect concurrent modification? |

> 💡 **Why two interfaces?**  
> Separation of *capability* (`Iterable`) from *mechanism* (`Iterator`). One `Iterable` can produce many independent `Iterator`s.



## 🛠️ `Iterator<T>` — 4 Core Methods

| Method | Returns | Safe to call when? | Gotcha / Socratic Prompt |
|--------|---------|--------------------|---------------------------|
| `hasNext()` | `boolean` | Always (idempotent) | ❓ What if collection is modified *after* `hasNext()` but *before* `next()`? |
| `next()` | `T` | Only if `hasNext() == true` | ❗ Throws `NoSuchElementException` if no next. Always pair with `hasNext()` in loops. |
| `remove()` | `void` | **Only once per `next()`**, and only *after* a `next()` | ❗ Illegal to call before first `next()` or twice in a row. |
| `forEachRemaining(λ)` | `void` | After some elements consumed | ✅ Efficient bulk operation — avoids manual `while` loop. |

> 🚨 Critical Insight:  
> **`remove()` is the *only* safe way to modify the underlying collection during iteration.**  
> Direct modification (e.g., `list.remove()`) → `ConcurrentModificationException`.


## 🔍 Obtaining an `Iterator`

```java
Iterator<T> it = collection.iterator();  // List, Set, Queue, etc.

// Also works for any Iterable:
Iterable<T> iterable = ...;
Iterator<T> it = iterable.iterator();
```

✅ Works for: `ArrayList`, `LinkedList`, `HashSet`, `TreeSet`, `HashMap.keySet()`, etc.  
❌ Does *not* guarantee order — only `List` and `SortedSet` do.


## 🔄 Iteration Patterns

### 1. Classic `while` loop (most control)
```java
while (it.hasNext()) {
    T item = it.next();
    // process item
    // optionally: it.remove();
}
```

### 2. `forEachRemaining()` (clean, functional)
```java
it.forEachRemaining(item -> System.out.println(item));
// ⚠️ No access to `remove()` inside the lambda!
```

### 3. `for-each` loop (uses `Iterable`, not `Iterator` directly)
```java
for (T item : collection) { ... } 
// → secretly calls collection.iterator() and uses hasNext()/next()
```

> 🤔 Compare:  
> `for-each` is syntactic sugar over `Iterator`, but **hides the iterator** — so you *can’t* call `remove()`.

---

## ⚠️ Concurrent Modification: The Rules

| Action | Throws `ConcurrentModificationException`? | Why? |
|-------|--------------------------------------------|------|
| `list.add()` / `list.remove()` during iteration | ✅ Yes (usually) | Iterator’s internal `modCount` ≠ collection’s `modCount` |
| `iterator.remove()` | ❌ No | Designed for safe removal — updates both structure *and* iterator state |
| Structural mod *before* first `next()` | ❓ *Sometimes* no — but **don’t rely on it!** | Fail-fast behavior is best-effort, not guaranteed |

> 🔎 **What counts as “structural modification”?**  
> Any change to *number of elements* (add/remove/clear). Replacing an element (`set()`) is usually safe.

---
# 🧬 `Iterable<T>` — The Bigger Picture

```java
public interface Iterable<T> {
    Iterator<T> iterator();          // ✅ Must implement
    default void forEach(λ);         // ✅ Default: uses iterator()
    default Spliterator<T> spliterator(); // ✅ For parallel streams
}
```

### Ways to iterate an `Iterable`:
| Method | Code | When to use |
|--------|------|-------------|
| `for-each` | `for (T t : iterable)` | ✅ Default choice (read-only, clean) |
| Manual `Iterator` | `Iterator<T> it = iterable.iterator();` | ✅ Need `remove()` or fine-grained control |
| `forEach()` | `iterable.forEach(t -> ...)` | ✅ Simple side effects (e.g., logging), no index |

> 📉 **Performance note**:  
> `for-each` on `ArrayList` is *slightly slower* than indexed `for` loop (due to `Iterator` allocation), but negligible except in ultra-hot loops.


## 🧪 Common Pitfalls & Fixes

| Problem | Symptom | Fix |
|--------|---------|-----|
| Calling `next()` without `hasNext()` | `NoSuchElementException` | Always guard with `hasNext()` |
| Modifying collection directly in loop | `ConcurrentModificationException` | Use `iterator.remove()`, or collect indices/elements to remove *after* |
| Assuming `Set.iterator()` order | Unpredictable iteration | Use `LinkedHashSet` (insertion order) or `TreeSet` (sorted) |
| Reusing same `Iterator` | Unexpected behavior (e.g., empty on second pass) | Call `collection.iterator()` again for new traversal |


## 🔄 Specialized Iterators

| Type | Use Case | Key Feature |
|------|----------|-------------|
| `ListIterator<T>` | Bidirectional list traversal | `hasPrevious()`, `previous()`, `add()`, `set()` |
| `Spliterator<T>` | Parallel/efficient bulk operations | Used by Streams; supports splitting |
| Custom `Iterator` | Domain-specific traversal (e.g., tree, circular buffer) | Implement `hasNext()`/`next()`; consider fail-fast design |

> 🎯 Pro Tip:  
> Prefer **composition** over inheritance:  
> ```java
> public class MyCollection implements Iterable<T> {
>     private List<T> data = ...;
>     public Iterator<T> iterator() { return data.iterator(); } // delegate!
> }
> ```


## ✅ Quick Decision Guide

| Goal | Recommended Approach |
|------|-----------------------|
| Read all elements, no modification | `for (T t : collection)` |
| Remove some elements during iteration | `while (it.hasNext()) { if (cond) it.remove(); }` |
| Process remaining items after partial iteration | `it.forEachRemaining(...)` |
| Need index or random access | Use `List` + `for (int i=0; i<size; i++)` |
| Want parallel processing | Use `Stream` + `spliterator()` (not raw `Iterator`) |


## 🌱 Socratic Self-Test (Try Without Looking!)

1. Can you call `remove()` twice in a row? Why/why not?  
2. Does `HashMap.values().iterator().remove()` remove the *value* or the *key-value pair*?  
3. If `it.hasNext()` returns `false`, can you “reset” the iterator?  
4. Why doesn’t `Iterator` have a `reset()` method?  

 *(Answers: 1. No — only after `next()`. 2. Entire entry. 3. No — iterators are one-shot. 4. Encourages stateless design; use new iterator instead.)*
