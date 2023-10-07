package app.controllers.settings;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.encryptDecryp.EncryptDecrypt;
import app.controllers.homepage.AppController;
import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.models.settings.SettingModel;
import app.repositories.BusinessInfoEntity;
import app.repositories.SmsAPIEntity;
import app.repositories.settings.TemplatesRepository;
import com.jfoenix.controls.JFXTextArea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import net.synedra.validatorfx.Validator;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class SettingsController extends SettingModel implements Initializable{
    Logger logger = Logger.getLogger("error");
    Validator validator = new Validator();
    MainModel MODEL_OBJECT = new MainModel();
    UserAlerts  ALERT_OBJECT;
    UserNotification NOTIFICATION_OBJECT = new UserNotification();

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
    @FXML private PasswordField accountPasswordField, passwordField;
    @FXML private TextField loanPercentageField;
    @FXML private Label percentageIndicator;
    @FXML private MFXListView<Object> templateListView;
    @FXML private JFXTextArea messageBodyField;
    @FXML private TextField messageTitleField;
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

        actionEventMethods();
        populateFields();
    }

    void populateFields() {
        templateListView.getSelectionModel().setAllowsMultipleSelection(false);
        templateListView.getItems().clear();
        int size = getMessageTemplates().size();
        for (TemplatesRepository items : getMessageTemplates()) {
            templateListView.getItems().add(items.getMessageTitle());
        }
        messageBodyField.clear();
        messageTitleField.clear();
    }

    void uploadLogo() throws IOException {
        ErrorLogger.LogError();
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
            logger.info(e.getLocalizedMessage());
        }
    }

    void saveImageToDestination(){
        String destinationFolder = "G:\\My Drive\\FINAL YEAR PROJECT\\FinSuit\\src\\main\\resources\\app\\uploads\\";
        String fileName = imageName.getText();
        Image selectedImage = logoViewer.getImage();
        File saveToDestination = new File(destinationFolder);

        if (selectedImage != null) {
            if (!saveToDestination.exists()) {saveToDestination.mkdirs();}
            try {
                File filePath = new File(destinationFolder + fileName);
                ImageIO.write(SwingFXUtils.fromFXImage(selectedImage, null), "png", filePath);
            }catch (Exception e){e.printStackTrace();}
        }

    }
    void fillFields() throws IOException {
        for (BusinessInfoEntity item : MODEL_OBJECT.getBusinessInfo()) {
              institutionNameField.setText(item.getName());
              numberField.setText(item.getMobileNumber());
              otherNumberField.setText(item.getOtherNumber());
              emailField.setText(item.getEmail());
              locationField.setText(item.getLocation());
              digitalAddField.setText(item.getDigital());
              imageName.setText(item.getLogo());
              accountPasswordField.setText(item.getAccountPassword());
              double percentageValue = item.getLoanPercentage();
              loanPercentageField.setText(String.valueOf(percentageValue));
              percentageIndicator.setText(percentageValue + "% of basic Salary" );
              String getImageSource = "G:\\My Drive\\FINAL YEAR PROJECT\\FinSuit\\src\\main\\resources\\app\\uploads\\" + item.getLogo();
              logoViewer.setImage(new Image(getImageSource));
        }

        for (SmsAPIEntity items : MODEL_OBJECT.getSmsApi()) {
            apiKeyField.setText(items.getKey());
            senderIdField.setText(items.getSender_id());
            emailField.setText(items.getEmailAddress());
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
                updateButton.setDisable(isNameFieldEmpty() || isNumberFieldEmpty() || isEmailFieldEmpty() || isDigitalFieldEmpty() || isAccountFieldEmpty());
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

    }

    @FXML void checkMessagesTitleName() {
        String value = messageTitleField.getText().toUpperCase().replaceAll("", " ");

        for (Object title : templateListView.getItems()) {
            if (value.matches(title.toString())) {
                ALERT_OBJECT = new UserAlerts("INVALID TITLE", "Sorry, message title must be unique from your list of titles");
                ALERT_OBJECT.warningAlert();
                messageTitleField.clear();
                return;
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
            String imageUrl = imageName.getText();
            String hashedValue = EncryptDecrypt.hashPlainText(accountPasswordField.getText());
            double percentageValue = Double.parseDouble(loanPercentageField.getText());

            ALERT_OBJECT = new UserAlerts("UPDATE SYSTEM CONFIG", "ARE YOU SURE YOU WANT TO UPDATE SYSTEM PARAMETERS?",
                    "please confirm your action to proceed else CANCEL to abort");
            if (ALERT_OBJECT.confirmationAlert()) {
                flag.set(updateBusinessInfo(name, number, otherNumber, email, hashedValue, digital, location, imageUrl, percentageValue));
                saveImageToDestination();
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
        if (!(isTitleFieldEmpty() || isMessageFieldEmpty())) {
            int userId = MODEL_OBJECT.getUserIdByName(getCurrentUser());
            String title = messageTitleField.getText().toUpperCase();
            String message = messageBodyField.getText();
            ALERT_OBJECT = new UserAlerts("SAVE TEMPLATE", "Do you wish to add current message to your templates?");
            if (ALERT_OBJECT.confirmationAlert()) {
                if(saveMessageTemplate(title, message, userId) > 0) {
                    NOTIFICATION_OBJECT.successNotification("SAVE SUCCESS", "Message successfully added to your templates.");
                    populateFields();
                }
            }
        }
    }
    @FXML void getSelectedTemplate() {
        String selectedItem = templateListView.getSelectionModel().getSelection().toString();
        System.out.println(selectedItem);
    }

}//end of class...
