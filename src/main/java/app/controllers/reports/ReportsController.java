package app.controllers.reports;

import app.stages.AppStages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    @FXML private Pane menuIcon;
    @FXML private VBox menuContainer;
    @FXML private BorderPane borderPane;
    @FXML private MenuButton loanReportUIButton;
    @FXML private MenuItem loansButton, customersButton, assignmentButton, paymentLogsButton, scheduleButton;

    public static String pageTitlePlaceHolder;

    public ReportsController() throws IOException {
    }

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
        boolean isMenuContainerVisible() {
            return menuContainer.isVisible();
        }



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);

    }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/

        Stage stage = AppStages.loanApplicationReport();
    @FXML private void showLoanReportsView(ActionEvent actionEvent) throws IOException {
        stage.showAndWait();
    }










}//end of class..
