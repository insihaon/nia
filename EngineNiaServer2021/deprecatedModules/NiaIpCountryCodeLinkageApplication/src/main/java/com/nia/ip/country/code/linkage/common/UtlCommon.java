package com.nia.ip.country.code.linkage.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 공통 Util
 * @author
 *
 */
public class UtlCommon {	
	private static final Logger LOGGER = Logger.getLogger(UtlCommon.class);


    public static String objectToJson(Object obj) throws Exception {

        ObjectMapper mapper;
        mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
	public static String getNowTimeToString() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		
    	return (new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(date));
    }

    public static boolean isCheckKr(String s){
        boolean isKr = false;
        try {
            isKr = s.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        }catch (Exception e){
			LOGGER.error(">>>>>>>>>>>>>>>>>>>>[UtlCommon] isCheckKr error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
        return isKr;
    }

    public static boolean isCheckDigits(String s){
        boolean isKr = false;
        try {
            isKr = s.matches(".*\\d.*");
        }catch (Exception e){
			LOGGER.error(">>>>>>>>>>>>>>>>>>>>[UtlCommon] isCheckDigits error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
        return isKr;
    }

    public static int getOnlyDigits(String s) {
        Pattern pattern;
        Matcher matcher;
        int number = 0;
	    try{
	        if(s != null){
                pattern = Pattern.compile("[^0-9]");
                matcher = pattern.matcher(s);
                number = Integer.parseInt(matcher.replaceAll(""));
            }
        }catch (Exception e){
            return 0;
        }
        return number;
     }

     public static String getOnlyStrings(String s) {
         Pattern pattern;
         Matcher matcher;
         String number = null;
         try{
             if(s != null){
                 pattern = Pattern.compile("[^a-z A-Z]");
                 matcher = pattern.matcher(s);
                 number = matcher.replaceAll("");
             }
         }catch (Exception e){
             return null;
         }
         return number;
     }

     public static Object jsonToObject(Object obj, String jMsg){
     	Object resultObject = null;
     	try{
     		ObjectMapper objectMapper = new ObjectMapper();
     		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
     		resultObject = objectMapper.readValue(jMsg, obj.getClass());
     	}catch (Exception e) {
     		LOGGER.error(">>>>>>>>>>[UtlCommon] jsonToObject() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
 		}
     	return resultObject;
     }

     public static String lTrim(String str){
	    String result = null;
	    try{
            result = str.replaceAll("^\\s+","");
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[UtlCommon] ltrim("+str+") error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
         return result;
     }

    public static String rTrim(String str){
        String result = null;
        try{
            result = str.replaceAll("\\s+$","");
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[UtlCommon] rTrim("+str+") error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
        return result;
    }

    public static String mapToUrlParam(Map<String, String> params) {
        StringBuffer paramData = new StringBuffer();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (paramData.length() != 0) {
                paramData.append('&');
            }
            paramData.append(param.getKey());
            paramData.append('=');
            paramData.append(param.getValue());
        }
        return paramData.toString();
    }
}
