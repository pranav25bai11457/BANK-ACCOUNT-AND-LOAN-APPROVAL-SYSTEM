package com.bank;

import java.util.List;
import java.util.Scanner;

/**
 * Main Console Application Entry Point.
 * Interactive menu system for Bank Account & Loan Evaluation System.
 */
public class Main {
    private static BankService bankService = new BankService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==================================================================");
        System.out.println("             BANK ACCOUNT & LOAN EVALUATION SYSTEM                ");
        System.out.println("==================================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your choice (1-7): ", 1, 7);

            switch (choice) {
                case 1:
                    handleCustomerMenu();
                    break;
                case 2:
                    handleAccountMenu();
                    break;
                case 3:
                    handleLoanEvaluatorMenu();
                    break;
                case 4:
                    handleExportMenu();
                    break;
                case 5:
                    bankService.loadSampleData();
                    System.out.println("\n[SUCCESS] Sample demo data loaded successfully!");
                    System.out.println("Loaded 3 Customers, Savings & Checking Accounts ready for testing.\n");
                    break;
                case 6:
                    runSystemSelfTests();
                    break;
                case 7:
                    running = false;
                    System.out.println("\nThank you for using Bank Account & Loan Evaluation System. Goodbye!");
                    break;
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n==================================================================");
        System.out.println("                          MAIN MENU                               ");
        System.out.println("==================================================================");
        System.out.println(" 1. Customer Management");
        System.out.println(" 2. Account Operations (Deposit / Withdraw / Transfer)");
        System.out.println(" 3. Loan Eligibility Evaluator & Amortization");
        System.out.println(" 4. Export Account & Loan Reports to File");
        System.out.println(" 5. Load Demo Sample Data");
        System.out.println(" 6. Run System Unit Self-Tests");
        System.out.println(" 7. Exit System");
        System.out.println("==================================================================");
    }

    // --- Sub-Menu Handlers ---

