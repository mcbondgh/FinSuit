package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.documents.DocumentGenerator;
import app.models.loans.LoansModel;
import app.repositories.loans.CollectionSheetEntity;
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
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class CollectionSheetController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    NotificationEntity LOG_NOTIFICATION = new NotificationEntity();
    CollectionSheetEntity COLLECTION_ENTITY;
    DocumentGenerator DOCUMENT_GENERATOR = new DocumentGenerator();

    int loggedInUser = getUserIdByName(AppController.activeUserPlaceHolder);
    String userName = AppController.activeUserPlaceHolder;

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
        try {
            AtomicBoolean checkSelection = new AtomicBoolean(false);
            collectionTable.getItems().forEach(item-> {
                checkSelection.set(item.getDueDateSelector().getValue() == null);
                if (checkSelection.get()){
                    item.getDueDateSelector().setStyle("-fx-background-color:#ffecec");
                }else item.getDueDateSelector().setStyle("-fx-background-color:#eeee");
            });

            //check if either of the comboxes is empty and show notification else collect table data...
            if (checkSelection.get()){
                NOTIFY.informationNotification("EMPTY SELECTION", "One or more empty date selection. Please fill all date fields to continue");
            }else {
                String officerName = officerNameLabel.getText();
                String date = sheetDate.getText();
                int responseStatus =DOCUMENT_GENERATOR.exportCollectionSheetAsExcel(officerName, date, collectionTable  );
                if (responseStatus == 200) {
                    NOTIFY.successNotification("FILE CREATED", "Collection sheet successfully generated.");
                    LOG_NOTIFICATION.setSender_method("SYSTEM NOTIFICATION");
                    LOG_NOTIFICATION.setTitle("COLLECTION SHEET GENERATED");
                    LOG_NOTIFICATION.setLogged_by(loggedInUser);
                    LOG_NOTIFICATION.setMessage("Employee ".concat(userName).concat(" have generated a collection sheet."));
                    logNotification(LOG_NOTIFICATION);
                }else NOTIFY.errorNotification("FAILED PROCESS", "Filed attempt to generated collection sheet");
            }
        }catch (NullPointerException exception) {
            NOTIFY.informationNotification("EMPTY SELECTION", "One or more empty date selection. Please fill all date fields to continue");
        }
    }// end of method...

    @FXML void officerIdSelected() {
        populateTableView();
        exportButton.setDisable(listView.getSelectionModel().isEmpty());
    }



}//end of class...
