package app.controllers.reports.ui;

import app.models.MainModel;
import app.repositories.transactions.TransactionsEntity;
import app.specialmethods.SpecialMethods;
import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TransactionReports extends MainModel implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS ***********************************************/

    @FXML
    private CheckBox transactionCheckBox;
    @FXML private HBox transDetailedPane;
    @FXML private MFXButton transactionGenerateBtn, transactionExportBtn;
    @FXML private ComboBox<Integer> limitSelector;
    @FXML private DatePicker transStartDatePicker, transEndDatePicker;
    @FXML private TableView<TransactionsEntity> transactionLogsTable;
    @FXML private Label cashSumLabel,eCashSumLabel;
    @FXML private MFXComboBox<String> transactionTypeBox, transactionIdBox, accountNumberBox;
    @FXML private DatePicker startDatePicker, endDatePicker;



    /*******************************************************************************************************************
     *********************************************** OTHER METHODS. ****************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        SpecialMethods.setRateValue(limitSelector);
        loadSelectors();
    }

    private void loadSelectors() {
        limitSelector.setValue(50);
        SpecialMethods.setTransactionTypes(transactionTypeBox);
        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        int limit = limitSelector.getValue();
        fetchTransactionLogsReport(start, end, limit).forEach(data -> {
            if (!transactionIdBox.getItems().contains(data.getTransaction_id())){
                transactionIdBox.getItems().add(data.getTransaction_id());
            }
            if (!accountNumberBox.getItems().contains(data.getAccount_number())){
                accountNumberBox.getItems().add(data.getAccount_number());
            }
         });
    }


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE METHODS *********************************************/
    boolean isDetailedCheckBoxSelected(){return transactionCheckBox.isSelected();}






    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION *******************************************/







    /*******************************************************************************************************************
     *********************************************** ACTION EVENTS METHODS IMPLEMENTATION ******************************/
    @FXML void detailedLogsSelected() {
        transDetailedPane.setVisible(isDetailedCheckBoxSelected());
    }


}//end of class
