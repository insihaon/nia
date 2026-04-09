package com.nia.data.linkage.service;

import com.nia.data.linkage.vo.alarm.json.AlarmHitsHitsDataVo;
import com.nia.data.linkage.vo.alarm.json.AlarmHitsVo;

import java.util.List;

public interface AlarmService {

    void getAlarmData();

    void setAlarmData(List<AlarmHitsVo> histsList);

    String setAlarmLvl(String level);
}
