package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.dbaccess.DBUser;
import sample.model.User;

import java.io.IOException;
import java.io.*;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/** this class acts as a gate to keep out unregistered users.
 * a username and password are input by the user and checked against the information in the database.
 * all attempts are written to a file.
 * once entered you are redirected to the appointment manager.
 */
public class LogIn implements Initializable {
    public Label logInError;
    public TextField username;
    public TextField password;
    public Button logInButton;
    public Label locality;

    private ResourceBundle myBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myBundle = resourceBundle;
        locality.setText(Locale.getDefault().getDisplayCountry() + " " + TimeZone.getDefault().getDisplayName());

    }

    /** this method logs users in.
     * all attempts are written to a file.
     * if failed attempt, error message is shown.
     * if successful, you are redirected to the appointment manager.
     * @param actionEvent
     * @throws IOException
     */
    public void onLogIn (ActionEvent actionEvent) throws IOException {

        //retrieve users from DB
        boolean userFound = false;
        boolean passwordMatch = false;
        int userID;
        String fileName = "src/sample/login_activity.txt";
        FileWriter fwVariable = new FileWriter(fileName, true);
        PrintWriter pwVariable = new PrintWriter(fwVariable);
        ObservableList<User> userList = DBUser.getAllUsers();
            //check username
        for (int i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
                if (u.username.equals(username.getText())) {
                    //user found
                    userFound = true;
                    if(u.password.equals(this.password.getText())){
                        passwordMatch = true;
                        //get userID for appoint 15 min
                        userID = u.getUserID();
                        DBUser.setCurrentUserID(userID);
                    }
                    break;
                }
            }


        if (!userFound){
            logInError.setText(myBundle.getString("Invalid Username"));
            //write invalid login to file
            pwVariable.println("UNSUCCESSFUL " + LocalDate.now() + " " + LocalTime.now() + "  User: " + username.getText());
            pwVariable.close();
            fwVariable.close();
        }
        else {
            //check password
            if (passwordMatch) {
                pwVariable.println("SUCCESSFUL " + LocalDate.now() + " " + LocalTime.now() + "  User: " + username.getText());
                pwVariable.close();
                fwVariable.close();
                //go to appointment manager
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/sample/view/AppointmentManager.fxml")));
                root.setStyle("-fx-font-family: 'Times New Roman'");
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Appointment Manager");
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                //write valid login to file and perform 15 minutes alert based on userID
            } else {
                //display password error
                logInError.setText(myBundle.getString("Invalid Password"));
                //write invalid login to file
                pwVariable.println("UNSUCCESSFUL " + LocalDate.now() + " " + LocalTime.now() + "  User: " + username.getText());
                pwVariable.close();
                fwVariable.close();

            }
        }


    }



}
