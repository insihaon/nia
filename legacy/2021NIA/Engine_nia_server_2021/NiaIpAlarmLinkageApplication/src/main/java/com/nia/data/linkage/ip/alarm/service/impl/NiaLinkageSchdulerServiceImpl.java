package com.nia.data.linkage.ip.alarm.service.impl;

import com.nia.data.linkage.ip.alarm.service.AlarmClearService;
import com.nia.data.linkage.ip.alarm.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaLinkageSchdulerServiceImpl {

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Autowired
    @Qualifier("AlarmClearService")
    private AlarmClearService alarmClearService;

    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpAlarmData() {
        alarmService.getAlarmData();
    }

    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpAlarmClearData() {
        alarmClearService.getAlarmClearData();
    }
}
