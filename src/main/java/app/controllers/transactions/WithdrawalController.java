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
    @FXML private Label currentBalanceLabel, accountHolderName;
    @FXML private ComboBox<PaymentMethods> paymentSelector;
    @FXML private TextField collectorsNameField, cashField;
    @FXML private MFXButton saveButton;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    boolean isAmountEmpty() {return cashField.getText().isEmpty();}
    boolean isPaymentMethodEmpty() {return paymentSelector.getValue() == null;}
    boolean accountNumberEmpty() {return accountNumberField.getValue().isEmpty();}
    boolean collectorNameEmpty() {return collectorsNameField.getText().isEmpty();}

    

    @FXML void validateInputFields(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9]")) {
            cashField.deletePreviousChar();
        }
    }
    
    @FXML void checkForEmptyFields() {
        try {
            saveButton.setDisable(accountNumberEmpty() || isAmountEmpty() || isPaymentMethodEmpty() || collectorNameEmpty());
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
        ArrayList<Object> items = getCustomerFullNameByAccountNumber(var1);
        accountHolderName.setText(items.get(0).toString());
        currentBalanceLabel.setText(String.valueOf(items.get(2)));
    }
    @FXML void setSelectedPaymentMethod() {

    }

    @FXML void saveButtonClicked() {
        try{
            double currentBalance = Double.parseDouble(currentBalanceLabel.getText());
            double withdrawalAmount = Double.parseDouble(cashField.getText());
            double balance = currentBalance - withdrawalAmount;

            if (balance < currentBalance) {
                System.out.println(true);
                ALERTS = new UserAlerts("INVALID AMOUNT", "Sorry, please review your withdrawal amount, withdrawal amount cannot be greater current balance", "you cannot withdraw more then your current account balance.");
                ALERTS.errorAlert();
                cashField.deletePreviousChar();
            } else System.out.println(false);
        }catch (NumberFormatException ignore){}
        
    }

}
