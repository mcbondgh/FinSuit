package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.loans.LoansModel;
import app.repositories.loans.LoansTableEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AssignSupervisorController extends LoansModel implements Initializable {
    public AssignSupervisorController() {}
    UserNotification NOTIFY = new UserNotification();
    UserAlerts ALERTS;
    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);



    /*******************************************************************************************************************
     *********************************************** FXML FILE EJECTION.
     *******************************************************************************************************************/
    @FXML
    private TableView<LoansTableEntity> supervisorsTable;
    @FXML private TableColumn<LoansTableEntity, Integer> idColumn;
    @FXML private TableColumn<LoansTableEntity, String > loanNumberColumn;
    @FXML private TableColumn<LoansTableEntity, String>  nameColumn;
    @FXML private  TableColumn<LoansTableEntity, String > dateColumn;
    @FXML private TableColumn<LoansTableEntity, String> statusColumn;
    @FXML private TableColumn<LoansTableEntity, ComboBox<String>> supervisorColumn;

    @FXML private MFXButton saveButton, clearButton, assignmentButton;
    @FXML private BorderPane borderPane;

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     *******************************************************************************************************************/
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            populateTableView();
        }

        void populateTableView() {
            supervisorsTable.getItems().clear();
            supervisorsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            idColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
            loanNumberColumn.setCellValueFactory(new PropertyValueFactory<>("loanNo"));
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("loanType"));
            supervisorColumn.setCellValueFactory(new PropertyValueFactory<>("supervisorSelector"));
            supervisorsTable.setItems(getLoansUnderApplicationStage());
        }

        private int processLoanAssignments() {
            int flag = 0;
            for (LoansTableEntity items : supervisorsTable.getItems()) {
                if (!(items.getSupervisorSelector().getValue() == null)) {
                    String loanNo = items.getLoanNo();
                    String assignedOfficer = items.getSupervisorSelector().getValue();
                    String employeeId = getEmployeeIdByUsername(assignedOfficer);
                    flag = saveAssignedGroupSupervisor(employeeId, loanNo, loggedInUserId);
                    flag += updateLoanApplicationStatus("processing", loanNo, loggedInUserId);
                }
            }
            return flag;
        }
    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS.
     *******************************************************************************************************************/
    private  boolean isTableEmpty() {
        return supervisorsTable.getItems().isEmpty();
    }
    public void checkTableStatus() {
        saveButton.setDisable(isTableEmpty());
    }

    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     *******************************************************************************************************************/
    @FXML public void assignmentButtonOnAction(ActionEvent actionEvent) throws IOException {
        int tableSize = supervisorsTable.getItems().size();
        int assignedLoans = 0;
        for (LoansTableEntity items : supervisorsTable.getItems()) {
            if (items.getSupervisorSelector().getValue() == null) {
                items.getSupervisorSelector().setStyle("-fx-background-color:#ffa99c");
            }
            else {
                items.getSupervisorSelector().setStyle("-fx-background-color:#eee");
                assignedLoans ++;
            }
        }
        if (assignedLoans == 0) {
            NOTIFY.errorNotification("UNASSIGNED LOANS", "Please assign at least 1 loan application to an officer.");
        } else {
            ALERTS = new UserAlerts("SAVE ASSIGNMENT", "You hava total loan application of " + tableSize +" and you have assign " + assignedLoans + " do you wish to save?",
                    "please confirm to save your current assignment else CANCEL to abort operation");
            if (ALERTS.confirmationAlert()) {
                if (processLoanAssignments() > 0) {
                    NOTIFY.successNotification("UPDATE SUCCESSFUL", "Good, applied loans have successfully been assigned to supervisors");
                    populateTableView();
                } else NOTIFY.errorNotification("UPDATE FAILED", "Sorry, your request to assign loan officers failed, please contact system admin for assistance.");
            }
        }
    }

    @FXML private void setClearButtonOnAction() {
        int selectedSize = supervisorsTable.getSelectionModel().getSelectedItems().size();
        if (selectedSize == 0) {
            NOTIFY.informationNotification("EMPTY SELECTION", "Please make at least one selection to reset assignment");
        } else {
            for (int i = 0; i < selectedSize; i++) {
                supervisorsTable.getSelectionModel().getSelectedItems().get(i).getSupervisorSelector().setValue(null);
            }
        }
    }

    @FXML void showAssignedSupervisorsView() throws IOException {
        String filePath = "views/loans/view-assigned-supervisors.fxml";
        borderPane.getCenter().setStyle("-fx-alignment:center");
        SpecialMethods.FlipView(borderPane, filePath);
    }


}//end of clas...
