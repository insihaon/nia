package com.nia.engine.mapper;

import com.nia.engine.vo.TopologyDataVo;
import com.nia.engine.vo.TopologyObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface TopologyMapper {

	/**
	 * selectAlarmMstList
	 * @param
	 * @return  TopologyObject
	 */
    TopologyObject selectAlarmMstList(String alarmid);

	TopologyDataVo selectTopology(HashMap<String, String> map);
}
