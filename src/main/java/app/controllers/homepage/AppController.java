package app.controllers.homepage;

import app.AppStarter;
import app.controllers.finance.FinanceController;
import app.controllers.accounts.AccountsController;
import app.controllers.customers.CustomersController;
import app.controllers.dashboard.DashboardController;
import app.controllers.loans.LoansController;
import app.controllers.messages.MessageBoxController;
import app.controllers.reports.ReportsController;
import app.controllers.resource.FrontController;
import app.controllers.settings.SettingsController;
import app.controllers.transactions.TransactionController;
import app.models.homepage.AppModel;
import app.specialmethods.SpecialMethods;
import app.stages.AppStages;
import com.jfoenix.controls.JFXListView;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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

public class AppController extends AppModel implements Initializable {

    AppStages APP_STAGES = new AppStages();
    public static String activeUserPlaceHolder;
    public static String rolePlaceholder;




    /*******************************************************************************************************************
    *********************************************** FXML NODE EJECTIONS
     *******************************************************************************************************************/
    @FXML public VBox sidebarPane;
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
        roleName.setText(rolePlaceholder);
        setVariables(appNameLabel, siteLogo);
        sidebarPane.getChildren().remove(customerBox);


//        try {
//            SpecialMethods.FlipView("views/dashboard/dashboard-page.fxml", borderPane);
//        } catch (IOException e) {
//            throw new RuntimeException(e);}
        navigationHandler();
    }


    private void navigationHandler() {
        dashboardButton.setOnAction(action -> {
            DashboardController.pageTitlePlaceHolder = dashboardButton.getText();
            try {
                SpecialMethods.FlipView("views/dashboard/dashboard-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        financeButton.setOnAction(event -> {
            FinanceController.pageTitlePlaceHolder = financeButton.getText();
            try {
                SpecialMethods.FlipView("views/finance/finance-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        messageBoxButton.setOnAction(action -> {
            MessageBoxController.pageTitlePlaceHolder = messageBoxButton.getText();
            try {
                SpecialMethods.FlipView("views/messageBox/messagebox-view.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        customersButton.setOnAction(action -> {
            CustomersController.pageTitlePlaceHolder = customersButton.getText();
            try {
                SpecialMethods.FlipView("views/customers/customers-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        accountsButton.setOnAction(action -> {
            AccountsController.pageTitlePlaceHolder = accountsButton.getText();
            try {
                SpecialMethods.FlipView("views/accounts/accounts-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        transactionButton.setOnAction(action -> {
            TransactionController.pageTitlePlaceHolder = transactionButton.getText();
            try {
                SpecialMethods.FlipView("views/transactions/transaction-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        loansButton.setOnAction(action -> {
            LoansController.pageTitlePlaceHolder = loansButton.getText();
            try {
                SpecialMethods.FlipView("views/loans/loans-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        humanResourceButton.setOnAction(action -> {
            FrontController.pageTitlePlaceHolder = humanResourceButton.getText();
            try {
                SpecialMethods.FlipView("views/resource/humanresource-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        reportsButton.setOnAction(action -> {
            ReportsController.pageTitlePlaceHolder = reportsButton.getText();
            try {
                SpecialMethods.FlipView("views/reports/reports-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        settingsButton.setOnAction(action -> {
            SettingsController.pageTitlePlaceHolder = settingsButton.getText();
            try {
                SpecialMethods.FlipView("views/settings/settings-page.fxml", borderPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        signOutButton.setOnMouseClicked(mouseEvent -> {
            try {
                signOutButton.getScene().getWindow().hide();
                Thread.getAllStackTraces().clear();
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
//        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), sidebarPane);
//        borderPane.setPrefWidth(1127);
//        transition.setDelay(Duration.seconds(5000));
//        transition.play();
//        for (int x = 0; x < sidebarPane.getChildren().size(); x++) {
//            sidebarPane.getChildren().get(x).setVisible(true);
//        }
    }

    @FXML private void closeSidebar(MouseEvent mouseEvent) {
//        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), sidebarPane);
//        borderPane.setPrefWidth(1269);
//        transition.play();
//        for (int x = 0; x < sidebarPane.getChildren().size(); x++) {
//            sidebarPane.getChildren().get(x).setVisible(false);
//        }
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