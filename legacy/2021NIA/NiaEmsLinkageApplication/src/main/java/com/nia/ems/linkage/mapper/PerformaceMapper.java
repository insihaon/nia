package com.nia.ems.linkage.mapper;

import com.nia.ems.linkage.vo.performance.PerformaceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface PerformaceMapper {
    String selectPerformanceUpdateTime();
    Integer selectPerformanceUpdateCnt(HashMap<String, String> map);
    void insertPerformace(HashMap<String, Object> map);
    void updatePerformanceUpdateTime(String updateTime);
}
