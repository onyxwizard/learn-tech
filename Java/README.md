# 🧠 Java Fundamentals: From Zero to Hero 🚀

> **Master Java the *right* way — with modern practices, clean code, and deep understanding!**  
> 💡 Perfect for beginners & intermediate devs leveling up their Java game.



## 📚 Table of Contents

- [✨ Why This Repo?](#-why-this-repo)
- [🎯 What You'll Learn](#-what-youll-learn)
- [🏗️ Project Structure](#️-project-structure)
- [🚀 Getting Started](#-getting-started)
- [🧪 How to Use](#-how-to-use)
- [💡 Tips & Best Practices](#-tips--best-practices)
- [🤝 Contributing](#-contributing)
- [📜 License](#-license)



## ✨ Why This Repo?

Java is **everywhere** — from Android apps to enterprise backends. But learning it *well* means more than just syntax.  
This repo is your **structured, hands-on playground** to internalize core Java concepts **the modern way**, using:

- ✅ **Records** (Java 14+)
- ✅ **Pattern Matching** (Java 17+)
- ✅ **Lambda Expressions & Streams**
- ✅ **Immutability-first design**
- ✅ **Clean OOP + Functional fusion**

No fluff. Just **executable examples**, **clear annotations**, and **real-world relevance**.



## 🎯 What You'll Learn

<!-- | 📌 Topic | 🔑 Key Concepts |
|--------|----------------|
| **OOP Deep Dive** | Objects, Classes, Inheritance, Interfaces, Packages |
| **Java Basics** | Variables, Arrays, Operators, Control Flow (`if`, `switch`, loops) |
| **Classes & Objects** | Constructors, Methods, Nested/Local/Anonymous Classes, Enums |
| **Records** 🆕 | Immutable data modeling, canonical/compact constructors |
| **Strings & Numbers** | `String`, `StringBuilder`, Autoboxing, `Character` |
| **Inheritance & Polymorphism** | Method overriding, `abstract` classes, `Object` superclass |
| **Interfaces** | Default/static methods, interface as type |
| **Generics** | Type safety, wildcards, type erasure |
| **Lambdas** | Functional interfaces, method refs, `Comparator` chaining |
| **Annotations** | Built-in & custom annotations, type annotations |
| **Pattern Matching** 🆕 | `instanceof`, `switch` patterns, record deconstruction |
| **Exceptions** | Checked vs unchecked, `try`-`catch`-`finally` |
| **Functional Style** | Refactoring loops → Streams, `map`/`filter`/`reduce` | -->


## 🧠 **1. Introducing Object-Oriented Programming (OOP)**  
> *Java’s soul: objects that think, act, and collaborate.*

- **1.1 What is an Object?** 👤  
  → Real-world things as code: state + behavior  
  → Identity, encapsulation, and message passing

- **1.2 What is a Class?** 🏗️  
  → Blueprint for objects  
  → Fields, methods, and access control (`private`, `public`)

- **1.3 What is Inheritance?** 🧬  
  → Reuse code with `extends`  
  → “Is-a” relationship & method overriding

- **1.4 What is an Interface?** 🤝  
  → Contract for behavior (`implements`)  
  → Multiple inheritance of *type* (not implementation)

- **1.5 What is a Package?** 📦  
  → Organize code like folders  
  → Avoid naming chaos & control visibility



## 🔤 **2. Java Language Basics**  
> *Syntax, logic, and flow — your coding ABCs.*

- **2.1 Creating Variables and Naming Them** 🏷️  
  → Declare + initialize  
  → CamelCase: `userName`, not `username_123`

- **2.2 Rules to Name Variables** ✅  
  → Letters, `$`, `_` — no spaces or keywords!

- **2.3 Primitive Type Variables** 🔢  
  → `int`, `double`, `boolean`, `char` — fast & lean

- **2.4 Arrays** 📊  
  → Fixed-size lists: `int[] scores = {90, 85, 95};`

- **2.5 Using `var`** 🪄 (Java 10+)  
  → Let Java guess: `var name = "Java";`  
  → Only for local variables!

- **2.6 Operators** ➕➖✖️➗  
  → Math, logic, comparison, assignment

- **2.7 Summary of Operators** 📋  
  → Precedence cheat sheet (who goes first?)

- **2.8 Expressions, Statements, Blocks** 💬  
  → Expression = value | Statement = action | Block = `{ ... }`

- **2.9 Control Flow Statements** 🚦  
  → `if`, `for`, `while`, `break`, `continue`

- **2.10 `switch` Statements** 🔄  
  → Classic branching (watch out for fall-through!)

- **2.11 `switch` Expressions** 🆕 (Java 14+)  
  → Clean, no fall-through: `case 1 -> "One";`



## 🏗️ **3. Classes and Objects**  
> *Bring your blueprints to life.*

- **3.1 Creating Classes** 🧱  
  → Define structure & behavior

- **3.2 Defining Methods** 📞  
  → Reusable actions with parameters & returns

- **3.3 Constructors** 🎁  
  → Special methods to build objects (`new MyClass()`)

- **3.4 Calling Methods & Constructors** 🔄  
  → `obj.doSomething()`, `this()` chaining

- **3.5 Creating and Using Objects** 🧍‍♂️🧍‍♀️  
  → Heap memory, references, garbage collection

- **3.6 More on Classes** 🔒  
  → `static`, `final`, `this` — power tools

- **3.7 Nested Classes** 🧩  
  → Inner, static nested — helpers that belong together

- **3.8 Enums** 🗂️  
  → Type-safe constants: `enum Color { RED, GREEN, BLUE }`

- **3.9 When to Use: Nested vs. Local vs. Anonymous vs. Lambda** 🤔  
  → Decision flow:  
    - **Nested**: Tightly coupled helper  
    - **Local**: One-time logic inside a method  
    - **Anonymous**: Quick interface implementation  
    - **Lambda**: ✨ Replace simple anonymous classes



## 📦 **4. Records: Immutable Data Made Easy** 🆕 (Java 14+)  
> *Boilerplate? Never heard of her.*

- **4.1 Why Records?** 🙌  
  → Auto `equals()`, `hashCode()`, `toString()`, getters

- **4.2 Record = Final Class** 🔒  
  → Implicitly `final` & extends `java.lang.Record`

- **4.3 Declaring Components** 🧾  
  → `record Point(int x, int y) { }`

- **4.4 What You *Can’t* Do** ⛔  
  → No extra instance fields, no inheritance

- **4.5 Canonical Constructor** 🧾  
  → Auto-generated: `new Point(1, 2)`

- **4.6 Compact Constructor** 🛠️  
  → Validate without repeating params:  
    ```java
    record Person(String name) {
        Person { if (name == null) throw new IllegalArgumentException(); }
    }
    ```

- **4.7 Custom Constructors** 🎛️  
  → Delegate to canonical: `this(name.trim())`

- **4.8 Accessing State** 👀  
  → Call `x()` and `y()` — no `getX()`!

- **4.9 Serialization** 💾  
  → Safe with `writeReplace()`

- **4.10 Real Use Case** 🌐  
  → DTOs, API models, config data — immutable by design



## 🔢 **5. Numbers and Strings**  
> *Data you’ll use every day.*

- **5.1 Numbers** 🧮  
  → Wrappers (`Integer`, `Double`), parsing, `Math`

- **5.2 Characters** 🔤  
  → `char` = single letter, `Character` = object

- **5.3 Strings** 📝  
  → Immutable text: `"Hello".length()`

- **5.4 String Builders** 🧵  
  → `StringBuilder` = fast, mutable strings

- **5.5 Autoboxing & Unboxing** 🔄  
  → Auto convert: `int` ↔ `Integer`  
  → Watch for `NullPointerException`!



## 🧬 **6. Inheritance**  
> *Build on what’s already great.*

- **6.1 Inheritance Basics** 👨‍👦  
  → `class Dog extends Animal`

- **6.2 Overriding vs. Hiding** 🎭  
  → `@Override` for instance methods  
  → Static methods = hidden, not overridden

- **6.3 Polymorphism** 🦸‍♂️  
  → One interface, many forms: `Animal a = new Dog();`

- **6.4 `Object`: The Root of All** 🌍  
  → Every class inherits `toString()`, `equals()`, etc.

- **6.5 Abstract Classes & Methods** 🖼️  
  → Partial blueprints: `abstract void draw();`



## 🤝 **7. Interfaces**  
> *Define *what*, not *how*.*

- **7.1 Interface Declaration** 📜  
  → `interface Flyable { void fly(); }`

- **7.2 Implementing an Interface** ✅  
  → `class Bird implements Flyable`

- **7.3 Interface as a Type** 🎯  
  → Program to interface: `List<String> list = new ArrayList<>();`

- **7.4 Default & Static Methods** ⚙️  
  → Add behavior without breaking old code



## 🧪 **8. Generics**  
> *Type safety without the headache.*

- **8.1 Introducing Generics** 🎯  
  → `List<String>` — no more casting!

- **8.2 Type Inference** 🤖  
  → Diamond operator: `new ArrayList<>()`

- **8.3 Wildcards** ❓  
  → `? extends Animal` (producer), `? super Dog` (consumer) → **PECS**

- **8.4 Type Erasure** 👻  
  → Generics vanish at runtime — compile-time only!

- **8.5 Restrictions** ⚠️  
  → No primitives, no `new T()`, no generic arrays



## ⚡ **9. Lambda Expressions**  
> *Functional programming, Java-style.*

- **9.1 First Lambda** 💥  
  → `(a, b) -> a + b`

- **9.2 Using Lambdas** 🧰  
  → With `Comparator`, `Predicate`, `Runnable`

- **9.3 Method References** 🔗  
  → `String::length`, `System.out::println`

- **9.4 Combining Lambdas** 🧪  
  → `filter(p -> p.age > 18).map(Person::name)`

- **9.5 Comparators** ⚖️  
  → `comparing(Person::name).thenComparing(Person::age)`



## 🏷️ **10. Annotations**  
> *Code that talks about code.*

- **10.1 What Are Annotations?** 📌  
  → `@Override`, `@Deprecated`, `@Test`

- **10.2 Annotation Format** 🧾  
  → `@Author(name = "Alice")`

- **10.3 Where They Go** 📍  
  → Classes, methods, params, even types!

- **10.4 Custom Annotations** 🛠️  
  → `@interface MyAnnotation { String value(); }`

- **10.5 Built-in Meta-Annotations** 🧠  
  → `@Retention`, `@Target`, `@Documented`

- **10.6 Type Annotations** 🎯 (Java 8+)  
  → `List<@NonNull String>`

- **10.7 Repeating Annotations** 🔄 (Java 8+)  
  → `@Author("A") @Author("B")` on same element



## 📦 **11. Packages**  
> *Keep your code tidy & scalable.*

- **11.1 Understanding Packages** 🗂️  
  → Like folders for classes

- **11.2 Creating a Package** 🏗️  
  → `package com.example.utils;`

- **11.3 Naming Conventions** 🌐  
  → Reverse domain: `com.yourcompany.project`

- **11.4 Using Package Members** 🔌  
  → `import`, static imports, FQNs

- **11.5 Wrapping Up** 🎁  
  → JARs, modules (`module-info.java`)



## 🔍 **12. Pattern Matching** 🆕 (Java 17+)  
> *Cleaner, safer type checks.*

- **12.1 Intro to Pattern Matching** 🕵️‍♂️  
  → Less casting, more clarity

- **12.2 `instanceof` Patterns** 🆕  
  → `if (obj instanceof String s) { s.length(); }`

- **12.3 `switch` Patterns** 🧩  
  → Match by type:  
    ```java
    switch (obj) {
        case Integer i -> System.out.println("Int: " + i);
        case String s  -> System.out.println("Text: " + s);
        default        -> System.out.println("??");
    }
    ```

- **12.4 Record Patterns** 🆕 (Java 21 preview)  
  → Destructure records:  
    ```java
    if (obj instanceof Point(int x, int y)) {
        // x and y ready to use!
    }
    ```



## 🚨 **13. Exceptions**  
> *Handle errors like a pro.*

- **13.1 What Is an Exception?** 💥  
  → `Throwable` → `Exception` vs. `Error`

- **13.2 Catching & Handling** 🛡️  
  → `try`-`catch`-`finally`, multi-catch

- **13.3 Throwing Exceptions** 🚀  
  → `throw new IllegalArgumentException("Invalid!");`

- **13.4 Unchecked Exceptions** 😬  
  → `RuntimeException` = no `throws` needed  
  → Use wisely — don’t overuse!



## ♻️ **14. Refactoring: Imperative → Functional Style**  
> *Modernize your legacy loops.*

- **14.1 Simple Loops → `forEach()`** 🔄  
  → `list.forEach(System.out::println);`

- **14.2 Loops with Steps → `IntStream`** 🔢  
  → `IntStream.range(1, 10).forEach(...)`

- **14.3 `for` + `if` → `filter()`** 🧹  
  → `list.stream().filter(x -> x > 0).forEach(...)`

- **14.4 Transform Data → `map()`** 🗺️  
  → `names.stream().map(String::toUpperCase)`

- **14.5 Data Sources → Streams** 🌊  
  → Collections, arrays, files → `stream()`

---


## 🏗️ Project Structure

```
java-fundamentals/
├── 📁 src/
│   ├── 📁 oop/                     # Object-Oriented Programming
│   ├── 📁 basics/                  # Variables, Operators, Control Flow
│   ├── 📁 classes/                 # Classes, Objects, Enums, Nested Classes
│   ├── 📁 records/                 # Modern immutable data with Records 🆕
│   ├── 📁 strings/                 # Strings, StringBuilder, Autoboxing
│   ├── 📁 inheritance/             # Inheritance, Polymorphism, Abstract Classes
│   ├── 📁 interfaces/              # Interfaces as contracts & types
│   ├── 📁 generics/                # Type-safe collections & methods
│   ├── 📁 lambdas/                 # Functional programming with Lambdas
│   ├── 📁 annotations/             # Metadata-driven code
│   ├── 📁 packages/                # Modular code organization
│   ├── 📁 pattern-matching/        # Modern `switch` & `instanceof` 🆕
│   ├── 📁 exceptions/              # Error handling strategies
│   └── 📁 functional-refactoring/  # Imperative → Functional transformation
│
├── 📄 README.md                    # You are here! 🌟
├── 📄 .gitignore
└── 📄 pom.xml                      # Maven build (Java 17+)
```

> 💡 **Each folder contains:**
> - `*.java` files with **annotated examples**
> - `ch_File_run.java` — runnable entry point
> - `NOTES.md` — key takeaways & gotchas



## 🚀 Getting Started

### Prerequisites
- **JDK 17+** (LTS recommended)
- **Maven** (for dependency management & builds)

### Clone & Run
```bash
git clone https://github.com/your-username/java-fundamentals.git
cd java-fundamentals

# Compile
mvn compile

# Run a demo (e.g., Records)
mvn exec:java -Dexec.mainClass="records.RecordDemo"
```

> 🔥 **Pro Tip**: Use **IntelliJ IDEA** or **VS Code + Java Extension Pack** for best experience!



## 🧪 How to Use

1. **Explore by Topic**: Dive into any folder based on your learning goal.
2. **Read the Code**: Examples are **heavily commented** with 📌 **annotations** explaining *why*.
3. **Tweak & Break**: Modify examples to see what happens — learning by doing!
4. **Run Demos**: Each module has a `Demo.java` to see concepts in action.
5. **Refactor Challenges**: Try converting imperative code → functional style in `functional-refactoring/`.



## 💡 Tips & Best Practices

- ✨ **Prefer `record` over boilerplate POJOs** for immutable data.
- 🔁 **Use `var`** for local variables (improves readability!).
- 🚫 **Avoid raw types** — embrace **Generics** for type safety.
- ⚡ **Lambdas > Anonymous Classes** for simple functional logic.
- 🧪 **Pattern Matching** reduces `if (obj instanceof X) { X x = (X) obj; ... }` boilerplate.
- 📦 **Organize code in packages** — even for small projects!



## 🤝 Contributing

Found a bug? Have a better example? **PRs welcome!**  
Just follow:
1. Fork the repo
2. Create your feature branch (`git checkout -b feat/amazing-example`)
3. Commit your changes (`git commit -m 'Add amazing example'`)
4. Push to the branch (`git push origin feat/amazing-example`)
5. Open a Pull Request

> 🙏 **Please keep examples clean, annotated, and JDK 17+ compatible.**


## 📜 License

MIT License — **use freely in your learning, teaching, or projects!**  
See [LICENSE](https://github.com/onyxwizard/java-learn/blob/main/LICENSE) for details.


> 💬 **"Java is to JavaScript what Car is to Carpet."**  
> But with this repo, you’ll master **real Java** — the elegant, powerful, enterprise-grade language it’s meant to be. 🌟

**Happy Coding!** 👨‍💻
