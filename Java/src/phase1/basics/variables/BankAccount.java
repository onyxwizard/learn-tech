// phase1/basics/variables/BankAccount.java

package phase1.basics.variables;

/**
 * 🏦 BankAccount class demonstrating ALL 4 kinds of Java variables:
 *
 * 1️⃣ INSTANCE VARIABLE     → balance (unique per object)
 * 2️⃣ CLASS (STATIC) FIELD  → totalAccounts (shared across all objects)
 * 3️⃣ CONSTANT             → MIN_BALANCE (static final + UPPER_SNAKE_CASE)
 * 4️⃣ PARAMETERS & LOCALS   → in methods/constructor
 *
 * 📏 Naming Conventions Followed:
 * - camelCase for variables: balance, totalAccounts
 * - UPPER_SNAKE_CASE for constants: MIN_BALANCE
 * - NO $ or _ at start → clean, readable code!
 */
public class BankAccount {

    // 🔸 INSTANCE VARIABLE (non-static field)
    // → Unique to each BankAccount object
    private double balance;

    // 🔸 CLASS VARIABLE (static field)
    // → Shared by ALL instances of BankAccount
    private static int totalAccounts = 0;

    // 🔸 CONSTANT (static final → immutable)
    // → Naming: UPPER_SNAKE_CASE (Java convention for constants)
    private static final double MIN_BALANCE = 0.0;

    // 🧱 CONSTRUCTOR
    // → 'initialDeposit' is a PARAMETER (a variable!)
    public BankAccount(double initialDeposit) {
        if (initialDeposit >= MIN_BALANCE) {
            this.balance = initialDeposit;
        } else {
            this.balance = MIN_BALANCE;
            System.out.println("⚠️ Warning: Deposit too low. Balance set to $" + MIN_BALANCE);
        }
        totalAccounts++; // Increment shared counter
    }

    // 💰 DEPOSIT METHOD
    // → 'amount' = PARAMETER
    // → No unnecessary local variable (direct update)
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount; // Arithmetic operator +=
            System.out.printf("✅ Deposited $%.2f. New balance: $%.2f%n", amount, balance);
        } else {
            System.out.println("❌ Deposit amount must be positive.");
        }
    }

    // 🏧 WITHDRAW METHOD
    // → FIXED LOGIC: now checks if amount > balance (was backwards!)
    // → Uses LOCAL VARIABLE for clarity (optional but clean)
    public boolean withdraw(double amount) {
        // 🔸 LOCAL VARIABLE: exists only inside this method
        boolean sufficientFunds = (amount <= balance);

        if (amount <= 0) {
            System.out.println("❌ Withdrawal amount must be positive.");
            return false;
        }

        if (!sufficientFunds) {
            System.out.println("❌ Insufficient funds! Current balance: $" + String.format("%.2f", balance));
            return false;
        }

        balance -= amount; // Arithmetic operator -=
        System.out.printf("💸 Withdrew $%.2f. Remaining balance: $%.2f%n", amount, balance);
        return true;
    }

    // 👀 GETTER for private field (encapsulation best practice)
    public double getBalance() {
        return balance;
    }

    // 📊 STATIC UTILITY METHOD
    // → Accesses static field 'totalAccounts'
    public static int getTotalAccounts() {
        return totalAccounts;
    }
}