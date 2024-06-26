package app.models;

import app.config.db.DbConnection;
import app.repositories.human_resources.LoanAgentsEntity;
import app.repositories.loans.AssignedSupervisors;
import app.errorLogger.ErrorLogger;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.SmsAPIEntity;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.accounts.CustomersDocumentRepository;
import app.repositories.business.BusinessTransactionLogs;
import app.repositories.business.ClosedTellerTransactionEntity;
import app.repositories.business.DomesticTransactionLogsEntity;
import app.repositories.human_resources.EmployeesData;
import app.repositories.loans.*;
import app.repositories.notifications.NotificationEntity;
import app.repositories.operations.MessageOperationsEntity;
import app.repositories.operations.PermissionsEntity;
import app.repositories.roles.UserRolesData;
import app.repositories.settings.TemplatesRepository;
import app.repositories.transactions.TransactionsEntity;
import app.repositories.users.UsersData;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainModel extends DbConnection {

    ErrorLogger logger = new ErrorLogger();
    String className = this.getClass().getSimpleName();
    public ArrayList<BusinessInfoEntity> getBusinessInfo() {
        ArrayList<BusinessInfoEntity> data = new ArrayList<>();
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
                byte[] logo = resultSet.getBytes("logo");//7
                double loanPercentage = resultSet.getDouble("loan_percentage"); //8
                double  taxPercentage = resultSet.getDouble("withdrawal_tax");
                data.add(new BusinessInfoEntity(name, number, otherNumber, email, accountPassword, digital, location, logo, loanPercentage, taxPercentage));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public ArrayList<SmsAPIEntity> getSmsApi() throws IOException {
        ArrayList<SmsAPIEntity> data = new ArrayList<SmsAPIEntity>();
        try {
            String query = "SELECT * FROM sms_and_email_api;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String key = resultSet.getString("api_key");
                String sender_id = resultSet.getString("sender_id");
                String email_address = resultSet.getString("email_address");
                String email_password = resultSet.getString("email_password");
                data.add(new SmsAPIEntity(key, sender_id, email_address, email_password));
            }
        }catch (SQLException e) {
            logger.logMessage(e.getCause().toString(), "getSmsApi", className);}
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
        }catch (Exception ignored) {;}
        return userId;
    }

    //GET USERNAME, PASSWORD AND ROLE BASED ON THE SUPPLIED USERNAME...
    public Map<String, Object> fetchUserDataByUserName(String username){
        Map<String, Object> data = new HashMap<>();
        try {
            String query = "SELECT username, user_password, role_name FROM users AS u\n" +
                    "INNER JOIN roles AS r \n" +
                    "USING(role_id) WHERE(username = '"+username+"' AND is_deleted = 0 AND is_active = 1);";
            resultSet = getConnection().createStatement().executeQuery(query);
            if (resultSet.next()) {
                data.putIfAbsent("username", resultSet.getString("username"));
                data.put("password", resultSet.getString("user_password"));
                data.put("role", resultSet.getString("role_name"));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return data;
    }

    public void recordUserLogin(int userId, byte roleId) {
        try {
            String query = "INSERT INTO logins(user_id, role_id) VALUES(?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setByte(2, roleId);
            preparedStatement.execute();
        }catch (Exception ignore){}
    }

    public String getEmployeeIdByUsername(String username) {
        String emp_id = "";
        try {
            String query = "SELECT emp_id FROM users WHERE(username = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                emp_id = resultSet.getString(1);
            }
        }catch (Exception e) {
            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logger.logMessage(className, "getEmployeeIdByUsername", error);}
        return emp_id;
    }

    public String getWorkIdByUserId(int userId) {
        String emp_id = "";
        try {
            String query = "SELECT emp_id FROM users WHERE(user_id = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                emp_id = resultSet.getString(1);
            }
        }catch (Exception e) {
            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logger.logMessage(className, "getWorkIdByUserId", error);
        }
        return emp_id;
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
        }catch (SQLException e) {

            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logger.logMessage(className, "getTotalEmployees", error);
        }
        return count;
    }
    public String getEmployeeFullNameByWorkId(String userId) {
        try {
            String query = "SELECT concat(firstname, \" \", lastname) AS fullname FROM employees AS emp \n" +
                    "INNER JOIN users AS u ON emp.work_id = u.emp_id\n" +
                    "WHERE u.emp_id = ?;";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("fullname");
            }
        }catch (Exception e){

            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logger.logMessage(className, "getEmployeeFullNameByWorkId",error);
        }
        return "not found";
    }
    protected ArrayList<Object> getCustomerDetailsByAccountNumber(String searchParameter) {
        ArrayList<Object> data = new ArrayList<>();
        try {
            String query = "SELECT concat(firstname, ' ', lastname, ' ', othername) AS fullname, " +
                    "account_number, mobile_number, id_number, email, account_balance, previous_balance, pinNumber, account_status, cd.customer_id AS accountNo " +
                    "FROM customer_data AS cd\n" +
                    "JOIN customer_account_data AS cad USING(customer_id) \n" +
                    "JOIN loans AS ln USING(customer_id)" +
                    "WHERE(cad.account_number = ? OR mobile_number = ? OR loan_no = ?);";

            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, searchParameter);
            preparedStatement.setString(2, searchParameter);
            preparedStatement.setString(3, searchParameter);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                data.add(resultSet.getString("fullname"));//0
                data.add(resultSet.getInt("accountNo"));//1
                data.add(resultSet.getDouble("account_balance"));//2
                data.add(resultSet.getString("account_number"));//3
                data.add(resultSet.getString("mobile_number"));//4
                data.add(resultSet.getString("email"));//5
                data.add(resultSet.getString("account_status"));//6
                data.add(resultSet.getString("pinNumber"));//7
                data.add(resultSet.getString("previous_balance"));//8
                data.add(resultSet.getString("id_number"));//9
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException e){e.printStackTrace();}
        return data;
    }

    public Map<String, Object> getLastTransactionDate(String accountNumber) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        Map<String, Object> data = new HashMap<>();
        try {
            String query = "SELECT transaction_type, transaction_date FROM transaction_logs\n" +
                    "WHERE(account_number = '"+accountNumber+"')\n" +
                    "ORDER BY transaction_date DESC LIMIT 1;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
               String dateTime = resultSet.getTimestamp("transaction_date").toLocalDateTime().format(formatter);
               String type = resultSet.getString("transaction_type");
               data.put("dateTime", dateTime);
               data.put("transType", type);
            }
        }catch (SQLException ignore){}

        return data;
    }

    public long totalCustomersCount() {
        long count = 0;
        try {
            String query = "SELECT MAX(customer_id) from customer_data;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return count;

    }

    public long getTotalTransactionCount() {
        long value = 0;
        try {
            String query = "SELECT MAX(id) AS result FROM transaction_logs;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                value = resultSet.getLong("result");
            }
            preparedStatement.closeOnCompletion();
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return value;
    }
    public int getTotalDisbursedLoans() {
        int result = 0;
        try {
            String query = "SELECT COUNT(loan_id) disbursed FROM loans \n" +
                    "WHERE application_status = 'disbursed';";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result =  resultSet.getInt("disbursed");
            }
        }catch (Exception ignored){}
        return result;
    }

    public long getTotalLoanCount() {
        try {
            String query = "SELECT MAX(loan_id) AS 'max_id' FROM loans;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               return resultSet.getLong("max_id");
            }
        }catch (SQLException ignore) {

        }
        return 0;
    }

    public long getTotalDisbursedLoanCount()  {
        try {
            String query = "SELECT COUNT(loan_no) AS result FROM loans WHERE((application_status = 'disbursed' OR 'pending_payment')" +
                    " AND loan_status = 'active');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("result");
            }
        }catch (SQLException ignore) {

        }
        return 0;
    }

    protected int getMonthlyDueLoans() {
        try {
            String query = """
                    SELECT COUNT(schedule_id) AS due FROM loan_schedule AS ls
                    WHERE YEAR(payment_date) = YEAR(CURRENT_DATE) AND MONTH(payment_date) = MONTH(CURRENT_DATE);
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               return resultSet.getInt(1);
            }
        }catch (Exception ignored){}
        return 0;
    }
    protected int getPartiallyPaidLoans() {
        try {
            String query = """
                    SELECT COUNT(loan_id) AS part_payments FROM loans
                    WHERE total_payment != repayment_amount;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (Exception ignored){}
        return 0;
    }


    protected int getTotalApprovedLoansCount() {
        int result = 0;
        try {
            String query = "SELECT COUNT(loan_id) FROM loans WHERE(application_status = 'pending_payment' AND loan_status = 'active');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        }catch (Exception ignored){}
        return result;
    }

    protected int getPendingApprovalLoansCount() {
        int result = 0;
        try {
            String query = "SELECT COUNT(loan_id) FROM loans WHERE(application_status = 'pending_approval' AND loan_status = 'active');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        }catch (Exception ignored){}
        return result;
    }

    protected int getTotalPaidLoans() {
        int result = 0;
        try {
            String query = """
                    SELECT COUNT(loan_id) cleared FROM loans AS total
                    WHERE loan_status = 'cleared';
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        }catch (Exception ignored){}
        return result;
    }

    protected int getTotalLoanRequests() {
        int result = 0;
        try {
            String query = "SELECT COUNT(loan_id) FROM loans WHERE(application_status != 'disbursed' AND loan_status = 'active');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {result = resultSet.getInt(1);}
            getConnection().close();
            resultSet.close();
            preparedStatement.close();
        }catch (Exception ignored){}
        return result;
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
    public ArrayList<CustomersDataRepository> fetchCustomersData() {
        ArrayList<CustomersDataRepository> data = new ArrayList<>();
        try {
            String query = "SELECT * FROM customer_data AS cd\n" +
                    "INNER JOIN customer_account_data AS cad\n" +
                    "USING(customer_id);";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {

                int id = resultSet.getInt("customer_id");
                String firstname = resultSet.getNString("firstname");
                String lastname = resultSet.getString("lastname");
                String othername = resultSet.getNString("othername");
                String gender = resultSet.getString("gender");
                Date dob = resultSet.getDate("dob");
                double account_balance = resultSet.getDouble("account_balance");
                int age = resultSet.getInt("age");
                String place_of_birth = resultSet.getNString("place_of_birth");
                String  mobile_number = resultSet.getString("mobile_number");
                String other_number = resultSet.getNString("other_number");
                String email = resultSet.getString("email");
                String digital_address = resultSet.getNString("digital_address");
                String residential_address = resultSet.getString("residential_address");
                String key_landmark = resultSet.getNString("key_landmark");
                String marital_status = resultSet.getString("marital_status");
                String name_of_spouse = resultSet.getNString("name_of_spouse");
                String id_type = resultSet.getString("id_type");
                String id_number = resultSet.getNString("id_number");
                String educational_background = resultSet.getString("educational_background");
                String additional_comment = resultSet.getString("additional_comment");
                String contact_person_fullname = resultSet.getNString("contact_person_fullname");
                Date contact_person_dob = resultSet.getDate("contact_person_dob");
                String contact_person_number = resultSet.getNString("contact_person_number");
                String contact_person_gender = resultSet.getString("contact_person_gender");
                String contact_person_landmark = resultSet.getString("contact_person_landmark");
                String contact_person_education_level = resultSet.getNString("contact_person_education_level");
                String contact_person_digital_address = resultSet.getString("contact_person_digital_address");
                String contact_person_id_type = resultSet.getNString("contact_person_id_type");
                String contact_person_id_number = resultSet.getString("contact_person_id_number");
                String contact_person_place_of_work = resultSet.getNString("contact_person_place_of_work");
                String institution_address = resultSet.getString("institution_address");
                String institution_number = resultSet.getString("institution_number");
                String relationship_to_applicant = resultSet.getNString("relationship_to_applicant");
                Timestamp date_created = resultSet.getTimestamp("date_created");
                int created_by = resultSet.getInt("created_by");
                Timestamp date_modified = resultSet.getTimestamp("date_modified");
                int modified_by = resultSet.getInt("modified_by");

                data.add(new CustomersDataRepository(
                        id,firstname, lastname, othername, gender, dob, age, place_of_birth, mobile_number, other_number,
                        email, digital_address, residential_address, key_landmark, marital_status, name_of_spouse, id_type,
                        id_number, educational_background, additional_comment, contact_person_fullname, contact_person_dob, contact_person_number,
                        contact_person_gender, contact_person_landmark, contact_person_education_level, contact_person_digital_address, contact_person_id_type,
                        contact_person_id_number, contact_person_place_of_work, institution_address, institution_number,relationship_to_applicant,date_created,
                        created_by, date_modified, modified_by
                ));
            }
            getConnection().close();
        }catch (Exception ignored) {}return data;
    }
    public ObservableList<CustomerAccountsDataRepository> fetchCustomersAccountData() {
        ObservableList<CustomerAccountsDataRepository> data = FXCollections.observableArrayList();
        try{
            String query = "SELECT * FROM customer_account_data\n" +
                    "CROSS JOIN loans USING(customer_id);";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
//            account_id, customer_id, account_type, account_number, current_balance, previous_balance, date_modified, modified_by
            while (resultSet.next()) {
                int account_id = resultSet.getInt("account_id");
                int customer_id = resultSet.getInt("customer_id");
                String account_type = resultSet.getString("account_type");
                String status = resultSet.getString("account_status");
                String account_number = resultSet.getString("account_number");
                String loanNo = resultSet.getString("loan_no");
                double current_balance = resultSet.getDouble("account_balance");
                double previous_balance = resultSet.getDouble("previous_balance");
                Timestamp date_modified = resultSet.getTimestamp("date_modified");
                int modified_by = resultSet.getInt("modified_by");

                data.add(new CustomerAccountsDataRepository(account_id, customer_id, account_number, loanNo, account_type, status, current_balance, previous_balance, date_modified, modified_by));
            }
        }catch (Exception ignore){}return data;
    }
    public ObservableList<CustomersDocumentRepository> fetchCustomerDocuments() {
        ObservableList<CustomersDocumentRepository> data = FXCollections.observableArrayList();
//        doc_id, customer_id, document_type, document_name, file_content, reason_for_upload, date_uploaded, date_modified, uploaded_by, modified_by
        try {
            String query = "SELECT * FROM customer_document";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int doc_id = resultSet.getInt("doc_id");
                int customer_id  = resultSet.getInt("customer_id");
                String document_type = resultSet.getString("document_type");
                String document_name = resultSet.getString("document_name");
                byte[] document_content = resultSet.getBytes("file_content");
                String reason_for_upload = resultSet.getString("reason_for_upload");
                Timestamp date_uploaded = resultSet.getTimestamp("date_uploaded");
                Timestamp date_modified = resultSet.getTimestamp("date_modified");
                int uploaded_by = resultSet.getInt("uploaded_by");
                int modified_by = resultSet.getInt("modified");
                data.add(new CustomersDocumentRepository(
                        doc_id, customer_id, document_type, document_name, document_content,
                        reason_for_upload, date_uploaded, date_modified, uploaded_by, modified_by
                ));
            }
        }catch (Exception ignore){}return data;
    }

    public ObservableList<UsersData> fetchAllUsers() {
        ObservableList<UsersData> data = new ObservableStack<>();
        try {
            String query= "SELECT * FROM users AS u\n" +
                    "CROSS JOIN roles using(role_id);";
            preparedStatement = getConnection().prepareStatement( query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String empId = resultSet.getString("emp_id");
                String role = resultSet.getString("role_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("user_password");
                byte active = resultSet.getByte("is_active");
                byte deleted = resultSet.getByte("is_deleted");
                Timestamp dateCreated = resultSet.getTimestamp("date_created");
                int addedBy = resultSet.getInt("added_by");
                Timestamp dateModified = resultSet.getTimestamp("date_modified");
                int modifiedBy = resultSet.getInt("modified_by");
                data.add(new UsersData(id, empId, role, username, password, active, deleted,  dateCreated, addedBy, dateModified, modifiedBy));
            }
        }catch(SQLException ignore){}

        return data;
    }
    public ObservableList<UsersData> fetchAssignedUsersOnly() {
        ObservableList<UsersData> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT emp_id, role_name, username, " +
                    "is_active FROM users AS u\n" +
                    "JOIN roles AS r \n" +
                    "ON  u.role_id = r.role_id\n" +
                    "WHERE( is_active = 1 OR is_deleted = 0);";
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
    protected void checkAccountNumberExists(List<String> accountTypes, List<String> idNumbers) {
        try {
            String query2 = "SELECT account_type, id_number FROM customer_account_data AS cad\n" +
                    "JOIN customer_data AS cd ON \n" +
                    "cd.customer_id = cad.customer_id;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query2);
            while (resultSet.next()) {
                accountTypes.add(resultSet.getString("account_type"));
                idNumbers.add(resultSet.getString("id_number"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int createAccount(CustomerAccountsDataRepository balanceDataModel) {
        int flag = 0;
        try {
            String query = "INSERT INTO customer_account_data(customer_id, account_type, account_number, account_balance, previous_balance, modified_by) VALUES(?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, balanceDataModel.getCustomer_id());
            preparedStatement.setString(2, balanceDataModel.getAccount_type());
            preparedStatement.setString(3, balanceDataModel.getAccount_number());
            preparedStatement.setDouble(4, balanceDataModel.getAccount_balance());
            preparedStatement.setDouble(5, balanceDataModel.getPrevious_balance());
            preparedStatement.setDouble(6, balanceDataModel.getModified_by());
            flag = preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            rollBack();
        }
        return flag;
    };
    public ArrayList<TemplatesRepository> fetchMessageTemplates() {
        ArrayList<TemplatesRepository> data = new ArrayList<>();
        try {
            String query = "SELECT * FROM message_templates;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("message_id");//0
                String title = resultSet.getString("title");//1
                String message = resultSet.getString("message");//2
                Timestamp date = resultSet.getTimestamp("date_modified");//3
                int userId = resultSet.getInt("modified_by");//4
                data.add(new TemplatesRepository(id, title, message, userId, date));
            }

        }catch (SQLException ignore){}

        return data;
    }
    public ObservableList<String> getMessageOperations() {
        ObservableList<String> data = FXCollections.observableArrayList();
        try  {
            String query = "SELECT * FROM system_operations;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                data.add(resultSet.getString("operation_type"));
            }
            statement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return data;
    }
    public ObservableList<TransactionsEntity> fetchTransactionLogs(int limitValue) {
        ObservableList<TransactionsEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT tl.id, transaction_id, payment_method,  cad.account_number, concat(firstname, ' ', lastname, ' ', othername) AS fullname,\n" +
                    "transaction_type, (cash_amount + ecash_amount) as amount, ecash_id, \n" +
                    "transaction_made_by AS 'made_by', payment_gateway, national_id_number, transaction_date, username  \n" +
                    "FROM customer_data AS cd\n" +
                    "INNER JOIN customer_account_data AS cad \n" +
                    "ON  cd.customer_id = cad.customer_id\n" +
                    "INNER JOIN transaction_logs AS tl ON \n" +
                    "cad.account_number = tl.account_number \n" +
                    "INNER JOIN USERS AS u ON \n" +
                    "tl.user_id = u.user_id\n" +
                    "ORDER BY transaction_id DESC LIMIT ?;\n";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, limitValue);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String fullName = resultSet.getString("fullname");
                    String transId = resultSet.getString("transaction_id");
                    String paymentMtd = resultSet.getString("payment_method");
                    String accountNo = resultSet.getString("cad.account_number");
                    String transType = resultSet.getString("transaction_type");
                    double amount = resultSet.getDouble("amount");
                    String madeBy = resultSet.getString("made_by");
                    Timestamp transDate = resultSet.getTimestamp("transaction_date");
                    String username = resultSet.getString("username");
                    String nationalIdNo = resultSet.getString("national_id_number");
                    String paymentGway = resultSet.getString("payment_gateway");
                    String ecashId = resultSet.getString("ecash_id");
                    data.add(new TransactionsEntity(id, fullName , accountNo, transId, transType, paymentMtd, paymentGway,  amount, ecashId,  transDate, madeBy, nationalIdNo, username));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (Exception e) {}
        return data;
    }
    public ObservableList<TransactionsEntity> fetchSimpleTransactionLogReport(LocalDate start, LocalDate end){
        ObservableList<TransactionsEntity> data = new ObservableStack<>();
        try {
            String query1 = """
                    SELECT id,
                    account_number,\s
                    transaction_id,\s
                    transaction_type,\s
                    payment_method,\s
                    payment_gateway,\s
                    cash_amount,\s
                    ecash_amount,
                    ecash_id,\s
                     transaction_date,
                     transaction_made_by,
                     username FROM transaction_logs AS tl\s
                    	INNER JOIN users AS u\s
                        ON u.user_id = tl.user_id
                    	WHERE (DATE(transaction_date) BETWEEN ? AND ? );
                    """;
                preparedStatement = getConnection().prepareStatement(query1);
                preparedStatement.setDate(1, Date.valueOf(start));
                preparedStatement.setDate(2, Date.valueOf(end));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String accountNo = resultSet.getString("account_number");
                    String transNo = resultSet.getString("transaction_id");
                    String type = resultSet.getString("transaction_type");
                    String method = resultSet.getString("payment_method");
                    String gateway = resultSet.getString("payment_gateway");
                    double cash = resultSet.getDouble("cash_amount");
                    double eCash = resultSet.getDouble("ecash_amount");
                    String ecashId = resultSet.getString("ecash_id");
                    Timestamp date = resultSet.getTimestamp("transaction_date");
                    String paidBy = resultSet.getString("transaction_made_by");
                    String username = resultSet.getString("username");
                    data.add(new TransactionsEntity(id, accountNo, transNo, type, method, gateway, cash, eCash, ecashId, date, paidBy, username ));
                }
        }catch (SQLException ignore){
        }
        return data;
    }
    public ObservableList<TransactionsEntity> fetchDetailedTransactionLogReport(LocalDate start, LocalDate end, String value){
        ObservableList<TransactionsEntity> data = new ObservableStack<>();
        try {
            String query2 = """
                    SELECT id, account_number,\s
                    transaction_id,\s
                    transaction_type,\s
                    payment_method,\s
                    payment_gateway,\s
                    cash_amount,\s
                    ecash_amount,
                    ecash_id,\s
                     transaction_date,
                     transaction_made_by,
                     username FROM transaction_logs AS tl\s
                    	INNER JOIN users AS u\s
                        ON u.user_id = tl.user_id
                    	WHERE (
                    			DATE(transaction_date) BETWEEN ? AND ? AND
                                (transaction_type = ? OR transaction_id LIKE ? OR account_number LIKE ? OR  username = ?)
                        );
                    """;
                preparedStatement = getConnection().prepareStatement(query2);
                preparedStatement.setDate(1, Date.valueOf(start));
                preparedStatement.setDate(2, Date.valueOf(end));
                preparedStatement.setString(3, value);
                preparedStatement.setString(4, value);
                preparedStatement.setString(5, value);
                preparedStatement.setString(6, value);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String accountNo = resultSet.getString("account_number");
                    String transNo = resultSet.getString("transaction_id");
                    String type = resultSet.getString("transaction_type");
                    String method = resultSet.getString("payment_method");
                    String gateway = resultSet.getString("payment_gateway");
                    double cash = resultSet.getDouble("cash_amount");
                    double eCash = resultSet.getDouble("ecash_amount");
                    String ecashId = resultSet.getString("ecash_id");
                    Timestamp date = resultSet.getTimestamp("transaction_date");
                    String paidBy = resultSet.getString("transaction_made_by");
                    String username = resultSet.getString("username");
                    data.add(new TransactionsEntity(id, accountNo, transNo, type, method, gateway, cash, eCash, ecashId, date, paidBy, username ));
            }
                getConnection().close();
        }catch (SQLException ignore){}
        return data;
    }
    public ObservableList<TransactionsEntity> getTodayTransactionLogs() {
        ObservableList<TransactionsEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT transaction_id, transaction_type, (cash_amount + ecash_amount) AS amount, \n" +
                    "TIME(transaction_date) AS `time` FROM transaction_logs WHERE DATE(transaction_date) = current_date()" +
                    "ORDER BY transaction_id DESC;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String transactionId  = resultSet.getString("transaction_id");
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                LocalTime localTime = resultSet.getTime("time").toLocalTime();
                data.add(new TransactionsEntity(transactionId, transactionType, amount, localTime));
            }
            resultSet.close();
            preparedStatement.close();
            getConnection().close();
        }catch (Exception ignore){}
        return data;
    }

    public ArrayList<MessageOperationsEntity> getMessageWithOperations() {
        ArrayList<MessageOperationsEntity> data = new ArrayList<>();
        try {
            String query = "SELECT id, title, message, operation_type FROM message_templates AS mt\n" +
                    "JOIN system_operations AS mo \n" +
                    "ON mt.message_id = mo.template_id;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String message = resultSet.getString("message");
                String operation = resultSet.getString("operation_type");
                data.add(new MessageOperationsEntity(id,operation, title, message));
            }
            resultSet.close();
            statement.close();
            getConnection().close();
        }catch (SQLException ignore){}

        return data;
    }

    public ObservableList<ScheduleTableValues> getLoanScheduleByLoanNo(String loanNo) {
        ObservableList<ScheduleTableValues> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM loan_schedule WHERE(loan_no = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, loanNo);
            resultSet = preparedStatement.executeQuery();
            int count = 1;
            while (resultSet.next()) {
                 double installment = resultSet.getDouble("monthly_installment");
                 double principal = resultSet.getDouble("principal_amount");
                 double interest = resultSet.getDouble("interest_amount");
                 LocalDate date = resultSet.getDate("payment_date").toLocalDate();
                 double balance = resultSet.getDouble("balance");
                 long scheduleId = resultSet.getLong("schedule_id");
                 data.add(new ScheduleTableValues( count ++, scheduleId, principal, interest, installment, balance, date));
            }
        }catch (SQLException ignore){}

        return data;
    }
    protected Map<String, String> getApplicantNameAndNumberByLoanId(String loanNo) {
        Map<String, String> data = new HashMap<>();
        try {
            String query = "SELECT concat(firstname, ' ', lastname) AS fullname, mobile_number FROM customer_data AS cd\n" +
                    "INNER JOIN loans AS ln ON \n" +
                    "cd.customer_id = ln.customer_id\n" +
                    "WHERE(loan_no = '"+loanNo+"');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                data.put("name", resultSet.getString("fullname"));
                data.put("number", resultSet.getString("mobile_number"));
            }
        }catch (SQLException e){e.printStackTrace();}
        return data;
    }
    protected void logNotification(NotificationEntity items) {
        try {
            String query = "INSERT INTO notifications(title, sender_method, message, logged_by) \n" +
                    "VALUES(?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, items.getTitle());
            preparedStatement.setString(2, items.getSender_method());
            preparedStatement.setString(3, items.getMessage());
            preparedStatement.setInt(4, items.getLogged_by());
            preparedStatement.execute();
            preparedStatement.close();
            getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
            rollBack();
        }
    }
    public int countUnpaidLoans() {
        int value = 0;
        try {
            preparedStatement = getConnection().prepareStatement("SELECT COUNT(*) FROM loans WHERE application_status = 'pending_payment';");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                value = resultSet.getInt(1);
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore){}

        return value;
    }

    public ObservableList<DisbursementEntity> getUnpaidLoans() {
        ObservableList<DisbursementEntity> data = new ObservableStack<>();
        try {
            String query = """
                    SELECT loan_id, loans.loan_no, mobile_number, account_number, processing_rate,
                           approved_amount, account_balance, previous_balance, application_status FROM loans
                           JOIN customer_account_data AS cad USING(customer_id)
                           INNER JOIN customer_data USING(customer_id)
                           INNER JOIN loan_qualification_values USING(loan_no)
                           WHERE(application_status = 'pending_payment');
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("loan_id");
                String no = resultSet.getString("loan_no");
                String accountNo = resultSet.getString("account_number");
                String mobileNumber = resultSet.getString("mobile_number");
                double amount = resultSet.getDouble("approved_amount");
                double accountBalance = resultSet.getDouble("account_balance");
                double preBalance = resultSet.getDouble("previous_balance");
                int processingRate = resultSet.getInt("processing_rate");
                String status = resultSet.getString("application_status");
                data.add(new DisbursementEntity(id, no, accountNo, amount, processingRate, accountBalance, preBalance, status, mobileNumber));
            }
        }catch (SQLException ignore){}
        return data;
    }
    public ObservableList<String> getDisbursedLoanNumbers() {
        ObservableList<String > data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_no FROM loans WHERE((application_status = 'disbursed' OR 'pending_payment') AND loan_status = 'active');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                data.add(resultSet.getString(1));
            }
        }catch (SQLException ignore){}
        return data;
    }
    public ObservableList<LoanScheduleEntity> getRepaymentSchedule(String loanNo) {
        LoanScheduleEntity entity = new LoanScheduleEntity();
        ObservableList<LoanScheduleEntity> data = FXCollections.observableArrayList();
        try {
//            String query = """
//                    SELECT schedule_id,
//                    monthly_installment,
//                    principal_amount,
//                    interest_amount,
//                    payment_date,
//                    penalty_amount,
//                    	(SELECT SUM(paid_amount) FROM loan_payment_logs
//                    	WHERE( installment_month = payment_date)) AS monthly_payment
//                    FROM loan_schedule AS ls
//                    WHERE loan_no = ?;
//                    """;

            String query = """
                    SELECT schedule_id, monthly_installment, principal_amount,
                                                 interest_amount,payment_date,penalty_amount,
                                          (SELECT SUM(paid_amount) FROM loan_payment_logs
                                          WHERE( installment_month = payment_date AND loan_no = ?)) AS monthly_payment
                                          FROM loan_schedule AS ls WHERE loan_no = ?;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, loanNo);
            preparedStatement.setString(2, loanNo);
            resultSet = preparedStatement.executeQuery();
            long index = 1;
            while(resultSet.next()) {
                entity.setSchedule_id(index++);
                entity.setMonthly_installment(resultSet.getDouble("monthly_installment"));
                entity.setPrincipal_amount(resultSet.getDouble("principal_amount"));
                entity.setInterest_amount(resultSet.getDouble("interest_amount"));
                entity.setPayment_date(resultSet.getDate("payment_date").toLocalDate());
                entity.setPenalty_amount(resultSet.getDouble("penalty_amount"));
                entity.setMonthly_payment(resultSet.getDouble("monthly_payment"));
                data.add(
                        new LoanScheduleEntity(
                                entity.getSchedule_id(), entity.getMonthly_installment(), entity.getPrincipal_amount(),
                                entity.getInterest_amount(), entity.getPayment_date(), entity.getPenalty_amount(), entity.getMonthly_payment())
                );
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            logger.logMessage(ex.fillInStackTrace().toString(), "getRepaymentSchedule", className);
        }
        return data;
    }


    /*
     This method when invoked, shall return all loan payments made by a client taking the loan number as a parameter.
     */
    public double getLoanTotalRepaymentAmount(String loanNo){

        double value = 0;
        try{
            String query = "SELECT SUM(paid_amount) AS total_payments FROM loan_payment_logs WHERE loan_no ='"+loanNo+"'";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                value = resultSet.getInt(1);
            }
        }catch (SQLException ex){
            logger.logMessage(ex.getMessage(), "getLoanTotalRepaymentAmount" , className);
        }
        return value;
    }
    
    public void updateAllLoanStatus(String loanNo) {
        try {
            String query = """
                    UPDATE loans SET loan_status = 'cleared' WHERE(
                    	(SELECT SUM(paid_amount) AS total_payments FROM loan_payment_logs WHERE loan_no = ?)
                    >= repayment_amount AND loan_status != 'terminated')
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, loanNo);
            preparedStatement.execute();
            preparedStatement.close();
            getConnection().close();
        } catch (SQLException ignore) {
        }
    }

    public ObservableList<LoansEntity> fetchAllLoans() {
        ObservableList<LoansEntity> data = new ObservableStack<>();
        try {
            String query = """
                    SELECT CONCAT(firstname, ' ', othername, ' ', lastname) AS fullname,
                    loan_id, loan_no, loan_type, requested_amount, approved_amount, repayment_amount,
                    total_payment, application_status, loan_purpose, loan_status,
                    is_drafted, ln.date_created,
                    ln.date_modified, ln.created_by, ln.updated_by, ln.approved_by FROM loans AS ln
                    INNER JOIN customer_data AS cd USING(customer_id);
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String name = resultSet.getString("fullname");
                int loan_id = resultSet.getInt("loan_id");
                String loan_no = resultSet.getString("loan_no");
                String loan_type = resultSet.getString("loan_type");
                double requested_amount = resultSet.getDouble("requested_amount");
                double approved_amount = resultSet.getDouble("approved_amount");
                byte is_drafted = resultSet.getByte("is_drafted");
                double total_payment = resultSet.getDouble("total_payment");
                String application_status = resultSet.getString("application_status");
                String loan_purpose = resultSet.getString("loan_purpose");
                String loan_status = resultSet.getString("loan_status");
                Timestamp date_modified = resultSet.getTimestamp("ln.date_modified");
                Timestamp date_created = resultSet.getTimestamp("ln.date_created");
                int created_by = resultSet.getInt("ln.created_by"   );
                int updated_by = resultSet.getInt("ln.updated_by");
                int approved_by = resultSet.getInt("ln.approved_by");
                data.add(new LoansEntity(loan_id, name, loan_no, loan_type, requested_amount, approved_amount, total_payment,
                        application_status, loan_purpose, loan_status, is_drafted, date_created, date_modified, created_by, updated_by, approved_by));
            }
        }catch (SQLException ignore){}
        return data;
    }

    public ObservableList<CollectionSheetEntity> fetchCollectionSheetData(String officerId){
        ObservableList<CollectionSheetEntity> data = FXCollections.observableArrayList();
        try {
            String query = """
                    SELECT\s
                    	DISTINCT(monthly_installment) as installment, ls.loan_id, CONCAT(e.lastname, ' ', e.firstname) AS officer,\s
                    	ls.loan_no, \s
                    	CONCAT(cd.lastname, ' ', cd.othername, ' ',cd.firstname) AS fullname FROM group_supervisors AS gs
                    INNER JOIN employees AS e
                    	ON gs.emp_id = work_id
                    INNER JOIN loans AS ls\s
                    	ON gs.loan_id = ls.loan_no
                    INNER JOIN loan_schedule AS sch
                    	ON ls.loan_no = sch.loan_no
                    INNER JOIN customer_data AS cd\s
                    	ON ls.customer_id = cd.customer_id
                    WHERE loan_status = 'active' AND work_id = ?
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, officerId);
            resultSet = preparedStatement.executeQuery();
            int id = 1;
            while(resultSet.next()){
                String officer = resultSet.getString("officer");
                String fullname = resultSet.getString("fullname");
                String loanNo = resultSet.getString("ls.loan_no");
                double installment = resultSet.getDouble("installment");
                data.add(new CollectionSheetEntity(id++, loanNo, fullname, officer, installment));
            }
        }catch (SQLException e){e.printStackTrace();}
        return data;
    }

    protected ObservableList<LoansEntity> getClearedAndTerminatedLoans() {
        ObservableList<LoansEntity> data = new ObservableStack<>();
        try {
            String query = "SELECT loan_no, loan_status, repayment_amount, SUM(paid_amount) AS total_payment , DATE(date_modified) AS modified_date\n" +
                    "FROM loan_payment_logs \n" +
                    "INNER JOIN loans AS ln USING(loan_no)\n" +
                    "WHERE loan_status = 'cleared' OR loan_status = 'terminated'\n" +
                    "GROUP BY loan_no, loan_status, repayment_amount, modified_date;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            AtomicInteger index = new AtomicInteger();
            while(resultSet.next()) {
                String loanNo = resultSet.getString("loan_no");
                double disbursedAmount = resultSet.getDouble("repayment_amount");
                double totalPayment = resultSet.getDouble("total_payment");
                String loanStatus = resultSet.getString("loan_status");
                Date modifiedDate = resultSet.getDate("modified_date");
                data.add(new LoansEntity(index.incrementAndGet(), loanNo, loanStatus, disbursedAmount, totalPayment, modifiedDate));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return data;
    }

    public ObservableList<LoanPaymentLogsEntity> getLoanPaymentLogs(String loanNo) {
        ObservableList<LoanPaymentLogsEntity> data = new ObservableStack<>();
        try {
//            String query = "SELECT purpose, installment_month, paid_amount, write_off, date_collected, collected_by\n" +
//                    "\t\tFROM loan_payment_logs AS pl\n" +
//                    "        INNER JOIN terminated_loans AS tl\n" +
//                    "        USING(loan_no)\n" +
//                    "        WHERE tl.loan_no = '"+loanNo+"';";

            String query = "SELECT installment_month, paid_amount, \n" +
                    "\t(SELECT purpose FROM terminated_loans WHERE loan_no = '"+loanNo+"') AS purpose,\n" +
                    "    (SELECT write_off FROM terminated_loans WHERE loan_no = '"+loanNo+"') AS write_off,\n" +
                    "\tdate_collected, collected_by\n" +
                    "\tFROM loan_payment_logs AS pl\n" +
                    "WHERE loan_no = '"+loanNo+"';";

            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            AtomicInteger index = new AtomicInteger();
            while(resultSet.next()) {
                String purpose = resultSet.getString("purpose");
                Date installmentDate = resultSet.getDate("installment_month");
                double amount = resultSet.getDouble("paid_amount");
                double writeOff = resultSet.getDouble("write_off");
                Timestamp dateCollected = resultSet.getTimestamp("date_collected");
                data.add(new LoanPaymentLogsEntity(index.incrementAndGet(), purpose, installmentDate, amount, writeOff, dateCollected));
            }
            resultSet.close();
            preparedStatement.close();
            getConnection().close();
        }catch (SQLException ex){
            logger.logMessage(Arrays.toString(ex.getStackTrace()), "getLoanPaymentLogs", className);
        }
        return data;
    }

    public Map<String, Object> getBusinessAccountInformation() {
        Map<String, Object> data = new HashMap<>();
        try {
            String query = "SELECT account_password, account_balance, previous_balance, account_date_modified\n" +
                    "FROM business_info;";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.executeQuery();
            resultSet = preparedStatement.getResultSet();
            if (resultSet.next()){
                data.put("password", resultSet.getString("account_password"));
                data.put("accountBalance", resultSet.getDouble("account_balance"));
                data.put("previousBalance", resultSet.getDouble("previous_balance"));
                data.put("date_modified", resultSet.getTimestamp("account_date_modified"));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore){}
        return data;
    }

    public ObservableList<BusinessTransactionLogs> getBusinessTransactionLogs() {
        ObservableList<BusinessTransactionLogs> data = new ObservableStack<>();
        try{
            String query = "SELECT id, transaction_type, bank_name, amount, transaction_id, account_number, \n" +
                    "transaction_date, notes, username, btl.date_created\n" +
                    "FROM business_transaction_logs AS btl\n" +
                    "INNER JOIN users AS u \n" +
                    "ON created_by = user_id LIMIT 50";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String type = resultSet.getString("transaction_type");
                String bank = resultSet.getNString("bank_name");
                String note = resultSet.getString("notes");
                String transId = resultSet.getString("transaction_id");
                String accountNum = resultSet.getString("account_number");
                Date transDate = resultSet.getDate("transaction_date");
                double amount = resultSet.getDouble("amount");
                String username = resultSet.getString("username");
                Timestamp dateCreated = resultSet.getTimestamp("btl.date_created");
                data.add(new BusinessTransactionLogs(id, type, bank, amount, transId,accountNum, username, transDate, note, dateCreated));
            }
        }catch (SQLException ignore){}

        return data;
    }

    public Map<String, Object> getRevenueAccountDetails() {
        Map<String, Object> data = new HashMap<>();
        try{
            String query = "SELECT * FROM revenue_account";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                data.put("accountId", resultSet.getInt("id"));
                data.put("balance", resultSet.getDouble("account_balance"));
                data.put("userId", resultSet.getInt("updated_by"));
                data.put("date", resultSet.getTimestamp("date_updated"));
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException e){}
        return data;
    }

    public Map<String, List<Object>> getTemporalCashierTableData() {
        Map<String, List<Object>> data = new HashMap<>();
        try{
            String query = "SELECT * FROM temporal_cashier_account";
            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()){
                String tellerNames = resultSet.getString("teller");
                List<Object> tableData = new ArrayList<>();
                tableData.add(resultSet.getInt("id"));//0
                tableData.add(resultSet.getDouble("amount"));//1
                tableData.add(resultSet.getTimestamp("entry_date"));//2
                data.put(tellerNames, tableData);
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore){}
        return data;
    }

    public List<String> getCashierNamesWithUnclosedAccountStatus() {
        List<String> data = new ArrayList<>();
        try{
            String query = "Select username FROM closed_teller_transaction_logs AS cttl\n" +
                    "INNER JOIN users AS u\n" +
                    "ON u.user_id = cttl.entered_by\n" +
                    "WHERE Date(entry_date) = current_date() AND is_closed = 0 AND is_suspended = 0;";
            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()){
                data.add(resultSet.getString("username"));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore){}
        return data;
    }

    public long getClosureTableIdByCashierId(int cashierId) {
        try {
            String query = "SELECT id FROM closed_teller_transaction_logs \n" +
                    "WHERE entered_by = '"+cashierId+"' AND DATE(entry_date) = CURRENT_DATE()";
            resultSet = getConnection().createStatement().executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return 0;
    }

    public Map<String, List<Double>> getCashierCurrentAndLoadedBalance() {
        Map<String, List<Double>> data = new HashMap<>();
        try{
            String query = "SELECT transferred_to AS `name`, SUM(dtl.amount) AS 'loaded_amount', tca.amount AS 'current_amount'\n" +
                    "FROM domestic_transfer_logs AS dtl\n" +
                    "INNER JOIN temporal_cashier_account as tca\n" +
                    "ON transferred_to = teller\n" +
                    "WHERE DATE(dtl.entry_date) = CURRENT_DATE() GROUP BY `name`;";
            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()){
                String tellerNames = resultSet.getString("name");
                List<Double> tableData = new ArrayList<>();
                tableData.add(resultSet.getDouble("loaded_amount"));//0
                tableData.add(resultSet.getDouble("current_amount"));//1
                data.put(tellerNames, tableData);
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore){}
        return data;
    }

    public ObservableList<DomesticTransactionLogsEntity> getAllInternalTransfersToCashier() {
        ObservableList<DomesticTransactionLogsEntity> data = new ObservableStack<>();
        try{
            String query = "SELECT *, TIME(entry_date) AS time FROM domestic_transfer_logs WHERE DATE(entry_date) = CURRENT_DATE();";
            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String type = resultSet.getString("transfer_type");
                String to = resultSet.getString("transferred_to");
                double amount = resultSet.getDouble("amount");
                int enteredBy = resultSet.getInt("entered_by");
                Timestamp entry_date = resultSet.getTimestamp("entry_date");
                LocalTime time = resultSet.getTime("time").toLocalTime();
//                double cash = resultSet.getDouble("cash");
//                double ecash = resultSet.getDouble("e_cash");
                data.add(new DomesticTransactionLogsEntity(id, type, to, amount,  enteredBy, entry_date, time));
            }
            resultSet.close();
            getConnection().close();
        }catch(SQLException ignore){ignore.printStackTrace();}
        return data;
    }

    public ObservableList<TransactionsEntity> fetchTodayTransactionByCashierName(String name) {
        ObservableList<TransactionsEntity> data = new ObservableStack<>();
        try {
            String query = "SELECT id, transaction_id, transaction_type, payment_method, \n" +
                    "SUM(ecash_amount + cash_amount) AS total, Time(transaction_date) AS `time`, username FROM \n" +
                    "transaction_logs AS tlogs\n" +
                    "INNER JOIN users AS u \n" +
                    "USING(user_id)\n" +
                    "WHERE(DATE(transaction_date) = CURRENT_DATE()  AND username = ? ) GROUP BY id;";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                long id = resultSet.getInt("id");
                String transId = resultSet.getString("transaction_id");
                String type = resultSet.getString("transaction_type");
                String method = resultSet.getString("payment_method");
                double total = resultSet.getDouble("total");
                LocalTime time = resultSet.getTime("time").toLocalTime();
                data.add(new TransactionsEntity(id, transId, type, method, total, time));
            }
        }catch (SQLException ignore){}
        return data;
    }

    //This method when invoked shall check if cashier closed their account for the day or not based on the
    //current date.
    public boolean isCashierTransactionClosed(Date currentDate, int cashierId) {
        try {
            String query = "SELECT Date(entry_date) as `Entry Date`" +
                    "FROM closed_teller_transaction_logs " +
                    "WHERE (DATE(entry_date) = '"+currentDate+"' AND entered_by = '"+cashierId+"')";
             resultSet = getConnection().createStatement().executeQuery(query);
             if (resultSet.next()) {
                 return resultSet.getDate("Entry Date").toLocalDate().equals(LocalDate.now());
             }
             resultSet.close();
             getConnection().close();
        }catch (SQLException ignore) {}
        return false;
    }

    public ObservableList<ClosedTellerTransactionEntity> getClosedCashierSummeryIdByCashierId() {
        ObservableList<ClosedTellerTransactionEntity> data = new ObservableStack<>();
        try {
            String query = "";
            resultSet = getConnection().createStatement().executeQuery(query);
        }catch (SQLException ignore){}

        return data;
    }

//    public int insertImage(String fileName, byte[] content) {
//        try{
//            preparedStatement = getConnection().prepareStatement("INSERT INTO image_table \n" +
//                    "VALUES(DEFAULT, ?,?)");
//            preparedStatement.setString(1, fileName);
//            preparedStatement.setBytes(2, content);
//            return preparedStatement.executeUpdate();
//        }catch (SQLException e) {e.printStackTrace();}
//        return 0;
//    }
//
//    public List<Object> retrieveImage(int id) {
//        List<Object>data = new ArrayList<>();
//        try{
//            resultSet = getConnection().prepareStatement("SELECT * FROM image_table WHERE id = '"+id+"'").executeQuery();
//            if (resultSet.next()) {
//                String name = resultSet.getString(2);
//                byte[] content = resultSet.getBytes(3);
//                data.add(name);//0
//                data.add(content);//1
//            }
//        }catch (Exception e){e.printStackTrace();}
//        return data;
//    }

   public ObservableList<AssignedSupervisors> getAllAssignedSupervisors(String supervisorName){
        ObservableList<AssignedSupervisors> data = new ObservableStack<>();
        try{
            String query = """
                    SELECT
                    	CONCAT(cd.firstname, ' ', cd.lastname, ' ', cd.othername) AS 'customer' ,\s
                    	cd.mobile_number, gs.loan_id AS `loan_number`, application_status,
                    		repayment_amount, total_payment
                    		FROM employees AS emp
                    		INNER JOIN group_supervisors AS gs
                    		ON emp.work_id = gs.emp_id
                    		INNER JOIN loans AS ln
                    		ON gs.loan_id = ln.loan_no
                    		INNER JOIN users AS u
                    		ON emp.work_id = u.emp_id
                    		INNER JOIN customer_data AS cd
                    		USING(customer_id)
                        WHERE loan_status = 'active' AND username = ?;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, supervisorName);
            resultSet = preparedStatement.executeQuery();
            AtomicInteger counter = new AtomicInteger(0);
            while (resultSet.next()) {
                String name = resultSet.getString("customer");
                String number = resultSet.getString("cd.mobile_number");
                String loanNumber = resultSet.getString("loan_number");
                String status = resultSet.getString("application_status");
                double repayment = resultSet.getDouble("repayment_amount");
                double total = resultSet.getDouble("total_payment");
                data.add(new AssignedSupervisors(counter.incrementAndGet(), name, number, loanNumber, status,repayment, total));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignored){}
        return data;
    }

    public ObservableList<NotificationEntity> getAllNotifications(int limit) {
       ObservableList<NotificationEntity> data = new ObservableStack<>();
       try {
           String query = """
                   SELECT id, title, sender_method, message, logged_date, `read`, username FROM notifications AS n
                   INNER JOIN users AS u
                   ON u.user_id = n.logged_by ORDER BY Id DESC LIMIT ?;
                  
                   """;
           preparedStatement = getConnection().prepareStatement(query);
           preparedStatement.setInt(1, limit);

           resultSet = preparedStatement.executeQuery();
           while(resultSet.next()) {
               long id = resultSet.getLong("id");
               String title = resultSet.getNString("title");
               String sender_method = resultSet.getString("sender_method");
               String message = resultSet.getNString("message");
               Timestamp logged_date = resultSet.getTimestamp("logged_date");
               boolean read = resultSet.getBoolean("read");
               UsersData username = new UsersData();
               username.setUsername(resultSet.getString("username"));
               data.add(new NotificationEntity(id, title, sender_method, message, logged_date, read, username));
           }

       }catch (SQLException e){
           logger.logMessage(e.getMessage(), "getAllNotifications", className);
       }
       return data;
    }

    public ObservableList<LoanAgentsEntity> getAllAgents() {
        ObservableList<LoanAgentsEntity> data = new ObservableStack<>();
        try {
            String query = """
                    SELECT a.agent_id, agent_name, mobile_number, other_number, information,  \s
                    (SELECT COUNT(agent_id) FROM customer_data AS cd WHERE(cd.agent_id = a.agent_id)) AS counts,\s
                    date_modified, added_by, DATE(date_joined) as date_joined
                    FROM agents AS a WHERE(is_deleted = FALSE);
                    """;
            resultSet = getConnection().prepareStatement(query).executeQuery();
            while(resultSet.next()){
//                agent_id, agent_name, mobile_number, other_number, information, date_joined, date_modified, added_by, is_deleted
                int id = resultSet.getInt("a.agent_id");
                String name = resultSet.getString("agent_name");
                String number = resultSet.getString("mobile_number");
                String other = resultSet.getNString("other_number");
                String information = resultSet.getNString("information");
                String date = DateFormat.getDateInstance().format(resultSet.getDate("date_joined"));
                Timestamp modified = resultSet.getTimestamp("date_modified");
                int userId = resultSet.getInt("added_by");
                int counts = resultSet.getInt("counts");
                data.add(new LoanAgentsEntity(id, userId, name, number, other, information, date, counts));
            }
        }catch (Exception e){
            logger.logMessage(e.getMessage(), "getAllAgents", className);
        }
        return data;
    }

    public ObservableList<PermissionsEntity> getAppModules() {
        ObservableList<PermissionsEntity> data = new ObservableStack<>();
        try {
            String query = "SELECT * FROM finsuit.modules;";
//            id, module_name, alias, descriptions, is_active
            resultSet = getConnection().createStatement().executeQuery(query);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("module_name");
                String desc = resultSet.getString("descriptions");
                String alias = resultSet.getString("alias");
                boolean status = resultSet.getBoolean("is_active");
                data.add(new PermissionsEntity(id, name, alias, desc, status));
            }
        }catch (SQLException ignore){}
        return data;
    }

    public ObservableList<PermissionsEntity> getAllPermissions() {
        ObservableList<PermissionsEntity> data = new ObservableStack<>();
        try {
            String query = "SELECT * FROM finsuit.permissions ORDER BY operation_id ASC;";
            resultSet = getConnection().createStatement().executeQuery(query);
            while(resultSet.next()) {
//              operation_id, module_id, operation_name, alias, description, is_active
                int id = resultSet.getInt("operation_id");
                int module_id = resultSet.getInt("module_id");
                String name = resultSet.getString("operation_name");
                String alias = resultSet.getString("alias");
                String desc = resultSet.getString("description");
                boolean status = resultSet.getBoolean("is_active");
                data.add(new PermissionsEntity(id, module_id, name,alias, desc, status));
            }
        }catch (SQLException ignore){}
        return data;
    }

    public ObservableList<PermissionsEntity> getModuleControlList(int getRoleId) {
        ObservableList<PermissionsEntity> data = new ObservableStack<>();
        try {
            String query = "SELECT * FROM module_control WHERE role_id = '"+getRoleId+"'";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int role = resultSet.getInt("role_id");
                String name = resultSet.getString("module_name");
                boolean status = resultSet.getBoolean("is_allowed");
                data.add(new PermissionsEntity(role, name, status));
            }
        }catch (SQLException ignore){}
        return data;
    }

    public ObservableList<PermissionsEntity> getAccessControlList(int getRoleId) {
        ObservableList<PermissionsEntity> data = new ObservableStack<>();
        try {
            String query = "SELECT * FROM access_control WHERE role_id = '"+getRoleId+"';";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int role = resultSet.getInt("role_id");
                int permissionId = resultSet.getInt("permission_id");
                boolean allowed = resultSet.getBoolean("is_allowed");
                data.add(new PermissionsEntity(role, permissionId, allowed));
            }
        }catch (SQLException ignore){}
        return data;
    }


}//END OF CLASS...
