package app.controllers.resource;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.humanResource.HumanResourceModel;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML private VBox menuContainer;
    @FXML
    Pane menuIcon, addEmployeePane;
    @FXML
    MFXButton manageUsersButton, viewEmployeesButton, addEmployeeButton, saveButton, cancelButton;
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
        pageTitle.setText(pageTitlePlaceHolder);
        String empCount = SpecialMethods.generateEmployeeId(getTotalEmployeesCount() + 1);
        empIdLabel.setText(empCount);

        setAddEmployeeButtonClicked();
        setViewEmployeesButtonClicked();
        setManageUsersButtonClicked();
        fillSelectors();
        checkForEmptyFields();
        cancelButtonClicked();
        saveButtonClicked();
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
        addEmployeePane.setOnMouseMoved(event -> {
            saveButton.setDisable(
                    isFirstnameEmpty() || isLastnameEmpty() || isMobileNumberEmpty() || isGenderEmpty() || isDobEmpty() || isEmailEmpty() ||
                    isDigitalAddressEmpty() || isAddressEmpty() || isLandmarkEmpty() || isIdNumberEmpty() || isIdSelectorEmpty() ||
                    isMaritalStateEmpty() || isQualificationEmpty() || isWorkingExpEmpty() || isEmploymentDateEmpty() || isSalaryEmpty() ||
                    isBankNameEmpty() || isAccountNameEmpty() || isAccountNumber() || isContactNameEmpty() || isContactAddress() || isPlaceOfWorkEmpty() ||
                    isContactMobileNumberEmpty() || isContactDigitalAddEmpty() || isContactLandMarkEmpty() || isOrgAddressEmpty()
            );
        });
        emailField.setOnKeyTyped(keyevent -> {
            String invalid = "-fx-border-color:#ff0000; -fx-border-radius: 5px; -fx-border-width:2px;";
            String valid = "-fx-border-color:#ddd; -fx-border-radius: 5px; ";
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$";

            if (!emailField.getText().matches(emailRegex)) {
                emailField.setStyle(invalid);
            } else emailField.setStyle(valid);
        });
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML void menuIconClicked() {
        menuContainer.setVisible(!menuContainer.isVisible());
    }
    @FXML void HideMenuContainer() {
        menuContainer.setVisible(false);
    }
    void setViewEmployeesButtonClicked() {
        viewEmployeesButton.setOnAction(event ->  {
            try {
                SpecialMethods.FlipView("views/resource/view-employees-page.fxml", borderPane);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    void setAddEmployeeButtonClicked() {
        addEmployeeButton.setOnAction(event -> {
            try {
                borderPane.setCenter(addEmployeePane);
            }catch (Exception e){}
        });
    }
    void setManageUsersButtonClicked() {
        manageUsersButton.setOnAction(event -> {
            try {
                SpecialMethods.FlipView("views/resource/manage-users-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
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
            int empId = Integer.parseInt(empIdLabel.getText());
            try {
                String firstname = firstNameField.getText();
                String lastname = lastNameField.getText();
                String otherName = otherNameField.getText();
                String gender = genderSelector.getValue();
                LocalDate dob = dobSelector.getValue();
                String mobileNumber = mobileNumberField.getText();
                String otherNumber = otherNumberField.getText();
                String email = emailField.getText();
                String digitalAdd = digitalAddressField.getText();
                String address = addressField.getText();
                String landmark = landMarkField.getText();
                String idType = idSelector.getValue();
                String idNumber = idNumberField.getText();
                String maritalStatus = maritalStatusField.getValue();
                String qualification = qualificationSelector.getValue();
                String designation = designationSelector.getValue();
                String workingExp = workingExperienceField.getText();
                LocalDate empDate = employmentDateSelector.getValue();
                double salary = Double.parseDouble(salaryField.getText());
                String bankName = bankNameField.getText();
                String accountName = accountNameField.getText();
                String accountNo = accountNumberField.getText();
                String c_fullname = c_nameField.getText();
                String c_mobileNumber = c_mobileNumberField.getText();
                String c_digitalAdd = c_digitalAddressField.getText();
                String c_address = c_addressField.getText();
                String c_landmark = c_landMarkField.getText();
                String c_placeOfWork = c_placeOfWorkField.getText();
                String c_orgNumber = c_organizationNumberField.getText();
                String c_orgAddress = c_organizationAddressField.getText();
                String comments = commentsField.getText();


            }catch (Exception e) {
                e.printStackTrace();
            }


        });
    }


            

}//end of class....
