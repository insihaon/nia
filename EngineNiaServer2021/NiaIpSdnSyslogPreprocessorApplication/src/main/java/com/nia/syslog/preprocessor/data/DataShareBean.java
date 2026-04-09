package com.nia.syslog.preprocessor.data;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataShareBean<T> {
    private final static Logger LOGGER = Logger.getLogger(DataShareBean.class);
    private Map<String, T> shareDataMap;


    public DataShareBean () {
        this.shareDataMap = Maps.newConcurrentMap();
    }

    public void putData(String key, T data) {
        if (shareDataMap ==  null) {
            LOGGER.error("Map is not initialize");
            return;
        }

        shareDataMap.put(key, data);
    }

    public T getData (String key) {

        if (shareDataMap == null) {
            return null;
        }

        return shareDataMap.get(key);
    }

    public int getSize () {
        if (this.shareDataMap == null) {
            LOGGER.error("Map is not initialize");
            return 0;
        }

        return shareDataMap.size();
    }
}
