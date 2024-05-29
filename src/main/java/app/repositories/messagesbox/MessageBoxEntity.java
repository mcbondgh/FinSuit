package app.repositories.messagesbox;

import javafx.scene.control.Label;

public class MessageBoxEntity
{
//    log_id, sent_to, title, message, Status, sent_date, sent_by
    private long log_id;
    private
    String sent_to, title, message;
    private
    Label status = new Label();
    private
    String sent_date;

    public MessageBoxEntity(long log_id, String sent_to, String title, String message, String status, String sent_date) {
        this.log_id = log_id;
        this.sent_to = sent_to;
        this.title = title;
        this.message = message;
        this.status.setText(status);
        this.sent_date = sent_date;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public String getSent_to() {
        return sent_to;
    }

    public void setSent_to(String sent_to) {
        this.sent_to = sent_to;
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
        return status.getText();
    }

    public void setStatus(String status) {
        this.status.setText(status);
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }
}//end of class...
