package com.nia.rca.test.simulator.amqp;

import com.nia.rca.test.simulator.vo.RCATicketHandlingStatus;
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
public class UiToEnginePrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(UiToEnginePrdAmqp.class);

	@Autowired
	@Qualifier("UiToEngine_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<RCATicketHandlingStatus> rcaTicketHandlingStatusFactory;

	public void sendMessageCmd() {
		try {
            LOGGER.info("==========>[UiToEnginePrdAmqp] sendMessageCmd <==============");
            RCATicketHandlingStatus rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
            rcaTicketHandlingStatus.setStatus("reset");
			rabbitTemplate.convertAndSend(rcaTicketHandlingStatus);
		} catch (Exception e) {
			LOGGER.error("=====> [UiToEnginePrdAmqp] sendMessageCmd error : " +ExceptionUtils.getStackTrace(e)+ " <=====");
		}
	}
}

