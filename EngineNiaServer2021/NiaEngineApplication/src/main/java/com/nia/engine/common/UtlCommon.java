package com.nia.engine.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
			LOGGER.error("getIntJson Error : key : " + key + ", json : " + json);
			return 0;
		}
    }
    
    public static long getLongJson(JSONObject json, String key) {
    	try {
			return json.isNull(key) ? 0 : json.getLong(key);
		} catch (JSONException e) {
			LOGGER.error("getIntJson Error : key : " + key + ", json : " + json);
			return 0;
		}
    }
    
    public static String getStringJson(JSONObject json, String key) {
    	try {
			return json.isNull(key) ? "" : json.getString(key);
		} catch (JSONException e) {
			LOGGER.error("getStringJson Error : key : " + key + ", json : " + json);
			return "";
		}
    }  
    
    public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj , String key) {

		Map<String, Object> map = null;
		
		try {
			map = new ObjectMapper().readValue(jsonObj.get(key).toString(), Map.class) ;
		} catch (JsonParseException e) {
			LOGGER.error(">>>>>>>>>>>>>>>[UtlCommon] getMapFromJsonObject error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<");
        } catch (JsonMappingException e) {
        	LOGGER.error(">>>>>>>>>>>>>>>[UtlCommon] getMapFromJsonObject error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<");
        } catch (IOException e) {
        	LOGGER.error(">>>>>>>>>>>>>>>[UtlCommon] getMapFromJsonObject error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<");
        } catch (JSONException e) {
        	LOGGER.error(">>>>>>>>>>>>>>>[UtlCommon] getMapFromJsonObject error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<");
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
				
	            if(key.equals(RcaCodeInfo.RCA_OBJECT_ROADM)){
	            	value = object.get(key);
	            	 map.put(key, value);
	            	 break;
				}else if(key.equals(RcaCodeInfo.RCA_OBJECT_BASIC)){
					value = object.get(key);
	            	 map.put(key, value);
	            	 break;
				}else if(key.equals(RcaCodeInfo.RCA_OBJECT_PTN)){
					value = object.get(key);
	            	 map.put(key, value);
	            	 break;
				}
				
	           /* if(value instanceof JSONArray) {
	                value = toList((JSONArray) value);
	            }
	
	            else if(value instanceof JSONObject) {
	                value = toMap((JSONObject) value);
	            }*/
	           
	        }
	        return map;
    	} catch (JSONException e) {
    		LOGGER.error(">>>>>>>>>>>>>>>>>>>>[UtlCommon] toMap error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
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
    		LOGGER.error(">>>>>>>>>>>>>>>>>>>>[UtlCommon] toList error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
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
    		LOGGER.error(">>>>>>>>>>>>>>>[UtlCommon] toListString error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<");
		}
        return list;
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
    
}
