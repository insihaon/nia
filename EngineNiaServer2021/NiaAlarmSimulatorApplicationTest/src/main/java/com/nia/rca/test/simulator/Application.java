package com.nia.rca.test.simulator;

import com.nia.rca.test.simulator.mapper.AlarmMapper;
import com.nia.rca.test.simulator.service.RcaResetService;
import com.nia.rca.test.simulator.singleton.SingletoneAlarmData;
import com.nia.rca.test.simulator.thread.AlarmThread;
import com.nia.rca.test.simulator.vo.AlarmVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.nia.rca.test.simulator.service.AlarmSimHdlService;

import java.util.ArrayList;

/**
 * 
 * @author 
 *
 */
@SpringBootApplication
public class Application implements CommandLineRunner{
	private final Logger LOGGER = Logger.getLogger(Application.class);

	@Autowired
	@Qualifier("AlarmThread")
	private AlarmThread alarmThread;

	@Autowired
	@Qualifier("AlarmSimHdlService")
	private AlarmSimHdlService alarmSimHdlService;

	@Autowired
	@Qualifier("RcaResetService")
	private RcaResetService rcaResetService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
	//	rcaResetService.rcaTicketReStart();
	//	alarmThread.run();
	//	alarmSimHdlService.alHdlProcessor();
	}
}
