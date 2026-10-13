import java.util.Scanner;

public class ATM {
    private static final int MAX_ACCOUNTS = 10;

    private final Account[] accounts;
    private final Scanner scanner;
    private int accountCount;

    public ATM(Scanner scanner) {
        this.scanner = scanner;
        this.accounts = new Account[MAX_ACCOUNTS];
        this.accountCount = 0;
    }

    public void createAccount() {
        if (accountCount == accounts.length) {
            System.out.println("The ATM cannot store any more accounts.");
            return;
        }

        System.out.println("\n===== CREATE ACCOUNT =====");
        String accountNumber = readAccountNumber("Enter 12-digit account number: ");
        if (findAccount(accountNumber) != null) {
            System.out.println("That account number is already in use.");
            return;
        }

        String customerName = readRequiredText("Enter customer name: ");
        String pin = readNewPin("Choose a 4-digit PIN: ");
        String mobileNumber = readMobileNumber();
        String accountType = readAccountType();
        double initialBalance = readMoney("Enter initial balance (0 or more): ", true);

        Account account = new Account(accountNumber, customerName, pin,
                mobileNumber, accountType, initialBalance);
        account.addTransaction("Account opened", initialBalance);
        accounts[accountCount] = account;
        accountCount++;

        System.out.println("Account created successfully.");
        System.out.println("Your account number is " + accountNumber + ".");
    }

    public Account login() {
        System.out.println("\n===== LOGIN =====");
        String accountNumber = readAccountNumber("Enter 12-digit account number: ");
        Account account = findAccount(accountNumber);

        if (account == null) {
            System.out.println("Account number not found.");
            return null;
        }

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Enter PIN: ");
            String enteredPin = scanner.nextLine().trim();

            if (account.getPin().equals(enteredPin)) {
                System.out.println("Login successful. Welcome, " + account.getCustomerName() + ".");
                return account;
            }

            int attemptsLeft = 3 - attempt;
            if (attemptsLeft > 0) {
                System.out.println("Incorrect PIN. Attempts left: " + attemptsLeft);
            }
        }

