package app.models.transactions;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.accounts.CustomerAccountsDataRepository;
import app.repositories.accounts.CustomersDataRepository;
import app.repositories.loans.DisbursementEntity;
import app.repositories.transactions.TransactionsEntity;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void saveDisbursementTransaction(TransactionsEntity var1, Map<String, Object> var2) {
        try {
            String query = "INSERT INTO transaction_logs(account_number, transaction_id, transaction_type, ecash_amount, user_id)\n" +
                    "VALUES(?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, var1.getAccount_number());
            preparedStatement.setString(2, var1.getTransaction_id());
            preparedStatement.setString(3, "DISBURSED FUND");
            preparedStatement.setDouble(4, var1.getEcash_amount());
            preparedStatement.setInt(5, var1.getUserId());
            preparedStatement.execute();

            String query2 = "UPDATE operations_account \n" +
                    "SET account_balance = ?, updated_by = ?, date_updated = DEFAULT; ";
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setDouble(1, Double.parseDouble(var2.get("balance").toString()));
            preparedStatement.setInt(2, Integer.parseInt(var2.get("userId").toString()));
            preparedStatement.execute();

            String query3 = "INSERT INTO operations_transaction_logs(reference_number, entry_type, amount, entered_by)\n" +
                    "VALUES(?,?,?,?);";
            preparedStatement = getConnection().prepareStatement(query3);
            preparedStatement.setString(1, var2.get("referenceNumber").toString());
            preparedStatement.setString(2, var2.get("entryType").toString());
            preparedStatement.setDouble(3, Double.parseDouble(var2.get("amount").toString()));
            preparedStatement.setInt(4, Integer.parseInt(var2.get("userId").toString()));
            preparedStatement.execute();
        }catch (SQLException e){
            e.printStackTrace();
            logger.log(e.getMessage());
            rollBack();}
    }

    protected int saveWithdrawal(CustomerAccountsDataRepository var) {
        AtomicInteger status = new AtomicInteger();
        try {
            String query1 =
                    """
                        UPDATE customer_account_data SET account_balance = ?, previous_balance = ?,
                        date_modified = DEFAULT, modified_by = ? WHERE(account_number = ?);
                    """;

            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setDouble(1, var.getAccount_balance());
            preparedStatement.setDouble(2, var.getPrevious_balance());
            preparedStatement.setInt(3, var.getModified_by());
            preparedStatement.setString(4, var.getAccount_number());
            status.setRelease(preparedStatement.executeUpdate());

        }catch (SQLException e) {e.printStackTrace();}
        return status.get();
    }

    protected int saveWithdrawalTransaction(TransactionsEntity var) {
        AtomicInteger status = new AtomicInteger();
        try{
            String query = "INSERT INTO transaction_logs(\n" +
                    "\t\taccount_number, transaction_id, transaction_type,\n" +
                    "        payment_method, payment_gateway, cash_amount, ecash_amount, transaction_tax,\n" +
                    "        ecash_id, transaction_made_by, national_id_number, user_id)\n" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, var.getAccount_number());
            preparedStatement.setString(2, var.getTransaction_id());
            preparedStatement.setString(3, var.getTransaction_type());
            preparedStatement.setString(4, var.getPayment_method());
            preparedStatement.setString(5, var.getPayment_gateway());
            preparedStatement.setDouble(6, var.getCash_amount());
            preparedStatement.setDouble(7, var.getEcash_amount());
            preparedStatement.setDouble(8, var.getTransactionTax());
            preparedStatement.setString(9, var.getEcash_id());
            preparedStatement.setString(10, var.getTransaction_made_by());
            preparedStatement.setString(11, var.getNationalIdNumber());
            preparedStatement.setInt(12, var.getUserId());
            status.setRelease(preparedStatement.executeUpdate());
        }catch (SQLException e){e.printStackTrace();}

        return status.get();
    }


}//end of class...
