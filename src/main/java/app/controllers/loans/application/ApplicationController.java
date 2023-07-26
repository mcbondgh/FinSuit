package app.controllers.loans.application;

import app.alerts.UserNotification;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label filterCustomerLabel,customerTypeLabel;
    @FXML private BorderPane borderPane;
    @FXML
    Pane customerSelectorPane;
    @FXML private ComboBox<String> customerTypeSelector;
    @FXML private TextField customerSearchBox;
    @FXML private MFXListView<String> listView;
    @FXML private GridPane gridPane;
    @FXML private MFXButton nextButton, backButton;

    public ApplicationController() throws IOException {
    }


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] customerTypes = {"New Client", "Existing Client"};
        customerTypeSelector.setValue(customerTypes[0]);
        for (String customerType : customerTypes) {
            customerTypeSelector.getItems().add(customerType);
        }
        try {
            loadForm();
            loadCustomersView();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    void loadForm() throws IOException {
        String fxmlFile = "views/loans/application-forms/loan-application-form1.fxml";
        SpecialMethods.FlipView(fxmlFile, borderPane);
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    @FXML
    void filterCustomers(KeyEvent event) {

    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/


    @FXML
    void customerTypeSelected(ActionEvent event) {
        String value = customerTypeSelector.getValue();
        switch (value) {
            case "New Client" -> {
                customerSelectorPane.setVisible(false);
//                listView.setVisible(false);
//                customerSearchBox.setVisible(false);
//                gridPane.setVisible(false);
//                filterCustomerLabel.setVisible(false);
            }
            case "Existing Client" -> {
                customerSelectorPane.setVisible(true);
//                listView.setVisible(true);
//                customerSearchBox.setVisible(true);
//                gridPane.setVisible(true);
//                filterCustomerLabel.setVisible(true);
//                customerTypeLabel.setVisible(true);
            }
        }
    }
    @FXML
    void operationTypeSelected(ActionEvent event) throws IOException {
//        String operation = operationTypeSelector.getValue();
//        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/loans/loan-payment-page.fxml"));
//        switch(operation) {
//            case "Loan Application" -> {
//                customerSelectorPane.setVisible(true);
//                borderPane.setCenter(null);
//                customerSelectorPane.setVisible(true);
//            }
//            case "Loan Payment" -> {
//                borderPane.setCenter(fxmlLoader.load());
//                customerSelectorPane.setVisible(false);
//            }
//        }
    }

    @FXML void nextButtonClicked() throws IOException {
        String fxmlFile = "views/loans/application-forms/loan-application-page2.fxml";
        SpecialMethods.FlipView(fxmlFile, borderPane);
        nextButton.setDisable(true);
        backButton.setDisable(false);
    }
    @FXML public void backButtonClicked() throws IOException {
        String fxmlFile = "views/loans/application-forms/loan-application-form1.fxml";
        SpecialMethods.FlipView(fxmlFile, borderPane);
        backButton.setDisable(true);
        nextButton.setDisable(false);
    }

    void loadCustomersView() throws IOException, InterruptedException {

    }



}//end of class...
