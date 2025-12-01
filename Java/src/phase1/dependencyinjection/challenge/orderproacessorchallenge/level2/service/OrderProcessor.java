package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.service;

//OrderProcessor.java
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.model.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl.*;

public class OrderProcessor {
    private final EmailService emailer;
    private final PaymentGateway gateway;
    private final Logger logger; // can be NoOpLogger

    // Primary constructor — all deps explicit
    public OrderProcessor(EmailService emailer, PaymentGateway gateway, Logger logger) {
        this.emailer = emailer;
        this.gateway = gateway;
        this.logger = logger != null ? logger : new NoOpLogger(); // ✅ null-safe
    }

    // Convenience: logger optional
    public OrderProcessor(EmailService emailer, PaymentGateway gateway) {
        this(emailer, gateway, null);
    }

    // ... rest unchanged — log() uses this.logger safely
    private void log(String msg) { logger.log(msg); }
    
    public boolean process(Order order) {
        log("Processing order: " + order.getId());

        if (!gateway.charge(order.getAmount())) {
            log("Payment failed for order: " + order.getId());
            return false;
        }

        emailer.send(
            order.getCustomer(),
            "Order Confirmed",
            "Thank you for your order #" + order.getId() + "!"
        );

        log("Order #" + order.getId() + " processed successfully.");
        return true;
    
    }
}