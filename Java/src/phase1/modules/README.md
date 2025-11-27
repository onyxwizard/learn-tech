# 📖 Chapter 1: Introduction  
## 1. Overview 🌍

> 💡 *Before diving into syntax and declarations — pause: Why did Java need modules at all? What problem does JPMS solve that packages couldn’t?*

Java 9 introduced a groundbreaking architectural shift: the **Java Platform Module System (JPMS)** — more commonly called **Modules**. 📦➡️🧱

This isn’t just “packages, but bigger.” It’s a **new level of encapsulation and dependency management**, sitting *above* packages and *below* JARs — bringing true modularity to the Java platform for the first time.

### 🔑 Core Ideas
| Concept | Before JPMS | With JPMS |
|--------|-------------|-----------|
| **Encapsulation** | `public` = visible to *all* | `public` + **module export** = visible *only to allowed modules* |
| **Dependencies** | Implicit (classpath chaos 🌪️) | Explicit (`requires` declarations ✅) |
| **JRE Size** | Monolithic `rt.jar` (~70MB+) | Slim, custom runtimes via `jlink` 🛠️ |

> 🤔 *Reflect: Have you ever faced “Jar Hell”? Classpath conflicts? Accidental API exposure? JPMS was born from these pains.*

### 🎯 What We’ll Do
In this tutorial, we’ll:
- ✅ Understand module declarations (`module-info.java`)
- ✅ Explore `requires`, `exports`, `opens`, `uses`, `provides`
- ✅ Build a small, modular project step-by-step — *learning by doing*
- ✅ See how `jdeps`, `jlink`, and `jmod` empower modular deployment

> 🚀 **Our goal**: Not just *know* modules — but *think* in modules.

Let’s begin our journey — from the monolith 🏰 to the modular world 🧩.

---

# 📦 Chapter 2: What *Is* a Module?  
> 🔍 *Before asking “How do I use modules?” — let’s ask: What problem does a “module” *actually* solve? What makes it more than just a folder of packages?*

A **module** is not just a bundle — it’s a *contract*.  
It groups **related packages + resources**, and—crucially—declares **what it offers**, **what it needs**, and **what it hides**.

Think of it as:  
> 📦 **A package of packages** — *plus intentionality*.

Let’s unpack its anatomy 🧬:

## 2.1 Packages — The Familiar Foundation 🧱  
✅ Still the same `com.example.util` you know and love.  
✅ Still organize code, avoid naming collisions.  

➡️ **But now**:  
Even if a class is `public`, it’s **not accessible** outside the module *unless* its package is explicitly **exported**.  
> 🤔 *Pause: Why might hiding public APIs be a good thing? (Hint: stability, security, maintainability.)*


## 2.2 Resources — No More “Where’s That Config?” 🖼️⚙️  
Before JPMS: resources scattered in `src/main/resources`, global classpath — hard to trace ownership.  
With modules:  
- Resources live *alongside* the code that uses them.  
- Each module ships its own assets (images, configs, i18n files).  
- No more accidental overwrites or “who owns `logback.xml`?” debates.  

➡️ **Result**: *Self-contained*, *relocatable* units. 🎒


## 2.3 The Heart: `module-info.java` ❤️📜  
This tiny file is where intention becomes reality. It declares:

| Directive | Purpose | Default? |
|---------|---------|----------|
| `module my.app {` | **Name** — e.g., `com.baeldung.core` (Reverse-DNS ✅) or `my.app` (project-style ✅) | — |
| `requires java.sql;` | **Dependencies** — explicit, compile-time enforced | ❌ (`NoClassDefFoundError` if missing) |
| `exports com.myapp.api;` | **Public API** — only exported packages are visible externally | ❌ (all packages *private* by default!) |
| `opens com.myapp.internal;` | **Reflection access** — allows frameworks (e.g., Spring, Hibernate) to access non-public members | ❌ (reflection blocked by default!) |
| `provides MyService with MyServiceImpl;` | **Service provider** — contributes an implementation | — |
| `uses MyService;` | **Service consumer** — declares intent to use a service | — |

> 📝 **Naming tip**: Use `lower.case.with.dots` — *no dashes*, *no uppercase*.  
> 🛑 **Critical insight**: *Encapsulation is now strict by default.* Freedom requires explicit permission.


## 2.4 Module Types — Who’s on the Path? 🧭  
Not all modules are created equal. The JVM sees four kinds:

| Type | How It’s Loaded | Access Privileges | Example |
|------|-----------------|-------------------|---------|
| **System Modules** 🖥️ | Built into JDK (`java.base`, `java.sql`, etc.) | Highly restricted; `java.base` is the root | `java.base`, `jdk.jshell` |
| **Application Modules** 🧩 | `module-info.java` → `module-info.class` in JAR | Full JPMS rules apply | *Our own code* ✅ |
| **Automatic Modules** 🤖 | Plain JAR on `--module-path` (no `module-info`) | *Reads all modules* ⚠️ (loose coupling) | Legacy `commons-lang3-3.12.jar` |
| **Unnamed Module** 🕳️ | On `--class-path` (legacy mode) | Reads *all* modules, but *exports nothing* | Old-school classpath apps |

