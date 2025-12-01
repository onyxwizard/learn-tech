package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.model;

//Order.java

public class Order {
    private final int id;
    private final int amount;
    private final String customerEmail;
    private final String region; // for Level 4 — default to "US"

    public Order(int id, int amount, String customerEmail) {
        this(id, amount, customerEmail, "US");
    }

    public Order(int id, int amount, String customerEmail, String region) {
        this.id = id;
        this.amount = amount;
        this.customerEmail = customerEmail;
        this.region = region != null ? region : "US";
    }

    public int getId() { return id; }
    public int getAmount() { return amount; }
    public String getCustomer() { return customerEmail; }
    public String getRegion() { return region; }
}