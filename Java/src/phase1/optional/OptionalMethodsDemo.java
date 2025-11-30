package phase1.optional;

/**
 * @author onyxwizard
 * @date 29-11-2025
 */

// OptionalMethodsDemo.java
// A comprehensive, professional demonstration of all 16 Optional<T> methods.
// Designed for learning, reference, and real-world correctness.

import java.util.*;
import java.util.function.*;
import java.math.BigDecimal;

/**
 * Demonstrates all 16 methods of java.util.Optional<T> in a safe, idiomatic way.
 * 
 * Design Principles:
 * - Absence is explicit: Optional used only as RETURN TYPE (never field/param)
 * - No raw .get() — always paired with isPresent() or replaced by safer alternatives
 * - Meaningful domain: User, Order, Payment — relatable and scalable
 * - Anti-patterns documented and avoided
 */
public class OptionalMethodsDemo {

    // === DOMAIN MODELS ===
    // Simple, immutable value objects — ideal for Optional usage

    record User(String name, Email email) {}
    record Email(String address) {}
    record Order(int id, BigDecimal total, Optional<Payment> payment) {}
    record Payment(String method, BigDecimal amount) {}

    // === FACTORY METHODS (10, 11, 1) ===

    /**
     * 10. Optional.of(T value)
     * ✅ Creates Optional with NON-NULL value.
     * ❌ Throws NullPointerException if value is null.
     * 👉 Use when you KNOW value is present (e.g., result of computation).
     */
    static Optional<User> findUserById(int id) {
        if (id == 1) {
            return Optional.of(new User("Alice", new Email("alice@example.com")));
        }
        // For id=2, we'll use ofNullable (below) — but here, safe assumption
        return Optional.of(new User("Bob", new Email("bob@example.com")));
    }

    /**
     * 11. Optional.ofNullable(T value)
     * ✅ Safe creation: returns Optional.empty() if value is null.
     * 👉 MOST COMMON method — use for all external/nullable inputs.
     */
    static Optional<Order> findOrderById(int id) {
        if (id == 100) {
            // Order with payment
            return Optional.ofNullable(new Order(100, new BigDecimal("99.99"),
                    Optional.of(new Payment("Credit Card", new BigDecimal("99.99")))));
        } else if (id == 101) {
            // Order WITHOUT payment (e.g., pending)
            return Optional.ofNullable(new Order(101, new BigDecimal("49.99"), Optional.empty()));
        }
        // id=999 → null order → becomes Optional.empty()
        return Optional.ofNullable(null);
    }

    /**
     * 1. Optional.empty()
     * ✅ Explicitly represent "no value" — preferred over null Optional.
     * 👉 Use in fallbacks, default returns, or when absence is intentional.
     */
    static Optional<User> findUserByEmail(String email) {
        // Simulate DB lookup: only "alice@example.com" exists
        if ("alice@example.com".equals(email)) {
            return Optional.of(new User("Alice", new Email(email)));
        }
        return Optional.empty(); // ← clean, explicit absence
    }

    // === QUERY METHODS (8, 16) ===

    /**
     * 8. isPresent() & 16. isEmpty() (Java 11+)
     * ✅ Check if value exists — prefer over .get() without guard.
     * 👉 Use when you need conditional logic (but prefer ifPresent/map/orElse).
     * ⚠️ isEmpty() is just !isPresent() — added for readability.
     */
    static void demonstratePresenceChecks() {
        Optional<User> user1 = findUserByEmail("alice@example.com");
        Optional<User> user2 = findUserByEmail("unknown@example.com");

        System.out.println("=== Presence Checks ===");
        System.out.println("User1 present? " + user1.isPresent() + " | empty? " + user1.isEmpty());
        System.out.println("User2 present? " + user2.isPresent() + " | empty? " + user2.isEmpty());
        // Output: true/false, false/true
    }

    // === VALUE ACCESS (5, 7, 12, 13, 14) ===

