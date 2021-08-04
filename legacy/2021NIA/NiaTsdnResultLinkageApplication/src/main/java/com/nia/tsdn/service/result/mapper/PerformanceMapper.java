package com.nia.tsdn.service.result.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PerformanceMapper {

    void insertPerformanceData(HashMap<String, Object> map);
}
