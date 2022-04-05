package com.nia.alarm.ip.simulator;

import com.nia.alarm.ip.simulator.service.AlarmSimHdlService;
import com.nia.alarm.ip.simulator.thread.AlarmThread;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;

/**
 * 
 * @author 
 *
 */
@EnableScheduling
@SpringBootApplication
public class Application implements CommandLineRunner{
	private final Logger LOGGER = Logger.getLogger(Application.class);

	@Autowired
	@Qualifier("AlarmThread")
	private AlarmThread alarmThread;

	@Autowired
	@Qualifier("AlarmSimHdlService")
	private AlarmSimHdlService alarmSimHdlService;

	@Value("${spring.profiles}")
	private String profiles;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		if("dev".equals(profiles)){
			alarmSimHdlService.alHdlProcessor();
		}else if("real".equals(profiles)){
			alarmThread.run();
		}else if("test".equals(profiles) || "codej".equals(profiles) || "etri".equals(profiles)){
			alarmSimHdlService.alTestHdlProcessor();
		}
	}
}
