package app.repositories.business;

import app.repositories.users.UsersData;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Locale;

public class BusinessTransactionLogs {
    private int id;
    private String transaction_type;
    private String bank_name;
    double amount;
    private String transaction_id;
    private String username;
    private String account_number;
    private Date transaction_date;
    String notes;
    String formattedAmount;
    int created_by;
    Timestamp date_created;

    public BusinessTransactionLogs() {
        formatCurrency();
    }

    public BusinessTransactionLogs(int id, String transaction_type, String bank_name, double amount, String transaction_id, String account_number, String username, Date transaction_date, String notes, Timestamp date_created) {
        this.id = id;
        this.transaction_type = transaction_type;
        this.bank_name = bank_name;
        this.amount = amount;
        this.transaction_id = transaction_id;
        this.account_number = account_number;
        this.transaction_date = transaction_date;
        this.username = username;
        this.notes = notes;
        this.date_created = date_created;
        formatCurrency();
    }

    private void formatCurrency() {
       formattedAmount = NumberFormat.getInstance(Locale.US).format(amount);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getCreated_by() {
        return created_by;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getFormattedAmount() {
        return formattedAmount;
    }

    public void setFormattedAmount(String formattedAmount) {
        this.formattedAmount = formattedAmount;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }
}//end of class...
