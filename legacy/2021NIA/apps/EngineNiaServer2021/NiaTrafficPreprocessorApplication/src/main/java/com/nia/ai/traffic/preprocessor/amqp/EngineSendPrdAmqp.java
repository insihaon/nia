package com.nia.ai.traffic.preprocessor.amqp;

import com.nia.ai.traffic.preprocessor.vo.EngineTrafficeResultVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration()
public class EngineSendPrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineSendPrdAmqp.class);

	@Autowired
	@Qualifier("NiaEngineTraffic_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(EngineTrafficeResultVo engineTrafficeResultVo) {
		LOGGER.info("==========>[EngineSendPrdAmqp] sendMessageCmd gb : "+engineTrafficeResultVo.getGb()+"<==============");

		try {
			rabbitTemplate.convertAndSend(engineTrafficeResultVo);
		} catch (Exception e) {
			LOGGER.error("=====> [EngineSendPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
			e.printStackTrace();
		}
	}


	public void sendMessageCmdAttAiTca(HashMap hashMap) {
		LOGGER.info("==========>[EngineSendPrdAmqp] sendMessageCmd gb : "+hashMap.get("gb")+"<==============");

		try {
			rabbitTemplate.convertAndSend(hashMap);
		} catch (Exception e) {
			LOGGER.error("=====> [EngineSendPrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
			e.printStackTrace();
		}
	}
}

