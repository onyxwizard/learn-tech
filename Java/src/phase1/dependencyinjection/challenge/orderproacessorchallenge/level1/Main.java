package phase1.dependencyinjection.challenge.orderproacessorchallenge.level1;

//Main.java

import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.model.Order;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.impl.*;
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.service.*;


public class Main {
    public static void main(String[] args) {
        // 🧩 Composition Root: create & wire dependencies
        EmailService emailer = new SmtpEmailService();
        PaymentGateway gateway = new PayPalGateway();
        Logger logger = new FileLogger("orders.log");

        OrderProcessor processor = new OrderProcessor(emailer, gateway, logger);

        // Test orders
        Order order1 = new Order(101, 150, "alice@example.com");
        Order order2 = new Order(102, 5, "bob@example.com"); // too low → fail

        System.out.println("=== Processing Orders ===");
        processor.process(order1);
        System.out.println();
        processor.process(order2);
    }
}