package com.nia.ping.alarm.preprocessor.mapper;

import com.nia.ping.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmCntVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingPolicyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface AlarmMapper{
    String selectPingAlarmKey();
    BasicAlarmVo selectPingAlarmCheck(HashMap<String, String> hashMap);
    PingPolicyVo selectPingPolicy();
    Boolean fcSetClearAlPool(BasicAlarmVo basicAlarmVo);
    void insertPingAlarm(PingAlarmVo pingAlarmVo);
    void insertPingCollect(PingAlarmVo pingAlarmVo);
    void updatePingAlarmCnt(HashMap<String, String> hashMap);
    void updatePingAlarmClear(HashMap<String, String> hashMap);
}
