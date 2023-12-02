package app.repositories.transactions;

import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TransactionsEntity {
    private long id;
    String fullname;
    private String account_number;
    private String transaction_id, transaction_type, payment_method, payment_gateway;
    private double cash_amount, ecash_amount, total_amount;

    private String ecash_id;
    private Timestamp transaction_date;
    private String transaction_made_by, nationalIdNumber;
    private String username;
    private int userId;
    private String formattedDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
    private LocalTime localTime;

    private Label transactionStatus = new Label();
    public TransactionsEntity(long id, String fullname, String account_number, String transaction_id, String transaction_type, String payment_method, String payment_gateway, double total_amount, String ecash_id, Timestamp transaction_date, String transaction_made_by, String nationalIdNumber, String username) {
        this.id = id;
        this.fullname = fullname;
        this.account_number = account_number;
        this.transaction_id = transaction_id;
        this.transaction_type = transaction_type;
        this.payment_method = payment_method;
        this.payment_gateway = payment_gateway;
        this.total_amount = total_amount;
        this.ecash_id = ecash_id;
        this.transaction_date = transaction_date;
        this.transaction_made_by = transaction_made_by;
        this.nationalIdNumber = nationalIdNumber;
        this.username = username;
        labelFormatter();
        formatVariables();
    }
    public TransactionsEntity() {}

    public TransactionsEntity(String transaction_id, String transaction_type, double total_amount, LocalTime localTime) {
        this.transaction_id = transaction_id;
        this.transaction_type = transaction_type;
        this.total_amount = total_amount;
        this.localTime = localTime;
        labelFormatter();
    }
    private void labelFormatter() {
        transactionStatus.setText(transaction_type);
        switch(transaction_type) {
            case "CASH DEPOSIT" ->
                transactionStatus.setStyle("-fx-font-size:9px; -fx-font-family:roboto; -fx-font-weight:bold; -fx-padding:5px; -fx-background-color:#278c8f;" +
                        "-fx-text-fill:#fff; -fx-background-radius:5px; -fx-alignment:center");
            case "CASH WITHDRAWAL" ->
                    transactionStatus.setStyle("-fx-font-size:9px; -fx-font-family:roboto; -fx-font-weight:bold; -fx-padding:5px; -fx-background-color: orange;" +
                            "-fx-text-fill:#fff; -fx-background-radius:5px; -fx-alignment:center");
            case "LOAN PAYMENT" ->
                    transactionStatus.setStyle("-fx-font-size:9px; -fx-font-family:roboto; -fx-font-weight:bold; -fx-padding:5px; -fx-background-color: #117bdd;" +
                            "-fx-text-fill:#fff; -fx-background-radius:5px; -fx-alignment:center");
            case "DISBURSED FUND" ->
                    transactionStatus.setStyle("-fx-font-size:9px; -fx-font-family:roboto; -fx-font-weight:bold; -fx-padding:5px; -fx-background-color:#25aa04;" +
                            "-fx-text-fill:#fff; -fx-background-radius:5px; -fx-alignment:center");
        }

    }

    private void formatVariables() {
        localTime = transaction_date.toLocalDateTime().toLocalTime();
        formattedDate = transaction_date.toLocalDateTime().format(formatter);
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public Timestamp getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Timestamp transaction_date) {
        this.transaction_date = transaction_date;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_gateway() {
        return payment_gateway;
    }

    public void setPayment_gateway(String payment_gateway) {
        this.payment_gateway = payment_gateway;
    }

    public double getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(double cash_amount) {
        this.cash_amount = cash_amount;
    }

    public double getEcash_amount() {
        return ecash_amount;
    }

    public void setEcash_amount(double ecash_amount) {
        this.ecash_amount = ecash_amount;
    }

    public String getEcash_id() {
        return ecash_id;
    }

    public void setEcash_id(String ecash_id) {
        this.ecash_id = ecash_id;
    }

    public String getTransaction_made_by() {
        return transaction_made_by;
    }

    public void setTransaction_made_by(String transaction_made_by) {
        this.transaction_made_by = transaction_made_by;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Label getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Label transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}// end of class...
