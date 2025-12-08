# 🌐 **1. `SocketChannel` — TCP Client (The Outbound Pipe)**

## 🧩 What It Is
A **bidirectional, non-blocking-capable** channel to a remote TCP server.

### 🔧 Core Operations
| Operation | Code | Notes |
|----------|------|-------|
| Connect | `ch.connect(addr)` | Blocking by default |
| Non-blocking | `ch.configureBlocking(false)` | Required for `Selector` |
| Read | `ch.read(buf)` | Returns `0` if no data (non-blocking) |
| Write | `ch.write(buf)` | May write partially — **always loop!** |
| Close | `ch.close()` | Sends TCP FIN |

## ✅ Minimal Practical: HTTP GET (Blocking)
```java
try (SocketChannel ch = SocketChannel.open()) {
    ch.connect(new InetSocketAddress("example.com", 80));
    
    // Send request
    String req = "GET / HTTP/1.1\r\nHost: example.com\r\n\r\n";
    ch.write(ByteBuffer.wrap(req.getBytes(UTF_8)));
    
    // Read response
    ByteBuffer buf = ByteBuffer.allocate(1024);
    int n = ch.read(buf);
    if (n > 0) {
        buf.flip();
        System.out.println(new String(buf.array(), 0, n, UTF_8));
    }
}
```

## ⚠️ Gotcha #1: **Partial Writes**
```java
ByteBuffer buf = ByteBuffer.wrap(new byte[100_000]);
while (buf.hasRemaining()) {  // ← MUST LOOP
    ch.write(buf);
}
```

## ⚠️ Gotcha #2: **Non-blocking Read**
```java
ch.configureBlocking(false);
int n = ch.read(buf);
if (n == 0) {
    // No data yet — do other work or select()
}
```

✅ **Use when**: Building HTTP clients, DB drivers, microservice comms.