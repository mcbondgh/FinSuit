package app;

import app.stages.AppStages;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppStarter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            AppStages.PayrollStage().show();
        }catch (Exception e) {
            e.printStackTrace();
           AppStages.PayrollStage().show();
        }
    }
    public static void main() {
        launch();
    }
    
    public void closeButton(ActionEvent event) {
        System.exit(0);
    }
}