package app.models;

import app.config.db.DbConnection;
import app.repositories.BusinessInfoEntity;
import app.repositories.SmsAPIEntity;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.accounts.CustomersDocumentRepository;
import app.repositories.human_resources.EmployeesData;
import app.repositories.roles.UserRolesData;
import app.repositories.settings.TemplatesRepository;
import app.repositories.transactions.TransactionsEntity;
import app.repositories.users.UsersData;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainModel extends DbConnection {
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
                String logo = resultSet.getString("logoPath");//7
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
        }catch (Exception e) {e.printStackTrace();}
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
        }catch (SQLException e) {e.printStackTrace();}
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
        }catch (Exception e){e.printStackTrace();}
        return "not found";
    }
    protected ArrayList<Object> getCustomerFullNameByAccountNumber(String accountNumber) {
        ArrayList<Object> data = new ArrayList<>();
        try {
            String query = "SELECT concat(firstname, ' ', lastname, ' ', othername) AS fullname, account_number, account_balance, cd.customer_id AS accountNo FROM customer_data AS cd\n" +
                    "JOIN customer_account_data AS cad ON \n" +
                    "cd.customer_id = cad.customer_id\n" +
                    "WHERE(cad.account_number = ? OR mobile_number = ?);";

            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, accountNumber);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                data.add(resultSet.getString("fullname"));//0
                data.add(resultSet.getInt("accountNo"));//1
                data.add(resultSet.getDouble("account_balance"));//2
                data.add(resultSet.getString("account_number"));//3
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException e){e.printStackTrace();}
        return data;
    }
    public long totalCustomersCount() {
        long count = 0;
        try {
            String query = "SELECT customer_id from customer_data order by customer_id desc limit 1;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            preparedStatement.close();
            resultSet.close();
            getConnection().close();
        }catch (SQLException e) {e.printStackTrace();}
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
    public int getTotalTransactionsForToday() {
        int result = 0;
        try {
            String query = "SELECT COUNT(*) count FROM transaction_logs WHERE(DATE(transaction_date) = CURRENT_DATE());";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result =  resultSet.getInt("count");
            }
        }catch (Exception ignored){}
        return result;
    }
    public String getLastCustomerAccountNumber() {
        try {
            String query = "SELECT MAX(account_number) AS result FROM customer_account_data ";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getString("result");
            }
        }catch (Exception ignore) {}
        return null;
    }
    public long getTotalLoanCount() {
        try {
            String query = "SELECT MAX(loan_id) AS 'max_id' FROM loans;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            commitTransaction();
            if (resultSet.next()) {
               return resultSet.getLong("max_id");
            }
        }catch (SQLException e) {
            rollBack();
        }
        return 0;
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
    protected int getTotalApprovedLoans() {
        int result = 0;
        try {
            String query = "SELECT COUNT(loan_id) FROM loans WHERE(application_status = 'pending_payment');";
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
            String query = "SELECT COUNT(loan_id) FROM loans WHERE(application_status = 'processing');";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {result = resultSet.getInt(1);}
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
                    "INNER JOIN customer_account_data cad\n" +
                    "ON cd.customer_id = cad.customer_id;";
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
                byte is_active = resultSet.getByte("is_active");
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
        }catch (Exception ignore) {}return data;
    }
    public ObservableList<CustomerAccountsDataRepository> fetchCustomersAccountData() {
        ObservableList<CustomerAccountsDataRepository> data = FXCollections.observableArrayList();
        try{
            String query = "SELECT * FROM customer_account_data";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
//            account_id, customer_id, account_type, account_number, current_balance, previous_balance, date_modified, modified_by
            while (resultSet.next()) {
                int account_id = resultSet.getInt("account_id");
                int customer_id = resultSet.getInt("customer_id");
                String account_type = resultSet.getString("account_type");
                String account_number = resultSet.getString("account_number");
                Double current_balance = resultSet.getDouble("account_balance");
                Double previous_balance = resultSet.getDouble("previous_balance");
                Timestamp date_modified = resultSet.getTimestamp("date_modified");
                int modified_by = resultSet.getInt("modified_by");

                data.add(new CustomerAccountsDataRepository(account_id, customer_id, account_number, account_type, current_balance, previous_balance, date_modified, modified_by));
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
    public int createAccountBalance(CustomerAccountsDataRepository balanceDataModel) {
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
            commitTransaction();
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
            String query = "SELECT * FROM message_operations;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                data.add(resultSet.getString("operation"));
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

    public ObservableList<TransactionsEntity> getTodayTransactionLogs() {
        ObservableList<TransactionsEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT transaction_id, transaction_type, (cash_amount + ecash_amount) AS amount, \n" +
                    "TIME(transaction_date) AS `time` FROM transaction_logs WHERE DATE(transaction_date) = current_date();";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String transactionId  = resultSet.getString("transaction_id");
                String transactionType = resultSet.getString("transaction_type");
                double amount = resultSet.getDouble("amount");
                LocalTime localTime = resultSet.getTime("time").toLocalTime();
                data.add(new TransactionsEntity(transactionId, transactionType, amount, localTime));
            }
        }catch (Exception igored){}
        return data;
    }





}//END OF CLASS...
