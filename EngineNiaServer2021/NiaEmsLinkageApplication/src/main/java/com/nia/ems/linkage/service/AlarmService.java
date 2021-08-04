package com.nia.ems.linkage.service;


import com.nia.ems.linkage.vo.alarm.AlarmVo;

public interface AlarmService {


    String setAlarmLvl(String level);

    AlarmVo pasingAlarmMsg(StringBuffer sbAlarmData);
}
