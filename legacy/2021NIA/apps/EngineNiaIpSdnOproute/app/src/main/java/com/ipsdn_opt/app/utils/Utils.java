package com.ipsdn_opt.app.utils;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public final class Utils {
    private Utils() {
        throw new UnsupportedOperationException("please do not instantiate utility class !");
    }

    public static Object jsonObjectSearch(JSONObject jObj, String searchKey) {
        return jsonObjectSearch(jObj, searchKey, null);
    }
    public static Object jsonObjectSearch(JSONObject jObj, String searchKey, String searchVal) {
        if(jObj==null) return null;
        
        if(jObj.has(searchKey)) {
            Object resultObj = jObj.get(searchKey);
            if(searchVal==null) {
                return resultObj;
            }
            else {
                if(resultObj instanceof JSONObject) {
                    Iterator<?> keys = jObj.keys();
                    while(keys.hasNext()) {
                        String key = (String)keys.next();
                        if(((JSONObject)resultObj).get(key).toString().equals(searchVal))
                            return resultObj;
                    }
                    return null;
                }
                else if(resultObj instanceof JSONArray) {
                    for(Object each : (JSONArray)resultObj) {
                        Iterator<?> keys = ((JSONObject)each).keys();
                        while(keys.hasNext()) {
                            String key = (String)keys.next();
                            if(((JSONObject)each).get(key).toString().equals(searchVal))
                                return each;
                        }
                    }
                    return null;
                }
                else {
                    if(((String)resultObj).equals(searchVal))
                        return resultObj;
                }
            }
        }
        else {
            Iterator<?> keys = jObj.keys();
            while(keys.hasNext()) {
                String key = (String)keys.next();
                if(jObj.get(key) instanceof JSONObject) {
                    Object res = jsonObjectSearch((JSONObject)jObj.get(key), searchKey, searchVal);
                    if(res==null) continue;
                    return res;
                }
                else if(jObj.get(key) instanceof JSONArray) {
                    Object res = jsonArraySearch((JSONArray)jObj.get(key), searchKey, searchVal);
                    if(res==null) continue;
                    return res;
                }
            }
        }
        return null;
    }
    public static Object jsonArraySearch(JSONArray jArr, String searchKey, String searchVal) {
        for(Object jObj : jArr) {
            Object res = jsonObjectSearch((JSONObject)jObj, searchKey, searchVal);
            if(res==null) continue;
            return res;
        }
        return null;
    }
}
