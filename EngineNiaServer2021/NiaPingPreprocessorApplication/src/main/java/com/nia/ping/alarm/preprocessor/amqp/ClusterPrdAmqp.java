package com.nia.ping.alarm.preprocessor.amqp;

import com.nia.ping.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class ClusterPrdAmqp {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterPrdAmqp.class);

	@Autowired
	@Qualifier("Cluster_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(BasicAlarmVo basicAlarmVo) {
		try {
            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [ClusterPrdAmqp] sendMessageCmd <=====\n");
            strLog.append("alarmtime : " + basicAlarmVo.getAlarmtime()+"\n");
            strLog.append("alarmid : " + basicAlarmVo.getAlarmno()+"\n");
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog.toString());

			rabbitTemplate.convertAndSend(basicAlarmVo);
		} catch (Exception e) {
			LOGGER.error("=====> [ClusterPrdAmqp] sendMessageCmd error : " + ExceptionUtils.getStackTrace(e)+ " <=====");
			e.printStackTrace();
		}
	}
}

