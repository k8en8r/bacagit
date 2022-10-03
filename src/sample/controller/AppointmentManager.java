package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Platform;
import sample.dbaccess.*;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;
import sample.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;

/** This class creates a scene where you can manage appointments.
You can add, update, and delete appointments as needed.
 */
public class AppointmentManager implements Initializable {


    public Label apptAlert;
    public Button cManager;
    public Button reportsButton;
    public Button exitButton;
    public Pane productsTable;
    public TableView <Appointment> allAppts;
    public TableColumn allApptIDs;
    public TableColumn allApptTitles;
    public TableColumn allApptDesc;
    public TableColumn allApptLocations;
    public TableColumn allApptContacts;
    public TableColumn allApptTypes;
    public TableColumn allApptDates;
    public TableColumn allApptStarts;
    public TableColumn allApptEnds;
    public TableColumn allApptCustIDs;
    public TableView <Appointment> weekAppts;
    public TableColumn weekApptIDs;
    public TableColumn weekApptTitles;
    public TableColumn weekApptDesc;
    public TableColumn weekApptContacts;
    public TableColumn weekApptLocations;
    public TableColumn weekApptTypes;
    public TableColumn weekApptDates;
    public TableColumn weekApptStarts;
    public TableColumn weekApptEnds;
    public TableColumn weekApptCustIDs;
    public TableColumn allUserIDs;
    public TableColumn weekUserIDs;
    public TableView <Appointment> monAppts;
    public TableColumn monApptIDs;
    public TableColumn monApptTitles;
    public TableColumn monApptDesc;
    public TableColumn monApptLocation;
    public TableColumn monApptContacts;
    public TableColumn monApptTypes;
    public TableColumn monApptDates;
    public TableColumn monApptStarts;
    public TableColumn monApptEnds;
    public TableColumn monApptCustIDs;
    public TableColumn monUserIDs;
    public Button newAppointment;
    public Button deleteAppointment;
    public Button saveAppointment;
    public TextField apptTitle;
    public TextField apptDesc;
    public TextField apptLoc;
    public ComboBox <Contact> apptContact;
    public ComboBox <String> apptType;
    public DatePicker apptDate;
    public TextField apptStart;
    public TextField apptEnd;
    public ComboBox <Customer> custID;
    public ComboBox <User> userID;
    public boolean newAppt = true;
    public TextField apptID;

    /** this lambda alerts me to a selection on the table.
     * lambda number 1
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAllTables();
        fillComboBoxes();
        int currentUserID = DBUser.getCurrentUserID();
        //15 minute appointment reminder
        Appointment upcomingAppt = DBAppointment.getUpcomingAppt(currentUserID);

        if(upcomingAppt != null){
            int upcomingApptID = upcomingAppt.apptID;
            LocalDate upcomingApptDate = upcomingAppt.date;
            LocalTime upcomingApptTime = upcomingAppt.startTime;
            
            apptAlert.setText("UPCOMING APPOINTMENT #" + upcomingApptID + " TODAY, " + upcomingApptDate +
                    ", AT " + upcomingApptTime);
        } else {
            apptAlert.setText("NO APPOINTMENTS WITHIN THE NEXT 15 MINUTES");
        }

        //lambda appointment selections
        allAppts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillAllFields(newSelection);
                apptAlert.setText("");
            }
        });
        weekAppts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillAllFields(newSelection);
                apptAlert.setText("");
            }
        });
        monAppts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillAllFields(newSelection);
                apptAlert.setText("");
            }
        });

    }

    /** this method fills the fields with selected customer information.
    once filled, they can be altered, and updated within the database.
     */
    public void fillAllFields(Appointment selection){
        newAppt = false;
        //set text fields
        apptID.setText(String.valueOf(selection.getApptID()));
        apptTitle.setText(selection.getTitle());
        apptDesc.setText(selection.getDescription());
        apptLoc.setText(selection.getLocation());
        apptStart.setText(String.valueOf(selection.getStartTime()));
        apptEnd.setText(String.valueOf(selection.getEndTime()));
        apptDate.setValue(selection.getDate());
        apptAlert.setText("");

        //select combobox values
        for(Contact C:apptContact.getItems()){
            if(C.getContactID() == selection.getContact().getContactID()){
                apptContact.setValue(C);
                break;
            }
        }

        for(Appointment A:DBAppointment.getAllAppointments(1)){
            if(A.getType().equals(selection.getType())){
                apptType.setValue(A.getType());
                break;
            }

        }
        for(Customer C:custID.getItems()){
            if(C.getId() == selection.getCustID()){
                custID.setValue(C);
                break;
            }

        }
        for(User U:userID.getItems()){
            if(U.getUserID() == selection.getUserID()){
                userID.setValue(U);
            }
        }
    }

