package com.nia.data.linkage.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface AlarmMapper {
    String selectElkAlarmCheck(String alarmId);
    void insertAlarm(HashMap<String, Object> map);
    void insertAlarmCheck(HashMap<String, Object> map);
}
