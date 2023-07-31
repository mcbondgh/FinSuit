package app.specialmethods;

import app.AppStarter;
import app.stages.AppStages;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    public static void FlipView(String fxmlFileName, BorderPane borderPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlFileName));
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000),borderPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        borderPane.setCenter(fxmlLoader.load());
        fadeTransition.play();
    }
    public static void FlipView(BorderPane borderPane, String fxmlFileName) throws IOException {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500),borderPane);
        FXMLLoader fxmlLoader = new FXMLLoader(AppStarter.class.getResource(fxmlFileName));
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1.0);
        borderPane.setCenter(fxmlLoader.load());
        fadeTransition.play();
    }

    public static <Strings> void setIdTypeParameters(ComboBox<String> comboBox) {
        String[] items = new String[]{"National Id", "Driving License", "SSNIT", "Voter Id", "Passport"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static void setGenderParameters(ComboBox<String> comboBox) {
        String[] items = {"Male", "Female", "Other"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static void setMaritalStatus(ComboBox<String> comboBox) {
        String[] items = {"Single", "Married", "Divorced"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static void setDesignation(ComboBox<String> comboBox) {
        String[] items = {"G.Manager", "C.F.O", "Finance Department", "Accounts Department", "Security & Sanitation", "Sales Department"};
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static void setQualification(ComboBox<String> comboBox) {
        String[] items = {"JHS Level", "SHS Level", "Tertiary Level", "Unspecified"};
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }

    public static void setLoanPeriod(ComboBox<Integer> comboBox) {
       int month = 120;
       for(int x = 3; x <= month; x+=3) {
           comboBox.getItems().add(x);
       }
    }
    public static void setInterestRate(ComboBox<Integer> comboBox) {
        int month = 100;
        for(int x = 1; x <= month; x++) {
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
                    dateLabel.setText(formatTime.format(time) + "\n" + formatDate.format(date));
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    public static String getTransactionId(long count) {
        String transactionId = "";
        if (count <=9) {transactionId = "00000000000" + count; }
        else if(count <= 99) {transactionId = "0000000000" + count;}
        else if (count == 100 || count <= 999) {transactionId = "000000000" + count;}//
        else if(count == 1000 || count <= 9999) {transactionId = "00000000" + count;}
        else if (count == 10000 || count <=99999) {transactionId = "0000000" + count;}
        else if(count == 100000 || count <=999999) {transactionId = "000000" + count;}
        else if (count == 10000000 || count <= 9999999) {transactionId = "00000" + count;}
        else if (count == 100000000 || count <= 99999999) {transactionId = "0000" + count;}
        else if (count == 1000000000 || count <= 999999999) {transactionId = "000" + count;}
        else if (count == 1000000000 || count <= 999999999) {transactionId = "00" + count;}
        else if (count == Long.parseLong("10000000000") && count <=Long.parseLong("99999999999")) {transactionId = "0" + count;}
        else {transactionId = String.valueOf(count);}
        return transactionId;
    }

    public static String generateAccountNumber(long count) {
        String accountNo = "";
        if (count <=9) {accountNo = "A00000000000" + count; }
        else if(count <= 99) {accountNo = "A0000000000" + count;}
        else if (count >= 100 || count <= 999) {accountNo = "A000000000" + count;}//
        else if(count == 1000 || count <= 9999) {accountNo = "A00000000" + count;}
        else if (count == 10000 || count <=99999) {accountNo = "A0000000" + count;}
        else if(count == 100000 || count <=999999) {accountNo = "A000000" + count;}
        else if (count == 10000000 || count <= 9999999) {accountNo = "A00000" + count;}
        else if (count == 100000000 || count <= 99999999) {accountNo = "A0000" + count;}
        else if (count == 1000000000 || count <= 999999999) {accountNo = "A000" + count;}
        else if (count == 1000000000 || count <= 999999999) {accountNo = "A00" + count;}
        else if (count == Long.parseLong("10000000000") && count <=Long.parseLong("99999999999")) {accountNo = "A0" + count;}
        else {accountNo = String.valueOf("A" + count);}
        return accountNo;
    }
    public static String generateLoanNumber(long count) {
        String loanId = "";
        if (count <=9) {loanId = "L00000000000" + count; }
        else if(count <= 99) {loanId = "L0000000000" + count;}
        else if (count >= 100 && count <= 999) {loanId = "L000000000" + count;}
        else if(count >= 1000 && count <= 9999) {loanId = "L00000000" + count;}
        else if (count == 10000 || count <=99999) {loanId = "L0000000" + count;}
        else if(count == 100000 || count <=999999) {loanId = "L000000" + count;}
        else if (count == 10000000 || count <= 9999999) {loanId = "L00000" + count;}
        else if (count == 100000000 || count <= 99999999) {loanId = "L0000" + count;}
        else if (count == 1000000000 || count <= 999999999) {loanId = "L000" + count;}
        else if (count == 1000000000 || count <= 999999999) {loanId = "L00" + count;}
        else if (count == Long.parseLong("10000000000") && count <=Long.parseLong("99999999999")) {loanId = "L0" + count;}
        else {loanId = String.valueOf("L" + count);}
        return loanId;
    }

    public static String generateEmployeeId(long count) {
        String empId = "";
        if (count <=9) {empId = "100000" + count; }
        else if (count >=10 && count <=99) {empId = "1000" + count;}
        else if (count >=100 && count <=999) {empId = "100" + count;}
        else if (count >=1000 && count <=9999) {empId = "10" + count;}
        else {empId = String.valueOf(count);}
        return empId;
    }


}//end of clas
