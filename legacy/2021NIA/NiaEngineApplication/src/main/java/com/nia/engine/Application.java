package com.nia.engine;

import com.nia.engine.service.*;
import com.nia.engine.vo.aiToEngine.AiToEngineNoxVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class Application implements CommandLineRunner{	
	
	private final Logger LOGGER = Logger.getLogger(Application.class);

	@Autowired
    @Qualifier("EngineBootSettingService")
    private EngineBootSettingService engineBootSettingService;

	@Autowired
	@Qualifier("FaultEventService")
	private FaultEventService faultEventService;

	public static void main(String[] args) { SpringApplication.run(Application.class, args); }

	@Override
	public void run(String... arg0) throws Exception {
//		faultEventService.jsonObjToFile("147");
		engineBootSettingService.engineInit();
	}

}
