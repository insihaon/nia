package com.nia.ip.sdn.syslog.linkage.amqp;

import com.nia.ip.sdn.syslog.linkage.common.LoggerPrint;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogDataVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration()
public class SyslogDataPrdAmqp {

	@Autowired
	@Qualifier("SyslogAlarm_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(SyslogDataVo syslogDataVo) {

		try {
            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [ClusterPrdAmqp] sendMessageCmd <=====\n");
            strLog.append("collectSeq : " + syslogDataVo.getCollectSeq()+"\n");
            strLog.append("timestamp : " + syslogDataVo.getTimestamp()+"\n");
            strLog.append("---------------------------------------------------------------");
            LoggerPrint.infoLog(strLog.toString());

			rabbitTemplate.convertAndSend(syslogDataVo);
		} catch (Exception e) {
			LoggerPrint.errorLog(e);
		}
	}
}