    /**
     * 5. get()
     * ❌ Dangerous! Throws NoSuchElementException if empty.
     * ✅ Only safe AFTER isPresent() — but better to avoid entirely.
     * 👉 Prefer orElse(), ifPresent(), etc. — .get() is a code smell.
     */
    static String getUserNameSafely(Optional<User> userOpt) {
        // Anti-pattern (avoid):
        // if (userOpt.isPresent()) return userOpt.get().name();  // redundant

        // Idiomatic: use orElse() or map()
        return userOpt.map(User::name).orElse("Anonymous");
    }

    /**
     * 7. ifPresent(Consumer)
     * ✅ Execute side-effect ONLY if value exists — clean and expressive.
     * 👉 Perfect for logging, notifications, or conditional actions.
     */
    static void sendWelcomeEmail(Optional<User> userOpt) {
        userOpt.ifPresent(user -> 
            System.out.println("📧 Sending welcome email to: " + user.email().address())
        );
        // No-op if empty — no if-check needed!
    }

    /**
     * 12. orElse(T other)
     * ✅ Return value or default — use when default is cheap (constant/literal).
     * ⚠️ `other` is evaluated EVEN IF NOT USED — avoid expensive expressions.
     */
    static String getOrderStatus(Optional<Order> orderOpt) {
        return orderOpt.map(o -> "Order #" + o.id() + " - $" + o.total())
                       .orElse("No order found");
    }

    /**
     * 13. orElseGet(Supplier)
     * ✅ Return value or compute default LAZILY — use for expensive defaults.
     * 👉 Supplier only invoked if Optional is empty — performance-safe.
     */
    static BigDecimal getPaymentAmount(Optional<Order> orderOpt) {
        return orderOpt.flatMap(Order::payment)
                       .map(Payment::amount)
                       .orElseGet(() -> {
                           System.out.println("⚠️ Computing fallback amount...");
                           return new BigDecimal("0.00"); // e.g., from config/cache
                       });
    }

    /**
     * 14. orElseThrow(Supplier)
     * ✅ Require value or fail — ideal for validation and APIs.
     * 👉 Throw CUSTOM exception with context — never use bare .get().
     */
    static Payment requirePayment(Optional<Order> orderOpt) {
        return orderOpt.flatMap(Order::payment)
                       .orElseThrow(() -> new IllegalArgumentException(
                           "Order " + orderOpt.map(Order::id).orElse(-1) + " has no payment!"));
    }

    // === TRANSFORMATION (9, 3, 4) ===

    /**
     * 9. map(Function)
     * ✅ Transform T → U (non-Optional) — chain safely.
     * 👉 Result is Optional<U>; null results become empty().
     */
    static Optional<String> extractEmailDomain(Optional<User> userOpt) {
        return userOpt.map(User::email)      // Optional<Email>
                      .map(Email::address)   // Optional<String>
                      .map(addr -> addr.substring(addr.indexOf('@') + 1)); // Optional<String>
        // e.g., "alice@example.com" → "example.com"
    }

    /**
     * 3. filter(Predicate)
     * ✅ Conditional presence: keep value only if predicate matches.
     * 👉 Use for validation or conditional chaining.
     */
    static Optional<User> findActiveUser(String email) {
        return findUserByEmail(email)
                .filter(user -> user.name().length() >= 3); // only "Alice", not "Al"
    }

    /**
     * 4. flatMap(Function → Optional<U>)
     * ✅ Chain Optionals without nesting — essential for deep access.
     * 👉 Converts Optional<Optional<U>> → Optional<U> (flattens).
     */
    static Optional<BigDecimal> getOrderPaymentAmount(Optional<Order> orderOpt) {
        return orderOpt.flatMap(Order::payment)  // Optional<Payment>
                       .map(Payment::amount);    // Optional<BigDecimal>
        // Without flatMap: orderOpt.map(Order::payment) → Optional<Optional<Payment>>
    }

    // === METADATA (2, 6, 15) ===

