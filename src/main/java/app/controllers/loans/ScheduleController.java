package app.controllers.loans;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ScheduleController {

    public static String loanNumberStaticVariable;

    @FXML private ImageView applicantProfile;
    @FXML private Label paidAmount, arrearsAmount;
    @FXML private Label applicantName, principalAmount;
    @FXML private Label applicantLoanNumber, applicantEmail;
    @FXML private Label applicantNumber, loanStatus, applicantAddress;
    @FXML private MFXButton smsButton, exportButton;


    public ScheduleController() {
        System.out.println(getSelectedLoanValue());
    }

    public static void setSelectedLoanValue(String parameter) {
        loanNumberStaticVariable = parameter;
    }
    public String getSelectedLoanValue() {
        return loanNumberStaticVariable;
    }



}//end of class...
