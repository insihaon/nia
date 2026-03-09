package com.nia.ai.performance.send.mapper;

import com.nia.ai.performance.send.vo.performance.PerformaceVo;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface PerformanceMapper {
    String selectUpdateTime();
    List<PerformaceVo> selectPerformanceDataList(HashMap<String, String> map);
    void updatePerformanceDataSendTime(HashMap<String, String> map);

}
