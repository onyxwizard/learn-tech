# 🔌 Java Socket Class Guide

## 🌐 Introduction to Sockets

**Sockets** provide the communication mechanism between two computers using **TCP (Transmission Control Protocol)**. They enable reliable, bidirectional communication between client and server applications.

### 🔄 How Socket Communication Works

1. **👤 Client Program**  
   - Creates a socket on its end of the communication
   - Attempts to connect that socket to a server

2. **🖥️ Server Program**  
   - When connection is made, creates a socket object on its end
   - Listens for incoming client connections

3. **💬 Communication**  
   - Client and server communicate by writing to and reading from sockets
   - Uses I/O streams for data exchange

### 📊 Socket Communication Flow

```
Client                            Server
  │                                  │
  ├── Create Socket ────────────────▶│
  │                                  │
  ├── Connect to Server ────────────▶│
  │                                  │
  ├── Write to OutputStream ────────▶│ (Server's InputStream)
  │                                  │
  ├── Read from InputStream ◀────────┤ (Server's OutputStream)
  │                                  │
  └── Close Socket ─────────────────▶│
```

## 📦 Java Socket Classes

| Class | Purpose |
|-------|---------|
| **`java.net.Socket`** | Represents a socket for both client and server communication |
| **`java.net.ServerSocket`** | Provides mechanism for server to listen for clients and establish connections |

## 📜 Socket Class Declaration

```java
public class Socket
   extends Object
      implements Closeable
```

## 🔧 Socket Class Constructors

| # | Constructor | Description |
|---|-------------|-------------|
| 1️⃣ | `public Socket()` | Creates an unconnected socket with system-default SocketImpl |
| 2️⃣ | `public Socket(String host, int port) throws UnknownHostException, IOException` | Connects to specified server at specified port |
| 3️⃣ | `public Socket(String host, int port, InetAddress localAddr, int localPort) throws IOException` | Creates socket connected to remote host, binds to local address/port |
| 4️⃣ | `public Socket(InetAddress host, int port) throws IOException` | Same as #2, but host is InetAddress object |
| 5️⃣ | `public Socket(InetAddress host, int port, InetAddress localAddress, int localPort) throws IOException` | Same as #3, but host is InetAddress object |
| 6️⃣ | `public Socket(Proxy proxy)` | Creates unconnected socket with specified proxy settings |

### ⚡ Important Note
When Socket constructors return, they don't just instantiate a Socket object – they actually attempt to connect to the specified server and port!

## 🛠️ Socket Class Methods

### 🔗 Connection Management

| # | Method | Description |
|---|--------|-------------|
| 1 | `void bind(SocketAddress bindpoint)` | Binds socket to local address |
| 2 | `void close()` | Closes the socket |
| 3 | `void connect(SocketAddress endpoint)` | Connects socket to server |
| 4 | `void connect(SocketAddress endpoint, int timeout)` | Connects with specified timeout |

### 🌐 Address Information

| # | Method | Description |
|---|--------|-------------|
| 5 | `InetAddress getInetAddress()` | Returns address socket is connected to |
| 6 | `InetAddress getLocalAddress()` | Gets local address socket is bound to |
| 7 | `int getLocalPort()` | Returns local port number |
| 8 | `int getPort()` | Returns remote port number |
| 9 | `SocketAddress getLocalSocketAddress()` | Returns local endpoint address |
| 10 | `SocketAddress getRemoteSocketAddress()` | Returns remote endpoint address |

### 📥📤 Stream Operations

| # | Method | Description |
|---|--------|-------------|
| 11 | `InputStream getInputStream()` | Returns input stream for socket |
| 12 | `OutputStream getOutputStream()` | Returns output stream for socket |
| 13 | `void shutdownInput()` | Places input stream at "end of stream" |
| 14 | `void shutdownOutput()` | Disables output stream for socket |

### ⚙️ Configuration & Options

| # | Method | Description |
|---|--------|-------------|
| 15 | `boolean getKeepAlive()` | Tests if SO_KEEPALIVE is enabled |
| 16 | `boolean getOOBInline()` | Tests if SO_OOBINLINE is enabled |
| 17 | `boolean getReuseAddress()` | Tests if SO_REUSEADDR is enabled |
| 18 | `boolean getTcpNoDelay()` | Tests if TCP_NODELAY is enabled |
| 19 | `int getSoTimeout()` | Returns SO_TIMEOUT setting |
| 20 | `void setKeepAlive(boolean on)` | Enable/disable SO_KEEPALIVE |
| 21 | `void setOOBInline(boolean on)` | Enable/disable SO_OOBINLINE |
| 22 | `void setReuseAddress(boolean on)` | Enable/disable SO_REUSEADDR |
| 23 | `void setSoTimeout(int timeout)` | Set SO_TIMEOUT (milliseconds) |
| 24 | `void setTcpNoDelay(boolean on)` | Enable/disable TCP_NODELAY |

### 📊 Buffer & Performance

| # | Method | Description |
|---|--------|-------------|
| 25 | `int getReceiveBufferSize()` | Gets SO_RCVBUF option value |
| 26 | `int getSendBufferSize()` | Gets SO_SNDBUF option value |
| 27 | `void setReceiveBufferSize(int size)` | Sets SO_RCVBUF option |
| 28 | `void setSendBufferSize(int size)` | Sets SO_SNDBUF option |
| 29 | `void setPerformancePreferences(int connectionTime, int latency, int bandwidth)` | Sets performance preferences |

