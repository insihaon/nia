package com.nia.data.linkage.ai.mapper.trans;

import com.nia.data.linkage.ai.vo.trans.equip.*;
import com.nia.data.linkage.ai.vo.trans.perf.PerformaceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TransDataMapper {
    List<PerformaceVo> selectAiSendPerformanceData(String ocrTime);
    ArrayList<EquipInfoVo> selectEquipList();
    ArrayList<EquipPortVo> selectEquipPortList();
    ArrayList<EquipSlotVo> selectEquipSlotList();
    ArrayList<NNiTopologyVo> selectNniTopologyList();
    ArrayList<UniTopologyVo> selectUniTopologyList();
    ArrayList<RoadmRepeaterRouteVo> selectRoadmTrunkList();
}
