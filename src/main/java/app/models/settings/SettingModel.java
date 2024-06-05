package app.models.settings;

import app.models.MainModel;
import app.repositories.operations.PermissionsEntity;

import java.sql.SQLException;

public class SettingModel extends MainModel {

    protected byte updateBusinessInfo(String name, String mobileNumber, String otherNumber, String email, String accountPassword, String digital, String location, byte[] logo, double percentageValue, double taxValue) {
        byte flag = 0;
        try {
            String query = "UPDATE business_info SET business_name = ?, mobile_number = ?, other_number = ?, email = ?, account_password = ?, digital_address = ?, location = ?, logo = ?, loan_percentage = ?, withdrawal_tax = ?, date_modified = DEFAULT";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, mobileNumber);
            preparedStatement.setString(3, otherNumber);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, accountPassword);
            preparedStatement.setString(6, digital);
            preparedStatement.setString(7, location);
            preparedStatement.setBytes(8, logo);
            preparedStatement.setDouble(9, percentageValue);
            preparedStatement.setDouble(10, taxValue);
            flag = (byte) preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.getMessage();
            rollBack();
        }
        return flag;
    }
    protected byte updateSenderId(String senderId, String email, String password) {
        byte flag = 0;
            try {
                String query = "UPDATE sms_and_email_api SET sender_id = ?, email_address = ?, email_password = ?";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, senderId);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                flag = (byte) preparedStatement.executeUpdate();
                preparedStatement.close();
                getConnection().close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        return flag;
    }
    protected int saveMessageTemplate(String title, String message, int userId) {
        int flag = 0;
        try {
            String query = "INSERT INTO message_templates(title, message, modified_by) VALUES(?, ?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, message);
            preparedStatement.setInt(3, userId);
            flag = preparedStatement.executeUpdate();
            preparedStatement.close();
            getConnection().close();
        }catch (SQLException ignore) {
            rollBack();
        }
        return flag;
    }
    protected void updateMessageTemplate(int id, String title, String body, int userId) {
        try {
            String query = "UPDATE message_templates SET title = ?, message = ?, modified_by = ? WHERE(message_id = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1,title);
            preparedStatement.setString(2, body);
            preparedStatement.setInt(3, userId);
            preparedStatement.setInt(4, id);
            preparedStatement.execute();
            getConnection().close();
        }catch (SQLException ignore) {}
    }
    protected void updateMessageOperation(int tempId, String operation) {
        try {
            String query = "UPDATE system_operations SET template_id = ? WHERE(operation_type = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, tempId);
            preparedStatement.setString(2, operation);
            preparedStatement.execute();

            preparedStatement.close();
            getConnection().close();
        }catch (Exception ignore) {}
    }

    protected int saveAccessControlPermissions(PermissionsEntity entity) {
        try {
            //control_id, module_id, role_id, permission_id, is_allowed, date_modified, modified_by
            String query = """
                        INSERT INTO access_control(module_id, role_id, permission_id, is_allowed, modified_by)
                        VALUES(?, ?, ?, ?, ?)
                        ON DUPLICATE KEY\s
                        UPDATE\s
                        permission_id = ?,
                        is_allowed = ?,
                        modified_by = ?;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, entity.getModule_id() );
            preparedStatement.setInt(2, entity.getRoleId());
            preparedStatement.setInt(3, entity.getOperation_id());
            preparedStatement.setBoolean(4, entity.getAllowed());
            preparedStatement.setInt(5, entity.getModified_by());
            preparedStatement.setInt(6, entity.getOperation_id());
            preparedStatement.setBoolean(7, entity.getAllowed());
            preparedStatement.setInt(8, entity.getModified_by());
            return preparedStatement.executeUpdate();
        }catch (SQLException ex) {ex.printStackTrace();}

        return 0;
    }





}//end of class...
