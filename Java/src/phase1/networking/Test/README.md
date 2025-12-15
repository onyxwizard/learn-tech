# 🚀 **PRACTICAL Java Networking for Backend Developers**

## ⚡ **FORGET THEORY - HERE'S WHAT ACTUALLY MATTERS**

### **🎯 DAILY REALITY CHECK**
As a backend dev, you'll spend 80% of your time on:
1. **Making HTTP calls to other services** (REST APIs, microservices)
2. **Handling timeouts and failures**
3. **Parsing JSON/XML responses**
4. **Setting proper headers**
5. **Writing clean, maintainable network code**

Let's cut through the theory and go straight to **production-ready code**.

---

## 📞 **1. MAKING HTTP REQUESTS (90% of your work)**

### **🟢 BASIC GET REQUEST (Most Common)**
```java
// 📦 Production-ready GET request
public String callExternalApi(String url) throws IOException {
    HttpURLConnection connection = null;
    try {
        // 1️⃣ Create URL
        URL apiUrl = new URL(url);
        
        // 2️⃣ Open connection
        connection = (HttpURLConnection) apiUrl.openConnection();
        
        // 3️⃣ CRITICAL: Set timeouts (ALWAYS DO THIS)
        connection.setConnectTimeout(30000);    // 30 seconds to connect
        connection.setReadTimeout(60000);       // 60 seconds to read response
        
        // 4️⃣ Set method
        connection.setRequestMethod("GET");
        
        // 5️⃣ Set headers (Always set these)
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "YourApp/1.0");
        
        // 6️⃣ Check response code FIRST
        int status = connection.getResponseCode();
        
        if (status >= 200 && status < 300) {
            // Success - read response
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } else {
            // Error - read error stream
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                String errorResponse = br.lines().collect(Collectors.joining());
                throw new IOException("HTTP Error: " + status + " - " + errorResponse);
            }
        }
    } finally {
        // 7️⃣ ALWAYS disconnect
        if (connection != null) {
            connection.disconnect();
        }
    }
}
```

### **🔵 POST REQUEST WITH JSON (Second Most Common)**
```java
// 📦 Production-ready POST with JSON
public String postJson(String url, String jsonPayload) throws IOException {
    HttpURLConnection connection = null;
    try {
        URL apiUrl = new URL(url);
        connection = (HttpURLConnection) apiUrl.openConnection();
        
        // 🚨 CRITICAL SETTINGS
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(60000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);  // 🎯 MUST SET FOR POST
        
        // 🏷️ MUST-HAVE HEADERS FOR JSON
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "YourApp/1.0");
        
        // 📤 Write JSON payload
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // 📥 Get response
        int status = connection.getResponseCode();
        
        if (status >= 200 && status < 300) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                return br.lines().collect(Collectors.joining());
            }
        } else {
            // Handle error with details
            String errorDetails = "";
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                errorDetails = br.lines().collect(Collectors.joining());
            }
            throw new IOException("POST failed: " + status + " - " + errorDetails);
        }
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
```

### **🟣 PUT/DELETE Requests (Same pattern, different method)**
```java
// Just change the method name
connection.setRequestMethod("PUT");
// or
connection.setRequestMethod("DELETE");
```

---

## ⚡ **2. REAL-WORLD UTILITY CLASS (Copy-Paste Ready)**

```java
// 📁 File: HttpUtil.java
// 📦 Copy this to your project and use it daily

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtil {
    
    // 🎯 DEFAULT TIMEOUTS (Tune these for your needs)
    private static final int CONNECT_TIMEOUT = 30000;  // 30 seconds
    private static final int READ_TIMEOUT = 60000;     // 60 seconds
    
    /**
     * 🔄 GET request with custom headers
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = createConnection(url, "GET");
            
            // Add custom headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            
            return handleResponse(conn);
        } finally {
            disconnect(conn);
        }
    }
    
    /**
     * 🔄 POST JSON with custom headers
     */
    public static String postJson(String url, String json, Map<String, String> headers) 
            throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = createConnection(url, "POST");
            conn.setDoOutput(true);
            
            // Default JSON headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            
            // Add custom headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            
            // Write JSON body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            return handleResponse(conn);
        } finally {
            disconnect(conn);
        }
    }
    
    /**
     * 🔄 POST Form data (key=value&key2=value2)
     */
    public static String postForm(String url, Map<String, String> formData, 
                                  Map<String, String> headers) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = createConnection(url, "POST");
            conn.setDoOutput(true);
            
            // Form URL encoded
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            // Add custom headers
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            
            // Build form data
            String formBody = formData.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"))
                .collect(Collectors.joining("&"));
            
            // Write form body
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = formBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            return handleResponse(conn);
        } finally {
            disconnect(conn);
        }
    }
    
    // 🔧 PRIVATE HELPER METHODS
    
    private static HttpURLConnection createConnection(String urlString, String method) 
            throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setRequestProperty("User-Agent", "Java-HttpClient/1.0");
        return conn;
    }
    
    private static String handleResponse(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        
        InputStream inputStream = (status >= 200 && status < 300) 
            ? conn.getInputStream() 
            : conn.getErrorStream();
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, "utf-8"))) {
            String response = br.lines().collect(Collectors.joining());
            
            if (status >= 200 && status < 300) {
                return response;
            } else {
                throw new IOException("HTTP " + status + ": " + response);
            }
        }
    }
    
    private static void disconnect(HttpURLConnection conn) {
        if (conn != null) {
            conn.disconnect();
        }
    }
}
```

