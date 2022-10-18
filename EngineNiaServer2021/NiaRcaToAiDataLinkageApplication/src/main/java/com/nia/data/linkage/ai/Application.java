package com.nia.data.linkage.ai;

import com.nia.data.linkage.ai.service.ip.alarm.IpAlarmToAiLinkageService;
import com.nia.data.linkage.ai.service.ip.equip.IpEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.service.ip.perf.IpPerfToAiLinkageService;
import com.nia.data.linkage.ai.service.ip.sflow.IpSflowToAiLinkageService;
import com.nia.data.linkage.ai.service.ipsdn.factor.IpSdnFactorLinkageService;
import com.nia.data.linkage.ai.service.ipsdn.resource.IpSdnResourceLinkageService;
import com.nia.data.linkage.ai.service.ipsdn.traffic.IpSdnTrafficLinkageService;
import com.nia.data.linkage.ai.service.trans.equip.TransEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.service.trans.perf.RoadmPmDataAiLinkageService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableEncryptableProperties
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    @Qualifier("RoadmPmDataAiLinkageService")
    private RoadmPmDataAiLinkageService roadmPmDataAiLinkageService;

    @Autowired
    @Qualifier("TransEquipTableDataAiLinkageService")
    private TransEquipTableDataAiLinkageService transEquipTableDataAiLinkageService;

    @Autowired
    @Qualifier("IpPerfToAiLinkageService")
    private IpPerfToAiLinkageService ipPerfToAiLinkageService;

    @Autowired
    @Qualifier("IpSflowToAiLinkageService")
    private IpSflowToAiLinkageService ipSflowToAiLinkageService;

    @Autowired
    @Qualifier("IpEquipTableDataAiLinkageService")
    private IpEquipTableDataAiLinkageService ipEquipTableDataAiLinkageService;

    @Autowired
    @Qualifier("IpAlarmToAiLinkageService")
    private IpAlarmToAiLinkageService ipAlarmToAiLinkageService;

    @Autowired
    @Qualifier("IpSdnResourceLinkageService")
    private IpSdnResourceLinkageService ipSdnResourceLinkageService;

    @Autowired
    @Qualifier("IpSdnFactorLinkageService")
    private IpSdnFactorLinkageService ipSdnFactorLinkageService;

    @Autowired
    @Qualifier("IpSdnTrafficLinkageService")
    private IpSdnTrafficLinkageService ipSdnTrafficLinkageService;


    @Autowired
    @Qualifier("EntityService")


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {

//        ipSdnResourceLinkageService.sendNodeData();
//        ipSdnResourceLinkageService.sendLinkData();
//        ipSdnResourceLinkageService.sendInterfaceData();

//        ipSdnFactorLinkageService.sendFactorData();
//        ipSdnTrafficLinkageService.sendTrafficData();



//        ipPerfToAiLinkageService.sendPerfLogData();
//        ipAlarmToAiLinkageService.sendAlarmData();
//        transEquipTableDataAiLinkageService.sendEquipMstData();
//        transEquipTableDataAiLinkageService.sendRoadmTrunkData();
//        transEquipTableDataAiLinkageService.sendEquipSlotData();
//        transEquipTableDataAiLinkageService.sendNniTopologyData();
//        transEquipTableDataAiLinkageService.sendEquipPortData();
//        roadmPmDataAiLinkageService.sendRoadmPmData();
//        ipEquipTableDataAiLinkageService.sendNodeData();
//        ipEquipTableDataAiLinkageService.sendCvnmsResourceIfData();
    }
}