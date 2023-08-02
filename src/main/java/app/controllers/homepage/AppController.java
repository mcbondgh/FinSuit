package app.controllers.homepage;

import app.controllers.accounting.AccountingController;
import app.controllers.accounts.AccountsController;
import app.controllers.customers.CustomersController;
import app.controllers.dashboard.DashboardController;
import app.controllers.loans.LoansController;
import app.controllers.messages.MessageBoxController;
import app.controllers.transactions.TransactionController;
import app.controllers.reports.ReportsController;
import app.controllers.resource.HumanResourceController;
import app.controllers.settings.SettingsController;
import app.models.homepage.AppModel;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.util.Duration;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController extends AppModel implements Initializable {


    AppStages APP_STAGES = new AppStages();
    public static String activeUserPlaceHolder;




    /*******************************************************************************************************************
    *********************************************** FXML NODE EJECTIONS
     *******************************************************************************************************************/
    @FXML private VBox sidebarPane;
    @FXML private TextField searchField;
    @FXML private Label appNameLabel, roleName, activeUsername;
    @FXML private Circle notificationPane;
    @FXML private Pane signOutButton, userProfilePane;
    @FXML private ImageView siteLogo, logoImage;
    @FXML private BorderPane borderPane;
    @FXML private MFXButton dashboardButton, customersButton, accountsButton, transactionButton, accountingButton;
    @FXML private MFXButton messageBoxButton, settingsButton, reportsButton, humanResourceButton, loanButton;
    @FXML private JFXListView<String> sortCustomersListView;




    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/
        boolean isSearchFieldEmpty() {
            return searchField.getText().isEmpty();
        }


    /*******************************************************************************************************************
     *********************************************** INPUT FIELD VALIDATION METHODS.
     ********************************************************************************************************************/
    @FXML void searchCustomerMethod() {
        sortCustomersListView.setVisible(!isSearchFieldEmpty());
    }





    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ******************************************************************************************************************/

    public void initialize(URL url, ResourceBundle resourceBundle) {
        DashboardController.pageTitlePlaceHolder = dashboardButton.getText();
        activeUsername.setText(activeUserPlaceHolder);
        setVariables(appNameLabel, siteLogo);

//        try {
//            SpecialMethods.FlipView("views/dashboard/dashboard-page.fxml", borderPane);
//        } catch (IOException e) {
//            throw new RuntimeException(e);}
        navigationHandler();
    }


    private void navigationHandler() {
        dashboardButton.setOnAction(action -> {
                try {
                    DashboardController.pageTitlePlaceHolder = dashboardButton.getText();
                    SpecialMethods.FlipView("views/dashboard/dashboard-page.fxml", borderPane);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        });
        accountingButton.setOnAction(event -> {
            try {
                AccountingController.pageTitlePlaceHolder = accountingButton.getText();
                SpecialMethods.FlipView("views/accounting/accounting-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        messageBoxButton.setOnAction(action -> {
            try {
                MessageBoxController.pageTitlePlaceHolder = messageBoxButton.getText();
                SpecialMethods.FlipView("views/messageBox/messagebox-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        customersButton.setOnAction(action -> {
            try {
                CustomersController.pageTitlePlaceHolder = customersButton.getText();
                SpecialMethods.FlipView("views/customers/customers-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        accountsButton.setOnAction(action -> {
            try {
                AccountsController.pageTitlePlaceHolder = accountsButton.getText();
                SpecialMethods.FlipView("views/accounts/accounts-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        transactionButton.setOnAction(action -> {
            try {
                TransactionController.pageTitlePlaceHolder = transactionButton.getText();
                SpecialMethods.FlipView("views/transactions/transaction-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loanButton.setOnAction(action -> {
            try {
                LoansController.pageTitlePlaceHolder = loanButton.getText();
                SpecialMethods.FlipView("views/loans/loans-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        humanResourceButton.setOnAction(action -> {
            try {
                HumanResourceController.pageTitlePlaceHolder = humanResourceButton.getText();
                SpecialMethods.FlipView("views/resource/humanresource-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        reportsButton.setOnAction(action -> {
            try {
                ReportsController.pageTitlePlaceHolder = reportsButton.getText();
                SpecialMethods.FlipView("views/reports/reports-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        settingsButton.setOnAction(action -> {
            try {
                SettingsController.pageTitlePlaceHolder = settingsButton.getText();
                SpecialMethods.FlipView("views/settings/settings-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        signOutButton.setOnMouseClicked(mouseEvent -> {
            try {
                signOutButton.getScene().getWindow().hide();
                AppStages.LoginStage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS
     ********************************************************************************************************************/
    @FXML private void expandSidebar(MouseEvent mouseEvent) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), sidebarPane);
        sidebarPane.setPrefWidth(170);
        transition.play();
        for (int x = 0; x < sidebarPane.getChildren().size(); x++) {
            sidebarPane.getChildren().get(x).setVisible(true);

        }
    }

    @FXML private void closeSidebar(MouseEvent mouseEvent) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), sidebarPane);
        sidebarPane.setPrefWidth(40);
        transition.play();
        for (int x = 0; x < sidebarPane.getChildren().size(); x++) {
            sidebarPane.getChildren().get(x).setVisible(false);
        }
    }
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