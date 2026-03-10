package com.nia.engine.amqp;

import com.nia.engine.vo.aiTraffic.EngineTrafficeResultVo;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.simple.JSONObject;
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
@Configuration
public class AiTicketAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(AiTicketAmqp.class);

	@Autowired
	@Qualifier("NiaEngineTraffic_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(EngineTrafficeResultVo engineTrafficeResultVo) {
		try{
			LOGGER.info("=====> [sendMessageCmd] : engineTrafficeResultVo " +engineTrafficeResultVo.toString() + " <=====");
			rabbitTemplate.convertAndSend(engineTrafficeResultVo);
			//Thread.sleep(1000);
		} catch (Exception e) {
			LOGGER.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
			try {
				Thread.sleep(1000);
				rabbitTemplate.convertAndSend(engineTrafficeResultVo);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
