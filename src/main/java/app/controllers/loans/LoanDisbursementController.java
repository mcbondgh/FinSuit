package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.enums.MessageStatus;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.models.transactions.TransactionModel;
import app.repositories.loans.DisbursementEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
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
    }
    void populateTable() {
        paymentTable.getItems().clear();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loanNumberColumn.setCellValueFactory(new PropertyValueFactory<>("loanNumber"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmountValue"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("payBtn"));
        processingFeeColumn.setCellValueFactory(new PropertyValueFactory<>("processingFee"));
//        transactIdColumn.setCellValueFactory(new PropertyValueFactory<>("transIdField"));
        paymentTable.setItems(getUnpaidLoans());
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
        ALERTS = new UserAlerts("SAVE PAYMENT", "Do you wish to save selected loans as disbursed funds?", "please confirm your action to save operation else CANCEL to abort.");
        if (ALERTS.confirmationAlert()) {
            int status = 0;
            double disbursedAmount = 0.0;
            String loanNo = "";
            AtomicReference<String> accountNumber = new AtomicReference<>();
           for (DisbursementEntity item : paymentTable.getItems()) {
                //Check if a pay button is checked or not. If checked then proceed with payment else skip
               if (item.getPayBtn().isSelected()) {
                   String transId = SpecialMethods.getTransactionId(getTotalTransactionCount() + 1);
                   loanNo = item.getLoanNumber();
                   disbursedAmount = item.getLoanAmount();
                   double currentBalance = item.getAccountBalance();

                   double newBalance = currentBalance + disbursedAmount;

                   DISBURSEMENT_ENTITY.setAccountNumber(item.getAccountNumber());
                   DISBURSEMENT_ENTITY.setAccountBalance(newBalance);
                   DISBURSEMENT_ENTITY.setPreviousBalance(currentBalance);

                   TRANS_ENTITY.setUserId(loggedInUserId);
                   TRANS_ENTITY.setEcash_amount(disbursedAmount);
                   TRANS_ENTITY.setTransaction_id(transId);
                   TRANS_ENTITY.setAccount_number(item.getAccountNumber());

                   status = saveDisbursedLoans(loanNo, loggedInUserId);
                   status += updateCustomerAccountData(DISBURSEMENT_ENTITY);
                   TRANS_MODEL.saveDisbursementTransaction(TRANS_ENTITY);

                   try {
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
                    LOGGER.setMessage("Ghc"+disbursedAmount + " has successfully been disbursed to customer with loan no ".concat(loanNo).concat(" by employee no. ").concat(getWorkIdByUserId(loggedInUserId)));
                    LOGGER.setLogged_by(loggedInUserId);
                    logNotification(LOGGER);

                   }catch (Exception e){e.printStackTrace();}
               }
           }
           if (status > 0) {
               NOTIFY.successNotification("OPERATION SAVED", "You have successfully saved selected loan facilities as disbursed funds");
               populateTable();
           }
        }
    }
    @FXML private void selectAllChecked() {
        paymentTable.getItems().forEach(item -> {
            item.getPayBtn().setSelected(!item.getPayBtn().isSelected());
        });
    }

}//end of class...
