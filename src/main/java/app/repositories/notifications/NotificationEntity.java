package app.repositories.notifications;

import java.sql.Timestamp;

public class NotificationEntity {
    long id;
    private String title, sender_method, message;
    private Timestamp logged_date;
    private int logged_by;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender_method() {
        return sender_method;
    }

    public void setSender_method(String sender_method) {
        this.sender_method = sender_method;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getLogged_date() {
        return logged_date;
    }

    public void setLogged_date(Timestamp logged_date) {
        this.logged_date = logged_date;
    }

    public int getLogged_by() {
        return logged_by;
    }

    public void setLogged_by(int logged_by) {
        this.logged_by = logged_by;
    }
}//end of class...
