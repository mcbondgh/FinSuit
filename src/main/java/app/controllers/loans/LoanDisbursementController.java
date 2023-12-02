package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.loans.LoansModel;
import app.models.transactions.TransactionModel;
import app.repositories.loans.DisbursementEntity;
import app.repositories.notifications.NotificationEntity;
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

public class LoanDisbursementController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    TransactionModel TRANS_MODEL = new TransactionModel();
    TransactionsEntity TRANS_ENTITY = new TransactionsEntity();
    NotificationEntity LOGGER = new NotificationEntity();
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
    @FXML private  TableColumn<DisbursementEntity, ComboBox<String>> methodColumn;
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
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("payBtn"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("method"));
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
        for (DisbursementEntity item : paymentTable.getItems()) {
            try{
                if (item.getPayBtn().isSelected() && item.getMethod().getValue().isEmpty()) {
                    item.getMethod().setStyle(item.getMethod().getValue().isEmpty() ? "-fx-background-color:#ffe1e1" : "-fx-background-color:#eee");
                    return;
                }
            }catch (NullPointerException e) {
                NOTIFY.informationNotification("INVALID PAYMENT METHOD", "Please select a payment method");
                return;
            }
        }

        ALERTS = new UserAlerts("SAVE PAYMENT", "Do you wish to save selected loans as disbursed funds?", "please confirm your action to save operation else CANCEL to abort.");
        if (ALERTS.confirmationAlert()) {
            int status = 0;
            double disbursedAmount = 0.0;
            String loanNo = "";
           for (DisbursementEntity item : paymentTable.getItems()) {
               if (item.getPayBtn().isSelected()) {
                   String transId = SpecialMethods.getTransactionId(getTotalTransactionCount() + 1);
                   status = saveDisbursedLoans(item.getLoanNumber(), loggedInUserId);
                       disbursedAmount = item.getLoanAmount();
                       loanNo = item.getLoanNumber();
                       String method = item.getMethod().getValue();

                       TRANS_ENTITY.setUserId(loggedInUserId);
                       TRANS_ENTITY.setAccount_number(loanNo);
                       TRANS_ENTITY.setTransaction_id(transId);
                       TRANS_ENTITY.setPayment_method(method);
                       TRANS_ENTITY.setEcash_amount(method.equals("eCASH") ? disbursedAmount: 0.00);
                       TRANS_ENTITY.setCash_amount(method.equals("CASH")? disbursedAmount : 0.00);
                       TRANS_MODEL.saveDisbursementTransaction(TRANS_ENTITY);
               }
           }
           LOGGER.setTitle("DISBURSED FUND");
           LOGGER.setMessage("Ghc"+disbursedAmount + " has successfully been paid to customer with loan no ".concat(loanNo).concat(" by employee no. ").concat(getWorkIdByUserId(loggedInUserId)));
           LOGGER.setLogged_by(loggedInUserId);
           logNotification(LOGGER);
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