### **🎯 HOW TO USE THE UTILITY CLASS**
```java
// 1️⃣ Simple GET
String response = HttpUtil.get("https://api.example.com/users", null);

// 2️⃣ GET with headers
Map<String, String> headers = new HashMap<>();
headers.put("Authorization", "Bearer " + token);
headers.put("X-Client-ID", "your-client-id");
String response = HttpUtil.get("https://api.example.com/data", headers);

// 3️⃣ POST JSON
String json = "{\"name\":\"John\", \"email\":\"john@example.com\"}";
String response = HttpUtil.postJson("https://api.example.com/users", json, headers);

// 4️⃣ POST Form data
Map<String, String> formData = new HashMap<>();
formData.put("username", "john");
formData.put("password", "secret");
String response = HttpUtil.postForm("https://api.example.com/login", formData, null);
```

---

## ⏱️ **3. HANDLING TIMEOUTS (Production Critical)**

### **🎯 THE 3 TIMEOUTS YOU MUST SET**

```java
// ⚡ PRODUCTION SETTINGS - NEVER CHANGE THESE
connection.setConnectTimeout(30000);    // 30 seconds to establish connection
connection.setReadTimeout(60000);       // 60 seconds to wait for response
connection.setInstanceFollowRedirects(true);  // Allow redirects

// 🎯 For database or slow services
connection.setConnectTimeout(45000);    // 45 seconds
connection.setReadTimeout(120000);      // 2 minutes

// 🎯 For fast internal microservices
connection.setConnectTimeout(5000);     // 5 seconds
connection.setReadTimeout(10000);       // 10 seconds
```

### **⚠️ HANDLING TIMEOUT ERRORS**
```java
try {
    String response = callExternalApi(url);
} catch (SocketTimeoutException e) {
    // ⏰ Connection or read timed out
    log.error("Timeout calling {}: {}", url, e.getMessage());
    throw new ServiceTimeoutException("Service unavailable");
} catch (ConnectException e) {
    // 🔌 Cannot connect (server down, network issue)
    log.error("Cannot connect to {}: {}", url, e.getMessage());
    throw new ServiceUnavailableException("Service offline");
} catch (IOException e) {
    // 📡 Other network errors
    log.error("Network error for {}: {}", url, e.getMessage());
    throw new NetworkException("Network failure");
}
```

---

## 🔄 **4. RETRY LOGIC (Production Must-Have)**

```java
// 📦 Production-ready retry with exponential backoff
public String callWithRetry(String url, int maxRetries) {
    int retryCount = 0;
    long waitTime = 1000;  // Start with 1 second
    
    while (retryCount <= maxRetries) {
        try {
            return callExternalApi(url);
            
        } catch (SocketTimeoutException | ConnectException e) {
            retryCount++;
            
            if (retryCount > maxRetries) {
                log.error("Max retries exceeded for {}", url);
                throw new ServiceUnavailableException("Service failed after retries");
            }
            
            // ⏳ Exponential backoff
            waitTime *= 2;  // Double wait time each retry
            log.warn("Retry {}/{} for {} - waiting {}ms", 
                     retryCount, maxRetries, url, waitTime);
            
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Retry interrupted");
            }
        } catch (IOException e) {
            // Don't retry for other IOExceptions
            throw new RuntimeException("Non-retryable error", e);
        }
    }
    
    throw new ServiceUnavailableException("Service unavailable");
}
```

