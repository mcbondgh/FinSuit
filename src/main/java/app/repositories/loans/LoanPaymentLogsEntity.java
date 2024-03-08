package app.repositories.loans;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class LoanPaymentLogsEntity {
    private BigInteger log_id;
    private String loan_no;
    private Date installment_month;
    private double paid_amount, write_offs;
    private Timestamp date_collected;
    private int collected_by;

    public LoanPaymentLogsEntity() {}

    public LoanPaymentLogsEntity(BigInteger log_id, String loan_no, Date installment_month, double paid_amount, double write_offs, Timestamp date_collected, int collected_by) {
        this.log_id = log_id;
        this.loan_no = loan_no;
        this.installment_month = installment_month;
        this.paid_amount = paid_amount;
        this.write_offs = write_offs;
        this.date_collected = date_collected;
        this.collected_by = collected_by;
    }

    public BigInteger getLog_id() {
        return log_id;
    }
    public void setLog_id(BigInteger log_id) {
        this.log_id = log_id;
    }
    public String getLoan_no() {
        return loan_no;
    }
    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }
    public Date getInstallment_month() {
        return installment_month;
    }
    public void setInstallment_month(Date installment_month) {
        this.installment_month = installment_month;
    }
    public double getPaid_amount() {
        return paid_amount;
    }
    public void setPaid_amount(double paid_amount) {
        this.paid_amount = paid_amount;
    }
    public double getWrite_offs() {
        return write_offs;
    }
    public void setWrite_offs(double write_offs) {
        this.write_offs = write_offs;
    }
    public Timestamp getDate_collected() {
        return date_collected;
    }
    public void setDate_collected(Timestamp date_collected) {
        this.date_collected = date_collected;
    }
    public int getCollected_by() {
        return collected_by;
    }
    public void setCollected_by(int collected_by) {
        this.collected_by = collected_by;
    }
}//end of class
