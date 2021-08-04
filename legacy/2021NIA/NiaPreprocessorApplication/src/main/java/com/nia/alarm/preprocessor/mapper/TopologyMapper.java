package com.nia.alarm.preprocessor.mapper;

import com.nia.alarm.preprocessor.vo.euqipment.EquipPortVo;
import com.nia.alarm.preprocessor.vo.topology.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TopologyMapper {

    TopologyDataVo selectTopologyList(HashMap<String, String> map);
    UniTopologyDataVo selectUniTopologyList(HashMap<String, String> map);
    EquipPortVo selectEquipPort(HashMap<String, String> map);
    RoadmRepeaterRouteVo selectRoadmTrunkId(HashMap<String, String> map);
}
