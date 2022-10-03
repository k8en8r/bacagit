package sample.dbaccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.JDBC;
import sample.model.Contact;
import sample.model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**this class collects the contacts within the database.
 *
 */
public class DBContact {

    /**this method puts all contacts from the database into a contact object.
     *
     * @return
     */
    public static ObservableList<Contact> getAllContacts() {
        //create array list of divisions
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //retrieve division id and division name
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                Contact c = new Contact(contactID, contactName);
                contactList.add(c);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactList;
    }
}
