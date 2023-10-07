package app.models.settings;

import app.models.MainModel;

import java.sql.SQLException;

public class SettingModel extends MainModel {

    protected byte updateBusinessInfo(String name, String mobileNumber, String otherNumber, String email, String accountPassword, String digital, String location, String logo, double percentageValue) {
        byte flag = 0;
        try {
            String query = "UPDATE business_info SET business_name = ?, mobile_number = ?, other_number = ?, email = ?, account_password = ?, digital_address = ?, location = ?, logoPath = ?, loan_percentage = ?, date_modified = DEFAULT";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, mobileNumber);
            preparedStatement.setString(3, otherNumber);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, accountPassword);
            preparedStatement.setString(6, digital);
            preparedStatement.setString(7, location);
            preparedStatement.setString(8, logo);
            preparedStatement.setDouble(9, percentageValue);
            flag = (byte) preparedStatement.executeUpdate();
            commitTransaction();
        }catch (SQLException e) {
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
            commitTransaction();
            preparedStatement.close();
            getConnection().close();
        }catch (SQLException ignore) {
            rollBack();
        }
        return flag;
    }




}//end of class...
