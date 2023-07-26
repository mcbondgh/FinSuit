package app.controllers.loans;

import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoanCustomersController implements Initializable {

    Stage loanApplicationStage = AppStages.loanApplicationStage();


    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS.
     ********************************************************************************************************************/
    @FXML
    private MFXButton addNewLoanButton, payLoanButton, filterButton;
    @FXML private MFXLegacyTableView loanApplicantsTable;
    @FXML private AnchorPane anchorPage;


    public LoanCustomersController() throws IOException {}

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF SPECIAL METHODS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** INPUT VALIDATION METHODS
     ********************************************************************************************************************/
    public void searchCustomerMethod(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addNewLoanButton.setDisable(loanApplicationStage.isShowing());
        payLoanButtonClicked();
        filterButtonClicked();
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    public void newLoanButtonClicked(ActionEvent event) throws IOException {
        loanApplicationStage.show();
    }

    void payLoanButtonClicked() {
        payLoanButton.setOnAction(event -> {
            try {
                AppStages.loanPaymentStage().show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    void filterButtonClicked() {
        filterButton.setOnAction(event -> {
            try {
                AppStages.loanScheduleStage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



}//end of class
