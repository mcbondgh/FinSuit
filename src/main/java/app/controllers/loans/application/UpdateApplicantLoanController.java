package app.controllers.loans.application;

import app.models.MainModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateApplicantLoanController extends MainModel implements Initializable {
    @FXML
    private static Label loanNumberLabel;
    public static void setLoanNumber(String loanNumber) {
        loanNumberLabel.setText(loanNumber);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void validateRequestAmountInput(KeyEvent keyEvent) {
    }

    public void checkCustomerIdField(KeyEvent keyEvent) {
    }
}
