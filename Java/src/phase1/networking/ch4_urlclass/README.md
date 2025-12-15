# 🌐 Java URL Class Guide

## 📖 Introduction to URLs

**URL** stands for **Uniform Resource Locator** and represents a resource on the World Wide Web, such as a webpage or FTP directory. This guide shows you how to write Java programs that communicate with URLs.

### 🔗 URL Structure Breakdown

A URL can be broken down into the following parts:
```
protocol://host:port/path?query#ref
```

**Components Explained:**
- **🌐 Protocol**: Examples include HTTP, HTTPS, FTP, and File
- **🏠 Host**: Also called the authority
- **🚪 Port**: The communication endpoint (optional, defaults to protocol's standard port)
- **📁 Path**: Also referred to as the filename
- **❓ Query**: Additional parameters
- **🔗 Ref**: Fragment identifier

### 📋 URL Example

Here's a sample URL with HTTP protocol:
```
https://www.amrood.com/index.htm?language=en#j2se
```

**Note:** This URL doesn't specify a port, so it uses the default port for the protocol (HTTP defaults to port 80, HTTPS to 443).

## ☕ Java URL Class

The `URL` class is part of the `java.net` package and represents a Uniform Resource Locator. URLs are used for identifying online resources like webpages, images, videos, and files.

### 📜 Class Declaration

```java
public final class URL
   extends Object
      implements Serializable
```

## 🔧 URL Class Constructors

The `java.net.URL` class provides several constructors for creating URL objects:

| # | Constructor | Description |
|---|-------------|-------------|
| 1️⃣ | `public URL(String protocol, String host, int port, String file) throws MalformedURLException` | Creates a URL by putting together the given parts |
| 2️⃣ | `public URL(String protocol, String host, int port, String file, URLStreamHandler handler) throws MalformedURLException` | Creates a URL with the specified handler within a context |
| 3️⃣ | `public URL(String protocol, String host, String file) throws MalformedURLException` | Uses default port for the given protocol |
| 4️⃣ | `public URL(String url) throws MalformedURLException` | Creates a URL from a String |
| 5️⃣ | `public URL(URL context, String url) throws MalformedURLException` | Creates a URL by parsing URL and String arguments |
| 6️⃣ | `public URL(URL context, String url, URLStreamHandler handler) throws MalformedURLException` | Creates a URL with specified handler by parsing arguments |

## 🛠️ URL Class Methods

The URL class contains many methods for accessing various parts of a URL:

| # | Method | Description |
|---|--------|-------------|
| 1️⃣ | `public boolean equals(Object obj)` | Compares this URL for equality with another object |
| 2️⃣ | `public String getAuthority()` | Returns the authority of the URL |
| 3️⃣ | `public Object getContent()` | Returns the contents of this URL |
| 4️⃣ | `public Object getContent(Class<?>[] classes)` | Returns the contents of this URL |
| 5️⃣ | `public int getDefaultPort()` | Returns the default port for the protocol |
| 6️⃣ | `public String getFile()` | Returns the filename of the URL |
| 7️⃣ | `public String getHost()` | Returns the host of the URL |
| 8️⃣ | `public String getPath()` | Returns the path of the URL |
| 9️⃣ | `public int getPort()` | Returns the port of the URL |
| 🔟 | `public String getProtocol()` | Returns the protocol of the URL |
| 1️⃣1️⃣ | `public String getQuery()` | Returns the query part of the URL |
| 1️⃣2️⃣ | `public String getRef()` | Returns the reference part of the URL |
| 1️⃣3️⃣ | `public String getUserInfo()` | Returns the userInfo part of the URL |
| 1️⃣4️⃣ | `public int hashCode()` | Creates an integer suitable for hash table indexing |
| 1️⃣5️⃣ | `public URLConnection openConnection()` | Returns a URLConnection to the remote object |
| 1️⃣6️⃣ | `public URLConnection openConnection(Proxy proxy)` | Returns a connection through the specified proxy |
| 1️⃣7️⃣ | `public InputStream openStream()` | Opens a connection and returns an InputStream |
| 1️⃣8️⃣ | `public boolean sameFile(URL other)` | Compares two URLs, excluding fragment component |
| 1️⃣9️⃣ | `public static void setURLStreamHandlerFactory(URLStreamHandlerFactory fac)` | Sets URLStreamHandlerFactory |
| 2️⃣0️⃣ | `public String toExternalForm()` | Returns string representation of this URL |
| 2️⃣1️⃣ | `public String toString()` | Returns string representation of this URL |
| 2️⃣2️⃣ | `public String toURI()` | Returns a URI equivalent to this URL |

## 💻 Example: URL Class Usage

The following program demonstrates how to use the URL class to break down a URL into its components:

```java
// File Name: URLDemo.java
import java.io.IOException;
import java.net.URL;

public class URLDemo {
   public static void main(String[] args) {
      try {
         URL url = new URL("https://www.tutorialspoint.com/index.htm?language=en#j2se");
         
         System.out.println("URL is " + url.toString());
         System.out.println("protocol is " + url.getProtocol());
         System.out.println("authority is " + url.getAuthority());
         System.out.println("file name is " + url.getFile());
         System.out.println("host is " + url.getHost());
         System.out.println("path is " + url.getPath());
         System.out.println("port is " + url.getPort());
         System.out.println("default port is " + url.getDefaultPort());
         System.out.println("query is " + url.getQuery());
         System.out.println("ref is " + url.getRef());
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
```

### 📊 Sample Output

```
URL is https://www.tutorialspoint.com/index.htm?language=en#j2se
protocol is https
authority is www.tutorialspoint.com
file name is /index.htm?language=en
host is www.tutorialspoint.com
path is /index.htm
port is -1
default port is 443
query is language=en
ref is j2se
```

## 🚀 How to Use This Guide

1. **📚 Study the URL structure** to understand how web addresses work
2. **🔧 Explore the constructors** to learn different ways to create URL objects
3. **🛠️ Practice with methods** to extract and manipulate URL components
4. **💻 Run the example code** to see URL parsing in action
5. **🔗 Experiment** with different URLs to understand their components

## 📝 Summary

The Java `URL` class provides a comprehensive set of tools for working with Uniform Resource Locators. Whether you're building web clients, parsing URLs, or accessing network resources, this class offers all the functionality needed to handle URLs effectively in your Java applications.

**✨ Happy Coding with Java Networking! ✨**