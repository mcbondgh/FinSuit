package app.controllers.login;

import app.controllers.homepage.AppController;
import app.repositories.business.BusinessInfoEntity;
import app.models.MainModel;
import app.repositories.users.UsersData;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LoginController extends MainModel implements Initializable{

//    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    AppStages APP_STAGES = new AppStages();


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTION
     ********************************************************************************************************************/
    @FXML private Label appNameHeader;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private MFXButton loginButton, closeButton;
    @FXML private Label loginIndicator;
    @FXML private ImageView logoViewer;
    @FXML private Hyperlink resetPasswordLink, activateButton;
    @FXML private MFXProgressBar progressBar;
    @FXML private VBox loadingPane;


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/

    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonClickEvent();
        setLoginParameters();
    }

    void setLoginParameters() {
        ArrayList<BusinessInfoEntity> items = getBusinessInfo();
        appNameHeader.setText(items.get(0).getName());
        byte[] logoData = items.get(0).getLogo();
        logoViewer.setImage(new Image(new ByteArrayInputStream(logoData)));

//        for (BusinessInfoEntity items : getBusinessInfo()) {
//            appNameHeader.setText(items.getName());
//            byte[] getImageSource = items.getLogo();
//            logoViewer.setImage(new Image(new ByteArrayInputStream(getImageSource)));
//        }
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS
     ********************************************************************************************************************/
    void buttonClickEvent() {
        loginButton.setOnAction(event -> {
//            loginIndicator.setVisible(true);
            try {
                String username = usernameField.getText();
                String password = passwordField.getText();
                AuthenticateUserLogins authenticateUserLogins = new AuthenticateUserLogins(username, password);
                UsersData usersData = authenticateUserLogins.getDetails();

                if(!Objects.equals(usersData.getUsername(), null)) {
                    AppController.activeUserPlaceHolder = usersData.getUsername();
                    AppController.rolePlaceholder = usersData.getRole();
                    loginIndicator.setVisible(true);
                    loginIndicator.setStyle("-fx-background-color:green");
                    loginIndicator.setText("✔ Authentication successful");
                    //show homepage after user successful login and hide the login page.
                    AppStages.mainStage();
                    loginButton.getScene().getWindow().hide();

                    int userId = getUserIdByName(usersData.getUsername());
                    byte roleId = (byte) getRoleIdByName(usersData.getRole());
                    recordUserLogin(userId, roleId);
                } else {
                    loginIndicator.setVisible(true);
                }
            } catch (Exception e) {
                loginIndicator.setVisible(true);
//                throw new RuntimeException(e);
            }

        });

        closeButton.setOnAction(event -> {
            Platform.exit();
//            System.exit(0);
        });

        resetPasswordLink.setOnAction(actionEvent -> {
            try {
                AppStages.passwordResetStage().showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



}//end of class...
