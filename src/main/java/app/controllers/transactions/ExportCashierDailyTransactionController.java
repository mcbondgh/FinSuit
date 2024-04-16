package app.controllers.transactions;

import app.models.transactions.TransactionModel;
import app.repositories.transactions.TransactionsEntity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ExportCashierDailyTransactionController extends TransactionModel implements Initializable {

    /***************************************************************************************************
            FXML FILE EJECTION
     ****************************************************************************************************/
    @FXML private Button exportButton;
    @FXML private TableView<TransactionsEntity> transactionsTable;
    @FXML private TableColumn<TransactionsEntity, Integer> counterColumn;
    @FXML private TableColumn<TransactionsEntity, String> transIdColumn;
    @FXML private TableColumn<TransactionsEntity, String> transTypeColumn;
    @FXML private TableColumn<TransactionsEntity, Double> amountColumn;
    @FXML private TableColumn<TransactionsEntity, String> payMethodColumn;
    @FXML private TableColumn<TransactionsEntity, LocalTime> timeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




}//end of class...
