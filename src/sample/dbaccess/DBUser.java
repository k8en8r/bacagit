package sample.dbaccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.JDBC;
import sample.model.Contact;
import sample.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**this class collects user data from the database
 *
 */
public class DBUser {
    private static int currentUserID;

    /** this method creates user objects from user data within the database.
     *
     * @return
     */
    public static ObservableList<User> getAllUsers() {
        //create array list of divisions
        ObservableList<User> userList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //retrieve division id and division name
                int userID = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");
                // does pull data correctly System.out.println(username + password);
                User u = new User(userID, username, password);
                userList.add(u);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userList;
    }

    /** this method sets a static int of the user who has logged into the system.
     * (used later to see if they have any upcoming appointments)
     * @param ID
     */
    public static void setCurrentUserID(int ID){
        currentUserID = ID;
    }

    /** this method pulls the user ID of the user who logged into the system.
     *
     * @return
     */
    public static int getCurrentUserID(){
        return currentUserID;
    }
}
