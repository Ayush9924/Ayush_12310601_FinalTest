// Customer is a Person — has extra bank-related info on top of basic person details
public class Customer extends Person {

    // static counter to keep track of total customers added during session
    public static int totalCustomersAdded = 0;

    // all private, using getters and setters below
    private int accountNumber;
    private double balance;
    private boolean loanTaken;
    private double salary;

    // constructor 1 — when you have all the info upfront
    public Customer(int personId, String name, String phoneNumber,
                    int accountNumber, double balance, double salary) {
        super(personId, name, phoneNumber);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.salary = salary;
        this.loanTaken = false; // by default, no loan taken
        totalCustomersAdded++;
    }

    // constructor 2 — when you also know the loan status (loading from file mostly)
    public Customer(int personId, String name, String phoneNumber,
                    int accountNumber, double balance, double salary, boolean loanTaken) {
        super(personId, name, phoneNumber);
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.salary = salary;
        this.loanTaken = loanTaken;
        totalCustomersAdded++;
    }

    // prints a nicely formatted customer card
    @Override
    public void displayDetails() {
        System.out.println("-----------------------------------------");
        System.out.println("Customer ID : " + getPersonId());
        System.out.println("Name        : " + getName());
        System.out.println("Phone       : " + getPhoneNumber());
        System.out.println("Account No  : " + accountNumber);
        System.out.printf("Balance     : \u20B9%.2f%n", balance);
        System.out.printf("Salary      : \u20B9%.2f%n", salary);
        System.out.println("Loan Taken  : " + loanTaken);
    }

    // --- getters ---
    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean isLoanTaken() {
        return loanTaken;
    }

    public double getSalary() {
        return salary;
    }

    // --- setters ---
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setLoanTaken(boolean loanTaken) {
        this.loanTaken = loanTaken;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
