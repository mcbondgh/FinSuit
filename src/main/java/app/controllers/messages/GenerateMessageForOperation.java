package app.controllers.messages;

import app.models.MainModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateMessageForOperation {
    MainModel DAO = new MainModel();

    private final StringBuilder stringBuilder = new StringBuilder();
    private final String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"));
    private String message = null;

    public String cashDepositMessage(String clientName, String amount, String accountNo, String depositor, String transactionNo, double balance) {
                DAO.getMessageWithOperations().forEach(item -> {
            if (item.getOperation_type().equalsIgnoreCase("Cash Deposit")) {
                message = item.getMessage().replace("[NAME]", clientName)
                        .replace("[AMOUNT]", "Ghc".concat(amount)).replace("[ACCOUNT NUMBER]", accountNo)
                        .replace("[DATE]", dateTime).replace("[TRANSACTION NO]", transactionNo)
                        .replace("[DEPOSITOR NAME]", depositor).replace("[BALANCE]", String.valueOf("Ghc".concat(String.valueOf(balance))));
                stringBuilder.append(message);
            }
        });
                return stringBuilder.toString();
    }
    public String accountOpeningMessage(String clientName, String accountType, String accountNumber) {
        DAO.getMessageWithOperations().forEach(item -> {
            if (item.getOperation_type().equalsIgnoreCase("Account Opening")) {
                message = item.getMessage().replace("[NAME]", clientName).replace("[ACCOUNT TYPE]", accountType).replace("[ACCOUNT NUMBER]", accountNumber);
                stringBuilder.append(message);
            }
        });
        return stringBuilder.toString();
    }
//    public static void main(String[] args) {
//        GenerateMessageForOperation temp = new GenerateMessageForOperation();
//    }


}//end of class...
