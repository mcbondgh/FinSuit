package app.controllers.loans;

import app.AppStarter;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoansController implements Initializable {

    Stage loanApplicationStage = AppStages.loanApplicationStage();


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;

    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML
    private MFXButton addNewLoanButton, payLoanButton, filterButton, loanRequestsButton, generateSheetButton, uploadSheetButton;
    @FXML private MFXLegacyTableView<Object> loanApplicantsTable;

    public LoansController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadForm();
            payLoanButtonClicked();
            filterButtonClicked();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void loadForm() throws InterruptedException, IOException {
        pageTitle.setText(pageTitlePlaceHolder);
        String fxmlFile = "views/loans/loan-customers-page.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlFile));
        borderPane.setCenter(fxmlLoader.load());
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    public void loadCustomersTable(MouseEvent mouseEvent) throws IOException {

    }
    @FXML
    private void setLoanRequestsButtonClicked() {
        try {
            AppStages.loanCalculatorStage().show();
            loanRequestsButton.setDisable(AppStages.loanCalculatorStage().isShowing());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
