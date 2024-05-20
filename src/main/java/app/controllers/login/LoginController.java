package app.controllers.login;

import app.controllers.homepage.AppController;
import app.repositories.business.BusinessInfoEntity;
import app.models.MainModel;
import app.repositories.users.UsersData;
import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController extends MainModel implements Initializable{

    AppStages APP_STAGES = new AppStages();


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTION
     ********************************************************************************************************************/
    @FXML private Label appNameHeader, resetPasswordLink;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private MFXButton loginButton, closeButton;
    @FXML private Pane loginIndicator;
    @FXML private ImageView logoViewer;


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
            loginIndicator.setVisible(true);
            try {
                AppController.activeUserPlaceHolder = Objects.equals(usernameField.getText(), "") ? "Admin" : usernameField.getText();                ;
                AppStages.MainStage();
                loginButton.getScene().getWindow().hide();
            } catch (IOException e) {throw new RuntimeException(e);}
        });
        closeButton.setOnAction(event -> {
            System.exit(0);
        });
    }



}//end of class...
