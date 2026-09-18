package com.bank;

/**
 * Custom Exception hierarchy for the Banking and Loan Application.
 * Demonstrates Exception Handling concepts in Java.
 */

// Base Bank Exception
public class Exceptions {
    public static class BankException extends Exception {
        public BankException(String message) {
            super(message);
        }
    }

    // Thrown when an account operation attempts to withdraw more than available funds/overdraft
    public static class InsufficientBalanceException extends BankException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    // Thrown when a loan application does not meet qualification criteria
    public static class LoanDeniedException extends BankException {
        public LoanDeniedException(String message) {
            super(message);
        }
    }

    // Thrown when invalid parameters (e.g. negative deposit amount) are passed
    public static class InvalidAmountException extends BankException {
        public InvalidAmountException(String message) {
            super(message);
        }
    }

    // Thrown when requested account is not found in bank service repository
    public static class AccountNotFoundException extends BankException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

    // Thrown when requested customer ID is not found in repository
    public static class CustomerNotFoundException extends BankException {
        public CustomerNotFoundException(String message) {
            super(message);
        }
    }
}
