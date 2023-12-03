package app.controllers.loans;

import app.alerts.UserAlerts;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.enums.MessageStatus;
import app.models.loans.LoansModel;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.transactions.TransactionsEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LoanPaymentController extends LoansModel implements Initializable {

    UserAlerts ALERT;
    SmsAPI SMS = new SmsAPI();
    MessageLogsEntity LOG_MESSAGE = new MessageLogsEntity();
    TransactionsEntity TRANS_ENTITY = new TransactionsEntity();
    NotificationEntity NOTIFY_ENTITY = new NotificationEntity();

    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);


    private static String loanNumber;
    private static LocalDate dueDate;
    private static double payableAmount;
    private static double penaltyAmount;
    private static String name, mobileNumber;

    public static String getLoanNumber() {return loanNumber;}
    public static void setLoanNumber(String value){loanNumber = value;}
    public static LocalDate getDueDate(){return dueDate;}
    public static void setDueDate(LocalDate value){dueDate = value;}
    public static double getPayableAmount() {return payableAmount;}
    public static void setPayableAmount(double value){payableAmount = value;}
    public static void setPenaltyAmount(double value){penaltyAmount = value;}
    public static double getPenaltyAmount(){return penaltyAmount;}

    public static String getName() {return name;}

    public static void setName(String name) {LoanPaymentController.name = name;}

    public static String getMobileNumber() {return mobileNumber;}

    public static void setMobileNumber(String mobileNumber) {LoanPaymentController.mobileNumber = mobileNumber;}



    /******************************************************************************************************************
                                                        FXML FILE EJECTIONS
     ******************************************************************************************************************/
    @FXML private Label loanNumberField;
    @FXML private Label dateField, payableField;
    @FXML private ComboBox<String>methodSelector;
    @FXML private CheckBox payableCheckBox;
    @FXML private TextField paymentAmountField;
    @FXML private MFXButton collectButton;
    @FXML private Label penaltyLabel, successIndicator;


    /******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS.
     ******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanNumberField.setText(getLoanNumber());
        dateField.setText(String.valueOf(getDueDate()));
        payableField.setText(String.valueOf(getPayableAmount()));
        penaltyLabel.setText(String.valueOf(getPenaltyAmount()));
        payableCheckBoxChecked();
        methodSelector.getItems().add("CASH");
        methodSelector.getItems().add("eCASH");

    }
    @FXML void checkForEmptyField() {
        collectButton.setDisable(isAmountFieldEmpty() || isMethodSelectorEmpty());
    }

    /******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     ******************************************************************************************************************/
    boolean isCheckBoxChecked() {return payableCheckBox.isSelected();}
    boolean isAmountFieldEmpty(){return paymentAmountField.getText().isBlank();}
    boolean isMethodSelectorEmpty(){return methodSelector.getValue() == null;}

    /******************************************************************************************************************
     ACTION EVENT METHODS IMPLEMENTATION...
     ******************************************************************************************************************/
    void payableCheckBoxChecked() {
        payableCheckBox.setOnAction(checked -> {
            if (isCheckBoxChecked()) {
                paymentAmountField.setText(payableField.getText());
            }else {
                paymentAmountField.setText("0.00");
            }
        });
    }//....end of method

    @FXML void collectButtonClicked() {
        String var = "for ".concat(dateField.getText());

        double amount = Double.parseDouble(paymentAmountField.getText());
        double penalty = Double.parseDouble(penaltyLabel.getText());
        Date date = Date.valueOf(dateField.getText());
        String payMethod = methodSelector.getValue();
        String loanNumber = loanNumberField.getText();

        Object[] variables = {getName(), amount, loanNumber, var};




    }//.........end of method




}//END OF CLASS...
