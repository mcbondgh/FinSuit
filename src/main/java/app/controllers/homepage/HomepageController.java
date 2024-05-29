package app.controllers.homepage;

import app.AppStarter;
import app.controllers.dashboard.DashboardController;
import app.models.homepage.AppModel;
import app.stages.AppStages;
import com.jfoenix.controls.JFXListView;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomepageController  extends AppModel implements Initializable {
    AppStages APP_STAGES = new AppStages();
    public static String activeUserPlaceHolder;




    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     *******************************************************************************************************************/
    @FXML
    private VBox sidebarPane;
    @FXML private TextField searchField;
    @FXML private Label appNameLabel, roleName, activeUsername;
    @FXML private Circle notificationPane;
    @FXML private Pane signOutButton, userProfilePane;
    @FXML private ImageView siteLogo, logoImage;
    @FXML private BorderPane borderPane;
    @FXML private MFXButton dashboardButton, customersButton, accountsButton, transactionButton, financeButton;
    @FXML private MFXButton messageBoxButton, settingsButton, reportsButton, humanResourceButton;
    @FXML private JFXListView<String> sortCustomersListView;
    @FXML private MFXButton loansButton;
    @FXML private HBox dashboardBox, customerBox, loanBox, accountBox;
    @FXML private HBox transactionBox, financeBox, messageBox, settingsBox, reportBox, resourceBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DashboardController.pageTitlePlaceHolder = dashboardButton.getText();
        activeUsername.setText(activeUserPlaceHolder);
        setVariables(appNameLabel, siteLogo);
        sidebarPane.getChildren().remove(customerBox);

        navigationHandler();
    }

    private void navigationHandler() {
        dashboardButton.setOnAction(action -> {
            DashboardController.pageTitlePlaceHolder = dashboardButton.getText();
            String viewPath = "views/dashboard/dashboard-page.fxml";
            try {
                FXMLLoader loader = new FXMLLoader(AppStarter.class.getResource(viewPath));
                borderPane.setCenter(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
//                    SpecialMethods.FlipView("views/dashboard/dashboard-page.fxml", borderPane);
        });
    }

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF KEY EVENTS
     *******************************************************************************************************************/



/*******************************************************************************************************************
 *********************************************** ACTION EVENT METHODS IMPLEMENTATION
 *******************************************************************************************************************/

@FXML void showUserProfile() {
    userProfilePane.setVisible(true);
    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), userProfilePane);
    fadeTransition.setFromValue(0);
    fadeTransition.setToValue(1.0);
    fadeTransition.play();
}
    @FXML void hideUserProfile() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), userProfilePane);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        userProfilePane.setVisible(false);
    }

}//end of class...
