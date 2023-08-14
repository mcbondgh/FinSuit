package app.controllers.resource;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.fetchedData.human_resources.EmployeesData;
import app.models.humanResource.HumanResourceModel;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

public class HumanResourceController extends HumanResourceModel implements Initializable{

    UserAlerts ALERT_OBJECT;
    UserNotification NOTIFICATION_OBJECT = new UserNotification();
    static String getActiveUserName = AppController.activeUserPlaceHolder;

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle, empIdLabel;
    @FXML
    Pane addEmployeePane;
    @FXML
    MFXButton saveButton, cancelButton;
    @FXML private TextField firstNameField, lastNameField, otherNameField, mobileNumberField, otherNumberField;
    @FXML private TextField emailField, digitalAddressField, addressField, landMarkField, idNumberField, workingExperienceField;
    @FXML private  TextField salaryField, bankNameField, accountNameField, accountNumberField;
    @FXML private TextArea commentsField;

    @FXML private TextField c_nameField, c_mobileNumberField, c_digitalAddressField, c_addressField, c_landMarkField, c_placeOfWorkField, c_organizationNumberField, c_organizationAddressField;
    @FXML private DatePicker dobSelector, employmentDateSelector;
    @FXML private ComboBox<String> maritalStatusField, genderSelector, qualificationSelector, idSelector, designationSelector;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
     boolean isFirstnameEmpty() {return firstNameField.getText().isEmpty();}
     boolean isLastnameEmpty() {return lastNameField.getText().isEmpty();}
    boolean isMobileNumberEmpty() {return mobileNumberField.getText().isEmpty();}
    boolean isGenderEmpty() {return genderSelector.getValue() == null;}
    boolean isDobEmpty(){return dobSelector.getValue() == null;}
    boolean isEmailEmpty() {return emailField.getText().isEmpty();}
    boolean isDigitalAddressEmpty() {return digitalAddressField.getText().isEmpty();}
    boolean isAddressEmpty() {return addressField.getText().isEmpty();}
    boolean isLandmarkEmpty() {return landMarkField.getText().isEmpty();}
    boolean isIdSelectorEmpty() {return idSelector.getValue() == null;}
    boolean isIdNumberEmpty() {return idNumberField.getText().isEmpty();}
    boolean isMaritalStateEmpty() {return maritalStatusField.getValue() ==null;}
    boolean isQualificationEmpty() {return qualificationSelector.getValue() == null;}
    boolean isWorkingExpEmpty() {return workingExperienceField.getText().isEmpty();}
    boolean isEmploymentDateEmpty() {return employmentDateSelector.getValue() == null;}
    boolean isSalaryEmpty() {return salaryField.getText().isEmpty();}
    boolean isBankNameEmpty() {return bankNameField.getText().isEmpty();}
    boolean isAccountNameEmpty() {return accountNameField.getText().isEmpty();}
    boolean isAccountNumber() {return accountNumberField.getText().isEmpty();}
    boolean isContactNameEmpty() {return c_nameField.getText().isEmpty();}
    boolean isContactMobileNumberEmpty() {return c_mobileNumberField.getText().isEmpty();}
    boolean isContactDigitalAddEmpty() {return c_digitalAddressField.getText().isEmpty();}
    boolean isContactLandMarkEmpty() {return c_landMarkField.getText().isEmpty();}
    boolean isContactAddress(){return c_addressField.getText().isEmpty();}
    boolean isPlaceOfWorkEmpty() {return c_placeOfWorkField.getText().isEmpty();}
    boolean isOrgNumberEmpty() {return c_organizationNumberField.getText().isEmpty();}
    boolean isOrgAddressEmpty() {return c_organizationAddressField.getText().isEmpty();}


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empIdLabel.setText(SpecialMethods.generateEmployeeId(getTotalEmployees() + 1));
        fillSelectors();
        checkForEmptyFields();
        cancelButtonClicked();
        saveButtonClicked();
        validateMobileNumberFields();
    }
    void fillSelectors() {
        SpecialMethods.setGenderParameters(genderSelector);
        SpecialMethods.setDesignation(designationSelector);
        SpecialMethods.setIdTypeParameters(idSelector);
        SpecialMethods.setMaritalStatus(maritalStatusField);
        SpecialMethods.setQualification(qualificationSelector);
    }
    void resetFields() {
        firstNameField.clear();
        lastNameField.clear();
        otherNameField.clear();
        dobSelector.setValue(null);
        mobileNumberField.clear();
        otherNumberField.clear();
        genderSelector.setValue(null);
        emailField.clear();
        digitalAddressField.clear();
        addressField.clear();
        landMarkField.clear();
        idSelector.setValue(null);
        idNumberField.clear();
        maritalStatusField.setValue(null);
        designationSelector.setValue(null);
        qualificationSelector.setValue(null);
        workingExperienceField.clear();
        employmentDateSelector.setValue(null);
        salaryField.clear();
        bankNameField.clear();
        accountNameField.clear();
        accountNumberField.clear();
        c_nameField.clear();
        c_mobileNumberField.clear();
        c_digitalAddressField.clear();
        c_landMarkField.clear();
        c_addressField.clear();
        c_placeOfWorkField.clear();
        c_organizationAddressField.clear();
        c_organizationNumberField.clear();
        commentsField.clear();
    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/
    @FXML
    private void validateSalaryInput(KeyEvent event) {
        if (!(event.getCode().isDigitKey() || event.getCode().equals(KeyCode.BACK_SPACE) ||
                event.getCode().equals(KeyCode.PERIOD) || event.getCode().isArrowKey())) {
            salaryField.deletePreviousChar();
            salaryField.deleteNextChar();
        }
    }
    void checkForEmptyFields() {
        String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
        String valid = "-fx-border-color:#ddd; -fx-border-radius: 5px; ";
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$";
        addEmployeePane.setOnMouseMoved(event -> {
            saveButton.setDisable(
                    isFirstnameEmpty() || isLastnameEmpty() || isMobileNumberEmpty() || isGenderEmpty() || isDobEmpty() || isEmailEmpty() ||
                    isDigitalAddressEmpty() || isAddressEmpty() || isLandmarkEmpty() || isIdNumberEmpty() || isIdSelectorEmpty() ||
                    isMaritalStateEmpty() || isQualificationEmpty() || isWorkingExpEmpty() || isEmploymentDateEmpty() || isSalaryEmpty() ||
                    isBankNameEmpty() || isAccountNameEmpty() || isAccountNumber() || isContactNameEmpty() || isContactAddress() || isPlaceOfWorkEmpty() ||
                    isContactMobileNumberEmpty() || isContactDigitalAddEmpty() || isContactLandMarkEmpty() || isOrgAddressEmpty() || !emailField.getStyle().equals(valid)
            );
        });
        emailField.setOnKeyTyped(keyEvent -> {
            if (!emailField.getText().matches(emailRegex)) {
                emailField.setStyle(invalid);
            } else emailField.setStyle(valid);

        });
    }
    void validateMobileNumberFields() {
        mobileNumberField.setOnKeyReleased(event -> {
            if (!(event.getCode().isDigitKey() || event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().isArrowKey()) ||
                    event.getCode().equals(KeyCode.TAB)) {
                mobileNumberField.deletePreviousChar();
                mobileNumberField.deleteNextChar();
            }
            if (mobileNumberField.getText().length() > 10) {
                mobileNumberField.deletePreviousChar();
                mobileNumberField.deleteNextChar();
            }
        });
        otherNumberField.setOnKeyReleased(event -> {
            if (!(event.getCode().isDigitKey() || event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().isArrowKey()) ||
                    event.getCode().equals(KeyCode.TAB)) {
                otherNumberField.deletePreviousChar();
                otherNumberField.deleteNextChar();
            }
            if (otherNumberField.getText().length() > 10) {
                otherNumberField.deletePreviousChar();
                otherNumberField.deleteNextChar();
            }
        });
        c_organizationNumberField.setOnKeyReleased(event -> {
            if (!(event.getCode().isDigitKey() || event.getCode().equals(KeyCode.BACK_SPACE) ||
                    event.getCode().isArrowKey()) || event.getCode().equals(KeyCode.TAB)) {
                c_organizationNumberField.deletePreviousChar();
                c_organizationNumberField.deleteNextChar();
            }
            if (c_organizationNumberField.getText().length() > 10) {
                c_organizationNumberField.deletePreviousChar();
                c_organizationNumberField.deleteNextChar();
            }
        });
        c_mobileNumberField.setOnKeyReleased(event -> {
            if (!(event.getCode().isDigitKey() || event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().isArrowKey()) ||
                    event.getCode().equals(KeyCode.TAB)) {
                c_mobileNumberField.deletePreviousChar();
                c_mobileNumberField.deleteNextChar();
            }
            if (c_mobileNumberField.getText().length() > 10) {
                c_mobileNumberField.deletePreviousChar();
                c_mobileNumberField.deleteNextChar();
            }
        });
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    void cancelButtonClicked() {
        cancelButton.setOnAction(event -> {
            ALERT_OBJECT = new UserAlerts("CLEAR FIELDS", "Are you certain you want to cancel and clear the form fields?", "please confirm your action else CANCEL to abort.");
            if (ALERT_OBJECT.confirmationAlert()) {
                resetFields();
            }
        });
    }

    void saveButtonClicked() {
        saveButton.setOnAction(event -> {
            EmployeesData data = new EmployeesData();
            int activeUserId = getUserIdByName(getActiveUserName);
            try {
                data.setWork_id(empIdLabel.getText());
                data.setFirstname(firstNameField.getText());
                data.setLastname(lastNameField.getText());
                data.setOthername(otherNameField.getText());
                data.setEmail(emailField.getText());
                data.setMobile_number(mobileNumberField.getText());
                data.setOther_number(otherNumberField.getText());
                data.setGender(genderSelector.getValue());
                data.setDbo(dobSelector.getValue());
                data.setDigital_address(digitalAddressField.getText());
                data.setResidential_address(addressField.getText());//11
                data.setLandmark(landMarkField.getText());
                data.setId_type(idSelector.getValue());
                data.setId_number(idNumberField.getText());
                data.setMarital_status(maritalStatusField.getValue());
                data.setQualification(qualificationSelector.getValue());
                data.setDesignation(designationSelector.getValue());
                data.setWorking_experience(workingExperienceField.getText());
                data.setEmployment_date(employmentDateSelector.getValue());//19
                data.setContact_person_name(c_nameField.getText());//20
                data.setContact_person_number(c_mobileNumberField.getText());//21
                data.setContact_person_digital_address(c_digitalAddressField.getText());//22
                data.setContact_person_address(c_addressField.getText());//23
                data.setContact_person_landmark(c_landMarkField.getText());
                data.setContact_person_place_of_work(c_placeOfWorkField.getText());//25
                data.setContact_person_org_number(c_organizationNumberField.getText());
                data.setContact_person_org_address(c_organizationAddressField.getText());
                data.setAdditional_information(commentsField.getText());
                data.setSalary(Double.parseDouble(salaryField.getText()));
                data.setBank_name(bankNameField.getText());
                data.setAccount_name(accountNameField.getText());
                data.setAccount_number(accountNumberField.getText());
                data.setAdded_by(activeUserId);
                data.setModified_by(activeUserId);

                ALERT_OBJECT = new UserAlerts("ADD EMPLOYEE", "Are you sure you want to add '" + lastNameField.getText() + "' to your list of employees?",
                        "please check and be sure all entries are correct and confirm your action to save else CANCEL to abort.");
                if (ALERT_OBJECT.confirmationAlert()) {
                    if (addNewEmployee(data)  > 0) {
                        NOTIFICATION_OBJECT.successNotification("EMPLOYEES SAVED", "Perfect, you have successfully saved employee's data.");
                        resetFields();
                        empIdLabel.setText(SpecialMethods.generateEmployeeId(getTotalEmployees() + 1));
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



            

}//end of class....
