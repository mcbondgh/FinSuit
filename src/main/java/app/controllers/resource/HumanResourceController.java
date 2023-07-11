package app.controllers.resource;

import app.specialmethods.SpecialMethods;
import com.jfoenix.animation.alert.VerticalTransition;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.AbstractMFXDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialogBuilder;
import io.github.palexdev.materialfx.utils.AnimationUtils;
import io.github.palexdev.materialfx.utils.DragResizer;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HumanResourceController implements Initializable {

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     ********************************************************************************************************************/
    @FXML
    private Label pageTitle;
    public static String pageTitlePlaceHolder;
    @FXML private BorderPane borderPane;
    @FXML private VBox menuContainer;
    @FXML
    Pane menuIcon, addEmployeePane;
    @FXML
    MFXButton manageUsersButton, viewEmployeesButton, addEmployeeButton, saveButton, cancelButton;

    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        setAddEmployeeButtonClicked();
        setViewEmployeesButtonClicked();
        setManageUsersButtonClicked();

        ;}

    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATIONS
     ********************************************************************************************************************/






    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION.
     ********************************************************************************************************************/
    @FXML void menuIconClicked() {
        menuContainer.setVisible(!menuContainer.isVisible());
    }
    @FXML void HideMenuContainer() {
        menuContainer.setVisible(false);
    }

    void setViewEmployeesButtonClicked() {
        viewEmployeesButton.setOnAction(event ->  {
            try {
                SpecialMethods.FlipView("views/resource/view-employees-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    void setAddEmployeeButtonClicked() {
        addEmployeeButton.setOnAction(event -> {
               borderPane.setCenter(addEmployeePane);
        });
    }
    void setManageUsersButtonClicked() {
        manageUsersButton.setOnAction(event -> {
            try {
                SpecialMethods.FlipView("views/resource/manage-users-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}//end of class....
