package app.models.message;

import app.models.MainModel;
import app.repositories.operations.MessageLogsEntity;

public class MessagesModel extends MainModel {

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


}// end of class...
