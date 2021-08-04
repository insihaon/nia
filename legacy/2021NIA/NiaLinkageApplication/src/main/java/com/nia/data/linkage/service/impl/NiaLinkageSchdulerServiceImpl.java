package com.nia.data.linkage.service.impl;

import com.nia.data.linkage.service.AlarmService;
import com.nia.data.linkage.service.PerformanceService;
import com.nia.data.linkage.service.TopologyService;
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
    @Qualifier("TopologyService")
    private TopologyService topologyService;

    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobPotnAlarmData() {
        alarmService.getAlarmData();
    }

    @Scheduled(cron = "* 5 1 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTopology() {
        topologyService.getTopologyData();
    }
}