---

## 📊 **5. LOGGING & MONITORING (Production Must)**

```java
// 📝 Always log network calls
public String callApiWithLogging(String url, String method, String body) {
    long startTime = System.currentTimeMillis();
    
    try {
        log.info("🚀 Calling {} {} [Request: {}]", method, url, 
                 body != null ? body.substring(0, Math.min(100, body.length())) : "empty");
        
        String response = makeHttpCall(url, method, body);
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("✅ {} {} completed in {}ms [Response: {}]", 
                 method, url, duration,
                 response.substring(0, Math.min(100, response.length())));
        
        // 📈 Send to metrics (Prometheus, etc.)
        metrics.timer("http_request_duration", duration);
        metrics.counter("http_request_success", 1);
        
        return response;
        
    } catch (IOException e) {
        long duration = System.currentTimeMillis() - startTime;
        log.error("❌ {} {} failed in {}ms: {}", 
                  method, url, duration, e.getMessage());
        
        // 📈 Error metrics
        metrics.counter("http_request_error", 1);
        
        throw new RuntimeException("API call failed", e);
    }
}
```

---

## 🔐 **6. SECURITY (Production Critical)**

### **🔒 HTTPS Only**
```java
// ALWAYS use HTTPS for external calls
String url = "https://api.example.com";  // ✅
// String url = "http://api.example.com"; // ❌ NEVER in production
```

### **🔑 Authentication Headers**
```java
// 1️⃣ Bearer Token (Most Common)
connection.setRequestProperty("Authorization", "Bearer " + accessToken);

// 2️⃣ Basic Auth
String auth = username + ":" + password;
String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

// 3️⃣ API Key
connection.setRequestProperty("X-API-Key", apiKey);
```

### **🛡️ SSL/TLS Configuration (Production)**
```java
// For production, you might need to configure SSL context
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustAllCerts, new SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

// 🚨 WARNING: Don't disable certificate validation in production!
// Only for testing/development
```

---

## 📦 **7. JSON PARSING (99% of APIs use JSON)**

```java
// 🎯 Using Jackson (Industry Standard)
ObjectMapper mapper = new ObjectMapper();

// Parse response
String jsonResponse = callExternalApi(url);
User user = mapper.readValue(jsonResponse, User.class);

// Create request JSON
String jsonRequest = mapper.writeValueAsString(requestObject);

// 🎯 Using Gson (Google's library)
Gson gson = new Gson();
User user = gson.fromJson(jsonResponse, User.class);
String jsonRequest = gson.toJson(requestObject);
```

---

## 🏗️ **8. MICROSERVICES COMMUNICATION**

### **🎯 Service-to-Service Call Pattern**
```java
@Service
public class UserServiceClient {
    
    private final String userServiceUrl;
    private final ObjectMapper mapper;
    
    public User getUserById(Long userId) {
        try {
            String url = userServiceUrl + "/users/" + userId;
            String response = HttpUtil.get(url, getDefaultHeaders());
            return mapper.readValue(response, User.class);
        } catch (IOException e) {
            throw new ServiceException("Failed to fetch user", e);
        }
    }
    
    public User createUser(CreateUserRequest request) {
        try {
            String url = userServiceUrl + "/users";
            String jsonRequest = mapper.writeValueAsString(request);
            String response = HttpUtil.postJson(url, jsonRequest, getDefaultHeaders());
            return mapper.readValue(response, User.class);
        } catch (IOException e) {
            throw new ServiceException("Failed to create user", e);
        }
    }
    
    private Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("X-Service-Name", "your-service-name");
        headers.put("X-Request-ID", generateRequestId());
        return headers;
    }
}
```

---

## 🎯 **QUICK REFERENCE - DAILY USE**

### **📋 What to Always Include:**
```java
// ✅ ALWAYS DO THESE
1. Set timeouts (connect & read)
2. Set Content-Type and Accept headers
3. Handle response codes (200-299 = success)
4. Close connections (use try-with-resources)
5. Log requests/responses
6. Implement retry logic for transient failures
7. Use HTTPS only
```

### **📋 Common Headers You'll Need:**
```java
connection.setRequestProperty("Content-Type", "application/json");
connection.setRequestProperty("Accept", "application/json");
connection.setRequestProperty("Authorization", "Bearer " + token);
connection.setRequestProperty("User-Agent", "YourApp/1.0");
connection.setRequestProperty("X-Request-ID", UUID.randomUUID().toString());
connection.setRequestProperty("X-Client-ID", "your-client-id");
```

