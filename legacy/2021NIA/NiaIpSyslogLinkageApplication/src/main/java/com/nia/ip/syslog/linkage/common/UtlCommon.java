package com.nia.ip.syslog.linkage.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
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
	
	public static final String JOB_EQUIP_RESULT_STATE_SUCCESS = "S";
	public static final String JOB_EQUIP_RESULT_STATE_FAIL = "F";
	public static final String JOB_EQUIP_RESULT_STATE_CANCEL = "C";

	public static String getNowTimeToString() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		
    	return (new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(date));
    }
    
    public static int getIntJson(JSONObject json, String key) {
    	try {
			return json.isNull(key) ? 0 : json.getInt(key);
		} catch (JSONException e) {
			LOGGER.info("getIntJson Error : key : " + key + ", json : " + json);
			return 0;
		}
    }
    
    public static long getLongJson(JSONObject json, String key) {
    	try {
			return json.isNull(key) ? 0 : json.getLong(key);
		} catch (JSONException e) {
			LOGGER.info("getIntJson Error : key : " + key + ", json : " + json);
			return 0;
		}
    }
    
    public static String getStringJson(JSONObject json, String key) {
    	try {
			return json.isNull(key) ? "" : json.getString(key);
		} catch (JSONException e) {
			LOGGER.info("getStringJson Error : key : " + key + ", json : " + json);
			return "";
		}
    }  
    
    public static boolean getBooleanJson(JSONObject json, String key) {
    	try {
			return !json.isNull(key) && json.getBoolean(key);
		} catch (JSONException e) {
			LOGGER.info("getIntJson Error : key : " + key + ", json : " + json);
			return false;
		}
    }
    
    public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj , String key) {

		Map<String, Object> map = null;
		
		try {
			
			map = new ObjectMapper().readValue(jsonObj.get(key).toString(), Map.class) ;
			
		} catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			e.printStackTrace();
		}
        return map;
	}
    
    public static Map<String, Object> toMap(JSONObject object){
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
	
	        Iterator<String> keysItr = object.keys();
	        while(keysItr.hasNext()) {
	            String key = keysItr.next();
	            Object value;
				
            	value = object.get(key);
            	 map.put(key, value);
            	 break;

	           /* if(value instanceof JSONArray) {
	                value = toList((JSONArray) value);
	            }
	
	            else if(value instanceof JSONObject) {
	                value = toMap((JSONObject) value);
	            }*/
	           
	        }
	        return map;
    	} catch (JSONException e) {
    		LOGGER.info(">>>>>>>>>>>>>>>>>>>>[UtlCommon] toMap error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
		return map;
    }
    
    public static List<Object> toList(JSONArray array) {
    	List<Object> list = new ArrayList<Object>();
    	try {
	        for(int i = 0; i < array.length(); i++) {
	            Object value;
				
				value = array.get(i);
				
	            if(value instanceof JSONArray) {
	                value = toList((JSONArray) value);
	            }
	
	            else if(value instanceof JSONObject) {
	                value = toMap((JSONObject) value);
	            }
	            list.add(value);
	        }
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list;
    }
    
    public static List<String> toListString(JSONArray array) {
    	List<String> list = new ArrayList<String>();
    	try {
	        for(int i = 0; i < array.length(); i++) {
	            Object value;
				
				value = array.get(i);
				
	            if(value instanceof JSONArray) {
	                value = toList((JSONArray) value);
	            }
	
	            else if(value instanceof JSONObject) {
	                value = toMap((JSONObject) value);
	            }
	            list.add((String) value);
	        }
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list;
    }

    public static boolean isCheckKr(String s){
        boolean isKr = false;
        try {
            isKr = s.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
        }catch (Exception e){

        }
        return isKr;
    }

    public static boolean isCheckDigits(String s){
        boolean isKr = false;
        try {
            isKr = s.matches(".*\\d.*");
        }catch (Exception e){

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

     public static String objectToJson(Object obj){

		 ObjectMapper mapper;
		 String resultMsg = null;
     	try{
			mapper = new ObjectMapper();
			resultMsg = mapper.writeValueAsString(obj);
     	}catch (Exception e) {
     		LOGGER.error(">>>>>>>>>>[UtlCommon] objectToJson() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
 		}
     	return resultMsg;
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
