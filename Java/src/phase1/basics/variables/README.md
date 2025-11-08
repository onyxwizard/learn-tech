## 🏗️ Project: **Bank Account Manager** (Step-by-Step Construction Plan)

### 📌 Step 1: Create the Class
- **File name**: `BankAccount.java`
- **Class name**: `BankAccount`

> 💡 This aligns with Java’s rule: **filename must match public class name**.


### 📌 Step 2: Declare Fields (Class-Level Variables)

Add **three fields** to your class:

1. **Instance Variable**
    - Purpose: Store the balance of **each individual account**
    - Type: `double`
    - Name: `balance` → use **camelCase**, start with lowercase
    - Access: `private` (good practice, though not required for basics)

2. **Class Variable (Static)**
    - Purpose: Count **total number of accounts created** (shared across all instances)
    - Type: `int`
    - Name: `totalAccounts` → camelCase
    - Modifier: `static`
    - Initialize to `0`

3. **Constant (Static Final)**
    - Purpose: Define the **minimum allowed balance** (e.g., $0.00)
    - Type: `double`
    - Name: `MIN_BALANCE` → **UPPER_SNAKE_CASE** (because it’s `static final`)
    - Value: `0.0`

> ✅ This covers:
> - Instance vs. static fields
> - Naming conventions for regular vs. constant fields
> - Default vs. explicit initialization



### 📌 Step 3: Create a Constructor

- **Signature**: `public BankAccount(double initialDeposit)`
- **Parameter**: `initialDeposit` → this is a **parameter variable** (not a field!)
- **Logic**:
    - If `initialDeposit >= MIN_BALANCE`, set `balance = initialDeposit`
    - Else, set `balance = MIN_BALANCE` and print a warning
    - **Increment** `totalAccounts` by 1 (this is shared!)

> 🔍 Remember: Parameters are **variables**, not fields. They only exist during constructor execution.



### 📌 Step 4: Add Two Methods

#### A. `deposit(double amount)`
- **Parameter**: `amount` (variable)
- **Local variable**: Create one inside the method (e.g., `newBalance`) to compute updated balance
- **Logic**: Only deposit if `amount > 0`

#### B. `withdraw(double amount)`
- **Parameter**: `amount`
- **Local variable**: Use one to check if withdrawal is possible (e.g., `canWithdraw = balance >= amount`)
- **Return type**: `boolean` (`true` if successful)

> ✅ This demonstrates:
> - **Local variables** (declared inside method braces `{ }`)
> - **Parameters** as input variables
> - **Blocks** (method bodies are blocks!)


### 📌 Step 5: Add a Static Utility Method

- **Method**: `public static int getTotalAccounts()`
- **Purpose**: Return the value of the **static field** `totalAccounts`
- **Why static?** Because it’s about the **class**, not a specific object



### 📌 Step 6: Write the `main` Method

In `main(String[] args)`:
- Create **two `BankAccount` objects** with different initial deposits
- Call `deposit()` and `withdraw()` on them
- Print the **total number of accounts** using your static method
- (Optional) Try **invalid names** as comments to test your understanding:
  ```java
  // int 1account = 0;     // ❌ why illegal?
  // int $temp = 5;        // ❌ why discouraged?
  ```

> 🎯 `args` is a **parameter** → another example of a variable!



## ✅ What You’re Practicing (Mapped to dev.java)

| Concept | Where You Use It |
|--------|------------------|
| **Instance Variables** | `balance` |
| **Class (Static) Variables** | `totalAccounts` |
| **Constants** | `MIN_BALANCE` (static final + UPPER_SNAKE_CASE) |
| **Local Variables** | Inside `deposit()`, `withdraw()` |
| **Parameters** | `initialDeposit`, `amount`, `args` |
| **Naming Rules** | No `$`, no `_` start, camelCase, UPPER_SNAKE_CASE for constants |
| **Blocks** | Method bodies, `if` statements |



## 🚀 Your Task Now
1. Create `BankAccount.java`
2. Implement all steps above **in your own words**
3. Run it with: `java BankAccount.java`
4. **Submit your full code here**

Once you do, I’ll:
- ✅ Confirm it follows Java variable rules
- 🔍 Point out any naming or scoping issues
- 💡 Suggest improvements (if any)