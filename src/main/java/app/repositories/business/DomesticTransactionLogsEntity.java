package app.repositories.business;

import java.math.BigInteger;
import java.sql.Timestamp;

public class DomesticTransactionLogsEntity {
//    id, transfer_type, transferred_to, amount, entered_by, entry_date
    private BigInteger id;
    private  String transferTypes;
    private String transferTO;
    private double amount;
    private int enteredBy;
    private Timestamp entryDate;

    public DomesticTransactionLogsEntity() {
    }
    public DomesticTransactionLogsEntity(BigInteger id, String transferTypes, String transferTO, double amount, int enteredBy, Timestamp entryDate) {
        this.id = id;
        this.transferTypes = transferTypes;
        this.transferTO = transferTO;
        this.amount = amount;
        this.enteredBy = enteredBy;
        this.entryDate = entryDate;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }
}//end of class...
