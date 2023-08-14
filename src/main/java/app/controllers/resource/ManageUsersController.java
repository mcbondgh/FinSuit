package app.controllers.resource;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.config.encryptDecryp.EncryptDecrypt;
import app.controllers.homepage.AppController;
import app.fetchedData.human_resources.EmployeesData;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.*;


public class ManageUsersController extends UsersModel implements Initializable {

    EmployeesData EMPLOYEES_OBJECT = new EmployeesData();
    UserNotification NOTIFICATION = new UserNotification();

    UserAlerts ALERT;
    int currentUser = getUserIdByName(AppController.activeUserPlaceHolder);

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
    private Label uppercaseChecker, numberChecker, notificationIndicator, lengthIndicator;
    @FXML
    private MFXButton addNewUserButton, cancelButton, saveButton;
    @FXML private ComboBox<String> roleSelector;
    @FXML private Pane usersPanel;



    /*******************************************************************************************************************
     *TRUE OR FALSE STATEMENTS
     *******************************************************************************************************************/
    boolean isEmployeeFieldEmpty() {return employeeSelector.getValue() == null;}
    boolean isUsernameEmpty() {return usernameField.getText().isEmpty();}
    boolean isPasswordEmpty() {return passwordField.getText().isEmpty();}
    boolean isConfirmPasswordEmpty(){return confirmPasswordField.getText().isEmpty();}
    boolean isUserRoleEmpty() {return roleSelector.getValue() == null;}
    boolean isUsersTableEmpty() {return usersTable.getItems().isEmpty();}




    /*******************************************************************************************************************
     *IMPLEMENTATION OF OTHER METHODS
     *******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpecialMethods.setUserRoleParameters(roleSelector);
        checkAllRequiredFields();
        passwordFieldsValidation();
        populateTableView();
        loadEmployeesSelector();
        actionEventMethods();
    }

    private void resetFields() {
        employeeSelector.setValue(null);
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        roleSelector.setValue(null);
        refreshTable();
        employeeSelector.setDisable(!usersTable.getSelectionModel().isEmpty());

    }
    void populateTableView() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("activeStatus"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        usersTable.setItems(fetchAssignedUsersOnly());
    }
    void refreshTable() {
        usersTable.getItems().clear();
        populateTableView();
    }
    void loadEmployeesSelector() {
        employeeSelector.getItems().clear();
        List<String> listItem = new ArrayList<>();
        for (EmployeesData epm : fetchEmployeeSummaryData()) {
            listItem.add(epm.getWork_id());
            for (UsersData user : usersTable.getItems()) {
                if (epm.getWork_id().contains(user.getEmp_id())) {
                    listItem.remove(user.getEmp_id());
                    break;
                }
            }
        }
        for (String value : listItem) {
            employeeSelector.getItems().add(value);
        }
    }




    /*******************************************************************************************************************
     ****** INPUT FIELD VALIDATIONS
     *******************************************************************************************************************/
    void passwordFieldsValidation() {
       passwordField.setOnKeyTyped(event -> {
           boolean passLength = passwordField.getLength() < 4;
           lengthIndicator.setStyle(passLength ? "-fx-text-fill: RED;" : "-fx-text-fill: GREEN");
           uppercaseChecker.setStyle(hasUpper() ? "-fx-text-fill: green" : "-fx-text-fill: red");
           numberChecker.setStyle(hasDigit() ? "-fx-text-fill: green" : "-fx-text-fill: red");
       });
       confirmPasswordField.setOnKeyReleased(event -> {
           String passValue = passwordField.getText();
           String confirmPassValue = confirmPasswordField.getText();
           notificationIndicator.setVisible(!Objects.equals(passValue, confirmPassValue));
       });
    }
    boolean hasUpper() {
        String passValue = passwordField.getText();
        for (char each : passValue.toCharArray()) {
            if (Character.isUpperCase(each)) {
                return true;
            }
        }
        return false;
    }
    boolean hasDigit() {
        String passValue = passwordField.getText();
        for (char each : passValue.toCharArray()) {
            if (Character.isDigit(each)) {
                return true;
            }
        }
        return false;
    }
    void checkAllRequiredFields(){
        usersPanel.setOnMouseMoved(mouseEvent -> {
            saveButton.setDisable(isUsersTableEmpty());
            try {
            String passValue = passwordField.getText();
            String confirmPassValue = confirmPasswordField.getText();
            addNewUserButton.setDisable(isUserRoleEmpty() || isPasswordEmpty() ||
                    !hasDigit() || !hasUpper() || isUsernameEmpty() ||
                    isEmployeeFieldEmpty() || notificationIndicator.isVisible() ||!Objects.equals(passValue, confirmPassValue));
            roleSelector.setDisable(roleSelector.getValue().equals("Super Admin"));
            }catch (NullPointerException ignore){}
        });
    }



