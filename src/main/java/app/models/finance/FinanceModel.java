package app.models.finance;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.business.*;
import app.repositories.notifications.NotificationEntity;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FinanceModel extends MainModel {

    ErrorLogger errorLogger = new ErrorLogger();
    String className = this.getClass().getSimpleName();
    /*
    * This method when invoked shall update business account balance
    * Create a log of the specified transaction and
    * Create a notification to indicate a successful transaction. This method is responsible
    * for the execution of the transfer of cash to and from bank account and the business account only.
    */

    public int updateBusinessAccount(BusinessInfoEntity entity) {
        int flag = 0;
        try {
            String updateQuery = "UPDATE business_info SET\n" +
                    "\taccount_balance = '"+entity.getAccountBalance()+"', " +
                    "previous_balance = '"+entity.getPreviousBalance()+"', " +
                    "account_date_modified = DEFAULT;";
            preparedStatement = getConnection().prepareStatement(updateQuery);
            flag = preparedStatement.executeUpdate();

        }catch (Exception ignore){}
        return flag;
    }
    protected int executeSystemAccountTransaction(BusinessTransactionLogs transactionLogs, NotificationEntity notificationEntity) {
        int flag = 0;
        try{

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

    protected int saveDomesticTransferLog(DomesticTransactionLogsEntity entity){
        int flag = 0;
        try {
            String query = "INSERT INTO domestic_transfer_logs(transfer_type, transferred_to, amount, entered_by)\n" +
                    "VALUES(?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, entity.getTransferTypes());
            preparedStatement.setString(2, entity.getTransferTO());
            preparedStatement.setDouble(3, entity.getAmount());
            preparedStatement.setInt(4, entity.getEnteredBy());
            flag = preparedStatement.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}

        return flag;
    }
    //this method when invoked shall take to parameters to insert or update the temporal_cashier_account based on the cashier's name
    public void modifyTemporalCashierAccountWhenLoaded(String name, double amount) {
        try {
            String query = "INSERT INTO temporal_cashier_account(teller, amount)\n" +
                    "\tVALUES('"+name+"', '"+amount+"')\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "\tamount = '"+amount+"' ;";
            getConnection().createStatement().execute(query);
        }catch (SQLException ignore){}
    }
    public int closeCashierTransactions(ClosedTellerTransactionEntity entity) {
        try {
            String query = "INSERT INTO closed_teller_transaction_logs(\n" +
                    "\tstart_amount, closed_amount, overage_amount, shortage_amount, `comment`,\n" +
                    "\t entered_by\n" +
                    " )\n" +
                    "VALUES(?, ?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setDouble(1, entity.getStartAmount());
            preparedStatement.setDouble(2, entity.getClosedAmount());
            preparedStatement.setDouble(3, entity.getOverageAmount());
            preparedStatement.setDouble(4, entity.getShortageAmount());
            preparedStatement.setString(5, entity.getNotes());
            preparedStatement.setInt(6, entity.getEnteredBy());
            return preparedStatement.executeUpdate();
        }catch (SQLException e){e.printStackTrace();}
        return 0;
    }

    public Map<String, Object> getCashierClosureDetailsByName(String cashierName) {
        Map<String, Object> data = new HashMap<>();
        try{
            String query = "SELECT shortage_amount, overage_amount, closed_amount, `comment`, DATE(entry_date) AS entry_date \n" +
                    "FROM closed_teller_transaction_logs\n" +
                    "INNER JOIN users AS u\n" +
                    "ON u.user_id = entered_by\n" +
                    "WHERE username = '"+cashierName+"' AND is_closed = 0;";
            resultSet = getConnection().createStatement().executeQuery(query);
            if (resultSet.next()) {
                data.put("shortageAmount", resultSet.getDouble("shortage_amount"));
                data.put("overageAmount", resultSet.getDouble("overage_amount"));
                data.put("balance", resultSet.getDouble("closed_amount"));
                data.put("comment", resultSet.getString("comment"));
                data.put("entryDate", resultSet.getDate("entry_date"));
            }
            resultSet.close();
            getConnection().close();
        }catch (SQLException ignore) {}

        return data;
    }

    protected ObservableList<SuspenseAccountEntity> fetchSuspenseAccountData() {
        ObservableList<SuspenseAccountEntity> data = new ObservableStack<>();
        try{
            String query = "SELECT tlogs.id, closed_amount, overage_amount, shortage_amount, entry_date, username\n" +
                    "FROM closed_teller_transaction_logs tlogs\n" +
                    "INNER JOIN users AS u\n" +
                    "ON u.user_id = entered_by\n" +
                    "WHERE is_suspended = 1;";
            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("tlogs.id");
                double closedAmount = resultSet.getDouble("closed_amount");
                double overage = resultSet.getDouble("overage_amount");
                double shortage = resultSet.getDouble("shortage_amount");
                Timestamp date = resultSet.getTimestamp("entry_date");
                String username = resultSet.getString("username");
                data.add(new SuspenseAccountEntity(id, username, date, overage, shortage, closedAmount));
            }
        }catch (SQLException ignore) {}

        return data;
    }
    protected void deleteCashierFromTemporalCashierTable(String cashierName){
        try{
            String query = "DELETE FROM temporal_cashier_account WHERE teller = '"+cashierName+"';";
            getConnection().createStatement().execute(query);
        }catch (Exception ignore){}
    }

    protected int updateClosureTable(ClosedTellerTransactionEntity entity) {
        try{
            String query = "UPDATE closed_teller_transaction_logs \n" +
                    "SET overage_amount = ?, shortage_amount = ?, closed_amount = ?,\n" +
                    "closed_by = ?, is_closed = ?, is_suspended = ?, closure_date = CURRENT_TIMESTAMP() \n" +
                    "WHERE id = ?";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setDouble(1, entity.getOverageAmount());
            preparedStatement.setDouble(2, entity.getShortageAmount());
            preparedStatement.setDouble(3, entity.getClosedAmount());
            preparedStatement.setInt(4, entity.getClosedBy());
            preparedStatement.setByte(5, entity.getIsClosed());
            preparedStatement.setByte(6, entity.getIsSuspended());
            preparedStatement.setLong(7, entity.getId());
            return preparedStatement.executeUpdate();
        }catch (SQLException ignored){}
        return 0;
    }

    protected int updateSuspenseAccount(ClosedTellerTransactionEntity entity) {
        try {
            String query = "UPDATE closed_teller_transaction_logs \n" +
                    "SET overage_amount = ?, shortage_amount = ?, is_suspended = ?\n" +
                    "WHERE id = ?;";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setDouble(1, entity.getOverageAmount());
            preparedStatement.setDouble(2, entity.getShortageAmount());
            preparedStatement.setByte(3, entity.getIsSuspended());
            preparedStatement.setLong(4, entity.getId());
            return preparedStatement.executeUpdate();
        }catch (SQLException e) {e.printStackTrace();}
        return 0;
    }

    public ObservableList<RevenueAccountEntity> getRevenueAccountData() {
        ObservableList<RevenueAccountEntity> data = new ObservableStack<>();
        try {
            var query = """
                    SELECT ral.id, reference_number, amount, entry_type,
                    expenditure_purpose, expenditure_type, username, entry_date
                    FROM revenue_account_logs AS ral
                    INNER JOIN users AS u
                    ON u.user_id = ral.entered_by\s
                    """;
            var query2 = "SELECT * FROM finsuit.revenue_account;";

            resultSet = getConnection().createStatement().executeQuery(query2);
            String balance = null;
            if (resultSet.next()){
              balance =  String.valueOf(resultSet.getDouble("account_balance"));
            }

            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()) {
                int counter = resultSet.getInt("id");
                String reference = resultSet.getString("reference_number");
                String username = resultSet.getString("username");
                String amount = resultSet.getString("amount");
                String type = resultSet.getString("entry_type");
                String date = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(resultSet.getTimestamp("entry_date").toLocalDateTime());
                data.add(new RevenueAccountEntity(counter, balance, reference, type, amount, username, date));
            }
            resultSet.close();
            getConnection().close();
        }catch (Exception e){
            errorLogger.logMessage(e.getLocalizedMessage(), "getRevenueAccountData", className);
        }
        return data;
    }

    public ObservableList<RevenueAccountEntity> getExpenditureTransactionLogs() {
        ObservableList<RevenueAccountEntity> data = new ObservableStack<>();
        try {
            var query = """
                    SELECT ral.id, amount, expenditure_purpose, expenditure_type, entry_date
                    FROM revenue_account_logs AS ral
                    INNER JOIN users AS u
                    ON u.user_id = ral.entered_by\s
                    WHERE(entry_type = 'Expenditure')
                    """;
            resultSet = getConnection().createStatement().executeQuery(query);
            while (resultSet.next()) {
                int counter = resultSet.getInt("id");
                String purpose = resultSet.getString("expenditure_purpose");
                String amount = resultSet.getString("amount");
                String type = resultSet.getString("expenditure_type");
                String date = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(resultSet.getTimestamp("entry_date").toLocalDateTime());
                data.add(new RevenueAccountEntity(counter, amount, date, purpose, type));
            }
            resultSet.close();
            getConnection().close();
        }catch (Exception e){
            errorLogger.logMessage(e.getLocalizedMessage(), "getExpenditureTransactionLogs", className);
            e.printStackTrace();}
        return data;
    }

    public int saveExpenditure(RevenueAccountEntity revenueAccount) {
        AtomicInteger var = new AtomicInteger(0);
        try {
            String query1  = "UPDATE revenue_account SET account_balance = ?, date_updated = DEFAULT;";
            preparedStatement = getConnection().prepareStatement(query1);
            preparedStatement.setDouble(1, Double.parseDouble(revenueAccount.getBalance()));
            var.getAndAdd(preparedStatement.executeUpdate());

            String query2 = "INSERT INTO revenue_account_logs(entry_type, amount, expenditure_purpose, entered_by, expenditure_type)\n" +
                    "VALUES(?, ?, ?, ?, ?);";
            preparedStatement = getConnection().prepareStatement(query2);
            preparedStatement.setString(1, revenueAccount.getEntry_type());
            preparedStatement.setDouble(2, Double.parseDouble(revenueAccount.getAmount()));
            preparedStatement.setString(3, revenueAccount.getExpenditure_purpose());
            preparedStatement.setInt(4, revenueAccount.getEntered_by());
            preparedStatement.setString(5, revenueAccount.getExpenditure_type());
            var.getAndAdd(preparedStatement.executeUpdate());

        }catch (SQLException ex) {
            errorLogger.logMessage(ex.getMessage(), "saveExpenditure", className);
            ex.printStackTrace();
        }
        return var.get();
    }


}//end of class...
