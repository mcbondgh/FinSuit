package app.controllers.finance;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.models.finance.FinanceModel;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.business.ClosedTellerTransactionEntity;
import app.repositories.business.SuspenseAccountEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class SuspenseAccountController extends FinanceModel implements Initializable {

    @FXML private TableView<SuspenseAccountEntity> accountTable;
    @FXML private TableColumn<SuspenseAccountEntity, Object> idColumn, nameColumn, statusColumn, dateColumn;
    @FXML private TextField overageField, shortageField, closureField;
    @FXML private MFXButton closeAccountButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        suspenseAccountTableAction();
        populateTable();
    }

    void populateTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("entryDateFormat"));
        accountTable.setItems(fetchSuspenseAccountData());
    }

    /********************************************************************************
    INPUT METHODS IMPLEMENTATION...
     **********************************************************************************/
    @FXML void validateOverageField(KeyEvent event){
        if (!event.getCharacter().matches("[0-9.]")) {
            overageField.deletePreviousChar();
        }
    }
    @FXML void validateShortageField(KeyEvent event) {
        if (!event.getCharacter().matches("[0-9.]")) {
            shortageField.deletePreviousChar();
        }
    }

    /********************************************************************************
        ACTION EVENT METHODS IMPLEMENTATION...
     **********************************************************************************/
    @FXML void clearAccountButtonClicked() {
        UserAlerts alerts;

        boolean emptySelection = accountTable.getSelectionModel().getSelectedItem() == null;

        if (emptySelection) {
            alerts = new UserAlerts("EMPTY SELECTION", "Please select a suspended account from in table to close",
                    "no selection was identified before clicking this button");
            alerts.errorAlert();
            return;
        }

        alerts = new UserAlerts("CLEAR SUSPENSE ACCOUNT", "Are you sure you want to clear suspended funds in this account?",
                "Please confirm to clear this suspense account, else CANCEL to abort..");

        if (alerts.confirmationAlert()) {
            long tableId = accountTable.getSelectionModel().getSelectedItem().getId();

            double overage = Double.parseDouble(overageField.getText().isBlank() ? "0.00" : overageField.getText());
            double shortage = Double.parseDouble(shortageField.getText().isEmpty() ? "0.00" : shortageField.getText());
            double currentBalance = Double.parseDouble(getBusinessAccountInformation().get("accountBalance").toString());
            double newBalance = (overage + shortage) + currentBalance;

            BusinessInfoEntity infoEntity = new BusinessInfoEntity();
            ClosedTellerTransactionEntity closedEntity = new ClosedTellerTransactionEntity();

            infoEntity.setPreviousBalance(currentBalance);
            infoEntity.setAccountBalance(newBalance);
            closedEntity.setIsSuspended((byte)0);
            closedEntity.setOverageAmount(overage);
            closedEntity.setShortageAmount(shortage);
            closedEntity.setId(tableId);

            int responseStatus = updateBusinessAccount(infoEntity);
            responseStatus+= updateSuspenseAccount(closedEntity);
            if (responseStatus == 2) {
                new UserNotification().successNotification("SUSPENSE ACCOUNT CLOSED", "Nice, you have successfully clear suspense account");
//                closeAccountButton.getScene().getWindow().hide();
                populateTable();
            }else {
                new UserNotification().errorNotification("FAILED EXECUTION", "Process to clear suspense account failed");
            }

        }



    }

    void suspenseAccountTableAction() {
        accountTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        accountTable.setOnMouseClicked(e -> {
            try {
                int selectedItem = accountTable.getSelectionModel().getSelectedItem().getId();
                fetchSuspenseAccountData().forEach(item -> {
                    if (Objects.equals(item.getId(), selectedItem)) {
                        overageField.setText(String.valueOf(item.getOverageAmount()));
                        shortageField.setText(String.valueOf(item.getShortageAmount()));
                        closureField.setText(String.valueOf(item.getClosureAmount()));
                    }
                });
            }catch (NullPointerException ignore) {}
        });
    }


}//end of class...
