package app.controllers.loans;

import app.alerts.UserAlerts;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.documents.DocumentGenerator;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.repositories.documents.ReceiptsEntity;
import app.repositories.loans.LoansEntity;
import app.repositories.loans.RepaymentEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LoanPaymentController extends LoansModel implements Initializable {

    UserAlerts ALERT;
    SmsAPI SMS = new SmsAPI();
    MessageLogsEntity LOG_MESSAGE = new MessageLogsEntity();
    TransactionsEntity TRANS_ENTITY = new TransactionsEntity();
    NotificationEntity NOTIFY_ENTITY = new NotificationEntity();
    MessageBuilders MESSAGE_BUILDER = new MessageBuilders();
    MessageLogsEntity MESSAGE_ENTITY = new MessageLogsEntity();
    RepaymentEntity REPAYMENT_ENTITY = new RepaymentEntity();
    LoansEntity LOAN_ENTITY = new LoansEntity();
    MessagesModel MESSAGE_MODEL = new MessagesModel();
    DocumentGenerator DOC_GENERATOR = new DocumentGenerator();
    ReceiptsEntity RECEIPT_ENTITY = new ReceiptsEntity();

    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);


    private static String loanNumber;
    private static LocalDate dueDate;
    private static double payableAmount;
    private static double penaltyAmount;
    private static String name, mobileNumber;

    public static String getLoanNumber() {return loanNumber;}
    public static void setLoanNumber(String value){loanNumber = value;}
    public static LocalDate getDueDate(){return dueDate;}
    public static void setDueDate(LocalDate value){dueDate = value;}
    public static double getPayableAmount() {return payableAmount;}
    public static void setPayableAmount(double value){payableAmount = value;}
    public static void setPenaltyAmount(double value){penaltyAmount = value;}
    public static double getPenaltyAmount(){return penaltyAmount;}

    public static String getName() {return name;}

    public static void setName(String name) {LoanPaymentController.name = name;}

    public static String getMobileNumber() {return mobileNumber;}

    public static void setMobileNumber(String mobileNumber) {LoanPaymentController.mobileNumber = mobileNumber;}



    /******************************************************************************************************************
                                                        FXML FILE EJECTIONS
     ******************************************************************************************************************/
    @FXML private Label loanNumberField;
    @FXML private Label dateField, payableField;
    @FXML private ComboBox<String>methodSelector;
    @FXML private CheckBox payableCheckBox, clearPenaltyBtn;
    @FXML private TextField paymentAmountField, penaltyField;
    @FXML private MFXButton collectButton;
    @FXML private Label successIndicator;


    /******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS.
     ******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanNumberField.setText(getLoanNumber());
        dateField.setText(String.valueOf(getDueDate()));
        payableField.setText(String.valueOf(getPayableAmount()));
        penaltyField.setText(String.valueOf(getPenaltyAmount()));
        payableCheckBoxChecked();
        methodSelector.getItems().add("CASH");
        methodSelector.getItems().add("eCASH");
        validateAmountField();

    }
    @FXML void checkForEmptyField() {
        collectButton.setDisable(isAmountFieldEmpty() || isMethodSelectorEmpty());
    }

    void paymentReceipt(){
        String transactionId = SpecialMethods.getTransactionId(getTotalTransactionCount());

        String customerName = "";
        String empId = getEmployeeIdByUsername(AppController.activeUserPlaceHolder);
        String loanNo = loanNumberField.getText();

        for (LoansEntity item : fetchAllLoans()){
            if (loanNo.equals(item.getLoan_no())){
                customerName = item.getCustomerName();
            }
        }
        RECEIPT_ENTITY.setTransactionDate(LocalDateTime.now().toString());
        RECEIPT_ENTITY.setTransactionNumber(transactionId);
        RECEIPT_ENTITY.setPaymentMethod(methodSelector.getValue());//6
        RECEIPT_ENTITY.setTransactionType("Loan Repayment");//3
        RECEIPT_ENTITY.setCustomerName(customerName);//1
        RECEIPT_ENTITY.setTransactionStatus("Successful");//4
        RECEIPT_ENTITY.setAmount(paymentAmountField.getText());//5
        RECEIPT_ENTITY.setCashierName(getEmployeeFullNameByWorkId(empId));//7
        RECEIPT_ENTITY.setAccountNumber(loanNo);//2
        String docName = customerName+ "_repayment_"+ getTotalTransactionCount();
        DOC_GENERATOR.generateLoanRepaymentReceipt(docName, RECEIPT_ENTITY);
    }

    /******************************************************************************************************************
     TRUE OR FALSE STATEMENTS
     ******************************************************************************************************************/
    boolean isCheckBoxChecked() {return payableCheckBox.isSelected();}
    boolean isAmountFieldEmpty(){return paymentAmountField.getText().isBlank();}
    boolean isMethodSelectorEmpty(){return methodSelector.getValue() == null;}

    /******************************************************************************************************************
     ACTION EVENT METHODS IMPLEMENTATION...
     ******************************************************************************************************************/
    void payableCheckBoxChecked() {
        payableCheckBox.setOnAction(checked -> {
            if (isCheckBoxChecked()) {
                paymentAmountField.setText(payableField.getText());
            }else {
                paymentAmountField.setText("0.00");
            }
        });
    }//....end of method

    void validateAmountField() {
        paymentAmountField.setOnKeyTyped(event-> {
            if (!event.getCharacter().matches("[0-9.]")) {
                paymentAmountField.deletePreviousChar();
            }
        });
    }

    @FXML private void writeOffButtonChecked() {
        penaltyField.setDisable(!clearPenaltyBtn.isSelected());
    }

    @FXML void collectButtonClicked() {
        collectButton.setDisable(true);
        double amount = Double.parseDouble(paymentAmountField.getText());
        double penalty = Double.parseDouble(penaltyField.getText());
        Date date = Date.valueOf(dateField.getText());
        String payMethod = methodSelector.getValue();
        String loanNumber = loanNumberField.getText();
        String transactionId = SpecialMethods.getTransactionId(getTotalTransactionCount() +1);
        double applicantTotalLoanRepayment = (getLoanTotalRepaymentAmount(loanNumber) + amount);

        Object[] variables = {getName(), amount, loanNumber, date};
        String message = MESSAGE_BUILDER.loanRepaymentMessage(List.of(variables));

        //SET VALUES FOR REPAYMENT ENTITY FOR DATABASE TABLE
        REPAYMENT_ENTITY.setLoan_no(loanNumber);
        REPAYMENT_ENTITY.setCollected_by(loggedInUserId);
        REPAYMENT_ENTITY.setInstallment_month(date);
        REPAYMENT_ENTITY.setPaid_amount(amount);

        //SET VALUES FOR TRANSACTION LOGS ENTITY
        TRANS_ENTITY.setAccount_number(loanNumber);
        TRANS_ENTITY.setPayment_method(payMethod);
        TRANS_ENTITY.setTransaction_id(transactionId);
        TRANS_ENTITY.setCash_amount(payMethod.equals("CASH") ? amount : 0.00);
        TRANS_ENTITY.setEcash_amount(payMethod.equals("eCASH") ? amount : 0.00);
        TRANS_ENTITY.setTransaction_type("REPAYMENT");
        TRANS_ENTITY.setUserId(loggedInUserId);

        //SET VALUES FOR LOANS
        LOAN_ENTITY.setLoan_no(loanNumber);
        LOAN_ENTITY.setTotal_payment(applicantTotalLoanRepayment);

        //SET VALES FOR NOTIFICATION
        NOTIFY_ENTITY.setTitle("REPAYMENT");
        NOTIFY_ENTITY.setMessage("Loan Repayment successfully made for application number ".concat(loanNumber) + " at " + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
        NOTIFY_ENTITY.setSender_method("SMS");
        NOTIFY_ENTITY.setLogged_by(loggedInUserId);

        if (amount > getPayableAmount()) {
            ALERT = new UserAlerts("INVALID AMOUNT", "Input value cannot be greater than payable", "please review your input for a valid payment amount.");
            ALERT.errorAlert();
        }else {
            ALERT = new UserAlerts("REPAY LOAN", "Do you wish to confirm payment for the specified loan number and date?", "please confirm your action to save else CANCEL to abort.");
            if (ALERT.confirmationAlert()) {
                int counter = 0;
                String smsStatus = "";
                counter = saveLoanPaymentTransaction(LOAN_ENTITY, TRANS_ENTITY, REPAYMENT_ENTITY, penalty);

                //SET VARIABLES FOR SENT MESSAGE.
                MESSAGE_ENTITY.setMessage(message);
                MESSAGE_ENTITY.setTitle("REPAYMENT");
                MESSAGE_ENTITY.setRecipient(mobileNumber);
                MESSAGE_ENTITY.setSent_by(loggedInUserId);
                logNotification(NOTIFY_ENTITY);

                try {
                    smsStatus = SMS.sendSms(mobileNumber, message);
                    MESSAGE_ENTITY.setStatus(smsStatus);
                    MESSAGE_MODEL.logNotificationMessages(MESSAGE_ENTITY);
                }catch (Exception e){
                    smsStatus = "NO INTERNET";
                    MESSAGE_ENTITY.setStatus(smsStatus);
                    MESSAGE_MODEL.logNotificationMessages(MESSAGE_ENTITY);}
                if (counter >= 3){
                    paymentReceipt();
                    paymentAmountField.clear();
                    successIndicator.setVisible(true);
                    Timer time = new Timer();
                    TimerTask task = new TimerTask() {
                        int delay = 2;
                        @Override
                        public void run() {
                            successIndicator.setVisible(true);
                            delay --;
                            if (delay == 0) {
                                Platform.runLater(() -> {
                                    methodSelector.setValue(null);
                                    collectButton.getScene().getWindow().hide();
                                });
                                this.cancel();
                            }
                        }
                    };
                    time.schedule(task, 1000, 1000);
                }
            }
        }





    }//.........end of method




}//END OF CLASS...
