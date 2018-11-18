package ru.home.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date rollDays(Date date, int days) {
        return roll(date, Calendar.DAY_OF_YEAR, days);
    }

    private static Date roll(Date date, int calendarField, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarField, count);
        return calendar.getTime();
    }

    public static String toString(Date birthDay) {
        Format dateFormat = dateFormat();
        return dateFormat.format(birthDay);
    }

    public static Date toDate(String birthDay) throws ParseException {
        SimpleDateFormat dateFormat = dateFormat();
        return ( dateFormat).parse(birthDay);
    }

    public static String dateFormatExample() {
        return dateFormat().format(currentDate());
    }

    public static SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }
}
