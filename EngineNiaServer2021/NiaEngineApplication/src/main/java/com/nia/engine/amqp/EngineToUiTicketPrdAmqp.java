package com.nia.engine.amqp;

import com.nia.engine.vo.RcaEngineResult;
import org.apache.commons.lang.exception.ExceptionUtils;
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
public class EngineToUiTicketPrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineToUiTicketPrdAmqp.class);

	@Autowired
	@Qualifier("EngineToUiTicket_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(RcaEngineResult rcaEngineResult) {
		try{
			LOGGER.info("=====> [sendMessageCmd] : rcaEngineResult " +rcaEngineResult.toString() + " <=====");
			rabbitTemplate.convertAndSend(rcaEngineResult);
			//Thread.sleep(1000);
		} catch (Exception e) {
			LOGGER.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
			try {
				Thread.sleep(1000);
				rabbitTemplate.convertAndSend(rcaEngineResult);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
