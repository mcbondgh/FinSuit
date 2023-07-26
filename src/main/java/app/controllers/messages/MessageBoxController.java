package app.controllers.messages;

import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckListView;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageBoxController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle, listCounter;
    public static String pageTitlePlaceHolder;
    @FXML private WebView webView;
    @FXML
    TextField numberField;
    @FXML private MFXButton addButton, smsButton, notificationsButton;
    @FXML
    private MFXCheckListView<String> listView;
    @FXML
    private BorderPane borderPane;
    @FXML private TabPane tabPane;
    @FXML private CheckBox customerCheckBox;
    @FXML private MFXFilterComboBox<String> customerNameSelector;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isCustomerCheckBoxSelected() {
        return customerCheckBox.isSelected();
    }
    boolean isMobileNumberFieldEmpty() {
        return numberField.getText().isEmpty();
    }

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { pageTitle.setText(pageTitlePlaceHolder) ;}

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    @FXML void validateNumberField() {
        addButton.setDisable(isMobileNumberFieldEmpty());
    }



    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML private void addButtonClicked() {
        addButton.setDisable(isMobileNumberFieldEmpty());
        String number = numberField.getText();
        listView.getItems().add(number);
        int increment = listView.getItems().size();
        listCounter.setText(String.valueOf(increment));
    }
    @FXML void smsButtonClicked() {
        tabPane.setVisible(true);
        borderPane.setCenter(tabPane);
    }
    @FXML void notificationsButtonClicked() throws IOException {
        String fxmlFile = "views/messageBox/notifications-page.fxml";
        tabPane.setVisible(false);
        SpecialMethods.FlipView(fxmlFile, borderPane);
    }
    @FXML void customerCheckBoxChecked() {
        customerNameSelector.setDisable(!isCustomerCheckBoxSelected());
    }

}//END OF CLASS
