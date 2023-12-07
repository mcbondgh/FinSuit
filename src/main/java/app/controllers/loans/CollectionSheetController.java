package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.models.loans.LoansModel;
import app.repositories.loans.CollectionSheetEntity;
import app.repositories.loans.LoanScheduleEntity;
import app.repositories.notifications.NotificationEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionSheetController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    NotificationEntity

    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTION .*************************************/
    @FXML private ListView<String> listView;
    @FXML private  Label sheetDate;
    @FXML private Label officerNameLabel;
    @FXML private TableView<CollectionSheetEntity> collectionTable;
    @FXML private MFXButton exportButton;

    @FXML private TableColumn<CollectionSheetEntity, Integer> indexColumn;
    @FXML private TableColumn<CollectionSheetEntity, String> loanNoColumn;
    @FXML private TableColumn<CollectionSheetEntity, String> nameColumn;
    @FXML private TableColumn<CollectionSheetEntity, String> installmentColumn;
    @FXML private TableColumn<CollectionSheetEntity, ComboBox<Date>> dateColumn;
    @FXML private TableColumn<CollectionSheetEntity, Double> amountColumn;

    public CollectionSheetController() {
    }

    /*******************************************************************************************************************
     *********************************************** OTHER METHODS IMPLEMENTATION.*************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadListView();
        sheetDate.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
    }

    private void populateTableView() {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        loanNoColumn.setCellValueFactory(new PropertyValueFactory<>("loanNo"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        installmentColumn.setCellValueFactory(new PropertyValueFactory<>("installmentAmount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDateSelector"));
//        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountField"));
        String value = listView.getSelectionModel().getSelectedItem();
        collectionTable.setItems(fetchCollectionSheetData(value));
        fetchCollectionSheetData(value).forEach(item-> {
            officerNameLabel.setText(item.getOfficerName());
        });
        collectionTable.getItems().forEach(item-> {
            String loanNo = item.getLoanNo();
            getRepaymentSchedule(loanNo).forEach(data -> {
                boolean result = Math.floor(data.getMonthly_payment()) < Math.floor(data.getMonthly_installment());
                if (result){
                    item.getDueDateSelector().getItems().add(Date.valueOf(data.getPayment_date()));
                }
            });
        });
    }

    private void loadListView() {
        fetchAssignedUsersOnly().forEach(item -> {
            if (item.getRole().equals("Loan Officer")){
                listView.getItems().add(item.getEmp_id());
            }
        });
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS.*************************************/
    @FXML void exportButtonOnAction() {
        collectionTable.getItems().forEach(item-> {

        });
    }

    @FXML void officerIdSelected() {
        populateTableView();
        exportButton.setDisable(listView.getSelectionModel().isEmpty());
    }



}//end of class...
