package app.controllers.finance;

import app.config.encryptDecryp.EncryptDecrypt;
import app.models.finance.FinanceModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class BusinessAccountController extends FinanceModel implements Initializable {
    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     ********************************************************************************************************************/
    @FXML private MFXProgressBar progressBar;
    @FXML private PasswordField passwordField;
    @FXML private Label accountBalanceIndicator, passwordErrorIndicator, previousBalanceIndicator;
    @FXML private MFXButton submitButton, saveTransactionButton;

    @FXML private DatePicker transDatePicker;
    @FXML private ComboBox<String> transTypeSelector, bankSelector;
    @FXML private TextField transactionAmountField, transactionIdField;
    @FXML private TextArea notesField;



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkPasswordField();
    }

    void runProgressTaskIndicator(String accountBalance,String previousBalance) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            double number = 0.0;
            @Override
            public void run() {
                number +=0.2;
                Platform.runLater(()-> {
                    progressBar.setProgress(number);
                    if (number == 1) {
                        this.cancel();
                        progressBar.setProgress(0);
                        NumberFormat format = NumberFormat.getNumberInstance(Locale.ENGLISH);
                        String yourNumber = format.format(100545.56);
                        accountBalanceIndicator.setText("Ghc".concat(accountBalance));
                        previousBalanceIndicator.setText("Ghc".concat(previousBalance));
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS.
     ********************************************************************************************************************/
    @FXML void checkPasswordField() {
        passwordField.setOnKeyTyped(keyEvent -> {
            submitButton.setDisable(passwordField.getText().isBlank());
        });
        transactionAmountField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9.]")) {
                transactionAmountField.deletePreviousChar();
            }
        });
    }

    @FXML private void submitButtonClicked() {
        Map<String, Object> data = getBusinessAccountInformation();
        String inputPassword = passwordField.getText();
        String encryptedPassword = data.get("password").toString();
        String accountBalance = data.get("accountBalance").toString();
        String previousBalance = data.get("previousBalance").toString();
        boolean isPasswordValid = EncryptDecrypt.verifyHashValue(inputPassword, encryptedPassword);

        //RUN THE DATA AND SET PARAMETERS BASED ON THE RETURNED DATA.
        if (isPasswordValid) {
            runProgressTaskIndicator(accountBalance, previousBalance);
            passwordErrorIndicator.setVisible(false);
        }else {
            passwordErrorIndicator.setVisible(true);
           accountBalanceIndicator.setText(null);
           previousBalanceIndicator.setText(null);
        }
    }

    @FXML private void saveTransactionButtonClicked() {
        Map<String, Object> data = getBusinessAccountInformation();
    }


}//end of class...
