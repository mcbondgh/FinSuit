package app.controllers.loans;

import app.models.MainModel;
import app.repositories.loans.AssignedSupervisors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;


public class ViewAssignedSupervisorsController extends MainModel implements Initializable {

    @FXML private TableView<AssignedSupervisors> tableView;
    @FXML
    TableColumn<AssignedSupervisors, String> nameColumn;
    @FXML
    TableColumn<AssignedSupervisors, String> loanNumberCol;
    @FXML
    TableColumn<AssignedSupervisors, String> statusCol;
    @FXML TableColumn<AssignedSupervisors, Integer> counterCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();
    }

    void populateTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("superName"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));
        loanNumberCol.setCellValueFactory(new PropertyValueFactory<>("loanNumber"));
        counterCol.setCellValueFactory(new PropertyValueFactory<>("counter"));
        tableView.setItems(getAllAssignedSupervisors());
    }



}//end of class...
