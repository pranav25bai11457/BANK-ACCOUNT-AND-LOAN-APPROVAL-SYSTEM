package com.bank;

/**
 * Represents a bank customer profile with financial metrics.
 * Demonstrates Encapsulation through private fields and getter/setter validation.
 */
public class Customer {
    private String customerId;
    private String name;
    private double monthlyIncome;
    private int creditScore;
    private double existingDebt;

    public Customer(String customerId, String name, double monthlyIncome, int creditScore, double existingDebt) {
        this.customerId = customerId;
        this.name = name;
        setMonthlyIncome(monthlyIncome);
        setCreditScore(creditScore);
        setExistingDebt(existingDebt);
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        if (monthlyIncome < 0) {
            throw new IllegalArgumentException("Monthly income cannot be negative.");
        }
        this.monthlyIncome = monthlyIncome;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        if (creditScore < 300 || creditScore > 850) {
            throw new IllegalArgumentException("Credit score must be between 300 and 850.");
        }
        this.creditScore = creditScore;
    }

    public double getExistingDebt() {
        return existingDebt;
    }

    public void setExistingDebt(double existingDebt) {
        if (existingDebt < 0) {
            throw new IllegalArgumentException("Existing debt cannot be negative.");
        }
        this.existingDebt = existingDebt;
    }

    /**
     * Calculates the Debt-to-Income (DTI) ratio percentage.
     * DTI = (Total Monthly Debt / Total Monthly Income) * 100
     */
    public double calculateDTI() {
        if (monthlyIncome == 0) return 100.0;
        return (existingDebt / monthlyIncome) * 100.0;
    }

    /**
     * Determines credit rating tier based on credit score.
     */
    public String getCreditRatingTier() {
        if (creditScore >= 750) return "Excellent";
        if (creditScore >= 700) return "Good";
        if (creditScore >= 650) return "Fair";
        return "Poor";
    }

    @Override
    public String toString() {
        return String.format("Customer ID: %s | Name: %s | Income: $%.2f | Credit Score: %d (%s) | Existing Debt: $%.2f | DTI: %.1f%%",
                customerId, name, monthlyIncome, creditScore, getCreditRatingTier(), existingDebt, calculateDTI());
    }
}
