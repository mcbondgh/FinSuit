package app.repositories.settings;

import java.sql.Timestamp;

public class TemplatesRepository {
    private int templateId;
    private String messageTitle;
    private String messageBody;
    private int userId;
    private Timestamp dateCreated;

    public TemplatesRepository(int templateId, String messageTitle, String messageBody, int userId, Timestamp dateCreated) {
        this.templateId = templateId;
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
        this.userId = userId;
        this.dateCreated = dateCreated;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}//end of class...
