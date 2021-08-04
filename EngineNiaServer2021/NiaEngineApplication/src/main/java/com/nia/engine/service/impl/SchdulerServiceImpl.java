package com.nia.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchdulerServiceImpl {

    @Autowired
    @Qualifier("EngineClearSchedulerJobService")
    private EngineClearSchedulerJobServiceImpl engineClearSchedulerJobServiceImpl;

    @Scheduled(cron = "* 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobClearTicketCheck() {
        engineClearSchedulerJobServiceImpl.clearTicketCheck();
    }

    @Scheduled(cron = "* 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTicketClear() {
        engineClearSchedulerJobServiceImpl.clearTicket();
    }


}
