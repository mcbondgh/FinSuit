package app.controllers.resource;

import app.abstract_classes.UserDetails;
import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.errorLogger.ErrorLogger;
import app.models.humanResource.HumanResourceModel;
import app.repositories.human_resources.LoanAgentsEntity;
import io.github.palexdev.materialfx.collections.ObservableStack;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AgentsController extends HumanResourceModel implements Initializable {

    String LOGGED_IN_USER;
    int USER_ID;
    UserNotification POPUP = new UserNotification();
    UserAlerts ALERTS;
    ErrorLogger errorLogger = new ErrorLogger();
    String className = this.getClass().getSimpleName();

    /*******************************************************************************************************
     * FXML FILE EJECTIONS
    ********************************************************************************************************/
    @FXML private MFXButton saveButton, deleteButton;
    @FXML private TextField nameField, numberField, otherNumberField;
    @FXML
    TextArea informationField;
    @FXML
    VBox verticalBox;

    @FXML private MFXLegacyTableView<LoanAgentsEntity> agentTable;
    @FXML
    TableColumn<LoanAgentsEntity, String> nameColumn, numberColumn, otherNumberColumn, dateColumn, countColumn;


    /*******************************************************************************************************
     * IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        USER_ID = getUserIdByName(AppController.activeUserPlaceHolder);
        validateInputFields();
        actionEventMethods();
        populateTable();
    }

    boolean nameFieldEmpty() {return nameField.getText().isEmpty();}
    boolean numberFieldEmpty(){return numberField.getText().isEmpty();}
    boolean tableEmpty() {return agentTable.getSelectionModel().isEmpty();}

    void populateTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("agentName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        otherNumberColumn.setCellValueFactory(new PropertyValueFactory<>("otherNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateJoined"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        agentTable.setItems(getAllAgents());
    }

    void resetFields() {
        nameField.clear();
        numberField.clear();
        populateTable();
    }

    /*******************************************************************************************************
     * INPUT FIELDS VALIDATION
     ********************************************************************************************************/
    void validateInputFields() {
        numberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                numberField.deletePreviousChar();
            }
            if (numberField.getText().length() > 10) {
                numberField.deletePreviousChar();
            }
        });
        otherNumberField.setOnKeyTyped(event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                otherNumberField.deletePreviousChar();
            }
            if (otherNumberField.getText().length() > 10) {
                otherNumberField.deletePreviousChar();
            }
        });

        verticalBox.setOnMouseMoved(event ->
                saveButton.setDisable(nameFieldEmpty() || numberFieldEmpty()));

        deleteButton.setDisable(tableEmpty());
    }


    /*******************************************************************************************************
     * ACTION EVENT METHODS IMPLEMENTATION
     ********************************************************************************************************/

    void actionEventMethods() {

        //set the table to allow only single selection
        agentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        saveButton.setOnAction(actionEvent -> {
            //GET THE CURRENT BUTTON TEXT VALUE
            MFXButton button = (MFXButton) actionEvent.getSource();

                String name = nameField.getText();
                String number = numberField.getText();
                String otherNumber = otherNumberField.getText();
                String info = informationField.getText();
                LoanAgentsEntity loanAgents = new LoanAgentsEntity(name, number, otherNumber, info, USER_ID);

            if (button.getText().equals("SAVE")) {
                ALERTS = new UserAlerts("SAVE AGENT", "Do you wish to add this person to your list of agents",
                        "please confirm your action to SAVE else CANCEL to abort");
                if (ALERTS.confirmationAlert()) {
                    if(saveAgent(loanAgents) > 0) {
                        POPUP.successNotification("AGENT SAVED", "Nice, you have successfully save this agent.");
                        Platform.runLater(this::resetFields);
                    } else {
                        POPUP.errorNotification("FAILED TO SAVE", "Failed to save agent, please try again");
                    }
                }
            } else {
                System.out.println(button.getAccessibleText());
                ALERTS = new UserAlerts("UPDATE AGENT DATA", "Do you wish to update the selected agent's details?",
                        "please confirm your action to UPDATE else CANCEL to abort");
                int agentId = agentTable.getSelectionModel().getSelectedItem().getAgentId();
                if (ALERTS.confirmationAlert()) {
                    loanAgents.setAgentId(agentId);
                    if(updateAgentData(loanAgents) > 0 ){
                        POPUP.successNotification("DATA UPDATED", "Nice, you have successfully updated agent's details.");
                        Platform.runLater(this::resetFields);
                    } else {
                        POPUP.errorNotification("FAILED TO SAVE", "Unable to updated agent's data. Retry the process again");
                    }
                }
            }
        });

        //
        deleteButton.setOnAction(event-> {
            ALERTS = new UserAlerts("DELETE AGENT", "ARE YOU SURE YOU WANT TO DELETE THE SELECTED AGENT?",
                    "this process will remove the agent from your list of agents, confirm to remove");
            if (ALERTS.confirmationAlert()) {
                int id =  agentTable.getSelectionModel().getSelectedItem().getAgentId();
                deleteAgent(id);
                Platform.runLater(this::populateTable);
            }
        });

        //PERFORM ACTION EVENT ON THE TABLE WHENEVER AN ITEM IS SELECTED
        agentTable.setOnMouseMoved(event -> {
                LoanAgentsEntity selection = agentTable.getSelectionModel().getSelectedItem();
                boolean emptySelection = agentTable.getSelectionModel().isEmpty();
                saveButton.setText(emptySelection ? "SAVE" : "UPDATE");
                deleteButton.setDisable(emptySelection);
            if (!(tableEmpty() && emptySelection)) {
                nameField.setText(selection.getAgentName());
                numberField.setText(selection.getMobileNumber());
                otherNumberField.setText(selection.getOtherNumber());
                informationField.setText(selection.getInformation());
            }
        });
    }


}//end of class...