    /** this method populates all 3 tables with appointment information from the database.
     */
    public void loadAllTables(){

        //populate table of all appointments
        allAppts.setItems(DBAppointment.getAllAppointments(1));
        allApptIDs.setCellValueFactory(new PropertyValueFactory<>("ApptID"));
        allApptTitles.setCellValueFactory(new PropertyValueFactory<>("Title"));
        allApptDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        allApptLocations.setCellValueFactory(new PropertyValueFactory<>("Location"));
        allApptContacts.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        allApptTypes.setCellValueFactory(new PropertyValueFactory<>("Type"));
        allApptDates.setCellValueFactory(new PropertyValueFactory<>("Date"));
        allApptStarts.setCellValueFactory(new PropertyValueFactory<>("Start"));
        allApptEnds.setCellValueFactory(new PropertyValueFactory<>("End"));
        allApptCustIDs.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        allUserIDs.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        allApptStarts.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        allApptEnds.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        //populate table of weeks appointments
        weekAppts.setItems(DBAppointment.getAllAppointments(2));
        weekApptIDs.setCellValueFactory(new PropertyValueFactory<>("ApptID"));
        weekApptTitles.setCellValueFactory(new PropertyValueFactory<>("Title"));
        weekApptDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        weekApptLocations.setCellValueFactory(new PropertyValueFactory<>("Location"));
        weekApptContacts.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        weekApptTypes.setCellValueFactory(new PropertyValueFactory<>("Type"));
        weekApptDates.setCellValueFactory(new PropertyValueFactory<>("Date"));
        weekApptStarts.setCellValueFactory(new PropertyValueFactory<>("Start"));
        weekApptEnds.setCellValueFactory(new PropertyValueFactory<>("End"));
        weekApptCustIDs.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        weekUserIDs.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        weekApptStarts.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        weekApptEnds.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        //populate table of months appointments
        monAppts.setItems(DBAppointment.getAllAppointments(3));
        monApptIDs.setCellValueFactory(new PropertyValueFactory<>("ApptID"));
        monApptTitles.setCellValueFactory(new PropertyValueFactory<>("Title"));
        monApptDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        monApptLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        monApptContacts.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        monApptTypes.setCellValueFactory(new PropertyValueFactory<>("Type"));
        monApptDates.setCellValueFactory(new PropertyValueFactory<>("Date"));
        monApptStarts.setCellValueFactory(new PropertyValueFactory<>("Start"));
        monApptEnds.setCellValueFactory(new PropertyValueFactory<>("End"));
        monApptCustIDs.setCellValueFactory(new PropertyValueFactory<>("CustID"));
        monUserIDs.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        monApptStarts.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        monApptEnds.setCellValueFactory(new PropertyValueFactory<>("EndTime"));

    }

    /** This method sets items in each of the comboboxes.
     * it also sets the appointment ID
     */
    public void fillComboBoxes(){
        apptID.setText("0");
        apptContact.setItems(DBContact.getAllContacts());
        apptType.setItems(DBAppointment.getAllTypes());
        custID.setItems(DBCustomer.getAllCustomers());
        userID.setItems(DBUser.getAllUsers());
    }

    /** this method clears all text fields and comboboxes.
     *
     */
    public void clearFields(){
        apptID.setText("0");
        apptTitle.setText("");
        apptDesc.setText("");
        apptLoc.setText("");
        apptContact.setValue(null);
        apptType.setValue(null);
        apptDate.setValue(null);
        apptStart.setText("");
        apptEnd.setText("");
        custID.setValue(null);
        userID.setValue(null);
    }

    public void onExitButton (ActionEvent actionEvent)  {

        Platform.exit();
    }

    /** this method allows you to create a new appointment.
     * if there is an existing appointment selected it is unselected and its information is cleared from the textfields
     * and comboboxes
     * @param actionEvent
     */
    public void onNewAppointment(ActionEvent actionEvent) {
        newAppt = true;
        clearFields();
        apptAlert.setText("");
    }

