package app.controllers.loans;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.documents.DocumentGenerator;
import app.models.MainModel;
import app.repositories.loans.AssignedSupervisors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


public class ViewAssignedSupervisorsController extends MainModel implements Initializable {

    final String ACTIVE_USER = AppController.activeUserPlaceHolder;
    final String USER_ROLE = AppController.rolePlaceholder;
    public Hyperlink exportButton;


    @FXML private TableView<AssignedSupervisors> tableView;
    @FXML
    TableColumn<AssignedSupervisors, String> nameColumn;
    @FXML
    TableColumn<AssignedSupervisors, String> loanNumberCol;
    @FXML
    TableColumn<AssignedSupervisors, String> statusCol;
    @FXML TableColumn<AssignedSupervisors, Integer> counterCol;
    @FXML TableColumn<AssignedSupervisors, Integer> numberColumn;
    @FXML TableColumn<AssignedSupervisors, Integer> payableColumn;
    @FXML TableColumn<AssignedSupervisors, Integer> totalPayColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTable();
    }

    void populateTable() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        loanNumberCol.setCellValueFactory(new PropertyValueFactory<>("loanNumber"));
        counterCol.setCellValueFactory(new PropertyValueFactory<>("counter"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        payableColumn.setCellValueFactory(new PropertyValueFactory<>("repayment"));
        totalPayColumn.setCellValueFactory(new PropertyValueFactory<>("totalPayment"));
        tableView.setItems(getAllAssignedSupervisors(ACTIVE_USER));
    }

    @FXML void exportButtonClicked() {
        if (!tableView.getItems().isEmpty()) {
            exportButton.setDisable(true);
            exportButton.setText("Exporting...");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3)));
            timeline.getStatus();
            timeline.play();
            DocumentGenerator DOC = new DocumentGenerator();
            UserNotification POPUP = new UserNotification();
            int exportStatus = DOC.exportAssignedSupervisorTable(ACTIVE_USER, tableView);

            timeline.setOnFinished(x -> {
                exportButton.setDisable(false);
                exportButton.setText(exportButton.getAccessibleText());
                if (exportStatus == 200) {
                    POPUP.successNotification("EXPORT SUCCESS", "Nice, table data successfully exported, please check directory 'FINSUIT DOCUMENT");
                } else {
                    POPUP.errorNotification("FAILED EXPORT", "Table data failed during export process, retry process again.");
                }
            });
        } else {
            new UserAlerts("EMPTY TABLE", "The table has no available data to export.").informationAlert();
        }
    }



}//end of class...
