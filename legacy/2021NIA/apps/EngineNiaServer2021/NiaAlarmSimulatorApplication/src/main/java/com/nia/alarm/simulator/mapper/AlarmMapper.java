package com.nia.alarm.simulator.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.nia.alarm.simulator.vo.AlarmVo;

@Mapper
public interface AlarmMapper{

	/**
	 * 알람 리스트 조회
	 * @param
	 * @return  ArrayList<AlarmVo>
	 */
    ArrayList<AlarmVo> selectAlarmList();

    void deleteAlarm(String alarmno);

}
