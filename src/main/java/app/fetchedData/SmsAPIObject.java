package app.fetchedData;

public class SmsAPIObject{

    String key;
    String sender_id;

    public SmsAPIObject(String key, String sender_id) {
        this.key = key;
        this.sender_id = sender_id;
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
}//end of class...
