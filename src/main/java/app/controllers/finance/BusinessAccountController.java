package app.controllers.finance;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.encryptDecryp.EncryptDecrypt;
import app.controllers.homepage.AppController;
import app.models.finance.FinanceModel;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.business.BusinessTransactionLogs;
import app.repositories.notifications.NotificationEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

public class BusinessAccountController extends FinanceModel implements Initializable {
    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     ********************************************************************************************************************/
    @FXML private MFXProgressBar progressBar;
    @FXML private PasswordField passwordField;
    @FXML private Label accountBalanceIndicator, passwordErrorIndicator, previousBalanceIndicator;
    @FXML private MFXButton submitButton, saveTransactionButton;

    @FXML private DatePicker transDatePicker;
    @FXML private ComboBox<String> transTypeSelector, bankSelector;
    @FXML private TextField transactionAmountField, transactionIdField;
    @FXML private TextField accountNumberField;
    @FXML private TextArea notesField;
    @FXML private TabPane tabPane;

    @FXML private TableView<BusinessTransactionLogs> businessTransactionTable;
    @FXML private TableColumn<BusinessTransactionLogs, BigInteger> businessLogIdColumn;
    @FXML private TableColumn<BusinessTransactionLogs, String> transferTypeColumn;
    @FXML private TableColumn<BusinessTransactionLogs, String> bankNameColumn;
    @FXML private TableColumn<BusinessTransactionLogs, Double> transferAmountColumn;
    @FXML private TableColumn<BusinessTransactionLogs, Timestamp> transIdColumn;
    @FXML private TableColumn<BusinessTransactionLogs, Timestamp> transactionDateColumn;
    NumberFormat formatCurrency = NumberFormat.getNumberInstance(Locale.ENGLISH);
    UserNotification showPopup = new UserNotification();
    UserAlerts ALERTS;
    private int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isTransDateEmpty() {
        return transDatePicker.getValue() == null;
    }
    boolean isTransAmountFieldEmpty() {return transactionAmountField.getText().isEmpty();}
    boolean isTransferTypeEmpty() {return transTypeSelector.getValue().isBlank();}
    boolean isBankSelectorEmpty() {return bankSelector.getValue().isEmpty();}
    boolean isAccountNumberEmpty() {return accountNumberField.getText().isEmpty();}
    boolean isTransIdEmpty() {return transactionIdField.getText().isEmpty();}

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkPasswordField();
        SpecialMethods.setBanks(bankSelector);
        SpecialMethods.setTransferTypes(transTypeSelector);
        populateAccountsTransactionsTable();
    }

    void populateAccountsTransactionsTable() {
        businessLogIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transferTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_type"));
        bankNameColumn.setCellValueFactory( new PropertyValueFactory<>("bank_name"));
        transferAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
        businessTransactionTable.setItems(getBusinessTransactionLogs());
    }

    void runProgressTaskIndicator(String accountBalance,String previousBalance) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            double number = 0.0;
            @Override
            public void run() {
                number +=0.3;
                Platform.runLater(()-> {
                    progressBar.setProgress(number);
                    if (number >= 1) {
                        this.cancel();
                        progressBar.setProgress(0);
                        accountBalanceIndicator.setText("Ghc ".concat( formatCurrency.format(Double.parseDouble(accountBalance))));
                        previousBalanceIndicator.setText("Ghc ".concat( formatCurrency.format(Double.parseDouble(previousBalance))));
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    void resetAccountFields() {
        transactionAmountField.clear();
        accountNumberField.clear();
        transactionIdField.clear();
        notesField.clear();
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS.
     ********************************************************************************************************************/
    @FXML void validateAmountField(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            transactionAmountField.deletePreviousChar() ;
        }
    }
    @FXML void tabItemSelected() {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.isSelected()) {
                tab.setStyle("-fx-background-color:#c6e7ff; -fx-font-weight:bold");
            } else {
                tab.setStyle("-fx-background-color:#fff; -fx-font-weight:normal");
            }
        }
    }
    @FXML void checkPasswordField() {
        passwordField.setOnKeyTyped(keyEvent -> {
            submitButton.setDisable(passwordField.getText().isBlank());
        });
    }
    @FXML void checkForEmptyFields() {
        saveTransactionButton.setDisable(
                isAccountNumberEmpty() || isBankSelectorEmpty() || isTransIdEmpty() || isTransAmountFieldEmpty() ||
                isTransferTypeEmpty() || isTransDateEmpty()
        );
    }
    @FXML private void submitButtonClicked() {
        Map<String, Object> data = getBusinessAccountInformation();
        String inputPassword = passwordField.getText();
        String encryptedPassword = data.get("password").toString();
        String accountBalance = data.get("accountBalance").toString();
        String previousBalance = data.get("previousBalance").toString();
        boolean isPasswordValid = EncryptDecrypt.verifyHashValue(inputPassword, encryptedPassword);

        //RUN THE DATA AND SET PARAMETERS BASED ON THE RETURNED DATA.
        if (isPasswordValid) {
            runProgressTaskIndicator(accountBalance, previousBalance);
            passwordErrorIndicator.setVisible(false);
        }else {
            passwordErrorIndicator.setVisible(true);
           accountBalanceIndicator.setText(null);
           previousBalanceIndicator.setText(null);
        }
    }

    @FXML private void saveTransactionButtonClicked() {
        Map<String, Object> data = getBusinessAccountInformation();
        double accountBal = Double.parseDouble(data.get("accountBalance").toString());
        double previousBal = Double.parseDouble(data.get("previousBalance").toString());

        //get field values
        Date transDate = Date.valueOf(transDatePicker.getValue());
        String transferType = transTypeSelector.getValue();
        String bank = bankSelector.getValue();
        double amount = Double.parseDouble(transactionAmountField.getText());
        String transId = transactionIdField.getText();
        String accountNumber = accountNumberField.getText();
        String notes = notesField.getText();

         /* if the user amount input is greater
        than the current account balance and TransferType is set to 'To Bank' then diny transaction else proceed */
        boolean isInvalidTransaction =(amount > accountBal) && Objects.equals(transferType, "To Bank");
        if (isInvalidTransaction) {
            showPopup.errorNotification("INVALID AMOUNT", "You cannot withdraw more than your current account balance.");
        } else {
            ALERTS = new UserAlerts("INITIALIZE TRANSACTION", "Do you wish to make this transfer?", "Please confirm to execute transfer, else Cancel to abort");
            if (ALERTS.confirmationAlert()) {
                double updatedBalance = transferType.equals("To Bank") ? accountBal - amount : (accountBal + amount);

                //Create an instance of AccountInfo and set the required values to update business_info table
                BusinessInfoEntity businessInfoEntity = new BusinessInfoEntity();
                BusinessTransactionLogs transactionLogs = new BusinessTransactionLogs();
                NotificationEntity notificationEntity = new NotificationEntity();

                businessInfoEntity.setAccountBalance(updatedBalance);
                businessInfoEntity.setPreviousBalance(accountBal);

                transactionLogs.setAccount_number(accountNumber);
                transactionLogs.setAmount(amount);
                transactionLogs.setTransaction_date(transDate);
                transactionLogs.setTransaction_id(transId);
                transactionLogs.setTransaction_type(transferType);
                transactionLogs.setBank_name(bank);
                transactionLogs.setCreated_by(currentUserId);
                transactionLogs.setNotes(notes);

                notificationEntity.setTitle("BUSINESS TRANSACTION");
                notificationEntity.setMessage("Transfer of " + amount + " was success. Transfer type was '" + transferType + "', bank name ".concat(bank) +" with account number " + accountNumber);
                notificationEntity.setSender_method("SYSTEM OPERATION");
                notificationEntity.setLogged_by(currentUserId);

                //call method to execute transaction
                if (executeSystemAccountTransaction(businessInfoEntity, transactionLogs, notificationEntity) == 3) {
                    showPopup.successNotification("TRANSFER SUCCESSFUL", "Nice, you have successfully executed this transaction.");
                    resetAccountFields();
                    populateAccountsTransactionsTable();
                    if (!accountBalanceIndicator.getText().isEmpty()) {
                        submitButtonClicked();
                    }
                } else {
                    showPopup.errorNotification("TRANSFER FAILED", "Transfer was unsuccessful, please report to system admin if problem persists.");
                }
            }
        }
    }// end of method...
    @FXML void getSelectedTableItem(MouseEvent mouseEvent){
        boolean isSelected = businessTransactionTable.getSelectionModel().isEmpty();
        if (!isSelected) {
            if (mouseEvent.getClickCount() == 2) {
                int id = businessTransactionTable.getSelectionModel().getSelectedItem().getId();
                for (BusinessTransactionLogs items: businessTransactionTable.getItems()) {
                    if (id == items.getId()) {
                        showPopup.informationNotification("NOTES", items.getNotes());
                    }
                }
            }
        }
    }

    @FXML void savePersonnelTransactionButtonClicked() {

    }

}//end of class...
