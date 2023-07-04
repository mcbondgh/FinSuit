package app.controllers.login;

import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    AppStages APP_STAGES = new AppStages();


    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTION
     ********************************************************************************************************************/
    @FXML private Label appNameHeader, resetPasswordLink;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private MFXButton loginButton, closeButton;



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS.
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonClickEvent();
    }



    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS
     ********************************************************************************************************************/
    void buttonClickEvent() {
        loginButton.setOnAction(event -> {
            try {
                AppStages.MainStage();
                loginButton.getScene().getWindow().hide();
            } catch (IOException e) {throw new RuntimeException(e);}
        });

        closeButton.setOnAction(event -> {
            System.exit(0);
        });
    }



}//end of class...
