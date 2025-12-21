# Java Properties Class - Complete Guide & Cheatsheet

## 📋 Overview
The **Java Properties class** (`java.util.Properties`) is a specialized **key-value store** for configuration data. It extends `Hashtable<Object, Object>` and provides methods to load/store properties from/to files, supporting both simple `.properties` files and XML format. Properties are ideal for application configuration, internationalization, and settings management.


## 🔑 Core Characteristics

### ✅ **String-Only Storage**
- Keys and values are always Strings
- Legacy methods allow Objects but should be avoided

### ✅ **File Persistence**
- Save to/load from `.properties` files
- Support for XML format (`storeToXML()`/`loadFromXML()`)

### ✅ **Hierarchical Defaults**
- Chain of default properties for fallback values
- Inheritance-based lookup mechanism

### ✅ **ISO-8859-1 by Default**
- Traditional `.properties` files use Latin-1 encoding
- XML files use UTF-8 by default

### ✅ **Thread Safety**
- Inherits thread safety from `Hashtable`
- All methods are synchronized

## 🏗️ Properties vs HashMap Comparison

| Feature | Properties | HashMap |
|---------|------------|---------|
| **Key/Value Types** | String only (by design) | Any Object |
| **File I/O** | Built-in load/store methods | Manual serialization needed |
| **Encoding** | Handles ISO-8859-1/UTF-8 automatically | Manual encoding handling |
| **Default Values** | Built-in inheritance mechanism | No built-in default system |
| **Thread Safety** | ✅ Yes (synchronized) | ❌ No (use ConcurrentHashMap) |
| **Comment Support** | ✅ Yes (in files) | ❌ No |
| **Internationalization** | Works with ResourceBundle | No special support |
| **Performance** | Slower (synchronized) | Faster |

---

## 📝 Creating & Basic Operations

### **Creating Properties Instances**
```java
// Basic creation
Properties props = new Properties();

// With default properties
Properties defaults = new Properties();
defaults.setProperty("default.key", "default.value");
Properties propsWithDefaults = new Properties(defaults);

// System properties (read-only snapshot)
Properties systemProps = System.getProperties();

// Empty immutable properties (Java 8+)
Properties empty = new Properties() {
    @Override public Object setProperty(String key, String value) {
        throw new UnsupportedOperationException("Immutable");
    }
};
```

### **Setting Properties**
```java
Properties props = new Properties();

// Standard method (recommended)
props.setProperty("database.url", "jdbc:mysql://localhost:3306/mydb");
props.setProperty("database.user", "admin");
props.setProperty("database.password", "secret123");
props.setProperty("server.port", "8080");
props.setProperty("feature.enabled", "true");

// Legacy put() method (AVOID - not type-safe)
props.put("unsafe.key", new Object());  // Can store non-String!
// This will cause issues with getProperty() later
```

### **Getting Properties**
```java
String url = props.getProperty("database.url");  // Returns value or null
String user = props.getProperty("database.user");

// With default value
String timeout = props.getProperty("connection.timeout", "30");  // Default: "30"

// Check if property exists
boolean hasKey = props.containsKey("database.url");

// Get all property names
Enumeration<?> propertyNames = props.propertyNames();
while (propertyNames.hasMoreElements()) {
    String key = (String) propertyNames.nextElement();
    String value = props.getProperty(key);
}
```

### **Removing Properties**
```java
// Remove single property
Object removed = props.remove("database.password");

// Remove with String method (same as above)
props.remove("server.port");

// Clear all properties
props.clear();
```

### **Iterating Properties**
```java
Properties props = new Properties();
props.setProperty("key1", "value1");
props.setProperty("key2", "value2");
props.setProperty("key3", "value3");

// Method 1: Enumeration (legacy)
Enumeration<?> keys = props.propertyNames();
while (keys.hasMoreElements()) {
    String key = (String) keys.nextElement();
    String value = props.getProperty(key);
    System.out.println(key + " = " + value);
}

// Method 2: keySet() with for-each (Java 5+)
for (String key : props.stringPropertyNames()) {  // Returns Set<String>
    String value = props.getProperty(key);
    System.out.println(key + " = " + value);
}

// Method 3: Stream API (Java 8+)
props.stringPropertyNames().stream()
     .sorted()
     .forEach(key -> 
         System.out.println(key + " = " + props.getProperty(key)));

// Method 4: forEach (Java 8+)
props.forEach((k, v) -> 
    System.out.println(k + " = " + v));
```

---

## 📁 File Operations

