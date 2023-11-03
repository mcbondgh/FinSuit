package app.repositories.loans;

import java.sql.Timestamp;
import java.time.LocalDate;

public class LoanScheduleEntity {
    private long schedule_id;
    private String loan_no;
    private Double monthly_installment, principal_amount, interest_amount;
    private LocalDate payment_date;
    private double balance, penalty_amount;
    private Timestamp date_created;
    private int generated_by;

    public LoanScheduleEntity(Double monthly_installment, Double principal_amount, Double interest_amount, LocalDate payment_date, double balance) {
        this.payment_date = payment_date;
        this.balance = balance;
        this.monthly_installment = monthly_installment;
        this.principal_amount = principal_amount;
        this.interest_amount = interest_amount;
    }

    public long getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(long schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public Double getMonthly_installment() {
        return monthly_installment;
    }

    public void setMonthly_installment(Double monthly_installment) {
        this.monthly_installment = monthly_installment;
    }

    public Double getPrincipal_amount() {
        return principal_amount;
    }

    public void setPrincipal_amount(Double principal_amount) {
        this.principal_amount = principal_amount;
    }

    public Double getInterest_amount() {
        return interest_amount;
    }

    public void setInterest_amount(Double interest_amount) {
        this.interest_amount = interest_amount;
    }

    public LocalDate getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDate payment_date) {
        this.payment_date = payment_date;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPenalty_amount() {
        return penalty_amount;
    }

    public void setPenalty_amount(double penalty_amount) {
        this.penalty_amount = penalty_amount;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }

    public int getGenerated_by() {
        return generated_by;
    }

    public void setGenerated_by(int generated_by) {
        this.generated_by = generated_by;
    }
}//end of class...