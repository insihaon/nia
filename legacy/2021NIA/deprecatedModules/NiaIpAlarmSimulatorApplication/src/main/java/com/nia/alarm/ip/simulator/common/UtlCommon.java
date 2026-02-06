package com.nia.alarm.ip.simulator.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
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
    
}
