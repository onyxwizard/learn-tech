package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.service;

//PayPalGateway.java
import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl.PaymentGateway;


public class PayPalGateway implements PaymentGateway {
    private final String apiKey;
    private final double feeRate; // e.g., 0.029 = 2.9%

    // Configurable constructor
    public PayPalGateway(String apiKey, double feeRate) {
        this.apiKey = apiKey;
        this.feeRate = feeRate;
    }

    @Override
    public boolean charge(int amount) {
        int feeCents = (int) (amount * feeRate);
        int net = amount - feeCents;
        boolean success = net >= 10; // min $0.10 to provider

        System.out.printf("[PayPal] Charged $%d → Fee: $%.2f → Net: $%d → %s%n",
            amount, feeCents / 100.0, net, success ? "✓" : "✗");
        return success;
    }
}