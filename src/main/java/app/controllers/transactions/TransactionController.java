package app.controllers.transactions;

import app.models.transactions.TransactionModel;
import app.repositories.transactions.TransactionsEntity;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController extends TransactionModel implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    public static String pageTitlePlaceHolder;
    @FXML private MFXButton depositButton, checkBalanceButton, withdrawalButton, loanPaymentButton;
    @FXML private BorderPane borderPane;
    @FXML private ComboBox<Integer> tableLimitSelector;
    @FXML private TextField searchField;

    @FXML private MFXLegacyTableView<TransactionsEntity> transactionsTable;
    @FXML private TableColumn<TransactionsEntity, String> transactionIdColumn;
    @FXML private TableColumn<TransactionsEntity, String> customerNameColumn;
    @FXML private TableColumn<TransactionsEntity, String> accountNumberColumn;
    @FXML private TableColumn<TransactionsEntity, String> transactionTypeColumn;
    @FXML private TableColumn<TransactionsEntity, String> methodColumn;
    @FXML private TableColumn<TransactionsEntity, Double> amountColumn;
    @FXML private TableColumn<TransactionsEntity, String> transactionDateColumn;
    @FXML private TableColumn<TransactionsEntity, String> cashierColumn;
    @FXML private  TableColumn<TransactionsEntity, String> transactionStatusColumn;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        checkBalanceButtonClicked();
        withdrawalButtonClicked();
        populateTable();
    }

    private void populateTable() {

        int[]values = {1, 5, 10, 20, 30, 50, 100, 200, 500};
        for (int value : values) {
            tableLimitSelector.getItems().add(value);
        }
        tableLimitSelector.setValue(values[2]);
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("account_number"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        cashierColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        transactionsTable.setItems(fetchTransactionLogs(tableLimitSelector.getValue()));
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    public void filterTransactionsTable(KeyEvent event) {
        try {
            transactionsTable.getItems().clear();
            FilteredList<TransactionsEntity> filteredList =  new FilteredList<>(fetchTransactionLogs(tableLimitSelector.getValue()), p -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(transactions -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (transactions.getFullname().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else if (transactions.getTransaction_id().toLowerCase().contains(searchKeyWord)) {
                        return true;}
                        else if (transactions.getTransaction_type().toLowerCase().contains(searchKeyWord)) {
                            return true;
                    } else return transactions.getAccount_number().toLowerCase().contains(searchKeyWord);
                });
            });
            SortedList<TransactionsEntity> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(transactionsTable.comparatorProperty());
            transactionsTable.setItems(sortedResult);
        }catch (Exception ignored) {}
    }

    public void reloadTableOnLimit() {
        transactionsTable.getItems().clear();
        transactionsTable.setItems(fetchTransactionLogs(tableLimitSelector.getValue()));
    }
    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML void depositButtonClicked() throws IOException {
        AppStages.depositStage().show();
    }

    void withdrawalButtonClicked() {
        withdrawalButton.setOnAction(e -> {
            try {
                AppStages.withdrawalStage().show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    void checkBalanceButtonClicked() {
        checkBalanceButton.setOnAction(e -> {
            try {
                AppStages.accountBalanceStage().show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML void viewTransactionOnAction() {
        try {
            AppStages.accountBalanceStage().show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}//end of class...
