package app.repositories.loans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ScheduleTableValues {
    private int index;
    private long scheduleId;
    private double principal;
    private double interestAmount;
    private double monthlyInstallment;
    private double balance;
    private LocalDate scheduleDate;
    private String formattedScheduleDate;

    public ScheduleTableValues() {}
    public ScheduleTableValues(int index, long scheduleId, double principal, double interestAmount, double monthlyInstallment, double balance, LocalDate scheduleDate) {
        this.index = index;
        this.scheduleId = scheduleId;
        this.principal = principal;
        this.interestAmount = interestAmount;
        this.monthlyInstallment = monthlyInstallment;
        this.balance = balance;
        this.scheduleDate = scheduleDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        formattedScheduleDate = scheduleDate.format(formatter);
    }
    public ScheduleTableValues(int index,  double principal, double interestAmount, double monthlyInstallment, double balance, LocalDate scheduleDate) {
        this.index = index;
        this.principal = principal;
        this.interestAmount = interestAmount;
        this.monthlyInstallment = monthlyInstallment;
        this.balance = balance;
        this.scheduleDate = scheduleDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        formattedScheduleDate = scheduleDate.format(formatter);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getPrincipal() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal = principal;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(double interestAmount) {
        this.interestAmount = interestAmount;
    }

    public double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public String getFormattedScheduleDate() {
        return formattedScheduleDate;
    }

    public void setFormattedScheduleDate(String formattedScheduleDate) {
        this.formattedScheduleDate = formattedScheduleDate;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    @Override
    public String toString() {
        return "ScheduleEntity{" +
                "index=" + index +
                ", principal=" + principal +
                ", interestAmount=" + interestAmount +
                ", monthlyInstallment=" + monthlyInstallment +
                ", balance=" + balance +
                ", scheduleDate=" + scheduleDate +
                '}';
    }


}//end of class...

