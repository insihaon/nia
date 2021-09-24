package com.nia.data.linkage.ip.perf.mapper.nia;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface NiaPerfMapper {
    String selectPerfYdKey(String key);
    void insertPerf(HashMap<String, Object> objectHashMap);
    void updatePerfYdKey(HashMap<String, String> strHashMap);
}
