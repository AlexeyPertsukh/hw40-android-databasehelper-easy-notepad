package com.example.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataTimeUtil {
    private DataTimeUtil() {
    }

    public static String getStringCurrentDateTime() {
        Date currentDate = new Date();
        String sDate = dateToString(currentDate);
        String sTime = timeToString(currentDate);
        return sDate + " " + sTime;
    }

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String timeToString(Date date) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(date);
    }
}
