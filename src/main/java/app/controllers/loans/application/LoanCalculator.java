package app.controllers.loans.application;

import app.models.MainModel;
import app.repositories.loans.ScheduleEntity;
import app.repositories.users.UsersData;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LoanCalculator extends MainModel implements Initializable {


    /*******************************************************************************************************************
     *********************************************** EJECTION OF FXML NODES
     ******************************************************************************************************************/
    @FXML private TextField basicSalaryField, statutoryField, totalDeductionField,finalAmountField;
    @FXML private Label remainingBalanceField;
    @FXML private  Label displayLoanAmount,displayInterestAmount, displayProcessingAmount, displayTotalLoanAmount;
    @FXML private Label displayMonthlyInstallmentAmount, displayStartDate, displayEndDate, displayLoanStatus;
    @FXML private TextField loanAmountField;
    @FXML private ComboBox<Integer> loanPeriodSelector, interestRateSelector, processingRateSelector;
    @FXML private ComboBox<String> supervisorSelector;
    @FXML private DatePicker datePicker;
    @FXML private TextArea loanReasonField;
    @FXML private Hyperlink exportLink;
    @FXML private MFXButton generateScheduleButton, saveButton;
    @FXML private TableView<ScheduleEntity> scheduleTable;
    @FXML private TableColumn<ScheduleEntity, Integer> noColumn;
    @FXML private TableColumn<ScheduleEntity, Double> principalColumn, interestColumn, installmentColumn, balanceColumn;
    @FXML private TableColumn<ScheduleEntity, LocalDate> paymentDateColumn;
    @FXML private Pane calculatorPane;



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ******************************************************************************************************************/
    boolean isLoanAmountEmpty() {return loanAmountField.getText().isEmpty();}
    boolean isInterestRateEmpty(){return interestRateSelector.getValue() == null;}
    boolean isLoanPeriodEmpty() {return loanPeriodSelector.getValue() == null;}
    boolean isStartDateEmpty() {return datePicker.getValue() == null;}
    boolean isReasonFieldEmpty() {return loanReasonField.getText().isEmpty();}



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF INPUT FIELDS VALIDATION
     ******************************************************************************************************************/





    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionEventsMethodsImplementation();inputFieldChaneListerImplementation();
        SpecialMethods.setInterestRate(interestRateSelector);
        SpecialMethods.setLoanPeriod(loanPeriodSelector);
        SpecialMethods.setInterestRate(processingRateSelector);
        for (UsersData users : fetchAssignedUsersOnly() ) {
            supervisorSelector.getItems().add(users.getUsername());
        }
    }

    private void populateTable() {
        noColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<>("principal"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestAmount"));
        installmentColumn.setCellValueFactory(new PropertyValueFactory<>("monthlyInstallment"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedScheduleDate"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        scheduleTable.getItems().clear();
    }


    private void generateScheduleSheet() {
        populateTable();

        ScheduleEntity entity;
        DecimalFormat df = new DecimalFormat("#.##");

        int loanTenure = loanPeriodSelector.getValue(); // 6
        double loanAmount = Double.parseDouble(displayLoanAmount.getText()); // 100.00
        double interestAmount = (loanAmount * interestRateSelector.getValue()) / 100; // (100*10) / 100
        double principal = loanAmount / loanTenure; // 100 / 6
        double  monthlyInstallment = Double.parseDouble(displayMonthlyInstallmentAmount.getText());
        LocalDate scheduleDate = datePicker.getValue();
        double totalLoanAmount = Double.parseDouble(displayTotalLoanAmount.getText());

        for (int x=1; x <= loanTenure; x++) {
            totalLoanAmount = totalLoanAmount - monthlyInstallment;

            double formattedPrincipal = Double.parseDouble(df.format(principal));
            double formattedLoanDifference = Double.parseDouble(df.format(totalLoanAmount));

            //check for a negative value else just allow the value
            double maxValue = Math.max(formattedLoanDifference, 0.0);

            entity = new ScheduleEntity(x, formattedPrincipal, interestAmount, monthlyInstallment, maxValue, scheduleDate.plusMonths(x));
            scheduleTable.getItems().add(entity);
        }

    }


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF ACTION EVENT METHODS
     ******************************************************************************************************************/
    void actionEventsMethodsImplementation() {
                DecimalFormat df = new DecimalFormat("#.##");
                loanPeriodSelector.setOnAction(action -> {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    int interest = interestRateSelector.getValue();
                    int tenure = loanPeriodSelector.getValue();
                    Object[] values = {loanAmount, interest, tenure};
                    for (Object value : values) {
                        if (!value.toString().isEmpty()) {
                            double interestAmount = (loanAmount * interest) / 100;
                            double totalInterestAmount =  tenure * interestAmount;
                            double finalResult = totalInterestAmount + loanAmount;
                            displayInterestAmount.setText(String.valueOf(df.format(totalInterestAmount)));
                            displayTotalLoanAmount.setText(String.valueOf(df.format(finalResult)));
                        }
                    }
                });

                interestRateSelector.setOnAction(action -> {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    int interest = interestRateSelector.getValue();
                    double interestAmount = (loanAmount * interest) / 100;
                    displayInterestAmount.setText(String.valueOf(df.format(interestAmount)));
                });

                processingRateSelector.setOnAction(event -> {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    int interest = processingRateSelector.getValue();
                    double interestAmount = (loanAmount * interest) / 100;
                    displayProcessingAmount.setText(String.valueOf(df.format(interestAmount)));
                });

                datePicker.setOnAction(click -> {
                    LocalDate startDate = datePicker.getValue();
                    displayStartDate.setText(String.valueOf(startDate));
                    int tenure = loanPeriodSelector.getValue();
                    LocalDate endDate = startDate.plusMonths(tenure);
                    displayEndDate.setText(String.valueOf(endDate));

                    double totalAmount = Double.parseDouble(displayTotalLoanAmount.getText());
                    double result = totalAmount / tenure;
                    displayMonthlyInstallmentAmount.setText(String.valueOf(df.format(result)));

                });

                calculatorPane.setOnMouseMoved(event -> {
                    generateScheduleButton.setDisable(isLoanPeriodEmpty() || isLoanAmountEmpty() || isInterestRateEmpty() || isStartDateEmpty() || isReasonFieldEmpty());
                });

                generateScheduleButton.setOnAction(action -> {
                    generateScheduleSheet();
                });


        };

    void inputFieldChaneListerImplementation() {
        basicSalaryField.setOnKeyTyped(keyPress -> {
            if (!keyPress.getCharacter().matches("[0-9]")) {
                basicSalaryField.deletePreviousChar();
            }
            try {
                double basicSalary = Double.parseDouble(basicSalaryField.getText());
                remainingBalanceField.setText(String.valueOf(basicSalary));
            }catch (NumberFormatException e) {
                remainingBalanceField.setText(String.valueOf(0.00));
            }
        });

        statutoryField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                statutoryField.deletePreviousChar();
            }
            try {
                double basicSalary = Double.parseDouble(basicSalaryField.getText());
                double statutory = Double.parseDouble(statutoryField.getText());
                double result = (basicSalary - statutory) * 0.5;
                remainingBalanceField.setText(String.valueOf(result));
            }catch (NumberFormatException e){
                remainingBalanceField.setText(basicSalaryField.getText());
            }
        });

        totalDeductionField.setOnKeyTyped(keyEvent -> {
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                totalDeductionField.deletePreviousChar();
            }
            try {
                Double balance = Double.parseDouble(remainingBalanceField.getText());
                double deduction = Double.parseDouble(totalDeductionField.getText());
                double result = balance - deduction;
                finalAmountField.setText(String.valueOf(result));
            }catch (NumberFormatException e) {
                finalAmountField.setText("0.00");
            }
        });

        loanAmountField.setOnKeyTyped(keyEvent -> {
            if (!(keyEvent.getCharacter().matches("[0-9]") || keyEvent.getCharacter().contains("."))) {
                loanAmountField.deletePreviousChar();
            }
            try {
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                displayLoanAmount.setText(String.valueOf(loanAmount));
            }catch (NumberFormatException ignore){
                if ("".equals(loanAmountField.getText())) {
                    displayLoanAmount.setText("0.0");
                }
            };
        });
    }//end of input event block

}//end of class
