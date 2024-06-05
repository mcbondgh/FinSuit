package app.controllers.settings;

import app.stages.AppStages;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PasswordResetController implements Initializable {

    @FXML
    MFXButton sendButton, verifyButton;
    @FXML
    Label timerLabel;
    @FXML
    VBox otpContainer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            actionEventMethods();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    TimerTask timerTask = new TimerTask() {

        final AtomicInteger timeCounter = new AtomicInteger(59);
        @Override
        public void run() {
           if (timeCounter.get() != 0) {
               timeCounter.decrementAndGet();
               Platform.runLater(()-> {
                   timerLabel.setText(String.valueOf(timeCounter.getAcquire()));
               });
           }
            if (timeCounter.get() == 0) {
                this.cancel();
            }

        };
    };

    void actionEventMethods() throws IOException {
        Timer timer = new Timer();
        sendButton.setOnAction(action -> {
            timer.scheduleAtFixedRate(timerTask, 1000, 1000);
            otpContainer.setDisable(false);
        });

        verifyButton.setOnAction(actionEvent -> {
            System.out.println(timerLabel.getText());
        });
    }


}
