package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl;

public class StripeGateway implements PaymentGateway {
    private final String secretKey;
    private final int timeoutMs;

    public StripeGateway(String secretKey, int timeoutMs) {
        this.secretKey = secretKey;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public boolean charge(int amount) {
        // Simulate network latency
        try { Thread.sleep(Math.min(timeoutMs, 100)); } catch (InterruptedException e) {}

        boolean success = amount >= 5;
        System.out.printf("[Stripe] Charged $%d (key: %s..) → %s%n",
            amount, secretKey.substring(0, 4), success ? "✓" : "✗");
        return success;
    }
}