> 💡 *Reflect: Why might automatic modules be a “bridge,” not a destination? What risks do they introduce?*


## 2.5 Distribution — One Module, One JAR 📦📦  
🔧 **Rule**: **One module = one JAR**.  
You *can* distribute as:
- ✅ A standard modular JAR (`META-INF/versions/9/module-info.class` inside)
- ✅ An “exploded” directory (e.g., during dev/testing)

📦 For multi-module projects (e.g., `app` + `core` + `utils`):  
→ Each module builds to its own JAR → assembled together at runtime.

> ⚠️ **Gotcha**: Trying to cram two modules into one JAR? The JVM will reject it. 🚫


## 🧩 Summary: A Module Is…  
> A **named**, **self-describing**, **encapsulated** unit of code + resources  
> — with explicit dependencies, APIs, and boundaries.  

It turns *implicit assumptions* into *explicit contracts*.  

Ready to see it in action? 🛠️  
➡️ In the next chapter, we’ll **build our first module** — from `module-info.java` to runtime.

---

# 🧱 Chapter 3: Default Modules — The JDK’s Modular Heart  
> 💡 *Before writing your first `module-info.java`, pause: What does the JDK itself look like now? If *it* is modular, what can we learn from its design?*

With Java 9+, the JDK itself was refactored into **modules** — no more monolithic `rt.jar`! 🪓➡️🧩  
This wasn’t just internal cleanup: it enables **custom runtimes**, **stronger security**, and **faster startup**.

Let’s explore this new landscape.

## 🔍 Discovering System Modules  
Run this in your terminal:  
```bash
java --list-modules
```

You’ll see dozens of entries like:
```
java.base@17
java.sql@17
java.xml@17
jdk.jconsole@17
javafx.controls@17   # if installed
```
Each is a **named, self-contained system module** — compiled, versioned, and interdependent.


## 🧭 The Four Module Families  
The JDK’s modules fall into four logical groups — each with a purpose and visibility boundary:

| Prefix | Purpose | Examples | Key Insight |
|--------|---------|----------|-------------|
| `java.*` 🌐 | **Java SE Platform API** — *what you’re allowed to depend on in portable apps* | `java.base`, `java.sql`, `java.xml`, `java.desktop` | `java.base` is the **root** — *every* module implicitly `requires` it 🌱 |
| `javafx.*` 🖼️ | **JavaFX UI Toolkit** (modularized separately since Java 11) | `javafx.controls`, `javafx.fxml` | Not part of SE — must be added explicitly (e.g., via SDK or Maven) |
| `jdk.*` ⚙️ | **JDK Tools & Implementation Details** — *internal to the JDK* | `jdk.jshell`, `jdk.compiler`, `jdk.jdi` | ❗ Avoid depending on these — no stability guarantees! |
| `oracle.*` 🛡️ | **Oracle-Specific Extensions** (e.g., commercial features) | `oracle.jdbc`, `oracle.security` | Vendor-specific — not portable across JVMs (OpenJDK won’t have these) |

> 🤔 *Reflect: Why separate `java.*` (public spec) from `jdk.*` (implementation)? How does this help long-term evolution and security?*



## 🌐 The Module Graph — Dependencies in Action  
Every module declares its dependencies. For example:
- `java.sql` → `requires java.logging`, `requires java.xml`, `requires transitive java.base`
- `java.desktop` → `requires java.prefs`, `requires transitive java.datatransfer`

You can visualize dependencies with:  
```bash
java --list-modules --verbose   # or
jdeps --list-deps $(java --list-modules | grep java.base | cut -d@ -f1)
```

👉 This is *exactly* the same model you’ll use for your own modules — just scaled up.  
The JDK is the ultimate case study in modular design. 📚


## ✅ Key Takeaways  
- ✅ The JDK is now a **collection of modules**, not a single JAR.  
- ✅ `java.base` is the universal foundation — minimal, essential, and stable.  
- ✅ **Separation of concerns** is enforced: public API (`java.*`) vs. internal tools (`jdk.*`).  
- ✅ Your app’s modules will sit *alongside* these — depending only on what they truly need.

> 🚀 Next: Let’s create our *own* module — and see how it integrates with `java.base`, `java.sql`, or others.

---

# 📜 Chapter 4: Module Declarations — The Contract of Intent  
> 🤔 *Before writing `module-info.java`: What makes a good contract? Clarity? Minimalism? Explicit boundaries? Modules force us to negotiate these intentionally.*

Every module starts with one file:  
📁 `module-info.java` — at the **root** of your source tree (side-by-side with your top-level package).  

This is your **module’s manifesto** — declaring *what it is*, *what it needs*, and *what it offers*.

```java
module my.app { 
    // directives go here — all optional, but rarely *all* omitted!
}
```

Let’s explore each directive — not just *what it does*, but *when (and why) to use it*.

## 🔗 4.1 `requires` — The Baseline Dependency  
```java
requires java.sql;
```
✅ **What**: Declares a *mandatory* compile-**and**-runtime dependency.  
✅ **Effect**: Public types from `java.sql` (e.g., `Connection`, `Statement`) are now usable in your module.  
⚠️ **Note**: If `java.sql` isn’t on the module path? → **compile error**.

