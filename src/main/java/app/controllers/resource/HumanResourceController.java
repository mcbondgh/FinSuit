package app.controllers.resource;

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
import java.util.ResourceBundle;

public class HumanResourceController implements Initializable {

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


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        setAddEmployeeButtonClicked();
        setViewEmployeesButtonClicked();
        setManageUsersButtonClicked();
        fillSelectors();

        }
    void fillSelectors() {
        SpecialMethods.setGenderParameters(genderSelector);
        SpecialMethods.setDesignation(designationSelector);
        SpecialMethods.setIdTypeParameters(idSelector);
        SpecialMethods.setMaritalStatus(maritalStatusField);
        SpecialMethods.setQualification(qualificationSelector);

    }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/






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
            try{
               borderPane.setCenter(addEmployeePane);
            }catch (IllegalArgumentException e) {}
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
    @FXML
    private void validateSalaryInput(KeyEvent event) {
        if (!(event.getCode().isDigitKey() || event.getCode().equals(KeyCode.BACK_SPACE) || event.getCode().equals(KeyCode.PERIOD) || event.getCode().isArrowKey())) {
            salaryField.deletePreviousChar();
            salaryField.deleteNextChar();
        }
    }

}//end of class....
