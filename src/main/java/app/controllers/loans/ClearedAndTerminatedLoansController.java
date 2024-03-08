package app.controllers.loans;

import app.models.loans.LoansModel;
import app.repositories.loans.LoanPaymentLogsEntity;
import app.repositories.loans.LoansEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.sql.Timestamp;

public class ClearedAndTerminatedLoansController extends LoansModel {


    /*******************************************************************************************************************
     * FXML FILE EJECTION
    *******************************************************************************************************************/
    @FXML private MFXButton loadTableButton;
    @FXML private VBox summaryVBox, tableVBox;
    @FXML private MFXLegacyTableView<LoansEntity> summaryTable;
    @FXML private MFXLegacyTableView<LoanPaymentLogsEntity> logsTable;

    //SUMMARY TABLE FIELDS.
    @FXML private TableColumn<LoansEntity, Integer> tableIndexColumn;
    @FXML private TableColumn<LoansEntity, String> loanNumberColumn;
    @FXML private TableColumn<LoansEntity, Double> disbursedColumn;
    @FXML private TableColumn<LoansEntity, Integer> totalPaymentColumn;
    @FXML private TableColumn<LoansEntity, Label> statusColumn;
    @FXML private TableColumn<LoansEntity, Timestamp> finishedDateColumn;

    //LOGS TABLE FIELDS
    @FXML private TableColumn<LoanPaymentLogsEntity, Integer> logsIndexColumn;
    @FXML private TableColumn<LoanPaymentLogsEntity, Date> dueDateColumn;
    @FXML private TableColumn<LoanPaymentLogsEntity, Double> logsAmountColumn;
    @FXML private TableColumn<LoanPaymentLogsEntity, Double> writeOffColumn;
    @FXML private TableColumn<LoanPaymentLogsEntity, Double> logsPaymentDateColumn;

    /*******************************************************************************************************************
     * IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/

    void populateSummaryTable() {
        tableIndexColumn.setCellValueFactory(new PropertyValueFactory<>("loan_id"));
        loanNumberColumn.setCellValueFactory(new PropertyValueFactory<>("loan_no"));
        disbursedColumn.setCellValueFactory(new PropertyValueFactory<>("approved_amount"));
        totalPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("total_payment"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
        summaryTable.setItems(getClearedAndTerminatedLoans());
    }
    void populateLogsTable() {

    }

    /*******************************************************************************************************************
     * ACTION EVENT METHODS
     *******************************************************************************************************************/
    @FXML private void loadTableOnClick() {

    }


}//END OF CLASS
