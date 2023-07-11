package app.controllers.loans;

import app.AppStarter;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoansController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle, filterCustomerLabel,customerTypeLabel;
    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML
    Pane loanApplicationPane, customerSelectorPane;
    @FXML private ComboBox<String> operationTypeSelector, customerTypeSelector;
    @FXML private TextField customerSearchBox;
    @FXML private MFXListView<String> listView;
    @FXML private GridPane gridPane;
    @FXML private MFXButton nextButton;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] customerTypes = {"New Client", "Existing Client"};
        String[] operationType = {"Loan Application", "Loan Payment"};
        pageTitle.setText(pageTitlePlaceHolder);

        for (int x = 0; x < operationType.length; x++){
            operationTypeSelector.getItems().add(operationType[x]);
            customerTypeSelector.getItems().add(customerTypes[x]);
        }
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
                borderPane.setCenter(loanApplicationPane);
                loanApplicationPane.setVisible(true);
                listView.setVisible(false);
                customerSearchBox.setVisible(false);
                gridPane.setVisible(false);
                filterCustomerLabel.setVisible(false);
            }
            case "Existing Client" -> {
                listView.setVisible(true);
                customerSearchBox.setVisible(true);
                gridPane.setVisible(true);
                filterCustomerLabel.setVisible(true);
                customerTypeLabel.setVisible(true);
            }
        }
    }
    @FXML
    void operationTypeSelected(ActionEvent event) throws IOException {
        String operation = operationTypeSelector.getValue();
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/loans/loan-payment-page.fxml"));
        switch(operation) {
            case "Loan Application" -> {
                customerSelectorPane.setVisible(true);
                borderPane.setCenter(null);
                customerSelectorPane.setVisible(true);
            }
            case "Loan Payment" -> {
                borderPane.setCenter(fxmlLoader.load());
                customerSelectorPane.setVisible(false);
            }
        }
    }
    @FXML void nextButtonClicked() throws IOException {
        String fxmlFile ="views/loans/loan-application-page2.fxml";
        SpecialMethods.FlipView(fxmlFile, borderPane);
    }
//    @FXML void backButtonClicked() throws IOException {
//        pageTitle.setVisible(false);
//        loanApplicationPane.setVisible(true);
//        SpecialMethods.FlipView(loanApplicationPane, borderPane);
//    }





}//end of class
