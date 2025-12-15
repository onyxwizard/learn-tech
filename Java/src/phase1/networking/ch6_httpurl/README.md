# 🔗 Java HttpURLConnection Class Guide

## 📚 Introduction

**HttpURLConnection** is an abstract class in the `java.net` package that represents an HTTP-specific URL connection. This class allows you to both read from and write to resources referenced by HTTP URLs, making it essential for HTTP communication in Java applications.

### 🔄 Getting an HttpURLConnection Instance

When you connect to a URL with an HTTP protocol, the `URL.openConnection()` method returns an `HttpURLConnection` object:

```java
URL url = new URL("https://example.com");
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
```

## 🚀 Steps to Make an HTTP Connection

Follow these steps to establish a connection and process HTTP resources:

1️⃣ **Get Connection Object**  
   Invoke `URL.openConnection()` to get an `HttpURLConnection` object

2️⃣ **Configure Connection**  
   Update setup parameters and request properties using setter methods

3️⃣ **Establish Connection**  
   Create a connection to the remote object using the `connect()` method

4️⃣ **Access Content**  
   Once connected, access the content and headers of the remote resource

## 📜 Class Declaration

```java
public abstract class HttpURLConnection
   extends URLConnection
```

## 🏷️ HttpURLConnection Class Fields

### 🎛️ Configuration Fields

| # | Field | Description |
|---|--------|-------------|
| 1 | `protected int chunkLength` | Chunk-length for chunked encoding streaming mode |
| 2 | `protected int fixedContentLength` | Fixed content-length for fixed-length streaming mode |
| 3 | `protected long fixedContentLengthLong` | Fixed content-length (long version) |
| 4 | `protected boolean instanceFollowRedirects` | If true, automatically follows redirects |
| 5 | `protected String method` | HTTP method (GET, POST, PUT, etc.) |
| 6 | `protected int responseCode` | Three-digit HTTP Status-Code |
| 7 | `protected String responseMessage` | HTTP response message |

### 🌐 HTTP Status Code Constants

The class provides constants for common HTTP status codes:

| Status Category | Code | Constant | Description |
|-----------------|------|----------|-------------|
| **2xx Success** | 200 | `HTTP_OK` | Request succeeded |
| | 201 | `HTTP_CREATED` | Resource created |
| | 202 | `HTTP_ACCEPTED` | Request accepted for processing |
| | 203 | `HTTP_NOT_AUTHORITATIVE` | Non-authoritative information |
| | 204 | `HTTP_NO_CONTENT` | No content to return |
| | 205 | `HTTP_RESET` | Reset content |
| | 206 | `HTTP_PARTIAL` | Partial content |
| **3xx Redirection** | 300 | `HTTP_MULT_CHOICE` | Multiple choices |
| | 301 | `HTTP_MOVED_PERM` | Moved permanently |
| | 302 | `HTTP_MOVED_TEMP` | Temporary redirect |
| | 303 | `HTTP_SEE_OTHER` | See other |
| | 304 | `HTTP_NOT_MODIFIED` | Not modified |
| | 305 | `HTTP_USE_PROXY` | Use proxy |
| **4xx Client Errors** | 400 | `HTTP_BAD_REQUEST` | Bad request |
| | 401 | `HTTP_UNAUTHORIZED` | Unauthorized |
| | 402 | `HTTP_PAYMENT_REQUIRED` | Payment required |
| | 403 | `HTTP_FORBIDDEN` | Forbidden |
| | 404 | `HTTP_NOT_FOUND` | Not found |
| | 405 | `HTTP_BAD_METHOD` | Method not allowed |
| | 406 | `HTTP_NOT_ACCEPTABLE` | Not acceptable |
| | 407 | `HTTP_PROXY_AUTH` | Proxy authentication required |
| | 408 | `HTTP_CLIENT_TIMEOUT` | Request timeout |
| | 409 | `HTTP_CONFLICT` | Conflict |
| | 410 | `HTTP_GONE` | Gone |
| | 411 | `HTTP_LENGTH_REQUIRED` | Length required |
| | 412 | `HTTP_PRECON_FAILED` | Precondition failed |
| | 413 | `HTTP_ENTITY_TOO_LARGE` | Request entity too large |
| | 414 | `HTTP_REQ_TOO_LONG` | Request-URI too large |
| | 415 | `HTTP_UNSUPPORTED_TYPE` | Unsupported media type |
| **5xx Server Errors** | 500 | `HTTP_INTERNAL_ERROR` | Internal server error |
| | 501 | `HTTP_NOT_IMPLEMENTED` | Not implemented |
| | 502 | `HTTP_BAD_GATEWAY` | Bad gateway |
| | 503 | `HTTP_UNAVAILABLE` | Service unavailable |
| | 504 | `HTTP_GATEWAY_TIMEOUT` | Gateway timeout |
| | 505 | `HTTP_VERSION` | HTTP version not supported |

## 🛠️ HttpURLConnection Class Methods

### 🔧 Connection Management Methods

| # | Method | Description |
|---|--------|-------------|
| 1 | `abstract void disconnect()` | Indicates that other requests are unlikely soon |
| 2 | `abstract boolean usingProxy()` | Checks if connection goes through a proxy |

### 📥 Data Retrieval Methods

