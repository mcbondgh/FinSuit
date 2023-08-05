package app.controllers.resource;

import app.fetchedData.human_resources.EmployeesData;
import app.models.humanResource.HumanResourceModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateEmployeeController extends HumanResourceModel implements Initializable {
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
    @FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addEmployeePane.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        empIdLabel.setText(ViewEmployeesController.staticEmployeeId);
        populateFields();
    }

    void populateFields() {
        for (EmployeesData employee : fetchAllEmployees()) {
            String emp_id = employee.getWork_id();
            if (Objects.equals(emp_id, empIdLabel.getText())) {
                firstNameField.setText(employee.getFirstname());
                lastNameField.setText(employee.getLastname());
                otherNameField.setText(employee.getOthername());
                mobileNumberField.setText(employee.getMobile_number());
                otherNameField.setText(employee.getOther_number());
                genderSelector.setValue(employee.getGender());
                dobSelector.setValue(employee.getDbo());
                emailField.setText(employee.getEmail());
                digitalAddressField.setText(employee.getDigital_address());
                addressField.setText(employee.getResidential_address());
                landMarkField.setText(employee.getLandmark());

            }
        }
    }

    /*******************************************************************************************************************
     *********************************************** INPUT KEY VALIDATION
     ********************************************************************************************************************/
    @FXML
    public void validateSalaryInput(KeyEvent event) {

    }


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF ACTION EVENT METHODS.
     ********************************************************************************************************************/

}//END OF CLASS
