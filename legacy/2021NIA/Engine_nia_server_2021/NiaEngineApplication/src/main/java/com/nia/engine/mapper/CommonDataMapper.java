package com.nia.engine.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface CommonDataMapper {
    String selectLinkageYdKey(String key);
    void insertLinkageHist(HashMap<String, String> strHashMap);
    void updateLinkageYdKey(HashMap<String, String> strHashMap);
}
