package app.controllers.settings;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.errorLogger.ErrorLogger;
import app.fetchedData.BusinessInfoObject;
import app.fetchedData.SmsAPIObject;
import app.models.MainModel;
import app.models.settings.SettingModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class SettingsController extends SettingModel implements Initializable{

    Logger logger = Logger.getLogger("error");
    MainModel MODEL_OBJECT = new MainModel();
    UserAlerts  ALERT_OBJECT;

    UserNotification NOTIFICATION_OBJECT = new UserNotification();;
    


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    public static String pageTitlePlaceHolder;
    @FXML
    private TextField institutionNameField,emailField, numberField, otherNumberField, digitalAddField, locationField;
    @FXML private TextField senderIdField;
    @FXML private MFXButton updateButton, uploadButton, updateSenderIdButton;
    @FXML private Pane systemInfoPane;
    @FXML private ImageView logoViewer;
    @FXML private Label imageName, apiKeyField;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
    boolean isNameFieldEmpty() {return institutionNameField.getText().isEmpty();}
    boolean isNumberFieldEmpty() {return numberField.getText().isEmpty();}
    boolean isEmailFieldEmpty() {return emailField.getText().isEmpty();}
    boolean isDigitalFieldEmpty() {return digitalAddField.getText().isEmpty();}
    boolean isSenderIdEmpty() {return senderIdField.getText().isEmpty();}
    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        validateInputFields();
        try {
            fillFields();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        actionEventMethods();
    }

    void uploadLogo() throws IOException {
        ErrorLogger.LogError();
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose File");
            FileChooser.ExtensionFilter extension = fileChooser.getSelectedExtensionFilter();
            fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("mage Files", "*.png", "*.jpg"));
            File selectedFile = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
            String filePath = selectedFile.getPath();
            imageName.setText(filePath);
            Image logo = new Image(filePath);
            logoViewer.setImage(logo);
        }catch (NullPointerException e) {
            logger.info(e.getLocalizedMessage());
        }
    }
    void fillFields() throws IOException {
        for (BusinessInfoObject item : MODEL_OBJECT.getBusinessInfo()) {
              institutionNameField.setText(item.getName());
              numberField.setText(item.getMobileNumber());
              otherNumberField.setText(item.getOtherNumber());
              emailField.setText(item.getEmail());
              locationField.setText(item.getLocation());
              digitalAddField.setText(item.getDigital());
              imageName.setText(item.getLogo());
              logoViewer.setImage(new Image(item.getLogo()));
        }
        for (SmsAPIObject items : MODEL_OBJECT.getSmsApi()) {
            apiKeyField.setText(items.getKey());
            senderIdField.setText(items.getSender_id());
        }
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    void validateInputFields() {
        int inputLength = 10;
            numberField.setOnKeyReleased(keyEvent ->{
                if (!(keyEvent.getCode().isDigitKey() || keyEvent.getCode().isArrowKey() || keyEvent.getCode().equals(KeyCode.BACK_SPACE))) {
                    numberField.deleteNextChar();
                    numberField.deletePreviousChar();
                }
                if (numberField.getText().length() > inputLength) {
                    numberField.deleteNextChar();
                    numberField.deletePreviousChar();
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

            senderIdField.setOnKeyPressed(event -> {
                updateSenderIdButton.setDisable(isSenderIdEmpty());
                if (senderIdField.getText().length() > 10) {
                    senderIdField.deleteNextChar();
                    senderIdField.deletePreviousChar();
                }
            });
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

            ALERT_OBJECT = new UserAlerts("UPDATE SYSTEM CONFIG", "ARE YOU SURE YOU WANT TO UPDATE SYSTEM PARAMETERS?",
                    "please confirm your action to proceed else CANCEL to abort");
            if (ALERT_OBJECT.confirmationAlert()) {
                flag.set(updateBusinessInfo(name, number, otherNumber, email, digital, location, imageUrl));
                if (flag.get() > 0) {
                    NOTIFICATION_OBJECT.successNotification("UPDATE SUCCESSFUL", "System parameters successfully update");
                }
            }
        });

        updateSenderIdButton.setOnAction(event -> {
            String senderId = senderIdField.getText();
            ALERT_OBJECT = new UserAlerts("UPDATE SMS VARIABLE", "ARE YOU SURE YOU WANT TO UPDATE SMS SENDER ID?", "proceed to update else CANCEL to abort.");
            if (ALERT_OBJECT.confirmationAlert()) {
                flag.set(updateSenderId(senderId));
                if (flag.get() > 0) {
                    NOTIFICATION_OBJECT.successNotification("SENDER ID UPDATE", "You have successfully updated SMS Sender Id.");
                }
            }
        });

    }

}//end of class...
