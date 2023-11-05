package app.repositories.loans;

import java.time.LocalDate;

public class PendingLoanApprovalEntity {

    private String loan_no;
    private double requested_amount, gross_salary, statutory_deduction;
    private double remaining_balance, total_deduction, amount, loan_amount;
    private int interest_rate, loan_period, processing_rate;
    private LocalDate start_date;
    private LocalDate end_date;

    public PendingLoanApprovalEntity() {}

    public PendingLoanApprovalEntity(String loan_no, double requested_amount, double gross_salary, double statutory_deduction, double remaining_balance, double total_deduction, double amount, double loan_amount, int interest_rate, int loan_period, int processing_rate, LocalDate start_date) {
        this.loan_no = loan_no;
        this.requested_amount = requested_amount;
        this.gross_salary = gross_salary;
        this.statutory_deduction = statutory_deduction;
        this.remaining_balance = remaining_balance;
        this.total_deduction = total_deduction;
        this.amount = amount;
        this.loan_amount = loan_amount;
        this.interest_rate = interest_rate;
        this.loan_period = loan_period;
        this.processing_rate = processing_rate;
        this.start_date = start_date;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public void setLoan_no(String loan_no) {
        this.loan_no = loan_no;
    }

    public double getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(double requested_amount) {
        this.requested_amount = requested_amount;
    }

    public double getGross_salary() {
        return gross_salary;
    }

    public void setGross_salary(double gross_salary) {
        this.gross_salary = gross_salary;
    }

    public double getStatutory_deduction() {
        return statutory_deduction;
    }

    public void setStatutory_deduction(double statutory_deduction) {
        this.statutory_deduction = statutory_deduction;
    }

    public double getRemaining_balance() {
        return remaining_balance;
    }

    public void setRemaining_balance(double remaining_balance) {
        this.remaining_balance = remaining_balance;
    }

    public double getTotal_deduction() {
        return total_deduction;
    }

    public void setTotal_deduction(double total_deduction) {
        this.total_deduction = total_deduction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLoan_amount() {
        return loan_amount;
    }

    public void setLoan_amount(double loan_amount) {
        this.loan_amount = loan_amount;
    }

    public int getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(int interest_rate) {
        this.interest_rate = interest_rate;
    }

    public int getLoan_period() {
        return loan_period;
    }

    public void setLoan_period(int loan_period) {
        this.loan_period = loan_period;
    }

    public int getProcessing_rate() {
        return processing_rate;
    }

    public void setProcessing_rate(int processing_rate) {
        this.processing_rate = processing_rate;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }
}//end of class...
