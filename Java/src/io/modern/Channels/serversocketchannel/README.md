# 🧱 I. Core NIO Classes (The Pillars)

| Class | Role | Key Methods | Critical Insight |
|-------|------|-------------|------------------|
| `ServerSocketChannel` | **Server listener** — accepts incoming connections | `open()`, `bind()`, `accept()`, `configureBlocking(false)`, `register(selector, OP_ACCEPT)` | Must be non-blocking to work with `Selector`. `accept()` returns `null` if no pending connection (non-blocking mode). |
| `SocketChannel` | **Bidirectional data pipe** — one per client | `open()`, `connect()`, `read()`, `write()`, `finishConnect()`, `configureBlocking(false)`, `register(selector, OP_READ/OP_WRITE)` | Can be used for *both* clients and servers. In server, created by `ServerSocketChannel.accept()`. |
| `Selector` | **Event multiplexer** — watches many channels | `open()`, `select()`, `selectedKeys()`, `wakeup()` | The heart of scalable I/O. One thread can manage 10k+ connections. Never block inside `select()` loop. |
| `SelectionKey` | **Channel + interest ops + attachment** | `isAcceptable()`, `isReadable()`, `isWritable()`, `isValid()`, `interestOps()`, `attach()`, `attachment()` | Think of it as a *control panel* for one channel’s event subscription. **Always call `iterator.remove()` after processing!** |
| `ByteBuffer` | **Data carrier** — wraps byte arrays/direct memory | `allocate()`, `allocateDirect()`, `wrap()`, `flip()`, `compact()`, `clear()`, `hasRemaining()`, `position()`, `limit()` | Misusing `flip()`/`compact()` is the #1 cause of NIO bugs. We’ll clarify this deeply. |


## 🔍 II. Deep Dive: Every Key Method & Its Meaning

Let’s walk through the **server lifecycle**, and explain *every* method you’ll use — why it exists, how to use it safely, and common pitfalls.


### 1️⃣ `ServerSocketChannel serverChannel = ServerSocketChannel.open();`

- ✅ **Purpose**: Create a new server socket (TCP listener).
- 🔧 **Must do next**: `serverChannel.bind(new InetSocketAddress(PORT))`
- ⚠️ **Gotcha**: 
  - By default, it’s **blocking** — `accept()` will hang until a client connects.
  - For `Selector` use, **always**: `serverChannel.configureBlocking(false);`

### 2️⃣ `serverChannel.register(selector, SelectionKey.OP_ACCEPT);`

- ✅ **Purpose**: Subscribe to “connection pending” events.
- 🔑 **OP_ACCEPT** = “Wake me up when `accept()` will return a non-null `SocketChannel`.”
- 📌 **Only `ServerSocketChannel` uses `OP_ACCEPT`**. Regular `SocketChannel` never does.

### 3️⃣ `serverChannel.accept()`

- ✅ **Purpose**: Accept a pending connection → returns a new `SocketChannel`.
- 🌪️ **Non-blocking behavior**:
  - If no pending connection → returns `null` (not exception!)
  - If connection ready → returns new `SocketChannel` (already connected!)
- ✅ **Server must**:
  - Configure new channel as non-blocking: `client.configureBlocking(false)`
  - Register it for `OP_READ` (usually): `client.register(selector, OP_READ, attachment)`

### 4️⃣ `Selector selector = Selector.open();`

- ✅ **Purpose**: Monitor multiple channels for readiness.
- 🔁 **Event loop core**:
  ```java
  while (true) {
      int ready = selector.select();        // blocks until event OR wakeup()
      // or selector.select(timeout)       // blocks up to N ms
      // or selector.selectNow()           // non-blocking check
      Set<SelectionKey> keys = selector.selectedKeys();
      // process keys → MUST remove each after!
  }
  ```
- ❗ **Critical rule**:  
  > 🚫 Never do heavy work (DB call, sleep, compute) inside the select loop — it blocks *all* I/O. Offload to thread pool.

