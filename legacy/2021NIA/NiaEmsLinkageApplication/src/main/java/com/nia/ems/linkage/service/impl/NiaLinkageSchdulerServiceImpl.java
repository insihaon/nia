package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.client.TelnetMmc;
import com.nia.ems.linkage.service.PerformanceService;
import com.nia.ems.linkage.service.RoadmEmsMmcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaLinkageSchdulerServiceImpl {

    @Autowired
    @Qualifier("RoadmEmsMmcService")
    private RoadmEmsMmcService roadmEmsMmcService;

    @Autowired
    @Qualifier("PerformanceService")
    private PerformanceService performanceService;

    @Scheduled(cron = "0 0/15 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobEmsSipcMmc()throws InterruptedException {
        roadmEmsMmcService.roadmSipcMMC();
    }

    @Scheduled(cron = "0 0/15 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobEmsPerMmc()throws InterruptedException {
        roadmEmsMmcService.roadmPmMMC();
    }

//    @Scheduled(cron = "0 0/15 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
//    public void conJobEmsPmMMC() throws InterruptedException {
//        performanceService.performanceDataCheck();
//    }

//    @Scheduled(cron = "* 5 1 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
//    public void conJobCreateTopology() throws InterruptedException {
//        roadmEmsMmcService.createRoadmUniTopology();
//        Thread.sleep(5000);
//        roadmEmsMmcService.roadmNetWorkMmc();
//        Thread.sleep(2000);
//    }

}
