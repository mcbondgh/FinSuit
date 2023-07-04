package app;

import app.stages.AppStages;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppStages.LoginStage();
    }

    public static void main() {
        launch();
    }
}