package com.application.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static final String YMD = "yyyy-MM-dd";
    public static final String YMD_HMS_S = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String YMD_HMS_POSTGRE = "yyyy-MM-dd|HH:mm:ss";

    public static long getUseTime(Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        long starttime = calendar.getTimeInMillis();
        calendar.setTime(end);
        long endtime = calendar.getTimeInMillis();
        return endtime - starttime;
    }

    public static void getMs(Date start, Date end) {
        SimpleDateFormat formatter = new SimpleDateFormat(YMD_HMS_S);
        System.out.println("START:" + formatter.format(start));
        System.out.println("END:" + formatter.format(end));
    }

    /**
     * date format with pattern
     */
    public static String getCurrentDate(String formater) {
        SimpleDateFormat formatter = new SimpleDateFormat(formater);
        return formatter.format(new Date());
    }

}
