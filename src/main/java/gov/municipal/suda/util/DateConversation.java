package gov.municipal.suda.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateConversation {

//    public static final LocalDate getLastDayOfMonth(String formatter, String date) {
//        LocalDate convertedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(formatter));
//        convertedDate = convertedDate.withDayOfMonth(
//                convertedDate.getMonth().length(convertedDate.isLeapYear()));
//       return convertedDate;
//    }

    public static final int getLastDayOfMonth(String formatter, String dateString) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(formatter);
        YearMonth yearMonth = YearMonth.parse(dateString, pattern);
        LocalDate date = yearMonth.atEndOfMonth();
        return date.lengthOfMonth();
    }

    public static final String change_DD_MM_YYYY_To_YYYY_MM_DD(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter).format(formatter2);
    }
}
