package com.nia.ai.traffic.preprocessor.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface TrafficMapper {

    void insertAnomalousTraffic(HashMap<String, String> map);
    void insertNoxiousTraffic(HashMap<String, String> map);
}
