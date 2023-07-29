package app.fetchedData;

public class SmsAPIObject{

    String key;
    String sender_id;
    String emailAddress;
    String password;

    public SmsAPIObject(String key, String sender_id, String emailAddress, String password) {
        this.key = key;
        this.sender_id = sender_id;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}//end of class...