        System.out.println("Too many incorrect PIN attempts. Returning to the main menu.");
        return null;
    }

    public void startSession(Account account) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Money");
            System.out.println("5. Mini Statement");
            System.out.println("6. Change PIN");
            System.out.println("7. Account Details");
            System.out.println("8. Logout");

            int choice = readChoice("Choose an option: ", 1, 8);
            switch (choice) {
                case 1:
                    checkBalance(account);
                    break;
                case 2:
                    deposit(account);
                    break;
                case 3:
                    withdraw(account);
                    break;
                case 4:
                    transfer(account);
                    break;
                case 5:
                    miniStatement(account);
                    break;
                case 6:
                    changePin(account);
                    break;
                case 7:
                    displayAccountDetails(account);
                    break;
                case 8:
                    loggedIn = false;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Please choose a listed option.");
            }
        }
    }

    public void checkBalance(Account account) {
        System.out.printf("Current balance: %.2f%n", account.getBalance());
    }

    public void deposit(Account account) {
        double amount = readMoney("Enter deposit amount: ", false);
        double updatedBalance = account.getBalance() + amount;
        if (!Double.isFinite(updatedBalance)) {
            System.out.println("That amount is too large to process.");
            return;
        }

        account.setBalance(updatedBalance);
        account.addTransaction("Deposit", amount);
        System.out.printf("Deposit successful. New balance: %.2f%n", account.getBalance());
    }

    public void withdraw(Account account) {
        double amount = readMoney("Enter withdrawal amount: ", false);
        if (amount > account.getBalance()) {
            System.out.println("Insufficient balance. Withdrawal cancelled.");
            return;
        }

        account.setBalance(account.getBalance() - amount);
        account.addTransaction("Withdrawal", amount);
        System.out.printf("Withdrawal successful. New balance: %.2f%n", account.getBalance());
    }

    public void transfer(Account sender) {
        String receiverNumber = readAccountNumber("Enter receiver's 12-digit account number: ");
        Account receiver = findAccount(receiverNumber);

        if (receiver == null) {
            System.out.println("Receiver account not found. Transfer cancelled.");
            return;
        }
        if (receiver == sender) {
            System.out.println("You cannot transfer money to the same account.");
            return;
        }

        double amount = readMoney("Enter transfer amount: ", false);
        if (amount > sender.getBalance()) {
            System.out.println("Insufficient balance. Transfer cancelled.");
            return;
        }

        double receiverBalance = receiver.getBalance() + amount;
        if (!Double.isFinite(receiverBalance)) {
            System.out.println("That amount is too large to process.");
            return;
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiverBalance);
        sender.addTransaction("Transfer sent to " + receiverNumber, amount);
        receiver.addTransaction("Transfer received", amount);
        System.out.printf("Transfer successful. Your new balance: %.2f%n", sender.getBalance());
    }

    public void miniStatement(Account account) {
        System.out.println("\n===== MINI STATEMENT =====");
        if (account.getTransactionCount() == 0) {
            System.out.println("No transactions to display.");
            return;
        }

        for (int index = account.getTransactionCount() - 1; index >= 0; index--) {
            System.out.printf("%-32s %.2f%n",
                    account.getTransactionType(index), account.getTransactionAmount(index));
        }
    }

    public void changePin(Account account) {
        System.out.print("Enter old PIN: ");
        String oldPin = scanner.nextLine().trim();
        if (!account.getPin().equals(oldPin)) {
            System.out.println("Incorrect old PIN. PIN was not changed.");
            return;
        }

        String newPin = readNewPin("Enter new 4-digit PIN: ");
        while (newPin.equals(oldPin)) {
            System.out.println("The new PIN must be different from the old PIN.");
            newPin = readNewPin("Enter new 4-digit PIN: ");
        }

        System.out.print("Confirm new PIN: ");
        String confirmation = scanner.nextLine().trim();
        if (!newPin.equals(confirmation)) {
            System.out.println("PINs do not match. PIN was not changed.");
            return;
        }

        account.setPin(newPin);
        System.out.println("PIN changed successfully.");
    }

    public void displayAccountDetails(Account account) {
        System.out.println("\n===== ACCOUNT DETAILS =====");
        System.out.println("Account number: " + account.getAccountNumber());
        System.out.println("Customer name: " + account.getCustomerName());
        System.out.println("Mobile number: " + account.getMobileNumber());
        System.out.println("Account type: " + account.getAccountType());
        System.out.printf("Balance: %.2f%n", account.getBalance());
    }

    private Account findAccount(String accountNumber) {
        for (int index = 0; index < accountCount; index++) {
            if (accounts[index].getAccountNumber().equals(accountNumber)) {
                return accounts[index];
            }
        }
        return null;
    }

    private String readAccountType() {
        System.out.println("Account type:");
        System.out.println("1. Savings");
        System.out.println("2. Current");
        int choice = readChoice("Choose account type: ", 1, 2);
        switch (choice) {
            case 1:
                return "Savings";
            case 2:
                return "Current";
            default:
                return "Savings";
        }
    }

    private String readMobileNumber() {
        while (true) {
            String mobileNumber = readRequiredText("Enter mobile number (digits only): ");
            if (containsOnlyDigits(mobileNumber)) {
                return mobileNumber;
            }
            System.out.println("Mobile number must contain digits only.");
        }
    }

    private String readAccountNumber(String prompt) {
        while (true) {
            String accountNumber = readRequiredText(prompt);
            if (accountNumber.length() == 12 && containsOnlyDigits(accountNumber)) {
                return accountNumber;
            }
            System.out.println("Account number must contain exactly 12 digits.");
        }
    }

    private String readNewPin(String prompt) {
        while (true) {
            String pin = readRequiredText(prompt);
            if (pin.length() == 4 && containsOnlyDigits(pin)) {
                return pin;
            }
            System.out.println("PIN must contain exactly 4 digits.");
        }
    }

    private boolean containsOnlyDigits(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int index = 0; index < value.length(); index++) {
            if (value.charAt(index) < '0' || value.charAt(index) > '9') {
                return false;
            }
        }
        return true;
    }

    private String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This value cannot be blank.");
        }
    }

    private int readChoice(String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= minimum && choice <= maximum) {
                    return choice;
                }
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
                continue;
            }
            System.out.println("Enter a number from " + minimum + " to " + maximum + ".");
        }
    }

    private double readMoney(String prompt, boolean allowZero) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double amount = Double.parseDouble(input);
                if (Double.isFinite(amount) && (allowZero ? amount >= 0 : amount > 0)) {
                    return amount;
                }
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            if (allowZero) {
                System.out.println("Enter an amount of zero or more.");
            } else {
                System.out.println("Enter an amount greater than zero.");
            }
        }
    }
}