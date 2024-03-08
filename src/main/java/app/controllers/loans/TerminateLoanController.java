package app.controllers.loans;

import app.APITesting;
import app.alerts.UserAlerts;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.controllers.messages.MessageBuilders;
import app.enums.MessageStatus;
import app.models.loans.LoansModel;
import app.models.message.MessagesModel;
import app.models.transactions.TransactionModel;
import app.repositories.loans.*;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.operations.MessageOperationsEntity;
import io.github.palexdev.materialfx.collections.ObservableStack;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.*;

public class TerminateLoanController extends TransactionModel implements Initializable {

    /*******************************************************************************************************************
                        FXML FILE EJECTION
     ******************************************************************************************************************/
    @FXML
    private Label principalLabel, interestLabel, penaltyLabel, nameLabel, loanNumberLabel;
    @FXML private Label balanceLabel, totalPayableAmountLabel;
    @FXML private TextField interestWeaverField, penaltyWeaverField, balanceWeaverField;
    @FXML private MFXButton terminateButton;
    @FXML private TextArea reasonField;
    private static String applicantName, mobileNumber, loanNumber;
    private static Map<String, Object> mappedValues = new HashMap<>();

    /*******************************************************************************************************************
    // CREATE GETTERS AND SETTERS FOR ALL PRIVATE STATIC FIELDS.
     ******************************************************************************************************************/
    public static String getApplicantName() {return applicantName;}
    public static String getLoanNumber(){return  loanNumber;}
    public static String getMobileNumber(){return mobileNumber;}
    public static void setAccumulatedTableValue(Map<String, Object> values) {
        mappedValues = values;
    }

    public static Map<String, Object> getAccumulatedTableValues() {return mappedValues;}

    public static void setApplicantName(String value) {
        applicantName = value;
    }
    public static void setLoanNumber(String value) { loanNumber = value;}
    public static void setMobileNumber(String value) { mobileNumber = value;}
    double principalAmount = Double.parseDouble(getAccumulatedTableValues().get("principal").toString());
    double interestAmount = Double.parseDouble(getAccumulatedTableValues().get("interest").toString());
    double  penaltyAmount = Double.parseDouble(getAccumulatedTableValues().get("penalty").toString());
    double balanceAmount = Double.parseDouble(getAccumulatedTableValues().get("balance").toString());

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        loanNumberLabel.setText(getLoanNumber());
        nameLabel.setText(getApplicantName());

