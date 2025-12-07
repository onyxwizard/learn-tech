# 📡 **3. `DatagramChannel` — UDP (The Firehose)**

## 🧩 What It Is
A channel for **connectionless, unreliable** UDP packets.

### 🔧 Core Operations
| Operation | Code | Notes |
|----------|------|-------|
| Send | `ch.send(buf, addr)` | Fire-and-forget |
| Receive | `ch.receive(buf)` | Returns `SocketAddress` or `null` |
| Connect (optional) | `ch.connect(addr)` | Binds to peer — faster sends |

## ✅ Minimal Practical: UDP Echo
```java
// Server
try (DatagramChannel ch = DatagramChannel.open()) {
    ch.bind(new InetSocketAddress(8081));
    ByteBuffer buf = ByteBuffer.allocate(64);
    
    while (true) {
        SocketAddress client = ch.receive(buf); // blocks
        buf.flip();
        ch.send(buf, client); // echo back
        buf.clear();
    }
}

// Client
try (DatagramChannel ch = DatagramChannel.open()) {
    ch.connect(new InetSocketAddress("localhost", 8081));
    ch.write(ByteBuffer.wrap("Hello".getBytes()));
    
    ByteBuffer buf = ByteBuffer.allocate(64);
    ch.read(buf);
    buf.flip();
    System.out.println(new String(buf.array(), 0, buf.remaining()));
}
```

### ⚠️ Gotchas:
- ❌ No delivery guarantee  
- ❌ No ordering  
- ✅ Use for: DNS, video streaming, game state, metrics

✅ **Use when**: Low-latency > reliability.