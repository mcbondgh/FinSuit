package app.controllers.transactions;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.enums.MessageStatus;
import app.enums.PaymentMethods;
import app.models.finance.FinanceModel;
import app.models.message.MessagesModel;
import app.models.transactions.TransactionModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.action.ActionCheck;

import java.net.URL;
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
    @FXML private ComboBox<PaymentMethods> paymentSelector, gatewaySelector;
    @FXML private TextField collectorsNameField, cashField, transactionIdField, idNumberField;
    @FXML private MFXButton saveButton, searchButton;
    @FXML private CheckBox accountOwnerCheckBox;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isAmountEmpty() {return cashField.getText().isEmpty();}
    boolean isPaymentMethodEmpty() {return paymentSelector.getValue() == null;}
    boolean accountNumberEmpty() {return accountNumberField.getValue().isEmpty();}
    boolean collectorNameEmpty() {return collectorsNameField.getText().isEmpty();}

    boolean eTransactionParameters() {
        return paymentSelector.getValue().equals(PaymentMethods.eCASH)  && transactionIdField.getText().isBlank() && isGatewayEmpty()
                || paymentSelector.getValue().equals(PaymentMethods.BOTH_METHODS)
                && transactionIdField.getText().isBlank() && isGatewayEmpty();
    }
    boolean idNumberEmpty() {return idNumberField.getText().isBlank();}
    boolean isGatewayEmpty() {return gatewaySelector.getValue() == null;}

    @FXML void validateInputFields(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            cashField.deletePreviousChar();
        }
    }
    
    @FXML void checkForEmptyFields() {
        try {
            saveButton.setDisable(
                    accountNumberEmpty() || isAmountEmpty() || isPaymentMethodEmpty() || collectorNameEmpty()
                    || idNumberEmpty() || eTransactionParameters()
            );
            cashField.setDisable(accountStatusLabel.getText().equalsIgnoreCase("closed"));
        } catch (NullPointerException ignore){}
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
        SpecialMethods.setPaymentGateways(gatewaySelector);
        ObservableList<String> listItems = FXCollections.observableArrayList();
        for (CustomerAccountsDataRepository data : fetchCustomersAccountData()) {
            listItems.add(data.getAccount_number());
            listItems.add(data.getLoanNo());
        }
            accountNumberField.setItems(listItems);
    }

    String checkIfUserIsCashier() {
        AtomicReference<String> cashierName = new AtomicReference<>();
        getTemporalCashierTableData().forEach((key, value) -> {
           if (key.equals(getCurrentUserPlaceholder())) {
               cashierName.set(key);
           }
        });
        return cashierName.get();
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION
     ********************************************************************************************************************/

    @FXML void accountOwnerButtonChecked() {
        try {
            if (accountOwnerCheckBox.isSelected()) {
                String var1 = accountNumberField.getValue();
                ArrayList<Object> items = getCustomerDetailsByAccountNumber(var1);
                idNumberField.setText(items.get(9).toString());
                collectorsNameField.setText(items.get(0).toString());
            } else {
                idNumberField.clear();
                collectorsNameField.clear();
            }
        }catch (IndexOutOfBoundsException ex) {
            new UserAlerts("EMPTY SELECTION", "Please select an account to get data").errorAlert();
        }
    }

    @FXML void customerAccountSelected() {
        String var1 = accountNumberField.getValue();
        ArrayList<Object> items = getCustomerDetailsByAccountNumber(var1);
        String status = getCustomerDetailsByAccountNumber(var1).get(6).toString();
        AtomicReference<Double> withdrawalTax = new AtomicReference<>();
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
            gatewaySelector.setDisable(false);
        }else {
            transactionIdField.setDisable(true);
            gatewaySelector.setDisable(true);
            gatewaySelector.setValue(null);
            transactionIdField.setText("null");
        }
    }

    @FXML void saveButtonClicked() {
        if (checkIfUserIsCashier() == null) {
                new UserAlerts("NOT A CASHIER", "You cannot perform transaction as your access to this operation is denied by your role", "you are restricted to access this operation because you are not a cashier.")
                        .warningAlert();
        } else {
            //Check if cashier has closed account for the day. If true then diny transaction else allow transaction.
            boolean isAccountClosed = new DepositController().checkIfCashierAccountIsClosed();
            if(isAccountClosed) {
                new UserNotification().errorNotification("ACCOUNT CLOSED","You cannot perform this transaction because account is closed for the day.");
                return;
            }
            try{
                ArrayList<Object> customerData = getCustomerDetailsByAccountNumber(accountNumberField.getText());
                int userId = getUserIdByName(getCurrentUserPlaceholder());

                double transactionCharge = Double.parseDouble(chargeValue.getText());
                double withdrawalAmount = Double.parseDouble(cashField.getText());
                double currentBalance = Double.parseDouble(customerData.get(2).toString());
                double totalWithdrawalAmount = withdrawalAmount + transactionCharge;
                double balance = currentBalance - totalWithdrawalAmount;
                String mobileNumber = customerData.get(4).toString();

                String transId = SpecialMethods.getTransactionId(getTotalTransactionCount() +1);
                String payMethod = paymentSelector.getValue().toString();
                String payGateway = gatewaySelector.getValue() == null ? "null" : gatewaySelector.getValue().toString();
                AtomicReference<String> accountNumber = new AtomicReference<>();
                fetchCustomersAccountData().forEach(item -> {
                    if (accountNumberField.getValue().equals(item.getLoanNo())) {
                        accountNumber.set(item.getAccount_number());
                    }
                    if (accountNumberField.getValue().equals(item.getAccount_number())) {
                        accountNumber.setRelease(item.getAccount_number());
                    }
                });
                if (balance < 0) {
                    ALERTS = new UserAlerts("INVALID AMOUNT", "INSUFFICIENT BALANCE, REVIEW WITHDRAWAL AMOUNT", "withdrawal amount cannot be greater than current account balance.");
                    ALERTS.errorAlert();
                    cashField.deletePreviousChar();
                } else {
                    ALERTS = new UserAlerts("CASH WITHDRAWAL", "DO YOU WISH TO CONFIRM WITHDRAWAL TRANSACTION", "Please confirm transaction to perform withdrawal else CANCEL to abort");
                    if(ALERTS.confirmationAlert()) {

                        NotificationEntity NOTIFY_ENTITY = new NotificationEntity();
                        MessageLogsEntity MSG_ENTITY = new MessageLogsEntity();
                        SmsAPI SMS = new SmsAPI();

                        //set parameters to update customer_account_data table
                        accountsDataRepository.setPrevious_balance(currentBalance);
                        accountsDataRepository.setAccount_balance(balance);
                        accountsDataRepository.setAccount_number(accountNumber.get());
                        accountsDataRepository.setModified_by(userId);
                        int responseStatus = saveWithdrawal(accountsDataRepository);

                        //set parameters to insert into the transaction_logs table
                        transactions.setAccount_number(accountNumber.get());
                        transactions.setEcash_amount(payMethod.equals("CASH") ? 0.00 : withdrawalAmount);
                        transactions.setCash_amount(payMethod.equals("CASH") ? withdrawalAmount : 0.00);
                        transactions.setTransactionTax(transactionCharge);
                        transactions.setTransaction_type("CASH WITHDRAWAL");
                        transactions.setNationalIdNumber(idNumberField.getText());
                        transactions.setEcash_id(transactionIdField.getText());
                        transactions.setPayment_method(payMethod);
                        transactions.setPayment_gateway(payGateway.isEmpty() ? "null": payGateway);
                        transactions.setTransaction_id(transId);
                        transactions.setTransaction_made_by(collectorsNameField.getText());
                        transactions.setUserId(userId);
                        responseStatus += saveWithdrawalTransaction(transactions);

                        //set parameters to send sms to account holder.
                        try{
                            String message = new MessageBuilders().cashWithdrawalMessage(withdrawalAmount, collectorsNameField.getText(), balance);
                            String msgStatus = MessageStatus.getMessageStatusResult(SMS.sendSms(mobileNumber, message)).toString();
                            NOTIFY_ENTITY.setSender_method("SMS");
                            NOTIFY_ENTITY.setTitle("Cash Withdrawal");
                            NOTIFY_ENTITY.setMessage(message);
                            NOTIFY_ENTITY.setLogged_by(userId);
                            logNotification(NOTIFY_ENTITY);

                            MSG_ENTITY.setMessage(message);
                            MSG_ENTITY.setRecipient(mobileNumber);
                            MSG_ENTITY.setStatus(msgStatus);
                            MSG_ENTITY.setSent_by(userId);
                            MSG_ENTITY.setTitle("Cash Withdrawal");
                            new MessagesModel().logNotificationMessages(MSG_ENTITY);

                        }catch (Exception ignore) {}
                        // get current cashier balance, subtract withdrawal amount and update cashier's balance.
                        double cashierBalance = SpecialMethods.getCashierCurrentBalance(getCurrentUserPlaceholder());
                        new FinanceModel().updateCashierCurrentBalanceAfterTransaction(getCurrentUserPlaceholder(), (cashierBalance - withdrawalAmount));

                        if (responseStatus == 2) {
                            Platform.runLater(()-> {
                                new UserAlerts("SUCCESSFUL WITHDRAWAL", "Nice cash withdrawal was successful")
                                        .informationAlert();
                                cashField.clear();
                                accountNumberField.clear();
                            });
                        }
                    };
                }
            }catch (NumberFormatException ignore){}
        }

    }

}
