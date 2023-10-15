package app.enums;

public enum PaymentMethods {
    CASH,
    eCASH,
    BOTH_METHODS,
    MOMO,
    AIRTELTIGO,
    VODA_CASH,
    BANK_TRANSFER;

    public static String convertPayMethod(PaymentMethods methods) {
        String value = "";
        try {
            switch(methods) {
                case CASH -> value = "CASH";
                case eCASH -> value = "E-CASH";
                case BANK_TRANSFER -> value = "BANK TRANSFER";
                case MOMO ->  value = "MOMO";
                case AIRTELTIGO -> value = "AIRTELTIGO CASH";
                case BOTH_METHODS -> value = "CASH & eCASH";
                case VODA_CASH -> value = "VODAFONE CASH";
            }
        }catch (NullPointerException e) {
            value = "Unspecified";
        }
        return value;
    }
}
