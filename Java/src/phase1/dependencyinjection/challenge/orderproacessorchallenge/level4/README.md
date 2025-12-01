# 📘 **Level 4: Butterfly DI — Context-Aware, Adaptive Dependency Binding**  
> *Dependencies that metamorphose based on runtime context — no conditionals in core logic*


## 🔍 **Challenge: Runtime Behavioral Variation**

Even with configurable DI (Level 2), some requirements *cannot* be resolved at startup:
- **EU orders** require VAT calculation; **US orders** do not  
- **Premium users** get priority shipping; free users get standard  
- **Mobile requests** compress responses; desktop does not  

Traditional approaches fail:
- ❌ `if (order.getRegion().equals("EU")) calcTax()` → violates **Single Responsibility**  
- ❌ Inheritance (`EuOrderProcessor extends OrderProcessor`) → combinatorial explosion  
- ❌ Service Locator (`TaxService.getFor(order)`) → hidden coupling, untestable  

> 💡 **Core issue**: *Static DI wires one behavior per application — but real behavior varies per request.*


## 🎯 **Goal**

Enable `OrderProcessor` to:
- Remain **completely unaware** of regional/tier/contextual rules  
- **Adapt its behavior** at *call time* based on `Order` context  
- Require **zero conditionals** (`if`, `switch`) in core logic  
- Support **new contexts** (e.g., `UK`, `APAC`) without modifying `OrderProcessor`  

✅ Success = Same `OrderProcessor.process(order)` works for:
- `order.region = "EU"` → taxed  
- `order.region = "US"` → no tax  
- `order.tier = "premium"` → priority shipping  
— all *without* a single `if` in `OrderProcessor`.

## 🧠 **Key Concepts**

| Concept | Role in Butterfly DI |
|--------|----------------------|
| **Strategy Pattern (Runtime-Selected)** | Tax, shipping, notification logic encapsulated in stateless strategies (`TaxPolicy`, `ShippingPolicy`) |
| **Resolver Pattern** | `TaxPolicyResolver.forOrder(order)` selects strategy *per request* — not per app |
| **Contextual Injection** | Inject *resolvers* (not concrete policies) → core asks: _“What policy fits this context?”_ |
| **Pure Functions** | Policies are stateless, side-effect-free → easy to test, cache, audit |
| **Metamorphosis Point** | Single line in `OrderProcessor`: `TaxPolicy policy = taxResolver.forOrder(order)` |

> 🔑 **Butterfly Metaphor**:  
> - **Chrysalis** = `OrderProcessor` (stable core)  
> - **Metamorphosis** = `resolver.forOrder(order)` (context-triggered adaptation)  
> - **Emergence** = `policy.apply(amount)` (new behavior, same interface)

## ✅ **What We Designed**

### **1. Strategy Abstraction: `TaxPolicy`**
```java
interface TaxPolicy {
    int applyTax(int amount, String region); // pure function
}
```
- `EuVatPolicy`: `amount * 1.20` if region = `"EU"`  
- `NoTaxPolicy`: returns `amount` (default)  
→ **No conditionals in core** — logic lives *in the strategy*.

### **2. Resolver: Context-to-Strategy Mapper**
```java
interface TaxPolicyResolver {
    TaxPolicy forOrder(Order order);
}

class RegionBasedTaxResolver implements TaxPolicyResolver {
    public TaxPolicy forOrder(Order order) {
        return "EU".equalsIgnoreCase(order.getRegion()) 
               ? euVatPolicy 
               : noTaxPolicy;
    }
}
```
→ Encapsulates *selection logic* — easily testable, replaceable.

### **3. Core Adaptation Point (The “Metamorphosis”)**
```java
public boolean process(Order order) {
    // 🦋 Butterfly moment: behavior emerges from context
    TaxPolicy taxPolicy = taxResolver.forOrder(order);
    int taxedAmount = taxPolicy.applyTax(order.getAmount(), order.getRegion());
    
    // ... rest unchanged
}
```
→ **One line** enables infinite variation. `OrderProcessor` stays pure.

