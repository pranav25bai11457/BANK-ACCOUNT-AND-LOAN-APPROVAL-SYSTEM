package com.bank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Base Class for Bank Accounts.
 * Demonstrates Abstraction, Inheritance, and Encapsulation.
 */
public abstract class Account {
    private String accountNumber;
    private String customerId;
    protected double balance;
    private List<String> transactionHistory;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Account(String accountNumber, String customerId, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        recordTransaction(String.format("Account opened with initial balance: $%.2f", initialBalance));
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    protected void recordTransaction(String details) {
        String timestamp = LocalDateTime.now().format(formatter);
        transactionHistory.add(String.format("[%s] %s | New Balance: $%.2f", timestamp, details, balance));
    }

    /**
     * Deposits money into the account.
     */
    public void deposit(double amount) throws Exceptions.InvalidAmountException {
        if (amount <= 0) {
            throw new Exceptions.InvalidAmountException("Deposit amount must be greater than zero.");
        }
        balance += amount;
        recordTransaction(String.format("DEPOSIT: +$%.2f", amount));
    }

    /**
     * Abstract withdrawal method to be implemented by concrete subclasses.
     * Demonstrates Polymorphism.
     */
    public abstract void withdraw(double amount) throws Exceptions.BankException;

    /**
     * Abstract method returning account type description.
     */
    public abstract String getAccountType();

    @Override
    public String toString() {
        return String.format("%s Account [%s] - Customer ID: %s | Balance: $%.2f",
                getAccountType(), accountNumber, customerId, balance);
    }
}

/**
 * Savings Account with interest rate and minimum balance requirement.
 * Inherits from Account.
 */
class SavingsAccount extends Account {
    private double annualInterestRate; // e.g. 3.5 for 3.5%
    private static final double MINIMUM_BALANCE = 100.0;

    public SavingsAccount(String accountNumber, String customerId, double initialBalance, double annualInterestRate) {
        super(accountNumber, customerId, initialBalance);
        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * Calculates and applies monthly interest to savings balance.
     */
    public double applyMonthlyInterest() {
        double monthlyRate = (annualInterestRate / 100.0) / 12.0;
        double interestEarned = balance * monthlyRate;
        balance += interestEarned;
        recordTransaction(String.format("INTEREST APPLIED (%.2f%% APR): +$%.2f", annualInterestRate, interestEarned));
        return interestEarned;
    }

    @Override
    public void withdraw(double amount) throws Exceptions.BankException {
        if (amount <= 0) {
            throw new Exceptions.InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if ((balance - amount) < MINIMUM_BALANCE) {
            throw new Exceptions.InsufficientBalanceException(
                String.format("Savings Account must maintain a minimum balance of $%.2f. Current balance: $%.2f, Requested: $%.2f",
                        MINIMUM_BALANCE, balance, amount));
        }
        balance -= amount;
        recordTransaction(String.format("WITHDRAWAL: -$%.2f", amount));
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }
}

/**
 * Checking Account with overdraft facility.
 * Inherits from Account.
 */
class CheckingAccount extends Account {
    private double overdraftLimit;
    private static final double OVERDRAFT_FEE = 15.0;

    public CheckingAccount(String accountNumber, String customerId, double initialBalance, double overdraftLimit) {
        super(accountNumber, customerId, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public void withdraw(double amount) throws Exceptions.BankException {
        if (amount <= 0) {
            throw new Exceptions.InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        double availableFunds = balance + overdraftLimit;
        if (amount > availableFunds) {
            throw new Exceptions.InsufficientBalanceException(
                String.format("Exceeds overdraft limit! Max available (Balance + Overdraft): $%.2f, Requested: $%.2f",
                        availableFunds, amount));
        }

        balance -= amount;
        if (balance < 0) {
            balance -= OVERDRAFT_FEE;
            recordTransaction(String.format("WITHDRAWAL (OVERDRAFT USED): -$%.2f (Fee of $%.2f applied)", amount, OVERDRAFT_FEE));
        } else {
            recordTransaction(String.format("WITHDRAWAL: -$%.2f", amount));
        }
    }

    @Override
    public String getAccountType() {
        return "Checking";
    }
}
