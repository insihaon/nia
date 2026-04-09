package com.nia.rca.cluster.preprocessor.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 공통 Util
 * @author
 *
 */
public class UtlCommon {
    private static final Logger LOGGER = Logger.getLogger(UtlCommon.class);

    public static String getOnlyDigits(String s) {
        Pattern pattern;
        Matcher matcher;
        String number = null;
        try{
            if(s != null){
                pattern = Pattern.compile("[^0-9]");
                matcher = pattern.matcher(s);
                number = matcher.replaceAll("");
            }
        }catch (Exception e){
            return null;
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
            LOGGER.error(">>>>>>>>>>[UtlCommon] jsonToObject() error : " + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
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
}
