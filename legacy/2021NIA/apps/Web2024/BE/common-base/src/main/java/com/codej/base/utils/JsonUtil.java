package com.codej.base.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * JsonUtil
 */
@Slf4j
public class JsonUtil {

    public static String TimeZoneID = "Asia/Seoul";

    public static synchronized TimeZone TimeZoneDefault() {
        return TimeZone.getTimeZone(JsonUtil.TimeZoneID);
    }

    private JsonUtil() {
        Locale.setDefault(Locale.KOREA);
    }

    private static Gson gson = new Gson();

    public static JsonObject parse(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return jsonObject;
    }

    /*
     * 해당 클래스나 맴버에 아래 어노테이션을 붙이면 json covert 시 제외 된다.
     * 
     * @JsonIgnoreProperties(ignoreUnknown = true)
     * 
     * @JsonIgnore
     */
    private static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setTimeZone(TimeZoneDefault());

    /*
     * Map map = Utils.convertJsonToClass(json, Map.class)
     * 대소문자를 구분함.
     * 
     * @JsonProperty("I_MEMID")
     * private long i_memid;
     * 위와 같이 선언 되었을 때 json 키가 - i_memid:변경불가, I_MEMID:변경가능
     */
    public static <T> T convertJsonToClass(String json, Class<T> valueType)
            throws IOException, JsonParseException, JsonMappingException {
        return objectMapper.readValue(json, valueType);
    }

    public static <T> T convertJsonToObject(String json, T obj)
            throws IOException, JsonParseException, JsonMappingException {
        return objectMapper.readValue(json, new TypeReference<T>() {
        });
    }

    public static Map<String, Object> convertJsonToMap(String json)
            throws IOException, JsonParseException, JsonMappingException {
        TypeReference<Map<String, Object>> map = new TypeReference<Map<String, Object>>() {
        };
        return objectMapper.readValue(json, map);
    }

    public static <T> List<T> convertJsonToListClass(String json, Class<T> valueType)
            throws IOException, JsonParseException, JsonMappingException {
        List<T> list = objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
        return list;
    }

    public static JsonObject convertJsonToJsonObject(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return jsonObject;
    }

    /*
     * Map map = Utils.convertMapToObject(map, Map.class)
     * 대소문자를 구분 안함
     */
    public static <T> T convertMapToObject(HashMap<String, Object> map, Class<T> valueType) {
        return objectMapper.convertValue(map, valueType);
    }

    /**
     * @param Map<String, Object>
     * @apiNote Map<String, Object>를 JSONObject로 변환처리.
     * @return JSONObject
     **/
    public static JSONObject convertMapToJson(Map<String, Object> map) {

        JSONObject json = new JSONObject();
        String key = "";
        Object value = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if (key != null && value != null) {
                json.put(key, value);
            }
        }
        return json;
    }

    public static HashMap<String, Object> convertObjectToMap(Object object) {
        HashMap<String, Object> map = objectMapper.convertValue(object, new TypeReference<HashMap<String, Object>>() {
        });
        return map;
    }

    public static <T> T convertClass(Object object, String clazzName) throws IOException, ClassNotFoundException {
        Class<?> clazz = Class.forName(clazzName);
        return convertClass(object, clazz);
    }

    public static <T> T convertClass(Object object, Class<?> clazz) throws IOException {
        String jsonString = convertClassToJsonString(object);
        return (T) convertJsonToClass(jsonString, clazz);
    }

    public static String convertClassToJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static Map<String, Object> fromJson(JsonObject jsonObject) {
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(jsonObject.toString());
            Map<String, Object> resultMap = objectMapper.convertValue(jsonNode, Map.class);
            return resultMap;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return new HashMap<String, Object>();
    }
}
