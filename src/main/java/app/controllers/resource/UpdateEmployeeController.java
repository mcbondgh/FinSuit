package app.controllers.resource;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.repositories.human_resources.EmployeesData;
import app.models.humanResource.HumanResourceModel;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateEmployeeController extends HumanResourceModel implements Initializable {

    UserAlerts ALERTS;
    UserNotification NOTIFICATION = new UserNotification();


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML private Label empIdLabel;

    private Label pageTitle;
    @FXML
    Pane addEmployeePane;
    @FXML
    MFXButton saveButton, cancelButton, removeButton;
    @FXML private TextField firstNameField, lastNameField, otherNameField, mobileNumberField, otherNumberField;
    @FXML private TextField emailField, digitalAddressField, addressField, landMarkField, idNumberField, workingExperienceField;
    @FXML private  TextField salaryField, bankNameField, accountNameField, accountNumberField;
    @FXML private TextArea commentsField;

    @FXML private CheckBox enableFieldsCheckBox;

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
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addEmployeePane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        empIdLabel.setText(ViewEmployeesController.staticEmployeeId);
        populateFields();
        validateMobileNumberFields();
        actionEventTriggerMethods();
        checkForEmptyFields();
        SpecialMethods.setQualification(qualificationSelector);
        SpecialMethods.setDesignation(designationSelector);
        SpecialMethods.setMaritalStatus(maritalStatusField);
        SpecialMethods.setIdTypeParameters(idSelector);
        SpecialMethods.setGenderParameters(genderSelector);
    }

    void populateFields() {
        for (EmployeesData employee : fetchAllEmployees()) {
            String emp_id = employee.getWork_id();
            if (Objects.equals(emp_id, empIdLabel.getText())) {
                firstNameField.setText(employee.getFirstname());
                lastNameField.setText(employee.getLastname());
                otherNameField.setText(employee.getOthername());
                mobileNumberField.setText(employee.getMobile_number());
                otherNumberField.setText(employee.getOther_number());
                genderSelector.setValue(employee.getGender());
                dobSelector.setValue(employee.getDbo());
                emailField.setText(employee.getEmail());
                digitalAddressField.setText(employee.getDigital_address());
                addressField.setText(employee.getResidential_address());
                landMarkField.setText(employee.getLandmark());
                idSelector.setValue(employee.getId_type());
                idNumberField.setText(employee.getId_number());
                maritalStatusField.setValue(employee.getMarital_status());
                qualificationSelector.setValue(employee.getQualification());
                designationSelector.setValue(employee.getDesignation());
                workingExperienceField.setText(employee.getWorking_experience());
                employmentDateSelector.setValue(employee.getEmployment_date());
                salaryField.setText(String.valueOf(employee.getSalary()));
                bankNameField.setText(employee.getBank_name());
                accountNameField.setText(employee.getAccount_name());
                accountNumberField.setText(employee.getAccount_number());
                c_nameField.setText(employee.getContact_person_name());
                c_placeOfWorkField.setText(employee.getContact_person_place_of_work());
                c_addressField.setText(employee.getContact_person_address());
                c_mobileNumberField.setText(employee.getContact_person_number());
                c_digitalAddressField.setText(employee.getContact_person_digital_address());
                c_landMarkField.setText(employee.getContact_person_landmark());
                c_organizationAddressField.setText(employee.getContact_person_org_address());
                c_organizationNumberField.setText(employee.getContact_person_number());
                commentsField.setText(employee.getAdditional_information());
            }
        }
    }



    /*******************************************************************************************************************
     *********************************************** INPUT KEY VALIDATION
     ********************************************************************************************************************/
    @FXML
    public void validateSalaryInput(KeyEvent event) {

    }
    void checkForEmptyFields() {
        String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
        String valid = "-fx-border-color:#eee; -fx-border-radius: 5px; ";
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$";
        addEmployeePane.setOnMouseMoved(event -> {
            saveButton.setDisable(
                    isFirstnameEmpty() || isLastnameEmpty() || isMobileNumberEmpty() || isGenderEmpty() || isDobEmpty() || isEmailEmpty() ||
                            isDigitalAddressEmpty() || isAddressEmpty() || isLandmarkEmpty() || isIdNumberEmpty() || isIdSelectorEmpty() ||
                            isMaritalStateEmpty() || isQualificationEmpty() || isWorkingExpEmpty() || isEmploymentDateEmpty() || isSalaryEmpty() ||
                            isBankNameEmpty() || isAccountNameEmpty() || isAccountNumber() || isContactNameEmpty() || isContactAddress() || isPlaceOfWorkEmpty() ||
                            isContactMobileNumberEmpty() || isContactDigitalAddEmpty() || isContactLandMarkEmpty() || isOrgAddressEmpty()
            );
            removeButton.setDisable(
                    isFirstnameEmpty() || isLastnameEmpty() || isMobileNumberEmpty() || isGenderEmpty() || isDobEmpty() || isEmailEmpty() ||
                            isDigitalAddressEmpty() || isAddressEmpty() || isLandmarkEmpty() || isIdNumberEmpty() || isIdSelectorEmpty() ||
                            isMaritalStateEmpty() || isQualificationEmpty() || isWorkingExpEmpty() || isEmploymentDateEmpty() || isSalaryEmpty() ||
                            isBankNameEmpty() || isAccountNameEmpty() || isAccountNumber() || isContactNameEmpty() || isContactAddress() || isPlaceOfWorkEmpty() ||
                            isContactMobileNumberEmpty() || isContactDigitalAddEmpty() || isContactLandMarkEmpty() || isOrgAddressEmpty()
            );
        });
        emailField.setOnKeyTyped(keyEvent -> {
            if (emailField.getText().matches(emailRegex)) {
                emailField.setStyle(valid);
            } else emailField.setStyle(invalid) ;
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
     *********************************************** IMPLEMENTATION OF ACTION EVENT METHODS.
     ********************************************************************************************************************/
    void actionEventTriggerMethods() {
        String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
        cancelButton.setOnAction(event -> {cancelButton.getScene().getWindow().hide();});
        String employeeId = empIdLabel.getText();
        int activeUserId = getUserIdByName( AppController.activeUserPlaceHolder);
        EmployeesData data = new EmployeesData();
        saveButton.setOnAction(event -> {
            saveButton.setDisable(emailField.getStyle().equals(invalid));
            if (!saveButton.isDisabled()) {
                saveButton.setDisable(saveButton.isPressed());
                ALERTS = new UserAlerts("UPDATE EMPLOYEE INFO", "ARE YOU SURE YOU WANT TO UPDATE CURRENT EMPLOYEE DETAILS? ", "Please confirm to update employee's information else CANCEL to abort.");
                if (ALERTS.confirmationAlert()) {
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
                    data.setLandmark(landMarkField.getText());//12
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
                    data.setAdditional_information(commentsField.getText());//28
                    data.setSalary(Double.parseDouble(salaryField.getText()));//29
                    data.setBank_name(bankNameField.getText());//30
                    data.setAccount_name(accountNameField.getText());
                    data.setAccount_number(accountNumberField.getText());
                    data.setAdded_by(activeUserId);
                    data.setModified_by(activeUserId);
                    if(updateEmployeeDetails(data, activeUserId, employeeId) > 0) {
                        NOTIFICATION.successNotification("UPDATE SUCCESSFUL", "You have successfully updated employee's information.");
                        try {
                            Thread.sleep(500);
                            addEmployeePane.getScene().getWindow().hide();
                        }catch (InterruptedException e) {throw new RuntimeException(e);}
                    }else {
                        NOTIFICATION.errorNotification("UPDATE FAILED", "Sorry, failed to update employee's information. Contact system admin for assistance");
                    }
                }
            }
        });
        removeButton.setOnAction(event -> {
            removeButton.setDisable(emailField.getStyle().equals(invalid));
            if (!removeButton.isDisabled()) {
                removeButton.setDisable(removeButton.isPressed());
                ALERTS = new UserAlerts("REMOVE USER", "ARE YOU SURE YOU WANT TO PERMANENTLY REMOVE '" + lastNameField.getText() + "' FROM YOUR LIST OF EMPLOYEES?", "" +
                        "By selecting YES, this employee shall permanently be removed from your list of employees, else CANCEL to abort." );
                if (ALERTS.confirmationAlert()) {
                    int result = removeEmployee(employeeId);
                    if (result > 0) {
                        try {
                            NOTIFICATION.successNotification("EMPLOYEE REMOVED", "You have successfully deleted employee from your database.");
                            Thread.sleep(600);
                            addEmployeePane.getScene().getWindow().hide();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        NOTIFICATION.errorNotification("REMOVE OPERATION FAILED", "Sorry employee removal failed, contact system admin for assistance.");
                    }
                }
            }
        });
    }




}//END OF CLASS
