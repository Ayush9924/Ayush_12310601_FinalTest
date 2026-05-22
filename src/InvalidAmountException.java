// custom exception — thrown when someone tries to deposit/withdraw 0 or negative
public class InvalidAmountException extends Exception {

    public InvalidAmountException() {
        super("Amount cannot be negative.");
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
