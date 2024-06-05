package app.controllers.reports.ui;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.models.loans.LoansModel;
import app.models.reports.ReportsModel;
import app.repositories.reports.LoanApplicationReportEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;

public class LoanApplicationReportController extends ReportsModel {

    UserNotification TOASTER = new UserNotification();
    UserAlerts ALERTS;


    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTION
     ********************************************************************************************************************/
    @FXML
    Hyperlink exportButton;
    @FXML private MFXButton loadTableButton;
    @FXML private DatePicker startPicker, endPicker;
    @FXML private TextField filterField;
    @FXML private MFXLegacyTableView<LoanApplicationReportEntity> tableView;

    @FXML
    TableColumn<LoanApplicationReportEntity, Integer> indexColumn;
    @FXML
    TableColumn<LoanApplicationReportEntity, String> nameColumn, loanNumberColumn,statusColumn, supervisorColumn;
    @FXML
    TableColumn<LoanApplicationReportEntity, Double> disbursedColumn, repaymentColumn, paidColumn;
    @FXML
    TableColumn<LoanApplicationReportEntity, Date> dateColumn;


    private boolean startPickerEmpty() {return startPicker.getValue() == null;}
    private boolean endPickerEmpty() {return endPicker.getValue() == null;}
    private boolean tableEmpty() {return tableView.getItems().isEmpty();}
    boolean invalidDateSelection(){
        return endPicker.getValue().isBefore(startPicker.getValue());
    }

    void populateTable() {
        indexColumn.setCellValueFactory( new PropertyValueFactory<>("counter"));
        nameColumn.setCellValueFactory( new PropertyValueFactory<>("fullname"));
        loanNumberColumn.setCellValueFactory( new PropertyValueFactory<>("loan_no"));
        statusColumn.setCellValueFactory( new PropertyValueFactory<>("application_status"));
        supervisorColumn.setCellValueFactory( new PropertyValueFactory<>("super_name"));
        disbursedColumn.setCellValueFactory( new PropertyValueFactory<>("approved_amount"));
        repaymentColumn.setCellValueFactory( new PropertyValueFactory<>("repayment_amount"));
        paidColumn.setCellValueFactory( new PropertyValueFactory<>("total_payment"));
        dateColumn.setCellValueFactory( new PropertyValueFactory<>("date_created"));
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS
     ********************************************************************************************************************/
    @FXML private void loadLoanTable() {
        if (startPickerEmpty() || endPickerEmpty()) {
            ALERTS = new UserAlerts("INVALID DATE", "Please provide a START and END date to load the table.");
            ALERTS.informationAlert();
        } else if (invalidDateSelection()) {
            ALERTS = new UserAlerts("INVALID DATE", "END date cannot be before START date.", "start date should be before end date.");
            ALERTS.informationAlert();
        } else {
            Date start = Date.valueOf(startPicker.getValue());
            Date end = Date.valueOf(endPicker.getValue());
            populateTable();
            tableView.setItems(fetchLoanApplicantReportData(start, end));
        }
    }

    @FXML void exportTableData() {
        if (tableEmpty()) {
            ALERTS = new UserAlerts("EMPTY TABLE", "Table is empty, please generate a report and export.");
            ALERTS.informationAlert();
        }
    }

}//end of class...
