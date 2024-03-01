package app.repositories.loans;

import app.models.MainModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.Cursor;
import javafx.scene.control.Label;

import java.sql.Timestamp;
import java.time.LocalDate;

public class LoanScheduleEntity {
    private long schedule_id;
    private String loan_no;
    private Double monthly_installment, principal_amount, interest_amount;
    private LocalDate payment_date;
    private double balance, penalty_amount;
    private double monthly_payment;
    private Timestamp date_created;
    private int generated_by;
    private Label statusLabel = new Label();
    private MFXButton payBtn = new MFXButton("Collect");
    private MFXButton logsBtn = new MFXButton("View");

    public LoanScheduleEntity() {}

    public LoanScheduleEntity(long schedule_id, String loan_no, Double monthly_installment, Double principal_amount, Double interest_amount, LocalDate payment_date, double balance, double penalty_amount, Timestamp date_created, int generated_by) {
        this.schedule_id = schedule_id;
        this.loan_no = loan_no;
        this.monthly_installment = monthly_installment;
        this.principal_amount = principal_amount;
        this.interest_amount = interest_amount;
        this.payment_date = payment_date;
        this.balance = balance;
        this.penalty_amount = penalty_amount;
        this.date_created = date_created;
        this.generated_by = generated_by;
    }

    public LoanScheduleEntity(long schedule_id, double monthly_installment, double principal_amount, double interest_amount,
                              LocalDate payment_date, double penalty_amount, double monthly_payment) {
        this.schedule_id = schedule_id;
        this.monthly_installment = monthly_installment;
        this.principal_amount = principal_amount;
        this.interest_amount = interest_amount;
        this.payment_date = payment_date;
        this.penalty_amount = penalty_amount;
        this.monthly_payment = monthly_payment;

        statusLogics();
        penaltyLogics();
        payBtnLogics();
    }

    private void payBtnLogics() {
        payBtn.setStyle("-fx-text-fill:#fff; -fx-background-color:#00a323;-fx-alignment:center; " +
                "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:60px; -fx-font-size:10px; -fx-font-family:roboto");
        payBtn.setCursor(Cursor.HAND);
        payBtn.setDisable(statusLabel.getText().equals("Cleared"));
        logsBtn.setStyle("-fx-background-color:#eee;-fx-alignment:center; " +
                "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:60px; -fx-font-size:10px; -fx-font-family:roboto");
        logsBtn.setCursor(Cursor.HAND);
    }

    private void penaltyLogics() {
        LocalDate today = LocalDate.now();
        double percentage = monthly_installment * 0.1;
        penalty_amount = payment_date.isBefore(today) && (monthly_payment != monthly_installment) ? percentage : 0.00;
        if (penalty_amount != 0.0) {
            monthly_installment += penalty_amount;
        }
        balance = monthly_installment - monthly_payment;
    }

    private void statusLogics() {
        String statusText = monthly_payment == 0.00 ? "Unpaid" : (monthly_payment > 0 && monthly_payment < monthly_installment) ? "Part Payment" : "Cleared";
        switch(statusText) {
            case "Unpaid"->
                statusLabel.setStyle("-fx-text-fill:#fff; -fx-background-color:#ff3939;-fx-alignment:center; " +
                        "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:70px; -fx-font-size:10px; -fx-font-family:roboto");

            case "Part Payment" ->
                    statusLabel.setStyle("-fx-text-fill:#fff; -fx-background-color:orange;-fx-alignment:center; " +
                            "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:70px; -fx-font-size:10px; -fx-font-family:roboto");
            default ->
                statusLabel.setStyle("-fx-text-fill:#fff; -fx-background-color:#1880c5;-fx-alignment:center; " +
                        "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:70px; -fx-font-size:10px; -fx-font-family:roboto");
        }
        statusLabel.setText(statusText);
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

    public MFXButton getLogsBtn() {
        return logsBtn;
    }

    public void setLogsBtn(MFXButton logsBtn) {
        this.logsBtn = logsBtn;
    }

    public Double getPrincipal_amount() {
        return principal_amount;
    }

    public double getMonthly_payment() {
        return monthly_payment;
    }

    public void setMonthly_payment(double monthly_payment) {
        this.monthly_payment = monthly_payment;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public MFXButton getPayBtn() {
        return payBtn;
    }

    public void setPayBtn(MFXButton payBtn) {
        this.payBtn = payBtn;
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