> 💡 *Ask: Is this dependency truly required for my module to function? If yes → `requires`.*

## ⚖️ 4.2 `requires static` — Optional at Runtime  
```java
requires static org.slf4j;
```
✅ Compile-time only.  
✅ Your code can *reference* SLF4J types — but if SLF4J isn’t present at runtime? → **no error** (assuming you guard usage with `Class.forName()` or DI).  
🎯 Use case: Optional integrations (logging, metrics, debug tooling).

> 🤔 *Reflect: How does this help library authors avoid “dependency bloat” for consumers?*

## 🌉 4.3 `requires transitive` — “Bring My Friends”  
```java
requires transitive com.fasterxml.jackson.databind;
```
✅ If Module A `requires` your module → A *automatically* reads `jackson.databind`, too.  
✅ Critical for *API libraries* where your public types *return* or *accept* types from a dependency.

> 🚨 Anti-pattern: Overusing `transitive` → unnecessary coupling.  
> ✅ Best practice: Only for dependencies whose types *leak* into your public API.

## 🚪 4.4 `exports` — Opening the Gate (Selectively)  
```java
exports com.myapp.api;
```
✅ Makes `public` types in `com.myapp.api` accessible to *all* modules that `require` yours.  
🔒 **Default**: All packages are *module-private* — even `public` classes are hidden.

> 💡 *Rule of thumb: Export only your *stable*, *intended* public API — not internals.*


## 🎯 4.5 `exports … to` — Invite-Only Access  
```java
exports com.myapp.internal to com.myapp.test, com.myapp.debug;
```
✅ Grants access *only* to specified modules.  
🛡️ Use for:  
- Test-only APIs  
- Friend modules (e.g., `core` → `cli` and `gui`, but not public consumers)

> 🤫 *Security win: Your “internal” stays internal — except for trusted allies.*


## 🔌 4.6 `uses` — “I Consume This Service”  
```java
uses javax.persistence.PersistenceProvider;
```
✅ Declares: *“I will look up implementations of this service interface at runtime (via `ServiceLoader`)”*.  
✅ Does **not** imply `requires` — the *provider* module supplies the interface + impl.

> 🧩 Key insight: Decouples *consumers* from *providers*. Your module only needs the *interface*, not the impl.


## 🎁 4.7 `provides … with` — “I Am a Service Provider”  
```java
provides javax.persistence.spi.PersistenceProvider 
    with com.myapp.MyPersistenceProvider;
```
✅ Registers your class as an implementation of a service.  
✅ At runtime, `ServiceLoader.load(PersistenceProvider.class)` will find it — *if* your module is on the module path.

> 🔄 Pattern: Clean separation of *API* (in one module) and *implementations* (in others).

## 🪞 4.8 `open module` — Full Reflection (Use Sparingly!)  
```java
open module my.app {}
```
✅ Grants *all* modules full reflective access to *all* packages (including private members).  
⚠️ **Only** use for:  
- Legacy frameworks that *require* deep reflection (e.g., older Hibernate, Spring versions)  
- Quick prototyping — **not** production!

> 🚫 Avoid if possible — breaks encapsulation.


## 🔍 4.9 `opens` — Reflect on This Package  
```java
opens com.myapp.config;
```
✅ Grants *all* modules reflective access to *one package*.  
✅ Safer than `open module` — but still broad.

> 💡 Use when a *specific* package needs injection/mapping (e.g., config beans).


## 🎯 4.10 `opens … to` — Reflection, by Invitation Only  
```java
opens com.myapp.domain to spring.core, hibernate.core;
```
✅ Grants reflective access *only* to listed modules.  
✅ **Best practice** for modern apps: explicit, minimal, secure.

> 🏆 Gold standard for production modules needing framework integration.

## 🧩 Putting It All Together — A Realistic Example  
```java
module com.baeldung.app {
    requires java.sql;
    requires static org.slf4j;
    requires transitive com.fasterxml.jackson.core;

    exports com.baeldung.api;
    exports com.baeldung.spi to com.baeldung.impl;

    opens com.baeldung.domain to spring.core;

    uses com.baeldung.spi.Plugin;
    provides com.baeldung.spi.Plugin with com.baeldung.plugins.DefaultPlugin;
}
```
> ✅ Minimal dependencies  
> ✅ Clear API boundaries  
> ✅ Secure reflection  
> ✅ Service-based extensibility


## 📌 Pro Tips  
- 📏 **Keep `module-info.java` clean**: Group related directives (e.g., all `requires`, then `exports`, etc.).  
- 🧪 **Test early**: Use `jdeps` to analyze dependencies; `java --describe-module` to inspect at runtime.  
- 🛑 **Avoid**: `exports`/`opens` to `ALL-UNNAMED` — it weakens modularity.

---
# ⚙️ Chapter 5: Command-Line Mastery — Beyond `javac` & `java`  
> 🤔 *If modules are declared in `module-info.java`, why do we need CLI flags? When does runtime flexibility outweigh compile-time rigidity?*

While Maven/Gradle handle most build plumbing, **CLI options give you surgical control** — for:
- 🐞 Debugging module resolution  
- 🧪 Patching or overriding in development  
- 🛠️ Running legacy code in modular JVMs  
- 🔍 Understanding *how* the module system really works

