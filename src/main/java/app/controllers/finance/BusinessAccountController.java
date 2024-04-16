package app.controllers.finance;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.encryptDecryp.EncryptDecrypt;
import app.controllers.homepage.AppController;
import app.models.finance.FinanceModel;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.business.BusinessTransactionLogs;
import app.repositories.business.DomesticTransactionLogsEntity;
import app.repositories.notifications.NotificationEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class BusinessAccountController extends FinanceModel implements Initializable {
    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     ********************************************************************************************************************/
    @FXML private MFXProgressBar progressBar;
    @FXML private PasswordField passwordField;
    @FXML private Label accountBalanceIndicator, passwordErrorIndicator, previousBalanceIndicator;
    @FXML private MFXButton checkBalanceBtn, bankTransferButton;

    @FXML private DatePicker transDatePicker;
    @FXML private ComboBox<String> transTypeSelector, bankSelector;
    @FXML private TextField transferAmountField, transactionIdField;
    @FXML private TextField accountNumberField;
    @FXML private TextArea notesField;
    @FXML private TabPane tabPane;
    @FXML private ComboBox<String>domesticTransTypeSelector, cashierSelector;
    @FXML private VBox domesticOptionBox;
    @FXML private TextField domesticAmountField;
    @FXML private MFXButton saveDomesticTransactionBtn;
    @FXML private MFXCheckListView<Object> cashierListView;

    //BUSINESS TRANSACTION TABLE NODES...
    @FXML private TableView<BusinessTransactionLogs> businessTransactionTable;
    @FXML private TableColumn<BusinessTransactionLogs, BigInteger> bIdColumn;
    @FXML private TableColumn<BusinessTransactionLogs, String> transferTypeColumn;
    @FXML private TableColumn<BusinessTransactionLogs, String> bankNameColumn;
    @FXML private TableColumn<BusinessTransactionLogs, Double> transferAmountColumn;
    @FXML private TableColumn<BusinessTransactionLogs, Timestamp> transIdColumn;
    @FXML private TableColumn<BusinessTransactionLogs, Timestamp> transactionDateColumn;

    //CASHIER TRANSACTION TABLE NODES
    @FXML private TableView<DomesticTransactionLogsEntity> cashierTransferTable;
    @FXML private TableColumn<DomesticTransactionLogsEntity, String> cashierTransTypeColumn;
    @FXML private TableColumn<DomesticTransactionLogsEntity, String> cashierNameColumn;
    @FXML private TableColumn<DomesticTransactionLogsEntity, String> amountColumn;
    @FXML private TableColumn<DomesticTransactionLogsEntity, Date> timeColumn;
    @FXML private TableColumn<DomesticTransactionLogsEntity, Integer> cashierIdColumn;

    NumberFormat formatCurrency = NumberFormat.getNumberInstance(Locale.ENGLISH);
    UserNotification showPopup = new UserNotification();
    UserAlerts ALERTS;
    private final int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isTransDateEmpty() {
        return transDatePicker.getValue() == null;
    }
    boolean isTransAmountFieldEmpty() {return transferAmountField.getText().isEmpty();}
    boolean isTransferTypeEmpty() {return transTypeSelector.getValue().isBlank();}
    boolean isBankSelectorEmpty() {return bankSelector.getValue().isEmpty();}
    boolean isAccountNumberEmpty() {return accountNumberField.getText().isEmpty();}
    boolean isTransIdEmpty() {return transactionIdField.getText().isEmpty();}
    boolean isTransferToEmpty() {return domesticTransTypeSelector.getValue().isBlank();}
    boolean isDomesticOptionsEmpty(){return cashierSelector.getValue().isBlank();}
    boolean isDomesticAmountFieldEmpty(){return domesticAmountField.getText().isBlank();}

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkPasswordField();
        SpecialMethods.setBanks(bankSelector);
        SpecialMethods.setTransferTypes(transTypeSelector);
        SpecialMethods.setDomesticTransactionType(domesticTransTypeSelector);
        populateAccountsTransactionsTable();
        populateCashierTransactionTable();
        setTableColor();
    }

    void populateAccountsTransactionsTable() {
        bIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transferTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_type"));
        bankNameColumn.setCellValueFactory( new PropertyValueFactory<>("bank_name"));
        transferAmountColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAmount"));
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
        businessTransactionTable.setItems(getBusinessTransactionLogs());
    }
    void populateCashierTransactionTable() {
        cashierIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cashierTransTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transferTypes"));
        cashierNameColumn.setCellValueFactory(new PropertyValueFactory<>("transferTO"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        cashierTransferTable.setItems(getAllInternalTransfersToCashier());
    }

    //PROGRESS BAR IMPLEMENTATION
    void runProgressTaskIndicator(String accountBalance,String previousBalance) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            double number = 0.0;
            @Override
            public void run() {
                number +=0.3;
                Platform.runLater(()-> {
                    progressBar.setAnimationSpeed(10.0);
                    checkBalanceBtn.setDisable(true);
                    progressBar.setProgress(number);
                    if (number >= 1) {
                        this.cancel();
                        progressBar.setProgress(0);
                        checkBalanceBtn.setDisable(false);
                        accountBalanceIndicator.setText("Ghc ".concat( formatCurrency.format(Double.parseDouble(accountBalance))));
                        previousBalanceIndicator.setText("Ghc ".concat( formatCurrency.format(Double.parseDouble(previousBalance))));
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    void resetAccountFields() {
        transferAmountField.clear();
        accountNumberField.clear();
        transactionIdField.clear();
        notesField.clear();
    }

    void setTableColor() {
        businessTransactionTable.setRowFactory(item -> new TableRow<>(){
            @Override
            protected void updateItem(BusinessTransactionLogs transactionLogs, boolean b) {
                super.updateItem(transactionLogs, b);
               if (!b && transactionLogs !=null) {
                   Date dateCreated = Date.valueOf(transactionLogs.getDate_created().toLocalDateTime().toLocalDate());
                   if (Objects.equals(dateCreated, Date.valueOf(LocalDate.now()))) {
                       setStyle("-fx-background-color:#c6f4cc");
                   }else setStyle("");
               }
            }
        });
    }
//    double computeTellerAccountBalance(String tellerName) {
//        AtomicReference<Double> amountValue = new AtomicReference<>(0.0);
//        getTemporalCashierTableData().forEach((key, value)-> {
//            amountValue.set(key.equals(tellerName) ? Double.parseDouble(value.get(1).toString()) : amountValue.get());
//        });
//        return amountValue.get();
//    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS.
     ********************************************************************************************************************/
    @FXML void validateAmountField(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            transferAmountField.deletePreviousChar();
            domesticAmountField.deletePreviousChar();
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
            checkBalanceBtn.setDisable(passwordField.getText().isBlank());
        });
    }
    @FXML void checkForEmptyFields() {
        bankTransferButton.setDisable(
                isAccountNumberEmpty() || isBankSelectorEmpty() || isTransIdEmpty() || isTransAmountFieldEmpty() ||
                isTransferTypeEmpty() || isTransDateEmpty()
        );
    }

    @FXML void checkForEmptyFieldsInDomesticTransaction() {
        try{
            saveDomesticTransactionBtn.setDisable(isDomesticOptionsEmpty() || isDomesticAmountFieldEmpty());
        }catch (NullPointerException ignore){}
    }

    @FXML private void checkBalanceBtnClicked() {
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
    @FXML private void saveBankTransfer() {
        Map<String, Object> data = getBusinessAccountInformation();
        double accountBal = Double.parseDouble(data.get("accountBalance").toString());
        double previousBal = Double.parseDouble(data.get("previousBalance").toString());

        //get field values
        Date transDate = Date.valueOf(transDatePicker.getValue());
        String transferType = transTypeSelector.getValue();
        String bank = bankSelector.getValue();
        double amount = Double.parseDouble(transferAmountField.getText());
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
                int responseStatus = updateBusinessAccount(businessInfoEntity);
                responseStatus += executeSystemAccountTransaction(transactionLogs, notificationEntity);
                if ( responseStatus == 3) {
                    showPopup.successNotification("TRANSFER SUCCESSFUL", "Nice, you have successfully executed this transaction.");
                    resetAccountFields();
                    populateAccountsTransactionsTable();
                    if (!accountBalanceIndicator.getText().isEmpty()) {
                        checkBalanceBtnClicked();
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
                        //we will process this for later update.
                        //This block is intended to show a pop-up form that contains detailed view about the
                        //selected item in the table
                    }
                }
            }
        }
    }
    //ON ACTION, LOAD THE SELECTION OPTION WITH EITHER BRANCH NAMES OR IN-HOUSE-PERSONNEL NAMES.
    @FXML void domesticTransferTypeOnAction() {
        domesticOptionBox.setDisable(domesticTransTypeSelector.getValue() == null);
        String option = domesticTransTypeSelector.getValue();
        cashierSelector.getItems().clear();
        if(Objects.equals(option, "Cashier")) {
           SpecialMethods.setDomesticTransactionTypes(cashierSelector);
        }else {
//            SpecialMethods.setBranches(domesticOptionSelector);
            new UserAlerts("EMPTY LIST", "You do not have branches in your system").informationAlert();
        }
    }
    @FXML void saveDomesticTransactionButtonOnClick() {
        BusinessInfoEntity businessInfoEntity = new BusinessInfoEntity();
        NotificationEntity notificationEntity = new NotificationEntity();
        Map<String, Object> data = getBusinessAccountInformation();
        DomesticTransactionLogsEntity domesticTransactionLogs = new DomesticTransactionLogsEntity();

        //COLLECT DATA
        String transferType = domesticTransTypeSelector.getValue();
        String transferTo = cashierSelector.getValue();
        double transferAmount = Double.parseDouble(domesticAmountField.getText());

        //Get current business account balance
        double currentBalance = Double.parseDouble(data.get("accountBalance").toString());

        //check if transfer is a valid transaction else reject transaction
        boolean isTransactionValid = currentBalance > transferAmount;
        if(!isTransactionValid) {
            showPopup.errorNotification("INVALID TRANSACTION", "Transfer amount cannot be greater than current account balance. adjust figure");
        } else {
            ALERTS = new UserAlerts("INTERNAL TRANSFER", "Do you wish to perform this transaction? ", "please confirm to EXECUTE, else abort to CANCEL");
            if(ALERTS.confirmationAlert()) {
                double updatedBalance = currentBalance - transferAmount;

                //set parameters for the various entities
                businessInfoEntity.setAccountBalance(updatedBalance);
                businessInfoEntity.setPreviousBalance(currentBalance);

                domesticTransactionLogs.setAmount(transferAmount);
                domesticTransactionLogs.setEnteredBy(currentUserId);
                domesticTransactionLogs.setTransferTO(transferTo);
                domesticTransactionLogs.setTransferTypes(transferType);

                notificationEntity.setLogged_by(currentUserId);
                notificationEntity.setTitle("DOMESTIC TRANSFER");
                notificationEntity.setMessage("Domestic transfer of " + transferAmount + " was successfully transferred to " + transferType + " with the name " + transferTo);
                notificationEntity.setSender_method("INTERNAL OPERATION");

                int responseStatus = updateBusinessAccount(businessInfoEntity);
                responseStatus += saveDomesticTransferLog(domesticTransactionLogs);

                //Get tellers current balance and insert or update based on the method's condition...
                double tellerAmount = SpecialMethods.getCashierCurrentBalance(transferTo) + transferAmount;
                modifyTemporalCashierAccount(transferTo, tellerAmount);

                //check if query executed successfully. if true show success notification else error
                if(responseStatus == 2) {
                    logNotification(notificationEntity);
                    showPopup.successNotification("TRANSFER SUCCESSFUL", "Nice, you have successfully executed this transaction.");
                    domesticAmountField.clear();
                    populateCashierTransactionTable();
                    if (!accountBalanceIndicator.getText().isEmpty()) {
                        checkBalanceBtnClicked();
                    }
                } else {
                    showPopup.errorNotification("TRANSFER FAILED", "Oops, transaction was unsuccessful, please contact system admin for assistance.");
                }
            }
        }
    }



}//end of class...
