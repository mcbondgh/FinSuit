package app.models;

import app.config.db.DbConnection;
import app.errorLogger.ErrorLogger;
import app.fetchedData.BusinessInfoObject;
import app.fetchedData.SmsAPIObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainModel extends DbConnection {
    public ArrayList<BusinessInfoObject> getBusinessInfo() {
        ArrayList<BusinessInfoObject> data = new ArrayList<>();
        try {
            String query = "SELECT * FROM business_info;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String name =  resultSet.getString("business_name"); //0
                String number = resultSet.getString("mobile_number"); //1
                String otherNumber = resultSet.getString("other_number"); //2
                String email = resultSet.getString("email"); //3
                String accountPassword = resultSet.getString("account_password");//4
                String digital = resultSet.getString("digital_address"); //5
                String location = resultSet.getString("location"); //6
                String logo = resultSet.getString("logoPath");//7
                data.add(new BusinessInfoObject(name, number, otherNumber, email, accountPassword, digital, location, logo));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    public ArrayList<SmsAPIObject> getSmsApi() throws IOException {
        ArrayList<SmsAPIObject> data = new ArrayList<SmsAPIObject>();
        try {
            String query = "SELECT * FROM sms_and_email_api;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String key = resultSet.getString("api_key");
                String sender_id = resultSet.getString("sender_id");
                String email_address = resultSet.getString("email_address");
                String email_password = resultSet.getString("email_password");
                data.add(new SmsAPIObject(key, sender_id, email_address, email_password));
            }
        }catch (SQLException e) {e.printStackTrace();}
        return data;
    }







}//END OF CLASS...
