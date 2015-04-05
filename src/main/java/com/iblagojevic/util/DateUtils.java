package com.iblagojevic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final String DATE_FORMAT = "dd-MMM-y HH:mm:ss";

    public static Date getStartOfDay(Date date) {
        return clearTime(date);
    }

    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    public static Date getDateFromString(String input) {
        Date result = null;
        try {
            result = new SimpleDateFormat(DATE_FORMAT).parse(input);
        }
        catch (ParseException pe) {
            result = new Date();
        }
        return result;
    }

    public static String dateToString(Date input) {
        return new SimpleDateFormat(DATE_FORMAT).format(input);
    }

}
