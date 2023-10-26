package app.enums;

public enum MessageStatus {
    //    OK - Successfully Sent
//    100 - Bad gateway request
//    101 - Wrong action
//    102 - Authentication failed
//    103 - Invalid phone number
//    104 - Phone coverage not active
//    105 - Insufficient balance
//    106 - Invalid Sender ID
//    109 - Invalid Schedule Time
//    111 - SMS contains spam word. Wait for approval

    OK,
    BAD_REQUEST,
    AUTHENTICATION_FAILED,
    WRONG_ACTION,
    INVALID_PHONE_NUMBER,
    INSUFFICIENT_BALANCE,
    INVALID_SENDER_ID,
    NO_INTERNET,
    SPAM_WORDS;

    public static Enum<MessageStatus> getMessageStatusResult(String status) {
        Enum<MessageStatus> result = null;
        switch (status) {
            case "ok" -> result = OK;
            case "100" -> result = BAD_REQUEST;
            case "101" -> result = WRONG_ACTION;
            case "102" -> result = AUTHENTICATION_FAILED;
            case "103" -> result = INVALID_PHONE_NUMBER;
            case "105" -> result = INSUFFICIENT_BALANCE;
            case "106" -> result = INVALID_SENDER_ID;
            case "111" -> result =  SPAM_WORDS;
            default -> result = NO_INTERNET;
        }
        return result;
    }






}// end class...
