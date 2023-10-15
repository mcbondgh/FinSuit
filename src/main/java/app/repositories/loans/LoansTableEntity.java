package app.repositories.loans;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

public class LoansTableEntity {
    private int no;
    private String fullName;
    private String loanNo;
    private Date applicationDate;
    private String formattedDate;
    private Double requestedAmount;
    private String status;
    private Label statusLabel;
    private String loanType;
    private MFXButton viewButton = new MFXButton("View");
    private MFXButton editButton = new MFXButton("Edit");

    private DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    public LoansTableEntity(int no, String fullName, String loanNo, Date applicationDate, Double requestedAmount, String status, String loanType) {
        this.no = no;
        this.fullName = fullName;
        this.loanNo = loanNo;
        this.applicationDate = applicationDate;
        this.requestedAmount = requestedAmount;
        this.status = status;
        this.loanType = loanType;
        this.formattedDate = formatter.format(LocalDate.parse(applicationDate.toString()));
        statusLabel = new Label("Unspecified");
        buttonStyling();
        badgeStyling();

    }

    private void buttonStyling() {
        viewButton.setStyle("-fx-border-radius: 8px;-fx-background-radius:8px; " +
                "-fx-border-color:#eee; -fx-text-fill: #2b2929; -fx-background-color: #fff; -fx-font-family poppins; " +
                "-fx-font-size: 11px; -fx-font-weight:bold");
        editButton.setStyle("-fx-border-radius: 8px;-fx-background-radius:8px; " +
                "-fx-border-color:#eee; -fx-text-fill: #2b2929; -fx-background-color:#fff; -fx-font-family poppins; " +
                "-fx-font-size: 11px; -fx-font-weight:bold");
    }

    private void badgeStyling() {
        if (status.equals("processing")) {
            statusLabel.setText("Processing");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color: #1e90ff; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-text-alignment: center -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("pending_approval")) {
            statusLabel.setText("Pending Approval");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color: #ffba27; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-text-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("pending_payment")) {
            statusLabel.setText("Pending Payment");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color:#5e2eb0; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-text-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("paid")) {
            statusLabel.setText("Paid");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color:#059d83; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-text-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
        }
        if (status.equals("rejected")) {
            statusLabel.setText("Cancelled");
            statusLabel.setStyle("-fx-padding: 5px; -fx-background-color: #ff0000; -fx-text-fill:#fff; -fx-background-radius:7px; -fx-font-size:9px; -fx-pref-width:100px; -fx-text-alignment: center; -fx-font-family:poppins; -fx-font-weight:bold;");
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

    public MFXButton getViewButton() {
        return viewButton;
    }

    public void setViewButton(MFXButton viewButton) {
        this.viewButton = viewButton;
    }

    public MFXButton getEditButton() {
        return editButton;
    }

    public void setEditButton(MFXButton editButton) {
        this.editButton = editButton;
    }
}//end of class...
