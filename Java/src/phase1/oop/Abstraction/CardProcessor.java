/** Abstraction
 * Two Types : abstract class vs Interface
 */
package phase1.oop.Abstraction;


abstract class PaymentProcessor {
    protected String merchantId;

    // Constructor — yes! Abstract classes can have them
    public PaymentProcessor(String merchantId) {
        this.merchantId = merchantId;
    }

    // ✅ Concrete: shared logic
    public void logTransaction(double amount) {
        System.out.println("[" + merchantId + "] Processing $" + amount);
    }

    // 🛑 Abstract: each payment type does this differently
    public abstract boolean processPayment(double amount);

    // 🎯 Template Method Pattern: high-level algo, delegates details
    public final boolean executePayment(double amount) {
        logTransaction(amount);
        boolean success = processPayment(amount);  // ← abstract hook
        if (success) notifySuccess();
        return success;
    }

    protected void notifySuccess() {
        System.out.println("✅ Payment succeeded.");
    }
}

class CreditCardProcessor extends PaymentProcessor {
    private String cardNumber;

    public CreditCardProcessor(String merchantId, String cardNumber) {
        super(merchantId);  // ← calls abstract class constructor
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("💳 Charging $" + amount + " to " + cardNumber);
        return Math.random() > 0.1; // 90% success
    }
}

class PayPalProcessor extends PaymentProcessor {
    private String email;

    public PayPalProcessor(String merchantId, String email) {
        super(merchantId);
        this.email = email;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("🅿️ Deducting $" + amount + " from " + email);
        return true; // PayPal rarely fails 😄
    }
}


public class CardProcessor {
  public static void main(String[] args) {
    PaymentProcessor p1 = new CreditCardProcessor("M123", "****1234");
    p1.executePayment(99.99);

    PaymentProcessor p2 = new PayPalProcessor("M123", "user@example.com");
    p2.executePayment(49.99);
  }
}