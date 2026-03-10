package com.nia.data.linkage.amqp;

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
public class AlarmLinkageResultPrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmLinkageResultPrdAmqp.class);

	@Autowired
	@Qualifier("AlarmLinkageResult_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd() {
		try {
            LOGGER.info("==========>[TopasLinkageResultPrdAmqp] sendMessageCmd url: <==============");
		//	rabbitTemplate.convertAndSend(gpuResultVo);
		} catch (Exception e) {
			LOGGER.error("=====> [TopasLinkageResultPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
		}
	}
}

