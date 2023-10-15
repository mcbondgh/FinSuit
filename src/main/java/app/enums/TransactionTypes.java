package app.enums;

public enum TransactionTypes {
    CASH_DEPOSIT,
    CASH_WITHDRAWAL,
    LOAN_PAYMENT,
    INVESTMENT;

    public static String convertType(TransactionTypes type) {
        String value = "";
        switch (type) {
            case CASH_DEPOSIT -> value = "CASH DEPOSIT";
            case CASH_WITHDRAWAL -> value = "CASH WITHDRAWAL";
            case LOAN_PAYMENT -> value = "LOAN PAYMENT";
            case INVESTMENT -> value = "INVESTMENT";
        }
        return value;
    }
}
