package app.repositories.business;

import java.sql.Timestamp;
import java.time.LocalTime;

public class DomesticTransactionLogsEntity {
//    id, transfer_type, transferred_to, amount, entered_by, entry_date, time
    private int id;
    private  String transferTypes;
    private String transferTO;
    private double amount, cashAmount, eCashAmount;
    private int enteredBy;
    private Timestamp entryDate;
    private LocalTime time;

    public DomesticTransactionLogsEntity() {
    }
    public DomesticTransactionLogsEntity(int id, String transferTypes, String transferTO, double amount, int enteredBy, Timestamp entryDate, LocalTime time)
    {
        this.id = id;
        this.transferTypes = transferTypes;
        this.transferTO = transferTO;
        this.amount = amount;
        this.enteredBy = enteredBy;
        this.entryDate = entryDate;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransferTypes() {
        return transferTypes;
    }

    public void setTransferTypes(String transferTypes) {
        this.transferTypes = transferTypes;
    }

    public String getTransferTO() {
        return transferTO;
    }

    public void setTransferTO(String transferTO) {
        this.transferTO = transferTO;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(int enteredBy) {
        this.enteredBy = enteredBy;
    }

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getECashAmount() {
        return eCashAmount;
    }

    public void setEcashAmount(double eCashAmount) {
        this.eCashAmount = eCashAmount;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}//end of class...
