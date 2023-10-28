package app.controllers.loans.application;

import app.alerts.UserAlerts;
import app.alerts.UserNotification;
import app.controllers.homepage.AppController;
import app.models.loans.LoansModel;
import app.repositories.BusinessInfoEntity;
import app.repositories.loans.LoansTableEntity;
import app.repositories.loans.ScheduleEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoanCalculatorController extends LoansModel implements Initializable {
    UserNotification NOTIFY = new UserNotification();
    UserAlerts ALERTS;


    /*******************************************************************************************************************
     *********************************************** EJECTION OF FXML NODES
     ******************************************************************************************************************/

    @FXML private MFXFilterComboBox<String> applicationNumberSelector;
    @FXML private TextField basicSalaryField, statutoryField, totalDeductionField,finalAmountField;
    @FXML private Label remainingBalanceField;
    @FXML private  Label displayLoanAmount,displayInterestAmount, displayProcessingAmount, displayTotalLoanAmount;
    @FXML private Label applicantFullnameLabel, loanTypeLabel, applicationDateLabel;
    @FXML private Label displayMonthlyInstallmentAmount, displayStartDate, displayEndDate, displayLoanStatus;
    @FXML private TextField loanAmountField;
    @FXML private ComboBox<Integer> loanPeriodSelector, interestRateSelector, processingRateSelector;
    @FXML private Label supervisorLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextArea loanReasonField;
    @FXML private Hyperlink exportLink;
    @FXML private MFXButton generateScheduleButton, saveButton;
    @FXML private TableView<ScheduleEntity> scheduleTable;
    @FXML private TableColumn<ScheduleEntity, Integer> noColumn;
    @FXML private TableColumn<ScheduleEntity, Double> principalColumn, interestColumn, installmentColumn, balanceColumn;
    @FXML private TableColumn<ScheduleEntity, LocalDate> paymentDateColumn;
    @FXML private Pane calculatorPane, qualificationPane;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    int loggedInUserId = getUserIdByName(AppController.activeUserPlaceHolder);


    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ******************************************************************************************************************/
    boolean isLoanAmountEmpty() {return loanAmountField.getText().isEmpty();}
    boolean isInterestRateEmpty(){return interestRateSelector.getValue() == null;}
    boolean isProcessingRateEmpty() {return processingRateSelector.getValue() == null;}
    boolean isLoanPeriodEmpty() {return loanPeriodSelector.getValue() == null;}
    boolean isStartDateEmpty() {return datePicker.getValue() == null;}
    boolean isReasonFieldEmpty() {return loanReasonField.getText().isEmpty();}
    boolean isQualificationAmountEmpty() {return finalAmountField.getText().isEmpty();}
    boolean isApplicationNoSelectorEmpty() {return applicationNumberSelector.getValue() == null;}



    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF INPUT FIELDS VALIDATION
     ******************************************************************************************************************/

    @FXML void calculateLoanValues() {
        if (!isLoanAmountEmpty()) {}
        if (!isInterestRateEmpty()) {
            double loanAmount = Double.parseDouble(loanAmountField.getText());
            int interest = interestRateSelector.getValue();
            double interestAmount = (loanAmount * interest) / 100;
            displayInterestAmount.setText(String.valueOf(decimalFormat.format(interestAmount)));
        }
        if (!isProcessingRateEmpty()) {
            double loanAmount = Double.parseDouble(loanAmountField.getText());
            int interest = processingRateSelector.getValue();
            double interestAmount = (loanAmount * interest) / 100;
            displayProcessingAmount.setText(String.valueOf(decimalFormat.format(interestAmount)));
        }

        if (!isLoanPeriodEmpty()) {
            double loanAmount = Double.parseDouble(loanAmountField.getText());
            int interest = interestRateSelector.getValue();
            int tenure = loanPeriodSelector.getValue();
            Object[] values = {loanAmount, interest, tenure};
            for (Object value : values) {
                if (!value.toString().isEmpty()) {
                    double interestAmount = (loanAmount * interest) / 100;
                    double totalInterestAmount =  tenure * interestAmount;
                    double finalResult = totalInterestAmount + loanAmount;
                    displayInterestAmount.setText(String.valueOf(decimalFormat.format(totalInterestAmount)));
                    displayTotalLoanAmount.setText(String.valueOf(decimalFormat.format(finalResult)));
                }
            }
        }
        if (!isStartDateEmpty()) {
            LocalDate startDate = datePicker.getValue();
            displayStartDate.setText(String.valueOf(startDate));
            int tenure = loanPeriodSelector.getValue();
            LocalDate endDate = startDate.plusMonths(tenure);
            displayEndDate.setText(String.valueOf(endDate));

            double totalAmount = Double.parseDouble(displayTotalLoanAmount.getText());
            double result = totalAmount / tenure;
            displayMonthlyInstallmentAmount.setText(String.valueOf(decimalFormat.format(result)));

            double monthlyInstallment = Double.parseDouble(displayMonthlyInstallmentAmount.getText());
            double qualificationAmount = Double.parseDouble(finalAmountField.getText());
            if (monthlyInstallment > qualificationAmount) {
                displayLoanStatus.setText("NOT QUALIFIED");
                displayLoanStatus.setStyle("-fx-background-color: #ffdfdf; -fx-text-fill:red");
            } else {
                displayLoanStatus.setText("QUALIFIED");
                displayLoanStatus.setStyle("-fx-background-color: #c1ffb7; -fx-text-fill:#0b8425");
            }
        }
    }


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ******************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actionEventsMethodsImplementation();inputFieldChaneListerImplementation();
        SpecialMethods.setInterestRate(interestRateSelector);
        SpecialMethods.setLoanPeriod(loanPeriodSelector);
        SpecialMethods.setInterestRate(processingRateSelector);
        setComboBoxVariables();
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

        int loanTenure = loanPeriodSelector.getValue(); // 6
        double loanAmount = Double.parseDouble(displayLoanAmount.getText()); // 100.00
        double interestAmount = (loanAmount * interestRateSelector.getValue()) / 100; // (100*10) / 100
        double principal = loanAmount / loanTenure; // 100 / 6
        double  monthlyInstallment = Double.parseDouble(displayMonthlyInstallmentAmount.getText());
        LocalDate scheduleDate = datePicker.getValue();
        double totalLoanAmount = Double.parseDouble(displayTotalLoanAmount.getText());

        for (int x=1; x <= loanTenure; x++) {
            totalLoanAmount = totalLoanAmount - monthlyInstallment;

            double formattedPrincipal = Double.parseDouble(decimalFormat.format(principal));
            double formattedLoanDifference = Double.parseDouble(decimalFormat.format(totalLoanAmount));

            //check for a negative value else just allow the value
            double maxValue = Math.max(formattedLoanDifference, 0.0);

            entity = new ScheduleEntity(x, formattedPrincipal, interestAmount, monthlyInstallment, maxValue, scheduleDate.plusMonths(x));
            scheduleTable.getItems().add(entity);
        }
    }
    private void setComboBoxVariables() {
        String workerId = getWorkIdByUserId(loggedInUserId);
        applicationNumberSelector.setItems(getGroupSupervisors(workerId));
    }


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF ACTION EVENT METHODS
     ******************************************************************************************************************/
    void actionEventsMethodsImplementation() {
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
                            displayInterestAmount.setText(String.valueOf(decimalFormat.format(totalInterestAmount)));
                            displayTotalLoanAmount.setText(String.valueOf(decimalFormat.format(finalResult)));
                        }
                    }
                });
                interestRateSelector.setOnAction(action -> {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    int interest = interestRateSelector.getValue();
                    double interestAmount = (loanAmount * interest) / 100;
                    displayInterestAmount.setText(String.valueOf(decimalFormat.format(interestAmount)));
                });

                processingRateSelector.setOnAction(event -> {
                    double loanAmount = Double.parseDouble(loanAmountField.getText());
                    int interest = processingRateSelector.getValue();
                    double interestAmount = (loanAmount * interest) / 100;
                    displayProcessingAmount.setText(String.valueOf(decimalFormat.format(interestAmount)));
                });

                datePicker.setOnAction(click -> {
                    if (isQualificationAmountEmpty()) {
                        NOTIFY.informationNotification("AMOUNT FIELD EMPTY", "Please set the qualification values to check for LOAN QUALIFICATION");
                    }else {
                        LocalDate startDate = datePicker.getValue();
                        displayStartDate.setText(String.valueOf(startDate));
                        int tenure = loanPeriodSelector.getValue();
                        LocalDate endDate = startDate.plusMonths(tenure);
                        displayEndDate.setText(String.valueOf(endDate));

                        double totalAmount = Double.parseDouble(displayTotalLoanAmount.getText());
                        double result = totalAmount / tenure;
                        displayMonthlyInstallmentAmount.setText(String.valueOf(decimalFormat.format(result)));

                        double monthlyInstallment = Double.parseDouble(displayMonthlyInstallmentAmount.getText());
                        double qualificationAmount = Double.parseDouble(finalAmountField.getText());
                        if (monthlyInstallment > qualificationAmount) {
                            displayLoanStatus.setText("NOT QUALIFIED");
                            displayLoanStatus.setStyle("-fx-background-color: #ffdfdf; -fx-text-fill:red");
                        } else {
                            displayLoanStatus.setText("QUALIFIED");
                            displayLoanStatus.setStyle("-fx-background-color: #c1ffb7; -fx-text-fill:#0b8425");
                        }
                    }
                });

                calculatorPane.setOnMouseMoved(event -> {
                    generateScheduleButton.setDisable(isLoanPeriodEmpty() || isLoanAmountEmpty() || isInterestRateEmpty() || isStartDateEmpty() || isReasonFieldEmpty());
                });

                generateScheduleButton.setOnAction(action -> {
                    generateScheduleSheet();
                    saveButton.setDisable(false);
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
            double percentageValue = 0.0;
            for (BusinessInfoEntity item : getBusinessInfo()) {
                percentageValue = (item.getLoanPercentage() / 100);

            }
            if (!keyEvent.getCharacter().matches("[0-9]")) {
                statutoryField.deletePreviousChar();
            }
            try {
                double basicSalary = Double.parseDouble(basicSalaryField.getText());
                double statutory = Double.parseDouble(statutoryField.getText());
                double result = (basicSalary - statutory) * percentageValue;
                remainingBalanceField.setText(String.valueOf(decimalFormat.format(result)));
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
    @FXML void getApplicantInformation() {
        calculatorPane.setDisable(isApplicationNoSelectorEmpty());
        qualificationPane.setDisable(isApplicationNoSelectorEmpty());
        String loanNo = applicationNumberSelector.getValue();
        for (LoansTableEntity data : getLoansUnderProcessingStage()) {
            if (Objects.equals(loanNo, data.getLoanNo())) {
                applicantFullnameLabel.setText(data.getFullName());
                loanTypeLabel.setText(data.getLoanType());
                applicationDateLabel.setText(data.getFormattedDate());
                loanAmountField.setText(data.getRequestedAmount().toString());
                displayLoanAmount.setText(data.getRequestedAmount().toString());
                loanReasonField.setText(data.getLoanPurpose());
            }
        }
    }
    @FXML public void generateScheduleButtonClicked() {

    }
    @FXML void saveButtonClicked() {
        String statusLabel = displayLoanStatus.getText();
        boolean result = statusLabel.equals("QUALIFIED");
        if (!result) {
            ALERTS = new UserAlerts("NOT QUALIFIED", "Sorry, applicant does not qualify for this loan facility, please reduce loan request amount",
                    "you need to reduce applicant loan amount to qualify else terminate loan process.");
            ALERTS.errorAlert();
        }else {

//            NOTIFY.successNotification("QUALIFIED", "Congratulations, applicant qualifies for loan facility.");
        }
    }


}//end of class
