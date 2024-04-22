package app.controllers.messages;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.models.message.MessagesModel;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.notifications.NotificationEntity;
import app.repositories.settings.TemplatesRepository;
import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageBoxController extends MessagesModel implements Initializable {

    UserNotification NOTIFY = new UserNotification();
    UserAlerts ALERT;

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle, listCounter, smsBalance;
    @FXML private WebView webView;
    @FXML
    TextField numberField ;
    @FXML private MFXButton addButton, sendButton, cancelButton, removeButton;
    @FXML
    private ListView<String> listView;

    @FXML private TabPane tabPane;
    @FXML private Tab notificationTab, logsTab, smsTab, topUpTab;
    @FXML private CheckBox customerCheckBox;
    @FXML private MFXFilterComboBox<String> customerNameSelector;
    @FXML private ComboBox<String> templateSelector;
    @FXML private JFXTextArea messageBodyField;
    @FXML private TextField messageTitleField, senderIdField;

    private static String currentUserPlaceHolder;
    public static String pageTitlePlaceHolder;
    public String getCurrentUserName() {return currentUserPlaceHolder;}
    public void setCurrentUserPlaceHolder(String userPlaceHolder) {
        currentUserPlaceHolder = userPlaceHolder;}


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isCustomerCheckBoxSelected() {
        return customerCheckBox.isSelected();
    }
    boolean isMobileNumberFieldEmpty() {
        return numberField.getText().isEmpty() || numberField.getText().isBlank();
    }
    boolean isTitleFieldEmpty(){return messageTitleField.getText().isEmpty();}
    boolean isMessageBodyEmpty() {return messageBodyField.getText().isEmpty();}
    boolean listViewEmpty() {return listView.getItems().isEmpty() || listView.getItems().size() == 0;}

    @FXML
    MFXLegacyTableView<NotificationEntity> notificationTable;
    @FXML TableColumn<NotificationEntity, Integer> notiNoColumn;
    @FXML TableColumn<NotificationEntity, Integer> notiTypeColumn;
    @FXML TableColumn<NotificationEntity, Integer> notiContentColumn;
    @FXML TableColumn<NotificationEntity, Integer> notiDateColumn;
    @FXML Label notificationTitle, notificationSentBy;
    @FXML ComboBox<Integer> limitSelector;
    @FXML TextField searchNotificationField;

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);

        populateFields();
    }

    void populateFields() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        customerNameSelector.setEditable(true);
        customerNameSelector.setResetOnPopupHidden(true);
        customerNameSelector.setScrollOnOpen(true);
        customerNameSelector.setSelectable(true);
        for (TemplatesRepository items : fetchMessageTemplates()) {
            templateSelector.getItems().add(items.getMessageTitle());
        }
        for (CustomersDataRepository items : fetchCustomersData()) {
            String fullName = items.getFirstname().concat(" " + items.getOthername() + " " + items.getLastname());
            customerNameSelector.getItems().add(fullName);
        }
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    @FXML void validateNumberField(KeyEvent event) {
        String numberValue = numberField.getText();
        if (!event.getCharacter().matches("[0-9]")) {
            numberField.deletePreviousChar();
        }
        if (numberValue.length() >= 11) {
            numberField.deletePreviousChar();
        }

        numberField.setOnKeyReleased(keyEvent -> {
            addButton.setDisable(isMobileNumberFieldEmpty());
            for (String value : listView.getItems()) {
                if (numberValue.equals(value)) {
                    NOTIFY.errorNotification("NUMBER EXIST", "This number already exists in the list of queued numbers.");
                    addButton.setDisable(true);
                    return;
                }
            }
        });
    }
    @FXML void enableRemoveButton() {
        removeButton.setDisable(listViewEmpty());
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML private void addButtonClicked() {
        addButton.setDisable(isMobileNumberFieldEmpty());
        String number = numberField.getText();
        int counter = Integer.parseInt(listCounter.getText());
        if (counter == 50) {
            NOTIFY.informationNotification("LIST FULL", "You have reached list capacity, you can process 50 numbers at a time.");
            addButton.setDisable(true);
        } else {
            listView.getItems().add(number);
            int increment = listView.getItems().size();
            listCounter.setText(String.valueOf(increment));
            numberField.clear();
            addButton.setDisable(true);
        }
    }

    @FXML void customerCheckBoxChecked() {
        customerNameSelector.setDisable(!isCustomerCheckBoxSelected());

    }

    public void removeButtonClicked(ActionEvent event) {
        ObservableList<String> selectedItems = listView.getSelectionModel().getSelectedItems();
        try {
            for (String item : selectedItems) {
                listView.getItems().removeAll(item);
                listCounter.setText(String.valueOf(listView.getItems().size()));
            }
        }catch (Exception ignore) {}

     }

     @FXML void templateSelectorOnAction() {
        String var = templateSelector.getValue();
        for (TemplatesRepository item : fetchMessageTemplates()) {
            if (var.equals(item.getMessageTitle())) {
                messageTitleField.setText(item.getMessageTitle());
                messageBodyField.setText(item.getMessageBody());
            }
        }
     }

     @FXML void NumberSelectorOnAction() {


     }


}//END OF CLASS
