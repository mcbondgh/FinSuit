package app.controllers.finance;

import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.finance.FinanceModel;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.business.ClosedTellerTransactionEntity;
import app.repositories.transactions.TransactionsEntity;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Map;
import java.util.ResourceBundle;

public class CloseCashierTransactionController extends FinanceModel implements Initializable {

    final String ACTIVE_USERNAME = AppController.activeUserPlaceHolder;
    final int USER_ID = getUserIdByName(ACTIVE_USERNAME);

    /**************************************************************************************************
            FXML FILE EJECTION
     */
    @FXML
    private MFXLegacyListView<String> cashierListView;
    @FXML private TableView<TransactionsEntity> transactionsTable;
    @FXML private TableColumn<TransactionsEntity, Integer> noColumn;
    @FXML private TableColumn<TransactionsEntity, String> transIdColumn;
    @FXML private TableColumn<TransactionsEntity, String> transTypeColumn;
    @FXML private TableColumn<TransactionsEntity, Double> amountColumn;
//    @FXML private TableColumn<TransactionsEntity, String> payMethodColumn;
    @FXML private TableColumn<TransactionsEntity, LocalTime> timeColumn;

    @FXML private TextField overageField, shortageField;
    @FXML private TextArea commentsField;
    @FXML private Label closingBalanceLabel;
    @FXML private MFXButton closeAccountButton,suspenseAccountButton ;
    @FXML private CheckBox suspendCheckBox;

    /**************************************************************************************************
            IMPLEMENTATION OF OTHER METHODS
     */
    void populateCashierTransactionTable() {
        noColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        transTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
//        payMethodColumn.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("localTime"));
    }

    //THIS METHOD WHEN INVOKED SHALL LOAD THE ListView with the names of only active cashiers whose accounts were loaded during the day.
    void getActiveCashiers() {
        cashierListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        new FinanceModel().getCashierNamesWithUnclosedAccountStatus().forEach(item -> {
            cashierListView.getItems().add(item);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getActiveCashiers();
        populateCashierTransactionTable();
        cashierSelected();
    }


    boolean isListViewEmpty() {return cashierListView.getItems().isEmpty();}

    /*******************************************************************************************************************
            IMPLEMENTATION OF ACTION EVENT METHODS
     */

    public void validateOverageField(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            overageField.deletePreviousChar();
        }
    }

    public void validateShortageField(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            shortageField.deletePreviousChar();
        }
    }

    private void cashierSelected() {
        cashierListView.setOnMouseClicked(selected -> {
            boolean isItemSelected = cashierListView.getSelectionModel().getSelectedItem().isEmpty();
            closeAccountButton.setDisable(isItemSelected);
            if (!isListViewEmpty()){
                String cashierName = cashierListView.getSelectionModel().getSelectedItem();
                transactionsTable.setItems(fetchTodayTransactionByCashierName(cashierName));

                //create a map to hold the returned result containing the cashier's data.
                Map<String, Object> cashierData = getCashierClosureDetailsByName(cashierName);
                overageField.setText(cashierData.getOrDefault("overageAmount", "0.00").toString());
                shortageField.setText(cashierData.getOrDefault("shortageAmount", "0.00").toString());
                commentsField.setText(cashierData.getOrDefault("comment", "").toString());
                closingBalanceLabel.setText(cashierData.getOrDefault("balance", "0.00").toString());
            }
        });
    }
    @FXML void closeCashierTransactions() {
        UserNotification notify = new UserNotification();
        Map<String, Object> businessAccount = getBusinessAccountInformation();
        double overage = Double.parseDouble(overageField.getText() == null ? "0.00" : overageField.getText());
        double shortage = Double.parseDouble(shortageField.getText() == null ? "0.00" : shortageField.getText());
        double currentBal = Double.parseDouble(businessAccount.get("accountBalance").toString());
        double closureBal = Double.parseDouble(closingBalanceLabel.getText());

        //GET ID FROM closed_teller_transaction_logs table to update the closure account associated to teller.
        String cashierName = cashierListView.getSelectionModel().getSelectedItem();
        long closureTableId = getClosureTableIdByCashierId(getUserIdByName(cashierName));

        BusinessInfoEntity businessInfoEntity = new BusinessInfoEntity();
        ClosedTellerTransactionEntity cashierEntity = new ClosedTellerTransactionEntity();

        //set parameters to update database entities...
        cashierEntity.setClosedAmount(closureBal);
        cashierEntity.setShortageAmount(shortage);
        cashierEntity.setOverageAmount(overage);
        cashierEntity.setClosedBy(USER_ID);
        cashierEntity.setId(closureTableId);

        //Check If the suspend button has been checked.
        if (suspendCheckBox.isSelected()) {
            cashierEntity.setIsClosed((byte) 0);
            deleteCashierFromTemporalCashierTable(cashierName);
            int responseStatus = updateClosureTable(cashierEntity);
            if (responseStatus > 0) {
                notify.successNotification("CLOSURE SUSPENDED", "You have successfully suspended cashier's transaction closure.");
                getActiveCashiers();
            }
        }else {
            double newBalance = currentBal + closureBal;

            //set parameters to update database entities...
            businessInfoEntity.setAccountBalance(newBalance);
            businessInfoEntity.setPreviousBalance(currentBal);

            cashierEntity.setIsClosed((byte)1);
            
            //execute queries...
            int responseStatus = updateBusinessAccount(businessInfoEntity);
            responseStatus += updateClosureTable(cashierEntity);
            deleteCashierFromTemporalCashierTable(cashierName);
            
            if (responseStatus == 2) {
                notify.successNotification("TRANSACTION CLOSURE PROCESS", "Nice, you have successfully closed cashier's transactions.");
                getActiveCashiers();
            }else {
                notify.errorNotification("FAILED CLOSURE PROCESS", "An error occurred while trying to close cashier's transaction account.");
            }
        }



    }

    @FXML void viewSuspenseAccount() throws IOException {
        AppStages.suspenseAccountStage().show();
    }
}//end of class...
