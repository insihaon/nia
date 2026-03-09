package com.nia.ai.performance.send;

import com.nia.ai.performance.send.service.BootSettingService;
import com.nia.ai.performance.send.service.PerformanceSendService;
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
    @Qualifier("PerformanceSendService")
    private PerformanceSendService PerformanceSendService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		//bootSettingService.init();
		//PerformanceSendService.sendPerformanceData();
	}
}
