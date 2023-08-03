package app.controllers.resource;

import app.fetchedData.human_resources.EmployeesData;
import app.models.humanResource.HumanResourceModel;
import app.stages.AppStages;
import com.jfoenix.controls.JFXCheckBox;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
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
    public static String staticEmployeeId;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTableFields();
        getSelectedEmployee();
    }


    void populateTableFields() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("work_id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("full_name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobile_number"));
        employmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedEmploymentDate"));
        designationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusLabel"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("actionCheckBox"));
        employeesTable.setItems(fetchEmployeeSummaryData());
    }


    void getSelectedEmployee() {
        employeesTable.setOnMouseClicked(mouseEvent -> {
           int mouseCount = mouseEvent.getClickCount();
           if (mouseCount == 2) {
               staticEmployeeId = employeesTable.getSelectionModel().getSelectedItem().getWork_id();
               try {
                   AppStages.updateEmployeeDetailsStage().show();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
        });
    }




}// end of class
