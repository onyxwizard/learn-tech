# 🧠 Java Backend Developer Daily Reference Sheet

## 🌐 **NETWORKING ESSENTIALS**

### 🔗 **URL Processing (Daily Use)**
```java
// 🌟 Create and parse URLs
URL url = new URL("https://api.example.com/users?page=1");
String protocol = url.getProtocol();    // "https"
String host = url.getHost();            // "api.example.com"
String path = url.getPath();            // "/users"
String query = url.getQuery();          // "page=1"
int port = url.getPort();               // -1 (default)
int defaultPort = url.getDefaultPort(); // 443
```

### 📡 **HTTP Communication (Daily Use)**
```java
// 🌟 Make HTTP requests
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setRequestMethod("GET");
connection.setConnectTimeout(5000);
connection.setReadTimeout(10000);
connection.setRequestProperty("Content-Type", "application/json");
connection.setRequestProperty("Authorization", "Bearer token");

int status = connection.getResponseCode();  // 🎯 Check this FIRST
if (status == 200) {
    try (BufferedReader in = new BufferedReader(
         new InputStreamReader(connection.getInputStream()))) {
        String response = in.lines().collect(Collectors.joining());
    }
} else {
    // Handle errors
}
connection.disconnect();
```

### 🔌 **Socket Communication (WebSocket/API Gateway)**
```java
// Client-side
Socket socket = new Socket("hostname", port);
socket.setSoTimeout(5000);

// Server-side (typically in separate thread)
ServerSocket serverSocket = new ServerSocket(8080);
while (running) {
    Socket clientSocket = serverSocket.accept();
    new Thread(() -> handleClient(clientSocket)).start();
}
```

### 🏗️ **Key Methods to Memorize**

| Category | Method | Purpose | Daily Use |
|----------|--------|---------|-----------|
| **URL** | `URL(url)` | Create URL object | ✅ High |
| | `getProtocol()` | Get protocol (http/https) | ✅ High |
| | `getHost()` | Get hostname | ✅ High |
| | `getPath()` | Get API endpoint path | ✅ High |
| **HttpURLConnection** | `openConnection()` | Open HTTP connection | ✅ High |
| | `setRequestMethod()` | GET/POST/PUT/DELETE | ✅ High |
| | `setRequestProperty()` | Set headers | ✅ High |
| | `getResponseCode()` | Get HTTP status | ✅ High |
| | `getInputStream()` | Read response | ✅ High |
| | `getOutputStream()` | Write request body | ✅ High |
| **Socket** | `getInputStream()` | Read from socket | 🟡 Medium |
| | `getOutputStream()` | Write to socket | 🟡 Medium |
| | `setSoTimeout()` | Set timeout | 🟡 Medium |

---

## 🔤 **GENERICS (Daily Use in Collections)**

### 📦 **Generic Collections (ALWAYS USE THESE)**
```java
// ✅ DO THIS - Type Safe
List<String> users = new ArrayList<>();
Map<Integer, User> userMap = new HashMap<>();
Set<String> uniqueEmails = new HashSet<>();

// ❌ NEVER DO THIS - Raw Types
List rawList = new ArrayList();  // Causes ClassCastException!
```

### 🛠️ **Generic Methods (Utility Classes)**
```java
// 🌟 Create reusable utility methods
public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    return list.stream().filter(predicate).collect(Collectors.toList());
}

// 🌟 Safe type conversion
public static <T> T safeCast(Object obj, Class<T> clazz) {
    return clazz.isInstance(obj) ? clazz.cast(obj) : null;
}
```

### 🏗️ **Generic Classes (DTOs/Entities)**
```java
// 🌟 Response wrapper for APIs
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;  // Generic payload
    
    // Builder pattern with generics
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }
}
```

### 📝 **Daily Use Patterns**

#### 1. **REST API Response Pattern**
```java
// Generic API response
public class ResponseEntity<T> {
    private HttpStatus status;
    private T body;
    
    public static <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(HttpStatus.OK, body);
    }
}

// Usage in Controller
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    return ResponseEntity.ok(user);
}
```

#### 2. **Service Layer Pattern**
```java
// Generic CRUD Service
public interface CrudService<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(ID id);
}

// Implementation
@Service
public class UserService implements CrudService<User, Long> {
    // Type-safe implementations
}
```

#### 3. **Repository Pattern**
```java
// Generic Repository
@Repository
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findByCreatedAtAfter(Date date);
}
```

---

## 🎯 **DAILY MUST-KNOW CONCEPTS**

### 1. **HTTP Status Codes (MUST MEMORIZE)**
```java
// Success
200 OK           // GET success
201 Created      // POST success
204 No Content   // DELETE success

// Client Errors
400 Bad Request          // Invalid request
401 Unauthorized         // Missing/wrong auth
403 Forbidden            // No permission
404 Not Found            // Resource doesn't exist

// Server Errors
500 Internal Server Error // Server messed up
502 Bad Gateway          // Upstream server error
503 Service Unavailable  // Server overloaded
```

