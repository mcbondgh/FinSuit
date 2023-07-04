package app.specialmethods;

import app.AppStarter;
import app.stages.AppStages;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class SpecialMethods {


    //THIS METHOD WHEN INVOKED SHALL TAKE THE NAME OF THE IMAGE AND THEN DISPLAY IT TO THE IMAGE-VIEW...
    public void setLogoImage(String imageName, ImageView imageViewName) {
        File filePath = new File(imageName);
        Image getImage = new Image(filePath.toString());
        imageViewName.setImage(getImage);
    }
    public void FlipView(String fxmlFileName, BorderPane borderPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlFileName));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000),borderPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        borderPane.setCenter(fxmlLoader.load());
        fadeTransition.play();
    }

    public void setIdTypeParameters(ComboBox<String> comboBox) {
        String[] items = new String[]{"National Id", "Driving License", "SSNIT", "Voter Id", "Passport"};
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public void setGenderParameters(ComboBox<String> comboBox) {
        String[] items = {"Male", "Female", "Other"};
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public void setLoanPeriod(ComboBox<Integer> comboBox) {
       int month = 120;
       for(int x = 0; x <= 120; x+=3) {
           comboBox.getItems().add(x);
       }
    }

    public static void generateTime(Label dateLabel, Label timeLabel) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            final int value = 0;
            @Override
            public void run() {
                LocalTime time = LocalTime.now();
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss a");
                DateTimeFormatter formatDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
                Platform.runLater(()->{
                    timeLabel.setText(formatTime.format(time));
                    dateLabel.setText(formatDate.format(date));
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }


}//end of clas
