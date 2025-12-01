# 🧪 **DI Challenge: The "OrderProcessor" Refactor**

## 📜 **Given: Legacy Code (Tightly Coupled)**
```java
public class OrderProcessor {
    private final EmailService emailer = new SmtpEmailService();
    private final PaymentGateway gateway = new PayPalGateway();
    private final Logger logger = new FileLogger("orders.log");

    public boolean process(Order order) {
        logger.log("Processing order: " + order.getId());
        
        if (!gateway.charge(order.getAmount())) {
            logger.log("Payment failed for: " + order.getId());
            return false;
        }
        
        emailer.send(order.getCustomer(), "Order Confirmed", "Thank you!");
        logger.log("Order processed: " + order.getId());
        return true;
    }
}
```

> 📌 Assumptions:  
> - `SmtpEmailService`, `PayPalGateway`, `FileLogger` are *concrete classes* (no interfaces yet).  
> - You can modify *all* code — but aim for minimal, clean change.


## ✅ **Your Tasks (Do them in order)**

### 🔹 **Level 1: Basic DI**
1. Introduce interfaces to enable polymorphism.  
2. Refactor `OrderProcessor` to use **constructor injection**.  
3. Show how to create a *testable* instance with mocks (no framework — manual DI).

> ✍️ Deliverable:  
> - Interfaces  
> - Refactored `OrderProcessor`  
> - Sample test wiring (e.g., `new OrderProcessor(mockEmail, mockGateway, mockLogger)`)

### 🔹 **Level 2: Real-World Constraints**
Now imagine:
- `Logger` is *optional* (some environments disable logging).  
- `PaymentGateway` needs *runtime configuration* (e.g., API key per tenant).  
- You must support *both* PayPal and Stripe — chosen at startup.

4. Adjust your design to:
   - Make `Logger` optional (but still injectable).
   - Support configurable gateways (e.g., via factory or config object).
   - Ensure `OrderProcessor` remains *unaware* of which gateway is used.

> ✍️ Deliverable:  
> - Updated interfaces/classes  
> - How you’d wire:  
>   `new OrderProcessor(stripeGateway(apiKey), smtpEmail, nullLogger)`  
>   vs  
>   `new OrderProcessor(paypalGateway(), consoleEmail, fileLogger)`

### 🔹 **Level 3: Deep Principle Check**
5. The team argues:  
   > *“Why not use Service Locator? It’s simpler — just call `ServiceLocator.get(PaymentGateway.class)`.”*  
   **Refute this** — with *specific, concrete drawbacks* (not just “it’s bad”).

6. Another dev says:  
   > *“We should inherit `OrderProcessor` — e.g., `PayPalOrderProcessor extends OrderProcessor`.”*  
   Explain why **composition (has-a) + DI** is superior here — using DIP, OCP, and testability.

### 🔹 **Level 4: Butterfly Spark 🦋 (Optional Bonus)**
7. Suppose orders in *EU* require a `TaxCalculator`, but *US* orders don’t.  
   - The tax rule depends on `order.getRegion()` *at runtime*.  
   - You *don’t* want to inject *all* calculators and branch inside `process()`.

How would a **Butterfly DI** approach handle this?  
(Recall: metamorphosis → behavior adapts *without* conditional logic in core.)

> Hint: Think *strategy per region*, selected *before* processing — but wired cleanly.

### 🕒 Time yourself: 20–25 minutes  
Then I’ll give you:
- ✅ Model solution (clean, idiomatic)  
- 💡 Key insights per level  
- 🎯 Where people commonly slip up  
- 🦋 How Butterfly DI could elegantly solve Level 4