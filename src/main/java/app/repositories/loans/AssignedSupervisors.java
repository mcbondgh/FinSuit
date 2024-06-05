package app.repositories.loans;

import app.repositories.accounts.CustomersDataRepository;

public class AssignedSupervisors {
    private int counter;
    private String customerName, mobileNumber, loanNumber, loanStatus;

    private double repayment, totalPayment;

     public AssignedSupervisors(int counter, String customerName, String mobileNumber, String loanNumber, String loanStatus, double repayment, double totalPayment) {
         this.counter = counter;
         this.customerName = customerName;
         this.mobileNumber = mobileNumber;
         this.loanNumber = loanNumber;
         this.loanStatus = loanStatus;
         this.repayment = repayment;
         this.totalPayment = totalPayment;
     }

     public String getLoanStatus() {
         return loanStatus;
     }

     public void setLoanStatus(String loanStatus) {
         this.loanStatus = loanStatus;
     }

     public String getLoanNumber() {
         return loanNumber;
     }

     public void setLoanNumber(String loanNumber) {
         this.loanNumber = loanNumber;
     }


    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getRepayment() {
        return repayment;
    }

    public void setRepayment(double repayment) {
        this.repayment = repayment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
