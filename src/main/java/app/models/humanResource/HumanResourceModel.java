package app.models.humanResource;

import app.fetchedData.human_resources.EmployeesData;
import app.models.MainModel;

import java.sql.Date;
import java.sql.SQLException;

public class HumanResourceModel extends MainModel {
    protected int addNewEmployee(EmployeesData emp) {
        int flag = 0;
        try {
//                    work_id, firstname, lastname, othername, email, mobile_number, other_number,
//                    gender, dbo, digital_address, residential_address, landmark, id_type,
//                    id_number, marital_status, qualification, designation, working_experience, employment_date,
//                    contact_person_name, contact_person_number, contact_person_digital_address, contact_person_address, contact_person_landmark,
//                    contact_person_place_of_work, contact_person_org_number, contact_person_org_address,
//                    additional_information, added_by, modified_by
            String query = "INSERT INTO employees(work_id, firstname, lastname, othername, email, mobile_number, other_number, gender, dbo, digital_address, residential_address, landmark, id_type, id_number, marital_status, qualification, designation, working_experience, employment_date, contact_person_name, contact_person_number, contact_person_digital_address, contact_person_address, contact_person_landmark, contact_person_place_of_work, contact_person_org_number, contact_person_org_address, additional_information, added_by, modified_by)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? ,? , ? , ? , ?, ?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, emp.getWork_id());
            preparedStatement.setString(2, emp.getFirstname());
            preparedStatement.setString(3, emp.getLastname());
            preparedStatement.setString(4, emp.getOthername());
            preparedStatement.setString(5, emp.getEmail());
            preparedStatement.setString(6, emp.getMobile_number());
            preparedStatement.setString(7, emp.getOther_number());
            preparedStatement.setString(8, emp.getGender());
            preparedStatement.setDate(9, Date.valueOf(emp.getDbo()));
            preparedStatement.setString(10, emp.getDigital_address());
            preparedStatement.setString(11, emp.getResidential_address());
            preparedStatement.setString(12, emp.getLandmark());
            preparedStatement.setString(13, emp.getId_type());
            preparedStatement.setString(14, emp.getId_number());
            preparedStatement.setString(15, emp.getMarital_status());
            preparedStatement.setString(16, emp.getQualification());
            preparedStatement.setString(17, emp.getDesignation());
            preparedStatement.setString(18, emp.getWorking_experience());
            preparedStatement.setDate(19, Date.valueOf(emp.getEmployment_date()));
            preparedStatement.setString(20, emp.getContact_person_name());
            preparedStatement.setString(21, emp.getContact_person_number());
            preparedStatement.setString(22, emp.getContact_person_digital_address());
            preparedStatement.setString(23, emp.getContact_person_address());
            preparedStatement.setString(24, emp.getContact_person_landmark());
            preparedStatement.setString(25, emp.getContact_person_place_of_work());
            preparedStatement.setString(26, emp.getContact_person_org_number());
            preparedStatement.setString(27, emp.getContact_person_org_address());
            preparedStatement.setString(28, emp.getAdditional_information());
            preparedStatement.setInt(29, emp.getAdded_by());
            preparedStatement.setInt(30, emp.getModified_by());
            flag = preparedStatement.executeUpdate();
        }catch (Exception e) {e.printStackTrace();};
        return flag;
    }
    protected  int addEmployeeAccountDetails(EmployeesData emp) {
        int flag = 0;
        try {
            String query = "INSERT INTO employees_account_details(emp_id, salary, bank_name, account_name, account_number, modified_by) VALUES(?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, emp.getWork_id());
            preparedStatement.setDouble(2, emp.getSalary());
            preparedStatement.setString(3, emp.getBank_name());
            preparedStatement.setString(4, emp.getAccount_name());
            preparedStatement.setString(5, emp.getAccount_number());
            preparedStatement.setInt(6, emp.getModified_by());
            flag = preparedStatement.executeUpdate();
        }catch (Exception e) {e.printStackTrace();}
        return flag;
    }

}//end of class....
