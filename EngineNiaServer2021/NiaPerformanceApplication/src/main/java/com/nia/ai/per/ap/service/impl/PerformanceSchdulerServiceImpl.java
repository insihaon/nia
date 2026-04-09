package com.nia.ai.per.ap.service.impl;

import com.nia.ai.per.ap.service.LineMonitoringHdlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PerformanceSchdulerServiceImpl {

    @Autowired
    @Qualifier("LineMonitoringHdlService")
    private LineMonitoringHdlService lineMonitoringHdlService;

    @Scheduled(cron = "0 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobAiPerformanceResultData() {
      //  lineMonitoringHdlService.lineMonitoringSchdulerHdlProcessor();
    }

}
