package ru.home.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    // функция прибавления/удаления количества дней (параметр days) у даты из параметра date
    public static Date rollDays(Date date, int days) {
        return roll(date, Calendar.DAY_OF_YEAR, days);
    }

    // функция изменения количества времени относительно даты Date на count временных единиц. Временная единица задается
    // полем calendarField и может быть минутами, секундами,количеством дней в году
    private static Date roll(Date date, int calendarField, int count) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarField, count);
        return calendar.getTime();
    }

    // преобразовать дату к строке в формате ДД.ММ.ГГГГ
    public static String toString(Date birthDay) {
        Format dateFormat = dateFormat();
        return dateFormat.format(birthDay);
    }

    // преобразовать строку в формате ДД.ММ.ГГГГ к дате
    public static Date toDate(String birthDay) throws ParseException {
        SimpleDateFormat dateFormat = dateFormat();
        return ( dateFormat).parse(birthDay);
    }

    // преобразовать текущую дату к строке формата ДД.ММ.ГГГГ
    public static String dateFormatExample() {
        return dateFormat().format(new Date());
    }

    // Вернуть формат ДД.ММ.ГГГГ в виде объекта
    public static SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }
}
