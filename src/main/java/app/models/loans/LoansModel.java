package app.models.loans;

import app.models.MainModel;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.LoanApplicationEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class LoansModel extends MainModel {

    protected int countTotalLoans(String searchParameter) {
        try {
            String query = "SELECT COUNT(ln.customer_id) AS loan_count FROM loans ln\n" +
                    "INNER JOIN customer_data AS cd\n" +
                    "ON cd.customer_id = ln.customer_id\n" +
                    "INNER JOIN customer_account_data AS cad\n" +
                    "ON cad.customer_id = ln.customer_id\n" +
                    "WHERE cd.id_number = '"+searchParameter+"' OR cad.account_number = '"+searchParameter+"';";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getInt("loan_count");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

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

    protected void getDrafts(Label draft_count, ComboBox<String> accountNumber) {
        try {
            String query = "SELECT COUNT(loan_id) AS drafts FROM loans WHERE is_drafted = 1;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                draft_count.setText(String.valueOf(resultSet.getInt("drafts")));
            }

            String query2 = "SELECT account_number FROM customer_account_data AS cad\n" +
                            "INNER JOIN loans as ln ON \n" +
                            "cad.customer_id = ln.customer_id;";
            statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query2);
            while (resultSet.next()) {
                accountNumber.getItems().add(resultSet.getString("account_number"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected int applyForLoan(LoanApplicationEntity applicationEntity, CustomersDataRepository customer) {
        int flag = 0;
        try {
            String query = "INSERT INTO customer_data(firstname, lastname, othername, gender, dob, age, mobile_number, other_number, email, digital_address, " +
                    "residential_address, key_landmark, marital_status, id_type, id_number, educational_background, " +
                    "contact_person_fullname, contact_person_number, contact_person_gender, " +
                    "contact_person_digital_address, contact_person_id_type, " +
                    "contact_person_id_number, contact_person_place_of_work, institution_address, relationship_to_applicant," +
                    " date_created, created_by)";


        }catch (Exception e) {
            rollBack();
        }


        return flag;
    }





}//end of class...