### **Properties File Format**
```properties
# Application configuration
# Created: 2024-01-15
# Author: Admin

# Database settings
database.url = jdbc:mysql://localhost:3306/mydb
database.user = admin
database.password = secret123
database.pool.size = 10

# Server settings
server.port = 8080
server.host = localhost
server.timeout = 30

# Feature flags
feature.logging = true
feature.cache = false
feature.debug = true

# Multi-line values (using backslash)
welcome.message = Welcome to our application.\
                  Please login to continue.\
                  Contact admin for help.
```

### **Saving to Properties File**
```java
Properties props = new Properties();
props.setProperty("app.name", "MyApplication");
props.setProperty("app.version", "1.0.0");
props.setProperty("app.author", "John Doe");

// Save to file
try (FileWriter writer = new FileWriter("config.properties")) {
    props.store(writer, "Application Configuration");
    // Optional: Specify charset (default: ISO-8859-1)
    // props.store(new OutputStreamWriter(new FileOutputStream("config.properties"), 
    //           StandardCharsets.UTF_8), "Config");
} catch (IOException e) {
    e.printStackTrace();
}

// Save with OutputStream
try (FileOutputStream fos = new FileOutputStream("config.properties")) {
    props.store(fos, "Application Configuration");
} catch (IOException e) {
    e.printStackTrace();
}
```

### **Loading from Properties File**
```java
Properties props = new Properties();

// Load from file
try (FileReader reader = new FileReader("config.properties")) {
    props.load(reader);
} catch (IOException e) {
    e.printStackTrace();
}

// Load with InputStream
try (FileInputStream fis = new FileInputStream("config.properties")) {
    props.load(fis);
} catch (IOException e) {
    e.printStackTrace();
}

// Load with specific charset (UTF-8)
try (InputStreamReader isr = new InputStreamReader(
        new FileInputStream("config.properties"), StandardCharsets.UTF_8)) {
    props.load(isr);
} catch (IOException e) {
    e.printStackTrace();
}
```

### **XML Format Support**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
    <comment>Application Configuration</comment>
    <entry key="app.name">MyApplication</entry>
    <entry key="app.version">1.0.0</entry>
    <entry key="app.author">John Doe</entry>
</properties>
```

### **XML File Operations**
```java
Properties props = new Properties();
props.setProperty("app.name", "MyApplication");
props.setProperty("app.version", "1.0.0");

// Save to XML
try (FileOutputStream fos = new FileOutputStream("config.xml")) {
    props.storeToXML(fos, "Application Configuration");
    // With custom encoding:
    // props.storeToXML(fos, "Config", "ISO-8859-1");
} catch (IOException e) {
    e.printStackTrace();
}

// Load from XML
try (FileInputStream fis = new FileInputStream("config.xml")) {
    props.loadFromXML(fis);
} catch (IOException e) {
    e.printStackTrace();
}
```

### **Encoding Considerations**
```java
// Default encodings:
// - .properties files: ISO-8859-1 (Latin-1)
// - XML files: UTF-8

// Working with UTF-8 .properties files:
Properties props = new Properties();

// Load UTF-8 properties file
try (Reader reader = new InputStreamReader(
        new FileInputStream("config.properties"), StandardCharsets.UTF_8)) {
    props.load(reader);
}

// Save as UTF-8
try (Writer writer = new OutputStreamWriter(
        new FileOutputStream("config.properties"), StandardCharsets.UTF_8)) {
    props.store(writer, "UTF-8 Config");
}

// For special characters in ISO-8859-1 files, use Unicode escape sequences
// property = \u20AC  // Euro symbol
```

---

## 🔗 Classpath & Resource Loading

### **Loading from Classpath**
```java
// Method 1: Using ClassLoader
try (InputStream input = getClass().getClassLoader()
        .getResourceAsStream("config.properties")) {
    if (input != null) {
        Properties props = new Properties();
        props.load(input);
    }
} catch (IOException e) {
    e.printStackTrace();
}

// Method 2: Using Class (relative to class location)
try (InputStream input = getClass()
        .getResourceAsStream("/com/myapp/config.properties")) {
    if (input != null) {
        Properties props = new Properties();
        props.load(input);
    }
} catch (IOException e) {
    e.printStackTrace();
}

