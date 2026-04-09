package kr.go.ap.core.utility.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class UtlCommon {

    public static String getNowTimeToString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        return (new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(date));
    }

    public static boolean isCheckKr(String s) throws NullPointerException {
        return s.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    public static boolean isCheckDigits(String s) throws NullPointerException {
        return s.matches(".*\\d.*");
    }

    public static int getOnlyDigits(String s) throws NumberFormatException {
        Pattern pattern;
        Matcher matcher;
        int number = 0;

        if (s != null) {
            pattern = Pattern.compile("[^0-9]");
            matcher = pattern.matcher(s);

            number = Integer.parseInt(matcher.replaceAll(""));
        }
        return number;
    }

    public static String getOnlyStrings(String s) throws Exception {
        Pattern pattern;
        Matcher matcher;
        String number = null;

        if (s != null) {
            pattern = Pattern.compile("[^a-z A-Z]");
            matcher = pattern.matcher(s);
            number = matcher.replaceAll("");
        }

        return number;
    }

    public static Object jsonToObject(Object obj, String jMsg) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        return objectMapper.readValue(jMsg, obj.getClass());
    }

    public static String objectToJson(Object obj) throws Exception {

        ObjectMapper mapper;
        mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static String lTrim(String str) throws NullPointerException {
        return str.replaceAll("^\\s+", "");
    }

    public static String rTrim(String str) throws NullPointerException {
        return str.replaceAll("\\s+$", "");
    }

    public static String aTrim(String str) throws NullPointerException {
        return str.replaceAll("\\s+", "");
    }

    public static String lrTrim(String str) throws NullPointerException {
        return str.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }

    public static String stringValueOf(int num) throws NullPointerException {
        if (num == 0) {
            return null;
        } else {
            return String.valueOf(num);
        }
    }

    public static String stringValueOf(long num) throws NullPointerException {
        if (num == 0) {
            return null;
        } else {
            return String.valueOf(num);
        }
    }

}
