package sample.dbaccess;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.JDBC;
import sample.model.Appointment;
import sample.model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

/** this class collects the appointments within the database.
 *
 * @param <appointmentList>
 */
public class DBAppointment<appointmentList> {

    /** this method puts appointments from the database into an appointment object.
     * there are 15 different timeframes (param) that are used to filter which appointments are pulled.
     * @param timeframe
     * @return
     */
    public static ObservableList<Appointment> getAllAppointments(int timeframe) {

        //create arraylist of countries from database
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT appointments.*, Contact_Name FROM appointments, contacts WHERE " +
                    "contacts.Contact_ID = appointments.Contact_ID";
            //timeframe int values
            // 1=all, 2=week, 3 = month, 4 = jan, 5 = feb......
            if (timeframe == 2) {
                sql += " AND yearweek(start) = yearweek(now())";
            }
            if (timeframe == 3) {
                sql += " AND month(start) = month(now())";
            }
            if(timeframe == 4){
                sql += " AND month(start) = 1";
            }
            if(timeframe == 5) {
                sql += " AND month(start) = 2";
            }
            if(timeframe == 6) {
                sql += " AND month(start) = 3";
            }
            if(timeframe == 7) {
                sql += " AND month(start) = 4";
            }
            if(timeframe == 8) {
                sql += " AND month(start) = 5";
            }
            if(timeframe == 9) {
                sql += " AND month(start) = 6";
            }
            if(timeframe == 10) {
                sql += " AND month(start) = 7";
            }
            if(timeframe == 11) {
                sql += " AND month(start) = 8";
            }
            if(timeframe == 12) {
                sql += " AND month(start) = 9";
            }
            if(timeframe == 13) {
                sql += " AND month(start) = 10";
            }
            if(timeframe == 14) {
                sql += " AND month(start) = 11";
            }
            if(timeframe == 15) {
                sql += " AND month(start) = 12";
            }

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                //if within timeframe!!
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                String appointmentContact = rs.getString("Contact_Name");
                //separate data and time and save date, start time and end time
                LocalDateTime appointmentStartDateTime = rs.getTimestamp("Start").toLocalDateTime();

                LocalDate appointmentDate = appointmentStartDateTime.toLocalDate();
                LocalTime appointmentStartTime = appointmentStartDateTime.toLocalTime();
                LocalTime appointmentEndTime = rs.getTimestamp("End").toLocalDateTime().toLocalTime();
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");
                Contact c = new Contact(contactID, appointmentContact);

                Appointment a = new Appointment(appointmentID, appointmentTitle, appointmentDescription,
                        appointmentLocation, appointmentType, appointmentDate, custID, userID,
                        appointmentStartTime, appointmentEndTime, c);
                appointmentList.add(a);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    /** this method checks if the user has any appointments within the next 15 minutes.
     *
     * @param userID
     * @return Appointment
     */
    public static Appointment getUpcomingAppt(int userID){
        for (Appointment A: getAllAppointments(1)){
            if (A.getUserID() == userID)
            {
                //15 minute check!!!
                if(A.getStartTime().isBefore(LocalTime.now().plus(15, ChronoUnit.MINUTES))
                        && (A.getStartTime().isAfter(LocalTime.now()) || A.getStartTime().equals(LocalTime.now()))) {
                    return A;
            }
            }
        }
        return null;
    }

    /** this method creates appointment types
     *
     * @return
     */
    public static ObservableList<String> getAllTypes() {
    ObservableList<String> apptTypes = FXCollections.observableArrayList("Planning Session", "De-Briefing", "Consultation");
      //  apptTypes.add("Planning Session");
        //apptTypes.add("De-Briefing");
        //apptTypes.add("Consultation");
        return apptTypes;
}

    /** this method deletes an appointment.
     *
     * @param apptID
     * @throws SQLException
     */
    public static void deleteAppointment(int apptID) throws SQLException {

        //delete appointments first
        String sqlDelAppointment = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement psApt = JDBC.getConnection().prepareStatement(sqlDelAppointment);
        psApt.setInt(1, apptID);
        psApt.execute();

    }

    /** this method checks for conflicting appointments.
     *  appointments need to be within office hours and may not overlap.
     * @param apptID
     * @param apptDate
     * @param apptStart
     * @param apptEnd
     * @return
     * @throws SQLException
     */
    public static int schedulingConflict(String apptID, LocalDate apptDate, LocalTime apptStart, LocalTime apptEnd) throws SQLException {
        /*error codes
        0 = no conflict
        1 = outside business hours (8AM-10PM EST ALL DAYS)
        2 = conflicting appt
         */
        int errorCode = 0;

        LocalTime officeHoursBegin = LocalTime.parse("08:00");
        //step 1 make localdatetime
        LocalDateTime ldt = LocalDateTime.of(apptDate, officeHoursBegin);
        //2 zoneddatetime in ET
        ZonedDateTime zstartET = ldt.atZone(ZoneId.of("America/New_York"));
        //3 convert to zoneddatetime in localtime
        ZonedDateTime zlocal = zstartET.withZoneSameInstant(ZoneId.systemDefault());
        //4 gt local time
        officeHoursBegin = zlocal.toLocalTime();


        LocalTime officeHoursEnd = LocalTime.parse("22:00");
        //step 1 make localdatetime
        LocalDateTime ldt2 = LocalDateTime.of(apptDate, officeHoursEnd);
        //2 zoneddatetime in ET
        ZonedDateTime zendET = ldt2.atZone(ZoneId.of("America/New_York"));
        //3 convert to zoneddatetime in localtime
        ZonedDateTime zlocal2 = zendET.withZoneSameInstant(ZoneId.systemDefault());
        //4 gt local time
        officeHoursEnd = zlocal2.toLocalTime();


        if (apptStart.isBefore(officeHoursBegin) || apptEnd.isAfter(officeHoursEnd)) {
            errorCode = 1;
            return errorCode;
        }
        ObservableList<Appointment> allList = getAllAppointments(1);
        int apptIdInt = Integer.parseInt(apptID);
        for (Appointment a : allList) {
            int appointmentID = a.getApptID();
            //separate data and time and save date, start time and end time
            LocalDate appointmentDate = a.getDate();
            LocalTime appointmentStartTime = a.getStartTime();
            LocalTime appointmentEndTime = a.getEndTime();

            // 5 cases:
            // propose start is > appt start & propose start is < appt end
            // propose end is > appt start & propose end is < appt end
            // ! proposed start is < appt start & proposed end is > appt end
            // ! proposed start = appt start
            // ! proposed end = appt end
            if (appointmentDate.isEqual(apptDate) && apptIdInt != appointmentID) {
                if (
                        (apptStart.isAfter(appointmentStartTime) && apptStart.isBefore(appointmentEndTime)) ||
                        (apptEnd.isAfter(appointmentStartTime) && apptEnd.isBefore(appointmentEndTime)) ||
                                (apptStart.isBefore(appointmentStartTime) && apptEnd.isAfter(appointmentEndTime)) ||
                                (apptStart.equals(appointmentStartTime)) || (apptEnd.equals(appointmentEndTime))
                ) {
                    errorCode = 2;
                    return errorCode;
                }
            }

        }

        return errorCode;
    }

    /** this method creates a new appointment in the database.
     *
     * @param apptTitle
     * @param apptDesc
     * @param apptLoc
     * @param apptType
     * @param apptDate
     * @param custID
     * @param userID
     * @param apptStart
     * @param apptEnd
     * @param contactID
     * @throws SQLException
     */
    public static void createNewAppointment( String apptTitle, String apptDesc,
                          String apptLoc, String apptType, LocalDate apptDate, int custID, int userID,
                          LocalTime apptStart, LocalTime apptEnd, int contactID) throws SQLException {

        Timestamp start = Timestamp.valueOf(LocalDateTime.of(apptDate, apptStart));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(apptDate, apptEnd));
        String sqlDelAppointment = "INSERT INTO appointments (Title, Description, Location, Start, End, Type, " +
                "Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psApt = JDBC.getConnection().prepareStatement(sqlDelAppointment);
        psApt.setString(1, apptTitle);
        psApt.setString(2, apptDesc);
        psApt.setString(3, apptLoc);
        psApt.setTimestamp(4, start);
        psApt.setTimestamp(5, end);
        psApt.setString( 6, apptType);
        psApt.setInt (7, custID);
        psApt.setInt (8, userID);
        psApt.setInt (9, contactID);
        psApt.execute();

    }

