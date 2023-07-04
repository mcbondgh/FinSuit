package app.stages;

import app.AppStarter;
import app.alerts.UserAlerts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStages {
    UserAlerts ALERTS;
    public static void MainStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Homepage");
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

}