### 2. **Content-Type Headers (MUST KNOW)**
```java
// Set these in your APIs
connection.setRequestProperty("Content-Type", "application/json");
connection.setRequestProperty("Accept", "application/json");
connection.setRequestProperty("Authorization", "Bearer " + token);

// Common content types:
// - application/json
// - application/x-www-form-urlencoded
// - multipart/form-data (file uploads)
```

### 3. **Timeouts (PRODUCTION CRITICAL)**
```java
// ALWAYS SET THESE
connection.setConnectTimeout(30000);    // 30 seconds to connect
connection.setReadTimeout(60000);       // 60 seconds to read response
socket.setSoTimeout(30000);             // 30 seconds socket timeout
```

### 4. **Resource Management (CRITICAL)**
```java
// ✅ DO THIS - Auto-close with try-with-resources
try (Socket socket = new Socket(host, port);
     BufferedReader reader = new BufferedReader(
         new InputStreamReader(socket.getInputStream()))) {
    // Use resources
}  // Auto-closed

// ✅ DO THIS - HTTP connection
HttpURLConnection connection = null;
try {
    connection = (HttpURLConnection) url.openConnection();
    // Use connection
} finally {
    if (connection != null) {
        connection.disconnect();
    }
}
```

---

## 🚀 **QUICK REFERENCE CARD**

### **For HTTP APIs (Daily Use)**
```java
// 1. Create URL
URL url = new URL("https://api.example.com/endpoint");

// 2. Open connection
HttpURLConnection conn = (HttpURLConnection) url.openConnection();

// 3. Configure
conn.setRequestMethod("GET");
conn.setConnectTimeout(5000);
conn.setReadTimeout(10000);
conn.setRequestProperty("Accept", "application/json");

// 4. Handle response
int status = conn.getResponseCode();
if (status >= 200 && status < 300) {
    try (BufferedReader in = new BufferedReader(
         new InputStreamReader(conn.getInputStream()))) {
        String response = in.lines().collect(Collectors.joining());
        // Parse JSON response
    }
} else {
    // Error handling
}

// 5. Clean up
conn.disconnect();
```

### **For Generic Collections (Always)**
```java
// ✅ Type-safe collections
List<String> names = new ArrayList<>();
Map<String, Object> config = new HashMap<>();
Set<Integer> ids = new HashSet<>();

// ✅ Generic methods
public <T> T parseJson(String json, Class<T> clazz) {
    return objectMapper.readValue(json, clazz);
}

// ✅ Generic DTOs
public class Response<T> {
    private T data;
    private String error;
}
```

### **For Error Handling (Production Ready)**
```java
try {
    // Network call
} catch (SocketTimeoutException e) {
    log.error("Connection timeout: {}", e.getMessage());
    throw new ServiceUnavailableException("Service timeout");
} catch (IOException e) {
    log.error("IO Error: {}", e.getMessage());
    throw new ServiceException("Network error");
} finally {
    // Always clean up
}
```

---

## 📋 **DAILY CHECKLIST**

### **Before Making HTTP Calls:**
- [ ] Set proper timeouts
- [ ] Add required headers
- [ ] Handle SSL/TLS (use HTTPS)
- [ ] Implement retry logic
- [ ] Add circuit breaker pattern

### **When Using Collections:**
- [ ] Always use generics (`List<String>` not `List`)
- [ ] Specify capacity for large collections
- [ ] Use immutable collections when possible
- [ ] Consider thread safety (ConcurrentHashMap)

### **For API Design:**
- [ ] Use generic response wrappers
- [ ] Implement proper error handling
- [ ] Add pagination with generics
- [ ] Use DTOs with type safety

---

## 🎓 **PRO TIPS FOR DAILY WORK**

1. **📦 Always use `List<String>` instead of raw `List`**
2. **⏱️ Always set timeouts in production code**
3. **🔐 Always use HTTPS for external calls**
4. **📊 Always check HTTP status codes before processing response**
5. **🧹 Always close connections in finally block**
6. **🎯 Use generic `ResponseEntity<T>` for REST APIs**
7. **🔄 Implement retry with exponential backoff for network calls**
8. **🚨 Log all network errors with context**
9. **📈 Monitor HTTP status codes in your metrics**
10. **🛡️ Validate all external inputs (URLs, headers, responses)**

---

## 💡 **MEMORY AIDS**

### **HTTP Status Code Groups:**
- **2xx** = ✅ Success (Good to go!)
- **3xx** = 🔄 Redirect (Go somewhere else)
- **4xx** = 🚫 Client Error (You messed up)
- **5xx** = 🔥 Server Error (They messed up)

### **Generic Syntax:**
- `<T>` = "Type parameter"
- `extends` = "Can be this type or subclass"
- `?` = "Unknown type" (wildcard)
- `List<String>` = "List of Strings" (not just List!)

### **Network Flow:**
```
URL → Connection → Headers → Send → Wait → Status → Read → Close
```

---

**🎯 Remember: In backend development, networking is your bread 🍞 and generics are your butter 🧈. Master these two, and you'll build robust, type-safe, scalable applications!**