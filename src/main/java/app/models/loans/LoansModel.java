package app.models.loans;

import app.models.MainModel;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.LoanApplicationEntity;
import app.repositories.loans.LoansTableEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.SQLException;
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

            String query2 = "INSERT INTO loan_applicant_details(loan_no, company_name, company_mobile_number, company_address, staff_id, occupation, employment_date, " +
                    "basic_salary, gross_salary, total_deduction, net_salary, guarantor_name, guarantor_gender, guarantor_number, guarantor_digital_address," +
                    "guarantor_landmark, guarantor_idType, guarantor_idNumber, guarantor_relationship, guarantor_occupation," +
                    "guarantor_place_of_work, guarantor_institution_address, guarantor_income) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


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
            preparedStatement.setString(2, applicationEntity.getCompany_name());
            preparedStatement.setString(3, applicationEntity.getCompany_mobile_number());
            preparedStatement.setString(4, applicationEntity.getCompany_address());
            preparedStatement.setString(5, applicationEntity.getStaff_id());
            preparedStatement.setString(6, applicationEntity.getOccupation());
            preparedStatement.setDate(7, Date.valueOf(applicationEntity.getEmployment_date()));
            preparedStatement.setDouble(8, applicationEntity.getBasic_salary());
            preparedStatement.setDouble(9, applicationEntity.getGross_salary());
            preparedStatement.setDouble(10, applicationEntity.getTotal_deduction());
            preparedStatement.setDouble(11, applicationEntity.getNet_salary());
            preparedStatement.setString(12, applicationEntity.getGuranter_name());
            preparedStatement.setString(13, applicationEntity.getGender());
            preparedStatement.setString(14, applicationEntity.getGuranter_number());
            preparedStatement.setString(15, applicationEntity.getGuranter_digital_address());
            preparedStatement.setString(16, applicationEntity.getGuarantor_landmark());
            preparedStatement.setString(17, applicationEntity.getGuranter_idType());
            preparedStatement.setString(18, applicationEntity.getGuranter_idNumber());
            preparedStatement.setString(19, applicationEntity.getGuranter_relationship());
            preparedStatement.setString(20, applicationEntity.getOccupation());
            preparedStatement.setString(21, applicationEntity.getGurater_place_of_work());
            preparedStatement.setString(22, applicationEntity.getGuranter_institution_address());
            preparedStatement.setDouble(23, applicationEntity.getNet_salary());
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
    protected int createLoan(long customerId, String loanNo, String loanType, double requestedAmount, int userId) {
        int flag = 0;
        try {
            String query = "INSERT INTO loans(customer_id, loan_no, loan_type, is_drafted, requested_amount, created_by) VALUES(?, ?, ?, ?, ?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setString(2, loanNo);
            preparedStatement.setString(3, loanType);
            preparedStatement.setDouble(4, 0);
            preparedStatement.setDouble(5, requestedAmount);
            preparedStatement.setInt(6, userId);
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
    protected ObservableList<LoansTableEntity> getLoansUnderProcessingStage (int user_id) {
        ObservableList<LoansTableEntity> data = FXCollections.observableArrayList();
        try {
            String query = "SELECT loan_id, username, CONCAT(lastname, ' ', firstname) AS fullname, loan_no, loan_type, DATE(ln.date_created) AS application_date, \n" +
                    "requested_amount, application_status FROM loans AS ln\n" +
                    "JOIN customer_data AS cd ON ln.customer_id = cd.customer_id " +
                    "JOIN users AS u ON ln.created_by = u.user_id WHERE(application_status = 'processing' AND user_id = ? OR user_id = 1);";
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
                data.add(new LoansTableEntity(no, fullname, loanNo, date, amount, username, status, loanType));
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
                    "requested_amount, application_status FROM loans AS ln\n" +
                    "JOIN customer_data AS cd ON ln.customer_id = cd.customer_id " +
                    "JOIN users AS u ON ln.created_by = u.user_id WHERE(application_status = 'application' AND user_id = ?);";
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
                data.add(new LoansTableEntity(no, fullname, loanNo, date, amount, username, status, loanType));
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
    protected  int updateLoanApplicationStatus(String employeeId, String loanNo, int userId) {
        int flag = 0;
        try {
            String query = "UPDATE loans SET application_status = 'processing', date_modified = DEFAULT, updated_by = ?, employee_id = ? WHERE(loan_no = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, employeeId);
            preparedStatement.setString(3, loanNo);
            flag = preparedStatement.executeUpdate();
            commitTransaction();
            getConnection().close();
        }catch (Exception e){rollBack();}
        return flag;
    }

}//end of class...
