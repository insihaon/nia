package com.nia.rca.cluster.preprocessor.amqp;

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
public class EnginePrdAmqp {	
	private static final Logger LOGGER = LoggerFactory.getLogger(EnginePrdAmqp.class);

	@Autowired
	@Qualifier("Engine_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(String tmpClusterKey) {
		try{
			rabbitTemplate.convertAndSend(tmpClusterKey);
		}catch( Exception e ) {
			LOGGER.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
			e.printStackTrace();
		}
	}
}
