package app.fetchedData.users;

import java.sql.Timestamp;

public class UsersData {
    private int user_id;
    private String emp_id;
    private int role_id;
    private String username, user_password;
    private byte is_active, is_deleted;
    private Timestamp date_created;
    private int added_by;
    private Timestamp date_modified;
    private int modified_by;

    public UsersData(int user_id, String emp_id, int role_id, String username, String user_password, byte is_active, byte is_deleted, Timestamp date_created, int added_by, Timestamp date_modified, int modified_by) {
        this.user_id = user_id;
        this.emp_id = emp_id;
        this.role_id = role_id;
        this.username = username;
        this.user_password = user_password;
        this.is_active = is_active;
        this.is_deleted = is_deleted;
        this.date_created = date_created;
        this.added_by = added_by;
        this.date_modified = date_modified;
        this.modified_by = modified_by;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public byte getIs_active() {
        return is_active;
    }

    public void setIs_active(byte is_active) {
        this.is_active = is_active;
    }

    public byte getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(byte is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public void setDate_created(Timestamp date_created) {
        this.date_created = date_created;
    }

    public int getAdded_by() {
        return added_by;
    }

    public void setAdded_by(int added_by) {
        this.added_by = added_by;
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
}//end of class...
