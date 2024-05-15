package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.enums.MessageStatus;
import app.models.finance.FinanceModel;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.models.transactions.TransactionModel;
import app.repositories.loans.DisbursementEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class LoanDisbursementController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    TransactionModel TRANS_MODEL = new TransactionModel();
    TransactionsEntity TRANS_ENTITY = new TransactionsEntity();
    NotificationEntity LOGGER = new NotificationEntity();
    DisbursementEntity DISBURSEMENT_ENTITY = new DisbursementEntity();
    MessagesModel MSG_MODEL = new MessagesModel();
    MessageLogsEntity MSG_ENTITY = new MessageLogsEntity();
    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);
    private String getLoggedInUsername() {return AppController.activeUserPlaceHolder;}

    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     *******************************************************************************************************************/

    @FXML private TableView<DisbursementEntity> paymentTable;
    @FXML private TableColumn<DisbursementEntity, Integer> idColumn;
    @FXML private TableColumn<DisbursementEntity, String> loanNumberColumn;
    @FXML private TableColumn<DisbursementEntity, Double> amountColumn;
    @FXML private TableColumn<DisbursementEntity, Label> statusColumn;
    @FXML private TableColumn<DisbursementEntity, CheckBox> actionColumn;
    @FXML private TableColumn<DisbursementEntity, Double> processingFeeColumn;
