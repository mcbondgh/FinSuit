package app.models;

import app.config.db.DbConnection;
import app.errorLogger.ErrorLogger;
import app.fetchedData.BusinessInfoObject;
import app.fetchedData.SmsAPIObject;
import app.fetchedData.human_resources.EmployeesData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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
    public int getUserIdByName(String username) {
        int userId = 0;
        try {
            String query = "SELECT user_id FROM users WHERE(username = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
        }catch (Exception e) {e.printStackTrace();}
        return userId;
    }
    public int getTotalEmployeesCount() {
        int count = 0;
        try {
            String query = "SELECT count(*) FROM employees";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }catch (SQLException e) {e.printStackTrace();}
        return count;
    }
    protected int getLastEmployeeId() {
        int flag = 0;
        try {
            String query = "SELECT COUNT(*) FROM employees ORDER BY emp_id DESC LIMIT 1;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {flag = resultSet.getInt(1);}
        }catch (Exception ignored){}
        return flag;
    }

    protected ObservableList<EmployeesData> fetchEmployeeSummaryData() {
        ObservableList<EmployeesData> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT  work_id , concat(firstname, \" \", othername, \" \", lastname) AS fullname,\n" +
                    "gender, mobile_number, employment_date, designation, salary, is_active FROM employees as emp\n" +
                    "INNER JOIN employees_account_details as acd \n" +
                    "ON emp.work_id = acd.emp_id;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String work_id = resultSet.getString("work_id");
                String fullname = resultSet.getNString("fullname");
                String gender= resultSet.getNString("gender");
                String mobile_number = resultSet.getString("mobile_number");
                LocalDate employment_date = resultSet.getDate("employment_date").toLocalDate();
                String designation = resultSet.getString("designation");
                double salary = resultSet.getDouble("salary");
                byte is_active = resultSet.getByte("is_active");
                data.add(new EmployeesData(work_id, fullname, gender, mobile_number, employment_date, designation, salary, is_active));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (Exception e){e.printStackTrace();}
        return data;
    }





}//END OF CLASS...
