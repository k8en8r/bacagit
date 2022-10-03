package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * this class is our main.
 */
public class Main extends Application {

    /**this method launches our log-in screen.
     *
     */
@Override
    public void start(Stage stage) throws Exception{

        //get language functioning
        stage.setTitle("Log In");
        ResourceBundle rb = ResourceBundle.getBundle("sample/RBFrench", Locale.getDefault());
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/LogIn.fxml")), rb);
        Scene scene = new Scene(root, 600, 360);
        scene.getRoot().setStyle("-fx-font-family: 'serif'");
        stage.setScene(scene);
        stage.show();
        }

    /** this method connects the program to the database.
     *
     * @param args
     */
    public static void main(String[] args) {


        JDBC.makeConnection();
        launch(args);
    }
}
