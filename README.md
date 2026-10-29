# Core Java ATM Management System

## 1. What the project does

This console program lets a user create accounts and then log in to check a balance, deposit or withdraw money, transfer money, review recent transactions, change a PIN, view account details, and log out. It uses only the Java standard library (`Scanner` for console input). It does not use a database, files, JDBC, a framework, or external libraries.

Account data lives only in the running program. When the program exits, all accounts and transactions are lost. The ATM can hold up to 10 accounts, and each account keeps its 10 most recent transactions.

## 2. How the classes fit together

- `Account` represents one customer's account. It encapsulates account fields and holds that account's recent transactions.
- `ATM` owns an `Account[]`, finds accounts, validates input, and performs account operations. It receives the shared `Scanner` from `Main`.
- `Main` creates the `Scanner` and `ATM`, displays the main menu, and connects login to the logged-in ATM menu.

The array stores references to `Account` objects. `accountCount` marks how many positions are filled. To find an account, `ATM` loops through those positions and compares account numbers. A collection is unnecessary for this small, fixed-capacity beginner project.

## 3. Concepts used

- **Classes and objects:** `Account` and `ATM` define objects; `Main` creates them.
- **Encapsulation:** account fields are private and accessed through getters and setters.
- **Constructors:** `Account()` is the default constructor; the constructor with parameters initializes a new account. `ATM(Scanner)` receives its input source.
- **Methods:** account creation, login, deposits, transfers, and other work are separate methods instead of one large `main()`.
- **Conditions and `switch`:** validate choices and select the requested menu operation.
- **Loops:** `while` repeats menus and input validation; `for` searches arrays, stores transactions, and allows up to three PIN attempts.
- **Arrays:** the ATM stores accounts in `Account[]`; each account stores recent transaction types and amounts in parallel arrays.
- **Strings and `Scanner`:** names, PINs, account numbers, and console input are strings. PINs stay strings so a leading zero is preserved.
- **Exception handling:** `NumberFormatException` is caught when converting menu choices and money amounts.

Inheritance and polymorphism are not included because the requested operations do not need different account subtypes. Adding them here would make the example harder to follow without adding useful behavior.

## 4. Build and run

Open a terminal in this folder and run:

```text
javac Account.java ATM.java Main.java
java Main
```

Use a Java Development Kit (JDK), which includes both `javac` and `java`.

## 5. Step-by-step source guide

### Step 1: `Account.java`

An `Account` holds its account number, customer name, PIN, mobile number, account type, and balance. The fields are private; other classes use getters and setters. The default constructor creates an empty account, while the parameterized constructor receives all account details. The transaction arrays and count keep a small recent history. When all 10 slots are used, the oldest entry is shifted out before the newest one is added.

### Step 2: `ATM.java`

An `ATM` owns the fixed-size account array and the shared scanner. `createAccount()` rejects duplicate account numbers and validates the required fields. `login()` checks the account number and allows three PIN attempts. `startSession()` displays the account menu and calls the matching operation method.

Each money operation checks its input before changing a balance. A transfer checks that the receiver exists, is not the sender, the sender has enough money, and the receiver's balance will remain a valid number; only then does it update both accounts. `miniStatement()` prints the most recent entries first. Account details intentionally omit the PIN.

Private input helpers keep repeated checks in one place. Invalid menu or amount text is handled by catching `NumberFormatException` and asking again.

### Step 3: `Main.java`

`main()` creates one `Scanner` and passes it to the ATM. Its `while` loop keeps the main menu running. A successful login returns an `Account`; `Main` passes that account into the logged-in menu. Logging out returns to the main menu. Choosing Exit ends the loop and closes the scanner.

### Step 4: Connecting the classes

The call flow is:

```text
Main -> ATM.createAccount() -> new Account(...)
Main -> ATM.login() -> Account
Main -> ATM.startSession(Account) -> ATM operation -> Account getters/setters
```

