package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.controllers.loans.application.UpdateApplicantLoanController;
import app.models.loans.LoansModel;
import app.repositories.loans.LoansTableEntity;
import app.repositories.notifications.NotificationEntity;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoansController extends LoansModel implements Initializable {

    Stage loanApplicationStage = AppStages.loanApplicationStage();
    int USER_ID = getUserIdByName(AppController.activeUserPlaceHolder);

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;

    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML private HBox hBox;
    @FXML private VBox anchorPane;
    @FXML private MenuButton menuButton;
    @FXML
    private MenuItem addNewLoanButton, disburseFundBtn, generateSheetButton, viewLoansButton;
    @FXML private MenuItem loanAssignmentButton, repaymentButton, unprocessedLoansButton;
    @FXML private MFXButton uploadSheetButton, loadTableButton;
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
            disburseLoanButtonOnAction();
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
        viewColumn.setCellValueFactory(new PropertyValueFactory<>("cancelButton"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("editButton"));
        loanApplicantsTable.setItems(getLoansUnderApplicationStage(USER_ID));
    }

    private void showRequestedLoanCount() {
        int counter = countRequestedLoans();
        int counter1 = countAssignedLoans();
        int counter2 = countUnpaidLoans();
        unprocessedLoansButton.setText(unprocessedLoansButton.getText() + " (" + counter1 +")");
        loanAssignmentButton.setText(loanAssignmentButton.getText() + " (" + counter +")");
        disburseFundBtn.setText(disburseFundBtn.getText () + " (" + counter2  + ")");
    }
    public void searchCustomerMethod(KeyEvent event) {
        try {
            loanApplicantsTable.getItems().clear();
            FilteredList<LoansTableEntity> filteredList =  new FilteredList<>(getLoansUnderApplicationStage(), p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) ->
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
            }));
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
                //Check if the cancel-button is enabled or disabled and show the schedule stage...
                boolean statusValue =
                        item.getStatusLabel().getText().equals("Cancelled");
                        item.getCancelButton().setDisable(statusValue);

                boolean editValue = item.getStatusLabel().getText().equals("Paid")
                        || item.getStatusLabel().getText().equals("Cancelled")
                        || item.getStatusLabel().getText().equals("Pending Approval")
                        ||item.getStatusLabel().getText().equals("Pending Payment");;
                item.getEditButton().setDisable(editValue);

                //Handle Click event for the view-button
                item.getCancelButton().setOnAction(action -> {
                    if (!item.getCancelButton().isDisabled()) {
                        try {
                            Stage stage = AppStages.cancellationStage();
                            CancellationController.LOAN_NUMBER = loanNumber;
                            CancellationController.USER_ID = USER_ID;
                            stage.showAndWait();
                            stage.setOnHidden(hidden -> {
                                populateTable();
                            });

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                //Handles click event for the edit-button
                item.getEditButton().setOnAction(action -> {
                    try {
                        UpdateApplicantLoanController.setLoanNumber = loanNumber;
                        Stage stage = AppStages.editLoanApplicantDetails();
                        stage.show();
                        stage.setOnHidden(event -> {
                            populateTable();
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
    @FXML public void clearedLoansBtnClicked() {
        try {
            borderPane.getChildren().remove(0);
            String fxmlFile = "views/loans/paid-loans-view.fxml";
            SpecialMethods.FlipView(borderPane, fxmlFile );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void loanAssignmentButtonClicked() {
        try {
            borderPane.getChildren().remove(0);
            String fxmlFile = "views/loans/loan-supervisor-assignment-view.fxml";
            SpecialMethods.FlipView(borderPane, fxmlFile );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void newLoanButtonClicked(ActionEvent event) throws IOException {
        loanApplicationStage.show();
    }
    void disburseLoanButtonOnAction() {
        disburseFundBtn.setOnAction(event -> {
            try {
                borderPane.getChildren().remove(0);
                String fxmlFile = "views/loans/loan-disbursement-page.fxml";
                SpecialMethods.FlipView(borderPane, fxmlFile );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @FXML void loadTableOnAction() {
        borderPane.setCenter(anchorPane);
        populateTable();
    }
    @FXML void unprocessedLoansButtonClicked() throws IOException {
        AppStages.loanCalculatorStage().show();
    }

    @FXML void loanRepaymentButtonClicked() {
        try {
            AppStages.loanScheduleAndRepaymentStage().showAndWait();
//            borderPane.getChildren().remove(0);
//            String fxmlFile = "views/loans/loan-schedule-page.fxml";
//            SpecialMethods.FlipView(borderPane, fxmlFile );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML void sheetButtonClicked() {
        try {
            borderPane.getChildren().remove(0);
            String fxmlFile = "views/loans/collection-sheet-view.fxml";
            SpecialMethods.FlipView(borderPane, fxmlFile );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}//end of class
