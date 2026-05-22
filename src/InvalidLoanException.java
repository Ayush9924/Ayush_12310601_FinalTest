// thrown when customer asks for a loan beyond what their salary allows
public class InvalidLoanException extends Exception {

    public InvalidLoanException() {
        super("Loan amount exceeds your eligible limit.");
    }

    public InvalidLoanException(String message) {
        super(message);
    }
}
