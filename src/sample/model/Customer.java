package sample.model;

/** this class creates a Customer object
 *
 */
public class Customer {
    public int id;
    public String name;
    public String address;
    public String postalCode;
    public String phone;
    public int divID;
    public int countryID;

    /** this method creates a Customer object with the given parameters.
     *
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param divID
     * @param countryID
     */
    public Customer(int id, String name, String address, String postalCode, String phone, int divID, int countryID){
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divID = divID;
        this.countryID = countryID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
