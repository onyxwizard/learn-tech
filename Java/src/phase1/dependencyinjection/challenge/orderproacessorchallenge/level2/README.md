# 📘 **Level 2: Configurable, Optional, Runtime-Selectable DI**  
> *From static wiring to dynamic, environment-aware composition*

## 🔍 **Challenge: Real-World Variability**

Level 1 solved coupling — but real systems face:
- **Optional concerns**: Logging disabled in prod metrics pipelines  
- **Runtime configuration**: API keys, fees, timeouts vary by tenant/environment  
- **Dynamic selection**: PayPal for EU, Stripe for US — decided at startup, not compile-time  

Yet, core logic (`OrderProcessor`) must remain:
- ✅ **Unaware** of which implementation is used  
- ✅ **Unchanged** when new options are added  
- ✅ **Testable** in isolation  

> 💡 **Core issue**: *Hardcoded dependencies and static wiring prevent adaptation without code changes.*


### 🎯 **Goal**

Extend Level 1 to support:
1. **Optional dependencies** (e.g., logger can be disabled)  
2. **Configurable dependencies** (e.g., `PayPalGateway(apiKey, feeRate)`)  
3. **Runtime selection** of implementations (e.g., choose gateway via config/env)  
4. **Zero changes to `OrderProcessor`** — preserve its purity  

✅ Success = Same `OrderProcessor` works for:
- Dev (verbose logs + PayPal sandbox)  
- Prod EU (silent + PayPal live)  
- Prod US (silent + Stripe)


### 🧠 **Key Concepts**

| Concept | Role in Level 2 |
|--------|-----------------|
| **Null Object Pattern** | Replaces `null` checks with safe no-op behavior (`NoOpLogger`). Preserves immutability & contract. |
| **Parameterized Construction** | Dependencies accept config (API keys, rates) at creation — not hardcoded. |
| **Factory Pattern** | Encapsulates object creation logic (`GatewayFactory`). Centralizes selection; decouples config from usage. |
| **Separation of Concerns** | `OrderProcessor` = *what* (process order); `GatewayFactory` = *how* (which gateway + config). |
| **Composition Root Evolution** | Now reads config → wires *context-aware* object graph. Still manual — no container needed. |

> ⚠️ **Why not Service Locator for config?**  
> - Factory is *stateless*, *testable*, and *explicit* — SL hides config dependencies globally.

### ✅ **What We Did**

| Step | Action | Rationale |
|------|--------|-----------|
| **1. Optional Logger** | Added `NoOpLogger` + constructor overload | Eliminates `if (logger != null)` in core; preserves immutability. Safer than `null`. |
| **2. Configurable Gateways** | Redesigned `PayPalGateway`, `StripeGateway` to accept params in constructor | Enables tenant-specific keys/fees; avoids static/singleton config anti-patterns. |
| **3. GatewayFactory** | Created `GatewayFactory.create(Config)` | Encapsulates selection logic; OCP-ready (add `WiseGateway` → update factory only). |
| **4. Config Abstraction** | Simple `Config` wrapper (simulates env/YAML) | Decouples config source (env, DB, vault) from usage. |
| **5. Composition Root Upgrade** | `Main` now uses `Config` + `GatewayFactory` | Wiring adapts to environment — same code, different behavior. |

#### 🔸 Critical Design Choice: **Factory Over Reflection**
```java
// NOT used:
// Class.forName(config.get("gateway.class")).newInstance()
```
→ Reflection breaks compile-time safety, IDE navigation, and testability.  
✅ **Factory** gives type safety, refactoring support, and explicit dependencies.


### 🗺️ **Mindmap: Level 2 Architecture**

```
                     [Level 2: Configurable DI]
                              ▲
               ┌──────────────┼───────────────────────┐
               │              │                       │
     [Principle]        [Pattern]              [Practice]
   Open/Closed (OCP)   Factory + Null Object   Config-Driven Wiring
               ▲              ▲                       ▲
               └───────┬──────┴───────────┬───────────┘
                       ▼                  ▼
           [Config Source]        [Runtime Selection]
     ┌─────────┬─────────┐      ┌─────────┴──────────┐
     ▼         ▼         ▼      ▼                    ▼
   env vars   YAML     vault   GatewayFactory    NoOpLogger
                                 │
                 ┌───────────────┼────────────────┐
                 ▼               ▼                ▼
         PayPalGateway     StripeGateway     ... (extensible)
       (apiKey, feeRate)  (secretKey, timeout)
                 ▲               ▲
                 └───────┬───────┘
                         ▼
                 [OrderProcessor]
            (still depends ONLY on abstractions)
                         │
                         ▼
                      [Main]
              (Composition Root + Config Reader)
```

#### 🔑 Flow of Adaptation:
> **Config → Factory → Concrete Impl → Injected into Core**  
> Core remains *blissfully unaware* of variation.


### ✅ **Outcome**

- ✅ **Environment-Adaptive**: Same binary, different behavior via config  
- ✅ **Tenant-Ready**: `PayPalGateway(tenant1Key, 0.029)` vs `PayPalGateway(tenant2Key, 0.035)`  
- ✅ **Safe Optionality**: `NoOpLogger` > `null` — no NPEs, no conditionals in core  
- ✅ **OCP-Compliant**: Add new gateway → update *only* `GatewayFactory`  
- ✅ **Testable Config Logic**: `GatewayFactoryTest` verifies selection rules in isolation  

> 🚀 **Foundation for Level 3/4**: Configurable DI is prerequisite for context-aware (Butterfly) behavior.