package app.controllers.finance;

import app.models.finance.FinanceModel;
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
