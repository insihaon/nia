package com.codej.base.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static final DateFormat YYYYMMDD_NORMAL = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat YYYYMMDD_MINIMAL = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat YYYYMMDDHHMMSS_NORMAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat YYYYMMDDHHMM_NORMAL = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final DateFormat YYYYMMDDHHMMSS_MINIMAL = new SimpleDateFormat("yyyyMMddHHmmss");

    private DateUtil() {
    }

    public static Timestamp getDate(String sDate) throws Exception {
        return (Timestamp) YYYYMMDDHHMMSS_NORMAL.parse(sDate);
    }

    public static Timestamp getDate(String sDate, String anotherFormat)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(anotherFormat);
        return (Timestamp) sdf.parse(sDate);
    }

    public static Timestamp stringToTimestamp(String sDate) {

        Timestamp tDate = Timestamp.valueOf(sDate);
        return tDate;
    }

    public static String timestampToString(String format, Timestamp timestamp) {
        String sTimeStamp = new SimpleDateFormat(format).format(timestamp);
        return sTimeStamp;
    }

    public static Timestamp longToTimestamp(long sDate) throws Exception {
        Timestamp date = new Timestamp(sDate);
        return date;
    }

    public static String getCurrentTime() {
        return getCurrentTime(null);
    }

    public static String getTimeString(LocalDateTime time, String fmt) {
        return time.format(DateTimeFormatter.ofPattern(fmt));
    }

    public static String getCurrentTime(DateFormat dateFormat) {
        if (dateFormat != null) {
            return dateFormat.format(new Date());
        } else {
            return YYYYMMDDHHMMSS_MINIMAL.format(new Date());
        }
    }

    public static String getCurrentDate() {
        return getCurrentDate(null);
    }

    public static String getCurrentDate(DateFormat dateFormat) {
        if (dateFormat != null) {
            return dateFormat.format(new Date());
        } else {
            return getCurrentDate_YYMMDD();
        }
    }

    public static String getCurrentDate_YYMMDD() {
        return YYYYMMDD_MINIMAL.format(new Date());
    }

    public static String getCurrentDate_YYYYMMDDHHMMSS() {
        return YYYYMMDDHHMMSS_NORMAL.format(new Date());
    }

    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    public static long getTimestampNano() {
        return System.nanoTime();
    }

    public static int getNano() {
        return new Timestamp(getTimestampNano()).getNanos();
    }

    public static String makeDaySequence(int in_index) {
        int index = in_index;
        return String.format("%s#%06d", new Object[] { getCurrentDate_YYMMDD(), index++ });
    }

    // LocalDateTime -> Timestamp로 변경
    public static Timestamp convertTimestamp(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }

    // Timestamp -> LocalDate로 변경
    public static LocalDateTime convertDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

}