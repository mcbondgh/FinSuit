package app.repositories.loans;

import app.models.MainModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.time.LocalDate;

public class CollectionSheetEntity extends MainModel {
    private int loanId;
    private String loanNo;
    private String customerName, officerName;
    private LocalDate sheetDate;
    private LocalDate dueDate;
    private double installmentAmount;
    private double paidAmount;
    private ComboBox<Date> dueDateSelector = new ComboBox<>();

    public CollectionSheetEntity(int loanId, String loanNo, String customerName, String officerName,  double installmentAmount) {
        this.loanId = loanId;
        this.loanNo = loanNo;
        this.customerName = customerName;
        this.officerName = officerName;
        this.installmentAmount = installmentAmount;
        comboBoxStyle();
    }

    private void comboBoxStyle() {
        dueDateSelector.setStyle("-fx-background-color:#eee; -fx-background-radius:5px; " +
                "-fx-pref-width:200; -fx-font-size:13px; -fx-padding:4px; -fx-margin-top:3px; -fx-font-family:roboto; -fx-pref-height:25px; -fx-text-fill:black;");
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getSheetDate() {
        return sheetDate;
    }

    public void setSheetDate(LocalDate sheetDate) {
        this.sheetDate = sheetDate;
    }


    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public ComboBox<Date> getDueDateSelector() {
        return dueDateSelector;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public void setDueDateSelector(ComboBox<Date> dueDateSelector) {
        this.dueDateSelector = dueDateSelector;
    }
}//end of class....
