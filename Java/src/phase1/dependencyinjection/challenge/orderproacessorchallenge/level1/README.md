# 📘 **Level 1: Basic Dependency Injection (Constructor Injection)**  
> *Decoupling via Explicit, Immutable Dependencies*

## 🔍 **Challenge: Tight Coupling in Legacy Design**

In traditional OOP, components often:
- Use `new` to create their own dependencies  
- Depend directly on concrete classes (not abstractions)  
- Hide their requirements from callers  

This leads to:
- ❌ **Rigidity**: Changing a dependency (e.g., `SmtpEmailService` → `ConsoleEmailService`) requires code modification.  
- ❌ **Untestability**: Cannot inject mocks — unit tests become integration tests.  
- ❌ **Violation of DIP**: High-level modules depend on low-level details.

> 💡 **Core issue**: *Control over dependency creation resides inside the component* — not externally.

### 🎯 **Goal**

Achieve **loose coupling** and **testability** by:
1. Introducing interfaces for key dependencies (`EmailService`, `PaymentGateway`, `Logger`)  
2. Refactoring `OrderProcessor` to receive dependencies via **constructor injection**  
3. Creating a **Composition Root** (`Main`) where wiring happens  
4. Ensuring zero `new` in business logic  

✅ Success = `OrderProcessor` knows *only abstractions*, yet works with any concrete implementation.



### 🧠 **Key Concepts**

| Concept | Role in Level 1 |
|--------|-----------------|
| **Dependency Inversion Principle (DIP)** | High-level `OrderProcessor` depends on *abstractions* (`EmailService`), not `SmtpEmailService`. |
| **Constructor Injection** | Preferred DI technique: dependencies are *required*, *immutable*, and *visible*. |
| **Composition over Inheritance** | Behavior composed at runtime (`has-a`) rather than inherited (`is-a`). |
| **Composition Root** | Single place (`Main`) where object graph is assembled — avoids scattered `new`. |
| **Explicit Contracts** | Constructor signature = API contract. IDEs, compilers, and humans all understand dependencies. |

> ⚠️ **Why not setter/interface injection?**  
> - Setters allow partial initialization → runtime NPE risk  
> - Interface injection couples to injector → rare and discouraged today

### ✅ **What We Did**

| Step | Action | Rationale |
|------|--------|-----------|
| **1. Interface Extraction** | Created `EmailService`, `PaymentGateway`, `Logger` | Enables polymorphism; satisfies DIP. |
| **2. Concrete Implementations** | Built `SmtpEmailService`, `PayPalGateway`, `FileLogger` | Showcases flexibility — swap without touching `OrderProcessor`. |
| **3. Constructor Injection** | `OrderProcessor(EmailService, PaymentGateway, Logger)` | Guarantees complete, immutable state; enables testability. |
| **4. Composition Root** | `Main` wires dependencies manually | Demonstrates DI without containers — proves the *pattern*, not the tool. |
| **5. Immutable `Order`** | Made `Order` a data carrier with final fields | Aligns with functional/data-oriented design; avoids side effects. |

#### 🔸 Critical Design Choice: **No Default Constructors**
```java
// NOT allowed:
// public OrderProcessor() { } // ← dangerous!
```
→ Forces callers to provide *all* dependencies → **fail-fast**, no hidden NPEs.


### 🗺️ **Mindmap: Level 1 Architecture**

```
                     [Level 1: Basic DI]
                              ▲
               ┌──────────────┼────────────────┐
               │              │                │
     [Principle]        [Pattern]        [Practice]
   Dependency Inversion  Constructor      Composition Root  
        (DIP)             Injection         (Manual Wiring)
               ▲              ▲                ▲
               └───────┬──────┴────────┬───────┘
                       ▼               ▼
             [Interfaces]        [Concrete Impl]
     ┌────────┬────────┬────┐   ┌──────┬───────┬────────┐
     ▼        ▼        ▼    ▼   ▼      ▼       ▼        ▼
EmailService PaymentGateway Logger SmtpEmail PayPalGateway FileLogger
     ▲        ▲          ▲
     └────────┴──────────┘
               │
        [OrderProcessor]
      (depends ONLY on abstractions)
               │
               ▼
            [Main]
     (Composition Root — wires all)
```

#### 🔑 Flow of Control (Hollywood Principle):
> **“Don’t call us — we’ll call you.”**  
> `Main` → constructs & injects → `OrderProcessor` uses deps → never creates them.


### ✅ **Outcome**

- ✅ **Testable**: Mock any dependency in unit tests  
- ✅ **Flexible**: Swap email/gateway/logger without recompiling `OrderProcessor`  
- ✅ **Readable**: Dependencies declared upfront — no hidden `new` surprises  
- ✅ **SOLID-Aligned**: DIP satisfied; OCP ready (new impls = no core changes)

> 🚀 **Foundation for evolution**: This design scales cleanly to Levels 2–4.