package app.repositories.notifications;

import app.repositories.users.UsersData;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class NotificationEntity {
    long id;
    private String title, sender_method, message;
    private Timestamp logged_date;
    private int logged_by;
    private UsersData username;
    private String localDate;
    private boolean is_read;

    public NotificationEntity() {
    }

    public NotificationEntity(String title, String sender_method, String message, int logged_by) {
        this.title = title;
        this.sender_method = sender_method;
        this.message = message;
        this.logged_by = logged_by;
    }

    public NotificationEntity(long id, String title, String sender_method, String message, Timestamp logged_date, boolean is_read, UsersData username) {
        this.id = id;
        this.title = title;
        this.sender_method = sender_method;
        this.message = message;
        this.logged_date = logged_date;
        this.username = username;
        this.is_read = is_read;
        localDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(logged_date.toLocalDateTime());
    }

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

    public UsersData getUsername() {
        return username;
    }

    public void setUsername(UsersData username) {
        this.username = username;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public boolean getIsRead() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}//end of class...
