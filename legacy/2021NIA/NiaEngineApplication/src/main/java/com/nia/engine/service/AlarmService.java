package com.nia.engine.service;

import com.nia.engine.vo.BasicAlarmVo;
import com.nia.engine.vo.RCATicketHandlingStatus;
import com.nia.engine.vo.TopologyObject;

import java.util.HashMap;
import java.util.List;

public interface AlarmService {

	/**
	 * 클러스터 알람 조회
	 * @param
	 * @return  ArrayList<BasicAlarmVo>
	 */
	List<BasicAlarmVo> selectAlarmMstList(String clusterno) throws Exception;

	/**
	 * 알람 조회
	 * @param
	 * @return BasicAlarmVo
	 */
	BasicAlarmVo selectAlarmInfo(String alarmno) throws Exception;

	/**
	 * 클러스터 Topology 조회
	 * @param
	 * @return  ArrayList<BasicAlarmVo>
	 */
	TopologyObject selectAlTopology(String alarmno) throws Exception;
}
