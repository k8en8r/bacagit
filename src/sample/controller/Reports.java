package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import sample.dbaccess.DBAppointment;
import sample.dbaccess.DBContact;
import sample.dbaccess.DBCountry;
import sample.dbaccess.DBCustomer;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** this class runs reports on data within the database.
 *
 */
public class Reports implements Initializable {
    public TableColumn apptIDs;
    public Button custMgr;
    public Button apptScheduler;
    public Button exitButton;
    public Button calcApptType;
    public ComboBox <String> apptTypeSelect;
    public ComboBox <Integer> apptMonSelect;
    public Label apptTypeResults;
    public ComboBox <Contact> contactSelect;
    public TableView <Appointment> contactsAppts;
    public TableColumn apptTitles;
    public TableColumn apptDesc;
    public TableColumn apptTypes;
    public TableColumn apptDates;
    public TableColumn apptStarts;
    public TableColumn apptEnds;
    public TableColumn custIDs;
    public TextField postalCode;
    public Button postalCodeButton;
    public Label postalCodeResults;
    public Pane productsTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        apptTypeSelect.setItems(DBAppointment.getAllTypes());
        //
        ObservableList<Integer> monthSelections = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12);
        apptMonSelect.setItems(monthSelections);
        contactSelect.setItems(DBContact.getAllContacts());

    }

    public void onCustMgr(ActionEvent actionEvent) throws IOException {
        //go to appointment manager
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/view/CustomerManager.fxml")));
        root.setStyle("-fx-font-family: 'Times New Roman'");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();
    }

    public void onApptScheduler(ActionEvent actionEvent) throws IOException {
        //go to appointment manager
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/view/AppointmentManager.fxml")));
        root.setStyle("-fx-font-family: 'Times New Roman'");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

    public void onExitButton(ActionEvent actionEvent) {
        Platform.exit();

    }

    /** This method calculates a number of appointments based on user input.
     * users select an appointment type, and month, and the number of appointments of that type within the
     * month is shown to the user
     * @param actionEvent
     */
    public void onCalcAppt(ActionEvent actionEvent) {
       String selectedApptType = apptTypeSelect.getValue();
       int selectedApptMon = apptMonSelect.getValue();
       int apptCounter = 0;
       if(selectedApptMon > 0 && selectedApptMon < 13 && selectedApptType != null){
           //monSelection Jan starts at 4 bc 1-3 are for apptmanager
           int monSelection = 0;
           if (selectedApptMon == 1){
               monSelection = 4;
           }
           else if (selectedApptMon == 2){
               monSelection = 5;
           }
           else if (selectedApptMon == 3){
               monSelection = 6;
           }
           else if (selectedApptMon == 4){
               monSelection = 7;
           }
           else if (selectedApptMon == 5){
               monSelection = 8;
           }
           else if (selectedApptMon == 6){
               monSelection = 9;
           }
           else if (selectedApptMon == 7){
               monSelection = 10;
           }
           else if (selectedApptMon == 8){
               monSelection = 11;
           }
           else if (selectedApptMon == 9){
               monSelection = 12;
           }
           else if (selectedApptMon == 10){
               monSelection = 13;
           }
           else if (selectedApptMon == 11){
               monSelection = 14;
           }
           else if (selectedApptMon == 12){
               monSelection = 15;
           }
           ObservableList<Appointment> apptsByMonAndType = FXCollections.observableArrayList();
           for (Appointment A: DBAppointment.getAllAppointments(monSelection)){
               if(A.type.equals(selectedApptType)){
                   apptCounter++;
               }
           }
           apptTypeResults.setText(apptCounter + " " + selectedApptType + " appointments in " + selectedApptMon);
       }else{
           apptTypeResults.setText("");
       }
    }

    /** lambda number 2.
     * this method shows all appointments each contact has.
     * the user selects a contact, and appointment information for that contact is populated within a table.
     * @param actionEvent
     */
    public void onContactSelection(ActionEvent actionEvent) {
        String theContactName = contactSelect.getValue().contactName;
        //ObservableList<Appointment> allContactAppts = FXCollections.observableArrayList();
        /*for (Appointment A: DBAppointment.getAllAppointments(1)){
            if(A.getContact().contactName.equals(theContactName)){
                allContactAppts.add(A);
            }
        }*/
        ObservableList<Appointment> allAppointments = DBAppointment.getAllAppointments(1);
        ObservableList<Appointment> allContactAppts = allAppointments.filtered(A->{
            if(A.getContact().contactName.equals(theContactName)){
                return true;
            }
            return false;
        });
        //populate table of contacts appointments
        contactsAppts.setItems(allContactAppts);
        apptIDs.setCellValueFactory(new PropertyValueFactory<>("ApptID"));
        apptTitles.setCellValueFactory(new PropertyValueFactory<>("Title"));
        apptDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        apptTypes.setCellValueFactory(new PropertyValueFactory<>("Type"));
        apptDates.setCellValueFactory(new PropertyValueFactory<>("Date"));
        apptStarts.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        apptEnds.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        custIDs.setCellValueFactory(new PropertyValueFactory<>("CustID"));

    }

    /** this method calculates how many customers live in a given postal code.
     * the user inputs a postal code and they are given the number of customers in the area.
     * @param actionEvent
     */
    public void onPostalCode(ActionEvent actionEvent) {
        ObservableList<Customer> allCusts = FXCollections.observableArrayList();
        int custCounter = 0;
        for (Customer C: DBCustomer.getAllCustomers()){
            if(C.postalCode.equals(postalCode.getText())){
                custCounter++;
            }
        }
        postalCodeResults.setText("There are " + custCounter + " customers in Postal Code " + postalCode.getText());
    }
}
