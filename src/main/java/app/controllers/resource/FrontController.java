package app.controllers.resource;

import app.models.humanResource.HumanResourceModel;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrontController implements Initializable{
    @FXML
    private BorderPane borderPane;
    @FXML private MenuButton menuButton;
    @FXML
    MenuItem manageUsersButton, viewEmployeesButton, addEmployeeButton, manageAgentsButton;
    @FXML
    Pane menuIcon;
    @FXML
    private Label empIdLabel, pageTitle;
    @FXML private VBox menuContainer;
    public static String pageTitlePlaceHolder;



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
            try {
                SpecialMethods.FlipView("views/resource/add-employee-page.fxml", borderPane);
            }catch (Exception e){e.printStackTrace();}
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
    private void setAgentsPageView() {
        manageAgentsButton.setOnAction(event -> {
            try {
                SpecialMethods.FlipView("views/resource/loan-agents-view.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        try {
            SpecialMethods.FlipView("views/resource/view-employees-page.fxml", borderPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setAddEmployeeButtonClicked();
        setViewEmployeesButtonClicked();
        setManageUsersButtonClicked();
        setAgentsPageView();
    }

    @FXML void menuIconClicked() {
        menuContainer.setVisible(!menuContainer.isVisible());
    }
    @FXML void HideMenuContainer() {
        menuContainer.setVisible(false);
    }
}
