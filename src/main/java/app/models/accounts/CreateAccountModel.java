package app.models.accounts;

import app.fetchedData.accounts.CreateAccountDataModel;
import app.models.MainModel;

import java.sql.SQLException;

public class CreateAccountModel extends MainModel {

    int createNewAccount(CreateAccountDataModel accountDataModel) {
        try {
            String query = "INSERT INTO customer_accounts ";
        }catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    };



}//end of class;
