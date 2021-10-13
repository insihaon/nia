package com.nia.data.linkage.ai;

import com.nia.data.linkage.ai.service.ip.equip.IpEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.service.ip.perf.IpPerfToAiLinkageService;
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
    @Qualifier("IpPerfToAiLinkageService")
    private IpPerfToAiLinkageService ipPerfToAiLinkageService;

    @Autowired
    @Qualifier("RoadmPmDataAiLinkageService")
    private RoadmPmDataAiLinkageService roadmPmDataAiLinkageService;

    @Autowired
    @Qualifier("IpEquipTableDataAiLinkageService")
    private IpEquipTableDataAiLinkageService ipEquipTableDataAiLinkageService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
//        roadmPmDataAiLinkageService.sendRoadmPmData();
//        ipEquipTableDataAiLinkageService.sendBackBoneLinkData();
    }
}