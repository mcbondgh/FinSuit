package app.controllers.loans;

import app.AppStarter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoansController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;

    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadForm();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void loadForm() throws InterruptedException, IOException {
        pageTitle.setText(pageTitlePlaceHolder);
        String fxmlFile = "views/loans/loan-customers-page.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlFile));
        borderPane.setCenter(fxmlLoader.load());
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    public void loadCustomersTable(MouseEvent mouseEvent) throws IOException {

    }

}//end of class
