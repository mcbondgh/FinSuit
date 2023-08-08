package app.controllers.resource;

import app.fetchedData.human_resources.EmployeesData;
import app.fetchedData.roles.UserRolesData;
import app.fetchedData.users.UsersData;
import app.models.users.UsersModel;
import app.specialmethods.SpecialMethods;
import com.jfoenix.controls.JFXCheckBox;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.ResourceBundle;


public class ManageUsersController extends UsersModel implements Initializable {

    EmployeesData EMPLOYEES_OBJECT = new EmployeesData();

    /*******************************************************************************************************************
     *FXML FILE EJECTIONS
     *******************************************************************************************************************/
    @FXML
    private MFXLegacyTableView<UsersData> usersTable;
    @FXML
    private TableColumn<UsersData, String> userIdColumn;
    @FXML
    private TableColumn<UsersData, String> usernameColumn;
    @FXML
    private TableColumn<UsersData, String> userRoleColumn;
    @FXML
    private TableColumn<UsersData, String> statusColumn;
    @FXML
    private TableColumn<UsersData, JFXCheckBox> updateColumn, removeColumn;

    @FXML
    private MFXFilterComboBox<String> employeeSelector;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private Label uppercaseChecker, numberChecker, notificationIndicator;
    @FXML
    private MFXButton saveButton, cancelButton;
    @FXML private ComboBox<String> roleSelector;



    /*******************************************************************************************************************
     *TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    boolean isEmployeeFieldEmpty() {return employeeSelector.getValue() == null;}
    boolean isUsernameEmpty() {return usernameField.getText().isEmpty();}
    boolean isPasswordEmpty() {return passwordField.getText().isEmpty();}
    boolean isConfirmPasswordEmpty(){return confirmPasswordField.getText().isEmpty();}




    /*******************************************************************************************************************
     *IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpecialMethods.setUserRoleParameters(roleSelector);
        loadEmployeesSelector();
        passwordFieldsValidation();
    }

    void loadEmployeesSelector() {

    }




    /*******************************************************************************************************************
     *INPUT FIELD VALIDATIONS
     *******************************************************************************************************************/
    void passwordFieldsValidation() {
        String validColor = "-fx-text-fill:green";
        String invalidColor = "-fx-text-fill: red";
        passwordField.setOnKeyTyped(event -> {
            String regExp = "^(?=.*[A-Z])(?=.*\\\\d).+$";
            String value = passwordField.getText();
            if (value.matches(regExp)) {
                numberChecker.setStyle(validColor);
                uppercaseChecker.setStyle(validColor);
            } else {
                numberChecker.setStyle(invalidColor);
                uppercaseChecker.setStyle(invalidColor);
            }
        });
    }


    /*******************************************************************************************************************
     *IMPLEMENTATION OF ACTION EVENT METHODS.
     *******************************************************************************************************************/
    void actionEventMethods() {
        employeeSelector.setOnAction(event -> {
            String selectedEmployee = employeeSelector.getSelectedItem();
            for (EmployeesData value : fetchAllEmployees()) {

            }
        });
    }

}//END OF CLASS
