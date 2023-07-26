package app.controllers.transactions;

import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    public static String pageTitlePlaceHolder;
    @FXML private MFXButton depositButton, checkBalanceButton, withdrawalButton;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        checkBalanceButtonClicked();
        withdrawalButtonClicked();
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    public void searchCustomerMethod(KeyEvent event) {
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML void depositButtonClicked() throws IOException {
        AppStages.depositStage().show();
    }
    void withdrawalButtonClicked() {
        withdrawalButton.setOnAction(e -> {
            try {
                AppStages.withdrawalStage().show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    void checkBalanceButtonClicked() {
        checkBalanceButton.setOnAction(e -> {
            try {
                AppStages.accountBalanceStage().show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}
