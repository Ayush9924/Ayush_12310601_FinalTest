// thrown when someone tries to withdraw more than they actually have
public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException() {
        super("Cannot withdraw amount greater than balance.");
    }

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