    private static void handleCustomerMenu() {
        System.out.println("\n--- Customer Management ---");
        System.out.println("1. Register New Customer");
        System.out.println("2. View Customer Details");
        System.out.println("3. List All Customers");
        int choice = readIntInput("Select option (1-3): ", 1, 3);

        try {
            if (choice == 1) {
                System.out.print("Enter Customer ID (e.g. CUST-201): ");
                String id = scanner.nextLine().trim();
                System.out.print("Enter Full Name: ");
                String name = scanner.nextLine().trim();
                double income = readDoubleInput("Enter Monthly Gross Income ($): ", 0, 1000000);
                int creditScore = readIntInput("Enter Credit Score (300-850): ", 300, 850);
                double debt = readDoubleInput("Enter Total Existing Monthly Debt ($): ", 0, 500000);

                Customer customer = bankService.registerCustomer(id, name, income, creditScore, debt);
                System.out.println("\n[SUCCESS] Customer Registered: " + customer);
            } else if (choice == 2) {
                System.out.print("Enter Customer ID: ");
                String id = scanner.nextLine().trim();
                Customer customer = bankService.getCustomer(id);
                System.out.println("\n" + customer);
            } else if (choice == 3) {
                List<Customer> list = bankService.getAllCustomers();
                if (list.isEmpty()) {
                    System.out.println("No customers registered yet. Tip: Option 5 on Main Menu loads demo data.");
                } else {
                    System.out.println("\n--- Registered Customers ---");
                    for (Customer c : list) {
                        System.out.println(c);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    private static void handleAccountMenu() {
        System.out.println("\n--- Account Operations ---");
        System.out.println("1. Open Savings Account");
        System.out.println("2. Open Checking Account");
        System.out.println("3. Deposit Funds");
        System.out.println("4. Withdraw Funds");
        System.out.println("5. Transfer Funds");
        System.out.println("6. Apply Monthly Interest (Savings Accounts)");
        System.out.println("7. View Account Details & History");
        int choice = readIntInput("Select option (1-7): ", 1, 7);

        try {
            switch (choice) {
                case 1: {
                    System.out.print("Enter Customer ID: ");
                    String id = scanner.nextLine().trim();
                    double initialBalance = readDoubleInput("Enter Initial Deposit ($): ", 0, 1000000);
                    double rate = readDoubleInput("Enter Annual Interest Rate (%): ", 0.1, 20.0);
                    SavingsAccount sa = bankService.openSavingsAccount(id, initialBalance, rate);
                    System.out.println("\n[SUCCESS] Opened Savings Account: " + sa);
                    break;
                }
                case 2: {
                    System.out.print("Enter Customer ID: ");
                    String id = scanner.nextLine().trim();
                    double initialBalance = readDoubleInput("Enter Initial Deposit ($): ", 0, 1000000);
                    double overdraft = readDoubleInput("Enter Overdraft Limit ($): ", 0, 10000.0);
                    CheckingAccount ca = bankService.openCheckingAccount(id, initialBalance, overdraft);
                    System.out.println("\n[SUCCESS] Opened Checking Account: " + ca);
                    break;
                }
                case 3: {
                    System.out.print("Enter Account Number: ");
                    String accNum = scanner.nextLine().trim();
                    double amount = readDoubleInput("Enter Deposit Amount ($): ", 0.01, 1000000);
                    bankService.deposit(accNum, amount);
                    Account acc = bankService.getAccount(accNum);
                    System.out.printf("\n[SUCCESS] Deposit complete. Updated Balance: $%.2f\n", acc.getBalance());
                    break;
                }
                case 4: {
                    System.out.print("Enter Account Number: ");
                    String accNum = scanner.nextLine().trim();
                    double amount = readDoubleInput("Enter Withdrawal Amount ($): ", 0.01, 1000000);
                    bankService.withdraw(accNum, amount);
                    Account acc = bankService.getAccount(accNum);
                    System.out.printf("\n[SUCCESS] Withdrawal complete. Updated Balance: $%.2f\n", acc.getBalance());
                    break;
                }
                case 5: {
                    System.out.print("Enter Source Account Number: ");
                    String src = scanner.nextLine().trim();
                    System.out.print("Enter Destination Account Number: ");
                    String dest = scanner.nextLine().trim();
                    double amount = readDoubleInput("Enter Transfer Amount ($): ", 0.01, 1000000);
                    bankService.transferFunds(src, dest, amount);
                    System.out.println("\n[SUCCESS] Fund transfer completed successfully!");
                    break;
                }
                case 6: {
                    System.out.print("Enter Savings Account Number: ");
                    String accNum = scanner.nextLine().trim();
                    Account acc = bankService.getAccount(accNum);
                    if (acc instanceof SavingsAccount) {
                        double interest = ((SavingsAccount) acc).applyMonthlyInterest();
                        System.out.printf("\n[SUCCESS] Applied monthly interest of $%.2f. New Balance: $%.2f\n", interest, acc.getBalance());
                    } else {
                        System.out.println("\n[ERROR] Monthly interest can only be applied to Savings Accounts.");
                    }
                    break;
                }
                case 7: {
                    System.out.print("Enter Account Number: ");
                    String accNum = scanner.nextLine().trim();
                    Account acc = bankService.getAccount(accNum);
                    System.out.println("\n" + acc);
                    System.out.println("\n--- Transaction History ---");
                    for (String log : acc.getTransactionHistory()) {
                        System.out.println(log);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    private static void handleLoanEvaluatorMenu() {
        System.out.println("\n--- Loan Eligibility Evaluator & Amortization ---");
        System.out.println("1. Apply for Personal Loan");
        System.out.println("2. Apply for Home Loan (Mortgage)");
        System.out.println("3. View All Loan Applications");
        int choice = readIntInput("Select option (1-3): ", 1, 3);

        try {
            if (choice == 1) {
                System.out.print("Enter Customer ID: ");
                String id = scanner.nextLine().trim();
                double amount = readDoubleInput("Enter Loan Amount ($): ", 500, 500000);
                double rate = readDoubleInput("Enter Annual Interest Rate (%): ", 1.0, 30.0);
                int tenure = readIntInput("Enter Tenure in Months (6-360): ", 6, 360);

                System.out.println("\nEvaluating Personal Loan Application...");
                PersonalLoan loan = bankService.applyPersonalLoan(id, amount, rate, tenure);

                System.out.println("\n==================================================================");
                System.out.println("               LOAN APPLICATION RESULT: " + loan.getStatus());
                System.out.println("==================================================================");
                System.out.println(loan);
                System.out.printf("Equated Monthly Installment (EMI): $%.2f\n", loan.calculateEMI());
                System.out.printf("Total Interest Payable: $%.2f\n", loan.calculateTotalInterest());

                System.out.print("\nDisplay monthly amortization schedule? (y/n): ");
                String printSched = scanner.nextLine().trim();
                if (printSched.equalsIgnoreCase("y")) {
                    System.out.println("\n--- Amortization Schedule ---");
                    for (String line : loan.generateAmortizationSchedule()) {
                        System.out.println(line);
                    }
                }
            } else if (choice == 2) {
                System.out.print("Enter Customer ID: ");
                String id = scanner.nextLine().trim();
                double propertyVal = readDoubleInput("Enter Property Valuation ($): ", 10000, 10000000);
                double downPay = readDoubleInput("Enter Down Payment ($): ", 0, propertyVal);
                double amount = readDoubleInput("Enter Requested Loan Amount ($): ", 1000, propertyVal - downPay);
                double rate = readDoubleInput("Enter Annual Interest Rate (%): ", 1.0, 20.0);
                int tenure = readIntInput("Enter Tenure in Months (12-360): ", 12, 360);

                System.out.println("\nEvaluating Home Loan Application...");
                HomeLoan loan = bankService.applyHomeLoan(id, amount, rate, tenure, propertyVal, downPay);

                System.out.println("\n==================================================================");
                System.out.println("               LOAN APPLICATION RESULT: " + loan.getStatus());
                System.out.println("==================================================================");
                System.out.println(loan);
                System.out.printf("Loan-to-Value (LTV) Ratio: %.1f%%\n", (amount / propertyVal) * 100.0);
                System.out.printf("Equated Monthly Installment (EMI): $%.2f\n", loan.calculateEMI());
                System.out.printf("Total Interest Payable: $%.2f\n", loan.calculateTotalInterest());

                System.out.print("\nDisplay monthly amortization schedule? (y/n): ");
                String printSched = scanner.nextLine().trim();
                if (printSched.equalsIgnoreCase("y")) {
                    System.out.println("\n--- Amortization Schedule ---");
                    for (String line : loan.generateAmortizationSchedule()) {
                        System.out.println(line);
                    }
                }
            } else if (choice == 3) {
                List<Loan> loans = bankService.getAllLoans();
                if (loans.isEmpty()) {
                    System.out.println("\nNo loan applications recorded yet.");
                } else {
                    System.out.println("\n--- Loan Applications Summary ---");
                    for (Loan l : loans) {
                        System.out.println(l);
                    }
                }
            }
        } catch (Exceptions.LoanDeniedException e) {
            System.out.println("\n==================================================================");
            System.out.println("               LOAN APPLICATION RESULT: DENIED                    ");
            System.out.println("==================================================================");
            System.out.println("Reason for Denial: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[ERROR] " + e.getMessage());
        }
    }

    private static void handleExportMenu() {
        System.out.println("\n--- Export Reports to File ---");
        System.out.println("1. Export Account Statement");
        System.out.println("2. Export Loan Amortization Report");
        int choice = readIntInput("Select option (1-2): ", 1, 2);

        try {
            if (choice == 1) {
                System.out.print("Enter Account Number: ");
                String accNum = scanner.nextLine().trim();
                Account acc = bankService.getAccount(accNum);
                Customer cust = bankService.getCustomer(acc.getCustomerId());
                String filePath = FileUtils.exportAccountStatement(acc, cust);
                System.out.println("\n[SUCCESS] Statement exported to file:");
                System.out.println(filePath);
            } else if (choice == 2) {
                List<Loan> loans = bankService.getAllLoans();
                if (loans.isEmpty()) {
                    System.out.println("No active loans available to export. Apply for a loan first!");
                    return;
                }
                System.out.println("Available Loans:");
                for (int i = 0; i < loans.size(); i++) {
                    System.out.printf("%d. %s\n", (i + 1), loans.get(i));
                }
                int idx = readIntInput("Select loan index: ", 1, loans.size()) - 1;
                Loan loan = loans.get(idx);
                Customer cust = bankService.getCustomer(loan.getCustomerId());
                String filePath = FileUtils.exportLoanReport(loan, cust);
                System.out.println("\n[SUCCESS] Loan statement exported to file:");
                System.out.println(filePath);
            }
        } catch (Exception e) {
            System.out.println("\n[ERROR] Export failed: " + e.getMessage());
        }
    }

    private static void runSystemSelfTests() {
        System.out.println("\n==================================================================");
        System.out.println("               RUNNING SYSTEM SELF-TEST SUITE                     ");
        System.out.println("==================================================================");

        int total = 0;
        int passed = 0;

        // Test 1: Customer Creation & DTI Calculation
        total++;
        try {
            Customer c = new Customer("T101", "Test User", 5000, 750, 1000);
            assert c.calculateDTI() == 20.0;
            System.out.println("[PASS] Test 1: Customer creation & DTI calculation (DTI = 20.0%)");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 1: " + t.getMessage());
        }

        // Test 2: Savings Account Minimum Balance Exception
        total++;
        try {
            SavingsAccount sa = new SavingsAccount("SA-TEST", "T101", 150, 4.0);
            sa.withdraw(100); // Should fail because min balance 100 required (leaving 50 < 100)
            System.out.println("[FAIL] Test 2: Minimum balance enforcement did not throw exception!");
        } catch (Exceptions.InsufficientBalanceException e) {
            System.out.println("[PASS] Test 2: Minimum balance enforcement caught correctly.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 2 unexpected exception: " + t.getMessage());
        }

        // Test 3: Checking Account Overdraft
        total++;
        try {
            CheckingAccount ca = new CheckingAccount("CA-TEST", "T101", 200, 300);
            ca.withdraw(400); // Available 500. Uses 200 overdraft + 15 fee. Balance = -215
            assert ca.getBalance() == -215.0;
            System.out.println("[PASS] Test 3: Checking overdraft & fee deduction verified.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 3: " + t.getMessage());
        }

        // Test 4: Personal Loan Credit Score Threshold
        total++;
        try {
            Customer lowCredit = new Customer("T102", "Low Credit", 6000, 550, 500);
            PersonalLoan pl = new PersonalLoan("PL-TEST", "T102", 5000, 10.0, 24);
            pl.evaluateEligibility(lowCredit);
            System.out.println("[FAIL] Test 4: Low credit loan application was not denied!");
        } catch (Exceptions.LoanDeniedException e) {
            System.out.println("[PASS] Test 4: Low credit score loan denial verified.");
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] Test 4: " + t.getMessage());
        }

        System.out.println("------------------------------------------------------------------");
        System.out.printf("Test Suite Completed: %d/%d Tests Passed (%.0f%%)\n", passed, total, (passed * 100.0 / total));
        System.out.println("==================================================================");
    }

    // --- Input Validation Helpers ---

    private static int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    private static double readDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a value between $%.2f and $%.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid decimal number.");
            }
        }
    }
}