// Method 3: Using absolute path from classpath root
try (InputStream input = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("config.properties")) {
    if (input != null) {
        Properties props = new Properties();
        props.load(input);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### **Loading Multiple Property Files**
```java
public Properties loadAllProperties(String... filenames) {
    Properties allProps = new Properties();
    for (String filename : filenames) {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(filename)) {
            if (input != null) {
                Properties fileProps = new Properties();
                fileProps.load(input);
                allProps.putAll(fileProps);
            }
        } catch (IOException e) {
            // Log warning but continue
            System.err.println("Warning: Could not load " + filename);
        }
    }
    return allProps;
}

// Usage
Properties config = loadAllProperties(
    "default.properties",
    "environment.properties",
    "user.properties"
);
```

---

## 🔄 Default Properties & Inheritance

### **Default Properties Chain**
```java
// Create defaults
Properties defaults = new Properties();
defaults.setProperty("timeout", "30");
defaults.setProperty("retries", "3");
defaults.setProperty("cache.size", "100");

// Create main properties with defaults
Properties config = new Properties(defaults);

// Override some defaults
config.setProperty("timeout", "60");  // Override default
config.setProperty("host", "localhost");  // New property

// Get values (falls back to defaults)
String timeout = config.getProperty("timeout");  // "60" (overridden)
String retries = config.getProperty("retries");  // "3" (from defaults)
String cacheSize = config.getProperty("cache.size");  // "100" (from defaults)
String missing = config.getProperty("missing");  // null (not in defaults either)

// With explicit default parameter (takes precedence)
String value = config.getProperty("missing", "fallback");  // "fallback"
```

### **Nested Defaults (Multiple Levels)**
```java
// Level 3: System defaults
Properties systemDefaults = new Properties();
systemDefaults.setProperty("language", "en");
systemDefaults.setProperty("timezone", "UTC");

// Level 2: Application defaults
Properties appDefaults = new Properties(systemDefaults);
appDefaults.setProperty("app.name", "MyApp");
appDefaults.setProperty("timeout", "30");

// Level 1: User configuration
Properties userConfig = new Properties(appDefaults);
userConfig.setProperty("timeout", "60");  // Override
userConfig.setProperty("username", "john");  // New

// Lookup chain: userConfig → appDefaults → systemDefaults
String lang = userConfig.getProperty("language");  // "en" (from systemDefaults)
String appName = userConfig.getProperty("app.name");  // "MyApp" (from appDefaults)
String timeout = userConfig.getProperty("timeout");  // "60" (overridden)
```

### **Listing Properties with Defaults**
```java
public void listAllProperties(Properties props) {
    // Get all properties (including defaults)
    Enumeration<?> names = props.propertyNames();
    while (names.hasMoreElements()) {
        String key = (String) names.nextElement();
        String value = props.getProperty(key);
        System.out.println(key + " = " + value);
    }
}

// Alternative: Print with source indication
public void listPropertiesWithSource(Properties props) {
    Properties current = props;
    while (current != null) {
        System.out.println("=== Properties from: " + current.getClass().getSimpleName() + " ===");
        for (String key : current.stringPropertyNames()) {
            String value = current.getProperty(key);
            System.out.println(key + " = " + value);
        }
        // Get defaults (if any)
        current = current.defaults;
    }
}
```

---

## 🖥️ System Properties

### **Accessing System Properties**
```java
// Get all system properties
Properties systemProps = System.getProperties();

// Get specific system property
String javaVersion = System.getProperty("java.version");
String userHome = System.getProperty("user.home");
String osName = System.getProperty("os.name");
String classPath = System.getProperty("java.class.path");
String fileSeparator = System.getProperty("file.separator");  // "/" or "\"

// Get with default
String encoding = System.getProperty("file.encoding", "UTF-8");

// List all system properties
systemProps.list(System.out);  // Prints all properties

// Iterate system properties
for (String key : systemProps.stringPropertyNames()) {
    System.out.println(key + " = " + systemProps.getProperty(key));
}
```

### **Setting System Properties**
```java
// Set system property (affects entire JVM)
System.setProperty("app.name", "MyApplication");
System.setProperty("log.level", "DEBUG");
System.setProperty("config.path", "/etc/myapp");

// Check if property was set
String appName = System.getProperty("app.name");  // "MyApplication"

// Properties set via command line take precedence
// java -Dapp.name=CLIApp -jar myapp.jar
```

### **Common System Properties**
```java
// Runtime information
String version = System.getProperty("java.version");
String vendor = System.getProperty("java.vendor");
String vmName = System.getProperty("java.vm.name");

// Operating System
String os = System.getProperty("os.name");
String osVersion = System.getProperty("os.version");
String osArch = System.getProperty("os.arch");

// User information
String user = System.getProperty("user.name");
String home = System.getProperty("user.home");
String dir = System.getProperty("user.dir");

// File system
String separator = System.getProperty("file.separator");
String pathSeparator = System.getProperty("path.separator");
String lineSeparator = System.getProperty("line.separator");

// JVM settings
String classPath = System.getProperty("java.class.path");
String libPath = System.getProperty("java.library.path");
String tmpDir = System.getProperty("java.io.tmpdir");

// Encoding
String encoding = System.getProperty("file.encoding");
String sunEncoding = System.getProperty("sun.jnu.encoding");
```

### **Command Line Properties**
```bash
# Setting properties via command line
java -Ddatabase.url=jdbc:mysql://localhost/mydb \
     -Dserver.port=8080 \
     -Dlog.level=DEBUG \
     -jar myapp.jar

# Multiple properties
java -Dconfig.file=/etc/app/config.properties \
     -Dapp.name="My App" \
     com.myapp.Main

# Properties with spaces (use quotes)
java -Dmessage="Hello World" -jar app.jar
```

```java
// Access command line properties in code
String dbUrl = System.getProperty("database.url");
String port = System.getProperty("server.port");
String logLevel = System.getProperty("log.level");

// Fallback to defaults
int serverPort = Integer.parseInt(
    System.getProperty("server.port", "8080")
);
boolean debugMode = Boolean.parseBoolean(
    System.getProperty("debug.mode", "false")
);
```

---

## 🔧 Advanced Operations

### **Property Substitution (Variable Expansion)**
```java
public Properties expandVariables(Properties props) {
    Properties expanded = new Properties();
    for (String key : props.stringPropertyNames()) {
        String value = props.getProperty(key);
        // Replace ${property} with actual value
        value = expand(value, props);
        expanded.setProperty(key, value);
    }
    return expanded;
}

private String expand(String value, Properties props) {
    if (value == null) return null;
    
    Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
    Matcher matcher = pattern.matcher(value);
    StringBuffer result = new StringBuffer();
    
    while (matcher.find()) {
        String varName = matcher.group(1);
        String varValue = props.getProperty(varName, "");
        matcher.appendReplacement(result, Matcher.quoteReplacement(varValue));
    }
    matcher.appendTail(result);
    
    return result.toString();
}

// Usage
Properties props = new Properties();
props.setProperty("app.dir", "/opt/myapp");
props.setProperty("log.file", "${app.dir}/logs/app.log");
props.setProperty("config.file", "${app.dir}/config.properties");

Properties expanded = expandVariables(props);
// log.file becomes "/opt/myapp/logs/app.log"
```

### **Type-Safe Property Access**
```java
public class TypedProperties {
    private final Properties props;
    
    public TypedProperties(Properties props) {
        this.props = props;
    }
    
    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
    
    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(props.getProperty(key));
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }
    
    public long getLong(String key, long defaultValue) {
        try {
            return Long.parseLong(props.getProperty(key));
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }
    
    public double getDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(props.getProperty(key));
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
    
    public List<String> getList(String key, List<String> defaultValue) {
        String value = props.getProperty(key);
        if (value == null) return defaultValue;
        return Arrays.asList(value.split("\\s*,\\s*"));
    }
    
    public <T extends Enum<T>> T getEnum(String key, Class<T> enumClass, T defaultValue) {
        String value = props.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}

// Usage
TypedProperties config = new TypedProperties(props);
int port = config.getInt("server.port", 8080);
boolean enabled = config.getBoolean("feature.enabled", false);
List<String> hosts = config.getList("database.hosts", Arrays.asList("localhost"));
LogLevel level = config.getEnum("log.level", LogLevel.class, LogLevel.INFO);
```

### **Property Validation**
```java
public class PropertyValidator {
    public static void validateRequired(Properties props, String... requiredKeys) 
            throws MissingPropertyException {
        for (String key : requiredKeys) {
            if (!props.containsKey(key)) {
                throw new MissingPropertyException("Required property missing: " + key);
            }
        }
    }
    
    public static void validatePort(Properties props, String key) 
            throws InvalidPropertyException {
        String value = props.getProperty(key);
        if (value != null) {
            try {
                int port = Integer.parseInt(value);
                if (port < 1 || port > 65535) {
                    throw new InvalidPropertyException(
                        "Invalid port for " + key + ": " + port);
                }
            } catch (NumberFormatException e) {
                throw new InvalidPropertyException(
                    "Invalid number for " + key + ": " + value);
            }
        }
    }
    
    public static void validateFileExists(Properties props, String key) 
            throws InvalidPropertyException {
        String path = props.getProperty(key);
        if (path != null) {
            File file = new File(path);
            if (!file.exists()) {
                throw new InvalidPropertyException(
                    "File does not exist for " + key + ": " + path);
            }
        }
    }
    
    public static void validateBoolean(Properties props, String key) 
            throws InvalidPropertyException {
        String value = props.getProperty(key);
        if (value != null && !value.equalsIgnoreCase("true") 
            && !value.equalsIgnoreCase("false")) {
            throw new InvalidPropertyException(
                "Invalid boolean for " + key + ": " + value);
        }
    }
}

// Usage
try {
    PropertyValidator.validateRequired(config, "database.url", "database.user");
    PropertyValidator.validatePort(config, "server.port");
    PropertyValidator.validateFileExists(config, "log.file");
    PropertyValidator.validateBoolean(config, "ssl.enabled");
} catch (InvalidPropertyException e) {
    // Handle validation error
}
```

### **Property Change Listeners**
```java
public class ObservableProperties extends Properties {
    private final List<PropertyChangeListener> listeners = new ArrayList<>();
    
    @Override
    public synchronized Object setProperty(String key, String value) {
        String oldValue = getProperty(key);
        Object result = super.setProperty(key, value);
        notifyListeners(key, oldValue, value);
        return result;
    }
    
    @Override
    public synchronized Object remove(Object key) {
        if (key instanceof String) {
            String oldValue = getProperty((String) key);
            Object result = super.remove(key);
            notifyListeners((String) key, oldValue, null);
            return result;
        }
        return super.remove(key);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String key, String oldValue, String newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(
            this, key, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }
}

// Usage
ObservableProperties props = new ObservableProperties();
props.addPropertyChangeListener(event -> {
    System.out.println("Property changed: " + event.getPropertyName() +
                      " from " + event.getOldValue() +
                      " to " + event.getNewValue());
});

props.setProperty("timeout", "30");
props.setProperty("timeout", "60");  // Triggers listener
props.remove("timeout");  // Triggers listener
```

---

## ⚠️ Common Pitfalls & Best Practices

### ✅ **Do:**
```java
// Use setProperty() and getProperty() methods
props.setProperty("key", "value");
String value = props.getProperty("key");

// Always specify default values
String timeout = props.getProperty("timeout", "30");

// Use stringPropertyNames() for iteration
for (String key : props.stringPropertyNames()) {
    // Safe iteration
}

// Handle encoding explicitly
try (InputStreamReader reader = new InputStreamReader(
        new FileInputStream("config.properties"), StandardCharsets.UTF_8)) {
    props.load(reader);
}

// Validate required properties
public void validateConfig(Properties props) {
    String[] required = {"db.url", "db.user", "db.password"};
    for (String key : required) {
        if (!props.containsKey(key)) {
            throw new IllegalArgumentException("Missing property: " + key);
        }
    }
}

// Use typed accessors
public int getIntProperty(Properties props, String key, int defaultValue) {
    try {
        return Integer.parseInt(props.getProperty(key));
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}
```

### ❌ **Don't:**
```java
// DON'T use put() and get() methods
props.put("key", "value");  // Wrong! Allows non-String values
props.get("key");           // Wrong! Returns Object, not String

// DON'T ignore encoding issues
props.load(new FileReader("config.properties"));  // Uses platform encoding

// DON'T assume property exists without checking
String value = props.getProperty("missing");  // Could be null
int length = value.length();  // NullPointerException!

// DON'T store sensitive data in plain text
props.setProperty("password", "secret123");  // Not secure!
// Use encryption or environment variables for secrets

// DON'T modify system properties unnecessarily
System.setProperty("user.dir", "/new/path");  // Affects entire JVM

// DON'T use Properties for large datasets
// Use database or specialized storage instead
```

### ⚠️ **Critical Issues:**
```java
// 1. put() vs setProperty() issue
Properties props = new Properties();
props.put("key", 123);  // Stores Integer
String value = props.getProperty("key");  // Returns null!

// 2. Encoding problems with non-Latin characters
props.setProperty("message", "café");  // Café with accent
props.store(new FileWriter("props.properties"), "");
// In file: caf\u00e9 (Unicode escape)
// If read with wrong encoding: garbled text

// 3. Concurrent modification during iteration
for (String key : props.stringPropertyNames()) {
    props.remove(key);  // ConcurrentModificationException!
}

// 4. Property name collisions
props.setProperty("app.name", "MyApp");
System.setProperty("app.name", "SystemApp");
// Which one wins depends on lookup order

// 5. Case sensitivity
props.setProperty("Timeout", "30");
String value1 = props.getProperty("timeout");  // null (case-sensitive)
String value2 = props.getProperty("Timeout");  // "30"

// 6. Path separator differences
props.setProperty("path", "C:\\Program Files\\App");  // Windows
// On Unix: "C:\Program Files\App" (backslashes as literal)
```

### 🔧 **Workarounds & Solutions:**
```java
// 1. Always use setProperty()/getProperty()
Properties safeProps = new Properties() {
    @Override
    public synchronized Object put(Object key, Object value) {
        throw new UnsupportedOperationException("Use setProperty()");
    }
    
    @Override
    public synchronized Object get(Object key) {
        throw new UnsupportedOperationException("Use getProperty()");
    }
};

// 2. Handle encoding properly
public Properties loadProperties(File file, Charset charset) throws IOException {
    Properties props = new Properties();
    try (Reader reader = new InputStreamReader(new FileInputStream(file), charset)) {
        props.load(reader);
    }
    return props;
}

// 3. Case-insensitive properties
public class CaseInsensitiveProperties extends Properties {
    @Override
    public synchronized Object setProperty(String key, String value) {
        return super.setProperty(key.toLowerCase(), value);
    }
    
    @Override
    public String getProperty(String key) {
        return super.getProperty(key.toLowerCase());
    }
    
    @Override
    public String getProperty(String key, String defaultValue) {
        return super.getProperty(key.toLowerCase(), defaultValue);
    }
}

// 4. Secure property storage
public class SecureProperties extends Properties {
    private final CryptoService crypto;
    
    public SecureProperties(CryptoService crypto) {
        this.crypto = crypto;
    }
    
    @Override
    public synchronized Object setProperty(String key, String value) {
        if (key.toLowerCase().contains("password") || 
            key.toLowerCase().contains("secret")) {
            try {
                value = crypto.encrypt(value);
            } catch (Exception e) {
                throw new RuntimeException("Encryption failed", e);
            }
        }
        return super.setProperty(key, value);
    }
    
    @Override
    public String getProperty(String key) {
        String value = super.getProperty(key);
        if (value != null && (key.toLowerCase().contains("password") || 
            key.toLowerCase().contains("secret"))) {
            try {
                return crypto.decrypt(value);
            } catch (Exception e) {
                throw new RuntimeException("Decryption failed", e);
            }
        }
        return value;
    }
}
```

---

## 📚 Real-World Examples

### **Example 1: Application Configuration Manager**
```java
public class AppConfig {
    private static final String CONFIG_FILE = "application.properties";
    private static final Properties props = new Properties();
    private static final Properties defaults = new Properties();
    
    static {
        // Load defaults
        defaults.setProperty("server.port", "8080");
        defaults.setProperty("server.host", "localhost");
        defaults.setProperty("database.url", "jdbc:h2:mem:test");
        defaults.setProperty("log.level", "INFO");
        
        // Load configuration
        loadConfig();
    }
    
    private static void loadConfig() {
        // 1. Try classpath resource
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
                return;
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load config from classpath");
        }
        
        // 2. Try file system
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
            } catch (IOException e) {
                System.err.println("Warning: Could not load config from file");
            }
        }
        
        // 3. Use defaults
        props.putAll(defaults);
    }
    
    public static String getString(String key) {
        return props.getProperty(key);
    }
    
    public static String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
    
    public static int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }
    
    public static int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
    
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(props.getProperty(key));
    }
    
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = props.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    public static void reload() {
        props.clear();
        loadConfig();
    }
}

// Usage
int port = AppConfig.getInt("server.port", 8080);
String dbUrl = AppConfig.getString("database.url");
boolean debug = AppConfig.getBoolean("debug.mode", false);
```

### **Example 2: Multi-Environment Configuration**
```java
public class EnvironmentConfig {
    private static final String ENV_PREFIX = "app.";
    private final Properties props = new Properties();
    
    public EnvironmentConfig(String environment) {
        // Load base config
        loadFromClasspath("config/base.properties");
        
        // Load environment-specific config
        loadFromClasspath("config/" + environment + ".properties");
        
        // Load local overrides (if exists)
        loadFromFile("local.properties");
        
        // Override with system properties
        overrideWithSystemProperties();
        
        // Override with environment variables
        overrideWithEnvironmentVariables();
    }
    
    private void loadFromClasspath(String resource) {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(resource)) {
            if (input != null) {
                Properties fileProps = new Properties();
                fileProps.load(input);
                props.putAll(fileProps);
            }
        } catch (IOException e) {
            // Resource might not exist, that's OK
        }
    }
    
    private void loadFromFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                Properties fileProps = new Properties();
                fileProps.load(fis);
                props.putAll(fileProps);
            } catch (IOException e) {
                System.err.println("Warning: Could not load " + filename);
            }
        }
    }
    
    private void overrideWithSystemProperties() {
        Properties systemProps = System.getProperties();
        for (String key : systemProps.stringPropertyNames()) {
            if (key.startsWith(ENV_PREFIX)) {
                String propKey = key.substring(ENV_PREFIX.length());
                props.setProperty(propKey, systemProps.getProperty(key));
            }
        }
    }
    
    private void overrideWithEnvironmentVariables() {
        Map<String, String> env = System.getenv();
        for (String key : env.keySet()) {
            if (key.startsWith(ENV_PREFIX.toUpperCase().replace('.', '_'))) {
                String propKey = key.substring(ENV_PREFIX.length())
                                 .toLowerCase()
                                 .replace('_', '.');
                props.setProperty(propKey, env.get(key));
            }
        }
    }
    
    public String getProperty(String key) {
        return props.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}

// Usage
EnvironmentConfig devConfig = new EnvironmentConfig("development");
EnvironmentConfig prodConfig = new EnvironmentConfig("production");

// Command line: java -Dapp.database.url=jdbc:mysql://prod/db -jar app.jar
// Environment: export APP_DATABASE_URL=jdbc:mysql://prod/db
```

### **Example 3: Configuration Validation & Reporting**
```java
public class ConfigValidator {
    public static ValidationResult validate(Properties config) {
        ValidationResult result = new ValidationResult();
        
        // Check required properties
        validateRequired(config, result, 
            "database.url", "database.username", "database.password");
        
        // Validate formats
        validateUrl(config, result, "database.url");
        validatePort(config, result, "server.port");
        validateBoolean(config, result, "ssl.enabled");
        validateFileExists(config, result, "log.file");
        validateDirectory(config, result, "data.dir");
        
        // Validate ranges
        validateRange(config, result, "connection.pool.size", 1, 100);
        validateRange(config, result, "cache.size.mb", 10, 1024);
        
        // Check for deprecated properties
        checkDeprecated(config, result, 
            "old.timeout", "Use server.timeout instead");
        
        return result;
    }
    
    private static void validateRequired(Properties config, 
            ValidationResult result, String... keys) {
        for (String key : keys) {
            if (!config.containsKey(key)) {
                result.addError(key, "Required property is missing");
            }
        }
    }
    
    private static void validatePort(Properties config, 
            ValidationResult result, String key) {
        String value = config.getProperty(key);
        if (value != null) {
            try {
                int port = Integer.parseInt(value);
                if (port < 1 || port > 65535) {
                    result.addError(key, "Port must be between 1 and 65535");
                }
            } catch (NumberFormatException e) {
                result.addError(key, "Port must be a valid integer");
            }
        }
    }
    
    private static void validateRange(Properties config, 
            ValidationResult result, String key, int min, int max) {
        String value = config.getProperty(key);
        if (value != null) {
            try {
                int num = Integer.parseInt(value);
                if (num < min || num > max) {
                    result.addError(key, 
                        String.format("Value must be between %d and %d", min, max));
                }
            } catch (NumberFormatException e) {
                result.addError(key, "Value must be a valid integer");
            }
        }
    }
    
    private static void checkDeprecated(Properties config, 
            ValidationResult result, String key, String message) {
        if (config.containsKey(key)) {
            result.addWarning(key, "Deprecated: " + message);
        }
    }
    
    public static class ValidationResult {
        private final List<String> errors = new ArrayList<>();
        private final List<String> warnings = new ArrayList<>();
        
        public void addError(String key, String message) {
            errors.add(key + ": " + message);
        }
        
        public void addWarning(String key, String message) {
            warnings.add(key + ": " + message);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public List<String> getErrors() {
            return Collections.unmodifiableList(errors);
        }
        
        public List<String> getWarnings() {
            return Collections.unmodifiableList(warnings);
        }
        
        public void printReport() {
            if (!warnings.isEmpty()) {
                System.out.println("Warnings:");
                warnings.forEach(w -> System.out.println("  " + w));
            }
            
            if (!errors.isEmpty()) {
                System.err.println("Errors:");
                errors.forEach(e -> System.err.println("  " + e));
            } else {
                System.out.println("Configuration is valid");
            }
        }
    }
}

// Usage
Properties config = loadConfig();
ValidationResult result = ConfigValidator.validate(config);
if (!result.isValid()) {
    result.printReport();
    System.exit(1);
}
```

---

## 🔍 Debugging & Troubleshooting

### **Common Issues & Solutions**
| Issue | Cause | Solution |
|-------|-------|----------|
| `getProperty()` returns null | Key doesn't exist or wrong case | Use `containsKey()` first or provide default |
| Special characters corrupted | Wrong encoding | Specify UTF-8 when loading/saving |
| Properties not loading | File not found | Check classpath and file permissions |
| ConcurrentModificationException | Modifying during iteration | Copy keys first or use synchronized block |
| NumberFormatException | Invalid numeric value | Validate before parsing or use default |
| SecurityException | Cannot read/write file | Check file permissions and security policy |
| Properties from command line ignored | Wrong property name | Use `-D` prefix and check with `System.getProperties()` |

### **Debugging Properties**
```java
public class PropertiesDebugger {
    public static void dumpProperties(Properties props, String title) {
        System.out.println("=== " + title + " ===");
        System.out.println("Total properties: " + props.size());
        
        // List all properties
        List<String> keys = new ArrayList<>(props.stringPropertyNames());
        Collections.sort(keys);
        
        for (String key : keys) {
            String value = props.getProperty(key);
            // Mask sensitive data
            if (key.toLowerCase().contains("password") || 
                key.toLowerCase().contains("secret")) {
                value = "***MASKED***";
            }
            System.out.printf("%-40s = %s%n", key, value);
        }
        System.out.println();
    }
    
    public static void tracePropertyLookup(Properties props, String key) {
        System.out.println("Looking up property: " + key);
        
        Properties current = props;
        int level = 0;
        
        while (current != null) {
            System.out.println("Level " + level + ": " + 
                (current == props ? "Main properties" : "Default properties"));
            
            boolean hasKey = current.containsKey(key);
            String value = current.getProperty(key);
            
            System.out.println("  Has key: " + hasKey);
            System.out.println("  Value: " + value);
            
            current = current.defaults;
            level++;
        }
        
        String finalValue = props.getProperty(key);
        System.out.println("Final result: " + finalValue);
        System.out.println();
    }
    
    public static void compareProperties(Properties props1, Properties props2) {
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(props1.stringPropertyNames());
        allKeys.addAll(props2.stringPropertyNames());
        
        List<String> sortedKeys = new ArrayList<>(allKeys);
        Collections.sort(sortedKeys);
        
        System.out.println("=== Property Comparison ===");
        System.out.printf("%-30s %-30s %-30s%n", "Key", "Props1", "Props2");
        System.out.println("----------------------------------------------------------------");
        
        for (String key : sortedKeys) {
            String val1 = props1.getProperty(key);
            String val2 = props2.getProperty(key);
            
            if (!Objects.equals(val1, val2)) {
                System.out.printf("%-30s %-30s %-30s (DIFFERENT)%n", 
                    key, val1, val2);
            } else {
                System.out.printf("%-30s %-30s %-30s%n", key, val1, val2);
            }
        }
    }
}

// Usage
PropertiesDebugger.dumpProperties(config, "Application Configuration");
PropertiesDebugger.tracePropertyLookup(config, "database.url");
PropertiesDebugger.compareProperties(config, defaults);
```

---

## 📖 Summary

The **Java Properties class** is essential for:
- **Application configuration** management
- **Internationalization** and localization
- **Simple key-value persistence** to files
- **System property** access and manipulation
- **Hierarchical configuration** with defaults

**Key Takeaways:**
1. **Always use `setProperty()` and `getProperty()`** - avoid `put()` and `get()`
2. **Handle encoding properly** - default is ISO-8859-1 for `.properties`, UTF-8 for XML
3. **Use defaults hierarchy** for layered configuration
4. **Validate properties** before use to avoid runtime errors
5. **Secure sensitive data** - don't store passwords in plain text
6. **Consider alternatives** for complex configuration (YAML, JSON, database)
7. **System properties** override everything and are set via `-D` flag

This comprehensive guide covers everything from basic operations to advanced techniques. Bookmark this cheatsheet for quick reference during your Java development work!