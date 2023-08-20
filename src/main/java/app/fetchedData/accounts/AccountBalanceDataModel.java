package app.fetchedData.accounts;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AccountBalanceDataModel {
    private long balance_id;
    private long account_id;
    private double current_balance;
    private double previous_balance;
    private Timestamp date_modified;
    private String formatted_date_modified;
    private int modified_by;


    public AccountBalanceDataModel(long balance_id, long account_id, double current_balance, double previous_balance, Timestamp date_modified, int modified_by) {
        this.balance_id = balance_id;
        this.account_id = account_id;
        this.current_balance = current_balance;
        this.previous_balance = previous_balance;
        this.date_modified = date_modified;
        this.modified_by = modified_by;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);;
        this.formatted_date_modified = date_modified.toLocalDateTime().format(dateTimeFormatter);
    }

    public long getBalance_id() {
        return balance_id;
    }

    public void setBalance_id(long balance_id) {
        this.balance_id = balance_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
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
