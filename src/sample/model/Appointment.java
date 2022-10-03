package sample.model;

import java.time.LocalDate;
import java.time.LocalTime;

/** this class creates an Appointment object.
 *
 */
public class Appointment {
    public int apptID;
    public String title;
    public String description;
    public String location;
    public String type;
    public LocalDate date;
    public int custID;
    public int userID;
    public LocalTime startTime;
    public LocalTime endTime;
    public Contact apptContact;

    /** this method creates a new Appointment object with given parameters.
     *
     * @param apptID
     * @param title
     * @param description
     * @param location
     * @param type
     * @param date
     * @param custID
     * @param userID
     * @param startTime
     * @param endTime
     * @param apptContact
     */
    public Appointment(int apptID, String title, String description, String location, String type,
                       LocalDate date, int custID, int userID, LocalTime startTime, LocalTime endTime, Contact apptContact) {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.apptContact = apptContact;
        this.date = date;
        this.custID = custID;
        this.userID = userID;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Contact getContact(){
        return apptContact;
    }

    public int getApptID(){
        return apptID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCustID() {
        return custID;
    }

    public int getUserID() {
        return userID;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
    @Override
    public String toString(){
        return type;
    }
}
