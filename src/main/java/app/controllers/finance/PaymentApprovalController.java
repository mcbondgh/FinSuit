package app.controllers.finance;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.documents.DocumentGenerator;
import app.enums.MessageStatus;
import app.errorLogger.ErrorLogger;
import app.models.finance.FinanceModel;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.loans.LoanScheduleEntity;
import app.repositories.loans.LoansEntity;
import app.repositories.loans.PendingLoanApprovalEntity;
import app.repositories.loans.ScheduleTableValues;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

public class PaymentApprovalController extends FinanceModel implements Initializable {


    LoansModel LOAN_MODEL = new LoansModel();
    LoansEntity LOANS_OBJ = new LoansEntity();
    LoanScheduleEntity SCHEDULE_OBJ = new LoanScheduleEntity();
    NotificationEntity NOTIFICATION;
    PendingLoanApprovalEntity QUALIFICATION_OBJ = new PendingLoanApprovalEntity();
    UserAlerts ALERT_OBJ;
    UserNotification NOTIFY = new UserNotification();
    MessageBuilders GENERATE_MESSAGE_OBJECT = new MessageBuilders();
    MessagesModel MESSAGE_MODEL_OBJECT = new MessagesModel();
    SmsAPI SMS_OBJECT = new SmsAPI();
    MessageLogsEntity logsEntity = new MessageLogsEntity();
    ErrorLogger errorLogger = new ErrorLogger();

    //------------------------------------------------------------------------------------------------------------------
    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);

    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     *******************************************************************************************************************/
    @FXML private Label loanCounterLabel;
    @FXML private ListView<String> listView;
    @FXML private Pane qualificationPane;
    @FXML private TextField loanAmountField;
    @FXML private ComboBox<Integer> interestRateSelector, loanPeriodSelector, processingRateSelector;
    @FXML private DatePicker datePicker;
    @FXML private TableView<ScheduleTableValues> scheduleTable;
    @FXML private TableColumn<Integer, Integer> noColumn;
    @FXML private TableColumn<ScheduleTableValues, Integer> installmentColumn;
    @FXML private TableColumn<ScheduleTableValues, Integer> principalColumn;
    @FXML private TableColumn<ScheduleTableValues, Integer> interestColumn;
    @FXML private TableColumn<ScheduleTableValues, Integer> paymentDateColumn;
    @FXML private TableColumn<ScheduleTableValues, Integer> balanceColumn;
    @FXML private TableColumn<ScheduleTableValues, Integer> scheduleId;
    @FXML private Hyperlink exportLink;
    @FXML private MFXButton generateScheduleButton, saveButton, rejectButton;
    @FXML private Label fullnameLabel, numberLabel;
    @FXML private Label displayTotalLoanAmount, displayInterestAmount, displayMonthlyInstallmentAmount,
            displayProcessingAmount, displayStartDate, displayEndDate;


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanCounterLabel.setText("( ".concat(String.valueOf(getPendingApprovalLoansCount())).concat(" )"));
        SpecialMethods.setLoanPeriod(loanPeriodSelector);
        SpecialMethods.setRateValue(interestRateSelector);
        SpecialMethods.setRateValue(processingRateSelector);
        accountNumberSelected();
        loadListView();
        setDatePicker();
    }

    private void loadListView() {
        listView.getItems().clear();
        for (PendingLoanApprovalEntity item : LOAN_MODEL.getLoansUnderPendingApproval()) {
            listView.getItems().add(item.getLoan_no());
        }
    }
    
    void populateScheduleTable() {
        noColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        installmentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyInstallment"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<>("principal"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestAmount"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedScheduleDate"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        scheduleId.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));

        String loanNo =listView.getSelectionModel().getSelectedItem();
        scheduleTable.setItems(getLoanScheduleByLoanNo(loanNo));
    }

    void refreshTable() {
        scheduleTable.getItems().clear();
        loadListView();
    }

    void calculateLoanValues() {
        double loanAmount = Double.parseDouble(loanAmountField.getText());
        int interestRate = interestRateSelector.getValue();
        int processingRate = processingRateSelector.getValue();
        int loanPeriod = loanPeriodSelector.getValue();

        double constant =(loanAmount / 100);
        double interestAmount = (constant * interestRate) * loanPeriod;
        double processingAmount =(constant * processingRate );
        double totalAmount = loanAmount + interestAmount;
        double installment = totalAmount / loanPeriod;

        displayTotalLoanAmount.setText(String.valueOf(totalAmount));
        displayInterestAmount.setText(String.valueOf(interestAmount));
        displayProcessingAmount.setText(String.valueOf(processingAmount));
        displayMonthlyInstallmentAmount.setText(String.valueOf(decimalFormat.format(installment)));

        displayStartDate.setText(String.valueOf(datePicker.getValue()));
        displayEndDate.setText(String.valueOf(datePicker.getValue().plusMonths(loanPeriod)));
    }

    //Invoke this method whenever the generateScheduleSheet button is clicked
    private void generateScheduleSheet() {
//        populateScheduleTable();
        ScheduleTableValues entity;

        double loanAmount = Double.parseDouble(loanAmountField.getText());
        int interestRate = interestRateSelector.getValue();
        int loanPeriod = loanPeriodSelector.getValue();

        double constant =(loanAmount / 100);
        double interestAmount = (constant * interestRate) ;
        double totalAmount = loanAmount + ((constant * interestRate) * loanPeriod);
        double installment = totalAmount / loanPeriod;
        double principal = loanAmount / loanPeriod;
        LocalDate scheduleDate = datePicker.getValue();

        scheduleTable.getItems().clear();
        try {
            for (int x = 0; x <= loanPeriod; x++) {
                totalAmount = totalAmount - installment;

                double formattedPrincipal = Double.parseDouble(decimalFormat.format(principal));
                double formattedLoanDifference = Double.parseDouble(decimalFormat.format(totalAmount));

                //Gets the schedule_id for each item at the index of (X)
                long scheduleId = getLoanScheduleByLoanNo(listView.getSelectionModel().getSelectedItem()).get(x).getScheduleId();

                //check for a negative value else just allow the value
                double maxValue = Math.max(formattedLoanDifference, 0.0);
                entity = new ScheduleTableValues(x, scheduleId, formattedPrincipal, interestAmount, Double.parseDouble(decimalFormat.format(installment)), maxValue, scheduleDate.plusMonths(x+1));

                scheduleTable.getItems().add(entity);
            }
        }catch (IndexOutOfBoundsException ignore ) {}

        exportLink.setDisable(false);
    }



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    @FXML
    void checkScheduleTableIfEmpty() {
        boolean empty = scheduleTable.getItems().isEmpty();
        exportLink.setDisable(empty);
        generateScheduleButton.setDisable(empty);
        saveButton.setDisable(empty);
        rejectButton.setDisable(listView.getSelectionModel().isEmpty());
    }




    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION METHOD
     ********************************************************************************************************************/