Let’s demystify the key flags — with *why*, *when*, and *how*.


## 🧭 Essential Module Path Flags

| Flag | Purpose | Example | When to Use |
|------|---------|---------|-------------|
| `--module-path` (or `-p`) | 🔗 **Where to find modules** (replaces `CLASSPATH` for modular code) | `java -p mods:lib -m my.app/com.myapp.Main` | ✅ Always — for any modular app |
| `--class-path` (or `-cp`) | 🕳️ For *non-modular* (unnamed module) code only | `java -cp legacy.jar com.LegacyApp` | ⚠️ Avoid mixing with `-p` unless bridging old/new |

> 💡 **Pro tip**: `-p mods` = `mods/` contains JARs (or exploded dirs) with `module-info.class`.

## 🛠️ Runtime Overrides — “Dynamic Directives”

These let you *patch* module behavior **without recompiling** — powerful, but use with care.

| Flag | Replaces | Example | Why? |
|------|----------|---------|------|
| `--add-reads <module>=<other>` | `requires` (but runtime-only) | `--add-reads my.app=java.sql` | 🔧 Fix missing `requires` in 3rd-party JARs (e.g., automatic modules) |
| `--add-exports <module>/<pkg>=<target>` | `exports … to` | `--add-exports java.base/sun.nio.ch=my.app` | 🚨 Access *internal JDK APIs* (e.g., for performance hacks — **not recommended for prod!**) |
| `--add-opens <module>/<pkg>=<target>` | `opens … to` | `--add-opens java.base/java.lang=my.app` | 🧪 Allow reflection into JDK internals (e.g., for testing, mocking, or legacy frameworks) |
| `--patch-module <module>=<path>` | Replace/extend a module | `--patch-module java.base=patches/` | 🛠️ Hotfix JDK bugs during dev; inject test doubles |

> ⚠️ **Warning**: Overuse breaks encapsulation — these are *escape hatches*, not design features.  
> 🤔 *Reflect: How might `--add-opens` help migrate a Spring 4 app to Java 17? What trade-offs does it introduce?*


## 📋 Inspection & Control

| Flag | Purpose | Example | Insight |
|------|---------|---------|---------|
| `--list-modules` | 📜 Show all *resolved* modules (name + version) | `java --list-modules \| grep java.` | See what’s *actually* loaded — including automatic modules |
| `--describe-module <name>` | 🔍 Deep-dive into a module’s structure | `java --describe-module java.sql` | View exports, requires, services — like `module-info.java` at runtime! |
| `--add-modules <mod1>,<mod2>` | ➕ Explicitly resolve extra modules | `--add-modules java.xml.bind` (in Java 9–10) | Needed for modules *not* required by your app but used indirectly (e.g., via reflection) |


## 🛡️ Strong Encapsulation — The `--illegal-access` Lever  
Java 9+ blocks illegal reflective access by default — but offers a grace period:

| Mode | Effect | CLI | Reality Check |
|------|--------|-----|---------------|
| `permit` (default ≤ Java 16) | 🟡 Warn *once* at startup | `--illegal-access=permit` | “Works, but noisy” — deprecated in Java 17+ |
| `warn` | 🟠 Warn *every time* illegal access occurs | `--illegal-access=warn` | Find hidden reflection issues |
| `deny` (default ≥ Java 17) | 🔴 **Fail fast** on illegal access | `--illegal-access=deny` | ✅ **Production best practice** |

> 💡 In Java 17+, `--illegal-access` is **ignored** — illegal access is *always denied*.  
> 🛠️ Fix properly with `--add-opens` or refactor.

## 🧪 Real-World Example: Running a “Broken” Modular App  
Imagine `my-app.jar` forgets to `requires java.sql` — but uses JDBC.

❌ Fails with:  
`java.lang.module.ResolutionException: Module my.app does not read module java.sql`

✅ Fix temporarily via CLI:  
```bash
java \
  --module-path mods \
  --add-reads my.app=java.sql \
  -m my.app/com.myapp.Main
```

→ Works! But now you know: go fix `module-info.java` 🛠️.


## ✅ Key Principles  
- **Compile-time declarations > runtime overrides** — use CLI for debugging, not design.  
- **Least privilege**: Prefer `--add-opens … to my.module` over global opens.  
- **Know your defaults**: `--illegal-access=deny` is the new normal in modern Java.

---
# 🔐 Chapter 6: Visibility & Reflection — The New Rules of Access  
> 🤔 *Before Java 9: “If it’s loaded, I can reflect on it.”  
> After Java 9: “If it’s not explicitly opened — no reflection, not even with `setAccessible(true)`.”  
> Why did this change? What does “secure by default” really mean?*

Strong encapsulation isn’t just about hiding code — it’s about **predictability, security, and evolvability**.  
But yes — it *does* break reflection-heavy frameworks. 😅 Let’s navigate this wisely.


## 🧱 The New Visibility Hierarchy  
In Java 9+, accessibility is a **two-layer gate**:

| Layer | Gatekeeper | What It Controls |
|-------|------------|------------------|
| **1. Module Readability** | `requires` / `--add-reads` | Can Module A *see* Module B at all? |
| **2. Package Accessibility** | `exports` / `opens` / CLI flags | Can Module A access *types* or *members* in Module B’s packages? |

