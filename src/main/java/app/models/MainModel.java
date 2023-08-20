package app.models;

import app.config.db.DbConnection;
import app.fetchedData.BusinessInfoObject;
import app.fetchedData.SmsAPIObject;
import app.fetchedData.human_resources.EmployeesData;
import app.fetchedData.roles.UserRolesData;
import app.fetchedData.users.UsersData;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public int getTotalEmployees() {
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

    public int getTotalAccountNumbers() {
        int count = 0;
        try {
            String query = "SELECT count(*) FROM customer_accounts";
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
            String query = "SELECT work_id , concat(firstname, \" \", othername, \" \", lastname) AS fullname,\n" +
                    "gender, mobile_number, employment_date, designation, salary, is_active FROM employees as emp WHERE(is_deleted = 0);";
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

    protected ObservableList<EmployeesData> fetchAllEmployees() {
        ObservableList<EmployeesData> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM employees WHERE(is_deleted = 0);";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int emp_id = resultSet.getInt("emp_id");
                String work_id = resultSet.getString("work_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String othername = resultSet.getString("othername");
                String email = resultSet.getString("email");
                String mobileNumber = resultSet.getString("mobile_number");
                String otherNumber = resultSet.getString("other_number");
                String gender = resultSet.getNString("gender");
                LocalDate dob = resultSet.getDate("dob").toLocalDate();
                String digitalAddress = resultSet.getString("digital_address");
                String residentialAddress = resultSet.getString("residential_address");
                String landMark = resultSet.getString("landmark");
                String idType = resultSet.getString("id_type");
                String idNumber = resultSet.getString("id_number");
                String maritalStatus = resultSet.getString("marital_status");
                String qualification = resultSet.getString("qualification");
                String designation = resultSet.getString("designation");
                String workingExperience = resultSet.getString("working_experience");
                LocalDate employmentDate = resultSet.getDate("employment_date").toLocalDate();
                String contactPersonName = resultSet.getString("contact_person_name");
                String contactPersonNumber = resultSet.getString("contact_person_number");
                String contactPersonDigitalAddress = resultSet.getString("contact_person_digital_address");
                String contactPersonAddress = resultSet.getString("contact_person_address");
                String contactPersonLandmark = resultSet.getString("contact_person_landmark");
                String placeOfWork = resultSet.getString("contact_person_place_of_work");
                String organizationNumber = resultSet.getString("contact_person_org_number");
                String organizationAddress = resultSet.getString("contact_person_org_address");
                String additionalInfo = resultSet.getString("additional_information");
                byte activeStatus = resultSet.getByte("is_active");
                byte deletedStatus = resultSet.getByte("is_deleted");
                LocalDateTime dateAdded = resultSet.getTimestamp("date_added").toLocalDateTime();
                LocalDateTime dateModified = resultSet.getTimestamp("date_modified").toLocalDateTime();
                int addedBy = resultSet.getInt("added_by");
                int modifiedBy = resultSet.getInt("modified_by");
                double salary = resultSet.getDouble("salary");
                String bankName = resultSet.getString("bank_name");
                String accountNumber = resultSet.getString("account_number");
                String accountName = resultSet.getString("account_name");
                data.add(new EmployeesData(
                        emp_id, work_id, firstname, lastname, othername, email, mobileNumber, otherNumber,gender, dob,digitalAddress, residentialAddress, landMark,
                        idType, idNumber, maritalStatus, qualification, designation, workingExperience,employmentDate, contactPersonName, contactPersonNumber,
                        contactPersonDigitalAddress, contactPersonAddress, contactPersonLandmark, placeOfWork, organizationNumber,
                        organizationAddress, additionalInfo, dateAdded, dateModified, activeStatus, deletedStatus ,addedBy, modifiedBy,
                        salary, bankName, accountName, accountNumber));
            }
            preparedStatement.close();
            getConnection().close();
        }catch (Exception e) {e.printStackTrace();}
        return data;
    }
    public ObservableList<UserRolesData> getUserRoles() {
        ObservableList<UserRolesData> roles  = new ObservableStack<>();
            try {
                String query  = "SELECT role_id, role_name FROM roles WHERE(is_default = 0)";
                preparedStatement = getConnection().prepareStatement(query);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("role_id");
                    String role_name = resultSet.getString("role_name");
                    roles.add(new UserRolesData(id, role_name));
                }
                preparedStatement.close();
                resultSet.close();
                getConnection().close();
            }catch (SQLException e){e.printStackTrace();}
        return roles;
    }

    public ObservableList<UsersData> fetchAssignedUsersOnly() {
        ObservableList<UsersData> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT emp_id, role_name, username, " +
                    "is_active FROM users AS u\n" +
                    "JOIN roles AS r \n" +
                    "ON  u.role_id = r.role_id\n" +
                    "WHERE( is_deleted = 0);";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String emp_id = resultSet.getString("emp_id");
                String role_name = resultSet.getString("role_name");
                String username = resultSet.getString("username");
                byte active = resultSet.getByte("is_active");
                data.add(new UsersData(emp_id, username, role_name, active));
            }
        }catch (Exception exception){exception.printStackTrace();}

        return data;
    }


    protected int getRoleIdByName(@NamedArg("role Name")String role_name) {
        int flag = 0;
            try {
                String query = "SELECT role_id FROM roles WHERE role_name = ?;";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, role_name);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    flag = resultSet.getInt(1);
                }
            }catch (SQLException ignored){}
        return flag;
    }





}//END OF CLASS...
