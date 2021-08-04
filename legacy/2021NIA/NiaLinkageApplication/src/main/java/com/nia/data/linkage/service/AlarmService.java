package com.nia.data.linkage.service;

import com.nia.data.linkage.vo.alarm.json.AlarmHitsDataVo;

public interface AlarmService {

    void getAlarmData();

    void setAlarmData(AlarmHitsDataVo alarmHitsDataVo);

    String setAlarmLvl(String level);
}
