## **Data Structures in Java (Linear & Non-Linear)**

### **I. LINEAR DATA STRUCTURES**
Elements arranged sequentially, one after another.

#### **1. Array-based**
- **Arrays** (`int[]`, `String[]`, etc.)
- **String** (`String`, `StringBuilder`, `StringBuffer`)
- **ArrayList** (`java.util.ArrayList<E>`)
- **Vector** (`java.util.Vector<E>`)
- **Stack** (`java.util.Stack<E>`)

#### **2. Linked Structures**
- **LinkedList** (`java.util.LinkedList<E>`)
- **Doubly Linked List** (implemented via `LinkedList`)
- **Circular Linked List** (custom implementation)

#### **3. Queue & Variants**
- **Queue Interface** (`java.util.Queue<E>`)
- **PriorityQueue** (`java.util.PriorityQueue<E>`)
- **ArrayDeque** (`java.util.ArrayDeque<E>`)
- **LinkedList** (can act as Queue)
- **BlockingQueue** (`java.util.concurrent.BlockingQueue`)
  - `ArrayBlockingQueue`
  - `LinkedBlockingQueue`
  - `PriorityBlockingQueue`

#### **4. Specialized Linear Structures**
- **BitSet** (`java.util.BitSet`)
- **EnumSet** (`java.util.EnumSet<E>`)

---

### **II. NON-LINEAR DATA STRUCTURES**
Elements have hierarchical/multiple relationships.

#### **1. Tree Structures**
- **TreeSet** (`java.util.TreeSet<E>`) - Red-Black Tree implementation
- **TreeMap** (`java.util.TreeMap<K,V>`) - Red-Black Tree
- **Binary Tree** (custom implementation)
- **Binary Search Tree (BST)** (custom)
- **AVL Tree** (custom)
- **Trie (Prefix Tree)** (custom)
- **Segment Tree** (custom)
- **Fenwick Tree (Binary Indexed Tree)** (custom)
- **Heap** (PriorityQueue is Min-Heap by default)
  - **Min-Heap**
  - **Max-Heap** (via `Collections.reverseOrder()`)

#### **2. Graph Structures**
- **Graph** (custom implementation)
  - Adjacency Matrix
  - Adjacency List
- **Directed Graph** (Digraph)
- **Weighted Graph**
- **Directed Acyclic Graph (DAG)**

#### **3. Hash-based Structures** (Non-linear organization)
- **HashMap** (`java.util.HashMap<K,V>`)
- **HashSet** (`java.util.HashSet<E>`)
- **LinkedHashMap** (`java.util.LinkedHashMap<K,V>`)
- **LinkedHashSet** (`java.util.LinkedHashSet<E>`)
- **IdentityHashMap** (`java.util.IdentityHashMap<K,V>`)
- **WeakHashMap** (`java.util.WeakHashMap<K,V>`)
- **HashTable** (`java.util.Hashtable<K,V>`)

#### **4. Composite/Complex Structures**
- **Collections of Collections**
  - `ArrayList<HashMap<String, Integer>>`
  - `HashMap<String, ArrayList<Integer>>`
- **Multi-dimensional Arrays**
  - `int[][]`, `String[][]`

---

### **III. JAVA COLLECTIONS FRAMEWORK HIERARCHY**

```
Collection Interface
├── List (Linear)
│   ├── ArrayList
│   ├── LinkedList
│   └── Vector → Stack
├── Queue (Linear)
│   ├── PriorityQueue
│   ├── ArrayDeque
│   └── LinkedList (also implements)
├── Set (Non-linear by concept, but implementation varies)
│   ├── HashSet
│   ├── LinkedHashSet
│   └── TreeSet
└── (Map is separate interface - Non-linear)
    ├── HashMap
    ├── LinkedHashMap
    ├── TreeMap
    ├── HashTable
    └── WeakHashMap
```



### **IV. SPECIAL CATEGORIES**

#### **Concurrent Structures** (java.util.concurrent)
- **Linear:**
  - `ConcurrentLinkedQueue`
  - `ConcurrentLinkedDeque`
  - `CopyOnWriteArrayList`
  - `CopyOnWriteArraySet`
  
- **Non-linear:**
  - `ConcurrentHashMap`
  - `ConcurrentSkipListMap`
  - `ConcurrentSkipListSet`

#### **Legacy Structures**
- **Stack** (`java.util.Stack`) - Linear
- **Vector** (`java.util.Vector`) - Linear
- **HashTable** (`java.util.Hashtable`) - Non-linear



### **V. COMMON CUSTOM IMPLEMENTATIONS**

#### **Linear:**
- Circular Buffer/Ring Buffer
- Dynamic Array (similar to ArrayList)
- Sparse Array
- Gap Buffer (for text editors)

#### **Non-linear:**
- B-Tree/B+ Tree
- Red-Black Tree (Java's TreeMap/TreeSet uses this)
- Suffix Tree/Suffix Array
- Union-Find (Disjoint Set)
- Bloom Filter
- Skip List (used in `ConcurrentSkipListMap`)



### **Quick Reference Table**

| **Type** | **Linear** | **Non-Linear** |
|----------|------------|----------------|
| **Sequential Access** | Arrays, ArrayList, LinkedList | ✗ |
| **Direct Access** | Arrays, ArrayList | HashMap, TreeMap |
| **Hierarchical** | ✗ | Trees, Graphs |
| **Network/Relations** | ✗ | Graphs, Networks |
| **Java Examples** | `String`, `ArrayList`, `Queue` | `HashMap`, `TreeSet`, `Graph` |


### **Key Points to Remember:**
1. **String** is linear (character sequence)
2. **Arrays** are the fundamental linear structure
3. **Lists** (ArrayList, LinkedList) are linear
4. **Maps** (HashMap, TreeMap) are non-linear
5. **Sets** (HashSet, TreeSet) are non-linear by nature
6. **Queues/Stacks** are linear with restricted access
7. **Trees/Graphs** are inherently non-linear

Most data structures in Java are part of the **Collections Framework** or require custom implementation (like Graphs, Tries, etc.).