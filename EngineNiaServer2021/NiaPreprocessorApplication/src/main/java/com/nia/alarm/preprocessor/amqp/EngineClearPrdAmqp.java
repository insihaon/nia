package com.nia.alarm.preprocessor.amqp;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Producer
 * @author
 *
 */
@Configuration()
public class EngineClearPrdAmqp {	
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineClearPrdAmqp.class);

	@Autowired
	@Qualifier("EngineClear_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(String alarmNo) {
		try {
            LOGGER.info("=====> EngineClearPrdAmqp sendMessageCmd : "+ alarmNo);
			rabbitTemplate.convertAndSend(alarmNo);
		}catch( Exception e ) {
			LOGGER.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
    	}
	}
}
