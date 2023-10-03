package app.stages;

import app.AppStarter;
import app.alerts.UserAlerts;
import app.specialmethods.SpecialMethods;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AppStages {
    UserAlerts ALERTS;
    public static void MainStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        Stage stage = new Stage();
        stage.setTitle("Homepage");
        Image icon = SpecialMethods.setLogoImage();
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
        stage.getScene().getWindow().setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            UserAlerts alerts = new UserAlerts("SIGN OUT", "PLEASE CLOSE THE APPLICATION BY USING THE SIGN OUT BUTTON");
            alerts.informationAlert();
        });
    }
    public static void LoginStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void testPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("test-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Test Page");
        stage.setScene(scene);
        stage.show();
    }
    public static void loanScheduleStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/loans/loan-schedule-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Customer Loan Schedule");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static Stage loanApplicationStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/loans/application-forms/application-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Loan Application");
        stage.setScene(scene);
        stage.centerOnScreen();
        return  stage;
    }

    public static Stage loanCalculatorStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/loans/application-forms/calculator.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Loan Calculator");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.initStyle(StageStyle.DECORATED);
        return  stage;
    }
    public static Stage depositStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/transactions/deposit-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Make Deposit");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        return  stage;
    }
    public static Stage accountBalanceStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/transactions/check-balance-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Make Deposit");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        return  stage;
    }
    public static Stage withdrawalStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/transactions/withdrawal-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Make Deposit");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        return  stage;
    }
    public static Stage loanPaymentStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/loans/loan-payment-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Collect Loan Payment");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        return  stage;
    }
    public static Stage editCustomerDataStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/accounts/edit-customer-details-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Edit Customer Data");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        return  stage;
    }

    public static Stage updateEmployeeDetailsStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/resource/updateEmployee-popup.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Update Employee Data");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        return  stage;
    }
    public static Stage databaseFailedStage() {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("database-failed-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Database Connection");
            stage.setScene(scene);
            stage.centerOnScreen();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return  stage;
    }

    public static Stage previewApplicantStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("views/accounts/preview-customer-details.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Update Employee Data");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        return  stage;
    }

}//end of class...
