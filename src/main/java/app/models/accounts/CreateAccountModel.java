package app.models.accounts;

import app.fetchedData.accounts.AccountBalanceDataModel;
import app.fetchedData.accounts.CreateAccountDataModel;
import app.models.MainModel;

import java.sql.SQLException;

public class CreateAccountModel extends MainModel {

    protected int createNewAccount(CreateAccountDataModel accountDataModel) {
        try {
            String query = "INSERT INTO customer_data(firstname, lastname, " +
                    "othername, gender, dob, age, place_of_birth, mobile_number, other_number, email, " +
                    "digital_address, residential_address, key_landmark, marital_status, name_of_spouse, " +
                    "id_type, id_number, educational_background, additional_comment, contact_person_fullname," +
                    " contact_person_dob, contact_person_number, contact_person_gender, contact_person_landmark, " +
                    "contact_person_education_level, contact_person_digital_address, contact_person_id_type, " +
                    "contact_person_id_number, contact_person_place_of_work, institution_address, institution_number, " +
                    "relationship_to_applicant, created_by) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; //35
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, accountDataModel.getFirstname());
            preparedStatement.setString(2, accountDataModel.getLastname());
            preparedStatement.setString(3, accountDataModel.getOthername());
            preparedStatement.setString(4, accountDataModel.getGender());
            preparedStatement.setDate(5, accountDataModel.getDob());
            preparedStatement.setInt(6, accountDataModel.getAge());
            preparedStatement.setString(7, accountDataModel.getPlace_of_birth());
            preparedStatement.setString(8, accountDataModel.getMobile_number());
            preparedStatement.setString(9, accountDataModel.getOther_number());//other number
            preparedStatement.setString(10, accountDataModel.getEmail());
            preparedStatement.setString(11, accountDataModel.getDigital_address());
            preparedStatement.setString(12, accountDataModel.getResidential_address());
            preparedStatement.setString(13, accountDataModel.getKey_landmark());
            preparedStatement.setString(14, accountDataModel.getMarital_status());
            preparedStatement.setString(15, accountDataModel.getName_of_spouse());
            preparedStatement.setString(16, accountDataModel.getId_type());
            preparedStatement.setString(17, accountDataModel.getId_number());
            preparedStatement.setString(18, accountDataModel.getEducational_background());
            preparedStatement.setString(19, accountDataModel.getAdditional_comment());
            preparedStatement.setString(20, accountDataModel.getContact_person_fullname());
            preparedStatement.setDate(21, accountDataModel.getContact_person_dob());
            preparedStatement.setString(22, accountDataModel.getContact_person_number());
            preparedStatement.setString(23, accountDataModel.getContact_person_gender());
            preparedStatement.setString(24, accountDataModel.getContact_person_landmark());
            preparedStatement.setString(25, accountDataModel.getContact_person_education_level());
            preparedStatement.setString(26, accountDataModel.getContact_person_digital_address());
            preparedStatement.setString(27, accountDataModel.getContact_person_id_type());
            preparedStatement.setString(28, accountDataModel.getContact_person_id_number());
            preparedStatement.setString(29, accountDataModel.getContact_person_place_of_work());
            preparedStatement.setString(30, accountDataModel.getInstitution_address());
            preparedStatement.setString(31, accountDataModel.getInstitution_number());
            preparedStatement.setString(32, accountDataModel.getRelationship_to_applicant());
            preparedStatement.setInt(33, accountDataModel.getCreated_by());
            return preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    };
    protected int createAccountBalance(AccountBalanceDataModel balanceDataModel) {
        try {
            String query = "INSERT INTO customer_account_data(customer_id, account_type, account_number, current_balance, previous_balance, modified_by) VALUES(?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, balanceDataModel.getCustomer_id());
            preparedStatement.setString(2, balanceDataModel.getAccount_type());
            preparedStatement.setString(3, balanceDataModel.getAccount_number());
            preparedStatement.setDouble(4, balanceDataModel.getCurrent_balance());
            preparedStatement.setDouble(5, balanceDataModel.getPrevious_balance());
            preparedStatement.setDouble(6, balanceDataModel.getModified_by());
            return preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    };


}//end of class;