        principalLabel.setText(String.valueOf(principalAmount));
        interestLabel.setText(String.valueOf(interestAmount));
        penaltyLabel.setText(String.valueOf(penaltyAmount));
        balanceLabel.setText(String.valueOf(balanceAmount));
        double payable = principalAmount + balanceAmount + interestAmount + penaltyAmount;
        totalPayableAmountLabel.setText(String.valueOf(payable));

//        balanceWeaverField.setText(String.valueOf(0.00));
        interestWeaverField.setText(String.valueOf(0.00));
        penaltyWeaverField.setText(String.valueOf(0.00));

    }
    /*******************************************************************************************************************
     INPUT FIELDS VALIDATIONS
     ******************************************************************************************************************/
    @FXML void validateInterestInput(KeyEvent event) {
        Platform.runLater(()-> {
            try{
                if (!event.getCharacter().matches("[0-9.]")){
                    interestWeaverField.deletePreviousChar();
                }
                    double inputValue = Double.parseDouble(interestWeaverField.getText());
                    double interestResult = interestAmount - inputValue;
                    interestLabel.setText(Double.toString(interestResult));
                    double payable = Double.parseDouble(principalLabel.getText()) +  Double.parseDouble(interestLabel.getText()) + Double.parseDouble(balanceLabel.getText());
                    totalPayableAmountLabel.setText(String.valueOf(payable));
            }catch (Exception e){
                interestLabel.setText(getAccumulatedTableValues().get("interest").toString());
                totalPayableAmountLabel.setText(String.valueOf(principalAmount + interestAmount + penaltyAmount + balanceAmount));
            }
        });
    }
    @FXML void validatePenaltyInput(KeyEvent event) {
        try {
            double penalty = Double.parseDouble(getAccumulatedTableValues().get("penalty").toString());
            if (!event.getCharacter().matches("[0-9.]")) {
                penaltyWeaverField.deletePreviousChar();
            }
                double inputValue = Double.parseDouble(penaltyWeaverField.getText());
                penaltyLabel.setText(String.valueOf(penalty - inputValue));
                double payable = Double.parseDouble(principalLabel.getText()) +  Double.parseDouble(penaltyLabel.getText()) + Double.parseDouble(interestLabel.getText()) + Double.parseDouble(balanceLabel.getText());
                totalPayableAmountLabel.setText(String.valueOf(payable));
        }catch (NumberFormatException e){
           penaltyLabel.setText(getAccumulatedTableValues().get("penalty").toString());
        }
    }

    /*******************************************************************************************************************
     IMPLEMENTATION OF OTHER METHODS.
     ******************************************************************************************************************/

    /*******************************************************************************************************************
     IMPLEMENTATION OF ACTION EVENT METHODS
     ******************************************************************************************************************/
    @FXML private void terminateButtonClicked() {
       boolean isInvalid = Double.parseDouble(interestLabel.getText()) < 0.00 || Double.parseDouble(penaltyLabel.getText()) < 0.00;
       if(isInvalid) {
           new UserAlerts("INVALID VALUE", "Interest or Penalty amount cannot be a negative value.", "Please check your inputs").errorAlert();
       } else if(reasonField.getText().isBlank()) {
           new UserAlerts("INVALID REASON", "Please provide a reason for terminating this loan", "this field is required.").errorAlert();
       } else {
         UserAlerts ALERT = new UserAlerts("TERMINATE LOAN FACILITY", "Do you wish to proceed with the termination of this loan facility?",
                   "please confirm to EXECUTE else cancel to ABORT");
         if (ALERT.confirmationAlert()) {
             int userId = getUserIdByName(AppController.activeUserPlaceHolder);
             NotificationEntity notification = new NotificationEntity();
             LoansEntity loansEntity = new LoansEntity();
             RepaymentEntity repaymentEntity = new RepaymentEntity();

             String reasonText = reasonField.getText();
             double payable = Double.parseDouble(totalPayableAmountLabel.getText());
             String loanNo = loanNumberLabel.getText();

             try {

                 double write_offs = Double.parseDouble(interestWeaverField.getText()) + Double.parseDouble(penaltyWeaverField.getText());
                 String message = new MessageBuilders().loanTerminationMessage(nameLabel.getText(), loanNo);
                 String messageStatus = new SmsAPI().sendSms(getMobileNumber(), message);
                 String statusCode = MessageStatus.getMessageStatusResult(messageStatus).toString();
                 String title = "LOAN TERMINATION";

                 double totalPaidAmount = getLoanTotalRepaymentAmount(loanNo) + payable;

                 //SET ENTITY VARIABLES
                 loansEntity.setLoan_no(loanNo);
                 loansEntity.setTermination_purpose(reasonText);
                 loansEntity.setTotal_payment(totalPaidAmount);
                 loansEntity.setUpdated_by(userId);

                 repaymentEntity.setWrite_offs(write_offs);
                 repaymentEntity.setLoan_no(loanNo);
                 repaymentEntity.setCollected_by(userId);
                 repaymentEntity.setPaid_amount(payable);

                 MessageLogsEntity messageLogsEntity = new MessageLogsEntity();
                 messageLogsEntity.setMessage(message);
                 messageLogsEntity.setRecipient(getMobileNumber());
                 messageLogsEntity.setTitle(title);
                 messageLogsEntity.setStatus(statusCode);
                 messageLogsEntity.setSent_by(userId);

                 notification.setTitle(title);
                 notification.setLogged_by(userId);
                 notification.setMessage("Loan termination was success for customer with name " + nameLabel.getText() + " and loan number " + loanNo );

                 //invoke Termination Method to perform save operation to database.
                 int queryStatus = new LoansModel().saveLoanTermination(loansEntity, repaymentEntity);
                 if(queryStatus == 2) {
                     logNotification(notification);
                     new MessagesModel().logNotificationMessages(messageLogsEntity);
                     new UserAlerts("LOAN TERMINATION", "Nice, loan termination process successfully executed", "You have successfully terminated the loan facility for this account.")
                             .successAlert();
                         terminateButton.getScene().getWindow().hide();
                  } else {
                     new UserAlerts("TERMINATION FAILED", "Loan termination process failed to execute successfully", "Retry this process and if not successful, contact system admin").errorAlert();
                 }

             }catch (Exception e) {
                 interestWeaverField.setText("0.00");
                 penaltyWeaverField.setText("0.00");
             }
         }

       }




    }


}//END OF CLASS
