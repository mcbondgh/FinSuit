package app.models.finance;

import app.models.MainModel;
import app.repositories.business.BusinessInfoEntity;
import app.repositories.business.BusinessTransactionLogs;
import app.repositories.notifications.NotificationEntity;

import java.sql.SQLException;

public class FinanceModel extends MainModel {

    /*
    * This method when invoked shall update business account balance
    * Create a log of the specified transaction and
    * Create a notification to indicate a successful transaction. This method is responsible
    * for the execution of the transfer of cash to and from bank account and the business account only.
    */
    protected int executeSystemAccountTransaction(BusinessInfoEntity entity, BusinessTransactionLogs transactionLogs, NotificationEntity notificationEntity) {
        int flag = 0;
        try{
            String updateQuery = "UPDATE business_info SET\n" +
                    "\taccount_balance = '"+entity.getAccountBalance()+"', " +
                    "previous_balance = '"+entity.getPreviousBalance()+"', " +
                    "account_date_modified = DEFAULT;";
            preparedStatement = getConnection().prepareStatement(updateQuery);
            flag = preparedStatement.executeUpdate();

            String insertQuery = "INSERT INTO business_transaction_logs(transaction_type, bank_name, amount, transaction_id, account_number, transaction_date, notes, created_by)\n" +
                    "VALUES(?,?,?,?,?,?,?,?);";
            preparedStatement = getConnection().prepareStatement(insertQuery);
            preparedStatement.setString(1, transactionLogs.getTransaction_type());
            preparedStatement.setString(2, transactionLogs.getBank_name());
            preparedStatement.setDouble(3, transactionLogs.getAmount());
            preparedStatement.setString(4, transactionLogs.getTransaction_id());
            preparedStatement.setString(5, transactionLogs.getAccount_number());
            preparedStatement.setDate(6, transactionLogs.getTransaction_date());
            preparedStatement.setString(7, transactionLogs.getNotes());
            preparedStatement.setInt(8, transactionLogs.getCreated_by());
            flag += preparedStatement.executeUpdate();

            String query = "INSERT INTO notifications(title, sender_method, message, logged_by)\n" +
                    "VALUES(?,?,?,?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, notificationEntity.getTitle());
            preparedStatement.setString(2, notificationEntity.getSender_method());
            preparedStatement.setString(3, notificationEntity.getMessage());
            preparedStatement.setInt(4, notificationEntity.getLogged_by());
            flag += preparedStatement.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}

        return flag;
    }



}//end of class...
