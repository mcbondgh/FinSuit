package app.controllers.accounts;

import app.repositories.accounts.ViewCustomersTableDataRepository;
import app.models.accounts.CreateAccountModel;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ViewAccountController extends CreateAccountModel implements Initializable {


    //FXML FILE EJECTIONS.
    @FXML
    private MFXLegacyTableView<ViewCustomersTableDataRepository> customersTable;
    @FXML
    private TableColumn<ViewCustomersTableDataRepository, String> fullNameColumn;
    @FXML
    private TableColumn<ViewCustomersTableDataRepository, String> genderColumn;
    @FXML
    private TableColumn<ViewCustomersTableDataRepository, String> ageColumn;
    @FXML
    private TableColumn<ViewCustomersTableDataRepository, String>mobileNumberColumn;
    @FXML
    private TableColumn<ViewCustomersTableDataRepository, String> accountTypeColumn;
    @FXML
    private TableColumn<ViewCustomersTableDataRepository, String> accountNumberColumn;

    @FXML private TableColumn<ViewCustomersTableDataRepository, String> idTypeColumn;
    @FXML private TableColumn<ViewCustomersTableDataRepository, Timestamp> registrationDateColumn;
    @FXML private TableColumn<Button, Button> actionColumn;

    @FXML private TextField searchField;

    public static String selectedCustomerAccountNumber;


    //TRUE OR FALSE STATEMENTS




    //IMPLEMENTATION OF OTHER METHODS.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();
        viewButtonClicked();
    }

    private void populateTable() {
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobile_number"));
        idTypeColumn.setCellValueFactory(new PropertyValueFactory<>("id_type"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("account_number"));
        accountTypeColumn.setCellValueFactory(new PropertyValueFactory<>("account_type"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("viewButton"));
        customersTable.setItems(fetchCustomersDataSummary());
    }



    //IMPLEMENTATION OF ACTION EVENT METHODS
    public void searchCustomerMethod(KeyEvent event) {
        try {
            customersTable.getItems().clear();
            FilteredList<ViewCustomersTableDataRepository> filteredList =  new FilteredList<>(fetchCustomersDataSummary(), p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(customersTableData -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (customersTableData.getAccount_number().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else if (customersTableData.getFullname().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else return customersTableData.getId_type().toLowerCase().contains(searchKeyWord);
                });
            });
            SortedList<ViewCustomersTableDataRepository> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(customersTable.comparatorProperty());
            customersTable.setItems(sortedResult);
        }catch (Exception ignored) {}
    }


    void viewButtonClicked() {
        for (ViewCustomersTableDataRepository data : customersTable.getItems()) {
            data.getViewButton().setOnAction(event -> {
               selectedCustomerAccountNumber = data.getAccount_number();
                try {
                    AppStages.editCustomerDataStage().show();
                }catch (Exception e){e.printStackTrace();}
            });
        }
    }


}// End of class
