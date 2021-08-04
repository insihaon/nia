package com.nia.ai.performance.result.amqp;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class PerformanceSendPrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceSendPrdAmqp.class);

	@Autowired
	@Qualifier("NiaPerformance_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(String ocrTime) {
		try {
			rabbitTemplate.convertAndSend(ocrTime);
		} catch (Exception e) {
			LOGGER.error("=====> [GpuPredictSendPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
			e.printStackTrace();
		}
	}
}

