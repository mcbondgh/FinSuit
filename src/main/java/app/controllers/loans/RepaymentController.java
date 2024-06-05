package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.documents.DocumentGenerator;
import app.enums.MessageStatus;
import app.errorLogger.ErrorLogger;
import app.models.loans.LoansModel;
import app.repositories.loans.LoanScheduleEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RepaymentController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    MessageLogsEntity MESSAGE_LOGS;
    NotificationEntity NOTIFY_ENTITY;
    MessageStatus MESSAGE_STATUS;
    SmsAPI SEND_SMS;
    DocumentGenerator DOC_GENERATOR = new DocumentGenerator();
    ErrorLogger errorLogger = new ErrorLogger() ;



    /*******************************************************************************************************************
                                                            FXML FILE EJECTIONS
     *******************************************************************************************************************/
    @FXML
    private ListView<String> listView;
    @FXML
    private Label loansCounter;
    @FXML private Label applicantName, disbursedAmount, applicantNumber,balanceAmount, paidAmount, loanStatus;
    @FXML private MFXButton exportButton, smsButton, terminateLoanBtn;
    @FXML private ImageView applicantProfileImage;

    @FXML private TableView<LoanScheduleEntity> scheduleTable;
    @FXML private TableColumn<LoanScheduleEntity, Long> indexColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> installmentColumn, principalColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> interestColumn;
    @FXML private TableColumn<LoanScheduleEntity, LocalDate> dateColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> penaltyColumn, amountColumn;
    @FXML private TableColumn<LoanScheduleEntity, Label> statusColumn;
    @FXML private TableColumn<LoanScheduleEntity, MFXButton>actionColumn;
    @FXML private TableColumn<LoanScheduleEntity, Double> balanceColumn;

    /*******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    boolean isListEmpty() {
        return listView.getItems().isEmpty();
    }
    boolean isListSelectionEmpty(){
        return listView.getSelectionModel().isEmpty();
    }

    public ObservableList<LoanScheduleEntity> getRepaymentTable() {
        return scheduleTable.getItems();
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

       for(String item : listView.getItems()) {
           updateAllLoanStatus(item);
       }
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
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        String selection = listView.getSelectionModel().getSelectedItem();
        scheduleTable.setItems(getRepaymentSchedule(selection));
    }

    private void dynamicButtons() {
        exportButton.setDisable(isListSelectionEmpty());
        terminateLoanBtn.setDisable(isListSelectionEmpty());
        smsButton.setDisable(isListSelectionEmpty());
        scheduleTable.setDisable(isListEmpty() || isListSelectionEmpty());
    }

    /*******************************************************************************************************************
     KEY EVENT METHODS
     *******************************************************************************************************************/
        public void searchCustomerMethod(KeyEvent keyEvent) {
            try {
                String input = keyEvent.getText();
                ObservableList<String> data = FXCollections.observableArrayList();
                for (String item : getDisbursedLoanNumbers()) {
                    if (item.matches(input)) {
                        data.add(item);
                    }
                }
                listView.setItems(data);
            }catch (NullPointerException e) {populateListView();}
        }//..........END OF METHOD

    public void refreshTableValues() {
             Timer timer = new Timer();
             TimerTask task = new TimerTask() {
                 @Override
                 public void run() {
                     Platform.runLater(()-> {
                         getSelectedLoanNumber();
                         populateListView();
//                         Timeline timeline = new Timeline();
//                         timeline.setDelay(Duration.seconds(counter.get()));
//                         timeline.setOnFinished(event -> {
//                             populateListView();
//                             setScheduleTableProperties();
//                         });
                     });
                     this.cancel();
                 }
             };
             timer.scheduleAtFixedRate(task, 500, 1000);
    }

    /*******************************************************************************************************************
     ACTION EVENT METHODS IMPLEMENTATION
     *******************************************************************************************************************/
    @FXML void getSelectedLoanNumber() {
        dynamicButtons();
        if (!isListSelectionEmpty()) {
            String selection = listView.getSelectionModel().getSelectedItem();
            updateAllLoanStatus(selection);
            Map<String, Object> data = getLoanDetailsByLoanNumber(selection);
            applicantName.setText(data.get("fullname").toString());
            applicantNumber.setText(data.get("mobile_number").toString());
            loanStatus.setText(data.get("loan_status").toString().toUpperCase());
            paidAmount.setText(data.get("total_payment").toString());
            balanceAmount.setText(data.get("balance").toString());
            disbursedAmount.setText(data.get("approved_amount").toString());
            byte[] imageByte = (byte[]) data.get("picture");
            Image image = new Image(new ByteArrayInputStream(imageByte));
            applicantProfileImage.setImage(image);
            //Equality, Opportunity,
            setScheduleTableProperties();
        }
    }//........END OF METHOD

    @FXML private void getClickedLoanValueFromTable() {
        String loanNo = listView.getSelectionModel().getSelectedItem();
        for (LoanScheduleEntity item : scheduleTable.getItems()) {
            item.getPayBtn().setOnAction(event -> {
                LoanPaymentController.setLoanNumber(loanNo);
                LoanPaymentController.setDueDate(item.getPayment_date());
                LoanPaymentController.setPayableAmount(item.getMonthly_installment() - item.getMonthly_payment());
                LoanPaymentController.setPenaltyAmount(item.getPenalty_amount());
                LoanPaymentController.setName(applicantName.getText());
                LoanPaymentController.setMobileNumber(applicantNumber.getText());
                try {
                    Stage loanStage = AppStages.loanPaymentStage();
                    loanStage.show();
                        loanStage.setOnHidden(action -> {
                            refreshTableValues();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }//-------END OF METHOD.

    @FXML void exportButtonClicked() {
        LocalDate datetime = LocalDate.now();
        String docName = applicantName.getText() + "_repayment_"+ datetime;
        String loanNo = listView.getSelectionModel().getSelectedItem();
        int statusCode = DOC_GENERATOR.exportLoanRepaymentSchedule(docName, loanNo, scheduleTable);
        switch (statusCode){
            case 200 -> NOTIFY.successNotification("DATA EXPORT", "Schedule data successfully exported");
            case 404 -> NOTIFY.informationNotification("FAILED EXPORT", "Failed to export schedule data, retry");
            default -> NOTIFY.successNotification("ERROR", "An error occurred while trying to export data, please contact system admin.");
        }
    }
    @FXML void terminateButtonClicked() {
        Map<String, Object> tableDataValues = new HashMap<>();
        double incrementPrincipal = 0;
        double incrementPenalty = 0;
        double incrementInterest = 0;
        double incrementBalance = 0;
        List<Object> dueDates = new ArrayList<>();

        for(LoanScheduleEntity item : scheduleTable.getItems()) {
            boolean matchesUnpaid = item.getStatusLabel().getText().matches("Unpaid");
            boolean matchesPartPayment = item.getStatusLabel().getText().matches("Part Payment");
            if (matchesUnpaid) {
                incrementPrincipal += item.getPrincipal_amount() - item.getMonthly_payment();
                incrementInterest += item.getInterest_amount();
            }
            if (matchesPartPayment) {
                incrementBalance += item.getBalance() - item.getPenalty_amount();
            }
            if(matchesPartPayment || matchesUnpaid) {
                incrementPenalty += item.getPenalty_amount();
            }
        }
            //Parse Data to the TerminateLoanView
            TerminateLoanController.setApplicantName(applicantName.getText());
            String loanNumber = listView.getSelectionModel().getSelectedItem();
            TerminateLoanController.setLoanNumber(loanNumber);
            TerminateLoanController.setMobileNumber(applicantNumber.getText());

            tableDataValues.put("principal", incrementPrincipal);
            tableDataValues.put("interest", incrementInterest);
            tableDataValues.put("penalty", incrementPenalty);
            tableDataValues.put("balance", incrementBalance);

            TerminateLoanController.setAccumulatedTableValue(tableDataValues);
        try{
            AppStages.terminateLoanStage().show();
        }catch (Exception e){
            errorLogger.logMessage(e.getMessage(), this.getClass().getName());
            e.printStackTrace();}
    }






}//END OF CLASS...
