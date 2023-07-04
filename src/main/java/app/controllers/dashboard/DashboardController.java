package app.controllers.dashboard;

import app.specialmethods.SpecialMethods;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML private Label pageTitle, timeLabel, dateLabel;
    public static String pageTitlePlaceHolder;



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        SpecialMethods.generateTime(dateLabel, timeLabel);
    }



    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** ACTION EVENTS METHODS IMPLEMENTATION
     ********************************************************************************************************************/

}//END OF CLASS...
