package com.nia.ems.linkage.amqp;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ Producer - MbaAiModel
 * @author
 *
 */
@Configuration()
public class MbaAiModelAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(MbaAiModelAmqp.class);

	@Autowired
	@Qualifier("MbaAiModel_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(String ocrTime) {
		try {
            LOGGER.info("==========>[MbaAiModelAmqp] sendMessageCmd <==============");
			rabbitTemplate.convertAndSend(ocrTime);
		} catch (Exception e) {
			LOGGER.error("=====> [MbaAiModelAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
		}
	}
}
