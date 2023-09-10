package app.controllers.loans.application;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML private TextField loanAmountField;
    @FXML private ComboBox<Integer> loanPeriodSelector;

    @FXML private ComboBox<Integer> interestRateSelector;
    @FXML private ComboBox<Integer> processingRateSelector;
    @FXML private TextArea loanReasonField;
    @FXML private MFXDatePicker startDatePicker;
    @FXML private Label displayLoanAmount, displayInterestAmount, displayProcessingAmount, displayMonthlyInstallmentAmount;
    @FXML private Label displayTotalLoanAmount, displayStartDate, displayEndDate, displayLoanStatus;

    @FXML
    private Label filterCustomerLabel,customerTypeLabel;
    @FXML
    private VBox vBox;
    @FXML
    Pane customerSelectorPane;
    @FXML private ComboBox<String> customerTypeSelector;
    @FXML private ComboBox<Integer> insuranceRateSelector;
    @FXML private TextField customerSearchBox;
    @FXML private MFXListView<String> listView;
    @FXML private GridPane gridPane;
    @FXML private TextField salaryAmountField, loanTotalDeductionField, loansDeductionField, affordabilityField;
    @FXML private  Label remainingBalanceLabel;

    public ApplicationController() throws IOException {
    }


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    boolean isLoanAmountFieldEmpty() {
        return loanAmountField.getText().isBlank() || loanAmountField.getText().isEmpty();
    }
    boolean isInterestRateEmpty() {
        return interestRateSelector.getValue() == null;
    }
    boolean isProcessingRateEmpty() {return processingRateSelector.getValue() == null;}
    boolean isLoanPeriodSelectorEmpty() {return loanPeriodSelector.getValue() == null;}
    boolean isStartDateEmpty() {return startDatePicker.getValue() == null;}
    boolean isLoanReasonEmpty() {return loanReasonField.getText().isBlank();}






    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionEventsMethodsImplementation();
        String[] customerTypes = {"New Client", "Existing Client"};
        customerTypeSelector.setValue(customerTypes[0]);
        for (String customerType : customerTypes) {
            customerTypeSelector.getItems().add(customerType);
        }
    }

    double computeInterestAmount() {
        double principal = Double.parseDouble(loanAmountField.getText());
        int rate = Integer.parseInt(interestRateSelector.getValue().toString());
        return (principal * rate) / 100;
    }
    double computeProcessingAmount() {
        double principal = Double.parseDouble(loanAmountField.getText());
        int rate = Integer.parseInt(processingRateSelector.getValue().toString());
        return (principal * rate) / 100;
    }
    double computeMonthlyInstallment() {
        int monthValue = Integer.parseInt(loanPeriodSelector.getValue().toString());
        return computeTotalLoanAmount() / monthValue;
    }
    double computeTotalLoanAmount() {
        double loanAmount = Double.parseDouble(displayLoanAmount.getText());
        double interestAmount = Double.parseDouble(displayInterestAmount.getText());
        double processingRate = Double.parseDouble(displayProcessingAmount.getText());
        return Math.abs(loanAmount + interestAmount + processingRate);
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
    void actionEventsMethodsImplementation() {



//            for (int x= 1; x<=monthValue; x++) {
//                System.out.println("Month " + x + " " + startDate.plusMonths(x) + " monthly pay at " + displayMonthlyInstallmentAmount.getText());
//            }

    }



}//end of class...
