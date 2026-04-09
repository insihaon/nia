package com.nia.ems.linkage.mapper;

import com.nia.ems.linkage.vo.alarm.AlarmVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface AlarmMapper {
    void insertAlarm(AlarmVo alarmVo);
}