### 5️⃣ `SelectionKey key = client.register(selector, ops, attachment);`

| Param | Meaning |
|-------|---------|
| `selector` | The multiplexer to register with |
| `ops` | Bitmask: `OP_READ`, `OP_WRITE`, `OP_CONNECT`, `OP_ACCEPT` |
| `attachment` | Any object (e.g., request buffer, session state) — retrieved via `key.attachment()` |

- ✅ **Change interest later**:  
  `key.interestOps(key.interestOps() \| SelectionKey.OP_WRITE);`  
  (e.g., after generating response, switch from `OP_READ` → `OP_WRITE`)

- ⚠️ **Never store state in local variables** — use `attachment()`.


### 6️⃣ `key.isReadable()` / `key.isWritable()` / `key.isAcceptable()`

- ✅ **Purpose**: Check *why* selector woke up.
- 🔄 **Always check `isValid()` first**:
  ```java
  if (!key.isValid()) return; // channel closed elsewhere
  if (key.isReadable()) { ... }
  ```
- 📌 **One key can be multiple ops**:  
  Rare, but possible — e.g., `OP_READ \| OP_WRITE` if both conditions true.


### 7️⃣ `SocketChannel.read(ByteBuffer dst)`

- ✅ **Purpose**: Read data *from network* → into `dst` buffer.
- 📏 **Returns**:
  - `> 0`: bytes read
  - `0`: no data (non-blocking only)
  - `-1`: EOF (client closed connection)
- 🔁 **Always use in loop** — one `read()` may not get full message.

#### 🧠 Buffer State Machine (Most Important!)

Let’s simulate reading `"GET /"` (6 bytes) with a 10-byte buffer:

| Step | Buffer State | Action | Why |
|------|--------------|--------|-----|
| 1. `clear()` | `pos=0, lim=10, cap=10` | Prepare for write (network → buffer) | Default after allocation |
| 2. `read()` → 6 bytes | `pos=6, lim=10, cap=10` | Data written at 0→5 | OS copied 6 bytes |
| 3. `flip()` | `pos=0, lim=6, cap=10` | Prepare for *read* (buffer → app) | Now `limit = old pos` |
| 4. `decode(buffer)` | reads 0→5 | ✅ Correct |
| 5. `compact()` | `pos=0, lim=10, cap=10` | Shift unread data (none here) to front, ready for next `read()` | Safer than `clear()` if partial message |

> ✅ Golden rule:  
> **After `read()`: `flip()` to consume → `compact()` to prep for next read.**  
> ❌ Never `clear()` after `read()` — you’ll lose unread data!

### 8️⃣ `SocketChannel.write(ByteBuffer src)`

- ✅ **Purpose**: Write data *from buffer* → to network.
- 📏 **Returns**: number of bytes written (may be < `src.remaining()`!).
- ⚠️ **Kernel send buffer is finite** — if full, `write()` returns `0` (non-blocking) or blocks (blocking).
- 🔁 **Must loop or use `OP_WRITE`**:
  ```java
  while (buffer.hasRemaining()) {
      int wrote = channel.write(buffer);
      if (wrote == 0) break; // non-blocking: no space → wait for OP_WRITE
  }
  ```

#### 🛡️ Backpressure Pattern:
```java
if (buffer.hasRemaining()) {
    // Couldn’t send all → stay interested in OP_WRITE
    key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
} else {
    // Done → go back to OP_READ (or close)
    key.interestOps(SelectionKey.OP_READ);
}
```

### 9️⃣ `SelectionKey.attach(Object obj)` / `key.attachment()`

- ✅ **Purpose**: Store per-connection state (no thread-locals needed).
- 📦 Typical attachment:
  ```java
  class Connection {
      ByteBuffer inBuffer = ByteBuffer.allocate(8192);
      ByteBuffer outBuffer;
      HttpRequest request;
      long lastActivity;
  }
  ```
- ✅ **Thread-safe**: One key → one connection → one thread processes it at a time.


