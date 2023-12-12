package app.controllers.reports.ui;

import app.alerts.UserNotification;
import app.documents.DocumentGenerator;
import app.enums.TransactionTypes;
import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.accounts.ViewCustomersTableDataRepository;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TransactionReports extends MainModel implements Initializable {
    UserNotification NOTIFY = new UserNotification();
    DocumentGenerator DOC_GENERATOR = new DocumentGenerator();

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS ***********************************************/

    @FXML
    private CheckBox transactionCheckBox;
    @FXML private HBox transDetailedPane;
    @FXML private MFXButton transactionGenerateBtn, transactionExportBtn;
    @FXML private DatePicker transStartDatePicker, transEndDatePicker;
    @FXML private TableView<TransactionsEntity> transactionLogsTable;
    @FXML private TableColumn<TransactionsEntity, Integer> indexColumn;
    @FXML private TableColumn<TransactionsEntity, String> transIdColumn;
    @FXML private TableColumn<TransactionsEntity, String> transTypeColumn;
    @FXML private TableColumn<TransactionsEntity, String> transAccountNumberColumn;
    @FXML private TableColumn<TransactionsEntity, String> paymentMethodColumn;
    @FXML private TableColumn<TransactionsEntity, String> gatewayColumn;
    @FXML private TableColumn<TransactionsEntity, Double> cashColumn;
    @FXML private TableColumn<TransactionsEntity, Double> ecashColumn;
    @FXML private TableColumn<TransactionsEntity, String> ecashIdColumn;
    @FXML private TableColumn<TransactionsEntity, Timestamp> transDateColumn;
    @FXML private TableColumn<TransactionsEntity, String> initiatedByColumn;
    @FXML private TableColumn<TransactionsEntity, String> paidByColumn;

    @FXML private Label cashSumLabel,eCashSumLabel, totalSumLabel;
    @FXML private MFXComboBox<String> transactionTypeBox, transactionIdBox, accountNumberBox, cashierSelector;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private Pane searchPanel;
    @FXML private TextField searchField;



    /*******************************************************************************************************************
     *********************************************** OTHER METHODS. ****************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadSelectors();
        transactionLogsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadSelectors() {
//        SpecialMethods.setTransactionTypes(transactionTypeBox);
        for (Enum<TransactionTypes> item : TransactionTypes.values()){
            transactionTypeBox.getItems().add(item.name());
        }
//        LocalDate start = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
//        startDatePicker.setValue(start);
        endDatePicker.setValue(LocalDate.now());
    }

    void populateTable() {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        transTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_type"));
        transAccountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("account_number"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        gatewayColumn.setCellValueFactory(new PropertyValueFactory<>("payment_gateway"));
        cashColumn.setCellValueFactory(new PropertyValueFactory<>("cash_amount"));
        ecashColumn.setCellValueFactory(new PropertyValueFactory<>("ecash_amount"));
        ecashIdColumn.setCellValueFactory(new PropertyValueFactory<>("ecash_id"));
        transDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        initiatedByColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        paidByColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_made_by"));

        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        transactionLogsTable.setItems(fetchSimpleTransactionLogReport(start, end));
    }

    void computeCashValues(){
        AtomicReference<Double> cash = new AtomicReference<>(0.0);
        AtomicReference<Double> eCash = new AtomicReference<>(0.0);
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        transactionLogsTable.getItems().forEach(data -> {
            cash.updateAndGet(v -> v + data.getCash_amount());
            eCash.updateAndGet(v -> v + data.getEcash_amount());
        });
        cashSumLabel.setText(format.format(cash.get()));
        eCashSumLabel.setText(format.format(eCash.get()));
        totalSumLabel.setText(format.format(cash.get()+ eCash.get()));
    }


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE METHODS *********************************************/
    boolean isDetailedCheckBoxSelected(){return transactionCheckBox.isSelected();}
    boolean isTransactionTableEmpty(){return transactionLogsTable.getItems().isEmpty();}
    boolean isTableSelectionEmpty(){return transactionLogsTable.getSelectionModel().isEmpty();}
    boolean isDateFieldEmpty() {return startDatePicker.getValue() == null || endDatePicker.getValue() == null;}



    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION *******************************************/
    @FXML void filterTable() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        try {
            transactionLogsTable.getItems().clear();
            FilteredList<TransactionsEntity> filteredList =  new FilteredList<>(fetchSimpleTransactionLogReport(start, end), p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(tableData -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (tableData.getAccount_number().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else if (tableData.getTransaction_type().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    }
                    else if (tableData.getTransaction_id().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    }
                    else if (tableData.getPayment_method().toLowerCase().contains(searchKeyWord)) {
                        return true;

                    } else return tableData.getUsername().toLowerCase().contains(searchKeyWord);

                });
                computeCashValues();
            });
            SortedList<TransactionsEntity> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(transactionLogsTable.comparatorProperty());
            transactionLogsTable.setItems(sortedResult);
        }catch (Exception ignore){

        }

    }



    /*******************************************************************************************************************
     *********************************************** ACTION EVENTS METHODS IMPLEMENTATION ******************************/
    @FXML void detailedLogsSelected() {
        searchPanel.setDisable(!isDetailedCheckBoxSelected());

    }
    @FXML void dateValueChange() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        fetchSimpleTransactionLogReport(start, end).forEach(data -> {
            if (!transactionIdBox.getItems().contains(data.getTransaction_id())){
                transactionIdBox.getItems().add(data.getTransaction_id());
            }
            if (!accountNumberBox.getItems().contains(data.getAccount_number())){
                accountNumberBox.getItems().add(data.getAccount_number());

            } if (!cashierSelector.getItems().contains(data.getUsername())){
                cashierSelector.getItems().add(data.getUsername());
            }
        });
    }

    @FXML void generateReport() {
        if (isDateFieldEmpty()){
            NOTIFY.informationNotification("INVALID DATES", "Please select START and END Dates to generate report.");
        }
        populateTable();
        transactionExportBtn.setDisable(isTransactionTableEmpty());
        computeCashValues();
    }

    public void exportData(ActionEvent actionEvent) {
        int visibleColumn = transactionLogsTable.getVisibleLeafColumns().size();
        if (!isTransactionTableEmpty()){
           int exportStatus = DOC_GENERATOR.generateTransactionLogsReport(transactionLogsTable);
           if (exportStatus == 200){
               NOTIFY.successNotification("REPORT GENERATED", "Transaction report successfully generated.");
           }else NOTIFY.errorNotification("FAILED PROCESS", "Transaction report failed to generate, please regenerate report.");
        }
    }
}//end of class