➡️ **Both must be satisfied** — even for reflection.


## 🔍 What’s *Really* Accessible? (By Default)

| Member Type | Normal Access (`new`, method call) | Reflection (`getDeclaredField() + setAccessible(true)`) |
|-------------|-----------------------------------|--------------------------------------------------------|
| `public` in **exported** package | ✅ Yes | ✅ Yes |
| `public` in **non-exported** package | ❌ No | ❌ No |
| `private`/`protected`/package-private in **exported** package | ❌ No | ❌ **No** → `InaccessibleObjectException`! |
| Any member in **opened** package | ❌ (compile) / ✅ (runtime via reflection) | ✅ Yes — *if module opened it to you* |

> 💥 Critical: `setAccessible(true)` **does not bypass module encapsulation**.  
> It only bypasses *Java language* access checks — not *module system* checks.


## 🛠️ How to Grant Reflection Access (The Right Way)

### ✅ Preferred: Declare It in `module-info.java`
| Directive | Scope | When to Use |
|----------|-------|-------------|
| `open module my.module { }` | Entire module | ✅ Quick dev/test; framework-heavy apps (e.g., older Spring) |
| `opens com.my.pkg;` | One package → *all modules* | ⚠️ Rare — too permissive |
| `opens com.my.pkg to spring.core, junit;` | One package → *specific modules* | 🏆 **Production best practice** |

```java
// module-info.java — clean, intentional, auditable
module com.baeldung.app {
    opens com.baeldung.domain to spring.core, hibernate.core;
    opens com.baeldung.config to spring.core;
}
```

### 🛠️ Escape Hatch: CLI Overrides (When You Can’t Change the Module)
If you’re using a *3rd-party library* that’s not modular (or poorly modularized):

```bash
java \
  --module-path mods \
  --add-opens java.base/java.lang=com.example.app \
  --add-opens java.desktop/sun.awt=com.example.app \
  -m com.example.app/com.example.Main
```

> 🎯 Use cases:
> - Running legacy frameworks on Java 17+
> - Patching missing `opens` in automatic modules
> - CI/CD environments where you control JVM args

> ⚠️ **Limitations**:
> - Requires control over launch command (❌ not possible in some cloud/serverless envs)
> - Doesn’t help if the *framework itself* doesn’t use `setAccessible(true)` properly

## 🧪 Real-World Examples

### 🔧 Spring Boot (Pre-3.0)  
Many beans use reflection on `private` fields.  
✅ Fix:  
```java
opens com.myapp.domain to spring.core, spring.beans;
```
Or (temporarily):  
```bash
--add-opens com.myapp/com.myapp.domain=spring.core
```

### 🧪 JUnit 5  
Uses reflection to instantiate/test `private` methods.  
✅ Fix:  
```java
opens com.myapp to org.junit.platform.commons;
```
(Or use `@ExtendWith` and public test methods — even better! 🌟)

## 🚫 Anti-Patterns to Avoid
| What | Why It’s Bad |
|------|--------------|
| `--add-opens ALL-UNNAMED=ALL-UNNAMED` | ❌ Defeats modularity; insecure |
| Exporting internal packages just for reflection | ❌ Confuses API contract (`exports` ≠ `opens`!) |
| Ignoring `InaccessibleObjectException` | ❌ Hides design debt — will break in future JDKs |

## 💡 Pro Tips for Library Authors
1. **Separate API from implementation**:  
   - `exports` your public interfaces  
   - `opens` only internal packages *to your own test module*
2. **Prefer constructor/setter injection** over field injection — reduces reflection needs.
3. **Document reflection requirements** in your module README:  
   > ℹ️ *This module requires `--add-opens com.lib/internal=your.app` if used with Framework X.*


## 🔄 The Bigger Picture  
This shift isn’t about making life harder — it’s about:  
- 🛡️ Preventing accidental coupling to internals (e.g., `sun.misc.Unsafe`)  
- 🚀 Enabling JVM optimizations (e.g., ahead-of-time compilation, smaller images)  
- 🌱 Allowing JDK teams to *evolve* internal APIs safely  

> As Brian Goetz said:  
> *“Modules don’t take away reflection — they take away *surprise* reflection.”*
---
# 🧩 Chapter 7: Putting It All Together — A Modular Hello World  
> 🤔 *Now that we know the rules — can we *feel* modularity? Let’s build, break, and fix — with nothing but `javac`, `java`, and intention.*

We’ll create a **two-module app** — then extend it with **services** — all from the command line.  
No Maven. No Gradle. Just pure JPMS. 🖥️✨


## 📂 7.1 Project Structure — Modular by Design  
Let’s build a clean, scalable layout:

```bash
mkdir -p module-project/simple-modules
cd module-project
```

📁 Final structure:
```
module-project/
├── compile-simple-modules.sh   # ← build script
├── run-simple-module-app.sh    # ← run script
└── simple-modules/
    ├── hello.modules/          # ← Library module
    │   ├── module-info.java
    │   └── com/baeldung/modules/hello/
    │       ├── HelloModules.java
    │       └── HelloInterface.java   # ← added later
    │
    └── main.app/               # ← Application module
        ├── module-info.java
        └── com/baeldung/modules/main/
            └── MainApp.java
```

