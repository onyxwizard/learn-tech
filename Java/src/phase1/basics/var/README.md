## 🎯 **Project: "Modern Java Refactor Challenge"**

### 💡 **Goal**
Refactor a legacy-style Java method into **clean, modern code using `var`** — while **following all rules and best practices**.

You’ll learn to:
- ✅ Replace redundant type declarations with `var`
- ❌ Avoid `var` where it hurts readability
- 🧪 Understand compiler behavior through intentional errors (as comments)
- 📊 Produce output that proves your refactor works



### 📁 **File to Create**
`ModernRefactor.java`

> 💡 Run with: `java ModernRefactor.java` (**JDK 10+ required**)



### 🧱 **Starter Code (Legacy Style – DO NOT MODIFY THIS LOGIC!)**

You are given a **working but verbose** method that processes user data. Your job is to **rewrite it using `var` where appropriate**, **without changing any behavior**.

```java
// ❌ Legacy-style code (your starting point - DO NOT run this as-is)
public class ModernRefactor {
    public static void main(String[] args) {
        // DO NOT change the logic — only refactor type declarations to use `var` where valid and clear
        
        String appName = "UserProcessor";
        List<String> roles = Arrays.asList("admin", "editor", "viewer");
        Map<String, Object> config = new HashMap<>();
        config.put("timeout", 30);
        config.put("retries", 3);
        
        System.out.println("App: " + appName);
        System.out.println("Roles: " + roles);
        System.out.println("Config: " + config);
        
        for (int i = 0; i < roles.size(); i++) {
            String role = roles.get(i);
            System.out.println("Processing role: " + role);
        }
        
        for (String role : roles) {
            if (role.equals("admin")) {
                System.out.println("→ Special access granted to: " + role);
            }
        }
        
        Path logPath = Path.of("app.log");
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line = reader.readLine();
            if (line != null) {
                System.out.println("Log preview: " + line);
            }
        } catch (IOException e) {
            System.out.println("Log file not found — using defaults.");
        }
    }
}
```

> ⚠️ **Note**: This code **won’t compile as-is** because `Files` and `Path` aren’t imported, and the file `app.log` may not exist. You’ll fix that too!



### ✅ **Your Task**

1. **Create `ModernRefactor.java`**
2. **Add necessary imports** (`java.nio.file.*`, `java.io.*`, `java.util.*`)
3. **Refactor all local variable declarations** to use `var` **where valid and clear**
4. **Keep logic 100% identical** — only change type declarations
5. **Handle the missing file gracefully** (your try-catch already does this)
6. **Add one commented-out example** of **invalid `var` usage** (e.g., field or uninitialized var) with a `// ❌ COMPILER ERROR` comment
7. **Ensure it compiles and runs cleanly**



### 🖨️ **Expected Output**
When you run it (even if `app.log` doesn’t exist), you should see:

```
App: UserProcessor
Roles: [admin, editor, viewer]
Config: {retries=3, timeout=30}
Processing role: admin
Processing role: editor
Processing role: viewer
→ Special access granted to: admin
Log file not found — using defaults.
```

> ✅ The output must match **exactly** — only your **variable declarations** change.



### 📝 **Rules to Follow**
| Do ✅ | Don’t ❌ |
|------|--------|
| Use `var` for local variables with clear initializers | Use `var` for fields or method parameters |
| Use `var` in `for` loops (both types) | Use `var` without an initializer |
| Use `var` in try-with-resources | Use `var` when type isn’t obvious (e.g., `var x = 5;`) |
| Keep all logic and output identical | Change any method calls or logic |



### 🔍 **Hints**
- You’ll need:
  ```java
  import java.io.*;
  import java.nio.file.*;
  import java.util.*;
  ```
- For the `HashMap`, you can write:
  ```java
  var config = new HashMap<String, Object>();
  ```
- The loop counters and enhanced-for variables are **perfect for `var`**
- The `BufferedReader` in try-with-resources? **Yes, use `var`!**


### 🧪 **Bonus Challenge (Optional)**
After your main refactor, add this at the end:

```java
// 💡 When NOT to use var:
int delayMs = 2500; // Clear it's milliseconds (int)
// var delayMs = 2500; // ❓ Is it int? long? ambiguous!
```



### ✅ **Deliverable**
Submit your `ModernRefactor.java` that:
- Compiles on JDK 10+
- Runs without errors
- Uses `var` correctly and cleanly
- Includes **one commented invalid example**
- Produces the exact expected output



Ready to refactor like a modern Java pro? 🛠️  
Go build it — and I’ll review your code for correctness, style, and `var` best practices! 😊