### 🔟 `selector.select(timeout)` vs `select()` vs `selectNow()`

| Method | Blocking? | Use Case |
|--------|-----------|----------|
| `select()` | ✅ Yes (indefinite) | Pure event loop (wake with `wakeup()`) |
| `select(500)` | ✅ Yes (max 500ms) | Allows periodic tasks (e.g., timeout checks) |
| `selectNow()` | ❌ No | Polling (rare — high CPU) |

> ✅ **Best practice**: `select(500)` + background timeout checker (as in our server).


## 🧩 III. Advanced Patterns You *Must* Know

### A. 🕒 Timeout Management (Production Critical!)

- **Problem**: `Selector` doesn’t support per-channel timeouts.
- **Solution**:  
  - Track `lastActivity` (e.g., `System.currentTimeMillis()`) per channel.  
  - Run a `ScheduledExecutorService` to check/cleanup stale connections.  
  - Use `READ_TIMEOUT` (request incomplete) and `WRITE_TIMEOUT` (client not reading).

### B. 🔄 Partial Message Handling (HTTP, Protobuf, etc.)

- **Never assume one `read()` = one message**.
- **State machine per connection**:
  ```java
  enum State { READING_HEADERS, READING_BODY, SENDING_RESPONSE }
  ```
- Use `ByteBuffer` + `compact()` to accumulate.

### C. 🧹 Graceful Shutdown

```java
// Signal loop to exit
volatile boolean running = true;

// In shutdown hook:
running = false;
selector.wakeup(); // unblocks select() immediately
```

### D. 📊 Monitoring (What Prod Engineers Watch)

| Metric | How to Track |
|--------|--------------|
| Connection count | `selector.keys().size()` |
| Slow clients | `now - lastActivity > threshold` |
| Buffer pressure | `inBuffer.position()` high → client not reading |
| Selector wakeups/sec | High = chatty clients |


## 🚫 IV. Top 5 NIO Pitfalls (And How to Avoid Them)

| Pitfall | Symptom | Fix |
|--------|---------|-----|
| 1. Forgetting `key.iterator().remove()` | "Stuck" keys, duplicate events | Always `keys.remove()` after `keys.next()` |
| 2. Misusing `flip()`/`clear()`/`compact()` | Garbage data, missed bytes | Follow: `read()` → `flip()` → consume → `compact()` |
| 3. Blocking in select loop | Server freeze under load | Offload work (DB, compute) to thread pool |
| 4. Not handling `write() == 0` | Partial sends, hangs | Use `OP_WRITE` + loop until `!hasRemaining()` |
| 5. Ignoring `IOException` on `read()`/`write()` | Zombie connections | Always `catch`, log, `safeClose()` |

## 📚 V. When to Use NIO vs Alternatives

| Use Case | Recommendation |
|----------|----------------|
| Learning, simple servers | ✅ NIO (you’re doing great!) |
| Production HTTP APIs | 🛠️ Use **Netty** or **Vert.x** (NIO done right) |
| High-throughput binary protocols | ✅ Raw NIO + careful buffer mgmt |
| Simple CLI tools | ⚠️ Maybe just `java.net.Socket` (blocking is fine) |
| Need TLS/WebSocket/HTTP/2 | 🚫 Don’t roll your own — use Netty |

> 💡 **Rule of thumb**:  
> Write NIO once to *understand* it — then use a battle-tested framework for production.


## ✅ Your NIO Server Checklist

Before deploying any NIO server, ensure you have:

- [ ] `configureBlocking(false)` on all channels  
- [ ] `selector.select(timeout)` + timeout checker  
- [ ] `flip()` → consume → `compact()` buffer cycle  
- [ ] `OP_WRITE` registration for partial writes  
- [ ] `attachment()` for per-connection state  
- [ ] `key.iterator().remove()` after processing  
- [ ] `safeClose()` on all error paths  
- [ ] No blocking calls in select loop  
