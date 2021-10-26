package com.nia.data.linkage.ai.service.impl;

import com.nia.data.linkage.ai.service.impl.ip.equip.IpEquipTableDataAiLinkageServiceImpl;
import com.nia.data.linkage.ai.service.impl.ip.perf.IpPerfToAiLinkageServiceImpl;
import com.nia.data.linkage.ai.service.impl.ip.sflow.IpSflowToAiLinkageServiceImpl;
import com.nia.data.linkage.ai.service.impl.trans.equip.TransEquipTableDataAiLinkageServiceImpl;
import com.nia.data.linkage.ai.service.impl.trans.perf.RoadmPmDataAiLinkageServiceImpl;
import com.nia.data.linkage.ai.service.ip.equip.IpEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.service.ip.perf.IpPerfToAiLinkageService;
import com.nia.data.linkage.ai.service.ip.sflow.IpSflowToAiLinkageService;
import com.nia.data.linkage.ai.service.trans.equip.TransEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.service.trans.perf.RoadmPmDataAiLinkageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaAiLinkageSchdulerServiceImpl extends IpPerfToAiLinkageServiceImpl {

    @Autowired
    @Qualifier("IpPerfToAiLinkageService")
    private IpPerfToAiLinkageService ipPerfToAiLinkageService;

    @Autowired
    @Qualifier("IpSflowToAiLinkageService")
    private IpSflowToAiLinkageService ipSflowToAiLinkageService;

    @Autowired
    @Qualifier("RoadmPmDataAiLinkageService")
    private RoadmPmDataAiLinkageService roadmPmDataAiLinkageService;

    @Autowired
    @Qualifier("IpEquipTableDataAiLinkageService")
    private IpEquipTableDataAiLinkageService ipEquipTableDataAiLinkageService;

    @Autowired
    @Qualifier("TransEquipTableDataAiLinkageService")
    private TransEquipTableDataAiLinkageService transEquipTableDataAiLinkageService;

    @Scheduled(cron = "0 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpPerfData() {
        ipPerfToAiLinkageService.sendPerfLogData();
    }

    @Scheduled(cron = "0 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpSflowLogfData() {
        ipSflowToAiLinkageService.sendSflowLogData();
    }

    @Scheduled(cron = "0 0/15 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobRoadmPmDataAiLinkage(){
        roadmPmDataAiLinkageService.sendRoadmPmData();
    }

    @Scheduled(cron = "0 0 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpBackBoneLinkData() {
        ipEquipTableDataAiLinkageService.sendBackBoneLinkData();
    }

    @Scheduled(cron = "0 10 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpPortData() {
        ipEquipTableDataAiLinkageService.sendPortData();
    }

    @Scheduled(cron = "0 20 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpNodeData() {
        ipEquipTableDataAiLinkageService.sendNodeData();
    }

    @Scheduled(cron = "0 0 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTransEquipMstData() {
        transEquipTableDataAiLinkageService.sendEquipMstData();
    }

    @Scheduled(cron = "0 10 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTransEquipPortMstData() {
        transEquipTableDataAiLinkageService.sendEquipPortData();
    }

    @Scheduled(cron = "0 20 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTransEquipSlotMstData() {
        transEquipTableDataAiLinkageService.sendEquipSlotData();
    }

    @Scheduled(cron = "0 30 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTransNniTopologyData() {
        transEquipTableDataAiLinkageService.sendNniTopologyData();
    }

    @Scheduled(cron = "0 40 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTransUniTopologyData() {
        transEquipTableDataAiLinkageService.sendUniTopologyData();
    }

    @Scheduled(cron = "0 50 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobTransRoadmTrunkData() {
        transEquipTableDataAiLinkageService.sendRoadmTrunkData();
    }
}