    /** this method updates an exsiting appointment in the database.
     *
     * @param apptID
     * @param apptTitle
     * @param apptDesc
     * @param apptLoc
     * @param apptType
     * @param apptDate
     * @param custID
     * @param userID
     * @param apptStart
     * @param apptEnd
     * @param contactID
     * @throws SQLException
     */
    public static void updateAppointment( int apptID, String apptTitle, String apptDesc,
                                            String apptLoc, String apptType, LocalDate apptDate, int custID, int userID,
                                            LocalTime apptStart, LocalTime apptEnd, int contactID) throws SQLException {

        Timestamp start = Timestamp.valueOf(LocalDateTime.of(apptDate, apptStart));
        Timestamp end = Timestamp.valueOf(LocalDateTime.of(apptDate, apptEnd));
        String sqlDelAppointment = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Start = ?, " +
        "End = ?, Type = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement psApt = JDBC.getConnection().prepareStatement(sqlDelAppointment);
        psApt.setString(1, apptTitle);
        psApt.setString(2, apptDesc);
        psApt.setString(3, apptLoc);
        psApt.setTimestamp(4, start);
        psApt.setTimestamp(5, end);
        psApt.setString( 6, apptType);
        psApt.setInt (7, custID);
        psApt.setInt (8, userID);
        psApt.setInt (9, contactID);
        psApt.setInt (10, apptID);
        psApt.execute();
    }


}
