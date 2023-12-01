package app.models.loans;

import app.models.MainModel;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoansModel extends MainModel {

    protected List<Object> countTotalLoans(String searchParameter) {
        List<Object> data = new ArrayList<>();
        try {
            String query = "SELECT COUNT(ln.customer_id) AS loan_count, sum(ln.disbursed_amount) AS 'disbursed_amount' , SUM(total_payment) total_payment FROM loans ln\n" +
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
                    "created_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            String query2 = "INSERT INTO loan_applicant_details(loan_no, profile_picture, company_name, company_mobile_number, company_address, staff_id, occupation, employment_date, " +
                    "basic_salary, gross_salary, total_deduction, net_salary, guarantor_name, guarantor_gender, guarantor_number, guarantor_digital_address," +
                    "guarantor_landmark, guarantor_idType, guarantor_idNumber, guarantor_relationship, guarantor_occupation," +
                    "guarantor_place_of_work, guarantor_institution_address, guarantor_income) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


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
            flag = preparedStatement.executeUpdate();
            commitTransaction();

            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, applicationEntity.getLoan_no());
            preparedStatement.setString(2, applicationEntity.getProfile_picture());
            preparedStatement.setString(3, applicationEntity.getCompany_name());
            preparedStatement.setString(4, applicationEntity.getCompany_mobile_number());
            preparedStatement.setString(5, applicationEntity.getCompany_address());
            preparedStatement.setString(6, applicationEntity.getStaff_id());
            preparedStatement.setString(7, applicationEntity.getOccupation());
            preparedStatement.setDate(8, Date.valueOf(applicationEntity.getEmployment_date()));
            preparedStatement.setDouble(9, applicationEntity.getBasic_salary());
            preparedStatement.setDouble(10, applicationEntity.getGross_salary());
            preparedStatement.setDouble(11, applicationEntity.getTotal_deduction());
            preparedStatement.setDouble(12, applicationEntity.getNet_salary());
            preparedStatement.setString(13, applicationEntity.getGuranter_name());
            preparedStatement.setString(14, applicationEntity.getGender());
            preparedStatement.setString(15, applicationEntity.getGuranter_number());
            preparedStatement.setString(16, applicationEntity.getGuranter_digital_address());
            preparedStatement.setString(17, applicationEntity.getGuarantor_landmark());
            preparedStatement.setString(18, applicationEntity.getGuranter_idType());
            preparedStatement.setString(19, applicationEntity.getGuranter_idNumber());
            preparedStatement.setString(20, applicationEntity.getGuranter_relationship());
            preparedStatement.setString(21, applicationEntity.getOccupation());
            preparedStatement.setString(22, applicationEntity.getGurater_place_of_work());
            preparedStatement.setString(23, applicationEntity.getGuranter_institution_address());
            preparedStatement.setDouble(24, applicationEntity.getNet_salary());
            flag += preparedStatement.executeUpdate();
            commitTransaction();

            preparedStatement.close();
            getConnection().close();
        }catch (Exception e) {
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
            commitTransaction();
            preparedStatement.close();
            getConnection().close();
        } catch (SQLException e) {
            rollBack();
            e.printStackTrace();
        }
        return flag;
    }
    protected ObservableList<LoansTableEntity> getLoansByApplicationStatus() {
        ObservableList<LoansTableEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_id, username, CONCAT(lastname, ' ', firstname) AS fullname, loan_no, loan_type, loan_purpose," +
                    "DATE(ln.date_created) AS application_date, \n" +
                    "requested_amount, application_status FROM loans AS ln\n" +
                    "JOIN customer_data AS cd ON ln.customer_id = cd.customer_id " +
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
                data.add(new LoansTableEntity(no, fullname, loanNo, date, amount, username, status, loanType, loanPurpose));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}
        return data;
    }
    protected ObservableList<LoansTableEntity> getLoansUnderApplicationStage (int user_id) {
        ObservableList<LoansTableEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_id, username, CONCAT(lastname, ' ', firstname) AS fullname, loan_no, loan_type, DATE(ln.date_created) AS application_date, \n" +
                    "requested_amount, loan_purpose, application_status FROM loans AS ln\n" +
                    "JOIN customer_data AS cd ON ln.customer_id = cd.customer_id " +
                    "JOIN users AS u ON ln.created_by = u.user_id WHERE(application_status = 'application' AND (user_id = ? OR user_id = 1));";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, user_id);
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
                data.add(new LoansTableEntity(no, fullname, loanNo, date, amount, username, status, loanType, loanPurpose));
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
        }catch (SQLException ignore){}
        return 0;
    }

    //THIS METHOD WHEN INVOKED SHALL UPDATE THE loans table BASED ON THE ARGUMENTS PARSED TO IT.
    //THIS SHALL CHANGE THE LOAN STATUS OF THE loans TABLE from application to processing...
    public int updateLoanApplicationStatus(String applicationStatus, String loanNo, int userId) {
        int flag = 0;
        try {
            String query = "UPDATE loans SET application_status = ?, date_modified = DEFAULT, updated_by = ? WHERE(loan_no = ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1,applicationStatus);
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, loanNo);
            flag = preparedStatement.executeUpdate();
            commitTransaction();
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
            commitTransaction();
            getConnection().close();
        }catch (Exception e){e.printStackTrace();}
        return flag;
    }
    public ObservableList<String> getGroupSupervisors(String employeeId) {
        ObservableList<String> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT gs.loan_id, emp_id FROM group_supervisors as gs\n" +
                    "INNER JOIN loans AS ln\n" +
                    "ON gs.loan_id = ln.loan_no\n" +
                    "WHERE(gs.emp_id = '"+employeeId+"' AND application_status = 'processing');";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                data.add(resultSet.getString(1));
            }
        }catch (SQLException ignore){}
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
        }catch (Exception ignore){}
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
            String query1 = "UPDATE loans\n" +
                    "SET approved_amount = ?, application_status = 'pending_payment', date_modified = DEFAULT, approved_by = ?\n" +
                    "WHERE(loan_no = ?);";

            String query2 = "UPDATE loan_qualification_values\n" +
                    "SET loan_amount = ?, interest_rate = ?, loan_period = ?, processing_rate = ?, start_date = ?, end_date = ?\n" +
                    "WHERE(loan_no = ?);";

            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setDouble(1, loans.getDisbursed_amount());
            preparedStatement.setInt(2, loans.getApproved_by());
            preparedStatement.setString(3, loans.getLoan_no());
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
        System.out.println(status);
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

    protected int saveDisbursedLoans(String loanNo, int userId) {
        int status = 0;
        try{
            String query = "UPDATE loans SET application_status = 'paid', date_modified = DEFAULT, updated_by = ? WHERE(loan_no = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, loanNo);
            status = preparedStatement.executeUpdate();
        }catch (SQLException ignore){}
        return status;
    }


}//end of class...
