package sample.dbaccess;

import sample.JDBC;
import sample.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/** this method collects all countries from the database
 *
 * @param <countryList>
 */
public class DBCountry<countryList> {
    /** this method puts all country information from the database into a country object.
     *
     * @return
     */
    public static ObservableList<Country> getAllCountries() {
        //create arraylist of countries from database
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //retrieve country id and name
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country c = new Country(countryID, countryName);
                countryList.add(c);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return countryList;
    }

}
