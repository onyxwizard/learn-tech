package phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.service;

//PayPalGateway.java
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level1.impl.PaymentGateway;


public class PayPalGateway implements PaymentGateway {
    private static final int MIN_AMOUNT = 10;

    @Override
    public boolean charge(int amount) {
        boolean success = amount >= MIN_AMOUNT;
        System.out.println("[PAYMENT] PayPal charged $" + amount + " → " + (success ? "✓" : "✗"));
        return success;
    }
}