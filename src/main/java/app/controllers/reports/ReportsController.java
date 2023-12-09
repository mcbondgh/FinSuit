package app.controllers.reports;

import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.ArrayChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
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
    @FXML private MFXButton transactionViewBtn, userEmployeesBtn, customersBtn, smsBtn, loanReportBtn;

    public static String pageTitlePlaceHolder;

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
        actionEventMethods();

    }

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/


    void actionEventMethods() {
        userEmployeesBtn.setOnAction(actionEvent -> {
            try {
//            borderPane.getChildren().remove(0);
                String fxmlFile = "views/reports/ui/user-logs.fxml";
                SpecialMethods.FlipView(borderPane, fxmlFile );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        transactionViewBtn.setOnAction(actionEvent -> {
            try {
//            borderPane.getChildren().remove(0);
                String fxmlFile = "views/reports/ui/transactional-statement.fxml";
                SpecialMethods.FlipView(borderPane, fxmlFile );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });











//        menuIcon.setOnMouseClicked(e -> {
//            menuContainer.setVisible(!isMenuContainerVisible());
//        });
//        menuContainer.setOnMouseExited(e -> {
//            menuContainer.setVisible(false);
//        });
    }

}//end of class..
