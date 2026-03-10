package com.nia.ems.linkage.amqp;

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
public class PerformanceToAiPrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceToAiPrdAmqp.class);

	@Autowired
	@Qualifier("PerformanceToAiSend_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(String ocrTime) {
		try {
            LOGGER.info("==========>[PerformanceToAiPrdAmqp] sendMessageCmd <==============");
			rabbitTemplate.convertAndSend(ocrTime);
		} catch (Exception e) {
			LOGGER.error("=====> [PerformanceToAiPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
		}
	}
}

