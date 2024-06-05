package app.repositories.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;

public class LoanApplicationReportEntity {
    private static final Logger log = LoggerFactory.getLogger(LoanApplicationReportEntity.class);
    String fullname, loan_no, application_status, super_name;
    double approved_amount, repayment_amount, total_payment;
    Date date_created;
    private int counter;
    public LoanApplicationReportEntity(int counter, String fullname, String loan_no, String application_status, String super_name, double approved_amount, double repayment_amount, double total_payment, Date date_created) {
        this.counter = counter;
        this.fullname = fullname;
        this.loan_no = loan_no;
        this.application_status = application_status;
        this.super_name = super_name;
        this.approved_amount = approved_amount;
        this.repayment_amount = repayment_amount;
        this.total_payment = total_payment;
        this.date_created = date_created;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public String getApplication_status() {
        return application_status;
    }

    public void setApplication_status(String application_status) {
        this.application_status = application_status;
    }

    public String getSuper_name() {
        return super_name;
    }

    public void setSuper_name(String super_name) {
        this.super_name = super_name;
    }

    public double getApproved_amount() {
        return approved_amount;
    }

    public void setApproved_amount(double approved_amount) {
        this.approved_amount = approved_amount;
    }

    public double getRepayment_amount() {
        return repayment_amount;
    }

    public void setRepayment_amount(double repayment_amount) {
        this.repayment_amount = repayment_amount;
    }

    public double getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(double total_payment) {
        this.total_payment = total_payment;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }
}//end of class
