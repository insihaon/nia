package com.nia.data.linkage.ai.mapper.common;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface CommonMapper {
    String selectLinkageYdKey(String key);

    void insertLinkageHist(HashMap<String, String> strHashMap);
    void updateLinkageYdKey(HashMap<String, String> strHashMap);
}
