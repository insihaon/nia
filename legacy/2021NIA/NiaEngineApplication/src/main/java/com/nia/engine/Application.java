package com.nia.engine;

import com.nia.engine.amqp.UiToEngineTicketPrdAmqp;
import com.nia.engine.listener.UITicketMsgListener;
import com.nia.engine.service.*;
import com.nia.engine.service.impl.EngineClearSchedulerJobServiceImpl;
import com.nia.engine.service.impl.SingleDomainRcaServiceImpl;
import com.nia.engine.vo.BasicAlarmVo;
import com.nia.engine.vo.ClusterObject;
import com.nia.engine.vo.RCATicketHandlingStatus;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.json.simple.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author 
 *
 */

@SpringBootApplication()
@EnableScheduling
public class Application implements CommandLineRunner{	
	
	private final Logger LOGGER = Logger.getLogger(Application.class);

	@Autowired
    @Qualifier("EngineBootSettingService")
    private EngineBootSettingService engineBootSettingService;

	public static void main(String[] args) { SpringApplication.run(Application.class, args); }

	@Override
	public void run(String... arg0) throws Exception {
		engineBootSettingService.engineInit();
	}
}
