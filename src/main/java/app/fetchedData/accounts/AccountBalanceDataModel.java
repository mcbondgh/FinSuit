package app.fetchedData.accounts;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AccountBalanceDataModel {
    private long account_id;
    private long customer_id;

    private String account_number, account_type;
    private double current_balance;
    private double previous_balance;
    private Timestamp date_modified;
    private String formatted_date_modified;
    private int modified_by;

    public AccountBalanceDataModel() {
    }

    public AccountBalanceDataModel(long account_id, double current_balance, double previous_balance, Timestamp date_modified, int modified_by) {
        this.customer_id = account_id;
        this.current_balance = current_balance;
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

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public double getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(double current_balance) {
        this.current_balance = current_balance;
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

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }
}
