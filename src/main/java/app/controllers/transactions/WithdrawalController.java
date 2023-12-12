package app.controllers.transactions;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.enums.PaymentMethods;
import app.models.transactions.TransactionModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.action.ActionCheck;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

@ActionCheck
public class WithdrawalController extends TransactionModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    private static String currentUserPlaceholder;
    TransactionsEntity transactions = new TransactionsEntity();
    CustomerAccountsDataRepository accountsDataRepository = new CustomerAccountsDataRepository();

    final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public static String getCurrentUserPlaceholder() {
        return currentUserPlaceholder;
    }
    public static void setCurrentUserPlaceholder(String placeholder) {
        currentUserPlaceholder = placeholder;
    }


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTION
     ********************************************************************************************************************/
    @FXML private MFXFilterComboBox<String> accountNumberField;
    @FXML private Label chargeValue, accountHolderName, accountStatusLabel;
    @FXML private ComboBox<PaymentMethods> paymentSelector;
    @FXML private TextField collectorsNameField, cashField, transactionIdField, idNumberField;
    @FXML private MFXButton saveButton;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    boolean isAmountEmpty() {return cashField.getText().isEmpty();}
    boolean isPaymentMethodEmpty() {return paymentSelector.getValue() == null;}
    boolean accountNumberEmpty() {return accountNumberField.getValue().isEmpty();}
    boolean collectorNameEmpty() {return collectorsNameField.getText().isEmpty();}
    boolean idNumberEmpty() {return idNumberField.getText().isBlank();}

    

    @FXML void validateInputFields(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9]")) {
            cashField.deletePreviousChar();
        }
    }
    
    @FXML void checkForEmptyFields() {
        try {
            saveButton.setDisable(
                    accountNumberEmpty() || isAmountEmpty() || isPaymentMethodEmpty() ||
                            collectorNameEmpty() || accountStatusLabel.getText().equalsIgnoreCase("closed")
                    || idNumberEmpty()
            );
            cashField.setDisable(accountStatusLabel.getText().equalsIgnoreCase("closed"));
        }catch (NullPointerException ignore){}
    }
 
    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateFields();
    }

    private void populateFields() {
        setCurrentUserPlaceholder(AppController.activeUserPlaceHolder);
        SpecialMethods.setPaymentMethods(paymentSelector);
        for (CustomerAccountsDataRepository data : fetchCustomersAccountData()) {
            accountNumberField.getItems().add(data.getAccount_number());
        }
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION
     ********************************************************************************************************************/
    @FXML void setOnAccountNumberSelected() {
        String var1 = accountNumberField.getValue();
        ArrayList<Object> items = getCustomerDetailsByAccountNumber(var1);
        String status = getCustomerDetailsByAccountNumber(var1).get(6).toString();
        AtomicReference<Double> withdrawalTax = new AtomicReference<>((double) 0);
        getBusinessInfo().forEach(data -> {
            withdrawalTax.set(data.getTaxPercentage());
        });
        accountStatusLabel.setText(status.toUpperCase());
        accountStatusLabel.setStyle(status.equals("active") ? "-fx-text-fill:green; -fx-background-color:white; -fx-border-color:#ddd" :
                "-fx-text-fill:red; -fx-background-color:white; -fx-border-color:#ddd");
        accountHolderName.setText(items.get(0).toString());
        chargeValue.setText(String.valueOf(withdrawalTax.getAcquire()));
    }
    @FXML void setSelectedPaymentMethod() {
        if (paymentSelector.getValue().equals(PaymentMethods.eCASH) || paymentSelector.getValue().equals(PaymentMethods.BOTH_METHODS)) {
            transactionIdField.setDisable(false);
        }else {
            transactionIdField.setDisable(true);
            transactionIdField.setText("null");
        }
    }

    @FXML void saveButtonClicked() {
        try{
            double transactionCharge = Double.parseDouble(chargeValue.getText());
            double withdrawalAmount = Double.parseDouble(cashField.getText());
            double currentBalance = Double.parseDouble(getCustomerDetailsByAccountNumber(accountNumberField.getText()).get(2).toString());
            double totalWithdrawalAmount = withdrawalAmount + transactionCharge;
            double balance = currentBalance - totalWithdrawalAmount;
            if (balance < 0) {
                ALERTS = new UserAlerts("INVALID AMOUNT", "Sorry, please review your withdrawal amount, withdrawal amount cannot be greater current balance", "you cannot withdraw more then your current account balance.");
                ALERTS.errorAlert();
                cashField.deletePreviousChar();
            } else {
                System.out.println("all clear for withdrawal");
            };
        }catch (NumberFormatException ignore){}
        
    }

}
