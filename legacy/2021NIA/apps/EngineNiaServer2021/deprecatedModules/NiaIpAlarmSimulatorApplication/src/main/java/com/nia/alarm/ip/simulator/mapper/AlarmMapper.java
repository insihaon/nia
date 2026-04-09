package com.nia.alarm.ip.simulator.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.nia.alarm.ip.simulator.vo.AlarmVo;

@Mapper
public interface AlarmMapper{

	/**
	 * 알람 리스트 조회
	 * @param
	 * @return  ArrayList<AlarmVo>
	 */
    ArrayList<AlarmVo> selectAlarmList();
    ArrayList<AlarmVo> selectTestAlarmList();
    ArrayList<AlarmVo> selectEtriTestAlarmList();
    void fcSetClearSimulator();
    void fcSetClearSimulatorEtri();
    void deleteAlarm(int alarmno);

}
