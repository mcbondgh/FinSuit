package app.models.transactions;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.loans.DisbursementEntity;
import app.repositories.transactions.TransactionsEntity;

import java.sql.SQLException;

public class TransactionModel extends MainModel {

    ErrorLogger logger = new ErrorLogger();

    protected int saveDepositTransaction(CustomerAccountsDataRepository account, TransactionsEntity transaction) {
        int flag = 0;
        try {
            String query1 = "UPDATE customer_account_data SET account_balance = ?, previous_balance = ?, date_modified = DEFAULT, modified_by = ? WHERE(account_number = ?);";

            String query2 = "INSERT INTO transaction_logs(account_number, transaction_id, transaction_type, payment_method, payment_gateway, cash_amount, ecash_amount, ecash_id, transaction_made_by, national_id_number, user_id)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setDouble(1, account.getAccount_balance());
            preparedStatement.setDouble(2, account.getPrevious_balance());
            preparedStatement.setInt(3, account.getModified_by());
            preparedStatement.setString(4, account.getAccount_number());
            flag = preparedStatement.executeUpdate();

            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, transaction.getAccount_number());
            preparedStatement.setString(2, transaction.getTransaction_id());
            preparedStatement.setString(3, transaction.getTransaction_type());
            preparedStatement.setString(4, transaction.getPayment_method());
            preparedStatement.setString(5, transaction.getPayment_gateway());
            preparedStatement.setDouble(6, transaction.getCash_amount());
            preparedStatement.setDouble(7, transaction.getEcash_amount());
            preparedStatement.setString(8, transaction.getEcash_id());
            preparedStatement.setString(9, transaction.getTransaction_made_by());
            preparedStatement.setString(10, transaction.getNationalIdNumber());
            preparedStatement.setInt(11, transaction.getUserId());
            flag += preparedStatement.executeUpdate();

        }catch (Exception e) {
            logger.log(e.getCause().toString());
            rollBack();
        }
        return flag;
    }

    public void saveDisbursementTransaction(TransactionsEntity var1) {
        try {
            String query = "INSERT INTO transaction_logs(account_number, transaction_id, transaction_type, payment_method, cash_amount, ecash_amount, user_id)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, var1.getAccount_number());
            preparedStatement.setString(2, var1.getTransaction_id());
            preparedStatement.setString(3, "DISBURSED FUND");
            preparedStatement.setString(4, var1.getPayment_method());
            preparedStatement.setDouble(5, var1.getCash_amount());
            preparedStatement.setDouble(6, var1.getEcash_amount());
            preparedStatement.setInt(7, var1.getUserId());
            preparedStatement.execute();
        }catch (SQLException e){
            logger.log(e.getCause().toString());
            rollBack();}
    }


}//end of class...
