import java.io.*;
import java.util.*;

// handles all reading and writing to/from the txt files
// if a file doesn't exist, we create it and move on — no crash
public class FileHandler {

    // file paths — keeping them here so they're easy to change
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String LOANS_FILE     = "loans.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    // reads customers.txt and builds the HashMap
    // format: personId,name,phone,accountNumber,balance,salary,loanTaken
    public HashMap<Integer, Customer> loadCustomers() {
        HashMap<Integer, Customer> customerMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank lines

                String[] parts = line.split(",");
                if (parts.length < 7) continue; // skip malformed lines

                int pid          = Integer.parseInt(parts[0].trim());
                String name      = parts[1].trim();
                String phone     = parts[2].trim();
                int accNum       = Integer.parseInt(parts[3].trim());
                double balance   = Double.parseDouble(parts[4].trim());
                double salary    = Double.parseDouble(parts[5].trim());
                boolean loanTaken = Boolean.parseBoolean(parts[6].trim());

                Customer myCustomer = new Customer(pid, name, phone, accNum, balance, salary, loanTaken);
                customerMap.put(pid, myCustomer);
            }
        } catch (FileNotFoundException e) {
            // file doesn't exist yet — totally fine, create it
            System.out.println("ERROR: " + CUSTOMERS_FILE + " file not found.");
            System.out.println("Creating new file...");
            createEmptyFile(CUSTOMERS_FILE);
        } catch (IOException e) {
            System.out.println("Something went wrong reading customers: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Skipped a bad line in customers.txt — bad format.");
        }

        return customerMap;
    }

    // reads loans.txt and builds the ArrayList
    // format: loanId,customerId,loanType,principalAmount,rateOfInterest,timeInYears
    public ArrayList<Loan> loadLoans() {
        ArrayList<Loan> loanList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(LOANS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String loanId     = parts[0].trim();
                int custId        = Integer.parseInt(parts[1].trim());
                String loanType   = parts[2].trim();
                double principal  = Double.parseDouble(parts[3].trim());
                double rate       = Double.parseDouble(parts[4].trim());
                int time          = Integer.parseInt(parts[5].trim());

                Loan myLoan = new Loan(loanId, custId, loanType, principal, rate, time);
                loanList.add(myLoan);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: " + LOANS_FILE + " file not found.");
            System.out.println("Creating new file...");
            createEmptyFile(LOANS_FILE);
        } catch (IOException e) {
            System.out.println("Something went wrong reading loans: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Skipped a bad line in loans.txt — bad format.");
        }

        return loanList;
    }

    // reads transactions.txt and loads into a LinkedList — preserves order
    public LinkedList<String> loadTransactions() {
        LinkedList<String> txHistory = new LinkedList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    txHistory.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: " + TRANSACTIONS_FILE + " file not found.");
            System.out.println("Creating new file...");
            createEmptyFile(TRANSACTIONS_FILE);
        } catch (IOException e) {
            System.out.println("Something went wrong reading transactions: " + e.getMessage());
        }

        return txHistory;
    }

    // writes all customers back to the file — overwrites everything
    // format: personId,name,phone,accountNumber,balance,salary,loanTaken
    public void saveCustomers(HashMap<Integer, Customer> allCustomers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE, false))) {
            for (Customer c : allCustomers.values()) {
                String record = c.getPersonId() + "," +
                                c.getName() + "," +
                                c.getPhoneNumber() + "," +
                                c.getAccountNumber() + "," +
                                c.getBalance() + "," +
                                c.getSalary() + "," +
                                c.isLoanTaken();
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Couldn't save customers: " + e.getMessage());
        }
    }

    // saves all loans to the file — overwrites
    public void saveLoans(ArrayList<Loan> loanList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOANS_FILE, false))) {
            for (Loan loan : loanList) {
                String record = loan.getLoanId() + "," +
                                loan.getCustomerId() + "," +
                                loan.getLoanType() + "," +
                                loan.getPrincipalAmount() + "," +
                                loan.getRateOfInterest() + "," +
                                loan.getTimeInYears();
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Couldn't save loans: " + e.getMessage());
        }
    }

    // saves all transactions to file — overwrites (we already have full list in memory)
    public void saveTransactions(LinkedList<String> txHistory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, false))) {
            for (String entry : txHistory) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Couldn't save transactions: " + e.getMessage());
        }
    }

    // appends a single transaction line live during execution so we don't lose it
    public void appendTransaction(String logEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Couldn't append transaction: " + e.getMessage());
        }
    }

    // utility — just creates a blank file so we don't keep getting "not found"
    private void createEmptyFile(String fileName) {
        try {
            File newFile = new File(fileName);
            newFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Couldn't create " + fileName + ": " + e.getMessage());
        }
    }
}
