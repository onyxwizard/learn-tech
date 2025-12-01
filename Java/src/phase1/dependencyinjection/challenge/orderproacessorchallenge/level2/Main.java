package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2;

//Main.java

import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.model.Order;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.service.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.config.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.factory.*;


import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 🌐 Simulate config (e.g., from env, YAML, etc.)
        Config config = new Config(Map.of(
            "payment.provider", "stripe",
            "stripe.secret.key", "sk_live_abc123xyz",
            "stripe.timeout.ms", "200"
            // Note: no logger config → use no-op
        ));

        // 🔌 Dependencies
        EmailService emailer = new SmtpEmailService();
        PaymentGateway gateway = GatewayFactory.create(config);
        Logger logger = new FileLogger("orders.log"); // or omit for silent mode

        // 🧩 Composition Root — now supports runtime config!
        OrderProcessor processor = new OrderProcessor(emailer, gateway, logger);
        // Or: new OrderProcessor(emailer, gateway); // ← silent logging

        // 🧪 Test
        System.out.println("=== Level 2: Configurable DI ===");
        processor.process(new Order(201, 100, "user@eu.com", "EU"));
        processor.process(new Order(202, 3, "user@us.com", "US"));
    }
}