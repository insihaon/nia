package com.nia.ems.linkage;

import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application implements CommandLineRunner{

	@Autowired
    @Qualifier("BootSettingService")
    private BootSettingService bootSettingService;

	@Autowired
    @Qualifier("RoadmEmsMmcService")
    private RoadmEmsMmcService roadmEmsMmcService;

	@Autowired
    @Qualifier("RoadmEmsEventMsgService")
    private RoadmEmsEventMsgService roadmEmsEventMsgService;

	@Autowired
    @Qualifier("RoadmEmsPmMmcCheckService")
    private RoadmEmsPmMmcCheckService roadmEmsPmMmcCheckService;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		bootSettingService.init();
		roadmEmsEventMsgService.eventMsgListener();
		roadmEmsPmMmcCheckService.pmMmcLinkageCheck();

//		roadmEmsMmcService.roadmSipcMMC();
//		roadmEmsMmcService.roadmPmMMC();

//		roadmEmsMmcService.roadmPmMMC();

//		roadmEmsMmcService.roadmNetWorkMmc();

//		roadmPmDataAiLinkageService.sendRoadmPmData();

//		roadmEmsMmcService.roadmAlarmMMC();

	//	roadmEmsPerMmcService.roadmSipcMMC();
	//	roadmEmsPerMmcService.roadmPmMMC();
	//	roadmEmsPerMmcService.roadmAlarmMMC();
	//	roadmEmsPerMmcService.roadmSlotMMC();
	//	roadmEmsPerMmcService.createRoadmUniTopology();
//		roadmEmsMmcService.roadmNetWorkMmc();
	//	topologyService.getTopologyData();
	//	alarmService.getAlarmData();
	//	performanceService.getPerformanceData();

	}
}
