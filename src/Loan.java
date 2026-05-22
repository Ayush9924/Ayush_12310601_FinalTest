// holds all loan info and handles interest calculations
public class Loan {

    private String loanId;
    private int customerId;   // which customer this loan belongs to
    private String loanType;
    private double principalAmount;
    private double rateOfInterest;
    private int timeInYears;

    public Loan(String loanId, int customerId, String loanType,
                double principalAmount, double rateOfInterest, int timeInYears) {
        this.loanId = loanId;
        this.customerId = customerId;
        this.loanType = loanType;
        this.principalAmount = principalAmount;
        this.rateOfInterest = rateOfInterest;
        this.timeInYears = timeInYears;
    }

    // simple interest — straightforward (P * R * T) / 100
    public double calculateSimpleInterest() {
        return (principalAmount * rateOfInterest * timeInYears) / 100.0;
    }

    // compound interest — P * (1 + R/100)^T - P
    public double calculateCompoundInterest() {
        double totalAmount = principalAmount * Math.pow((1 + rateOfInterest / 100.0), timeInYears);
        return totalAmount - principalAmount;
    }

    // --- getters ---
    public String getLoanId() {
        return loanId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getLoanType() {
        return loanType;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public double getRateOfInterest() {
        return rateOfInterest;
    }

    public int getTimeInYears() {
        return timeInYears;
    }

    // --- setters ---
    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setPrincipalAmount(double principalAmount) {
        this.principalAmount = principalAmount;
    }

    public void setRateOfInterest(double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public void setTimeInYears(int timeInYears) {
        this.timeInYears = timeInYears;
    }
}
