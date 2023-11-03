package app.controllers.finance;

import app.models.finance.FinanceModel;
import app.models.loans.LoansModel;
import app.repositories.loans.LoanScheduleEntity;
import app.repositories.loans.PendingLoanApprovalEntity;
import app.specialmethods.SpecialMethods;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaymentApprovalController extends FinanceModel implements Initializable {


    LoansModel LOAN_MODEL = new LoansModel();
    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /*******************************************************************************************************************
     *********************************************** FXML NODE EJECTIONS
     *******************************************************************************************************************/
    @FXML private Label loanCounterLabel;
    @FXML private ListView<String> listView;
    @FXML private Pane qualificationPane;
    @FXML private TextField loanAmountField;
    @FXML private ComboBox<Integer> interestRateSelector, loanPeriodSelector, processingRateSelector;
    @FXML private DatePicker datePicker;
    @FXML private TableView<LoanScheduleEntity> scheduleTable;
    @FXML private TableColumn<Integer, Integer> noColumn;
    @FXML private TableColumn<LoanScheduleEntity, Integer> installmentColumn;
    @FXML private TableColumn<LoanScheduleEntity, Integer> principalColumn;
    @FXML private TableColumn<LoanScheduleEntity, Integer> interestColumn;
    @FXML private TableColumn<LoanScheduleEntity, Integer> paymentDateColumn;
    @FXML private TableColumn<LoanScheduleEntity, Integer> balanceColumn;
    @FXML private Hyperlink exportLink;
    @FXML private MFXButton generateScheduleButton, saveButton, rejectButton;
    @FXML private Label displayTotalLoanAmount, displayInterestAmount, displayMonthlyInstallmentAmount,
            displayProcessingAmount, displayStartDate, displayEndDate;


    /*******************************************************************************************************************
     *********************************************** IMPLEMENTATION OF OTHER METHODS
     ********************************************************************************************************************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loanCounterLabel.setText("( ".concat(String.valueOf(getPendingApprovalLoansCount())).concat(" )"));
        SpecialMethods.setLoanPeriod(loanPeriodSelector);
        SpecialMethods.setRateValue(interestRateSelector);
        SpecialMethods.setRateValue(processingRateSelector);
        accountNumberSelected();
        loadListView();
    }

    private void loadListView() {
        listView.getItems().clear();
        for (PendingLoanApprovalEntity item : LOAN_MODEL.getLoansUnderPendingApproval()) {
            listView.getItems().add(item.getLoan_no());
        }
    }
    
    void populateScheduleTable() {

    }

    void calculateLoanValues() {
        double loanAmount = Double.parseDouble(loanAmountField.getText());
        int interestRate = interestRateSelector.getValue();
        int processingRate = processingRateSelector.getValue();
        int loanPeriod = loanPeriodSelector.getValue();

        double constant =(loanAmount / 100);
        double interestAmount = (constant * interestRate) * loanPeriod;
        double processingAmount =(constant * processingRate );
        double totalAmount = loanAmount + interestAmount;
        double installment = totalAmount / loanPeriod;

        displayTotalLoanAmount.setText(String.valueOf(totalAmount));
        displayInterestAmount.setText(String.valueOf(interestAmount));
        displayProcessingAmount.setText(String.valueOf(processingAmount));
        displayMonthlyInstallmentAmount.setText(String.valueOf(decimalFormat.format(installment)));

        displayStartDate.setText(String.valueOf(datePicker.getValue()));
        displayEndDate.setText(String.valueOf(datePicker.getValue().plusMonths(loanPeriod)));
    }



    /*******************************************************************************************************************
     *********************************************** TRUE OR FALSE STATEMENTS
     ********************************************************************************************************************/





    /*******************************************************************************************************************
     *********************************************** INPUT FIELDS VALIDATION METHOD
     ********************************************************************************************************************/
@FXML void validateLoanAmountInput(KeyEvent keyEvent) {

    if (!(keyEvent.getCharacter().matches("[0-9]") || keyEvent.getCharacter().contains("."))) {
        loanAmountField.deletePreviousChar();
    }
    try {
        calculateLoanValues();
    }catch (NumberFormatException ignore){
        if ("".equals(loanAmountField.getText())) {
            displayTotalLoanAmount.setText("0.0");
        }
    };
}


    /*******************************************************************************************************************
     *********************************************** ACTION EVENT METHODS IMPLEMENTATION
     ********************************************************************************************************************/

    public void exportButtonClicked(ActionEvent actionEvent) {
    }

    public void calculateLoanValues(MouseEvent mouseEvent) {
    }

    void accountNumberSelected() {
        listView.setOnMouseClicked(event -> {
            try{
                String selectedValue = listView.getSelectionModel().getSelectedItem();
                qualificationPane.setDisable(selectedValue.isEmpty());

                //check if selection is not empty if false then get associated values pertaining to the selected loan no.
                if (!selectedValue.isEmpty()) {
                    for (PendingLoanApprovalEntity items : LOAN_MODEL.getLoansUnderPendingApproval()){
                        if (Objects.equals(selectedValue, items.getLoan_no())){
                            loanAmountField.setText(String.valueOf(items.getLoan_amount()));
                            interestRateSelector.setValue(items.getInterest_rate());
                            loanPeriodSelector.setValue(items.getLoan_period());
                            processingRateSelector.setValue(items.getProcessing_rate());
                            datePicker.setValue(items.getStart_date());
                        }
                    }
                    calculateLoanValues();
                }

            }catch (NullPointerException ignore){}

        });
    }



}//end of class...
