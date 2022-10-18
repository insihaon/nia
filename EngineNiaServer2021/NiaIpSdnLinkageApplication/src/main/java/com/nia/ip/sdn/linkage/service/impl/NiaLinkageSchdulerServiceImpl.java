package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaLinkageSchdulerServiceImpl {

    @Autowired
    @Qualifier("InterfaceService")
    private InterfaceService interfaceService;

    @Autowired
    @Qualifier("LinkService")
    private LinkService linkService;

    @Autowired
    @Qualifier("NodeService")
    private NodeService nodeService;

    @Autowired
    @Qualifier("LinkTrafficeService")
    private LinkTrafficeService linkTrafficeService;

    @Autowired
    @Qualifier("NodeFactorService")
    private NodeFactorService nodeFactorService;


    @Scheduled(cron = "0 0 1 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobInterfaceData() {
        interfaceService.getInterfaceData();
    }

    @Scheduled(cron = "0 5 1 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobLinkData() {
        linkService.getLinkData();
    }

    @Scheduled(cron = "0 10 1 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobNodeData() {
        nodeService.getNodeData();
    }

    @Scheduled(cron = "15 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobLinkTrafficeData() {
        linkTrafficeService.getLinkTrafficeData();
    }


    @Scheduled(cron = "15 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobNodeFactorData() {
        nodeFactorService.getNodeFactorData();
    }
}
