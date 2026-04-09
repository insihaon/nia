package com.nia.ai.performance.send.service.impl;

import com.nia.ai.performance.send.service.PerformanceSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaAiLinkageSchdulerServiceImpl {

    @Autowired
    @Qualifier("PerformanceSendService")
    private PerformanceSendService performanceSendService;


//    @Scheduled(cron = "0 0/15 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
//    public void conJobPerformanceSend() throws Exception {
//            Thread.sleep((1000*180));
//            performanceSendService.sendPerformanceData();
//    }
}
