package app.controllers.loans.application;

import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LoanForm2 implements Initializable {


    /*******************************************************************************************************************
     *********************************************** EJECTION OF FXML NODES
     ******************************************************************************************************************/
    @FXML private TextField loanAmountField;
    @FXML private ComboBox<Integer> loanPeriodSelector;
    @FXML private ComboBox<Integer> interestRateSelector;
    @FXML private ComboBox<Integer> processingRateSelector;
    @FXML private TextArea loanReasonField;
    @FXML private MFXDatePicker startDatePicker;
    @FXML private Label displayLoanAmount, displayInterestAmount, displayProcessingAmount, displayMonthlyInstallmentAmount;
    @FXML private Label displayTotalLoanAmount, displayStartDate, displayEndDate, displayLoanStatus;



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ******************************************************************************************************************/
    boolean isLoanAmountFieldEmpty() {
        return loanAmountField.getText().isBlank() || loanAmountField.getText().isEmpty();
    }
    boolean isInterestRateEmpty() {
        return interestRateSelector.getValue() == null;
    }
    boolean isProcessingRateEmpty() {return processingRateSelector.getValue() == null;}
    boolean isLoanPeriodSelectorEmpty() {return loanPeriodSelector.getValue() == null;}
    boolean isStartDateEmpty() {return startDatePicker.getValue() == null;}
    boolean isLoanReasonEmpty() {return loanReasonField.getText().isBlank();}



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF INPUT FIELDS VALIDATION
     ******************************************************************************************************************/



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpecialMethods.setLoanPeriod(loanPeriodSelector);
        SpecialMethods.setInterestRate(interestRateSelector);
        SpecialMethods.setInterestRate(processingRateSelector);
        actionEventsMethodsImplementation();
    }

    double computeInterestAmount() {
        double principal = Double.parseDouble(loanAmountField.getText());
        int rate = Integer.parseInt(interestRateSelector.getValue().toString());
        return (principal * rate) / 100;
    }
    double computeProcessingAmount() {
        double principal = Double.parseDouble(loanAmountField.getText());
        int rate = Integer.parseInt(processingRateSelector.getValue().toString());
        return (principal * rate) / 100;
    }
    double computeMonthlyInstallment() {
        int monthValue = Integer.parseInt(loanPeriodSelector.getValue().toString());
        return computeTotalLoanAmount() / monthValue;
    }
    double computeTotalLoanAmount() {
        double loanAmount = Double.parseDouble(displayLoanAmount.getText());
        double interestAmount = Double.parseDouble(displayInterestAmount.getText());
        double processingRate = Double.parseDouble(displayProcessingAmount.getText());
        return Math.abs(loanAmount + interestAmount + processingRate);
    }



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF ACTION EVENT METHODS
     ******************************************************************************************************************/
    void actionEventsMethodsImplementation() {
        loanAmountField.setOnKeyReleased(event -> {
            interestRateSelector.setDisable(isLoanAmountFieldEmpty());
            loanPeriodSelector.setDisable(isLoanAmountFieldEmpty());
            processingRateSelector.setDisable(isLoanAmountFieldEmpty());
            if (isLoanAmountFieldEmpty()){displayLoanAmount.setText("0.00");}
            if (!(event.getCode().isDigitKey() || event.getCode().isArrowKey() || event.getCode().equals(KeyCode.PERIOD)) || event.getCode().equals(KeyCode.BACK_SPACE)) {
                loanAmountField.deleteNextChar();
                loanAmountField.deletePreviousChar();
            } else {
                float value = Float.parseFloat(loanAmountField.getText());
                displayLoanAmount.setText(String.valueOf(Math.abs(value)));
            }
        });//end of Action event

        interestRateSelector.setOnAction(event -> {
            displayInterestAmount.setText(String.valueOf(computeInterestAmount()));
        });//end of Action event

        processingRateSelector.setOnAction(event -> {
            displayProcessingAmount.setText(String.valueOf(computeProcessingAmount()));
            displayTotalLoanAmount.setText(String.valueOf(computeTotalLoanAmount()));
        });//end of action event.

        loanPeriodSelector.setOnAction(event -> {
            displayMonthlyInstallmentAmount.setText(String.valueOf(computeMonthlyInstallment()).substring(0,5));
        }); //end of action event.

        startDatePicker.setOnAction(event -> {
            byte monthValue = Byte.parseByte(loanPeriodSelector.getValue().toString());
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = startDate.plusMonths(monthValue);
            displayStartDate.setText(String.valueOf(startDate));
            displayEndDate.setText(String.valueOf(endDate));

//            for (int x= 1; x<=monthValue; x++) {
//                System.out.println("Month " + x + " " + startDate.plusMonths(x) + " monthly pay at " + displayMonthlyInstallmentAmount.getText());
//            }
        });


    }



}
