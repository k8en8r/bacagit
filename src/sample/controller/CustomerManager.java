package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import sample.dbaccess.DBCountry;
import sample.dbaccess.DBCustomer;
import sample.dbaccess.DBDivision;
import sample.model.Country;
import sample.model.Customer;
import sample.model.Division;

/** This class creates a scene that allows you to manage customers.
You can add, update, and delete customers as needed.
 */
public class CustomerManager implements Initializable {

    public Button apptMgr;
    public Button reportsButton;
    public Button exitButton;
    public TableView <Customer>allCustomers;
    public TableColumn customerIDs;
    public TableColumn customerNames;
    public TableColumn allAddresses;
    public TableColumn allPostalCodes;
    public TableColumn allPhoneNums;
    public TableColumn allDivisionIDs;
    public Button newCustomer;
    public Button deleteCustomer;
    public Button saveCustomer;
    public TextField custID;
    public TextField custName;
    public TextField custPhone;
    public ComboBox <Country>custCountry;
    public ComboBox <Division>custDiv;
    public TextField custAdd;
    public TextField custPost;
    public boolean newCust = true;
    public Label custAlert;

    /** this lambda alerts me of a selection in the table
     * lambda number 1
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCustTable();
        custCountry.setItems(DBCountry.getAllCountries());
        custID.setText("AUTOGEN");

        //lambda customer selection
        allCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                newCust = false;
                custID.setText(String.valueOf(newSelection.getId()));
                custName.setText(newSelection.getName());
                custPhone.setText(newSelection.getPhone());
                for(Country c:custCountry.getItems()){
                    if(c.getCountryID() == newSelection.countryID){
                        custCountry.setValue(c);
                        break;
                    }
                }
                custDiv.setItems(DBDivision.getAllDivisions(newSelection.countryID));
                for(Division d:custDiv.getItems()){
                    if(d.divID ==newSelection.divID){
                        custDiv.setValue(d);
                        break;
                    }
                }
                custAdd.setText(newSelection.getAddress());
                custPost.setText(newSelection.getPostalCode());
            }
        });

    }

    /** this method pulls customer information from the database and populates it into a table
     *
     */
    public void loadCustTable(){
        //populate table of customers
        allCustomers.setItems(DBCustomer.getAllCustomers());
        customerIDs.setCellValueFactory(new PropertyValueFactory<>("Id"));
        allAddresses.setCellValueFactory(new PropertyValueFactory<>("Address"));
        allPostalCodes.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        allPhoneNums.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        allDivisionIDs.setCellValueFactory(new PropertyValueFactory<>("DivID"));
        customerNames.setCellValueFactory(new PropertyValueFactory<>("Name"));

    }

    /** this method clears all text fields and comboboxes
     *
     */
    public void clearFields(){
        custID.setText("AUTOGEN");
        custName.clear();
        custPhone.clear();
        custCountry.setValue(null);
        custDiv.setValue(null);
        custAdd.clear();
        custPost.clear();
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

    public void onExitButton(ActionEvent actionEvent) {
        Platform.exit();
    }

    /** this method allows the user to create a new customer.
     * if a customer is already selected it clears the selection as well as the text fields and comboboxes.
     * @param actionEvent
     */
    public void onNewCustomer(ActionEvent actionEvent) {
        allCustomers.getSelectionModel().clearSelection();
        clearFields();
        newCust = true;
    }


    /** this method deletes customer data from the database.
     * prior to deleting the customer, all appointments related to the customer need to be deleted first
     * due to foreign key constraints.
     * if no customer is selected, nothing is done.
     * @param actionEvent
     */
    public void onDeleteCustomer(ActionEvent actionEvent) {
        //Deletes currently selected customer from DB
        if (!newCust) {
            custAlert.setText("CUSTOMER #" + custID.getText() + " HAS BEEN DELETED");
            DBCustomer.deleteCustomer(Integer.parseInt(custID.getText()));
        }
        allCustomers.getSelectionModel().clearSelection();
        loadCustTable();
        clearFields();
        newCust = true;
    }

    /** this method saves a customer to the database.
     * if it is an existing customer it is updated.
     * if it is a new customer it is inserted.
     */
    public void onSaveCustomer(ActionEvent actionEvent) throws SQLException {
        if (newCust) {
            DBCustomer.saveNewCustomer(custName.getText(), custAdd.getText(), custPost.getText(), custPhone.getText(),custDiv.getValue().divID);
        }
        else {
            DBCustomer.saveExistingCustomer(custName.getText(), custAdd.getText(), custPost.getText(), custPhone.getText(),custDiv.getValue().divID, Integer.parseInt(custID.getText()));

        }
        loadCustTable();
        clearFields();
    }

    public void onApptMgr(ActionEvent actionEvent) throws IOException {
        //go to appointment manager
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/view/AppointmentManager.fxml")));
        root.setStyle("-fx-font-family: 'Times New Roman'");
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

    /** this method populates the combobox for division data based on the country that is selected.
     *
     * @param actionEvent
     */
    public void countrySelection(ActionEvent actionEvent) {
        //Needed to check if null bc onNewCustomer and onDeleteCustomer triggers this event
        Country c = custCountry.getValue();
        if (c != null) {
            //update divisions based on country selection
            custDiv.setItems(DBDivision.getAllDivisions(c.countryID));
        }

    }
}