> 💡 **Why this layout?**  
> - `simple-modules/` isolates *all* modules — easy to add more (`util`, `config`, etc.)  
> - Flat sibling structure → clean `--module-source-path`  


## 📦 7.2 Module 1: `hello.modules` — The API Provider  

### ✅ Step 1: Create the class  
`simple-modules/hello.modules/com/baeldung/modules/hello/HelloModules.java`  
```java
package com.baeldung.modules.hello;

public class HelloModules {
    public static void doSomething() {
        System.out.println("Hello, Modules!");
    }
}
```

### ✅ Step 2: Declare the module  
`simple-modules/hello.modules/module-info.java`  
```java
module hello.modules {
    exports com.baeldung.modules.hello;
}
```

> 🤔 *Reflect: What happens if we omit `exports`? Try it — see the compile error!*  
> 🔒 **Encapsulation in action**: Without `exports`, `HelloModules` is *invisible* — even though it’s `public`.


## 🚀 7.3 Module 2: `main.app` — The Consumer  

### ✅ Step 1: Declare dependency  
`simple-modules/main.app/module-info.java`  
```java
module main.app {
    requires hello.modules;  // ← explicit, compile-time enforced
}
```

### ✅ Step 2: Use the API  
`simple-modules/main.app/com/baeldung/modules/main/MainApp.java`  
```java
package com.baeldung.modules.main;

import com.baeldung.modules.hello.HelloModules;

public class MainApp {
    public static void main(String[] args) {
        HelloModules.doSomething();  // ← works because package is exported!
    }
}
```

> 💡 **Note**: No `import static` needed — `doSomething()` is *static*, not a service (yet!).


## 🔨 7.4 Build Script — One Command to Rule Them All  

`compile-simple-modules.sh`  
```bash
#!/usr/bin/env bash
set -e  # exit on error

echo "🔍 Compiling all modules..."
javac \
  -d outDir \
  --module-source-path simple-modules \
  $(find simple-modules -name "*.java")

echo "✅ Modules built to: outDir/"
ls -R outDir
```

🔑 Key flags:  
- `-d outDir` → output directory  
- `--module-source-path simple-modules` → tells `javac`: *“This is a multi-module project”*  
- `$(find ...)` → auto-includes all `.java` files (no manual lists!)

> 🛠️ **Run it**:  
> ```bash
> chmod +x compile-simple-modules.sh
> ./compile-simple-modules.sh
> ```

✔️ Expect:  
```
outDir/
├── hello.modules/
│   └── com/baeldung/modules/hello/HelloModules.class
└── main.app/
    └── com/baeldung/modules/main/MainApp.class
```


## ▶️ 7.5 Run It — The Moment of Truth!  

`run-simple-module-app.sh`  
```bash
#!/usr/bin/env bash
java \
  --module-path outDir \
  -m main.app/com.baeldung.modules.main.MainApp
```

> 🔑 `--module-path outDir` = where to find compiled modules  
> 🔑 `-m main.app/...` = run `MainApp.main()` in module `main.app`

🚀 **Run it**:  
```bash
chmod +x run-simple-module-app.sh
./run-simple-module-app.sh
```

🎯 **Expected output**:  
```
Hello, Modules!
```

🎉 **Success!** You’ve built your first modular app.  
> 🤔 *What if you swap `requires hello.modules` → `requires static hello.modules` and remove the call? Does it still compile? Run?*


## 🔌 7.6 Level Up: Services with `provides … with` & `uses`  

Let’s replace static calls with **pluggable services** — the *real* power of JPMS.

### ✅ Step 1: Define the service interface  
`simple-modules/hello.modules/com/baeldung/modules/hello/HelloInterface.java`  
```java
package com.baeldung.modules.hello;

public interface HelloInterface {
    void sayHello();
}
```

### ✅ Step 2: Implement it  
Update `HelloModules.java`:  
```java
public class HelloModules implements HelloInterface {  // ← now an impl
    public static void doSomething() {
        System.out.println("Hello, Modules!");
    }

    @Override
    public void sayHello() {
        System.out.println("Hello from Service!");
    }
}
```

### ✅ Step 3: Declare the service provider  
Update `hello.modules/module-info.java`:  
```java
module hello.modules {
    exports com.baeldung.modules.hello;

    provides com.baeldung.modules.hello.HelloInterface  // ← service contract
        with com.baeldung.modules.hello.HelloModules;     // ← implementation
}
```

### ✅ Step 4: Declare the consumer  
Update `main.app/module-info.java`:  
```java
module main.app {
    requires hello.modules;
    uses com.baeldung.modules.hello.HelloInterface;  // ← "I will load this service"
}
```

### ✅ Step 5: Load the service  
Update `MainApp.java`:  
```java
package com.baeldung.modules.main;

import com.baeldung.modules.hello.HelloInterface;
import java.util.ServiceLoader;

public class MainApp {
    public static void main(String[] args) {
        // Static call (still works)
        com.baeldung.modules.hello.HelloModules.doSomething();

        // Service-based call (new!)
        ServiceLoader<HelloInterface> loader = ServiceLoader.load(HelloInterface.class);
        HelloInterface service = loader.findFirst()
            .orElseThrow(() -> new RuntimeException("No HelloInterface found!"));
        service.sayHello();
    }
}
```

