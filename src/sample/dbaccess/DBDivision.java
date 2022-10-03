package sample.dbaccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.JDBC;
import sample.model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** this class collects divisions from the database.
 *
 */
public class DBDivision {

    /** this method puts all division data from the database into a division object, based on the country selected.
     *
     * @param countryID
     * @return
     */
    public static ObservableList<Division> getAllDivisions(int countryID) {
        //create array list of divisions
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, countryID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //retrieve division id and division name
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                Division d = new Division(divisionID, divisionName);
                divisionList.add(d);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return divisionList;
    }

}