    /**
     * 2. equals(Object)
     * ✅ Compare two Optionals — equal if both empty OR both present and values equal.
     * 👉 Useful in tests or collections (e.g., Map keys).
     */
    static void demonstrateEquals() {
        Optional<String> a = Optional.of("hello");
        Optional<String> b = Optional.of("hello");
        Optional<String> c = Optional.of("world");
        Optional<String> d = Optional.empty();

        System.out.println("\n=== equals() ===");
        System.out.println("a.equals(b): " + a.equals(b)); // true
        System.out.println("a.equals(c): " + a.equals(c)); // false
        System.out.println("d.equals(Optional.empty()): " + d.equals(Optional.empty())); // true
    }

    /**
     * 6. hashCode()
     * ✅ Consistent with equals() — present value's hash, or 0 if empty.
     * 👉 Enables use in HashMap/HashSet.
     */
    static void demonstrateHashCode() {
        Optional<String> opt = Optional.of("test");
        System.out.println("\n=== hashCode() ===");
        System.out.println("Optional.of(\"test\").hashCode(): " + opt.hashCode());
        System.out.println("Optional.empty().hashCode(): " + Optional.empty().hashCode());
    }

    /**
     * 15. toString()
     * ✅ Debug-friendly string: "Optional[value]" or "Optional.empty".
     * 👉 NEVER rely on this format in production logic — for logging/debug only.
     */
    static void demonstrateToString() {
        System.out.println("\n=== toString() ===");
        System.out.println("User: " + findUserByEmail("alice@example.com"));
        System.out.println("User: " + findUserByEmail("none@example.com"));
        // Output: Optional[User[name=Alice, ...]], Optional.empty
    }

    // === MAIN: Orchestrates all demos safely ===
    public static void main(String[] args) {
        System.out.println("🔍 java.util.Optional<T> — Complete Method Reference\n");

        // 1, 10, 11: Creation
        Optional<User> user1 = findUserById(1);
        Optional<Order> order1 = findOrderById(100);
        Optional<Order> order2 = findOrderById(101);
        Optional<Order> noOrder = findOrderById(999);

        // 8, 16: Presence
        demonstratePresenceChecks();

        // 7: ifPresent
        System.out.println("\n=== ifPresent ===");
        sendWelcomeEmail(user1);    // sends email
        sendWelcomeEmail(noOrder.map(o -> new User("Ghost", new Email("ghost@void.com")))); // no-op

        // 12, 13: Defaults
        System.out.println("\n=== orElse / orElseGet ===");
        System.out.println("Status: " + getOrderStatus(order1));     // found
        System.out.println("Status: " + getOrderStatus(noOrder));    // default
        System.out.println("Amount: $" + getPaymentAmount(order2));  // fallback computed
        System.out.println("Amount: $" + getPaymentAmount(noOrder)); // fallback computed once

        // 14: orElseThrow (safe demo — catch exception)
        System.out.println("\n=== orElseThrow ===");
        try {
            Payment p = requirePayment(order1);
            System.out.println("Payment: " + p.method());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // 9, 3, 4: Transformations
        System.out.println("\n=== Transformations ===");
        System.out.println("Domain: " + extractEmailDomain(user1));
        System.out.println("Active user: " + findActiveUser("alice@example.com"));
        System.out.println("Payment amount: " + getOrderPaymentAmount(order1));

        // 2, 6, 15: Metadata
        demonstrateEquals();
        demonstrateHashCode();
        demonstrateToString();

        // 5: get() — DEMONSTRATE DANGER (but safely)
        System.out.println("\n=== ⚠️ get() — Use With Extreme Caution ===");
        if (user1.isPresent()) {
            System.out.println("Safe get(): " + user1.get().name()); // OK (guarded)
        }
        // Never do: user1.get() without check — would crash for empty Optional!

        System.out.println("\n✅ All 16 Optional methods demonstrated — safely and idiomatically.");
        System.out.println("💡 Pro Tip: Prefer map/flatMap/filter/orElse over isPresent+get.");
    }
}