### ✅ Step 6: Recompile & Run  
```bash
./compile-simple-modules.sh
./run-simple-module-app.sh
```

🎯 **New output**:  
```
Hello, Modules!
Hello from Service!
```

> 🎯 **Why this matters**:  
> - Your app no longer *depends* on `HelloModules` — only on `HelloInterface`.  
> - Swap implementations *without recompiling `main.app`* — just drop in a new module!  
> - Hide implementations in non-exported packages (e.g., `com.baeldung.internal`) — only the interface is public.

## 🧪 Try It Yourself! (Mini Challenges)  
Now that you’ve got the foundation — experiment!  
1. 🚪 Move `HelloModules` to `com.baeldung.internal` — can you still use it via service? *(Hint: no `exports` needed!)*  
2. 🧩 Add a *second* implementation (`HelloImpl2`) — what does `ServiceLoader` return?  
3. 🔓 Add `opens com.baeldung.modules.hello` — can you reflect on `private` fields now?  
4. 🚫 Remove `uses` — does it still compile? Run? (Spoiler: compile ✅, runtime ❌ if no impl found)

## 🎓 Key Takeaways  
- ✅ Modules = **explicit contracts**, not implicit assumptions.  
- ✅ `exports` ≠ `opens` — API vs. reflection access.  
- ✅ Services (`provides`/`uses`) enable **loose coupling** and **runtime discovery**.  
- ✅ CLI tools (`javac`, `java`) are your best friends for learning.

---
# 🕳️ Chapter 8: The Unnamed Module — Java’s Backward-Compatibility Lifeline  
> 🤔 *If modules are so powerful — why does Java still allow code *outside* the module system? What trade-offs did the designers make to avoid breaking the world?*

The **unnamed module** is not a “module” in the formal sense — it’s a **compatibility construct**:  
> 📦 *All code on the `--class-path` (not `--module-path`) lives here — as one big, flat, “legacy” module.*

It has special privileges — and limitations — designed to keep pre-Java 9 code running *while* encouraging migration.


## 🧩 What *Is* the Unnamed Module?  

| Property | Unnamed Module | Named Module |
|---------|----------------|--------------|
| **How created** | Put JAR/class on `--class-path` | Put JAR/dir with `module-info.class` on `--module-path` |
| **Name** | `null` (no name) | e.g., `com.baeldung.app` |
| **Reads** | ✅ All *other* modules (system + named + automatic) | ❌ Only modules it `requires` |
| **Exports** | ❌ Exports *nothing* → all packages are **module-private** | ✅ Only packages explicitly `exports` |
| **Opens** | ❌ Opens *nothing* for reflection (unless CLI overrides used) | ✅ Controlled via `opens`/`open module` |

> 💡 **Key insight**:  
> The unnamed module is *omnivorous* (it can *use* anything) but *mute* (it *offers* nothing to others).  
> → Great for running old apps — poor for building modular ones.


## 🔌 Why Add Modules Explicitly? (`--add-modules`)  

Even though the unnamed module *reads all modules*, **some modules are *not resolved by default*** — especially if they’re not required by anything in the root set.

### 🎯 Common Scenarios  
| Problem | Cause | Fix |
|--------|-------|-----|
| `java.lang.NoClassDefFoundError: javax/xml/bind/JAXBException` | `java.xml.bind` was *removed* from default root set in Java 9+ (later deleted in Java 11) | `--add-modules java.xml.bind` *(Java 9–10 only)* |
| `ServiceConfigurationError: No implementation for javax.persistence.spi.PersistenceProvider` | JPA impl (e.g., Hibernate) needs `java.sql`, but unnamed module app doesn’t pull it in | `--add-modules java.sql` |
| `ClassNotFoundException: com.sun.xml.internal.ws.spi.ProviderImpl` | Internal JDK service provider not resolved | `--add-modules jdk.xml.ws` *(if available)* |

> ⚠️ **Note**: In Java 11+, `java.xml.bind`, `java.activation`, etc., are **gone** — you must add them as *dependencies* (e.g., `jakarta.xml.bind-api`).


## ⚙️ How `--add-modules` Works  

```bash
java --add-modules java.sql,java.xml -cp legacy-app.jar com.LegacyMain
```

- 🔍 Tells the JVM: *“Even if no module explicitly `requires` these, include them in the module graph.”*  
- ✅ Resolves the module + its transitive dependencies  
- ✅ Makes their *exported* packages available to the unnamed module (via its “read all” privilege)

> 🤔 *Reflect: Why not just auto-resolve *all* system modules?*  
> 🎯 **Answer**: To keep minimal runtimes lean — unused modules aren’t loaded.


## 🧪 Real-World Example: Running a Java 8 Spring App on Java 17  

Your old `spring-boot-1.5` app uses JAXB for REST → fails on Java 17 with:  
`java.lang.NoClassDefFoundError: javax/xml/bind/annotation/XmlRootElement`

