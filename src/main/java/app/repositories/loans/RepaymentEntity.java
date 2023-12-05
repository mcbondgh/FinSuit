package app.repositories.loans;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class RepaymentEntity {
    private long log_id;
    private String loan_no;
    private Date installment_month;
    private double paid_amount;
    private Timestamp date_collected;
    private int collected_by;

    public RepaymentEntity() {}

    public RepaymentEntity(long log_id, String loan_no, Date installment_month, double paid_amount, Timestamp date_collected, int collected_by) {
        this.log_id = log_id;
        this.loan_no = loan_no;
        this.installment_month = installment_month;
        this.paid_amount = paid_amount;
        this.date_collected = date_collected;
        this.collected_by = collected_by;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
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
}//end of class...

