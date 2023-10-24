package app.controllers.transactions;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageTemplates;
import app.documents.DocumentGenerator;
import app.enums.PaymentMethods;
import app.enums.TransactionTypes;
import app.models.transactions.TransactionModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.documents.ReceiptsEntity;
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

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DepositController extends TransactionModel implements Initializable {
    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    private static String currentUserPlaceholder;
    TransactionsEntity transactions = new TransactionsEntity();
    CustomerAccountsDataRepository accountsDataRepository = new CustomerAccountsDataRepository();
    SmsAPI SMS_GATEWAY = new SmsAPI();
    MessageTemplates MESSAGE_OBJECT = new MessageTemplates();

    //-----------------------------------------------------------------------------------------------------------------

    final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private static int CUSTOMER_ID;
    private String TRANSACTION_ID = "";
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);



    public static String getCurrentUserPlaceholder() {
        return currentUserPlaceholder;
    }
    public static void setCurrentUserPlaceholder(String placeholder) {
        currentUserPlaceholder = placeholder;
    }
    @FXML
    private MFXFilterComboBox<String>accountNumberField;
    @FXML private Label accountHolderName, totalCashLabel, accountNumberHolder;
    @FXML private ComboBox<PaymentMethods> paymentSelector, gatewaySelector;
    @FXML private TextField cashField, eCashField, transactionIdField, depositorNameField, depositorIdField;
    @FXML private MFXButton saveButton;
    private String MOBILE_NUMBER, EMAIL_ADDRESS;






    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    boolean isAmountEmpty() {return cashField.getText().isEmpty();}
    boolean isPaymentMethodEmpty() {return paymentSelector.getValue() == null;}
    boolean isGatewayEmpty() {return gatewaySelector.getValue() == null;}
    boolean eCashEmpty() {return eCashField.getText().isEmpty();}
    boolean transactionIdEmpty() {return transactionIdField.getText().isEmpty();}
    boolean accountNumberEmpty() {return accountNumberField.getValue().isEmpty();}
    boolean depositorNameEmpty() {return depositorNameField.getText().isEmpty();}
    boolean depositorIdEmpty() {return depositorIdField.getText().isEmpty();}


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateFields();
        TRANSACTION_ID = SpecialMethods.getTransactionId(getTotalTransactionCount() + 1);
    }

    private void populateFields() {

        setCurrentUserPlaceholder(AppController.activeUserPlaceHolder);

        accountNumberField.setResetOnPopupHidden(true);
        accountNumberField.getItems().clear();
        for (CustomerAccountsDataRepository data : fetchCustomersAccountData()) {
            accountNumberField.getItems().add(data.getAccount_number());
        }
        for (CustomersDataRepository data : fetchCustomersData()) {
            accountNumberField.getItems().add(data.getMobile_number());
        }
        SpecialMethods.setPaymentMethods(paymentSelector);
        SpecialMethods.setPaymentGateways(gatewaySelector);
    }

    void clearFields() {
        accountNumberField.setValue(null);
        accountHolderName.setText("FIN-SUIT GHANA");
        paymentSelector.setValue(null);
        gatewaySelector.setValue(null);
        cashField.clear();
        eCashField.clear();
        totalCashLabel.setText("0.00");
        transactionIdField.clear();
        depositorNameField.clear();
        accountNumberHolder.setText("--------------");
        depositorIdField.clear();
    }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION
     ********************************************************************************************************************/
    @FXML void validateInputFields(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            cashField.deletePreviousChar();
            eCashField.deletePreviousChar();
        }
        try{
            double value = Double.parseDouble(cashField.getText()) + Double.parseDouble(eCashField.getText());
            totalCashLabel.setText(String.valueOf(value));
        }catch (NumberFormatException ignore){}
    }
    @FXML void checkForEmptyFields() {
        try{
            saveButton.setDisable(isAmountEmpty() || accountNumberEmpty() || isPaymentMethodEmpty() || depositorNameEmpty() || depositorIdEmpty());
        }catch (NumberFormatException ignore){}
    }



    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION
     ********************************************************************************************************************/
    @FXML void setOnAccountNumberSelected() {
        String var1 = accountNumberField.getValue();
        ArrayList<Object> items = getCustomerFullNameByAccountNumber(var1);

        accountHolderName.setText(items.get(0).toString());
        accountNumberHolder.setText(items.get(3).toString());
        CUSTOMER_ID = (int) items.get(1);
        MOBILE_NUMBER = items.get(4).toString();
        EMAIL_ADDRESS = items.get(5).toString();
    }
    @FXML void setSelectedPaymentMethod() {
        try{
            PaymentMethods value = paymentSelector.getValue();
            gatewaySelector.setDisable(value.equals(PaymentMethods.CASH));
            cashField.setDisable(value.equals(PaymentMethods.eCASH));
            cashField.setText(value.equals(PaymentMethods.eCASH) ? "0.00": cashField.getText());
            gatewaySelector.setValue(null);
            eCashField.setText("0.00");
            transactionIdField.setText("Unspecified");
        }catch (Exception ignored){}
    }
    @FXML void setGatewaySelectorOnAction() {
        try {
            boolean value = gatewaySelector.getValue().equals(PaymentMethods.BANK_TRANSFER)
                            || gatewaySelector.getValue().equals(PaymentMethods.AIRTELTIGO)
                            || gatewaySelector.getValue().equals(PaymentMethods.MOMO)
                            || gatewaySelector.getValue().equals(PaymentMethods.VODA_CASH);
            eCashField.setDisable(!value);
            transactionIdField.setDisable(!value);
        }catch (NullPointerException ignore){}
    }
    @FXML void clearButtonClicked() {
        gatewaySelector.setValue(null);
        eCashField.setDisable(isGatewayEmpty());
        transactionIdField.setDisable(isGatewayEmpty());
        eCashField.setText("0.00");
        transactionIdField.setText("Unspecified");
        totalCashLabel.setText(decimalFormat.format(cashField.getText()));

    }

    @FXML void saveButtonClicked() {
        try{
            int userId = getUserIdByName(getCurrentUserPlaceholder());
            String clientName = accountHolderName.getText();
            String accountNumber = accountNumberHolder.getText();
            String paymentMethod = PaymentMethods.convertPayMethod(paymentSelector.getValue());
            String gateway = PaymentMethods.convertPayMethod(gatewaySelector.getValue());
            String transactionType = TransactionTypes.convertType(TransactionTypes.CASH_DEPOSIT);
            String eCashId = transactionIdField.getText();
            double amount = Double.parseDouble(cashField.getText());
            double eCash = Double.parseDouble(eCashField.getText());
            String depositorName = depositorNameField.getText();
            String depositorIdNumber = depositorIdField.getText();
            double totalAmount = Double.parseDouble(totalCashLabel.getText());

            //RECEIPT VARIABLES...
            DocumentGenerator documentGenerator = new DocumentGenerator();
            ReceiptsEntity receiptsEntity = new ReceiptsEntity();
            String work_id = getEmployeeIdByUsername(getCurrentUserPlaceholder());

            String pdfName = clientName + "-" + LocalDate.now() + ".pdf";
            String directoryPath = documentGenerator.generateFolder(pdfName);

            String cashierName = getEmployeeFullNameByWorkId(work_id);
            String transactionDate = LocalDateTime.now().format(formatter);

            // get the user's current account balance by their account number...
            double currentBalance = 0;
            for (CustomerAccountsDataRepository data : fetchCustomersAccountData()) {
                if (data.getAccount_number().equals(accountNumber)) {
                    currentBalance = data.getAccount_balance();
                }
            }

            //SET VALUES FOR accountRepository to update current customer's account balance details..
            double newAccountBalance = totalAmount + currentBalance;
            accountsDataRepository.setAccount_balance(newAccountBalance);
            accountsDataRepository.setPrevious_balance(currentBalance);
            accountsDataRepository.setModified_by(userId);
            accountsDataRepository.setAccount_number(accountNumber);

            //SET VALUES FOR transactionEntity to insert into the transaction_logs table...
            transactions.setAccount_number(accountNumber);
            transactions.setTransaction_id(TRANSACTION_ID);
            transactions.setTransaction_type(transactionType);
            transactions.setPayment_method(paymentMethod);
            transactions.setPayment_gateway(gateway);
            transactions.setCash_amount(amount);
            transactions.setEcash_amount(eCash);
            transactions.setEcash_id(eCashId);
            transactions.setTransaction_made_by(depositorName);
            transactions.setNationalIdNumber(depositorIdNumber);
            transactions.setUserId(userId);

            //SET VALUES FOR receiptEntity to create receipt file..
            receiptsEntity.setCustomerName(clientName);
            receiptsEntity.setAccountNumber(accountNumber);
            receiptsEntity.setTransactionType("Cash Deposit");
            receiptsEntity.setPaymentMethod(paymentMethod);
            receiptsEntity.setAmount(String.valueOf(totalAmount));
            receiptsEntity.setDepositorName(depositorName);
            receiptsEntity.setDepositorIdNumber(depositorIdNumber);
            receiptsEntity.setTransactionDate(transactionDate);
            receiptsEntity.setTransactionStatus("SUCCESSFUL");
            receiptsEntity.setCashierName(cashierName);
            receiptsEntity.setTransactionNumber(TRANSACTION_ID);

            ALERTS = new UserAlerts("SAVE TRANSACTION", "Do you wish to save current deposit transaction?",
                    "please confirm your action to save transaction else CANCEL to abort.");
            if(ALERTS.confirmationAlert()) {
                if(saveDepositTransaction(accountsDataRepository, transactions) > 1) {
                    documentGenerator.generateDepositReceipt(directoryPath, receiptsEntity);
                    NOTIFY.successNotification("TRANSACTION SUCCESSFUL", "Customer account number " + accountNumber + " has been credited with a deposit of Ghc" + totalAmount);

                    String message = MESSAGE_OBJECT.cashDepositMessage(clientName, String.valueOf(amount), accountNumber, depositorName, TRANSACTION_ID, newAccountBalance);
                    String status = SMS_GATEWAY.sendSms(MOBILE_NUMBER, message);
                    System.out.println(status);
                    clearFields();
                }
            }
        }catch (NumberFormatException ignore) {
            eCashField.setText("0.00");
            transactionIdField.setText("Unspecified");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}//end of class..
