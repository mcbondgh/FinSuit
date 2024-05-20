package app.specialmethods;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class EasyConvertors {

    private static DateTimeFormatter dateTimeFormatter;
    private static NumberFormat currency;

    @NotNull
    public static String convertDateTime(LocalDateTime dateTime) {
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return dateTimeFormatter.format(dateTime);
    }
    public static String convertDateTime(@NotNull Timestamp dateTime) {
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return dateTimeFormatter.format(dateTime.toLocalDateTime());
    }
    @NotNull
    public static String convertDate(LocalDate date) {
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        return dateTimeFormatter.format(date);
    }

    public static String convertDate(@NotNull Date date) {
        dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
        return dateTimeFormatter.format(date.toLocalDate());
    }

    public static String convertNumber(double number) {
       currency = NumberFormat.getInstance();
       return currency.format(number);
    }


}
