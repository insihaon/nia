package com.nia.data.linkage.ip.alarm.mapper.linkage;

import com.nia.data.linkage.ip.alarm.vo.alarm.AlarmVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface LinkageAlarmMapper {
    ArrayList<AlarmVo> selectAlarmList(int alarmId);
}
