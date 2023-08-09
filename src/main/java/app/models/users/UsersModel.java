package app.models.users;

import app.models.MainModel;

import java.sql.SQLException;

public class UsersModel extends MainModel {

//    user_id, emp_id, role_id, username, user_password, is_active, is_deleted, date_created, added_by, date_modified, modified_by
    protected int updateUser(String username, int role_id, String password, int modified_by ) {
        int flag = 0;

        return flag;
    }
    protected int updateUserStatuOnly(int user_id, byte statusValue) {
        int flag = 0;
        try {
            String query = "UPDATE users SET ";
        }catch (SQLException ignore){}
        return flag;
    }



}//end of class...
