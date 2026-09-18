package com.bank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * File I/O Utility class for exporting bank statements and loan reports.
 * Demonstrates Java File I/O (FileWriter, BufferedWriter, Exceptions).
 */
public class FileUtils {

    /**
     * Exports account details and transaction history to a .txt file.
     */
    public static String exportAccountStatement(Account account, Customer customer) throws IOException {
        String fileName = "Statement_" + account.getAccountNumber() + ".txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("=================================================================\n");
            writer.write("                    BANK ACCOUNT STATEMENT                      \n");
            writer.write("=================================================================\n");
            writer.write("Generated On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Customer Name: " + customer.getName() + " (ID: " + customer.getCustomerId() + ")\n");
            writer.write("Account Type: " + account.getAccountType() + " | Account Number: " + account.getAccountNumber() + "\n");
            writer.write(String.format("Current Balance: $%.2f\n", account.getBalance()));
            writer.write("-----------------------------------------------------------------\n");
            writer.write("TRANSACTION HISTORY:\n");
            writer.write("-----------------------------------------------------------------\n");

            List<String> history = account.getTransactionHistory();
            if (history.isEmpty()) {
                writer.write("No transactions recorded.\n");
            } else {
                for (String record : history) {
                    writer.write(record + "\n");
                }
            }
            writer.write("=================================================================\n");
        }
        return file.getAbsolutePath();
    }

    /**
     * Exports loan details and amortization schedule to a .txt file.
     */
    public static String exportLoanReport(Loan loan, Customer customer) throws IOException {
        String fileName = "LoanReport_" + loan.getLoanId() + ".txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("=================================================================\n");
            writer.write("                   LOAN EVALUATION STATEMENT                     \n");
            writer.write("=================================================================\n");
            writer.write("Generated On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");
            writer.write("Customer Name: " + customer.getName() + " (ID: " + customer.getCustomerId() + ")\n");
            writer.write("Credit Score: " + customer.getCreditScore() + " | DTI Ratio: " + String.format("%.1f%%", customer.calculateDTI()) + "\n");
            writer.write("-----------------------------------------------------------------\n");
            writer.write("LOAN DETAILS:\n");
            writer.write("Loan Type: " + loan.getLoanType() + " Loan | Loan ID: " + loan.getLoanId() + "\n");
            writer.write(String.format("Principal Amount: $%.2f\n", loan.getPrincipalAmount()));
            writer.write(String.format("Annual Interest Rate: %.2f%%\n", loan.getAnnualInterestRate()));
            writer.write("Tenure: " + loan.getTenureMonths() + " months\n");
            writer.write(String.format("Equated Monthly Installment (EMI): $%.2f\n", loan.calculateEMI()));
            writer.write(String.format("Total Interest Payable: $%.2f\n", loan.calculateTotalInterest()));
            writer.write(String.format("Total Payable Amount: $%.2f\n", (loan.getPrincipalAmount() + loan.calculateTotalInterest())));
            writer.write("Application Status: " + loan.getStatus() + "\n");
            writer.write("-----------------------------------------------------------------\n");
            writer.write("AMORTIZATION SCHEDULE:\n");
            writer.write("-----------------------------------------------------------------\n");

            List<String> schedule = loan.generateAmortizationSchedule();
            for (String line : schedule) {
                writer.write(line + "\n");
            }
            writer.write("=================================================================\n");
        }
        return file.getAbsolutePath();
    }
}
