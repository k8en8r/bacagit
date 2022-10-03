package sample.dbaccess;

import sample.JDBC;
import sample.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Division;

import java.sql.*;

/** this class collects all customers from the database.
 *
 * @param <customerList>
 */
public class DBCustomer<customerList> {

    /** this method puts all customer information into a customer object.
     *
     * @return
     */
    public static ObservableList<Customer> getAllCustomers() {
        //create arraylist of customers from database
        ObservableList<Customer> cList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT customers.*,country_ID FROM customers,first_level_divisions where " +
                    "customers.division_ID = first_level_divisions.Division_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //retrieve customer info from database
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divID = rs.getInt("Division_ID");
                int countryID = rs.getInt("country_ID");
                Customer c = new Customer(customerID, customerName, address, postalCode, phone, divID, countryID);
                cList.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return cList;
    }

    /** this method deletes a customer from teh database.
     *
     * @param customerID
     */
    public static void deleteCustomer(int customerID) {
        try {
            //delete appointments first
            String sqlDelAppointment = "DELETE FROM appointments WHERE Customer_ID = ?";
            PreparedStatement psApt = JDBC.getConnection().prepareStatement(sqlDelAppointment);
            psApt.setInt(1, customerID);
            psApt.execute();
            //delete customer
            String sqlDelCustomer = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement psCust = JDBC.getConnection().prepareStatement(sqlDelCustomer);
            psCust.setInt(1, customerID);
            psCust.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    /** this method inserts a new customer into the database.
     *
     * @param custName
     * @param custAdd
     * @param custPost
     * @param custPhone
     * @param divID
     * @throws SQLException
     */
    public static void saveNewCustomer(String custName, String custAdd, String custPost, String custPhone, int divID) throws SQLException {

        String sqlAdd = "INSERT INTO customers (Customer_Name,Address,Postal_Code,Phone,Division_ID) VALUES (?,?,?,?,?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sqlAdd);
        ps.setString(1, custName);
        ps.setString(2, custAdd);
        ps.setString(3, custPost);
        ps.setString(4, custPhone);
        ps.setInt(5, divID);
        ps.execute();

    }

    /** this method updates an existing customer in the database.
     *
     * @param custName
     * @param custAdd
     * @param custPost
     * @param custPhone
     * @param custDiv
     * @param custID
     * @throws SQLException
     */
    public static void saveExistingCustomer(String custName, String custAdd, String custPost, String custPhone, int custDiv, int custID) throws SQLException {
        String sqlUpdate = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sqlUpdate);
        ps.setString(1, custName);
        ps.setString(2, custAdd);
        ps.setString(3, custPost);
        ps.setString(4, custPhone);
        ps.setInt(5, custDiv);
        ps.setInt(6, custID);
        ps.execute();
    }
}
