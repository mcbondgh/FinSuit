package app.controllers.loans;

import app.models.transactions.TransactionModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TerminateLoanController extends TransactionModel implements Initializable {

    /*******************************************************************************************************************
                        FXML FILE EJECTION
     ******************************************************************************************************************/
    @FXML
    private Label principalLabel, interestLabel, penaltyLabel, nameLabel, loanNumberLabel;
    @FXML private Label balanceLabel, accumulatedBalaceLabel;
    @FXML private TextField interestWeaverField, penaltyWeaverField, balanceWeaverField;
    @FXML private MFXButton terminateButton;
    private static String applicantName, mobileNumber, loanNumber;
    private static Map<String, Double> mappedValues = new HashMap<>();

    /*******************************************************************************************************************
    // CREATE GETTERS AND SETTERS FOR ALL PRIVATE STATIC FIELDS.
     ******************************************************************************************************************/
    public static String getApplicantName() {return applicantName;}
    public static String getLoanNumber(){return  loanNumber;}
    public static String getMobileNumber(){return mobileNumber;}
    public static void setAccumulatedTableValue(Map<String, Double> values) {
        mappedValues = values;
    }
    public static Map<String, Double> getAccumulatedTableValues() {return mappedValues;}

    public static void setApplicantName(String value) {
        applicantName = value;
    }
    public static void setLoanNumber(String value) { loanNumber = value;}
    public static void setMobileNumber(String value) { mobileNumber = value;}

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        loanNumberLabel.setText(getLoanNumber());
        nameLabel.setText(getApplicantName());
        principalLabel.setText(getAccumulatedTableValues().get("principal").toString());
        interestLabel.setText(getAccumulatedTableValues().get("interest").toString());
        penaltyLabel.setText(getAccumulatedTableValues().get("penalty").toString());
        balanceLabel.setText(getAccumulatedTableValues().get("balance").toString());
        accumulatedBalaceLabel.setText(principalLabel.getText());

        balanceWeaverField.setText(String.valueOf(0.00));
        interestWeaverField.setText(String.valueOf(0.00));
        penaltyWeaverField.setText(String.valueOf(0.00));

    }
    /*******************************************************************************************************************
     INPUT FIELDS VALIDATIONS
     ******************************************************************************************************************/
    @FXML void validateInputDataType(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            interestWeaverField.deletePreviousChar();
            penaltyWeaverField.deletePreviousChar();
            balanceWeaverField.deletePreviousChar();
        } else {
            double interest = Double.parseDouble(interestWeaverField.getText());
            double penalty = Double.parseDouble(penaltyWeaverField.getText());
            double payable = Double.parseDouble(accumulatedBalaceLabel.getText());

            accumulatedBalaceLabel.setText(String.valueOf(payable));
        }

    }

    /*******************************************************************************************************************
     FXML FILE EJECTION
     ******************************************************************************************************************/

}//END OF CLASS
