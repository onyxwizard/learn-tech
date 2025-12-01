package phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.service;

//OrderProcessor.java
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.model.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.impl.*;

public class OrderProcessor {
    private final EmailService emailer;
    private final PaymentGateway gateway;
    private final Logger logger;

    // ✅ Constructor injection — all deps required
    public OrderProcessor(EmailService emailer, PaymentGateway gateway, Logger logger) {
        this.emailer = emailer;
        this.gateway = gateway;
        this.logger = logger;
    }

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

    private void log(String msg) {
        logger.log(msg); // Simple wrapper — Level 2 will make this optional
    }
}