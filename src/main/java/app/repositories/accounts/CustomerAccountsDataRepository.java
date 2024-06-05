package app.repositories.accounts;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CustomerAccountsDataRepository {
    private long account_id;
    private long customer_id;
    private String account_number, account_type;
    private double account_balance;
    private double previous_balance;
    private String account_status;
    private Timestamp date_modified;
    private String formatted_date_modified;
    private String loanNo;
    private int modified_by;


    public CustomerAccountsDataRepository() {}

    public CustomerAccountsDataRepository(long customer_id, String account_type, String account_number, int modified_by){
        this.customer_id = customer_id;
        this.account_type = account_type;
        this.account_number = account_number;
        this.modified_by = modified_by;
    }

    public CustomerAccountsDataRepository(long account_id, int customer_id,  String account_number, String loanNo, String account_type, String account_status, double account_balance, double previous_balance, Timestamp date_modified, int modified_by) {
        this.account_id = account_id;
        this.customer_id = customer_id;
        this.account_number = account_number;
        this.loanNo = loanNo;
        this.account_type = account_type;
        this.account_status = account_status;
        this.account_balance = account_balance;
        this.previous_balance = previous_balance;
        this.date_modified = date_modified;
        this.modified_by = modified_by;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);;
        this.formatted_date_modified = date_modified.toLocalDateTime().format(dateTimeFormatter);
    }

    public long getAccount_id() {
        return account_id;
    }

    private void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(double account_balance) {
        this.account_balance = account_balance;
    }

    public double getPrevious_balance() {
        return previous_balance;
    }

    public void setPrevious_balance(double previous_balance) {
        this.previous_balance = previous_balance;
    }

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public String getFormatted_date_modified() {
        return formatted_date_modified;
    }

    public void setFormatted_date_modified(String formatted_date_modified) {
        this.formatted_date_modified = formatted_date_modified;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }
}
