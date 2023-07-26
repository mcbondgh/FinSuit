package app.models.settings;

import app.config.db.DbConnection;

import java.sql.SQLException;

public class SettingModel extends DbConnection {

    protected byte updateBusinessInfo(String name, String mobileNumber, String otherNumber, String email, String digital, String location, String logo) {
        byte flag = 0;
        try {
            String query = "UPDATE business_info SET business_name = ?, mobile_number = ?, other_number = ?, email = ?, digital_address = ?, location = ?, logoPath = ?, date_modified = DEFAULT";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, mobileNumber);
            preparedStatement.setString(3, otherNumber);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, digital);
            preparedStatement.setString(6, location);
            preparedStatement.setString(7, logo);
            flag = (byte) preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    protected byte updateSenderId(String senderId) {
        byte flag = 0;
            try {
                String query = "UPDATE sms_api SET sender_id = ?";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, senderId);
                flag = (byte) preparedStatement.executeUpdate();
                preparedStatement.close();
                getConnection().close();
            }catch (SQLException e){e.printStackTrace();}

        return flag;
    }


}//end of class...