### 🔍 Status Checking

| # | Method | Description |
|---|--------|-------------|
| 30 | `boolean isBound()` | Returns binding state |
| 31 | `boolean isClosed()` | Returns closed state |
| 32 | `boolean isConnected()` | Returns connection state |
| 33 | `boolean isInputShutdown()` | Checks if read-half is closed |
| 34 | `boolean isOutputShutdown()` | Checks if write-half is closed |

## 💻 Socket Client Example

Here's a complete client program that connects to a server and exchanges greetings:

```java
package com.tutorialspoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class GreetingClient {

   public static void main(String[] args) {
      // Get server and port from command line arguments
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      
      try {
         System.out.println("🔌 Connecting to " + serverName + " on port " + port);
         
         // Create socket connection
         Socket client = new Socket(serverName, port);
         System.out.println("✅ Just connected to " + client.getRemoteSocketAddress());
         
         // Send greeting to server
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         out.writeUTF("Hello from " + client.getLocalSocketAddress());
         
         // Receive response from server
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         System.out.println("📨 Server says: " + in.readUTF());
         
         // Close connection
         client.close();
         System.out.println("🔒 Connection closed");
         
      } catch (IOException e) {
         System.err.println("❌ Error: " + e.getMessage());
         e.printStackTrace();
      }
   }
}
```

## 🖥️ Socket Server Example

Here's a complete server program that listens for client connections:

```java
package com.tutorialspoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GreetingServer extends Thread {
   private ServerSocket serverSocket;
   
   public GreetingServer(int port) throws IOException {
      // Create server socket on specified port
      serverSocket = new ServerSocket(port);
      // Set timeout for accept() operation
      serverSocket.setSoTimeout(10000);
   }

   public void run() {
      while(true) {
         try {
            System.out.println("👂 Waiting for client on port " + 
               serverSocket.getLocalPort() + "...");
            
            // Wait for client connection
            Socket server = serverSocket.accept();
            System.out.println("✅ Just connected to " + server.getRemoteSocketAddress());
            
            // Read client message
            DataInputStream in = new DataInputStream(server.getInputStream());
            String clientMessage = in.readUTF();
            System.out.println("📨 Client says: " + clientMessage);
            
            // Send response to client
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
               + "\nGoodbye!");
            
            // Close connection with this client
            server.close();
            System.out.println("🔒 Connection with client closed");
            
         } catch (SocketTimeoutException s) {
            System.out.println("⏰ Socket timed out!");
            break;
         } catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public static void main(String[] args) {
      // Get port from command line argument
      int port = Integer.parseInt(args[0]);
      try {
         // Create and start server thread
         Thread t = new GreetingServer(port);
         t.start();
         System.out.println("🚀 Server started on port " + port);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
```

## 🚀 Running the Examples

### 📋 Compilation
```bash
# Compile both files
javac com/tutorialspoint/GreetingServer.java
javac com/tutorialspoint/GreetingClient.java
```

### 🖥️ Starting the Server
```bash
# Run server on port 6066
java com.tutorialspoint.GreetingServer 6066
```

**Expected Server Output:**
```
🚀 Server started on port 6066
👂 Waiting for client on port 6066...
```

### 👤 Running the Client
```bash
# Connect to local server on port 6066
java com.tutorialspoint.GreetingClient localhost 6066
```

**Expected Client Output:**
```
🔌 Connecting to localhost on port 6066
✅ Just connected to localhost/127.0.0.1:6066
📨 Server says: Thank you for connecting to /127.0.0.1:6066
Goodbye!
🔒 Connection closed
```

**Updated Server Output after Client Connection:**
```
✅ Just connected to /127.0.0.1:49462
📨 Client says: Hello from /127.0.0.1:49462
🔒 Connection with client closed
👂 Waiting for client on port 6066...
```

## 📝 Best Practices

### 🛡️ Error Handling
```java
try (Socket socket = new Socket(host, port)) {
    // Use socket
} catch (IOException e) {
    System.err.println("Connection failed: " + e.getMessage());
}
```

### ⏱️ Timeout Configuration
```java
socket.setSoTimeout(5000); // 5-second timeout
socket.setKeepAlive(true); // Enable keep-alive
```

### 💾 Resource Management
```java
// Always close sockets properly
try {
    if (socket != null && !socket.isClosed()) {
        socket.close();
    }
} catch (IOException e) {
    // Handle close error
}
```

### 🔄 Multi-threaded Servers
For handling multiple clients simultaneously, create a new thread for each client connection:

```java
while (true) {
    Socket clientSocket = serverSocket.accept();
    new Thread(() -> handleClient(clientSocket)).start();
}
```

## 🚨 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| **Connection refused** | Check if server is running and port is correct |
| **Timeout errors** | Increase timeout values or check network |
| **Port already in use** | Use different port or set SO_REUSEADDR |
| **Slow performance** | Adjust buffer sizes and disable Nagle's algorithm |
| **Resource leaks** | Use try-with-resources for automatic closing |

## 🎯 Key Points to Remember

1. **🔗 TCP vs UDP** - Socket class is for TCP (connection-oriented), use DatagramSocket for UDP
2. **🔄 Bidirectional** - Sockets provide both input and output streams
3. **🌐 Client & Server** - Both use Socket class after connection is established
4. **⏳ Blocking** - Socket operations are blocking by default
5. **🔒 Thread-safe** - Socket methods are synchronized for thread safety


**🔌 Happy Socket Programming with Java! 🔌**