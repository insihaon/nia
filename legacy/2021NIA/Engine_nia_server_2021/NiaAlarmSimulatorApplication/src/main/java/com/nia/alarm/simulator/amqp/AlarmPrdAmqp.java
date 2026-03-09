package com.nia.alarm.simulator.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import com.nia.alarm.simulator.vo.AlarmVo;

@Configuration
public class AlarmPrdAmqp {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AlarmPrdAmqp.class);
	
	@Autowired
	@Qualifier("NiaAlarm_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(AlarmVo alarmVo) {
		try {
           // LOGGER.info("=====>alarmVo("+alarmVo.getAlarmno()+") receiveTime("+alarmVo.getReceivetime()+") : currentTime("+ UtlDateHelper.getCurrentTime()+") <=====");
			rabbitTemplate.convertAndSend(alarmVo);
		} catch (Exception e) {
			LOGGER.error("=====> [sendMessageCmd] : error " +e.getMessage()+ " <=====");
			e.printStackTrace();
		}
	}
}