✅ **Solution 1 (Temporary)**: Add Jakarta EE API + CLI flag  
```xml
<!-- pom.xml -->
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.0</version>
</dependency>
```
```bash
java \
  --add-modules ALL-SYSTEM \          # resolves *all* system modules
  --add-opens java.base/java.lang=ALL-UNNAMED \
  -jar legacy-app.jar
```

✅ **Solution 2 (Better)**: Migrate to Jakarta XML Binding + Spring Boot 3  
→ No CLI hacks needed — fully modular-friendly. 🌟

## 🛠️ In Build Tools  

### Maven (`maven-compiler-plugin`)  
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.11.0</version>
  <configuration>
    <release>17</release>
    <compilerArgs>
      <arg>--add-modules</arg>
      <arg>java.sql,java.xml</arg>
    </compilerArgs>
  </configuration>
</plugin>
```

### Gradle  
```gradle
tasks.withType(JavaCompile) {
    options.compilerArgs += ['--add-modules', 'java.sql,java.xml']
}
```

> 💡 **Pro tip**: Prefer adding *only what you need* — `ALL-SYSTEM` bloats the classpath and hides real dependencies.


## 🚫 Anti-Patterns to Avoid  
| What | Why It’s Bad |
|------|--------------|
| `--add-modules ALL-UNNAMED` | ❌ Invalid — `ALL-UNNAMED` isn’t a module name |
| Relying on `--add-modules` forever | ❌ Masks design debt — migrate to named modules! |
| Using removed modules (e.g., `java.xml.bind` in Java 11+) | ❌ Won’t work — replace with Jakarta EE APIs |

## 🌉 The Bridge Forward  
The unnamed module is a **temporary harbor**, not a destination.  
Use `--add-modules` to:  
- 🚢 **Migrate incrementally** (run old app → modularize one module at a time)  
- 🧪 **Diagnose missing dependencies** (`jdeps --print-module-deps your-app.jar`)  
- 📊 **Audit your legacy code** before full modularization  

> As the JDK evolves, fewer modules will be “missing by default” — because fewer apps will need them.  
> You’re not just fixing a runtime error — you’re future-proofing your code. 🛡️

---
# 🏁 Chapter 9: Conclusion — You’ve Crossed the Modular Threshold  

> 🤔 *Look back: How has your understanding of “encapsulation” changed since Chapter 1? Was it just about `private` fields — or something deeper?*

You’ve done it.  
You’ve moved from **implicit assumptions** to **explicit contracts**.  
From classpath chaos 🌪️ to intentional architecture 🧩.  
From “it works (for now)” to “it’s *designed* to evolve.”  

Let’s recap the journey:

| 📚 Chapter | 🎯 Core Insight |
|-----------|----------------|
| **1. Overview** | Modules are *not* packages 2.0 — they’re a new layer of **design intention**. |
| **2. What’s a Module?** | A module = packages + resources + `module-info.java` — a **self-describing unit**. |
| **3. Default Modules** | Even the JDK practices what it preaches — modularity starts at the top. 🖥️ |
| **4. Module Declarations** | `requires`, `exports`, `opens`, `provides`… each directive is a *promise* you make. |
| **5. Command Line** | The JVM speaks modular — learn its language to debug, optimize, and understand. |
| **6. Visibility** | Strong encapsulation isn’t restrictive — it’s *liberating* (once you adapt). 🔐 |
| **7. Hands-On** | Theory becomes real when you type `javac --module-source-path` and see it *work*. 🛠️ |
| **8. Unnamed Module** | Backward compatibility is a bridge — not a destination. Walk across with care. 🌉 |


## 🌱 Where to Go From Here  

You now hold the keys to:

✅ **Build modular libraries** — with clean APIs, secure internals, and service extensibility.  
✅ **Diagnose migration issues** — using `jdeps`, `--describe-module`, and `--add-modules`.  
✅ **Prepare for the future** — where custom runtimes (`jlink`) and native images (GraalVM) are the norm.

### 🔜 Next Steps (If You’re Curious…)
| Path | What You’ll Explore |
|------|---------------------|
| 📦 **Multi-Module Builds** | Maven/Gradle modular projects — `moditect`, module path vs. classpath |
| 🔗 **`jlink`: Custom Runtimes** | Strip the JDK down to *only what your app needs* — 50MB → 20MB! |
| ☁️ **Modular Microservices** | How modules fit (or don’t) in containerized, cloud-native worlds |
| ⚡ **GraalVM Native Image** | Can modular apps be compiled to native? (Spoiler: Yes — with care!) |



## 🙏 Final Thought  

> *“Modules don’t make Java harder — they make *bad design* harder to ignore.”*  
>  
> The module system rewards clarity, foresight, and respect for boundaries — between your code, your dependencies, and the platform itself.

You didn’t just learn syntax.  
You’ve begun thinking like a **modular architect**. 🏗️


## 📂 Code & Community  

📘 **Full code for this guide** is available on GitHub:  
🔗 [`github.com/baeldung/java-modules-demo`](https://github.com/baeldung/java-modules-demo) *(example link — replace with real one)*

🔐 **Baeldung Pro Members**:  
→ Clone, run, experiment, and extend the project in your IDE.  
→ Join the discussion: *What was your biggest “aha!” moment? What tripped you up?*

---
