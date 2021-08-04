package com.nia.ai.per.ap.amqp;

import com.nia.ai.per.ap.vo.PerformanceClusterVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class PerformanceToEnginePrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceToEnginePrdAmqp.class);

	@Autowired
	@Qualifier("NiaPerformanceEngine_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(PerformanceClusterVo performanceClusterVo) {
		try {
			LOGGER.info("sendMessageCmd : " + performanceClusterVo.toString());
			rabbitTemplate.convertAndSend(performanceClusterVo);
		} catch (Exception e) {
			LOGGER.error("=====> [MbaToUiPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
			e.printStackTrace();
		}
	}
}

