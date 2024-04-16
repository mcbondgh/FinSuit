package app.controllers.transactions;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.documents.DocumentGenerator;
import app.models.finance.FinanceModel;
import app.repositories.business.ClosedTellerTransactionEntity;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.transactions.TransactionsEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TellerDashboardController extends FinanceModel implements Initializable{
    NumberFormat currency = NumberFormat.getInstance();
    final int USER_ID = getUserIdByName(AppController.activeUserPlaceHolder);
    final String ACTIVE_USERNAME = AppController.activeUserPlaceHolder;
    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label currentBalaceLabel, loadedBalanceLabel, tellerAccountStatus, closingBalanceLabel;
    @FXML private VBox tellerBalanceContainer;
    @FXML private Label totalTransactionsLabel;
    @FXML private Button exportButton;

    @FXML private TextField cashField, overageField, shortageField;
    @FXML private TextArea commentsField;
    @FXML private MFXButton closeAccountButton, cancelButton;

    @FXML private TableView<TransactionsEntity> transactionsTable;
    @FXML private TableColumn<TransactionsEntity, Integer> counterColumn;
    @FXML private TableColumn<TransactionsEntity, String> transIdColumn;
    @FXML private TableColumn<TransactionsEntity, String> transTypeColumn;
    @FXML private TableColumn<TransactionsEntity, Double> amountColumn;
    @FXML private TableColumn<TransactionsEntity, String> payMethodColumn;
    @FXML private TableColumn<TransactionsEntity, LocalTime> timeColumn;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isTableEmpty() {return transactionsTable.getItems().isEmpty();}
    @FXML void validateCashField(KeyEvent event) {
        closeAccountButton.setDisable(cashField.getText().isBlank() || cashField.getText().isEmpty());
        if (cashField.isFocused() && !event.getCharacter().matches("[0-9.]")) {
            cashField.deletePreviousChar();
        }
        try {
            double currentBal = Double.parseDouble(currentBalaceLabel.getText().replace(",", ""));
            double result = currentBal + Double.parseDouble(cashField.getText());
            closingBalanceLabel.setText(currency.format(result));
        }catch (NumberFormatException ignore) {
            closingBalanceLabel.setText(currentBalaceLabel.getText());
        }
    }
    @FXML void validateOverageField(KeyEvent event) {
        if (overageField.isFocused() && !event.getCharacter().matches("[0-9.]")) {
            overageField.deletePreviousChar();
        }
    }
    @FXML void validateShortageField(KeyEvent event) {
        if (shortageField.isFocused() && !event.getCharacter().matches("[0-9.]")) {
            shortageField.deletePreviousChar();
        }
    }


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    public void initialize(URL url, ResourceBundle bundle){
        setCashierDashboardParameters();
        populateTable();

        AtomicReference<Double> totalTransactions = new AtomicReference<>(0.00);
        transactionsTable.getItems().forEach(item -> {
            totalTransactions.updateAndGet(v -> (v + item.getTotal_amount()));
        });
        totalTransactionsLabel.setText(currency.format(totalTransactions.get()));
    }

    void populateTable() {
        counterColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        transTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        payMethodColumn.setCellValueFactory(new PropertyValueFactory<>("payment_method"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("localTime"));
        transactionsTable.setItems(fetchTodayTransactionByCashierName(ACTIVE_USERNAME));
    }
    void clearFields() {
        cashField.clear();
        overageField.setText("0.00");
        shortageField.setText("0.00");
        commentsField.clear();
        commentsField.setPromptText("Write your notes here...");
        closingBalanceLabel.setText("0.00");
        updateCashierAccountClosureStatus();
    }

    void setCashierDashboardParameters() {
        // <=49% balance color -> #ffe3e3, <=74% color -> #b28446 >=75% color -> #4591b5
        String userName = AppController.activeUserPlaceHolder;

        getCashierCurrentAndLoadedBalance().forEach((key, value) -> {
            double loadedAmount = value.get(0);
            double currentAmount = value.get(1);
            double lessThan50 = (loadedAmount * 50) / 100;
            double lessThan75 = (loadedAmount * 75) / 100;

            if(currentAmount < lessThan50) {
                currentBalaceLabel.setStyle("-fx-text-fill:#ff0000");
                tellerBalanceContainer.setStyle("-fx-background-color:#ffe3e3; -fx-border-color:#ddd; -fx-border-radius:5px; -fx-background-radius:5px;");
            } else if(currentAmount >= lessThan50 && currentAmount < lessThan75) {
                currentBalaceLabel.setStyle("-fx-text-fill:#b28446");
                tellerBalanceContainer.setStyle("-fx-background-color:#fff; -fx-border-color:#ddd; -fx-border-radius:5px; -fx-background-radius:5px;");
            } else {
                currentBalaceLabel.setStyle("-fx-text-fill:#4591b5");
                tellerBalanceContainer.setStyle("-fx-background-color:#fff; -fx-border-color:#ddd; -fx-border-radius:5px; -fx-background-radius:5px;");
            }
            if (key.equals(userName)){
                loadedBalanceLabel.setText(currency.format(loadedAmount));
                currentBalaceLabel.setText(currency.format(currentAmount));
            }
        });
        Platform.runLater(this::updateCashierAccountClosureStatus);
    }

    private void updateCashierAccountClosureStatus() {
        // get and set account status label to CLOSED if query result is TRUE else set to OPEN if FALSE.
        Date today = Date.valueOf(LocalDate.now());
        boolean result = isCashierTransactionClosed(today, USER_ID);
        if (result) {
            tellerAccountStatus.setText("CLOSED");
            tellerAccountStatus.setStyle("-fx-text-fill:#ff0000");
        }else {
            tellerAccountStatus.setText("OPEN");
            tellerAccountStatus.setStyle("-fx-text-fill:#36ce25");
        }
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS
     ********************************************************************************************************************/
    @FXML void exportTableToPdf() throws InterruptedException {
        if (!isTableEmpty()) {
            String workId = getWorkIdByUserId(USER_ID);
            String empName = getEmployeeFullNameByWorkId(workId);
            String documentName = ACTIVE_USERNAME.concat(" " +String.valueOf(LocalDate.now()));
            Thread.sleep(2000);
            exportButton.setText("exporting..");
            exportButton.setDisable(true);
            Platform.runLater(() -> {
                int status = new DocumentGenerator().generateCashierTransactionSummarySheet(documentName, empName, transactionsTable);
                    if (status == 200) {
                        new UserNotification().informationNotification("EXPORT SUCCESSFUL", "PDF file successfully created. Navigate to directory to access file.");
                        exportButton.setText("EXPORT PDF");
                        exportButton.setDisable(false);
                    } else new UserNotification().informationNotification("EXPORT FAILED", "PDF file failed to export. Please refresh page and export again");
            });
        }else  {
            new UserNotification().errorNotification("EMPTY TABLE", "Table data is empty, nothing to export.");
        }
    }

    //This button when clicked shall collect and insert into the closed_teller_transaction_logs table
    //And set an indication that the transaction for the day pertaining to this teller has been officially closed.
    @FXML void closeCashierTransactions() {
        double loadedBalance = Double.parseDouble(loadedBalanceLabel.getText().replaceAll(",", ""));
        double cashAmount = Double.parseDouble(cashField.getText());
        double eCashAmount = Double.parseDouble(currentBalaceLabel.getText().replace(",", ""));
        double closingBalance = Double.parseDouble(closingBalanceLabel.getText().replace(",", ""));
        double overageAmount = Double.parseDouble(overageField.getText());
        double shortageAmount = Double.parseDouble(shortageField.getText());
        String note = commentsField.getText();

        UserAlerts ALERT = new UserAlerts("CLOSE DAILY TRANSACTION ACCOUNT", "Are you sure you want to close your transaction account" +
                "for the day?", "please confirm your action to close your , else CANCEL to abort.account");

        //check if the cashier's account has been loaded for the day. If TRUE proceed else diny the closure of the account.
        if (loadedBalance == 0.0) {
            new UserNotification().errorNotification("TRANSACTIONS CLOSURE FAILED",
                    "Sorry, you cannot close a empty account. Your start amount is 0.00");
            return;
        }

        //CHECK IF CASHIER TRANSACTIONS HAS BEEN CLOSED OR NOT.
        boolean statusResult = isCashierTransactionClosed(Date.valueOf(LocalDate.now()), USER_ID);
        if (statusResult) {
            new UserNotification().errorNotification("ACCOUNT CLOSED", "Your daily transaction account is closed for today");
            return;
        }


        if (ALERT.confirmationAlert()) {

            //set values for the entity class associated to this class to allow method get values to save.
            ClosedTellerTransactionEntity entity = new ClosedTellerTransactionEntity();
            entity.seteCash(eCashAmount);
            entity.setNotes(note);
            entity.setPhysicalCash(cashAmount);
            entity.setEnteredBy(USER_ID);
            entity.setOverageAmount(overageAmount);
            entity.setShortageAmount(shortageAmount);
            entity.setClosedAmount(closingBalance);
            entity.setStartAmount(loadedBalance);

            NotificationEntity notification = new NotificationEntity();
            notification.setTitle("DAILY TRANSACTION CLOSURE");
            notification.setMessage("Cashier successfully closed account for the day with total balance ".concat(closingBalanceLabel.getText()));
            notification.setLogged_by(USER_ID);
            notification.setSender_method("INTERNAL TRANSACTION");
            notification.setLogged_date(Timestamp.valueOf(LocalDateTime.now()));

            int responseStatus = closeCashierTransactions(entity);
            logNotification(notification);
            //check if data was successfully saved
            if(responseStatus > 0) {
                String title = "SUCCESSFULLY CLOSED";
                String body = "Nice, you have successfully closed your transactions account for today on ".concat(LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
                new UserNotification().successNotification(title, body);
                Platform.runLater(this::clearFields);
            } else {
                new UserNotification().errorNotification("FAILED TO SAVE", "Sorry your transaction account failed to close. Retry to save again");
            }
        }
    }


}//end of class...