### **4. Composition Root: Wiring the Ecosystem**
```java
TaxPolicyResolver taxResolver = new RegionBasedTaxResolver(
    new EuVatPolicy(), 
    new NoTaxPolicy()
);

OrderProcessor processor = new OrderProcessor(
    emailer, gateway, logger, taxResolver  // ← injected resolver, not policy
);
```
→ Core depends on *abstraction* (`TaxPolicyResolver`), not concrete rules.



### 🗺️ **Mindmap: Butterfly DI Architecture**

```
                     [Level 4: Butterfly DI]
                              ▲
               ┌──────────────┼───────────────────────────────┐
               │              │                               │
     [Principle]        [Pattern]                      [Metaphor]
   Contextual IoC   Strategy + Resolver              Metamorphosis
               ▲              ▲                               ▲
               └───────┬──────┴─────────────────┬─────────────┘
                       ▼                        ▼
            [Request Context]           [Behavior Emergence]
             (Order.region,             ┌────────┴────────┐
              Order.tier, etc.)         ▼                 ▼
                               TaxPolicyResolver    ShippingResolver
                                      │                 │
                      ┌───────────────┼─────────────────┼──────────────┐
                      ▼               ▼                 ▼              ▼
               EuVatPolicy     NoTaxPolicy     PriorityShipping   StdShipping
               (region-aware)  (default)       (tier-aware)       (default)
                      ▲               ▲                 ▲              ▲
                      └───────────────┴─────────────────┴──────────────┘
                                         │
                                 [OrderProcessor]
                           (core logic — no conditionals)
                                         │
                                         ▼
                                      [Main]
                          (wires resolvers, not policies)
```

### 🔑 Flow of Adaptation:
> **Order Context → Resolver → Strategy → Behavior**  
> Core remains *immutable, testable, and unaware*.



### ✅ **Outcome**

- ✅ **Zero conditionals in core**: `OrderProcessor` has no `if (region == "EU")`  
- ✅ **OCP in action**: Add `UkVatPolicy` → update *only* `RegionBasedTaxResolver`  
- ✅ **Testable strategies**: `EuVatPolicyTest` verifies VAT logic in isolation  
- ✅ **Composable**: Combine tax + shipping + notification resolvers cleanly  
- ✅ **Scalable**: Works for 2 regions or 200 — no code changes to core  

> 🦋 **Butterfly DI is not a framework** — it’s a **mindset**:  
> _“Don’t hardcode variation — inject the ability to vary.”_


### 🌐 Real-World Applications

| Domain | Butterfly DI Use Case |
|--------|------------------------|
| **E-Commerce** | Tax, shipping, currency conversion per region/user |
| **SaaS** | Feature flags, rate limits, UI themes per tenant |
| **Gaming** | Difficulty, rewards, physics per player level |
| **IoT** | Sensor calibration, alert thresholds per device type |

### 🏁 Final Synthesis: The DI Evolution

| Level | Control Point | Flexibility | Core Awareness |
|-------|----------------|-------------|----------------|
| **1. Basic DI** | Startup (manual) | Swap impls globally | None (abstraction-only) |
| **2. Configurable DI** | Startup (config-driven) | Tenant/env-specific impls | None |
| **3. Principle Defense** | Design-time | Prevents anti-patterns | N/A (meta-level) |
| **4. Butterfly DI** | **Per-request** | **Context-aware behavior** | **None — pure adaptation** |

> ✅ You’ve now mastered DI from **static wiring** → **dynamic metamorphosis**.


### 🦋 Where to Go From Here?

- **Productionize**: Add caching to resolvers (`@Cached` policies)  
- **Distribute**: `TaxPolicyResolver` calls gRPC `TaxService`  
- **Observe**: `policy.applyTax()` emits metrics/events  
- **Extend**: `OrderEnricher` pipeline (tax → shipping → fraud check)