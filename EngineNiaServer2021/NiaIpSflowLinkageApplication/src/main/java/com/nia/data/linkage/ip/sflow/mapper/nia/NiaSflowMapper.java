package com.nia.data.linkage.ip.sflow.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaSflowMapper {
    String selectSflowYdKey(String key);
    void insertSflowLog(HashMap<String, Object> objectHashMap);
    void insertSflowProtocolLog(HashMap<String, Object> objectHashMap);
    void updateSflowYdKey(HashMap<String, String> strHashMap);
}
