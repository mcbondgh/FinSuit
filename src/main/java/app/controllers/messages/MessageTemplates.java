package app.controllers.messages;

import app.models.MainModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTemplates  {
    MainModel DAO = new MainModel();

    private final StringBuilder stringBuilder = new StringBuilder();
    private final String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a"));

    public String cashDepositMessage(String clientName, String amount, String accountNo, String depositor, String transactionNo, double balance) {
                DAO.getMessageWithOperations().forEach(item -> {
            if (item.getOperation_type().equalsIgnoreCase("Cash Deposit")) {
                String message = item.getMessage().replace("[NAME]", clientName)
                        .replace("[AMOUNT]", "Ghc".concat(amount)).replace("[ACCOUNT NUMBER]", accountNo)
                        .replace("[DATE]", dateTime).replace("[TRANSACTION NO]", transactionNo)
                        .replace("[DEPOSITOR NAME]", depositor).replace("[BALANCE]", String.valueOf("Ghc".concat(String.valueOf(balance))));
                stringBuilder.append(message);
            }
        });
                return stringBuilder.toString();
    }
    public String accountOpeningMessage(String name) {


        return "";
    }
    public static void main(String[] args) {
        MessageTemplates temp = new MessageTemplates();
    }


}//end of class...