    /** this method deletes the appointment of your choice.
     * if no appointment is selected it does nothing.
     * @param actionEvent
     * @throws SQLException
     */
    public void onDeleteAppointment(ActionEvent actionEvent) throws SQLException {
        //Deletes currently selected customer from DB
        if (!newAppt) {
            apptAlert.setText(apptType.getValue() + " APPOINTMENT #" + apptID.getText() + "HAS BEEN DELETED");
            DBAppointment.deleteAppointment(Integer.parseInt(apptID.getText()));
        }
        allAppts.getSelectionModel().clearSelection();
        weekAppts.getSelectionModel().clearSelection();
        monAppts.getSelectionModel().clearSelection();
        loadAllTables();
        clearFields();
        newAppt = true;
    }


    public void onCManager(ActionEvent actionEvent) throws IOException {
        //go to customer manager
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/view/CustomerManager.fxml")));
        root.setStyle("-fx-font-family: 'Times New Roman'");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
    }

    public void onReportsButton(ActionEvent actionEvent) throws IOException {
        //go to reports screen
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/view/Reports.fxml")));
        root.setStyle("-fx-font-family: 'Times New Roman'");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }

    /** this method saves user input into the appointments within the database.
     *  if there is invalid input it displays error messages and does not save.
     * @param actionEvent
     * @throws SQLException
     */
    public void onSaveAppointment(ActionEvent actionEvent) throws SQLException {
        //if is new appt create new appointment in DB (check for double booking first)
        /*schedulingConflict
        0 = no conflict
        1 = outside business hours (8AM-10PM EST ALL DAYS)
        2 = conflicting appt
        3 = no date
        4 = incorrect time input format
         */
        boolean hasConflict = false;
        int conflictCode = 0;

        //check inputs before continuing
        if (apptDate == null) {
            apptAlert.setText("NO DATE SELECTED.");
            hasConflict = true;
        }
        String startTime = apptStart.getText();
        String endTime = apptEnd.getText();
        try {
            LocalTime.parse(startTime);
            LocalTime.parse(endTime);
        }catch (DateTimeParseException E){
            hasConflict = true;
            apptAlert.setText("INCORRECT TIME INPUT");
        }
    if (apptDate.getValue() == null){
        hasConflict = true;
        apptAlert.setText("INVALID DATE");
    }
    if (apptType.getValue() == null){
        hasConflict = true;
        apptAlert.setText("SELECT A TYPE");
    }
    if (custID.getValue() == null){
        hasConflict = true;
        apptAlert.setText("SELECT A CUSTOMER");
    }
    if (userID.getValue() == null){
        hasConflict = true;
        apptAlert.setText("SELECT A USER");
    }
    if (apptContact.getValue() == null){
        hasConflict = true;
        apptAlert.setText("SELECT A CONTACT");
    }
    //cutosmer user and contact ^^
        //check both time inputs


        if (!hasConflict) {
            conflictCode = DBAppointment.schedulingConflict(apptID.getText().toString(), apptDate.getValue(), LocalTime.parse(apptStart.getText()), LocalTime.parse(apptEnd.getText()));

            if (conflictCode == 1) {
                hasConflict = true;
                // business hours error message
                apptAlert.setText("APPOINTMENT MUST BE WITHIN BUSINESS HOURS");

            } else if (conflictCode == 2) {
                hasConflict = true;
                //conflicting appt error
                apptAlert.setText("CANNOT SCHEDULE OVERLAPPING APPOINTMENT");

            }

            if (!hasConflict && newAppt) {
                //CREATE NEW APPT IN DB
                System.out.println("NEW APPT MADE");
                DBAppointment.createNewAppointment( apptTitle.getText(), apptDesc.getText(),
                        apptLoc.getText(), apptType.getValue(), apptDate.getValue(), custID.getValue().getId(),
                        userID.getValue().getUserID(), LocalTime.parse(apptStart.getText()), LocalTime.parse(apptEnd.getText()), apptContact.getValue().getContactID());
            loadAllTables();
            clearFields();
            } else if (!hasConflict && !newAppt) {
                //UPDATE APPT IN DB
                System.out.println("APPOINTMENT UPDATED");
                DBAppointment.updateAppointment(Integer.parseInt(apptID.getText()), apptTitle.getText(), apptDesc.getText(),
                        apptLoc.getText(), apptType.getValue(), apptDate.getValue(), custID.getValue().getId(),
                        userID.getValue().getUserID(), LocalTime.parse(apptStart.getText()), LocalTime.parse(apptEnd.getText()), apptContact.getValue().contactID);
            loadAllTables();
            clearFields();
            }
        }
    }
}

