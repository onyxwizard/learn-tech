# 🔗 **URL Processing in Java: From Legacy to Modern**

## 🧩 **What Is a URL? — More Than Just a String**

> 🔍 *Is `"https://example.com"` a URL or a URI?*  
> → **Both** — but all URLs are URIs, not all URIs are URLs.

| Term | Definition | Example |
|------|------------|---------|
| **URI** (Uniform Resource Identifier) | *Identifies* a resource (name or address) | `urn:isbn:0451450523`, `https://example.com` |
| **URL** (Uniform Resource Locator) | *Locates* a resource (how to get it) | `https://example.com/page.html` |
| **URN** (Uniform Resource Name) | *Names* a resource (persistent ID) | `urn:ietf:rfc:7230` |

✅ **In practice**:  
> Use `URI` for *parsing and building* (immutable, safe),  
> Use `URL` only for *opening connections* (legacy, mutable).

---

## 📚 Your Examples — Annotated & Enhanced

### 🔴 `URLDemo.java`: Parsing a URL

Your output is correct — but note two subtle issues:

```java
URL url = new URL("https://www.tutorialspoint.com/index.htm?language=en#j2se  ");
// ❌ Trailing space → may cause MalformedURLException in strict parsers
```

✅ **Fix: Trim and validate**
```java
String raw = "https://www.tutorialspoint.com/...#j2se  ";
URL url = new URL(raw.trim()); // ✅ Safe
```

Also:
- `getPort()` returns `-1` → no explicit port (uses default: 443 for HTTPS)  
- `getFile()` includes query (`/index.htm?language=en`) — not just path  
- `getRef()` is `j2se` — the fragment (not sent to server!)

🧠 **Key insight**:  
> The **fragment (`#ref`)** is *client-side only* — never sent to the server.  
> `https://site.com#top` and `https://site.com#bottom` → same HTTP request.

---

### 🔴 `URLConnectionDemo.java`: Reading a Web Page

Your code works — but has **critical production flaws**:

| Issue | Risk | Fix |
|------|------|-----|
| **No timeout** | Hangs forever on network issues | `connection.setConnectTimeout(5000)` |
| **No status check** | 404/500 errors → exception in `getInputStream()` | `if (connection.getResponseCode() == 200)` |
| **No charset handling** | Garbled text on non-UTF-8 sites | `InputStreamReader(..., StandardCharsets.UTF_8)` |
| **No resource cleanup** | Leaked connections | `try-with-resources` |

✅ **Production-Ready Version**:
```java
try {
    URL url = new URL("https://www.tutorialspoint.com".trim());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
    // 🔑 Critical settings
    conn.setRequestMethod("GET");
    conn.setConnectTimeout(5_000);
    conn.setReadTimeout(10_000);
    conn.setRequestProperty("User-Agent", "JavaApp/1.0");
    
    int status = conn.getResponseCode();
    if (status != 200) {
        throw new IOException("HTTP " + status + ": " + conn.getResponseMessage());
    }

    // ✅ Safe reading with charset
    try (BufferedReader in = new BufferedReader(
             new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
        
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }
} catch (IOException e) {
    System.err.println("Failed: " + e.getMessage());
}
```

---

## ⚠️ **Critical Pitfalls (Often Missed)**

### 1. **URL is Mutable and Broken**
```java
URL url = new URL("http://example.com");
url.set("http", "evil.com", 80, "/"); // ❌ Mutates URL! Deprecated but still compiles
```
✅ **Fix**: Use `URI` to build, convert to `URL` only for connection:
```java
URI uri = new URI("https", "example.com", "/path", "query", "frag");
URL url = uri.toURL(); // Immutable up to this point
```

### 2. **DNS Rebinding Attacks**
```java
new URL("http://localhost:8080/admin"); // ❌ Dangerous if host comes from user input
```
✅ **Validate host**:
```java
String host = "user-input.com";
if (!host.matches("^[a-zA-Z0-9.-]+$") || host.startsWith("-") || host.endsWith("-")) {
    throw new SecurityException("Invalid host");
}
```

### 3. **SSRF (Server-Side Request Forgery)**
Never let users control full URLs in server-side apps:
```java
// ❌ NEVER do this:
String url = request.getParameter("url");
new URL(url).openConnection(); // → Can access 169.254.169.254 (AWS metadata)
```

---

## 🧭 **URLConnection: Legacy — But When to Use It?**

| Use Case | Recommendation |
|---------|----------------|
| **Simple HTTP GET** | ✅ `URLConnection` is fine (with timeouts!) |
| **POST, headers, cookies** | ⚠️ Possible, but verbose |
| **HTTPS, redirects, auth** | ❌ Prefer `HttpClient` |
| **Non-HTTP (FTP, JAR)** | ✅ Only option |

✅ **When `URLConnection` still shines**:
- Reading from `file://`, `jar://`, `ftp://`  
- Lightweight apps where adding a dependency isn’t allowed  
- Android (where `HttpClient` was removed in API 23)

## 🚀 **The Future: `HttpClient` (Java 11+)**

Java 11 introduced `java.net.http.HttpClient` — modern, async, and secure:

### ✅ Simple GET (Blocking)
```java
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://www.tutorialspoint.com"))
    .timeout(Duration.ofSeconds(10))
    .header("User-Agent", "JavaApp/1.0")
    .build();

HttpResponse<String> response = client.send(request, 
    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

System.out.println(response.body());
```

### ✅ Async GET
```java
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println)
    .join();
```

### ✅ POST with JSON
```java
HttpRequest post = HttpRequest.newBuilder()
    .uri(URI.create("https://api.example.com/data"))
    .header("Content-Type", "application/json")
    .POST(BodyPublishers.ofString("{\"name\":\"Alice\"}"))
    .build();
```

✅ **Advantages over `URLConnection`**:
- Built-in timeouts  
- Immutable, fluent API  
- Async support (`CompletableFuture`)  
- HTTP/2 and WebSocket support  
- No `ClassCastException` for `HttpURLConnection`

## ✅ **Summary: URL Processing — The Right Way**

| Principle | Action |
|---------|--------|
| **Prefer `URI` for parsing/building** | Immutable, safe, modern |
| **Use `URL` only for `openConnection()`** | Legacy, but necessary for non-HTTP |
| **Always set timeouts** | 5s connect, 10s read |
| **Validate user-provided URLs** | Prevent SSRF, DNS rebinding |
| **Prefer `HttpClient` (Java 11+)** | For HTTP — simpler, safer, async-ready |

> 🔑 **Golden Rule**:  
> **URLs are user input — treat them like SQL queries.**  
> Validate, sanitize, and never trust.
