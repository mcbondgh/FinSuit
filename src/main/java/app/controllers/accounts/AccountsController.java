package app.controllers.accounts;

import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    @FXML
    private Pane menuIcon;
    @FXML private VBox menuContainer;
    @FXML private BorderPane borderPane;
    @FXML
    private Label pageTitle;
    @FXML private MFXButton createAccountButton, viewAccountButton;

    public static String pageTitlePlaceHolder;

    boolean isMenuContainerOpened() {return menuContainer.isVisible();}

    @FXML
    void menuIconClicked() {
        menuContainer.setVisible(!isMenuContainerOpened());
    }
    @FXML
    void HideMenuContainer() {
        menuContainer.setVisible(false);
    }
    String viewAccountsPath = "views/accounts/view-accountHolders.fxml";
    String newAccountsPath =  "views/accounts/createNewAccount-view.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageTitle.setText(pageTitlePlaceHolder);
        actionEventsImplementation();
        try {
            SpecialMethods.FlipView(borderPane, viewAccountsPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void actionEventsImplementation() {
        createAccountButton.setOnAction(event -> {
            try {
                SpecialMethods.FlipView(borderPane, newAccountsPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        viewAccountButton.setOnAction(event -> {
            try {
                SpecialMethods.FlipView(borderPane, viewAccountsPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }//END OF ACTION EVENT CONTAINER...

}//end of class...
