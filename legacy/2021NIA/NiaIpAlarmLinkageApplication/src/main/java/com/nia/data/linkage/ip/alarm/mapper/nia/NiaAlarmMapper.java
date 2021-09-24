package com.nia.data.linkage.ip.alarm.mapper.nia;

import com.nia.data.linkage.ip.alarm.vo.alarm.AlarmVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Mapper
public interface NiaAlarmMapper {
    String selectAlarmYdKey(String key);
    void insertIpAlarm(HashMap<String, Object> objectHashMap);
    void updateAlarmYdKey(HashMap<String, String> strHashMap);
}
