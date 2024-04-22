package app.repositories.business;

import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class SuspenseAccountEntity {
    private int id;
    private String cashierName;
    private final Label status = new Label("Suspended");
    private String entryDateFormat;
    private Timestamp entryDate;
    private double overageAmount, shortageAmount, closureAmount;

    public SuspenseAccountEntity(int id, String cashierName, Timestamp entryDate, double overageAmount, double shortageAmount, double closureAmount) {
        this.id = id;
        this.cashierName = cashierName;
        this.entryDate = entryDate;
        this.overageAmount = overageAmount;
        this.shortageAmount = shortageAmount;
        this.closureAmount = closureAmount;
        entryDateFormat = entryDate.toLocalDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
        status.setStyle("-fx-font-size:14px; -fx-text-fill:#ff0000; -fx-font-family:arial; -fx-alignment:center");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public Label getStatus() {
        return status;
    }

    public String getEntryDateFormat() {
        return entryDateFormat;
    }

    public void setEntryDateFormat(String entryDateFormat) {
        this.entryDateFormat = entryDateFormat;
    }

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
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

    public double getClosureAmount() {
        return closureAmount;
    }

    public void setClosureAmount(double closureAmount) {
        this.closureAmount = closureAmount;
    }
}//end of class...
