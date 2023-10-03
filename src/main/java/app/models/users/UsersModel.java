package app.models.users;

import app.models.MainModel;

import java.sql.SQLException;

public class UsersModel extends MainModel {

    protected int addNewUser(String emp_id, int role_id, String username, String password, int added_by) {
        int flag = 0;
            try {
                String query = "INSERT INTO users(emp_id, role_id, username, user_password, added_by) VALUES(?, ?, ?, ?, ?);";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, emp_id);
                preparedStatement.setInt(2, role_id);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, password);
                preparedStatement.setInt(5, added_by);
                flag = preparedStatement.executeUpdate();
                commitTransaction();
            }catch (SQLException e){rollBack();}
        return flag;
    }



//    user_id, emp_id, role_id, username, user_password, is_active, is_deleted, date_created, added_by, date_modified, modified_by
    protected int updateUserLogins(String emp_id, String username, int role_id, String password, int modified_by ) {
        int flag = 0;
            try {
                String query = "UPDATE users SET username = ?, role_id = ?, user_password = ?, date_modified = DEFAULT, modified_by = ? WHERE(emp_id = ?);";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, role_id);
                preparedStatement.setString(3, password);
                preparedStatement.setInt(4, modified_by);
                preparedStatement.setString(5, emp_id);
                flag = preparedStatement.executeUpdate();
                commitTransaction();
            }catch (Exception e){rollBack();}
        return flag;
    }

    protected void updateStatusOnly(String emp_id, byte statusValue) {
        try {
            String query = "UPDATE users SET is_active = ? WHERE(emp_id = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setByte(1, statusValue);
            preparedStatement.setString(2, emp_id);
            preparedStatement.execute();
            commitTransaction();
        }catch (SQLException ignore){
            rollBack();
        }

    }



}//end of class...
