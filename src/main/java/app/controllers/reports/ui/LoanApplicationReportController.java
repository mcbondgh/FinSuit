package app.controllers.reports.ui;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.documents.DocumentGenerator;
import app.models.loans.LoansModel;
import app.models.reports.ReportsModel;
import app.repositories.accounts.ViewCustomersTableDataRepository;
import app.repositories.reports.LoanApplicationReportEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;

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
    @FXML private TextField filterField, disbursementField, paymentField;
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

    void computeTableValues() {
        AtomicReference<Double> totalDisbursement = new AtomicReference<>(0.0);
        AtomicReference<Double> totalPayed = new AtomicReference<>(0.0);
        tableView.getItems().forEach(item -> {
            if (item.getApplication_status().equals("disbursed")) {
                totalDisbursement.getAndUpdate(value -> value + item.getApproved_amount());
                totalPayed.getAndUpdate(value -> value + item.getTotal_payment());
            }
        });
        disbursementField.setText(String.valueOf(totalDisbursement.get()));
        paymentField.setText(String.valueOf(totalPayed.get()));
    }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    @FXML void filterTableByKeyWord() {
        try{
//            customersTable.getItems().clear();
            Date start = Date.valueOf(startPicker.getValue());
            Date end = Date.valueOf(endPicker.getValue());
            ObservableList<LoanApplicationReportEntity> data = fetchLoanApplicantReportData(start, end);
            FilteredList<LoanApplicationReportEntity> filteredList =  new FilteredList<>( data, p -> true);
            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(tableData -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (tableData.getApplication_status().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else if (tableData.getFullname().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else if (tableData.getLoan_no().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else return tableData.getSuper_name().toLowerCase().contains(searchKeyWord);
                });
            });
            SortedList<LoanApplicationReportEntity> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedResult);
            computeTableValues();
        }catch (Exception ignored) {}
    }



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
            computeTableValues();
        }
    }
    @FXML void exportTableData() {
        if (tableEmpty()) {
            ALERTS = new UserAlerts("EMPTY TABLE", "Table is empty, please generate a report and export.");
            ALERTS.informationAlert();
        } else {
            //export table data
            exportData();
        }
    }
    //EXTRACTED METHOD THAT WILL BE CALLED WHEN THE EXPORT BUTTON IS CLICKED.
    private void exportData() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4)));
        exportButton.setDisable(true);
        exportButton.setText("Exporting...");
        timeline.playFromStart();
        DocumentGenerator documentGenerator = new DocumentGenerator();
        String docName = "Loan Application Report " + LocalDateTime.now().getNano();

        int responseStatus = documentGenerator.generateLoanApplicationReport(docName, tableView);

        timeline.setOnFinished(finished -> {
            exportButton.setDisable(false);
            exportButton.setText("EXPORT DATA");
            if ( responseStatus == 200) {
                TOASTER.successNotification("LOAN REPORT", "Report successfully exported to your desktop");
            } else {
                TOASTER.errorNotification("FAILED EXPORT", "Report export was unsuccessful, please regenerate and export data.");
            }
        });
    }

}//end of class...
