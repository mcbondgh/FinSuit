package app.enums;

public enum TransactionTypes {
    CASH_DEPOSIT,
    CASH_WITHDRAWAL,
    REPAYMENT,
    DISBURSED_FUND;

    public static String convertType(TransactionTypes type) {
        String value = "";
        switch (type) {
            case CASH_DEPOSIT -> value = "CASH DEPOSIT";
            case CASH_WITHDRAWAL -> value = "CASH WITHDRAWAL";
            case REPAYMENT -> value = "REPAYMENT";
            case DISBURSED_FUND -> value = "DISBURSED FUND";
        }
        return value;
    }
}
