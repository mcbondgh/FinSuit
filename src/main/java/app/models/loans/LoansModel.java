package app.models.loans;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.*;
import app.repositories.transactions.TransactionsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.jsf.el.SpringBeanFacesELResolver;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoansModel extends MainModel {
    ErrorLogger logError = new ErrorLogger();
    String className = this.getClass().getName();

    protected List<Object> countTotalLoans(String searchParameter) {
        List<Object> data = new ArrayList<>();
        try {
            String query = "SELECT COUNT(ln.customer_id) AS loan_count, sum(ln.approved_amount) AS 'disbursed_amount' , SUM(total_payment) total_payment FROM loans ln\n" +
                    "INNER JOIN customer_data AS cd\n" +
                    "ON cd.customer_id = ln.customer_id\n" +
                    "INNER JOIN customer_account_data AS cad\n" +
                    "ON cad.customer_id = ln.customer_id \n" +
                    "WHERE cd.id_number = '"+searchParameter+"' OR cad.account_number = '"+searchParameter+"';";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                data.add(resultSet.getInt("loan_count"));//0
                data.add(resultSet.getDouble("disbursed_amount"));//1
                data.add(resultSet.getDouble("total_payment"));//2
            }
            getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return data;

    }
    protected ObservableList<Object> getCustomerIdAndAccountNumbers() {
        ObservableList<Object> data = FXCollections.observableArrayList();
        try {
            String query = """
                    SELECT id_number, account_number FROM customer_data cd
                    INNER JOIN customer_account_data AS cad
                    ON cd.customer_id = cad.customer_id;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(resultSet.getString(1));
                data.add(resultSet.getString(2));
            }
            getConnection().close();
        }catch (SQLException e) {
            rollBack();
        }
        return data;
    }
//    protected void getDraftsCount(Label draft_count, ComboBox<String> accountNumber) {
//        try {
//            String query = "SELECT COUNT(loan_id) AS drafts FROM loans WHERE is_drafted = 1;";
//            statement = getConnection().createStatement();
//            resultSet = statement.executeQuery(query);
//            if (resultSet.next()) {
//                draft_count.setText(String.valueOf(resultSet.getInt("drafts")));
//            }
//
//            String query2 = "SELECT account_number FROM customer_account_data AS cad\n" +
//                            "INNER JOIN loans as ln ON \n" +
//                            "cad.customer_id = ln.customer_id;";
//            statement = getConnection().createStatement();
//            resultSet = statement.executeQuery(query2);
//            while (resultSet.next()) {
//                accountNumber.getItems().add(resultSet.getString("account_number"));
//            }
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    protected int applyForLoan(LoanApplicationEntity applicationEntity, CustomersDataRepository customer) {
        int flag = 0;
        try {
            String query = "INSERT INTO customer_data(firstname, lastname, othername, gender, dob, age, mobile_number, other_number, email, digital_address, " +
                    "residential_address, key_landmark, marital_status, id_type, id_number, educational_background, " +
                    "contact_person_fullname, contact_person_number, contact_person_gender, contact_person_landmark, contact_person_digital_address,  contact_person_id_type, " +
                    "contact_person_id_number, contact_person_place_of_work, institution_address, relationship_to_applicant," +
                    "created_by, agent_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String query2 = "INSERT INTO loan_applicant_details(loan_no, profile_picture, image, company_name, company_mobile_number, company_address, staff_id, occupation, employment_date, " +
                    "basic_salary, gross_salary, total_deduction, net_salary, guarantor_name, guarantor_gender, guarantor_number, guarantor_digital_address," +
                    "guarantor_landmark, guarantor_idType, guarantor_idNumber, guarantor_relationship, guarantor_occupation," +
                    "guarantor_place_of_work, guarantor_institution_address, guarantor_income) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, customer.getFirstname());
            preparedStatement.setString(2, customer.getLastname());
            preparedStatement.setString(3, customer.getOthername());
            preparedStatement.setString(4, customer.getGender());
            preparedStatement.setDate(5, customer.getDob());
            preparedStatement.setInt(6, customer.getAge());
            preparedStatement.setString(7, customer.getMobile_number());
            preparedStatement.setString(8, customer.getOther_number());
            preparedStatement.setString(9, customer.getEmail());
            preparedStatement.setString(10, customer.getDigital_address());
            preparedStatement.setString(11, customer.getResidential_address());
            preparedStatement.setString(12, customer.getKey_landmark());
            preparedStatement.setString(13, customer.getMarital_status());
            preparedStatement.setString(14, customer.getId_type());
            preparedStatement.setString(15, customer.getId_number());
            preparedStatement.setString(16, customer.getEducational_background());
            preparedStatement.setString(17, customer.getContact_person_fullname());
            preparedStatement.setString(18, customer.getContact_person_number());
            preparedStatement.setString(19, customer.getContact_person_gender());
            preparedStatement.setString(20, customer.getContact_person_landmark());
            preparedStatement.setString(21, customer.getContact_person_digital_address());
            preparedStatement.setString(22, customer.getContact_person_id_type());
            preparedStatement.setString(23, customer.getContact_person_id_number());
            preparedStatement.setString(24, customer.getContact_person_place_of_work());
            preparedStatement.setString(25, customer.getInstitution_address());
            preparedStatement.setString(26, customer.getRelationship_to_applicant());
            preparedStatement.setInt(27, customer.getCreated_by());
            preparedStatement.setInt(28, customer.getAgentId());
            flag = preparedStatement.executeUpdate();

            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, applicationEntity.getLoan_no());
            preparedStatement.setString(2, applicationEntity.getProfile_picture());
            preparedStatement.setBytes(3, applicationEntity.getImage());
            preparedStatement.setString(4, applicationEntity.getCompany_name());
            preparedStatement.setString(5, applicationEntity.getCompany_mobile_number());
            preparedStatement.setString(6, applicationEntity.getCompany_address());
            preparedStatement.setString(7, applicationEntity.getStaff_id());
            preparedStatement.setString(8, applicationEntity.getOccupation());
            preparedStatement.setDate(9, Date.valueOf(applicationEntity.getEmployment_date()));
            preparedStatement.setDouble(10, applicationEntity.getBasic_salary());
            preparedStatement.setDouble(11, applicationEntity.getGross_salary());
            preparedStatement.setDouble(12, applicationEntity.getTotal_deduction());
            preparedStatement.setDouble(13, applicationEntity.getNet_salary());
            preparedStatement.setString(14, applicationEntity.getGuranter_name());
            preparedStatement.setString(15, applicationEntity.getGender());
            preparedStatement.setString(16, applicationEntity.getGuranter_number());
            preparedStatement.setString(17, applicationEntity.getGuranter_digital_address());
            preparedStatement.setString(18, applicationEntity.getGuarantor_landmark());
            preparedStatement.setString(19, applicationEntity.getGuranter_idType());
            preparedStatement.setString(20, applicationEntity.getGuranter_idNumber());
            preparedStatement.setString(21, applicationEntity.getGuranter_relationship());
            preparedStatement.setString(22, applicationEntity.getOccupation());
            preparedStatement.setString(23, applicationEntity.getGurater_place_of_work());
            preparedStatement.setString(24, applicationEntity.getGuranter_institution_address());
            preparedStatement.setDouble(25, applicationEntity.getNet_salary());
            flag += preparedStatement.executeUpdate();
            preparedStatement.close();
            getConnection().close();
        }catch (Exception e) {
            logError.logMessage(e.getLocalizedMessage(), "applyForLoan", className);
            rollBack();
            e.printStackTrace();
        }
        return flag;
    }
    protected int createLoan(long customerId, String loanNo, String loanType, double requestedAmount, String loanPurpose, int userId) {
        int flag = 0;
        try {
            String query = "INSERT INTO loans(customer_id, loan_no, loan_type, is_drafted, requested_amount, loan_purpose, created_by) VALUES(?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setString(2, loanNo);
            preparedStatement.setString(3, loanType);
            preparedStatement.setDouble(4, 0);
            preparedStatement.setDouble(5, requestedAmount);
            preparedStatement.setString(6, loanPurpose);
            preparedStatement.setInt(7, userId);
            flag = preparedStatement.executeUpdate();
            preparedStatement.close();
            getConnection().close();
        } catch (SQLException e) {
            rollBack();
            e.printStackTrace();
        }
        return flag;
    }

    protected int updateLoanApplicantData(LoanApplicationEntity loanApplicant, CustomersDataRepository customerData , LoansEntity loans) {
        AtomicInteger status = new AtomicInteger();
        try {

            String query1 = """
                    UPDATE customer_data
                    	SET firstname = ?, lastname = ?, othername = ?, gender = ?, dob = ?, mobile_number = ?, other_number = ?,\s
                        email = ?, digital_address = ?, residential_address = ?, key_landmark = ?, marital_status = ?, id_type =?,\s
                        id_number = ?, educational_background = ?, contact_person_fullname = ?, contact_person_number = ?,\s
                        contact_person_gender = ?, contact_person_landmark =?, contact_person_digital_address = ?,  contact_person_id_type = ?,
                    	contact_person_id_number = ?, contact_person_place_of_work = ?, institution_address = ?, relationship_to_applicant = ?,
                        date_modified = DEFAULT, modified_by = ?, agent_id = ?
                        WHERE(customer_id = ?);
                    """;
            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setString(1, customerData.getFirstname());preparedStatement.setString(2, customerData.getLastname());
            preparedStatement.setString(3, customerData.getOthername());preparedStatement.setString(4, customerData.getGender());
            preparedStatement.setDate(5, customerData.getDob());preparedStatement.setString(6, customerData.getMobile_number());
            preparedStatement.setString(7, customerData.getOther_number());preparedStatement.setString(8, customerData.getEmail());
            preparedStatement.setString(9, customerData.getDigital_address());preparedStatement.setString(10, customerData.getResidential_address());
            preparedStatement.setString(11, customerData.getKey_landmark());preparedStatement.setString(12, customerData.getMarital_status());
            preparedStatement.setString(13, customerData.getId_type()); preparedStatement.setString(14, customerData.getId_number());
            preparedStatement.setString(15, customerData.getEducational_background());preparedStatement.setString(16, customerData.getContact_person_fullname());
            preparedStatement.setString(17, customerData.getContact_person_number());preparedStatement.setString(18, customerData.getContact_person_gender());
            preparedStatement.setString(19, customerData.getContact_person_landmark());preparedStatement.setString(20, customerData.getContact_person_digital_address());
            preparedStatement.setString(21, customerData.getContact_person_id_type());preparedStatement.setString(22, customerData.getContact_person_id_number());
            preparedStatement.setString(23,customerData.getContact_person_place_of_work());preparedStatement.setString(24, customerData.getInstitution_address());
            preparedStatement.setString(25, customerData.getRelationship_to_applicant());preparedStatement.setInt(26, customerData.getModified_by());
            preparedStatement.setInt(27, customerData.getAgentId());preparedStatement.setLong(28, customerData.getAccount_Id());
            status.getAndAdd(preparedStatement.executeUpdate());

            String query2 = """
                    UPDATE loan_applicant_details\s
                    SET image = ? , company_name = ?, company_mobile_number = ?, company_address =?, staff_id =?, occupation =?, employment_date =?,\s
                    basic_salary =?, gross_salary=?, total_deduction=?, net_salary=?, guarantor_name=?, guarantor_gender=?, guarantor_number=?,\s
                    guarantor_digital_address=?, guarantor_landmark =?, guarantor_idType=?, guarantor_idNumber=?, guarantor_relationship=?,
                    guarantor_occupation =?, guarantor_place_of_work =?, guarantor_institution_address =?, guarantor_income =?
                    WHERE(loan_no = ?);
                    """;
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setBytes(1, loanApplicant.getImage());preparedStatement.setString(2, loanApplicant.getCompany_name());
            preparedStatement.setString(3, loanApplicant.getCompany_mobile_number());preparedStatement.setString(4, loanApplicant.getCompany_address());
            preparedStatement.setString(5, loanApplicant.getStaff_id());
            preparedStatement.setString(6, loanApplicant.getGuranter_occupation());
            preparedStatement.setDate(7, Date.valueOf(loanApplicant.getEmployment_date()));
            preparedStatement.setDouble(8, loanApplicant.getBasic_salary());preparedStatement.setDouble(9, loanApplicant.getGross_salary());
            preparedStatement.setDouble(10, loanApplicant.getTotal_deduction());
            preparedStatement.setDouble(11, loanApplicant.getNet_salary());
            preparedStatement.setString(12, loanApplicant.getGuranter_name()); preparedStatement.setString(13, loanApplicant.getGender());
            preparedStatement.setString(14, loanApplicant.getGuranter_number());
            preparedStatement.setString(15, loanApplicant.getGuranter_digital_address());
            preparedStatement.setString(16, loanApplicant.getGuarantor_landmark());
            preparedStatement.setString(17, loanApplicant.getGuranter_idType());
            preparedStatement.setString(18, loanApplicant.getGuranter_idNumber());
            preparedStatement.setString(19, loanApplicant.getGuranter_relationship());
            preparedStatement.setString(20, loanApplicant.getGuranter_occupation());
            preparedStatement.setString(21, loanApplicant.getGurater_place_of_work());
            preparedStatement.setString(22, loanApplicant.getGuranter_institution_address());
            preparedStatement.setDouble(23, loanApplicant.getGuranter_income());
            preparedStatement.setString(24, loanApplicant.getLoan_no());
            status.getAndAdd(preparedStatement.executeUpdate());

            String query3 = """
                    UPDATE loans\s
                    	SET loan_type =?, requested_amount =?,\s
                    	loan_purpose =?, date_modified = DEFAULT, updated_by = ?
                    WHERE(loan_no = ?);
                    """;
            preparedStatement = getConnection().prepareStatement(query3);
            preparedStatement.setString(1, loans.getLoan_type());
            preparedStatement.setDouble(2, loans.getRequested_amount());
            preparedStatement.setString(3, loans.getLoan_purpose());
            preparedStatement.setInt(4, loans.getUpdated_by());
            preparedStatement.setString(5, loans.getLoan_no());
            status.getAndAdd(preparedStatement.executeUpdate());

            preparedStatement.close();
            getConnection().close();
        }catch (Exception e) {
            e.printStackTrace();
            logError.logMessage(e.getLocalizedMessage(), "updateLoanApplicantData", className);
        }
        return status.get();
    }

    protected ObservableList<LoansTableEntity> getLoansByApplicationStatus() {
        ObservableList<LoansTableEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_id, username, CONCAT(lastname, ' ', firstname, ' ', othername) AS fullname, gross_salary, loan_no, loan_type, loan_purpose,\n" +
                    "DATE(ln.date_created) AS application_date, \n" +
                    "requested_amount, application_status FROM loans AS ln\n" +
                    "JOIN customer_data AS cd USING(customer_id)\n" +
                    "INNER JOIN loan_applicant_details using(loan_no)\n" +
                    "JOIN users AS u ON ln.created_by = u.user_id WHERE(application_status = 'processing' AND loan_status = 'active');";
            preparedStatement = getConnection().prepareStatement(query);
//            preparedStatement.setString(1, user_id);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int no = resultSet.getInt("loan_id");
                String fullname = resultSet.getString("fullname");
                String loanNo = resultSet.getString("loan_no");
                String loanType = resultSet.getString("loan_type");
                Date date = resultSet.getDate("application_date");
                double amount = resultSet.getDouble("requested_amount");
                String status = resultSet.getString("application_status");
                String username = resultSet.getString("username");
                String loanPurpose = resultSet.getString("loan_purpose");
                double grossSalary = resultSet.getDouble("gross_salary");
                data.add(new LoansTableEntity(no, fullname, loanNo, date, amount, username, status, loanType, loanPurpose, grossSalary));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return data;
    }
    protected ObservableList<LoansTableEntity> getLoansUnderApplicationStage(int user_id) {
        ObservableList<LoansTableEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_id, username, CONCAT(lastname, ' ', firstname) AS fullname, loan_no, loan_type, DATE(ln.date_created) AS application_date, \n" +
                    "requested_amount, loan_purpose, application_status FROM loans AS ln\n" +
                    "JOIN customer_data AS cd ON ln.customer_id = cd.customer_id " +
                    "JOIN users AS u ON ln.created_by = u.user_id WHERE(application_status != 'disbursed');";
            preparedStatement = getConnection().prepareStatement(query);
//            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int no = resultSet.getInt("loan_id");
                String fullname = resultSet.getString("fullname");
                String loanNo = resultSet.getString("loan_no");
                String loanType = resultSet.getString("loan_type");
                Date date = resultSet.getDate("application_date");
                double amount = resultSet.getDouble("requested_amount");
                String status = resultSet.getString("application_status");
                String username = resultSet.getString("username");
                String loanPurpose = resultSet.getString("loan_purpose");
                data.add(new LoansTableEntity(no, fullname, loanNo, date, amount, username, status, loanType, loanPurpose, 0.00));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return data;
    }
    protected ObservableList<LoansTableEntity> getLoansUnderApplicationStage() {
        ObservableList<LoansTableEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_id, CONCAT(lastname, ' ', firstname) AS fullname, loan_no, DATE(ln.date_created) AS application_date, \n" +
                    "loan_type FROM loans AS ln JOIN customer_data AS cd ON ln.customer_id = cd.customer_id WHERE(application_status = 'application'); ";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt("loan_id");
                String name = resultSet.getString("fullname");
                String loanNo = resultSet.getString("loan_no");
                Date date = resultSet.getDate("application_date");
                String loanType = resultSet.getString("loan_type");
                data.add(new LoansTableEntity(id, name, loanNo, date, loanType));
            }
            statement.close();
            resultSet.close();
            getConnection().close();
        }catch (Exception ignore){}
        return data;
    }
    protected int countRequestedLoans() {
        try {
            String query = "SELECT COUNT(loan_id) as count FROM loans WHERE(application_status = 'application');";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
            getConnection().close();
        }catch (SQLException ignore){}
        return 0;
    }
    protected ArrayList<Object> getExistingCustomerForLoan(String searchParameter) {
        ArrayList<Object> data = new ArrayList<>();
        try {
            String query = "SELECT firstname, lastname, othername, gender, dob, mobile_number, other_number, email, \n" +
                    "\t\tdigital_address, residential_address, key_landmark, marital_status, id_type, id_number,\n" +
                    "        educational_background\n" +
                    "FROM customer_data AS cd \n" +
                    "JOIN customer_account_data AS cad ON cd.customer_id = cad.customer_id\n" +
                    "WHERE cd.id_number = ? OR cad.account_number = ?;";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, searchParameter);
            preparedStatement.setString(2, searchParameter);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                data.add(resultSet.getString("firstname"));//1
                data.add(resultSet.getString("lastname"));//2
                data.add(resultSet.getString("othername"));//3
                data.add(resultSet.getString("gender"));//4
                data.add(resultSet.getDate("dob"));//5
                data.add(resultSet.getString("mobile_number"));//6
                data.add(resultSet.getString("other_number"));//7
                data.add(resultSet.getString("email"));//8
                data.add(resultSet.getString("digital_address"));//9
                data.add(resultSet.getString("residential_address"));//10
                data.add(resultSet.getString("key_landmark"));//11
                data.add(resultSet.getString("marital_status"));//12
                data.add(resultSet.getString("id_type"));//13
                data.add(resultSet.getString("id_number"));//14
                data.add(resultSet.getString("educational_background"));//15
            }
            getConnection().close();
        }catch (SQLException ignore) {
            ignore.printStackTrace();
        }

        return data;
    }
    public int countAssignedLoans() {
        try {
            String query = "SELECT COUNT(loan_id) as count FROM loans WHERE(application_status = 'processing');";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
            getConnection().close();
        }catch (SQLException ignore){}
        return 0;
    }

    //THIS METHOD WHEN INVOKED SHALL UPDATE THE loans table BASED ON THE ARGUMENTS PARSED TO IT.
    //THIS SHALL CHANGE THE LOAN STATUS IN THE loans TABLE from application to processing...
    public int updateLoanApplicationStatus(String applicationStatus, String loanNo, int userId) {
        int flag = 0;
        try {
            String query = "UPDATE loans SET application_status = ?, date_modified = DEFAULT, updated_by = ? WHERE(loan_no = ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1,applicationStatus);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, loanNo);
            flag = preparedStatement.executeUpdate();
            preparedStatement.close();
            getConnection().close();
        }catch (Exception e){rollBack();
        e.printStackTrace();}
        return flag;
    }

    public int saveAssignedGroupSupervisor(String employeeId, String loanNo, int userId) {
        int flag = 0;
        try {
            String query2 = "INSERT INTO group_supervisors(emp_id, loan_id, added_by) VALUES(?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, employeeId);
            preparedStatement.setString(2, loanNo);
            preparedStatement.setInt(3, userId);
            flag = preparedStatement.executeUpdate();
            getConnection().close();
        }catch (Exception e){e.printStackTrace();}
        return flag;
    }
    public ObservableList<String> getGroupSupervisors(String employeeId) {
        ObservableList<String> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT DISTINCT gs.loan_id, emp_id FROM group_supervisors as gs\n" +
                    "INNER JOIN loans AS ln\n" +
                    "ON gs.loan_id = ln.loan_no\n" +
                    "WHERE(gs.emp_id = '"+employeeId+"' AND application_status = 'processing');";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                data.add(resultSet.getString(1));
            }
            getConnection().close();
        }catch (SQLException ignore){ignore.printStackTrace();}
        return data;
    }
    public ObservableList<PendingLoanApprovalEntity> getLoansUnderPendingApproval() {
        ObservableList<PendingLoanApprovalEntity> data = FXCollections.observableArrayList();
        try{
            String query = "SELECT lqv.loan_no, requested_amount, gross_salary, statutory_deduction, \n" +
                    "\tremaining_balance, total_deduction, amount, loan_amount, \n" +
                    "interest_rate, loan_period, processing_rate, start_date FROM loans ln\n" +
                    "INNER JOIN loan_qualification_values AS lqv \n" +
                    "ON lqv.loan_no = ln.loan_no WHERE(loan_status = 'active' AND application_status = 'pending_approval')";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String loanNo = resultSet.getString("lqv.loan_no");
                double requestedNo = resultSet.getDouble("requested_amount");
                double gross = resultSet.getDouble("gross_salary");
                double statutory = resultSet.getDouble("statutory_deduction");
                double remaining = resultSet.getDouble("remaining_balance");
                double deduction = resultSet.getDouble("total_deduction");
                double amount = resultSet.getDouble("amount");
                double loanAmount = resultSet.getDouble("loan_amount");
                int interest = resultSet.getInt("interest_rate");
                int period = resultSet.getInt("loan_period");
                int processing = resultSet.getInt("processing_rate");
                LocalDate date = resultSet.getDate("start_date").toLocalDate();
                data.addAll(new PendingLoanApprovalEntity(loanNo, requestedNo, gross, statutory, remaining, deduction, amount, loanAmount, interest, period, processing, date));
            }
                getConnection().close();
        }catch (Exception ignore){ignore.printStackTrace();}
        return data;
    }
    protected void saveLoanSchedule(String loanNo, double monthlyInstallment, double principal, double interest, LocalDate date, double balance, int loggedInUserId) {
        try {
            String query = "INSERT INTO loan_schedule(loan_no, monthly_installment, principal_amount, interest_amount, payment_date, balance, generated_by)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, loanNo);
            preparedStatement.setDouble(2, monthlyInstallment);
            preparedStatement.setDouble(3,principal);
            preparedStatement.setDouble(4, interest);
            preparedStatement.setDate(5, Date.valueOf(date));
            preparedStatement.setDouble(6, balance);
            preparedStatement.setInt(7, loggedInUserId);
            preparedStatement.execute();
            commitTransaction();
            getConnection().close();
        }catch (Exception e) {rollBack();}

    }
    protected int saveLoanCalculatorValues(String loanNo, double gross, double statsDeduction, double remainingBal, double totalDeduction, double amount, double loanAmount, int interestRate, int loanPeriod, int processRate, LocalDate startDate, LocalDate endDate) {
        int flag = 0;
        try {
            String query = "INSERT INTO loan_qualification_values(loan_no, gross_salary, statutory_deduction, remaining_balance, total_deduction, amount, loan_amount, interest_rate, loan_period, processing_rate, start_date, end_date) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
           preparedStatement.setString(1, loanNo);
           preparedStatement.setDouble(2, gross);
           preparedStatement.setDouble(3, statsDeduction);
           preparedStatement.setDouble(4, remainingBal);
           preparedStatement.setDouble(5, totalDeduction);
           preparedStatement.setDouble(6, amount);
           preparedStatement.setDouble(7, loanAmount);
           preparedStatement.setInt(8, interestRate);
           preparedStatement.setInt(9, loanPeriod);
           preparedStatement.setInt(10, processRate);
           preparedStatement.setDate(11, Date.valueOf(startDate));
           preparedStatement.setDate(12, Date.valueOf(endDate));
            flag = preparedStatement.executeUpdate();
            commitTransaction();
            getConnection().close();
        }catch (Exception e) {
            e.printStackTrace();
            rollBack();
        }
        return flag;
    }
    public int approveLoanForDisbursement(@NotNull PendingLoanApprovalEntity qualifications, @NotNull LoansEntity loans) {
        int status = 0;
        try{
            String query1 = """
                    UPDATE loans
                    SET approved_amount = ?, repayment_amount = ?, application_status = 'pending_payment', date_modified = DEFAULT, approved_by = ?
                    WHERE(loan_no = ?);""";

            String query2 = """
                    UPDATE loan_qualification_values
                    SET loan_amount = ?, interest_rate = ?, loan_period = ?, processing_rate = ?, start_date = ?, end_date = ?
                    WHERE(loan_no = ?);""";

            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setDouble(1, loans.getApproved_amount());
            preparedStatement.setDouble(2, loans.getTotal_loan_amount());
            preparedStatement.setInt(3, loans.getApproved_by());
            preparedStatement.setString(4, loans.getLoan_no());
            status = preparedStatement.executeUpdate();

            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setDouble(1, qualifications.getLoan_amount());
            preparedStatement.setInt(2, qualifications.getInterest_rate());
            preparedStatement.setInt(3, qualifications.getLoan_period());
            preparedStatement.setInt(4, qualifications.getProcessing_rate());
            preparedStatement.setDate(5, Date.valueOf(qualifications.getStart_date()));
            preparedStatement.setDate(6, Date.valueOf(qualifications.getEnd_date()));
            preparedStatement.setString(7, qualifications.getLoan_no());
            status += preparedStatement.executeUpdate();

        }catch (SQLException e){
            rollBack();
            e.printStackTrace();
        }
        return status;
    }
    public void updateLoanSchedule(double installment, double principal, double interest, LocalDate date, double balance, int generatedBy, long scheduleId) {
        try {
            String query3 = "UPDATE loan_schedule\n" +
                    "SET monthly_installment = ?, principal_amount= ?, interest_amount = ?, payment_date = ?, balance = ?, generated_by = ?\n" +
                    "WHERE(schedule_id = ?);";
            preparedStatement = getConnection().prepareStatement(query3);
            preparedStatement.setDouble(1, installment);
            preparedStatement.setDouble(2,principal);
            preparedStatement.setDouble(3, interest);
            preparedStatement.setDate(4, Date.valueOf(date));
            preparedStatement.setDouble(5, balance);
            preparedStatement.setInt(6, generatedBy);
            preparedStatement.setLong(7, scheduleId);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            rollBack();
        }
    }

    protected int updateCustomerAccountData(DisbursementEntity var) {
        AtomicInteger status = new AtomicInteger();
        try {
            String query = """
                    UPDATE customer_account_data SET account_balance = ?,
                    	previous_balance = ?, date_modified = DEFAULT WHERE account_number = ? ;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setDouble(1, var.getAccountBalance());
            preparedStatement.setDouble(2, var.getPreviousBalance());
            preparedStatement.setString(3, var.getAccountNumber());
            status.set(preparedStatement.executeUpdate());
        }catch (Exception ignore) {}
        return status.get();
    }

    protected int saveDisbursedLoans(String loanNo, int userId) {
        int status = 0;
        try{
            String query = "UPDATE loans SET application_status = 'disbursed', date_modified = DEFAULT, updated_by = ? WHERE(loan_no = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, loanNo);
            status = preparedStatement.executeUpdate();
            getConnection().close();
        }catch (SQLException ignore){}
        return status;
    }

    protected Map<String, Object> getLoanDetailsByLoanNumber(String loanNo) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            String query = """
                    SELECT CONCAT(firstname, ' ', othername, ' ', lastname) AS fullname,\s
                    	mobile_number,\s
                    	repayment_amount,
                        loan_status,
                        total_payment, image,
                        (repayment_amount - total_payment) AS balance FROM customer_data AS cd
                        INNER JOIN loans AS ln USING(customer_id)
                        INNER JOIN loan_applicant_details AS ap
                        ON ln.loan_no = ap.loan_no
                        WHERE(ln.loan_no = ?);
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, loanNo);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                data.put("fullname", resultSet.getString("fullname"));
                data.put("mobile_number", resultSet.getString("mobile_number"));
                data.put("approved_amount", resultSet.getDouble("repayment_amount"));
                data.put("loan_status", resultSet.getString("loan_status"));
                data.put("total_payment", resultSet.getDouble("total_payment"));
                data.put("balance", resultSet.getDouble("balance"));
                data.put("picture", resultSet.getBytes("image"));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException e){
            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logError.logMessage(className, "getLoanDetailsByLoanNumber", error);
        }
        return data;
    }

    public int saveLoanTermination(LoansEntity loans, RepaymentEntity repay) {
        int flag = 0;
        try {
            String query1 = "UPDATE loans \n" +
                    "\tSET total_payment = ?, loan_status = 'terminated', date_modified = DEFAULT, updated_by = ?\n" +
                    "WHERE(loan_no = ?);";

            String query2 = "INSERT INTO loan_payment_logs(loan_no, paid_amount, date_collected, collected_by)\n" +
                    "VALUES(?, ?, DEFAULT, ?);";

            String query3 = "INSERT INTO `finsuit`.`terminated_loans` (`loan_no`, `purpose`, `write_off`, `terminated_by`) \n" +
                    "VALUES (?, ?, ?, ?);";

            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setDouble(1, loans.getTotal_payment());
            preparedStatement.setInt(2, loans.getUpdated_by());
            preparedStatement.setString(3, loans.getLoan_no());
            flag = preparedStatement.executeUpdate();

            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, repay.getLoan_no());
            preparedStatement.setDouble(2, repay.getPaid_amount());
            preparedStatement.setInt(3, repay.getCollected_by());
            flag += preparedStatement.executeUpdate();

            preparedStatement = getConnection().prepareStatement(query3);
            preparedStatement.setString(1, loans.getLoan_no());
            preparedStatement.setString(2, loans.getTermination_purpose());
            preparedStatement.setDouble(3, repay.getWrite_offs());
            preparedStatement.setInt(4, repay.getCollected_by());
            flag += preparedStatement.executeUpdate();

            preparedStatement.close();
            getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
            String error = Arrays.toString(e.getStackTrace());
            logError.logMessage(error, "saveLoanTermination", className);
        }
        return flag;
    }

    protected int saveLoanPaymentTransaction(LoansEntity loans, TransactionsEntity trans, RepaymentEntity repay, double penalty) {
        int status = 0;
        try {
            String query1 = "INSERT INTO loan_payment_logs(loan_no, installment_month, paid_amount, collected_by)\n" +
                    "VALUES(?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setString(1, repay.getLoan_no());
            preparedStatement.setDate(2, repay.getInstallment_month());
            preparedStatement.setDouble(3, repay.getPaid_amount());
            preparedStatement.setLong(4, repay.getCollected_by());
            status = preparedStatement.executeUpdate();

            String query2 = """
                    INSERT INTO transaction_logs(\s
                    \taccount_number,
                    \ttransaction_id,\s
                        transaction_type,
                        payment_method,
                    \tcash_amount,\s
                        ecash_amount,\s
                        user_id)
                    VALUES(?, ?, ?, ?, ?, ?, ?);""";
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, trans.getAccount_number());
            preparedStatement.setString(2, trans.getTransaction_id());
            preparedStatement.setString(3, "REPAYMENT");
            preparedStatement.setString(4, trans.getPayment_method());
            preparedStatement.setDouble(5, trans.getCash_amount());
            preparedStatement.setDouble(6, trans.getEcash_amount());
            preparedStatement.setInt(7, trans.getUserId());
            status += preparedStatement.executeUpdate();

            String query5 = "UPDATE loans \n" +
                    "SET total_payment = ?, date_modified = DEFAULT, updated_by = ? WHERE(loan_no = ?);";
            preparedStatement = getConnection().prepareStatement(query5);
            preparedStatement.setDouble(1, loans.getTotal_payment());
            preparedStatement.setInt(2, trans.getUserId());
            preparedStatement.setString(3, loans.getLoan_no());
            preparedStatement.execute();

            String query3 = """
                    UPDATE loan_schedule
                    SET penalty_amount = ? WHERE payment_date = ?;
                    """;
            preparedStatement = getConnection().prepareStatement(query3);
            preparedStatement.setDouble(1, penalty);
            preparedStatement.setDate(2, repay.getInstallment_month());
            status += preparedStatement.executeUpdate();
            getConnection().close();
        }catch (SQLException e) {
            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logError.logMessage(className, "saveLoanPaymentTransaction", error);
            rollBack();
        }
        return status;
    }

    protected Map<String, Object> getLoanApplicantDataByLoanNumber(String loanNo) {
        Map<String, Object> applicantData = new HashMap<>();
        try {
            String query = """
                    SELECT * FROM customer_data AS cd
                    LEFT JOIN loans AS ln
                    USING(customer_id)
                    CROSS JOIN loan_applicant_details AS ap
                    USING(loan_no)
                    CROSS JOIN agents AS a\s
                    USING(agent_id)
                    WHERE loan_no = ?;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, loanNo);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                agent_id, loan_no, customer_id, firstname, lastname, othername, gender, dob, age, place_of_birth, mobile_number, other_number,
