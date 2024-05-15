package app.enums;

public enum PaymentMethods {
    CASH(),
    eCASH,
    BOTH_METHODS,
    MOMO,
    AT_CASH,
    T_CASH,
    BANK_TRANSFER;

    public static String convertPayMethod(PaymentMethods methods) {
        String value = "";
        try {
            switch(methods) {
                case CASH -> value = "CASH";
                case eCASH -> value = "E-CASH";
                case BANK_TRANSFER -> value = "BANK TRANSFER";
                case MOMO ->  value = "MOMO";
                case AT_CASH -> value = "AT CASH";
                case BOTH_METHODS -> value = "CASH & eCASH";
                case T_CASH -> value = "TELECEL CASH";
            }
        }catch (NullPointerException e) {
            value = "Unspecified";
        }
        return value;
    }
}
