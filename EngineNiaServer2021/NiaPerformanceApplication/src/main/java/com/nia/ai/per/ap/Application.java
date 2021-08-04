package com.nia.ai.per.ap;

import com.nia.ai.per.ap.mapper.PerformanceMapper;
import com.nia.ai.per.ap.service.BootSettingService;
import com.nia.ai.per.ap.service.LineMonitoringHdlService;
import com.nia.ai.per.ap.vo.RoadmPerformanceOrgListVo;
import com.nia.ai.per.ap.vo.RoadmPerformanceVo;
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
    private PerformanceMapper performanceMapper;
    @Autowired
    @Qualifier("BootSettingService")
    private BootSettingService bootSettingService;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<RoadmPerformanceOrgListVo> roadmPerformanceOrgListVoObjectFactory;

    @Autowired
    @Qualifier("LineMonitoringHdlService")
	private LineMonitoringHdlService lineMonitoringHdlService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {
        bootSettingService.init();
        String ocrTime = "2020-11-18 09:00:00";
//        String ocrTime = "2020-11-06 14:00:00";

      //  lineMonitoringHdlService.lineMonitoringHdlProcessor(ocrTime);
    }
}