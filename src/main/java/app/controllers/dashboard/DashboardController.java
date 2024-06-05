package app.controllers.dashboard;

import app.config.sms.SmsAPI;
import app.models.MainModel;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DashboardController extends MainModel implements Initializable {

    SmsAPI SMS_API = new SmsAPI();

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML private Label pageTitle, dateLabel;
    public static String pageTitlePlaceHolder;
    @FXML private Label smsBalanceLabel, dueLoansLabel, approvedLoansLabel, partiallyPaidLabel;
    @FXML private Label totalCustomersLabel, paidLoansLabel, loanApplicationLabel, disbursedLoansLabel;
    @FXML private MFXLegacyTableView<TransactionsEntity> transactionsTable;
    @FXML private TableColumn<TransactionsEntity, String> transactionIdColumn;
    @FXML private  TableColumn<TransactionsEntity, String> transactionTypeColumn;
    @FXML private TableColumn<TransactionsEntity, LocalTime> timeColumn;
    @FXML private  TableColumn<TransactionsEntity, Double> amountColumn;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        SpecialMethods.generateTime(dateLabel);
        setVariables();
    }

    void setVariables() {
        int applicationCounter = 0;

        totalCustomersLabel.setText(String.valueOf(totalCustomersCount()));
        disbursedLoansLabel.setText(String.valueOf(getTotalDisbursedLoans()));
        approvedLoansLabel.setText(String.valueOf(getTotalApprovedLoansCount()));
        dueLoansLabel.setText(String.valueOf(getMonthlyDueLoans()));
        partiallyPaidLabel.setText(String.valueOf(getPartiallyPaidLoans()));
        loanApplicationLabel.setText(String.valueOf(getTotalLoanRequests()));
        paidLoansLabel.setText(String.valueOf(getTotalPaidLoans()));
        try {
            smsBalanceLabel.setText(SMS_API.getSmsBalance());
        }catch (Exception ignored) {}

        //LOADING THE transactionsTable with specified column data...
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionStatus"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("localTime"));
        transactionsTable.setItems(getTodayTransactionLogs());
    }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** ACTION EVENTS METHODS IMPLEMENTATION
     ********************************************************************************************************************/





}//END OF CLASS...
