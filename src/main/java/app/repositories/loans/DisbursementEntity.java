package app.repositories.loans;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class DisbursementEntity {

    private int id, processing_rate;
    private String loanNumber;
    private double loanAmount;
    private String accountNumber;
    private String applicationStatus;
    private double accountBalance, previousBalance;
    private Label status = new Label();
    private CheckBox payBtn = new CheckBox();
    private ComboBox<String> method = new ComboBox<>();
    private TextField transIdField = new TextField();
    private String mobileNumber;
    private double processingFee, loanAmountValue;

    public DisbursementEntity() {
    }

    public DisbursementEntity(int id, String loanNumber, String accountNumber, double loanAmount,
                              int processing_rate, double accountBalance,
                              double previousBalance, String appStatus, String mobileNumber) {
        this.id = id;
        this.loanNumber = loanNumber;
        this.accountNumber = accountNumber;
        this.loanAmount = loanAmount;
        applicationStatus = appStatus;
        this.processing_rate = processing_rate;
        this.accountBalance = accountBalance;
        this.previousBalance = previousBalance;
        this.mobileNumber = mobileNumber;
//        comboBox();
        styleStatus();
        computeRate();
//        transactionTypeChangeListener();
    }

    private void comboBox() {
        method.getItems().add("CASH");
        method.getItems().add("eCASH");
        method.setStyle("-fx-pref-width:120px; -fx-font-family:roboto: -fx-font-weight:bold; -fx-background-color:#eee");
    }

    private void styleStatus(){
        boolean value = applicationStatus.equals("pending_payment");
        payBtn.setStyle("-fx-font-family:roboto; -fx-font-size:12px; -fx-font-weight:bold;");
        if (value) {
            status.setText("Unpaid");
            status.setStyle("-fx-text-fill:#fff; -fx-background-color:#ff3939;-fx-alignment:center; " +
                    "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:70px; -fx-font-size:12px; -fx-font-family:roboto");
        }
    }

    private void computeRate() {
        processingFee = (loanAmount / 100) * processing_rate;
        loanAmountValue = loanAmount - processingFee;
    }

    private void transactionTypeChangeListener() {
        transIdField.setDisable(true);
        method.setOnAction(actionEvent -> {
            if (method.getValue().equals("CASH")) {
                transIdField.clear();
                transIdField.setDisable(true);
            }else {
                transIdField.setDisable(false);
            }
        });
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public ComboBox<String> getMethod() {
        return method;
    }

    public void setMethod(ComboBox<String> method) {
        this.method = method;
    }

    public Label getStatus() {
        return status;
    }

    public void setStatus(Label status) {
        this.status = status;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CheckBox getPayBtn() {
        return payBtn;
    }

    public TextField getTransIdField() {
        return transIdField;
    }

    public void setTransIdField(TextField transIdField) {
        this.transIdField = transIdField;
    }

    public void setPayBtn(CheckBox payBtn) {
        this.payBtn = payBtn;
    }
    public double getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(double processingFee) {
        this.processingFee = processingFee;
    }

    public double getLoanAmountValue() {
        return loanAmountValue;
    }

    public void setLoanAmountValue(double loanAmountValue) {
        this.loanAmountValue = loanAmountValue;
    }

}//end of class...
