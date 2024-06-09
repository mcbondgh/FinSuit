package app.controllers.settings;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.encryptDecryp.EncryptDecrypt;
import app.controllers.homepage.AppController;
import app.models.MainModel;
import app.models.settings.SettingModel;
import app.repositories.SmsAPIEntity;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.operations.PermissionsEntity;
import app.repositories.settings.TemplatesRepository;
import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXButton;

import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import net.synedra.validatorfx.Validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingsController extends SettingModel implements Initializable{
    Logger logger = Logger.getLogger("error");
    Validator validator = new Validator();
    MainModel MODEL_OBJECT = new MainModel();
    UserAlerts  ALERT_OBJECT;
    UserNotification NOTIFICATION_OBJECT = new UserNotification();
    String ACTIVE_USER = AppController.activeUserPlaceHolder;

    private static String currentUserPlaceHolder;

    public String getCurrentUser() {
        return currentUserPlaceHolder;
    }
    public void setCurrentUser(String currentUser) {
        currentUserPlaceHolder = currentUser;
    }
    


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    public static String pageTitlePlaceHolder;
    @FXML
    private TextField institutionNameField,emailField, numberField, otherNumberField, digitalAddField, locationField;
    @FXML private TextField senderIdField, senderMailField;
    @FXML private MFXButton updateButton, uploadButton, updateSenderIdButton;
    @FXML private Pane systemInfoPane, apiPane, warningPane;
    @FXML private ImageView logoViewer;
    @FXML private Label imageName, apiKeyField;
    @FXML private CheckBox modifyButton;
    @FXML private ComboBox<String> templateSelector, operationSelector;
    @FXML private PasswordField accountPasswordField, passwordField;
    @FXML private TextField loanPercentageField;
    @FXML private Label percentageIndicator, taxIndicator;
    @FXML private ListView<String>templateListView;
    @FXML private JFXTextArea messageBodyField;
    @FXML private TextField messageTitleField, withdrawalRateField;
    @FXML private MFXButton saveTemplateButton, cancelTemplateButton, saveOperationButton;




    Tooltip tooltip;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isNameFieldEmpty() {return institutionNameField.getText().isEmpty();}
    boolean isNumberFieldEmpty() {return numberField.getText().isEmpty();}
    boolean isEmailFieldEmpty() {return emailField.getText().isEmpty();}
    boolean isDigitalFieldEmpty() {return digitalAddField.getText().isEmpty();}
    boolean isSenderIdEmpty() {return senderIdField.getText().isEmpty();}
    boolean isAccountFieldEmpty(){return accountPasswordField.getText().isEmpty();}
    boolean isSenderMailFieldEmpty() {return senderMailField.getText().isEmpty();}
    boolean isPasswordFieldEmpty() {return passwordField.getText().isEmpty();}
    boolean isModifyButtonChecked() {return modifyButton.isSelected();}
    boolean isTitleFieldEmpty() {return messageTitleField.getText().isEmpty();}
    boolean isMessageFieldEmpty() {return messageBodyField.getText().isEmpty();}

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        validateInputFields();
        setCurrentUser(AppController.activeUserPlaceHolder);
        try {
            fillFields();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        operationSelector.setItems(getMessageOperations());
        actionEventMethods();
        populateFields();
        fillUserRolesSelector();
        actionEventMethodsImplementationForRolesAndPermission();
    }

    void populateFields() {
        templateListView.getItems().clear();
        for (TemplatesRepository value : fetchMessageTemplates()) {
            templateListView.getItems().add(value.getMessageTitle());
            templateSelector.getItems().add(value.getMessageTitle());
        }
        messageBodyField.clear();
        messageTitleField.clear();
    }

    void uploadLogo() throws IOException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose File");
            fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("mage Files", "*.png", "*.jpg"));
            File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            String filePath = selectedFile.getPath();
            imageName.setText(selectedFile.getName());
            Image logo = new Image(filePath);
            logoViewer.setImage(logo);
        }catch (NullPointerException e) {
            logger.logp(Level.SEVERE, this.getClass().getSimpleName(), "SettingsController::uploadLogo", e.getLocalizedMessage());
        }
    }

    byte[] getLogoData() throws IOException {
        byte[] data = null;
        try{
            data = new FileInputStream(logoViewer.getImage().getUrl()).readAllBytes();
        }catch (Exception ignore){
            data = MODEL_OBJECT.getBusinessInfo().get(0).getLogo();
        }
        return data;
    }
    void fillFields() throws IOException {
        for (BusinessInfoEntity item : MODEL_OBJECT.getBusinessInfo()) {
              institutionNameField.setText(item.getName());
              numberField.setText(item.getMobileNumber());
              otherNumberField.setText(item.getOtherNumber());
              emailField.setText(item.getEmail());
              locationField.setText(item.getLocation());
              digitalAddField.setText(item.getDigital());
              imageName.setText("business-logo");
//              accountPasswordField.setText(item.getAccountPassword());
              double percentageValue = item.getLoanPercentage();
              double taxValue = item.getTaxPercentage();
              loanPercentageField.setText(String.valueOf(percentageValue));
              percentageIndicator.setText(percentageValue + "% of basic Salary" );
//              String getImageSource = ImageReadWriter.displayImage(item.getLogo());
              logoViewer.setImage(new Image(new ByteArrayInputStream(item.getLogo())));
              withdrawalRateField.setText(String.valueOf(taxValue));
              taxIndicator.setText(taxValue + "% of withdrawal amount");
        }

        for (SmsAPIEntity items : MODEL_OBJECT.getSmsApi()) {
            apiKeyField.setText(items.getKey());
            senderIdField.setText(items.getSender_id());
            passwordField.setText(items.getPassword());
            senderMailField.setText(items.getEmailAddress());
        }
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    void validateInputFields() {
        int inputLength = 10;
            numberField.setOnKeyReleased(keyEvent ->{
                if (!(keyEvent.getCode().isDigitKey() || keyEvent.getCode().isArrowKey() || keyEvent.getCode().equals(KeyCode.BACK_SPACE))) {
                    numberField.deletePreviousChar();
                    numberField.deleteNextChar();
                }
                if (numberField.getText().length() > inputLength) {
                    numberField.deletePreviousChar();
                    numberField.deleteNextChar();
                }
            });

            otherNumberField.setOnKeyReleased(keyEvent ->{
                if (!(keyEvent.getCode().isDigitKey() || keyEvent.getCode().isArrowKey() || keyEvent.getCode().equals(KeyCode.BACK_SPACE))) {
                    otherNumberField.deleteNextChar();
                    otherNumberField.deletePreviousChar();
                }
                if (otherNumberField.getText().length() > inputLength) {
                    otherNumberField.deleteNextChar();
                    otherNumberField.deletePreviousChar();
                }
            });

            systemInfoPane.setOnMouseMoved(mouseEvent -> {
                updateButton.setDisable(isNameFieldEmpty() || isNumberFieldEmpty() || isEmailFieldEmpty() || isDigitalFieldEmpty());
            });
            apiPane.setOnMouseMoved(mouseEvent -> {
                updateSenderIdButton.setDisable(isSenderIdEmpty() || isSenderMailFieldEmpty()  || isPasswordFieldEmpty());
            });

            senderIdField.setOnKeyPressed(event -> {
                if (senderIdField.getText().length() > 10) {
                    senderIdField.deleteNextChar();
                    senderIdField.deletePreviousChar();
                }
            });
            loanPercentageField.setOnKeyTyped(keyEvent -> {
                String value = loanPercentageField.getText();
                if (!keyEvent.getCharacter().matches("[0-9.]")) {
                    loanPercentageField.deletePreviousChar();
                }
                percentageIndicator.setText(value + "% of basic Salary");
            });
            withdrawalRateField.setOnKeyTyped(keyEvent -> {
                String value = withdrawalRateField.getText();
                if (!keyEvent.getCharacter().matches("[0-9.]")) {
                    withdrawalRateField.deletePreviousChar();
                }
                taxIndicator.setText(value + "% of input");
            });

    }

    @FXML void checkMessagesTitleName() {
        String value = messageTitleField.getText().toUpperCase();
        for (Object title : templateListView.getItems()) {
            if (value.matches(title.toString())) {
                ALERT_OBJECT = new UserAlerts("INVALID TITLE", "Sorry, message title must be unique from your list of titles");
                ALERT_OBJECT.warningAlert();
                messageTitleField.clear();
                break;
            }
        }
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    void actionEventMethods() {
        AtomicReference<Byte> flag = new AtomicReference<>((byte) 0);
        uploadButton.setOnAction(action -> {
            try {
                uploadLogo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        updateButton.setOnAction(action -> {
            String name = institutionNameField.getText();
            String number = numberField.getText();
            String otherNumber = otherNumberField.getText();
            String email = emailField.getText();
            String digital = digitalAddField.getText();
            String location = locationField.getText();
            byte[] logoImage = null;
            try {
                logoImage = getLogoData();
            } catch (IOException e) {throw new RuntimeException(e);

            }
            String hashedValue = EncryptDecrypt.hashPlainText(accountPasswordField.getText().isBlank() ? EncryptDecrypt.DEFAULT_PASSWORD : accountPasswordField.getText());
            double percentageValue = Double.parseDouble(loanPercentageField.getText());
            double taxPercentage = Double.parseDouble(withdrawalRateField.getText());

            ALERT_OBJECT = new UserAlerts("UPDATE SYSTEM CONFIG", "ARE YOU SURE YOU WANT TO UPDATE SYSTEM PARAMETERS?",
                    "please confirm your action to proceed else CANCEL to abort");
            if (ALERT_OBJECT.confirmationAlert()) {
                flag.set(updateBusinessInfo(name, number, otherNumber, email, hashedValue, digital, location, logoImage, percentageValue, taxPercentage));

                if (flag.get() > 0) {
                    NOTIFICATION_OBJECT.successNotification("UPDATE SUCCESSFUL", "System parameters successfully update");
                }
            }
        });
        updateSenderIdButton.setOnAction(event -> {
            String senderId = senderIdField.getText();
            String email = senderMailField.getText();
            String password = passwordField.getText();
            ALERT_OBJECT = new UserAlerts("UPDATE SMS VARIABLE", "ARE YOU SURE YOU WANT TO UPDATE SMS SENDER ID?", "proceed to update else CANCEL to abort.");
            if (ALERT_OBJECT.confirmationAlert()) {
                flag.set(updateSenderId(senderId, email, password));
                if (flag.get() > 0) {
                    NOTIFICATION_OBJECT.successNotification("SENDER ID UPDATE", "You have successfully updated SMS Sender Id.");
                }
            }
        });
        modifyButton.setOnAction(event -> {
            senderMailField.setDisable(!isModifyButtonChecked());
            passwordField.setDisable(!isModifyButtonChecked());
            warningPane.setVisible(isModifyButtonChecked());
        });

    }
    @FXML
    void saveTemplateButtonClicked() {
        int userId = MODEL_OBJECT.getUserIdByName(getCurrentUser());
        if (!(isTitleFieldEmpty() || isMessageFieldEmpty())) {
            String title = messageTitleField.getText().toUpperCase();
            String message = messageBodyField.getText();
            if (saveTemplateButton.getText().equals("Save")) {
                ALERT_OBJECT = new UserAlerts("SAVE TEMPLATE", "Do you wish to add current message to your templates?");
                if (ALERT_OBJECT.confirmationAlert()) {
                    if(saveMessageTemplate(title, message, userId) > 0) {
                        NOTIFICATION_OBJECT.successNotification("SAVE SUCCESS", "Message successfully added to your templates.");
                        populateFields();
                    }
                }
            } else {
                int templateId = 0;
                String updatedTitle = "";
                for (TemplatesRepository item : fetchMessageTemplates()) {
                    if (title.matches(item.getMessageTitle())) {
                        updatedTitle = messageTitleField.getText();
                        templateId = item.getTemplateId();
                        break;
                    }
                }
                updateMessageTemplate(templateId, updatedTitle, message, userId);
                NOTIFICATION_OBJECT.successNotification("UPDATE SUCCESSFUL", "Message template successfully update.");
            }
        }
    }
    @FXML void getSelectedTemplate() {
        if (!templateListView.getItems().isEmpty()) {
            String selectedItem = templateListView.getSelectionModel().getSelectedItem();
            for (TemplatesRepository item : fetchMessageTemplates())  {
                if (selectedItem.matches(item.getMessageTitle())) {
                    messageTitleField.setText(item.getMessageTitle());
                    messageBodyField.setText(item.getMessageBody());
                }
            }
            saveTemplateButton.setText("Update");
        }
    }
    @FXML void clearSelectionButtonClicked() {
        populateFields();
        saveTemplateButton.setText("Save");
    }

    @FXML void saveOperationButtonClicked() {
        boolean emptySelection = operationSelector.getValue() == null || templateSelector.getValue() == null;
        if (!emptySelection) {
            String tempTitle = templateSelector.getValue();
            String operation = operationSelector.getValue();
            int temId = 0;
            for (TemplatesRepository item : fetchMessageTemplates()) {
                if (tempTitle.equalsIgnoreCase(item.getMessageTitle())) {
                    temId = item.getTemplateId();
                    break;
                }
            }
            updateMessageOperation(temId, operation);
            NOTIFICATION_OBJECT.informationNotification("OPERATION SAVED", "You have successfully assigned a message to a system operation.");
        }
    }




    /*******************************************************************************************************************
     *********************************************** ACCESS CONTROL AND PERMISSION SECTION
     ********************************************************************************************************************/

    boolean roleSelectorEmpty() {return roleSelector.getValue().isEmpty();}
    @FXML private ComboBox<String> roleSelector;
    @FXML private MFXButton savePermissionButton;
    @FXML private MFXLegacyTableView<PermissionsEntity> permissionsTable;
    @FXML private GridPane viewGridPane;
    @FXML private MFXScrollPane scrollPane;
    @FXML private CheckBox dashboardViewSelector,financeViewSelector, transactionViewSelector, customersViewSelector;
    @FXML private CheckBox reportViewSelector,resourceViewSelector, messageViewSelector, settingsViewSelector, loansViewSelector;

    @FXML private TableColumn<PermissionsEntity, Integer> accessIndexColumn;
    @FXML private TableColumn<PermissionsEntity, String> accessNameColumn;
    @FXML private TableColumn<PermissionsEntity, String> accessDescriptionColumn;
    @FXML private TableColumn<PermissionsEntity, MFXCheckbox> accessButtonColumn;

    //fill the table views and comboBox with values;
    void fillUserRolesSelector() {
        getUserRoles().forEach(item -> {
            roleSelector.getItems().add(item.getRole_name());
        });
        populateViewFields();
    }

    private void populateViewFields() {
        accessIndexColumn.setCellValueFactory(new PropertyValueFactory<>("operation_id"));
        accessNameColumn.setCellValueFactory(new PropertyValueFactory<>("operation_name"));
        accessDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptions"));
        accessButtonColumn.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        permissionsTable.setItems(getAllPermissions());
    }

    //this method when invoked shall get the status for each selected role item from the database.
    void getModuleStatusByRoleName() {
        int ROLE_ID = getRoleIdByName(roleSelector.getValue());
        int tableSize = permissionsTable.getItems().size();
        int gridSize = viewGridPane.getChildren().size();
        ObservableList<PermissionsEntity> moduleData = getModuleControlList(ROLE_ID);
        for (int i = 0; i <gridSize; i++) {
            String text = viewGridPane.getChildren().get(i).getAccessibleText();
            String module = moduleData.get(i).getModule_name();
            if (Objects.equals(text, module)){
                CheckBox check = (CheckBox) viewGridPane.getChildren().get(i);
                check.setSelected(moduleData.get(i).isAllowModule());
            }

        }

        ObservableList<PermissionsEntity> accessData = getAccessControlList(ROLE_ID);
        for (int i = 0; i < tableSize; i++) {
            boolean status = accessData.get(i).isAllowPermission();
            int dataId = accessData.get(i).getPermission_id();
            int tableId = permissionsTable.getItems().get(i).getPermission_id();
            if (Objects.equals(dataId, tableId)) {
                permissionsTable.getItems().get(i).setAllowPermission(status);
            }
        }

//        ArrayList<String> gridList = new ArrayList<>();
//        int gridSize = viewGridPane.getChildren().size();
//        for (int i = 0; i < gridSize; i++) {
//            CheckBox checkBox = (CheckBox) viewGridPane.getChildren().get(i);
//            gridList.add(viewGridPane.getChildren().get(i).getAccessibleText());
//            for (int j = 0; j < gridSize; j++) {
//                String textName = gridList.get(i);
//                if (Objects.equals(textName, getAppModules().get(j).getAlias())) {
//                    checkBox.setSelected(getAppModules().get(j).getAllowed());
//                }
//            }
//        }
//         ObservableList<PermissionsEntity> accessControlList = getAccessControlList(ROLE_ID);
    }

    int getAccessControlSelectedPermissions() {
        String roleName = roleSelector.getValue();
        int ROLE_ID = getRoleIdByName(roleName);
        int USER_ID = getUserIdByName(ACTIVE_USER);
        int gridSize = viewGridPane.getChildren().size();
        int tableSize = permissionsTable.getItems().size();
        PermissionsEntity permissions = new PermissionsEntity();
        AtomicInteger counter = new AtomicInteger(0);

        //iterate through the grid and get all selected VIEW CHECKBOXES
        for (int i = 0; i < gridSize; i++) {
            CheckBox checkbox = (CheckBox) viewGridPane.getChildren().get(i);
            boolean status = checkbox.isSelected();
            String name = getAppModules().get(i).getModule_name();
            permissions.setModule_name(name);
            permissions.setAllowModule(status);
            permissions.setRole_id(ROLE_ID);
            if (countDuplicateKeysInModulesTable(name, ROLE_ID) > 0) {
                counter.getAndAdd(updateModuleControlTable(status, name, ROLE_ID));
            } else {
                counter.getAndAdd(saveModuleControlVariables(permissions));
            }
        }

        //iterate through the PERMISSIONS TABLE and get all selected CHECKBOXES
        for (PermissionsEntity item : permissionsTable.getItems()) {
            int itemId = item.getOperation_id();
            boolean allowed = item.getCheckbox().isSelected();
            permissions.setRole_id(ROLE_ID);
            permissions.setOperation_id(itemId);
            permissions.setModified_by(USER_ID);
            permissions.setAllowPermission(allowed);

            if (countDuplicateKeysInAccessControlTable(itemId, ROLE_ID) > 0) {
                counter.getAndAdd(updateAccessControlTableOnDuplicateKeys(allowed, itemId, ROLE_ID));
            }else {
                counter.getAndAdd(saveAccessControlPermissions(permissions));
            }

        }
        return counter.get();
    }


    // implementation of all action event methods here...
    void actionEventMethodsImplementationForRolesAndPermission() {
        //enable or disable tables when item upon item selected.
        roleSelector.setOnAction(actionEvent ->  {
            savePermissionButton.setDisable(false);
            getModuleStatusByRoleName();
        });

        savePermissionButton.setOnAction(actionEvent -> {
            //Get variables by based on selected role
            savePermissionButton.setDisable(true);
            AtomicInteger counter = new AtomicInteger(0);
            String roleName = roleSelector.getValue();

            UserNotification NOTIFICATION = new UserNotification();
            UserAlerts ALERTS = new UserAlerts("SAVE DATA", "Do you want to save your selected access control permissions for the '" + roleName + "' role?",
                    "Please confirm your action to SAVE else CANCEL to abort.");
            if (ALERTS.confirmationAlert()) {
                counter.set(getAccessControlSelectedPermissions());
                if (counter.get() > 0) {
                    System.out.println(counter.get());
                    savePermissionButton.setDisable(false);
                    NOTIFICATION.successNotification("ACCESS CONTROL SAVED", "Nice you have successfully saved permission privilege");
                } else {
                    NOTIFICATION.errorNotification("FAILED OPERATION", "Sorry your action was unsuccessful during the process.");
                }
            }
        });

    }//end of action event method.







}//end of class...
