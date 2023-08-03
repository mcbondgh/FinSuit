package app.controllers.resource;

import app.models.humanResource.HumanResourceModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateEmployeeController extends HumanResourceModel implements Initializable {
    @FXML private Label empIdLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empIdLabel.setText(ViewEmployeesController.staticEmployeeId);

    }

    public void validateSalaryInput(KeyEvent event) {
    }
}
