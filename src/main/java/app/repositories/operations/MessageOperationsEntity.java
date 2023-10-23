package app.repositories.operations;

import java.sql.Timestamp;

public class MessageOperationsEntity {
    int id;
    private int template_id;
    private String operation_type;
    private int message_id;
    private String title, message;
    private Timestamp date_modified;
    private int modified_by;

    public MessageOperationsEntity(int id, int template_id, String operation_type, int message_id, String title, String message, Timestamp date_modified, int modified_by) {
        this.id = id;
        this.template_id = template_id;
        this.operation_type = operation_type;
        this.message_id = message_id;
        this.title = title;
        this.message = message;
        this.date_modified = date_modified;
        this.modified_by = modified_by;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
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

    public Timestamp getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Timestamp date_modified) {
        this.date_modified = date_modified;
    }

    public int getModified_by() {
        return modified_by;
    }

    public void setModified_by(int modified_by) {
        this.modified_by = modified_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }


}// end of class...
