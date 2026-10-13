import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM(scanner);
        boolean running = true;

        while (running) {
            System.out.println("\n===== ATM MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            int choice = readMenuChoice(scanner);
            switch (choice) {
                case 1:
                    atm.createAccount();
                    break;
                case 2:
                    Account account = atm.login();
                    if (account != null) {
                        atm.startSession(account);
                    }
                    break;
                case 3:
                    running = false;
                    System.out.println("Thank you for using the ATM Management System.");
                    break;
                default:
                    System.out.println("Please choose a listed option.");
            }
        }

        scanner.close();
    }

    private static int readMenuChoice(Scanner scanner) {
        while (true) {
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    return choice;
                }
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a whole number.");
                continue;
            }
            System.out.println("Enter 1, 2, or 3.");
        }
    }
}