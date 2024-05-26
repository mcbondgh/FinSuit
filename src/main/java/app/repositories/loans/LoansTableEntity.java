package app.repositories.loans;

import app.models.MainModel;
import app.repositories.users.UsersData;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class LoansTableEntity {
    private int no;
    private String fullName;
    private String username;
    private String loanNo;
    private String loanPurpose;
    private Date applicationDate;
    private String formattedDate;
    private Double requestedAmount;
    private String status;
    private Label statusLabel = new Label("Unspecified");
    private String loanType;
    private MFXButton cancelButton = new MFXButton("Cancel");
    private MFXButton editButton = new MFXButton("Edit");
    private ComboBox<String> supervisorSelector = new ComboBox<>();
    double grossSalary;

    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    public LoansTableEntity(int no, String fullName, String loanNo, Date applicationDate, Double requestedAmount, String username, String status, String loanType, String loanPurpose, double grossSalary) {
        this.no = no;
        this.fullName = fullName;
        this.loanNo = loanNo;
        this.applicationDate = applicationDate;
        this.requestedAmount = requestedAmount;
        this.status = status;
        this.loanType = loanType;
        this.loanPurpose = loanPurpose;
        this.username = username;
        this.grossSalary = grossSalary;
        this.formattedDate = formatter.format(LocalDate.parse(applicationDate.toString()));
//        statusLabel = new Label("Unspecified");
        buttonStyling();
        badgeStyling();
    }

    public LoansTableEntity(int no, String fullName, String loanNo, Date applicationDate, String loanType) {
        this.no = no;
        this.fullName = fullName;
        this.loanNo = loanNo;
        this.applicationDate = applicationDate;
        this.status = status;
        this.loanType = loanType;
        this.formattedDate = formatter.format(LocalDate.parse(applicationDate.toString()));
//        statusLabel = new Label("Unspecified");
        comboBoxStyling();

    }



    private void buttonStyling() {
        cancelButton.setStyle("-fx-border-radius: 8px;-fx-background-radius:8px; " +
                "-fx-border-color:#eee; -fx-text-fill: #fff; -fx-background-color:#e25757; -fx-font-family poppins; " +
                "-fx-font-size: 11px; -fx-font-weight:bold");
        editButton.setStyle("-fx-border-radius: 8px;-fx-background-radius:8px; " +
                "-fx-border-color:#eee; -fx-text-fill: #fff; -fx-background-color:#3f968f; -fx-font-family poppins; " +
                "-fx-font-size: 11px; -fx-font-weight:bold");
    }

    private void comboBoxStyling() {
        MainModel MODEL = new MainModel();
        for (UsersData data : MODEL.fetchAssignedUsersOnly()) {
           if (data.getRole().equals("Loan Officer")) {
               supervisorSelector.getItems().add(data.getUsername());
           }
        }
        supervisorSelector.setStyle("-fx-background-color:#eee; -fx-background-radius:5px; " +
                "-fx-pref-width:200; -fx-font-size:13px; -fx-padding:4px; -fx-margin-top:3px; -fx-font-family:poppins; -fx-pref-height:25px; -fx-text-fill:black;");
    }

    private void badgeStyling() {
        if (status.equals("application")) {
            statusLabel.setText("Loan application");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color: #1e90ff; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("processing")) {
            statusLabel.setText("Processing");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color: #ffba27; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("pending_approval")) {
            statusLabel.setText("Pending Approval");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color:#5e2eb0; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("pending_payment")) {
            statusLabel.setText("Pending Payment");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color:#059d83; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("disbursed")) {
            statusLabel.setText("Disbursed");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color:#1b828d; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("closed") || status.equals("terminated")) {
            statusLabel.setText("Cancelled");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color:brown; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("rejected")) {
            statusLabel.setText("Rejected");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color: #ff0000; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public Double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(Double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public MFXButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(MFXButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    public MFXButton getEditButton() {
        return editButton;
    }

    public void setEditButton(MFXButton editButton) {
        this.editButton = editButton;
    }

    public ComboBox<String> getSupervisorSelector() {
        return supervisorSelector;
    }

    public void setSupervisorSelector(ComboBox<String> supervisorSelector) {
        this.supervisorSelector = supervisorSelector;
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }
}//end of class...
