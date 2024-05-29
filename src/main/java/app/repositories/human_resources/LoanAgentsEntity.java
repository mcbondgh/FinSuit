package app.repositories.human_resources;

import java.sql.Timestamp;

public class LoanAgentsEntity {
//  agent_id, agent_name, mobile_number, other_number, information, date_joined, date_modified, added_by, is_deleted

    private int agentId, addedBy;
    private String agentName;
    private String mobileNumber, otherNumber, information;
    private String dateJoined, dateModified;
    private boolean isDeleted;
    private int count;

    public LoanAgentsEntity(int agentId, int addedBy, String agentName, String mobileNumber, String otherNumber, String information, String dateJoined, int count) {
        this.agentId = agentId;
        this.addedBy = addedBy;
        this.agentName = agentName;
        this.mobileNumber = mobileNumber;
        this.otherNumber = otherNumber;
        this.information = information;
        this.dateJoined = dateJoined;
        this.count = count;
    }

    public LoanAgentsEntity() {
    }

    public LoanAgentsEntity(String agentName, String mobileNumber, String otherNumber, String information, int addedBy) {
        this.agentName = agentName;
        this.mobileNumber = mobileNumber;
        this.otherNumber = otherNumber;
        this.information = information;
        this.addedBy = addedBy;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(int addedBy) {
        this.addedBy = addedBy;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtherNumber() {
        return otherNumber;
    }

    public void setOtherNumber(String otherNumber) {
        this.otherNumber = otherNumber;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(String dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}//end of class
