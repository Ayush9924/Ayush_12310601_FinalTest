import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    // the bank's name — static so it's shared everywhere
    static final String BANK_NAME = "FinNova Bank";

    // our main data stores for the session
    static HashMap<Integer, Customer>  allCustomers = new HashMap<>();
    static ArrayList<Loan>             loanList     = new ArrayList<>();
    static LinkedList<String>          txHistory    = new LinkedList<>();
    static TreeMap<Integer, Double>    loanReport   = new TreeMap<>();  // customerId -> loanAmount (for sorted reports)

    static FileHandler fileHandler = new FileHandler();
    static Scanner     scanner     = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcomeBanner();
        loadAllData();

        boolean running = true;
        while (running) {
            printMainMenu();
            String choiceInput = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(choiceInput);

                switch (choice) {
                    case 1  -> addCustomer();
                    case 2  -> viewCustomers();
                    case 3  -> createLoan();
                    case 4  -> calculateSimpleInterest();
                    case 5  -> calculateCompoundInterest();
                    case 6  -> depositMoney();
                    case 7  -> withdrawMoney();
                    case 8  -> viewTransactions();
                    case 9  -> searchCustomer();
                    case 10 -> generateReports();
                    case 11 -> {
                        exitAndSave();
                        running = false;
                    }
                    default -> System.out.println("Invalid Choice. Please Enter Between 1 - 11");
                }

            } catch (NumberFormatException e) {
                // user typed something that isn't a number
                System.out.println("Invalid Choice. Please Enter Between 1 - 11");
            }
        }

        scanner.close();
    }

    // --- startup ---

    private static void printWelcomeBanner() {
        System.out.println("=========================================");
        System.out.println("     WELCOME TO FINNOVA BANK");
        System.out.println("=========================================");
    }

    private static void loadAllData() {
        System.out.println("\nLoading customer records...");
        allCustomers = fileHandler.loadCustomers();

        System.out.println("Loading loan records...");
        loanList = fileHandler.loadLoans();

        System.out.println("Loading transactions...");
        txHistory = fileHandler.loadTransactions();

        // rebuild loanReport TreeMap from loaded loans
        for (Loan loan : loanList) {
            loanReport.put(loan.getCustomerId(), loan.getPrincipalAmount());
        }

        System.out.println("Data Loaded Successfully.\n");
    }

    // --- menu ---

    private static void printMainMenu() {
        System.out.println("=========================================");
        System.out.println("           MAIN MENU");
        System.out.println("=========================================");
        System.out.println("1.  Add Customer");
        System.out.println("2.  View Customers");
        System.out.println("3.  Create Loan");
        System.out.println("4.  Calculate Simple Interest");
        System.out.println("5.  Calculate Compound Interest");
        System.out.println("6.  Deposit Money");
        System.out.println("7.  Withdraw Money");
        System.out.println("8.  View Transactions");
        System.out.println("9.  Search Customer");
        System.out.println("10. Generate Reports");
        System.out.println("11. Exit");
        System.out.print("\nEnter your choice: ");
    }

    // --- 1. Add Customer ---

    private static void addCustomer() {
        System.out.println("\n--- Add New Customer ---");
        try {
            System.out.print("Enter Customer ID: ");
            int custId = Integer.parseInt(scanner.nextLine().trim());

            // okay let's check if this customer already exists before adding a duplicate
            if (allCustomers.containsKey(custId)) {
                System.out.println("Customer with ID " + custId + " already exists.");
                return;
            }

            System.out.print("Enter Name: ");
            String custName = scanner.nextLine().trim();

            System.out.print("Enter Phone Number: ");
            String custPhone = scanner.nextLine().trim();

            System.out.print("Enter Account Number: ");
            int accNum = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Initial Balance: ");
            double initBalance = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Monthly Salary: ");
            double userSalary = Double.parseDouble(scanner.nextLine().trim());

            Customer newCustomer = new Customer(custId, custName, custPhone, accNum, initBalance, userSalary);
            allCustomers.put(custId, newCustomer);

            // save to file right away
            fileHandler.saveCustomers(allCustomers);

            System.out.println("Customer Added Successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // --- 2. View Customers ---

    private static void viewCustomers() {
        if (allCustomers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        System.out.println("\n=========================================");
        System.out.println("          CUSTOMER DETAILS");
        System.out.println("=========================================");

        // polymorphism in action — calling displayDetails() through the map
        for (Customer eachCustomer : allCustomers.values()) {
            eachCustomer.displayDetails();
        }
        System.out.println("-----------------------------------------");
    }

    // --- 3. Create Loan ---

    private static void createLoan() {
        System.out.println("\n--- Create Loan ---");
        try {
            System.out.print("Enter Customer ID: ");
            int custId = Integer.parseInt(scanner.nextLine().trim());

            // okay let's check if this customer actually exists before going further
            Customer foundCustomer = allCustomers.get(custId);
            if (foundCustomer == null) {
                System.out.println("ERROR: Customer Not Found");
                return;
            }

            System.out.print("Enter Loan ID: ");
            String loanId = scanner.nextLine().trim();

            System.out.print("Enter Loan Type (e.g. Home Loan, Car Loan): ");
            String loanType = scanner.nextLine().trim();

            System.out.print("Enter Principal Amount: ");
            double principalAmount = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Rate Of Interest: ");
            double rateOfInterest = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Time (Years): ");
            int timeInYears = Integer.parseInt(scanner.nextLine().trim());

            // salary decides how much loan they can get, simple logic
            double userSalary = foundCustomer.getSalary();
            double maxAllowed;

            if (userSalary < 25000) {
                maxAllowed = 200000;
            } else if (userSalary <= 50000) {
                maxAllowed = 500000;
            } else {
                maxAllowed = 1000000;
            }

            System.out.printf("Loan Eligibility Check%n");
            System.out.printf("Salary = \u20B9%.2f%n", userSalary);
            System.out.printf("Eligible Loan = \u20B9%.2f%n", maxAllowed);

            // if they're asking for more than allowed, reject it
            if (principalAmount > maxAllowed) {
                throw new InvalidLoanException(
                    "Requested \u20B9" + principalAmount + " but maximum allowed is \u20B9" + maxAllowed
                );
            }

            System.out.println("Loan Approved.");

            Loan newLoan = new Loan(loanId, custId, loanType, principalAmount, rateOfInterest, timeInYears);
            loanList.add(newLoan);

            // mark the customer so we know they have a loan now
            foundCustomer.setLoanTaken(true);

            // update loanReport map for the report feature
            loanReport.put(custId, principalAmount);

            // log this transaction live
            String timestamp = getTimestamp();
            String txLog = "[" + timestamp + "] Loan Created For Customer " + custId +
                           " Loan Amount = \u20B9" + principalAmount;
            txHistory.add(txLog);
            fileHandler.appendTransaction(txLog);

            // save updated files
            fileHandler.saveLoans(loanList);
            fileHandler.saveCustomers(allCustomers);

            System.out.println("Loan Created Successfully.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (InvalidLoanException e) {
            System.out.println("ERROR: Loan Rejected — " + e.getMessage());
        }
    }

    // --- 4. Calculate Simple Interest ---

    private static void calculateSimpleInterest() {
        System.out.println("\n--- Calculate Simple Interest ---");
        try {
            System.out.print("Enter Principal Amount: ");
            double principal = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Rate Of Interest (%): ");
            double rate = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Time (Years): ");
            int time = Integer.parseInt(scanner.nextLine().trim());

            // create a temporary loan object just to use the formula methods
            Loan tempLoan = new Loan("TEMP", 0, "General", principal, rate, time);
            double si = tempLoan.calculateSimpleInterest();

            System.out.printf("Simple Interest = \u20B9%.2f%n", si);
            System.out.printf("Total Amount    = \u20B9%.2f%n", principal + si);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (ArithmeticException e) {
            System.out.println("Math error during SI calculation: " + e.getMessage());
        }
    }

    // --- 5. Calculate Compound Interest ---

    private static void calculateCompoundInterest() {
        System.out.println("\n--- Calculate Compound Interest ---");
        try {
            System.out.print("Enter Principal Amount: ");
            double principal = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Rate Of Interest (%): ");
            double rate = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter Time (Years): ");
            int time = Integer.parseInt(scanner.nextLine().trim());

            Loan tempLoan = new Loan("TEMP", 0, "General", principal, rate, time);
            double ci = tempLoan.calculateCompoundInterest();

            System.out.printf("Compound Interest = \u20B9%.2f%n", ci);
            System.out.printf("Total Amount      = \u20B9%.2f%n", principal + ci);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (ArithmeticException e) {
            System.out.println("Math error during CI calculation: " + e.getMessage());
        }
    }

    // --- 6. Deposit Money ---

    private static void depositMoney() {
        System.out.println("\n--- Deposit Money ---");
        try {
            System.out.print("Enter Account Number: ");
            int accNum = Integer.parseInt(scanner.nextLine().trim());

            // find the customer by account number — gotta scan through the map
            Customer targetCustomer = findByAccountNumber(accNum);
            if (targetCustomer == null) {
                System.out.println("ERROR: Customer Not Found for account " + accNum);
                return;
            }

            System.out.print("Enter Deposit Amount: ");
            double depositAmt = Double.parseDouble(scanner.nextLine().trim());

            // use BankAccount to handle the operation with proper validation
            BankAccount bankAcc = new BankAccount(accNum, targetCustomer.getBalance());
            bankAcc.deposit(depositAmt);

            // sync balance back to the customer object
            targetCustomer.setBalance(bankAcc.getBalance());

            // log the transaction live
            String timestamp = getTimestamp();
            String txLog = "[" + timestamp + "] Account " + accNum + " Deposited \u20B9" + depositAmt;
            txHistory.add(txLog);
            fileHandler.appendTransaction(txLog);

            // update the file with new balance
            fileHandler.saveCustomers(allCustomers);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: InvalidAmountException — " + e.getMessage());
        } finally {
            // finally block always runs — could log or clean up here
        }
    }

    // --- 7. Withdraw Money ---

    private static void withdrawMoney() {
        System.out.println("\n--- Withdraw Money ---");
        try {
            System.out.print("Enter Account Number: ");
            int accNum = Integer.parseInt(scanner.nextLine().trim());

            Customer targetCustomer = findByAccountNumber(accNum);
            if (targetCustomer == null) {
                System.out.println("ERROR: Customer Not Found for account " + accNum);
                return;
            }

            System.out.print("Enter Withdraw Amount: ");
            double withdrawAmt = Double.parseDouble(scanner.nextLine().trim());

            BankAccount bankAcc = new BankAccount(accNum, targetCustomer.getBalance());
            bankAcc.withdraw(withdrawAmt);

            // sync the updated balance back
            targetCustomer.setBalance(bankAcc.getBalance());

            String timestamp = getTimestamp();
            String txLog = "[" + timestamp + "] Account " + accNum + " Withdrawn \u20B9" + withdrawAmt;
            txHistory.add(txLog);
            fileHandler.appendTransaction(txLog);

            fileHandler.saveCustomers(allCustomers);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (InvalidAmountException e) {
            System.out.println("ERROR: InvalidAmountException — " + e.getMessage());
        } catch (InsufficientBalanceException e) {
            System.out.println("ERROR: Insufficient Balance Exception");
            System.out.println(e.getMessage());
        } finally {
            // finally block — could add cleanup or auditing logic here if needed
        }
    }

    // --- 8. View Transactions ---

    private static void viewTransactions() {
        System.out.println("\n=========================================");
        System.out.println("        TRANSACTION HISTORY");
        System.out.println("=========================================");

        if (txHistory.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (String entry : txHistory) {
            System.out.println(entry);
        }
    }

    // --- 9. Search Customer ---

    private static void searchCustomer() {
        System.out.println("\n--- Search Customer ---");
        try {
            System.out.print("Enter Customer ID: ");
            int searchId = Integer.parseInt(scanner.nextLine().trim());

            Customer foundCustomer = allCustomers.get(searchId);

            // let the user know clearly if not found
            if (foundCustomer == null) {
                System.out.println("ERROR: Customer Not Found");
                return;
            }

            System.out.println("Customer Found");
            System.out.println("-----------------------------------------");
            System.out.println("Name       : " + foundCustomer.getName());
            System.out.println("Account No : " + foundCustomer.getAccountNumber());
            System.out.printf("Balance    : \u20B9%.2f%n", foundCustomer.getBalance());
            System.out.printf("Salary     : \u20B9%.2f%n", foundCustomer.getSalary());
            System.out.println("-----------------------------------------");

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // --- 10. Generate Reports ---

    private static void generateReports() {
        System.out.println("\n=========================================");
        System.out.println("           BANK REPORT");
        System.out.println("=========================================");

        // total customers — just the size of the map
        int totalCustomers = allCustomers.size();
        System.out.println("Total Customers           : " + totalCustomers);

        // sum up all loan principals
        double totalLoansIssued = 0;
        double highestLoanAmt   = 0;
        double totalInterest    = 0;
        String customerWithHighestLoan = "N/A";

        for (Loan loan : loanList) {
            totalLoansIssued += loan.getPrincipalAmount();
            totalInterest    += loan.calculateSimpleInterest(); // interest bank earns

            if (loan.getPrincipalAmount() > highestLoanAmt) {
                highestLoanAmt = loan.getPrincipalAmount();
                // figure out who this loan belongs to
                Customer loanOwner = allCustomers.get(loan.getCustomerId());
                if (loanOwner != null) {
                    customerWithHighestLoan = loanOwner.getName();
                }
            }
        }

        // total balance across all customer accounts
        double totalBankBalance = 0;
        for (Customer c : allCustomers.values()) {
            totalBankBalance += c.getBalance();
        }

        System.out.printf("Total Loans Issued        : \u20B9%.2f%n", totalLoansIssued);
        System.out.printf("Highest Loan Amount       : \u20B9%.2f%n", highestLoanAmt);
        System.out.printf("Total Bank Balance        : \u20B9%.2f%n", totalBankBalance);
        System.out.printf("Interest Generated        : \u20B9%.2f%n", totalInterest);
        System.out.println("Customer With Highest Loan: " + customerWithHighestLoan);
        System.out.println("=========================================");
    }

    // --- 11. Exit ---

    private static void exitAndSave() {
        System.out.println("\nSaving customer data...");
        fileHandler.saveCustomers(allCustomers);

        System.out.println("Saving loan data...");
        fileHandler.saveLoans(loanList);

        System.out.println("Saving transaction history...");
        fileHandler.saveTransactions(txHistory);

        System.out.println("\nData Saved Successfully.");
        System.out.println("Thank You For Using " + BANK_NAME);
    }

    // --- helper methods ---

    // find customer by account number — HashMap is keyed by customerId so we loop
    private static Customer findByAccountNumber(int accNum) {
        for (Customer c : allCustomers.values()) {
            if (c.getAccountNumber() == accNum) {
                return c;
            }
        }
        return null; // not found
    }

    // formats current date/time nicely for transaction logs
    private static String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        return now.format(formatter);
    }
}
