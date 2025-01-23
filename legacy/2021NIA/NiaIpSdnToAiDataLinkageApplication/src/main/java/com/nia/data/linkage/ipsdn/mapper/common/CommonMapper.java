package com.nia.data.linkage.ipsdn.mapper.common;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.HashMap;

@Mapper
public interface CommonMapper {
    String selectLinkageYdKey(String key);

    void insertLinkageHist(HashMap<String, String> strHashMap);
    void updateLinkageYdKey(HashMap<String, String> strHashMap);
}
