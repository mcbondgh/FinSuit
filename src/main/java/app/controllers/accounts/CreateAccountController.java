package app.controllers.accounts;

import app.controllers.homepage.AppController;
import app.models.createAccount.CreateAccountModel;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import com.jfoenix.controls.JFXToggleButton;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.temporal.TemporalAccessor;
import java.util.ResourceBundle;

public class CreateAccountController extends CreateAccountModel implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle, accountNumberLabel;
    public static String pageTitlePlaceHolder;
    @FXML private MFXButton previewButton, saveButton, cancelButton;
    @FXML private MFXScrollPane scrollPane;
    @FXML private AnchorPane anchorPane;
    @FXML private Pane contactPersonPane, politicallyExposedPane,applicantPane;
    @FXML private TextField firstNameField, lastNameField, otherNameField;
    @FXML private TextField placeOfBirthField, customerMobileNumberField, customerOtherNumberField, customerEmailAddressField;
    @FXML private TextField customerDigitalAddressField, customerResidentialField, customerLandmarkField, nameOfSpouseField, customerIdNumberField;
    @FXML private ComboBox<String> genderSelector, maritalStatusSelector, customerIdSelector, customerEducationalBackground;
    @FXML private DatePicker customerDobSelector, c_DobSelector;
    @FXML private JFXToggleButton politicallyExposedButton;
    @FXML private TextArea commentsField;
    @FXML private TextField fullNameField, ageField, c_numberField, institutionNumberField;
    @FXML private ComboBox<String> c_genderSelector, accountTypeSelector, c_idSelector, relationshipSelector, c_educationalBackgroundSelector;

    int currentUserId = getUserIdByName(AppController.activeUserPlaceHolder);

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        try {
            actionEventMethodsImplementation();
            populateFields();
            inputFieldValidationMethods();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void populateFields() {
        SpecialMethods.setGenderParameters(genderSelector);
        SpecialMethods.setIdTypeParameters(customerIdSelector);
        SpecialMethods.setIdTypeParameters(c_idSelector);
        SpecialMethods.setRelationshipTypes(relationshipSelector);
        SpecialMethods.setMaritalStatus(maritalStatusSelector);
        SpecialMethods.setAccountType(accountTypeSelector);
        SpecialMethods.setGenderParameters(c_genderSelector);
        SpecialMethods.setQualification(customerEducationalBackground);
        SpecialMethods.setQualification(c_educationalBackgroundSelector);
        String totalCount = SpecialMethods.generateAccountNumber(getTotalAccountNumbers());
        accountNumberLabel.setText(totalCount);
    }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/

    void inputFieldValidationMethods() {
        customerMobileNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                customerMobileNumberField.deletePreviousChar();
            }
            if (customerMobileNumberField.getText().length() >= 11) {
                customerMobileNumberField.deletePreviousChar();
            }
        });
        c_numberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                c_numberField.deletePreviousChar();
            }
            if (c_numberField.getText().length() >= 11) {
                c_numberField.deletePreviousChar();
            }
        });

        institutionNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                institutionNumberField.deletePreviousChar();
            }
            if (institutionNumberField.getText().length() >= 11) {
                institutionNumberField.deletePreviousChar();
            }
        });
        customerOtherNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                customerOtherNumberField.deletePreviousChar();
            }
            if (customerOtherNumberField.getText().length() >= 11) {
                customerOtherNumberField.deletePreviousChar();
            }
        });
    }



    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/

    void actionEventMethodsImplementation() throws IOException {
        Stage previewStage = AppStages.previewApplicantStage();
        previewButton.setOnAction(event -> {
                previewStage.show();
        });

        scrollPane.setOnMouseEntered(mouseEvent -> {
              previewButton.setDisable(previewStage.isShowing());
        });

        politicallyExposedButton.setOnAction(event -> {
            politicallyExposedPane.setDisable(!politicallyExposedButton.isSelected());
        });

        customerDobSelector.setOnAction(action -> {
            LocalDate currentDate = LocalDate.now();
            LocalDate dob = customerDobSelector.getValue();
            int age = currentDate.getYear() - dob.getYear();
            if (dob.isAfter(currentDate)) {
                ageField.setText("NV");
                ageField.setStyle("-fx-text-fill:red; -fx-background-color:#ffdbdb");
            } else {
                ageField.setText(String.valueOf(age)); ageField.setStyle("-fx-text-fill:#000; fx-background-color:#ffdbdb");
            }
        });




    }//end of action event methods implementation

}//end of class
