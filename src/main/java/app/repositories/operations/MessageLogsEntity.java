package app.repositories.operations;

import javafx.scene.control.Label;

import java.sql.Timestamp;

public class MessageLogsEntity {

    private int log_id;
    private String mobileNumber;
    private String title;
    private String message;
    private String status;
    private Label statusLabe = new Label();
    private Timestamp sent_date;
    private int sent_by;

    public MessageLogsEntity(int log_id, String mobileNumber, String title, String message, String status, Label statusLabe, Timestamp sent_date, int sent_by) {
        this.log_id = log_id;
        this.mobileNumber = mobileNumber;
        this.title = title;
        this.message = message;
        this.status = status;
        this.statusLabe = statusLabe;
        this.sent_date = sent_date;
        this.sent_by = sent_by;
    }


    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public Label getStatusLabe() {
        return statusLabe;
    }

    public void setStatusLabe(Label statusLabe) {
        this.statusLabe = statusLabe;
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
}