@FXML void validateLoanAmountInput(KeyEvent keyEvent) {
    if (!(keyEvent.getCharacter().matches("[0-9]") || keyEvent.getCharacter().contains("."))) {
        loanAmountField.deletePreviousChar();
    }
    try {
        calculateLoanValues();
    }catch (NumberFormatException ignore){
        if ("".equals(loanAmountField.getText())) {
            displayTotalLoanAmount.setText("0.0");
        }
    };
}

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION
     ********************************************************************************************************************/

    void setDatePicker() {
        datePicker.setOnAction(event -> {calculateLoanValues();});
    }

    void accountNumberSelected() {
        listView.setOnMouseClicked(event -> {
            try{
                String selectedValue = listView.getSelectionModel().getSelectedItem();
                qualificationPane.setDisable(selectedValue.isEmpty());
                Map<String, String> returnedValue = getApplicantNameAndNumberByLoanId(selectedValue);
                fullnameLabel.setText(returnedValue.get("name"));
                numberLabel.setText(returnedValue.get("number"));

                //check if selection is not empty if false then get associated values pertaining to the selected loan no.
                if (!selectedValue.isEmpty()) {
                    for (PendingLoanApprovalEntity items : LOAN_MODEL.getLoansUnderPendingApproval()){
                        if (Objects.equals(selectedValue, items.getLoan_no())){
                            loanAmountField.setText(String.valueOf(items.getLoan_amount()));
                            interestRateSelector.setValue(items.getInterest_rate());
                            loanPeriodSelector.setValue(items.getLoan_period());
                            processingRateSelector.setValue(items.getProcessing_rate());
                            datePicker.setValue(items.getStart_date());
                        }
                    }
                    calculateLoanValues();
                    populateScheduleTable();
                    generateScheduleButton.setDisable(false);
                    exportLink.setDisable(false);
                    saveButton.setDisable(false);
                    rejectButton.setDisable(false);
                }
            }catch (NullPointerException ignore){}
        });
    }
    public void exportButtonClicked(ActionEvent actionEvent) {
        new Thread(()-> {
            try {
                System.out.println(Thread.currentThread().getName());
                DocumentGenerator documentGenerator = new DocumentGenerator();
                exportLink.setDisable(true);
                documentGenerator.exportScheduleAsPdf(fullnameLabel.getText(), scheduleTable, displayTotalLoanAmount.getText());
                Thread.sleep(2000);
                exportLink.setDisable(false);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()-> {
                NOTIFY.successNotification("EXPORT SCHEDULE", "Nice, applicant schedule data successfully exported.");
            });

        }).start();
    }
    @FXML void generateScheduleButtonClicked() {
        generateScheduleSheet();
    }

    public void saveButtonClicked() {
        saveButton.setDisable(true);
        String selectedLoanNo = listView.getSelectionModel().getSelectedItem();
        int interest = interestRateSelector.getValue();
        int processing = processingRateSelector.getValue();
        int period = loanPeriodSelector.getValue();
        double loanAmount = Double.parseDouble(loanAmountField.getText());
        double disbursedAmount = Double.parseDouble(displayTotalLoanAmount.getText());
        LocalDate startDate = LocalDate.parse(displayStartDate.getText());
        Date endDate = Date.valueOf(displayEndDate.getText());

        ALERT_OBJ = new UserAlerts("APPROVE LOAN PAYMENT", "Do you wish to approve disbursement of " + disbursedAmount +" for loan no. " + selectedLoanNo + "?",
                "please confirm to approve disbursement process else CANCEL to abort.");
        if (ALERT_OBJ.confirmationAlert()) {
            new Thread(() -> {
                CustomerAccountsDataRepository accountRepository = new CustomerAccountsDataRepository();
                //CREATE ACCOUNT FOR LOAN CLIENT
                long customerCount = totalCustomersCount();
                accountRepository.setCustomer_id(customerCount);
                accountRepository.setAccount_number(SpecialMethods.generateAccountNumber(customerCount + 1));
                accountRepository.setAccount_type("Loan Servicing");
                accountRepository.setModified_by(loggedInUserId);
                createAccount(accountRepository);

                //SET LOAN ENTITY VALUES
                LOANS_OBJ.setLoan_no(selectedLoanNo);
                LOANS_OBJ.setApproved_amount(loanAmount);
                LOANS_OBJ.setTotal_loan_amount(disbursedAmount);
                LOANS_OBJ.setApproved_by(loggedInUserId);

                //SET QUALIFICATION ENTITY VALUES
                QUALIFICATION_OBJ.setLoan_amount(loanAmount);
                QUALIFICATION_OBJ.setInterest_rate(interest);
                QUALIFICATION_OBJ.setProcessing_rate(processing);
                QUALIFICATION_OBJ.setLoan_period(period);
                QUALIFICATION_OBJ.setStart_date(startDate);
                QUALIFICATION_OBJ.setEnd_date(endDate.toLocalDate());
                QUALIFICATION_OBJ.setLoan_no(selectedLoanNo);

                //SET NOTIFICATIONS ENTITY VALUES;
                NOTIFICATION = new NotificationEntity();
                NOTIFICATION.setTitle("Disbursement Approval");
                NOTIFICATION.setSender_method("UPDATE ONLY");
                NOTIFICATION.setMessage("Disbursement of Ghc" + disbursedAmount + " has successfully been approved for loan number " + selectedLoanNo + " while awaiting payment");
                NOTIFICATION.setLogged_by(loggedInUserId);

                String message = GENERATE_MESSAGE_OBJECT.loanDisbursementMessage(fullnameLabel.getText(), selectedLoanNo, disbursedAmount);
                logNotification(NOTIFICATION);
               scheduleTable.getItems().forEach(item ->{
                    LOAN_MODEL.updateLoanSchedule(item.getMonthlyInstallment(), item.getPrincipal(), item.getInterestAmount(), item .getScheduleDate(),item.getBalance(), loggedInUserId, item.getScheduleId());
                });
                try {
                    String response = SMS_OBJECT.sendSms(numberLabel.getText(), message);
                    String status = MessageStatus.getMessageStatusResult(response).toString();
                    logsEntity.setRecipient(numberLabel.getText());
                    logsEntity.setSent_by(loggedInUserId);
                    logsEntity.setStatus(status);
                    logsEntity.setTitle("DISBURSEMENT APPROVAL");
                    logsEntity.setMessage(message);
                    MESSAGE_MODEL_OBJECT.logNotificationMessages(logsEntity);
                    int result = LOAN_MODEL.approveLoanForDisbursement(QUALIFICATION_OBJ, LOANS_OBJ);
                    Platform.runLater(this::loadListView);
                    scheduleTable.getItems().clear();
                    if( result > 0) {
                        Platform.runLater(() -> {
                            NOTIFY.successNotification("DISBURSEMENT APPROVAL", "Nice, selected loan number has successfully been approved for payment.");
                        });
                    }
                } catch (IOException ignore) {}

            }).start();
        }
    }
    public void rejectButtonClicked() {
        UserAlerts ALERTS = new UserAlerts("REJECT LOAN", "Do you wish to reject this loan application, please confirm or reject action?",
                "by rejecting loan, you agree to disapprove the requested loan");
        UserNotification popUp = new UserNotification();
        NOTIFICATION = new NotificationEntity();

        var loanNumber = listView.getSelectionModel().getSelectedItem();
        NOTIFICATION.setMessage("Loan application with number " + loanNumber + " was rejected at the approval stage.");
        NOTIFICATION.setTitle("LOAN APPROVAL REJECTED");
        NOTIFICATION.setLogged_by(loggedInUserId);
        NOTIFICATION.setSender_method("INTERNAL OPERATION");

        if (ALERTS.confirmationAlert()) {
            Map<String, Object> data = new HashMap<>();
            data.put("loanNumber", loanNumber);
            data.put("applicationStatus", "rejected");
            data.put("loanStatus", "rejected");
            int responseStatus = new LoansModel().cancelLoanApplication(data);
            if (responseStatus > 0) {
                Platform.runLater(this::refreshTable);
                logNotification(NOTIFICATION);
            } else {
                new UserNotification().errorNotification("REJECTION FAILED", "Application failed to reject loan process");
            }
        }
    }


}//end of class...
