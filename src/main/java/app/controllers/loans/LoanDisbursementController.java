package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.loans.LoansModel;
import app.repositories.loans.DisbursementEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoanDisbursementController extends LoansModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFY = new UserNotification();
    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);

    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTIONS
     *******************************************************************************************************************/

    @FXML private TableView<DisbursementEntity> paymentTable;
    @FXML private TableColumn<DisbursementEntity, Integer> idColumn;
    @FXML private TableColumn<DisbursementEntity, String> loanNumberColumn;
    @FXML private TableColumn<DisbursementEntity, Double> amountColumn;
    @FXML private TableColumn<DisbursementEntity, Label> statusColumn;
    @FXML private TableColumn<DisbursementEntity, CheckBox> actionColumn;
    @FXML private MFXButton saveButton, clearButton;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    private boolean isTableEmpty() {
        return paymentTable.getItems().isEmpty();
    }

    /*******************************************************************************************************************
     *********************************************** OTHER METHODS IMPLEMENTATIONS.
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();
    }
    void populateTable() {
        paymentTable.getItems().clear();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loanNumberColumn.setCellValueFactory(new PropertyValueFactory<>("loanNumber"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("payBtn"));
        paymentTable.setItems(getUnpaidLoans());
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     *******************************************************************************************************************/

    @FXML void CheckTableSelection() {
        int checked = 0;
        for (DisbursementEntity check : paymentTable.getItems()) {
            if (check.getPayBtn().isSelected()) {
                checked ++;
            }
        }
        saveButton.setDisable(checked == 0);
    }
    public void setClearButtonOnAction(ActionEvent actionEvent) {
        paymentTable.getItems().forEach(item -> {
            if (item.getPayBtn().isSelected()) {
                item.getPayBtn().setSelected(false);
                saveButton.setDisable(!item.getPayBtn().isSelected());
            }
        });
    }
    @FXML private void saveButtonClicked() {
        ALERTS = new UserAlerts("SAVE PAYMENT", "Do you wish to save selected loans as disbursed funds?", "please confirm your action to save operation else CANCEL to abort.");
        if (ALERTS.confirmationAlert()) {
            int status = 0;
           for (DisbursementEntity item : paymentTable.getItems()) {
               if (item.getPayBtn().isSelected()) {
                   status = saveDisbursedLoans(item.getLoanNumber(), loggedInUserId);

               }
           }
           if (status > 0) {
               NOTIFY.successNotification("OPERATION SAVED", "You have successfully saved selected loan facilities as disbursed funds");
               populateTable();
           }
        }
    }
    @FXML private void selectAllChecked() {
        paymentTable.getItems().forEach(item -> {
            item.getPayBtn().setSelected(!item.getPayBtn().isSelected());
        });
    }

}//end of class...
