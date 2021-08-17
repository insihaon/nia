package com.nia.ping.alarm.preprocessor.common;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtlDateHelper {
    public static String sFormat = "yyyy/MM/dd-HH:mm:ss";

    public static Timestamp getDate(String sDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        return (Timestamp) sdf.parse(sDate);
    }

    public static Timestamp getDate(String sDate, String anotherFormat)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(anotherFormat);
        return (Timestamp) sdf.parse(sDate);
    }
    
    public static Timestamp stringToTimestamp(String sDate){
    	
    	Timestamp tDate = Timestamp.valueOf(sDate);
    	return tDate;
    }
    
    public static String timestampToString(String format, Timestamp timestamp){
    	String sTimeStamp = new SimpleDateFormat(format).format(timestamp);
		return sTimeStamp;
    }
    
    public static Timestamp longToTimestamp(long sDate){
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        Timestamp date = new Timestamp(sDate);
        return date;
    }
    
    public static Timestamp getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datetime1 = dayTime.format(cal.getTime());
		Timestamp tDate = Timestamp.valueOf(datetime1);
		
        return tDate;
    }
    
    public static String getCurrentDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
		String datetime1 = dayTime.format(cal.getTime());
        return datetime1;
    }
}