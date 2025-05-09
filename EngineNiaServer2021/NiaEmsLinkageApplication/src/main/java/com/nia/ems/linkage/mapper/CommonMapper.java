package com.nia.ems.linkage.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface CommonMapper {
    void updateLinkageYdKey(HashMap<String,String> strHashMap);
}
