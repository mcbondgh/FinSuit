package app.controllers.transactions;

import app.alerts.UserAlerts;
import app.config.encryptDecryp.EncryptDecrypt;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.enums.MessageStatus;
import app.models.message.MessagesModel;
import app.models.transactions.TransactionModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class CheckAccountBalance extends TransactionModel implements Initializable {

    /*******************************************************************************************************************
                        FXML FILE EJECTIONS...
     *******************************************************************************************************************/
    @FXML
    private MFXFilterComboBox<String> accountNumberField;
    @FXML private MFXButton getBalanceButton;
    @FXML private Label customerNameField, currentBalanceField, previousBalanceField;
    @FXML private Label transactionTypeLabel, lastWithdrawalDateLabel;
    @FXML private Label usernameLabel, errorIndicator;
    @FXML private TextField pinNumberField;
    @FXML Hyperlink sendSmsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listItems = FXCollections.observableArrayList();
        for (CustomerAccountsDataRepository data : fetchCustomersAccountData()) {
            listItems.add(data.getAccount_number());
        }
        accountNumberField.setItems(listItems);
        getBalanceButton.setDisable(true);
        sendSmsButton.setDisable(true);
    }
    int userId = getUserIdByName(AppController.activeUserPlaceHolder);


    /*******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/


    /*******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    @FXML void checkPinNumberField() {
        getBalanceButton.setDisable(pinNumberField.getText().isEmpty());
    }


    /*******************************************************************************************************************
     IMPLEMENTATION OF ACTION EVENT METHODS
     *******************************************************************************************************************/

      public void getBalanceButtonClicked(ActionEvent actionEvent) {
        ArrayList<Object> customerData = getCustomerDetailsByAccountNumber(accountNumberField.getValue());
        Map<String, Object> transactionData = getLastTransactionDate(accountNumberField.getValue());
        String databasePin = customerData.get(7).toString();
        String userInputPin = pinNumberField.getText();

//        if (pinNumberField.getText().isBlank()) {
//            errorIndicator.setText("Please provide customer pin number");
//            errorIndicator.setVisible(true);
//        } else errorIndicator.setVisible(false);
        boolean isPinEqual = EncryptDecrypt.validateText(databasePin, userInputPin);
        if(!isPinEqual) {
            errorIndicator.setVisible(true);
            customerNameField.setText("");
            currentBalanceField.setText("GHc 0.00");
            previousBalanceField.setText("GHc 0.00");
            lastWithdrawalDateLabel.setText("--/--/---- --:--:--");
        }else {
            errorIndicator.setVisible(false);
            sendSmsButton.setDisable(false);
            String customerName = customerData.get(0).toString();
            String current_balance = customerData.get(2).toString();
            String previous_balance = customerData.get((8)).toString();

            customerNameField.setText(customerName);
            currentBalanceField.setText("Ghc ".concat(current_balance));
            previousBalanceField.setText("Ghc ".concat(previous_balance));

            lastWithdrawalDateLabel.setText(transactionData.get("dateTime").toString());
            transactionTypeLabel.setText(transactionData.get("transType").toString());

            NotificationEntity NOTIFY = new NotificationEntity();

            //LOG USER ACTIVITY INTO NOTIFICATION TABLE
            String message = customerName.concat(" with account number").concat(accountNumberField.getText()).concat(" balance was successfully checked");
            NOTIFY.setMessage(message);
            NOTIFY.setLogged_by(userId);
            NOTIFY.setTitle("ACCOUNT BALANCE CHECKED");
            logNotification(NOTIFY);
        }
    }
    @FXML void sendAccountBalanceViaSms() {
        ArrayList<Object> customerData = getCustomerDetailsByAccountNumber(accountNumberField.getValue());
        UserAlerts ALERT = new UserAlerts("SEND SMS", "Do you wish to send customer's account balance details via sms?", "please confirm to send via SMS else CANCEL to abort");
        if (ALERT.confirmationAlert()) {
            String customerName = customerData.get(0).toString();
            String current_balance = customerData.get(2).toString();
            String mobileNumber = customerData.get(4).toString();
            String previous_balance = customerData.get((8)).toString();
            String accountNo = accountNumberField.getValue();

            MessageLogsEntity MSG_OBJ = new MessageLogsEntity();
            SmsAPI SMS_OBJ = new SmsAPI();

            String message = "Hello ".concat(customerName)
                    .concat(", your current balance for account number " + accountNo + " is Ghc" + current_balance)
                    .concat(". Kindly visit our office if you need any clarity.");
            try {
                String responseStatus = SMS_OBJ.sendSms(mobileNumber, message);
                MSG_OBJ.setSent_by(userId);
                MSG_OBJ.setStatus(MessageStatus.getMessageStatusResult(responseStatus).toString());
                MSG_OBJ.setRecipient(mobileNumber);
                MSG_OBJ.setTitle("ACCOUNT BALANCE CHECKED");
                MSG_OBJ.setMessage(message);
                new MessagesModel().logNotificationMessages(MSG_OBJ);
            }catch (Exception ignore){}
        }
    }


}//end of class
