package app.specialmethods;

import app.AppStarter;
import app.enums.PaymentMethods;
import app.models.MainModel;
import app.repositories.BusinessInfoEntity;
import app.repositories.roles.UserRolesData;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class SpecialMethods {

    static MainModel MODEL = new MainModel();


    //THIS METHOD WHEN INVOKED SHALL TAKE THE NAME OF THE IMAGE AND THEN DISPLAY IT TO THE IMAGE-VIEW...
    public static Image setLogoImage() {
        String filePath = "";
        for(BusinessInfoEntity item : MODEL.getBusinessInfo()) {
            filePath = "C:\\Users\\Druglord\\Documents\\FinSuit\\src\\main\\resources\\app\\uploads\\" + item.getLogo();
        }
        return new Image(filePath);
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


    public static void setUserRoleParameters(ComboBox<String> comboBox) {
        List<String> x = new ArrayList<>();
        for (UserRolesData items :MODEL.getUserRoles()) {
            x.add(items.getRole_name());
            Collections.sort(x);
        }
        for (String var : x) {
            comboBox.getItems().add(var);
        }
    }
    public static  void setIdTypeParameters(ComboBox<String> comboBox) {
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
        String[] items = {"Single", "Married", "Divorced", "Separated"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static void setDesignation(ComboBox<String> comboBox) {
        String[] items = {"G. Manager", "C.F.O", "Finance Department", "Accounts Department", "Security & Sanitation", "Sales Department"};
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

    public static void setInitialDepositAmount(ComboBox<Double> comboBox) {
        double [] amountValue = {10.00, 20.00, 30.00, 40.00, 50.00};
        for (double var : amountValue) {
            comboBox.getItems().add(var);
        }
    }
    public static void setLoanPeriod(ComboBox<Integer> comboBox) {
       int month = 120;
       for(int x = 1; x <= month; x++) {
           comboBox.getItems().add(x);
       }
    }
    public static void setRateValue(ComboBox<Integer> comboBox) {
        int month = 100;
        for(int x = 1; x <= month; x++) {
            comboBox.getItems().add(x);
        }
    }
    public static void generateTime(Label dateLabel) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalTime time = LocalTime.now();
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss a");
                DateTimeFormatter formatDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);
                Platform.runLater(()->{
//                    timeLabel.setText(formatTime.format(time));
                    dateLabel.setText(formatTime.format(time).concat(", ")+ formatDate.format(date));
                });
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    public static void setAccountType(ComboBox<String> comboBox) {
        String[] items = {"Savings Account", "Susu Account", "Current Account", "Other Account"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }



    public static void setLoanType(ComboBox<String> comboBox) {
        String[] items = {"Business Loan", "Church Loan", "Group Loan", "Personal Loan", "Agro Loan"};
        Arrays.sort(items);
        Arrays.stream(items).toList().forEach(item -> comboBox.getItems().add(item));
    }
    public static void setRelationshipTypes(ComboBox<String> comboBox) {
        String[] items = {"Father", "Mother", "Brother", "Sister", "Uncle", "Aunty", "Friend", "Cousin", "Other", "Guardian", "Relative"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }

    public static void setTransactionType(ComboBox<String> comboBox) {
        String[] items = {"Withdrawal", "Deposit", "Transfer"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static void setBanks(ComboBox<String> comboBox) {
        String[] items = {"ADB", "Commercial Bank", "CBG", "Mobile Money", "UMB", "Fidelity Bank", "Access Bank", "ABSA Bank", "Rural Bank"};
        Arrays.sort(items);
        for (String x : items) {
            comboBox.getItems().add(x);
        }
    }
    public static String getTransactionId(long count) {
        String transactionId = "";
        if (count <=9) {transactionId = "00000000000" + count; }
        else if(count <= 99) {transactionId = "0000000000" + count;}
        else if (count <= 999) {transactionId = "000000000" + count;}//
        else if(count <= 9999) {transactionId = "00000000" + count;}
        else if (count <=99999) {transactionId = "0000000" + count;}
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
        if (count <=9) {accountNo = "100000000000" + count; }
        else if(count <= 99) {accountNo = "10000000000" + count;}
        else if (count >= 100 || count <= 999) {accountNo = "1000000000" + count;}//
        else if(count == 1000 || count <= 9999) {accountNo = "100000000" + count;}
        else if (count == 10000 || count <=99999) {accountNo = "10000000" + count;}
        else if(count == 100000 || count <=999999) {accountNo = "1000000" + count;}
        else if (count == 10000000 || count <= 9999999) {accountNo = "100000" + count;}
        else if (count == 100000000 || count <= 99999999) {accountNo = "10000" + count;}
        else if (count == 1000000000 || count <= 999999999) {accountNo = "1000" + count;}
        else if (count == 1000000000 || count <= 999999999) {accountNo = "100" + count;}
        else if (count == Long.parseLong("10000000000") && count <=Long.parseLong("99999999999")) {accountNo = "10" + count;}
        else {accountNo = String.valueOf(count);}
        return accountNo;
    }
    public static String generateLoanNumber(long count) {
        String loanId = "";
        if (count <=9) {loanId = "200000000000" + count; }
        else if(count <= 99) {loanId = "20000000000" + count;}
        else if (count >= 100 && count <= 999) {loanId = "2000000000" + count;}
        else if(count >= 1000 && count <= 9999) {loanId = "200000000" + count;}
        else if (count == 10000 || count <=99999) {loanId = "20000000" + count;}
        else if(count == 100000 || count <=999999) {loanId = "2000000" + count;}
        else if (count == 10000000 || count <= 9999999) {loanId = "200000" + count;}
        else if (count == 100000000 || count <= 99999999) {loanId = "20000" + count;}
        else if (count == 1000000000 || count <= 999999999) {loanId = "2000" + count;}
        else if (count == 1000000000 || count <= 999999999) {loanId = "200" + count;}
        else if (count == Long.parseLong("10000000000") && count <=Long.parseLong("99999999999")) {loanId = "20" + count;}
        else {loanId = String.valueOf(count);}
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

    public static void setPaymentMethods(ComboBox<PaymentMethods> comboBox) {
        comboBox.getItems().add(PaymentMethods.eCASH);
        comboBox.getItems().add(PaymentMethods.CASH);
        comboBox.getItems().add(PaymentMethods.BOTH_METHODS);
    }

    public static void setPaymentGateways(ComboBox<PaymentMethods> comboBox) {
        comboBox.getItems().add(PaymentMethods.BANK_TRANSFER);
        comboBox.getItems().add(PaymentMethods.MOMO);
        comboBox.getItems().add(PaymentMethods.AIRTELTIGO);
        comboBox.getItems().add(PaymentMethods.VODA_CASH);
    }


}//end of clas