//                email, digital_address, residential_address, key_landmark, marital_status,name_of_spouse, id_type, id_number,
//                educational_background, additional_comment, contact_person_fullname,
//                contact_person_dob, contact_person_number,
//                contact_person_gender, contact_person_landmark, contact_person_education_level, contact_person_digital_address,
//                contact_person_id_type, contact_person_id_number, contact_person_place_of_work, institution_address, institution_number,
//                relationship_to_applicant,
//               image, company_name, company_mobile_number,
//                company_address, staff_id, occupation, employment_date, basic_salary, gross_salary, total_deduction, net_salary,
//                guarantor_name, guarantor_gender, guarantor_number, guarantor_digital_address, guarantor_residential_address,
//                guarantor_landmark, guarantor_idType, guarantor_idNumber, guarantor_relationship, guarantor_occupation,
//                guarantor_place_of_work, guarantor_institution_address, guarantor_income,information

                applicantData.put("customer_id", resultSet.getInt("customer_id"));
                applicantData.put("loanType", resultSet.getString("loan_type"));
                applicantData.put("amount", resultSet.getDouble("requested_amount"));
                applicantData.put("loanPurpose", resultSet.getString("loan_purpose"));
                applicantData.put("firstname", resultSet.getString("firstname"));
                applicantData.put("lastname", resultSet.getString("lastname"));
                applicantData.put("otherName", resultSet.getString("othername"));
                applicantData.put("gender", resultSet.getString("gender"));
                applicantData.put("dob", resultSet.getString("dob"));
                applicantData.put("place_of_birth", resultSet.getString("place_of_birth"));
                applicantData.put("mobile_number", resultSet.getString("mobile_number"));
                applicantData.put("other_number", resultSet.getString("other_number"));
                applicantData.put("email", resultSet.getString("email"));
                applicantData.put("digital_address", resultSet.getString("digital_address"));
                applicantData.put("residential_address", resultSet.getString("residential_address"));
                applicantData.put("key_landmark", resultSet.getString("key_landmark"));
                applicantData.put("marital_status", resultSet.getString("marital_status"));
                applicantData.put("name_of_spouse", resultSet.getString("name_of_spouse"));
                applicantData.put("id_type", resultSet.getString("id_type"));
                applicantData.put("id_number", resultSet.getString("id_number"));
                applicantData.put("educational_background", resultSet.getString("educational_background"));
                applicantData.put("additional_comment", resultSet.getString("additional_comment"));

                applicantData.put("contactPersonName", resultSet.getString("contact_person_fullname"));
                applicantData.put("contactNumber", resultSet.getString("contact_person_number"));
                applicantData.put("contactLandmark", resultSet.getString("contact_person_landmark"));
                applicantData.put("contactGender", resultSet.getString("contact_person_gender"));
                applicantData.put("contactIdType", resultSet.getString("contact_person_id_type"));
                applicantData.put("contactIdNumber", resultSet.getString("contact_person_id_number"));
                applicantData.put("contactWorkplace", resultSet.getString("contact_person_place_of_work"));
                applicantData.put("contactInstitutionAddress", resultSet.getString("institution_address"));
                applicantData.put("contact_digital_address", resultSet.getString("contact_person_digital_address"));
                applicantData.put("relationship_to_applicant", resultSet.getString("relationship_to_applicant"));

                applicantData.put("image", resultSet.getBytes("image"));
                applicantData.put("agentName", resultSet.getString("agent_name"));
                applicantData.put("company_name",resultSet.getString("company_name"));
                applicantData.put("company_number", resultSet.getString("company_mobile_number"));
                applicantData.put("company_address", resultSet.getString("company_address"));
                applicantData.put("staff_id", resultSet.getString("staff_id"));
                applicantData.put("occupation", resultSet.getString("occupation"));
                applicantData.put("employment_date", resultSet.getDate("employment_date"));
                applicantData.put("basic_salary", resultSet.getDouble("basic_salary"));
                applicantData.put("gross_salary", resultSet.getDouble("gross_salary"));
                applicantData.put("net_salary", resultSet.getDouble("net_salary"));
                applicantData.put("total_deduction", resultSet.getDouble("total_deduction"));

                applicantData.put("guarantor_name", resultSet.getString("guarantor_name"));
                applicantData.put("guarantor_number", resultSet.getString("guarantor_number"));
                applicantData.put("guarantor_gender", resultSet.getString("guarantor_gender"));
                applicantData.put("guarantor_address", resultSet.getString("guarantor_digital_address"));
                applicantData.put("guarantor_landmark", resultSet.getString("guarantor_landmark"));
                applicantData.put("guarantor_idType", resultSet.getString("guarantor_idType"));
                applicantData.put("guarantor_idNumber", resultSet.getString("guarantor_idNumber"));
                applicantData.put("relationship", resultSet.getString("guarantor_relationship"));
                applicantData.put("guarantor_occupation", resultSet.getString("guarantor_occupation"));
                applicantData.put("place_of_work", resultSet.getString("guarantor_place_of_work"));
                applicantData.put("institution_address", resultSet.getString("guarantor_institution_address"));
                applicantData.put("income", resultSet.getDouble("guarantor_income"));

                applicantData.put("contact_name", resultSet.getString(""));

            }
            getConnection().close();
        }catch (SQLException e) {
            String className = this.getClass().getName();
            logError.logMessage(String.valueOf(e.getCause()), "getLoanApplicantDataByLoanNumber", className);
        }
        return applicantData;
    }

    public int cancelLoanApplication(Map<String, Object> values) {
        AtomicInteger flag = new AtomicInteger(0);
        try {
            String query = """
                    UPDATE loans SET application_status = ?, loan_status = ?, date_modified = DEFAULT\s
                    WHERE loan_no = ?;
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, values.get("applicationStatus").toString());
            preparedStatement.setString(2, values.get("loanStatus").toString());
            preparedStatement.setString(3, values.get("loanNumber").toString());
            flag.getAndAdd(preparedStatement.executeUpdate());

            String query2 = """
                    INSERT INTO terminated_loans(loan_no, purpose, terminated_by, date_terminated)
                    VALUES(?, ?, ?, DEFAULT);
                    """;
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, values.get("loanNumber").toString());
            preparedStatement.setString(2, values.get("purpose").toString());
            preparedStatement.setInt(3, Integer.parseInt(values.get("userId").toString()));
            flag.getAndAdd(preparedStatement.executeUpdate());
            preparedStatement.close();
            getConnection().close();
        }catch (Exception e) {
            String className = this.getClass().getName();
            String error = Arrays.toString(e.getStackTrace());
            logError.logMessage(className, "cancelLoanApplication", error);
        }
        return flag.get();

    }




}//end of class...