    /*******************************************************************************************************************
     *IMPLEMENTATION OF ACTION EVENT METHODS.
     *******************************************************************************************************************/
    void actionEventMethods() {
        employeeSelector.setOnAction(event -> {
            String selectedEmployee = employeeSelector.getValue();
            for (EmployeesData value : fetchAllEmployees()) {
                if (Objects.equals(value.getWork_id(), selectedEmployee)) {
                    usernameField.setText(value.getEmail());
                    roleSelector.setValue(null);
                    break;
                }
            }
        });

        cancelButton.setOnAction(e -> {
            resetFields();
            addNewUserButton.setText("➕ADD");
        });

        usersTable.setOnMouseClicked(mouseEvent -> {
            if (!isUsersTableEmpty()) {
                try {
                    String selectedItem = usersTable.getSelectionModel().getSelectedItem().getEmp_id();
                    for (UsersData item : usersTable.getItems()) {
                        if (Objects.equals(selectedItem, item.getEmp_id())) {
                            employeeSelector.setValue(item.getEmp_id());
                            usernameField.setText(item.getUsername());
                            roleSelector.setValue(item.getRole());
                            addNewUserButton.setText("🔄UPDATE");
                            break;
                        }
                    }
                    employeeSelector.setDisable(!usersTable.getSelectionModel().isEmpty());
                }catch (NullPointerException ignore){}
            }
        });

        saveButton.setOnAction(event -> {
            for (UsersData item : usersTable.getItems()) {
                    String user_id = item.getEmp_id();
                    boolean isChecked = item.getCheckBox().isSelected();
                if (item.getCheckBox().isSelected()) {
                    updateStatusOnly(user_id, (byte) 1);
                }else {
                    updateStatusOnly(user_id, (byte) 0);
                }
            }
            refreshTable();
        });

        addNewUserButton.setOnAction(action -> {
            String buttonText = addNewUserButton.getText();

            String empId = employeeSelector.getValue();
            String username = usernameField.getText();
            int role = getRoleIdByName(roleSelector.getValue());
            String password = passwordField.getText();
            String hashPlainText = EncryptDecrypt.hashPlainText(password);
            if (Objects.equals(buttonText, "➕ADD")) {
                ALERT = new UserAlerts("ADD USER", "ARE YOU SURE YOU WANT TO ADD SELECTED EMPLOYEE AS A USER?", "Please confirm your action to add this new user, else CANCEL to abort.");
                if (ALERT.confirmationAlert()) {
                    if(addNewUser(empId, role, username,hashPlainText, currentUser)> 0) {
                        NOTIFICATION.successNotification("ADD SUCCESSFUL", "You have successfully added " + username +" to users list");
                        refreshTable();
                        resetFields();
                        loadEmployeesSelector();
                    }else NOTIFICATION.errorNotification("OPERATION FAILED", "Operation to add " + username +
                            " to your list of users failed. Contact system admin for assistance");}
            }else {
                ALERT = new UserAlerts("ADD USER", "ARE YOU SURE YOU WANT TO ADD SELECTED EMPLOYEE AS A USER?", "Please confirm your action to add this new user, else CANCEL to abort.");
                if (ALERT.confirmationAlert()) {
                    if (updateUserLogins(empId, username, role, hashPlainText, currentUser) > 0) {
                        NOTIFICATION.successNotification("UPDATE SUCCESSFUL", "You have successfully updated " + username + " login details");
                        refreshTable();
                        resetFields();
                    } else
                        NOTIFICATION.errorNotification("UPDATE FAILED", "Operation to updates selected user failed, contact system admin for assistance");
                }
            }
        });
    }


}//END OF CLASS
