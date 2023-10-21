package app.controllers.loans;

import app.controllers.homepage.AppController;
import app.models.loans.LoansModel;
import app.repositories.loans.LoansTableEntity;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoansController extends LoansModel implements Initializable {

    Stage loanApplicationStage = AppStages.loanApplicationStage();
    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;

    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML private HBox hBox;
    @FXML
    private MFXButton addNewLoanButton, payLoanButton, loadTableButton, loanRequestsButton, generateSheetButton, uploadSheetButton,viewLoansButton;
    @FXML private MFXLegacyTableView<LoansTableEntity> loanApplicantsTable;
    @FXML private TableColumn<LoansTableEntity, Integer> noColumn;
    @FXML private TableColumn<LoansTableEntity, String>fullNameColumn;
    @FXML private TableColumn<LoansTableEntity, String>loansIdColumn;
    @FXML private TableColumn<LoansTableEntity, String>dateColumn;
    @FXML private TableColumn<LoansTableEntity, Double>amountColumn;
    @FXML private TableColumn<LoansTableEntity, Label> statusColumn;
    @FXML private TableColumn<LoansTableEntity, String> loanTypeColumn;
    @FXML private TableColumn<LoansTableEntity, MFXButton> viewColumn;
    @FXML private TableColumn<LoansTableEntity, MFXButton> editColumn;

    @FXML private TextField searchField;
    @FXML private TextField finalAmountField;
    @FXML private Label displayLoanStatus;

    public LoansController() throws IOException {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadForm();
            payLoanButtonClicked();
            showRequestedLoanCount();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    void loadForm() throws InterruptedException, IOException {
        pageTitle.setText(pageTitlePlaceHolder);
//        String fxmlFile = "views/loans/loan-customers-page.fxml";
//        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlFile));
//        borderPane.setCenter(fxmlLoader.load());
    }

    /*******************************************************************************************************************
     *********************************************** OTHER METHODS IMPLEMENTATION.
     *******************************************************************************************************************/
    private void populateTable() {
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        loansIdColumn.setCellValueFactory(new PropertyValueFactory<>("loanNo"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("requestedAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
        loanTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loanType"));
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("viewButton"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        loanApplicantsTable.setItems(getLoansUnderProcessingOnly(loggedInUserId));


    }

    private void showRequestedLoanCount() {
        int counter = countRequestedLoans();
        Label label = new Label(String.valueOf(counter));
        label.setStyle("-fx-background-color:#e600000; -fx-background-radius:50%; -fx-text-fill:#fff; -fx-padding:5px;");
        loanRequestsButton.setText("Loan Requests " + label);
    }

    public void searchCustomerMethod(KeyEvent event) {
        try {
            loanApplicantsTable.getItems().clear();
            FilteredList<LoansTableEntity> filteredList =  new FilteredList<>(getLoansUnderProcessingOnly(loggedInUserId), p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(customersTableData -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (customersTableData.getFullName().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else if (customersTableData.getLoanNo().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else return customersTableData.getLoanType().toLowerCase().contains(searchKeyWord);
                });
            });
            SortedList<LoansTableEntity> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(loanApplicantsTable.comparatorProperty());
            loanApplicantsTable.setItems(sortedResult);
        }catch (Exception ignored) {}
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     *******************************************************************************************************************/
    @FXML void loansTableItemSelected() throws IOException {
        if (!loanApplicantsTable.getItems().isEmpty()) {

            for (LoansTableEntity item : loanApplicantsTable.getItems()) {
                String loanNumber = item.getLoanNo();
                //Check if the view-button is enabled or disabled and show the schedule stage...
                boolean statusValue = item.getStatusLabel().getText().equals("Processing") || item.getStatusLabel().getText().equals("Pending Approval")
                        || item.getStatusLabel().getText().equals("Pending Payment");
                item.getViewButton().setDisable(statusValue);

                boolean editValue = item.getStatusLabel().getText().equals("Paid") || item.getStatusLabel().getText().equals("Cancelled");
                item.getEditButton().setDisable(editValue);

                //Handle Click event for the view-button
                item.getViewButton().setOnAction(action -> {
                    if (!item.getViewButton().isDisabled()) {
                        try {
                            ScheduleController.setSelectedLoanValue(loanNumber);
                            AppStages.loanScheduleStage();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                //Handles click event for the edit-button
                item.getEditButton().setOnAction(action -> {

                });
            }
        }
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

    @FXML void loadTableOnAction() {
        populateTable();
    }



}//end of class
