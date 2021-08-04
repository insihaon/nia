package com.nia.ai.per.ap.mapper;

import com.nia.ai.per.ap.vo.RoadmRepeaterRouteVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface EquipmentMapper {
    RoadmRepeaterRouteVo selectRoadmRepeaterRoutenum(HashMap<String, String> map);
}
