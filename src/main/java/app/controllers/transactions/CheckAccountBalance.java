package app.controllers.transactions;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CheckAccountBalance implements Initializable {

    /*******************************************************************************************************************
                        FXML FILE EJECTIONS...
     *******************************************************************************************************************/
    @FXML
    private MFXFilterComboBox accountNumberField;
    @FXML private MFXButton getBalanceButton;
    @FXML private Label customerNameField, currentBalanceField, previousBalanceField;
    @FXML private Label lastWithdrawalAmountField, lastWithdrawalDate;
    @FXML private Label usernameLabel;
//    @FXML HiperLink receiptButton;





    /*******************************************************************************************************************
     FXML FILE EJECTIONS...
     *******************************************************************************************************************/



    /*******************************************************************************************************************
     FXML FILE EJECTIONS...
     *******************************************************************************************************************/



    /*******************************************************************************************************************
     FXML FILE EJECTIONS...
     *******************************************************************************************************************/



    /*******************************************************************************************************************
     FXML FILE EJECTIONS...
     *******************************************************************************************************************/


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void getBalanceButtonClicked(ActionEvent actionEvent) {
    }
}
