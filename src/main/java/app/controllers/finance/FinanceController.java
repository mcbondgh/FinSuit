package app.controllers.finance;

import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FinanceController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML private MFXButton loadTableButton, accountsButton, viewLoansButton, viewCashierSummaryBtn;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        businessAccountButtonClicked();

    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML void viewLoansButtonClicked() {
        try {
            AppStages.paymentApprovalStage().showAndWait();
        }catch (Exception ignored){}
    }
    @FXML void businessAccountButtonClicked() {
        try {
            SpecialMethods.FlipView(borderPane, "views/finance/internal-transaction-view.fxml");
        } catch (IOException ignore) {}

    }
    @FXML void viewCashierSummary() {
        try{
            SpecialMethods.FlipView(borderPane, "views/finance/cashier-transactions-view.fxml");
        }catch (IOException ignore){}
    }

}//end of class...