package com.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing bank customers, accounts, transfers, and loan applications.
 * Demonstrates Collections Framework (HashMap, ArrayList) and Service-Layer logic.
 */
public class BankService {
    private Map<String, Customer> customers;
    private Map<String, Account> accounts;
    private List<Loan> loans;
    private int accountCounter = 1001;
    private int loanCounter = 5001;

    public BankService() {
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        this.loans = new ArrayList<>();
    }

    // --- Customer Management ---

    public Customer registerCustomer(String customerId, String name, double monthlyIncome, int creditScore, double existingDebt) {
        Customer customer = new Customer(customerId, name, monthlyIncome, creditScore, existingDebt);
        customers.put(customerId, customer);
        return customer;
    }

    public Customer getCustomer(String customerId) throws Exceptions.CustomerNotFoundException {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new Exceptions.CustomerNotFoundException("Customer with ID '" + customerId + "' not found.");
        }
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    // --- Account Management ---

    public SavingsAccount openSavingsAccount(String customerId, double initialBalance, double annualInterestRate)
            throws Exceptions.CustomerNotFoundException {
        getCustomer(customerId); // validate customer exists
        String accNum = "SA-" + (accountCounter++);
        SavingsAccount account = new SavingsAccount(accNum, customerId, initialBalance, annualInterestRate);
        accounts.put(accNum, account);
        return account;
    }

    public CheckingAccount openCheckingAccount(String customerId, double initialBalance, double overdraftLimit)
            throws Exceptions.CustomerNotFoundException {
        getCustomer(customerId); // validate customer exists
        String accNum = "CA-" + (accountCounter++);
        CheckingAccount account = new CheckingAccount(accNum, customerId, initialBalance, overdraftLimit);
        accounts.put(accNum, account);
        return account;
    }

    public Account getAccount(String accountNumber) throws Exceptions.AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new Exceptions.AccountNotFoundException("Account with number '" + accountNumber + "' not found.");
        }
        return account;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> getCustomerAccounts(String customerId) {
        List<Account> result = new ArrayList<>();
        for (Account acc : accounts.values()) {
            if (acc.getCustomerId().equals(customerId)) {
                result.add(acc);
            }
        }
        return result;
    }

    // --- Financial Operations ---

    public void deposit(String accountNumber, double amount) throws Exceptions.BankException {
        Account acc = getAccount(accountNumber);
        acc.deposit(amount);
    }

    public void withdraw(String accountNumber, double amount) throws Exceptions.BankException {
        Account acc = getAccount(accountNumber);
        acc.withdraw(amount);
    }

    public void transferFunds(String sourceAccNum, String destAccNum, double amount) throws Exceptions.BankException {
        if (sourceAccNum.equalsIgnoreCase(destAccNum)) {
            throw new Exceptions.InvalidAmountException("Source and destination accounts must be different.");
        }
        Account source = getAccount(sourceAccNum);
        Account dest = getAccount(destAccNum);

        source.withdraw(amount);
        try {
            dest.deposit(amount);
        } catch (Exceptions.InvalidAmountException e) {
            // Rollback source withdrawal if destination deposit fails
            source.deposit(amount);
            throw e;
        }
    }

    // --- Loan Application & Evaluation ---

    public PersonalLoan applyPersonalLoan(String customerId, double amount, double annualInterestRate, int tenureMonths)
            throws Exceptions.BankException {
        Customer customer = getCustomer(customerId);
        String loanId = "PL-" + (loanCounter++);
        PersonalLoan loan = new PersonalLoan(loanId, customerId, amount, annualInterestRate, tenureMonths);
        loan.evaluateEligibility(customer);
        loans.add(loan);
        return loan;
    }

    public HomeLoan applyHomeLoan(String customerId, double amount, double annualInterestRate, int tenureMonths,
                                 double propertyValue, double downPayment) throws Exceptions.BankException {
        Customer customer = getCustomer(customerId);
        String loanId = "HL-" + (loanCounter++);
        HomeLoan loan = new HomeLoan(loanId, customerId, amount, annualInterestRate, tenureMonths, propertyValue, downPayment);
        loan.evaluateEligibility(customer);
        loans.add(loan);
        return loan;
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    // --- Load Demo Sample Data ---

    public void loadSampleData() {
        try {
            // Customer 1: Strong Financial Profile
            registerCustomer("CUST-101", "Alex Morgan", 8500.0, 780, 1200.0);
            openSavingsAccount("CUST-101", 15000.0, 4.0);
            openCheckingAccount("CUST-101", 3500.0, 1000.0);

            // Customer 2: Average Financial Profile
            registerCustomer("CUST-102", "David Miller", 4200.0, 680, 1400.0);
            openSavingsAccount("CUST-102", 2500.0, 3.5);

            // Customer 3: High Debt Profile
            registerCustomer("CUST-103", "Sarah Connor", 3100.0, 610, 1800.0);
            openCheckingAccount("CUST-103", 800.0, 500.0);

        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }
}
