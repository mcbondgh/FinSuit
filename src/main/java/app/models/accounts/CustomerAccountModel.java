package app.models.accounts;

import app.models.MainModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.accounts.CustomersDocumentRepository;
import app.repositories.accounts.ViewCustomersTableDataRepository;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CustomerAccountModel extends MainModel {

    protected int createNewAccount(CustomersDataRepository accountDataModel) {
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
    protected int createAccountBalance(CustomerAccountsDataRepository balanceDataModel) {
        try {
            String query = "INSERT INTO customer_account_data(customer_id, account_type, account_number, account_balance, previous_balance, modified_by) VALUES(?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, balanceDataModel.getCustomer_id());
            preparedStatement.setString(2, balanceDataModel.getAccount_type());
            preparedStatement.setString(3, balanceDataModel.getAccount_number());
            preparedStatement.setDouble(4, balanceDataModel.getAccount_balance());
            preparedStatement.setDouble(5, balanceDataModel.getPrevious_balance());
            preparedStatement.setDouble(6, balanceDataModel.getModified_by());
            return preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    };

    protected ObservableList<ViewCustomersTableDataRepository> fetchCustomersDataSummary() {
        ObservableList<ViewCustomersTableDataRepository> data = new ObservableStack<>();
        try {
            String query = "SELECT concat(firstname, \" \", lastname, \" \", othername) AS fullname, gender, age, mobile_number,\n" +
                    "\tid_type, account_type, account_number, date_created FROM customer_data AS cd \n" +
                    "    INNER JOIN customer_account_data AS cad ON cd.customer_id = cad.customer_id;";
            preparedStatement = getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String fullname = resultSet.getString("fullname");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String mobile = resultSet.getString("mobile_number");
                String id_type = resultSet.getString("id_type");
                String account_number = resultSet.getString("account_number");
                String account_type = resultSet.getString("account_type");
                Timestamp date = resultSet.getTimestamp("date_created");
                data.add(new ViewCustomersTableDataRepository(fullname, gender, age, mobile, id_type, account_type, account_number, date));
            }
        }catch (SQLException ignore){}
        return data;
    }
    protected void updateAccountType(String accountType, String accountNumber) {
        try {
            String query = "UPDATE customer_account_data SET account_type = ? WHERE(account_number = ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, accountType);
            preparedStatement.setString(2, accountNumber);
            preparedStatement.execute();
        }catch (Exception e) {e.printStackTrace();}
    }

    protected void saveDocument(CustomersDocumentRepository documentRepository) {
        try {
            String query = "INSERT INTO customer_document(customer_id, document_name, file_content, reason_for_upload, uploaded_by) VALUES(?,?,?,?,?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, documentRepository.getCustomer_id());
            preparedStatement.setString(2, documentRepository.getDocument_name());
            preparedStatement.setBytes(3, documentRepository.getFile_content());
            preparedStatement.setString(4, documentRepository.getReason_for_upload());
            preparedStatement.setInt(5, documentRepository.getUploaded_by());
            preparedStatement.execute();
        }catch (Exception e) {e.printStackTrace();}
    }

    protected int getCustomerIdByAccountNumber(String accountNumber){
        try {
            String query = "SELECT customer_id FROM customer_account_data WHERE (account_number = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, accountNumber);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (Exception ignore) {}
        return 0;
    }

    protected int updateDocument(CustomersDocumentRepository documentRepository) {
        int flag = 0;
        //doc_id, customer_id, document_name, file_content, reason_for_upload, date_uploaded, date_modified, uploaded_by, modified_by
        try {
            String query = """
                        UPDATE customer_document SET document_name = ?, file_name = ?, reason_for_upload = ?, date_modified = ?, modified_by = ?
                        WHERE(customer_id = ?);
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, documentRepository.getDocument_name());
            preparedStatement.setBytes(2, documentRepository.getFile_content());
            preparedStatement.setString(3, documentRepository.getReason_for_upload());
            preparedStatement.setTimestamp(4, documentRepository.getDate_modified());
            preparedStatement.setInt(5, documentRepository.getModified_by());
            preparedStatement.setLong(6, documentRepository.getCustomer_id());
            flag = preparedStatement.executeUpdate();
            commitTransaction();
        }catch (Exception e){rollBack();}

        return flag;
    }

    protected int updateCustomerAccountData(CustomersDataRepository repo) {
        int flag = 0;
        try {
            //place_of_birth, mobile_number, other_number, email, digital_address, residential_address, key_landmark, marital_status,
            // name_of_spouse, id_type, id_number, educational_background, additional_comment, contact_person_fullname,
            // contact_person_dob, contact_person_number, contact_person_gender, contact_person_landmark, contact_person_education_level,
            // contact_person_digital_address, contact_person_id_type, contact_person_id_number, contact_person_place_of_work,
            // institution_address, institution_number, relationship_to_applicant, date_created, is_active, created_by, date_modified, modified_by
            String query = """
                    UPDATE customer_data SET firstname = ?, lastname = ?, othername = ?, dob = ?, age = ?, place_of_birth = ?, mobile_number = ?, other_number = ?, 
                    email = ?, digital_address = ?, residential_address = ?, key_landmark = ?, marital_status = ?, name_of_spouse = ?, id_type = ?, 
                    id_number = ?, educational_background = ?, additional_comment = ?, contact_person_fullname = ?, contact_person_dob = ?, contact_person_number = ?,
                    contact_person_gender = ?, contact_person_landmark = ?, contact_person_education_level = ?, contact_person_digital_address = ?, contact_person_id_type = ?,
                    contact_person_id_number = ?, contact_person_place_of_work = ?, institution_address = ?, institution_number = ?, relationship_to_applicant = ?, 
                    date_modified = DEFAULT, modified_by = ? WHERE(customer_id = ?);
                    """;
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, repo.getFirstname());
            preparedStatement.setString(2, repo.getLastname());
            preparedStatement.setString(3, repo.getOthername());
            preparedStatement.setDate(4, repo.getDob());
            preparedStatement.setInt(5, repo.getAge());
            preparedStatement.setString(6, repo.getPlace_of_birth());
            preparedStatement.setString(7, repo.getMobile_number());
            preparedStatement.setString(8, repo.getOther_number());
            preparedStatement.setString(9, repo.getEmail());
            preparedStatement.setString(10, repo.getDigital_address());
            preparedStatement.setString(11, repo.getResidential_address());
            preparedStatement.setString(12, repo.getKey_landmark());
            preparedStatement.setString(13, repo.getMarital_status());
            preparedStatement.setString(14, repo.getName_of_spouse());
            preparedStatement.setString(15, repo.getId_type());
            preparedStatement.setString(16, repo.getId_number());
            preparedStatement.setString(17, repo.getEducational_background());
            preparedStatement.setString(18, repo.getAdditional_comment());
            preparedStatement.setString(19, repo.getContact_person_fullname());
            preparedStatement.setDate(20, repo.getContact_person_dob());
            preparedStatement.setString(21, repo.getContact_person_number());
            preparedStatement.setString(22, repo.getContact_person_gender());
            preparedStatement.setString(23, repo.getContact_person_landmark());
            preparedStatement.setString(24, repo.getContact_person_education_level());
            preparedStatement.setString(25, repo.getContact_person_digital_address());
            preparedStatement.setString(26, repo.getContact_person_id_type());
            preparedStatement.setString(27, repo.getContact_person_id_number());
            preparedStatement.setString(28, repo.getContact_person_place_of_work());
            preparedStatement.setString(29, repo.getInstitution_address());
            preparedStatement.setString(30, repo.getInstitution_number());
            preparedStatement.setString(31, repo.getRelationship_to_applicant());
            preparedStatement.setLong(32, repo.getAccount_Id());
            flag = preparedStatement.executeUpdate();
            commitTransaction();
        }catch (Exception e) {rollBack();}
        return flag;
    }

    public ArrayList<CustomersDocumentRepository> getLatestDocumentUpload(long customer_id) {
        CustomersDocumentRepository documentRepository = new CustomersDocumentRepository();
        ArrayList<CustomersDocumentRepository> data = new ArrayList<>();
        try {
            String query = "SELECT * FROM customer_document WHERE( customer_id = ?) ORDER BY doc_id DESC LIMIT 1;";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setLong(1, customer_id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                documentRepository.setDocument_name(resultSet.getString("document_name"));
                documentRepository.setFile_content(resultSet.getBytes("file_content"));
                documentRepository.setReason_for_upload(resultSet.getString("reason_for_upload"));
                data.add(documentRepository);
            }
            commitTransaction();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

}//end of class;
