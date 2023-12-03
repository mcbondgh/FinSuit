package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.enums.MessageStatus;
import app.models.loans.LoansModel;
import app.repositories.SmsAPIEntity;
import app.repositories.loans.LoanScheduleEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class RepaymentController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    MessageLogsEntity MESSAGE_LOGS;
    NotificationEntity NOTIFY_ENTITY;
    MessageStatus MESSAGE_STATUS;
    SmsAPI SEND_SMS;



    /*******************************************************************************************************************
                                                            FXML FILE EJECTIONS
     *******************************************************************************************************************/
    @FXML
    private ListView<String> listView;
    @FXML
    private Label loansCounter;
    @FXML private Label applicantName, disbursedAmount, applicantNumber,balanceAmount, paidAmount, loanStatus;
    @FXML private MFXButton exportButton, smsButton;

    @FXML private TableView<LoanScheduleEntity> scheduleTable;
    @FXML private TableColumn<LoanScheduleEntity, Long> indexColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> installmentColumn, principalColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> interestColumn;
    @FXML private TableColumn<LoanScheduleEntity, LocalDate> dateColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> penaltyColumn, amountColumn;
    @FXML private TableColumn<LoanScheduleEntity, Label> statusColumn;
    @FXML private TableColumn<LoanScheduleEntity, MFXButton>actionColumn;

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

    void setScheduleTableProperties() {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("schedule_id"));
        installmentColumn.setCellValueFactory(new PropertyValueFactory<>("monthly_installment"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<>("principal_amount"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interest_amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("payment_date"));
        penaltyColumn.setCellValueFactory(new PropertyValueFactory<>("penalty_amount"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("monthly_payment"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("payBtn"));
    }

    /*******************************************************************************************************************
     KEY EVENT METHODS
     *******************************************************************************************************************/
        public void searchCustomerMethod(KeyEvent keyEvent) {
            try {
                String input = keyEvent.getText();
                ObservableList<String> data = FXCollections.observableArrayList();
                for (String item : getDisbursedLoanNumbers()) {
                    if (item.contains(input)) {
                        data.add(item);
                    }
                }
                listView.setItems(data);
            }catch (NullPointerException e) {populateListView();}
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
            setScheduleTableProperties();
            scheduleTable.setItems(getRepaymentSchedule(selection));

        }

    }//........END OF METHOD

    @FXML private void getClickedLoanValueFromTable() {
        String loanNo = listView.getSelectionModel().getSelectedItem();
        for (LoanScheduleEntity item : scheduleTable.getItems()) {
            item.getPayBtn().setOnAction(event -> {
                LoanPaymentController.setLoanNumber(loanNo);
                LoanPaymentController.setDueDate(item.getPayment_date());
                LoanPaymentController.setPayableAmount(item.getMonthly_installment());
                LoanPaymentController.setPenaltyAmount(item.getPenalty_amount());
                LoanPaymentController.setName(applicantName.getText());
                LoanPaymentController.setMobileNumber(applicantNumber.getText());
                try {
                    AppStages.loanPaymentStage().show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }//-------END OF METHOD.








}//END OF CLASS...
