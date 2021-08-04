package com.nia.alarm.simulator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AlarmSchdulerService {

    @Autowired
    @Qualifier("AlarmSchedulerJobService")
    private AlarmSchedulerJobService alarmSchedulerJobService;

    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobAlarmRead() {
        alarmSchedulerJobService.executeInternal();
    }

}
