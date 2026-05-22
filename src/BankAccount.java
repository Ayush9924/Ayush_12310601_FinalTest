// handles deposit and withdraw operations on a customer's account
// balance is managed here with proper validation
public class BankAccount {

    private double balance;
    private int accountNumber;

    public BankAccount(int accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // deposit — can't deposit zero or negative, that makes no sense
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount cannot be negative.");
        }
        balance += amount;
        System.out.printf("\u20B9%.2f Deposited Successfully.%n", amount);
        System.out.printf("Updated Balance = \u20B9%.2f%n", balance);
    }

    // withdraw — check both: is amount valid, and does the account have enough
    public void withdraw(double amount) throws InvalidAmountException, InsufficientBalanceException {
        // can't let them enter a negative number
        if (amount <= 0) {
            throw new InvalidAmountException("Amount cannot be negative.");
        }
        // can't let them withdraw more than they have, that's basic
        if (amount > balance) {
            throw new InsufficientBalanceException("Cannot withdraw amount greater than balance.");
        }
        balance -= amount;
        System.out.printf("\u20B9%.2f Withdrawn Successfully.%n", amount);
        System.out.printf("Remaining Balance = \u20B9%.2f%n", balance);
    }

    // just prints the balance
    public void checkBalance() {
        System.out.printf("Current Balance = \u20B9%.2f%n", balance);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}
