package com.bank;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface defining contract for loan eligibility evaluation.
 * Demonstrates Interfaces and Abstraction.
 */
interface Evaluatable {
    boolean evaluateEligibility(Customer customer) throws Exceptions.LoanDeniedException;
}

/**
 * Abstract Base Class for Loans.
 * Demonstrates Abstraction, Polymorphism, and Financial Calculations.
 */
public abstract class Loan implements Evaluatable {
    private String loanId;
    private String customerId;
    protected double principalAmount;
    protected double annualInterestRate; // Annual percentage rate e.g. 8.5%
    protected int tenureMonths;
    protected String status; // "PENDING", "APPROVED", "DENIED"

    public Loan(String loanId, String customerId, double principalAmount, double annualInterestRate, int tenureMonths) {
        if (principalAmount <= 0 || tenureMonths <= 0 || annualInterestRate <= 0) {
            throw new IllegalArgumentException("Loan parameters must be positive numbers.");
        }
        this.loanId = loanId;
        this.customerId = customerId;
        this.principalAmount = principalAmount;
        this.annualInterestRate = annualInterestRate;
        this.tenureMonths = tenureMonths;
        this.status = "PENDING";
    }

    public String getLoanId() {
        return loanId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculates Equated Monthly Installment (EMI) using the standard financial formula:
     * EMI = [P x R x (1+R)^N] / [(1+R)^N - 1]
     */
    public double calculateEMI() {
        double monthlyRate = (annualInterestRate / 100.0) / 12.0;
        double mathPower = Math.pow(1 + monthlyRate, tenureMonths);
        return (principalAmount * monthlyRate * mathPower) / (mathPower - 1);
    }

    /**
     * Calculates total interest payable over loan tenure.
     */
    public double calculateTotalInterest() {
        return (calculateEMI() * tenureMonths) - principalAmount;
    }

    /**
     * Generates a monthly amortization schedule breakdown.
     */
    public List<String> generateAmortizationSchedule() {
        List<String> schedule = new ArrayList<>();
        double monthlyRate = (annualInterestRate / 100.0) / 12.0;
        double emi = calculateEMI();
        double remainingBalance = principalAmount;

        schedule.add(String.format("%-6s | %-12s | %-12s | %-12s | %-14s",
                "Month", "EMI ($)", "Principal ($)", "Interest ($)", "Balance ($)"));
        schedule.add("------------------------------------------------------------------");

        for (int month = 1; month <= tenureMonths; month++) {
            double interestForMonth = remainingBalance * monthlyRate;
            double principalForMonth = emi - interestForMonth;
            remainingBalance -= principalForMonth;

            if (remainingBalance < 0) remainingBalance = 0;

            schedule.add(String.format("%-6d | %-12.2f | %-12.2f | %-12.2f | %-14.2f",
                    month, emi, principalForMonth, interestForMonth, remainingBalance));
        }
        return schedule;
    }

    public abstract String getLoanType();

    @Override
    public String toString() {
        return String.format("%s Loan [%s] - Customer ID: %s | Amount: $%.2f | Rate: %.2f%% | Tenure: %d mos | EMI: $%.2f | Status: %s",
                getLoanType(), loanId, customerId, principalAmount, annualInterestRate, tenureMonths, calculateEMI(), status);
    }
}

/**
 * Concrete Personal Loan evaluation model.
 */
class PersonalLoan extends Loan {
    private static final double MAX_DTI_THRESHOLD = 40.0;
    private static final int MIN_CREDIT_SCORE = 650;

    public PersonalLoan(String loanId, String customerId, double principalAmount, double annualInterestRate, int tenureMonths) {
        super(loanId, customerId, principalAmount, annualInterestRate, tenureMonths);
    }

    @Override
    public boolean evaluateEligibility(Customer customer) throws Exceptions.LoanDeniedException {
        if (customer.getCreditScore() < MIN_CREDIT_SCORE) {
            status = "DENIED";
            throw new Exceptions.LoanDeniedException(
                String.format("Credit score (%d) is below the minimum required score (%d) for Personal Loan.",
                        customer.getCreditScore(), MIN_CREDIT_SCORE));
        }

        double newMonthlyDebt = customer.getExistingDebt() + calculateEMI();
        double projectedDTI = (newMonthlyDebt / customer.getMonthlyIncome()) * 100.0;

        if (projectedDTI > MAX_DTI_THRESHOLD) {
            status = "DENIED";
            throw new Exceptions.LoanDeniedException(
                String.format("Projected Debt-To-Income ratio (%.1f%%) exceeds maximum allowed threshold (%.1f%%).",
                        projectedDTI, MAX_DTI_THRESHOLD));
        }

        double maxLoanLimit = customer.getMonthlyIncome() * 10;
        if (principalAmount > maxLoanLimit) {
            status = "DENIED";
            throw new Exceptions.LoanDeniedException(
                String.format("Requested amount ($%.2f) exceeds maximum eligible limit ($%.2f based on 10x monthly income).",
                        principalAmount, maxLoanLimit));
        }

        status = "APPROVED";
        return true;
    }

    @Override
    public String getLoanType() {
        return "Personal";
    }
}

/**
 * Concrete Home Loan evaluation model with collateral & down payment evaluation.
 */
class HomeLoan extends Loan {
    private double propertyValue;
    private double downPayment;
    private static final double MAX_LTV_RATIO = 80.0; // Loan to Value <= 80%
    private static final double MAX_DTI_THRESHOLD = 45.0;
    private static final int MIN_CREDIT_SCORE = 620;

    public HomeLoan(String loanId, String customerId, double principalAmount, double annualInterestRate, int tenureMonths,
                    double propertyValue, double downPayment) {
        super(loanId, customerId, principalAmount, annualInterestRate, tenureMonths);
        this.propertyValue = propertyValue;
        this.downPayment = downPayment;
    }

    public double getPropertyValue() {
        return propertyValue;
    }

    public double getDownPayment() {
        return downPayment;
    }

    @Override
    public boolean evaluateEligibility(Customer customer) throws Exceptions.LoanDeniedException {
        if (customer.getCreditScore() < MIN_CREDIT_SCORE) {
            status = "DENIED";
            throw new Exceptions.LoanDeniedException(
                String.format("Credit score (%d) is below minimum threshold (%d) for Home Loan.",
                        customer.getCreditScore(), MIN_CREDIT_SCORE));
        }

        double ltv = (principalAmount / propertyValue) * 100.0;
        if (ltv > MAX_LTV_RATIO) {
            status = "DENIED";
            throw new Exceptions.LoanDeniedException(
                String.format("Loan-To-Value ratio (%.1f%%) exceeds maximum allowed limit (%.1f%%). Please increase down payment.",
                        ltv, MAX_LTV_RATIO));
        }

        double newMonthlyDebt = customer.getExistingDebt() + calculateEMI();
        double projectedDTI = (newMonthlyDebt / customer.getMonthlyIncome()) * 100.0;
        if (projectedDTI > MAX_DTI_THRESHOLD) {
            status = "DENIED";
            throw new Exceptions.LoanDeniedException(
                String.format("Projected Debt-To-Income ratio (%.1f%%) exceeds maximum threshold (%.1f%%).",
                        projectedDTI, MAX_DTI_THRESHOLD));
        }

        status = "APPROVED";
        return true;
    }

    @Override
    public String getLoanType() {
        return "Home";
    }
}
