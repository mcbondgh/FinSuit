package app.controllers.loans;

import app.repositories.loans.LoanScheduleEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class ScheduleController {

    public static String loanNumberStaticVariable;

    @FXML private ImageView applicantProfileImage;
    @FXML private Label paidAmount, arrearsAmount;
    @FXML private Label applicantName, disbursedAmount;
    @FXML private Label applicantNumber, loanStatus;

    @FXML private MFXButton smsButton, exportButton;

    @FXML private TableView<LoanScheduleEntity> scheduleTable;


    public ScheduleController() {

    }

    public static void setSelectedLoanValue(String parameter) {
        loanNumberStaticVariable = parameter;
    }
    public String getSelectedLoanValue() {
        return loanNumberStaticVariable;
    }



}//end of class...