Because the same `Account` object is stored in the ATM array and passed to the session, changing its balance or PIN updates the account held by the ATM.

### Step 5: Test the operations

Try this sequence manually:

1. Create account `123456789012` for Alice, PIN `0123`, type Savings, starting balance `500`.
2. Create account `987654321098` for Bob, PIN `9876`, type Current, starting balance `100`.
3. Try creating another account with number `123456789012`; it should be rejected.
4. Log in to `123456789012`. Check the balance, deposit `100`, withdraw `50`, and transfer `25` to `987654321098`.
5. Review the mini statement and account details. The sender's balance should be `525` and the receiver's balance should be `125`.
6. Change Alice's PIN from `0123` to `4321`, log out, and log in using `4321`.
7. Enter an incorrect PIN three times; login should stop and return to the main menu.
8. Try a negative deposit, a zero withdrawal, an unknown receiver, and a withdrawal greater than the balance. Each should be rejected without changing a balance.

## 6. Sample console output

Prompts and menu redraws are shortened below; the amounts illustrate the test sequence above.

### Create account

```text
===== CREATE ACCOUNT =====
Enter 12-digit account number: 123456789012
Enter customer name: Alice
Choose a 4-digit PIN: 0123
Enter mobile number (digits only): 5550100
Account type:
1. Savings
2. Current
Choose account type: 1
Enter initial balance (0 or more): 500
Account created successfully.
Your account number is 123456789012.
```

### Login and balance

```text
===== LOGIN =====
Enter 12-digit account number: 123456789012
Enter PIN: 0123
Login successful. Welcome, Alice.
Current balance: 500.00
```

### Deposit and withdrawal

```text
Enter deposit amount: 100
Deposit successful. New balance: 600.00
Enter withdrawal amount: 50
Withdrawal successful. New balance: 550.00
```

An invalid or unaffordable transaction does not change the balance:

```text
Enter deposit amount: -5
Enter an amount greater than zero.
Enter withdrawal amount: 10000
Insufficient balance. Withdrawal cancelled.
```

### Transfer

```text
Enter receiver's 12-digit account number: 987654321098
Enter transfer amount: 25
Transfer successful. Your new balance: 525.00
```

### Mini statement

```text
===== MINI STATEMENT =====
Transfer sent to 987654321098    25.00
Withdrawal                       50.00
Deposit                          100.00
Account opened                   500.00
```

### Change PIN and account details

```text
Enter old PIN: 0123
Enter new 4-digit PIN: 4321
Confirm new PIN: 4321
PIN changed successfully.

===== ACCOUNT DETAILS =====
Account number: 123456789012
Customer name: Alice
Mobile number: 5550100
Account type: Savings
Balance: 525.00
```

Notice the PIN is not printed in account details.

### Three incorrect PIN attempts and logout

```text
Enter PIN: 0000
Incorrect PIN. Attempts left: 2
Enter PIN: 0000
Incorrect PIN. Attempts left: 1
Enter PIN: 0000
Too many incorrect PIN attempts. Returning to the main menu.

Logged out successfully.
```

## 7. Common errors and solutions

- **`javac` is not recognized:** install a JDK and make sure its `bin` folder is on `PATH`; reopen the terminal afterward.
- **`cannot find symbol` for `Account` or `ATM`:** compile all three `.java` files from the project folder using the command above.
- **`class Account is public, should be declared in a file named Account.java`:** Java requires a public class name to match its filename, including capitalization.
- **A PIN such as `0123` loses its first zero:** represent PINs as `String`, not `int`.
- **The program says an amount is invalid:** deposit and withdrawal amounts must be greater than zero; the initial balance may be zero.
- **Account cannot be found after restarting:** this is expected because the program intentionally stores data only in memory.
- **The ATM refuses another account:** the fixed array capacity is 10 accounts; increase `MAX_ACCOUNTS` if the exercise needs more.

