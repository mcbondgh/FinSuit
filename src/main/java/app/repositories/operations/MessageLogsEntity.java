package app.repositories.operations;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.control.Label;

import java.sql.Timestamp;

public class MessageLogsEntity {

    private int log_id;
    private String recipient;
    private String title;
    private String message;
    private String status;
    private Label statusLabel = new Label();
    private final MFXButton resendButton = new MFXButton("Resend");
    private Timestamp sent_date;
    private int sent_by;
    String username, date_sent;

    public MessageLogsEntity(){}
    public MessageLogsEntity(int log_id, String recipient, String title, String message, String status, String sent_date, String username) {
        this.log_id = log_id;
        this.recipient = recipient;
        this.title = title;
        this.message = message;
        this.statusLabel.setText(status);
        this.date_sent = sent_date;
        this.username = username;

        setStatusProperties();
        setButtonProperties();
    }

    private void setButtonProperties() {
        resendButton.setStyle("-fx-background-color: #278c8f; " +
                "-fx-font-family:poppins medium; " +
                "-fx-font-size:11px; -fx-padding:5px; " +
                "-fx-width:60; -fx-text-fill:#fff; fx-text-align:center");
    }

    private void setStatusProperties() {
        if (statusLabel.getText().equalsIgnoreCase("OK")) {
            statusLabel.setStyle("-fx-text-fill:#0aa12b");
            statusLabel.setText("Delivered");
            resendButton.setDisable(true);
        } else if(statusLabel.getText().equalsIgnoreCase("INVALID_PHONE_NUMBER")
                || statusLabel.getText().equalsIgnoreCase("103")) {
            statusLabel.setStyle("-fx-text-fill:#1263cc");
            statusLabel.setText("Invalid Number");
            resendButton.setDisable(false);
        } else {
            statusLabel.setStyle("-fx-text-fill:#cc3112");
            statusLabel.setText("Failed");
            resendButton.setDisable(false);
        }
    }


    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Timestamp getSent_date() {
        return sent_date;
    }

    public void setSent_date(Timestamp sent_date) {
        this.sent_date = sent_date;
    }

    public int getSent_by() {
        return sent_by;
    }

    public void setSent_by(int sent_by) {
        this.sent_by = sent_by;
    }

    public MFXButton getResendButton() {
        return resendButton;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate_sent() {
        return date_sent;
    }

    public void setDate_sent(String date_sent) {
        this.date_sent = date_sent;
    }
}
