package app.models.humanResource;

import app.fetchedData.human_resources.EmployeesData;
import app.models.MainModel;
import javafx.collections.ObservableList;

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
            String query = "INSERT INTO employees(work_id, firstname, lastname, othername, email, " +
                    "mobile_number, other_number, gender, dob, digital_address, residential_address, landmark, " +
                    "id_type, id_number, marital_status, qualification, designation, working_experience, employment_date, " +
                    "contact_person_name, contact_person_number, contact_person_digital_address, contact_person_address, " +
                    "contact_person_landmark, contact_person_place_of_work, contact_person_org_number, contact_person_org_address, " +
                    "additional_information, salary, bank_name, account_name, account_number, added_by, modified_by)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ? ,? , ? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
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
            preparedStatement.setDouble(29, emp.getSalary());
            preparedStatement.setString(30, emp.getBank_name());
            preparedStatement.setString(31, emp.getAccount_name());
            preparedStatement.setString(32, emp.getAccount_number());
            preparedStatement.setInt(33, emp.getAdded_by());
            preparedStatement.setInt(34, emp.getModified_by());
            flag = preparedStatement.executeUpdate();
        }catch (Exception e) {e.printStackTrace();};
        return flag;
    }

    protected void updateEmployeeStatus(String empId, byte statusValue) {
        try {
            String query = "UPDATE employees SET is_active = ? WHERE(work_id = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setByte(1, statusValue);
            preparedStatement.setString(2, empId);
            preparedStatement.execute();
        }catch (SQLException ignored) {}

    }
    protected int removeEmployee(String employeeId) {
        int flag = 0;
            try {
                String query = "UPDATE employees SET is_deleted = 1 WHERE(work_id = ?)";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, employeeId);
                flag = preparedStatement.executeUpdate();
            }catch (SQLException ignored) {}
        return flag;
    }

    protected int updateEmployeeDetails(EmployeesData emp, int activeUserId, String emp_id) {
        int flag = 0;
            try {
                String query = "UPDATE employees SET firstname = ?, lastname = ?, othername = ?, email = ?, mobile_number = ?, " +
                        "other_number = ?, gender = ?, dob = ?, digital_address = ?, residential_address = ?, landmark = ?, " +
                        "id_type = ?, id_number = ?, marital_status = ?, qualification = ?, designation = ?, working_experience = ?, " +
                        "employment_date = ?, contact_person_name = ?, contact_person_number = ?, contact_person_digital_address = ?, " +
                        "contact_person_address = ?, contact_person_landmark = ?, contact_person_place_of_work = ?, contact_person_org_number = ?, " +
                        "contact_person_org_address = ?, additional_information = ?, salary = ?, bank_name = ?, account_name = ?, account_number = ?, " +
                        "date_modified = DEFAULT, modified_by = ? WHERE(work_id = ?)";
                preparedStatement = getConnection().prepareStatement(query);
                preparedStatement.setString(1, emp.getFirstname());
                preparedStatement.setString(2, emp.getLastname());
                preparedStatement.setString(3, emp.getOthername());
                preparedStatement.setString(4, emp.getEmail());
                preparedStatement.setString(5, emp.getMobile_number());
                preparedStatement.setString(6, emp.getOther_number());
                preparedStatement.setString(7, emp.getGender());
                preparedStatement.setDate(8, Date.valueOf(emp.getDbo()));
                preparedStatement.setString(9, emp.getDigital_address());
                preparedStatement.setString(10, emp.getResidential_address());
                preparedStatement.setString(11, emp.getLandmark());
                preparedStatement.setString(12, emp.getId_type());
                preparedStatement.setString(13, emp.getId_number());
                preparedStatement.setString(14, emp.getMarital_status());
                preparedStatement.setString(15, emp.getQualification());
                preparedStatement.setString(16, emp.getDesignation());
                preparedStatement.setString(17, emp.getWorking_experience());
                preparedStatement.setDate(18, Date.valueOf(emp.getEmployment_date()));
                preparedStatement.setString(19, emp.getContact_person_name());
                preparedStatement.setString(20, emp.getContact_person_number());
                preparedStatement.setString(21, emp.getContact_person_digital_address());
                preparedStatement.setString(22, emp.getContact_person_address());
                preparedStatement.setString(23, emp.getContact_person_landmark());
                preparedStatement.setString(24, emp.getContact_person_place_of_work());
                preparedStatement.setString(25, emp.getContact_person_org_number());
                preparedStatement.setString(26, emp.getContact_person_org_address());
                preparedStatement.setString(27, emp.getAdditional_information());
                preparedStatement.setDouble(28, emp.getSalary());
                preparedStatement.setString(29, emp.getBank_name());
                preparedStatement.setString(30, emp.getAccount_name());
                preparedStatement.setString(31, emp.getAccount_number());
                preparedStatement.setInt(32, activeUserId);
                preparedStatement.setString(33, emp_id);
                flag = preparedStatement.executeUpdate();
            }catch (SQLException e) {e.printStackTrace();}

        return flag;
    }

}//end of class....
