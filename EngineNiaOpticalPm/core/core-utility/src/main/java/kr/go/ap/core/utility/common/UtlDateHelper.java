package kr.go.ap.core.utility.common;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@UtilityClass
public class UtlDateHelper {
    public static String sFormat = "yyyy/MM/dd-HH:mm:ss";

    public static Timestamp getDate(String sDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        return (Timestamp) sdf.parse(sDate);
    }

    public static Timestamp getDate(String sDate, String anotherFormat)
            throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(anotherFormat);
        return longToTimestamp(sdf.parse(sDate).getTime());
    }

    public static Timestamp stringToTimestamp(String sDate) throws IllegalArgumentException {

        Timestamp tDate = Timestamp.valueOf(sDate);
        return tDate;
    }

    public static Timestamp stringToTimestamp2(String sDate) {
        Timestamp tDate = null;

        try {
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formatDate = dtFormat.parse(sDate);
            String strNewDtFormat = newDtFormat.format(formatDate);

            tDate = Timestamp.valueOf(strNewDtFormat);
        } catch (ParseException e) {
            return tDate;
        }
        return tDate;
    }

    public static String timestampToString(String format, Timestamp timestamp) {
        String sTimeStamp = new SimpleDateFormat(format).format(timestamp);
        return sTimeStamp;
    }

    public static Timestamp longToTimestamp(long sDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
        Timestamp date = new Timestamp(sDate);
        return date;
    }

    public static Timestamp getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime1 = dayTime.format(cal.getTime());
        Timestamp tDate = Timestamp.valueOf(datetime1);

        return tDate;
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
        String datetime1 = dayTime.format(cal.getTime());
        return datetime1;
    }

    public static String getPartitionKey() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMM");
        String datetime1 = dayTime.format(cal.getTime());
        return datetime1;
    }

    public static String getCurrentDate(String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dayTime = new SimpleDateFormat(format);
        String datetime1 = dayTime.format(cal.getTime());
        return datetime1;
    }

    public static String selectPartitionKey() {
        String curPartitionKey = getPartitionKey();

        return String.valueOf(Integer.parseInt(curPartitionKey) - 1);
    }

    public static Timestamp subtractDaysFromCurrentDate(int daysToSubtract) {
        // 현재 날짜 및 시간 가져오기
        LocalDateTime currentDate = LocalDateTime.now();

        // 입력된 날짜만큼 빼기
        LocalDateTime resultDate = currentDate.minusDays(daysToSubtract);

        // LocalDateTime을 Timestamp로 변환
        Timestamp resultTimestamp = Timestamp.valueOf(resultDate);

        return resultTimestamp;
    }

    public static Timestamp subtractMinutes(Timestamp timestamp, int minutes) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }

        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        LocalDateTime updatedTime = localDateTime.minusMinutes(minutes);

        return Timestamp.valueOf(updatedTime);

    }
}