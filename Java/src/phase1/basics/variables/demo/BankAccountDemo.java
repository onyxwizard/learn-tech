// phase1/basics/variables/demo/BankAccountDemo.java

package phase1.basics.variables.demo;

import phase1.basics.variables.BankAccount;

/**
 * 🚀 Demo class to test BankAccount behavior.
 *
 * Highlights:
 * - Creating objects (instances)
 * - Calling methods with PARAMETERS
 * - Accessing static data via class name
 * - Using printf for formatted output
 */
public class BankAccountDemo {
    public static void main(String[] args) {
        // 'args' → PARAMETER of main() → another variable!

        System.out.println("🏦 Creating bank accounts...\n");

        // 🔸 Create two BankAccount objects
        BankAccount account1 = new BankAccount(100.0); // initial deposit
        BankAccount account2 = new BankAccount(-50.0); // triggers warning

        // 💰 Perform transactions
        account1.deposit(25.50);
        account1.withdraw(30.0);

        account2.deposit(200.0);
        account2.withdraw(250.0); // should fail

        // 👀 Print balances using getter (not direct field access!)
        System.out.printf("\n👤 Account 1 balance: $%.2f%n", account1.getBalance());
        System.out.printf("👤 Account 2 balance: $%.2f%n", account2.getBalance());

        // 📊 Print total accounts using STATIC method
        System.out.println("\n📊 Total accounts created: " + BankAccount.getTotalAccounts());
    }
}