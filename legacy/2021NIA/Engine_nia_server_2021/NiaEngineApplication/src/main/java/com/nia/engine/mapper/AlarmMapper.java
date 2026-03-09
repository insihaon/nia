package com.nia.engine.mapper;

import java.util.HashMap;
import java.util.List;

import com.nia.engine.vo.*;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmMapper{

	/**
	 * 클러스터 알람 조회
	 * @param
	 * @return  ArrayList<BasicAlarmVo>
	 */
	List<BasicAlarmVo> selectAlarmMstList(String clusterno);

	/**
	 * 알람 조회
	 * @param
	 * @return  ArrayList<BasicAlarmVo>
	 */
	BasicAlarmVo selectAlarmInfo(String alarmno);

	/**
	 * 클러스터 Topology 조회
	 * @param
	 * @return  TopologyObject
	 */
	TopologyObject selectAlTopology(String alarmno);
}
