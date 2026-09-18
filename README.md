# Bank Account & Loan Evaluator System

A modular Java application for managing customer bank accounts and evaluating loan eligibility with automated amortization schedules.

---

## Key Features

1. **Customer Management**: Profile tracking with income, credit score, debt levels, and automated Debt-to-Income (DTI) ratio calculation.
2. **Bank Account Management**:
   - **Savings Accounts**: Monthly compounding interest and minimum balance enforcement ($100).
   - **Checking Accounts**: Overdraft protection with overdraft fee deduction.
   - **Fund Transfers**: Direct transfers between savings and checking accounts with rollback safety.
3. **Loan Eligibility Evaluator**:
   - **Personal Loans**: DTI assessment (<= 40%), credit score qualification (>= 650), income multiplier limits.
   - **Home Loans**: Loan-to-Value (LTV <= 80%) appraisal, down payment qualification, and mortgage EMI calculation.
   - **Amortization Schedule**: Full month-by-month principal, interest, and remaining balance schedule generator.
4. **File Export Utilities**: Export formal account transaction statements and loan evaluation reports to `.txt` files.
5. **Built-in System Self-Tests**: Self-verification suite testing minimum balance enforcement, overdraft fees, credit score denials, and math calculations.

---

## Object-Oriented Design Highlights

- **Encapsulation**: Private state fields in `Customer`, `Account`, and `Loan` with validation rules in setters and constructors.
- **Inheritance**: `SavingsAccount` and `CheckingAccount` inherit from `Account`. `PersonalLoan` and `HomeLoan` inherit from `Loan`.
- **Polymorphism**: Overridden `withdraw()` behaviors across savings and checking accounts, and overridden `evaluateEligibility()` across loan products.
- **Abstraction**: Abstract base classes `Account` and `Loan` defining template methods, combined with the `Evaluatable` interface.
- **Exception Handling**: Custom exception hierarchy (`InsufficientBalanceException`, `LoanDeniedException`, `InvalidAmountException`, `AccountNotFoundException`).
- **Collections Framework**: `HashMap` for fast customer/account lookup and `ArrayList` for transaction records and loan tracking.
- **File I/O**: `BufferedWriter` and `FileWriter` for saving statements.

---

## Project Structure

```
c:\Java_project\
├── src\
│   └── com\
│       └── bank\
│           ├── Customer.java          # Customer domain model & DTI calculator
│           ├── Account.java           # Account abstract base class, SavingsAccount & CheckingAccount
│           ├── Loan.java              # Evaluatable interface, Loan abstract class, PersonalLoan & HomeLoan
│           ├── Exceptions.java        # Custom exception classes
│           ├── BankService.java       # Account, transfer, and loan management logic
│           ├── FileUtils.java         # Statement & report file exporter
│           └── Main.java              # Interactive Console Menu
├── build.bat                          # Batch compilation script
├── run.bat                            # Batch execution script
└── README.md                          # Project documentation
```

---

## How to Compile and Run

### Option 1: Using Batch Scripts (Windows)

To compile the application:
```cmd
build.bat
```

To run the application:
```cmd
run.bat
```

### Option 2: Using Command Prompt / Terminal Manually

1. **Compile**:
   ```cmd
   javac -d bin src\com\bank\*.java
   ```

2. **Run**:
   ```cmd
   java -ea -cp bin com.bank.Main
   ```

---

## Usage Guide

Upon launching the application, select from the CLI main menu:
- Select **`5`** to load demo sample data instantly for testing.
- Select **`1`** to register a customer or view profiles.
- Select **`2`** to perform deposits, withdrawals, or transfers.
- Select **`3`** to evaluate a Personal or Home Loan and view the monthly amortization table.
- Select **`4`** to export statements to a `.txt` file in the project folder.
- Select **`6`** to execute the built-in system unit test suite.
