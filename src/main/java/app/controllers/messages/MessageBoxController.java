package app.controllers.messages;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.sms.SmsAPI;
import app.controllers.homepage.AppController;
import app.enums.MessageStatus;
import app.models.message.MessagesModel;
import app.repositories.SmsAPIEntity;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.accounts.ViewCustomersTableDataRepository;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageLogsEntity;
import app.repositories.settings.TemplatesRepository;
import app.repositories.users.UsersData;
import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
    @FXML private TextArea messageContentField;

    @FXML private TextArea smsContentField;
    @FXML private TextField smsSearchField;

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
    boolean listViewEmpty() {return listView.getItems().isEmpty() || listView.getItems().isEmpty();}

    @FXML
    TableView<NotificationEntity> notificationTable;
    @FXML TableColumn<NotificationEntity, Integer> notiNoColumn;
    @FXML TableColumn<NotificationEntity, Integer> notiTitleColumn;
    @FXML TableColumn<NotificationEntity, Integer> notiTypeColumn;
    @FXML TableColumn<NotificationEntity, Integer> notiDateColumn;
    @FXML Label notificationSentBy;
    @FXML ComboBox<Integer> limitSelector;
    @FXML TextField searchNotificationField;

    @FXML
    TableView<MessageLogsEntity> smsTable;
    @FXML TableColumn<MessageLogsEntity, Integer> idColumn;
    @FXML TableColumn<MessageLogsEntity, String> recipientColumn;
    @FXML TableColumn<MessageLogsEntity, String> statusColumn;
    @FXML TableColumn<MessageLogsEntity, String> sendDateColumn;
    @FXML TableColumn<MessageLogsEntity, String> titleColumn;
    @FXML TableColumn<MessageLogsEntity, MFXButton> actionColumn;



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        setCurrentUserPlaceHolder(AppController.activeUserPlaceHolder);
        setTableData();
        populateFields();
        limitSelector.setValue(10);
        populateSMSTable();
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

        int[] values = {1, 5, 10, 20, 30, 40, 50, 100, 200, 500};
        for (int value : values) {
            limitSelector.getItems().add(value);
        }
    }

    void setTableData() {
        notiNoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        notiTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        notiTypeColumn.setCellValueFactory(new PropertyValueFactory<>("sender_method"));
        notiDateColumn.setCellValueFactory(new PropertyValueFactory<>("localDate"));
        notificationTable.setItems(getAllNotifications(10));
        setReadAndUnreadNotifications();
    }

    void populateSMSTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("log_id"));
        recipientColumn.setCellValueFactory(new PropertyValueFactory<>("recipient"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
        sendDateColumn.setCellValueFactory(new PropertyValueFactory<>("date_sent"));
        actionColumn.setCellValueFactory( new PropertyValueFactory<>("resendButton"));
        smsTable.setItems(getAllMessageLogs());
    }

    void setReadAndUnreadNotifications() {
        notificationTable.setRowFactory(item -> new TableRow<>(){
            @Override
            protected void updateItem(NotificationEntity notification, boolean b) {
                super.updateItem(notification, b);
                if (!b && notification !=null) {
                    if (Objects.equals(notification.getIsRead(), false)) {
                        setStyle("-fx-font-color:red; -fx-font-weight:bold; -fx-font-family:poppins medium;");
                    }else setStyle("-fx-color:#ddd; -fx-font-weight:regular; -fx-font-family:poppins medium;");
                }
            }
        });
    }

    private void simulateMessageResend(@NotNull MessageLogsEntity item, String number, String content, int msgId) {
        try {
            item.getResendButton().setDisable(true);
            item.getResendButton().setText("Sending...");
            int userId = getUserIdByName(getCurrentUserName());
            Timeline timeline = new Timeline(new KeyFrame(Duration.INDEFINITE));
            timeline.play();
            String responseStatus = new SmsAPI().sendSms(number, content);

            if (!responseStatus.isEmpty()) {
                NOTIFY = new UserNotification();
                String smsStatus = MessageStatus.getMessageStatusResult(responseStatus).toString();
                updateSMSstatus(responseStatus, content, msgId, userId);
                NOTIFY.successNotification("SMS STATUS", "SMS status is " + smsStatus);
                timeline.stop();
                populateSMSTable();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void searchCustomerMethod(KeyEvent event) {
        try {
//            customersTable.getItems().clear();
            FilteredList<MessageLogsEntity> filteredList =  new FilteredList<>(getAllMessageLogs(), p -> true);
            smsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(smsData -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (smsData.getRecipient().toLowerCase().contains(searchKeyWord)) {
                        return true;
                    } else return smsData.getStatus().toLowerCase().contains(searchKeyWord);
                });
            });
            SortedList<MessageLogsEntity> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(smsTable.comparatorProperty());
            smsTable.setItems(sortedResult);
        }catch (Exception ignored) {}
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


    public void searchSMS(KeyEvent event) {
        try {
            FilteredList<MessageLogsEntity> filteredList =  new FilteredList<>(getAllMessageLogs(), p -> true);
            smsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(smsData -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (smsData.getRecipient().contains(searchKeyWord)) {
                        return true;
                    } else return smsData.getStatusLabel().getText().toLowerCase().contains(searchKeyWord);
                });
            });
            SortedList<MessageLogsEntity> sortedResult = new SortedList<>(filteredList);
            sortedResult.comparatorProperty().bind(smsTable.comparatorProperty());
            smsTable.setItems(sortedResult);
        }catch (Exception ignored) {}
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

     @FXML void limitSelectorOnAction() {
        notificationTable.getItems().clear();
        notificationTable.setItems(getAllNotifications(limitSelector.getValue()));
     }

     @FXML void notificationSelected() {
        boolean itemSelected = notificationTable.getSelectionModel().isEmpty();
        if (!itemSelected) {
            UsersData username = notificationTable.getSelectionModel().getSelectedItem().getUsername();
            String message = notificationTable.getSelectionModel().getSelectedItem().getMessage();
            long messageId = notificationTable.getSelectionModel().getSelectedItem().getId();
            notificationSentBy.setText(username.getUsername());
            messageContentField.setText(message);
            updateNotificationTable(messageId);
        }
     }

     @FXML void smsItemSelected() {
        boolean tableEmpty = smsTable.getItems().isEmpty();
        if(!tableEmpty) {
            String content = smsTable.getSelectionModel().getSelectedItem().getMessage();
            smsContentField.setText(content);

            smsTable.getItems().forEach(item-> {
                item.getResendButton().setOnAction(clicked -> {
                   ALERT = new UserAlerts("RESEND SMS", "Do you wish to resend SMS to this customer?",
                           "please confirm action to resend else CANCEL to abort");
                   if (ALERT.confirmationAlert()) {
                       String number = item.getRecipient();
                       int msgId = item.getLog_id();
                       Platform.runLater(()-> {
                           simulateMessageResend(item, number, smsContentField.getText(), msgId);
                       });
                   }
                });
            });
        }

     }




}//END OF CLASS
