package app.controllers.loans;

import app.alerts.UserAlerts;
import app.models.loans.LoansModel;
import app.repositories.notifications.NotificationEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class CancellationController extends LoansModel implements Initializable {

    @FXML
    public MFXButton cancellationButton;
    @FXML public TextArea cancellationField;
    public static String LOAN_NUMBER;
    public static int USER_ID;



    @FXML
    void saveButtonOnAction() {
        NotificationEntity notification = new NotificationEntity();
        UserAlerts ALERTS = new UserAlerts("CANCEL LOAN", "You have requested to officially cancel this loan process. Are you sure you want to proceed?",
                "by confirming, you will terminate the loan process.");
        //                        if(ALERTS.confirmationAlert()) {
//                            int responseStatus = cancelLoanApplication(loanNumber, "terminated", "closed");
//                            if(responseStatus > 0) {
//                                notification.setTitle("LOAN TERMINATED");
//                                notification.setMessage("Loan application number " + loanNumber + " has successfully been cancelled");
//                                notification.setLogged_by(USER_ID);
//                                logNotification(notification);
//                                userNotification.successNotification("LOAN PROCESS TERMINATED", "Nice, you have successfully terminated the loan process.");
//                                populateTable();
//                            } else {
//                                userNotification.errorNotification("TERMINATION FAILED", "Sorry the process to terminate this loan failed, retry");
//                            }
//                        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
