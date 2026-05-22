# FinNova Bank — Smart Loan & Banking Management System

A console-based Java application simulating a banking system for FinNova Bank.
Built as a Java assessment covering OOP, Collections, File Handling, and Exception Handling.

---

## Project Structure

```
FinNovaBank/
├── src/
│   ├── Main.java                       ← Entry point, menu logic, all 11 features
│   ├── Person.java                     ← Abstract base class
│   ├── Customer.java                   ← Extends Person, encapsulated customer data
│   ├── Loan.java                       ← Loan data + SI/CI calculations
│   ├── BankAccount.java                ← Deposit/Withdraw with exception handling
│   ├── FileHandler.java                ← All file read/write operations
│   ├── InvalidAmountException.java     ← Custom exception for negative amounts
│   ├── InsufficientBalanceException.java ← Custom exception for overdraft
│   └── InvalidLoanException.java       ← Custom exception for loan limit breach
├── customers.txt                       ← Persisted customer records
├── loans.txt                           ← Persisted loan records
├── transactions.txt                    ← Appended transaction logs
└── README.md
```

---

## How to Compile & Run

### Step 1 — Compile all Java files
From the `FinNovaBank/` root directory:
```bash
javac -d out src/*.java
```

### Step 2 — Run the application
The data files (customers.txt, loans.txt, transactions.txt) must be in the directory where you run the program:
```bash
cd out
java Main
```

Or from root, copying the txt files:
```bash
javac -d out src/*.java
copy customers.txt out\
copy loans.txt out\
copy transactions.txt out\
cd out
java Main
```

---

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | Add Customer | Register a new customer with salary and initial balance |
| 2 | View Customers | Display all customer records formatted |
| 3 | Create Loan | Salary-based eligibility check, approve or reject |
| 4 | Calculate SI | Simple Interest = (P × R × T) / 100 |
| 5 | Calculate CI | Compound Interest = P × (1 + R/100)^T − P |
| 6 | Deposit Money | Add funds, auto-log transaction |
| 7 | Withdraw Money | Deduct funds with overdraft protection |
| 8 | View Transactions | Full transaction history from LinkedList |
| 9 | Search Customer | Find customer by ID |
| 10 | Generate Reports | Bank-wide statistics and summary |
| 11 | Exit | Saves all data back to files |

---

## Loan Eligibility Rules

| Monthly Salary | Max Loan |
|---------------|----------|
| Below ₹25,000 | ₹2,00,000 |
| ₹25,000 – ₹50,000 | ₹5,00,000 |
| Above ₹50,000 | ₹10,00,000 |

---

## Data File Formats

**customers.txt**
```
personId,name,phoneNumber,accountNumber,balance,salary,loanTaken
101,Rahul Sharma,9876543210,5001,25000.0,45000.0,false
```

**loans.txt**
```
loanId,customerId,loanType,principalAmount,rateOfInterest,timeInYears
L201,101,Home Loan,300000.0,8.0,3
```

**transactions.txt**
```
[22-05-2026 10:15 AM] Account 5001 Deposited ₹10000.0
```

---

## OOP Concepts Used

- **Inheritance** → `Customer extends Person`
- **Abstraction** → `Person` is abstract with `displayDetails()` as abstract method
- **Polymorphism** → `displayDetails()` called via loop on `Customer` objects
- **Method Overriding** → `Customer.displayDetails()` overrides `Person.displayDetails()`
- **Constructor Overloading** → `Customer` has 2 constructors
- **Encapsulation** → All fields `private`, accessed via getters/setters

## Collections Used

| Collection | Usage |
|-----------|-------|
| `HashMap<Integer, Customer>` | Fast customer lookup by ID |
| `ArrayList<Loan>` | Dynamic list of all loans |
| `LinkedList<String>` | Ordered transaction history |
| `TreeMap<Integer, Double>` | Sorted loan amounts by customer ID |

## Exception Handling

- `InvalidAmountException` — negative/zero deposit or withdrawal
- `InsufficientBalanceException` — withdrawal exceeds balance
- `InvalidLoanException` — loan request exceeds salary-based limit
- `FileNotFoundException` — missing data file (auto-creates empty file)
- `NumberFormatException` — non-numeric user input
- `ArithmeticException` — math errors in calculations
- All use `try-catch-finally` with `throw`/`throws`