//    @FXML private  TableColumn<DisbursementEntity, ComboBox<String>> methodColumn;
//    @FXML private  TableColumn<DisbursementEntity, ComboBox<String>> transactIdColumn;
    @FXML private MFXButton saveButton, clearButton;
    @FXML private Label cashierBalanceLabel;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    private boolean isTableEmpty() {
        return paymentTable.getItems().isEmpty();
    }

    /*******************************************************************************************************************
     *********************************************** OTHER METHODS IMPLEMENTATIONS.
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();
        setCashierBalanceLabel();
    }
    void populateTable() {
        paymentTable.getItems().clear();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loanNumberColumn.setCellValueFactory(new PropertyValueFactory<>("loanNumber"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("disbursementAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("payBtn"));
        processingFeeColumn.setCellValueFactory(new PropertyValueFactory<>("processingFee"));
//        transactIdColumn.setCellValueFactory(new PropertyValueFactory<>("transIdField"));
        paymentTable.setItems(getUnpaidLoans());
    }

    private void setCashierBalanceLabel() {
        double cashierCurrentBalance = SpecialMethods.getCashierCurrentBalance(getLoggedInUsername());
        NumberFormat numberFormat = NumberFormat.getInstance();
        cashierBalanceLabel.setText(numberFormat.format(cashierCurrentBalance));
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     *******************************************************************************************************************/

    @FXML void CheckTableSelection() {
        int checked = 0;
        for (int x = 0; x < paymentTable.getItems().size(); x ++) {
            try {
                if (paymentTable.getItems().get(x).getPayBtn().isSelected()) {
                    checked ++;
                }
            }catch (NullPointerException ignore) {}
        }
        saveButton.setDisable(checked == 0);
    }
    public void setClearButtonOnAction(ActionEvent actionEvent) {
        paymentTable.getItems().forEach(item -> {
            if (item.getPayBtn().isSelected()) {
                item.getPayBtn().setSelected(false);
                saveButton.setDisable(!item.getPayBtn().isSelected());
            }
        });
    }
    @FXML private void saveButtonClicked() {
//        for (DisbursementEntity item : paymentTable.getItems()) {
//            try{
//                if (item.getPayBtn().isSelected() && item.getMethod().getValue().isEmpty()) {
//                    item.getMethod().setStyle(item.getMethod().getValue().isEmpty() ? "-fx-background-color:#ffe1e1" : "-fx-background-color:#eee");
//                    return;
//                }
//                if(!(item.getTransIdField().isDisabled()) && item.getTransIdField().getText().isBlank()) {
//                    ALERTS = new UserAlerts("EMPTY INPUT", "Please provide a transaction Id", "Transaction Id Field cannot be empty");
//                    ALERTS.errorAlert();
//                    return;
//                }
//            }catch (NullPointerException e) {
//                NOTIFY.informationNotification("INVALID PAYMENT METHOD", "Please select a payment method");
//                return;
//            }
//        }

        try{
            //CHECK IF CASHIER HAS ENOUGH BALANCE TO PERFORM TRANSACTION, ELSE REJECT TRANSACTION
            AtomicReference<Double> accumulatedDisbursementAmount = new AtomicReference<>(0.00);
            double cashierCurrentBalance = Double.parseDouble(cashierBalanceLabel.getText().replace(",", ""));
            paymentTable.getItems().forEach(item -> {
                if (item.getPayBtn().isSelected()) {
                    accumulatedDisbursementAmount.updateAndGet(value -> value + item.getDisbursementAmount());
                }
            });
            boolean hasEnoughBalance = cashierCurrentBalance > accumulatedDisbursementAmount.get();
            //*********************************/
            if (hasEnoughBalance) {
                ALERTS = new UserAlerts("SAVE PAYMENT", "Do you wish to disburse loan to customer account?", "please confirm your action to save operation else CANCEL to abort.");
                if (ALERTS.confirmationAlert()) {
                    int status = 0;
                    double disbursedAmount = 0.0;
                    double processingFee = 0.0;
                    String loanNo = "";

                    //CREATE A MAP TO STORE RETURNED RESULT FROM THE DATABASE THAT HOLDS THE operations_account data.
                    Map<String, Object> operationsAccountData = getOperationsAccountDetails();

                    /*CREATE A MAP THAT WOULD BE POPULATED WITH THE REQUIRED DATA TO UPDATE AND INSERT INTO THE
                    operations_account AND operations_transaction_logs
                    */
                    Map<String, Object> operationsMap = new HashMap<>();

                    for (DisbursementEntity item : paymentTable.getItems()) {
                        //Check if a pay button is checked or not. If checked then proceed with payment else skip
                        if (item.getPayBtn().isSelected()) {

                            String transId = SpecialMethods.getTransactionId(getTotalTransactionCount() + 1);
                            loanNo = item.getLoanNumber();
                            disbursedAmount = item.getDisbursementAmount();
                            processingFee = item.getProcessingFee();
                            double operationsAccountBalance = Double.parseDouble(operationsAccountData.get("balance").toString());

                            double currentBalance = item.getAccountBalance();
                            double customerUpdatedAccountBalance = currentBalance + disbursedAmount;
                            double operationsUpdatedAccountBalance = processingFee + operationsAccountBalance;

                            //SET VALUES FOR THE operationsMap
                            operationsMap.put("balance", operationsUpdatedAccountBalance);
                            operationsMap.put("userId", loggedInUserId);
                            operationsMap.putIfAbsent("referenceNumber", loanNo);
                            operationsMap.putIfAbsent("entryType", "Processing Fee");
                            operationsMap.putIfAbsent("amount", processingFee);
                            operationsMap.putIfAbsent("userId", loggedInUserId);

                            DISBURSEMENT_ENTITY.setAccountNumber(item.getAccountNumber());
                            DISBURSEMENT_ENTITY.setAccountBalance(customerUpdatedAccountBalance);
                            DISBURSEMENT_ENTITY.setPreviousBalance(currentBalance);

                            TRANS_ENTITY.setUserId(loggedInUserId);
                            TRANS_ENTITY.setEcash_amount(disbursedAmount);
                            TRANS_ENTITY.setTransaction_id(transId);
                            TRANS_ENTITY.setAccount_number(item.getAccountNumber());

                            status = saveDisbursedLoans(loanNo, loggedInUserId);
                            status += updateCustomerAccountData(DISBURSEMENT_ENTITY);
                            TRANS_MODEL.saveDisbursementTransaction(TRANS_ENTITY, operationsMap);

                                String message = new MessageBuilders().loanDisbursementMessage("Applicant", loanNo, disbursedAmount);
                                String response = new SmsAPI().sendSms(item.getMobileNumber(), message);
                                String msgStatus = MessageStatus.getMessageStatusResult(response).toString();
                                MSG_ENTITY.setSent_by(loggedInUserId);
                                MSG_ENTITY.setStatus(msgStatus);
                                MSG_ENTITY.setRecipient(item.getMobileNumber());
                                MSG_ENTITY.setTitle("DISBURSED FUND");
                                MSG_ENTITY.setMessage(message);
                                MSG_MODEL.logNotificationMessages(MSG_ENTITY);

                                LOGGER.setTitle("DISBURSED FUND");
                                LOGGER.setMessage("Ghc"+disbursedAmount + " has successfully been loaded into customer's account referencing loan no ".concat(loanNo).concat(" by employee no. ").concat(getWorkIdByUserId(loggedInUserId)));
                                LOGGER.setLogged_by(loggedInUserId);
                                logNotification(LOGGER);
                        }
                    }
                    if (status > 0) {
                        //update cashier's account after successful disbursement
                        double cashierBalance = cashierCurrentBalance - accumulatedDisbursementAmount.get();
                        new FinanceModel().modifyTemporalCashierAccountWhenLoaded(getLoggedInUsername(), cashierBalance);
                        Platform.runLater(this::setCashierBalanceLabel);
                        NOTIFY.successNotification("OPERATION SAVED", "You have successfully saved selected loan facilities as disbursed funds");
                        populateTable();
                    }
                }
            } else {
                ALERTS = new UserAlerts("LOW BALANCE", "Sorry, you do not have enough balance to perform deposit", "please load your account to perform this transaction");
                ALERTS.informationAlert();
            }
        }catch (Exception e) {
            e.printStackTrace();
            ALERTS = new UserAlerts("INVALID PROCESS", "You do not have permission to perform loan disbursement.", "access to this operation has been denied.");
            ALERTS.informationAlert();
        }


    }
    @FXML private void selectAllChecked() {
        paymentTable.getItems().forEach(item -> {
            item.getPayBtn().setSelected(!item.getPayBtn().isSelected());
        });
    }

}//end of class...
