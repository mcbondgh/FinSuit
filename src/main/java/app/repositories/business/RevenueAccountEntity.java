package app.repositories.business;

public class RevenueAccountEntity {
    private int id, entered_by;
    private String balance, reference_number, entry_type, amount, username, entry_date;
    private String expenditure_purpose, expenditure_type;

    public RevenueAccountEntity() {
    }

    public RevenueAccountEntity(int id, String balance, String reference_number, String entry_type, String amount,
                                String username, String entry_date) {
        this.id = id;
        this.balance = balance;
        this.reference_number = reference_number;
        this.entry_type = entry_type;
        this.amount = amount;
        this.username = username;
        this.entry_date = entry_date;
    }

    public RevenueAccountEntity(int id,  String amount, String entry_date, String expenditure_purpose, String expenditure_type) {
        this.id = id;
        this.amount = amount;
        this.entry_date = entry_date;
        this.expenditure_purpose = expenditure_purpose;
        this.expenditure_type = expenditure_type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getEntry_type() {
        return entry_type;
    }

    public void setEntry_type(String entry_type) {
        this.entry_type = entry_type;
    }

    public String getAmount() {
        return amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getEntered_by() {
        return entered_by;
    }

    public void setEntered_by(int entered_by) {
        this.entered_by = entered_by;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getExpenditure_purpose() {
        return expenditure_purpose;
    }

    public void setExpenditure_purpose(String expenditure_purpose) {
        this.expenditure_purpose = expenditure_purpose;
    }

    public String getExpenditure_type() {
        return expenditure_type;
    }

    public void setExpenditure_type(String expenditure_type) {
        this.expenditure_type = expenditure_type;
    }
}
