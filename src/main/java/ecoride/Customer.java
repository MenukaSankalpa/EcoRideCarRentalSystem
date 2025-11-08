package ecoride;

/**
 * Domain entity representing a Customer.
 */
public class Customer {
    private final String idDocument; // NIC or Passport
    private String name;
    private String contactNumber;
    private String email;

    public Customer(String idDocument, String name, String contactNumber, String email) {
        this.idDocument = idDocument;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public String getIdDocument() { return idDocument; }
    public String getName() { return name; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, idDocument, contactNumber);
    }
}
