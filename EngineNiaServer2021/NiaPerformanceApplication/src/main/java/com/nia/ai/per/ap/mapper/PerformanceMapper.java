package com.nia.ai.per.ap.mapper;

import com.nia.ai.per.ap.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PerformanceMapper {

    List<RoadmPerformanceOrgVo> selectPerformanceList(HashMap<String, String> map);
    void insertRowSignalData(HashMap<String, Object> map);
}
