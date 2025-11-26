# 📌 Java Built-in Annotations: The Essential Reference  
*For Java 8 to Java 21+ Developers*

> ✅ Annotations are **metadata** — they describe code but don’t change behavior *by themselves*.  
> 🔌 Some annotations *enable* behavior — via the **compiler**, **build tools**, or **runtime reflection**.

Introduced in **Java 5**, annotations are now foundational to modern Java (Spring, Jakarta EE, JUnit, Lombok, etc.). This guide covers **only JDK-provided annotations** you’ll use *directly* in application code — no internals, no deprecated relics.


## 🏆 The Core Six: Annotations Every Java Developer Should Know

| Annotation | Retention | Target | Purpose | When to Use |
|-----------|-----------|--------|---------|-------------|
| [`@Override`](#override) | `SOURCE` | Method | Ensures method overrides superclass/interface | ✅ **Always** on overridden methods |
| [`@Deprecated`](#deprecated) | `RUNTIME` | Any | Marks obsolete API | When replacing/removing code |
| [`@SuppressWarnings`](#suppresswarnings) | `SOURCE` | Any | Suppresses compiler warnings | Only with justification + comment |
| [`@FunctionalInterface`](#functionalinterface) | `RUNTIME` | Interface | Documents SAM (single-abstract-method) interface | For all lambda-compatible interfaces |
| [`@SafeVarargs`](#safevarargs) | `RUNTIME` | Method/Constructor | Guarantees generic varargs method is heap-safe | For `final`/`static` generic varargs |
| [`@Repeatable`](#repeatable) | `RUNTIME` | Annotation type | Enables multiple uses of same annotation | When designing custom annotations |

> 🚫 **Removed / Avoid**:  
> - `@Contended` (`jdk.internal.vm.annotation.Contended`) — *internal JDK API*; not for application use.  
> - `@Profiled`, `@Native` — *deprecated or removed* in modern JDKs.

---

## 🔍 Deep Dive: Usage & Best Practices

### 1. `@Override` — Your Compile-Time Safety Net  
**Retention**: `SOURCE` | **Target**: `ElementType.METHOD`

### 🔍 What It Does
- Tells the compiler: *“This method *must* override a superclass/interface method.”*  
- If no matching method exists → **compile error** (not warning!).  
- Zero runtime cost — stripped at compile time (`RetentionPolicy.SOURCE`).

```java
@Override
public boolean equals(Object obj) { ... }
```

- ✅ Verifies the method *actually* overrides a superclass/interface method.  
- 🚫 Catches silent bugs (e.g., `hashcode()` → `hashCode()`).  
- 📌 **Always use it** — zero runtime cost, maximum correctness.

### 🛠️ Minimal Example
```java
// OverrideExample.java
public class OverrideExample {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.speak(); // "Woof!"
    }
}

class Animal {
    public void speak() {
        System.out.println("...");
    }
}

class Dog extends Animal {
    @Override  // ✅ Correct — overrides Animal.speak()
    public void speak() {
        System.out.println("Woof!");
    }

    // Uncomment to see error:
    // @Override
    // public void barks() { }  // ❌ Compile error: no method 'barks()' in superclass
}
```

✅ Run it:
```bash
javac OverrideExample.java && java OverrideExample
# Output: Woof!
```

### 🚨 Common Mistakes
| Mistake | Error | Fix |
|--------|-------|-----|
| Misspelled method name (`hashcode()`) | Silent bug (new method!) | `@Override` catches it |
| Wrong parameter type (`equals(String)`) | Silent bug | `@Override` → error |
| Missing `@Override` on interface default override | No error — but risky | ✅ Always add it |

### 💡 Real-World Use
- **JUnit 5**: `@Test` methods *must* be `void`, no-arg — but `@Override` isn’t used there.  
- **Your code**: Every `equals()`, `hashCode()`, `toString()`, `compareTo()` — **always** annotate.

---

### 2. `@Deprecated` — Respectful API Evolution  
**Retention**: `RUNTIME` | **Target**: Any

### 🔍 What It Does
- Generates **compile-time warning** when code is used.  
- Retained in bytecode (`RUNTIME`) → tools (IDEs, linters) can warn at edit time.  
- `forRemoval = true` → stronger signal (e.g., IntelliJ shows strikethrough).

```java
@Deprecated(since = "3.2", forRemoval = true)
public void legacyAuth() { ... }
```

- 🔔 Triggers compile-time warning on usage.  
- `since`: First version where deprecated.  
- `forRemoval = true`: Planned for deletion — migrate urgently.  
- 📌 **Always pair with Javadoc**:
  ```java
  /**
   * @deprecated Use {@link AuthService#login()} instead.
   */
  @Deprecated(since = "3.2")
  public void legacyAuth() { ... }
  ```
### 🛠️ Minimal Example
```java
// DeprecatedExample.java
public class DeprecatedExample {
    public static void main(String[] args) {
        LegacyLogger.log("Hello"); // ⚠️ Warning: 'log(String)' is deprecated
        NewLogger.log("Hello");    // ✅ Clean
    }
}

class LegacyLogger {
    @Deprecated(since = "2.0", forRemoval = true)
    public static void log(String msg) {
        System.out.println("[LEGACY] " + msg);
    }
}

class NewLogger {
    public static void log(String msg) {
        System.out.println("[NEW] " + msg);
    }
}
```

✅ Compile with warnings:
```bash
javac -Xlint:deprecation DeprecatedExample.java
# warning: [deprecation] log(String) in LegacyLogger has been deprecated
```

### 🚨 Common Mistakes
| Mistake | Risk | Fix |
|--------|------|-----|
| Deprecating without `since`/`forRemoval` | Unclear urgency | Always add `since` |
| Not updating Javadoc | Users don’t know *what* to use | Add `@deprecated Use X instead` |
| Deprecating and deleting in same version | Breaks clients | Deprecate in v1, remove in v2 |

### 💡 Real-World Use
- **Java SE**: `Thread.stop()`, `Date` constructors  
- **Spring**: `@EnableWebMvcSecurity` → replaced by `@EnableWebSecurity`  
- **Your feature flags**: Deprecate `DARK_MODE` before removing it.

---

### 3. `@SuppressWarnings` — Use *Only* with Proof  
**Retention**: `SOURCE` | **Target**: Any

### 🔍 What It Does
- Suppresses **specific compiler warnings** in scope (method/class).  
- Must specify warning key (e.g., `"unchecked"`).  
- `RetentionPolicy.SOURCE` → gone after compile.

```java
@SuppressWarnings("unchecked")
// Safe: generic array hidden in utility; no heap pollution
List<String> safeList = new ArrayList<>(Arrays.asList("a", "b"));
```

#### Common Values:
| Value | When to Use |
|-------|-------------|
| `"unchecked"` | Raw types, unchecked casts (e.g., legacy APIs) |
| `"deprecation"` | Intentionally using `@Deprecated` code |
| `"unused"` | Unused parameters in generated/override methods |
| `"preview"` | Using preview features (Java 12+) |

> ⚠️ **Golden Rule**:  
> - Never suppress without a **commented justification**.  
> - Prefer fixing the root cause (e.g., add generics) over suppression.

### 🛠️ Minimal Example
```java
// SuppressExample.java
import java.util.*;

public class SuppressExample {
    public static void main(String[] args) {
        List<String> safeList = createLegacyList();
        System.out.println(safeList); // [a, b]
    }

    @SuppressWarnings("unchecked")
    // Safe: legacy API returns raw List; we guarantee String contents
    private static List<String> createLegacyList() {
        // Simulate legacy method returning raw List
        List raw = Arrays.asList("a", "b");
        return raw; // unchecked cast
    }
}
```

✅ Run it:
```bash
javac -Xlint:unchecked SuppressExample.java  # no warning!
java SuppressExample
```

### 🚨 Common Mistakes
| Mistake | Risk | Fix |
|--------|------|-----|
| `@SuppressWarnings("all")` | Hides *real* bugs | Only suppress specific keys |
| No comment | Future devs won’t know why it’s safe | Always document |
| Suppressing at class level | Over-broad | Narrow to smallest scope (method/variable) |

### 💡 Real-World Use
- **JPA**: `@Query` with native SQL → `"unchecked"` on `List<?>` casts  
- **Jackson**: Generic deserialization → `"unchecked"`  
- **Your code**: When bridging to legacy APIs (e.g., pre-generics libraries)
---

### 4. `@FunctionalInterface` — Clarity for Lambdas  
**Retention**: `RUNTIME` | **Target**: `ElementType.TYPE` (interfaces only)


### 🔍 What It Does
- Documents and enforces: *“This interface has exactly one abstract method.”*  
- Enables lambda syntax: `Runnable r = () -> System.out.println("Hi");`  
- `RetentionPolicy.RUNTIME` → visible to tools (IDEs, Spring).


```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
    // default/static methods allowed
}
```

- ✅ Ensures exactly **one abstract method** (SAM).  
- 📌 Required for clarity — though lambdas work without it, *explicit intent matters*.  
- ❗ Fails compilation if interface has >1 abstract method.

### 🛠️ Minimal Example
```java
// FunctionalExample.java
@FunctionalInterface
interface Greeter {
    void greet(String name);  // only abstract method
    // default void log() { }  // ✅ allowed
}

public class FunctionalExample {
    public static void main(String[] args) {
        // Lambda usage:
        Greeter polite = name -> System.out.println("Hello, " + name + "!");
        polite.greet("Alice"); // Hello, Alice!

        // Method reference:
        Greeter loud = System.out::println;
        loud.greet("BOB"); // BOB

        // Uncomment to break:
        // void extra(); // ❌ Compile error: multiple abstract methods
    }
}
```

✅ Run it:
```bash
javac FunctionalExample.java && java FunctionalExample
```

### 🚨 Common Mistakes
| Mistake | Error | Fix |
|--------|-------|-----|
| Adding 2nd abstract method | Compile error | Keep only one |
| Using on class/enum | Compile error | Only for interfaces |
| Assuming required for lambdas | Not required — but *strongly* recommended | ✅ Always use it |

### 💡 Real-World Use
- **Java SE**: `Runnable`, `Callable`, `Predicate`, `Function`  
- **Spring**: `@FunctionalInterface` on `Converter`, `Validator`  
- **Your code**: Any callback interface (e.g., `FeatureFlagValidator<T>`)

---

### 5. `@SafeVarargs` — For Generic Varargs  
**Retention**: `RUNTIME` | **Target**: `ElementType.METHOD`, `ElementType.CONSTRUCTOR`

### 🔍 What It Does
- Asserts: *“This method doesn’t store the varargs array — no heap pollution.”*  
- Required for `final`/`static` generic varargs (else compiler warns).  
- `RetentionPolicy.RUNTIME` → visible via reflection.

```java
@SafeVarargs
public static <T> Set<T> union(T... elements) {
    return Set.of(elements); // Safe: JDK 9+ Set.of is heap-safe
}
```

- 🛡️ Asserts the method doesn’t store the varargs array (prevents heap pollution).  
- Required for `final`/`static` generic varargs methods (else warning).  
- 📌 Only use after auditing for safety — never blindly.


### 🛠️ Minimal Example
```java
// SafeVarargsExample.java
import java.util.*;

public class SafeVarargsExample {
    public static void main(String[] args) {
        Set<String> set = union("a", "b", "c");
        System.out.println(set); // [a, b, c] (order may vary)
    }

    @SafeVarargs
    @SuppressWarnings("varargs") // Required for non-final methods in Java < 9
    public static <T> Set<T> union(T... elements) {
        // Safe: Set.of() doesn't store the array (JDK 9+)
        return Set.of(elements);
    }
}
```

✅ Run it (Java 17):
```bash
javac SafeVarargsExample.java && java SafeVarargsExample
```

### 🚨 Common Mistakes
| Mistake | Risk | Fix |
|--------|------|-----|
| Using on non-`final`/non-`static` method | Warning (not error) | Add `@SuppressWarnings("varargs")` *only if truly safe* |
| Storing varargs array | Heap pollution (ClassCastException at runtime!) | Never do `T[] arr = elements;` |
| Blindly adding annotation | False sense of security | Audit first!

### 💡 Real-World Use
- **Java SE**: `Arrays.asList()`, `EnumSet.of()`, `Collections.addAll()`  
- **JUnit**: `assertAll(Executable...)`  
- **Your code**: Utility methods like `combineErrors(Error...)`

---

### 6. `@Repeatable` — Multiple Annotations, Clean Syntax  
**Retention**: `RUNTIME` | **Target**: `ElementType.ANNOTATION_TYPE`

### 🔍 What It Does
- Enables **multiple uses** of the same annotation on one element.  
- Requires a *container annotation* (e.g., `@Authorizations`).  
- `RetentionPolicy.RUNTIME` → visible via reflection.

```java
@Repeatable(Authorizations.class)
public @interface Authorize {
    String role();
}

public @interface Authorizations {
    Authorize[] value();
}

// Usage:
@Authorize("ADMIN")
@Authorize("AUDITOR")
public void sensitiveOperation() { ... }
```

- ✅ Enables natural, readable repeated annotations.  
- 📌 Required when designing annotations meant for multiple application.


### 🛠️ Minimal Example
```java
// RepeatableExample.java
import java.lang.annotation.*;

@Repeatable(Authorizations.class)
@interface Authorize {
    String role();
}

@interface Authorizations {
    Authorize[] value();
}

class SecureService {
    @Authorize("ADMIN")
    @Authorize("AUDITOR")
    public void deleteData() {
        System.out.println("Data deleted");
    }
}

public class RepeatableExample {
    public static void main(String[] args) throws Exception {
        var method = SecureService.class.getMethod("deleteData");
        
        // Read repeated annotations:
        Authorize[] auths = method.getAnnotationsByType(Authorize.class);
        for (Authorize a : auths) {
            System.out.println("Requires: " + a.role());
        }
        // Output:
        // Requires: ADMIN
        // Requires: AUDITOR
    }
}
```

✅ Run it:
```bash
javac RepeatableExample.java && java RepeatableExample
```

### 🚨 Common Mistakes
| Mistake | Error | Fix |
|--------|-------|-----|
| Forgetting container annotation | Compile error | Define `@Authorizations` |
| Using on non-annotation type | Compile error | Only on `@interface` |
| Not using `getAnnotationsByType()` | Misses repeats | Use `AnnotatedElement.getAnnotationsByType()`

### 💡 Real-World Use
- **Jakarta EE**: `@RolesAllowed("ADMIN")`, `@RolesAllowed("USER")`  
- **Spring Security**: `@PreAuthorize` × multiple  
- **Your code**: `@FeatureFlag(DARK_MODE)`, `@FeatureFlag(BETA_CHAT)` on one method

---

## 🚫 What’s *Not* in This List (And Why)

| Annotation | Status | Reason |
|-----------|--------|--------|
| `@Retention`, `@Target`, `@Inherited`, `@Documented` | ✅ Valid | But **meta-annotations** — used *only when defining custom annotations*, not in app code |
| `@Profiled`, `@Native` | ❌ Removed | Deprecated in Java 9+, gone in Java 11+ |
| `@Contended` | ⚠️ Internal | `jdk.internal.*` — *never use in production* |

> 🔑 **Key Insight**:  
> You use **the Core Six** daily.  
> You use **meta-annotations** only when *building frameworks or libraries*.



## ✅ Best Practices Summary

| Do ✅ | Don’t ❌ |
|------|----------|
| Always use `@Override` | Suppress `@Override` warnings |
| Deprecate *before* deletion | Remove public API without deprecation |
| Comment *why* you suppress warnings | Use `@SuppressWarnings` silently |
| Prefer `@FunctionalInterface` | Rely on implicit SAM detection |
| Audit before `@SafeVarargs` | Apply it without review |


## 📚 Official References
- [JLS §9.6: Predefined Annotations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.6)  
- [Java Annotation API (java.lang)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/package-summary.html#annotation.type)  
- [Java Language Guide: Annotations](https://docs.oracle.com/javase/tutorial/java/annotations/)

