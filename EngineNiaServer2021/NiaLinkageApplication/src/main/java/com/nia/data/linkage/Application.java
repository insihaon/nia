package com.nia.data.linkage;

import com.nia.data.linkage.config.Telnet;
import com.nia.data.linkage.config.TelnetSample;
import com.nia.data.linkage.config.TelnetTest;
import com.nia.data.linkage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author 
 *
 */
@EnableScheduling
@SpringBootApplication
public class Application implements CommandLineRunner{

	@Autowired
    @Qualifier("BootSettingService")
    private BootSettingService bootSettingService;

	@Autowired
    @Qualifier("TopologyService")
    private TopologyService topologyService;

	@Autowired
    @Qualifier("PerformanceService")
    private PerformanceService performanceService;

	@Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
//		bootSettingService.init();
//		topologyService.getTopologyData();
	//	alarmService.getAlarmData();
	}
}
