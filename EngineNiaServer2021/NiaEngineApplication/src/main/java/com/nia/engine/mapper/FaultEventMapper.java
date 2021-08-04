package com.nia.engine.mapper;

import com.nia.engine.vo.falutEvent.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface FaultEventMapper {

	/**
	 * 장애 이벤트 Key 생성
	 * @param
	 * @return  ArrayList<BasicAlarmVo>
	 */
	String selectFaultEventKey();

	FaultEventVo selectFaultEvent(String eventNo);

	List<FaultEventAlarmDataVo> selectFaultEventAlarm(String eventNo);

	List<FaultEventPerformanceDataVo> selectFaultEventPerformance(String eventNo);

	List<FaultEventNniTopologyDataVo> selectFaultEventNniTopology(String eventNo);

	List<FaultEventUniTopologyDataVo> selectFaultEventUniTopology(String eventNo);

	/**
	 * 장애 이벤트 데이터 저장
	 * @param
	 * @return
	 */
	void insertFaultEvent(HashMap<String, String> map);

	/**
	 * 장애 이벤트 장비 알람 데이터 저장
	 * @param
	 * @return
	 */
	void insertFaultEventAlarmCurMst(HashMap<String, String> map);

	/**
	 * 장애 이벤트 성능 데이터 저장
	 * @param
	 * @return
	 */
	void insertFaultEventAiPerformanceMst(HashMap<String, String> map);

	/**
	 * 장애 이벤트 NNI TOPOLOGY 데이터 저장
	 * @param
	 * @return
	 */
	void insertFaultEventNniTopology(HashMap<String, String> map);

	/**
	 * 장애 이벤트 UNI TOPOLOGY 데이터 저장
	 * @param
	 * @return
	 */
	void insertFaultEventUniTopology(HashMap<String, String> map);
}