| # | Method | Description |
|---|--------|-------------|
| 3 | `InputStream getErrorStream()` | Returns error stream if connection failed |
| 4 | `String getHeaderField(int n)` | Returns value for nth header field |
| 5 | `String getHeaderFieldKey(int n)` | Returns key for nth header field |
| 6 | `int getResponseCode()` | Gets HTTP status code |
| 7 | `String getResponseMessage()` | Gets HTTP response message |

### ⚙️ Configuration Methods

| # | Method | Description |
|---|--------|-------------|
| 8 | `String getRequestMethod()` | Gets the request method |
| 9 | `void setRequestMethod(String method)` | Sets request method (GET, POST, etc.) |
| 10 | `Permission getPermission()` | Returns SocketPermission for connection |
| 11 | `void setAuthenticator(Authenticator auth)` | Sets authenticator for HTTP auth |

### 🔀 Redirection Control Methods

| # | Method | Description |
|---|--------|-------------|
| 12 | `static boolean getFollowRedirects()` | Checks if redirects are followed automatically |
| 13 | `boolean getInstanceFollowRedirects()` | Checks instance-specific redirect setting |
| 14 | `static void setFollowRedirects(boolean set)` | Sets global redirect following |
| 15 | `void setInstanceFollowRedirects(boolean follow)` | Sets instance-specific redirect following |

### 📤 Streaming Methods

| # | Method | Description |
|---|--------|-------------|
| 16 | `void setChunkedStreamingMode(int chunklen)` | Enables chunked streaming (unknown content length) |
| 17 | `void setFixedLengthStreamingMode(int contentLength)` | Enables fixed-length streaming |
| 18 | `void setFixedLengthStreamingMode(long contentLength)` | Enables fixed-length streaming (long) |

## 📋 Inheritance Hierarchy

```
java.lang.Object
    ↳ java.net.URLConnection
        ↳ java.net.HttpURLConnection
```

## 💻 Example: Using HttpURLConnection

The following example demonstrates how to connect to an HTTP URL and read its content:

```java
package com.tutorialspoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUrlConnectionDemo {
   public static void main(String[] args) {
      try {
         // Step 1: Create URL object
         URL url = new URL("https://www.tutorialspoint.com");
         
         // Step 2: Open connection
         URLConnection urlConnection = url.openConnection();
         HttpURLConnection connection = null;
         
         // Step 3: Verify it's an HTTP connection
         if(urlConnection instanceof HttpURLConnection) {
            connection = (HttpURLConnection) urlConnection;
         } else {
            System.out.println("⚠️ Please enter an HTTP URL.");
            return;
         }
         
         // Step 4: Read the response
         BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
         
         StringBuilder urlString = new StringBuilder();
         String current;
         
         while((current = in.readLine()) != null) {
            urlString.append(current);
         }
         
         // Step 5: Display the content
         System.out.println(urlString.toString());
         
         // Step 6: Get response info
         System.out.println("\n📊 Response Code: " + connection.getResponseCode());
         System.out.println("📝 Response Message: " + connection.getResponseMessage());
         
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
```

### 📊 Sample Output

```
$ java HttpURLConnectionDemo

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to Tutorialspoint</title>
    <!-- ... full HTML content ... -->
</body>
</html>

📊 Response Code: 200
📝 Response Message: OK
```

## 🚀 Advanced Usage Examples

### 📤 Making a POST Request

```java
HttpURLConnection connection = (HttpURLConnection) url.openConnection();
connection.setRequestMethod("POST");
connection.setDoOutput(true);
connection.setRequestProperty("Content-Type", "application/json");

// Write JSON data
try(OutputStream os = connection.getOutputStream()) {
    String jsonInput = "{\"name\":\"John\", \"age\":30}";
    os.write(jsonInput.getBytes());
    os.flush();
}

// Read response
int responseCode = connection.getResponseCode();
```

### ⏱️ Setting Timeouts

```java
connection.setConnectTimeout(5000); // 5 seconds
connection.setReadTimeout(10000);   // 10 seconds
```

### 📁 Handling Headers

```java
// Set custom headers
connection.setRequestProperty("User-Agent", "MyJavaApp/1.0");
connection.setRequestProperty("Accept", "application/json");

// Read response headers
Map<String, List<String>> headers = connection.getHeaderFields();
String contentType = connection.getHeaderField("Content-Type");
```

## 📝 Best Practices

1. **🔒 Always Close Connections**  
   Use try-with-resources or finally blocks to ensure connections are closed

2. **⚡ Handle Timeouts**  
   Always set appropriate connect and read timeouts

3. **📊 Check Response Codes**  
   Verify HTTP status codes before processing responses

4. **💾 Release Resources**  
   Close input/output streams to free system resources

5. **🔄 Handle Redirects**  
   Configure redirect behavior based on your application needs

6. **🛡️ Use HTTPS**  
   Prefer HTTPS URLs for secure communication

## 🚨 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| **Connection timeout** | Increase timeout values or check network connectivity |
| **HTTP 404 Not Found** | Verify the URL is correct |
| **HTTP 401 Unauthorized** | Add proper authentication headers |
| **SSL/TLS issues** | Ensure proper SSL certificates are installed |
| **Slow performance** | Use connection pooling or implement caching |

**🌐 Happy HTTP Programming with Java! 🌐**