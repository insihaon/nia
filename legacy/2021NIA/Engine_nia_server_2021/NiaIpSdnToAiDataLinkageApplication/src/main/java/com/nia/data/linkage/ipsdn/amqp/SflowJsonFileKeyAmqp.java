package com.nia.data.linkage.ipsdn.amqp;

import com.nia.data.linkage.ipsdn.common.LoggerPrint;
import com.nia.data.linkage.ipsdn.vo.ipsdn.sflow.SflowDataVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class SflowJsonFileKeyAmqp {

	@Autowired
	@Qualifier("NiaSflowJsonFileKey_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(String key) {

		try {
            StringBuffer strLog = new StringBuffer();
            strLog.append("---------------------------------------------------------------");
            LoggerPrint.infoLog("[SflowJsonFileKeyAmqp] json key : " + key);

			rabbitTemplate.convertAndSend(key);
		} catch (Exception e) {
			LoggerPrint.errorLog(e);
		}
	}
}

