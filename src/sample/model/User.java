package sample.model;

/** this class creates a User object.
 *
 */
public class User {
    public int userID;
    public String username;
    public String password;

    /** this method creates a User object with the given parameters.
     *
     * @param userID
     * @param username
     * @param password
     */
    public User(int userID, String username, String password){
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    @Override
    public String toString(){
        return String.valueOf(userID);
    }
}
