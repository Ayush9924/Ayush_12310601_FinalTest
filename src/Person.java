// the base class for any person in the system — customers inherit from here
public abstract class Person {

    // keeping these private for encapsulation, subclasses go through getters/setters
    private int personId;
    private String name;
    private String phoneNumber;

    // basic constructor
    public Person(int personId, String name, String phoneNumber) {
        this.personId = personId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // every subclass has to figure out how to display their own details
    public abstract void displayDetails();

    // --- getters ---
    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // --- setters ---
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
