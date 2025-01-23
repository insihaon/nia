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

    @Autowired
    @Qualifier("RcaTicketProfileSchedulerJobService")
    private RcaTicketProfileSchedulerJobServiceImpl rcaTicketProfileSchedulerJobServiceImpl;

    @Autowired
    @Qualifier("SopTimeoutAiService")
    private SopTimeoutAiService sopTimeoutAiService;




    @Scheduled(cron = "0/10 * * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void cronJobTimeoutSopCheck(){
        sopTimeoutAiService.processTimeoutSop();
    }


    @Scheduled(cron = "0 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobClearTicketCheck() {
        engineClearSchedulerJobServiceImpl.clearTicketCheck();
    }

    @Scheduled(cron = "0/10 * * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobClearPassing5mTicket(){
        engineClearSchedulerJobServiceImpl.TicketClearPassing5Minute();
    }

    @Scheduled(cron = "0/10 * * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobClearPassing5mSyslog(){
        engineClearSchedulerJobServiceImpl.SyslogClearPassing5MinuteAndRemoteCommand();
    }

//    @Scheduled(cron = "0/20 * * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
//    public void TicketPortDownCommand(){
//        engineClearSchedulerJobServiceImpl.TicketPortDownCommand();
//    }

//    @Scheduled(cron = "0/20 * * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
//    public void SyslogPortDownCommand(){
//        engineClearSchedulerJobServiceImpl.SyslogPortDownCommand();
//    }

    @Scheduled(cron = "30 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTicketClear() {
        engineClearSchedulerJobServiceImpl.clearTicket();
    }

    @Scheduled(cron = "0 2/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTicketProfile() {
        rcaTicketProfileSchedulerJobServiceImpl.profileCheck();
    }
}
