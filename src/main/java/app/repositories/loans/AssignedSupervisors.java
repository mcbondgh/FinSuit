package app.repositories.loans;

public class AssignedSupervisors {
    private int counter;
    private String superName, loanNumber, loanStatus;

     public AssignedSupervisors(int counter, String superName, String loanNumber, String loanStatus) {
         this.counter = counter;
         this.superName = superName;
         this.loanNumber = loanNumber;
         this.loanStatus = loanStatus;
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

     public String getSuperName() {
         return superName;
     }

     public void setSuperName(String superName) {
         this.superName = superName;
     }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