### **📋 Response Codes to Handle:**
```java
// Success
200 OK            // GET/PUT success
201 Created       // POST success
204 No Content    // DELETE success

// Client Errors (Your fault)
400 Bad Request   // Invalid request
401 Unauthorized  // Missing/invalid auth
403 Forbidden     // No permission
404 Not Found     // Resource doesn't exist
429 Too Many Requests // Rate limited

// Server Errors (Their fault)
500 Internal Server Error
502 Bad Gateway
503 Service Unavailable
504 Gateway Timeout
```

---

## 🚀 **FINAL PRODUCTION-READY TEMPLATE**

```java
// 📁 File: ApiClient.java
// 🎯 Copy this and customize for your needs

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ApiClient {
    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private final String baseUrl;
    private final String apiKey;
    
    public ApiClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }
    
    public <T> T get(String endpoint, Class<T> responseType) {
        return call("GET", endpoint, null, responseType);
    }
    
    public <T> T post(String endpoint, Object request, Class<T> responseType) {
        return call("POST", endpoint, request, responseType);
    }
    
    public <T> T put(String endpoint, Object request, Class<T> responseType) {
        return call("PUT", endpoint, request, responseType);
    }
    
    public void delete(String endpoint) {
        call("DELETE", endpoint, null, Void.class);
    }
    
    private <T> T call(String method, String endpoint, Object request, 
                       Class<T> responseType) {
        String url = baseUrl + endpoint;
        String requestId = UUID.randomUUID().toString();
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Build request
            String requestBody = request != null ? 
                mapper.writeValueAsString(request) : null;
            
            log.info("🌐 {} {} [ID: {}]", method, url, requestId);
            
            // Make HTTP call
            String responseJson = makeHttpCall(method, url, requestBody, requestId);
            
            // Parse response
            T result = responseType != Void.class ? 
                mapper.readValue(responseJson, responseType) : null;
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ {} {} completed in {}ms [ID: {}]", 
                     method, url, duration, requestId);
            
            return result;
            
        } catch (SocketTimeoutException e) {
            log.error("⏰ Timeout {} {} [ID: {}]: {}", 
                      method, url, requestId, e.getMessage());
            throw new ServiceTimeoutException("Service timeout");
            
        } catch (IOException e) {
            log.error("❌ Failed {} {} [ID: {}]: {}", 
                      method, url, requestId, e.getMessage());
            throw new ServiceException("API call failed", e);
        }
    }
    
    private String makeHttpCall(String method, String url, String body, 
                                String requestId) throws IOException {
        HttpURLConnection conn = null;
        try {
            URL apiUrl = new URL(url);
            conn = (HttpURLConnection) apiUrl.openConnection();
            
            // Essential settings
            conn.setRequestMethod(method);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(60000);
            
            // Headers
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");
            headers.put("X-API-Key", apiKey);
            headers.put("X-Request-ID", requestId);
            headers.put("User-Agent", "ApiClient/1.0");
            
            for (Map.Entry<String, String> header : headers.entrySet()) {
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
            
            // Write body for POST/PUT
            if (body != null && (method.equals("POST") || method.equals("PUT"))) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }
            
            // Get response
            int status = conn.getResponseCode();
            InputStream inputStream = (status >= 200 && status < 300) 
                ? conn.getInputStream() 
                : conn.getErrorStream();
            
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(inputStream, "utf-8"))) {
                String response = br.lines().collect(Collectors.joining());
                
                if (status >= 200 && status < 300) {
                    return response;
                } else {
                    throw new IOException("HTTP " + status + ": " + response);
                }
            }
            
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
```

---

## 🎯 **YOUR DAILY CHECKLIST**

1. **✅ Use the ApiClient template above**
2. **✅ Always set timeouts (30s connect, 60s read)**
3. **✅ Always add Content-Type and Accept headers**
4. **✅ Always check response codes**
5. **✅ Always log requests with request ID**
6. **✅ Always handle SocketTimeoutException**
7. **✅ Use HTTPS only**
8. **✅ Parse JSON with Jackson/Gson**
9. **✅ Implement retry for transient failures**
10. **✅ Add metrics for monitoring**

---

**🚀 THAT'S IT!** Copy these code templates, understand the patterns, and you'll handle 95% of networking tasks as a Java backend developer. No theory, just practical, production-ready code.