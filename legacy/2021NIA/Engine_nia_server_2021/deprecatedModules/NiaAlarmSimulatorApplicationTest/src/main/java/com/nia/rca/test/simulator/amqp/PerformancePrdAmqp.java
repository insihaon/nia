package com.nia.rca.test.simulator.amqp;

import com.nia.rca.test.simulator.vo.AlarmVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PerformancePrdAmqp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformancePrdAmqp.class);
	
	@Autowired
	@Qualifier("NiaPerformance_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(String ocrTime) {
		try {
			rabbitTemplate.convertAndSend(ocrTime);
		} catch (Exception e) {
			LOGGER.error("=====> [sendMessageCmd] : error " +e.getMessage()+ " <=====");
			e.printStackTrace();
		}
	}
}

