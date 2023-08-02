package app.controllers.resource;

import app.fetchedData.human_resources.EmployeesData;
import app.models.humanResource.HumanResourceModel;
import com.jfoenix.controls.JFXCheckBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewEmployeesController extends HumanResourceModel implements Initializable  {

    /*******************************************************************************************************************
     *********************************************** EJECTION OF FXML FILES
     ********************************************************************************************************************/
    @FXML
    MFXLegacyTableView<EmployeesData> employeesTable;
    @FXML private TableColumn<EmployeesData, String> idColumn;
    @FXML private TableColumn<EmployeesData, String> fullNameColumn;
    @FXML private TableColumn<EmployeesData, String> mobileNumberColumn;
    @FXML private TableColumn<EmployeesData, LocalDate> employmentDateColumn;
    @FXML private TableColumn<EmployeesData, String> designationColumn;
    @FXML private TableColumn<EmployeesData, Double> salaryColumn;
    @FXML private TableColumn<EmployeesData, Label> statusColumn;
    @FXML private TableColumn<EmployeesData, JFXCheckBox> actionColumn;
    @FXML private TableColumn<EmployeesData, String> genderColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    void populateTableFields() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("emp_id"));
    }




}// end of class
