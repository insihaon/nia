package com.nia.rca.cluster.preprocessor.mapper;

import com.nia.rca.cluster.preprocessor.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface AlarmMapper{

    void insertAlarmMst(BasicAlarmVo basicAlarmVo);

	void insertAlTopology(TopologyObject topologyObject);

	void updateAlarmClusterNo(HashMap<String, String> map);
}
