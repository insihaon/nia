package com.nia.data.linkage.ip.sflow.service.impl;

import com.nia.data.linkage.ip.sflow.service.SflowLogService;
import com.nia.data.linkage.ip.sflow.service.SflowProtocolLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaLinkageSchdulerServiceImpl extends SflowLogServiceImpl {

    @Autowired
    @Qualifier("SflowLogService")
    private SflowLogService sflowLogService;

    @Autowired
    @Qualifier("SflowProtocolLogService")
    private SflowProtocolLogService sflowProtocolLogService;

    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpSflowLogData() {
        sflowLogService.getSflowLogData();
    }

    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpSflowProtocolLogData() {
        sflowProtocolLogService.getSflowProtocolLogData();
    }
}
