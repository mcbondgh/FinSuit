package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.enums.MessageStatus;
import app.models.loans.LoansModel;
import app.repositories.loans.LoansEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class RepaymentController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    MessageLogsEntity MESSAGE_LOGS;
    NotificationEntity NOTIFY_ENTITY;
    MessageStatus MESSAGE_STATUS;



    /*******************************************************************************************************************
                                                            FXML FILE EJECTIONS
     *******************************************************************************************************************/
    @FXML
    private ListView<String> listView;
    @FXML
    private Label loansCounter;
    @FXML private Label applicantName, disbursedAmount, applicantNumber,balanceAmount, paidAmount, loanStatus;
    @FXML private MFXButton exportButton, smsButton;

    @FXML private TableView<Object> scheduleTable;
    /*******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    boolean isListEmpty() {
        return listView.getItems().isEmpty();
    }
    boolean isListSelectionEmpty(){
        return listView.getSelectionModel().isEmpty();
    }


    /*******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateListView();

    }

    private void populateListView() {
       listView.setItems(getDisbursedLoanNumbers());
       loansCounter.setText(String.valueOf(getTotalDisbursedLoanCount()));
    }


    /*******************************************************************************************************************
     KEY EVENT METHODS
     *******************************************************************************************************************/
        public void searchCustomerMethod(KeyEvent keyEvent) {

        }//..........END OF METHOD


    /*******************************************************************************************************************
     ACTION EVENT METHODS IMPLEMENTATION
     *******************************************************************************************************************/
    @FXML void getSelectedLoanNumber() {
        scheduleTable.setDisable(isListEmpty() || isListSelectionEmpty());
        if (isListSelectionEmpty()) {
            NOTIFY.informationNotification("INVALID SELECTION", "You have not made any selection, please make a selection to get value.");
        }else {
            String selection = listView.getSelectionModel().getSelectedItem();
            Map<String, Object> data = getLoanDetailsByLoanNumber(selection);
            applicantName.setText(data.get("fullname").toString());
            applicantNumber.setText(data.get("mobile_number").toString());
            loanStatus.setText(data.get("loan_status").toString());
            paidAmount.setText(data.get("total_payment").toString());
            balanceAmount.setText(data.get("balance").toString());
            disbursedAmount.setText(data.get("approved_amount").toString());
        }

    }//........END OF METHOD









}//END OF CLASS...
