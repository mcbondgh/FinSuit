package app.models.message;

import app.errorLogger.ErrorLogger;
import app.models.MainModel;
import app.repositories.operations.MessageLogsEntity;
import com.google.api.services.sqladmin.SQLAdmin;
import io.github.palexdev.materialfx.collections.ObservableStack;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;

public class MessagesModel extends MainModel {

    ErrorLogger errorLogger = new ErrorLogger();
    String className = this.getClass().getSimpleName();
    public void logNotificationMessages(MessageLogsEntity messageLogsEntity) {
        try{
            String query = "INSERT INTO message_logs(sent_to, title, message, Status, sent_by) " +
                    "VALUES(?, ?, ?, ?, ?)";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, messageLogsEntity.getRecipient());
            preparedStatement.setString(2,messageLogsEntity.getTitle());
            preparedStatement.setString(3, messageLogsEntity.getMessage());
            preparedStatement.setString(4, messageLogsEntity.getStatus());
            preparedStatement.setInt(5, messageLogsEntity.getSent_by());
            preparedStatement.execute();
        }catch (Exception e) {
            e.printStackTrace();
            rollBack();}
    }

    public void updateNotificationTable(long tableId) {
        try {
            statement = getConnection().createStatement();
            statement.execute("UPDATE notifications SET `read` = true WHERE(id = '"+tableId+"')");
            statement.close();
            getConnection().close();
        }catch (SQLException ignore) {
        }
    }

    public ObservableList<MessageLogsEntity> getAllMessageLogs() {
        ObservableList<MessageLogsEntity> data = new ObservableStack<>();
        try {
            String query = """
                    SELECT log_id, sent_to, title, message, status, username, sent_date FROM finsuit.message_logs AS ml
                    	INNER JOIN users AS u ON\s
                        u.user_id = ml.sent_by;
                    """;
            resultSet = getConnection().prepareStatement(query).executeQuery();
            while (resultSet.next()) {
//                 sent_to, title, message, Status, sent_date, sent_by
                int id = resultSet.getInt("log_id");
                 String sentTo = resultSet.getString("sent_to");
                 String title = resultSet.getString("title");
                 String message = resultSet.getString("message");
                 String status = resultSet.getString("status");
                 String date = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(resultSet.getTimestamp("sent_date").toLocalDateTime());
                 String username = resultSet.getString("username");
                 data.add(new MessageLogsEntity(id, sentTo, title, message, status, date, username));
            }
        }catch (SQLException ex) {
            errorLogger.logMessage(ex.getMessage(), className);
        }
        return data;
    }

    protected void updateSMSstatus(String status,String message, int logId, int userId) {
        try {
            String query = "UPDATE message_logs SET status = ?, sent_date = DEFAULT, message = ?, sent_by = ?  WHERE(log_id = ?);";
            preparedStatement = getConnection().prepareStatement(query);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, message);
            preparedStatement.setInt(3, userId );
            preparedStatement.setInt(4, logId);
            preparedStatement.execute();
        }catch (Exception e) {}
    }


}// end of class...
