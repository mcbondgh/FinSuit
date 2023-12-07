package app.repositories.loans;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class DisbursementEntity {

    private int id;
    private String loanNumber;
    private double loanAmount;

    private String applicationStatus;
    private Label status = new Label();
    private CheckBox payBtn = new CheckBox("Pay");
    private ComboBox<String> method = new ComboBox<>();


    public DisbursementEntity(int id, String loanNumber, double loanAmount, String appStatus) {
        this.id = id;
        this.loanNumber = loanNumber;
        this.loanAmount = loanAmount;
        applicationStatus = appStatus;
        comboBox();
        styleStatus();
    }

    private void comboBox() {
        method.getItems().add("CASH");
        method.getItems().add("eCASH");
        method.setStyle("-fx-pref-width:120px; -fx-font-family:roboto: -fx-font-weight:bold; -fx-background-color:#eee");
    }

    private void styleStatus(){
        boolean value = applicationStatus.equals("pending_payment");
        payBtn.setStyle("-fx-font-family:roboto; -fx-font-size:12px; -fx-font-weight:bold;");
        if (value) {
            status.setText("Unpaid");
            status.setStyle("-fx-text-fill:#fff; -fx-background-color:#ff3939;-fx-alignment:center; " +
                    "-fx-padding:4px; -fx-background-radius: 5px; -fx-pref-width:70px; -fx-font-size:12px; -fx-font-family:roboto");
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public ComboBox<String> getMethod() {
        return method;
    }

    public void setMethod(ComboBox<String> method) {
        this.method = method;
    }

    public Label getStatus() {
        return status;
    }

    public void setStatus(Label status) {
        this.status = status;
    }

    public CheckBox getPayBtn() {
        return payBtn;
    }

    public void setPayBtn(CheckBox payBtn) {
        this.payBtn = payBtn;
    }
}//end of class...
