package sample.model;

/** this class creates a Contact object
 *
 */
public class Contact {
    public int contactID;
    public String contactName;

    /** this method creates a Contact object with the given parameters.
     *
     * @param contactID
     * @param contactName
     */
    public Contact(int contactID, String contactName){
        this.contactID = contactID;
        this.contactName = contactName;
    }

    public int getContactID(){
        return contactID;
    }
    @Override
    public String toString(){
        return contactName;
    }
}
