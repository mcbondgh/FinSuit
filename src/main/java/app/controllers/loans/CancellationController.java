package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.models.loans.LoansModel;
import app.repositories.notifications.NotificationEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CancellationController extends LoansModel implements Initializable {

    @FXML
    public MFXButton cancellationButton;
    @FXML public TextArea cancellationField;
    public static String LOAN_NUMBER;
    public static int USER_ID;

    UserNotification userNotification = new UserNotification();


    @FXML
    void saveButtonOnAction() {
        NotificationEntity notification = new NotificationEntity();
        UserAlerts ALERTS = new UserAlerts("CANCEL LOAN", "You have requested to officially cancel this loan process. Are you sure you want to proceed?",
                "by confirming, you will terminate the loan process.");
                                if(ALERTS.confirmationAlert()) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("loanNumber", LOAN_NUMBER);
                                    data.put("applicationStatus", "terminated");
                                    data.put("loanStatus", "closed");
                                    data.put("purpose", cancellationField.getText());
                                    data.put("userId", USER_ID);
                            int responseStatus = cancelLoanApplication(data);
                            if(responseStatus > 0) {
                                notification.setTitle("LOAN CANCELLATION");
                                notification.setMessage("Loan application number " + LOAN_NUMBER + " has successfully been cancelled");
                                notification.setLogged_by(USER_ID);
                                logNotification(notification);
                                cancellationButton.getScene().getWindow().hide();
                                userNotification.successNotification("LOAN PROCESS CANCELLED", "Nice, you have successfully terminated the loan process.");
                            } else {
                                userNotification.errorNotification("CANCELLATION PROCESS FAILED", "Sorry the process to terminate this loan failed, retry");
                            }
                        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancellationButton.setDisable(true);
        cancellationField.setOnKeyTyped(event -> {
            cancellationButton.setDisable(cancellationField.getText().isEmpty());
        });
    }
}
