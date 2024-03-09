package app.controllers.loans;

import app.models.loans.LoansModel;
import app.repositories.loans.LoanPaymentLogsEntity;
import app.repositories.loans.LoansEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
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
    @FXML private TextArea reasonField;
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
//    @FXML private TableColumn<LoanPaymentLogsEntity, Integer> logsIndexColumn;
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
        finishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("lastPaymentDate"));
        summaryTable.setItems(getClearedAndTerminatedLoans());
    }
    void populateLogsTable(String loanNo) {
//        logsIndexColumn.setCellValueFactory(new PropertyValueFactory<>("log_id"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("installment_month"));
        logsAmountColumn.setCellValueFactory(new PropertyValueFactory<>("paid_amount"));
        writeOffColumn.setCellValueFactory(new PropertyValueFactory<>("write_offs"));
        logsPaymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("date_collected"));
        logsTable.setItems(getLoanPaymentLogs(loanNo));

    }

    /*******************************************************************************************************************
     * ACTION EVENT METHODS
     *******************************************************************************************************************/
    @FXML private void loadTableOnClick() {
        populateSummaryTable();
    }
    @FXML private void summaryTableItemSelected() {
        try {
            String loanNo = summaryTable.getSelectionModel().getSelectedItem().getLoan_no();
            populateLogsTable(loanNo);
            String reason = logsTable.getItems().get(0).getTerminationPurpose();
            reasonField.setText(reason);
        }catch (NullPointerException ignore) {}
    }


}//END OF CLASS
