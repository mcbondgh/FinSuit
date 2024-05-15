package app.repositories.business;

import java.math.BigInteger;
import java.sql.Timestamp;

public class ClosedTellerTransactionEntity {
//    id, start_amount, closed_amount, physical_cash, e_cash, overage_amount, shortage_amount, entry_date, closure_date, entered_by
    private long id;
    private double startAmount;
    private double closedAmount;
    private double physicalCash;
    private double eCash;
    private double overageAmount;
    private double shortageAmount;
    private Timestamp closureDate, entryDate;
    private int enteredBy;
    private int closedBy;
    private String notes;
    private byte isClosed;
    private byte isSuspended;

    public ClosedTellerTransactionEntity() {
    }

    public ClosedTellerTransactionEntity(long id, double startAmount, double closedAmount, double physicalCash,
                                         double eCash, double overageAmount, double shortageAmount, Timestamp closureDate,
                                         int enteredBy, int closedBy) {
        this.id = id;
        this.startAmount = startAmount;
        this.closedAmount = closedAmount;
        this.physicalCash = physicalCash;
        this.eCash = eCash;
        this.overageAmount = overageAmount;
        this.shortageAmount = shortageAmount;
        this.closureDate = closureDate;
        this.enteredBy = enteredBy;
        this.closedBy = closedBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(double startAmount) {
        this.startAmount = startAmount;
    }

    public double getClosedAmount() {
        return closedAmount;
    }

    public void setClosedAmount(double closedAmount) {
        this.closedAmount = closedAmount;
    }

    public double getPhysicalCash() {
        return physicalCash;
    }

    public void setPhysicalCash(double physicalCash) {
        this.physicalCash = physicalCash;
    }

    public double geteCash() {
        return eCash;
    }

    public void seteCash(double eCash) {
        this.eCash = eCash;
    }

    public double getOverageAmount() {
        return overageAmount;
    }

    public void setOverageAmount(double overageAmount) {
        this.overageAmount = overageAmount;
    }

    public double getShortageAmount() {
        return shortageAmount;
    }

    public void setShortageAmount(double shortageAmount) {
        this.shortageAmount = shortageAmount;
    }

    public Timestamp getClosureDate() {
        return closureDate;
    }

    public Timestamp getEntryDate(){return entryDate;}
    public String getNotes(){return this.notes;}
    public void setNotes(String notes){this.notes = notes;}

    public void setEntryDate(Timestamp entryDate){this.entryDate = entryDate;}

    public void setClosureDate(Timestamp closureDate) {
        this.closureDate = closureDate;
    }

    public int getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(int enteredBy) {
        this.enteredBy = enteredBy;
    }

    public int getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(int closedBy) {
        this.closedBy = closedBy;
    }

    public byte getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(byte isClosed) {
        this.isClosed = isClosed;
    }
    public byte getIsSuspended() {
        return isSuspended;
    }
    public void setIsSuspended(byte isSuspended) {
        this.isSuspended = isSuspended;
    }
}//end of class...
